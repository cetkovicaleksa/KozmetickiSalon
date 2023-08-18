package entiteti;

public class Menadzer extends Zaposleni {
	//could override the izracunajBazuPlate method
	
	public Menadzer() {
		super();
	}
	
	public Menadzer(
						String ime, String prezime, String brojTelefona, String adresa,
						String korisnickoIme, String lozinka, Pol pol, int godineStaza,
						double bazaPlate, boolean bonus, NivoStrucneSpreme nivoStrucneSpreme
					) {
		super(ime, prezime, brojTelefona, adresa, korisnickoIme, lozinka, pol, godineStaza, bazaPlate, bonus, nivoStrucneSpreme);
	}
}
