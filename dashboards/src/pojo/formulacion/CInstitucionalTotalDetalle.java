package pojo.formulacion;

import java.util.ArrayList;

public class CInstitucionalTotalDetalle {
	int ejercicio;
	int entidad;
	String entidad_nombre;
	Integer unidad_ejecutora;
	String unidad_ejecutora_nombre;
	Integer programa;
	String programa_nombre;
	Integer renglon;
	String renglon_nombre;
	Double ejecutado_dos_antes;
	Double aprobado_anterior;
	Double aprobado_anterior_mas_ampliaciones;
	Double recomendado;
	ArrayList<CInstitucionalTotalDetalle> children;
	Integer nivel;
	
	public CInstitucionalTotalDetalle(int ejercicio, int entidad, String entidad_nombre, Integer unidad_ejecutora, String unidad_ejecutora_nombre,
			Integer programa, String programa_nombre, Integer renglon, String renglon_nombre, Double ejecutado_dos_antes,
			Double aprobado_anterior,Double aprobado_anterior_mas_ampliaciones, Double recomendado, Integer nivel, 
			ArrayList<CInstitucionalTotalDetalle> children) {
		super();
		this.ejercicio = ejercicio;
		this.entidad = entidad;
		this.entidad_nombre = entidad_nombre;
		this.unidad_ejecutora = unidad_ejecutora;
		this.unidad_ejecutora_nombre = unidad_ejecutora_nombre;
		this.programa = programa;
		this.programa_nombre = programa_nombre;
		this.renglon = renglon;
		this.renglon_nombre = renglon_nombre;
		this.ejecutado_dos_antes = ejecutado_dos_antes;
		this.aprobado_anterior = aprobado_anterior;
		this.aprobado_anterior_mas_ampliaciones = aprobado_anterior_mas_ampliaciones;
		this.recomendado = recomendado;
		this.nivel = nivel;
		this.children = children;
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
	
	public Integer getUnidad_ejecutora() {
		return unidad_ejecutora;
	}

	public void setUnidad_ejecutora(Integer unidad_ejecutora) {
		this.unidad_ejecutora = unidad_ejecutora;
	}

	public String getUnidad_ejecutora_nombre() {
		return unidad_ejecutora_nombre;
	}

	public void setUnidad_ejecutora_nombre(String unidad_ejecutora_nombre) {
		this.unidad_ejecutora_nombre = unidad_ejecutora_nombre;
	}
	
	public Integer getPrograma() {
		return programa;
	}

	public void setPrograma(Integer programa) {
		this.programa = programa;
	}

	public String getPrograma_nombre() {
		return unidad_ejecutora_nombre;
	}

	public void setPrograma_nombre(String programa_nombre) {
		this.programa_nombre = programa_nombre;
	}

	public Integer getRenglon() {
		return programa;
	}

	public void setRenglon(Integer renglon) {
		this.renglon = renglon;
	}

	public String getRenglon_nombre() {
		return renglon_nombre;
	}

	public void setRenglon_nombre(String renglon_nombre) {
		this.renglon_nombre = renglon_nombre;
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
	
	public ArrayList<CInstitucionalTotalDetalle> getChildren() {
		return children;
	}
	
	public void setChildren(ArrayList<CInstitucionalTotalDetalle> children) {
		this.children = children;
	}
	
	public Integer getNivel() {
		return nivel;
	}
	
	public void setNivel(Integer nivel) {
		this.nivel = nivel;
	}
}
