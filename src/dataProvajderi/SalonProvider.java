package dataProvajderi;

import java.io.IOException;
import java.util.Collections;

import entiteti.BonusCriteria;
import entiteti.Salon;
import helpers.Converter;

public class SalonProvider {
	
	private Salon salon;
	private String salonPath;
	private String criteriaPath;
	
	public static final Converter<Salon, String[]> TO_CSV = salon -> {
		return new String[] {
				""
		};
	};
	
	public static final Converter<String[], Salon> FROM_CSV = s -> {
		return new Salon(); //TODO implement converters
	};
	
	public static final Converter<BonusCriteria, String[]> CRITERIA_TO_CSV = criteria -> {
		return null;
	};
	
	public static final Converter<String[], BonusCriteria> CRITERIA_FROM_CSV = c -> {
		return null;
	};
	
	
	
	public SalonProvider() {}
	
	public SalonProvider(String salonPath, String criteriaPath) {
		this(new Salon(), salonPath, criteriaPath);
	}
	
	public SalonProvider(Salon salon, String path, String criteriaPath) {
		setSalon(salon);
		setSalonPath(path);
	}
	
	
	public Salon getSalon() {
		return salon;
	}
	
	public void setSalon(Salon salon) {
		this.salon = salon;
	}
	
	public String getSalonPath() {
		return salonPath;
	}
	
	public void setSalonPath(String path) {
		this.salonPath = path;
	}
	
	public String getCriteriaPath() {
		return criteriaPath;
	}

	public void setCriteriaPath(String criteriaPath) {
		this.criteriaPath = criteriaPath;
	}

	
	
	public void saveData() throws IOException {
		DataProvider.writeToCsv(Collections.singletonList(TO_CSV.convert(getSalon())), salonPath, DataProvider.CSV_DELIMITER);
		DataProvider.writeToCsv(Collections.singletonList( CRITERIA_TO_CSV.convert(getSalon().getBonusCriteria()) ), getCriteriaPath(), DataProvider.CSV_DELIMITER);
	}
	
	
	private Salon loadSalon() throws IOException{
		Salon salon = FROM_CSV.convert(DataProvider.loadFromCsv(salonPath, DataProvider.CSV_DELIMITER).get(0));	
		salon.setBonusCriteria(CRITERIA_FROM_CSV.convert(DataProvider.loadFromCsv(getCriteriaPath(), DataProvider.CSV_DELIMITER).get(0)));
		
		return salon;
	}
	
	public void loadData() throws IOException {
		setSalon( loadSalon() );
	}

}
