package entiteti.blueprints;

import helpers.Query;
import helpers.Updater;

public interface IBlueprint<T> {
	public T constructEntity();
	public Query<T> getQueryToMatch();
	public Updater<T> getUpdaterToMatch();
}
