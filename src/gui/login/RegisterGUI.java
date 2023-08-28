package gui.login;

import java.awt.event.KeyAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.function.Function;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.sun.glass.events.KeyEvent;

import entiteti.Klijent;
import entiteti.Pol;
import gui.interfaces.LoggedOutSalon;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class RegisterGUI extends JFrame{

	private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField nameField;
    private JTextField surnameField;
    private JComboBox<String> genderComboBox;
    private JTextField phoneNumberField;
    private JTextField addressField;
    private JButton registerButton;
    private JButton loginButton;
    
    private LoggedOutSalon loggedOutSalon;
    
    
    public RegisterGUI(LoggedOutSalon loggedOutSalon) {
    	this.loggedOutSalon = loggedOutSalon;
    	
    	super.setTitle("Registracija");
        super.setSize(360, 400);
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setLocationRelativeTo(null);
        
        initialize();
        setupLayout();
        addListeners();
    }
    
    
    
    private void initialize() {
    	this.usernameField = new JTextField(10);
        this.passwordField = new JPasswordField(10);
        this.nameField = new JTextField(10);
        this.surnameField = new JTextField(10);
        this.genderComboBox = new JComboBox<>(new String[]{"---Pol---", "Muski", "Zenski"});
        this.phoneNumberField = new JTextField(10);
        this.addressField = new JTextField(10);
        this.loginButton = new JButton("Login.");
        this.registerButton = new JButton("Registruj se");
    }
    
    
    private void setupLayout() {
    	JPanel panel = new JPanel(new MigLayout("align center center, gap 10"));

        panel.add(new JLabel("Korisnicko ime:"), "align label");
        panel.add(usernameField, "wrap, growx");

        panel.add(new JLabel("Lozinka:"), "align label");
        panel.add(passwordField, "wrap, growx");

        panel.add(new JLabel("Ime:"), "align label");
        panel.add(nameField, "wrap, growx");

        panel.add(new JLabel("Prezime:"), "align label");
        panel.add(surnameField, "wrap, growx");

        panel.add(new JLabel("Pol:"), "align label");
        panel.add(genderComboBox, "wrap, growx");

        panel.add(new JLabel("Broj telefona:"), "align label");
        panel.add(phoneNumberField, "wrap, growx");

        panel.add(new JLabel("Adresa:"), "align label");
        panel.add(addressField, "wrap, growx");

        JPanel buttonPanel = new JPanel(new MigLayout("align center"));
        buttonPanel.add(registerButton, "split 2, align center");
        buttonPanel.add(loginButton, "align center");

        panel.add(buttonPanel, "align center, span, wrap");
        getContentPane().add(panel);
        //getContentPane().add(buttonPanel);
    }
    
    
    private void addListeners() {
    	loginButton.addActionListener(x -> {
    		dispose();
            new LoginGUI(this.loggedOutSalon).setVisible(true);
        });
    	
    	super.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                loggedOutSalon.exit();
            	dispose();
            }
        });
    	
    	this.registerButton.addActionListener(x -> {
    		String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String name = nameField.getText();
            String surname = surnameField.getText();
            String gender = (String) genderComboBox.getSelectedItem();
            String phoneNumber = phoneNumberField.getText();
            String address = addressField.getText();
            
            if(!validateInput(username, password, name, surname, gender, phoneNumber, address)) {
            	return;
            }
            
            Pol genderPol = ("Muski".equals(gender)) ? Pol.MUSKI : Pol.ZENSKI;
            Klijent klijent = loggedOutSalon.registerKorisnik(name, surname, genderPol, phoneNumber, address, username, password);
            JOptionPane.showMessageDialog(RegisterGUI.this, "Registracija je uspjesna.", ":)", JOptionPane.INFORMATION_MESSAGE);
            loggedOutSalon.logIn(klijent);
            dispose();            
    	});
    	
    	KeyAdapter keyAdapter = new KeyAdapter() {

			@Override
			public void keyPressed(java.awt.event.KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (e.getSource() == addressField) {
                        registerButton.doClick();
                    }
                }
			}
            
        };
        
        addressField.addKeyListener(keyAdapter);
    }
    
    
    private boolean validateInput(String username, String password, String name, String surname, String gender, String phoneNumber, String address) {
    	Function<String, Boolean> showDialog = msg -> {
    		JOptionPane.showMessageDialog(RegisterGUI.this, msg, ":(", JOptionPane.ERROR_MESSAGE);
    		return true;
    	};
    	
    	return !( 
    			 username.trim().isEmpty() && showDialog.apply("Korisnicko ime je obavezno.") || 
    			 
    			 RegisterGUI.this.loggedOutSalon.usernameTaken(username) && showDialog.apply("Korisnicko ime '"+ username + "' je zauzeto.") ||
    			
    			  password.trim().isEmpty() && showDialog.apply("Lozinka je obavezna.") ||
    			  
    			  name.trim().isEmpty() && showDialog.apply("Ime je obavezno.") ||
    			  
    			  surname.trim().isEmpty() && showDialog.apply("Prezime je obavezno.") ||
    			  
    			  gender.equals("---Pol---") && showDialog.apply("Pol je obavezan.") ||
    			  
    			  phoneNumber.trim().isEmpty() && showDialog.apply("Broj telefona je obavezan.") || 
    			  
    			  !phoneNumber.matches("[0-9]+") && showDialog.apply("Broj telefona moze sadrzati samo cifre.") ||
    			  
    			  address.trim().isEmpty() && showDialog.apply("Adresa je obavezna.")    			  
    			);
    }
}
