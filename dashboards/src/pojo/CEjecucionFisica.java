package pojo;

public class CEjecucionFisica {
	
	private Integer parent;
	private Integer entidad;
	private String nombre;
	private Double porcentaje_ejecucion_presupuestaria;
	private Double porcentaje_ejecucion_fisica;
	
	public CEjecucionFisica(Integer parent, Integer entidad, String nombre, Double porcentaje_ejecucion_presupuestaria,
			Double porcentaje_ejecucion_fisica) {
		super();
		this.parent = parent;
		this.entidad = entidad;
		this.nombre = nombre;
		this.porcentaje_ejecucion_presupuestaria = porcentaje_ejecucion_presupuestaria;
		this.porcentaje_ejecucion_fisica = porcentaje_ejecucion_fisica;
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
	
}
