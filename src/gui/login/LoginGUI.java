package gui.login;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.function.Function;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import entiteti.Klijent;
import entiteti.Korisnik;
import entiteti.Pol;
import exceptions.PasswordMissmatchException;
import exceptions.UsernameNotFoundException;
import gui.interfaces.LoggedOutSalon;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class LoginGUI extends JFrame {
	
	private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    
    private LoggedOutSalon loggedOutSalon;
    
    
    public LoginGUI(LoggedOutSalon loggedOutSalon) {
    	this.loggedOutSalon = loggedOutSalon;
    	
    	super.setTitle("Login");
        super.setSize(360, 240);
        super.setLocationRelativeTo(null);
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        initialize();
        addListeners();
        setupLayout();
    }
    
    
    
    private void initialize() {
    	this.usernameField = new JTextField(10);
        this.passwordField = new JPasswordField(10);
        this.loginButton = new JButton("Uloguj se");
        this.registerButton = new JButton("Registracija");
    }
    
    
    private void setupLayout() {
    	JPanel panel = new JPanel();
    	panel.setLayout(new MigLayout("align 50% 50%"));

        panel.add(new JLabel("Korisnicko ime:"), "align left");
        panel.add(usernameField, "wrap");

        panel.add(new JLabel("Lozinka:"), "align left");
        panel.add(passwordField, "wrap");

        panel.add(loginButton, "gapTop 1%, span 2, split 2, align center, sizegroup btn");
        panel.add(registerButton, "align center, sizegroup btn");

        getContentPane().add(panel);
    }

    
    private void addListeners() {
    	super.addWindowListener(new WindowAdapter() {
            
    		@Override
            public void windowClosing(WindowEvent e) {
            	loggedOutSalon.exit();
                dispose();
            }
            
        });
    	
    	registerButton.addActionListener(x -> {
    		dispose();
    		new RegisterGUI(this.loggedOutSalon).setVisible(true);
    	});
    	
    	loginButton.addActionListener(x -> {
    		String username = usernameField.getText();
			String password = new String(passwordField.getPassword()); //TODO: recheck
				
			if(!validateInput(username, password)) {
				return;
			}
				
			try {
				loggedOutSalon.logIn( loggedOutSalon.authenticateKorisnik(username, password) );
			}catch(UsernameNotFoundException unfe) {
				JOptionPane.showMessageDialog(LoginGUI.this, "Korisnicko ime nije pronadjeno.", ":(", JOptionPane.ERROR_MESSAGE);					
			}catch(PasswordMissmatchException pme) {
				JOptionPane.showMessageDialog(LoginGUI.this, "Pogresna lozinka", ":(", JOptionPane.ERROR_MESSAGE);
			}
			//TODO: maby add some more catch statements?
		});
    }
    
    
    private boolean validateInput(String username, String password) {
    	Function<String, Void> showDialog = msg -> {
    		JOptionPane.showMessageDialog(LoginGUI.this, msg, ":(", JOptionPane.ERROR_MESSAGE);
    		return null;
    	};
    	
    	if(username.trim().isEmpty() && password.trim().isEmpty()) {
    		showDialog.apply("Molimo unesite korisnicko ime i lozinku.");
    		return false;
    	}
    	
    	if(username.trim().isEmpty()) {
    		showDialog.apply("Molimo unesite korisnicko ime.");
    		return false;
    	}
    	
    	if(password.trim().isEmpty()) {
    		showDialog.apply("Molimo unesite lozinku.");
    		return false;
    	}
    	
    	return true;    	
    }
    
    
    public static void main(String args[]) {
    	
    	LoggedOutSalon los = new LoggedOutSalon() {

			@Override
			public void exit() {
				System.out.println("Saving data...");
				
			}

			@Override
			public void logIn(Korisnik korisnik) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public Korisnik authenticateKorisnik(String username, String password)
					throws UsernameNotFoundException, PasswordMissmatchException {
				throw new PasswordMissmatchException();
				//return null;
			}

			@Override
			public Klijent registerKorisnik(String name, String surname, Pol sex, String phoneNumber, String adress,
					String username, String password) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean usernameTaken(String username) {
				// TODO Auto-generated method stub
				return false;
			}
    		
    	};
    	
    	
    	RegisterGUI lg = new RegisterGUI(los);
    	lg.setVisible(true);
    	//lg.validateInput("s", "123");
    	
    	
    }
}
