package entiteti;

import java.util.ArrayList;

public class Kozmeticar extends Zaposleni{
	
	ArrayList<KozmetickiTretman> tretmani;
	
	public Kozmeticar() {
		super();
		setTretmani(new ArrayList<>());
	}
	
	public Kozmeticar(
			String ime, String prezime, String brojTelefona, String adresa,
			String korisnickoIme, String lozinka, Pol pol, int godineStaza,
			double bazaPlate, boolean bonus, NivoStrucneSpreme nivoStrucneSpreme, ArrayList<KozmetickiTretman> tretmani
		) {
		super(ime, prezime, brojTelefona, adresa, korisnickoIme, lozinka, pol, godineStaza, bazaPlate, bonus, nivoStrucneSpreme);
		setTretmani(tretmani);		
	}
	
	
	public ArrayList<KozmetickiTretman> getTretmani() {
		return tretmani;
	}

	public void setTretmani(ArrayList<KozmetickiTretman> tretmani) {
		this.tretmani = tretmani;
	}
	
	public void addTretman(KozmetickiTretman tretman) {
		getTretmani().add(tretman);
	}
	
	public void removeTretman(KozmetickiTretman tretman) {
		getTretmani().remove(tretman);
	}
	
	

	

}
