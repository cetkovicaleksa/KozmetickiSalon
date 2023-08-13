package helpers;

public class Paths {
	
	private static Paths instance;
	
	private String klijenti, kozmeticari, menadzeri, 
	recepcioneri, kozmetickiTretmaniFilePath, tipoviKozmetickihTretmana,
	zakazaniTretman;
	
	private Paths() {
		
	}
	
	public static Paths getInstance() {
		if (instance == null) {
			instance = new Paths();
		}
		
		return instance;
	}

}
