package pojo;

import java.util.ArrayList;

public class CGasto {
	int ejercicio;
	int codigo;
	String nombre;
	Double[] pronosticos;
	ArrayList<CGasto> children;
	
	public CGasto(int ejercicio, int codigo, String nombre, Double[] pronosticos, ArrayList<CGasto> children) {
		super();
		this.ejercicio = ejercicio;
		this.codigo = codigo;
		this.nombre = nombre;
		this.pronosticos = pronosticos;
		this.children = children;
	}

	public int getEjercicio() {
		return ejercicio;
	}

	public void setEjercicio(int ejercicio) {
		this.ejercicio = ejercicio;
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

	public Double[] getPronosticos() {
		return pronosticos;
	}

	public void setPronosticos(Double[] pronosticos) {
		this.pronosticos = pronosticos;
	}

	public ArrayList<CGasto> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<CGasto> children) {
		this.children = children;
	}
	
	
}
