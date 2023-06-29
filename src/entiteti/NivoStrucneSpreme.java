package entiteti;

public enum NivoStrucneSpreme {
	
	SKOLE_BEZ(3),
	SREDNJA_SKOLA(10),
	OSNOVNE_STUDIJE(20),
	MASTER_STUDIJE(30),
	DOKTORSKE_STUDIJE(40);
	
	private int value;
	
	private NivoStrucneSpreme(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return this.value;
	}

}
