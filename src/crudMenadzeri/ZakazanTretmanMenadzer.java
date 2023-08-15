package crudMenadzeri;

import java.io.IOException;

import dataProvajderi.IdNotUniqueException;
import dataProvajderi.ZakazanTretmanProvider;
import entiteti.Klijent;
import entiteti.Korisnik;
import entiteti.Kozmeticar;
import entiteti.KozmetickiTretman;
import entiteti.KozmetickiTretman.TipTretmana;
import entiteti.Recepcioner;
import entiteti.Menadzer;
import entiteti.ZakazanTretman;
import helpers.DefaultDict;
import helpers.Query;

public class ZakazanTretmanMenadzer extends crudMenadzeri.Menadzer<ZakazanTretman> {
	
	private ZakazanTretmanProvider zakazanTretmanProvider;
	private TipTretmanaMenadzer tipTretmanaMenadzer;
	//need the rest for loading and saving data
	private KlijentMenadzer klijentMenadzer;
	private KozmeticarMenadzer kozmeticarMenadzer;
	private RecepcionerMenadzer recepcionerMenadzer;
	private MenadzerMenadzer menadzerMenadzer;
	

	//public ZakazanTretmanMenadzer() TODO: see if you should make it so that you don't need to have recepcioner and menadzer?

	public ZakazanTretmanMenadzer(
			ZakazanTretmanProvider zakazanTretmanProvider, TipTretmanaMenadzer tipTretmanaMenadzer,
			KlijentMenadzer klijentMenadzer, KozmeticarMenadzer kozmeticarMenadzer,
			RecepcionerMenadzer recepcionerMenadzer, MenadzerMenadzer menadzerMenadzer) {
		super();
		setMainProvider(zakazanTretmanProvider);
		setTipTretmanaMenadzer(tipTretmanaMenadzer);
		setKlijentMenadzer(klijentMenadzer);
		setKozmeticarMenadzer(kozmeticarMenadzer);
		setRecepcionerMenadzer(recepcionerMenadzer);
		setMenadzerMenadzer(menadzerMenadzer);
	}


	
	@Override
	public ZakazanTretmanProvider getMainProvider() {
		return zakazanTretmanProvider;
	}

	public void setMainProvider(ZakazanTretmanProvider zakazanTretmanProvider) {
		this.zakazanTretmanProvider = zakazanTretmanProvider;
	}
	
	
	public TipTretmanaMenadzer getTipTretmanaMenadzer() {
		return tipTretmanaMenadzer;
	}

	public void setTipTretmanaMenadzer(TipTretmanaMenadzer tipTretmanaMenadzer) {
		this.tipTretmanaMenadzer = tipTretmanaMenadzer;
	}


	public KlijentMenadzer getKlijentMenadzer() {
		return klijentMenadzer;
	}

	public void setKlijentMenadzer(KlijentMenadzer klijentMenadzer) {
		this.klijentMenadzer = klijentMenadzer;
	}


	public KozmeticarMenadzer getKozmeticarMenadzer() {
		return kozmeticarMenadzer;
	}

	public void setKozmeticarMenadzer(KozmeticarMenadzer kozmeticarMenadzer) {
		this.kozmeticarMenadzer = kozmeticarMenadzer;
	}


	public RecepcionerMenadzer getRecepcionerMenadzer() {
		return recepcionerMenadzer;
	}

	public void setRecepcionerMenadzer(RecepcionerMenadzer recepcionerMenadzer) {
		this.recepcionerMenadzer = recepcionerMenadzer;
	}


	public MenadzerMenadzer getMenadzerMenadzer() {
		return menadzerMenadzer;
	}

	public void setMenadzerMenadzer(MenadzerMenadzer menadzerMenadzer) {
		this.menadzerMenadzer = menadzerMenadzer;
	}
	
	
	
	@Override
	public void create(ZakazanTretman entitet) throws IdNotUniqueException {
		TipTretmana tretman = entitet.getTipTretmana();
		
		if( tretman != null && !getTipTretmanaMenadzer().read(new Query<TipTretmana>(x -> x.equals(tretman))).isEmpty() ){
			
		}
		
		super.create(entitet);
	}



	@Override
	public void load() throws IOException {
		Dict korisniciIds = new Dict(getKlijentMenadzer().getIds(), getKozmeticarMenadzer().getIds(), getRecepcionerMenadzer().getIds(), getMenadzerMenadzer().getIds());
		getMainProvider().loadData(korisniciIds, getKozmeticarMenadzer().getIds(), getTipTretmanaMenadzer().getIds());
	}
	
	
	
	
	
