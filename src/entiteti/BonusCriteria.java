package entiteti;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import helpers.Query;
import helpers.ThreeArgumentFunction;

public class BonusCriteria {
	
	private Set<String> specialEmployeeUsernames; //these employees will have bonus despite the other criteria
	
	private boolean ignoreIfHadBonusLastTime;
	
	private int godineStazaThreshold;
	private NivoStrucneSpreme nivoStrucneSpremeThreshold;
	
	private double bazaPlateMin;
	private double bazaPlateMax;
	
	
	//for kozmeticari
	private int inTheLastNumberOfDays; //everything below is for the last inTleLastNumberOfDays days
		
	private int numberOfCompletedTreatmentsThreshold;
	private double moneyEarnedThreshold;
	
	
	public BonusCriteria() { // TODO just recheck if the default value of primitive numbers is 0, and boolean is false
		specialEmployeeUsernames = new HashSet<>();
		nivoStrucneSpremeThreshold = NivoStrucneSpreme.SKOLE_BEZ;
		bazaPlateMax = Double.MAX_VALUE;
		inTheLastNumberOfDays = Integer.MAX_VALUE; // all time
	}
	
	public BonusCriteria(Set<String> specialEmployeeUsernames, boolean ignoreIfHadBonusLastTime, int godineStazaThreshold, 
			NivoStrucneSpreme nivoStrucneSpremeThreshold, double bazaPlateMin, double bazaPlateMax, int inTheLastNumberOfDays,
			int numberOfCompletedTreatmentsThreshold, double moneyEarnedThreshold) {
		this.specialEmployeeUsernames = specialEmployeeUsernames;
		this.ignoreIfHadBonusLastTime = ignoreIfHadBonusLastTime;
		this.godineStazaThreshold = godineStazaThreshold;
		this.nivoStrucneSpremeThreshold = nivoStrucneSpremeThreshold;
		this.bazaPlateMin = bazaPlateMin;
		this.bazaPlateMax = bazaPlateMax;
		this.inTheLastNumberOfDays = inTheLastNumberOfDays;
		this.numberOfCompletedTreatmentsThreshold = numberOfCompletedTreatmentsThreshold;
		this.moneyEarnedThreshold = moneyEarnedThreshold;
	}
	
