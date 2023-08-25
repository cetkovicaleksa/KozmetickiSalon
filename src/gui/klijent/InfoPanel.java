package gui.klijent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import entiteti.Klijent;
import entiteti.Pol;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class InfoPanel extends JPanel{
	
	private Klijent klijent;
	
	public InfoPanel(Klijent klijent) {
		this.klijent = klijent;
		
		setupLayout();
	}
	
	private void setupLayout() {
		setLayout(new MigLayout("fill, center"));

	    JPanel cardPanel = new JPanel(new MigLayout("wrap 2", "[][grow, fill]"));
	    cardPanel.setBorder(BorderFactory.createEtchedBorder());  // Adding a border for the card effect
	    cardPanel.setOpaque(true);  // Make sure the background of the card is visible

	    cardPanel.add(new JLabel("Ime: "));
	    cardPanel.add(new JLabel(klijent.getIme()), "wrap");

	    cardPanel.add(new JLabel("Prezime: "));
	    cardPanel.add(new JLabel(klijent.getPrezime()), "wrap");

	    cardPanel.add(new JLabel("Korisnicko ime: "));
	    cardPanel.add(new JLabel(klijent.getKorisnickoIme()), "wrap");

	    cardPanel.add(new JLabel("Adresa: "));
	    cardPanel.add(new JLabel(klijent.getAdresa()), "wrap");

	    cardPanel.add(new JLabel("Telefon: "));
	    cardPanel.add(new JLabel(klijent.getBrojTelefona()), "wrap");

	    cardPanel.add(new JLabel("Pol: "));
	    cardPanel.add(new JLabel((Pol.MUSKI.equals(klijent.getPol())) ? "Muski" : "Zenski"	), "wrap");

	    cardPanel.add(new JLabel("Klijent ima karticu lojalnosti: "));
	    cardPanel.add(new JLabel((klijent.getHasLoyaltyCard()) ? "Da" : "Ne"), "wrap");

	    add(cardPanel, "center, center");  // Center the cardPanel both horizontally and vertically
	}

	
	
	public static void main(String[] args) {
        Klijent klijent = new Klijent(
                "ime", "prezime", "-65", "adresa", "korisnickoIme", "lozinka", Pol.ZENSKI, false);

        JFrame frame = new JFrame();
        frame.add(new InfoPanel(klijent));

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

}
