package dataProvajderi;

import helpers.Tester;

public interface Provider<T> {
	
	public T get(Tester<T> selektor);
	public void put(Tester<T> selektor, T entitet);
	public void delete(Tester<T> selektor);
	public void post(T entitet);
	
	public T getById(int id);  //TODO: hm mozda, a mozda ne provjeri kasnije

}
