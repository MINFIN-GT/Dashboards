package pojo.transparencia;

public class CEstadoCalamidad {
	int ejercicio;
	int programa;
	int suprograma;
	String nombre;
	String fecha_declaracion;
	String decreto;
	String link;
	
	public int getEjercicio() {
		return ejercicio;
	}

	public void setEjercicio(int ejercicio) {
		this.ejercicio = ejercicio;
	}

	public int getPrograma() {
		return programa;
	}

	public void setPrograma(int programa) {
		this.programa = programa;
	}

	public int getSuprograma() {
		return suprograma;
	}

	public void setSuprograma(int suprograma) {
		this.suprograma = suprograma;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getFecha_declaracion() {
		return fecha_declaracion;
	}

	public void setFecha_declaracion(String fecha_declaracion) {
		this.fecha_declaracion = fecha_declaracion;
	}

	public String getDecreto() {
		return decreto;
	}

	public void setDecreto(String decreto) {
		this.decreto = decreto;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public CEstadoCalamidad(int ejercicio, int programa, int suprograma, String nombre, String fecha_declaracion,
			String decreto, String link) {
		super();
		this.ejercicio = ejercicio;
		this.programa = programa;
		this.suprograma = suprograma;
		this.nombre = nombre;
		this.fecha_declaracion = fecha_declaracion;
		this.decreto = decreto;
		this.link = link;
	}
}
