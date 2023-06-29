package dataProvajderi;

//import java.io.IOException;
import java.util.ArrayList;

import helpers.DefaultDict;
import helpers.Query;
import helpers.Updater;

public interface IsProvider<T> {
		
	public ArrayList<T> get(Query<T> selektor);
	public void put(Query<T> selektor, Updater<T> updater);
	public void post(T entitet);
	public void delete(Query<T> selektor);
	
	public String getId(T entitet);
	public T getById(String id);
	public DefaultDict<String, T> getIds();
	
	//public void saveData() throws IOException;
	//public void loadData() throws IOException;

}
