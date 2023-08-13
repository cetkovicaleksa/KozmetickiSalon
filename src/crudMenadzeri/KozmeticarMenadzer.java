package crudMenadzeri;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import dataProvajderi.IdNotUniqueException;
import entiteti.Kozmeticar;
import helpers.Query;
import helpers.Updater;

public class KozmeticarMenadzer implements ICRUDManager<Kozmeticar> {
	
	

	@Override
	public void create(Kozmeticar entitet) throws IdNotUniqueException {
		// TODO Auto-generated method stub
	}

	@Override
	public List<Kozmeticar> read(Query<Kozmeticar> selector) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<Kozmeticar> readAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(Query<Kozmeticar> selector, Updater<Kozmeticar> updater) throws IdNotUniqueException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(Query<Kozmeticar> selector) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void load() throws IOException {
		// TODO Auto-generated method stub
		
	}

}
