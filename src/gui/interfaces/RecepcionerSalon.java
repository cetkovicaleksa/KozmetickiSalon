package gui.interfaces;

import entiteti.Korisnik;
import entiteti.StatusTretmana;

public interface RecepcionerSalon extends LoggedInSalon{
	
	public void zakaziTretman(Korisnik korisnik);
	
	public void otkaziTretman();
	
	public void zakazaniTretmani();

	public void izmjeniStatusTretmana(StatusTretmana status);
}
