package gui.menadzer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.XChartPanel;

import com.github.lgooddatepicker.components.DatePicker;

import entiteti.StatusTretmana;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class IzvjestajZakazanihTretmanaPoStatusu extends JFrame {

    private BiFunction<LocalDate, LocalDate, Map<StatusTretmana, Integer>> izvjestajFunc;

    private PieChart chart;
    private XChartPanel<PieChart> chartPanel;
    private DatePicker startDatePicker;
    private DatePicker endDatePicker;


    public IzvjestajZakazanihTretmanaPoStatusu(BiFunction<LocalDate, LocalDate, Map<StatusTretmana, Integer>> izvjestajFunc) {
        this.izvjestajFunc = izvjestajFunc;
        

        setTitle("Izvjestaj o broju zakazanih tretmana po statusu.");
        setSize(680, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initialize();
    }

    private void initialize() {
        setLayout(new MigLayout("fill, wrap 2", "[grow][grow]", "[][grow][]")); // Define MigLayout constraints

        chart = new PieChartBuilder()
                .title("Izvjestaj zakazanih tretmana po statusu.")
                .height(400) // Adjusted height
                .width(600)
                .build();

        chartPanel = new XChartPanel<>(chart);

        // Create JXDatePicker components for selecting start and end dates
        startDatePicker = new DatePicker();
        endDatePicker = new DatePicker();

        

        // Create a button to close the frame
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the frame
            }
        });

        // Add components to the frame using MigLayout constraints
        add(new JLabel("Start Date:"));
        add(startDatePicker, "growx");
        add(new JLabel("End Date:"));
        add(endDatePicker, "growx");
        add(chartPanel, "span 2, grow");
        add(closeButton, "span 2, align center");

        update();
    }

    private void update(Map<StatusTretmana, Integer> izvjestaj) {
        chart.getSeriesMap().clear();

        Map<StatusTretmana, String> nazivi = new HashMap<>();
        nazivi.put(StatusTretmana.ZAKAZAN, "Zakazani");
        nazivi.put(StatusTretmana.IZVRSEN, "Izvrseni");
        nazivi.put(StatusTretmana.OTKAZAO_KLIJENT, "Otkazao klijent");
        nazivi.put(StatusTretmana.OTKAZAO_SALON, "Otkazao salon");
        nazivi.put(StatusTretmana.NIJE_SE_POJAVIO, "Nije se pojavio");

        izvjestaj.forEach((status, value) -> chart.addSeries(nazivi.get(status), value));
    }

    public void update() {
    	LocalDate startDate = startDatePicker.getDate();
    	LocalDate endDate = endDatePicker.getDate();
    	
        update(izvjestajFunc.apply((startDate == null ? LocalDate.MIN : startDate), (endDate == null ? LocalDate.MAX : endDate)));
    }
	
	
	
	public static void main(String[] args) {
		new IzvjestajZakazanihTretmanaPoStatusu((x, y) -> {
			Map<StatusTretmana, Integer> map = new HashMap<>();
			map.put(StatusTretmana.ZAKAZAN, 30);
			map.put(StatusTretmana.IZVRSEN, 12);
			map.put(StatusTretmana.OTKAZAO_KLIJENT, 3);
			map.put(StatusTretmana.OTKAZAO_SALON, 20);
			map.put(StatusTretmana.NIJE_SE_POJAVIO, 15);
			
			return map;
			
		}).setVisible(true);
	}
}
