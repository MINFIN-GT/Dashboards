package pojo.varios;

public class CPrestamo {
	String codigo;
	String nombre;
	double asignado;
	double modificaciones;
 	double ejecutado;
	double vigente;
	double ejecucion_financiera;
	
	public CPrestamo(String codigo, String nombre, double asignado, double modificaciones, double ejecutado, double vigente, double ejecucion_financiera) {
		super(); 
		this.codigo = codigo;
		this.nombre = nombre;
		this.asignado = asignado;
		this.modificaciones = modificaciones;
		this.ejecutado = ejecutado;
		this.vigente = vigente;
		this.ejecucion_financiera = ejecucion_financiera;

	}
	public double getModificaciones() {
		return modificaciones;
	}
	public void setModificaciones(double modificaciones) {
		this.modificaciones = modificaciones;
	}
	public double getAsignado() {
		return asignado;
	}
	public void setAsignado(double asignado) {
		this.asignado = asignado;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public double getEjecutado() {
		return ejecutado;
	}
	public void setEjecutado(double ejecutado) {
		this.ejecutado = ejecutado;
	}
	public double getVigente() {
		return vigente;
	}
	public void setVigente(double vigente) {
		this.vigente = vigente;
	}
	public double getEjecucion_financiera() {
		return ejecucion_financiera;
	}
	public void setEjecucion_financiera(double ejecucion_financiera) {
		this.ejecucion_financiera = ejecucion_financiera;
	}
	
}
