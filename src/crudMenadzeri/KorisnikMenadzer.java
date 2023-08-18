package crudMenadzeri;

import java.util.Collection;

import dataProvajderi.DataProvider;
import entiteti.Korisnik;
import helpers.Query;
import helpers.Updater;

public abstract class KorisnikMenadzer<T extends Korisnik> extends Menadzer<T> {
	
	private DataProvider<T, ?> mainProvider;
	private ZakazanTretmanMenadzer zakazanTretmanMenadzer;
	
	
	protected KorisnikMenadzer() {
		super();
	}
	
	protected KorisnikMenadzer(DataProvider<T, ?> mainProvider) {
		this();
		setMainProvider(mainProvider);
	}
		
	protected KorisnikMenadzer(DataProvider<T, ?> mainProvider, ZakazanTretmanMenadzer zakazanTretmanProvider) {
		this();
		setMainProvider(mainProvider);
		setZakazanTretmanMenadzer(zakazanTretmanProvider);
	}
	
	
	@Override
	protected DataProvider<T, ?> getMainProvider(){
		return mainProvider;
	}
	
	protected void setMainProvider(DataProvider<T, ?> mainProvider) {
		this.mainProvider = mainProvider;
	}
	
	
	protected void setZakazanTretmanMenadzer(ZakazanTretmanMenadzer zakazanTretmanProvider) {
		this.zakazanTretmanMenadzer = zakazanTretmanProvider;
	}
	
	protected ZakazanTretmanMenadzer getZakazanTretmanMenadzer() {
		return zakazanTretmanMenadzer;
	}
	
	
	
	protected void removeKlijentiFromZakazaniTretmani(Collection<T> clients) {
		T deletedKlijent = getMainProvider().getDeletedInstance();
		
		getZakazanTretmanMenadzer().update(
				new Query<>(zt -> clients.contains(zt.getKlijent())),
				new Updater<>(zt -> zt.setKlijent(deletedKlijent))
		);
	}
	
}
