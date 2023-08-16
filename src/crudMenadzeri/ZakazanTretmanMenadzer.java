package crudMenadzeri;

import java.io.IOException;
import java.util.function.Function;
import java.util.function.Supplier;

import dataProvajderi.DataProvider;
import dataProvajderi.IdNotUniqueException;
import dataProvajderi.ZakazanTretmanProvider;
import entiteti.Klijent;
import entiteti.Korisnik;
import entiteti.Kozmeticar;
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
	
	private final Function<Korisnik, String> GET_ID_FOR_KORISNIK;
	private final DefaultDict<String, Korisnik> KORISNIK_IDS_DICT;
	
	//setting up the id getter and from id getter
	{
		GET_ID_FOR_KORISNIK = k -> {
			
			return 	(k instanceof Klijent) ? getKlijentMenadzer().getId((Klijent) k) :
				    (k instanceof Kozmeticar) ? getKozmeticarMenadzer().getId((Kozmeticar) k) :
				    (k instanceof Recepcioner) ? getRecepcionerMenadzer().getId((Recepcioner) k) :
				    (k instanceof Menadzer) ? getMenadzerMenadzer().getId((Menadzer) k) : DataProvider.DELETED_ID;
		};
		
		
		Query<Korisnik> isKorisnikDeletedQuery = new Query<>(korisnik -> {
			return getKlijentMenadzer().getDeletedInstance().equals(korisnik) ||
				   getKozmeticarMenadzer().getDeletedInstance().equals(korisnik) ||
				   getRecepcionerMenadzer() != null && getRecepcionerMenadzer().getDeletedInstance().equals(korisnik) ||
				   getMenadzerMenadzer() != null && getMenadzerMenadzer().getDeletedInstance().equals(korisnik);
		});
		
		Korisnik deletedKorisnik = new Korisnik(){
			@Override
			public boolean equals(Object obj) {
				if(obj == null) {
					return false;
				}
				
				if(this == obj) {
					return true;
				}
				
				if( !(obj instanceof Korisnik) ) {
					return false;
				}
				
				Korisnik korisnik = (Korisnik) obj;
				return isKorisnikDeletedQuery.test(korisnik);	
			}	
		};
		
		Supplier<DefaultDict<String, Korisnik>> getIdFromKorisnik = () -> {
			class D extends DefaultDict<String, Korisnik>{
				
				@Override
				public Korisnik get(String key) {
					Korisnik korisnik = getKlijentMenadzer().getById(key);
					if( !isKorisnikDeletedQuery.test(korisnik)) {
						return korisnik;
					}
					
					korisnik = getKozmeticarMenadzer().getById(key);
					if( !isKorisnikDeletedQuery.test(korisnik)) {
						return korisnik;
					}
					
					korisnik = (getRecepcionerMenadzer() == null) ? deletedKorisnik : getRecepcionerMenadzer().getById(key);
					if( !isKorisnikDeletedQuery.test(korisnik)) {
						return korisnik;
					}
					
					korisnik = (getMenadzerMenadzer() == null) ? deletedKorisnik : getMenadzerMenadzer().getById(key); 
					if( !isKorisnikDeletedQuery.test(korisnik) ) {
						return korisnik;
					}
					
					return deletedKorisnik;
				}
				
				@Override
				public void put(String key, Korisnik value) {
					throw new UnsupportedOperationException("This ids dictionary is read only.");
				}
				
				@Override
				public Korisnik getDefaultValue() {
					return deletedKorisnik;
				}	
			}
			
			return new D();
		};
		
		
		KORISNIK_IDS_DICT = getIdFromKorisnik.get();
	}

	
	
	
	public ZakazanTretmanMenadzer(
			ZakazanTretmanProvider zakazanTretmanProvider, TipTretmanaMenadzer tipTretmanaMenadzer,
			KlijentMenadzer klijentMenadzer, KozmeticarMenadzer kozmeticarMenadzer) {
		
	}
	
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
	public void create(ZakazanTretman entitet) throws IdNotUniqueException, IllegalArgumentException {
		
		if( !getTipTretmanaMenadzer().exists(entitet.getTipTretmana())) {
			throw new IllegalArgumentException("Can't create a ZakazanTretman ["+ getMainProvider().getId(entitet) + "] that has a non existent TipTretmana as its treatment.");
		}
		
		Kozmeticar kozmeticar = entitet.getKozmeticar();
		if( !getKozmeticarMenadzer().exists(kozmeticar) ) {
			throw new IllegalArgumentException("Can't create a ZakazanTretman that has a non existent Kozmeticar as its kozmeticar.");
		}
		
		Korisnik klijent = entitet.getKlijent();
		
		if(kozmeticar.equals(klijent)) {
			throw new IllegalArgumentException("Can't create a ZakazanTretman that has the same Korisnik as the kozmeticar and klijent.");
		}		
		
		if( !klijentExists(klijent) ) {
			throw new IllegalArgumentException("Can't create a ZakazanTretman that has a non existent Korisnik as its klijent.");
		}
			
		getMainProvider().post(entitet);
	}



	@Override
	public void load() throws IOException {
		getMainProvider().loadData(KORISNIK_IDS_DICT, getKozmeticarMenadzer().getIds(), getTipTretmanaMenadzer().getIds());
	}
	
	
	@Override
	public void save() throws IOException {
		getMainProvider().saveData(GET_ID_FOR_KORISNIK, getKozmeticarMenadzer()::getId, getTipTretmanaMenadzer()::getId);
	}
	
	
	
	private boolean klijentExists(Korisnik klijent) {
		return 	klijent instanceof Klijent && getKlijentMenadzer().exists((Klijent) klijent) ||
				klijent instanceof Kozmeticar && getKozmeticarMenadzer().exists((Kozmeticar) klijent) ||
				klijent instanceof Menadzer && getMenadzerMenadzer() != null && getMenadzerMenadzer().exists((Menadzer) klijent) ||
				klijent instanceof Recepcioner && getRecepcionerMenadzer() != null && getRecepcionerMenadzer().exists((Recepcioner) klijent);
	}


}
