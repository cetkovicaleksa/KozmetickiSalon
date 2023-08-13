package crudMenadzeri;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import dataProvajderi.IdNotUniqueException;
import dataProvajderi.RecepcionerProvider;
import entiteti.Recepcioner;
import helpers.Query;
import helpers.Updater;

public class RecepcionerMenadzer implements ICRUDManager<Recepcioner> {
	
	private RecepcionerProvider recepcionerProvider;
	
	
	public RecepcionerMenadzer() {}
	
	public RecepcionerMenadzer(RecepcionerProvider recepcionerProvider) {
		setRecepcionerProvider(recepcionerProvider);
	}
	

	public RecepcionerProvider getRecepcionerProvider() {
		return recepcionerProvider;
	}

	public void setRecepcionerProvider(RecepcionerProvider recepcionerProvider) {
		this.recepcionerProvider = recepcionerProvider;
	}
	

	
	@Override
	public void create(Recepcioner entitet) throws IdNotUniqueException {
		getRecepcionerProvider().post(entitet);;
	}

	@Override
	public List<Recepcioner> read(Query<Recepcioner> selector) {
		return getRecepcionerProvider().get(selector);
	}

	@Override
	public Iterator<Recepcioner> readAll() {
		return getRecepcionerProvider().get();
	}

	@Override
	public boolean update(Query<Recepcioner> selector, Updater<Recepcioner> updater) throws IdNotUniqueException {
		return getRecepcionerProvider().put(selector, updater);
	}

	@Override
	public boolean delete(Query<Recepcioner> selector) {
		return getRecepcionerProvider().delete(selector);
	}

	@Override
	public void load() throws IOException {
		getRecepcionerProvider().loadData();
	}

}
