package gui.interfaces;

import entiteti.Korisnik;

public interface LoggedInSalon extends KozmetickiSalon{
	
	public void logOut();
	
	/**Returns the currently logged in user.
	 * @return The logged in user*/
	public Korisnik getLoggedInKorisnik();
}
