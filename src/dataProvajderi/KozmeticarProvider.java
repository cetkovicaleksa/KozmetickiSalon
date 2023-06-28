package dataProvajderi;

import java.util.ArrayList;

import entiteti.Kozmeticar;

public class KozmeticarProvider extends ProviderExtrovert<Kozmeticar> {
	
	private Kozmeticar deleted = new Kozmeticar() {};

	@Override
	public Kozmeticar getDeletedInstance() { return this.deleted; }

	@Override
	protected ArrayList<String[]> convertDataToString(ArrayList<Kozmeticar> data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ArrayList<Kozmeticar> convertStringToData(ArrayList<String[]> data) {
		// TODO Auto-generated method stub
		return null;
	}

	

	

}
