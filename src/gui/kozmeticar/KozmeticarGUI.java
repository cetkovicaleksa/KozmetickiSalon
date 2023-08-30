package gui.kozmeticar;

import java.awt.BorderLayout;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.swing.JTabbedPane;

import entiteti.Klijent;
import entiteti.Korisnik;
import entiteti.Kozmeticar;
import entiteti.KozmetickiTretman;
import entiteti.StatusTretmana;
import entiteti.ZakazanTretman;
import gui.KorisnikGUI;
import gui.interfaces.KozmeticarSalon;
import gui.interfaces.LoggedInSalon;

@SuppressWarnings("serial")
public class KozmeticarGUI extends KorisnikGUI{
	
	private KozmeticarSalon kozmeticarSalon;
	
	private JTabbedPane jTabbedPane;
	private KozmetickiTretmanPanel kozmetickiTretmanPanel;
	private ZakazanTretmanPanel zakazanTretmanPanel;
	private RasporedPanel rasporedPanel;

	
	public KozmeticarGUI(KozmeticarSalon kozmeticarSalon) {
		super();
		this.kozmeticarSalon = kozmeticarSalon;
		
		super.setSize(800, 600);
		super.setLocationRelativeTo(null);
		super.setTitle(kozmeticarSalon.getLoggedInKorisnik().getKorisnickoIme());
		initialize();
		setupLayout();
	}

	@Override
	protected LoggedInSalon getLoggedInSalon() {
		return kozmeticarSalon;
	}
	
	private void initialize(){
		jTabbedPane = new JTabbedPane();
		kozmetickiTretmanPanel = new KozmetickiTretmanPanel(kozmeticarSalon.getLoggedInKorisnik()::getTretmani);
		zakazanTretmanPanel = new ZakazanTretmanPanel(kozmeticarSalon::zakazaniTretmaniKozmeticara);
		rasporedPanel = new RasporedPanel(
				kozmeticarSalon::rasporedKozmeticara,
				(ZakazanTretman zt) -> {
					kozmeticarSalon.izvrsiTretman(zt);
					KozmeticarGUI.this.updateData();
				}
		);
	}
	
	private void setupLayout() {
		jTabbedPane.addTab("Vasi kozmeticki tretmani.", kozmetickiTretmanPanel);
		jTabbedPane.addTab("Prikaz zakazanih tretmana.", zakazanTretmanPanel);
		jTabbedPane.addTab("Raspored.", rasporedPanel);
		super.add(jTabbedPane, BorderLayout.CENTER);
	}
	
	
	private void updateData() {
		this.rasporedPanel.updateData();
		//this.zakazanTretmanPanel.updateData();
	}
	
	
	
	public static void min(String[] args) {
        // Create 5 ArrayLists of ZakazanTretman instances
        ArrayList<ZakazanTretman> zakazaniList1 = createZakazaniList(3);
        ArrayList<ZakazanTretman> zakazaniList2 = createZakazaniList(3);
        ArrayList<ZakazanTretman> zakazaniList3 = createZakazaniList(3);
        ArrayList<ZakazanTretman> zakazaniList4 = createZakazaniList(3);
        ArrayList<ZakazanTretman> zakazaniList5 = createZakazaniList(3);
        
        

        
    }

    private static ArrayList<ZakazanTretman> createZakazaniList(int count) {
        ArrayList<ZakazanTretman> zakazaniList = new ArrayList<>();

        Kozmeticar kozmeticar = new Kozmeticar();
        kozmeticar.setKorisnickoIme("koz");
        Klijent klijent = new Klijent();
        klijent.setIme("John");
        klijent.setPrezime("Doe");

        for (int i = 0; i < count; i++) {
            KozmetickiTretman.TipTretmana tt = new KozmetickiTretman("nazivkt", "opiskt").newTipTretmana("nazivtt", 100, 30);
            ZakazanTretman zt = new ZakazanTretman(tt, kozmeticar, klijent, LocalDate.now(), LocalTime.now());
            zakazaniList.add(zt);
        }

        return zakazaniList;
    }
    
    
    
