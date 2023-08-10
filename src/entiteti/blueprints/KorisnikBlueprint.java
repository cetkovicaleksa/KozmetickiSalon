package entiteti.blueprints;

import java.lang.reflect.Method;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.sun.net.ssl.internal.ssl.Provider;

import entiteti.Entitet;
import entiteti.Korisnik;
import entiteti.Pol;
import helpers.IsUpdater;

public abstract class KorisnikBlueprint extends Blueprint<Korisnik> {
	
	public static final String[] fields = new String[]
			{
					"ime", "prezime", "adresa", "brojTelefona",
					"korisnickoIme", "lozinka", "pol"
			};

	
	
	public void ime(String ime) {
		Function<Korisnik, Void> func;
		IsUpdater<Korisnik> updater;
		Predicate<Korisnik> tester;
		
		if(super.isForBuilder()) {
			func = (x) -> {
				x.setIme(ime);
				return null;
			};
			
			super.add(func);
		}
		
		if(super.isForQuery()) {
			tester = (x) -> x.getIme().equals(ime);
			
			super.add(tester);
		}
		
		if(super.isForUpdater()) {
			updater = (x) -> x.setIme(ime);
			super.add(updater);
		}
		
	}
	
	
	public void prezime(String prezime) {
		Function<Korisnik, Void> func;
		IsUpdater<Korisnik> updater;
		Predicate<Korisnik> tester;
		
		if(super.isForBuilder()) {
			func = (x) -> {
				x.setPrezime(prezime);
				return null;
			};
			
			super.add(func);
		}
		
		if(super.isForQuery()) {
			tester = (x) -> x.getPrezime().equals(prezime);
			
			super.add(tester);
		}
		
		if(super.isForUpdater()) {
			updater = (x) -> x.setPrezime(prezime);
			super.add(updater);
		}
		
	}
	
	@SuppressWarnings("unused")
	protected <E extends Korisnik, X> void choose(X value, Function<E, X> getter, IsUpdater<E> updater, BiFunction<X, X, Boolean> tester) {
				
		if(super.isForBuilder()) {
			Function<E, Void> func = (e) -> {
				updater.update(e);				
				return null;
			};
			
			super.add(func);
		}
		
		if(super.isForQuery()) {
			Predicate<E> test = (e) -> tester.apply(value, getter.apply(e));
			super.add(test);
		}
		
		if(super.isForUpdater()) {
			IsUpdater<E> update = (e) -> updater.update(e);
			super.add(update);
		}
	}
	
	

}