	public Query<Zaposleni> getEmployeeCriteria() {
		Query<Zaposleni> primaryQuery = new Query<>(
				
				(specialEmployeeUsernames.isEmpty() ? 
						zaposleni -> false : 
						zaposleni -> specialEmployeeUsernames.contains(zaposleni.getKorisnickoIme())						
				)									
		);
		
		
		Query<Zaposleni> secondaryQuery = new Query<>();
		
		if(ignoreIfHadBonusLastTime) { 
			secondaryQuery.i(zaposleni -> !zaposleni.hasBonus()); // if employee had bonus last time he/she won't have it now
		}
		
		if(godineStazaThreshold > 0) {
			secondaryQuery.i(zaposleni -> zaposleni.getGodineStaza() >= godineStazaThreshold);
		}
		
		if(nivoStrucneSpremeThreshold != NivoStrucneSpreme.SKOLE_BEZ) {
			secondaryQuery.i(zaposleni -> zaposleni.getNivoStrucneSpreme().getValue() >= nivoStrucneSpremeThreshold.getValue());
		}
		
		if(bazaPlateMin > 0) {
			secondaryQuery.i(zaposleni -> zaposleni.getBazaPlate() >= bazaPlateMin);
		}
		
		if(bazaPlateMax != Double.MAX_VALUE) {
			secondaryQuery.i(zaposleni -> zaposleni.getBazaPlate() <= bazaPlateMax);
		}
		
		
		return new Query<>(zaposleni -> 
				primaryQuery.test(zaposleni) || secondaryQuery.test(zaposleni)
		);
	}
	
	
	public Query<Zaposleni> getEmployeeCriteria(ThreeArgumentFunction<Kozmeticar, LocalDate, LocalDate, KozmeticarIzvjestaj> izvjestajForKozmeticarFunction){
		Query<Zaposleni> employeeQuery = getEmployeeCriteria();
		
		Query<Kozmeticar> kozmeticarQuery = new Query<Kozmeticar>(kozmeticar -> {
			
			KozmeticarIzvjestaj izvjestaj = izvjestajForKozmeticarFunction.apply(
					kozmeticar, LocalDate.now().minusDays(inTheLastNumberOfDays), LocalDate.now()
					);		
					
			return (numberOfCompletedTreatmentsThreshold <= izvjestaj.getTotalNumberOfTreatments() 
					&& moneyEarnedThreshold <= izvjestaj.getTotalMoneyEarned());
		});
		
		
		return new Query<>(employee -> // TODO you can use the query methods but do not trust them, they need recheck
		 employeeQuery.test(employee) && ( !(employee instanceof Kozmeticar) || kozmeticarQuery.test((Kozmeticar) employee)) );		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	public Set<String> getSpecialEmployeeUsernames() {
		return specialEmployeeUsernames;
	}

	public void setSpecialEmployeeUsernames(Set<String> specialEmployeeUsernames) {
		this.specialEmployeeUsernames = specialEmployeeUsernames;
	}

	public boolean isIgnoreIfHadBonusLastTime() {
		return ignoreIfHadBonusLastTime;
	}

	public void setIgnoreIfHadBonusLastTime(boolean ignoreIfHadBonusLastTime) {
		this.ignoreIfHadBonusLastTime = ignoreIfHadBonusLastTime;
	}

	public int getGodineStazaThreshold() {
		return godineStazaThreshold;
	}

	public void setGodineStazaThreshold(int godineStazaThreshold) {
		this.godineStazaThreshold = godineStazaThreshold;
	}

	public NivoStrucneSpreme getNivoStrucneSpremeThreshold() {
		return nivoStrucneSpremeThreshold;
	}

	public void setNivoStrucneSpremeThreshold(NivoStrucneSpreme nivoStrucneSpremeThreshold) {
		this.nivoStrucneSpremeThreshold = nivoStrucneSpremeThreshold;
	}

	public double getBazaPlateMin() {
		return bazaPlateMin;
	}

	public void setBazaPlateMin(double bazaPlateMin) {
		this.bazaPlateMin = bazaPlateMin;
	}

	public double getBazaPlateMax() {
		return bazaPlateMax;
	}

	public void setBazaPlateMax(double bazaPlateMax) {
		this.bazaPlateMax = bazaPlateMax;
	}

	public int getInTheLastNumberOfDays() {
		return inTheLastNumberOfDays;
	}

	public void setInTheLastNumberOfDays(int inTheLastNumberOfDays) {
		this.inTheLastNumberOfDays = inTheLastNumberOfDays;
	}

	public int getNumberOfCompletedTreatmentsThreshold() {
		return numberOfCompletedTreatmentsThreshold;
	}

	public void setNumberOfCompletedTreatmentsThreshold(int numberOfCompletedTreatmentsThreshold) {
		this.numberOfCompletedTreatmentsThreshold = numberOfCompletedTreatmentsThreshold;
	}

	public double getMoneyEarnedThreshold() {
		return moneyEarnedThreshold;
	}

	public void setMoneyEarnedThreshold(double moneyEarnedThreshold) {
		this.moneyEarnedThreshold = moneyEarnedThreshold;
	}































	public static class KozmeticarIzvjestaj{		
		private LocalDate beginingDate;
		private LocalDate endDate;
		
		private Kozmeticar kozmeticar;
		
		private Map<StatusTretmana, Integer> numbersOfTreatments;
		private Map<StatusTretmana, Number> moneyEarnedByTreatmentStatus;
		

		public KozmeticarIzvjestaj() {}
		
		public KozmeticarIzvjestaj(
				LocalDate beginingDate, LocalDate endDate, Kozmeticar kozmeticar, 
				Map<StatusTretmana, Integer> numbersOfTreatments, Map<StatusTretmana, Number> moneyEarnedByTreatmentStatus) {
			setBeginingDate(beginingDate);
			setEndDate(endDate);
			setKozmeticar(kozmeticar);
			setNumbersOfTreatments(numbersOfTreatments);
			setMoneyEarnedByTreatmentStatus(moneyEarnedByTreatmentStatus);
		}
		
		
		public int getNumberOfTreatments(StatusTretmana status) {
			Integer number = getNumbersOfTreatments().get(status);
			return (number == null ? 0 : number);
		}
		
		public double getMoneyEarned(StatusTretmana status) {
			Number moneyEarned = getMoneyEarnedByTreatmentStatus().get(status);
			return (moneyEarned == null ? 0 : moneyEarned.doubleValue());
		}
		
		public int getTotalNumberOfTreatments() {
			int numberOfTreatments = 0;
			for(Integer number : getNumbersOfTreatments().values()) {
				numberOfTreatments += number;
			}
			
			return numberOfTreatments;
		}
		
		public double getTotalMoneyEarned() {
			double moneyEarned = 0;
			for(Number number : getMoneyEarnedByTreatmentStatus().values()) {
				moneyEarned += number.doubleValue();
			}
			
			return moneyEarned;
		}
		
		
		public LocalDate getBeginingDate() {
			return beginingDate;
		}

		public void setBeginingDate(LocalDate beginingDate) {
			this.beginingDate = beginingDate;
		}

		public LocalDate getEndDate() {
			return endDate;
		}

		public void setEndDate(LocalDate endDate) {
			this.endDate = endDate;
		}

		public Kozmeticar getKozmeticar() {
			return kozmeticar;
		}

		public void setKozmeticar(Kozmeticar kozmeticar) {
			this.kozmeticar = kozmeticar;
		}

		public Map<StatusTretmana, Integer> getNumbersOfTreatments() {
			return numbersOfTreatments;
		}

		public void setNumbersOfTreatments(Map<StatusTretmana, Integer> numbersOfTreatments) {
			this.numbersOfTreatments = numbersOfTreatments;
		}

		public Map<StatusTretmana, Number> getMoneyEarnedByTreatmentStatus() {
			return moneyEarnedByTreatmentStatus;
		}

		public void setMoneyEarnedByTreatmentStatus(Map<StatusTretmana, Number> moneyEarnedByTreatmentStatus) {
			this.moneyEarnedByTreatmentStatus = moneyEarnedByTreatmentStatus;
		}

		
		@Override
		public int hashCode() {
			return new Integer[] {
					getKozmeticar().hashCode(),
					getBeginingDate().hashCode(),
					getEndDate().hashCode()
			}.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if(obj == null) {
				return false;
			}
			
			if(obj instanceof KozmeticarIzvjestaj) {
				KozmeticarIzvjestaj ki = (KozmeticarIzvjestaj) obj;
				return getKozmeticar().equals(ki.getKozmeticar()) && 
					   getBeginingDate().equals(ki.getBeginingDate()) && 
					   getEndDate().equals(ki.getEndDate());
			}
			
			return false;
		}

		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return super.toString();
		}
	}
	
	
	
}
