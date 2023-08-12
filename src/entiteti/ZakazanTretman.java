package entiteti;

import java.time.LocalDate;
import java.time.LocalTime;

public class ZakazanTretman implements Entitet {
	
	private KozmetickiTretman.TipTretmana tipTretmana;
	private Kozmeticar kozmeticar;
    private Korisnik klijent;
    
    private double cijena;
	private int trajanje;
    private LocalDate datum;
    private LocalTime vrijeme;
    
    private StatusTretmana status;
    
    public ZakazanTretman() {}
    
    public ZakazanTretman(
			KozmetickiTretman.TipTretmana tipTretmana, Kozmeticar kozmeticar, Korisnik klijent,
			LocalDate datum, LocalTime vrijeme
		 ) {
    	this(tipTretmana, kozmeticar, klijent, datum, vrijeme, StatusTretmana.ZAKAZAN);
    }
    
    public ZakazanTretman(
    						KozmetickiTretman.TipTretmana tipTretmana, Kozmeticar kozmeticar, Korisnik klijent,
    						LocalDate datum, LocalTime vrijeme, StatusTretmana status
    					 ) {    	
    	setTipTretmana(tipTretmana);
    	setCijena(tipTretmana.getCijena());
    	setTrajanje(tipTretmana.getTrajanje());
    	
    	setKozmeticar(kozmeticar);
    	setKlijent(klijent);
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

	public Korisnik getKlijent() {
		return klijent;
	}

	public void setKlijent(Korisnik klijent) {
		this.klijent = klijent;
	}

	public double getCijena() {
		return cijena;
	}

	public void setCijena(double cijena) {
		this.cijena = cijena;
	}
	
	public int getTrajanje() {
		return trajanje;
	}
	
	public void setTrajanje(int trajanje) {
		this.trajanje = trajanje;
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
	
	
	public double getCijena(boolean _getOld) {
		return getTipTretmana().getCijena();
	}
	
	public int getTrajanje(boolean _getOld) {
		return getTipTretmana().getTrajanje();
	}
	
	
	public String getNaziv() {
		return getTipTretmana().getNaziv();
	}
	
	public KozmetickiTretman getTretman() {
		return getTipTretmana().getTretman();
	}
	

}
