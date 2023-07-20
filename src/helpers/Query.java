package helpers;

import java.util.function.Predicate;

/**Pomocna klasa koja se koristi za pretragu korisnika i 
 * ostalih entiteta. Provjerava da li prosledjeni objekat 
 * klase <T> ispunjava uslove zadate filterom.*/
public class Query<T> implements Predicate<T>{
	
	private static final Predicate<?> NULL_FILTER = (x) -> true;
	private Predicate<T> filter = getNullFilter();
	
	public Query(){}
		
	public Query(Predicate<T> filter) {
		setFilter(filter);
	}
	
	public boolean isNull() {
		return getFilter() == Query.<T>getNullFilter();
	}
	
	@Override
	public boolean test(T entity){
		return getFilter().test(entity);	
	}
	
	
	private void setFilter(Predicate<T> filter) {
		this.filter = filter;
	}
	public Predicate<T> getFilter(){
		return this.filter;
	}


	@SuppressWarnings("unchecked")
	private static <X> Predicate<X> getNullFilter(){
		return (Predicate<X>) NULL_FILTER;
	}
	
	public Query<T> clearQuery() {
		setFilter(getNullFilter());
		return this;
	}
	
	//logicke kombinacije filtera sa novo-unjetim filterima
	
	/**Metoda koja negira trenutni filter.*/
	public Query<T> ne() {
		setFilter( getFilter().negate() );
		//if(getFilter() != getNullFilter()) {
			//setFilter( getFilter().negate() );			
		//}
		
		return this;
	}
	
	/**Metoda koja kombinuje trenutni filter sa datim argumentom.
	 * Novi filter predstavlja logicko i ova dva filtera.
	 * @param drugiFilter mora implementirati Predicate<T>*/
	public Query<T> i(Predicate<T> drugiFilter) {
		Predicate<T> newFilter = isNull() ? drugiFilter : getFilter().and(drugiFilter);
		setFilter(newFilter);
		return this;
	}
	
	/**Metoda koja kombinuje trenutni filter sa datim argumentom.
	 * Novi filter predstavlja logicko ili ova dva filtera.
	 * @param drugiFilter mora implementirati Predicate<T>*/
	public Query<T> ili(Predicate<T> drugiFilter) {
		Predicate<T> newFilter = isNull() ? drugiFilter : getFilter().or(drugiFilter);
		setFilter(newFilter);
		return this;
	}
	
	/**Metoda koja kombinuje trenutni filter sa datim argumentom.
	 * Novi filter predstavlja logicko nili ova dva filtera.	 * 
	 * @param drugiFilter mora implementirati Predicate<T>
	 * 
	 * A xor B <=> (not A and B) or (A and not B)*/
	public Query<T>  nili(Predicate<T> drugiFilter) {
		Predicate<T> newFilter = isNull() ? drugiFilter : getFilter().negate().and(drugiFilter).or( getFilter().and(drugiFilter.negate()) );
		setFilter(newFilter);
		return this;
		            //       not A                 B                A                    not B     
		//this.filter = this.filter.negate().and(drugiFilter).or( this.filter.and(drugiFilter.negate()) );
		
	}
		
	/**Metoda koja kombinuje trenutni filter sa unjetim u
	 * iskaz: trenutni filter <=> drugiFilter
	 * @param drugiFilter mora implementirati Predicate<T>
	 * 
	 * x <=> y  <=>  not x and not y  or  x and y*/
	public Query<T> akko(Predicate<T> drugiFilter){
		Predicate<T> newFilter = isNull() ? drugiFilter : getFilter().and(drugiFilter).or( getFilter().negate().and(drugiFilter.negate()) );
		setFilter(newFilter);
		return this;
	}
	
	/**Metoda koja od mijenja filter testera tako sto se unjeti filter i trenutni
	 * filter kombinuju u iskaz: drugiFilter => trenutni filter
	 * @param drugiFilter mora implementirati Predicate<T>
	 * 
	 * x => y  <=>  not x or y*/
	public Query<T> dodajPotrebanUslov(Predicate<T> drugiFilter) {
		Predicate<T> newFilter = isNull() ? drugiFilter : getFilter().or( drugiFilter.negate() );
		setFilter(newFilter);
		return this;
	}
	
	/**Metoda koja od mijenja filter testera tako sto se unjeti filter i trenutni
	 * filter kombinuju u iskaz: trenutni filter => drugiFilter
	 * @param drugiFilter mora implementirati Predicate<T>
	 * 
	 * x => y  <=>  not x or y*/
	public Query<T> dodajDovoljanUslov(Predicate<T> drugiFilter) {
		Predicate<T> newFilter = isNull() ? drugiFilter : getFilter().negate().or(drugiFilter);
		setFilter(newFilter);
		return this;
	}
	
}




