package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
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

import dao.CEjecucionDAO;
import dao.CEntidadDAO;
import dao.CRecursoDAO;
import dao.CTesoreria;
import pojo.CEntidad;
import pojo.CGasto;
import pojo.CRecurso;
import pojo.CRecursoAuxiliar;
import pojo.CTesoreriaCuenta;
import pojo.CUnidadEjecutora;

/**
 * Servlet implementation class SFlujoCaja
 */
@WebServlet("/SFlujoCaja")
public class SFlujoCaja extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SFlujoCaja() {
        super();
        // TODO Auto-generated constructor stub
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
	    response.setHeader("Content-Encoding", "gzip");
		response.setCharacterEncoding("UTF-8");
		Map<String, String> map = gson.fromJson(sb.toString(), type);
		String action = map.get("action");
		String response_text=null;
		if(action.equals("getRecursos")){
			int ejercicio = map.get("ejercicio")!=null ? Integer.parseInt(map.get("ejercicio")) : DateTime.now().getYear();
			ArrayList<CRecurso> recursos = CRecursoDAO.getRecursos(ejercicio);
			response_text=new GsonBuilder().serializeNulls().create().toJson(recursos);
	        response_text = String.join("", "\"recursos\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(action.equals("getAuxiliares")){
			int ejercicio = map.get("ejercicio")!=null ? Integer.parseInt(map.get("ejercicio")) : DateTime.now().getYear();
			int recurso = map.get("recursoId")!=null ? Integer.parseInt(map.get("recursoId")) : 0;
			ArrayList<CRecursoAuxiliar> auxiliares = CRecursoDAO.getAuxiliares(ejercicio, recurso);
			response_text=new GsonBuilder().serializeNulls().create().toJson(auxiliares);
	        response_text = String.join("", "\"auxiliares\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(action.equals("getPronosticosIngresos")){
			Type typea = new TypeToken<Map<String, Integer[]>>(){}.getType();
			int ejercicio = map.get("ejercicio")!=null ? Integer.parseInt(map.get("ejercicio")) : DateTime.now().getYear();
			boolean fecha_real = map.get("fecha_real")!=null ? Integer.parseInt(map.get("fecha_real"))==1 : false; 
			String[] recursosIds = map.get("recursosIds")!=null && map.get("recursosIds").length()>2 ?  map.get("recursosIds").substring(1,map.get("recursosIds").length()-2).split(",") : null;
			Map<String,Integer[]> auxiliaresIds = map.get("auxiliaresIds")!=null && map.get("auxiliaresIds").length()>0 ? gson.fromJson(map.get("auxiliaresIds"), typea) : null;
			int ajustado = map.get("ajustado")!=null ? Integer.parseInt(map.get("ajustado")) : 0;
			Double[] pronosticos = CRecursoDAO.getPronosticos(ejercicio, recursosIds, auxiliaresIds, ajustado, fecha_real);
			Double[] historicos = CRecursoDAO.getHistoricos(ejercicio, recursosIds, auxiliaresIds, fecha_real);
			Double[][] data_historia = CRecursoDAO.getTodaHistoria(recursosIds, auxiliaresIds, fecha_real);
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(pronosticos);
			String response_text_historicos = new GsonBuilder().serializeNulls().create().toJson(historicos);
			String response_text_historia = new GsonBuilder().serializeNulls().create().toJson(data_historia);
	        response_text = String.join("", "\"pronosticos\":",response_text,", \"historicos\":", response_text_historicos,",\"historia\":", response_text_historia);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(action.equals("getPronosticosIngresosDetalle")){
			Type typea = new TypeToken<Map<String, Integer[]>>(){}.getType();
			int ejercicio = map.get("ejercicio")!=null ? Integer.parseInt(map.get("ejercicio")) : DateTime.now().getYear();
			int mes = map.get("mes")!=null ? Integer.parseInt(map.get("mes")) : DateTime.now().getMonthOfYear();
			boolean fecha_real = map.get("fecha_real")!=null ? Integer.parseInt(map.get("fecha_real"))==1 : false; 
			String[] recursosIds = map.get("recursosIds")!=null && map.get("recursosIds").length()>2 ?  map.get("recursosIds").substring(1,map.get("recursosIds").length()-2).split(",") : null;
			Map<String,Integer[]> auxiliaresIds = map.get("auxiliaresIds")!=null && map.get("auxiliaresIds").length()>0 ? gson.fromJson(map.get("auxiliaresIds"), typea) : null;
			int numero = map.get("numero")!=null ? Integer.parseInt(map.get("numero")) : 0;
			ArrayList<CRecurso> recursos = CRecursoDAO.getPronosticosDetalle(ejercicio, mes, numero, recursosIds, auxiliaresIds, fecha_real);
			response_text=new GsonBuilder().serializeNulls().create().toJson(recursos);
			response_text = String.join("", "\"pronosticos\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(action.equals("getEntidades")){
			int ejercicio = map.get("ejercicio")!=null ? Integer.parseInt(map.get("ejercicio")) : DateTime.now().getYear();
			ArrayList<CEntidad> entidades = CEntidadDAO.getEntidades(ejercicio);
			response_text=new GsonBuilder().serializeNulls().create().toJson(entidades);
	        response_text = String.join("", "\"entidades\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(action.equals("getUnidadesEjecutoras")){
			int ejercicio = map.get("ejercicio")!=null ? Integer.parseInt(map.get("ejercicio")) : DateTime.now().getYear();
			int entidad = map.get("entidadId")!=null ? Integer.parseInt(map.get("entidadId")) : 0;
			ArrayList<CUnidadEjecutora> unidad_ejecutoras = CEntidadDAO.getUnidadesEjecutoras(ejercicio, entidad);
			response_text=new GsonBuilder().serializeNulls().create().toJson(unidad_ejecutoras);
	        response_text = String.join("", "\"unidades_ejecutoras\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(action.equals("getPronosticosEgresos")){
			int ejercicio = map.get("ejercicio")!=null ? Integer.parseInt(map.get("ejercicio")) : DateTime.now().getYear();
			int entidad = map.get("entidadId")!=null ? Integer.parseInt(map.get("entidadId")) : 0;
			int unidad_ejecutora = map.get("unidad_ejecutoraId")!=null ? Integer.parseInt(map.get("unidad_ejecutoraId")) : 0;
			int ajustado = map.get("ajustado")!=null ? Integer.parseInt(map.get("ajustado")) : 0;
			int con_regularizaciones = map.get("con_regularizaciones")!=null ? Integer.parseInt(map.get("con_regularizaciones")) : 0;
			boolean fecha_egresos_devengado = map.get("fecha_egresos")!=null ? Integer.parseInt(map.get("fecha_egresos"))==1 : false; 
			Double[] pronosticos = (con_regularizaciones==1) ? CEntidadDAO.getPronosticosEgresos(ejercicio, entidad, unidad_ejecutora, ajustado) :
				CEntidadDAO.getPronosticosEgresosSinRegularizaciones(ejercicio, entidad, unidad_ejecutora, ajustado, fecha_egresos_devengado) ;
			Double[] historicos = (con_regularizaciones==1) ? CEntidadDAO.getPronosticosHistoricosEgresos(ejercicio, entidad,unidad_ejecutora) : 
				CEntidadDAO.getPronosticosHistoricosEgresosSinRegularizaciones(ejercicio, entidad,unidad_ejecutora, fecha_egresos_devengado);
			Double[][] data_historia = (con_regularizaciones==1) ? CEntidadDAO.getTodaHistoria(entidad,unidad_ejecutora) : 
				CEntidadDAO.getTodaHistoriaSinRegularizaciones(entidad,unidad_ejecutora);
			response_text=new GsonBuilder().serializeNulls().create().toJson(pronosticos);
			String response_text_historicos = new GsonBuilder().serializeNulls().create().toJson(historicos);
			String response_text_historia = new GsonBuilder().serializeNulls().create().toJson(data_historia);
	        response_text = String.join("", "\"pronosticos\":",response_text,", \"historicos\":", response_text_historicos,",\"historia\":", response_text_historia);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(action.equals("getPronosticosEgresosTree")){
			int ejercicio = map.get("ejercicio")!=null ? Integer.parseInt(map.get("ejercicio")) : DateTime.now().getYear();
			int entidad = map.get("entidadId")!=null ? Integer.parseInt(map.get("entidadId")) : 0;
			int unidad_ejecutora = map.get("unidad_ejecutoraId")!=null ? Integer.parseInt(map.get("unidad_ejecutoraId")) : 0;
			int ajustado = map.get("ajustado")!=null ? Integer.parseInt(map.get("ajustado")) : 0;
			int con_regularizaciones = map.get("con_regularizaciones")!=null ? Integer.parseInt(map.get("con_regularizaciones")) : 0;
			ArrayList<CGasto> arbol = (con_regularizaciones==1) ? CEntidadDAO.getPronosticosEgresosTree(ejercicio, entidad, unidad_ejecutora, ajustado) :
				CEntidadDAO.getPronosticosEgresosTreeSinRegularizaciones(ejercicio, entidad, unidad_ejecutora, ajustado);
			response_text=new GsonBuilder().serializeNulls().create().toJson(arbol);
			response_text = String.join("", "\"arbol\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(action.equals("getPronosticosFlujo")){
			int ejercicio = map.get("ejercicio")!=null ? Integer.parseInt(map.get("ejercicio")) : DateTime.now().getYear();
			boolean fecha_real = map.get("fecha_real")!=null ? Integer.parseInt(map.get("fecha_real"))==1 : false; 
			boolean fecha_egresos_devengado = map.get("fecha_egresos")!=null ? Integer.parseInt(map.get("fecha_egresos"))==1 : false; 
			DateTime now = DateTime.now();
			int mes = (ejercicio < now.getYear()) ? 1 : now.getMonthOfYear();
			Double[] pronosticos_egresos = CEntidadDAO.getPronosticosEgresosSinRegularizaciones(ejercicio, 0, 0, 0, fecha_egresos_devengado);
			Double[] historicos_egresos = (mes-1>0) ? CEntidadDAO.getPronosticosHistoricosEgresosSinRegularizaciones(ejercicio,  0, 0, fecha_egresos_devengado) : new Double[0];
			Double[] pronosticos_ingresos = CRecursoDAO.getPronosticos(ejercicio, null, null,0, fecha_real);
			Double[] historicos_ingresos = (mes-1>0) ? CRecursoDAO.getHistoricos(ejercicio, null, null, fecha_real) : new Double[0];
			Double[] pronosticos_egresos_contables =  CEjecucionDAO.getPronosticosEgresosContables(ejercicio, null, 0, 0);
			Double[] historicos_egresos_contables = CEjecucionDAO.getPronosticosHistoricosEgresosContables(ejercicio, null);
			ArrayList<CTesoreriaCuenta> cuentas= CTesoreria.getSaldoInicialCuentas(ejercicio);
			String response_pronosticos_egresos = new GsonBuilder().serializeNulls().create().toJson(pronosticos_egresos);
			String response_historicos_egresos = new GsonBuilder().serializeNulls().create().toJson(historicos_egresos);
			String response_pronosticos_ingresos = new GsonBuilder().serializeNulls().create().toJson(pronosticos_ingresos);
			String response_historicos_ingresos = new GsonBuilder().serializeNulls().create().toJson(historicos_ingresos);
			String response_pronosticos_egresos_contables = new GsonBuilder().serializeNulls().create().toJson(pronosticos_egresos_contables);
			String response_historicos_egresos_contables = new GsonBuilder().serializeNulls().create().toJson(historicos_egresos_contables);
			String response_saldo_cuentas = new GsonBuilder().serializeNulls().create().toJson(cuentas);
			response_text = String.join("", "\"pronosticos_egresos\":",response_pronosticos_egresos,", \"historicos_egresos\":", response_historicos_egresos,
					",\"pronosticos_ingresos\":", response_pronosticos_ingresos,",\"historicos_ingresos\":", response_historicos_ingresos,
					",\"pronosticos_egresos_contables\":", response_pronosticos_egresos_contables,",\"historicos_egresos_contables\":", response_historicos_egresos_contables,
					",\"cuentas_saldo\":", response_saldo_cuentas);
			response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(action.equals("getRecursosTree")){
			int ejercicio = map.get("ejercicio")!=null ? Integer.parseInt(map.get("ejercicio")) : DateTime.now().getYear();
			CRecurso root = CRecursoDAO.getRecursosTree(ejercicio);
			response_text = String.join("", "\"recursos\":",new GsonBuilder().serializeNulls().create().toJson(root));
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(action.equals("getPronosticosIngresosPorRecurso")) {
			int ejercicio = map.get("ejercicio")!=null ? Integer.parseInt(map.get("ejercicio")) : DateTime.now().getYear();
			String[] recursosIds = map.get("recursosIds")!=null && map.get("recursosIds").length()>2 ?  map.get("recursosIds").substring(1,map.get("recursosIds").length()-2).split(",") : null;
			int ajustado = map.get("ajustado")!=null ? Integer.parseInt(map.get("ajustado")) : 0;
			boolean fecha_real = map.get("fecha_real")!=null ? Integer.parseInt(map.get("fecha_real"))==1 : false; 
			ArrayList<CRecurso> pronosticos = CRecursoDAO.getPronosticosPorRecurso(ejercicio, recursosIds, ajustado, fecha_real);
			response_text=new GsonBuilder().serializeNulls().create().toJson(pronosticos);
			response_text = String.join("", "\"pronosticos\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
        OutputStream output = response.getOutputStream();
		GZIPOutputStream gz = new GZIPOutputStream(output);
        gz.write(response_text.getBytes("UTF-8"));
        gz.close();
        output.close();
	}

}
