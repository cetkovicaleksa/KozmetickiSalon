package crudMenadzeri;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.function.Predicate;


import dataProvajderi.KlijentProvider;
import dataProvajderi.KozmeticarProvider;
import dataProvajderi.KozmetickiTretmanProvider;
import dataProvajderi.MenadzerProvider;
import dataProvajderi.RecepcionerProvider;
import dataProvajderi.TipTretmanaProvider;
import dataProvajderi.ZakazanTretmanProvider;
import entiteti.Korisnik;
import entiteti.Kozmeticar;
import entiteti.KozmetickiTretman;
import entiteti.StatusTretmana;
import entiteti.ZakazanTretman;
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
	
	
	
	public Map<StatusTretmana, List<ZakazanTretman>> getZakazaniTretmaniKorisnika(Korisnik korisnik, boolean asKlijent, boolean asKozmeticar){
		Map<StatusTretmana, List<ZakazanTretman>> result = new TreeMap<>();
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
		Iterator<KozmetickiTretman.TipTretmana> iter = getTipTretmanaMenadzer().readAll();
		Map<KozmetickiTretman, Collection<KozmetickiTretman.TipTretmana>> map = new HashMap<>();
		
		while(iter.hasNext()) {
			KozmetickiTretman.TipTretmana tipTretmana = iter.next();
			KozmetickiTretman kozmetickiTretman = tipTretmana.getTretman();
			
			map.putIfAbsent(kozmetickiTretman, new ArrayList<>());
			map.get(kozmetickiTretman).add(tipTretmana);
		}
		
		return map.values();
	}
	
	
	public Collection<Kozmeticar> getKozmeticariThatCanPreformTreatment(KozmetickiTretman tretman) {
		return getKozmeticarMenadzer().read(
				new Query<>(kozmeticar -> kozmeticar.getTretmani().contains(tretman)) //TODO: see if the tretmani is always initialized!!
		);
	}
	
	public SortedSet<Integer> getKozmeticarFreeHours(Kozmeticar kozmeticar, LocalDate datum){
		//TODO: finish
		return null;
	}
	

	public void save() throws IOException{
		getKlijentMenadzer().save();
		getKozmeticarMenadzer().save();
		getRecepcionerMenadzer().save();
		getMenadzerMenadzer().save();
		
		getKozmetickiTretmanMenadzer().save();
		getTipTretmanaMenadzer().save();
		getZakazanTretmanMenadzer().save();
		
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
