package gui.menadzer;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import gui.KorisnikGUI;
import gui.interfaces.IzvjestajiSalon;
import gui.interfaces.LoggedInSalon;

@SuppressWarnings("serial")
public class MenadzerGUI extends KorisnikGUI{
	
	private LoggedInSalon menadzerSalon;
	private IzvjestajiSalon izvjestajiSalon;
	
	private JTabbedPane tabbedPane;
	
	private JPanel izvjestajiPane;
	
	
	
	public MenadzerGUI(LoggedInSalon menadzerSalon, IzvjestajiSalon izvjestajSalon) {
		this.menadzerSalon = menadzerSalon;
		this.izvjestajiSalon = izvjestajiSalon;
		
		initialize();		
	}

	@Override
	protected LoggedInSalon getLoggedInSalon() {
		return menadzerSalon;
	}

	
	
	
	private void initialize() {
		this.izvjestajiPane = new IzvjestajiPanel(izvjestajiSalon);
	}
}
