package gui.interfaces;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import entiteti.Klijent;
import entiteti.Kozmeticar;
import entiteti.KozmetickiTretman;
import entiteti.KozmetickiTretman.TipTretmana;
import entiteti.StatusTretmana;
import entiteti.ZakazanTretman;

public interface KlijentSalon extends LoggedInSalon{
	
	@Override
	public Klijent getLoggedInKorisnik();
	
	public Map<StatusTretmana, ZakazanTretman> zakazaniTretmaniKlijenta();
	
	public boolean stanjeNaKarticiLojalnosti();
	
	
	/**@return A collection of collections of TipTretmana. A nested collection is a group of TipTretmana that have the same KozmetickiTretman.*/
	public List<List<KozmetickiTretman.TipTretmana>> getTretmaniSelection();  //TODO: returns kozmetickiTretmani and tipTretmana for displaying
	
	
	public double getPrice(KozmetickiTretman.TipTretmana tipTretmana);
	
	
	public void zakaziTretman(TipTretmana tipTretmana, Kozmeticar kozmeticar, LocalDate datum, LocalTime vrijeme);
	
	
	
	public List<Kozmeticar> getKozmeticariThatCanPreformTreatment(KozmetickiTretman tretman);
	
	
	
	
	
	
	public void otkaziTretman(ZakazanTretman zakazanTretman);
	
	//ZakazanTretman getZakazanTretman();
}
