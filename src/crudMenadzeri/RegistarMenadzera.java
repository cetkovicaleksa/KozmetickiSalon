package crudMenadzeri;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.function.Predicate;


import dataProvajderi.KlijentProvider;
import dataProvajderi.KozmeticarProvider;
import dataProvajderi.KozmetickiTretmanProvider;
import dataProvajderi.MenadzerProvider;
import dataProvajderi.RecepcionerProvider;
import dataProvajderi.SalonProvider;
import dataProvajderi.TipTretmanaProvider;
import dataProvajderi.ZakazanTretmanProvider;
import entiteti.BonusCriteria;
import entiteti.Klijent;
import entiteti.Korisnik;
import entiteti.Kozmeticar;
import entiteti.KozmetickiTretman;
import entiteti.NivoStrucneSpreme;
import entiteti.StatusTretmana;
import entiteti.ZakazanTretman;
import entiteti.Zaposleni;
import entiteti.KozmetickiTretman.TipTretmana;
import helpers.Query;
import helpers.Settings;
import helpers.Updater;

public class RegistarMenadzera {
	
	private Settings settings;
	
	private KlijentMenadzer klijentMenadzer;
	private KozmeticarMenadzer kozmeticarMenadzer;
	private RecepcionerMenadzer recepcionerMenadzer;
	private MenadzerMenadzer menadzerMenadzer;
	
	private KozmetickiTretmanMenadzer kozmetickiTretmanMenadzer;
	private TipTretmanaMenadzer tipTretmanaMenadzer;
	private ZakazanTretmanMenadzer zakazanTretmanMenadzer;
	
	private SalonMenadzer salonMenadzer;
	


	public RegistarMenadzera() {}
	
	public RegistarMenadzera(Settings settings) {
		setSettings(settings);
		initializeMenadzers();
	}
	
	
	
	public Map<StatusTretmana, Collection<ZakazanTretman>> getZakazaniTretmaniKorisnika(Korisnik korisnik, boolean asKlijent, boolean asKozmeticar){
		Map<StatusTretmana, Collection<ZakazanTretman>> result = new TreeMap<>();
		Query<ZakazanTretman> query = new Query<>();
		
		if(asKlijent) {
			query.i(zt -> korisnik.equals(zt.getKlijent()));
		}
		
		if(asKozmeticar) {
			query.i(zt -> korisnik.equals(zt.getKozmeticar()));
		}

		
		Updater<ZakazanTretman> fakeUpdater = new Updater<>(
				zt -> {
					StatusTretmana status = zt.getStatus();
	                
	                result.putIfAbsent(status, new ArrayList<>());
	                result.get(status).add(zt);
				}
		);		
		
		getZakazanTretmanMenadzer().update(query, fakeUpdater);
		return result;
	}
	
	
	
	public SortedMap<LocalDate, SortedMap<LocalTime, ZakazanTretman>> getRasporedKozmeticara(Kozmeticar kozmeticar) {
		List<ZakazanTretman> zakazaniTretmaniKozmeticara = getZakazanTretmanMenadzer().read(new Query<>(zt -> kozmeticar.equals(zt.getKozmeticar())));
		SortedMap<LocalDate, SortedMap<LocalTime, ZakazanTretman>> raspored = new TreeMap<>();
		
		zakazaniTretmaniKozmeticara.forEach(tretman -> {
			LocalDate datum = tretman.getDatum();			
			
			raspored.computeIfAbsent( datum, k -> {  // TODO seems stupid 
													SortedMap<LocalTime, ZakazanTretman> map = new TreeMap<>();
													raspored.put(datum, map);
													return map;
												   } 
			).put(tretman.getVrijeme(), tretman);
		});
		
		return raspored;
	}
	
	
	
	
	
	
	public SortedSet<Integer> getKozmeticarFreeHours(Kozmeticar kozmeticar, LocalDate datum){
		SortedSet<Integer> freeHours = new TreeSet<>();
		for(int i = 0; i <= 23; i++) {
			freeHours.add(i);
		}
		
		List<ZakazanTretman> zakazaniTretmaniZaDatum = getZakazanTretmanMenadzer().read(
															new Query<>(
																 zt -> kozmeticar.equals(zt.getKozmeticar()) && datum.equals(zt.getDatum())
															)
														);		
		
		zakazaniTretmaniZaDatum.forEach(zt -> {
			freeHours.remove(zt.getVrijeme().getHour());  // TODO check this
		});
		
		return freeHours;
	}
	
	
	
