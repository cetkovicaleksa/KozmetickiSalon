package gui.menadzer;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import javax.swing.JFrame;

import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.XChartPanel;

import entiteti.StatusTretmana;

@SuppressWarnings("serial")
public class IzvjestajZakazanihTretmanaPoStatusu extends JFrame{

	private BiFunction<LocalDate, LocalDate, Map<StatusTretmana, Integer>> izvjestajFunc;
	
	private PieChart chart;
	private LocalDate startDate;
	private LocalDate endDate;
	
	public IzvjestajZakazanihTretmanaPoStatusu(BiFunction<LocalDate, LocalDate, Map<StatusTretmana, Integer>> izvjestajFunc) {
		this(izvjestajFunc, LocalDate.now(), LocalDate.now().minusMonths(1));
	}
	
	public IzvjestajZakazanihTretmanaPoStatusu(BiFunction<LocalDate, LocalDate, Map<StatusTretmana, Integer>> izvjestajFunc, LocalDate startDate, LocalDate endDate) {
		this.izvjestajFunc = izvjestajFunc;
		this.startDate = startDate;
		this.endDate = endDate;
		
		super.setTitle("Izvjestaj o broju zakazanih tretmana po statusu.");
		super.setSize(680, 500);
		super.setLocationRelativeTo(null);
		super.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		initialize();
	}
	
	private void initialize() {
		chart = new PieChartBuilder()
				.title("Izvjestaj zakazanih tretmana po statusu.")
				.height(500)
				.width(600)
				.build();
				
				
		Map<StatusTretmana, Integer> izvjestaj = izvjestajFunc.apply(startDate, endDate);
		
		Map<StatusTretmana, String> nazivi = new HashMap<>();
		nazivi.put(StatusTretmana.ZAKAZAN, "Zakazani");
		nazivi.put(StatusTretmana.IZVRSEN, "Izvrseni");
		nazivi.put(StatusTretmana.OTKAZAO_KLIJENT, "Otkazao klijent");
		nazivi.put(StatusTretmana.OTKAZAO_SALON, "Otkazao salon");
		nazivi.put(StatusTretmana.NIJE_SE_POJAVIO, "Nije se pojavio");
		
		
		for(Map.Entry<StatusTretmana, Integer> entry : izvjestaj.entrySet()) {
			chart.addSeries(nazivi.get(entry.getKey()), entry.getValue());
		}
		
		add(new XChartPanel<PieChart>(chart));
	}
	
	
	
	public static void main(String[] args) {
		new IzvjestajZakazanihTretmanaPoStatusu((x, y) -> {
			Map<StatusTretmana, Integer> map = new HashMap<>();
			map.put(StatusTretmana.ZAKAZAN, 30);
			map.put(StatusTretmana.IZVRSEN, 12);
			map.put(StatusTretmana.OTKAZAO_KLIJENT, 3);
			map.put(StatusTretmana.OTKAZAO_SALON, 2);
			map.put(StatusTretmana.NIJE_SE_POJAVIO, 15);
			
			return map;
			
		}).setVisible(true);
	}
}
