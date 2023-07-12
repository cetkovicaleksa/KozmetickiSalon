package entiteti;

public class KozmetickiTretman implements Entitet {
	
	private String naziv, opis;
	
	public KozmetickiTretman() {}
	
	public KozmetickiTretman(String naziv, String opis) {
		setNaziv(naziv);
		setOpis(opis);
	}
	
	public KozmetickiTretman.TipTretmana newTipTretmana(String naziv, float cijena, int trajanje) {
		return new KozmetickiTretman.TipTretmana(naziv, cijena, trajanje, this);
	}
		
	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public String getOpis() {
		return opis;
	}

	public void setOpis(String opis) {
		this.opis = opis;
	}
	
	
	
    
	public static class TipTretmana implements Entitet{
		
		private String naziv;
		private float cijena;
		private int trajanje;
		private KozmetickiTretman tretman;
		
		public TipTretmana() {}
		
		public TipTretmana(String naziv, float cijena, int trajanje, KozmetickiTretman tretman) {
			setNaziv(naziv);
			setCijena(cijena);
			setTrajanje(trajanje);
			setTretman(tretman);
		}
		
		public String getNaziv() {
			return naziv;
		}
		public void setNaziv(String naziv) {
			this.naziv = naziv;
		}
		public float getCijena() {
			return cijena;
		}
		public void setCijena(float cijena) {
			this.cijena = cijena;
		}
		public int getTrajanje() {
			return trajanje;
		}
		public void setTrajanje(int trajanje) {
			this.trajanje = trajanje;
		}
		public KozmetickiTretman getTretman() {
			return tretman;
		}
		public void setTretman(KozmetickiTretman tretman) {
			this.tretman = tretman;
		}
	}

}
