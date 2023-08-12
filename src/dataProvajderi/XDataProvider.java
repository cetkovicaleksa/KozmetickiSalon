package dataProvajderi;

import java.io.IOException;
import java.util.ArrayList;

import entiteti.Entitet;

public abstract class XDataProvider<T extends Entitet, I> extends DataProvider<T, I>{
	

	@Override
	public void loadData() throws IOException {
		throw new UnsupportedOperationException("This provider can't load data without having access to other provider data.");
	}

	
	@Override
	public void saveData() throws IOException {
		throw new UnsupportedOperationException("This provider can't load data without having access to other provider data.");
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

	
	@Override
	protected Data<I, T> convertStringToData(ArrayList<String[]> stringData) {
		throw new UnsupportedOperationException("Method not implemented for this provider type.");
	}

	
	@Override
	protected ArrayList<String[]> convertDataToString(Data<I, T> data) {
		throw new UnsupportedOperationException("Method not implemented for this provider type.");
	}
	
	
}