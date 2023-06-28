package dataProvajderi;

import java.util.ArrayList;

import helpers.Query;
import helpers.Updater;

public interface IsProvider<T> {
		
	public ArrayList<T> get(Query<T> selektor);
	public void put(Query<T> selektor, Updater<T> updater);
	public void delete(Query<T> selektor);
	public void post(T entitet);
	
	
	public void saveData();  //maby add path parameter
	public void loadData();

}
