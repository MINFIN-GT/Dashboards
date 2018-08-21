package pojo;

public class CTesoreriaCuenta {
	private int ejercicio;
	private String cuenta;
	private Double saldo_inicial;
	
	public CTesoreriaCuenta(int ejercicio,String cuenta, Double saldo_inicial) {
		this.ejercicio = ejercicio;
		this.cuenta = cuenta;
		this.saldo_inicial = saldo_inicial;
	}
	
	public int getEjercicio() {
		return ejercicio;
	}

	public void setEjercicio(int ejercicio) {
		this.ejercicio = ejercicio;
	}

	public String getCuenta() {
		return cuenta;
	}

	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}

	public Double getSaldo_inicial() {
		return saldo_inicial;
	}

	public void setSaldo_inicial(Double saldo_inicial) {
		this.saldo_inicial = saldo_inicial;
	}
	
}
