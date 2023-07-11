package dataProvajderi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.lang.ClassCastException;

import helpers.DefaultDict;
import helpers.Query;
import helpers.Updater;

public abstract class DataManager<T> implements IsProvider<T> {
	
	private Collection<?> data;
	private IsProvider<T> providerLogic;
	private Function<T, String> idFunction;
	
	private String filePath;
	private final String csvDelimiter = ",";
	
	
	public DataManager() {}
	
	
	
	
	private Collection<?> getData(){
		return this.data;
	}
	
	private void setData(Collection<?> newData) {
		this.data = newData;
	}
	
	protected IsProvider<T> getProviderLogic(){
		return this.providerLogic;
	}
	
	protected void setProviderLogic(IsProvider<T> newProviderLogic) {
		this.providerLogic = newProviderLogic;
	}
	
	protected Function<T, String> getIdFunction(){
		return this.idFunction;
	}
	
	protected void setIdFunction(Function<T, String> newIdFunction){
		this.idFunction = newIdFunction;
	}
	
	protected void newIdFunction(Function<T, String> newIdFunction) {
		Iterator<T> iter1 = getProviderLogic().get();
		
		while (iter1.hasNext()) {
	        T entity1 = iter1.next();
	        String id1 = newIdFunction.apply(entity1);
	        
	        Iterator<T> iter2 = getProviderLogic().get();
	        
	        while (iter2.hasNext()) {
	            T entity2 = iter2.next();
	            
	            if (entity1 == entity2) {
	                continue;
	            }
	            
	            if (id1.equals(newIdFunction.apply(entity2))) {
	                // TODO: throw an exception because entities' IDs are not unique with the new natural ID
	                return;
	            }
	        }
	    }
		
		setIdFunction(newIdFunction);
	    
	}
	
	
	
	
	
	
	
	{
		setData(pickCollection());
		try {
			Supplier<Collection<?>> getData = () -> this.getData();  //TODO: recheck if this calls the getters every time the anonymous func is called
			Supplier<Function<T, String>> getIdFunction = () -> this.getIdFunction();
			
			setProviderLogic(pickProviderLogic(getData, getIdFunction));			
			
		}catch(Exception e){
			
		}
	}
	
	/***/
	protected Collection<?> pickCollection() {
		return new ArrayList<T>();
	}
	
	/***/
	protected IsProvider<T> pickProviderLogic(Supplier<Collection<?>> getData, Supplier<Function<T, String>> getIdFunction){  //this method could acess the getData and getIdFunction without passing in arguments, but methods that override this one won't be able to
		//TODO: cast to arraylist bc default is arraylist		
		return new IsProvider<T>() {  
			private Supplier<Collection<?>> data = getData;
			private Supplier<Function<T, String>> idFunction = getIdFunction;
			
			public Collection<?> getData(){
				return this.data.get();
			}
			
			public Function<T, String> getIdFunction(){
				return this.idFunction.get();
			}

			@Override
			public List<T> get(Query<T> selektor) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Iterator<T> get() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void put(Query<T> selektor, Updater<T> updater) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void post(T entitet) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void delete(Query<T> selektor) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public String getId(T entitet) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public T getById(String id) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public DefaultDict<String, T> getIds() {
				// TODO Auto-generated method stub
				return null;
			}
			
		};
	}
	
	
	
	@Override
	public List<T> get(Query<T> selektor) {
		return getProviderLogic().get(selektor);
	}
	@Override
	public Iterator<T> get() {
		return getProviderLogic().get();
	}
	@Override
	public void put(Query<T> selektor, Updater<T> updater) {
		getProviderLogic().put(selektor, updater);		
	}
	@Override
	public void post(T entitet) {
		getProviderLogic().post(entitet);		
	}
	@Override
	public void delete(Query<T> selektor) {
		getProviderLogic().delete(selektor);		
	}
	@Override
	public String getId(T entitet) {
		return getProviderLogic().getId(entitet);
	}
	@Override
	public T getById(String id) {
		return getProviderLogic().getById(id);
	}
	@Override
	public DefaultDict<String, T> getIds() {
		return getProviderLogic().getIds();
	}
	
	
	
	
		
}
