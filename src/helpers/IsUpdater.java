package helpers;

@FunctionalInterface
public interface IsUpdater<T> {
	public void update(T entity);
}
