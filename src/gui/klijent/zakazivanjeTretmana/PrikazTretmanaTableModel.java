package gui.klijent.zakazivanjeTretmana;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import entiteti.Entitet;
import entiteti.KozmetickiTretman;

@SuppressWarnings("serial")
public class PrikazTretmanaTableModel extends AbstractTableModel{
	
	private Function<KozmetickiTretman.TipTretmana, Double> getPriceFunction;
	private Entitet[] tretmaniSaTipovimaTretmana;
		
	private String[] columnNames = {"Naziv kozmetickog tretmana", "Opis", "Naziv tipa tretmana", "Cijena", "Trajanje"};
	
	
	
	public PrikazTretmanaTableModel(Collection<Collection<KozmetickiTretman.TipTretmana>> grupisaniTipoviTretmana, Function<KozmetickiTretman.TipTretmana, Double> getPriceFunction) {
		this.getPriceFunction = getPriceFunction;
		setData(grupisaniTipoviTretmana);
	}
	

	public void setData(Collection<Collection<KozmetickiTretman.TipTretmana>> grupisaniTipoviTretmana) {
		int numberOfTreatments = grupisaniTipoviTretmana.size();
		
		int numberOfTypes = 0;
		for(Collection<KozmetickiTretman.TipTretmana> collectionOfTypes : grupisaniTipoviTretmana) {
			numberOfTypes += collectionOfTypes.size();
		}
		
		this.tretmaniSaTipovimaTretmana = new Entitet[numberOfTypes + numberOfTreatments];
		
		int row = 0;
		for(Collection<KozmetickiTretman.TipTretmana> collectionOfTypes : grupisaniTipoviTretmana) {
			List<KozmetickiTretman.TipTretmana> listOfTypes = new ArrayList<>(collectionOfTypes);
			tretmaniSaTipovimaTretmana[row++] = listOfTypes.get(0).getTretman();
			
			for(KozmetickiTretman.TipTretmana tipTretmana : listOfTypes) {
				tretmaniSaTipovimaTretmana[row++] = tipTretmana;
			}
		}
		
		fireTableDataChanged();
	}


	@Override
	public int getRowCount() {
		return tretmaniSaTipovimaTretmana.length;
	}


	@Override
	public int getColumnCount() {
		return columnNames.length;
	}
	
	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Entitet entitet = tretmaniSaTipovimaTretmana[rowIndex];
				
		if(entitet instanceof KozmetickiTretman) {
			KozmetickiTretman tretman = (KozmetickiTretman) entitet;
			
			switch (columnIndex) {
				case 0:
					return tretman.getNaziv();
				case 1:
					return tretman.getOpis();
				default:
					return (columnIndex < getColumnCount() ? "" : null);	
			}
		}
		
		if(entitet instanceof KozmetickiTretman.TipTretmana) {
			KozmetickiTretman.TipTretmana tretman = (KozmetickiTretman.TipTretmana) entitet;
			
			switch (columnIndex) {
			case 2:
				return tretman.getNaziv();
			case 3:
				return tretman.getCijena();
			case 4:
				return getPriceFunction.apply(tretman) + " min.";
			default:
				return (columnIndex < getColumnCount() ? "" : null);
			}
		}
		
