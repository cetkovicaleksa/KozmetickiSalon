package helpers;

import java.util.function.Function;

import entiteti.Klijent;
import entiteti.Korisnik;
import entiteti.Kozmeticar;
import entiteti.KozmetickiTretman;
import entiteti.Menadzer;
import entiteti.Recepcioner;
import entiteti.ZakazanTretman;
import dataProvajderi.DataProvider;
import dataProvajderi.KozmeticarProvider;

import dataProvajderi.KlijentProvider;
import dataProvajderi.KozmeticarProvider;
import dataProvajderi.RecepcionerProvider;
import dataProvajderi.MenadzerProvider;
import dataProvajderi.KozmetickiTretmanProvider;
import dataProvajderi.TipTretmanaProvider;
import dataProvajderi.ZakazanTretmanProvider;

@SuppressWarnings("unused")
public class Settings {
	
	private String klijentFilePath, kozmeticarFilePath, menadzerFilePath, 
	recepcionerFilePath, kozmetickiTretmanFilePath, tipKozmetickogTretmanaFilePath,
	zakazanTretmanFilePath, salonFilePath, criteriaFilePath;
	
	private Function<Klijent, String> klijentIdFunction;
	private Function<Kozmeticar, String> kozmeticarIdFunction;
	private Function<Recepcioner, String> recepcionerIdFunction;
	private Function<Menadzer, String> menadzerIdFunction;
	
	private Function<KozmetickiTretman, String> kozmetickiTretmanIdFunction;
	private Function<KozmetickiTretman.TipTretmana, String> tipKozmetickogTretmanaIdFunction;
	private Function<ZakazanTretman, String> zakazanTretmanIdFunction;
	
	
	//constants
	private static final String KLIJENTI = "resources/klijenti.csv";
	private static final String KOZMETICARI = "resources/kozmeticari.csv";
	private static final String RECEPCIONERI = "resources/recepcioneri.csv";
	private static final String MENADZERI = "resources/menadzeri.csv";
	
	private static final String KOZMETICKI_TRETMAN = "resources/kozmeticki_tretmani.csv";
	private static final String TIPOVI_TRETMANA = "resources/tipovi_tretmana.csv";
	private static final String ZAKAZANI_TRETMANI = "resources/zakazani_tretmani.csv";
	
	private static final String CJENOVNIK = "resources/cjenovnik.csv";
	private static final String SALON = "resources/salon.csv";
	private static final String CRITERIA = "resources/salon_employee_bonus_criteria.csv";
	
	private static final Function<Klijent, String> KLIJENT_ID = klijent -> (KlijentProvider.DELETED.equals(klijent)) ? DataProvider.DELETED_ID : klijent.getKorisnickoIme() ;
	public static final Function<Kozmeticar, String> KOZMETICAR_ID = kozmeticar -> (KozmeticarProvider.DELETED.equals(kozmeticar)) ? DataProvider.DELETED_ID : kozmeticar.getKorisnickoIme();
	private static final Function<Recepcioner, String> RECEPCIONER_ID = recepcioner -> (RecepcionerProvider.DELETED.equals(recepcioner)) ? DataProvider.DELETED_ID : recepcioner.getKorisnickoIme();
	private static final Function<Menadzer, String> MENADZER_ID = menadzer -> (MenadzerProvider.DELETED.equals(menadzer)) ? DataProvider.DELETED_ID : menadzer.getKorisnickoIme();
	
	public static final Function<KozmetickiTretman, String> KOZMETICKI_TRETMAN_ID = kozmetickiTretman -> (KozmetickiTretmanProvider.DELETED.equals(kozmetickiTretman)) ? DataProvider.DELETED_ID : kozmetickiTretman.getNaziv();
	private static final Function<KozmetickiTretman.TipTretmana, String> TIP_TRETMANA_ID = tipTretmana -> (TipTretmanaProvider.DELETED.equals(tipTretmana)) ? DataProvider.DELETED_ID : tipTretmana.getNaziv() + DataProvider.ID_DELIMITER + KOZMETICKI_TRETMAN_ID.apply(tipTretmana.getTretman());
	private static final Function<ZakazanTretman, String> ZAKAZAN_TRETMAN_ID = zakazanTretman -> {
		if(ZakazanTretmanProvider.DELETED.equals(zakazanTretman)) {
			return DataProvider.DELETED_ID;
		}
		
		return Float.toString(zakazanTretman.getDatum().toEpochDay()) + 
				DataProvider.ID_DELIMITER + 
				zakazanTretman.getVrijeme().toSecondOfDay() + DataProvider.ID_DELIMITER + KOZMETICAR_ID.apply(zakazanTretman.getKozmeticar());
	};

	
	
