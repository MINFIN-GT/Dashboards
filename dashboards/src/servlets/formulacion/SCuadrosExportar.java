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
import dao.formulacion.CuadroExportarDAO;
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
			ArrayList<ArrayList<?>>	lstdatos = CuadroExportarDAO.generarDatos(ejercicio, numeroCuadro);
			wb = excel.generateExcel(lstdatos, ejercicio, numeroCuadro);
			wb.write(outByteStream);
			outByteStream.close();
			outArray = Base64.encode(outByteStream.toByteArray());
		}catch(Exception e){
			CLogger.write("1", SInstitucional.class, e);
		}
		return outArray;
	}
}
