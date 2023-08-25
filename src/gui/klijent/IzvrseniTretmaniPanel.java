package gui.klijent;

import java.util.Collection;
import java.util.function.Supplier;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;

import entiteti.ZakazanTretman;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class IzvrseniTretmaniPanel extends JPanel{
	
	private Supplier<Collection<ZakazanTretman>> getIzvrseniTretmaniSupplier;
	
	private JTable table;
    private TableRowSorter<AbstractTableModel> tableSorter = new TableRowSorter<AbstractTableModel>();
    private JLabel placeholder;

    
    public IzvrseniTretmaniPanel(Supplier<Collection<ZakazanTretman>> getIzvrseniTretmaniSupplier) {
    	this.getIzvrseniTretmaniSupplier = getIzvrseniTretmaniSupplier;
    	
    	initialize();
    	setupLayout();
    }
    
    
    private void initialize() {
    	table = new JTable(new ZakazaniTretmaniTableModel(getIzvrseniTretmaniSupplier.get()));
    	table.getTableHeader().setReorderingAllowed(false);
    	tableSorter.setModel((AbstractTableModel) table.getModel());
    	table.setRowSorter(tableSorter);
    	
    	placeholder = new JLabel("Nema izvrsenih tretmana.");
    	recheckPlaceholder();
    }
    
    private void setupLayout(){
        setLayout(new MigLayout("fill"));
        add(new JScrollPane(table), "grow, push, wrap");
        add(placeholder, "center");
    }
    
    
    private void recheckPlaceholder() {
    	placeholder.setVisible(table.getRowCount() == 0);
    }
    
    public void updateData() {
    	if(table != null) {
    		((ZakazaniTretmaniTableModel) table.getModel())
    		.updateData(getIzvrseniTretmaniSupplier.get());
    		
    		recheckPlaceholder();
    	}
    }
}
