package dataProvajderi;

import java.util.function.Function;
import java.util.function.Supplier;

import entiteti.Entitet;

public abstract class MultiDataManager<T extends Entitet, I, R> extends DataProvider<T, I> {
	
	private Supplier<R> getRegistry;
	
	public MultiDataManager(Function<T, String> idFunction, String filePath, Supplier<R> getRegistry) {
		super(idFunction, filePath);
		setGetRegistry(getRegistry);
	}
	
	
	protected R getRegistry(){
		return getRegistry.get();
	}
	
	protected Supplier<R> getGetRegistry(){
		return getRegistry;
	}
	
	protected void setGetRegistry(Supplier<R> newGetRegistry) {
		getRegistry = newGetRegistry;
	}
	
	@Override
	public void loadData() {
		
	}
}
