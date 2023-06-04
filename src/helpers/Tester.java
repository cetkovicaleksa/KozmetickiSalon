package helpers;

import java.util.function.Predicate;

/**Pomocna klasa koja se koristi za pretragu korisnika i 
 * ostalih entiteta. Provjerava da li prosledjeni objekat 
 * klase T ispunjava uslove zadate filterom.*/
public class Tester<T> {
	
	private Predicate<T> filter;
	
	Tester(){}
	
	/**Konstruktor.
	 * @param filter referenca na objekat koji overajduje test() metodu funkcionalnog interfejsa Predicate.*/	
	public Tester(Predicate<T> filter) {
		this.filter = filter;
	}
	
	public boolean test(T aki) {
		return this.filter.test(aki);		
	}
	
	/**Metoda koja omogucava promjenu trenutnog filtera.
	 * Medjutim filter je i danlje nad istom klasom <T>.*/
	public void setFilter(Predicate<T> filter) {
		this.filter = filter;
	}
	
	/**Metoda koja negira trenutni filter.*/
	public void ne() {
		this.filter = this.filter.negate();
	}
	
	/**Metoda koja kombinuje trenutni filter sa datim argumentom.
	 * Novi filter predstavlja logicko i ova dva filtera.
	 * @param drugiFilter mora biti kompatabilan sa trenutnim filterom*/
	public void i(Predicate<T> drugifilter) {
		this.filter = this.filter.and(drugifilter);
	}
	
	/**Metoda koja kombinuje trenutni filter sa datim argumentom.
	 * Novi filter predstavlja logicko ili ova dva filtera.
	 * @param drugiFilter mora biti kompatabilan sa trenutnim filterom*/
	public void ili(Predicate<T> drugiFilter) {
		this.filter = this.filter.or(drugiFilter);
	}
	
	/**Metoda koja kombinuje trenutni filter sa datim argumentom.
	 * Novi filter predstavlja logicko nili ova dva filtera.	 * 
	 * @param drugiFilter mora biti kompatabilan sa trenutnim filterom
	 * 
	 * A xor B <=> (not A and B) or (A and not B)*/
	public void nili(Predicate<T> drugiFilter) {
		            //       not A                 B                A                    not B     
		this.filter = this.filter.negate().and(drugiFilter).or( this.filter.and(drugiFilter.negate()) );
		
	}
	
	
	
	
	public static void main(String[] args) {
		Tester<Integer> testerTest = new Tester<>(x -> x > 10);
		
		int aki = 20 + 10;
		int ana = 18;
		
		System.out.println("Aki: " + testerTest.test(aki));
		System.out.println("Ana: " + testerTest.test(ana));
		
		testerTest.nili(x -> x < 20);
		testerTest.i(x -> x == 30);
		
		
		
		System.out.println("Aki: " + testerTest.test(aki));
		System.out.println("Ana: " + testerTest.test(ana));
		
		testerTest.ne();
		
		System.out.println("Aki: " + testerTest.test(aki));
		System.out.println("Ana: " + testerTest.test(ana));
		
		
		
		
	}
}




