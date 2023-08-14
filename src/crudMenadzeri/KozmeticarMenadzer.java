package crudMenadzeri;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import dataProvajderi.IdNotUniqueException;
import dataProvajderi.KozmeticarProvider;
import dataProvajderi.KozmetickiTretmanProvider;
import dataProvajderi.ZakazanTretmanProvider;
import entiteti.Kozmeticar;
import entiteti.KozmetickiTretman;
import helpers.Query;

public class KozmeticarMenadzer extends Manager<Kozmeticar> { //TODO: replace KozmetickiTretmanProvider with KozmetickiTretmanManager
	
	private KozmetickiTretmanProvider kozmetickiTretmanProvider;
	
	public KozmeticarMenadzer(KozmeticarProvider kozmeticarProvider, KozmetickiTretmanProvider kozmetickTretmanProvider, ZakazanTretmanProvider zakazanTretmanProvider) {
		super(kozmeticarProvider, zakazanTretmanProvider);
		setKozmetickiTretmanProvider(kozmetickTretmanProvider);
	}
	
	protected KozmetickiTretmanProvider getKozmetickiTretmanProvider() {
		return kozmetickiTretmanProvider;
	}
	
	protected void setKozmetickiTretmanProvider(KozmetickiTretmanProvider kozmetickiTretmanProvider) {
		this.kozmetickiTretmanProvider = kozmetickiTretmanProvider;
	}
	
	
	protected KozmeticarProvider getKozmeticarProvider() {
		return (KozmeticarProvider) super.getMainProvider();
	}

	
	
	@Override
	public void create(Kozmeticar entitet) throws IdNotUniqueException {
		super.create(entitet);
		
		//provjeravamo da li postoje tretmani za koje je entitet obucen, a da ne postoje u KozmetickiTretmanProvider
		KozmeticarProvider kozmeticarProvider = getKozmeticarProvider();
		KozmetickiTretmanProvider kozmetickiTretmanProvider = getKozmetickiTretmanProvider();
		
		entitet.getTretmani().forEach(tretman -> {
			
			Iterator<Kozmeticar> iter = kozmeticarProvider.get();
			boolean foundTretman = false;
			
			while(iter.hasNext()) {
				if (iter.next().getTretmani().contains(tretman)) {
					foundTretman = true;
					break;
				}
			}
			
			if(!foundTretman) {
				kozmetickiTretmanProvider.post(tretman); //may throw exception
			}
			
			//Query<Kozmeticar> q = new Query<>(k -> k.getTretmani().contains(tretman));
			
			//if( !kozmeticarProvider.get(q).isEmpty() ) {
				//kozmetickiTretmanProvider.post(tretman);
			//}			
		});
	}

	
	@Override
	public boolean delete(Query<Kozmeticar> selector) {
		List<Kozmeticar> kozmeticari = getKozmeticarProvider().get(selector);
		if(kozmeticari.isEmpty()) {
			return false;
		}
		
		LinkedList<KozmetickiTretman> tretmani = new LinkedList<>();
		
		kozmeticari.forEach(kozmeticar1 -> {
			kozmeticari.forEach(kozmeticar2 -> {
				
			});
		});
		
		kozmeticari.forEach(kozmeticar -> {
			
			kozmeticar.getTretmani().forEach(tretman -> {
				if (tretmani.contains(tretman)) {
					
				}
				tretmani.add(tretman);
			});
			
		});
		
		return true;
	}

	
	@Override
	public void load() throws IOException {
		getKozmeticarProvider().loadData(getKozmetickiTretmanProvider().getIds());;
	}

}