    public static SortedMap<LocalDate, SortedMap<LocalTime, ZakazanTretman>> convertToSortedMap(List<ZakazanTretman> zakazanTretmanList) {
        SortedMap<LocalDate, SortedMap<LocalTime, ZakazanTretman>> resultMap = new TreeMap<>();

        for (ZakazanTretman tretman : zakazanTretmanList) {
            LocalDate date = tretman.getDatum();
            LocalTime time = tretman.getVrijeme();

            // Initialize inner SortedMap if not present
            resultMap.computeIfAbsent(date, k -> new TreeMap<>())
                    .put(time, tretman);
        }

        return resultMap;
    }
    
	public static void main(String[] args) {
		KozmeticarSalon ks = new KozmeticarSalon() {

			@Override
			public void logOut() {
				System.out.println("LOGOUT");
				
			}

			@Override
			public void exit() {
				System.out.println("EXIT");
				
			}

			

			@Override
			public void izvrsiTretman(ZakazanTretman tretman) {
				System.out.println("izvsen tretman");
				
			}

			@Override
			public SortedMap<LocalDate, SortedMap<LocalTime, ZakazanTretman>> rasporedKozmeticara() {
				return convertToSortedMap(createZakazaniList(30));
			}

			@Override
			public Kozmeticar getLoggedInKorisnik() {
				Kozmeticar test = new Kozmeticar();
				test.setKorisnickoIme("cetkovic");
				
				KozmetickiTretman kt1 = new KozmetickiTretman("naziv1", "opis1");
				test.addTretman(kt1);
				KozmetickiTretman kt2 = new KozmetickiTretman("naziv2", "opis2");
				test.addTretman(kt2);
				KozmetickiTretman kt3 = new KozmetickiTretman("naziv3", "opis3");
				test.addTretman(kt3);
				//KozmetickiTretman kt4 = new KozmetickiTretman("naziv4", "opis4");
				//test.addTretman(kt4);
				
				return test;
			}

			@Override
			public Map<StatusTretmana, Collection<ZakazanTretman>> zakazaniTretmaniKozmeticara() {
				ArrayList<ZakazanTretman> zakazaniList1 = createZakazaniList(20);
		        ArrayList<ZakazanTretman> zakazaniList2 = createZakazaniList(30);
		        ArrayList<ZakazanTretman> zakazaniList3 = createZakazaniList(32);
		        ArrayList<ZakazanTretman> zakazaniList4 = createZakazaniList(31);
		        ArrayList<ZakazanTretman> zakazaniList5 = createZakazaniList(31);
		        
		        
		        zakazaniList1.forEach(zt -> zt.setStatus(StatusTretmana.ZAKAZAN));
		        zakazaniList2.forEach(zt -> zt.setStatus(StatusTretmana.IZVRSEN));
		        zakazaniList3.forEach(zt -> zt.setStatus(StatusTretmana.OTKAZAO_KLIJENT));
		        zakazaniList4.forEach(zt -> zt.setStatus(StatusTretmana.OTKAZAO_SALON));
		        zakazaniList5.forEach(zt -> zt.setStatus(StatusTretmana.NIJE_SE_POJAVIO));
		        
		        Map<StatusTretmana, Collection<ZakazanTretman>> map = new HashMap<>();
		        map.put(StatusTretmana.ZAKAZAN, zakazaniList1);
		        map.put(StatusTretmana.IZVRSEN, zakazaniList2);
		        map.put(StatusTretmana.OTKAZAO_KLIJENT, zakazaniList3);
		        map.put(StatusTretmana.OTKAZAO_SALON, zakazaniList4);
		        map.put(StatusTretmana.NIJE_SE_POJAVIO, zakazaniList5);
		        
		        return map;
			}
			
		};
		new KozmeticarGUI(ks).setVisible(true);
	}
}
