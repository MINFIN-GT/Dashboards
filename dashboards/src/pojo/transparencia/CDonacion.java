package pojo.transparencia;

import java.sql.Timestamp;

public class CDonacion {
	int id;
	int programa;
	int subprograma;
	String donante;
	String procedencia;
	String metodo_acreditamiento;
	Timestamp fecha_ingreso;
	double monto_d;
	double monto_q;
	String estado;
	String destino;
	String usuario_creacion;
	Timestamp fecha_creacion;
	String usuario_modificacion;
	Timestamp fecha_modificacion;
	
	public CDonacion(int id,int programa, int subprograma, String donante, String procedencia, String metodo_acreditamiento, Timestamp fecha_ingreso,
			double monto_d, double monto_q, String estado, String destino, String usuario_creacion,
			Timestamp fecha_creacion, String usuario_modificacion, Timestamp fecha_modificacion) {
		super();
		this.id = id;
		this.programa = programa;
		this.subprograma = subprograma;
		this.donante = donante;
		this.procedencia = procedencia;
		this.metodo_acreditamiento = metodo_acreditamiento;
		this.fecha_ingreso = fecha_ingreso;
		this.monto_d = monto_d;
		this.monto_q = monto_q;
		this.estado = estado;
		this.destino = destino;
		this.usuario_creacion = usuario_creacion;
		this.fecha_creacion = fecha_creacion;
		this.usuario_modificacion = usuario_modificacion;
		this.fecha_modificacion = fecha_modificacion;
		
	}
	
	public int getPrograma(){
		return programa;
	}
	
	public void setPrograma(int programa){
		this.programa = programa;
	}
	
	public int getSubprograma(){
		return this.subprograma;
	}
	
	public void setSubprograma(int subprograma){
		this.subprograma = subprograma;
	}

	public String getDonante() {
		return donante;
	}

	public void setDonante(String donante) {
		this.donante = donante;
	}

	public String getProcedencia() {
		return procedencia;
	}

	public void setProcedencia(String procedencia) {
		this.procedencia = procedencia;
	}

	public String getMetodo_acreditamiento() {
		return metodo_acreditamiento;
	}

	public void setMetodo_acreditamiento(String metodo_acreditamiento) {
		this.metodo_acreditamiento = metodo_acreditamiento;
	}

	public Timestamp getFecha_ingreso() {
		return fecha_ingreso;
	}

	public void setFecha_ingreso(Timestamp fecha_ingreso) {
		this.fecha_ingreso = fecha_ingreso;
	}

	public double getMonto_d() {
		return monto_d;
	}

	public void setMonto_d(double monto_d) {
		this.monto_d = monto_d;
	}

	public double getMonto_q() {
		return monto_q;
	}

	public void setMonto_q(double monto_q) {
		this.monto_q = monto_q;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getDestino() {
		return destino;
	}

	public void setDestino(String destino) {
		this.destino = destino;
	}

	public String getUsuario_creacion() {
		return usuario_creacion;
	}

	public void setUsuario_creacion(String usuario_creacion) {
		this.usuario_creacion = usuario_creacion;
	}

	public Timestamp getFecha_creacion() {
		return fecha_creacion;
	}

	public void setFecha_creacion(Timestamp fecha_creacion) {
		this.fecha_creacion = fecha_creacion;
	}

	public String getUsuario_modificacion() {
		return usuario_modificacion;
	}

	public void setUsuario_modificacion(String usuario_modificacion) {
		this.usuario_modificacion = usuario_modificacion;
	}

	public Timestamp getFecha_modificacion() {
		return fecha_modificacion;
	}

	public void setFecha_modificacion(Timestamp fecha_modificacion) {
		this.fecha_modificacion = fecha_modificacion;
	}
	
	
}
