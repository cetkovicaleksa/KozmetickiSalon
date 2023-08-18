package crudMenadzeri;

import java.util.List;

import dataProvajderi.IdNotUniqueException;
import dataProvajderi.KozmetickiTretmanProvider;
import entiteti.KozmetickiTretman;
import helpers.Query;

public class KozmetickiTretmanMenadzer extends Menadzer<KozmetickiTretman> {
	
	private KozmetickiTretmanProvider mainProvider;
	private TipTretmanaMenadzer tipTretmanaMenadzer;
	private KozmeticarMenadzer kozmeticarMenadzer;

	
	public KozmetickiTretmanMenadzer() {
		super();
	}

	public KozmetickiTretmanMenadzer(KozmetickiTretmanProvider kozmetickiTretmanProvider, TipTretmanaMenadzer ipTretmanaMenadzer, KozmeticarMenadzer kozmeticarMenadzer) {
		this();
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
	/**Adds the new kozmeticki tretman if there is a kozmeticar that has it in its list of treatments.*/
	public void create(KozmetickiTretman entitet) throws IdNotUniqueException, IllegalArgumentException {
		if(entitet == null || getMainProvider().getDeletedInstance().equals(entitet)) {
			throw new IllegalArgumentException("KozmetickiTretman can't be null or deleted kozmeticki tretman.");
		}
		
		if( !getKozmeticarMenadzer().kozmeticarThatCenPreformTreatmentExists(entitet) ) {
			throw new IllegalArgumentException("Can't add a KozmetickiTretman that doesn't have a Kozmeticar that can preform it.");
		}
			
		try {
			getMainProvider().post(entitet);
		}catch(IdNotUniqueException e) {
			throw e; //maby add custom messages so that they can be displayed in popup or something
		}
	}
	
	void createKozmetickiTretman(KozmetickiTretman entitet) throws IdNotUniqueException{
		getMainProvider().post(entitet);
	}

	
	@Override
	public boolean delete(Query<KozmetickiTretman> selector) {
		List<KozmetickiTretman> tretmaniZaBrisanje = getMainProvider().get(selector);
		
		if(!getMainProvider().delete(selector)) {
			return false;
		}
		//brisemo sve tipove tretmana koji pripadaju tretmaniZaBrisanje
		getTipTretmanaMenadzer().delete(
				new Query<>(
						kt -> tretmaniZaBrisanje.contains(kt.getTretman())
					)
		);		
		
		//izbacujemo tretmani iz tretmana za koje su kozmeticari obuceni
		getKozmeticarMenadzer().removeTretmaniFromAllKozmeticari( tretmaniZaBrisanje.toArray(new KozmetickiTretman[0]) );
		return true;
	}

	
}
