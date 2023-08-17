package crudMenadzeri;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import entiteti.Entitet;

public class MenadzerFactory {
	
	 private Map<Class<? extends Entitet>, Supplier<? extends Menadzer<? extends Entitet>>> managerSuppliers = new HashMap<>();	 
	 

	public <E extends Entitet> void registerManager(Class<E> entityClass, Supplier<? extends Menadzer<E>> supplier) {
		managerSuppliers.put(entityClass, supplier);
	}

	public <E extends Entitet> Menadzer<E> getManager(Class<E> entityClass) {
		Supplier<? extends Menadzer<? extends Entitet>> supplier = managerSuppliers.get(entityClass);

	    if (supplier == null) {
	    	throw new IllegalArgumentException("Manager not found for class: " + entityClass.getName());
	    }

	    return (Menadzer<E>) supplier.get();
	}
}
