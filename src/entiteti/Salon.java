package entiteti;

import java.util.Arrays;
import java.util.SortedSet;
import java.util.TreeSet;

public class Salon implements Entitet{

	private String naziv;
	
	private int openingHour;
	private int closingHour;
	private SortedSet<Dan> workingDays;
	
	private double income;
	private double expenses;
	
	//loyalty card for clients
	private double loyaltyCardThreshold;
	private double loyaltyCardDiscount; // 0 <= loyaltyCardDiscount <= 1
	
	//for employees
	private BonusCriteria bonusCriteria;	
	
	
	public Salon() {
		workingDays = new TreeSet<>();
		loyaltyCardDiscount = 1;
		bonusCriteria = new BonusCriteria();
	}
	
	public Salon(
			String naziv, int openingHour, int closingHour, SortedSet<Dan> workingDays, 
			double income, double expenses, double loyaltyCardThreshold, 
			double loyaltyCardDiscount, BonusCriteria bonusCriteria) {
		setNaziv(naziv);
		setOpeningHour(openingHour);
		setClosingHour(closingHour);
		setWorkingDays(workingDays);
		setIncome(income);
		setExpenses(expenses);
		setLoyaltyCardThreshold(loyaltyCardThreshold);
		setLoyaltyCardDiscount(loyaltyCardDiscount);
		setBonusCriteria(bonusCriteria);
	}
	
	public Salon(
			String naziv, int openingHour, int closingHour,
			double income, double expenses, double loyaltyCardThreshold, 
			double loyaltyCardDiscount, BonusCriteria bonusCriteria, Dan... workingDays) {
		
		this(naziv, openingHour, closingHour, new TreeSet<>(Arrays.asList(workingDays)), income, expenses, loyaltyCardThreshold, loyaltyCardDiscount, bonusCriteria);
	}
	
	
	
	public String getNaziv() {
		return naziv;
	}
	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}
	public int getOpeningHour() {
		return openingHour;
	}
	public void setOpeningHour(int openingHour) {
		this.openingHour = openingHour;
	}
	public int getClosingHour() {
		return closingHour;
	}
	public void setClosingHour(int closingHour) {
		this.closingHour = closingHour;
	}
	public SortedSet<Dan> getWorkingDays() {
		return workingDays;
	}
	public void setWorkingDays(SortedSet<Dan> workingDays) {
		this.workingDays = workingDays;
	}
	public double getLoyaltyCardThreshold() {
		return loyaltyCardThreshold;
	}
	public void setLoyaltyCardThreshold(double loyaltyCardThreshold) {
		this.loyaltyCardThreshold = loyaltyCardThreshold;
	}
	public double getLoyaltyCardDiscount() {
		return loyaltyCardDiscount;
	}
	public void setLoyaltyCardDiscount(double loyaltyCardDiscount) {
		this.loyaltyCardDiscount = loyaltyCardDiscount;
	}
	public double getIncome() {
		return income;
	}
	public void setIncome(double income) {
		this.income = income;
	}
	public double getExpenses() {
		return expenses;
	}
	public void setExpenses(double expenses) {
		this.expenses = expenses;
	}	
	public BonusCriteria getBonusCriteria() {
		return bonusCriteria;
	}
	public void setBonusCriteria(BonusCriteria bonusCriteria) {
		this.bonusCriteria = bonusCriteria;
	}
}
