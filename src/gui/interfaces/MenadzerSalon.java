package gui.interfaces;

import entiteti.Menadzer;

public interface MenadzerSalon extends LoggedInSalon{

	@Override	
	public Menadzer getLoggedInKorisnik();
	
	public IzvjestajiSalon getIzvjestajiSalon();
}
