package servlets.transparencia;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
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

import dao.transparencia.CActividadDAO;
import dao.transparencia.CCompraDAO;
import dao.transparencia.CDocumentoDAO;
import dao.transparencia.CDonacionDAO;
import dao.transparencia.CEjecucionFFDAO;
import dao.transparencia.CEstadoCalamidadDAO;
import pojo.transparencia.CEstadoCalamidad;


/**
 * Servlet implementation class STransparenciaVentanas
 */
@WebServlet("/STransparenciaVentanas")
public class STransparenciaVentanas extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	
	class stresults{
    	int actividades;
    	int documentos;
    	int compras;
    	int donaciones;
    	double ejecucion_financiera;
    	double ejecucion_fisica;
    	String titulo;
    	String latitude;
    	String longitude;
    	String tipo;
    }
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public STransparenciaVentanas() {
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
		response.setHeader("Content-Encoding", "gzip");
		response.setCharacterEncoding("UTF-8");
		
		Gson gson = new Gson();
		Type type = new TypeToken<Map<String, String>>() {
		}.getType();
		StringBuilder sb = new StringBuilder();
		BufferedReader br = request.getReader();
		String str;
		while ((str = br.readLine()) != null) {
			sb.append(str);
		};
		Map<String, String> map = gson.fromJson(sb.toString(), type);
		
		int subprograma = Integer.parseInt(map.get("subprograma"));
		
		OutputStream output = response.getOutputStream();
		GZIPOutputStream gz = new GZIPOutputStream(output);
		
		stresults results = new stresults();
		results.actividades = CActividadDAO.numActividades(subprograma);
		results.documentos = CDocumentoDAO.numDocumentos(subprograma);	
		results.compras = CCompraDAO.numCompras(subprograma);
		results.donaciones = CDonacionDAO.numDonaciones(subprograma);
		results.ejecucion_financiera = CEjecucionFFDAO.ejecucionFinanciera(94,subprograma);
		results.ejecucion_fisica = CEjecucionFFDAO.ejecucionFisica(94,subprograma);
		CEstadoCalamidad estadoc = CEstadoCalamidadDAO.getEstadoCalamidad(subprograma);
		if(estadoc!=null){
			results.titulo = estadoc.getNombre();
			results.latitude = estadoc.getLatitude();
			results.longitude = estadoc.getLongitude();
			switch(estadoc.getTipo_estado_calamidad()){
				case 1: results.tipo = "Calamidad"; break;
				case 2: results.tipo = "Sitio"; break;
			}
		}			
					
		String response_text=new GsonBuilder().serializeNulls().create().toJson(results);
		response_text = String.join("", "\"results\":",response_text);
			            
		response_text = String.join("", "{\"success\":true,", response_text,"}");
			            
		gz.write(response_text.getBytes("UTF-8"));
		gz.close();
		output.close();	
		
	}

}
