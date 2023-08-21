package gui.kozmeticar;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import entiteti.Klijent;
import entiteti.Kozmeticar;
import entiteti.KozmetickiTretman;
import entiteti.ZakazanTretman;
import gui.interfaces.KozmeticarSalon;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class RasporedPanel extends JPanel{  //TODO: need to add updating data when appointment is completed!!!

	private Supplier<SortedMap<LocalDate, SortedMap<LocalTime, ZakazanTretman>>> rasporedKozmeticaraSupplier;
	private Consumer<ZakazanTretman> izvrsiTretmanConsumer;
	public static final DateTimeFormatter FORMATER_DATUMA = java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
	
	private JTable table;
	
	public RasporedPanel(KozmeticarSalon kozmeticarSalon) {
		this(kozmeticarSalon::rasporedKozmeticara, kozmeticarSalon::izvrsiTretman);
	}
	
	public RasporedPanel(Supplier<SortedMap<LocalDate, SortedMap<LocalTime, ZakazanTretman>>> rasporedKozmeticaraSupplier, Consumer<ZakazanTretman> izvrsiTretmanConsumer) {
		this.rasporedKozmeticaraSupplier = rasporedKozmeticaraSupplier;
		this.izvrsiTretmanConsumer = izvrsiTretmanConsumer;
		
		initialize();
		setupLayout();
		addListeners();
	}
	
	private void initialize() {
		this.table = new JTable(new RasporedTableModel(rasporedKozmeticaraSupplier.get()));
		table.getTableHeader().setReorderingAllowed(false);
	}
	
	private void setupLayout() {
		setLayout(new BorderLayout());

	    JPanel contentPanel = new JPanel();
	    contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS)); // Vertical BoxLayout

	    JLabel label = new JLabel("Klikni na naziv tretmana za izvrsavanje.");
	    label.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the label horizontally
	    contentPanel.add(Box.createVerticalStrut(20)); // Add space on top
	    contentPanel.add(label);
	    contentPanel.add(Box.createVerticalStrut(20)); // Add space on bottom

	    contentPanel.add(new JScrollPane(table));

	    add(contentPanel, BorderLayout.CENTER);
	}
	
	private void addListeners() {
		table.addMouseMotionListener(new MouseAdapter() {
	        @Override
	        public void mouseMoved(MouseEvent e) {
	            int row = table.rowAtPoint(e.getPoint());
	            int column = table.columnAtPoint(e.getPoint());

	            // Check if the cursor is over a ZakazanTretman cell
	            if (column == 2 && ((RasporedTableModel)table.getModel()).getZakazanTretman(row) != null) {
	                table.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	            } else {
	                table.setCursor(Cursor.getDefaultCursor());
	            }
	        }
	    });
		
		table.addMouseListener(new MouseAdapter() {
	        @Override
	        public void mouseClicked(MouseEvent e) {
	            int row = table.rowAtPoint(e.getPoint());
	            int column = table.columnAtPoint(e.getPoint());

	            // Check if the clicked cell contains a ZakazanTretman
	            if (column == 2) {
	                ZakazanTretman selectedTretman = ((RasporedTableModel)table.getModel()).getZakazanTretman(row);
	                if (selectedTretman != null) {
	                	showZakazanTretmanPopup(selectedTretman);
	                }
	            }
	        }
	    });
	}
	
	
	private void showZakazanTretmanPopup(ZakazanTretman zakazanTretman) {
		JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Prikaz zakazanog tretmana.", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel dialogContent = new JPanel(new MigLayout("fill, wrap 2", "[grow][grow]"));
        
        JLabel kozmeticiTretmanLabel = new JLabel("Kozmeticki tretman:");
        JLabel tipTretmanaLabel = new JLabel("Tip tretmana:");
        JLabel klijentLabel = new JLabel("Klijent:");
        JLabel trajanjeLabel = new JLabel("Trajanje:");
        JLabel datumLabel = new JLabel("Datum:");
        JLabel vrijemeLabel = new JLabel("Vrijeme:");

        JLabel kozmeticiTretmanValue = new JLabel(zakazanTretman.getTipTretmana().getTretman().getNaziv());
        JLabel tipTretmanaValue = new JLabel(zakazanTretman.getTipTretmana().getNaziv());
        JLabel klijentValue = new JLabel(zakazanTretman.getKlijent().getKorisnickoIme());
        JLabel trajanjeValue = new JLabel(zakazanTretman.getTipTretmana().getTrajanje() + " min.");
        JLabel datumValue = new JLabel(FORMATER_DATUMA.format(zakazanTretman.getDatum()));
        JLabel vrijemeValue = new JLabel(zakazanTretman.getVrijeme().toString());

        JButton button = new JButton("Izvrsi tretman.");
        button.addActionListener(x -> {
        	String[] options = {"Da", "Ne", "Nazad"};
            int confirm = JOptionPane.showOptionDialog(
                dialog,
                "Da li ste sigurni da zelite da izvrsite tretman.",
                "Potvrda",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[1]
            );

            if (confirm == JOptionPane.NO_OPTION) {
                return;
            }

            if (confirm == JOptionPane.YES_OPTION) {
                this.izvrsiTretmanConsumer.accept(zakazanTretman);
                //TODO: see what to do
                //((RasporedTableModel) table.getModel())
            }

            dialog.dispose();       
        });

        
        dialogContent.add(kozmeticiTretmanLabel, "alignx leading");
        dialogContent.add(kozmeticiTretmanValue, "alignx trailing, growx");
        dialogContent.add(tipTretmanaLabel, "alignx leading");
        dialogContent.add(tipTretmanaValue, "alignx trailing, growx, span");
        dialogContent.add(klijentLabel, "alignx leading");
        dialogContent.add(klijentValue, "alignx trailing, growx");
        dialogContent.add(trajanjeLabel, "alignx leading");
        dialogContent.add(trajanjeValue, "alignx trailing, growx");
        dialogContent.add(datumLabel, "alignx leading");
        dialogContent.add(datumValue, "alignx trailing, growx");
        dialogContent.add(vrijemeLabel, "alignx leading");
        dialogContent.add(vrijemeValue, "alignx trailing, growx");
        dialogContent.add(button, "span 2, align center, gaptop 15");

        dialog.getContentPane().add(dialogContent);
        
        dialog.setPreferredSize(new Dimension(350, 400));
        dialog.setResizable(false);
        dialog.setTitle("Potvrda izvrsavanja tretmana.");

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
	
	
	public static void main(String[] args) {
		KozmetickiTretman kt = new KozmetickiTretman();
		kt.setNaziv("koz tret");
		
		KozmetickiTretman.TipTretmana tt = kt.newTipTretmana("tip tretmana", 2, 22);
		Kozmeticar k = new Kozmeticar();
		k.setKorisnickoIme("kozmeticar");
		
		Klijent kl = new Klijent();
		kl.setKorisnickoIme("klijent");
		ZakazanTretman zt = new ZakazanTretman(tt, k, kl, LocalDate.now(), LocalTime.now());
		
		Supplier<SortedMap<LocalDate, SortedMap<LocalTime, ZakazanTretman>>> rasporedKozmeticaraSupplier = () -> new TreeMap<>();
		Consumer<ZakazanTretman> izvrsiTretmanConsumer = z -> System.out.println(z.getTipTretmana().getNaziv());
		
		RasporedPanel raspPanel = new RasporedPanel(rasporedKozmeticaraSupplier, izvrsiTretmanConsumer);
		
		
		raspPanel.showZakazanTretmanPopup(zt);
	}
}
