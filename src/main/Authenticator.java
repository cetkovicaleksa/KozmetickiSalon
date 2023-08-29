package main;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

import javax.swing.JOptionPane;

import crudMenadzeri.KorisnikMenadzer;
import crudMenadzeri.RegistarMenadzera;
import dataProvajderi.IdNotUniqueException;
import entiteti.Klijent;
import entiteti.Korisnik;
import entiteti.Pol;
import gui.interfaces.LoggedOutSalon;
import gui.login.LoginGUI;
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
	
	
	

	@Override
	public void exit() {
		save.run();
	}
	
	
	@Override
	public void login() {
		new LoginGUI(this).setVisible(true);
	}
	
	@Override
	public void logIn(Korisnik korisnik) {
		try {
			guiFactory.getKorisnikGUI(korisnik, this, getRegistar()).setVisible(true);
		}catch(IllegalArgumentException e) {
			JOptionPane.showMessageDialog(
	                null,
	                "Graficki interfejs za ovaj tip korisnika ne postoji. [ " + korisnik.getClass() + " ]",
	                "Greska",
	                JOptionPane.ERROR_MESSAGE
	        );
			login();
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
