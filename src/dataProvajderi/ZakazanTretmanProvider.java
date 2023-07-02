package dataProvajderi;

import java.io.IOException;
import java.util.ArrayList;

import entiteti.Korisnik;
import entiteti.Kozmeticar;
import entiteti.KozmetickiTretman;
import entiteti.ZakazanTretman;
import helpers.DefaultDict;

public class ZakazanTretmanProvider extends ProviderExtrovert<ZakazanTretman>{
	
	private final ZakazanTretman deleted = new ZakazanTretman() {
		// TODO :)
	};

	@Override
	public ZakazanTretman getDeletedInstance() { return this.deleted; }

	@Override
	protected ArrayList<String[]> convertDataToString(ArrayList<ZakazanTretman> data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ArrayList<ZakazanTretman> convertStringToData(ArrayList<String[]> data) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public void loadData(DefaultDict<String, Korisnik> korisniciDict, DefaultDict<String, Kozmeticar> kozmeticariDict,
			DefaultDict<String, KozmetickiTretman.TipTretmana> tipoviTretmanaDict) throws IOException{
		
		ProviderRegistry backup = super.getMainProvider();
		// TODO finish
		super.saveData();
		super.setMainProvider(backup);
	
	}
	
	public void saveData(DefaultDict<String, Korisnik> korisniciDict, DefaultDict<String, Kozmeticar> kozmeticariDict,
			DefaultDict<String, KozmetickiTretman.TipTretmana> tipoviTretmanaDict) throws IOException {
		
		ProviderRegistry backup = super.getMainProvider();
		// TODO finish
		super.saveData();
		super.setMainProvider(backup);
		
	}

}
