package entiteti.blueprints;

import helpers.Query;
import helpers.Updater;

public interface IBlueprint<T> {
	public T build();
	public Query<T> query();
	public Updater<T> updater();
}
