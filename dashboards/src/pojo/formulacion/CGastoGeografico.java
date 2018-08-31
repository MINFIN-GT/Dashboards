package pojo.formulacion;

public class CGastoGeografico {
	int ejercicio;
	int departamento;
	String nombre_departamento;
	int geografico;
	String nombre_geografico;
	Double recomendado;
	
	
	public CGastoGeografico(int ejercicio, int departamento, String nombre_departamento, int geografico,
			String nombre_geografico, Double recomendado) {
		super();
		this.ejercicio = ejercicio;
		this.departamento = departamento;
		this.nombre_departamento = nombre_departamento;
		this.geografico = geografico;
		this.nombre_geografico = nombre_geografico;
		this.recomendado = recomendado;
	}


	public int getEjercicio() {
		return ejercicio;
	}


	public void setEjercicio(int ejercicio) {
		this.ejercicio = ejercicio;
	}


	public int getDepartamento() {
		return departamento;
	}


	public void setDepartamento(int departamento) {
		this.departamento = departamento;
	}


	public String getNombre_departamento() {
		return nombre_departamento;
	}


	public void setNombre_departamento(String nombre_departamento) {
		this.nombre_departamento = nombre_departamento;
	}


	public int getGeografico() {
		return geografico;
	}


	public void setGeografico(int geografico) {
		this.geografico = geografico;
	}


	public String getNombre_geografico() {
		return nombre_geografico;
	}


	public void setNombre_geografico(String nombre_geografico) {
		this.nombre_geografico = nombre_geografico;
	}


	public Double getRecomendado() {
		return recomendado;
	}


	public void setRecomendado(Double recomendado) {
		this.recomendado = recomendado;
	}
	
	
}
