package gui.interfaces;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;

import entiteti.Korisnik;
import entiteti.Kozmeticar;
import entiteti.KozmetickiTretman;
import entiteti.Recepcioner;
import entiteti.StatusTretmana;
import entiteti.ZakazanTretman;
import entiteti.KozmetickiTretman.TipTretmana;

public interface RecepcionerSalon extends LoggedInSalon{
	
	@Override
	public Recepcioner getLoggedInKorisnik();
	
	public void zakaziTretman(TipTretmana tipTretmana, Kozmeticar kozmeticar, Korisnik klijent, LocalDate datum, LocalTime vrijeme);
	
	public void otkaziTretman(ZakazanTretman zakazanTretman);
	
	public Collection<ZakazanTretman> getZakazaniTretmani();

	/**Update the treatment with the passed parameters. The fields that are null will be unchanged.*/
	public void updateZakazanTretman(
			KozmetickiTretman.TipTretmana tipTretmana, Kozmeticar kozmeticar, Korisnik klijent,
			LocalDate datum, LocalTime vrijeme, StatusTretmana status
			);
	
	/**List of lists of tip tretmana where an inner list represents a group of tip tretmana that have the same kozmeticki tretman.
	 * @see KozmetickiTretman*/
	public Collection<Collection<KozmetickiTretman.TipTretmana>> getTretmaniSelection();
	
	
}
