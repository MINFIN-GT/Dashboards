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

import dao.transparencia.CCompraDAO;
import pojo.transparencia.CCompra;
import pojo.transparencia.CEntidadCompra;
import shiro.utilities.CShiro;

/**
 * Servlet implementation class STransparenciaCompras
 */
@WebServlet("/STransparenciaCompras")
public class STransparenciaCompras extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public STransparenciaCompras() {
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
		response.getWriter().append("Served at: ").append(request.getContextPath());
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
			ArrayList<CCompra> compras = CCompraDAO.getCompras(subprograma);
			response_text = new GsonBuilder().serializeNulls().create().toJson(compras);		
			response_text = String.join("", "\"compras\":", response_text);
			response_text = String.join("", "{\"success\":true,", response_text, "}");
		}else if(action.compareTo("getlist_entidades")==0){
			ArrayList<CEntidadCompra> compras = CCompraDAO.getComprasEntidades(subprograma);
			response_text = new GsonBuilder().serializeNulls().create().toJson(compras);		
			response_text = String.join("", "\"compras\":", response_text);
			response_text = String.join("", "{\"success\":true,", response_text, "}");
		}else if(action.compareTo("getlist_por_entidad")==0) {
			int entidad = map.get("entidad")!=null ? Integer.parseInt(map.get("entidad")) : 0;
			ArrayList<CCompra> compras = CCompraDAO.getComprasPorEntidad(subprograma, entidad);
			response_text = new GsonBuilder().serializeNulls().create().toJson(compras);		
			response_text = String.join("", "\"compras\":", response_text);
			response_text = String.join("", "{\"success\":true,", response_text, "}");
		}
		else if (action.compareTo("add") == 0) {
			int id_compra = map.get("idCompra")!=null ? Integer.parseInt(map.get("idCompra")) : 0;
			boolean existe_nog=CCompraDAO.getCompra(id_compra);
			if(existe_nog){
			CCompra compra = new CCompra(map.get("tipoCompra"), map.get("idCompra"),94, subprograma, CShiro.getIdUser());
				if (CCompraDAO.crearCompra(compra))
					response_text = String.join("", "{\"success\":true, \"result\":\"creada\"}");
				else
					response_text = String.join("", "{\"success\":false, \"result\":\"no creada\"}");
			}
			else{
				response_text = "{\"success\": false, \"result\":\"El nog indicado no existe\"}";
			}
		}else if (action.compareTo("delete") == 0) {
			if (CCompraDAO.deleteCompra(map.get("idCompra"), map.get("tipoCompra"),94,subprograma))
				response_text = String.join("", "{\"success\":true, \"result\":\"borrada\"}");
			else
				response_text = String.join("", "{\"success\":false, \"result\":\"no borrada\"}");
		}			
		gz.write(response_text.getBytes("UTF-8"));
		gz.close();
		output.close();
	}

}
