package crudMenadzeri;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import dataProvajderi.IdNotUniqueException;
import dataProvajderi.KlijentProvider;
import dataProvajderi.ZakazanTretmanProvider;
import entiteti.Klijent;
import entiteti.Korisnik;
import entiteti.ZakazanTretman;
import helpers.Query;
import helpers.Updater;

public class KlijentManager implements ICRUDManager<Klijent> {
	
	private KlijentProvider klijentProvider;
	private ZakazanTretmanProvider zakazanTretmanProvider;
	
	
	public KlijentManager() {}
	
	
	public KlijentManager(KlijentProvider klijentProvider, ZakazanTretmanProvider zakazanTretmanProvider) {
		setKlijentProvider(klijentProvider);
		setZakazanTretmanProvider(zakazanTretmanProvider);
	}

	
	protected KlijentProvider getKlijentProvider() {
		return klijentProvider;
	}
	
	protected void setKlijentProvider(KlijentProvider klijentProvider) {
		this.klijentProvider = klijentProvider;
	}
	
	protected ZakazanTretmanProvider getZakazanTretmanProvider() {
		return zakazanTretmanProvider;
	}
	
	protected void setZakazanTretmanProvider(ZakazanTretmanProvider zakazanTretmanProvider) {
		this.zakazanTretmanProvider = zakazanTretmanProvider;
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
		return getKlijentProvider().put(selector, updater);
	}
	
	@Override
	public boolean delete(Query<Klijent> selector) {
		List<Klijent> klijenti = getKlijentProvider().get(selector);
		getKlijentProvider().delete(selector);
		
		KlijentRemoverFromZakazanTretman remover = new KlijentRemoverFromZakazanTretman();
		klijenti.forEach(remover::run);
		
		return remover.hasRemoved();			
	}

	@Override
	public void load() throws IOException{
		getKlijentProvider().loadData();
		
	}
	
	
	public class KlijentRemoverFromZakazanTretman{
		private Iterator<Korisnik> klijentIterator;
		private ZakazanTretmanProvider zakazanTretmanProvider;
		private Korisnik trenutniKlijent, deletedKlijent;
					
		private Query<ZakazanTretman> q = new Query<>(zt -> {
			return zt.getKlijent() == trenutniKlijent;
		});
		
		private Updater<ZakazanTretman> u = new Updater<>(zt -> {
			zt.setKlijent(deletedKlijent);
		});
		
		public KlijentRemoverFromZakazanTretman(){}
		
		
		
		public KlijentRemoverFromZakazanTretman(ZakazanTretmanProvider zakazanTretmanProvider, Iterator<Korisnik> klijentIterator, Korisnik deletedKlijent) {
			this.deletedKlijent = deletedKlijent;
			this.zakazanTretmanProvider = zakazanTretmanProvider;
			this.klijentIterator = klijentIterator;
		}
		
		public void run(Klijent klijent) {
			
			this.klijent = klijent;
			ztp.put(q,  u);
		}
		
		public boolean hasRemovedAny() {
			return klijent != null;
		}
	}
	
	

}
