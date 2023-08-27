package crudMenadzeri;

import java.io.IOException;

import dataProvajderi.SalonProvider;
import entiteti.Dan;
import entiteti.Salon;
import helpers.Updater;

public class SalonMenadzer {
	
	private SalonProvider salonProvider;
	private Salon salon;
	
	
	public SalonMenadzer() {}
	
	public SalonMenadzer(SalonProvider salonProvider) {
		setSalonProvider(salonProvider);
		if((salon = salonProvider.getSalon()) == null){
			salonProvider.setSalon((salon = new Salon()));
		}
	}
	
	public SalonProvider getSalonProvider() {
		return salonProvider;
	}

	public void setSalonProvider(SalonProvider salonProvider) {
		this.salonProvider = salonProvider;
	}
	
	
	
	public void create(Salon salon) {
		getSalonProvider().setSalon((this.salon = salon));
	}
	
	public Salon read() {
		return salon;
	}
	
	public void update(Updater<Salon> updater) {
		updater.update(salon);
	}
	
	public void delete() {
		getSalonProvider().setSalon((salon = new Salon()));
	}
	
	
	
	public boolean addWorkingDay(Dan dan) {
		return salon.getWorkingDays().add(dan);
	}
	
	public boolean removeWorkingDay(Dan dan) {
		return salon.getWorkingDays().remove(dan);
	}
	
	
	
	public void addExpense(Number newExpense) {
		salon.setExpenses(salon.getExpenses() + newExpense.doubleValue());
	}
	
	public void addIncome(Number newIncome) {
		salon.setIncome(salon.getIncome() + newIncome.doubleValue());
	}
	
	
	
	
	
	
	//saving and loading data
	
	public void loadData() throws IOException {
		getSalonProvider().loadData();
		salon = getSalonProvider().getSalon();
	}
	
	public void saveData() throws IOException {
		getSalonProvider().saveData();
	}

}
