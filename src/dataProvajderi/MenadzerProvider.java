package dataProvajderi;

import java.util.ArrayList;

import entiteti.Menadzer;

public class MenadzerProvider extends Provider<Menadzer> {
	
	private Menadzer deleted = new Menadzer() {};
	
	@Override
	public Menadzer getDeletedInstance() { return this.deleted; }
	
	@Override
	protected ArrayList<String[]> convertDataToString(ArrayList<Menadzer> data) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	protected ArrayList<Menadzer> convertStringToData(ArrayList<String[]> data) {
		// TODO Auto-generated method stub
		return null;
	}

	


	

}
