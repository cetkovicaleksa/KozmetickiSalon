package dataProvajderi;

import java.util.ArrayList;
import java.util.function.Function;

import entiteti.NivoStrucneSpreme;
import entiteti.Pol;
import entiteti.Recepcioner;
import helpers.Converter;

public class RecepcionerProvider extends DataProvider<Recepcioner, String> {
	
	public static final Recepcioner DELETED = new Recepcioner() {
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
	
	
	public static final Converter<Recepcioner, String[]> TO_CSV = recepcioner -> {
		String[] r = new String[11];
        
        r[0] = recepcioner.getIme();
        r[1] = recepcioner.getPrezime();
        r[2] = recepcioner.getBrojTelefona();
        r[3] = recepcioner.getAdresa();
        r[4] = recepcioner.getKorisnickoIme();
        r[5] = recepcioner.getLozinka();
        
        r[6] = recepcioner.getPol().name();
        r[7] = Integer.toString(recepcioner.getGodineStaza());
        r[8] = Double.toString(recepcioner.getBazaPlate());
        r[9] = Boolean.toString(recepcioner.hasBonus());
        r[10] = recepcioner.getNivoStrucneSpreme().name();
	    return r;
	};
	
	public static final Converter<String[], Recepcioner> FROM_CSV = r -> {
		Recepcioner recepcioner = new Recepcioner();
        
        recepcioner.setIme(r[0]);
        recepcioner.setPrezime(r[1]);
        recepcioner.setBrojTelefona(r[2]);
        recepcioner.setAdresa(r[3]);
        recepcioner.setKorisnickoIme(r[4]);
        recepcioner.setLozinka(r[5]);
        
        recepcioner.setPol(Pol.valueOf(r[6]));
        recepcioner.setGodineStaza(Integer.parseInt(r[7]));
        recepcioner.setBazaPlate(Double.parseDouble(r[8]));
        recepcioner.setBonus(Boolean.parseBoolean(r[9]));
        recepcioner.setNivoStrucneSpreme(NivoStrucneSpreme.valueOf(r[10]));
	    return recepcioner;
	};

	
	
	public RecepcionerProvider() {
		super();
	}
	
	public RecepcionerProvider(Function<Recepcioner, String> idFunction, String filePath) {
		super(idFunction, filePath);
	}
	
	
	
	@Override
	public Recepcioner getDeletedInstance() { return DELETED; }


	@Override
	protected Data<String, Recepcioner> convertStringToData(ArrayList<String[]> stringData) {
		ArrayList<Recepcioner> convertedData = new ArrayList<>();
				
		stringData.forEach((r) -> {
	        convertedData.add(FROM_CSV.convert(r));
	    });
		
		return new Data<>(convertedData);
	}


	@Override
	protected ArrayList<String[]> convertDataToString(Data<String, Recepcioner> data) {
		ArrayList<String[]> convertedData = new ArrayList<>();
		
		data.list().forEach((recepcioner) -> {
	        convertedData.add(TO_CSV.convert(recepcioner));
	    });
		
		return convertedData;
	}


}
