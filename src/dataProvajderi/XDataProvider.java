package dataProvajderi;

import java.util.ArrayList;
import java.util.function.Function;

import entiteti.Entitet;

public abstract class XDataProvider<T extends Entitet, I> extends DataProvider<T, I>{
	
	public XDataProvider() {
		super();
	}
	
	public XDataProvider(Function<T, String> idFunction, String filePath) {
		super(idFunction, filePath);
	}

	@Override
	public void loadData() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("This provider can't load data without having access to other provider data.");
	}

	
	@Override
	public void saveData() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("This provider can't load data without having access to other provider data.");
	}
	
		
	@Override
	protected Data<I, T> convertStringToData(ArrayList<String[]> stringData) throws UnsupportedOperationException{
		throw new UnsupportedOperationException("Method not implemented for this provider type.");
	}

	
	@Override
	protected ArrayList<String[]> convertDataToString(Data<I, T> data) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Method not implemented for this provider type.");
	}
	
	
	protected void setData(Data<I, T> newData) {	
		super.setData(newData);
	}
	
	
	protected Data<I, T> getData() {
		return super.getData();
	}
	
	
	public String getFilePath() {
		return super.getFilePath();
	}
	
	
}
