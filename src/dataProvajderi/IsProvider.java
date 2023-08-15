package dataProvajderi;

//import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import helpers.DefaultDict;
import helpers.Query;
import helpers.Updater;

/**
 * An interface representing a data provider for entities of type T.
 *
 * @param <T> the type of entities provided by this data provider.
 */
public interface IsProvider<T> {
	
	/**
     * Retrieves a list of entities that satisfy the specified query.
     *
     * @param selector the query used to filter the entities.
     * @return a list of entities that match the query.
     */
	public List<T> get(Query<T> selector);
	
	
	/**
    * Returns an iterator to iterate through all the entities in the data provider.
    *
    * @return an iterator over the entities in the data provider.
    */
	public Iterator<T> get();
	
	
	/**
     * Updates entities that satisfy the specified query using the provided updater.
     *
     * @param selector the query used to select entities for updating.
     * @param updater  the updater function to apply to the selected entities.
     * @return true if any entities were updated, false otherwise.
     */
	public boolean put(Query<T> selector, Updater<T> updater);
	
	
	/**
     * Adds a new entity to the data provider.
     *
     * @param entity the entity to be added.
     * @throws IdNotUniqueException if the natural ID of the new entity is not unique in the data provider.
     */
	public void post(T entity) throws IdNotUniqueException;
	
	
	/**
     * Removes all entities that satisfy the specified query.
     *
     * @param selector the query used to select entities for removal.
     * @return true if any entities were removed, false otherwise.
     */
	public boolean delete(Query<T> selector);
	
	
	/**Removes the given entity from the provider*/
	public boolean delete(T entitet);
	
	
	/**
     * Returns the natural ID of the given entity.
     * This method does not require checking whether the entity exists in the data provider.
     *
     * @param entity the entity to retrieve the natural ID from.
     * @return the natural ID of the entity.
     */
	public String getId(T entity);
	
	
	/**
     * Retrieves an entity with the given ID.
     * If the entity with the given ID does not exist, it returns a reference to a deleted instance.
     *
     * @param id the ID of the entity to retrieve.
     * @return the entity with the specified ID, or a reference to a deleted instance if not found.
     */
	public T getById(String id);
	
	
	/**
     * Returns a map of entity IDs to entities.
     * The map returns a reference to a deleted entity when accessing non-existent keys.
     *
     * @return a DefaultDict containing entity IDs mapped to their respective entities.
     */
	public DefaultDict<String, T> getIds();
	
}
