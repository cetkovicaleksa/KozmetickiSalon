package crudMenadzeri;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import dataProvajderi.DataProvider;
import entiteti.Korisnik;
import entiteti.ZakazanTretman;
import helpers.Query;
import helpers.Updater;

public abstract class KorisnikMenadzer<T extends Korisnik> extends Menadzer<T> {
	
	private DataProvider<T, ?> mainProvider;
	private ZakazanTretmanMenadzer zakazanTretmanMenadzer;
	
	
	public KorisnikMenadzer() {
		super();
	}
		
	public KorisnikMenadzer(DataProvider<T, ?> mainProvider, ZakazanTretmanMenadzer zakazanTretmanProvider) {
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
	
	
	

	
	
	// old code
	
	/**A class that helps with removing clients from their zakazaniTretman-i when deleting them.
	 * Should be made without parameter just using Korisnik but constructors won't work for some reason??*/
	public class KlijentFromZTRemover<E extends Korisnik>{
		private Iterator<E> klijentIterator;
		private ZakazanTretmanMenadzer zakazanTretmanProvider;
		private E trenutniKlijent, placeholderKlijent;
		private int numRemoved = 0;
		
		private final Query<ZakazanTretman> Q = new Query<>(zt -> {
			return zt.getKlijent().equals(trenutniKlijent);  //TODO
		});
		
		private final Updater<ZakazanTretman> U = new Updater<>(zt -> {
			zt.setKlijent(placeholderKlijent);
			numRemoved++;
		});
		
		public KlijentFromZTRemover() {}
		
		public KlijentFromZTRemover(ZakazanTretmanMenadzer zakazanTretmanProvider, E klijent, E placeholderKorisnik) {
			this(zakazanTretmanProvider, Collections.singletonList(klijent).iterator(), placeholderKorisnik);
		}
		
		public KlijentFromZTRemover(ZakazanTretmanMenadzer zakazanTretmanProvider, List<E> klijentList, E placeholderKorisnik) {
			this(zakazanTretmanProvider, klijentList.iterator(), placeholderKorisnik);
		}
		
		public KlijentFromZTRemover(ZakazanTretmanMenadzer zakazanTretmanProvider, Iterator<E> klijentIterator, E placeholderKorisnik) {
			setZakazanTretmanMenadzer(zakazanTretmanProvider);
			setKlijentIterator(klijentIterator);
			setPlaceholderKlijent(placeholderKlijent);
		}
		

		public Iterator<E> getKlijentIterator() {
			return klijentIterator;
		}

		public void setKlijentIterator(Iterator<E> klijentIterator) {
			this.klijentIterator = klijentIterator;
		}

		public ZakazanTretmanMenadzer getZakazanTretmanMenadzer() {
			return zakazanTretmanProvider;
		}

		public void setZakazanTretmanMenadzer(ZakazanTretmanMenadzer zakazanTretmanProvider) {
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
			ZakazanTretmanMenadzer ztm = getZakazanTretmanMenadzer();
			
			while(iter.hasNext()) {
				setTrenutniKlijent(iter.next());
				ztm.update(Q, U);
			}
			
			int n = numRemoved;
			numRemoved = 0;
			return n;
		}
		
	}
	
	
	
	
}
