package dataProvajderi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.function.Function;

import helpers.DefaultDict;
import helpers.Query;
import helpers.Updater;
import entiteti.Entitet;

/**
 * An abstract base class for implementing data providers that handle entities of type <T>.
 * The DataProvider provides basic CRUD (Create, Read, Update, Delete) operations for managing
 * a collection of entities, which can be stored in various data structures such as a List or Map.
 * Concrete subclasses should implement methods to convert data between string representation
 * and the specific data structure used to store the entities.
 *
 * @param <T> The type of entities managed by this DataProvider. It should implement the Entitet marker interface.
 * @param <I> The type of the identifier used to uniquely identify the entities when using a Map for quick acess.
 */
public abstract class DataProvider<T extends Entitet, I> implements IsProvider<T> {
	
	/**
	 * The data that stores entities.
	 * 
	 * @see DataProvider.Data*/
	private Data<I, T> data;
	
	/**Function from entity to its natural id.*/
	private Function<T, String> idFunction;
	
	private String filePath;
	public final static String DELETED_ID = "";
	public static final String CSV_DELIMITER = "\t";
	public final static String CSV_INNER_DELIMITER = ",";
	public static final String ID_DELIMITER = "_";
	
	{
		setData(defaultData());
	}
	
	public DataProvider() {}
	
	public DataProvider(Function<T, String> idFunction, String filePath) {
		setIdFunction(idFunction);
		setFilePath(filePath);
	}
	
	protected Data<I, T> getData(){
		return this.data;
	}
	
	protected void setData(Data<I, T> newData) {
		this.data = newData;
	}
		
	protected Function<T, String> getIdFunction(){
		return this.idFunction;
	}
	
	protected void setIdFunction(Function<T, String> newIdFunction){
		this.idFunction = newIdFunction;
	}
	
	/**Set the idFunction while checking if it creates unique ids for already existent entities.
	 * 
	 * @param newIdFunction new function
	 * @throws IdNotUniqueException if the ids are not unique with the new function*/
	public void setNewIdFunction(Function<T, String> newIdFunction) throws IdNotUniqueException {
		if( !DELETED_ID.equals(newIdFunction.apply(getDeletedInstance())) ) {
			//TODO: throw new IllegalArgumentException("");
		}
		
		Iterator<T> iterator = get();
		
		while (iterator.hasNext()) {
	        String entityId = newIdFunction.apply(iterator.next());
	        
	        if(!isIdUnique(entityId)) {
	        	throw new IdNotUniqueException(
	        			"The provided function does not provide unique IDs for all entities that exist in the provider."
	        			);
	        }  
	    }
		
		setIdFunction(newIdFunction);
	}
	
	public String getFilePath(){
		return this.filePath;
	}
	
	public void setFilePath(String newFilePath){
		this.filePath = newFilePath;
	}
	
		
	/**Concrete class should override this method if it wants to change the default way entites are stored.
	 * If it does so, it should make sure that the other <IsProvider> methods will work with it.
	 * If not, any of the <IsProvider> methods should be overriden.*/
	protected Data<I, T> defaultData(){
		return new Data<I, T>(new ArrayList<>());
	}
	
	

