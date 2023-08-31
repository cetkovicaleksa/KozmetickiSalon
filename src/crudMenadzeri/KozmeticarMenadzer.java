package crudMenadzeri;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import dataProvajderi.DataProvider;
import dataProvajderi.IdNotUniqueException;
import dataProvajderi.KozmeticarProvider;
import entiteti.BonusCriteria;
import entiteti.Kozmeticar;
import entiteti.KozmetickiTretman;
import entiteti.StatusTretmana;
import entiteti.ZakazanTretman;
import entiteti.BonusCriteria.KozmeticarIzvjestaj;
import helpers.Query;
import helpers.Updater;

public class KozmeticarMenadzer extends KorisnikMenadzer<Kozmeticar> {
	
	private KozmetickiTretmanMenadzer kozmetickiTretmanMenadzer;
	
	public KozmeticarMenadzer() {
		super();
	}
	
	public KozmeticarMenadzer(KozmeticarProvider kozmeticarProvider, KozmetickiTretmanMenadzer kozmetickTretmanMenadzer, ZakazanTretmanMenadzer zakazanTretmanMenadzer) {
		super(kozmeticarProvider, zakazanTretmanMenadzer);
		setKozmetickiTretmanMenadzer(kozmetickTretmanMenadzer);
	}
	
	
	protected KozmetickiTretmanMenadzer getKozmetickiTretmanMenadzer() {
		return kozmetickiTretmanMenadzer;
	}
	
	protected void setKozmetickiTretmanMenadzer(KozmetickiTretmanMenadzer kozmetickiTretmanMenadzer) {
		this.kozmetickiTretmanMenadzer = kozmetickiTretmanMenadzer;
	}
	
	
	//private KozmeticarProvider getKozmeticarProvider() {
		//return (KozmeticarProvider) super.getMainProvider();
	//}

	
	
	@Override
	/**Adds a new kozmeticar to the provider and checks whether all tretmani that the kozmeticar has exist.
	 * If not new tretmani are added to the tretmani provider.*/  //TODO: so a kozmeticki tretman may exist witout it tipovi tretmana
	public void create(Kozmeticar entitet) throws IdNotUniqueException, IllegalArgumentException {
		DataProvider<Kozmeticar, ?> mainProvider = super.getMainProvider();
		
		if(entitet == null || mainProvider.getDeletedInstance().equals(entitet)) {
			throw new IllegalArgumentException("Can't add a Kozmeticar that is either null or deleted kozmeticar.");
		}
		
		try{
			mainProvider.post(entitet); 
		}catch(IdNotUniqueException e) {
			throw e;
		}
		
		try {
			addNewKozmetickiTretmani(entitet.getTretmani());		
		}catch(IdNotUniqueException e) {
			throw e; //maby don't add the kozmeticar or just say that not all treatments have unique ids
		}
	}
	
	
	/**Adds all treatments that kozmeticar has and are not in the KozmetickiTretmanMenadzer.*/
	private void addNewKozmetickiTretmani(Collection<KozmetickiTretman> tretmaniKozmeticara) throws IdNotUniqueException{		
		if(tretmaniKozmeticara == null || tretmaniKozmeticara.isEmpty()) {
			return;
		}
		
		//provjeravamo da li postoje tretmani za koje je kozmeticar obucen, a da ne postoje u KozmetickiTretmanProvider
		//ako ne postoje dodajemo ih
		KozmetickiTretmanMenadzer kozmetickiTretmanMenadzer = getKozmetickiTretmanMenadzer();
		Iterator<KozmetickiTretman> kozmetickiTretmaniIter;
		boolean foundCurrentTretman = false;
		
		for(KozmetickiTretman tretman : tretmaniKozmeticara) {
			kozmetickiTretmaniIter = kozmetickiTretmanMenadzer.readAll();
			foundCurrentTretman &= false;
			
			while(kozmetickiTretmaniIter.hasNext()) {
				if(kozmetickiTretmaniIter.next().equals(tretman)) {
					foundCurrentTretman = true;
					break;
				}
			}
			
			if(!foundCurrentTretman) {
				kozmetickiTretmanMenadzer.create(tretman); //TODO: because we know that there is a kozmeticar that can preform this tretment
			}
		}
	}

	
	
