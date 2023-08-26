package gui.recepcioner;

import java.time.LocalTime;

import javax.print.attribute.AttributeSet;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import com.github.lgooddatepicker.components.DatePicker;

import entiteti.Korisnik;
import entiteti.Kozmeticar;
import entiteti.KozmetickiTretman;
import entiteti.StatusTretmana;
import entiteti.ZakazanTretman;
import gui.KorisnikGUI;
import gui.interfaces.LoggedInSalon;
import gui.interfaces.RecepcionerSalon;
import javafx.scene.control.CheckBox;

@SuppressWarnings("serial")
public class RecepcionerGUI extends KorisnikGUI{

	private RecepcionerSalon recepcionerSalon;
	
	private ZakazanTretman selectedZakazanTretman;
	
	private JTable table; //table of zakazani tretmani
    private TableRowSorter<AbstractTableModel> tableSorter = new TableRowSorter<AbstractTableModel>();
	
    private JTextField minPriceFilterTextField;
    private JTextField maxPriceFilterTextField;
    
    
    
    private JComboBox<KozmetickiTretman> kozmetickiTretmanComboBox;
    private JComboBox<KozmetickiTretman.TipTretmana> tipTretmanaComboBox;
    
    private JComboBox<Korisnik> klijentComboBox;    
    private JComboBox<Kozmeticar> kozmeticarComboBox;
    private DatePicker datePicker;
    private JComboBox<LocalTime> timeComboBox;
    private JComboBox<StatusTretmana> statusComboBox;
    
    
    private JButton izmjeniTretmanButton;
    private JButton otkaziTretmanButton;
    private JButton zakaziTretmanButton;
    
    private JButton saveButton;
    private JButton cancelButton;
	
	

	public RecepcionerGUI(RecepcionerSalon recepcionerSalon) {
		this.recepcionerSalon = recepcionerSalon;
		
		setTitle("Recepcioner: " + recepcionerSalon.getLoggedInKorisnik());
		setSize(1000, 660);
		setLocationRelativeTo(null);
		
		addListeners();
	}
	
	
	private void initialize() {
		table = new JTable(new ZakazanTretmanTableModel(recepcionerSalon.getZakazaniTretmani()));
		table.getTableHeader().setReorderingAllowed(false);
		table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableSorter.setModel((AbstractTableModel) table.getModel());
        table.setRowSorter(tableSorter);
        
        PlainDocument pd1 = new PlainDocument() {

			@Override
			public void insertString(int offs, String str, javax.swing.text.AttributeSet a)
					throws BadLocationException {
				
			}
        	
        };
        
        
	}
	
	
	
	
	
	
	
	
	
	@Override
	protected LoggedInSalon getLoggedInSalon() {
		return recepcionerSalon;
	}
	
	
	private void updateTable() {
		((ZakazanTretmanTableModel) table.getModel()).setData(recepcionerSalon.getZakazaniTretmani());
	}
	
	
	
	private boolean readyForEditing() {
		return selectedZakazanTretman != null;
	}
	
	private void setSelectedZakazanTretman(ZakazanTretman zakazanTretman) {
		this.selectedZakazanTretman = zakazanTretman;
		
		boolean isNotNull = zakazanTretman != null;
		izmjeniTretmanButton.setEnabled(isNotNull);
		otkaziTretmanButton.setEnabled(isNotNull && StatusTretmana.ZAKAZAN == zakazanTretman.getStatus());
	}
	
	
	
	
	private void addListeners() {
		otkaziTretmanButton.addActionListener( e -> {
			int selectedRow = table.getSelectedRow();
			if(selectedRow == -1){
                JOptionPane.showMessageDialog(null, "Morate izabrati neki zakazan tretman.", "Greska", JOptionPane.WARNING_MESSAGE);
                return;
            }
			
			int option = JOptionPane.showConfirmDialog(null, "Da li ste sigurni da zeite da otkazete tretman?", "Potvrda", JOptionPane.YES_NO_OPTION);
            if(option == JOptionPane.YES_OPTION){
            	ZakazanTretman tretman = ((ZakazanTretmanTableModel) table.getModel()).getZakazanTretman(selectedRow);
            	recepcionerSalon.otkaziTretman(tretman);
                JOptionPane.showMessageDialog(null, "Tretman je uspjesno otkazan.", "Uspeh", JOptionPane.INFORMATION_MESSAGE);
                //TODO: updateTable();
            }
		});
		
		table.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
			if(e.getValueIsAdjusting()) {
				return;
			}
			
			int row = table.getSelectedRow();			
			setSelectedZakazanTretman(
					row == -1 ? null : ((ZakazanTretmanTableModel) table.getModel()).getZakazanTretman(row)
			);			
		});
		
		izmjeniTretmanButton.addActionListener(e -> {
			if(!readyForEditing()) {
				//
			}
			
						
		});
		
		zakaziTretmanButton.addActionListener(e -> {
			table.clearSelection();
			setSelectedZakazanTretman(null);
			
		});
		
		
		
	}
	
	private void fillInputs() {
		if(selectedZakazanTretman == null) {
			return;
		}
		
		tipTretmanaComboBox.setSelectedItem(selectedZakazanTretman.getTipTretmana());
		klijentComboBox.setSelectedItem(selectedZakazanTretman.getKlijent());
		kozmeticarComboBox.setSelectedItem(selectedZakazanTretman.getKozmeticar());
		datePicker.setDate(selectedZakazanTretman.getDatum());
		timeComboBox.setSelectedItem(selectedZakazanTretman.getVrijeme());
		statusComboBox.setSelectedItem(selectedZakazanTretman.getStatus());
	}
	
	private void clearDate() {
		datePicker.clear();
		clearTimes();
	}
	
	private void clearTimes() {
		timeComboBox.removeAllItems();
	}
}
