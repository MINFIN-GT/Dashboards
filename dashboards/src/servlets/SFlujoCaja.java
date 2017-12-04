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

import dao.CRecursoDAO;
import pojo.CRecurso;
import pojo.CRecursoAuxiliar;

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
			int ejercicio = map.get("ejercicio")!=null ? Integer.parseInt(map.get("ejercicio")) : DateTime.now().getYear();
			int mes = map.get("mes")!=null ? Integer.parseInt(map.get("mes")) : DateTime.now().getMonthOfYear();
			int recurso = map.get("recursoId")!=null ? Integer.parseInt(map.get("recursoId")) : 0;
			int auxiliar = map.get("auxiliarId")!=null ? Integer.parseInt(map.get("auxiliarId")) : 0;
			int numero = map.get("numero")!=null ? Integer.parseInt(map.get("numero")) : 0;
			int ajustado = map.get("ajustado")!=null ? Integer.parseInt(map.get("ajustado")) : 0;
			Double[] pronosticos = CRecursoDAO.getPronosticos(ejercicio, mes,recurso, auxiliar, ajustado, numero);
			Double[] historicos = CRecursoDAO.getHistoricos(ejercicio, mes, recurso,auxiliar, 12);
			Double[][] data_historia = CRecursoDAO.getTodaHistoria(recurso,auxiliar);
			response_text=new GsonBuilder().serializeNulls().create().toJson(pronosticos);
			String response_text_historicos = new GsonBuilder().serializeNulls().create().toJson(historicos);
			String response_text_historia = new GsonBuilder().serializeNulls().create().toJson(data_historia);
	        response_text = String.join("", "\"pronosticos\":",response_text,", \"historicos\":", response_text_historicos,",\"historia\":", response_text_historia);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
        OutputStream output = response.getOutputStream();
		GZIPOutputStream gz = new GZIPOutputStream(output);
        gz.write(response_text.getBytes("UTF-8"));
        gz.close();
        output.close();
	}

}
