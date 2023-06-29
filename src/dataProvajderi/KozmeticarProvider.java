package dataProvajderi;

import java.util.ArrayList;

import entiteti.Kozmeticar;
import entiteti.KozmetickiTretman;
import entiteti.NivoStrucneSpreme;
import entiteti.Pol;
import helpers.DefaultDict;

public class KozmeticarProvider extends ProviderExtrovert<Kozmeticar> {
	
	private final Kozmeticar deleted = new Kozmeticar() {
		@Override
		public void setGodineStaza(int godineStaza) {}
		@Override
		public void setBazaPlate(double bazaPlate) {}
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
		public double izracunajBazuPlate() {return 0;}
		@Override
		public void setTretmani(ArrayList<KozmetickiTretman> t) {}
	};

	@Override
	public Kozmeticar getDeletedInstance() { return this.deleted; }

	
	@Override
	protected ArrayList<String[]> convertDataToString(ArrayList<Kozmeticar> data) {  //doesn't take care of an accidental deleted treatment appereance
		
		ArrayList<String[]> convertedData = new ArrayList<>();
		
		Provider<KozmetickiTretman> tretmaniProvider = super.getMainProvider().getKozmetickiTretmanProvider();
		StringBuilder sb = new StringBuilder();
		
		data.forEach( (kozmeticar) -> {
			String[] k = new String[11];
			convertedData.add(k);
			
			k[0] = kozmeticar.getIme();
			k[1] = kozmeticar.getPrezime();
			k[2] = kozmeticar.getBrojTelefona();
			k[3] = kozmeticar.getAdresa();
		    k[4] = kozmeticar.getKorisnickoIme();
		    k[5] = kozmeticar.getLozinka();
		    k[6] = kozmeticar.getPol().name();
		    
		    k[7] = Integer.toString( kozmeticar.getGodineStaza() );
		    k[8] = Double.toString( kozmeticar.getBazaPlate() );
		    k[9] = kozmeticar.getNivoStrucneSpreme().name();
		    
		    for(KozmetickiTretman kt: kozmeticar.getTretmani()) {
		    	sb.append( tretmaniProvider.getId(kt) ).append("|");
		    }
		    
		    k[10] = sb.toString();
		    sb.setLength(0);
		});
		
		return convertedData;
		
	}

	@Override
	protected ArrayList<Kozmeticar> convertStringToData(ArrayList<String[]> data) { //moraju se prvo ucitati kozmeticki tretmani ili ce svaki kozmeticar biti instanciran sa praznom listom tretmana
		
		ArrayList<Kozmeticar> convertedData = new ArrayList<>();
		
		Provider<KozmetickiTretman> tretmaniProvider = super.getMainProvider().getKozmetickiTretmanProvider();
		DefaultDict<String, KozmetickiTretman> tretmaniIds = tretmaniProvider.getIds();
		KozmetickiTretman deleted = tretmaniProvider.getDeletedInstance();
		
		data.forEach( (k) -> {
			Kozmeticar kozmeticar = new Kozmeticar();
			convertedData.add(kozmeticar);
			
			kozmeticar.setIme(k[0]);
	        kozmeticar.setPrezime(k[1]);
	        kozmeticar.setBrojTelefona(k[2]);
	        kozmeticar.setAdresa(k[3]);
	        kozmeticar.setKorisnickoIme(k[4]);
	        kozmeticar.setLozinka(k[5]);
	        kozmeticar.setPol(Pol.valueOf(k[6]));
	        kozmeticar.setGodineStaza(Integer.parseInt(k[7]));
	        kozmeticar.setBazaPlate(Double.parseDouble(k[8]));
	        kozmeticar.setNivoStrucneSpreme(NivoStrucneSpreme.valueOf(k[9]));		
	        
	        ArrayList<KozmetickiTretman> tretmani = new ArrayList<>();
	        kozmeticar.setTretmani(tretmani);
	        
	        if(k[10].isEmpty()) {
	        	return;
	        }
	        
	        String[] tretmaniStr = k[10].split("\\|");
	        
	        for(String id : tretmaniStr) {
	        	KozmetickiTretman tretman = tretmaniIds.get(id);
	        	
	        	if(tretman != deleted) {
	        		tretmani.add(tretman);
	        	}
	        }
			
		});
		
		return convertedData;
			
	}

	

	

}
