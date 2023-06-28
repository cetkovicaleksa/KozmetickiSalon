package dataProvajderi;

import java.util.function.Function;

public abstract class ProviderExtrovert<T> extends Provider<T> {
	
	private ProviderRegistry mainProvider;
	
	public ProviderExtrovert() {}
	
	public ProviderExtrovert(String path, Function<T, String> idFunction, ProviderRegistry mainProvider) {
		super(path, idFunction);
		setMainProvider(mainProvider);
	}
	
	protected ProviderRegistry getMainProvider() {return this.mainProvider;}
	protected void setMainProvider(ProviderRegistry newMainProvider) {this.mainProvider = newMainProvider;}

}
