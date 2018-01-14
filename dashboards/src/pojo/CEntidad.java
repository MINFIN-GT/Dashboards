package pojo;

public class CEntidad {
	Integer ejercicio;
	Integer entidad;
	String nombre;
	String abreviatura;
	String nombre_control;
	
	public CEntidad(Integer ejercicio, Integer entidad, String nombre, String abreviatura, String nombre_control) {
		super();
		this.ejercicio = ejercicio;
		this.entidad = entidad;
		this.nombre = nombre;
		this.abreviatura = abreviatura;
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
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getAbreviatura() {
		return abreviatura;
	}
	public void setAbreviatura(String abreviatura) {
		this.abreviatura = abreviatura;
	}

	public String getNombre_control() {
		return nombre_control;
	}

	public void setNombre_control(String nombre_control) {
		this.nombre_control = nombre_control;
	}
}
