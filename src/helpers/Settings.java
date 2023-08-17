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

@SuppressWarnings("unused")
public class Settings {
	
	private String klijentFilePath, kozmeticarFilePath, menadzerFilePath, 
	recepcionerFilePath, kozmetickiTretmanFilePath, tipKozmetickogTretmanaFilePath,
	zakazanTretmanFilePath;
	
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
	
	private static final Function<Klijent, String> KLIJENT_ID = klijent -> klijent.getKorisnickoIme() ;
	public static final Function<Kozmeticar, String> KOZMETICAR_ID = kozmeticar -> kozmeticar.getKorisnickoIme();
	private static final Function<Recepcioner, String> RECEPCIONER_ID = recepcioner -> recepcioner.getKorisnickoIme();
	private static final Function<Menadzer, String> MENADZER_ID = menadzer -> menadzer.getKorisnickoIme();
	
	public static final Function<KozmetickiTretman, String> KOZMETICKI_TRETMAN_ID = kozmetickiTretman -> kozmetickiTretman.getNaziv();
	private static final Function<KozmetickiTretman.TipTretmana, String> TIP_TRETMANA_ID = tipTretmana -> tipTretmana.getNaziv();
	private static final Function<ZakazanTretman, String> ZAKAZAN_TRETMAN_ID = zakazanTretman -> {
		return Float.toString(zakazanTretman.getDatum().toEpochDay()) + 
				DataProvider.ID_DELIMITER + 
				zakazanTretman.getVrijeme().toSecondOfDay();
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
	

}
