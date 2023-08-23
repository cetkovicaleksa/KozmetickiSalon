package gui.klijent.zakazivanjeTretmana;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


import entiteti.Klijent;
import entiteti.Kozmeticar;
import entiteti.KozmetickiTretman;
import gui.interfaces.KlijentSalon;
import javafx.scene.control.DatePicker;

@SuppressWarnings("serial")
public class ZakazivanjeTretmanaPanel extends JPanel{
	
	private HasTablesToUpdate updateTables;
	private KlijentSalon klijentSalon;
	
	
	private JButton backButton;
	private JButton forwardButton;
	private JPanel sharedPanel;
	
	
	private CardLayout cardLayout;
	private JPanel currentPanel;
	private int currentStep;
		
	
	
	private JTable table;
	private JList<Kozmeticar> kozmeticariList;
	
	private KozmetickiTretman.TipTretmana selectedTipTretmana;
	
	
	private DatePicker datePicker;
	private JComboBox<LocalTime> timePicker;
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
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
				//get the avaliable times for kozmeticar and date if it is present
			}
		});
		
		timePicker.addActionListener( e -> {
			LocalTime time = (LocalTime) timePicker.getSelectedItem();
			forwardButton.setEnabled( time != null );
		});
		
		datePicker.valueProperty().addListener( (observable, oldValue, newValue) -> {
			//get the avaliable times for kozmeticar and date if present
		});
	}
	
	
	
	
	private void finishedChoosingTretman() {
		if(selectedTipTretmana == null){
            JOptionPane.showMessageDialog(null, "Morate izabrati tip tretmana.", "Greska", JOptionPane.ERROR_MESSAGE);
            return;
        }
		
        cardLayout.next(currentPanel);
        ++currentStep;
        
        backButton.setEnabled(true);
        //forwardButton.setEnabled(true);
        forwardButton.setText("Preskoci odabir kozmeticara.");
	}
	
	private void finishedChoosingKozmeticar() {
		Kozmeticar kozmeticar = kozmeticariList.getSelectedValue();
		if(kozmeticar == null) {
			//TODO: pick random kozmeticar
		}
		
		++currentStep;
		forwardButton.setEnabled(false);
		forwardButton.setText("Zakazi");
	}
	
	private void finishedChoosingTiming() {
		LocalDate datum = datePicker.getValue();
		if(datum == null) {
			//dialog
		}
		
		LocalTime vrijeme = (LocalTime) timePicker.getSelectedItem();
		if(vrijeme == null) {
			//dialog
		}
		
		
	}
	
	
	
	
	
	public class JListDemo {
		
		JListDemo(){
			
		}

	    public void main(String[] args) {
	        SwingUtilities.invokeLater(() -> createAndShowGUI());
	    }

	    private void createAndShowGUI() {
	        JFrame frame = new JFrame("JList Demo");
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.setSize(300, 200);
	        frame.setLayout(new BorderLayout());

	        DefaultListModel<String> listModel = new DefaultListModel<>();
	        JList<String> itemList = new JList<>(listModel);
	        itemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

	        JButton addButton = new JButton("Add Item");
	        addButton.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                String newItem = JOptionPane.showInputDialog(frame, "Enter item:");
	                if (newItem != null && !newItem.isEmpty()) {
	                    listModel.addElement(newItem);
	                }
	            }
	        });

	        JButton removeButton = new JButton("Remove Item");
	        removeButton.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                int selectedIndex = itemList.getSelectedIndex();
	                if (selectedIndex != -1) {
	                    listModel.remove(selectedIndex);
	                }
	            }
	        });

	        frame.add(new JScrollPane(itemList), BorderLayout.CENTER);

	        JPanel buttonPanel = new JPanel();
	        buttonPanel.add(addButton);
	        buttonPanel.add(removeButton);
	        frame.add(buttonPanel, BorderLayout.SOUTH);

	        frame.setVisible(true);
	    }
	}
	
	
	
	public static void main(String[] args) {
		new ZakazivanjeTretmanaPanel().new JListDemo().main(new String[0]);
	}
	
	
}
