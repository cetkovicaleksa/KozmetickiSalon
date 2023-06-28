package saloni;

import java.util.HashMap;

import dataProvajderi.DataManager;
import entiteti.Korisnik;
import helpers.Query;
import exceptions.PasswordMissmatchException;
import exceptions.UsernameNotFoundException;

public class Salon {
	
	private String nazivSalona;
	
	private DataManager dataManager;
	
	
	
	
	
	
	
	
	
	
	
	
	public Korisnik login(String korisnickoIme, String lozinka) {
		Query<Korisnik> findKorisnik = new Query<>(x -> x.getKorisnickoIme() == korisnickoIme);		
		Korisnik korisnik = this.speakToTheManager().get(findKorisnik);  //TODO: provjeri da li ce raditi kako treba sa providerom??
		
		
		if (korisnik == null) throw new UsernameNotFoundException("Nepostojece korisnicko ime: " + korisnickoIme);
		
		findKorisnik.i(k -> k.getLozinka() == lozinka);
		
		if (!findKorisnik.test(korisnik)) throw new PasswordMissmatchException("Pogresna lozinka.");				
		
		return korisnik;
		
	}
	
	private DataManager speakToTheManager() {
		return this.dataManager;
	}
	
	
	
	

	private static abstract class Authenticator{
		
		private HashMap<Salon, UlogovaniSalon[]> cache = new HashMap<>();
		
		
		
		
	}

}
