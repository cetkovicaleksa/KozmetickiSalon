package gui.klijent;

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
        setLayout(new MigLayout("fillx, wrap 2", "[][grow, fill]"));
        
        add(new JLabel("Ime: "));
        add(new JLabel(klijent.getIme()), "wrap");
        
        add(new JLabel("Prezime: "));
        add(new JLabel(klijent.getPrezime()), "wrap");
        
        add(new JLabel("Korisnicko ime: "));
        add(new JLabel(klijent.getKorisnickoIme()), "wrap");
        
        add(new JLabel("Adresa: "));
        add(new JLabel(klijent.getAdresa()), "wrap");
        
        add(new JLabel("Telefon: "));
        add(new JLabel(klijent.getBrojTelefona()), "wrap");
        
        add(new JLabel("Pol: "));
        add(new JLabel((Pol.MUSKI.equals(klijent.getPol())) ? "Muski" : "Zenski"), "wrap"); //can use ==
        
        add(new JLabel("Klijent ima karticu lojalnosti: "));
        add(new JLabel((klijent.getHasLoyaltyCard()) ? "Da" : "Ne"), "wrap");
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
