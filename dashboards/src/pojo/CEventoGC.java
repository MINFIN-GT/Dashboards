package pojo;

public class CEventoGC {
	int ejercicio;
	int entidad_gc;
	int entidad_sicoin;
	String nombre;
	String nombre_corto;
	int[] ano_1;
	int[] ano_2;
	int[] ano_actual;
	
	public CEventoGC(int ejercicio, int entidad_gc, int entidad_sicoin, String nombre, String nombre_corto, int[] ano_1,
			int[] ano_2, int[] ano_actual) {
		super();
		this.ejercicio = ejercicio;
		this.entidad_gc = entidad_gc;
		this.entidad_sicoin = entidad_sicoin;
		this.nombre = nombre;
		this.nombre_corto = nombre_corto;
		this.ano_1 = ano_1;
		this.ano_2 = ano_2;
		this.ano_actual = ano_actual;
	}
	
	public int getEjercicio() {
		return ejercicio;
	}
	public void setEjercicio(int ejercicio) {
		this.ejercicio = ejercicio;
	}
	public int getEntidad_gc() {
		return entidad_gc;
	}
	public void setEntidad_gc(int entidad_gc) {
		this.entidad_gc = entidad_gc;
	}
	public int getEntidad_sicoin() {
		return entidad_sicoin;
	}
	public void setEntidad_sicoin(int entidad_sicoin) {
		this.entidad_sicoin = entidad_sicoin;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getNombre_corto() {
		return nombre_corto;
	}
	public void setNombre_corto(String nombre_corto) {
		this.nombre_corto = nombre_corto;
	}
	public int[] getAno_1() {
		return ano_1;
	}
	public void setAno_1(int[] ano_1) {
		this.ano_1 = ano_1;
	}
	public int[] getAno_2() {
		return ano_2;
	}
	public void setAno_2(int[] ano_2) {
		this.ano_2 = ano_2;
	}
	public int[] getAno_actual() {
		return ano_actual;
	}
	public void setAno_actual(int[] ano_actual) {
		this.ano_actual = ano_actual;
	}
}
