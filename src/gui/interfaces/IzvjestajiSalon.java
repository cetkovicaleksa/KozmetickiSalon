package gui.interfaces;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;

import entiteti.Klijent;
import entiteti.StatusTretmana;

public interface IzvjestajiSalon {
	
	/**Broj tretmana po statusu za izabrani opseg datuma.*/
	public Map<StatusTretmana, Integer> izvjestajZakazanihTretmanaPoRazlozima(LocalDate beginingDate, LocalDate endDate);
	
	/**Svi klijenti koji imaju karticu lojalnosti.*/
	public Collection<Klijent> klijentiKojiImajuKarticuLojalnosti();
}
