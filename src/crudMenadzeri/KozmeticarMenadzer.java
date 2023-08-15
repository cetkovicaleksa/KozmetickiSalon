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

public class KozmeticarMenadzer extends KorisnikMenadzer<Kozmeticar> {
	
	private KozmetickiTretmanMenadzer kozmetickiTretmanMenadzer;
	
	public KozmeticarMenadzer(KozmeticarProvider kozmeticarProvider, KozmetickiTretmanMenadzer kozmetickTretmanMenadzer, ZakazanTretmanMenadzer zakazanTretmanMenadzer) {
		super(kozmeticarProvider, zakazanTretmanMenadzer);
		setKozmetickiTretmanMenadzer(kozmetickTretmanMenadzer);
	}
	
	
	protected KozmetickiTretmanMenadzer kozmetickiTretmanMenadzer() {
		return kozmetickiTretmanMenadzer;
	}
	
	protected void setKozmetickiTretmanMenadzer(KozmetickiTretmanMenadzer kozmetickiTretmanMenadzer) {
		this.kozmetickiTretmanMenadzer = kozmetickiTretmanMenadzer;
	}
	
	
	private KozmeticarProvider getKozmeticarProvider() {
		return (KozmeticarProvider) super.getMainProvider();
	}

	
	
	@Override
	public void create(Kozmeticar entitet) throws IdNotUniqueException {
		KozmeticarProvider kozmeticarProvider = getKozmeticarProvider();
		
		if(entitet == null || kozmeticarProvider.getDeletedInstance().equals(entitet)) {
			
		}
		super.create(entitet);
		
		//provjeravamo da li postoje tretmani za koje je kozmeticar obucen, a da ne postoje u KozmetickiTretmanProvider
		//ako ne postoje dodajemo ih
		
		KozmetickiTretmanMenadzer kozmetickiTretmanMenadzer = kozmetickiTretmanMenadzer();		
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
				kozmetickiTretmanMenadzer.create(tretman); //may throw exception
			}		
		});
	}

	
	@Override
	public boolean delete(Query<Kozmeticar> selector) {
		List<Kozmeticar> kozmeticari = getKozmeticarProvider().get(selector);
		if(kozmeticari.isEmpty()) {
			return false;
		}
		
		//TODO: finish
		
		return true;
	}

	
	@Override
	public void load() throws IOException {
		getKozmeticarProvider().loadData(kozmetickiTretmanMenadzer().getIds());;
	}

}
