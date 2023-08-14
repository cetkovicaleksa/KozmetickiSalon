package crudMenadzeri;


import java.util.List;

import dataProvajderi.KlijentProvider;
import dataProvajderi.ZakazanTretmanProvider;
import entiteti.Klijent;
import helpers.Query;


public class KlijentManager extends Manager<Klijent> {

	public KlijentManager(KlijentProvider klijentProvider, ZakazanTretmanProvider zakazanTretmanProvider) {
		super(klijentProvider, zakazanTretmanProvider);
	}
	
	
	@Override
	public boolean delete(Query<Klijent> selector) {
		List<Klijent> klijenti = super.getMainProvider().get(selector);
		super.getMainProvider().delete(selector);
		
		//removing deleted clients form appointments
		return new KlijentFromZTRemover<>(super.getZakazanTretmanProvider(), klijenti, super.getMainProvider().getDeletedInstance()).run() != 0;
	}

	
	
	
	
	
	

}