	public void zakaziTretman(TipTretmana tipTretmana, Kozmeticar kozmeticar, Korisnik klijent, LocalDate datum,
					LocalTime vrijeme) {
		getZakazanTretmanMenadzer().create(
				new ZakazanTretman(tipTretmana, kozmeticar, klijent, datum, vrijeme)
		);				
	}
	
	
	
	public void recheckLoyaltyCards() {
		Iterator<ZakazanTretman> iter = getZakazanTretmanMenadzer().readAll();
		Map<Klijent, Double> totalSpentMap = new HashMap<>();
		
		while(iter.hasNext()) {
			ZakazanTretman tretman = iter.next();
			Korisnik korisnik = tretman.getKlijent();
			
			if(korisnik instanceof Klijent) {
				Klijent klijent = (Klijent) korisnik;
				totalSpentMap.put(klijent, tretman.getCijena() + totalSpentMap.computeIfAbsent(klijent, k -> 0d));
			}
		}
		
		double loyaltyThreshold = getSalonMenadzer().read().getLoyaltyCardThreshold();
		getKlijentMenadzer().update(
				new Query<>(klijent -> loyaltyThreshold <= totalSpentMap.computeIfAbsent(klijent, k -> 0d)),
				new Updater<>(klijent -> klijent.setHasLoyaltyCard(true))
		);
	}
	
	public void recheckLoyaltyCards(Number newLoyaltyCardThreshold) {
		getSalonMenadzer().read().setLoyaltyCardThreshold(newLoyaltyCardThreshold.doubleValue());
		recheckLoyaltyCards();
	}
	
	
	public void recheckEmployeeBonuses() {
		Query<Zaposleni> bonusQuery = 
				getSalonMenadzer().read().getBonusCriteria().getEmployeeCriteria(this::getIzvjestajForKozmeticar);
				
		
		getKozmeticarMenadzer().update(
				new Query<>(kozmeticar -> bonusQuery.test(kozmeticar)),
				new Updater<>(kozmeticar -> kozmeticar.setBonus(true))
			);
		
		getRecepcionerMenadzer().update(
				new Query<>(recepcioner -> bonusQuery.test(recepcioner)),
				new Updater<>(recepcioner -> recepcioner.setBonus(true))
			);
		
		getMenadzerMenadzer().update(
				new Query<>(menadzer -> bonusQuery.test(menadzer)),
				new Updater<>(menadzer -> menadzer.setBonus(true))
			);
	}
	
	public void recheckEmployeeBonuses(Set<String> specialEmployeeUsernames, boolean ignoreIfHadBonusLastTime, int godineStazaThreshold, 
			NivoStrucneSpreme nivoStrucneSpremeThreshold, double bazaPlateMin, double bazaPlateMax, int inTheLastNumberOfDays,
			int numberOfCompletedTreatmentsThreshold, double moneyEarnedThreshold) {
		
		getSalonMenadzer().read().setBonusCriteria(
				new BonusCriteria(
						specialEmployeeUsernames, ignoreIfHadBonusLastTime, godineStazaThreshold, 
						nivoStrucneSpremeThreshold, bazaPlateMin, bazaPlateMax, inTheLastNumberOfDays,
						numberOfCompletedTreatmentsThreshold, moneyEarnedThreshold
						)
		);
		
		recheckEmployeeBonuses();
	}
	
	
	
