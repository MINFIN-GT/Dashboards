package pojo.formulacion;

public class CInstitucionalTotal {
	int ejercicio;
	int entidad;
	String entidad_nombre;
	Double ejecutado_dos_antes;
	Double aprobado_anterior;
	Double aprobado_anterior_mas_ampliaciones;
	Double recomendado;
	
	public CInstitucionalTotal(int ejercicio, int entidad, String entidad_nombre, Double ejecutado_dos_antes,
			Double aprobado_anterior,Double aprobado_anterior_mas_ampliaciones, Double recomendado) {
		super();
		this.ejercicio = ejercicio;
		this.entidad = entidad;
		this.entidad_nombre = entidad_nombre;
		this.ejecutado_dos_antes = ejecutado_dos_antes;
		this.aprobado_anterior = aprobado_anterior;
		this.aprobado_anterior_mas_ampliaciones = aprobado_anterior_mas_ampliaciones;
		this.recomendado = recomendado;
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

	public String getEntidad_nombre() {
		return entidad_nombre;
	}

	public void setEntidad_nombre(String entidad_nombre) {
		this.entidad_nombre = entidad_nombre;
	}

	public Double getEjecutado_dos_antes() {
		return ejecutado_dos_antes;
	}

	public void setEjecutado_dos_antes(Double ejecutado_dos_antes) {
		this.ejecutado_dos_antes = ejecutado_dos_antes;
	}

	public Double getAproobado_anterior() {
		return aprobado_anterior;
	}

	public void setAprboado_anterior(Double aprboado_anterior) {
		this.aprobado_anterior = aprboado_anterior;
	}
	
	public Double getAproobado_anterior_mas_ampliaciones() {
		return aprobado_anterior_mas_ampliaciones;
	}

	public void setAprboado_anterior_mas_ampliaciones(Double aprboado_anterior_mas_ampliaciones) {
		this.aprobado_anterior_mas_ampliaciones = aprboado_anterior_mas_ampliaciones;
	}

	public Double getRecomendado() {
		return recomendado;
	}

	public void setRecomendado(Double recomendado) {
		this.recomendado = recomendado;
	}
	
	
}