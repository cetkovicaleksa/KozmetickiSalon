package dataProvajderi;

import java.io.IOException;
import java.util.Collections;

import entiteti.Salon;
import helpers.Converter;

public class SalonProvider {
	
	private Salon salon;
	private String path;
	
	public static final Converter<Salon, String[]> TO_CSV = salon -> {
		return null;
	};
	
	public static final Converter<String[], Salon> FROM_CSV = s -> {
		return null;
	};
	
	
	public SalonProvider() {}
	
	public SalonProvider(Salon salon, String path) {
		setSalon(salon);
		setPath(path);
	}
	
	
	public Salon getSalon() {
		return salon;
	}
	
	public void setSalon(Salon salon) {
		this.salon = salon;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	
	
	
	public void saveData() throws IOException {
		DataProvider.writeToCsv(Collections.singletonList(TO_CSV.convert(getSalon())), path, DataProvider.CSV_DELIMITER);
	}
	
	public void loadData() throws IOException {
		setSalon( FROM_CSV.convert(DataProvider.loadFromCsv(path, DataProvider.CSV_DELIMITER).get(0)) );
	}

}
