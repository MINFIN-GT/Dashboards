package servlets.formulacion;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.codec.Base64;

import com.google.gson.Gson;
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
 * Servlet implementation class SCuadrosExportar
 */
@WebServlet("/SCuadrosExportar")
public class SCuadrosExportar extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SCuadrosExportar() {
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
		int ejercicio = Utils.String2Int(map.get("ejercicio"), 0);
		
		if(action.equals("exportarExcel")) {
			try {
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
			}catch(Exception e) {
				CLogger.write("1", SCuadrosExportar.class, e);
			}
			
		}
	}
	
	private byte[] exportarExcel(Integer ejercicio, Integer numeroCuadro) throws IOException{
		byte [] outArray = null;
		Workbook wb=null;
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		try{	
			CExcelFormulacion excel= new CExcelFormulacion();	
			outByteStream = new ByteArrayOutputStream(10204);		
			ArrayList<ArrayList<?>>	lstdatos = generarDatos(ejercicio, numeroCuadro);
			wb = excel.generateExcel(lstdatos, ejercicio, numeroCuadro);
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
			/*if(numeroCuadro == -1 || numeroCuadro == 1) {
				datos.add(null);
			}
			if(numeroCuadro ==-1 || numeroCuadro == 2) {
				datos.add(null);
			}
			if(numeroCuadro ==-1 || numeroCuadro == 3) {
				ArrayList<CRecursoEconomico> eRecursoTotal = CRecursoDAO.getRecursosTotal(ejercicio);
				datos.add(eRecursoTotal);
			}*/
			if(numeroCuadro ==-1 || numeroCuadro == 4) {
				ArrayList<CGastoEconomico> eGastoTotal = CGastoDAO.getGastosTotal(ejercicio);
				datos.add(eGastoTotal);
			}
			if(numeroCuadro ==-1 || numeroCuadro == 5) {
				ArrayList<CInstitucionalTotal> eInstitucionalTotal = CInstitucionalDAO.getInstitucionalTotal(ejercicio);
				datos.add(eInstitucionalTotal);
			}
			if(numeroCuadro ==-1 || numeroCuadro == 6) {
				ArrayList<CInstitucionalTipoGasto> eInstitucionalTipoGasto = CInstitucionalDAO.getInstitucionalTipoGasto(ejercicio);
				datos.add(eInstitucionalTipoGasto);
			}
			if(numeroCuadro ==-1 || numeroCuadro == 7) {				
				ArrayList<CInstitucionalTipoGastoGrupoGasto> eInstitucionalTipoGastoGrupoGasto = CInstitucionalDAO.getInstitucionalTipoGastoGrupoGasto(ejercicio, 10);
				datos.add(eInstitucionalTipoGastoGrupoGasto);
				eInstitucionalTipoGastoGrupoGasto = CInstitucionalDAO.getInstitucionalTipoGastoGrupoGasto(ejercicio, 20);
				datos.add(eInstitucionalTipoGastoGrupoGasto);
				eInstitucionalTipoGastoGrupoGasto = CInstitucionalDAO.getInstitucionalTipoGastoGrupoGasto(ejercicio, 30);
				datos.add(eInstitucionalTipoGastoGrupoGasto);
			}
			if(numeroCuadro ==-1 || numeroCuadro == 8) {
				ArrayList<CInstitucionalFinalidad> eInstitucionalFinalidad = CInstitucionalDAO.getInstitucionalFinalidad(ejercicio);
				datos.add(eInstitucionalFinalidad);
			}
			if(numeroCuadro ==-1 || numeroCuadro == 9) {
				ArrayList<CFinalidadEconomico> finalidades = CFinalidadDAO.getFinalidadRecurso(ejercicio);
				datos.add(finalidades);
			}
			if(numeroCuadro ==-1 || numeroCuadro == 10) {
				ArrayList<CInstitucionalTipoGastoRegion> entidades = CInstitucionalDAO.getInstitucionalTipoGastoRegion(ejercicio,10);
				datos.add(entidades);
				entidades = CInstitucionalDAO.getInstitucionalTipoGastoRegion(ejercicio,20);
				datos.add(entidades);
				entidades = CInstitucionalDAO.getInstitucionalTipoGastoRegion(ejercicio,30);
				datos.add(entidades);
			}
			if(numeroCuadro ==-1 || numeroCuadro == 11) {
				ArrayList<CFinalidadRegion> finalidades = CFinalidadDAO.getFinalidadRegion(ejercicio);
				datos.add(finalidades);
			}		
		}catch(Exception e) {
			CLogger.write("2", SInstitucional.class, e);
		}
		
		return datos;
	} 

}
