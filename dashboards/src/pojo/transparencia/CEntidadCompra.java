package pojo.transparencia;

public class CEntidadCompra {
	int id_entidad;
	String entidad;
	int num_eventos;
	int num_adjudicados;
	double total_adjudicado;
	
	public CEntidadCompra(int id_entidad, String entidad, int num_eventos, int num_adjudicados,
			double total_adjudicado) {
		super();
		this.id_entidad = id_entidad;
		this.entidad = entidad;
		this.num_eventos = num_eventos;
		this.num_adjudicados = num_adjudicados;
		this.total_adjudicado = total_adjudicado;
	}

	public int getId_entidad() {
		return id_entidad;
	}

	public void setId_entidad(int id_entidad) {
		this.id_entidad = id_entidad;
	}

	public String getEntidad() {
		return entidad;
	}

	public void setEntidad(String entidad) {
		this.entidad = entidad;
	}

	public int getNum_eventos() {
		return num_eventos;
	}

	public void setNum_eventos(int num_eventos) {
		this.num_eventos = num_eventos;
	}

	public int getNum_adjudicados() {
		return num_adjudicados;
	}

	public void setNum_adjudicados(int num_adjudicados) {
		this.num_adjudicados = num_adjudicados;
	}

	public double getTotal_adjudicado() {
		return total_adjudicado;
	}

	public void setTotal_adjudicado(double total_adjudicado) {
		this.total_adjudicado = total_adjudicado;
	}
}
