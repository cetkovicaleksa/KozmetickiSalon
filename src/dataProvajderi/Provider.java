package dataProvajderi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Function;

import exceptions.NoPayloadDataException;
import helpers.DefaultDict;
import helpers.Query;
import helpers.Updater;

public abstract class Provider<T> implements IsProvider<T> {
	
	private IsProvider<T> provider;
	private Collection<?> data;  //just a reference to the entity collection in provider
	private String path;
	private final String delimiter = ",";
	private Function<T, String> naturalId; //za generisanje id-a za entitet
	
	/**The default constructor sets the provider and data to use an ArrayList as the entity collection.*/	
	public Provider(){
		initializeProviderAndData();
	}
		
	public Provider(String path) {
		this();
		setPath(path);
	}

	public Provider(String path, Function<T, String> idFunction) {
		this();
		setPath(path);
		setNaturalId(idFunction);  //mozda ne koristiti set zato sto on provjeri da li su entiteti jedinstveni po funkciji
	}
	
	
	protected IsProvider<T> getProvider(){return this.provider;}
	protected void setProvider(IsProvider<T> newData) {this.provider = newData;}
	//protected void setData(ArrayList<T> newData) {}
	
	protected Collection<?> getData(){ return this.data; }
	protected void setData(Collection<?> newData) { this.data = newData; }
	
	public String getPath() {return this.path;}
	protected void setPath(String path) {this.path = path;}
	
	public String getDelimiter() {return this.delimiter;}
		
	protected Function<T, String> getNaturalId(){return this.naturalId;}	
	protected void setNaturalId(Function<T, String> newNaturalId) {
		Iterator<T> iter1 = getProvider().get();
	    
	    while (iter1.hasNext()) {
	        T entity1 = iter1.next();
	        String id = newNaturalId.apply(entity1);
	        
	        Iterator<T> iter2 = getProvider().get();
	        
	        while (iter2.hasNext()) {
	            T entity2 = iter2.next();
	            
	            if (entity1 == entity2) {
	                continue;
	            }
	            
	            if (id.equals(newNaturalId.apply(entity2))) {
	                // TODO: throw an exception because entities' IDs are not unique with the new natural ID
	                return;
	            }
	        }
	    }
	    
	    this.naturalId = newNaturalId;
	}

	
	
	public String getId(T entitet) {
		return getProvider().getId(entitet);
	}	
	public T getById(String id) {
		return getProvider().getById(id);
	}
	
	public DefaultDict<String, T> getIds(){
		return getProvider().getIds();
	}
	public abstract T getDeletedInstance();
	
	
	protected void initializeProviderAndData() {
		
		this.setProvider( new IsProvider<T>() {
			
			private ArrayList<T> data = new ArrayList<>();
			
			{
				Provider.this.setData(this.getData());
			}
			
			
			private ArrayList<T> getData(){return this.data;}
			//private void setData(ArrayList<T> newData) { this.data = newData; }
			
			@Override
			public ArrayList<T> get(Query<T> selektor) {
				
				ArrayList<T> found = new ArrayList<>();
				
				for( T entitet: this.getData() ) {
					if( selektor.test(entitet) == true ) {
						found.add(entitet);
					}
				}
				return found;
				
			}
			
			@Override
			public Iterator<T> get(){
				return this.getData().iterator();
			}

			@Override
			public void put(Query<T> selektor, Updater<T> updater) throws NoPayloadDataException {
				
				Function<T, String> idMaker = Provider.this.getNaturalId();
				ListIterator<T> iterator1 = this.getData().listIterator(); //zbog backup-a koristimo listiterator
								
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
							current = head;
						}

						@Override
						public boolean hasNext() {
							return current != null && current.next != null;
						}

						@Override
						public Node next() {
							current = current.next;
							return current;
						}
						
					}
					
					
				}
				
				Backup backup = new Backup();
				
				//prolazimo kroz sve entitete i trazimo onaj koji zadovoljava selektor
				while (iterator1.hasNext()) {
					
					T entitet1 = iterator1.next();
					
					if(selektor.test(entitet1)) {
						//nasli smo entitet koji zadovoljava selektor
						String originalId = idMaker.apply(entitet1);
						//making a backup and updating the entity
						backup.add(iterator1, Provider.this.deepCopy(entitet1));
						updater.update(entitet1);
						
						String newId = idMaker.apply(entitet1);
						//izmjena entiteta nije promjenila natural id entiteta, preskacemo provjeru jedinstvenosti id-a
						if(originalId.equals(newId)) {
							continue;
						}
						
						//id entiteta se izmjenio pa prolazimo kroz sve ostale entitete i provjeravamo da li je noviId jedinstven
					    Iterator<T> iterator2 = this.get();
					    while(iterator2.hasNext()) {
					    	T entitet2 = iterator2.next();
					    	
					    	if(entitet2 == entitet1) {
					    		continue;
					    	}
					    	
					    	if(newId.equals(idMaker.apply(entitet2))) {
					    		//if one of the entities that user wants to change cannot be changed, none of the entities won't be changed
					    		for(Backup.Node bp: backup) {
					    			bp.undo();
					    		}
					    		return; //TODO: not unique id throw exception
					    	}
					    }
						
					}
					
					
				}
				
			
				
			}

