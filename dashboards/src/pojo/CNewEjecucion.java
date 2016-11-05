package pojo;

public class CNewEjecucion {
	private Integer ejercicio;
	private Integer mes;
	
	private Integer codigo;
	private String nombre;
		
	private Double ano_1;
	private Double ano_2;
	private Double ano_3;
	private Double ano_4;
	private Double ano_5;
	
	private Double anticipo_couta;
	private Double anticipo_couta_acumulado;
	private Double aprobado_cuota;
	private Double aprobado_cuota_acumulado;
	
	private Double cuota_total;
	private Double cuota_total_acumulado;
	
	private Double ejecutado;
	private Double ejecutado_acumulado;
	
	private Double vigente;

	public CNewEjecucion(Integer ejercicio, Integer mes, Integer codigo, String nombre, Double ano_1, Double ano_2,
			Double ano_3, Double ano_4, Double ano_5, Double anticipo_couta, Double anticipo_couta_acumulado,
			Double aprobado_cuota, Double aprobado_cuota_acumulado, Double cuota_total, Double cuota_total_acumulado,
			Double ejecutado, Double ejecutado_acumulado, Double vigente) {
		super();
		this.ejercicio = ejercicio;
		this.mes = mes;
		this.codigo = codigo;
		this.nombre = nombre;
		this.ano_1 = ano_1;
		this.ano_2 = ano_2;
		this.ano_3 = ano_3;
		this.ano_4 = ano_4;
		this.ano_5 = ano_5;
		this.anticipo_couta = anticipo_couta;
		this.anticipo_couta_acumulado = anticipo_couta_acumulado;
		this.aprobado_cuota = aprobado_cuota;
		this.aprobado_cuota_acumulado = aprobado_cuota_acumulado;
		this.cuota_total = cuota_total;
		this.cuota_total_acumulado = cuota_total_acumulado;
		this.ejecutado = ejecutado;
		this.ejecutado_acumulado = ejecutado_acumulado;
		this.vigente = vigente;
	}

	public Integer getEjercicio() {
		return ejercicio;
	}

	public void setEjercicio(Integer ejercicio) {
		this.ejercicio = ejercicio;
	}

	public Integer getMes() {
		return mes;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Double getAno_1() {
		return ano_1;
	}

	public void setAno_1(Double ano_1) {
		this.ano_1 = ano_1;
	}

	public Double getAno_2() {
		return ano_2;
	}

	public void setAno_2(Double ano_2) {
		this.ano_2 = ano_2;
	}

	public Double getAno_3() {
		return ano_3;
	}

	public void setAno_3(Double ano_3) {
		this.ano_3 = ano_3;
	}

	public Double getAno_4() {
		return ano_4;
	}

	public void setAno_4(Double ano_4) {
		this.ano_4 = ano_4;
	}

	public Double getAno_5() {
		return ano_5;
	}

	public void setAno_5(Double ano_5) {
		this.ano_5 = ano_5;
	}

	public Double getAnticipo_couta() {
		return anticipo_couta;
	}

	public void setAnticipo_couta(Double anticipo_couta) {
		this.anticipo_couta = anticipo_couta;
	}

	public Double getAnticipo_couta_acumulado() {
		return anticipo_couta_acumulado;
	}

	public void setAnticipo_couta_acumulado(Double anticipo_couta_acumulado) {
		this.anticipo_couta_acumulado = anticipo_couta_acumulado;
	}

	public Double getAprobado_cuota() {
		return aprobado_cuota;
	}

	public void setAprobado_cuota(Double aprobado_cuota) {
		this.aprobado_cuota = aprobado_cuota;
	}

	public Double getAprobado_cuota_acumulado() {
		return aprobado_cuota_acumulado;
	}

	public void setAprobado_cuota_acumulado(Double aprobado_cuota_acumulado) {
		this.aprobado_cuota_acumulado = aprobado_cuota_acumulado;
	}

	public Double getCuota_total() {
		return cuota_total;
	}

	public void setCuota_total(Double cuota_total) {
		this.cuota_total = cuota_total;
	}

	public Double getCuota_total_acumulado() {
		return cuota_total_acumulado;
	}

	public void setCuota_total_acumulado(Double cuota_total_acumulado) {
		this.cuota_total_acumulado = cuota_total_acumulado;
	}

	public Double getEjecutado() {
		return ejecutado;
	}

	public void setEjecutado(Double ejecutado) {
		this.ejecutado = ejecutado;
	}

	public Double getEjecutado_acumulado() {
		return ejecutado_acumulado;
	}

	public void setEjecutado_acumulado(Double ejecutado_acumulado) {
		this.ejecutado_acumulado = ejecutado_acumulado;
	}

	public Double getVigente() {
		return vigente;
	}

	public void setVigente(Double vigente) {
		this.vigente = vigente;
	}

}

