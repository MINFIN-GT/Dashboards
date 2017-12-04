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


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import dao.CEventoGCDAO;
import pojo.CEventoGC;
import pojo.CModalidadGC;


/**
 * Servlet implementation class SEjecucion
 */
@WebServlet("/SEventoGC")
public class SEventoGC extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	class stentidad{
		int entidad;
    	String nombre;
    	String nombre_corto;
    	int[] ano1;
    	int[] ano2;
    	int[] ano_actual;
    }
	
	class stmodalidad{
		String nombre;
		int eventos;
	}
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SEventoGC() {
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
		int ejercicio = map.get("ejercicio")!=null ? Integer.parseInt(map.get("ejercicio")) : 0;
		int mes = map.get("mes")!=null ? Integer.parseInt(map.get("mes")) : 0;
		if(action.compareTo("entidadesData")==0){
			ArrayList<stentidad> stentidades=new ArrayList<stentidad>();
			ArrayList<CEventoGC> entidades = CEventoGCDAO.getEntidadesEventos(ejercicio);
			
			if(entidades!=null && entidades.size()>0){
				for(CEventoGC centidad : entidades){
					stentidad sttemp = new stentidad();
					sttemp.entidad = centidad.getEntidad_gc();
					sttemp.nombre = centidad.getNombre();
					sttemp.nombre_corto = centidad.getNombre_corto();
					sttemp.ano1 = centidad.getAno_1();
					sttemp.ano2 = centidad.getAno_2();
					sttemp.ano_actual = centidad.getAno_actual();
					stentidades.add(sttemp);
				}
				
				response.setHeader("Content-Encoding", "gzip");
				response.setCharacterEncoding("UTF-8");
				String response_text=new GsonBuilder().serializeNulls().create().toJson(stentidades);
		        response_text = String.join("", "\"entidades\":",response_text);
			    response_text = String.join("", "{\"success\":true,", response_text,"}");
			        
			    OutputStream output = response.getOutputStream();
				GZIPOutputStream gz = new GZIPOutputStream(output);
			    gz.write(response_text.getBytes("UTF-8"));
		        gz.close();
		        output.close();
				
			}
		}
		else if(action.compareTo("modalidadesData")==0){
			ArrayList<stmodalidad> stmodalidades=new ArrayList<stmodalidad>();
			ArrayList<CModalidadGC> modalidades = CEventoGCDAO.getModalidadesEventos(ejercicio,mes);
			
			if(modalidades!=null && modalidades.size()>0){
				for(CModalidadGC cmodalidad : modalidades){
					stmodalidad sttemp = new stmodalidad();
					sttemp.nombre = cmodalidad.getNombre();
					sttemp.eventos = cmodalidad.getTotal();
					stmodalidades.add(sttemp);
				}
				
				response.setHeader("Content-Encoding", "gzip");
				response.setCharacterEncoding("UTF-8");
				String response_text=new GsonBuilder().serializeNulls().create().toJson(stmodalidades);
		        response_text = String.join("", "\"modalidades\":",response_text);
			    response_text = String.join("", "{\"success\":true,", response_text,"}");
			        
			    OutputStream output = response.getOutputStream();
				GZIPOutputStream gz = new GZIPOutputStream(output);
			    gz.write(response_text.getBytes("UTF-8"));
		        gz.close();
		        output.close();
				
			}
		}
		else if(action.compareTo("estadosData")==0){
			ArrayList<stmodalidad> stmodalidades=new ArrayList<stmodalidad>();
			ArrayList<CModalidadGC> estados = CEventoGCDAO.getEstadosEventos(ejercicio,mes);
			
			if(estados!=null && estados.size()>0){
				for(CModalidadGC cestado : estados){
					stmodalidad sttemp = new stmodalidad();
					sttemp.nombre = cestado.getNombre();
					sttemp.eventos = cestado.getTotal();
					stmodalidades.add(sttemp);
				}
				
				response.setHeader("Content-Encoding", "gzip");
				response.setCharacterEncoding("UTF-8");
				String response_text=new GsonBuilder().serializeNulls().create().toJson(stmodalidades);
		        response_text = String.join("", "\"estados\":",response_text);
			    response_text = String.join("", "{\"success\":true,", response_text,"}");
			        
			    OutputStream output = response.getOutputStream();
				GZIPOutputStream gz = new GZIPOutputStream(output);
			    gz.write(response_text.getBytes("UTF-8"));
		        gz.close();
		        output.close();
				
			}
		}
	}

}
