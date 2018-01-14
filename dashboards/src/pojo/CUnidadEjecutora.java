package pojo;

public class CUnidadEjecutora {
	
	Integer ejercicio;
	Integer entidad;
	Integer unidad_ejecutora;
	String nombre;
	String nombre_control;
	
	public CUnidadEjecutora(Integer ejercicio, Integer entidad, Integer unidad_ejecutora, String nombre, String nombre_control) {
		super();
		this.ejercicio = ejercicio;
		this.entidad = entidad;
		this.unidad_ejecutora = unidad_ejecutora;
		this.nombre = nombre;
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
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNombre_control() {
		return nombre_control;
	}

	public void setNombre_control(String nombre_control) {
		this.nombre_control = nombre_control;
	}
	
	
}
