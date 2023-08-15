package crudMenadzeri;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import dataProvajderi.DataProvider;
import dataProvajderi.IdNotUniqueException;
import dataProvajderi.TipTretmanaProvider;
import entiteti.KozmetickiTretman;
import entiteti.KozmetickiTretman.TipTretmana;
import helpers.Query;

public class TipTretmanaMenadzer extends Menadzer<KozmetickiTretman.TipTretmana>{

	private TipTretmanaProvider mainProvider;
	private KozmetickiTretmanMenadzer kozmetickiTretmanMenadzer;
	private ZakazanTretmanMenadzer zakazanTretmanMenadzer;



	public TipTretmanaMenadzer(TipTretmanaProvider mainProvider, KozmetickiTretmanMenadzer kozmetickiTretmanMenadzer) {
		super();
		setMainProvider(mainProvider);
		setKozmetickiTretmanMenadzer(kozmetickiTretmanMenadzer);
	}
	
	
	
	@Override
	protected DataProvider<TipTretmana, ?> getMainProvider() {
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
		
		//prolazimo kroz kozmeticke tretmane i gledamo da li postoji tretman datog entiteta
		Iterator<KozmetickiTretman> iter = getKozmetickiTretmanMenadzer().readAll();		
		while(iter.hasNext()) {
			if(tretman.equals(iter.next())) {
				super.create(entitet);
				return;
			}
		}
		
		throw new IllegalArgumentException("Can't add a TipTretmana when it belongs to a non existent KozmetickiTretman.");
	}


	@Override
	public boolean delete(Query<TipTretmana> selector) {
		List<TipTretmana> tipoviTretmana = getMainProvider().get(selector);
		
		if(tipoviTretmana.isEmpty()) {
			return false;
		}
		
		KozmetickiTretmanMenadzer ktm = getKozmetickiTretmanMenadzer();
		ListIterator<TipTretmana> iter = tipoviTretmana.listIterator();
		//za svaki tip tretmana koji treba izbrisati gledamo da li njegov odgovarajuci tretman posjeduje jos tipova tretmana,
		//ako ne onda i njega brisemo
		while(iter.hasNext()) {
			TipTretmana tipTretmana= iter.next();
			KozmetickiTretman tretman = tipTretmana.getTretman();
			
			if(tretman == null || ktm.getDeletedInstance().equals(tretman)) {
				continue;
			}
			
			//trazimo bar jedan tip tretmana da pripada tretmanu trenutnog tipa, ako ne postoji brisemo tretman
			if(getMainProvider().get(new Query<>(tt -> tretman.equals(tt.getTretman()))).isEmpty()) {
				iter.remove();
			}
		}
		
		boolean hasRemoved = (tipoviTretmana.isEmpty()) ? false : true;
		
		tipoviTretmana.forEach(tipTretmana -> {
			KozmetickiTretman tretman = tipTretmana.getTretman();
			ktm.delete(new Query<>(kt -> tretman.equals(kt)));
		});
		
		return hasRemoved;
	}


	@Override
	public void load() throws IOException {
		((TipTretmanaProvider) getMainProvider()).loadData(getKozmetickiTretmanMenadzer().getIds());
	}
	

	
	
	

}
