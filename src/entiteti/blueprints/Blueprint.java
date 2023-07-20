package entiteti.blueprints;

import java.util.ArrayList;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import entiteti.Entitet;
import helpers.Query;
import helpers.IsUpdater;
import helpers.Updater;

public abstract class Blueprint<T extends Entitet> implements IBlueprint<T>{
	
	private Query<T> query; // = new Query<>();
	private Updater<T> updater; // = new Updater<>();
	
	private boolean forQuery = false; // if we are not building for query nor we are building for updater, we are building for entity
	private boolean forUpdater = false;

	private final ArrayList<Function<T, Void>> builder = new ArrayList<>();
	private BiFunction<Predicate<T>, Query<T>, Query<T>> currentCombiningLogic; //for adding new conditions to query
	
	{
		and();  // sets the currentCombiningLogic to be logical and
	}
	
	public Blueprint() {}	
	
	
	
	protected Query<T> getQuery() {
		return query;
	}
	
	protected void setQuery(Query<T> query) {
		this.query = query;
	}
	

	protected Updater<T> getUpdater() {
		return updater;
	}
	
	protected void setUpdater(Updater<T> updater) {
		this.updater = updater;
	}
	

	protected BiFunction<Predicate<T>, Query<T>, Query<T>> getCurrentCombiningLogic() {
		return currentCombiningLogic;
	}
	
	protected void setCurrentCombiningLogic(BiFunction<Predicate<T>, Query<T>, Query<T>> currentCombiningLogic) {
		this.currentCombiningLogic = currentCombiningLogic;
	}

	
	protected ArrayList<Function<T, Void>> getBuilder() {
		return builder;
	}
	
	
	
	protected abstract T getNewEntity();
	
	
	
	protected boolean isForQuery() {
		return forQuery;
	}
	
	protected boolean isForUpdater() {
		return forUpdater;
	}
	
	protected boolean isForBuilder() {
		return !(forQuery || forUpdater);
	}
	
	//all the methods that use the builder pattern should be overriden in concrete classes to ensure that the return type is more precise
	public Blueprint<T> forUpdater() {
		if(getUpdater() == null) {
			setUpdater(new Updater<>());
		}
		
		forUpdater |= true;
		return this;
	}
	
	public Blueprint<T> forQuery() {
		if(getQuery() == null){
			setQuery(new Query<>());
		}
		
		forQuery |= true;
		return this;
	}
	
	
	public Blueprint<T> notForUpdater() {
		forUpdater &= false;
		return this;
	}
	
	public Blueprint<T> notForQuery() {
		forQuery &= false;
		return this;
	}
	
	
	
	@Override
	public T build() {
		T entity = getNewEntity();
		getBuilder().forEach( (setter) -> setter.apply(entity) );
		return entity;
	}
	
	@Override
	public Query<T> query(){
		return getQuery();
		//setQuery(new Query<>()); //TODO: deepcopy the query
	}
	
	@Override
	public Updater<T> updater(){
		return getUpdater();
		//TODO: deepcopy the updater
	}
	
	
	
	//see if this overriding will work?!
	protected void add(Function<T, Void> setter) {
		getBuilder().add(setter);
	}
	
	protected void add(IsUpdater<T> function) {
		getUpdater().addThingsToBeChanged(function);
	}
	
	protected void add(Predicate<T> relation) {
		getCurrentCombiningLogic().apply(relation, getQuery());
	}
	
	
	
	public void and() {
		setCurrentCombiningLogic((newCondition, query) -> CombiningLogic.and(newCondition, query));		
	}
	public void or() {
		setCurrentCombiningLogic((newCondition, query) -> CombiningLogic.or(newCondition, query));
	}
	public void xor() {
		setCurrentCombiningLogic((newCondition, query) -> CombiningLogic.xor(newCondition, query));
	}
	public void equivalent() {
		setCurrentCombiningLogic((newCondition, query) -> CombiningLogic.equivalent(newCondition, query));
	}
	
	public void reset() {
	}
	
	
	
	@SuppressWarnings("unused")
	private static class CombiningLogic{
		
		static <T> Query<T> and(Predicate<T> newCondition, Query<T> query){
			return query.i(newCondition);
		}
		
		static <T> Query<T> or(Predicate<T> newCondition, Query<T> query){
			return query.ili(newCondition);
		}
		
		static <T> Query<T> xor(Predicate<T> newCondition, Query<T> query){
			return query.nili(newCondition);
		}
		
		static <T> Query<T> equivalent(Predicate<T> newCondition, Query<T> query){
			return query.akko(newCondition);
		}
		
		static <T> Query<T> isImplied(Predicate<T> newCondition, Query<T> query){
			return query.dodajPotrebanUslov(newCondition);
		}
		
		static <T> Query<T> implies(Predicate<T> newCondition, Query<T> query){
			return query.dodajDovoljanUslov(newCondition);
		}
	}
	
	
	
	
	public static class Field<E extends Entitet, V>{
		private String name;  //name of the field, which is used to get the getter and setter methods (assumes conventional naming)
		private V value;  //the value of the field that can be any type
		private Class<E> clazz; //to know that the Field is for the right entity class
		private BiFunction<V, V, Boolean> relation; //only for tester purposes
		
		
		public Field() {}
		
		public Field(String name, V value) {
			setName(name);
			setValue(value);
		}
		
		public Field(String name, V value, BiFunction<V, V, Boolean> relation) {
			this(name, value);
			setRelation(relation);
		}
		
		public String getName() {
			return this.name;
		}
		public void setName(String newName) {
			this.name = newName;
		}
		
		public V getValue() {
			return this.value;
		}
		public void setValue(V newValue) {
			this.value = newValue;
		}
		
		public BiFunction<V, V, Boolean> getRelation(){
			return this.relation;
		}
		public void setRelation(BiFunction<V, V, Boolean> newRelation) {
			this.relation = newRelation;
		}
		
		
		
		public Class<V> getValueClass(){
			return Field.getValueClass(getValue());
		}
		
		public String getGetterName() {
			return "get" + Field.name(getName());
		}
		
		public String getSetterName() {
			return "set" + Field.name(getName());
		}
		
		public boolean isForTester() {
			return getRelation() == null;
		}
		
		public boolean isForSetter() {
			return ! isForTester();
		}
		
		//in the outer class???
		public Predicate<V> getTester(Class<V> clazz) {
			if(isForTester()) {
				
			}
			
			return null;
		}
		
		public Updater<V> getSetter(Class<V> clazz) {
			if(isForSetter()) {
				
			}
			
			return null;
		}

		
		private static <T> Class<T> getValueClass(T value){
			return (Class<T>) value.getClass();
		}
		
		private static String name(String fieldName) {
			return Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
		}
		
	}
	

}