	public BonusCriteria.KozmeticarIzvjestaj getIzvjestajForKozmeticar(Kozmeticar kozmeticar, LocalDate beginingDate, LocalDate endDate){
		Map<StatusTretmana, Integer> numberOfTreatmentsMap = new HashMap<>();
		Map<StatusTretmana, Number> moneyEarnedMap = new HashMap<>();
		
		for(StatusTretmana status : new StatusTretmana[] {
											StatusTretmana.ZAKAZAN, StatusTretmana.IZVRSEN, 
											StatusTretmana.OTKAZAO_KLIJENT, StatusTretmana.OTKAZAO_SALON, 
											StatusTretmana.NIJE_SE_POJAVIO}
		   ) {
			numberOfTreatmentsMap.put(status, 0);
			moneyEarnedMap.put(status, 0d);
		}
		
		List<ZakazanTretman> zakazaniTretmaniKozmeticaraZaDateDatume = 
				getZakazanTretmanMenadzer().read(
						new Query<>(tretman -> {
							if(!kozmeticar.equals(tretman.getKozmeticar())) {
								return false;
							}
							LocalDate datum = tretman.getDatum();
							return (beginingDate.isBefore(datum) && endDate.isAfter(datum));
						})
				);
		
		for(ZakazanTretman tretman : zakazaniTretmaniKozmeticaraZaDateDatume) {
			StatusTretmana status = tretman.getStatus();
			numberOfTreatmentsMap.put(status, 1 + numberOfTreatmentsMap.get(status));
			moneyEarnedMap.put(status, tretman.getCijena() + moneyEarnedMap.get(status).doubleValue());
		}
		
		return new BonusCriteria.KozmeticarIzvjestaj(beginingDate, endDate, kozmeticar, numberOfTreatmentsMap, moneyEarnedMap);		
	}
	
	
	
	

	public void save() throws IOException{
		getKlijentMenadzer().save();
		getKozmeticarMenadzer().save();
		getRecepcionerMenadzer().save();
		getMenadzerMenadzer().save();
		
		getKozmetickiTretmanMenadzer().save();
		getTipTretmanaMenadzer().save();
		getZakazanTretmanMenadzer().save();
		
		getSalonMenadzer().save();
		
		//TODO: cjenovnik
	}
	
	public void load() throws IOException{
		getKlijentMenadzer().load();
		getRecepcionerMenadzer().load();
		getMenadzerMenadzer().load();
		
		getKozmetickiTretmanMenadzer().load();
		getKozmeticarMenadzer().load();
		getTipTretmanaMenadzer().load();
		getZakazanTretmanMenadzer().load();
		
		//getSalonMenadzer().load();
	}
	
	private void initializeMenadzers() {
		Settings settings = getSettings();
		if(settings == null) {
			//
		}
		
		ZakazanTretmanMenadzer ztm = new ZakazanTretmanMenadzer();
		TipTretmanaMenadzer ttm = new TipTretmanaMenadzer();
		KozmetickiTretmanMenadzer ktm = new KozmetickiTretmanMenadzer();
		
		KlijentMenadzer km = new KlijentMenadzer(new KlijentProvider(Settings.getKlijentId(), settings.getKlijentFilePath()), ztm);
		KozmeticarMenadzer kzm = new KozmeticarMenadzer(new KozmeticarProvider(Settings.getKozmeticarId(), settings.getKozmeticarFilePath()), ktm, ztm);
		RecepcionerMenadzer rm = new RecepcionerMenadzer(new RecepcionerProvider(Settings.getRecepcionerId(), settings.getRecepcionerFilePath()), ztm);
		MenadzerMenadzer mm = new MenadzerMenadzer(new MenadzerProvider(Settings.getMenadzerId(), settings.getMenadzerFilePath()), ztm);
		
		ttm.setKozmetickiTretmanMenadzer(ktm);
		ttm.setMainProvider(new TipTretmanaProvider(Settings.getTipTretmanaId(), settings.getTipKozmetickogTretmanaFilePath()));
		
		ktm.setKozmeticarMenadzer(kzm);
		ktm.setTipTretmanaMenadzer(ttm);
		ktm.setMainProvider(new KozmetickiTretmanProvider(Settings.getKozmetickiTretmanId(), settings.getKozmetickiTretmanFilePath()));
		
		ztm.setKlijentMenadzer(km);
		ztm.setKozmeticarMenadzer(kzm);
		ztm.setMenadzerMenadzer(mm);
		ztm.setRecepcionerMenadzer(rm);
		ztm.setTipTretmanaMenadzer(ttm);
		ztm.setMainProvider(new ZakazanTretmanProvider(Settings.getZakazanTretmanId(), settings.getZakazanTretmanFilePath()));
		
		this.klijentMenadzer = km;
		this.kozmeticarMenadzer = kzm;
		this.recepcionerMenadzer = rm;
		this.menadzerMenadzer = mm;
		
		this.zakazanTretmanMenadzer = ztm;
		this.kozmetickiTretmanMenadzer = ktm;
		this.tipTretmanaMenadzer = ttm;
		
		this.salonMenadzer = new SalonMenadzer(new SalonProvider(settings.getSalonFilePath(), settings.getCriteriaFilePath()));
	}
	
	
	
	
	
	
	public Settings getSettings() {
		return settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings; //TODO: recheck menadzers
		initializeMenadzers(); //temporary
	}

