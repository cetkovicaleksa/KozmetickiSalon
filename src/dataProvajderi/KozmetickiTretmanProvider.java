package dataProvajderi;

import java.util.ArrayList;

import entiteti.KozmetickiTretman;
import helpers.Converter;

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
		@Override
		public boolean equals(Object obj) {
			return (this == obj);
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
		@Override
		public boolean equals(Object obj) {
			return (this == obj);
		}
	};
	
	
	public static final Converter<KozmetickiTretman, String[]> TO_CSV = kozmetickiTretman -> {
		String[] kt = new String[2];
	
		kt[0] = kozmetickiTretman.getNaziv();
		kt[1] = kozmetickiTretman.getOpis();
	    return kt;
	};
	
	public static final Converter<String[], KozmetickiTretman> FROM_CSV = kt -> {
		KozmetickiTretman kozmetickiTretman = new KozmetickiTretman();
		
		kozmetickiTretman.setNaziv(kt[0]);
		kozmetickiTretman.setOpis(kt[1]);
	    return kozmetickiTretman;
	};
	

	
	@Override
	public KozmetickiTretman getDeletedInstance() { return DELETED; }


	@Override
	protected ArrayList<String[]> convertDataToString(Data<String, KozmetickiTretman> data) {
		ArrayList<String[]> convertedData = new ArrayList<>();
	
		data.list().forEach(kozmetickiTretman -> {
			convertedData.add(TO_CSV.convert(kozmetickiTretman));
		});
		
		return convertedData;
	}


	@Override
	protected Data<String, KozmetickiTretman> convertStringToData(ArrayList<String[]> data) {
		ArrayList<KozmetickiTretman> convertedData = new ArrayList<>();
		
		data.forEach( kt -> {
			convertedData.add(FROM_CSV.convert(kt));
		});
		
		return new DataProvider.Data<>(convertedData);
		
	}


}
