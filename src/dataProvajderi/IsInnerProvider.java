package dataProvajderi;

public interface IsInnerProvider<T, D> extends IsProvider<T> {
	public D getData();
	public void setData(D newData);
}
