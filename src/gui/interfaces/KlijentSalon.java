package gui.interfaces;

import java.time.LocalDate;
import java.time.LocalTime;
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
	
	
	/**All zakazani tretmani that logged in klijent has in the saloon by their status.
	 * @return A map from status tretmana to a list of zakazan tretman that have that status
	 * @see StatusTretmana, ZakazanTretman*/
	public Map<StatusTretmana, List<ZakazanTretman>> getZakazaniTretmaniKlijenta();
	
	
	/**List of lists of tip tretmana where an inner list represents a group of tip tretmana that have the same kozmeticki tretman.
	 * @see KozmetickiTretman*/
	public List<List<KozmetickiTretman.TipTretmana>> getTretmaniSelection();
	
	
	/**Get the price for the given tip tretmana taking in mind the loyalty card status of klijent.
	 * @param tipTretmana A tip tretmana that must exist in the saloon
	 * @return The price*/
	public double getPrice(KozmetickiTretman.TipTretmana tipTretmana);
	
	
	/**Get all kozmeticar that can preform the given treatment in the saloon.
	 * @param tretman A KozmetickiTretman that exists in the saloon
	 * @return A list of Kozmeticar*/
	public List<Kozmeticar> getKozmeticariThatCanPreformTreatment(KozmetickiTretman tretman);
	
	
	/**Get a list of integer values that represent the hours of day on a given date that the provided kozmeticar can preform the given
	 * tip tretmana.
	 * @return List of integer values that represent whole hours of day
	 * @param kozmeticar A kozmeticar that must exist in the saloon
	 * @param datum The date of interest
	 * @param tipTretmana The tip tretmana of interest*/
	public List<Integer> getKozmeticarFreeHours(Kozmeticar kozmeticar, LocalDate datum, KozmetickiTretman.TipTretmana tipTretmana);
	
	
	/***/
	public void zakaziTretman(TipTretmana tipTretmana, Kozmeticar kozmeticar, LocalDate datum, LocalTime vrijeme);

	
	/***/
	public void otkaziTretman(ZakazanTretman zakazanTretman);
}
