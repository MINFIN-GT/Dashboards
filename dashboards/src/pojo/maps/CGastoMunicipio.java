package pojo.maps;

public class CGastoMunicipio {
	String nombre;
	Integer codigo;

	Double ejecutado;
	Double vigente;
	Double asignado;

	public CGastoMunicipio(String nombre, Integer codigo, Double ejecutado, Double vigente, Double asignado) {
		this.nombre = nombre;
		this.codigo = codigo;
		this.ejecutado = ejecutado;
		this.vigente = vigente;
		this.asignado = asignado;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public Double getEjecutado() {
		return ejecutado;
	}

	public void setEjecutado(Double ejecutado) {
		this.ejecutado = ejecutado;
	}

	public Double getVigente() {
		return vigente;
	}

	public void setVigente(Double vigente) {
		this.vigente = vigente;
	}

	public Double getAsignado() {
		return asignado;
	}

	public void setAsignado(Double asignado) {
		this.asignado = asignado;
	}

}
