package pojo;

public class CRecursoAuxiliar {
	Integer ejercicio;
	Integer entidad;
	Integer unidad_ejecutora;
	Integer recurso;
	Integer recurso_auxiliar;
	String nombre;
	String sigla;
	Integer recaudacion;
	String nombre_control;
	
	public CRecursoAuxiliar(Integer ejercicio, Integer entidad, Integer unidad_ejecutora, Integer recurso,
			Integer recurso_auxiliar, String nombre, String sigla, Integer recaudacion, String nombre_control) {
		super();
		this.ejercicio = ejercicio;
		this.entidad = entidad;
		this.unidad_ejecutora = unidad_ejecutora;
		this.recurso = recurso;
		this.recurso_auxiliar = recurso_auxiliar;
		this.nombre = nombre;
		this.sigla = sigla;
		this.recaudacion = recaudacion;
		this.nombre_control = nombre_control;
	}

	public Integer getEjercicio() {
		return ejercicio;
	}

	public void setEjercicio(Integer ejercicio) {
		this.ejercicio = ejercicio;
	}

	public Integer getEntidad() {
		return entidad;
	}

	public void setEntidad(Integer entidad) {
		this.entidad = entidad;
	}

	public Integer getUnidad_ejecutora() {
		return unidad_ejecutora;
	}

	public void setUnidad_ejecutora(Integer unidad_ejecutora) {
		this.unidad_ejecutora = unidad_ejecutora;
	}

	public Integer getRecurso() {
		return recurso;
	}

	public void setRecurso(Integer recurso) {
		this.recurso = recurso;
	}

	public Integer getRecurso_auxiliar() {
		return recurso_auxiliar;
	}

	public void setRecurso_auxiliar(Integer recurso_auxiliar) {
		this.recurso_auxiliar = recurso_auxiliar;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public Integer getRecaudacion() {
		return recaudacion;
	}

	public void setRecaudacion(Integer recaudacion) {
		this.recaudacion = recaudacion;
	}

	public String getNombre_control() {
		return nombre_control;
	}

	public void setNombre_control(String nombre_control) {
		this.nombre_control = nombre_control;
	}
	
	
}
