package gui;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.SortedMap;

import entiteti.KozmetickiTretman;
import entiteti.ZakazanTretman;

public interface KozmeticarSalon extends LoggedInSalon{
	
	public Collection<KozmetickiTretman> tretmaniKozmeticara();
	
	public void izvrsiTretman(ZakazanTretman tretman);
	
	public SortedMap<LocalDate, SortedMap<LocalTime, ZakazanTretman>> rasporedKozmeticara();
}
