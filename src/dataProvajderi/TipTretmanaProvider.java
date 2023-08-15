package dataProvajderi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Function;

import entiteti.KozmetickiTretman;
import helpers.Converter;
import helpers.DefaultDict;

public class TipTretmanaProvider extends XDataProvider<KozmetickiTretman.TipTretmana, String>{
		
	public static final KozmetickiTretman.TipTretmana DELETED = KozmetickiTretmanProvider.DELETED_TIP_TRETMANA;
	
	public static final Converter<KozmetickiTretman.TipTretmana, String[]> TO_CSV = tipTretmana -> {
		String[] tt = new String[4];
		
		tt[0] = tipTretmana.getNaziv();
		tt[1] = Double.toString(tipTretmana.getCijena());
		tt[2] = Integer.toString(tipTretmana.getTrajanje());
	    return tt;
	};
	
	public static final Converter<String[], KozmetickiTretman.TipTretmana> FROM_CSV = tt -> {
		KozmetickiTretman.TipTretmana tipTretmana = new KozmetickiTretman.TipTretmana();
		
		tipTretmana.setNaziv(tt[0]);
		tipTretmana.setCijena(Float.parseFloat(tt[1]));
		tipTretmana.setTrajanje(Integer.parseInt(tt[2]));
	    return tipTretmana;
	};
	
	
	
	@Override
	public KozmetickiTretman.TipTretmana getDeletedInstance() { return DELETED; }
	
	
	//a treatment that belongs to a deleted treatment won't be ignored
	public void loadData(DefaultDict<String, KozmetickiTretman> tretmaniIds) throws IOException{
		ArrayList<String[]> loadedData = DataProvider.loadFromCsv(super.getFilePath(), DataProvider.CSV_DELIMITER);
		ArrayList<KozmetickiTretman.TipTretmana> initializedData = new ArrayList<>();
		//KozmetickiTretman deletedKozmetickiTretman = tretmaniIds.getDefaultValue();
		super.setData(new Data<>(initializedData));
		
		loadedData.forEach(tt -> {
			KozmetickiTretman.TipTretmana tipTretmana = FROM_CSV.convert(tt);
			initializedData.add(tipTretmana);
				
			tipTretmana.setTretman(tretmaniIds.get(tt[3]));
			
			//KozmetickiTretman tretman = tretmaniIds.get(tt[3]);
			
			//if(tretman != deletedKozmetickiTretman) {
			//	tipTretmana.setTretman(tretman);
			//}
		});
	}
	
	
	public void saveData(Function<KozmetickiTretman, String> getKozmetickiTretmanId) throws IOException{
		ArrayList<String[]> convertedData = new ArrayList<>();
		
		super.getData().list().forEach(tipTretmana -> {
			String[] tt = TO_CSV.convert(tipTretmana);
			convertedData.add(tt);
			
			tt[3] = getKozmetickiTretmanId.apply(tipTretmana.getTretman());
		});
		
		DataProvider.writeToCsv(convertedData, super.getFilePath(), DataProvider.CSV_DELIMITER);
	}

}
