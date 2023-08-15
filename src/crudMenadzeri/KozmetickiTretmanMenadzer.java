package crudMenadzeri;

import java.util.List;

import dataProvajderi.IdNotUniqueException;
import dataProvajderi.KozmetickiTretmanProvider;
import entiteti.Kozmeticar;
import entiteti.KozmetickiTretman;
import helpers.Query;

public class KozmetickiTretmanMenadzer extends Menadzer<KozmetickiTretman> {
	
	private KozmetickiTretmanProvider mainProvider;
	private TipTretmanaMenadzer tipTretmanaMenadzer;
	private KozmeticarMenadzer kozmeticarMenadzer;


	public KozmetickiTretmanMenadzer(KozmetickiTretmanProvider kozmetickiTretmanProvider, TipTretmanaMenadzer ipTretmanaMenadzer, KozmeticarMenadzer kozmeticarMenadzer) {
		super();
		setMainProvider(kozmetickiTretmanProvider);
		setTipTretmanaMenadzer(ipTretmanaMenadzer);
		setKozmeticarMenadzer(kozmeticarMenadzer);
	}
	


	@Override
	protected KozmetickiTretmanProvider getMainProvider() {
		return mainProvider;
	}
	
	protected void setMainProvider(KozmetickiTretmanProvider mainProvider) {
		this.mainProvider = mainProvider;
	}
	
	
	public TipTretmanaMenadzer getTipTretmanaMenadzer() {
		return tipTretmanaMenadzer;
	}

	public void setTipTretmanaMenadzer(TipTretmanaMenadzer ipTretmanaMenadzer) {
		this.tipTretmanaMenadzer = ipTretmanaMenadzer;
	}
	
	
	public KozmeticarMenadzer getKozmeticarMenadzer() {
		return kozmeticarMenadzer;
	}

	public void setKozmeticarMenadzer(KozmeticarMenadzer kozmeticarMenadzer) {
		this.kozmeticarMenadzer = kozmeticarMenadzer;
	}
	
	
	
	@Override
	public void create(KozmetickiTretman entitet) throws IdNotUniqueException {
		if(entitet == null || getMainProvider().getDeletedInstance().equals(entitet)) {
			throw new IllegalArgumentException("KozmetickiTretman can't be null or deleted kozmeticki tretman.");
		}
		
		List<Kozmeticar> obuceniKozmeticari = getKozmeticarMenadzer().read( new Query<>(k -> k.getTretmani().contains(entitet)) );
		
		if(obuceniKozmeticari.isEmpty()) {
			throw new IllegalArgumentException("Can't add a KozmetickiTretman that doesn't have a Kozmeticar that can preform it.");
		}
			
		super.create(entitet);
	}

	
	@Override
	public boolean delete(Query<KozmetickiTretman> selector) {
		List<KozmetickiTretman> tretmaniZaBrisanje = getMainProvider().get(selector);
		
		if(!getMainProvider().delete(selector)) {
			return false;
		}
		//brisemo sve tipove tretmana koji pripadaju tretmaniZaBrisanje
		Query<KozmetickiTretman.TipTretmana> q = new Query<>(kt -> tretmaniZaBrisanje.contains(kt.getTretman()));
		getTipTretmanaMenadzer().delete(q);		
		return true;
	}

	
}