	//need this if we want to be able to schedule appointments for all Korisnik
	private class Dict extends DefaultDict<String, Korisnik>{
		private DefaultDict<String, Klijent> klijentiIds;
		private DefaultDict<String, Kozmeticar> kozmeticariIds;
		private DefaultDict<String, Recepcioner> recepcioneriIds;
		private DefaultDict<String, Menadzer> menadzeriIds;
		private Query<Korisnik> deletedQuery;
		
		

		private Dict() {}
		
		private Dict(DefaultDict<String, Klijent> klijentiIds, DefaultDict<String, Kozmeticar> kozmeticariIds,
				DefaultDict<String, Recepcioner> recepcioneriIds, DefaultDict<String, Menadzer> menadzeriIds) {
			
		}

		

		@Override
		public Korisnik getDefaultValue() {
			return new Korisnik(){
				{
					if(getDeletedQuery() == null) {						
						Query<Korisnik> deletedQuery = new Query<>();
						
						if(getKlijentiIds() != null){
							deletedQuery.ili(k -> getKlijentiIds().getDefaultValue().equals(k));							
						}
						if(getKozmeticariIds() != null){
							deletedQuery.ili(k -> getKozmeticariIds().getDefaultValue().equals(k));							
						}
						if(getRecepcioneriIds() != null){
							deletedQuery.ili(k -> getRecepcioneriIds().getDefaultValue().equals(k));							
						}
						if(getMenadzeriIds() != null){
							deletedQuery.ili(k -> getMenadzeriIds().getDefaultValue().equals(k));							
						}
						
						setDeletedQuery(deletedQuery);						
					}
				}

				@Override
				public boolean equals(Object obj) {
					if(obj instanceof Korisnik) {
						Korisnik korisnik = (Korisnik) obj;
						return getDeletedQuery().test(korisnik);						
					}
					
					return false;
				}	
			};
		}

		
		@Override
		public Korisnik get(String key) {
			Korisnik deleted = getDefaultValue();
			Korisnik korisnik;
			
			if(getKlijentiIds() != null) {
				korisnik = getKlijentiIds().get(key);
				if(!deleted.equals(korisnik)) {
					return korisnik;
				}
			}
			
			if(getKozmeticariIds() != null) {
				korisnik = getKozmeticariIds().get(key);
				if(!deleted.equals(korisnik)) {
					return korisnik;
				}
			}
			
			if( getMenadzeriIds() != null) {
				korisnik =  getMenadzeriIds().get(key);
				if(!deleted.equals(korisnik)) {
					return korisnik;
				}
			}
			
			if(getRecepcioneriIds() != null) {
				korisnik = getRecepcioneriIds().get(key);
				if(!deleted.equals(korisnik)) {
					return korisnik;
				}
			}
			
			return deleted;
		}
		

		@Override
		public void put(String key, Korisnik value) {
			if(value instanceof Klijent && getKlijentiIds() != null) {
				getKlijentiIds().put(key, (Klijent) value);
			}
			
			if(value instanceof Kozmeticar && getKozmeticariIds() != null) {
				getKozmeticariIds().put(key, (Kozmeticar) value);
			}
			
			if(value instanceof Recepcioner && getRecepcioneriIds() != null) {
				getRecepcioneriIds().put(key, (Recepcioner) value);
			}
			
			if(value instanceof Menadzer && getMenadzeriIds() != null) {
				getMenadzeriIds().put(key, (Menadzer) value);
			}
			
			throw new UnsupportedOperationException("Class not supported: " + value.getClass());
		}
		
		
		public DefaultDict<String, Kozmeticar> getKozmeticariIds() {
			return kozmeticariIds;
		}

		public DefaultDict<String, Recepcioner> getRecepcioneriIds() {
			return recepcioneriIds;
		}

		public DefaultDict<String, Menadzer> getMenadzeriIds() {
			return menadzeriIds;
		}
		
		public DefaultDict<String, Klijent> getKlijentiIds() {
			return klijentiIds;
		}
		
		public Query<Korisnik> getDeletedQuery() {
			return deletedQuery;
		}

		public void setDeletedQuery(Query<Korisnik> deletedQuery) {
			this.deletedQuery = deletedQuery;
		}
	}


}
