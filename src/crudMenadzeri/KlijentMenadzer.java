package crudMenadzeri;


import java.util.List;

import dataProvajderi.KlijentProvider;
import entiteti.Klijent;
import helpers.Query;


public class KlijentMenadzer extends KorisnikMenadzer<Klijent> {

	public KlijentMenadzer(KlijentProvider klijentProvider, ZakazanTretmanMenadzer zakazanTretmanMenadzer) {
		super(klijentProvider, zakazanTretmanMenadzer);
	}
	
	
	@Override
	public boolean delete(Query<Klijent> selector) {
		List<Klijent> klijenti = super.getMainProvider().get(selector);
		super.getMainProvider().delete(selector);
		
		//brisemo klijenta iz svih njegovi zakazanih tretmana
		return new KlijentFromZTRemover<>(super.getZakazanTretmanMenadzer(), klijenti, super.getMainProvider().getDeletedInstance()).run() != 0;
	}

}
