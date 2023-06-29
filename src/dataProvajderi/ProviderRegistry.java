package dataProvajderi;

public class ProviderRegistry {
	
	private KlijentProvider klijentProvider;
	private RecepcionerProvider recepcionerProvider;
	private KozmeticarProvider kozmeticarProvider;
	private MenadzerProvider menadzerProvider;
	
	private KozmetickiTretmanProvider kozmetickiTretmanProvider;
	private TipTretmanaProvider tipTretmanaProvider;
	
	
	public ProviderRegistry() {}
	
	public ProviderRegistry(KlijentProvider klijentProvider, RecepcionerProvider recepcionerProvider,
							KozmeticarProvider kozmeticarProvider, MenadzerProvider menadzerProvider,
							KozmetickiTretmanProvider kozmetickiTretmanProvider, TipTretmanaProvider tipTretmanaProvider
							) {		
		setKlijentProvider(klijentProvider);
		setRecepcionerProvider(recepcionerProvider);
		setKozmeticarProvider(kozmeticarProvider);
		setMenadzerProvider(menadzerProvider);
		setKozmetickiTretmanProvider(kozmetickiTretmanProvider);
		setTipTretmanaProvider(tipTretmanaProvider);
	}
	
	
	//get, put, post, delete koji vode racuna da npr kada se izbrise korisnik da se izbace reference na njega iz ostalih entiteta

	//i koji preusmjeravaju na odgovarajuci provider
	
	
	
	
	
	
	
	
	
	
	
	
	public KlijentProvider getKlijentProvider() {
		return klijentProvider;
	}

	public void setKlijentProvider(KlijentProvider klijentProvider) {
		this.klijentProvider = klijentProvider;
	}

	public RecepcionerProvider getRecepcionerProvider() {
		return recepcionerProvider;
	}

	public void setRecepcionerProvider(RecepcionerProvider recepcionerProvider) {
		this.recepcionerProvider = recepcionerProvider;
	}

	public KozmeticarProvider getKozmeticarProvider() {
		return kozmeticarProvider;
	}

	public void setKozmeticarProvider(KozmeticarProvider kozmeticarProvider) {
		this.kozmeticarProvider = kozmeticarProvider;
	}

	public MenadzerProvider getMenadzerProvider() {
		return menadzerProvider;
	}

	public void setMenadzerProvider(MenadzerProvider menadzerProvider) {
		this.menadzerProvider = menadzerProvider;
	}

	public KozmetickiTretmanProvider getKozmetickiTretmanProvider() {
		return kozmetickiTretmanProvider;
	}

	public void setKozmetickiTretmanProvider(KozmetickiTretmanProvider kozmetickiTretmanProvider) {
		this.kozmetickiTretmanProvider = kozmetickiTretmanProvider;
	}

	public TipTretmanaProvider getTipTretmanaProvider() {
		return tipTretmanaProvider;
	}

	public void setTipTretmanaProvider(TipTretmanaProvider tipTretmanaProvider) {
		this.tipTretmanaProvider = tipTretmanaProvider;
	}
	
	
	
	
	
	

	
	
	

}
