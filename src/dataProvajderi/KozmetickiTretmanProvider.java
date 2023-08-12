package dataProvajderi;

import java.util.ArrayList;

import entiteti.KozmetickiTretman;

public class KozmetickiTretmanProvider extends DataProvider<KozmetickiTretman, String>{
	
	public static final KozmetickiTretman DELETED = new KozmetickiTretman() {
		@Override
		public void setNaziv(String naziv) {}
		@Override
		public void setOpis(String opis) {}
		@Override
		public TipTretmana newTipTretmana(String naziv, float cijena, int trajanje) { //TODO
			throw new UnsupportedOperationException("Can't create new TipTretmana for the deleted instance.");
		}
	};
	
	public static final KozmetickiTretman.TipTretmana DELETED_TIP_TRETMANA = new KozmetickiTretman.TipTretmana() {
		{
			super.setTretman(DELETED); //idk if it needs the super keyword?
		}
		
		@Override
		public void setNaziv(String naziv) {}
		@Override
		public void setCijena(double cijena) {}
		@Override
		public void setTrajanje(int trajanje) {}
		@Override
		public void setTretman(KozmetickiTretman tretman) {}
	};
	

	@Override
	public KozmetickiTretman getDeletedInstance() { return DELETED; }


	@Override
	protected ArrayList<String[]> convertDataToString(Data<String, KozmetickiTretman> data) {
		ArrayList<String[]> convertedData = new ArrayList<>();
		
		data.list().forEach(kozmetickiTretman -> {
			String[] kt = new String[2];
			convertedData.add(kt);
			
			kt[0] = kozmetickiTretman.getNaziv();
			kt[1] = kozmetickiTretman.getOpis();
		});
		
		return convertedData;
	}


	@Override
	protected Data<String, KozmetickiTretman> convertStringToData(ArrayList<String[]> data) {
		ArrayList<KozmetickiTretman> convertedData = new ArrayList<>();
		
		data.forEach( kt -> {
			KozmetickiTretman kozmetickiTretman = new KozmetickiTretman();
			convertedData.add(kozmetickiTretman);
			
			kozmetickiTretman.setNaziv(kt[0]);
			kozmetickiTretman.setOpis(kt[1]);
		});
		
		return new DataProvider.Data<>(convertedData);
		
	}


}
