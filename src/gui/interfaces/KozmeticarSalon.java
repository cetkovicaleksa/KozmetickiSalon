package gui.interfaces;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import entiteti.Kozmeticar;
import entiteti.StatusTretmana;
import entiteti.ZakazanTretman;

public interface KozmeticarSalon extends LoggedInSalon{
	
	@Override
	public Kozmeticar getLoggedInKorisnik();
	
	public Map<StatusTretmana, List<ZakazanTretman>> zakazaniTretmaniKozmeticara();
	
	public void izvrsiTretman(ZakazanTretman tretman);
	
	public SortedMap<LocalDate, SortedMap<LocalTime, ZakazanTretman>> rasporedKozmeticara();
}
