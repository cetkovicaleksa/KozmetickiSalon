package crudMenadzeri;

import java.io.IOException;
import java.util.Iterator;
import java.util.function.Function;

import dataProvajderi.DataProvider;
import dataProvajderi.KlijentProvider;
import dataProvajderi.RecepcionerProvider;
import entiteti.Klijent;
import helpers.Query;
import helpers.Updater;

public abstract class Manager {
	
	
	public static void test() {
		Function<Klijent, String> f = k -> (k.getIme() == null) ? DataProvider.DELETED_ID : k.getIme();
		
		//KlijentProvider kp = new KlijentProvider(f, "");
		kp.setNewIdFunction(f);
		kp.setFilePath("resources/test.txt");
		
		try {
			kp.loadData();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Iterator<Klijent> iter = kp.get();
		while (iter.hasNext()) {
			System.out.println(iter.next().getIme());
		}
		System.out.println(kp.get(new Query<>(x -> true)).size());
	}
	
	public static void main(String[] args) {
		test();
		
		Klijent aki = new Klijent();
		aki.setIme("Aleksa");
		Klijent ana = new Klijent();
		ana.setIme("Ana");
		Klijent m = new Klijent();
		m.setIme("M");
		Klijent t = new Klijent();
		t.setIme("T");
		
		Function<Klijent, String> f = k -> (k.getIme() == null) ? DataProvider.DELETED_ID : k.getIme();
		System.out.println(f.apply(aki));
		KlijentProvider kp = new KlijentProvider();
		kp.setFilePath("resources/test.txt");
		kp.setNewIdFunction(f);
		
		kp.post(aki);
		//kp.post(new Klijent());
		kp.post(ana);
		kp.post(m);
		System.out.println(kp.getId(ana));
		Iterator<Klijent> iter = kp.get();
		while(iter.hasNext()) {
			System.out.println(iter.next().getIme());
		}
		kp.post(t);
		iter = kp.get();
		//kp.post(t); iter is not usable if we change the data after it has been initialized: throws concurent exception
		
		while(iter.hasNext()) {
			System.out.println(iter.next().getIme());
		}
		
		System.out.println("____");
		kp.put(new Query<>(k -> k == aki), new Updater<>());
		iter = kp.get();
		kp.put(new Query<>(k -> true | k == ana), new Updater<>(k -> k.setPrezime("Cetkovic")));
		while(iter.hasNext()) {
			System.out.println(iter.next().getPrezime());
		}
		
		try {
			kp.saveData();
		}catch (IOException e){
			System.out.println("Uh, oh!");
			
		}

				
		
		
	}
	
}
