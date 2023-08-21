package gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

//import gui.interfaces.KozmetickiSalon;
import gui.interfaces.LoggedInSalon;

@SuppressWarnings("serial")
public abstract class KorisnikGUI extends JFrame{
	
	public KorisnikGUI() {
		addMenuBar();
		
		super.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				getLoggedInSalon().exit();
				dispose();
			}
			
		});
	}
	
	
	protected abstract LoggedInSalon getLoggedInSalon();
	
	private void logOut() {
		getLoggedInSalon().logOut();
		dispose();
	}
	
	
	private void addMenuBar() {
		JMenuBar menuBar = new JMenuBar();
        super.setJMenuBar(menuBar);
        
        JMenu menu = new JMenu("Log out");
        menuBar.add(menu);
        
        menu.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {}

			@Override
			public void mousePressed(MouseEvent e) {
				logOut();
			}

			@Override
			public void mouseReleased(MouseEvent e) {}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}
        	
        });
	}
}
