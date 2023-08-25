package entiteti;

public abstract class Korisnik implements Entitet {
	
	private String ime, prezime, brojTelefona,
	  adresa, korisnickoIme, lozinka;
	private Pol pol;
	
	
	public Korisnik() {
		setPol(Pol.NIJE_NAVEDEN);
	}
	
	public Korisnik(String korisnickoIme, String lozinka) {
		this();
		setKorisnickoIme(korisnickoIme);
		setLozinka(lozinka);
	}
	
	public Korisnik(String ime, String prezime, String brojTelefona, String adresa, String korisnickoIme, String lozinka, Pol pol) {
		setIme(ime);
		setPrezime(prezime);
		setBrojTelefona(brojTelefona);
		setAdresa(adresa);
		setKorisnickoIme(korisnickoIme);
		setLozinka(lozinka);
		setPol(pol);
	}
	
	

	public String getIme() { return this.ime; }
	public void setIme(String ime) { this.ime = ime; }

	public String getPrezime() { return this.prezime; }
	public void setPrezime(String prezime) { this.prezime = prezime; }

	public String getBrojTelefona() { return this.brojTelefona; }
	public void setBrojTelefona(String brojTelefona) { this.brojTelefona = brojTelefona; }

	public String getAdresa() {	return adresa; }
	public void setAdresa(String adresa) { this.adresa = adresa; }

	public String getKorisnickoIme() { return korisnickoIme; }
	public void setKorisnickoIme(String korisnickoIme) { this.korisnickoIme = korisnickoIme; }

	public String getLozinka() { return lozinka; }
	public void setLozinka(String lozinka) { this.lozinka = lozinka; }

	public Pol getPol() { return pol; }
	public void setPol(Pol pol) { this.pol = pol; }

	
	@Override
	public String toString() {
		return String.format("%s %s [ %s ]", getIme(), getPrezime(), getKorisnickoIme());
	}
}
