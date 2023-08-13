package entiteti;

public abstract class Zaposleni extends Korisnik implements Entitet {
	
	private int godineStaza;
	private double bazaPlate;
	private boolean bonus;
	private NivoStrucneSpreme nivoStrucneSpreme;
	
	public Zaposleni() {
		setNivoStrucneSpreme(NivoStrucneSpreme.SKOLE_BEZ);
	}
	
	public Zaposleni(
						String ime, String prezime, String brojTelefona, String adresa,
						String korisnickoIme, String lozinka, Pol pol, int godineStaza,
						double bazaPlate, boolean bonus, NivoStrucneSpreme nivoStrucneSpreme
					) {
		super(ime, prezime, brojTelefona, adresa, korisnickoIme, lozinka, pol);
		setGodineStaza(godineStaza);
		setBazaPlate(bazaPlate);
		setBonus(bonus);
		setNivoStrucneSpreme(nivoStrucneSpreme);	
	}
	
	
	public double izracunajPlatu() {
		return 800 + getNivoStrucneSpreme().getValue() * getGodineStaza(); //izracunati bazu koja ce se kombinovati sa bonusom ako zadovoljava uslove bonusa
	}
	
	
	
	public int getGodineStaza() {
		return godineStaza;
	}

	public void setGodineStaza(int godineStaza) {
		this.godineStaza = godineStaza;
	}

	public double getBazaPlate() {
		return bazaPlate;
	}

	public void setBazaPlate(double bazaPlate) {
		this.bazaPlate = bazaPlate;
	}
	
	public boolean hasBonus() {
		return bonus;
	}
	
	public void setBonus(boolean bonus) {
		this.bonus = bonus;
	}

	public NivoStrucneSpreme getNivoStrucneSpreme() {
		return nivoStrucneSpreme;
	}

	public void setNivoStrucneSpreme(NivoStrucneSpreme nivoStrucneSpreme) {
		this.nivoStrucneSpreme = nivoStrucneSpreme;
	}

	

}
