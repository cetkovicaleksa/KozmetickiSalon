package gui.klijent;

import java.util.Collection;
import java.util.function.Function;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;

import entiteti.StatusTretmana;
import entiteti.ZakazanTretman;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class OtkazaniTretmaniPanel extends JPanel{
	
	private Function<StatusTretmana, Collection<ZakazanTretman>> getZakazanTretmanForStatusFunction;
	
	private JTable otkazaoKlijentTable;
	private JTable otkazaoSalonTable;
	
	private TableRowSorter<AbstractTableModel> otkazaoKlijentTableSorter = new TableRowSorter<AbstractTableModel>();
    private TableRowSorter<AbstractTableModel> otkazaoSalonTableSorter = new TableRowSorter<AbstractTableModel>();
    private JLabel otkazaoKlijentPlaceholder;
    private JLabel otkazaoSalonPlaceholder;
    
    
    
    public OtkazaniTretmaniPanel(Function<StatusTretmana, Collection<ZakazanTretman>> getZakazanTretmanForStatusFunction) {
    	this.getZakazanTretmanForStatusFunction = getZakazanTretmanForStatusFunction;
    	
    	initialize();
    	setupLayout();
    }
    
    
    
    private void initialize() {
    	this.otkazaoKlijentTable = new JTable(new ZakazaniTretmaniTableModel(getZakazanTretmanForStatusFunction.apply(StatusTretmana.OTKAZAO_KLIJENT)));
    	otkazaoKlijentTable.getTableHeader().setReorderingAllowed(false);
    	otkazaoKlijentTableSorter.setModel((AbstractTableModel) otkazaoKlijentTable.getModel());
    	otkazaoKlijentTable.setRowSorter(otkazaoKlijentTableSorter);
    	
    	this.otkazaoSalonTable = new JTable(new ZakazaniTretmaniTableModel(getZakazanTretmanForStatusFunction.apply(StatusTretmana.OTKAZAO_SALON)));
    	otkazaoSalonTable.getTableHeader().setReorderingAllowed(false);
    	otkazaoSalonTableSorter.setModel((AbstractTableModel) otkazaoSalonTable.getModel());
    	otkazaoSalonTable.setRowSorter(otkazaoSalonTableSorter);
    	
    	
    	this.otkazaoKlijentPlaceholder = new JLabel("Nema tretmana koje je otkazao klijent.");
    	this.otkazaoSalonPlaceholder = new JLabel("Nema tretmana koje je otkazao salon.");
    	recheckPlaceholders();
    }
    
    private void setupLayout() {
        setLayout(new MigLayout("fill, insets 20", "[grow]", "[][][grow][][grow]"));

        add(new JLabel("Tretmani koje je otkazao klijent:"), "wrap");
        add(new JScrollPane(otkazaoKlijentTable), "grow, push, wrap");
        add(otkazaoKlijentPlaceholder, "center, wrap");

        add(new JLabel("Tretmani koje je otkazao salon:"), "wrap");
        add(new JScrollPane(otkazaoSalonTable), "grow, push, wrap");
        add(otkazaoSalonPlaceholder, "center");
    }
   
    
    
    private void recheckPlaceholders() {
    	this.otkazaoKlijentPlaceholder.setVisible(otkazaoKlijentTable.getRowCount() == 0);
    	this.otkazaoSalonPlaceholder.setVisible(otkazaoSalonTable.getRowCount() == 0);
    }
    
    public void updateData() {
    	if(otkazaoKlijentTable != null) {
    		((ZakazaniTretmaniTableModel) otkazaoKlijentTable.getModel())
    		.updateData(getZakazanTretmanForStatusFunction.apply(StatusTretmana.OTKAZAO_KLIJENT));
    	}
    	
    	if(otkazaoSalonTable != null) {
    		((ZakazaniTretmaniTableModel) otkazaoSalonTable.getModel())
    		.updateData(getZakazanTretmanForStatusFunction.apply(StatusTretmana.OTKAZAO_SALON));
    	}
    		
    	recheckPlaceholders();
    }

}
