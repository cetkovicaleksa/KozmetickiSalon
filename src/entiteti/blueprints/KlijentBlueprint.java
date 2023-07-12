package entiteti.blueprints;

import entiteti.Entitet;
import helpers.Query;
import helpers.Updater;

public class KlijentBlueprint extends Blueprint {
	

	@Override
	public Blueprint buildBluePrint() {
		return new KlijentBlueprint();
	}
	
	
	public KlijentBlueprint setIme(String ime) {
		super.add("ime", ime);
		return this;
	}
	
	public KlijentBlueprint setPrezime(String prezime) {
		super.add("prezime", prezime);
		return this;
	}
	//TODO: add constants for field names?!?
	

	@Override
	public Entitet makeEntitet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Query<Entitet> getQueryToMatch() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Updater<Entitet> getUpdaterToMatch() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	

}
