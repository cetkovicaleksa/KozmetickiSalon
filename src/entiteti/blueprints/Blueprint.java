package entiteti.blueprints;

import java.util.HashSet;
import java.util.Objects;

import entiteti.Entitet;
import helpers.Query;
import helpers.Updater;

public abstract class Blueprint {
	
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
	
	protected HashSet<Field<?>> getFields(){
		return this.fields;
	}
	
	public abstract Blueprint buildBluePrint();
	public abstract Entitet makeEntitet();
	public abstract Query<Entitet> getQueryToMatch();	
	public abstract Updater<Entitet> getUpdaterToMatch();
	
	protected void add(String name, Object value) {
		getFields().add(new Field<>(name, value));
	}
	
	
	
	
	class Field<T>{
		private String name;
		private T value;
		
		@SuppressWarnings("unused")
		private Field() {}
		
		public Field(String name, T value) {
			this.name = name;
			this.value = value;
		}
		
		public String getName() {
			return this.name;
		}
		
		public T getValue() {
			return this.value;
		}
		
		public void setValue(T newValue) { //allows to change to subclass value
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
		
		
		
		
		
	

}
