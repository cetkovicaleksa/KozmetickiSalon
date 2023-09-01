package gui.menadzer;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import gui.KorisnikGUI;
import gui.interfaces.IzvjestajiSalon;
import gui.interfaces.LoggedInSalon;
import gui.interfaces.MenadzerSalon;

@SuppressWarnings("serial")
public class MenadzerGUI extends KorisnikGUI{
	
	private MenadzerSalon menadzerSalon;
	
	private JTabbedPane tabbedPane;
	
	private IzvjestajiPanel izvjestajiPane;
	
	
	
	public MenadzerGUI(MenadzerSalon menadzerSalon) {
		this.menadzerSalon = menadzerSalon;
		
		setSize(800, 600);
		setLocationRelativeTo(null);
		setTitle("Menadzer: " + menadzerSalon.getLoggedInKorisnik());
		
		initialize();		
	}

	@Override
	protected LoggedInSalon getLoggedInSalon() {
		return menadzerSalon;
	}

	
	
	
	private void initialize() {
		this.izvjestajiPane = new IzvjestajiPanel(menadzerSalon.getIzvjestajiSalon());
		
		this.tabbedPane = new JTabbedPane();
		tabbedPane.add("Izvjestaji", izvjestajiPane);
		
		super.add(tabbedPane, BorderLayout.CENTER);
	}
}
