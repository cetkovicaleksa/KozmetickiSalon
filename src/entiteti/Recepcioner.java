package entiteti;

public class Recepcioner extends Zaposleni {
	//could override the izracunajBazuPlate method
	
	public Recepcioner() {
		super();
	}
	
	public Recepcioner(
						String ime, String prezime, String brojTelefona, String adresa,
						String korisnickoIme, String lozinka, Pol pol, int godineStaza,
						double bazaPlate, boolean bonus, NivoStrucneSpreme nivoStrucneSpreme
					) {
		super(ime, prezime, brojTelefona, adresa, korisnickoIme, lozinka, pol, godineStaza, bazaPlate, bonus, nivoStrucneSpreme);
	}
}