	public KlijentMenadzer getKlijentMenadzer() {
		return klijentMenadzer;
	}

	public void setKlijentMenadzer(KlijentMenadzer klijentMenadzer) {
		this.klijentMenadzer = klijentMenadzer;
		getZakazanTretmanMenadzer().setKlijentMenadzer(klijentMenadzer);
	}

	public KozmeticarMenadzer getKozmeticarMenadzer() {
		return kozmeticarMenadzer;
	}

	public void setKozmeticarMenadzer(KozmeticarMenadzer kozmeticarMenadzer) {
		this.kozmeticarMenadzer = kozmeticarMenadzer;
		getZakazanTretmanMenadzer().setKozmeticarMenadzer(kozmeticarMenadzer);
		getKozmetickiTretmanMenadzer().setKozmeticarMenadzer(kozmeticarMenadzer);
	}

	public RecepcionerMenadzer getRecepcionerMenadzer() {
		return recepcionerMenadzer;
	}

	public void setRecepcionerMenadzer(RecepcionerMenadzer recepcionerMenadzer) {
		this.recepcionerMenadzer = recepcionerMenadzer;
		getZakazanTretmanMenadzer().setRecepcionerMenadzer(recepcionerMenadzer);
	}

	public MenadzerMenadzer getMenadzerMenadzer() {
		return menadzerMenadzer;
	}

	public void setMenadzerMenadzer(MenadzerMenadzer menadzerMenadzer) {
		this.menadzerMenadzer = menadzerMenadzer;
		getZakazanTretmanMenadzer().setMenadzerMenadzer(menadzerMenadzer);
	}

	public KozmetickiTretmanMenadzer getKozmetickiTretmanMenadzer() {
		return kozmetickiTretmanMenadzer;
	}

	public void setKozmetickiTretmanMenadzer(KozmetickiTretmanMenadzer kozmetickiTretmanMenadzer) {
		this.kozmetickiTretmanMenadzer = kozmetickiTretmanMenadzer;
		getKozmeticarMenadzer().setKozmetickiTretmanMenadzer(kozmetickiTretmanMenadzer);
		getTipTretmanaMenadzer().setKozmetickiTretmanMenadzer(kozmetickiTretmanMenadzer);
	}

	public TipTretmanaMenadzer getTipTretmanaMenadzer() {
		return tipTretmanaMenadzer;
	}

	public void setTipTretmanaMenadzer(TipTretmanaMenadzer tipTretmanaMenadzer) {
		this.tipTretmanaMenadzer = tipTretmanaMenadzer;
		getKozmetickiTretmanMenadzer().setTipTretmanaMenadzer(tipTretmanaMenadzer);
	}

	public ZakazanTretmanMenadzer getZakazanTretmanMenadzer() {
		return zakazanTretmanMenadzer;
	}

	public void setZakazanTretmanMenadzer(ZakazanTretmanMenadzer zakazanTretmanMenadzer) {
		this.zakazanTretmanMenadzer = zakazanTretmanMenadzer;
		getKlijentMenadzer().setZakazanTretmanMenadzer(zakazanTretmanMenadzer);
		getKozmeticarMenadzer().setZakazanTretmanMenadzer(zakazanTretmanMenadzer);
		getRecepcionerMenadzer().setZakazanTretmanMenadzer(zakazanTretmanMenadzer);
		getMenadzerMenadzer().setZakazanTretmanMenadzer(zakazanTretmanMenadzer);
	}
	
	public SalonMenadzer getSalonMenadzer() {
		return salonMenadzer;
	}

	public void setSalonMenadzer(SalonMenadzer salonMenadzer) {
		this.salonMenadzer = salonMenadzer;
	}
	
	
	
	public static void main(String[] args) {
		ArrayList<String[]> strings = new ArrayList<>();
		
		
		String[] k = KozmeticarProvider.TO_CSV.convert(
				new Kozmeticar(
						
				)
	       );
		
		strings.add(k);
	}
}
