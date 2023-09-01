package main;

import java.io.IOException;

import crudMenadzeri.RegistarMenadzera;
import gui.interfaces.KozmetickiSalon;
import helpers.Settings;

public class App implements Runnable, KozmetickiSalon{
	
	private RegistarMenadzera registar;
	private final Authenticator authenticator = new Authenticator(this, this::getRegistar);

	
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
	
	
	
	@Override
	public void run() {
		load();
		authenticator.login();
	}
	
	
	public void load() {
		try {
			registar.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void exit() {
		try {
			registar.save();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

	
	public static void main(String[] args) {
		new App().run();
	}
}
