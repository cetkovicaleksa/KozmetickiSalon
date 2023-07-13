package entiteti.blueprints;

import entiteti.Klijent;
import entiteti.Pol;

public class KlijentBlueprint extends KorisnikBlueprint<Klijent> {
	
	public static final String[] fields = new String[] {"hasLoyaltyCard"};

	
	
	public static KlijentBlueprint build() {
		return new KlijentBlueprint();
	}


	public KlijentBlueprint ime(String ime) {
		super.setIme(ime);
		return this;
	}

	public KlijentBlueprint prezime(String prezime) {
		super.setPrezime(prezime);
		return this;
	}

	public KlijentBlueprint adresa(String adresa) {
		super.setAdresa(adresa);
		return this;
	}

	public KlijentBlueprint brojTelefona(String brojTelefona) {
		super.setBrojTelefona(brojTelefona);
		return this;
	}

	public KlijentBlueprint korisnickoIme(String korisnickoIme) {
		super.setKorisnickoIme(korisnickoIme);
		return this;
	}
	
	public KlijentBlueprint lozinka(String lozinka) {
		super.setLozinka(lozinka);
		return this;
	}

	public KlijentBlueprint pol(Pol pol) {
		super.setPol(pol);
		return this;
	}


	public KlijentBlueprint hasLoyaltyCard(boolean hasLoyaltyCard) {
		super.add(fields[0], hasLoyaltyCard);
		return this;
	}




	@Override
	protected Klijent getNewEntity() {
		return new Klijent();
	}	

	@Override
	protected String[] getAllFieldNames() {
		int len = KorisnikBlueprint.fields.length + KlijentBlueprint.fields.length;
		//TODO
		return fields;
	}
}
