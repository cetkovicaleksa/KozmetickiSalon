package gui.kozmeticar;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import entiteti.KozmetickiTretman;

@SuppressWarnings("serial")
public class KozmetickiTretmanTableModel extends AbstractTableModel{
	
	private List<KozmetickiTretman> kozmetickiTretmani;
	private final String[] columnNames = {"Naziv", "Opis"};
	
	
	public KozmetickiTretmanTableModel(List<KozmetickiTretman> kozmetickiTretmani) {
		setKozmetickiTretmani(kozmetickiTretmani);
	}

	

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}



	@Override
	public int getRowCount() {
		return kozmetickiTretmani.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		KozmetickiTretman tretman = getKozmetickiTretman(rowIndex);
		
		switch (columnIndex) {
			case 0:
				return tretman.getNaziv();
			case 1:
				return tretman.getOpis();
			default:
				return null;
		}
				
	}
	
	
	public KozmetickiTretman getKozmetickiTretman(int index) {
		return kozmetickiTretmani.get(index);
	}
	
	public void setKozmetickiTretmani(List<KozmetickiTretman> kozmetickiTretmani) {
		this.kozmetickiTretmani = kozmetickiTretmani;
		super.fireTableDataChanged();
	}

}
