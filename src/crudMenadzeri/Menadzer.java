package crudMenadzeri;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import dataProvajderi.DataProvider;
import dataProvajderi.IdNotUniqueException;
import entiteti.Entitet;
import helpers.DefaultDict;
import helpers.Query;
import helpers.Updater;

public abstract class Menadzer<T extends Entitet> implements CRUDMenadzer<T> {
	
	protected Menadzer() {}
	
	protected abstract DataProvider<T, ?> getMainProvider();
	
	
	
	@Override
	public void create(T entitet) throws IdNotUniqueException {
		getMainProvider().post(entitet);
	}
	
	
	@Override
	public List<T> read(Query<T> selector) {
		return getMainProvider().get(selector);
	}
	
	
	@Override
	public Iterator<T> readAll() {
		return getMainProvider().get();
	}
	
	
	@Override
	public boolean update(Query<T> selector, Updater<T> updater) throws IdNotUniqueException {
		return getMainProvider().put(selector, updater);
	}
	
	
	@Override
	public boolean delete(Query<T> selector) {
		return getMainProvider().delete(selector);
	}
	
	
	@Override
	public void load() throws IOException, UnsupportedOperationException {
		getMainProvider().loadData();		
	}
	
	
	public void save() throws IOException, UnsupportedOperationException {
		getMainProvider().saveData();
	}
	
	
	
	DefaultDict<String, T> getIds(){
		return getMainProvider().getIds();
	}
	
	
	String getId(T entitet) {
		return getMainProvider().getId(entitet);
	}
	
	
	T getById(String id) {
		return getMainProvider().getById(id);
	}
	
	
	T getDeletedInstance() {
		return getMainProvider().getDeletedInstance();
	}
	
	
	boolean exists(T entitet) {
		if(entitet == null || getMainProvider().getDeletedInstance().equals(entitet)) {
			return false;
		}
		
		Iterator<T> iter = getMainProvider().get();
		while(iter.hasNext()) {
			if(iter.next().equals(entitet)) {
				return true;
			}
		}
		
		return false;
	}

}
