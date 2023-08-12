package dataProvajderi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Function;

import entiteti.KozmetickiTretman;
import helpers.DefaultDict;

public class TipTretmanaProvider extends XDataProvider<KozmetickiTretman.TipTretmana, String>{
		
	public static final KozmetickiTretman.TipTretmana DELETED = KozmetickiTretmanProvider.DELETED_TIP_TRETMANA;
	
	
	@Override
	public KozmetickiTretman.TipTretmana getDeletedInstance() { return DELETED; }
	
	
	//a treatment that belongs to a deleted treatment won't be ignored
	public void loadData(DefaultDict<String, KozmetickiTretman> tretmaniIds) throws IOException{
		ArrayList<String[]> loadedData = DataProvider.loadFromCsv(super.getFilePath(), DataProvider.CSV_DELIMITER);
		ArrayList<KozmetickiTretman.TipTretmana> initializedData = new ArrayList<>();
		//KozmetickiTretman deletedKozmetickiTretman = tretmaniIds.getDefaultValue();
		super.setData(new Data<>(initializedData));
		
		loadedData.forEach(tt -> {
			KozmetickiTretman.TipTretmana tipTretmana = new KozmetickiTretman.TipTretmana();
			initializedData.add(tipTretmana);
			
			tipTretmana.setNaziv(tt[0]);
			tipTretmana.setCijena(Float.parseFloat(tt[1]));
			tipTretmana.setTrajanje(Integer.parseInt(tt[2]));
	
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
			String[] tt = new String[4];
			convertedData.add(tt);
			
			tt[0] = tipTretmana.getNaziv();
			tt[1] = Float.toString(tipTretmana.getCijena());
			tt[2] = Integer.toString(tipTretmana.getTrajanje());
			tt[3] = getKozmetickiTretmanId.apply(tipTretmana.getTretman()); //TODO: see what to do when it is deleted?
		});
		
		DataProvider.writeToCsv(convertedData, super.getFilePath(), DataProvider.CSV_DELIMITER);
		
	}

}
