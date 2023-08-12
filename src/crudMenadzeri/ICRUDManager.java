package crudMenadzeri;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import dataProvajderi.IdNotUniqueException;
import entiteti.Entitet;
import helpers.Query;
import helpers.Updater;

public interface ICRUDManager<T extends Entitet> {
	
	public void create(T entitet) throws IdNotUniqueException;
	
	public List<T> read(Query<T> selector);
	
	public Iterator<T> readAll();
	
	public boolean update(Query<T> selector, Updater<T> updater);
	
	public boolean delete(Query<T> selector);
	
	public void load() throws IOException;
}