package crudMenadzeri;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import dataProvajderi.IdNotUniqueException;
import dataProvajderi.TipTretmanaProvider;
import entiteti.KozmetickiTretman;
import entiteti.KozmetickiTretman.TipTretmana;
import helpers.Query;

public class TipTretmanaMenadzer extends Menadzer<KozmetickiTretman.TipTretmana>{

	private TipTretmanaProvider mainProvider;
	private KozmetickiTretmanMenadzer kozmetickiTretmanMenadzer;
	//private ZakazanTretmanMenadzer zakazanTretmanMenadzer;



	public TipTretmanaMenadzer(TipTretmanaProvider mainProvider, KozmetickiTretmanMenadzer kozmetickiTretmanMenadzer) {
		super();
		setMainProvider(mainProvider);
		setKozmetickiTretmanMenadzer(kozmetickiTretmanMenadzer);
	}
	
	
	
	@Override
	protected TipTretmanaProvider getMainProvider() {
		return mainProvider;
	}
	
	public void setMainProvider(TipTretmanaProvider mainProvider) {
		this.mainProvider = mainProvider;
	}
	
	
	public KozmetickiTretmanMenadzer getKozmetickiTretmanMenadzer() {
		return kozmetickiTretmanMenadzer;
	}

	public void setKozmetickiTretmanMenadzer(KozmetickiTretmanMenadzer kozmetickiTretmanMenadzer) {
		this.kozmetickiTretmanMenadzer = kozmetickiTretmanMenadzer;
	}
	
	
	
	@Override
	public void create(TipTretmana entitet) throws IdNotUniqueException, IllegalArgumentException {
		TipTretmana deletedTipTretmana = getMainProvider().getDeletedInstance();
		KozmetickiTretman deletedKozmetickiTretman = deletedTipTretmana.getTretman();
		
		if(entitet == null) {
			throw new IllegalArgumentException("TipTretmana can't be null.");
		}
		
		KozmetickiTretman tretman = entitet.getTretman();
		//ne moze se dodati deleted tip tretmana
		if(deletedTipTretmana.equals(entitet)) {
			throw new IllegalArgumentException("TipTretmana can't be deleted TipTretmana.");
		}
		//ne moze se dodati tip tretmana koji nema svoj tretman ili mu je tretman deleted
		if(tretman == null || deletedKozmetickiTretman.equals(tretman)) {
			throw new IllegalArgumentException("Can't add new TipTretmana that has a deleted KozmetickiTretman or null as its treatment.");
		}
		
		if(!getKozmetickiTretmanMenadzer().exists(tretman)) {
			throw new IllegalArgumentException("Can't add a TipTretmana when it belongs to a non existent KozmetickiTretman.");
		}
		
		getMainProvider().post(entitet);
	}
	

	@Override
	public boolean delete(Query<TipTretmana> selector) {
		TipTretmanaProvider mainProvider = getMainProvider();
		List<TipTretmana> tipoviTretmanaZaBrisanje = mainProvider.get(selector);
		
		if(tipoviTretmanaZaBrisanje.isEmpty() || !mainProvider.delete(selector)) { 
			return false;
		}
		
		//izbrisali smo tipove tretmana, sada provjeravamo da li su neki kozmeticki tretmani ostali bez tipova tretmana i
		//brisemo one koji jesu
		List<KozmetickiTretman> kozmetickiTretmaniZaBrisanje = new ArrayList<>();
		KozmetickiTretmanMenadzer kozmetickiTretmanMenadzer = getKozmetickiTretmanMenadzer();
		KozmetickiTretman deletedKozmetickiTretman = kozmetickiTretmanMenadzer.getDeletedInstance();
		
		for(TipTretmana tipTretmana : tipoviTretmanaZaBrisanje) {
			KozmetickiTretman tretman = tipTretmana.getTretman();
			
			if(tretman == null || deletedKozmetickiTretman.equals(tretman)) { //should never happen
				continue;
			}
			
			Iterator<TipTretmana> sviTipoviTretmanaIter = mainProvider.get();
			boolean foundTipTretmanaForTretman = false;
			
			while(sviTipoviTretmanaIter.hasNext()) {
				if(tretman.equals(sviTipoviTretmanaIter.next().getTretman())) {
					foundTipTretmanaForTretman = true;
					break;
				}
			}
			
			if( !foundTipTretmanaForTretman ) {
				kozmetickiTretmaniZaBrisanje.add(tretman);
			}
		}
		
		if( !kozmetickiTretmaniZaBrisanje.isEmpty() ) {
			kozmetickiTretmanMenadzer.delete(
					new Query<>(kozmetickiTretman -> kozmetickiTretmaniZaBrisanje.contains(kozmetickiTretman))
			);
		}
		
		return true;		
	}
	


	@Override
	public void load() throws IOException {
		getMainProvider().loadData(getKozmetickiTretmanMenadzer().getIds());
	}
	
	@Override
	public void save() throws IOException {
		getMainProvider().saveData(getKozmetickiTretmanMenadzer()::getId);
	}
	

	List<KozmetickiTretman.TipTretmana> tipoviTretmana(KozmetickiTretman kozmetickiTretman) {
		return getMainProvider().get(new Query<>(
				tt -> tt.getTretman().equals(kozmetickiTretman)
				));
	}
}
