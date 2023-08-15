package crudMenadzeri;

import java.util.List;

import dataProvajderi.RecepcionerProvider;
import entiteti.Recepcioner;
import helpers.Query;

public class RecepcionerMenadzer extends KorisnikMenadzer<Recepcioner> {
			
	public RecepcionerMenadzer(RecepcionerProvider recepcionerProvider) {
		super();
		super.setMainProvider(recepcionerProvider);
	}
	
	public RecepcionerMenadzer(RecepcionerProvider recepcionerProvider, ZakazanTretmanMenadzer zakazanTretmanMenadzer) {
		super(recepcionerProvider, zakazanTretmanMenadzer);
	}

	@Override
	public boolean delete(Query<Recepcioner> selector) {
		ZakazanTretmanMenadzer ztm = super.getZakazanTretmanMenadzer();		
		if(ztm == null) {
			return super.delete(selector);			
		}
		
		List<Recepcioner> recepcioneri = super.getMainProvider().get(selector);
		super.getMainProvider().delete(selector);
		
		return new KlijentFromZTRemover<>(ztm, recepcioneri, super.getMainProvider().getDeletedInstance()).run() != 0;
	}

}
