package crudMenadzeri;

import java.util.List;

import dataProvajderi.MenadzerProvider;
import entiteti.Menadzer;
import helpers.Query;

public class MenadzerMenadzer extends KorisnikMenadzer<Menadzer> {
	
	public MenadzerMenadzer() {
		super();
	}
	
	public MenadzerMenadzer(MenadzerProvider menadzerProvider) {
		super(menadzerProvider);
	}
	
	public MenadzerMenadzer(MenadzerProvider menadzerProvider, ZakazanTretmanMenadzer zakazanTretmanMenadzer) {
		super(menadzerProvider, zakazanTretmanMenadzer);
	}

	@Override
	public boolean delete(Query<Menadzer> selector) {	
		if( super.getZakazanTretmanMenadzer() == null ) {
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
