package crudMenadzeri;


import java.util.List;

import dataProvajderi.KlijentProvider;
import entiteti.Klijent;
import helpers.Query;


public class KlijentMenadzer extends KorisnikMenadzer<Klijent> {
	
	public KlijentMenadzer() {
		super();
	}

	public KlijentMenadzer(KlijentProvider klijentProvider, ZakazanTretmanMenadzer zakazanTretmanMenadzer) {
		super(klijentProvider, zakazanTretmanMenadzer);
	}
	
	
	@Override
	public boolean delete(Query<Klijent> selector) {
		List<Klijent> klijenti = super.getMainProvider().get(selector);
		if(klijenti.isEmpty()) {
			return false;
		}
		super.getMainProvider().delete(selector);
		
		//brisemo klijenta iz svih njegovi zakazanih tretmana
		//new KlijentFromZTRemover<>(super.getZakazanTretmanMenadzer(), klijenti, super.getMainProvider().getDeletedInstance()).run();
		super.removeKlijentiFromZakazaniTretmani(klijenti);
		return true;
	}
	
	
	public List<Klijent> getKlijentiThatHaveLoyaltyCard(){
		return ((KlijentProvider) super.getMainProvider()).get( new Query<>(klijent -> klijent.getHasLoyaltyCard())	);
	}
}
