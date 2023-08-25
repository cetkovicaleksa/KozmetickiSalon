package gui.klijent;

import java.awt.BorderLayout;

import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import entiteti.StatusTretmana;
import entiteti.ZakazanTretman;
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
	private ZakazaniTretmaniPanel zakazaniTretmaniPanel;
	private OtkazaniTretmaniPanel otkazaniTretmaniPanel;
	private IzvrseniTretmaniPanel izvrseniTretmaniPanel;
	
	
	
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
		zakazaniTretmaniPanel = new ZakazaniTretmaniPanel(

				() -> klijentSalon.getZakazaniTretmaniKlijenta().get(StatusTretmana.ZAKAZAN),

				(ZakazanTretman zakazanTretman) -> {
					klijentSalon.otkaziTretman(zakazanTretman);
					this.updateData();
				}
		);
		
		otkazaniTretmaniPanel = new OtkazaniTretmaniPanel(
				(StatusTretmana status) -> klijentSalon.getZakazaniTretmaniKlijenta().get(status)
	    );
		
		izvrseniTretmaniPanel = new IzvrseniTretmaniPanel(
				() -> klijentSalon.getZakazaniTretmaniKlijenta().get(StatusTretmana.IZVRSEN)
		);
	}
	
	private void setupLayout() {
		tabbedPane.addTab("Zakazi tretman.", zakazivanjeTretmanaPanel);
		tabbedPane.addTab("Prikaz i otkazivanje zakazanih tretmana.", zakazaniTretmaniPanel);
		tabbedPane.addTab("Otkazani tretmani.", otkazaniTretmaniPanel);
		tabbedPane.addTab("Izvrseni tretmani", izvrseniTretmaniPanel);
		tabbedPane.addTab("Podaci o klijentu.", infoPanel);
		
		add(tabbedPane, BorderLayout.CENTER);		
	}

	private void updateData() {
		otkazaniTretmaniPanel.updateData();
		zakazaniTretmaniPanel.updateData();
		izvrseniTretmaniPanel.updateData();
	}
	
	
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			KlijentGUI kg = new KlijentGUI(ZakazivanjeTretmanaPanel.getKlijentSalon());
			kg.setVisible(true);
		});
		
	}
}
