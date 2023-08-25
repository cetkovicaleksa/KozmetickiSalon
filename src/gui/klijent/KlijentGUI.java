package gui.klijent;

import java.awt.BorderLayout;

import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import gui.KorisnikGUI;
import gui.interfaces.KlijentSalon;
import gui.interfaces.LoggedInSalon;
import gui.klijent.zakazivanjeTretmana.ZakazivanjeTretmanaPanel;

@SuppressWarnings("serial")
public class KlijentGUI extends KorisnikGUI{
	
	private KlijentSalon klijentSalon;
	
	private JTabbedPane tabbedPane;
	private ZakazivanjeTretmanaPanel zakazivanjeTretmanaPanel;
	private InfoPanel infoPanel;
	
	
	
	public KlijentGUI(KlijentSalon klijentSalon) {
		this.klijentSalon = klijentSalon;
		
		setTitle("Klijent: " + klijentSalon.getLoggedInKorisnik().getKorisnickoIme());
		setSize(900, 600);
		setLocationRelativeTo(null);
		initialize();
		setupLayout();
	}
	
	
	

	@Override
	protected LoggedInSalon getLoggedInSalon() {
		return klijentSalon;
	}
	
	
	private void initialize() {
		tabbedPane = new JTabbedPane();
		zakazivanjeTretmanaPanel = new ZakazivanjeTretmanaPanel(klijentSalon, this::updateData);
		infoPanel = new InfoPanel(klijentSalon.getLoggedInKorisnik());
	}
	
	private void setupLayout() {
		tabbedPane.addTab("Zakazi tretman", zakazivanjeTretmanaPanel);
		tabbedPane.addTab("Podaci o klijentu.", infoPanel);
		
		add(tabbedPane, BorderLayout.CENTER);		
	}

	private void updateData() {
		System.out.println("updating data...");
	}
	
	
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			KlijentGUI kg = new KlijentGUI(ZakazivanjeTretmanaPanel.getKlijentSalon());
			kg.setVisible(true);
		});
		
	}
}
