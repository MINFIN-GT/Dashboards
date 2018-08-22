package pojo.formulacion;

public class CInstitucionalTipoGasto {
	int ejercicio;
	int entidad;
	String entidad_nombre;
	Double recomendado;
	String tp11_nombre;
	Double tp11_monto;
	String tp12_nombre;
	Double tp12_monto;
	String tp13_nombre;
	Double tp13_monto;
	String tp21_nombre;
	Double tp21_monto;
	String tp22_nombre;
	Double tp22_monto;
	String tp23_nombre;
	Double tp23_monto;
	String tp31_nombre;
	Double tp31_monto;
	
	public CInstitucionalTipoGasto(int ejercicio, int entidad, String entidad_nombre, Double recomendado,
			Double tp11_monto, Double tp12_monto, Double tp13_monto, Double tp21_monto, Double tp22_monto,
			Double tp23_monto, Double tp31_monto) {
		super();
		this.ejercicio = ejercicio;
		this.entidad = entidad;
		this.entidad_nombre = entidad_nombre;
		this.recomendado = recomendado;
		this.tp11_monto = tp11_monto;
		this.tp12_monto = tp12_monto;
		this.tp13_monto = tp13_monto;
		this.tp21_monto = tp21_monto;
		this.tp22_monto = tp22_monto;
		this.tp23_monto = tp23_monto;
		this.tp31_monto = tp31_monto;
		this.tp11_nombre = "Administración";
		this.tp12_nombre = "Desarrollo Humano";
		this.tp13_nombre = "Transferencias Corrientes";
		this.tp21_nombre = "Inversión Física";
		this.tp22_nombre = "Transferencias de Capital";
		this.tp23_nombre = "Inversión Financiera";
		this.tp31_nombre = "Deuda Pública";
	}

	public int getEjercicio() {
		return ejercicio;
	}

	public void setEjercicio(int ejercicio) {
		this.ejercicio = ejercicio;
	}

	public int getEntidad() {
		return entidad;
	}

	public void setEntidad(int entidad) {
		this.entidad = entidad;
	}

	public String getEntidad_nombre() {
		return entidad_nombre;
	}

	public void setEntidad_nombre(String entidad_nombre) {
		this.entidad_nombre = entidad_nombre;
	}

	public Double getRecomendado() {
		return recomendado;
	}

	public void setRecomendado(Double recomendado) {
		this.recomendado = recomendado;
	}

	public String getTp11_nombre() {
		return tp11_nombre;
	}

	public void setTp11_nombre(String tp11_nombre) {
		this.tp11_nombre = tp11_nombre;
	}

	public Double getTp11_monto() {
		return tp11_monto;
	}

	public void setTp11_monto(Double tp11_monto) {
		this.tp11_monto = tp11_monto;
	}

	public String getTp12_nombre() {
		return tp12_nombre;
	}

	public void setTp12_nombre(String tp12_nombre) {
		this.tp12_nombre = tp12_nombre;
	}

	public Double getTp12_monto() {
		return tp12_monto;
	}

	public void setTp12_monto(Double tp12_monto) {
		this.tp12_monto = tp12_monto;
	}

	public String getTp13_nombre() {
		return tp13_nombre;
	}

	public void setTp13_nombre(String tp13_nombre) {
		this.tp13_nombre = tp13_nombre;
	}

	public Double getTp13_monto() {
		return tp13_monto;
	}

	public void setTp13_monto(Double tp13_monto) {
		this.tp13_monto = tp13_monto;
	}

	public String getTp21_nombre() {
		return tp21_nombre;
	}

	public void setTp21_nombre(String tp21_nombre) {
		this.tp21_nombre = tp21_nombre;
	}

	public Double getTp21_monto() {
		return tp21_monto;
	}

	public void setTp21_monto(Double tp21_monto) {
		this.tp21_monto = tp21_monto;
	}

	public String getTp22_nombre() {
		return tp22_nombre;
	}

	public void setTp22_nombre(String tp22_nombre) {
		this.tp22_nombre = tp22_nombre;
	}

	public Double getTp22_monto() {
		return tp22_monto;
	}

	public void setTp22_monto(Double tp22_monto) {
		this.tp22_monto = tp22_monto;
	}

	public String getTp23_nombre() {
		return tp23_nombre;
	}

	public void setTp23_nombre(String tp23_nombre) {
		this.tp23_nombre = tp23_nombre;
	}

	public Double getTp23_monto() {
		return tp23_monto;
	}

	public void setTp23_monto(Double tp23_monto) {
		this.tp23_monto = tp23_monto;
	}

	public String getTp31_nombre() {
		return tp31_nombre;
	}

	public void setTp31_nombre(String tp31_nombre) {
		this.tp31_nombre = tp31_nombre;
	}

	public Double getTp31_monto() {
		return tp31_monto;
	}

	public void setTp31_monto(Double tp31_monto) {
		this.tp31_monto = tp31_monto;
	}
	
}
