package entiteti.blueprints;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

import entiteti.Entitet;
import entiteti.Klijent;
import helpers.Query;
import helpers.Updater;

public abstract class Blueprint<T extends Entitet> implements IBlueprint<T>{
	
	private final HashSet<Field<?>> fields;
	{
		@SuppressWarnings("serial")
		class AkiSet<E> extends HashSet<E>{
			@Override
			public boolean add(E e) {
				if (contains(e)) {
					remove(e);
			    }
			    return super.add(e);
			}
		}
		
		this.fields = new AkiSet<>();
	}	
	
	public Blueprint() {}
	
	//shallow copies all the values from entitet to make new blueprint
	public Blueprint(T entitet) {
		Class<?> clazz = entitet.getClass();
		
		for(String fieldName : getAllFieldNames()) {
			try {
				Method getter = clazz.getMethod(fieldName);
				this.add(fieldName, getter.invoke(entitet));
				
			} catch (NoSuchMethodException | SecurityException e) {
				// TODO Auto-generated catch block
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
			}		
		}
	}
	
	private HashSet<Field<?>> getFields(){
		return this.fields;
	}
		
	protected abstract T getNewEntity();
	protected abstract String[] getAllFieldNames();
	
	protected void add(String name, Object value) {
		getFields().add(new Field<>(name, value));
	}
	
	public T constructEntity() {
		T entitet = getNewEntity();
		
		for(Field<?> field : getFields()) {
			try {
				Method setter = entitet.getClass().getMethod(field.getSetterName(), field.type());
				setter.invoke(entitet, field.getValue());
			}catch(Exception e) {
				//should handle various exceptions TODO
			}
		}
		return entitet;
	};
	
	/**Returns a query that tests positive if an entity matches the blueprint.*/
	public Query<T> getQueryToMatch(){
		Query<T> query = new Query<>();
		
		for(Field<?> field : getFields()) {
			try {
				
				Method getter = getNewEntity().getClass().getMethod(field.getGetterName());
				Object value = field.getValue();
				
				query.i( x -> {
					try {
						return Relations.equals(getter.invoke(x), value);
					}catch(InvocationTargetException | IllegalAccessException | IllegalArgumentException e) {
						return false;
					}
				});
				
			}catch(NoSuchMethodException e) {
				//handle all exceptions!!
			}
		}
		
		return query;
	}
	
	public Updater<T> getUpdaterToMatch(){
		class Setter<X>{
			public Method method;
			public X value;
			
			public Setter(Method method, X value) {
				this.method = method;
				this.value = value;
			}
			
			public void update(T entitet) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
				this.method.invoke(entitet, this.value);
			}
		}
		
		Updater<T> updater = new Updater<>();
		ArrayList<Setter<?>> setters = new ArrayList<>();
		Class<?> clazz = getNewEntity().getClass();
		
		for(Field<?> field : getFields()) {
			try{
				Method setter = clazz.getMethod(field.getSetterName(), field.type());
				setters.add(new Setter<>(setter, field.getValue()));
			}catch(NoSuchMethodException e) {
				//TODO
			}
		}
		
		updater.addThingsToBeChanged((entitet) -> {
			
			for(Setter<?> s : setters) {
				try {
					s.update(entitet);
				}catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					//id what to do?
				}
			}
		});
		
		return updater;
	};

	
	
	
	static class Relations{
		
		public static boolean equals(Object x, Object criteria) {
			return x.equals(criteria);
		}
		
		public static boolean lowerThan(Number x, Number criteria) {
			return x.doubleValue() < criteria.doubleValue();			
		}
		
		public static boolean lowerThan(LocalDate x, LocalDate criteria) {
			return x.compareTo(criteria) < 0;
		}
		
		public static boolean lowerThan(LocalDateTime x, LocalDateTime criteria) {
			return x.compareTo(criteria) < 0;
		}
		
		public static boolean greaterThan(Comparable<Object> x, Comparable<Object> criteria) {
			return x.compareTo(criteria) > 0;
		}
		
		public static boolean greaterThan(LocalDate x, LocalDate criteria) {
			return x.compareTo(criteria) > 0;
		}
		
			
	}
	
	
	
	
	protected class Field<V>{
		private String name;
		private V value;
		
		@SuppressWarnings("unused")
		private Field() {}
		
		public Field(String name, V value) {
			this.name = name;
			this.value = value;
		}
		
		public String getName() {
			return this.name;
		}
		
		public V getValue() {
			return this.value;
		}
		
		public void setValue(V newValue) { //allows to change to subclass value
			this.value = newValue;
		}
		
		public Class<?> type(){
			return getValue().getClass();
		}
		
		public String getGetterName() {
			return "get" + name();
		}
		
		public String getSetterName() {
			return "set" + name();
		}
		
		private String name() {
			return Character.toUpperCase(getName().charAt(0)) + getName().substring(1);
		}
		
		@Override
		public int hashCode() {
			return Objects.hashCode(getName());
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null || obj.getClass() != getClass()) {
				return false;
			}			
			Field<?> other = (Field<?>) obj;
			if (other.type() != type()) {
				return false;
			}
			return other.getName().equals(getName());
		}	
		
	}
		
		
		
	public static void main(String[] args) {
		
		Klijent aleksa = new Klijent();
		aleksa.setPrezime("cetkovic");
		aleksa.setHasLoyaltyCard(true);
	
		KlijentBlueprint bp = new KlijentBlueprint();
		
		
		System.out.println(bp.getClass());
		//System.out.println(bp.getFields());
		bp.ime("Aleksa").ime("Ana");
		//System.out.println(bp.getFields());
		Klijent ana = bp.constructEntity();
		System.out.println(ana.getIme() +"|"+ ana.getPrezime());
		System.out.println(ana);
		
		
		Query<Klijent> tester = bp.getQueryToMatch().ne().ne().akko((e) -> e != aleksa).clearQuery().ne();//.ili((e) -> e == ana);
		aleksa.setIme("Aleksa");
		
		System.out.println(tester.test(ana));
		System.out.println(tester.test(aleksa));
		//System.out.println(bp.getFields());
	}
		
	

}
