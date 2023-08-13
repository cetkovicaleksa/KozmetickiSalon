package dataProvajderi;

import java.util.ArrayList;

import entiteti.Klijent;
import entiteti.Pol;

public class KlijentProvider extends DataProvider<Klijent, String> {
	
	public static final Klijent DELETED = new Klijent() {
		@Override
		public void setHasLoyaltyCard(boolean hasLoyaltyCard) {}
		@Override
		public void setUkupnoPotrosio(double ukupnoPotrosio) {}
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
	};
	
	
	@Override
	public Klijent getDeletedInstance() { return DELETED; }


	@Override
	protected ArrayList<String[]> convertDataToString(Data<String, Klijent> data) {
		ArrayList<String[]> convertedData = new ArrayList<>();
		
		data.list().forEach( (klijent) -> {
			String[] k = new String[9];
			convertedData.add(k);
			
			k[0] = klijent.getIme();
			k[1] = klijent.getPrezime();
			k[2] = klijent.getBrojTelefona();
			k[3] = klijent.getAdresa();
		    k[4] = klijent.getKorisnickoIme();
		    k[5] = klijent.getLozinka();
		    k[6] = klijent.getPol().name();
		    k[7] = Double.toString(klijent.getUkupnoPotrosio());
		    k[8] = Boolean.toString(klijent.getHasLoyaltyCard());
		});
		
		return convertedData;
	}


	@Override
	protected Data<String, Klijent> convertStringToData(ArrayList<String[]> stringData) {
		ArrayList<Klijent> convertedData = new ArrayList<>();
		Data<String, Klijent> data = new Data<>(convertedData);
				
		stringData.forEach( (k) -> {
			Klijent klijent = new Klijent();
			convertedData.add(klijent);
			
			klijent.setIme(k[0]);
			klijent.setPrezime(k[1]);
			klijent.setBrojTelefona(k[2]);
			klijent.setAdresa(k[3]);
		    klijent.setKorisnickoIme(k[4]);
		    klijent.setLozinka(k[5]);
		    klijent.setPol(Pol.valueOf(k[6]));
		    klijent.setUkupnoPotrosio(Double.parseDouble(k[7]));
		    klijent.setHasLoyaltyCard(Boolean.parseBoolean(k[8]));
		});
		
		return data;
	}
		
	
	
}
