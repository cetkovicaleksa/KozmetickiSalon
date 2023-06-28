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
import java.util.Iterator;
import java.util.function.Function;

import exceptions.NoPayloadDataException;
import helpers.DefaultDict;
import helpers.Query;
import helpers.Updater;

public abstract class Provider<T> implements IsProvider<T> {
	
	private ArrayList<T> data;
	private String path;
	private Function<T, String> naturalId; //za generisanje id-a za entitet
	
	public Provider(){}
	
	public Provider(String path) {
		setPath(path);
	}

	public Provider(String path, Function<T, String> idFunction) {
		setPath(path);
		setNaturalId(idFunction);  //mozda ne koristiti set zato sto on provjeri da li su entiteti jedinstveni po funkciji
	}
	
	
	protected ArrayList<T> getData(){return this.data;}
	protected void setData(ArrayList<T> newData) {this.data = newData;}
	
	public String getPath() {return this.path;}
	protected void setPath(String path) {this.path = path;}
		
	protected Function<T, String> getNaturalId(){return this.naturalId;}	
	protected void setNaturalId(Function<T, String> newNaturalId) {
		
		for( T entitet: getData() ) {
			
			String id = newNaturalId.apply(entitet);
			int num = 0;
			
			for(T entitet2 : getData()) {
				
				if(  id.equals( newNaturalId.apply(entitet2) )  ) {					
					if(++num > 1) {
						return; //TODO: throw exception bc entities ids are not unique with new idMaker
					}
				}
				
			}
			
			
		}
	}
	
	
	public String getId(T entitet) { return getNaturalId().apply(entitet); }	
	public T getById(String id) {
		
		Function<T, String> idMaker = getNaturalId();
		
		for(T entitet : getData()) {
			if(  id.equals( idMaker.apply(entitet) )  ) {
				return entitet;
			}
		}
		
		return null;
		
	}
	
	//Vrati referencu na jedinu instancu izbrisanog entiteta za taj entitet ako nepostoji id entiteta
	public DefaultDict<String, T> getIds(){
		
		DefaultDict<String, T> dict = new DefaultDict<>( () -> getDeletedInstance() );
		Function<T, String> idFunction = getNaturalId();
		
		for(T entitet : getData()) {
			dict.put(idFunction.apply(entitet), entitet);
		}
		
		return dict;
	}
	public abstract T getDeletedInstance();
	
	
	
	/**Metoda koja vrati sve entitete koji ispunjavaju uslov zadat selektorom.*/
	public ArrayList<T> get(Query<T> selektor) {
		
		ArrayList<T> found = new ArrayList<>();
		
		for( T entitet: getData() ) {
			if( selektor.test(entitet) == true ) {
				found.add(entitet);
			}
		}
		return found;
		
	}
	
	/**Metoda koja pozove updater.update(entitet) nad svim entitetima koji zadovoljavaju uslov zadat selektorom.*/
	public void put(Query<T> selektor, Updater<T> updater) {
		
		Function<T, String> idMaker = getNaturalId();
		
		for( T entitet: getData() ) {
			
			if( selektor.test(entitet) ) {
				
				try {
					
					T backup = this.deepCopy(entitet);  //TODO: napravi deepcopy u slucaju da promjena entiteta remeti jedinstvenost identifikatora
					updater.update(entitet);
					
					boolean unique = true;
					String id = idMaker.apply(entitet);
					
					for(T checkEntitet : getData()) {
						if(  id.equals( idMaker.apply(checkEntitet) )  ) {
							
							unique = false;
							break;
							
						}
					}
					
					if(unique == true) {
						return;
					}
					
					entitet = backup;
					//TODO: postoji entitet sa natural id-om modifikovanog entiteta
					
				} catch (NoPayloadDataException e) {
					throw e;
				} 
				
			}
			
		}
		
	}
	
	//need to add id tester so we don't allow adding multiple entity with the same id aka username...
	public void post(T entitet) { 
		
		Function<T, String> idMaker = getNaturalId();
		String id = idMaker.apply(entitet);
		
		for(T entitet2 : getData()) {
			if(  idMaker.apply(entitet2).equals( id )  ) {
				return;  //TODO: postoji entitet sa natural id-om novog entiteta
			}
		}
		
		getData().add(entitet);
		
	}
	
	/**Metoda koja izbrise sve entitete koji zadovoljavaju uslov zadat selektorom.*/
	public void delete(Query<T> selektor) {
		
		Iterator<T> iterator = getData().iterator();
		
		while ( iterator.hasNext() ) {
			
			if( selektor.test( iterator.next() ) ) {
				iterator.remove();
			}
			
		}
		
		
	}



	
	protected abstract ArrayList<String[]> convertDataToString(ArrayList<T> data); 
	protected abstract ArrayList<T> convertStringToData(ArrayList<String[]> data);
	
	//Check twice to see this works
	public void loadData() throws IOException{
		this.setData(  this.convertStringToData( loadFromCsv(this.getPath(), ",") )  );				
	}
		
	public void saveData() throws IOException{
		writeToCsv( this.convertDataToString( this.getData() ), this.getPath(), ",");
	};

	
	protected static void writeToCsv(ArrayList<String[]> entityStrings, String path, String delimiter) throws IOException {
    	
    	try ( BufferedWriter bw = new BufferedWriter(new FileWriter(path)) ) {
            
    		for ( String[] entityFields : entityStrings ) {
                String line = String.join(delimiter, entityFields);
                bw.write(line);
                bw.newLine();
            }
    		} catch (IOException e) {
            throw e;
    		}
    	    	
    }
    
    protected static ArrayList<String[]> loadFromCsv(String path, String delimiter) throws IOException {
       
    	ArrayList<String[]> data = new ArrayList<>();

        try ( BufferedReader br = new BufferedReader(new FileReader(path)) ) {
            String line;

            while ( (line = br.readLine()) != null ) {
                String[] entityFields = line.split(delimiter);
                data.add(entityFields);
            }
        } catch (IOException e) {
            throw e;
        }

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