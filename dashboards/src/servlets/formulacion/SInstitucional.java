package servlets.formulacion;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.codec.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import dao.formulacion.CFinalidadDAO;
import dao.formulacion.CGastoDAO;
import dao.formulacion.CInstitucionalDAO;
import dao.formulacion.CRecursoDAO;
import pojo.formulacion.CFinalidadEconomico;
import pojo.formulacion.CFinalidadRegion;
import pojo.formulacion.CGastoEconomico;
import pojo.formulacion.CInstitucionalFinalidad;
import pojo.formulacion.CInstitucionalTipoGasto;
import pojo.formulacion.CInstitucionalTipoGastoGrupoGasto;
import pojo.formulacion.CInstitucionalTipoGastoRegion;
import pojo.formulacion.CInstitucionalTotal;
import pojo.formulacion.CRecursoEconomico;
import utilities.CExcelFormulacion;
import utilities.CLogger;
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
		if(action.equals("getRecursosTotal")) {
			if(ejercicio>0) {
				ArrayList<CRecursoEconomico> entidades = CRecursoDAO.getRecursosTotal(ejercicio);
				response.setHeader("Content-Encoding", "gzip");
				response.setCharacterEncoding("UTF-8");
				response_text=new GsonBuilder().serializeNulls().create().toJson(entidades);
	            response_text = String.join("", "\"recursos\":",response_text);
	            response_text = String.join("", "{\"success\":true,", response_text,"}");
			}
			else {
				response_text = String.join("", "{\"success\":false }");
			}
	    }
		else if(action.equals("getGastosTotal")) {
			if(ejercicio>0) {
				ArrayList<CGastoEconomico> entidades = CGastoDAO.getGastosTotal(ejercicio);
				response.setHeader("Content-Encoding", "gzip");
				response.setCharacterEncoding("UTF-8");
				response_text=new GsonBuilder().serializeNulls().create().toJson(entidades);
	            response_text = String.join("", "\"gastos\":",response_text);
	            response_text = String.join("", "{\"success\":true,", response_text,"}");
			}
			else {
				response_text = String.join("", "{\"success\":false }");
			}
	    }
		else if(action.equals("getInstitucionalTotal")) {
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
		}else if(action.equals("exportarExcel")) {
			int numeroCuadro = Utils.String2Int(map.get("numeroCuadro"), 0);
			byte [] outArray = exportarExcel(ejercicio, numeroCuadro);
			response.setContentType("application/ms-excel");
			response.setContentLength(outArray.length);
			response.setHeader("Cache-Control", "no-cache"); 
			response.setHeader("Content-Disposition", "attachment; Cuadros Globales Recomendado " + ejercicio + ".xlsx");
			ServletOutputStream outStream = response.getOutputStream();
			outStream.write(outArray);
			outStream.flush();
			outStream.close();
		}
		
		if(!action.equals("exportarExcel")) {
			OutputStream output = response.getOutputStream();
			GZIPOutputStream gz = new GZIPOutputStream(output);
	        gz.write(response_text.getBytes("UTF-8"));
	        gz.close();
	        output.close();
		}
	}

	private byte[] exportarExcel(Integer ejercicio, Integer numeroCuadro) throws IOException{
		byte [] outArray = null;
		try{	
			CExcelFormulacion excel= new CExcelFormulacion();	
			ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();		
			ArrayList<ArrayList<?>>	lstdatos = generarDatos(ejercicio, numeroCuadro);
			Workbook wb = excel.generateExcel(lstdatos, ejercicio, numeroCuadro);
			wb.write(outByteStream);
			outByteStream.close();
			outArray = Base64.encode(outByteStream.toByteArray());
		}catch(Exception e){
			CLogger.write("1", SInstitucional.class, e);
		}
		return outArray;
	}
	
	private ArrayList<ArrayList<?>> generarDatos(Integer ejercicio, Integer numeroCuadro){
		ArrayList<ArrayList<?>> datos = new ArrayList<>();
		
		try {
			if(numeroCuadro == -1 || numeroCuadro == 0) {
				datos.add(null);
			}else if(numeroCuadro ==-1 || numeroCuadro == 1) {
				datos.add(null);
			}else if(numeroCuadro ==-1 || numeroCuadro == 2) {
				ArrayList<CRecursoEconomico> eRecursoTotal = CRecursoDAO.getRecursosTotal(ejercicio);
				datos.add(eRecursoTotal);
			}else if(numeroCuadro ==-1 || numeroCuadro == 3) {
				ArrayList<CGastoEconomico> eGastoTotal = CGastoDAO.getGastosTotal(ejercicio);
				datos.add(eGastoTotal);
			}else if(numeroCuadro ==-1 || numeroCuadro == 4) {
				ArrayList<CInstitucionalTotal> eInstitucionalTotal = CInstitucionalDAO.getInstitucionalTotal(ejercicio);
				datos.add(eInstitucionalTotal);
			}else if(numeroCuadro ==-1 || numeroCuadro == 5) {
				ArrayList<CInstitucionalTipoGasto> eInstitucionalTipoGasto = CInstitucionalDAO.getInstitucionalTipoGasto(ejercicio);
				datos.add(eInstitucionalTipoGasto);
			}else if(numeroCuadro ==-1 || numeroCuadro == 6) {
				ArrayList<CInstitucionalTipoGastoGrupoGasto> eInstitucionalTipoGastoGrupoGasto = CInstitucionalDAO.getInstitucionalTipoGastoGrupoGasto(ejercicio, 10);
				datos.add(eInstitucionalTipoGastoGrupoGasto);
			}else if(numeroCuadro ==-1 || numeroCuadro == 7) {
				ArrayList<CInstitucionalFinalidad> eInstitucionalFinalidad = CInstitucionalDAO.getInstitucionalFinalidad(ejercicio);
				datos.add(eInstitucionalFinalidad);
			}else if(numeroCuadro ==-1 || numeroCuadro == 8) {
				ArrayList<CInstitucionalTipoGastoRegion> eInstitucionalTipoGastoRegion = CInstitucionalDAO.getInstitucionalTipoGastoRegion(ejercicio, 10);
				datos.add(eInstitucionalTipoGastoRegion);
			}else if(numeroCuadro ==-1 || numeroCuadro == 9) {
				ArrayList<CFinalidadRegion> fRegion = CFinalidadDAO.getFinalidadRegion(ejercicio);
				datos.add(fRegion);
			}else if(numeroCuadro ==-1 || numeroCuadro == 10) {
				ArrayList<CFinalidadEconomico> fRecurso = CFinalidadDAO.getFinalidadRecurso(ejercicio);
				datos.add(fRecurso);
			}			
		}catch(Exception e) {
			CLogger.write("2", SInstitucional.class, e);
		}
		
		return datos;
	} 
}
