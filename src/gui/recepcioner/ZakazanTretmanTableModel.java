package gui.recepcioner;

import java.util.Collection;

import javax.swing.table.AbstractTableModel;

import entiteti.ZakazanTretman;

@SuppressWarnings("serial")
public class ZakazanTretmanTableModel extends AbstractTableModel{

	private ZakazanTretman[] data;
	private String[] columnNames = {
			"Naziv", "Klijent", "Kozmeticar", "Datum", "Vrijeme", "Trajanje", "Cijena", "Status"
	};
	
	
	public ZakazanTretmanTableModel(Collection<ZakazanTretman> zakazaniTretmani) {
		setData(zakazaniTretmani);
	}
	
	
	public void setData(Collection<ZakazanTretman> zakazaniTretmani) {
		data = new ZakazanTretman[zakazaniTretmani.size()];
		
		int row = 0;
		for(ZakazanTretman tretman : zakazaniTretmani) {
			data[row++] = tretman;
		}
		
		fireTableDataChanged();
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
	public String getColumnName(int column) {
		return columnNames[column];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if(rowIndex >= getRowCount() || columnIndex >= getColumnCount() ){
			return null;
		}
		
		ZakazanTretman tretman = data[rowIndex];
		switch (columnIndex) {
			case 0:
				return tretman.getNaziv();
			case 1:
				return tretman.getKlijent();
			case 2:
				return tretman.getKozmeticar();
			case 3:
				return tretman.getDatum();
			case 4:
				return tretman.getVrijeme();
			case 5:
				return tretman.getTrajanje() + " min";
			case 6:
				return tretman.getCijena();
			case 7:
				switch (tretman.getStatus()) {
                	case ZAKAZAN:
                		return "Zakazan";
                	case IZVRSEN:
                		return "Izvrsen";
                	case OTKAZAO_KLIJENT:
                		return "Otkazao klijent";
                	case OTKAZAO_SALON:
                		return "Otkazao salon";
                	case NIJE_SE_POJAVIO:
                		return "Nije se pojavio";
                	default:
                		return null;
            }
			
			default:
           	 return null;
		}
	}
	
	
	public ZakazanTretman getZakazanTretman(int rowIndex) {
		return (rowIndex >= 0 && rowIndex < getRowCount() ? data[rowIndex] : null);
	}
}
