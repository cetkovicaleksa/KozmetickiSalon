package dataProvajderi;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.function.Function;

import entiteti.Korisnik;
import entiteti.Kozmeticar;
import entiteti.KozmetickiTretman;
import entiteti.KozmetickiTretman.TipTretmana;
import entiteti.Pol;
import entiteti.StatusTretmana;
import entiteti.ZakazanTretman;
import helpers.Converter;
import helpers.DefaultDict;

public class ZakazanTretmanProvider extends XDataProvider<ZakazanTretman, String>{
	
	public static final DateTimeFormatter FORMATER_DATUMA = java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
	public static final ZakazanTretman DELETED = new ZakazanTretman() {
		{
			super.setTipTretmana(TipTretmanaProvider.DELETED);
			super.setKozmeticar(KozmeticarProvider.DELETED);
			super.setKlijent(KlijentProvider.DELETED);
		}
		
		@Override
		public void setTipTretmana(TipTretmana tipTretmana) {}
		@Override
		public void setKozmeticar(Kozmeticar kozmeticar) {}
		@Override
		public void setKlijent(Korisnik klijent) {}
		@Override
		public void setCijena(double cijena) {}
		@Override
		public void setTrajanje(int trajanje) {}
		@Override
		public void setDatum(LocalDate datum) {}
		@Override
		public void setVrijeme(LocalTime vrijeme) {}
		@Override
		public void setStatus(StatusTretmana status) {}
		@Override
		public boolean equals(Object obj) {
			return (this == obj);
		}
	};
	
	public static final Korisnik DELETED_KORISNIK = new Korisnik() {
		@Override
		public void setIme(String ime) {}
		@Override
		public void setPrezime(String prezime) {}
		@Override
		public void setBrojTelefona(String brojTelefona) {}
		@Override
		public void setAdresa(String adresa) {}
		@Override
		public void setKorisnickoIme(String korisnickoIme) {}
		@Override
		public void setLozinka(String lozinka) {}
		@Override
		public void setPol(Pol pol) {}
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
			return (korisnik == KlijentProvider.DELETED || 
					korisnik == KozmeticarProvider.DELETED || 
					korisnik == RecepcionerProvider.DELETED || 
					korisnik == MenadzerProvider.DELETED);	
		}
		
	};
	
	
	public static final Converter<ZakazanTretman, String[]> TO_CSV = zakazanTretman -> {
		String[] zt = new String[7];
		
		zt[3] = Double.toString(zakazanTretman.getCijena());
		zt[4] = Integer.toString(zakazanTretman.getTrajanje());
		zt[5] = zakazanTretman.getDatum().format(FORMATER_DATUMA);
		zt[6] = zakazanTretman.getStatus().name();
	    return zt;
	};
	
	public static final Converter<String[], ZakazanTretman> FROM_CSV = zt -> {
		ZakazanTretman zakazanTretman = new ZakazanTretman();
		
		zakazanTretman.setCijena(Float.parseFloat(zt[3]));
		zakazanTretman.setTrajanje(Integer.parseInt(zt[4]));
		zakazanTretman.setDatum(LocalDate.parse(zt[5], FORMATER_DATUMA));
		zakazanTretman.setStatus(StatusTretmana.valueOf(zt[6]));
	    return zakazanTretman;
	};
	
	

	@Override
	public ZakazanTretman getDeletedInstance() { return DELETED; }
	
	
	public void loadData(
			DefaultDict<String, Korisnik> klijentiIds, DefaultDict<String, Kozmeticar> kozmeticariIds,
			DefaultDict<String, KozmetickiTretman.TipTretmana> tipoviTretmanaIds) throws IOException
	{
		ArrayList<String[]> loadedData = DataProvider.loadFromCsv(super.getFilePath(), DataProvider.CSV_DELIMITER);
		ArrayList<ZakazanTretman> initializedData = new ArrayList<>();
		super.setData(new Data<>(initializedData));
		
		loadedData.forEach(zt -> {
			ZakazanTretman zakazanTretman = FROM_CSV.convert(zt);
			initializedData.add(zakazanTretman);
			
			zakazanTretman.setTipTretmana(tipoviTretmanaIds.get(zt[0])); //may be a deleted TipTretmana or Kozmeticar or Korisnik
			zakazanTretman.setKozmeticar(kozmeticariIds.get(zt[1]));
			zakazanTretman.setKlijent(klijentiIds.get(zt[2]));			
		});			
	}
	
	
	public void saveData(
			Function<Korisnik, String> getKorisnikId, Function<Kozmeticar, String> getKozmeticarId,
			Function<KozmetickiTretman.TipTretmana, String> getTipTretmanaId) throws IOException 
	{
		ArrayList<String[]> convertedData = new ArrayList<>();
		
		super.getData().list().forEach(zakazanTretman -> {
			String[] zt = TO_CSV.convert(zakazanTretman);	
			convertedData.add(zt);
			
			zt[0] = getTipTretmanaId.apply(zakazanTretman.getTipTretmana());
			zt[1] = getKozmeticarId.apply(zakazanTretman.getKozmeticar());
			zt[2] = getKorisnikId.apply(zakazanTretman.getKlijent());
		});
		
		DataProvider.writeToCsv(convertedData, super.getFilePath(), DataProvider.CSV_DELIMITER);
	}

}
