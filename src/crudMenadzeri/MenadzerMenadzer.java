package crudMenadzeri;

import java.util.List;

import dataProvajderi.MenadzerProvider;
import entiteti.Menadzer;
import helpers.Query;

public class MenadzerMenadzer extends KorisnikMenadzer<Menadzer> {
	
	public MenadzerMenadzer(MenadzerProvider menadzerProvider) {
		super.setMainProvider(menadzerProvider);
	}
	
	public MenadzerMenadzer(MenadzerProvider menadzerProvider, ZakazanTretmanMenadzer zakazanTretmanMenadzer) {
		super(menadzerProvider, zakazanTretmanMenadzer);
	}

	@Override
	public boolean delete(Query<Menadzer> selector) {
		ZakazanTretmanMenadzer ztm = super.getZakazanTretmanMenadzer();		
		if(ztm == null) {
			return super.delete(selector);			
		}
		
		List<Menadzer> menadzeri = super.getMainProvider().get(selector);
		if(menadzeri.isEmpty()) {
			return false;
		}
		
		super.getMainProvider().delete(selector);
		super.removeKlijentiFromZakazaniTretmani(menadzeri);
		return true;
	}

}
