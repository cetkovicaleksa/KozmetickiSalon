package main;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.function.Predicate;
import java.util.function.Supplier;

import crudMenadzeri.KorisnikMenadzer;
import crudMenadzeri.RegistarMenadzera;
import dataProvajderi.IdNotUniqueException;
import entiteti.Klijent;
import entiteti.Korisnik;
import entiteti.Kozmeticar;
import entiteti.KozmetickiTretman;
import entiteti.KozmetickiTretman.TipTretmana;
import entiteti.Menadzer;
import entiteti.Pol;
import entiteti.StatusTretmana;
import entiteti.ZakazanTretman;
import gui.KorisnikGUI;
import gui.interfaces.KlijentSalon;
import gui.interfaces.LoggedOutSalon;
import gui.klijent.KlijentGUI;
import gui.kozmeticar.KozmeticarGUI;
import gui.login.LoginGUI;
import gui.recepcioner.RecepcionerGUI;
import helpers.PasswordMissmatchException;
import helpers.Query;
import helpers.UsernameNotFoundException;

public class Authenticator implements LoggedOutSalon{
	
	private Supplier<RegistarMenadzera> registarSupplier;
	private Runnable save;
	private GUIFactory guiFactory;
	
	
	
	public Authenticator(Supplier<RegistarMenadzera> registarSupplier, Runnable save) {
		this.registarSupplier = registarSupplier;
		this.save = save;
		this.guiFactory = GUIFactory.getInstance();
	}
	
	
	private RegistarMenadzera getRegistar() {
		return registarSupplier.get();
	}
	
	public void login() {
		new LoginGUI(this).setVisible(true);
	}

	@Override
	public void exit() {
		save.run();
	}

	@Override
	public void logIn(Korisnik korisnik) {
		try {
			guiFactory.getKorisnikGUI(korisnik, this, getRegistar()).setVisible(true);
		}catch(IllegalArgumentException e) {
			throw e;
		}
	}

	
	@Override
	public Korisnik authenticateKorisnik(String username, String password)
			throws UsernameNotFoundException, PasswordMissmatchException {
		
		if(username == null || password == null) {
			throw new IllegalArgumentException("Please provide a username and a password.");
		}
		
		Predicate<Korisnik> usernameTester = korisnik -> username.equals(korisnik.getKorisnickoIme());
		Iterator<KorisnikMenadzer<? extends Korisnik>> iter = getKorisnikMenadzeriIterator();
		Korisnik korisnik = null;
		
		while(iter.hasNext()) {
			List<? extends Korisnik> korisnici = iter.next().read( new Query<>(user -> usernameTester.test(user)) );

			if(korisnici.size() > 1) {
				//really bad
			}
			
			if(korisnici.size() == 0) {
				continue;
			}
			
			korisnik = korisnici.get(0);
			break;
		}
		
		if(korisnik == null) {
			throw new UsernameNotFoundException();
		}
		
		if(password.equals(korisnik.getLozinka()))
				return korisnik;
		
		throw new PasswordMissmatchException();
	}

	
	@Override
	public boolean usernameTaken(String username) {
		Predicate<Korisnik> usernameTester = korisnik -> username.equals(korisnik.getKorisnickoIme());
		Iterator<KorisnikMenadzer<? extends Korisnik>> korisnikMenadzeriIterator = getKorisnikMenadzeriIterator();
		
		while(korisnikMenadzeriIterator.hasNext()) {
			if(
					!korisnikMenadzeriIterator.next().read( new Query<>(korisnik -> usernameTester.test(korisnik)) ).isEmpty()
	          ) {
				return true;
			}
		}
		
		return false;
	}

	
	@Override
	public Klijent registerKorisnik(String name, String surname, Pol gender, String phoneNumber, String address,
			String username, String password) {

		Klijent klijent = new Klijent(
				name, surname, phoneNumber, address, username, password,
				(gender == null ? Pol.NIJE_NAVEDEN : gender), false
		);
		
		try {
			getRegistar().getKlijentMenadzer().create(klijent);
		}catch (IdNotUniqueException e) {
			//shouldn't happen TODO: see what to do!
			throw e;
		}
		
		return klijent;
	}
	
	
	
	private KlijentGUI getGUI(Klijent klijent) {
		
		return new KlijentGUI(new KlijentSalon() {

			@Override
			public void logOut() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void exit() {
				System.out.println("EXIT KLIJENT");
				//Authenticator.this.exit();				
			}

			@Override
			public Klijent getLoggedInKorisnik() {
				return klijent;
			}

			@Override
			public Map<StatusTretmana, Collection<ZakazanTretman>> getZakazaniTretmaniKlijenta() {	
				return registarSupplier.get().getZakazaniTretmaniKorisnika(klijent, true, false);
			}

			@Override
			public Collection<Collection<TipTretmana>> getTretmaniSelection() {
				return registarSupplier.get().getTretmaniSelection();
			}

			@Override
			public double getPrice(TipTretmana tipTretmana) {
				double price = tipTretmana.getCijena();
				
				if(klijent.getHasLoyaltyCard()) {
					price *= registarSupplier.get().getSalonMenadzer().read().getLoyaltyCardDiscount();
				}
				
				return price;
			}

			@Override
			public Collection<Kozmeticar> getKozmeticariThatCanPreformTreatment(KozmetickiTretman tretman) {
				return registarSupplier.get().getKozmeticariThatCanPreformTreatment(tretman);
			}

			@Override
			public SortedSet<Integer> getKozmeticarFreeHours(Kozmeticar kozmeticar, LocalDate datum,
					TipTretmana tipTretmana) {
				return registarSupplier.get().getKozmeticarFreeHours(kozmeticar, datum);
			}

			@Override
			public void zakaziTretman(TipTretmana tipTretmana, Kozmeticar kozmeticar, LocalDate datum,
					LocalTime vrijeme) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void otkaziTretman(ZakazanTretman zakazanTretman) {
				zakazanTretman.setStatus(StatusTretmana.OTKAZAO_KLIJENT);
			}
			
		});
	}
	
	
	
	
	private Iterator<KorisnikMenadzer<? extends Korisnik>> getKorisnikMenadzeriIterator(){
		RegistarMenadzera registar = getRegistar();
	    
	    List<KorisnikMenadzer<? extends Korisnik>> korisnikMenadzerList = new ArrayList<>();
	    
	    korisnikMenadzerList.add(registar.getKlijentMenadzer());
	    korisnikMenadzerList.add(registar.getKozmeticarMenadzer());
	    korisnikMenadzerList.add(registar.getRecepcionerMenadzer());
	    korisnikMenadzerList.add(registar.getMenadzerMenadzer());
	    
	    return korisnikMenadzerList.iterator();
	}

}
