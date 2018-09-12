package pojo;

import java.util.ArrayList;

public class CDeudaMensualizada {
	int entidad;
	String entidad_nombre;
	int actividad;
	String actividad_nombre;
	int fuente;
	String fuente_nombre;
	int renglon;
	String renglon_nombre;
	Double asignado;
	Double[] vigente_meses;
	Double[] ejecucion_meses;
	ArrayList<CDeudaMensualizada> children;
	int nivel;
	
	public CDeudaMensualizada(int entidad, String entidad_nombre, int actividad, String actividad_nombre, int fuente,
			String fuente_nombre, int renglon, String renglon_nombre, Double asignado, Double[] vigente_meses,
			Double[] ejecucion_meses, ArrayList<CDeudaMensualizada> children, int nivel) {
		super();
		this.entidad = entidad;
		this.entidad_nombre = entidad_nombre;
		this.actividad = actividad;
		this.actividad_nombre = actividad_nombre;
		this.fuente = fuente;
		this.fuente_nombre = fuente_nombre;
		this.renglon = renglon;
		this.renglon_nombre = renglon_nombre;
		this.asignado = asignado;
		this.vigente_meses = vigente_meses;
		this.ejecucion_meses = ejecucion_meses;
		this.children = children;
		this.nivel = nivel;
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

	public int getActividad() {
		return actividad;
	}

	public void setActividad(int actividad) {
		this.actividad = actividad;
	}

	public String getActividad_nombre() {
		return actividad_nombre;
	}

	public void setActividad_nombre(String actividad_nombre) {
		this.actividad_nombre = actividad_nombre;
	}

	public int getFuente() {
		return fuente;
	}

	public void setFuente(int fuente) {
		this.fuente = fuente;
	}

	public String getFuente_nombre() {
		return fuente_nombre;
	}

	public void setFuente_nombre(String fuente_nombre) {
		this.fuente_nombre = fuente_nombre;
	}

	public int getRenglon() {
		return renglon;
	}

	public void setRenglon(int renglon) {
		this.renglon = renglon;
	}

	public String getRenglon_nombre() {
		return renglon_nombre;
	}

	public void setRenglon_nombre(String renglon_nombre) {
		this.renglon_nombre = renglon_nombre;
	}

	public Double getAsignado() {
		return asignado;
	}

	public void setAsignado(Double asignado) {
		this.asignado = asignado;
	}

	public Double[] getVigente_meses() {
		return vigente_meses;
	}

	public void setVigente_meses(Double[] vigente_meses) {
		this.vigente_meses = vigente_meses;
	}

	public Double[] getEjecucion_meses() {
		return ejecucion_meses;
	}

	public void setEjecucion_meses(Double[] ejecucion_meses) {
		this.ejecucion_meses = ejecucion_meses;
	}

	public ArrayList<CDeudaMensualizada> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<CDeudaMensualizada> children) {
		this.children = children;
	}

	public int getNivel() {
		return nivel;
	}

	public void setNivel(int nivel) {
		this.nivel = nivel;
	}
	
	
	
}
