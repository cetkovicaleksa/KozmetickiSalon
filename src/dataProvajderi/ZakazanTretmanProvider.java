package dataProvajderi;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.function.Function;

import entiteti.Klijent;
import entiteti.Korisnik;
import entiteti.Kozmeticar;
import entiteti.KozmetickiTretman;
import entiteti.KozmetickiTretman.TipTretmana;
import entiteti.StatusTretmana;
import entiteti.ZakazanTretman;
import helpers.DefaultDict;

public class ZakazanTretmanProvider extends XDataProvider<ZakazanTretman, String>{
	
	public static final ZakazanTretman DELETED = new ZakazanTretman() {
		{
			super.setTipTretmana(TipTretmanaProvider.DELETED);
			super.setKozmeticar(KozmeticarProvider.DELETED);
			super.setKlijent(KlijentProvider.DELETED);
		}
		
		@Override
		public void setTipTretmana(TipTretmana tipTretmana) {}
		@Override
		public void setKozmeticar(Kozmeticar kozmeticar) {}
		@Override
		public void setKlijent(Klijent klijent) {}
		@Override
		public void setCijena(float cijena) {}
		@Override
		public void setTrajanje(int trajanje) {}
		@Override
		public void setDatum(LocalDate datum) {}
		@Override
		public void setVrijeme(LocalTime vrijeme) {}
		@Override
		public void setStatus(StatusTretmana status) {}
		
	};
	

	@Override
	public ZakazanTretman getDeletedInstance() { return DELETED; }
	
	
	public void loadData(
			DefaultDict<String, Korisnik> korisniciIds, DefaultDict<String, Kozmeticar> kozmeticariIds,
			DefaultDict<String, KozmetickiTretman.TipTretmana> tipoviTretmanaIds) throws IOException
	{
		
		
	
	}
	
	public void saveData(
			Function<Korisnik, String> getKorisnikId, Function<Kozmeticar, String> getKozmeticarId,
			Function<KozmetickiTretman.TipTretmana, String> getTipTretmanaId) throws IOException 
	{
		
		
		
	}

}
