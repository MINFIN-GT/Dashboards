package pojo;

public class CRecurso {
	Integer ejercicio;
	Integer recurso;
	String nombre;
	Integer grupo_ingreso;
	String hoja;
	Integer clase;
	Integer seccion;
	Integer grupo;
	String nombre_control;
	
	
	public CRecurso(Integer ejercicio, Integer recurso, String nombre, Integer grupo_ingreso, String hoja,
			Integer clase, Integer seccion, Integer grupo, String nombre_control) {
		super();
		this.ejercicio = ejercicio;
		this.recurso = recurso;
		this.nombre = nombre;
		this.grupo_ingreso = grupo_ingreso;
		this.hoja = hoja;
		this.clase = clase;
		this.seccion = seccion;
		this.grupo = grupo;
		this.nombre_control = nombre_control;
	}


	public Integer getEjercicio() {
		return ejercicio;
	}


	public void setEjercicio(Integer ejercicio) {
		this.ejercicio = ejercicio;
	}


	public Integer getRecurso() {
		return recurso;
	}


	public void setRecurso(Integer recurso) {
		this.recurso = recurso;
	}


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public Integer getGrupo_ingreso() {
		return grupo_ingreso;
	}


	public void setGrupo_ingreso(Integer grupo_ingreso) {
		this.grupo_ingreso = grupo_ingreso;
	}


	public String getHoja() {
		return hoja;
	}


	public void setHoja(String hoja) {
		this.hoja = hoja;
	}


	public Integer getClase() {
		return clase;
	}


	public void setClase(Integer clase) {
		this.clase = clase;
	}


	public Integer getSeccion() {
		return seccion;
	}


	public void setSeccion(Integer seccion) {
		this.seccion = seccion;
	}


	public Integer getGrupo() {
		return grupo;
	}


	public void setGrupo(Integer grupo) {
		this.grupo = grupo;
	}
	
	public String getNombreControl() {
		return nombre_control;
	}


	public void setNombreControl(String nombre_control) {
		this.nombre_control = nombre_control;
	}
	
}