	@Override
	/**Deletes all kozmeticari that satisfy the query and searches for kozmeticki tretmani that will be kozmeticarless after deletion.
	 *The found kozmeticki tretmani will also be removed from their menadzer. Also removes the kozmeticar from any zakazan tretman where he is a client or the kozmeticar.*/
	public boolean delete(Query<Kozmeticar> selector) {
		DataProvider<Kozmeticar, ?> kozmeticarProvider = super.getMainProvider();
		List<Kozmeticar> kozmeticariZaBrisanje = kozmeticarProvider.get(selector);
		
		if(kozmeticariZaBrisanje.isEmpty()) {
			return false;
		}
		
		kozmeticarProvider.delete(selector);
		
		ArrayList<KozmetickiTretman> tretmani = new ArrayList<>();		
		//add all the treatments to tretmani that may need to be deleted (union of the treatments from all deleted kozmeticari)
		for(Kozmeticar izbrisaniKozmeticar : kozmeticariZaBrisanje) {
			for(KozmetickiTretman tretmanKozmeticara : izbrisaniKozmeticar.getTretmani()) {
				if(!tretmani.contains(tretmanKozmeticara)) {
					tretmani.add(tretmanKozmeticara);
				}
			}
		}
		
		this.deleteKozmetickiTretmaniThatHaveNoKozmeticar(tretmani.toArray(new KozmetickiTretman[0]));
		this.removeKozmeticariFromZakazaniTretmani(kozmeticariZaBrisanje.toArray(new Kozmeticar[0]));		
		return true;
	}
	
	
	private void deleteKozmetickiTretmaniThatHaveNoKozmeticar(KozmetickiTretman... potentiallyDeletedTreatments) {
		DataProvider<Kozmeticar, ?> kozmeticarProvider = super.getMainProvider();
		KozmetickiTretmanMenadzer kozmetickiTretmanMenadzer = getKozmetickiTretmanMenadzer();
		
		//we are only deleting KozmetickiTretman the KozmetickiTretmanMenadzer will make sure that its all TipTretmana are deleted
		for(KozmetickiTretman tretman : potentiallyDeletedTreatments) {
			Iterator<Kozmeticar> kozmeticariIter = kozmeticarProvider.get();
			boolean foundKozmeticarThatHasTreatment = false;
			
			while(kozmeticariIter.hasNext()) {
				if(kozmeticariIter.next().getTretmani().contains(tretman)) {
					foundKozmeticarThatHasTreatment  = true;
					break;
				}
			}
			
			if(!foundKozmeticarThatHasTreatment) {
				kozmetickiTretmanMenadzer.delete(new Query<>(kt -> kt.equals(tretman)));
			}
		}
	}
	

	//removes kozmeticar as a kozmeticar and as a client form the zakazani tretman
	private void removeKozmeticariFromZakazaniTretmani(Kozmeticar... kozmeticariZaBrisanje) {
		ZakazanTretmanMenadzer ztm = super.getZakazanTretmanMenadzer();
		Kozmeticar deletedKozmeticar = super.getMainProvider().getDeletedInstance();

		Function<Kozmeticar, Query<ZakazanTretman>> getQuery = k -> {
			return new Query<>(zt -> k.equals(zt.getKlijent()) || k.equals(zt.getKozmeticar()));
		};
		
		BiFunction<Kozmeticar, Kozmeticar, Updater<ZakazanTretman>> getUpdater = (kozmeticar, deleted) -> {
			return new Updater<>(zt -> {
				if(kozmeticar.equals(zt.getKlijent())) {
					zt.setKlijent(deleted);
				}
				
				if(kozmeticar.equals(zt.getKozmeticar())) {
					zt.setKozmeticar(deleted);
				}
			});		
		};
		
		
		for(Kozmeticar kozmeticar : kozmeticariZaBrisanje) {
			ztm.update(getQuery.apply(kozmeticar), getUpdater.apply(kozmeticar, deletedKozmeticar));
		}
	}
	
	
	
	@Override
	public void load() throws IOException {
		((KozmeticarProvider) super.getMainProvider()).loadData(getKozmetickiTretmanMenadzer().getIds());;
	}
	
