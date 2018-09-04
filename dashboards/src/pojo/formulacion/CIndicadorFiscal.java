package pojo.formulacion;

public class CIndicadorFiscal {
	String texto;
	int posicion;
	int nivel;
	int negrillas;
	
	public CIndicadorFiscal(String texto, int posicion, int nivel, int negrillas) {
		super();
		this.texto = texto;
		this.posicion = posicion;
		this.nivel = nivel;
		this.negrillas = negrillas;
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
