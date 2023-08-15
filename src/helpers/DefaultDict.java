package helpers;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class DefaultDict<K, V> {
	
    private final Map<K, V> map = new HashMap<>();
    private final Supplier<V> defaultValueSupplier;
        
    public DefaultDict() {
    	this( () -> null );
    }

    public DefaultDict(Supplier<V> defaultValueSupplier) {
        this.defaultValueSupplier = defaultValueSupplier;
    }
    
    public DefaultDict(V defaultValue) {
    	this.defaultValueSupplier = () -> defaultValue;
    }
    
    public V getDefaultValue() {
    	return defaultValueSupplier.get();
    }

    public V get(K key) {
        return map.computeIfAbsent(key, k -> defaultValueSupplier.get());
    }

    public void put(K key, V value) {
        map.put(key, value);
    }

    
	protected Map<K, V> getMap() {
		return map;
	}

	protected Supplier<V> getDefaultValueSupplier() {
		return defaultValueSupplier;
	}
	
	

 
}

