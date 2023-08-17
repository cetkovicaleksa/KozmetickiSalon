package crudMenadzeri;

import java.io.IOException;

import dataProvajderi.KozmeticarProvider;
import dataProvajderi.KozmetickiTretmanProvider;
import entiteti.Kozmeticar;
import entiteti.KozmetickiTretman;
import helpers.DefaultDict;
import helpers.Settings;

public class CjenovnikMenadzer {

	public static void main(String[] args) {
		KozmetickiTretmanProvider ktp = new KozmetickiTretmanProvider();
		ktp.setNewIdFunction(x -> {
			if(x == KozmetickiTretmanProvider.DELETED) {
				return "";
			}
			
			return Settings.KOZMETICKI_TRETMAN_ID.apply(x);
		});
		ktp.setFilePath("resources/tretmani.txt");
		
		KozmeticarProvider kp = new KozmeticarProvider();
		kp.setNewIdFunction(x -> {
			if(x == KozmeticarProvider.DELETED) {
				return "";
			}
			
			return Settings.KOZMETICAR_ID.apply(x);
		});
		kp.setFilePath("resources/kozmeticari.txt");
		
		
		DefaultDict<String, KozmetickiTretman> d = new DefaultDict<>(() -> new KozmetickiTretman());
		//d.put("naziv", t);
		
		Kozmeticar k = new Kozmeticar();
		k.setKorisnickoIme("aki");
		
		Kozmeticar k1 = new Kozmeticar();
		k1.setKorisnickoIme("ana");
		
		Kozmeticar k3 = new Kozmeticar();
		k3.setKorisnickoIme("a");
		
		KozmetickiTretman t = new KozmetickiTretman("naziv", "opis");
		k.addTretman(t);
		k.addTretman(t);
		k.addTretman(t);
		k.addTretman(t);
		
		kp.post(k);
		kp.post(k1);
		kp.post(k3);
		//ktp.post(t);
		
		try{
			//kp.saveData(ktp::getId);;
			//kp.loadData(d);
			ktp.loadData();
		}catch(IOException e) {
			System.out.println("done");
		}
		
		System.out.println("");
		
	}
}
