package dataProvajderi;

import java.util.ArrayList;

import entiteti.KozmetickiTretman;

public class KozmetickiTretmanProvider extends OutdatedProvider<KozmetickiTretman>{
	
	private final KozmetickiTretman deleted = new KozmetickiTretman() {
		@Override
		public void setNaziv(String naziv) {}
		@Override
		public void setOpis(String opis) {}
		@Override
		public TipTretmana newTipTretmana(String naziv, float cijena, int trajanje) { return null; }
	};
	

	@Override
	public KozmetickiTretman getDeletedInstance() { return this.deleted; }

	@Override
	protected ArrayList<String[]> convertDataToString(ArrayList<KozmetickiTretman> data) {
		
		ArrayList<String[]> convertedData = new ArrayList<>();		
		
		data.forEach( tretman -> {
			String[] t = new String[2];
			convertedData.add(t);
			
			t[0] = tretman.getNaziv();
			t[1] = tretman.getOpis();
		});
		
		return convertedData;
		
	}

	@Override
	protected ArrayList<KozmetickiTretman> convertStringToData(ArrayList<String[]> data) {
		
		ArrayList<KozmetickiTretman> convertedData = new ArrayList<>();
		
		data.forEach( t -> {
			KozmetickiTretman tretman = new KozmetickiTretman();
			convertedData.add(tretman);
			
			tretman.setNaziv(t[0]);
			tretman.setOpis(t[1]);
		});
		
		return convertedData;
		
	}


}
