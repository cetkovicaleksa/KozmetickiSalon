package entiteti;

import java.time.LocalDate;
import java.time.LocalTime;

public class ZakazanTretman implements Entitet {
	
	private KozmetickiTretman.TipTretmana tipTretmana; //no no TODO: this can't be like this because it is not supposed to change when tiptretmana changes price
	private Kozmeticar kozmeticar;
    private Klijent klijent;
    private double cijena;
    
    private LocalDate datum;
    private LocalTime vrijeme;
    
    private StatusTretmana status;
    
    public ZakazanTretman() {}
    
    public ZakazanTretman(
    						KozmetickiTretman.TipTretmana tipTretmana, Kozmeticar kozmeticar, Klijent klijent,
    						double cijena, LocalDate datum, LocalTime vrijeme, StatusTretmana status
    					 ) {
    	setTipTretmana(tipTretmana);
    	setKozmeticar(kozmeticar);
    	setKlijent(klijent);
    	setCijena(cijena);
    	setDatum(datum);
    	setVrijeme(vrijeme);
    	setStatus(status);
    }

	public KozmetickiTretman.TipTretmana getTipTretmana() {
		return tipTretmana;
	}

	public void setTipTretmana(KozmetickiTretman.TipTretmana tipTretmana) {
		this.tipTretmana = tipTretmana;
	}

	public Kozmeticar getKozmeticar() {
		return kozmeticar;
	}

	public void setKozmeticar(Kozmeticar kozmeticar) {
		this.kozmeticar = kozmeticar;
	}

	public Klijent getKlijent() {
		return klijent;
	}

	public void setKlijent(Klijent klijent) {
		this.klijent = klijent;
	}

	public double getCijena() {
		return cijena;
	}

	public void setCijena(double cijena) {
		this.cijena = cijena;
	}

	public LocalDate getDatum() {
		return datum;
	}

	public void setDatum(LocalDate datum) {
		this.datum = datum;
	}

	public LocalTime getVrijeme() {
		return vrijeme;
	}

	public void setVrijeme(LocalTime vrijeme) {
		this.vrijeme = vrijeme;
	}

	public StatusTretmana getStatus() {
		return status;
	}

	public void setStatus(StatusTretmana status) {
		this.status = status;
	}
	
	

}
