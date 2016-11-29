package pojo.maps;

public class CGastoGeografico {
	private Integer mes;
	private Integer ejercicio;

	private Integer geografico;
	private String nombreGeografico;
	
	private Double gasto;
	private Double poblacion; 

	public CGastoGeografico(Integer mes, Integer ejercicio, Integer geografico, String nombreGeografico, Double gasto, Double poblacion) {
		super();
		this.mes = mes;
		this.ejercicio = ejercicio;
		this.geografico = geografico;
		this.nombreGeografico = nombreGeografico;
		this.gasto = gasto;
		this.poblacion = poblacion;
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

	public Double getPoblacion() {
		return poblacion;
	}

	public void setPoblacion(Double poblacion) {
		this.poblacion = poblacion;
	}
	
	
}
