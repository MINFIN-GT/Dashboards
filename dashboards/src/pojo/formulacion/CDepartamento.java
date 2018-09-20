package pojo.formulacion;

public class CDepartamento {
	int codigo;
	String nombre;
	Double recomendado;
	int nivel;
	
	public CDepartamento(int codigo, String nombre, Double recomendado, int nivel) {
		super();
		this.codigo = codigo;
		this.nombre = nombre;
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
