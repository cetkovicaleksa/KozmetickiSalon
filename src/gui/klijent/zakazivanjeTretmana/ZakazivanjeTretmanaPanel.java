package gui.klijent.zakazivanjeTretmana;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.function.BooleanSupplier;
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
import javax.swing.table.TableCellRenderer;

import com.github.lgooddatepicker.components.DatePicker;

import entiteti.Klijent;
import entiteti.Kozmeticar;
import entiteti.KozmetickiTretman;
import entiteti.Pol;
import entiteti.KozmetickiTretman.TipTretmana;
import entiteti.StatusTretmana;
import entiteti.ZakazanTretman;
import gui.interfaces.KlijentSalon;

import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class ZakazivanjeTretmanaPanel extends JPanel{
	
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
	

	//private final BooleanSupplier backButtonEnabled = () -> currentStep > 0;
	
	private final BooleanSupplier forwardButtonEnabled = () -> (
			
			(currentStep == 1) ||
		    (currentStep == 0 && selectedTipTretmana != null) ||
		    (currentStep == 2 && timePicker.getSelectedIndex() != -1)
		    
		);
	
	private static final Supplier<String> backButtonText = () -> "Nazad";
	
	private final Supplier<String> forwardButtonText = () -> {
		if(currentStep == 1) {
			return (selectedKozmeticar == null ? "Preskoci odabir kozmeticara" : "Odaberi kozmeticara: " + selectedKozmeticar.getKorisnickoIme());
		}
		
		return (currentStep == 2 ? "Zakazi" : "Nastavi");
	};
				
	
	
	
	
	public ZakazivanjeTretmanaPanel(KlijentSalon klijentSalon) {
		this.klijentSalon = klijentSalon;
		
		initialize();
		setupLayout();
		addListeners();
	}
	
	
	
	
	
	
	
	private void initialize() {
		backButton = new JButton(backButtonText.get());
		forwardButton = new JButton(forwardButtonText.get());
		
		table = new JTable(new PrikazTretmanaTableModel(klijentSalon.getTretmaniSelection(), klijentSalon::getPrice));
		table.getTableHeader().setReorderingAllowed(false);
		table.setSelectionModel(((PrikazTretmanaTableModel) table.getModel()).getSelectionModel());
		TableCellRenderer renderer = ((PrikazTretmanaTableModel) table.getModel()).getCellRenderer();
				
		for (int columnIndex = 0; columnIndex < table.getColumnCount(); columnIndex++) {
			table.getColumnModel().getColumn(columnIndex).setCellRenderer(renderer);
	    }
		
		
		kozmeticariList = new JList<>();
		kozmeticariList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		datePicker = new DatePicker();
        timePicker = new JComboBox<>();
        reset();
	}
	
	
	private void setupLayout() {
		cardLayout = new CardLayout();
		currentPanel = new JPanel(cardLayout);
		setLayout(new MigLayout("fill", "", "[grow][]"));
		
		//step 0
		JPanel izborTipaTretmanaPanel = new JPanel(new MigLayout("fill"));
		izborTipaTretmanaPanel.add(new JLabel("Izbor tipa tretmana: "), "grow, wrap");
        izborTipaTretmanaPanel.add(new JScrollPane(table), "grow, wrap");

        
        
        //step 1
        JPanel izborKozmeticaraPanel = new JPanel(new MigLayout("fill, wrap 2", "", "[grow][]"));
        izborKozmeticaraPanel.add(new JLabel("Izaberite kozmeticara ili preskocite odabir kozmeticara cime Vam se on automatski dodjeljuje:"), "wrap");
        izborKozmeticaraPanel.add(kozmeticariList, "grow, span 2, wrap");
        
        //stap 2
        JPanel izborTajmingaPanel = new JPanel(new MigLayout("center center", "", "[grow][]"));
        JPanel innerPanel = new JPanel(new MigLayout("fill", "", "[grow][]"));

        innerPanel.add(new JLabel("Izaberite datum:"), "aligny center, wrap");
        innerPanel.add(datePicker, "aligny center, width 150, wrap");

        innerPanel.add(new JLabel("Izaberite vrijeme:"), "aligny center, wrap");
        innerPanel.add(timePicker, "aligny center, width 200, wrap");
        
        izborTajmingaPanel.add(innerPanel, "center");


        
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
			if(--currentStep == 0) {
				backButton.setEnabled(false);
			}
			
			forwardButton.setText(forwardButtonText.get());
			forwardButton.setEnabled(forwardButtonEnabled.getAsBoolean());
			if(currentStep == 1) {
				kozmeticariList.setSelectedValue(selectedKozmeticar, true);
			}
		});
		
		forwardButton.addActionListener( e -> {
			switch (currentStep) {
				case 0:
					finishedChoosingTretman();
					kozmeticariList.clearSelection();
					break;
				case 1:
					finishedChoosingKozmeticar();
					break;
				case 2:
					finishedChoosingTiming();
				default:
					return;
			}
			
			forwardButton.setEnabled(forwardButtonEnabled.getAsBoolean());
			forwardButton.setText(forwardButtonText.get());
		});
		
		table.getSelectionModel().addListSelectionListener( (ListSelectionEvent e) -> {
			if(e.getValueIsAdjusting()) {
				return;
			}
			
			int row = table.getSelectedRow();
			forwardButton.setEnabled( 
					(row != -1 && (selectedTipTretmana = ((PrikazTretmanaTableModel) table.getModel()).getTipTretmana(row)) != null) 
			);
			
			if(forwardButton.isEnabled()) {
				Collection<Kozmeticar> kozmeticari = klijentSalon.getKozmeticariThatCanPreformTreatment(selectedTipTretmana.getTretman());
				kozmeticariList.setListData(kozmeticari.toArray(new Kozmeticar[0]));
			}
		});
		
		kozmeticariList.getSelectionModel().addListSelectionListener( (ListSelectionEvent e) -> {
			if(!e.getValueIsAdjusting()) {
				selectedKozmeticar = kozmeticariList.getSelectedValue();
				forwardButton.setText(forwardButtonText.get());
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
	}
	
	private void finishedChoosingKozmeticar() {
		Kozmeticar kozmeticar = kozmeticariList.getSelectedValue();
		if(kozmeticar == null) {
			ListModel<Kozmeticar> model = kozmeticariList.getModel();
			if(model.getSize() <= 0) {
				JOptionPane.showMessageDialog(null, "Za izabrani tip tretmana ne postoji kozmeticar.", "Velika greska", JOptionPane.ERROR_MESSAGE);
				return;
			}
			int index = Math.abs(new Random().nextInt() + 1);
			
			kozmeticar = model.getElementAt(index % (model.getSize() - 1));
		}
		
		selectedKozmeticar = kozmeticar;
		cardLayout.next(currentPanel);
		++currentStep;
	}
	
	private void finishedChoosingTiming() {
		LocalDate datum = datePicker.getDate();
		if(datum == null) {
			JOptionPane.showMessageDialog(null, "Morate izabrati datum", "Greska", JOptionPane.ERROR_MESSAGE);
            return;
		}
		
		LocalTime vrijeme = (LocalTime) timePicker.getSelectedItem();
		if(vrijeme == null) {
			JOptionPane.showMessageDialog(null, "Morate izabrati vrijeme", "Greska", JOptionPane.ERROR_MESSAGE);
            return;
		}
		
		klijentSalon.zakaziTretman(selectedTipTretmana, selectedKozmeticar, datum, vrijeme);
		JOptionPane.showMessageDialog(null, "Tretman" + " '"+ selectedTipTretmana.getNaziv() + "' " + "je uspjesno zakazan kod kozmeticara" + " '"+ selectedKozmeticar.getIme() + "' " + datum + " u " + vrijeme + "h.", "Uspjeh", JOptionPane.INFORMATION_MESSAGE);	
		reset();
	}
	
	
	private void reset() {
		while(currentStep > 0) {
			currentStep--;
			cardLayout.previous(currentPanel);
		}
		
		backButton.setEnabled(false);
		forwardButton.setEnabled(forwardButtonEnabled.getAsBoolean());
		forwardButton.setText(forwardButtonText.get());
		
		datePicker.setDate(null);
		timePicker.setEnabled(false);
		timePicker.removeAllItems();
		
		kozmeticariList.clearSelection();
		table.clearSelection();
	}
	
	
	public void updateData() {
		((PrikazTretmanaTableModel) table.getModel()).setData(klijentSalon.getTretmaniSelection());
	}
	
	
	
	
	public static void main(String[] args) {
		
		
		
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame();
			JPanel panel = new ZakazivanjeTretmanaPanel(getKlijentSalon());
			
			frame.add(panel);
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.setSize(900, 400);
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
		});
	}
	
	public static void update() {
		System.out.println("Updating tables...");
	}
	
	public static KlijentSalon getKlijentSalon() {
		KozmetickiTretman.TipTretmana tt = new KozmetickiTretman("kt1", "opiskt1").newTipTretmana("tt", 3, 15);
		Klijent aki = new Klijent(
				"aleksa", "cetkovic", "1234567890", "adresa", "cetkovicaleksa", "lokomotiva",
				Pol.MUSKI, true
		);
		
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
				return aki;
			}

			@Override
			public Map<StatusTretmana, List<ZakazanTretman>> getZakazaniTretmaniKlijenta() {
				Map<StatusTretmana, List<ZakazanTretman>> zt = new HashMap<>();
				zt.put(StatusTretmana.ZAKAZAN, Collections.singletonList(new ZakazanTretman()));
				zt.put(StatusTretmana.OTKAZAO_KLIJENT, Collections.singletonList(new ZakazanTretman()));
				zt.put(StatusTretmana.OTKAZAO_SALON, new ArrayList<>());
				zt.put(StatusTretmana.NIJE_SE_POJAVIO, Collections.singletonList(new ZakazanTretman()));
				zt.put(StatusTretmana.IZVRSEN, Collections.singletonList(new ZakazanTretman()));
				return zt;
			}


			@Override
			public List<List<TipTretmana>> getTretmaniSelection() {
				List<List<TipTretmana>> l = new ArrayList<>();
				for(int i = 1; i<8; i++) {
					KozmetickiTretman kt = new KozmetickiTretman("kozmetickiTretman" + i, "opis kt" + i);
					
					l.add(Collections.singletonList(kt.newTipTretmana("tipTretmana" + i, i * 200, i * 15)));
				}
				return l;
			}

			@Override
			public double getPrice(TipTretmana tipTretmana) {
				return (getLoggedInKorisnik().getHasLoyaltyCard() ? tipTretmana.getCijena() * 10 / 100 : tipTretmana.getCijena());
			}

			@Override
			public List<Kozmeticar> getKozmeticariThatCanPreformTreatment(KozmetickiTretman tretman) {
				Supplier<Kozmeticar> kozmeticarSupplier = () -> new Kozmeticar() {
					{
						String imeIKIme = "kozmeticar" + new Random().nextInt();
						setKorisnickoIme(imeIKIme);
						setIme(imeIKIme);
						//addTretman(tt.getTretman());
					}
					@Override
					public String toString() {
						return this.getKorisnickoIme();
					}
				};
				
				
				
				ArrayList<Kozmeticar> l = new ArrayList<>();
				for(int i = 0; i<10 ; i++) {
					l.add(kozmeticarSupplier.get());
				}
				return l;
			}

			@Override
			public List<Integer> getKozmeticarFreeHours(Kozmeticar kozmeticar, LocalDate datum,
					TipTretmana tipoviTretmana) {
				// TODO Auto-generated method stub
				List<Integer> l = new ArrayList<>();
				for(int i = 2; i<10; i++) {
					l.add(i);
				}
				
				return l;
			}

			@Override
			public void zakaziTretman(TipTretmana tipTretmana, Kozmeticar kozmeticar, LocalDate datum,
					LocalTime vrijeme) {
				System.out.println("Zakazan tretman");
			}

			@Override
			public void otkaziTretman(ZakazanTretman zakazanTretman) {
				System.out.println("Otkazan tretman");
			}
			
		};
	}
}
