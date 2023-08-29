package dataProvajderi;

import java.io.IOException;
import java.util.Collections;
import java.util.TreeSet;

import entiteti.BonusCriteria;
import entiteti.NivoStrucneSpreme;
import entiteti.Salon;
import helpers.Converter;

public class SalonProvider {
	
	private Salon salon;
	private String salonPath;
	private String criteriaPath;
	
	public static final Converter<Salon, String[]> TO_CSV = salon -> {
		return new String[] {
				salon.getNaziv(), Integer.toString(salon.getOpeningHour()),
				Integer.toString(salon.getClosingHour()), Double.toString(salon.getIncome()),
				Double.toString(salon.getExpenses()), Double.toString(salon.getLoyaltyCardThreshold()),
				Double.toString(salon.getLoyaltyCardDiscount())
				
				//(salon.getWorkingDays()).toString()
				
		};
	};
	
	public static final Converter<String[], Salon> FROM_CSV = s -> {
		Salon salon = new Salon();
		
		salon.setNaziv(s[0]);
		salon.setOpeningHour(Integer.parseInt(s[1]));
		salon.setClosingHour(Integer.parseInt(s[2]));
		salon.setIncome(Double.parseDouble(s[3]));
		salon.setExpenses(Double.parseDouble(s[4]));
		salon.setLoyaltyCardThreshold(Double.parseDouble(s[5]));
		salon.setLoyaltyCardDiscount(Double.parseDouble(s[6]));
		
		//salon.setWorkingDays(new TreeSet<>());
		
		return salon;
	};
	
	public static final Converter<BonusCriteria, String[]> CRITERIA_TO_CSV = criteria -> {
		return new String[] {
				Boolean.toString(criteria.isIgnoreIfHadBonusLastTime()),
				Integer.toString(criteria.getGodineStazaThreshold()),
				criteria.getNivoStrucneSpremeThreshold().name(),
				Double.toString(criteria.getBazaPlateMin()),
				Double.toString(criteria.getBazaPlateMax()),
				Integer.toString(criteria.getInTheLastNumberOfDays()),
				Integer.toString(criteria.getNumberOfCompletedTreatmentsThreshold()),
				Double.toString(criteria.getMoneyEarnedThreshold())
				
				//set
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
		//do the set
		
		return criteria;
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
