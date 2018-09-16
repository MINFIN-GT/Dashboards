package pojo.formulacion;

public class CInstitucionalTotalDetalle {
	int codigo;
	String nombre;
	Double ejecutado_dos_antes;
	Double aprobado_anterior;
	Double aprobado_anterior_mas_ampliaciones;
	Double recomendado;
	int nivel;
	
	public CInstitucionalTotalDetalle(int codigo, String nombre, Double ejecutado_dos_antes, Double aprobado_anterior,
			Double aprobado_anterior_mas_ampliaciones, Double recomendado, int nivel) {
		super();
		this.codigo = codigo;
		this.nombre = nombre;
		this.ejecutado_dos_antes = ejecutado_dos_antes;
		this.aprobado_anterior = aprobado_anterior;
		this.aprobado_anterior_mas_ampliaciones = aprobado_anterior_mas_ampliaciones;
		this.recomendado = recomendado;
		this.nivel = nivel;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Double getEjecutado_dos_antes() {
		return ejecutado_dos_antes;
	}

	public void setEjecutado_dos_antes(Double ejecutado_dos_antes) {
		this.ejecutado_dos_antes = ejecutado_dos_antes;
	}

	public Double getAprobado_anterior() {
		return aprobado_anterior;
	}

	public void setAprobado_anterior(Double aprobado_anterior) {
		this.aprobado_anterior = aprobado_anterior;
	}

	public Double getAprobado_anterior_mas_ampliaciones() {
		return aprobado_anterior_mas_ampliaciones;
	}

	public void setAprobado_anterior_mas_ampliaciones(Double aprobado_anterior_mas_ampliaciones) {
		this.aprobado_anterior_mas_ampliaciones = aprobado_anterior_mas_ampliaciones;
	}

	public Double getRecomendado() {
		return recomendado;
	}

	public void setRecomendado(Double recomendado) {
		this.recomendado = recomendado;
	}

	public int getNivel() {
		return nivel;
	}

	public void setNivel(int nivel) {
		this.nivel = nivel;
	}
	
	
}
