package gui.kozmeticar;

import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import entiteti.ZakazanTretman;

@SuppressWarnings("serial")
public class ZakazanTretmanTableModel extends AbstractTableModel{
	
	private List<ZakazanTretman> zakazaniTretmani;
	private final String[] columnNames = {
											"Naziv", "Klijent", "Datum", "Vrijeme",
											"Trajanje", "Cijena", "Status"
										  };
	public static final DateTimeFormatter FORMATER_DATUMA = java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
	
	
	public ZakazanTretmanTableModel(List<ZakazanTretman> zakazaniTretmani) {
		setZakazaniTretmani(zakazaniTretmani);
	}
	
	
	
	

	@Override
	public int getRowCount() {
		return zakazaniTretmani.size();
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
		
		switch (columnIndex) {
			case 0:
				return tretman.getTipTretmana().getTretman().getNaziv() + " --- " + tretman.getTipTretmana().getNaziv();
			case 1:
				return tretman.getKlijent().getKorisnickoIme();
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

	
	public void setZakazaniTretmani(List<ZakazanTretman> zakazaniTretmani) {
		this.zakazaniTretmani = zakazaniTretmani;
		super.fireTableDataChanged();
	}
	
	public ZakazanTretman getZakazanTretman(int row) {
		return this.zakazaniTretmani.get(row);
	}
}
