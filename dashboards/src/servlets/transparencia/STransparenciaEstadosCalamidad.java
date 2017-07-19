package servlets.transparencia;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
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

import dao.transparencia.CEjecucionFFDAO;
import dao.transparencia.CEstadoCalamidadDAO;
import pojo.transparencia.CEstadoCalamidad;

/**
 * Servlet implementation class STransparenciaEstadosCalamidad
 */
@WebServlet("/STransparenciaEstadosCalamidad")
public class STransparenciaEstadosCalamidad extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	class stestadocalamidad{
    	int ejercicio;
    	int programa;
    	int subprograma;
    	String fecha_declaracion;
    	String decreto;
    	String nombre;
    	String link;
    	String titulo;
    	String latitude;
    	String longitude;
    	String tipo;
    	String ejecucion;
    }
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public STransparenciaEstadosCalamidad() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Content-Encoding", "gzip");
		response.setCharacterEncoding("UTF-8");
		
		OutputStream output = response.getOutputStream();
		GZIPOutputStream gz = new GZIPOutputStream(output);
		String response_text = "";
		
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
		if(action.compareTo("getlist")==0){
		
			ArrayList<CEstadoCalamidad> estados = CEstadoCalamidadDAO.getEstadosCalamidad();
			ArrayList<stestadocalamidad> stestados= new ArrayList<stestadocalamidad>();
			DecimalFormat formatter = new DecimalFormat("#,##0.00");
			for(CEstadoCalamidad estado : estados){
				stestadocalamidad temp = new stestadocalamidad();
				temp.ejercicio = estado.getEjercicio();
				temp.programa = estado.getPrograma();
				temp.subprograma = estado.getSuprograma();
				temp.nombre = estado.getNombre();
				temp.decreto = estado.getDecreto();
				temp.fecha_declaracion = estado.getFecha_declaracion();
				temp.link = estado.getLink();
				temp.titulo = estado.getNombre();
				temp.latitude = estado.getLatitude();
				temp.longitude = estado.getLongitude();
				temp.ejecucion = formatter.format(CEjecucionFFDAO.ejecucionFinancieraMonto(estado.getPrograma(), estado.getSuprograma()));
				switch(estado.getTipo_estado_calamidad()){
					case 1: temp.tipo = "Calamidad"; break;
					case 2: temp.tipo = "Sitio"; break;
				}
				stestados.add(temp);
			}
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(stestados);
	        response_text = String.join("", "\"estados_calamidad\":",response_text);	            
	        
		}
		else if(action.compareTo("getEstado")==0){
			int subprograma = Integer.parseInt(map.get("subprograma"));
			CEstadoCalamidad estadoc = CEstadoCalamidadDAO.getEstadoCalamidad(subprograma);
			stestadocalamidad temp = new stestadocalamidad();
			if(estadoc!=null){
				temp.ejercicio = estadoc.getEjercicio();
				temp.programa = estadoc.getPrograma();
				temp.subprograma = estadoc.getSuprograma();
				temp.nombre = estadoc.getNombre();
				temp.decreto = estadoc.getDecreto();
				temp.fecha_declaracion = estadoc.getFecha_declaracion();
				temp.link = estadoc.getLink();
				temp.titulo = estadoc.getNombre();
				temp.latitude = estadoc.getLatitude();
				temp.longitude = estadoc.getLongitude();
				switch(estadoc.getTipo_estado_calamidad()){
					case 1: temp.tipo = "Calamidad"; break;
					case 2: temp.tipo = "Sitio"; break;
				}
			}			
						
			response_text=new GsonBuilder().serializeNulls().create().toJson(temp);
			response_text = String.join("", "\"results\":",response_text);
			
		}
		response_text = String.join("", "{\"success\":true,", response_text,"}");
		gz.write(response_text.getBytes("UTF-8"));
		gz.close();
		output.close();	
	}

}
