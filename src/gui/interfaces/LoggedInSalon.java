package gui.interfaces;

import entiteti.Korisnik;

public interface LoggedInSalon extends KozmetickiSalon{
	
	public void logOut();
	
	public Korisnik getLoggedInKorisnik();
}
