package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import dao.CEjecucionFisicaDAO;
import pojo.CEjecucionFisica;
import pojo.CMeta;
import pojo.varios.CEjecucionFisicaMensualizada;

/**
 * Servlet implementation class SEjecucion
 */
@WebServlet("/SEjecucionFisica")
public class SEjecucionFisica extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	class stentidad{
		Integer parent;
    	int entidad;
    	String nombre;
    	String sigla;
    	double porcentaje_ejecucion_financiera;
    	double porcentaje_ejecucion_fisica;
    	double ejecutado_financiero;
    	double vigente_financiero;
    	double spi;
    	int icono_ejecucion_anual;
    }
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SEjecucionFisica() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		Gson gson = new Gson();
		Type type = new TypeToken<Map<String, String>>(){}.getType();
		StringBuilder sb = new StringBuilder();
	    BufferedReader br = request.getReader();
	    String str;
	    while( (str = br.readLine()) != null ){
	        sb.append(str);
	    };
		Map<String, String> map = gson.fromJson(sb.toString(), type);
		String action = map.get("action");
		ArrayList<CEjecucionFisica> entidades=null; 
		String response_text="";
		Integer entidad = map.get("entidad")!=null ? Integer.parseInt(map.get("entidad")) : null;
		Integer unidad_ejecutora = map.get("ue")!=null ? Integer.parseInt(map.get("ue")) : null;
		Integer programa = map.get("programa")!=null ? Integer.parseInt(map.get("programa")) : null;
		Integer subprograma = map.get("subprograma")!=null ? Integer.parseInt(map.get("subprograma")) : null;
		Integer proyecto = map.get("proyecto")!=null ? Integer.parseInt(map.get("proyecto")) : null;
		Integer actividad = map.get("actividad")!=null ? Integer.parseInt(map.get("actividad")) : null;
		Integer obra = map.get("obra")!=null ? Integer.parseInt(map.get("obra")) : null;
		int level = map.get("level")!=null ? Integer.parseInt(map.get("level")) : 0;
		if(action.equals("entidadesData")){
			entidades = CEjecucionFisicaDAO.getEjecucion(level, entidad, unidad_ejecutora, programa, subprograma, proyecto, actividad, obra);
			if(entidades!=null && entidades.size()>0)
				response_text=new GsonBuilder().serializeNulls().create().toJson(aEntidadesEstructuras(entidades));
			response_text = String.join("", "\"entidades\":",response_text);
		}
		else if(action.equals("ejecucionMensualizada")){
			ArrayList<CEjecucionFisicaMensualizada> ejecucion=new ArrayList<CEjecucionFisicaMensualizada>();
			ejecucion = CEjecucionFisicaDAO.getEjecucionFisicaMensual(level,entidad,unidad_ejecutora, programa, subprograma, proyecto, actividad, obra);
			if(ejecucion!=null && ejecucion.size()>0)
				response_text = new GsonBuilder().serializeNulls().create().toJson(ejecucion);
			response_text = String.join("", "\"ejecucion\":",response_text);
		}
		else if(action.equals("metas")){
			DateTime date = new DateTime();
			ArrayList<CMeta> metas = new ArrayList<CMeta>();
			metas = CEjecucionFisicaDAO.getMetas(date.getYear(), entidad, unidad_ejecutora, programa, subprograma, proyecto, actividad, obra);
			if(metas!=null && metas.size()>0)
				response_text = new GsonBuilder().serializeNulls().create().toJson(metas);
			response_text = String.join("", "\"metas\":",response_text);
		}
		response.setHeader("Content-Encoding", "gzip");
		response.setCharacterEncoding("UTF-8");
		
        response_text = String.join("", "{\"success\":true,", response_text,"}");
        OutputStream output = response.getOutputStream();
		GZIPOutputStream gz = new GZIPOutputStream(output);
        gz.write(response_text.getBytes("UTF-8"));
        gz.close();
        output.close();
	}
	
	private ArrayList<stentidad> aEntidadesEstructuras(ArrayList<CEjecucionFisica> entidades){
		ArrayList<stentidad> ret = new ArrayList<stentidad>();
		DateTime now = new DateTime();
		for(CEjecucionFisica centidad : entidades){
			stentidad sttemp = new stentidad();
			sttemp.parent = centidad.getParent();
			sttemp.entidad = centidad.getEntidad();
			sttemp.sigla = centidad.getSigla();
			sttemp.nombre = centidad.getNombre();
			sttemp.porcentaje_ejecucion_financiera = centidad.getPorcentaje_ejecucion_presupuestaria();
			sttemp.porcentaje_ejecucion_fisica = centidad.getPorcentaje_ejecucion_fisica();
			sttemp.ejecutado_financiero = centidad.getEjecutado_financiero();
			sttemp.vigente_financiero = centidad.getVigente_financiero();
			sttemp.spi = (new BigDecimal((sttemp.porcentaje_ejecucion_fisica*100.00)/((100.00/12.00)*now.getMonthOfYear())).setScale(2,BigDecimal.ROUND_HALF_DOWN)).doubleValue(); 
			double semaforo = ((sttemp.porcentaje_ejecucion_fisica)/((100.00/12.00)*now.getMonthOfYear()))*100.00;
			if(semaforo<50)
				sttemp.icono_ejecucion_anual = 4;
			else if(semaforo<75)
				sttemp.icono_ejecucion_anual = 2;
			else if(semaforo<=100)
				sttemp.icono_ejecucion_anual = 3;
			else
				sttemp.icono_ejecucion_anual = 1;
			ret.add(sttemp);
		}
		return ret;
	}

}
