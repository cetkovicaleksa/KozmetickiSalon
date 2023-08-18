package entiteti;


public class Klijent extends Korisnik {
	
	//private double ukupnoPotrosio;
	private boolean hasLoyaltyCard;


	public Klijent() {
		super();
	}
	
	public Klijent(String ime, String prezime, String brojTelefona, String adresa, String korisnickoIme, String lozinka, Pol pol, boolean hasLoyaltyCard) {
		super(ime, prezime, brojTelefona, adresa, korisnickoIme, lozinka, pol);
		setHasLoyaltyCard(hasLoyaltyCard);
	}

	public boolean getHasLoyaltyCard() {
		return hasLoyaltyCard;
	}

	public void setHasLoyaltyCard(boolean hasLoyaltyCard) {
		this.hasLoyaltyCard = hasLoyaltyCard;
	}
	
	//public double getUkupnoPotrosio() {
		//return ukupnoPotrosio;
	//}

	//public void setUkupnoPotrosio(double ukupnoPotrosio) {
		//this.ukupnoPotrosio = ukupnoPotrosio;
	//}

}
