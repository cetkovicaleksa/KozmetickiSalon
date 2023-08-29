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
import entiteti.Korisnik;
import entiteti.Kozmeticar;
import entiteti.KozmetickiTretman;
import entiteti.StatusTretmana;
import entiteti.ZakazanTretman;
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
	
	
	public Collection<Collection<KozmetickiTretman.TipTretmana>> getTretmaniSelection(){
		return getTipTretmanaMenadzer().getTretmaniSelection();
	}
	
	
	public Collection<Kozmeticar> getKozmeticariThatCanPreformTreatment(KozmetickiTretman tretman) {
		return getKozmeticarMenadzer().allKozmeticariThatCanPreformTreatment(tretman);
	}
	
	public SortedSet<Integer> getKozmeticarFreeHours(Kozmeticar kozmeticar, LocalDate datum){
		//TODO: finish
		SortedSet<Integer> freeHours = new TreeSet<>();
		
		
		return freeHours;
	}
	
	public void zakaziTretman(TipTretmana tipTretmana, Kozmeticar kozmeticar, Korisnik klijent, LocalDate datum,
					LocalTime vrijeme) {
		getZakazanTretmanMenadzer().create(
				new ZakazanTretman(tipTretmana, kozmeticar, klijent, datum, vrijeme)
		);				
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
		getKozmeticarMenadzer().load();
		getRecepcionerMenadzer().load();
		getMenadzerMenadzer().load();
		
		getKozmetickiTretmanMenadzer().load();
		getTipTretmanaMenadzer().load();
		getZakazanTretmanMenadzer().load();
		
		getSalonMenadzer().load();
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
		
		this.salonMenadzer = new SalonMenadzer(new SalonProvider(settings.getSalonFilePath()));
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
}
