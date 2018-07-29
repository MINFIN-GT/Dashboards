package servlets.transparencia;

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

import dao.transparencia.CEjecucionFFDAO;
import pojo.transparencia.CEjecucionFF;

/**
 * Servlet implementation class SEjecucionFF
 */
@WebServlet("/SEjecucionFOtros")
public class SEjecucionFOtros extends HttpServlet {
	private static final long serialVersionUID = 1L;
		
	class stData{
			Integer codigo;
	    	String nombre;
	     	double ejecutado;
	    	double compromiso;
	    }
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SEjecucionFOtros() {
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
		Integer entidad = map.get("entidad")!=null ? Integer.parseInt(map.get("entidad")) : null;
		Integer unidad_ejecutora = map.get("unidad_ejecutora")!=null ? Integer.parseInt(map.get("unidad_ejecutora")) : null;
		Integer programa = map.get("programa")!=null ? Integer.parseInt(map.get("programa")) : null;
		Integer subprograma = map.get("subprograma")!=null ? Integer.parseInt(map.get("subprograma")) : null;
		Integer proyecto = map.get("proyecto")!=null ? Integer.parseInt(map.get("proyecto")) : null;
		Integer actividad = map.get("actividad")!=null ? Integer.parseInt(map.get("actividad")) : null;
		Integer nivel = map.get("level")!=null ? Integer.parseInt(map.get("level")) : null;
		ArrayList<stData> stDatos=new ArrayList<stData>();
		ArrayList<CEjecucionFF> valores=null; 
		valores = CEjecucionFFDAO.getEntidadesOtrosEjecucion(nivel,entidad,unidad_ejecutora,programa,subprograma,proyecto,actividad);
		if(valores!=null && valores.size()>0){
			for(CEjecucionFF cvalor : valores){
				stData sttemp = new stData();
				sttemp.codigo = cvalor.getCodigo();
				sttemp.nombre = cvalor.getNombre();
				sttemp.ejecutado =  cvalor.getEjecutado();
				sttemp.compromiso = cvalor.getCompromiso();
				stDatos.add(sttemp);
			}
			response.setHeader("Content-Encoding", "gzip");
			response.setCharacterEncoding("UTF-8");
			String response_text=new GsonBuilder().serializeNulls().create().toJson(stDatos);
            response_text = String.join("", "\"datos\":",response_text);    
	        response_text = String.join("", "{\"success\":true,", response_text,"}");	        
	        OutputStream output = response.getOutputStream();
			GZIPOutputStream gz = new GZIPOutputStream(output);
	        gz.write(response_text.getBytes("UTF-8"));
            gz.close();
            output.close();
		}		
	}
	
	
}
