package main;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.function.Function;
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
import entiteti.Recepcioner;
import entiteti.Salon;
import entiteti.StatusTretmana;
import entiteti.ZakazanTretman;
import gui.KorisnikGUI;
import gui.interfaces.KlijentSalon;
import gui.interfaces.LoggedOutSalon;
import gui.klijent.KlijentGUI;
import gui.kozmeticar.KozmeticarGUI;
import gui.recepcioner.RecepcionerGUI;
import helpers.PasswordMissmatchException;
import helpers.Query;
import helpers.UsernameNotFoundException;

public class Authenticator implements LoggedOutSalon{
	
	private Supplier<RegistarMenadzera> registarSupplier;
	
	private final Map<Class<?>, Function<Korisnik, KorisnikGUI>> guiMap;
	
	{
		guiMap = new HashMap<>();
		
		guiMap.put(Klijent.class, klijent -> getGUI((Klijent) klijent));
		guiMap.put(Kozmeticar.class, kozmeticar -> getGUI((Kozmeticar) kozmeticar));
		guiMap.put(Recepcioner.class, recepcioner -> getGUI((Recepcioner) recepcioner));
		guiMap.put(Menadzer.class, menadzer -> getGUI((Menadzer) menadzer));
	}
	
	
	
	public Authenticator(Supplier<RegistarMenadzera> registarSupplier) {
		this.registarSupplier = registarSupplier;
	}
	
	

	@Override
	public void exit() {
		try {
			registarSupplier.get().save();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	@Override
	public void logIn(Korisnik korisnik) {
		
		Function<Korisnik, KorisnikGUI> getGUI = guiMap.get(korisnik.getClass());
		
		if(getGUI == null) {
			throw new IllegalArgumentException("Unsupported korisnik type!");
		}
		
		getGUI.apply(korisnik).setVisible(true);						
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
			registarSupplier.get().getKlijentMenadzer().create(klijent);
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
			public Map<StatusTretmana, List<ZakazanTretman>> getZakazaniTretmaniKlijenta() {	
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
	
	private KozmeticarGUI getGUI(Kozmeticar kozmeticar) {
		return null;
	}
	
	private RecepcionerGUI getGUI(Recepcioner recepcioner) {
		return null;
	}
	
	private KorisnikGUI getGUI(Menadzer menadzer) {
		return null;		
	}
	
	
	
	
	private Iterator<KorisnikMenadzer<? extends Korisnik>> getKorisnikMenadzeriIterator(){
		RegistarMenadzera registar = registarSupplier.get();
	    
	    List<KorisnikMenadzer<? extends Korisnik>> korisnikMenadzerList = new ArrayList<>();
	    
	    korisnikMenadzerList.add(registar.getKlijentMenadzer());
	    korisnikMenadzerList.add(registar.getKozmeticarMenadzer());
	    korisnikMenadzerList.add(registar.getRecepcionerMenadzer());
	    korisnikMenadzerList.add(registar.getMenadzerMenadzer());
	    
	    return korisnikMenadzerList.iterator();
	}

}