	@Override
	public List<T> get(Query<T> selector) {
		ArrayList<T> found = new ArrayList<>();
		Iterator<T> iterator = get();
		
		while(iterator.hasNext()) {
			T entitet = iterator.next();
			if(selector.test(entitet) == true) {
				found.add(entitet);
			}
		}

		return found;
	}
	
	
	@Override
	public Iterator<T> get() {
		if(getData().isList()) {
			return getData().list().listIterator();
		}
		
		if(getData().isCollection()) {
			return getData().collection().iterator();
		}
		
		throw new ProviderCompatibilityException("The default get all method is only compatible with collections.");
	}
	
	
	@Override
	public boolean put(Query<T> selector, Updater<T> updater) throws IdNotUniqueException{
		if(!getData().isList()) {
			throw new ProviderCompatibilityException("The default put method is only compatible with lists.");
		}
		
		class Backup implements Iterable<Backup.Node>{ //maby paramatrize the class???
			Node head;
			Node tail;
			
			Backup(){}
											
			class Node{
				ListIterator<T> iterator;
				T entitet;
				Node next = null;
			
			   Node(ListIterator<T> iter, T ent){
				   iterator = iter;
				   entitet = ent;
			   }
			   
			   public void undo() {
					iterator.set(entitet);
				}
			}
			
			public void add(ListIterator<T> iter, T ent) {
				Node node = new Node(iter, ent);
				
				if(head == null) {
					head = tail = node;
				}else {
					tail = tail.next = node;
				}
				
			}

			@Override
			public Iterator<Node> iterator() {
				return new BackupIterator();
			}
			
			class BackupIterator implements Iterator<Backup.Node>{
				Node current;
				
				BackupIterator(){
					current = null;
				}

				@Override
				public boolean hasNext() {
					return head != null && ( current == null || current.next != null );
				}

				@Override
				public Node next() {
					current = (current == null) ? head : current.next;
					return current;
				}
				
			}
			
			
		}
		
		Function<T, String> idFunction = getIdFunction();
		ListIterator<T> iterator = (ListIterator<T>) get(); //zbog backup-a koristimo listiterator
		Backup backup = new Backup();
		boolean ret = false;
		
		//prolazimo kroz sve entitete i trazimo onaj koji zadovoljava selektor
		while (iterator.hasNext()) {
			T entitet = iterator.next();
			
			if(!selector.test(entitet)) {
				continue;
			}
			
			//nasli smo entitet koji zadovoljava selektor
			ret |= true;
			String originalId = idFunction.apply(entitet);
			//pravimo backup (u slucaju da novi natural id entiteta nije jedinstven) i updejtujemo entitet
			backup.add(iterator, copyEntity(entitet));
			updater.update(entitet);
			
			String newId = idFunction.apply(entitet);
			//ako se id nije promjenio preskacemo provjeru jedinstvenosti
			if(newId.equals(originalId) || isIdUnique(newId)) {
				continue;
			}
			
			//novi id nije jedinstven
			//vracamo sve entitete u pocetno stanje
			for(Backup.Node bp : backup) {
				bp.undo();
			}
			
			throw new IdNotUniqueException(
					"The id of entity with ID: " + originalId + 
					" is not unique in the provider after updating. [" + newId + "].\nNo entities were updated."
					); 
			}
		
		return ret;
	}
	
	
	@Override
	public void post(T entitet) throws IdNotUniqueException {
		if(getDeletedInstance().equals(entitet)) {
			throw new UnsupportedOperationException("Can't post the deleted instance.");
		}
		
		if(!getData().isCollection()) {
			throw new ProviderCompatibilityException("The default post method is only compatible with collections.");
		}
		
		Collection<T> data = getData().collection();
		String id = getIdFunction().apply(entitet);
		
		if(isIdUnique(id)) {
			data.add(entitet);
			return;
		}
				
		throw new IdNotUniqueException(
				"The natural ID of the entity is not unique in the provider. ID:[" + id + "]"
				);		
	}
	
	
	@Override
	public boolean delete(Query<T> selector) {
		if(!getData().isList()) {
			throw new ProviderCompatibilityException("The default delete method is only compatible with lists.");
		}
		
		ListIterator<T> iterator = (ListIterator<T>) get();
		boolean ret = false;
		
		while ( iterator.hasNext() ) {
			if( selector.test( iterator.next() ) ) {
				iterator.remove();
				ret |= true;
			}			
		}	
		
		return ret;
	}
	
	@Override
	public boolean delete(T entitet) {  //sceptical about this
		if(!getData().isList()) {
			throw new ProviderCompatibilityException("The default delete method is only compatible with lists.");
		}
		
		return getData().list().remove(entitet);
	}
	
	
	@Override
	public String getId(T entitet) {  //TODO: problem when entity is not in the collection??!! add custom id for deleted that will be the same for all entities
		Iterator<T> iterator = get();
		while(iterator.hasNext()) {
			if(iterator.next() == entitet) {
				return getIdFunction().apply(entitet);
			}
		}
		
		return DELETED_ID;
	}
	
	
	@Override
	public T getById(String id) {
		Function<T, String> idFunction = getIdFunction();
		Iterator<T> iterator = get();
		
		while(iterator.hasNext()) {
			T entitet = iterator.next();
			
			if(idFunction.apply(entitet).equals(id)) {
				return entitet;
			}
		}
		
		return getDeletedInstance();
	}
	
	@Override
	public DefaultDict<String, T> getIds() {
		DefaultDict<String, T> ids = new DefaultDict<>(this::getDeletedInstance);
		Function<T, String> idFunction = getIdFunction();
		Iterator<T> iterator = get();
		
		while(iterator.hasNext()) {
			T entitet = iterator.next();
			ids.put(idFunction.apply(entitet), entitet);
		}
		
		return ids;
	}
	
	/**Returns the reference to the only instance of entity that represents a deleted entity.
	 * 
	 * @return the instance
	 * */
	public abstract T getDeletedInstance();
	
