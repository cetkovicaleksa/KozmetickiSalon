package main;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
import gui.interfaces.KozmeticarSalon;
import gui.interfaces.LoggedOutSalon;
import gui.interfaces.RecepcionerSalon;
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
		guiMap.put(Recepcioner.class, (recepcioner, salon, registar) -> getRecepcionerGUI((Recepcioner) recepcioner, salon, registar));
		guiMap.put(Menadzer.class, (menadzer, salon, registar) -> getMenadzerGUI((Menadzer) menadzer, salon, registar));
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
				return registar.getTipTretmanaMenadzer().getTretmaniSelection();
			}

			@Override
			public double getPrice(TipTretmana tipTretmana) {
				double price = tipTretmana.getCijena();
				
				if(klijent.getHasLoyaltyCard()) {
					price *= registar.getSalonMenadzer().read().getLoyaltyCardDiscount();
				}
				
				return price;
			}

			@Override
			public Collection<Kozmeticar> getKozmeticariThatCanPreformTreatment(KozmetickiTretman tretman) {
				return registar.getKozmeticarMenadzer().allKozmeticariThatCanPreformTreatment(tretman);
			}

			@Override
			public SortedSet<Integer> getKozmeticarFreeHours(Kozmeticar kozmeticar, LocalDate datum,
					TipTretmana tipTretmana) {
				return GUIFactory.getKozmeticarFreeHours(registar, kozmeticar, datum, tipTretmana);
			}

			@Override
			public void zakaziTretman(TipTretmana tipTretmana, Kozmeticar kozmeticar, LocalDate datum,
					LocalTime vrijeme) {
				registar.zakaziTretman(tipTretmana, kozmeticar, getLoggedInKorisnik(), datum, vrijeme);
			}

			@Override
			public void otkaziTretman(ZakazanTretman zakazanTretman) {
				zakazanTretman.setStatus(StatusTretmana.OTKAZAO_KLIJENT);
				
				// return 90% of money to client
				zakazanTretman.setCijena(zakazanTretman.getCijena() * 0.1);
				
				// TODO: maby recheck if the client still satisfies the loyalty criteria
			}
		});
	}
	
	
	public KozmeticarGUI getKozmeticarGUI(Kozmeticar kozmeticar, LoggedOutSalon loggedOutSalon, RegistarMenadzera registar) {
		return new KozmeticarGUI(new KozmeticarSalon() {

			@Override
			public void logOut() {
				loggedOutSalon.login();				
			}

			@Override
			public void exit() {
				loggedOutSalon.exit();				
			}

			@Override
			public Kozmeticar getLoggedInKorisnik() {
				return kozmeticar;
			}

			@Override
			public Map<StatusTretmana, Collection<ZakazanTretman>> zakazaniTretmaniKozmeticara() {
				return registar.getZakazaniTretmaniKorisnika(kozmeticar, false, true);
			}

			@Override
			public void izvrsiTretman(ZakazanTretman tretman) {
				tretman.setStatus(StatusTretmana.IZVRSEN);				
			}

			@Override
			public SortedMap<LocalDate, SortedMap<LocalTime, ZakazanTretman>> rasporedKozmeticara() {
				return registar.getRasporedKozmeticara(kozmeticar);
			}
			
		});
	}
	
	
	public RecepcionerGUI getRecepcionerGUI(Recepcioner recepcioner, LoggedOutSalon loggedOutSalon, RegistarMenadzera registar) {
		return new RecepcionerGUI(new RecepcionerSalon() {

			@Override
			public void logOut() {
				loggedOutSalon.login();
			}

			@Override
			public void exit() {
				loggedOutSalon.exit();				
			}

			@Override
			public Recepcioner getLoggedInKorisnik() {
				return recepcioner;
			}

			@Override
			public void zakaziTretman(TipTretmana tipTretmana, Kozmeticar kozmeticar, Korisnik klijent, LocalDate datum,
					LocalTime vrijeme) {
				registar.zakaziTretman(tipTretmana, kozmeticar, klijent, datum, vrijeme);				
			}

			@Override
			public double getPrice(TipTretmana tipTretmana, Korisnik klijent) {
				double price = tipTretmana.getCijena();
				
				if(klijent instanceof Klijent && ((Klijent) klijent).getHasLoyaltyCard()) {
					price *= registar.getSalonMenadzer().read().getLoyaltyCardDiscount();
				}
				
				return price;
			}

			@Override
			public void otkaziTretman(ZakazanTretman zakazanTretman) {
				zakazanTretman.setStatus(StatusTretmana.OTKAZAO_SALON);
				
				// return 100% of the money to the client
				zakazanTretman.setCijena(0);
			}

			@Override
			public void updateZakazanTretman(TipTretmana tipTretmana, Kozmeticar kozmeticar, Korisnik klijent,
					LocalDate datum, LocalTime vrijeme, StatusTretmana status) {
				// TODO Auto-generated method stub
				// TODO stupid needs recheck
			}

			@Override
			public Collection<Collection<TipTretmana>> getTretmaniSelection() {
				return registar.getTipTretmanaMenadzer().getTretmaniSelection();
			}

			@Override
			public Collection<ZakazanTretman> getZakazaniTretmani() { //TODO: see how this works
				return StreamSupport.stream( 
						Spliterators.spliteratorUnknownSize(
					    		registar.getZakazanTretmanMenadzer().readAll(), Spliterator.ORDERED),
					    false
					   ).collect(Collectors.toList());
			}
			
		});
	}
	
	
	public KorisnikGUI getMenadzerGUI(Menadzer menadzer, LoggedOutSalon loggedOutSalon, RegistarMenadzera registar) {
		throw new IllegalArgumentException();
	}
	

	
	private static SortedSet<Integer> getKozmeticarFreeHours(RegistarMenadzera registar, Kozmeticar kozmeticar, LocalDate datum, KozmetickiTretman.TipTretmana tretman){
		SortedSet<Integer> freeHours = registar.getKozmeticarFreeHours(kozmeticar, datum);
		
		int trajanjeTretmana = tretman.getTrajanje(); // number of minutes
		if(trajanjeTretmana <= 60 || freeHours.isEmpty()) {
			return freeHours;
		}
		
		int brojTermina = (trajanjeTretmana % 60 == 0 ? trajanjeTretmana / 60 : trajanjeTretmana / 60 + 1);
		
		for(Integer hour : freeHours) {
			// TODO: need to remove all the hours that the treatment is too long to fit
		}
		
		return freeHours;
	}
}
