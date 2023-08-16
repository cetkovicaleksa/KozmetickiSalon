package crudMenadzeri;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import dataProvajderi.IdNotUniqueException;
import entiteti.Entitet;
import helpers.Query;
import helpers.Updater;

public interface CRUDMenadzer<T extends Entitet> {
	
	public void create(T entitet) throws IdNotUniqueException;
	
	public List<T> read(Query<T> selector);
	
	public Iterator<T> readAll();
	
	public boolean update(Query<T> selector, Updater<T> updater) throws IdNotUniqueException;
	
	public boolean delete(Query<T> selector);
	
	public void load() throws IOException;
	
	public void save() throws IOException;
}
