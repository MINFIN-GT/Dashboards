package servlets.formulacion;

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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import dao.formulacion.CFinalidadDAO;
import dao.formulacion.CInstitucionalDAO;
import pojo.formulacion.CFinalidadEconomico;
import pojo.formulacion.CFinalidadRegion;
import pojo.formulacion.CInstitucionalFinalidad;
import pojo.formulacion.CInstitucionalTipoGasto;
import pojo.formulacion.CInstitucionalTipoGastoGrupoGasto;
import pojo.formulacion.CInstitucionalTipoGastoRegion;
import pojo.formulacion.CInstitucionalTotal;
import utilities.Utils;

/**
 * Servlet implementation class SInstitucional
 */
@WebServlet("/SInstitucional")
public class SInstitucional extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SInstitucional() {
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
		Map<String, String> map = gson.fromJson(sb.toString(), type);
		String action = map.get("action");
		String response_text="";
		int ejercicio = Utils.String2Int(map.get("ejercicio"), 0);
		if(action.equals("getInstitucionalTotal")) {
			if(ejercicio>0) {
				ArrayList<CInstitucionalTotal> entidades = CInstitucionalDAO.getInstitucionalTotal(ejercicio);
				response.setHeader("Content-Encoding", "gzip");
				response.setCharacterEncoding("UTF-8");
				response_text=new GsonBuilder().serializeNulls().create().toJson(entidades);
	            response_text = String.join("", "\"entidades\":",response_text);
	            response_text = String.join("", "{\"success\":true,", response_text,"}");
			}
			else {
				response_text = String.join("", "{\"success\":false }");
			}
	    }
		else if(action.equals("getInstitucionalTipoGasto")) {
			if(ejercicio>0) {
				ArrayList<CInstitucionalTipoGasto> entidades = CInstitucionalDAO.getInstitucionalTipoGasto(ejercicio);
				response.setHeader("Content-Encoding", "gzip");
				response.setCharacterEncoding("UTF-8");
				response_text=new GsonBuilder().serializeNulls().create().toJson(entidades);
	            response_text = String.join("", "\"entidades\":",response_text);
	            response_text = String.join("", "{\"success\":true,", response_text,"}");
			}
			else {
				response_text = String.join("", "{\"success\":false }");
			}
		}
		else if(action.equals("getInstitucionalTipoGastoGrupoGasto")) {
			if(ejercicio>0) {
				int tipo_gasto = Utils.String2Int(map.get("tipo_gasto"), 0);
				ArrayList<CInstitucionalTipoGastoGrupoGasto> entidades = CInstitucionalDAO.getInstitucionalTipoGastoGrupoGasto(ejercicio,tipo_gasto);
				response.setHeader("Content-Encoding", "gzip");
				response.setCharacterEncoding("UTF-8");
				response_text=new GsonBuilder().serializeNulls().create().toJson(entidades);
	            response_text = String.join("", "\"entidades\":",response_text);
	            response_text = String.join("", "{\"success\":true,", response_text,"}");
			}
			else {
				response_text = String.join("", "{\"success\":false }");
			}
		}
		else if(action.equals("getInstitucionalFinalidad")) {
			if(ejercicio>0) {
				ArrayList<CInstitucionalFinalidad> entidades = CInstitucionalDAO.getInstitucionalFinalidad(ejercicio);
				response.setHeader("Content-Encoding", "gzip");
				response.setCharacterEncoding("UTF-8");
				response_text=new GsonBuilder().serializeNulls().create().toJson(entidades);
	            response_text = String.join("", "\"entidades\":",response_text);
	            response_text = String.join("", "{\"success\":true,", response_text,"}");
			}
			else {
				response_text = String.join("", "{\"success\":false }");
			}
		}
		else if(action.equals("getInstitucionalTipoGastoRegion")) {
			if(ejercicio>0) {
				int tipo_gasto = Utils.String2Int(map.get("tipo_gasto"), 0);
				ArrayList<CInstitucionalTipoGastoRegion> entidades = CInstitucionalDAO.getInstitucionalTipoGastoRegion(ejercicio,tipo_gasto);
				response.setHeader("Content-Encoding", "gzip");
				response.setCharacterEncoding("UTF-8");
				response_text=new GsonBuilder().serializeNulls().create().toJson(entidades);
	            response_text = String.join("", "\"entidades\":",response_text);
	            response_text = String.join("", "{\"success\":true,", response_text,"}");
			}
			else {
				response_text = String.join("", "{\"success\":false }");
			}
		}
		else if(action.equals("getFinalidadRegion")) {
			if(ejercicio>0) {
				ArrayList<CFinalidadRegion> finalidades = CFinalidadDAO.getFinalidadRegion(ejercicio);
				response.setHeader("Content-Encoding", "gzip");
				response.setCharacterEncoding("UTF-8");
				response_text=new GsonBuilder().serializeNulls().create().toJson(finalidades);
	            response_text = String.join("", "\"finalidades\":",response_text);
	            response_text = String.join("", "{\"success\":true,", response_text,"}");
			}
			else {
				response_text = String.join("", "{\"success\":false }");
			}
		}
		else if(action.equals("getFinalidadEconomico")) {
			if(ejercicio>0) {
				ArrayList<CFinalidadEconomico> finalidades = CFinalidadDAO.getFinalidadRecurso(ejercicio);
				response.setHeader("Content-Encoding", "gzip");
				response.setCharacterEncoding("UTF-8");
				response_text=new GsonBuilder().serializeNulls().create().toJson(finalidades);
	            response_text = String.join("", "\"finalidades\":",response_text);
	            response_text = String.join("", "{\"success\":true,", response_text,"}");
			}
			else {
				response_text = String.join("", "{\"success\":false }");
			}
		}
		 OutputStream output = response.getOutputStream();
			GZIPOutputStream gz = new GZIPOutputStream(output);
	        gz.write(response_text.getBytes("UTF-8"));
         gz.close();
         output.close();
	}

}
