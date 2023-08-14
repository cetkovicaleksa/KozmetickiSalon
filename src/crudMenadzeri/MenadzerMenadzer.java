package crudMenadzeri;

import java.util.List;

import dataProvajderi.MenadzerProvider;
import dataProvajderi.ZakazanTretmanProvider;
import entiteti.Menadzer;
import helpers.Query;

public class MenadzerMenadzer extends Manager<Menadzer> {
	
	public MenadzerMenadzer(MenadzerProvider menadzerProvider) {
		super();
		super.setMainProvider(menadzerProvider);
	}
	
	public MenadzerMenadzer(MenadzerProvider menadzerProvider, ZakazanTretmanProvider zakazanTretmanProvider) {
		super(menadzerProvider, zakazanTretmanProvider);
	}

	@Override
	public boolean delete(Query<Menadzer> selector) {
		ZakazanTretmanProvider ztp = super.getZakazanTretmanProvider();		
		if(ztp == null) {
			return super.delete(selector);			
		}
		
		List<Menadzer> menadzeri = super.getMainProvider().get(selector);
		super.getMainProvider().delete(selector);
		
		return new KlijentFromZTRemover<>(ztp, menadzeri, super.getMainProvider().getDeletedInstance()).run() != 0;
	}

}
