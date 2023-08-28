package helpers;

@FunctionalInterface
public interface ThreeArgumentFunction<X, Y, Z, U> {
	public U apply(X x, Y y, Z z);
}
