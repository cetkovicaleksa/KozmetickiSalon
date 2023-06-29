package dataProvajderi;

import java.util.ArrayList;

import entiteti.Menadzer;
import entiteti.NivoStrucneSpreme;
import entiteti.Pol;

public class MenadzerProvider extends Provider<Menadzer> {
	
	private Menadzer deleted = new Menadzer() {
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
	};
	
	
	@Override
	public Menadzer getDeletedInstance() { return this.deleted; }
	
	@Override
	protected ArrayList<String[]> convertDataToString(ArrayList<Menadzer> data) {
		ArrayList<String[]> convertedData = new ArrayList<>();		
		
		data.forEach( (menadzer) -> {
			String[] m = new String[10];
			convertedData.add(m);
			
			m[0] = menadzer.getIme();
			m[1] = menadzer.getPrezime();
			m[2] = menadzer.getBrojTelefona();
			m[3] = menadzer.getAdresa();
		    m[4] = menadzer.getKorisnickoIme();
		    m[5] = menadzer.getLozinka();
		    m[6] = menadzer.getPol().name();
		    
		    m[7] = Integer.toString( menadzer.getGodineStaza() );
		    m[8] = Double.toString( menadzer.getBazaPlate() );
		    m[9] = menadzer.getNivoStrucneSpreme().name();
		});
		
		return convertedData;
	}
	
	@Override
	protected ArrayList<Menadzer> convertStringToData(ArrayList<String[]> data) {
		ArrayList<Menadzer> convertedData = new ArrayList<>();
		
		data.forEach( (m) -> {
			Menadzer menadzer = new Menadzer();
			convertedData.add(menadzer);
			
	        menadzer.setIme(m[0]);
	        menadzer.setPrezime(m[1]);
	        menadzer.setBrojTelefona(m[2]);
	        menadzer.setAdresa(m[3]);
	        menadzer.setKorisnickoIme(m[4]);
	        menadzer.setLozinka(m[5]);
	        menadzer.setPol(Pol.valueOf(m[6]));
	        menadzer.setGodineStaza(Integer.parseInt(m[7]));
	        menadzer.setBazaPlate(Double.parseDouble(m[8]));
	        menadzer.setNivoStrucneSpreme(NivoStrucneSpreme.valueOf(m[9]));
		});
		
		return convertedData;
	}

	


	

}
