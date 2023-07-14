package dataProvajderi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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

public abstract class DataManager<T extends Entitet, I> implements IsProvider<T> {
	
	private Data<I, T> data;	
	private Function<T, String> idFunction;
	
	private String filePath;
	private final String csvDelimiter = ",";
	
	{
		setData(defaultData());
	}
	
	public DataManager() {}
	
	public DataManager(Function<T, String> idFunction, String filePath) {
		setIdFunction(idFunction);
		setFilePath(filePath);
	}
	
	private Data<I, T> getData(){
		return this.data;
	}
	
	private void setData(Data<I, T> newData) {
		this.data = newData;
	}
		
	protected Function<T, String> getIdFunction(){
		return this.idFunction;
	}
	
	protected void setIdFunction(Function<T, String> newIdFunction){
		this.idFunction = newIdFunction;
	}
	
	public void newIdFunction(Function<T, String> newIdFunction) {
		Iterator<T> iterator = get();
		
		while (iterator.hasNext()) {
	        String entityId = newIdFunction.apply(iterator.next());
	        
	        if(!isIdUnique(entityId)) {
	        	return; //TODO: throw exception because ids are not unique with new id function
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
	
	public String getCsvDelimiter(){
		return this.csvDelimiter;
	}
		
	/***/
	protected Data<I, T> defaultData(){
		return new Data<>(new ArrayList<>());
	}
	
	

	@Override
	public List<T> get(Query<T> selektor) {
		ArrayList<T> found = new ArrayList<>();
		Iterator<T> iterator = get();
		
		while(iterator.hasNext()) {
			T entitet = iterator.next();
			if(selektor.test(entitet) == true) {
				found.add(entitet);
			}
		}

		return found;
	}
	
	
	@Override
	public Iterator<T> get() { //maby should improve safety
		if(getData().isList()) {
			return getData().list().listIterator();
		}
		
		if(getData().isCollection()) {
			return getData().collection().iterator();
		}
		
		return null;  //TODO: throw exception bc not overriden
	}
	
	
	@Override
	public void put(Query<T> selektor, Updater<T> updater) {
		if(!getData().isList()) {
			return; //TODO: throw exception
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
		
		//prolazimo kroz sve entitete i trazimo onaj koji zadovoljava selektor
		while (iterator.hasNext()) {
			T entitet = iterator.next();
			
			if(!selektor.test(entitet)) {
				continue;
			}
			
			//nasli smo entitet koji zadovoljava selektor			
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
			
			return; //TODO: throw exception because new id is not unique
		}
	
	}
	
	
	@Override
	public void post(T entitet) {
		if(!getData().isCollection()) {
			return; //TODO: throw exception
		}
		
		Collection<T> data = getData().collection();
		String id = getIdFunction().apply(entitet);
		
		if(isIdUnique(id)) {
			data.add(entitet);
			return;
		}
				
		return; //TODO: id not unique, throw exxception		
	}
	
	
	@Override
	public void delete(Query<T> selektor) {
		if(!getData().isList()) {
			return; //TODO: Throw exceptino
		}
		
		ListIterator<T> iterator = (ListIterator<T>) get();
		
		while ( iterator.hasNext() ) {
			if( selektor.test( iterator.next() ) ) {
				iterator.remove();
			}			
		}		
	}
	
	
	@Override
	public String getId(T entitet) { //returns an id even if it doesn't exist in the collection
		return getIdFunction().apply(entitet); 
	}
	
	
	@Override
	public T getById(String id) {
		Function<T, String> idFunction = getIdFunction();
		Iterator<T> iterator = get();
		
		while(iterator.hasNext()) {
			T entitet = iterator.next();
			
			if(id.equals(idFunction.apply(entitet))) {
				return entitet;
			}
		}
		
		return getDeletedInstance();
	}
	
	@Override
	public DefaultDict<String, T> getIds() {
		DefaultDict<String, T> ids = new DefaultDict<>(() -> getDeletedInstance());
		Function<T, String> idFunction = getIdFunction();
		Iterator<T> iterator = get();
		
		while(iterator.hasNext()) {
			T entitet = iterator.next();
			ids.put(idFunction.apply(entitet), entitet);
		}
		
		return ids;
	}
	
	protected abstract T getDeletedInstance();
	
	
	protected boolean isIdUnique(String id) {
		Iterator<T> iterator = get();
		Function<T, String> idFunction = getIdFunction();
		int numOfRepeats = 0;
		
		while(iterator.hasNext()) {
			String checkEntityId = idFunction.apply(iterator.next());
			
			if(!id.equals(checkEntityId)) {
				continue;
			}
			
			if(++numOfRepeats > 1) {
				return false;				
			}
		}
		
		return true;
	}
		
	
		
	public void loadData() throws IOException{
		setData( convertStringToData(DataManager.loadFromCsv(getFilePath(), getCsvDelimiter())) );
	};
	
	public void saveData() throws IOException{
		DataManager.writeToCsv(convertDataToString(getData()), getFilePath(), getCsvDelimiter());
	}
	
	protected abstract Data<I, T> convertStringToData(ArrayList<String[]> stringData);
	protected abstract ArrayList<String[]> convertDataToString(Data<I, T> data);
	
	
	
	
	
	//loading and saving strings to file
	
	protected static void writeToCsv(Iterator<String[]> entityStrings, String path, String delimiter) throws IOException{
		ArrayList<String[]> lista = new ArrayList<>();
		while(entityStrings.hasNext()) {
			lista.add(entityStrings.next());
		}
		DataManager.writeToCsv(lista, path, delimiter);
	}
	
	protected static void writeToCsv(List<String[]> entityStrings, String path, String delimiter) throws IOException {
    	
    	try ( BufferedWriter bw = new BufferedWriter(new FileWriter(path)) ) {
            
    		for ( String[] entityFields : entityStrings ) {
                String line = String.join(delimiter, entityFields);
                bw.write(line);
                bw.newLine();
            }
    		} catch (IOException e) { throw e; }
    	    	
    }
    
	protected static ArrayList<String[]> loadFromCsv(String path, String delimiter) throws IOException {
       
    	ArrayList<String[]> data = new ArrayList<>();

        try ( BufferedReader br = new BufferedReader(new FileReader(path)) ) {
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
		return null;		
	}
	
	
	
	
	protected enum DataType{
		LIST, COLLECTION, MAP, NO_DATA, COLLECTION_AND_MAP, LIST_AND_MAP
	}
	
	protected class Data<K, V extends Entitet> {
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
