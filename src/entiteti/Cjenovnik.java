package entiteti;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Cjenovnik {  //TODO: haven't finished this, not used in the project
	
	private ArrayList<Entry> entries;	
	
	
	
	
	public void updateEntries(List<KozmetickiTretman.TipTretmana> tipoviTretmana) {
		ArrayList<Entry> newEntries = new ArrayList<>();
		
		tipoviTretmana.forEach(tt -> {
			
			
		});		
	}
	
	public void deletedTreatmentTypes(List<KozmetickiTretman.TipTretmana> tipoviTretmana) {
		
	}
	
	

	protected ArrayList<Entry> getEntries() {
		return entries;
	}

	protected void setEntries(ArrayList<Entry> inactivePrices) {
		this.entries = inactivePrices;
	}

	public List<Entry> getPrices(){
		return null;
	}
	
	
	public static class Entry{
		private KozmetickiTretman kozmetickiTretman;
		private List<KozmetickiTretman.TipTretmana> tipoviTretmana;
		private Status status;
		
		
		public Entry() {
			setStatus(Status.INACTIVE);
		}
		
		public Entry(List<KozmetickiTretman.TipTretmana> tipoviTretmana) {
			this(tipoviTretmana.get(0).getTretman(), tipoviTretmana);
		}
		
		public Entry(KozmetickiTretman kozmetickiTretman, List<KozmetickiTretman.TipTretmana> tipoviTretmana) {
			if(!validateTipoviTretmana(kozmetickiTretman, tipoviTretmana)) {
				//TODO: throw exception
			}
			
			setKozmetickiTretman(kozmetickiTretman);
			setTipoviTretmana(tipoviTretmana);
			setStatus(Status.ACTIVE);
		}
		
		private boolean validateTipoviTretmana(KozmetickiTretman kozmetickiTretman, List<KozmetickiTretman.TipTretmana> tipoviTretmana) {
			for(KozmetickiTretman.TipTretmana tipTretmana : tipoviTretmana) {
				if( !kozmetickiTretman.equals(tipTretmana.getTretman()) ) {
					return false;
				}
			}
			
			return true;
		}
		
		
		
		public KozmetickiTretman getKozmetickiTretman() {
			return kozmetickiTretman;
		}

		public void setKozmetickiTretman(KozmetickiTretman kozmetickiTretman) {
			this.kozmetickiTretman = kozmetickiTretman;
		}

		public List<KozmetickiTretman.TipTretmana> getTipoviTretmana() {
			return tipoviTretmana;
		}

		public void setTipoviTretmana(List<KozmetickiTretman.TipTretmana> tipoviTretmana) {
			this.tipoviTretmana = tipoviTretmana;
		}

		public Status getStatus() {
			return status;
		}

		public void setStatus(Status status) {
			this.status = status;
		}

		@Override
		public String toString() {
			String base =getKozmetickiTretman().getNaziv() + ":\n";
			
			List<KozmetickiTretman.TipTretmana> tipoviTretmana = getTipoviTretmana();
			if(tipoviTretmana == null || tipoviTretmana.isEmpty()) {
				return base;
			}
			
			StringBuilder sb = new StringBuilder();
			tipoviTretmana.forEach(tt -> {
				sb.append("---").append(tt.getNaziv()).append("   ").append(tt.getCijena()).append("\n");
			});			
			
			return base + sb.toString();
		}
		
		@Override
		public int hashCode() {
			return getKozmetickiTretman().hashCode();
		}


		@Override
		public boolean equals(Object obj) {
			if(obj == null || ! (obj instanceof Entry)) {
				return false;
			}
			
			return this.getKozmetickiTretman().equals(((Entry) obj).getKozmetickiTretman());
		}

		
		
		
	}

}
