package gui.kozmeticar;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.SortedMap;

import javax.swing.table.AbstractTableModel;

import entiteti.KozmetickiTretman;
import entiteti.ZakazanTretman;

@SuppressWarnings("serial")
public class RasporedTableModel extends AbstractTableModel{
	
	private SortedMap<LocalDate, SortedMap<LocalTime, ZakazanTretman>> rasporedKozmeticara;
	private final String[] columnNames = {"Datum", "Vrijeme", "Naziv tretmana / tip tretmana"};
	private Object[][] data;
	
	public RasporedTableModel(SortedMap<LocalDate, SortedMap<LocalTime, ZakazanTretman>> rasporedKozmeticara) {
		this.rasporedKozmeticara = rasporedKozmeticara;
		
		generateData();
	}
	
	
	private void generateData() {
        int totalRows = rasporedKozmeticara.size();
        for (SortedMap<LocalTime, ZakazanTretman> timeTretmanMap : rasporedKozmeticara.values()) {
            totalRows += timeTretmanMap.size();
        }

        data = new Object[totalRows][columnNames.length];
        int row = 0;

        for (LocalDate date : rasporedKozmeticara.keySet()) {
            data[row][0] = date;
            data[row][1] = ""; // Empty time cell
            data[row++][2] = ""; // Empty tretman cell

            SortedMap<LocalTime, ZakazanTretman> timeTretmanMap = rasporedKozmeticara.get(date);
            for (LocalTime time : timeTretmanMap.keySet()) {
                data[row][0] = ""; // Empty date cell
                data[row][1] = time;
                data[row++][2] = timeTretmanMap.get(time);
            }
        }
    }
	

	@Override
	public int getRowCount() {
		return data.length;
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
	    if (rowIndex >= 0 && rowIndex < getRowCount() && columnIndex >= 0 && columnIndex < getColumnCount()) {
	        Object ret = data[rowIndex][columnIndex];
	        if (ret instanceof ZakazanTretman) {
	            ZakazanTretman zakazanTretman = (ZakazanTretman) ret;
	            KozmetickiTretman.TipTretmana tipTretmana = zakazanTretman.getTipTretmana();
	            if (tipTretmana != null) {
	                String nazivTretmana = tipTretmana.getTretman().getNaziv();
	                String nazivTipa = tipTretmana.getNaziv();
	                return nazivTretmana + " / " + nazivTipa;
	            }
	        }
	        return ret;
	    }
	    return null;
	}

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}
	
	public ZakazanTretman getZakazanTretman(int rowIndex) {
	    if (rowIndex >= 0 && rowIndex < getRowCount()) {
	        Object tretmanObject = getValueAt(rowIndex, 2);
	        if (tretmanObject instanceof ZakazanTretman) {
	            return (ZakazanTretman) tretmanObject;
	        }
	    }
	    return null;
	}
	
	public void setRasporedKozmeticara(SortedMap<LocalDate, SortedMap<LocalTime, ZakazanTretman>> rasporedKozmeticara) {
		this.rasporedKozmeticara = rasporedKozmeticara;
		super.fireTableDataChanged();
	}

}
