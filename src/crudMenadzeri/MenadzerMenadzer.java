package crudMenadzeri;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import dataProvajderi.IdNotUniqueException;
import dataProvajderi.MenadzerProvider;
import entiteti.Menadzer;
import helpers.Query;
import helpers.Updater;

public class MenadzerMenadzer implements ICRUDManager<Menadzer> {
	
	private MenadzerProvider menadzerProvider;
	
	public MenadzerMenadzer() {}
	
	public MenadzerMenadzer(MenadzerProvider menadzerProvider) {
		setMenadzerProvider(menadzerProvider);
	}
	

	public MenadzerProvider getMenadzerProvider() {
		return menadzerProvider;
	}

	public void setMenadzerProvider(MenadzerProvider menadzerProvider) {
		this.menadzerProvider = menadzerProvider;
	}
	

	
	@Override
	public void create(Menadzer entitet) throws IdNotUniqueException {
		getMenadzerProvider().post(entitet);;
	}

	@Override
	public List<Menadzer> read(Query<Menadzer> selector) {
		return getMenadzerProvider().get(selector);
	}

	@Override
	public Iterator<Menadzer> readAll() {
		return getMenadzerProvider().get();
	}

	@Override
	public boolean update(Query<Menadzer> selector, Updater<Menadzer> updater) throws IdNotUniqueException {
		return getMenadzerProvider().put(selector, updater);
	}

	@Override
	public boolean delete(Query<Menadzer> selector) {
		return getMenadzerProvider().delete(selector);
	}

	@Override
	public void load() throws IOException {
		getMenadzerProvider().loadData();
	}

}
