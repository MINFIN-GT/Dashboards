package pojo.transparencia;

public class CCur {
	int programa;
	int subprograma;
	int ejercicio;
	int entidad;
	int unidad_ejecutora;
	long cur;
	
	public CCur(int programa, int subprograma,int ejercicio, int entidad, int unidad_ejecutora, long cur) {
		super();
		this.programa = programa;
		this.subprograma = subprograma;
		this.ejercicio = ejercicio;
		this.entidad = entidad;
		this.unidad_ejecutora = unidad_ejecutora;
		this.cur = cur;
	}

	public int getPrograma() {
		return programa;
	}

	public void setPrograma(int programa) {
		this.programa = programa;
	}

	public int getSubprograma() {
		return subprograma;
	}

	public void setSubprograma(int subprograma) {
		this.subprograma = subprograma;
	}

	public int getEjercicio() {
		return ejercicio;
	}

	public void setEjercicio(int ejercicio) {
		this.ejercicio = ejercicio;
	}

	public int getEntidad() {
		return entidad;
	}

	public void setEntidad(int entidad) {
		this.entidad = entidad;
	}

	public int getUnidad_ejecutora() {
		return unidad_ejecutora;
	}

	public void setUnidad_ejecutora(int unidad_ejecutora) {
		this.unidad_ejecutora = unidad_ejecutora;
	}

	public long getCur() {
		return cur;
	}

	public void setCur(long cur) {
		this.cur = cur;
	}
}
