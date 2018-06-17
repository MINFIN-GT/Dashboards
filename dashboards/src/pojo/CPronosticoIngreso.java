package pojo;

import java.util.ArrayList;

public class CPronosticoIngreso {
	Integer codigo;
	String nombre;
	ArrayList<Double> pronosticos;
	
	public CPronosticoIngreso(Integer codigo, String nombre, ArrayList<Double> pronosticos) {
		super();
		this.codigo = codigo;
		this.nombre = nombre;
		this.pronosticos = pronosticos;
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

	public ArrayList<Double> getPronosticos() {
		return pronosticos;
	}

	public void setPronosticos(ArrayList<Double> pronosticos) {
		this.pronosticos = pronosticos;
	}
	
	
	
}
