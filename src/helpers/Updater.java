package helpers;

import java.lang.reflect.InvocationTargetException;

import exceptions.IncompatibleUpdaterException;
import exceptions.NoPayloadDataException;
//import java.lang.reflect.Method;

/***/
public class Updater<T> implements IsUpdater<T>{
	
	private static final IsUpdater<?> NULL_UPDATER = (x) -> {};
	private IsUpdater<T> updater = getNullUpdater();
	
	public Updater(){}
	
	public Updater(IsUpdater<T> updater) {
		setUpdater(updater);
	}

	public IsUpdater<T> getUpdater() {
		return this.updater;
	}
	private void setUpdater(IsUpdater<T> newUpdater) {
		this.updater = newUpdater;
		}
	

	public boolean isNull() {
		return getUpdater() == Updater.getNullUpdater();
	}
	
	@Override
	public void update(T entity) throws NoPayloadDataException, IncompatibleUpdaterException {
		try {
			getUpdater().update(entity);
		}catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException e) {
			throw new IncompatibleUpdaterException("", e);
		}
	}	
	
	public Updater<T> clearUpdater() {
		setUpdater(getNullUpdater());
		return this;
	}
	
	/**Metoda koja veze trenutni setter sa novo-unjetim setterom.
	 * Omogucava naknadno dodavanje atributa klase <T> koji se mjenjaju metodom update.
	 * @param noviSetter*/	
	public void addThingsToBeChanged(IsUpdater<T> noviSetter) {
		if (isNull()) {
			setUpdater(noviSetter);
			return;
		}
		
		IsUpdater<T> stariSetter = getUpdater();
		setUpdater( entity -> 
		{
			stariSetter.update(entity);
			noviSetter.update(entity);
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
	
	
	@SuppressWarnings("unchecked")
	private static <T> IsUpdater<T> getNullUpdater(){
		return (IsUpdater<T>) NULL_UPDATER;
	}
	
}