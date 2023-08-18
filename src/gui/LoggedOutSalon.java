package gui;

import entiteti.Klijent;
import entiteti.Korisnik;
import entiteti.Pol;
import exceptions.PasswordMissmatchException;
import exceptions.UsernameNotFoundException;

public interface LoggedOutSalon extends KozmetickiSalon{
	
	public void logIn(Korisnik korisnik);
	
	public Korisnik authenticateKorisnik(String username, String password) throws UsernameNotFoundException, PasswordMissmatchException;
	
	
	public Klijent registerKorisnik(
			String name, String surname, Pol sex, String phoneNumber, String adress, String username, String password);
	
	public Klijent registerKorisnik(String username, String password);
	
}
