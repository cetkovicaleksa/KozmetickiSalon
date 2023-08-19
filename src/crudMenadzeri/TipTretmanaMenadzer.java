package crudMenadzeri;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import dataProvajderi.IdNotUniqueException;
import dataProvajderi.TipTretmanaProvider;
import entiteti.KozmetickiTretman;
import entiteti.KozmetickiTretman.TipTretmana;
import helpers.Query;
import helpers.Updater;

public class TipTretmanaMenadzer extends Menadzer<KozmetickiTretman.TipTretmana>{

	private TipTretmanaProvider mainProvider;
	private KozmetickiTretmanMenadzer kozmetickiTretmanMenadzer;
	//private ZakazanTretmanMenadzer zakazanTretmanMenadzer;


	public TipTretmanaMenadzer() {
		super();
	}
	
	public TipTretmanaMenadzer(TipTretmanaProvider mainProvider, KozmetickiTretmanMenadzer kozmetickiTretmanMenadzer) {
		this();
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
	/**Adds a new TipTretmana only if it belongs to an existent KozmetickiTretman.*/
	public void create(TipTretmana entitet) throws IdNotUniqueException, IllegalArgumentException, IllegalArgumentException {
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
	/**Deletes tipovi tretmana.*/ //TODO: check whether you should delete KozmetickiTretman because it conflicts with KozmeticarMenadzer
	public boolean delete(Query<TipTretmana> selector) {
		TipTretmanaProvider mainProvider = getMainProvider();
		List<TipTretmana> tipoviTretmanaZaBrisanje = mainProvider.get(selector);
		
		if(tipoviTretmanaZaBrisanje.isEmpty() || !mainProvider.delete(selector)) { 
			return false;
		}
		
		return true;		
	}
	
	public void deleteKozmetickiTretmaniThatHaveNoTipTretmana() { //hmm
		HashMap<KozmetickiTretman, Integer> map = new HashMap<>(); // explain
		KozmetickiTretmanMenadzer ktm = getKozmetickiTretmanMenadzer();
		
		getMainProvider().put(
				new Query<>(),
				new Updater<>(tipTretmana -> {
					Integer num = map.get(tipTretmana.getTretman());
					
					if(num == null || num++ == 0) {
						map.put(tipTretmana.getTretman(), 1);
					}		
				})
		);
		
		ktm.delete(
				new Query<>(kt -> !map.containsKey(kt))
		);
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