	public Settings() {}
	
	public static Settings getDefaultSettings() {
		Settings settings = new Settings();
		settings.setKlijentFilePath(KLIJENTI);
		settings.setKozmeticarFilePath(KOZMETICARI);
		settings.setRecepcionerFilePath(RECEPCIONERI);
		settings.setMenadzerFilePath(MENADZERI);
		settings.setKozmetickiTretmanFilePath(KOZMETICKI_TRETMAN);
		settings.setTipKozmetickogTretmanaFilePath(TIPOVI_TRETMANA);
		settings.setZakazanTretmanFilePath(ZAKAZANI_TRETMANI);
		settings.setSalonFilePath(SALON);
		settings.setCriteriaFilePath(CRITERIA);
		
		
		return settings;
	}

	
	
	
	public String getKlijentFilePath() {
		return klijentFilePath;
	}

	public void setKlijentFilePath(String klijentFilePath) {
		this.klijentFilePath = klijentFilePath;
	}

	public String getKozmeticarFilePath() {
		return kozmeticarFilePath;
	}

	public void setKozmeticarFilePath(String kozmeticarFilePath) {
		this.kozmeticarFilePath = kozmeticarFilePath;
	}

	public String getMenadzerFilePath() {
		return menadzerFilePath;
	}

	public void setMenadzerFilePath(String menadzerFilePath) {
		this.menadzerFilePath = menadzerFilePath;
	}

	public String getRecepcionerFilePath() {
		return recepcionerFilePath;
	}

	public void setRecepcionerFilePath(String recepcionerFilePath) {
		this.recepcionerFilePath = recepcionerFilePath;
	}

	public String getKozmetickiTretmanFilePath() {
		return kozmetickiTretmanFilePath;
	}

	public void setKozmetickiTretmanFilePath(String kozmetickiTretmanFilePath) {
		this.kozmetickiTretmanFilePath = kozmetickiTretmanFilePath;
	}

	public String getTipKozmetickogTretmanaFilePath() {
		return tipKozmetickogTretmanaFilePath;
	}

	public void setTipKozmetickogTretmanaFilePath(String tipKozmetickogTretmanaFilePath) {
		this.tipKozmetickogTretmanaFilePath = tipKozmetickogTretmanaFilePath;
	}

	public String getZakazanTretmanFilePath() {
		return zakazanTretmanFilePath;
	}

	public void setZakazanTretmanFilePath(String zakazanTretmanFilePath) {
		this.zakazanTretmanFilePath = zakazanTretmanFilePath;
	}
	
	public String getSalonFilePath() {
		return this.salonFilePath;
	}
	
	public void setSalonFilePath(String salonFilePath) {
		this.salonFilePath = salonFilePath;
	}
	
	public String getCriteriaFilePath() {
		return criteriaFilePath;
	}
	
	public void setCriteriaFilePath(String criteriaFilePath) {
		this.criteriaFilePath = criteriaFilePath;
	}

	
	
	public static Function<Klijent, String> getKlijentId() {
		return KLIJENT_ID;
	}

	public static Function<Kozmeticar, String> getKozmeticarId() {
		return KOZMETICAR_ID;
	}

	public static Function<Recepcioner, String> getRecepcionerId() {
		return RECEPCIONER_ID;
	}

	public static Function<Menadzer, String> getMenadzerId() {
		return MENADZER_ID;
	}

	public static Function<KozmetickiTretman, String> getKozmetickiTretmanId() {
		return KOZMETICKI_TRETMAN_ID;
	}

	public static Function<KozmetickiTretman.TipTretmana, String> getTipTretmanaId() {
		return TIP_TRETMANA_ID;
	}

	public static Function<ZakazanTretman, String> getZakazanTretmanId() {
		return ZAKAZAN_TRETMAN_ID;
	}	
	
	
	

}
