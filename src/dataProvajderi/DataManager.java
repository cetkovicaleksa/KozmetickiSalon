package dataProvajderi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.lang.ClassCastException;

import helpers.DefaultDict;
import helpers.Query;
import helpers.Updater;
import entiteti.Entitet;

public abstract class DataManager<T extends Entitet> implements IsProvider<T> {
	
	private ArrayList<T> data;
	private Function<T, String> idFunction;
	
	private String filePath;
	private final String csvDelimiter = ",";
	
	{
		setData(defaultCollection());
	}
	
	public DataManager() {}
	
	public DataManager(Function<T, String> idFunction, String filePath) {
		setIdFunction(idFunction);
		setFilePath(filePath);
	}
	
	
	private ArrayList<T> getData(){
		return this.data;
	}
	
	private void setData(ArrayList<T> newData) {
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
	protected ArrayList<T> defaultCollection(){
		return new ArrayList<>();
	}
	
	
	//TODO: these methods below should handle the exceptions that occur if subclass doesn't override correctly
	@Override
	public List<T> get(Query<T> selektor) {
		return defaultGet(selektor);
	}
	@Override
	public Iterator<T> get() {
		return defaultGetAll();
	}
	@Override
	public void put(Query<T> selektor, Updater<T> updater) {
		defaultPut(selektor, updater);		
	}
	@Override
	public void post(T entitet) {
		defaultPost(entitet);		
	}
	@Override
	public void delete(Query<T> selektor) {
		defaultDelete(selektor);		
	}
	@Override
	public String getId(T entitet) {
		return defaultGetId(entitet);
	}
	@Override
	public T getById(String id) {
		return defaultGetById(id);
	}
	@Override
	public DefaultDict<String, T> getIds() {
		return defaultGetIds();
	}
	
	
	
	
	
	
	protected List<T> defaultGet(Query<T> selektor){
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
	
	protected Iterator<T> defaultGetAll(){
		return getData().listIterator();
	}
	
	protected void defaultPut(Query<T> selektor, Updater<T> updater) {
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

	
	protected void defaultPost(T entitet) {
		Function<T, String> idFunction = getIdFunction();
		String id = idFunction.apply(entitet);
		
		if(isIdUnique(id)) {
			getData().add(entitet);
			return;
		}
				
		return; //TODO: id not unique
	}
	
	protected void defaultDelete(Query<T> selektor) {		
		ListIterator<T> iterator = (ListIterator<T>) get();
		
		while ( iterator.hasNext() ) {
			if( selektor.test( iterator.next() ) ) {
				iterator.remove();
			}			
		}		
	}
	
	protected String defaultGetId(T entitet) {
		return getIdFunction().apply(entitet);
	}
	
	protected T defaultGetById(String id) {
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
	
	protected DefaultDict<String, T> defaultGetIds(){
		DefaultDict<String, T> ids = new DefaultDict<>(() -> getDeletedInstance());
		Function<T, String> idFunction = getIdFunction();
		Iterator<T> iterator = get();
		
		while(iterator.hasNext()) {
			T entitet = iterator.next();
			ids.put(idFunction.apply(entitet), entitet);
		}
		
		return ids;
	}
	
	
	
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
	
	protected void visitAllEntities(Function<T, Void> f) {
		
	}
	
	
	
	protected abstract T getDeletedInstance();
	
	public T copyEntity(T entity) {
		//TODO: finish
		return null;		
	}
	
	
		
}
