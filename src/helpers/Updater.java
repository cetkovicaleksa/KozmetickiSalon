package helpers;

import java.lang.reflect.InvocationTargetException;

import exceptions.IncompatibleUpdaterException;
import exceptions.NoPayloadDataException;
//import java.lang.reflect.Method;

/***/
public class Updater<T> implements IsUpdater<T>{
	
	/**setter ce pozivati setere objekta <T> koji se proslijedi metodi update(T entity)*/
	private IsUpdater<T> setter;
	
	public Updater(){}
	
	public Updater(IsUpdater<T> updater) {
		setSetter(updater);
	}

	public IsUpdater<T> getSetter() { return this.setter; }
	private void setSetter(IsUpdater<T> setter) { this.setter = setter;	}
	

	@Override
	public void update(T entity) throws NoPayloadDataException, IncompatibleUpdaterException {
		if (getSetter() == null) throw new NoPayloadDataException();
		try {
			getSetter().update(entity);
		}catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException e) {
			throw new IncompatibleUpdaterException("", e);
		}
	}
	
	public void updateIgnore(T entity){
		if (getSetter() == null) return;
		getSetter().update(entity);
	}
	
	
	
	public Updater<T> clearUpdater() {
		setSetter(null);
		return this;
	}
	
	/**Metoda koja veze trenutni setter sa novo-unjetim setterom.
	 * Omogucava naknadno dodavanje atributa klase <T> koji se mjenjaju metodom update.
	 * @param noviSetter*/	
	public void addThingsToBeChanged(IsUpdater<T> noviSetter) {
		if (getSetter() == null) {
			setSetter(noviSetter);
			return;
		}
		
		IsUpdater<T> stariSetter = getSetter();
		setSetter( entity -> 
		{
			stariSetter.update(entity);
			noviSetter.update(entity);
			return;
		} );
		
		/*Prvi stack overflow experience :)
		 * 

		setSetter(entity -> {
			getSetter().update(entity);
			noviSetter.update(entity);
			return;
		});
		
		*/
		
		
	}
	
	
	
	
}