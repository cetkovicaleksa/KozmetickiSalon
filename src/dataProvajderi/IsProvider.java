package dataProvajderi;

//import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import helpers.DefaultDict;
import helpers.Query;
import helpers.Updater;

public interface IsProvider<T> {
		
	/**Method that returns a list of entities that satisfy the query.*/
	public List<T> get(Query<T> selektor);
	
	/**Method that returns an iterator to iterate through all the entities.*/
	public Iterator<T> get();
	
	/**Method that calls updater.update() on every entity that satisfies the crieteria given with the query.*/
	public void put(Query<T> selektor, Updater<T> updater);
	
	/**Method that adds a new entity to the collection.*/
	public void post(T entitet);
	
	/**Method that removes all entities that satisfy the query.*/
	public void delete(Query<T> selektor);
	
	/**Method that returns a string id for given entity. Doesn't require checking whether the entity exists in the system, just returns natural id for entity.*/
	public String getId(T entitet);
	
	/**Method that returns an entity with the given id. If the entity with the given id doesn't exist returns a reference to the deleted instance.*/
	public T getById(String id);
	
	/**Method that returns a map id : entity. The map returns a reference to the deleted entity when you try to acces non existent keys.*/
	public DefaultDict<String, T> getIds();
	
}
