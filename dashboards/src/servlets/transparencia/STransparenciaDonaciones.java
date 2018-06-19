package servlets.transparencia;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gdata.data.DateTime;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import dao.transparencia.CDonacionDAO;
import pojo.transparencia.CDonacion;
import shiro.utilities.CShiro;

/**
 * Servlet implementation class STransparenciaCompras
 */
@WebServlet("/STransparenciaDonaciones")
public class STransparenciaDonaciones extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public STransparenciaDonaciones() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response,)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setHeader("Content-Encoding", "gzip");
		response.setCharacterEncoding("UTF-8");

		OutputStream output = response.getOutputStream();
		GZIPOutputStream gz = new GZIPOutputStream(output);
		String response_text = "";

		Gson gson = new Gson();
		Type type = new TypeToken<Map<String, String>>() {
		}.getType();
		StringBuilder sb = new StringBuilder();
		BufferedReader br = request.getReader();
		String str;
		while ((str = br.readLine()) != null) {
			sb.append(str);
		}
		;
		Map<String, String> map = gson.fromJson(sb.toString(), type);
		String action = map.get("action");
		int subprograma = map.get("subprograma")!=null ? Integer.parseInt(map.get("subprograma")) : 0;
		if (action.compareTo("getlist") == 0) {
			ArrayList<CDonacion> donaciones = CDonacionDAO.getDonaciones(subprograma);
			response_text = new GsonBuilder().serializeNulls().create().toJson(donaciones);		
			response_text = String.join("", "\"donaciones\":", response_text);
			response_text = String.join("", "{\"success\":true,", response_text, "}");
		}else if (action.compareTo("add") == 0) {
			try {
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
				int id_donacion = map.get("idDonacion")!=null ? Integer.parseInt(map.get("idDonacion")) : 0;
				CDonacion donacion = new CDonacion(id_donacion, 94, subprograma, map.get("donante"), map.get("procedencia"), 
					map.get("metodo_acreditamiento"), new Timestamp(formatter.parse(map.get("fecha_ingreso")).getTime()), 
					Double.parseDouble(map.get("monto_d")), Double.parseDouble(map.get("monto_q")), 
					map.get("estado"), map.get("destino"), CShiro.getIdUser(), 
					new Timestamp(DateTime.now().getValue()), null, null); 
				if (CDonacionDAO.crearDonacion(donacion))
					response_text = String.join("", "{\"success\":true, \"result\":\"creada\"}");
				else
					response_text = String.join("", "{\"success\":false, \"result\":\"no creada\"}");
			}
			catch(Exception e) {
				response_text = String.join("", "{\"success\":false, \"result\":\"no creada\"}");
			}
		}else if (action.compareTo("delete") == 0) {
			if (CDonacionDAO.deleteDonacion(Integer.parseInt(map.get("idDonacion"))))
				response_text = String.join("", "{\"success\":true, \"result\":\"borrada\"}");
			else
				response_text = String.join("", "{\"success\":false, \"result\":\"no borrada\"}");
		}			
		gz.write(response_text.getBytes("UTF-8"));
		gz.close();
		output.close();
	}

}
