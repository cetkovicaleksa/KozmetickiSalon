package gui.menadzer;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import gui.interfaces.IzvjestajiSalon;

@SuppressWarnings("serial")
public class IzvjestajiPanel extends JPanel {
	
	private IzvjestajiSalon izvjestajiSalon;
	
	private IzvjestajZakazanihTretmanaPoStatusu izvjestajZakazanihTretmanaPoStatusu;
	private JButton openIzvjestajiZakazanihTretmanaPoStatusu;
	
	
	
	public IzvjestajiPanel(IzvjestajiSalon salon) {
		this.izvjestajiSalon = salon;
		
		initialize();
		setupListeners();
	}
	
	
	private void initialize() {
		this.izvjestajZakazanihTretmanaPoStatusu = new IzvjestajZakazanihTretmanaPoStatusu(izvjestajiSalon::izvjestajBrojaTretmanaPoRazlozima);
		this.openIzvjestajiZakazanihTretmanaPoStatusu = new JButton("IZVJESTAJ BROJA TRETMANA PO STATUSU");
	}
	
	
	
	private void setupListeners() {
		this.openIzvjestajiZakazanihTretmanaPoStatusu.addActionListener(ae -> {
			this.izvjestajZakazanihTretmanaPoStatusu.setLocationRelativeTo(null);
			this.izvjestajZakazanihTretmanaPoStatusu.setVisible(true);
			
			this.izvjestajZakazanihTretmanaPoStatusu.addWindowListener(new WindowListener() {

				@Override
				public void windowOpened(WindowEvent e) {
					openIzvjestajiZakazanihTretmanaPoStatusu.setEnabled(false);					
				}

				@Override
				public void windowClosing(WindowEvent e) {
					// TODO Auto-generated method stub
				}

				@Override
				public void windowClosed(WindowEvent e) {
					openIzvjestajiZakazanihTretmanaPoStatusu.setEnabled(true);					
				}

				@Override
				public void windowIconified(WindowEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void windowDeiconified(WindowEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void windowActivated(WindowEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void windowDeactivated(WindowEvent e) {
					// TODO Auto-generated method stub
					
				}
				
			});			
		});
	}

}
