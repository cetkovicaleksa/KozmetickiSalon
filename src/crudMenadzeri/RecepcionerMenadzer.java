package crudMenadzeri;

import java.util.List;

import dataProvajderi.RecepcionerProvider;
import dataProvajderi.ZakazanTretmanProvider;
import entiteti.Recepcioner;
import helpers.Query;

public class RecepcionerMenadzer extends Manager<Recepcioner> {
			
	public RecepcionerMenadzer(RecepcionerProvider recepcionerProvider) {
		super();
		super.setMainProvider(recepcionerProvider);
	}
	
	public RecepcionerMenadzer(RecepcionerProvider recepcionerProvider, ZakazanTretmanProvider zakazanTretmanProvider) {
		super(recepcionerProvider, zakazanTretmanProvider);
	}

	@Override
	public boolean delete(Query<Recepcioner> selector) {
		ZakazanTretmanProvider ztp = super.getZakazanTretmanProvider();		
		if(ztp == null) {
			return super.delete(selector);			
		}
		
		List<Recepcioner> recepcioneri = super.getMainProvider().get(selector);
		super.getMainProvider().delete(selector);
		
		return new KlijentFromZTRemover<>(ztp, recepcioneri, super.getMainProvider().getDeletedInstance()).run() != 0;
	}

}
