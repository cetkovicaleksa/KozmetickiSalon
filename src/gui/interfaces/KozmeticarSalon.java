package gui.interfaces;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Map;
import java.util.SortedMap;

import entiteti.Kozmeticar;
import entiteti.StatusTretmana;
import entiteti.ZakazanTretman;

public interface KozmeticarSalon extends LoggedInSalon{
	
	@Override
	public Kozmeticar getLoggedInKorisnik();
	
	
	/**Get all the treatments that the logged in kozmeticar is set to be a kozmeticar grouped by status.
	 * @return Map from StatusTretmana to a list of ZakazanTretman that have that status*/	
	public Map<StatusTretmana, Collection<ZakazanTretman>> zakazaniTretmaniKozmeticara();
	
	
	
	public void izvrsiTretman(ZakazanTretman tretman);
	
	
	/***/
	public SortedMap<LocalDate, SortedMap<LocalTime, ZakazanTretman>> rasporedKozmeticara();
}
