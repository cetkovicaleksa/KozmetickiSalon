package gui.klijent;

import java.time.format.DateTimeFormatter;
import java.util.Collection;

import javax.swing.table.AbstractTableModel;

import entiteti.ZakazanTretman;

@SuppressWarnings("serial")
public class ZakazaniTretmaniTableModel extends AbstractTableModel{

	private ZakazanTretman[] zakazaniTretmani;
	private String[] columnNames = {"Naziv", "Kozmeticar", "Datum", "Vrijeme", "Trajanje", "Cijena"};
	
	private static final DateTimeFormatter dateTimeFormater = java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
	
	
	
	public ZakazaniTretmaniTableModel(Collection<ZakazanTretman> zakazaniTretmani) {
		updateData(zakazaniTretmani);
	}
	
	public void updateData(Collection<ZakazanTretman> zakazaniTretmani) {
		this.zakazaniTretmani = new ZakazanTretman[(zakazaniTretmani == null ? 0 : zakazaniTretmani.size())];
		if(this.zakazaniTretmani.length == 0) {
			return;
		}
		
		int row = 0;
		for(ZakazanTretman zt : zakazaniTretmani) {
			this.zakazaniTretmani[row++] = zt;
		}
		
		fireTableDataChanged();
	}

	
	@Override
	public int getRowCount() {
		return zakazaniTretmani.length;
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}
	
	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	@Override
	public String getValueAt(int rowIndex, int columnIndex) {
		ZakazanTretman tretman = getZakazanTretman(rowIndex);
		if(tretman == null) {
			return null;
		}
		
		switch (columnIndex) {
			case 0:
				return tretman.getTipTretmana().getNaziv();
			case 1:
                return tretman.getKozmeticar().toString();
            case 2:
                return dateTimeFormater.format(tretman.getDatum());
            case 3:
                return tretman.getVrijeme().toString();
            case 4:
                return tretman.getTrajanje() + " min";
            case 5:
                return Double.toString(tretman.getCijena());
            default:
                return null;
			
		}
	}
	
	public ZakazanTretman getZakazanTretman(int rowIndex) {
		return (rowIndex > 0 && rowIndex < getRowCount() ? zakazaniTretmani[rowIndex] : null);
	}
}
