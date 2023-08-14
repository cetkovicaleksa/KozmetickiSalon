package crudMenadzeri;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import dataProvajderi.DataProvider;
import dataProvajderi.IdNotUniqueException;
import dataProvajderi.ZakazanTretmanProvider;
import entiteti.Entitet;
import entiteti.Korisnik;
import entiteti.ZakazanTretman;
import helpers.Query;
import helpers.Updater;

public abstract class Manager<T extends Entitet> implements ICRUDManager<T> {
	
	private DataProvider<T, ?> mainProvider;
	private ZakazanTretmanProvider zakazanTretmanProvider;
	
	
	public Manager() {}
	
	public Manager(DataProvider<T, ?> mainProvider, ZakazanTretmanProvider zakazanTretmanProvider) {
		setMainProvider(mainProvider);
		setZakazanTretmanProvider(zakazanTretmanProvider);
	}
	
	
	protected void setMainProvider(DataProvider<T, ?> mainProvider) {
		this.mainProvider = mainProvider;
	}
	
	protected DataProvider<T, ?> getMainProvider(){
		return mainProvider;
	}
	
	protected void setZakazanTretmanProvider(ZakazanTretmanProvider zakazanTretmanProvider) {
		this.zakazanTretmanProvider = zakazanTretmanProvider;
	}
	
	protected ZakazanTretmanProvider getZakazanTretmanProvider() {
		return zakazanTretmanProvider;
	}
	
	
	
	@Override
	public void create(T entitet) throws IdNotUniqueException {
		getMainProvider().post(entitet);
	}
	
	
	@Override
	public List<T> read(Query<T> selector) {
		return getMainProvider().get(selector);
	}
	
	
	@Override
	public Iterator<T> readAll() {
		return getMainProvider().get();
	}
	
	
	@Override
	public boolean update(Query<T> selector, Updater<T> updater) throws IdNotUniqueException {
		return getMainProvider().put(selector, updater);
	}
	
	
	@Override
	public boolean delete(Query<T> selector) {
		return getMainProvider().delete(selector);
	}
	
	
	@Override
	public void load() throws IOException {
		getMainProvider().loadData();		
	}
	
	
	
	/**A class that helps with removing clients from their zakazaniTretman-i when deleting them.
	 * Should be made without parameter just using Korisnik but constructors won't work for some reason??*/
	public class KlijentFromZTRemover<E extends Korisnik>{
		private Iterator<E> klijentIterator;
		private ZakazanTretmanProvider zakazanTretmanProvider;
		private E trenutniKlijent, placeholderKlijent;
		private int numRemoved = 0;
		
		private final Query<ZakazanTretman> Q = new Query<>(zt -> {
			return zt.getKlijent() == trenutniKlijent;
		});
		
		private final Updater<ZakazanTretman> U = new Updater<>(zt -> {
			zt.setKlijent(placeholderKlijent);
			numRemoved++;
		});
		
		public KlijentFromZTRemover() {}
		
		public KlijentFromZTRemover(ZakazanTretmanProvider zakazanTretmanProvider, E klijent, E placeholderKorisnik) {
			this(zakazanTretmanProvider, Collections.singletonList(klijent).iterator(), placeholderKorisnik);
		}
		
		public KlijentFromZTRemover(ZakazanTretmanProvider zakazanTretmanProvider, List<E> klijentList, E placeholderKorisnik) {
			this(zakazanTretmanProvider, klijentList.iterator(), placeholderKorisnik);
		}
		
		public KlijentFromZTRemover(ZakazanTretmanProvider zakazanTretmanProvider, Iterator<E> klijentIterator, E placeholderKorisnik) {
			setZakazanTretmanProvider(zakazanTretmanProvider);
			setKlijentIterator(klijentIterator);
			setPlaceholderKlijent(placeholderKlijent);
		}
		

		public Iterator<E> getKlijentIterator() {
			return klijentIterator;
		}

		public void setKlijentIterator(Iterator<E> klijentIterator) {
			this.klijentIterator = klijentIterator;
		}

		public ZakazanTretmanProvider getZakazanTretmanProvider() {
			return zakazanTretmanProvider;
		}

		public void setZakazanTretmanProvider(ZakazanTretmanProvider zakazanTretmanProvider) {
			this.zakazanTretmanProvider = zakazanTretmanProvider;
		}

		public E getTrenutniKlijent() {
			return trenutniKlijent;
		}

		public void setTrenutniKlijent(E trenutniKlijent) {
			this.trenutniKlijent = trenutniKlijent;
		}

		public Korisnik getPlaceholderKlijent() {
			return placeholderKlijent;
		}

		public void setPlaceholderKlijent(E placeholderKlijent) {
			this.placeholderKlijent = placeholderKlijent;
		}
		
		/**Returnes the number of ZakazanTretman that had their Korisnik klijent substituted for placeholderKlijent.*/
		public int run() {
			Iterator<E> iter = getKlijentIterator();
			ZakazanTretmanProvider ztp = getZakazanTretmanProvider();
			
			while(iter.hasNext()) {
				ztp.put(Q, U);
			}
			
			int n = numRemoved;
			numRemoved = 0;
			return n;
		}
		
	}
	
	
	
	
}
