package dataProvajderi;

import java.io.IOException;
import java.util.ArrayList;

import entiteti.KozmetickiTretman;
import helpers.DefaultDict;

public class TipTretmanaProvider extends ProviderExtrovert<KozmetickiTretman.TipTretmana>{
		
	private final KozmetickiTretman.TipTretmana deleted = new KozmetickiTretman.TipTretmana() {
		@Override
		public void setNaziv(String naziv) {}
		@Override
		public void setCijena(float cijena) {}
		@Override
		public void setTrajanje(int trajanje) {}
		@Override
		public void setTretman(KozmetickiTretman tretman) {}
	};

	@Override
	public KozmetickiTretman.TipTretmana getDeletedInstance() { return this.deleted; }
	
	

	@Override
	protected ArrayList<String[]> convertDataToString(ArrayList<KozmetickiTretman.TipTretmana> data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ArrayList<KozmetickiTretman.TipTretmana> convertStringToData(ArrayList<String[]> data) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public void loadData(DefaultDict<String, KozmetickiTretman> kozmetickiTretmaniDict) throws IOException{
		
		ProviderRegistry backup = super.getMainProvider();
		// TODO finish
		super.saveData();
		super.setMainProvider(backup);
		
	}
	
	public void saveData(DefaultDict<String, KozmetickiTretman> kozmetickiTretmaniDict) throws IOException{
		
		ProviderRegistry backup = super.getMainProvider();
		// TODO finish
		super.saveData();
		super.setMainProvider(backup);
		
	}

}
