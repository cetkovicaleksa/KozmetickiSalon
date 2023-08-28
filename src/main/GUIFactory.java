package main;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.function.BiFunction;

import crudMenadzeri.RegistarMenadzera;
import entiteti.Klijent;
import entiteti.Korisnik;
import entiteti.Kozmeticar;
import entiteti.KozmetickiTretman;
import entiteti.KozmetickiTretman.TipTretmana;
import entiteti.Menadzer;
import entiteti.Recepcioner;
import entiteti.StatusTretmana;
import entiteti.ZakazanTretman;
import gui.KorisnikGUI;
import gui.interfaces.KlijentSalon;
import gui.interfaces.LoggedOutSalon;
import gui.klijent.KlijentGUI;
import gui.kozmeticar.KozmeticarGUI;
import gui.recepcioner.RecepcionerGUI;
import helpers.ThreeArgumentFunction;

public class GUIFactory {
	
	private final Map<Class<?>, ThreeArgumentFunction<Korisnik, LoggedOutSalon, RegistarMenadzera, KorisnikGUI>> guiMap;
	
	{
		guiMap = new HashMap<>();
		
		guiMap.put(Klijent.class, (klijent, salon, registar) -> getKlijentGUI((Klijent) klijent, salon, registar));
		guiMap.put(Kozmeticar.class, (kozmeticar, salon, registar) -> getKozmeticarGUI((Kozmeticar) kozmeticar, salon, registar));
		//guiMap.put(Recepcioner.class, (recepcioner, authenticator) -> getRecepcionerGUI((Recepcioner) recepcioner, null));
		//guiMap.put(Menadzer.class, (menadzer, authenticator) -> getMenadzerGUI((Menadzer) menadzer, null));
	}
	
	private static GUIFactory instance;
	
	private GUIFactory() {}
	
	
	public static GUIFactory getInstance() {
		if(instance == null) {
			instance = new GUIFactory();
		}
		
		return instance;
	}
	
	
	public KorisnikGUI getKorisnikGUI(Korisnik korisnik, LoggedOutSalon loggedOutSalon, RegistarMenadzera registar) throws IllegalArgumentException {
		ThreeArgumentFunction<Korisnik, LoggedOutSalon, RegistarMenadzera, KorisnikGUI> getGUI = guiMap.get(korisnik.getClass());
		
		if(getGUI == null) {
			throw new IllegalArgumentException("Unsupported Korisnik type.");
		}
		
		return getGUI.apply(korisnik, loggedOutSalon, registar);	
	}
	
	
	
	public KlijentGUI getKlijentGUI(Klijent klijent, LoggedOutSalon loggedOutSalon, RegistarMenadzera registar) {
		return new KlijentGUI(new KlijentSalon() {

			@Override
			public void logOut() {
				loggedOutSalon.login();
			}

			@Override
			public void exit() {
				loggedOutSalon.exit();				
			}

			@Override
			public Klijent getLoggedInKorisnik() {
				return klijent;
			}

			@Override
			public Map<StatusTretmana, Collection<ZakazanTretman>> getZakazaniTretmaniKlijenta() {
				return registar.getZakazaniTretmaniKorisnika(klijent, true, false);
			}

			@Override
			public Collection<Collection<TipTretmana>> getTretmaniSelection() {
				return registar.getTretmaniSelection();
			}

			@Override
			public double getPrice(TipTretmana tipTretmana) {
				return (klijent.getHasLoyaltyCard() ? 
						tipTretmana.getCijena() * registar.getSalonMenadzer().read().getLoyaltyCardDiscount() : tipTretmana.getCijena());
			}

			@Override
			public Collection<Kozmeticar> getKozmeticariThatCanPreformTreatment(KozmetickiTretman tretman) {
				return registar.getKozmeticariThatCanPreformTreatment(tretman);
			}

			@Override
			public SortedSet<Integer> getKozmeticarFreeHours(Kozmeticar kozmeticar, LocalDate datum,
					TipTretmana tipTretmana) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void zakaziTretman(TipTretmana tipTretmana, Kozmeticar kozmeticar, LocalDate datum,
					LocalTime vrijeme) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void otkaziTretman(ZakazanTretman zakazanTretman) {
				zakazanTretman.setStatus(StatusTretmana.OTKAZAO_KLIJENT);				
			}
		});
	}
	
	public KozmeticarGUI getKozmeticarGUI(Kozmeticar kozmeticar, LoggedOutSalon loggedOutSalon, RegistarMenadzera registar) {
		return null;
	}
	
	public RecepcionerGUI getRecepcionerGUI(Recepcioner recepcioner, RegistarMenadzera registar) {
		return null;
	}
	
	public KorisnikGUI getMenadzerGUI(Menadzer menadzer, RegistarMenadzera registar) {
		return null;
	}
	

}