	@Override
	public void save() throws IOException {
		((KozmeticarProvider) super.getMainProvider()).saveData(getKozmetickiTretmanMenadzer()::getId);
	}
	
	
	
	public List<Kozmeticar> allKozmeticariThatCanPreformTreatment(KozmetickiTretman tretman){
		return getMainProvider().get(
					new Query<>(kozmeticar -> {
						return ( kozmeticar.getTretmani() != null && kozmeticar.getTretmani().contains(tretman) );
					})
				);
	}
	
	
	boolean kozmeticarThatCenPreformTreatmentExists(KozmetickiTretman tretman) {
		return !(this.allKozmeticariThatCanPreformTreatment(tretman).isEmpty());
	}
	
	
	void removeTretmaniFromAllKozmeticari(KozmetickiTretman... tretmani) {
		Query<Kozmeticar> query = new Query<>();
		for(KozmetickiTretman tretman : tretmani) {
			query.ili(
					kozmeticar -> ( kozmeticar.getTretmani() != null && kozmeticar.getTretmani().contains(tretman) )
			);
		}
		
		List<Kozmeticar> kozmeticari = super.getMainProvider().get(query);
		
		if(kozmeticari.isEmpty()) {
			return;
		}
		
		List<KozmetickiTretman> tretmaniLista = Arrays.asList(tretmani);
		kozmeticari.forEach(kozmeticar -> {
			kozmeticar.getTretmani().removeAll(tretmaniLista);
		});
	}
	
	
	
	public List<BonusCriteria.KozmeticarIzvjestaj> izvjestajKozmeticaraZaDatume(LocalDate beginingDate, LocalDate endDate){
		List<BonusCriteria.KozmeticarIzvjestaj> izvjestaj = new ArrayList<>();
		Map<Kozmeticar, Map<StatusTretmana, Integer>> brojTretmanaZaKozmeticaraMap = new HashMap<>();
		Map<Kozmeticar, Map<StatusTretmana, Number>> zaradaPoStatusuZaKozmeticaraMap = new HashMap<>();
				
		List<ZakazanTretman> zakazaniTretmaniZaDateDatume = getZakazanTretmanMenadzer().read(
				new Query<>(zakazanTretman -> {
					LocalDate datum = zakazanTretman.getDatum();
					return datum.isBefore(endDate) && datum.isAfter(beginingDate);
				})
		);
		
		zakazaniTretmaniZaDateDatume.forEach(zakazanTretman -> {
			
			Kozmeticar kozmeticar = zakazanTretman.getKozmeticar();
			StatusTretmana status = zakazanTretman.getStatus();
			double cijena = zakazanTretman.getCijena();
			
			// ako prvi put nailazimo na ovog kozmeticara pravimo nove mape za njega
			Map<StatusTretmana, Integer> brojMapa = brojTretmanaZaKozmeticaraMap.computeIfAbsent(kozmeticar, k -> {
				
				Map<StatusTretmana, Integer> map = new HashMap<>();
				brojTretmanaZaKozmeticaraMap.put(kozmeticar, map);
				
				for(StatusTretmana statusTretmana : StatusTretmana.values()) {
					map.put(statusTretmana, 0);
				}
				
				return map;				
			});
			
			Map<StatusTretmana, Number> zaradaMapa = zaradaPoStatusuZaKozmeticaraMap.computeIfAbsent(kozmeticar, k -> {
				
				Map<StatusTretmana, Number> map = new HashMap<>();
				zaradaPoStatusuZaKozmeticaraMap.put(kozmeticar, map);
				
				for(StatusTretmana statusTretmana : StatusTretmana.values()) {
					map.put(statusTretmana, 0);
				}
				
				return map;
			});
			
			// povecavamo broj tretmana za status i zaradu za status tretmana
			brojMapa.put(status, brojMapa.get(status) + 1);
			zaradaMapa.put(status, zaradaMapa.get(status).doubleValue() + cijena);
		});
		
		
		
		brojTretmanaZaKozmeticaraMap.forEach( (kozmeticar, mapa) -> izvjestaj.add(
				new KozmeticarIzvjestaj(beginingDate, endDate, kozmeticar, mapa, zaradaPoStatusuZaKozmeticaraMap.get(kozmeticar))
				) 
		);
	
		return izvjestaj;
	}

}
