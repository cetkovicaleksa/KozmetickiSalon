package gui.kozmeticar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import entiteti.KozmetickiTretman;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class KozmetickiTretmanPanel extends JPanel{
	
	private Supplier<Collection<KozmetickiTretman>> tretmaniKozmeticaraSupplier;
	
	private JTable table;
	
	public KozmetickiTretmanPanel(Supplier<Collection<KozmetickiTretman>> tretmaniKozmeticaraSupplier) {
		this.tretmaniKozmeticaraSupplier = tretmaniKozmeticaraSupplier;
		
		initialize();
		setupLayout();
	}
	
	private void initialize() {
		table = new JTable( new KozmetickiTretmanTableModel(new ArrayList<>(tretmaniKozmeticaraSupplier.get())) );
		table.getTableHeader().setReorderingAllowed(false);
	}
	
	private void setupLayout() {
		setLayout(new MigLayout("fill, center, wrap 1", "[grow, center]")); // Set MigLayout for the panel
		JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); // Display vertical scrollbar when needed
        add(scrollPane, "center");
	}

}
