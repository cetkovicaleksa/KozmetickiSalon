package gui.kozmeticar;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import entiteti.StatusTretmana;
import entiteti.ZakazanTretman;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class ZakazanTretmanPanel extends JPanel{

	private Supplier<Map<StatusTretmana, List<ZakazanTretman>>> zakazaniTretmaniKozmeticaraSupplier;
	
	private JTable table;
	
	private JCheckBox zakazanCheckBox;
	private JCheckBox izvrsenCheckBox;
	private JCheckBox otkazaoKlijentCheckBox;
	private JCheckBox otkazaoSalonCheckBox;
	private JCheckBox nijeSePojavioCheckBox;
	
	
	public ZakazanTretmanPanel(Supplier<Map<StatusTretmana, List<ZakazanTretman>>> zakazaniTretmaniKozmeticaraSupplier) {
		this.zakazaniTretmaniKozmeticaraSupplier = zakazaniTretmaniKozmeticaraSupplier;
		
		initialize();
		addListeners();
		setupLayout();		
	}
	
	
	private void initialize() {		
		table = new JTable(new ZakazanTretmanTableModel(null));
		table.getTableHeader().setReorderingAllowed(false);
		
		zakazanCheckBox = new JCheckBox("Zakazani");
		zakazanCheckBox.setSelected(true);
		
		izvrsenCheckBox = new JCheckBox("Izvrseni");
		izvrsenCheckBox.setSelected(true);
		
		otkazaoKlijentCheckBox = new JCheckBox("Otkazao klijent");
		otkazaoKlijentCheckBox.setSelected(true);
		
		otkazaoSalonCheckBox = new JCheckBox("Otkazao salon");
		otkazaoSalonCheckBox.setSelected(true);
		
		nijeSePojavioCheckBox = new JCheckBox("Nije se pojavio klijent");
		nijeSePojavioCheckBox.setSelected(true);
		
		filterZakazaniTretmani();			
	}
	
	
	private void addListeners() {
		ActionListener al = e -> filterZakazaniTretmani();
		
		zakazanCheckBox.addActionListener(al);
		izvrsenCheckBox.addActionListener(al);
		otkazaoKlijentCheckBox.addActionListener(al);
		otkazaoSalonCheckBox.addActionListener(al);
		nijeSePojavioCheckBox.addActionListener(al);
	}
	
	private void setupLayout() {
		setLayout(new MigLayout("fillx, insets 10", "[][grow, fill]"));

	    add(new JLabel("Filtriraj zakazane tretmane:"), "split, wrap");
	    add(zakazanCheckBox, "align left, wrap");
	    add(izvrsenCheckBox, "align left, wrap");
	    add(otkazaoKlijentCheckBox, "align left, wrap");
	    add(otkazaoSalonCheckBox, "align left, wrap");
	    add(nijeSePojavioCheckBox, "align left, wrap");
	    
	    add(new JScrollPane(table), "span 2, grow, push");
	}
	
	private void filterZakazaniTretmani() {
		List<ZakazanTretman> sviTretmaniKozmeticara = new ArrayList<>();
		Map<StatusTretmana, Boolean> filter = new HashMap<>();
		
		filter.put(StatusTretmana.ZAKAZAN, zakazanCheckBox.isSelected());
		filter.put(StatusTretmana.IZVRSEN, izvrsenCheckBox.isSelected());
		filter.put(StatusTretmana.OTKAZAO_KLIJENT, otkazaoKlijentCheckBox.isSelected());
		filter.put(StatusTretmana.OTKAZAO_SALON, otkazaoSalonCheckBox.isSelected());
		filter.put(StatusTretmana.NIJE_SE_POJAVIO, nijeSePojavioCheckBox.isSelected());
		
		
		for(Map.Entry<StatusTretmana, List<ZakazanTretman>> entry : zakazaniTretmaniKozmeticaraSupplier.get().entrySet()) {
			if(filter.get(entry.getKey())) {
				sviTretmaniKozmeticara.addAll( entry.getValue() );
			}
		}
		
		((ZakazanTretmanTableModel) table.getModel()).setZakazaniTretmani(sviTretmaniKozmeticara);
	}
}
