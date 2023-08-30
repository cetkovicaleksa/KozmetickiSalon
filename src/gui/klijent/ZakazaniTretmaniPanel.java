package gui.klijent;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;

import entiteti.ZakazanTretman;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class ZakazaniTretmaniPanel extends JPanel{

	private Supplier<Collection<ZakazanTretman>> zakazaniTretmaniKlijentaSupplier;
	private Consumer<ZakazanTretman> otkaziTretmanConsumer;
	
	private JTable table;	
	private JButton otkaziButton;
	private JLabel placeholder;
	
	private TableRowSorter<AbstractTableModel> tableSorter = new TableRowSorter<AbstractTableModel>();
	
	
	
	public ZakazaniTretmaniPanel(Supplier<Collection<ZakazanTretman>> zakazaniTretmaniKlijentaSupplier, Consumer<ZakazanTretman> otkaziTretmanConsumer) {
		this.zakazaniTretmaniKlijentaSupplier = zakazaniTretmaniKlijentaSupplier;
		this.otkaziTretmanConsumer = otkaziTretmanConsumer;
		
		initialize();
		setupLayout();
		addListeners();
	}
	
	
	private void initialize() {
		table = new JTable(new ZakazaniTretmaniTableModel(zakazaniTretmaniKlijentaSupplier.get()));
		table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        
        tableSorter.setModel((AbstractTableModel) table.getModel());
        table.setRowSorter(tableSorter);
        
        otkaziButton = new JButton("Otkazi tretman");
        otkaziButton.setEnabled(false);
        
        placeholder = new JLabel("Nema zakazanih tretmana.");
        recheckPlaceholder();
	}
	
	private void setupLayout(){
        setLayout(new MigLayout("fill"));
        add(new JScrollPane(table), "grow, push, wrap");
        add(placeholder, "center, wrap");
        add(otkaziButton, "right");
    }
	
	private void recheckPlaceholder() {
		placeholder.setVisible(table.getRowCount() == 0);
	}
	
	private void addListeners() {
		table.getSelectionModel().addListSelectionListener( e -> otkaziButton.setEnabled(table.getSelectedRow() != -1) );
		
		otkaziButton.addActionListener(e -> {
			int selectedRow = table.getSelectedRow();
			if(selectedRow == -1) {
				JOptionPane.showMessageDialog(null, "Nijedan tretman nije selektovan.", "Greska", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			int option = JOptionPane.showConfirmDialog(null, "Da li ste sigurni da zelite da otkazete tretman?", "Potvrda", JOptionPane.YES_NO_OPTION);
            if(option == JOptionPane.NO_OPTION){
                return;
            }
            
            ZakazanTretman zakazanTretman = ((ZakazaniTretmaniTableModel) table.getModel()).getZakazanTretman(selectedRow);            
            otkaziTretmanConsumer.accept(zakazanTretman);
            
            JOptionPane.showMessageDialog(null, "Tretman je uspjesno otkazan.", "Uspeh", JOptionPane.INFORMATION_MESSAGE);
            recheckPlaceholder();
		});	
	}
	
	
	
	public void updateData() {
		((ZakazaniTretmaniTableModel) table.getModel()).updateData(zakazaniTretmaniKlijentaSupplier.get());
		recheckPlaceholder();
	}
}
