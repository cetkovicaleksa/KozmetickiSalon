package entiteti;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Cjenovnik {
	
	private HashMap<String, ArrayList<KozmetickiTretman.TipTretmana>> prices; // id kozmetickiTretman : list of tipTretmana prices
	// could be private ArrayList<HashMap<String, HashMap<String, float>>>  // list of cjenovnik key treatment : treatment type id : price
	
	public Cjenovnik() {}
	
	public Cjenovnik(HashMap<String, ArrayList<KozmetickiTretman.TipTretmana>> prices) {
		setPrices(prices);
	}
	
	public Cjenovnik(Iterator<KozmetickiTretman.TipTretmana> tretmani) {
		
	}

	
	public HashMap<String, ArrayList<KozmetickiTretman.TipTretmana>> getPrices() {
		return prices;
	}

	public void setPrices(HashMap<String, ArrayList<KozmetickiTretman.TipTretmana>> prices) {
		this.prices = prices;
	}
	
	//TODO: FINISH THIS

}
