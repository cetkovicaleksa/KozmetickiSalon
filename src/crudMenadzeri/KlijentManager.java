package crudMenadzeri;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import dataProvajderi.IdNotUniqueException;
import dataProvajderi.KlijentProvider;
import dataProvajderi.ZakazanTretmanProvider;
import entiteti.Klijent;
import helpers.Query;
import helpers.Updater;

public class KlijentManager implements ICRUDManager<Klijent> {
	
	private KlijentProvider klijentProvider;
	private ZakazanTretmanProvider zakazanTretmanProvider;
	
	
	protected KlijentProvider getKlijentProvider() {
		return klijentProvider;
	}
	
	@Override
	public void create(Klijent entitet) throws IdNotUniqueException {
		getKlijentProvider().post(entitet);
	}
	
	@Override
	public List<Klijent> read(Query<Klijent> selector) {
		return getKlijentProvider().get(selector);
	}
	
	@Override
	public Iterator<Klijent> readAll() {
		return getKlijentProvider().get();
	}
	
	@Override
	public boolean update(Query<Klijent> selector, Updater<Klijent> updater) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean delete(Query<Klijent> selector) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void load() throws IOException{
		getKlijentProvider().loadData();
		
	}
	
	

}
