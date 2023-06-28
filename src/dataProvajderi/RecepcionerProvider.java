package dataProvajderi;

import java.util.ArrayList;

import entiteti.Recepcioner;

public class RecepcionerProvider extends Provider<Recepcioner> {
	
	private Recepcioner deleted = new Recepcioner() {};

	@Override
	public Recepcioner getDeletedInstance() { return this.deleted; }

	@Override
	protected ArrayList<String[]> convertDataToString(ArrayList<Recepcioner> data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ArrayList<Recepcioner> convertStringToData(ArrayList<String[]> data) {
		// TODO Auto-generated method stub
		return null;
	}

}
