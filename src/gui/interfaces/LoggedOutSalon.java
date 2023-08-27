package gui.interfaces;

import entiteti.Klijent;
import entiteti.Korisnik;
import entiteti.Pol;
import helpers.PasswordMissmatchException;
import helpers.UsernameNotFoundException;

public interface LoggedOutSalon extends KozmetickiSalon{
	
	public void logIn(Korisnik korisnik);
	
	public Korisnik authenticateKorisnik(String username, String password) throws UsernameNotFoundException, PasswordMissmatchException;
	
	public boolean usernameTaken(String username);
	
	public Klijent registerKorisnik(
			String name, String surname, Pol gender, String phoneNumber, String address, String username, String password
			);
	
}