	/**Checks if the given id is unique in the provider.
	 * This means that there is at most one entity with the given id.
	 * 
	 * @param id the string id of an entity that may or may not exist in the provider*/
	protected boolean isIdUnique(String id) {
		Iterator<T> iterator = get();
		Function<T, String> idFunction = getIdFunction();
		
		if(id == null || idFunction.apply(getDeletedInstance()).equals(id)) {
			return false;
		}
		
		boolean foundMatch = false;
		
		while(iterator.hasNext()) {			
			if( !idFunction.apply(iterator.next()).equals(id) ) {
				continue;
			}
			
			if(foundMatch) {
				return false;
			}
			
			foundMatch = true; // will only happen once
		}
		
		return true;
	}
		
	
	/***/
	public void loadData() throws IOException{
		ArrayList<String[]> lista = DataProvider.loadFromCsv(getFilePath(), CSV_DELIMITER);
		setData( convertStringToData(lista) );
	};
	
	/***/
	public void saveData() throws IOException{
		ArrayList<String[]> lista = convertDataToString(getData());
		DataProvider.writeToCsv(lista, getFilePath(), CSV_DELIMITER);
	}
	
	/***/
	protected abstract Data<I, T> convertStringToData(ArrayList<String[]> stringData);
	/***/
	protected abstract ArrayList<String[]> convertDataToString(Data<I, T> data);
	
	
	
	
	
	//loading and saving strings to file
	
	protected static void writeToCsv(Iterator<String[]> entityStrings, String path, String delimiter) throws IOException{
		ArrayList<String[]> lista = new ArrayList<>();
		while(entityStrings.hasNext()) {
			lista.add(entityStrings.next());
		}
		
		DataProvider.writeToCsv(lista, path, delimiter);
	}
	
	public static void writeToCsv(List<String[]> entityStrings, String path, String delimiter) throws IOException {
		File file = new File(path);
		if(!file.exists()) {
			try {
				file.createNewFile();
			}catch(IOException e) {   //maby change this
				throw e;
			}
		}
		    	
    	try ( BufferedWriter bw = new BufferedWriter(new FileWriter(file)) ) {
    
    		for ( String[] entityFields : entityStrings ) {
                String line = String.join(delimiter, entityFields);
                bw.write(line);
                bw.newLine();
            }
    		} catch (IOException e) { throw e; }    	    	
    }
    
	protected static ArrayList<String[]> loadFromCsv(String path, String delimiter) throws IOException {
		File file = new File(path);
		if(!file.exists()) {
			throw new IOException("File doesn't exist! [ " + path + " ]");
		}
       
    	ArrayList<String[]> data = new ArrayList<>();
        try ( BufferedReader br = new BufferedReader(new FileReader(file)) ) {
            String line;

            while ( (line = br.readLine()) != null ) {
                String[] entityFields = line.split(delimiter);
                data.add(entityFields);
            }
        } catch (IOException e) { throw e; }

        return data;
    }
	//---
	
	public T copyEntity(T entity) {
		//TODO: finish
		return entity;		
	}
	
	
	
	
	protected static enum DataType{
		LIST, COLLECTION, MAP, COLLECTION_AND_MAP, LIST_AND_MAP, NO_DATA
	}
	
	/***/
	protected static class Data<K, V extends Entitet> {
		private Collection<V> collection;
		private Map<K, V> map;
		
		protected Data(){}
		
		protected Data(Collection<V> data){
			collection = data;
		}
		
		protected Data(Map<K, V> data){
			map = data;
		}
		
		protected Data(Collection<V> collection, Map<K, V> map){
			this.collection = collection;
			this.map = map;
		}
		
		protected void setData(Collection<V> data) {			
			collection = data;
		}
		
		protected void setData(Map<K, V> data) {			
			map = data;
		}
		
		protected Collection<V> collection(){
			if(isCollection()) {
				return collection;
			}
			
			return null;
		}
		
		protected List<V> list(){
			if(isList()) {
				return (List<V>) collection;
			}
			
			return null; //TODO: throw exception
		}
		
		protected Map<K, V> map(){
			if(isMap()) {
				return map;
			}
			
			return null; //TODO: exception?
		}
		
		protected boolean isCollection() {
			return collection != null;
		}
		
		protected boolean isList() {
			return collection != null && collection instanceof List;
		}
		
		protected boolean isMap() {
			return map != null;
		}
		
		protected boolean is() {
			return collection != null || map != null;
		}
		
		protected DataType type() {
			if(collection == null && map == null) {
				return DataType.NO_DATA;				
			}
			
			if(collection != null && map != null) {
				return (collection instanceof List) ? DataType.LIST_AND_MAP : DataType.COLLECTION_AND_MAP;
			}
			
			if(collection == null) {
				return DataType.MAP;
			}
			
			return (collection instanceof List) ? DataType.LIST : DataType.COLLECTION;
		}
		
	}
		
}
