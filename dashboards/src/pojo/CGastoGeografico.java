package pojo;

public class CGastoGeografico {
	private Integer mes;
	private Integer ejercicio;

	private Integer geografico;
	private String nombreGeografico;
	
	private Double gasto;

	public CGastoGeografico(Integer mes, Integer ejercicio, Integer geografico, String nombreGeografico, Double gasto) {
		super();
		this.mes = mes;
		this.ejercicio = ejercicio;
		this.geografico = geografico;
		this.nombreGeografico = nombreGeografico;
		this.gasto = gasto;
	}

	public Integer getMes() {
		return mes;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}

	public Integer getEjercicio() {
		return ejercicio;
	}

	public void setEjercicio(Integer ejercicio) {
		this.ejercicio = ejercicio;
	}

	public Integer getGeografico() {
		return geografico;
	}

	public void setGeografico(Integer geografico) {
		this.geografico = geografico;
	}

	public String getNombreGeografico() {
		return nombreGeografico;
	}

	public void setNombreGeografico(String nombreGeografico) {
		this.nombreGeografico = nombreGeografico;
	}

	public Double getGasto() {
		return gasto;
	}

	public void setGasto(Double gasto) {
		this.gasto = gasto;
	}
	
	
}
