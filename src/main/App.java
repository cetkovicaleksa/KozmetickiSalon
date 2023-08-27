package main;

import java.io.IOException;

import crudMenadzeri.RegistarMenadzera;
import gui.login.LoginGUI;
import helpers.Settings;

public class App {
	
	private RegistarMenadzera registar;
	private final Authenticator authenticator = new Authenticator(this::getRegistar);

	
	public App() {
		this(Settings.getDefaultSettings());
	}
	
	public App(Settings settings) {
		setRegistar(new RegistarMenadzera(settings));
	}
	
	
	
	public RegistarMenadzera getRegistar() {
		return registar;
	}
	
	public void setRegistar(RegistarMenadzera registar) {
		this.registar = registar;
	}
	
	
	
	
	public void login() {
		new LoginGUI(authenticator).setVisible(true);
	}
	
	public void load() {
		try {
			registar.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run() {
		load();
		login();
	}

	

	
	public static void main(String[] args) {
		App app = new App();
		//app.load();
		app.login();
	}
}
