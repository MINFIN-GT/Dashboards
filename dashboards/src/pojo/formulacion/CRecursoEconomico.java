package pojo.formulacion;

public class CRecursoEconomico {
	String texto;
	int posicion;
	int nivel;
	Double ejecutado_dos_antes;
	Double aprobado_anterior;
	Double aprobado_anterior_mas_amp;
	Double recomendado;
	int negrillas;
	
	public CRecursoEconomico(String texto, int posicion, int nivel, Double ejecutado_dos_antes, Double aprobado_anterior,
			Double aprobado_anterior_mas_amp, Double recomendado, int negrillas) {
		super();
		this.texto = texto;
		this.posicion = posicion;
		this.nivel = nivel;
		this.ejecutado_dos_antes = ejecutado_dos_antes;
		this.aprobado_anterior = aprobado_anterior;
		this.aprobado_anterior_mas_amp = aprobado_anterior_mas_amp;
		this.recomendado = recomendado;
		this.negrillas=negrillas;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public int getPosicion() {
		return posicion;
	}

	public void setPosicion(int posicion) {
		this.posicion = posicion;
	}

	public Double getejecutado_dos_antes() {
		return ejecutado_dos_antes;
	}

	public void setejecutado_dos_antes(Double ejecutado_dos_antes) {
		this.ejecutado_dos_antes = ejecutado_dos_antes;
	}

	public Double getaprobado_anterior() {
		return aprobado_anterior;
	}

	public void setaprobado_anterior(Double aprobado_anterior) {
		this.aprobado_anterior = aprobado_anterior;
	}

	public Double getRecomendado() {
		return recomendado;
	}

	public void setRecomendado(Double recomendado) {
		this.recomendado = recomendado;
	}

	public Double getaprobado_anterior_mas_amp() {
		return aprobado_anterior_mas_amp;
	}

	public void setaprobado_anterior_mas_amp(Double aprobado_anterior_mas_amp) {
		this.aprobado_anterior_mas_amp = aprobado_anterior_mas_amp;
	}

	public int getNivel() {
		return nivel;
	}

	public void setNivel(int nivel) {
		this.nivel = nivel;
	}

	public int getNegrillas() {
		return negrillas;
	}

	public void setNegrillas(int negrillas) {
		this.negrillas = negrillas;
	}
	
	
}
