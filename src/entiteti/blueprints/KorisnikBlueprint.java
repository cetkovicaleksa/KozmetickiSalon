package entiteti.blueprints;

import entiteti.Entitet;
import entiteti.Pol;

public abstract class KorisnikBlueprint<T extends Entitet> extends Blueprint<T> {
	
	public static final String[] fields = new String[]
			{
					"ime", "prezime", "adresa", "brojTelefona",
					"korisnickoIme", "lozinka", "pol"
			};

	
	public void setIme(String ime) {
		add(0, ime);
	}
	
	public void setPrezime(String prezime) {
		add(1, prezime);
	}
	
	public void setAdresa(String adresa) {
		add(2, adresa);
	}
	
	public void setBrojTelefona(String brojTelefona) {
		add(3, brojTelefona);
	}
	
	public void setKorisnickoIme(String korisnickoIme) {
		add(4, korisnickoIme);
	}
	
	public void setLozinka(String lozinka) {
		add(5, lozinka);
	}
	
	public void setPol(Pol pol) {
		add(6, pol);
	}
	
	
	private void add(int fieldsIndex, Object value) {
		super.add(fields[fieldsIndex], value);
	}

}
