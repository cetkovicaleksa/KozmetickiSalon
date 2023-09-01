package gui.kozmeticar;

import java.time.format.DateTimeFormatter;
import java.util.Collection;

import javax.swing.table.AbstractTableModel;

import entiteti.ZakazanTretman;

@SuppressWarnings("serial")
public class ZakazanTretmanTableModel extends AbstractTableModel{
	
	private ZakazanTretman[] zakazaniTretmani;
	private final String[] columnNames = {
											"Naziv", "Klijent", "Datum", "Vrijeme",
											"Trajanje", "Cijena", "Status"
										  };
	public static final DateTimeFormatter FORMATER_DATUMA = java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
	
	
	public ZakazanTretmanTableModel(Collection<ZakazanTretman> zakazaniTretmani) {
		setZakazaniTretmani(zakazaniTretmani);
	}
	
	
	public void setZakazaniTretmani(Collection<ZakazanTretman> zakazaniTretmani) {
		this.zakazaniTretmani = new ZakazanTretman[zakazaniTretmani.size()];
		
		int row = 0;
		for(ZakazanTretman tretman : zakazaniTretmani) {
			this.zakazaniTretmani[row++] = tretman;
		}
		
		super.fireTableDataChanged();
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
	public Object getValueAt(int rowIndex, int columnIndex) {
		ZakazanTretman tretman = getZakazanTretman(rowIndex);
		if(tretman == null) {
			return null;
		}
		
		switch (columnIndex) {
			case 0:
				return tretman.getTipTretmana().getTretman().getNaziv() + " --- " + tretman.getTipTretmana().getNaziv();
			case 1:
				return tretman.getKlijent();
			case 2:
				return FORMATER_DATUMA.format(tretman.getDatum());
			case 3:
				return tretman.getVrijeme().toString();
			case 4:
				return tretman.getTipTretmana().getTrajanje() + "min.";
			case 5:
				return tretman.getCijena();
			case 6:
				return tretman.getStatus().name();
			default:
				return null;
		}
	}

	
	
	
	public ZakazanTretman getZakazanTretman(int row) {
		return ( row >= 0 && row < getRowCount() && zakazaniTretmani.length > 0 ? zakazaniTretmani[row] : null );
	}
}