			@Override
			public void post(T entitet) {
				
				Function<T, String> idMaker = Provider.this.getNaturalId(); //only to make it clear which method are we calling
				String id = idMaker.apply(entitet);
				
				for(T entitet2 : this.getData()) {
					if(  idMaker.apply(entitet2).equals( id )  ) {
						return;  //TODO: postoji entitet sa natural id-om novog entiteta
					}
				}
				
				getData().add(entitet);
				
			}

			@Override
			public void delete(Query<T> selektor) {
				
				Iterator<T> iterator = this.get();
				
				while ( iterator.hasNext() ) {
					
					if( selektor.test( iterator.next() ) ) {
						iterator.remove();
					}
					
				}
				
			}

			@Override
			public String getId(T entitet) {
				return Provider.this.getNaturalId().apply(entitet);
			}

			@Override
			public T getById(String id) {
				
				Function<T, String> idMaker = Provider.this.getNaturalId();
				
				for(T entitet : this.getData()) {
					if(  id.equals( idMaker.apply(entitet) )  ) {
						return entitet;
					}
				}
				
				return Provider.this.getDeletedInstance();
				
			}

			@Override
			public DefaultDict<String, T> getIds() {
				DefaultDict<String, T> ids = new DefaultDict<>( () -> Provider.this.getDeletedInstance() );
				Function<T, String> idMaker = Provider.this.getNaturalId();
				
				for(T entitet : this.getData() ) {
					ids.put(idMaker.apply(entitet), entitet);
				}
				
				return ids;
			}
			
			
			
		});
	}
	
	
	
	public List<T> get(Query<T> selektor) {
		return getProvider().get(selektor);
	}
	
	public Iterator<T> get(){
		return getProvider().get();
	}
	
	public void put(Query<T> selektor, Updater<T> updater) {
		
		
		
	}
	
	//need to add id tester so we don't allow adding multiple entity with the same id aka username...
	public void post(T entitet) { 
		getProvider().post(entitet);		
	}
	
	/**Metoda koja izbrise sve entitete koji zadovoljavaju uslov zadat selektorom.*/
	public void delete(Query<T> selektor) {
		getProvider().delete(selektor);	
	}



	
	protected abstract List<String[]> convertDataToString(List<T> data); 
	protected abstract List<T> convertStringToData(List<String[]> data);
	
	//Check twice to see this works
	public void loadData() throws IOException{
		//this.setData(  this.convertStringToData( loadFromCsv(this.getPath(), ",") )  );				
	}
		
	public void saveData() throws IOException{
		//writeToCsv( this.convertDataToString(  ), this.getPath(), this.getDelimiter());
	};

	
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






	@SuppressWarnings("unchecked")
	private T deepCopy(T entity) {
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
			objectOutputStream.writeObject(entity);
        
			ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
			ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
			return (T) objectInputStream.readObject();
			} catch (Exception e) {
			throw new RuntimeException("Failed to create a deep copy of the entity", e);
			}
	}

}