package entiteti;

import java.util.SortedSet;

public class Salon implements Entitet{

	private String naziv;
	
	private int openingHour;
	private int closingHour;
	private SortedSet<Dan> workingDays;
	
	private double income;
	private double expenses;
	
	//loyalty card for clients
	private double loyaltyCardThreshold;
	private double loyaltyCardDiscount;
	
	//for employees
	private BonusCriteria bonusCriteria;	
	
	
	// TODO Add constructors
	
	
	
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
