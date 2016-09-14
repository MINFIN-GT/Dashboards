package servlets.varios;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
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

import dao.varios.CPrestamoDAO;
import dao.varios.CPrestamoDAO.TIPO;
import pojo.varios.CPrestamo;

/**
 * Servlet implementation class SPrestamo
 */
@WebServlet("/SPrestamos")
public class SPrestamos extends HttpServlet {
	private static final long serialVersionUID = -649446665821218039L;

	class Prestamo {
		Integer correlativo;
		String prestamo_nombre;
		Integer entidad;
		String entidad_nombre;
		Integer unidad_ejecutora;
		String unidad_ejecutora_nombre;
		Integer organismo;
		String organismo_nombre;

		Double asignado;
		Double modificaciones;
		Double vigente;
		Double ejecutado;
		Double porcentaje;
		
		String sigla;
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SPrestamos() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");

		Gson gson = new Gson();

		Type type = new TypeToken<Map<String, String>>() {
		}.getType();

		StringBuilder sb = new StringBuilder();
		BufferedReader br = request.getReader();

		String str;

		while ((str = br.readLine()) != null) {
			sb.append(str);
		}

		Map<String, String> map = gson.fromJson(sb.toString(), type);

		String response_text = "";

		String tipo = map.get("tipo").toString();

		if (tipo.compareToIgnoreCase("PRESTAMOS") == 0) {
			response_text = getAllPrestamos(map);
		} else if (tipo.compareToIgnoreCase("ENTIDADES") == 0) {
			response_text = getPrestamosByEntidad(map);
		} else if (tipo.compareToIgnoreCase("ORGANISMOS") == 0) {
			response_text = getPrestamosByOrganismo(map);
		}

		response.setHeader("Content-Encoding", "gzip");
		response.setCharacterEncoding("UTF-8");
		OutputStream output = response.getOutputStream();
		GZIPOutputStream gz = new GZIPOutputStream(output);
		gz.write(response_text.getBytes("UTF-8"));
		gz.close();
		output.close();
	}

	private String getAllPrestamos(Map<String, String> map) {

		List<CPrestamo> cPrestamos = CPrestamoDAO.getAllPrestamos(TIPO.PRESTAMOS);
		List<Prestamo> prestamos = new ArrayList<Prestamo>();

		for (CPrestamo cPrestamo : cPrestamos) {
			Prestamo prestamo = new Prestamo();

			prestamo.correlativo = cPrestamo.getCorrelativo();
			prestamo.sigla = cPrestamo.getSigla();
			prestamo.prestamo_nombre = cPrestamo.getPrestamo_nombre();
			prestamo.entidad = cPrestamo.getEntidad();
			prestamo.entidad_nombre = cPrestamo.getEntidad_nombre();
			prestamo.unidad_ejecutora = cPrestamo.getUnidad_ejecutora();
			prestamo.unidad_ejecutora_nombre = cPrestamo.getUnidad_ejecutora_nombre();

			prestamo.asignado = cPrestamo.getAsignado().doubleValue();
			prestamo.ejecutado = cPrestamo.getEjecutado().doubleValue();
			prestamo.vigente = cPrestamo.getVigente().doubleValue();

			prestamo.modificaciones = prestamo.vigente.doubleValue() - prestamo.asignado.doubleValue();
			prestamo.porcentaje = prestamo.vigente.doubleValue() > 0
					? prestamo.ejecutado.doubleValue() / prestamo.vigente.doubleValue() * 100 : 0.0;

			prestamos.add(prestamo);
		}

		String response_text = new GsonBuilder().serializeNulls().create().toJson(prestamos);
		response_text = String.join("", "\"prestamos\":", response_text);

		response_text = String.join("", "{\"success\":true,", response_text, "}");

		return response_text;
	}

	private String getPrestamosByEntidad(Map<String, String> map) {

		List<CPrestamo> cPrestamos = CPrestamoDAO.getAllPrestamos(TIPO.ENTIDADES);
		List<Prestamo> prestamos = new ArrayList<Prestamo>();

		for (CPrestamo cPrestamo : cPrestamos) {
			Prestamo prestamo = new Prestamo();

			prestamo.entidad = cPrestamo.getEntidad();
			prestamo.entidad_nombre = cPrestamo.getEntidad_nombre();

			prestamo.asignado = cPrestamo.getAsignado().doubleValue();
			prestamo.ejecutado = cPrestamo.getEjecutado().doubleValue();
			prestamo.vigente = cPrestamo.getVigente().doubleValue();

			prestamo.modificaciones = prestamo.vigente.doubleValue() - prestamo.asignado.doubleValue();
			prestamo.porcentaje = prestamo.vigente.doubleValue() > 0
					? prestamo.ejecutado.doubleValue() / prestamo.vigente.doubleValue() * 100 : 0.0;

			prestamos.add(prestamo);
		}

		String response_text = new GsonBuilder().serializeNulls().create().toJson(prestamos);
		response_text = String.join("", "\"prestamos\":", response_text);

		response_text = String.join("", "{\"success\":true,", response_text, "}");

		return response_text;
	}

	private String getPrestamosByOrganismo(Map<String, String> map) {

		List<CPrestamo> cPrestamos = CPrestamoDAO.getAllPrestamos(TIPO.ORGANISMOS);
		List<Prestamo> prestamos = new ArrayList<Prestamo>();

		for (CPrestamo cPrestamo : cPrestamos) {
			Prestamo prestamo = new Prestamo();

			prestamo.organismo = cPrestamo.getOrganismo();
			prestamo.organismo_nombre = cPrestamo.getOrganismo_nombre();

			prestamo.ejecutado = cPrestamo.getEjecutado().doubleValue();

			prestamos.add(prestamo);
		}

		String response_text = new GsonBuilder().serializeNulls().create().toJson(prestamos);
		response_text = String.join("", "\"prestamos\":", response_text);

		response_text = String.join("", "{\"success\":true,", response_text, "}");

		return response_text;
	}

}
