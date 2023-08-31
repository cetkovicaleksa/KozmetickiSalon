package dataProvajderi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Function;

import entiteti.Kozmeticar;
import entiteti.KozmetickiTretman;
import entiteti.NivoStrucneSpreme;
import entiteti.Pol;
import helpers.Converter;
import helpers.DefaultDict;

public class KozmeticarProvider extends XDataProvider<Kozmeticar, String> {
	
	//private String msg = "Hmm vidi kako rjesiti ovo sa komunikacijom providera!!!!";
	
	public static final Kozmeticar DELETED = new Kozmeticar() {
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
		public void setTretmani(ArrayList<KozmetickiTretman> t) {}
		@Override
		public boolean equals(Object obj) {
			return (this == obj);
		}
	};
	
	private static final String NO_TRETMANI = "NEMA_TRETMANA";
	
	public static final Converter<Kozmeticar, String[]> TO_CSV = kozmeticar -> {
		String[] k = new String[12];
		
		k[0] = kozmeticar.getIme();
		k[1] = kozmeticar.getPrezime();
		k[2] = kozmeticar.getBrojTelefona();
		k[3] = kozmeticar.getAdresa();
	    k[4] = kozmeticar.getKorisnickoIme();
	    k[5] = kozmeticar.getLozinka();
	    k[6] = kozmeticar.getPol().name();
	    
	    k[7] = Integer.toString(kozmeticar.getGodineStaza());
	    k[8] = Double.toString(kozmeticar.getBazaPlate());
	    k[9] = Boolean.toString(kozmeticar.hasBonus());
	    k[10] = kozmeticar.getNivoStrucneSpreme().name();
	    return k;
	};
	
	public static final Converter<String[], Kozmeticar> FROM_CSV = k -> {
		Kozmeticar kozmeticar = new Kozmeticar();
		
		kozmeticar.setIme(k[0]);
        kozmeticar.setPrezime(k[1]);
        kozmeticar.setBrojTelefona(k[2]);
        kozmeticar.setAdresa(k[3]);
        kozmeticar.setKorisnickoIme(k[4]);
        kozmeticar.setLozinka(k[5]);
        
        kozmeticar.setPol(Pol.valueOf(k[6]));
        kozmeticar.setGodineStaza(Integer.parseInt(k[7]));
        kozmeticar.setBazaPlate(Double.parseDouble(k[8]));
        kozmeticar.setBonus(Boolean.parseBoolean(k[9]));
        kozmeticar.setNivoStrucneSpreme(NivoStrucneSpreme.valueOf(k[10]));
	    return kozmeticar;
	};

	

	public KozmeticarProvider() {
		super();
	}
	
	public KozmeticarProvider(Function<Kozmeticar, String> idFunction, String filePath) {
		super(idFunction, filePath);
	}
	
	
	
	@Override
	public Kozmeticar getDeletedInstance() { return DELETED; }
	
	public void loadData(DefaultDict<String, KozmetickiTretman> tretmaniIds) throws IOException {
		ArrayList<String[]> loadedData = DataProvider.loadFromCsv(super.getFilePath(), DataProvider.CSV_DELIMITER);
		ArrayList<Kozmeticar> initializedData = new ArrayList<>();
		KozmetickiTretman deleted = tretmaniIds.getDefaultValue();
		super.setData(new Data<>(initializedData));
		
		loadedData.forEach(k -> {
			Kozmeticar kozmeticar = FROM_CSV.convert(k);
			initializedData.add(kozmeticar);
				        
	        ArrayList<KozmetickiTretman> tretmani = new ArrayList<>();
	        kozmeticar.setTretmani(tretmani);
	        
	        if(k.length < 12 || k[11].isEmpty()) { //don't like this
	        	return;
	        }
	        
	        String[] tretmaniStr = k[11].split(DataProvider.CSV_INNER_DELIMITER);
	        
	        for(String id : tretmaniStr) {
	        	KozmetickiTretman tretman = tretmaniIds.get(id);
	        	
	        	if( !deleted.equals(tretman) ) {
	        		tretmani.add(tretman);
	        	}
	        }
	        //TODO: finish
		});
		
	}
	
	public void saveData(Function<KozmetickiTretman, String> getTretmanId) throws IOException {
		ArrayList<String[]> convertedData = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		
		super.getData().list().forEach(kozmeticar -> {
			String[] k = TO_CSV.convert(kozmeticar);
			convertedData.add(k);
		    
		    ArrayList<KozmetickiTretman> tretmani = kozmeticar.getTretmani();
		    int size = tretmani.size();
		    
		    for(int i = 0; i < size; i++) {  
		    	String id = getTretmanId.apply(tretmani.get(i));
		    	
		    	if( !DataProvider.DELETED_ID.equals(id) ) {
		    		sb.append(id);
		    		if(i < size - 1){ //if we are at the last treatment we don't add the delimiter
		    			sb.append(DataProvider.CSV_INNER_DELIMITER);
		    		}
		    	}
		    }
		    
		    k[11] = sb.toString(); //TODO: check if empty sb returns an empty string, and recheck this method
		    sb.setLength(0);
		});
		
		DataProvider.writeToCsv(convertedData, super.getFilePath(), DataProvider.CSV_DELIMITER);
	}

	
	


}
