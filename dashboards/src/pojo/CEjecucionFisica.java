package pojo;

public class CEjecucionFisica {
	
	private Integer parent;
	private Integer entidad;
	private String nombre;
	private String sigla;
	private Double porcentaje_ejecucion_presupuestaria;
	private Double porcentaje_ejecucion_fisica;
	private Double ejecutado_financiero;
	private Double vigente_financiero;
	
	public CEjecucionFisica(Integer parent, Integer entidad, String nombre, String sigla, Double porcentaje_ejecucion_presupuestaria,
			Double porcentaje_ejecucion_fisica, Double ejecutado_financiero, Double vigente_financiero) {
		super();
		this.parent = parent;
		this.entidad = entidad;
		this.nombre = nombre;
		this.sigla = sigla;
		this.porcentaje_ejecucion_presupuestaria = porcentaje_ejecucion_presupuestaria;
		this.porcentaje_ejecucion_fisica = porcentaje_ejecucion_fisica;
		this.ejecutado_financiero = ejecutado_financiero;
		this.vigente_financiero = vigente_financiero;
	}
	
	
	public Integer getParent() {
		return parent;
	}
	public void setParent(Integer parent) {
		this.parent = parent;
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
	public String getSigla() {
		return sigla;
	}
	public void setSigla(String sigla) {
		this.sigla = sigla;
	}
	public Double getPorcentaje_ejecucion_presupuestaria() {
		return porcentaje_ejecucion_presupuestaria;
	}
	public void setPorcentaje_ejecucion_presupuestaria(Double porcentaje_ejecucion_presupuestaria) {
		this.porcentaje_ejecucion_presupuestaria = porcentaje_ejecucion_presupuestaria;
	}
	public Double getPorcentaje_ejecucion_fisica() {
		return porcentaje_ejecucion_fisica;
	}
	public void setPorcentaje_ejecucion_fisica(Double porcentaje_ejecucion_fisica) {
		this.porcentaje_ejecucion_fisica = porcentaje_ejecucion_fisica;
	}


	public Double getEjecutado_financiero() {
		return ejecutado_financiero;
	}


	public void setEjecutado_financiero(Double ejecutado_financiero) {
		this.ejecutado_financiero = ejecutado_financiero;
	}


	public Double getVigente_financiero() {
		return vigente_financiero;
	}


	public void setVigente_financiero(Double vigente_financiero) {
		this.vigente_financiero = vigente_financiero;
	}
	
}
