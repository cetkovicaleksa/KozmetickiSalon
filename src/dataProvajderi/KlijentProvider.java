package dataProvajderi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Function;

import entiteti.Klijent;
import helpers.DefaultDict;

public class KlijentProvider extends Provider<Klijent> {
	
	final String delimiter = ",";
	
	@Override
	public void loadData() throws IOException {
		
		for(String[] klijentString : super.loadFromCsv(super.getPath(), delimiter)) {
			//make klijent instance from array of strings
			Klijent klijent = new Klijent();
			super.getData().add(klijent);
		}
		
	}

	@Override
	public void saveData() throws IOException {  //maby could have one saveData that calls a function that will convert entity to string

		ArrayList<String[]> stringData = new ArrayList<>();
		
		
		for(Klijent klijent : super.getData() ) {
			//TODO: convert each client into string with delimiter
		}
		
		super.writeToCsv(stringData, super.getPath(), delimiter);
		
	}

	@Override  //TODO: vidi moze li u provideru da bude ovo ako svi entiteti imaju getDeleted metodu??
	public DefaultDict<String, Klijent> getIds() {
		DefaultDict<String, Klijent> dict = new DefaultDict<>(() -> null);//Klijent.getDeletedKlijent());
		Function<Klijent, String> idMaker = super.getNaturalId();
		
		for(Klijent klijent : super.getData()) {
			dict.put(idMaker.apply(klijent), klijent);
		}
		// TODO Auto-generated method stub
		return dict;
	}
	

}
