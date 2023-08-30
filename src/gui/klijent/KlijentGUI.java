package gui.klijent;

import java.awt.BorderLayout;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import entiteti.Klijent;
import entiteti.Kozmeticar;
import entiteti.KozmetickiTretman;
import entiteti.KozmetickiTretman.TipTretmana;
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
		
		setTitle("Klijent: " + klijentSalon.getLoggedInKorisnik());
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
		zakazivanjeTretmanaPanel = new ZakazivanjeTretmanaPanel(new KlijentSalon() {

			@Override
			public void logOut() {
				KlijentGUI.super.logOut();				
			}

			@Override
			public void exit() {
				KlijentGUI.super.exit();
				
			}

			@Override
			public Klijent getLoggedInKorisnik() {
				return klijentSalon.getLoggedInKorisnik();
			}

			@Override
			public Map<StatusTretmana, Collection<ZakazanTretman>> getZakazaniTretmaniKlijenta() {
				return klijentSalon.getZakazaniTretmaniKlijenta();
			}

			@Override
			public Collection<Collection<TipTretmana>> getTretmaniSelection() {
				return klijentSalon.getTretmaniSelection();
			}

			@Override
			public double getPrice(TipTretmana tipTretmana) {
				return klijentSalon.getPrice(tipTretmana);
			}

			@Override
			public Collection<Kozmeticar> getKozmeticariThatCanPreformTreatment(KozmetickiTretman tretman) {
				return klijentSalon.getKozmeticariThatCanPreformTreatment(tretman);
			}

			@Override
			public SortedSet<Integer> getKozmeticarFreeHours(Kozmeticar kozmeticar, LocalDate datum,
					TipTretmana tipoviTretmana) {
				return klijentSalon.getKozmeticarFreeHours(kozmeticar, datum, tipoviTretmana);
			}

			@Override
			public void zakaziTretman(TipTretmana tipTretmana, Kozmeticar kozmeticar, LocalDate datum,
					LocalTime vrijeme) {
				klijentSalon.zakaziTretman(tipTretmana, kozmeticar, datum, vrijeme);
				KlijentGUI.this.updateData();
			}

			@Override
			public void otkaziTretman(ZakazanTretman zakazanTretman) {
				klijentSalon.otkaziTretman(zakazanTretman);
				KlijentGUI.this.updateData();
			}
		});
		
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