		return null;
	}
	
	
	public KozmetickiTretman.TipTretmana getTipTretmana(int row){
		if(row < 0 || row > getRowCount()) {
			return null;
		}
		return (tretmaniSaTipovimaTretmana[row] instanceof KozmetickiTretman.TipTretmana ? 
				(KozmetickiTretman.TipTretmana) tretmaniSaTipovimaTretmana[row] : null);
	}
	
	public CustomListSelectionModel getSelectionModel() {
		return this.new CustomListSelectionModel();
	}
	
	public CustomCellRenderer getCellRenderer() {
		return this.new CustomCellRenderer();
	}
	
	public class CustomListSelectionModel extends DefaultListSelectionModel {
		
		@Override
	    public void setSelectionInterval(int index0, int index1) {
	        if (isSelectedIndex(index0)) {
	            removeSelectionInterval(index0, index1);
	        } else {
	            super.setSelectionInterval(index0, index1);
	        }
	    }

	    @Override
	    public void addSelectionInterval(int index0, int index1) {
	        setSelectionInterval(index0, index1);
	    }

		@Override
		public boolean isSelectedIndex(int index) {
			return (tretmaniSaTipovimaTretmana[index] instanceof KozmetickiTretman ? false : super.isSelectedIndex(index));
		}
		
	}
	
	public class CustomCellRenderer extends DefaultTableCellRenderer {
		
		private final Color firstColor = Color.WHITE;
		private final Color secondColor = Color.LIGHT_GRAY;
		private final Color selectedColor = Color.ORANGE;

	    @Override
	    public Component getTableCellRendererComponent(
	            JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

	        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	   
	        component.setBackground( (isSelected ?  selectedColor : getColor(row) ) );	        
	        return component;
	    }
	        
	    
	    
	    private Color getColor(int row) {
	    	if(tretmaniSaTipovimaTretmana[row] instanceof KozmetickiTretman) {
	    		if(row == 0) {
	    			return firstColor;
	    		}
	    		
	    		return (firstColor == getColor(--row) ? secondColor : firstColor);
	    	}
	    	
	    	return getColor(--row);	    		
	    	}
	    	
	    	
	    
	}
	
	
	public static void main(String[] aki) {
	    ArrayList<KozmetickiTretman.TipTretmana> num1 = new ArrayList<>();
	    KozmetickiTretman tretman1 = new KozmetickiTretman("Tretman Lica", "Osnovni tretman za revitalizaciju kože lica.");
	    num1.add(tretman1.newTipTretmana("Osnovni Tip 1", 3, 15));
	    num1.add(tretman1.newTipTretmana("Osnovni Tip 2", 4, 26));
	    num1.add(tretman1.newTipTretmana("Osnovni Tip 3", 0, 1));

	    ArrayList<KozmetickiTretman.TipTretmana> num2 = new ArrayList<>();
	    KozmetickiTretman tretman2 = new KozmetickiTretman("Tretman Tijela", "Luksuzni tretman za opuštanje i hidrataciju kože tijela.");
	    num2.add(tretman2.newTipTretmana("Luksuzni Tip 1", 31, 215));
	    num2.add(tretman2.newTipTretmana("Luksuzni Tip 2", 42, 262));
	    num2.add(tretman2.newTipTretmana("Luksuzni Tip 3", 01, 11));

	    ArrayList<KozmetickiTretman.TipTretmana> num6 = new ArrayList<>();
	    KozmetickiTretman tretman6 = new KozmetickiTretman("Tretman Tijela", "Luksuzni tretman za opuštanje i hidrataciju kože tijela.");
	    num6.add(tretman6.newTipTretmana("Luksuzni Tip 1", 31, 215));
	    num6.add(tretman6.newTipTretmana("Luksuzni Tip 2", 42, 262));
	    num6.add(tretman6.newTipTretmana("Luksuzni Tip 3", 01, 11));
	    
	    ArrayList<KozmetickiTretman.TipTretmana> num3 = new ArrayList<>();
	    KozmetickiTretman tretman3 = new KozmetickiTretman("Tretman Tijela", "Luksuzni tretman za opuštanje i hidrataciju kože tijela.");
	    num3.add(tretman3.newTipTretmana("Luksuzni Tip 1", 31, 215));
	    num3.add(tretman3.newTipTretmana("Luksuzni Tip 2", 42, 262));
	    num3.add(tretman3.newTipTretmana("Luksuzni Tip 3", 01, 11));
	    
	    ArrayList<KozmetickiTretman.TipTretmana> num4 = new ArrayList<>();
	    KozmetickiTretman tretman4 = new KozmetickiTretman("Tretman Tijela", "Luksuzni tretman za opuštanje i hidrataciju kože tijela.");
	    num4.add(tretman4.newTipTretmana("Luksuzni Tip 1", 31, 215));
	    num4.add(tretman4.newTipTretmana("Luksuzni Tip 2", 42, 262));
	    num4.add(tretman4.newTipTretmana("Luksuzni Tip 3", 01, 11));
	    
	    ArrayList<KozmetickiTretman.TipTretmana> num5 = new ArrayList<>();
	    KozmetickiTretman tretman5 = new KozmetickiTretman("Tretman Tijela", "Luksuzni tretman za opuštanje i hidrataciju kože tijela.");
	    num5.add(tretman5.newTipTretmana("Luksuzni Tip 1", 31, 215));
	    num5.add(tretman5.newTipTretmana("Luksuzni Tip 2", 42, 262));
	    num5.add(tretman5.newTipTretmana("Luksuzni Tip 3", 01, 11));
	    
	    Function<KozmetickiTretman.TipTretmana, Double> f = tt -> tt.getCijena();
	    List<List<KozmetickiTretman.TipTretmana>> tretmani = Arrays.asList(num1, num2, num3, num4, num5, num6);

	    // Create an instance of your PrikazTretmanaTableModel
	    //PrikazTretmanaTableModel model = new PrikazTretmanaTableModel(tretmani, f);

	    // Set the data for the model
	    //model.setData(tretmani);

	    // Create a JTable with your model
	    //JTable table = new JTable(model);

	    // Set the custom selection model
	    //CustomListSelectionModel selectionModel = model.new CustomListSelectionModel();
	    //table.setSelectionModel(selectionModel);

	    // Set the custom cell renderer
	    //CustomCellRenderer cellRenderer = model.new CustomCellRenderer();
	    //table.setDefaultRenderer(Object.class, cellRenderer);

	    // Create a JScrollPane to hold the table
	    //JScrollPane scrollPane = new JScrollPane(table);

	    // Create a JFrame to display the table
	    JFrame frame = new JFrame("Prikaz Tretmana Table");
	    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	    //frame.add(scrollPane);
	    frame.pack();
	    frame.setLocationRelativeTo(null);
	    frame.setVisible(true);
	}
}
