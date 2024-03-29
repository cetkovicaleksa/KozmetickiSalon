package helpers;

@FunctionalInterface
public interface Converter<I, O> {
	public O convert(I input);
}
