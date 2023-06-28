package dataProvajderi;

import java.util.ArrayList;

import entiteti.Klijent;

public class KlijentProvider extends Provider<Klijent> {
	
	private Klijent deleted = new Klijent() {  // deleted instanca klijenta ce biti jednistvena na nivou salona
		//TODO: override all the setters so you can't change the deleted instance fields		
	};
	
	
	@Override
	public Klijent getDeletedInstance() { return this.deleted; }
	
	@Override
	protected ArrayList<String[]> convertDataToString(ArrayList<Klijent> data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ArrayList<Klijent> convertStringToData(ArrayList<String[]> data) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
