package gui.interfaces;

import java.util.Map;

import entiteti.StatusTretmana;
import entiteti.ZakazanTretman;

public interface KlijentSalon extends LoggedInSalon{
	
	public Map<StatusTretmana, ZakazanTretman> tretmaniKlijenta();
	
	public boolean stanjeNaKarticiLojalnosti();
	
	public void zakaziTretman();
	
	public void otkaziTretman();
	
	//ZakazanTretman getZakazanTretman();
}
