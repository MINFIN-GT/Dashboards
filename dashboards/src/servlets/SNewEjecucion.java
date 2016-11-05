package servlets;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.CNewEjecucionDAO;
import pojo.CNewEjecucion;
import utilities.Utils;

@WebServlet("/SNewEjecucion")
public class SNewEjecucion extends HttpServlet {
	private static final long serialVersionUID = 1693049433237575761L;
	
	class stentidad{
		Integer parent;
    	int entidad;
    	String nombre;
    	double ano1;
    	double ano2;
    	double ano3;
    	double ano4;
    	double ano5;
    	double cierre_estimado;
    	double solicitado;
    	double solicitado_acumulado;
    	double aprobado;
    	double aprobado_sin_anticipo;
    	double anticipo;
    	double aprobado_acumulado;
    	double ejecutado;
    	double ejecutado_acumulado;
    	double vigente;
    	double ejecucion_anual;
    	double aprobacion_anual;
    	int icono_ejecucion_anual;
    }

	public SNewEjecucion() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Map<String, String> map = Utils.getParams(request);

		int mes = Utils.String2Int(map.get("mes"));

		List<CNewEjecucion> ejecuciones = CNewEjecucionDAO.getEntidadesEjecucion(mes);
		
		List<stentidad> entidades = new ArrayList<stentidad>();
		
		for(CNewEjecucion ejecucion : ejecuciones){
			stentidad entidad = new stentidad();
			
			entidad.entidad = ejecucion.getCodigo();
			entidad.nombre = ejecucion.getNombre();
			entidad.ano1 = ejecucion.getAno_1();
			entidad.ano2 = ejecucion.getAno_2();
			entidad.ano3 = ejecucion.getAno_3();
			entidad.ano4 = ejecucion.getAno_4();
			entidad.ano5 = ejecucion.getAno_5();
			
			entidad.cierre_estimado = 0;
			entidad.solicitado = 0;
			entidad.solicitado_acumulado = 0;
			entidad.aprobado = ejecucion.getCuota_total();
			entidad.aprobado_sin_anticipo = ejecucion.getAprobado_cuota() ;
			entidad.anticipo = ejecucion.getAnticipo_couta();
			entidad.aprobado_acumulado = ejecucion.getCuota_total_acumulado();
			entidad.aprobacion_anual = 0;
			entidad.ejecutado = ejecucion.getEjecutado();
			entidad.ejecutado_acumulado = ejecucion.getEjecutado_acumulado();
			entidad.vigente = ejecucion.getVigente();
			
			entidad.ejecucion_anual = (entidad.vigente>0) ? (entidad.ejecutado_acumulado/entidad.vigente)*100.00 : 0.00;
			
			double semaforo = (entidad.ejecucion_anual*100.00)/(8.33*mes);
			if(semaforo<50)
				entidad.icono_ejecucion_anual = 4;
			else if(semaforo<75)
				entidad.icono_ejecucion_anual = 2;
			else if(semaforo<100)
				entidad.icono_ejecucion_anual = 3;
			else
				entidad.icono_ejecucion_anual = 1;
			
			entidades.add(entidad);
		}

		String response_text = Utils.getJSonString("entidades", entidades);

		response.setHeader("Content-Encoding", "gzip");
		response.setCharacterEncoding("UTF-8");
		OutputStream output = response.getOutputStream();
		GZIPOutputStream gz = new GZIPOutputStream(output);
		gz.write(response_text.getBytes("UTF-8"));
		gz.close();
		output.close();

	}

}
