package dataProvajderi;

import java.util.ArrayList;
import java.util.function.Function;

import entiteti.Menadzer;
import entiteti.NivoStrucneSpreme;
import entiteti.Pol;
import helpers.Converter;

public class MenadzerProvider extends DataProvider<Menadzer, String> {
	
	public static final Menadzer DELETED = new Menadzer() {
		@Override
		public void setGodineStaza(int godineStaza) {}
		@Override
		public void setBazaPlate(double bazaPlate) {}
		@Override
		public void setBonus(boolean bonus) {}
		@Override
		public void setNivoStrucneSpreme(NivoStrucneSpreme nivoStrucneSpreme) {}
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
		public double izracunajPlatu() {return 0;}
		@Override
		public boolean equals(Object obj) {
			return (this == obj);
		}
	};
	
	
	public static final Converter<Menadzer, String[]> TO_CSV = menadzer -> {
		String[] m = new String[11];
		
		m[0] = menadzer.getIme();
		m[1] = menadzer.getPrezime();
		m[2] = menadzer.getBrojTelefona();
		m[3] = menadzer.getAdresa();
	    m[4] = menadzer.getKorisnickoIme();
	    m[5] = menadzer.getLozinka();
	    
	    m[6] = menadzer.getPol().name();		    
	    m[7] = Integer.toString( menadzer.getGodineStaza() );
	    m[8] = Double.toString( menadzer.getBazaPlate() );
	    m[9] = Boolean.toString(menadzer.hasBonus());
	    m[10] = menadzer.getNivoStrucneSpreme().name();
	    return m;
	};
	
	public static final Converter<String[], Menadzer> FROM_CSV = m -> {
		Menadzer menadzer = new Menadzer();
		
        menadzer.setIme(m[0]);
        menadzer.setPrezime(m[1]);
        menadzer.setBrojTelefona(m[2]);
        menadzer.setAdresa(m[3]);
        menadzer.setKorisnickoIme(m[4]);
        menadzer.setLozinka(m[5]);
        
        menadzer.setPol(Pol.valueOf(m[6]));
        menadzer.setGodineStaza(Integer.parseInt(m[7]));
        menadzer.setBazaPlate(Double.parseDouble(m[8]));
        menadzer.setBonus(Boolean.parseBoolean(m[9]));
        menadzer.setNivoStrucneSpreme(NivoStrucneSpreme.valueOf(m[10]));
	    return menadzer;
	};

	
	
	public MenadzerProvider() {
		super();
	}
	
	public MenadzerProvider(Function<Menadzer, String> idFunction, String filePath) {
		super(idFunction, filePath);
	}
	

	@Override
	public Menadzer getDeletedInstance() { return DELETED; }


	@Override
	protected Data<String, Menadzer> convertStringToData(ArrayList<String[]> stringData) {
		ArrayList<Menadzer> convertedData = new ArrayList<>();
		
		stringData.forEach( (m) -> {
			convertedData.add(FROM_CSV.convert(m));
		});
		
		return new Data<>(convertedData);
	}


	@Override
	protected ArrayList<String[]> convertDataToString(Data<String, Menadzer> data) {
		ArrayList<String[]> convertedData = new ArrayList<>();
		
		data.list().forEach( (menadzer) -> {
			convertedData.add(TO_CSV.convert(menadzer));
		});
		
		return convertedData;
	}

	


	

}
