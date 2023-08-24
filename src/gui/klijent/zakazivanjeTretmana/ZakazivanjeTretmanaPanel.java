package gui.klijent.zakazivanjeTretmana;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.github.lgooddatepicker.components.DatePicker;

import entiteti.Klijent;
import entiteti.Kozmeticar;
import entiteti.KozmetickiTretman;
import entiteti.KozmetickiTretman.TipTretmana;
import entiteti.StatusTretmana;
import entiteti.ZakazanTretman;
import gui.interfaces.KlijentSalon;

import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class ZakazivanjeTretmanaPanel extends JPanel{
	
	private HasTablesToUpdate updateTables;
	private KlijentSalon klijentSalon;
	
	
	private JButton backButton;
	private JButton forwardButton;
	
	private CardLayout cardLayout;
	private JPanel currentPanel;
	private int currentStep;
		
	private JTable table;
	private JList<Kozmeticar> kozmeticariList;
	
	private KozmetickiTretman.TipTretmana selectedTipTretmana;
	private Kozmeticar selectedKozmeticar;
	
	
	private DatePicker datePicker;
	private JComboBox<LocalTime> timePicker;
	
	public ZakazivanjeTretmanaPanel(KlijentSalon klijentSalon) {
		this(klijentSalon, null);
	}
	
	public ZakazivanjeTretmanaPanel(KlijentSalon klijentSalon, HasTablesToUpdate updateTables) {
		this.klijentSalon = klijentSalon;
		this.updateTables = updateTables;
		
		initialize();
		setupLayout();
		addListeners();
	}
	
	
	
	
	
	
	
	private void initialize() {
		backButton = new JButton("Nazad");
		forwardButton = new JButton("Nastavi");
		
		table = new JTable(new PrikazTretmanaTableModel(klijentSalon.getTretmaniSelection(), klijentSalon::getPrice));
		table.getTableHeader().setReorderingAllowed(false);
		
		kozmeticariList = new JList<>();
		
		datePicker = new DatePicker();
        timePicker = new JComboBox<>();
        reset(false);
	}
	
	
	private void setupLayout() {
		cardLayout = new CardLayout();
		currentPanel = new JPanel(cardLayout);
		setLayout(new MigLayout("fill", "", "[grow][]"));
		
		//step 0
		JPanel izborTipaTretmanaPanel = new JPanel(new MigLayout("fill"));
        izborTipaTretmanaPanel.add(new JScrollPane(table), "grow, wrap");

        izborTipaTretmanaPanel.add(new JLabel("Izbor tipa tretmana: "), "grow, wrap");
        
        //step 1
        JPanel izborKozmeticaraPanel = new JPanel(new MigLayout("fill, wrap 2", "", "[grow][]"));
        izborKozmeticaraPanel.add(new JLabel("Izaberite kozmeticara ili preskocite odabir kozmeticara cime Vam se on automatski dodjeljuje:"), "wrap");
        izborKozmeticaraPanel.add(kozmeticariList, "grow, span 2, wrap");
        
        //stap 2
        JPanel izborTajmingaPanel = new JPanel(new MigLayout());
        izborTajmingaPanel.add(new JLabel("Izaberite datum i vrijeme:"));
        izborTajmingaPanel.add(new JLabel("Izaberite datum:"));
        izborTajmingaPanel.add(datePicker, "width 200px, wrap");
        izborTajmingaPanel.add(new JLabel("Izaberite vrijeme:"));
        izborTajmingaPanel.add(timePicker, "width 200px, wrap");
        
        currentPanel.add(izborTipaTretmanaPanel);
        currentPanel.add(izborKozmeticaraPanel);
        currentPanel.add(izborTajmingaPanel);

        add(currentPanel, "grow, wrap");
        add(backButton, "split 2, sg button, center");
        add(forwardButton, "sg button");  
	}
	
	
	
	
	
	
	
	
	
	private void addListeners() {
		
		backButton.addActionListener( e -> {
			cardLayout.previous(currentPanel);
			if( --currentStep == 0 ) {
				backButton.setEnabled(false);
			}
		});
		
		forwardButton.addActionListener( e -> {
			switch (currentStep) {
				case 0:
					finishedChoosingTretman();
					break;
				case 1:
					finishedChoosingKozmeticar();
					break;
				case 2:
					finishedChoosingTiming();
					break;
				default:
					//nothing
			}
		});
		
		table.getSelectionModel().addListSelectionListener( (ListSelectionEvent e) -> {
			if(e.getValueIsAdjusting()) {
				return;
			}
			
			int row = table.getSelectedRow();
			
			if(row == -1) {
				forwardButton.setEnabled(false);
			}else {
				forwardButton.setEnabled(true);
				selectedTipTretmana = ((PrikazTretmanaTableModel) table.getModel()).getTipTretmana(row);
				//get the data for kozmeticar panel
				Collection<Kozmeticar> kozmeticari = klijentSalon.getKozmeticariThatCanPreformTreatment(selectedTipTretmana.getTretman());
				kozmeticariList.setListData(kozmeticari.toArray(new Kozmeticar[0]));
			}
		});
		
		kozmeticariList.getSelectionModel().addListSelectionListener( (ListSelectionEvent e) -> {
			if(!e.getValueIsAdjusting()) {
				loadTimes();
			}
		});
		
		timePicker.addActionListener( e -> {
			LocalTime time = (LocalTime) timePicker.getSelectedItem();
			forwardButton.setEnabled( time != null );
		});
		
		datePicker.addDateChangeListener(dCL -> {
			loadTimes();
		});
	}
	
	
	
	private void loadTimes() {
		if(selectedKozmeticar == null) { //TODO
			timePicker.removeAllItems();
			return;
		}
		
		LocalDate datum = datePicker.getDate();
		forwardButton.setEnabled(true);
		timePicker.removeAllItems();
		if(datum == null) {
			return;
		}
		
		if(datum.isBefore(LocalDate.now())) {
			timePicker.setEnabled(false);
			return;
		}
		
		for(int vrijeme : klijentSalon.getKozmeticarFreeHours(selectedKozmeticar, datum, selectedTipTretmana)) {
			timePicker.addItem(LocalTime.of(vrijeme, 0));
		}
		
		timePicker.setEnabled(true);
	}
	
	
	
	private void finishedChoosingTretman() {
		if(selectedTipTretmana == null){
            JOptionPane.showMessageDialog(null, "Morate izabrati tip tretmana.", "Greska", JOptionPane.ERROR_MESSAGE);
            return;
        }
		
        cardLayout.next(currentPanel);
        ++currentStep;
        
        backButton.setEnabled(true);
        //forwardButton.setEnabled(false);
        forwardButton.setText("Preskoci odabir kozmeticara.");
	}
	
	private void finishedChoosingKozmeticar() {
		Kozmeticar kozmeticar = kozmeticariList.getSelectedValue();
		if(kozmeticar == null) {
			//TODO: pick random kozmeticar
			int index = new Random().nextInt();
			ListModel<Kozmeticar> model = kozmeticariList.getModel();
			kozmeticar = model.getElementAt(index % (model.getSize() - 1));
		}
		
		selectedKozmeticar = kozmeticar;
		cardLayout.next(currentPanel);
		++currentStep;
		forwardButton.setEnabled(false);
		forwardButton.setText("Zakazi");
	}
	
	private void finishedChoosingTiming() {
		LocalDate datum = datePicker.getDate();
		if(datum == null) {
			JOptionPane.showMessageDialog(null, "Morate izabrati datum", "Greska", JOptionPane.ERROR_MESSAGE);
            return;
		}
		
		LocalTime vrijeme = (LocalTime) timePicker.getSelectedItem();
		if(vrijeme == null) {
			JOptionPane.showMessageDialog(null, "Morate izabrati datum", "Greska", JOptionPane.ERROR_MESSAGE);
            return;
		}
		
		klijentSalon.zakaziTretman(selectedTipTretmana, selectedKozmeticar, datum, vrijeme);
		JOptionPane.showMessageDialog(null, "Tretman je uspjesno zakazan.", "Uspjeh", JOptionPane.INFORMATION_MESSAGE);	
		reset(true);
	}
	
	
	private void reset(boolean update) {
		currentStep = 0;
		//cardLayout.previous(currentPanel);
		//cardLayout.previous(currentPanel);
		
		backButton.setEnabled(false);
		forwardButton.setEnabled(false);
		forwardButton.setText("Nastavi");
		
		datePicker.setDate(null);
		timePicker.setEnabled(false);
		timePicker.removeAllItems();
		
		kozmeticariList.clearSelection();
		table.clearSelection();
		
		if(update && updateTables != null) {
			updateTables.onUpdate();
		}
	}
	
	
	
	
	
	public static void main(String[] args) {
		
		
		
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame();
			JPanel panel = new ZakazivanjeTretmanaPanel(getKlijentSalon(), ZakazivanjeTretmanaPanel::update);
			
			frame.add(panel);
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.setVisible(true);
		});
	}
	
	private static void update() {
		System.out.println("Updating tables...");
	}
	
	private static KlijentSalon getKlijentSalon() {
		return new KlijentSalon() {

			@Override
			public void logOut() {
				System.out.println("Logged out.");
				
			}

			@Override
			public void exit() {
				System.out.println("Exited.");
				
			}

			@Override
			public Klijent getLoggedInKorisnik() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Map<StatusTretmana, ZakazanTretman> zakazaniTretmaniKlijenta() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean stanjeNaKarticiLojalnosti() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public List<List<TipTretmana>> getTretmaniSelection() {
				// TODO Auto-generated method stub
				return new ArrayList<>();
			}

			@Override
			public double getPrice(TipTretmana tipTretmana) {
				return (stanjeNaKarticiLojalnosti() ? tipTretmana.getCijena() * 10 / 100 : tipTretmana.getCijena());
			}

			@Override
			public List<Kozmeticar> getKozmeticariThatCanPreformTreatment(KozmetickiTretman tretman) {
				// TODO Auto-generated method stub
				return new ArrayList<>();
			}

			@Override
			public List<Integer> getKozmeticarFreeHours(Kozmeticar kozmeticar, LocalDate datum,
					TipTretmana... tipoviTretmana) {
				// TODO Auto-generated method stub
				return new ArrayList<>();
			}

			@Override
			public void zakaziTretman(TipTretmana tipTretmana, Kozmeticar kozmeticar, LocalDate datum,
					LocalTime vrijeme) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void otkaziTretman(ZakazanTretman zakazanTretman) {
				// TODO Auto-generated method stub
				
			}
			
		};
	}
}
