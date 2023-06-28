package helpers;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class DefaultDict<K, V> {
	
    private final Map<K, V> map;
    private final Supplier<V> defaultValueSupplier;
    
    public DefaultDict() {
    	this( () -> null );
    }

    public DefaultDict(Supplier<V> defaultValueSupplier) {
        this.map = new HashMap<>();
        this.defaultValueSupplier = defaultValueSupplier;
    }

    public V get(K key) {
        return map.computeIfAbsent(key, k -> defaultValueSupplier.get());
    }

    public void put(K key, V value) {
        map.put(key, value);
    }

 
}

