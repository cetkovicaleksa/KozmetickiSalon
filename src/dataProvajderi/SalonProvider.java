package dataProvajderi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import entiteti.BonusCriteria;
import entiteti.Dan;
import entiteti.NivoStrucneSpreme;
import entiteti.Salon;
import helpers.Converter;

public class SalonProvider {
	
	private Salon salon;
	private String salonPath;
	
	private static final String NO_DAYS = "NODAYS";
	private static final String NO_USERNAMES = "NOSPECIALUSERS";
	
	public static final Converter<Salon, String[]> TO_CSV = salon -> {
		return new String[] {
				salon.getNaziv(), Integer.toString(salon.getOpeningHour()),
				Integer.toString(salon.getClosingHour()), workingDaysToCSV(salon.getWorkingDays()),
				Double.toString(salon.getIncome()), Double.toString(salon.getExpenses()), 
				Double.toString(salon.getLoyaltyCardThreshold()), Double.toString(salon.getLoyaltyCardDiscount())				
		};
	};
	
	public static final Converter<String[], Salon> FROM_CSV = s -> {
		return new Salon(
				s[0], Integer.parseInt(s[1]),
				Integer.parseInt(s[2]), workingDaysFromCSV(s[3]),
				Double.parseDouble(s[4]), Double.parseDouble(s[5]),
				Double.parseDouble(s[6]), Double.parseDouble(s[7]), null
		);
	};
	
	
	
	public static final Converter<BonusCriteria, String[]> CRITERIA_TO_CSV = criteria -> {
		Set<String> usernames = criteria.getSpecialEmployeeUsernames();
		String usernamesString = (usernames.isEmpty() ? NO_USERNAMES : String.join(DataProvider.CSV_INNER_DELIMITER, usernames));
		
		return new String[] {
				Boolean.toString(criteria.isIgnoreIfHadBonusLastTime()),
				Integer.toString(criteria.getGodineStazaThreshold()),
				criteria.getNivoStrucneSpremeThreshold().name(),
				Double.toString(criteria.getBazaPlateMin()),
				Double.toString(criteria.getBazaPlateMax()),
				Integer.toString(criteria.getInTheLastNumberOfDays()),
				Integer.toString(criteria.getNumberOfCompletedTreatmentsThreshold()),
				Double.toString(criteria.getMoneyEarnedThreshold()),
				
				usernamesString
		};
	};
	
	public static final Converter<String[], BonusCriteria> CRITERIA_FROM_CSV = c -> {
		BonusCriteria criteria = new BonusCriteria();
		
		criteria.setIgnoreIfHadBonusLastTime(Boolean.valueOf(c[0]));
		criteria.setGodineStazaThreshold(Integer.parseInt(c[1]));
		criteria.setNivoStrucneSpremeThreshold(NivoStrucneSpreme.valueOf(c[2]));
		criteria.setBazaPlateMin(Double.parseDouble(c[3]));
		criteria.setBazaPlateMax(Double.parseDouble(c[4]));
		criteria.setInTheLastNumberOfDays(Integer.parseInt(c[5]));
		criteria.setNumberOfCompletedTreatmentsThreshold(Integer.parseInt(c[6]));
		criteria.setMoneyEarnedThreshold(Double.parseDouble(c[7]));
		
		criteria.setSpecialEmployeeUsernames(
				new HashSet<>(
						Arrays.asList( c[8].split(DataProvider.CSV_INNER_DELIMITER) )
				)
		);
		
		return criteria;
	};
	
	
	
	public SalonProvider() {}
	
	public SalonProvider(String salonPath) {
		this(new Salon(), salonPath);
	}
	
	public SalonProvider(Salon salon, String path) {
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
	

	
	
	
	private static String workingDaysToCSV(Set<Dan> workingDays) {
		String[] days = new String[workingDays.size()];
		
		if(days.length == 0) {
			return NO_DAYS;
		}
		
		int index = 0;
		for(Dan day : workingDays) {
			days[index++] = day.name();
		}
		
		return String.join(DataProvider.CSV_INNER_DELIMITER, days);
	}
	
	private static SortedSet<Dan> workingDaysFromCSV(String csvValue){
		SortedSet<Dan> days = new TreeSet<>();
		
		if(NO_DAYS.equals(csvValue)) {
			return days;
		}
		
		String[] daysStrings = csvValue.split(DataProvider.CSV_INNER_DELIMITER);
		for(String dayString : daysStrings) {
			days.add(Dan.valueOf(dayString));
		}
		
		return days;
	}
	

	
	
	public void saveData() throws IOException {
		ArrayList<String[]> dataToSave = new ArrayList<>();
		
		dataToSave.add(TO_CSV.convert(this.salon));
		
		BonusCriteria criteria = this.salon.getBonusCriteria();
		dataToSave.add( CRITERIA_TO_CSV.convert(criteria == null ? new BonusCriteria() : criteria) );
		
		DataProvider.writeToCsv(dataToSave, getSalonPath(), DataProvider.CSV_DELIMITER);
	}
	
	
	private Salon loadSalon() throws IOException{
		List<String[]> loadedData = DataProvider.loadFromCsv(salonPath, DataProvider.CSV_DELIMITER);
		
		Salon salon = FROM_CSV.convert(loadedData.get(0));	
		
		String[] criteriaStrings = loadedData.size() != 2 ? new String[0] : loadedData.get(1);		
		salon.setBonusCriteria(criteriaStrings.length == 0 ? new BonusCriteria() : CRITERIA_FROM_CSV.convert(criteriaStrings));		
		
		return salon;
	}
	
	public void loadData() throws IOException {
		setSalon( loadSalon() );
	}

}
