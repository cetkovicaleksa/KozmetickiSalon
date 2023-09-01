package gui.interfaces;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import entiteti.BonusCriteria;
import entiteti.Klijent;
import entiteti.KozmetickiTretman;
import entiteti.StatusTretmana;

public interface IzvjestajiSalon {
	
	/**Broj zakazanih tretmana i zarada po kozmeticaru po statusu tretmana.*/
	public List<BonusCriteria.KozmeticarIzvjestaj> izvjestajBrojaTretmanaIZaradePoKozmeticaruZaOpsegDatuma(LocalDate beginingDate, LocalDate endDate);
	
	
	public Map<StatusTretmana, Integer> izvjestajBrojaTretmanaPoRazlozima(LocalDate beginingDate, LocalDate endDate);
	
	/**Svi klijenti koji imaju karticu lojalnosti.*/
	public Collection<Klijent> klijentiKojiImajuKarticuLojalnosti();
	
	
	public int brojIzvrsenihTretmanaZaTipTretmana(KozmetickiTretman.TipTretmana tipTretmana, LocalDate beginingDate, LocalDate endDate);
	
	
	public Number zaradaZaTipTretmana(KozmetickiTretman.TipTretmana tipTretmana, LocalDate beginingDate, LocalDate endDate);
}
