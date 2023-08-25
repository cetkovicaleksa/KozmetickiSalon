package gui.interfaces;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;

import entiteti.Klijent;
import entiteti.Korisnik;
import entiteti.StatusTretmana;

public interface KorisnikSalon<K extends Korisnik> extends LoggedInSalon{
	
	public void izvjestajKozmeticara(LocalDate beginingDate, LocalDate endDate); //kozmeticar -> broj izvrsenih tretmana i prohod	
	
}