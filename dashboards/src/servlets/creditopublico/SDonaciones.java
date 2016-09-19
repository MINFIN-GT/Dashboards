package servlets.creditopublico;

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

import dao.varios.CDonacionDAO;
import pojo.varios.CDonacion;

/**
 * Servlet implementation class SPrestamo
 */
@WebServlet("/SDonaciones")
public class SDonaciones extends HttpServlet {
	private static final long serialVersionUID = -649446665821218039L;

	class Donacion {
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
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SDonaciones() {
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

		if (tipo.compareToIgnoreCase("DONACIONES") == 0) {
			response_text = getAllDonaciones(map);
		} else if (tipo.compareToIgnoreCase("ENTIDADES") == 0) {
			response_text = getDonacionesByEntidad(map);
		} else if (tipo.compareToIgnoreCase("ORGANISMOS") == 0) {
			response_text = getDonacionesByOrganismo(map);
		}

		response.setHeader("Content-Encoding", "gzip");
		response.setCharacterEncoding("UTF-8");
		OutputStream output = response.getOutputStream();
		GZIPOutputStream gz = new GZIPOutputStream(output);
		gz.write(response_text.getBytes("UTF-8"));
		gz.close();
		output.close();
	}

	private String getAllDonaciones(Map<String, String> map) {

		List<CDonacion> cDonaciones = CDonacionDAO.getDonaciones();
		List<Donacion> donaciones = new ArrayList<Donacion>();

		for (CDonacion cPrestamo : cDonaciones) {
			Donacion donacion = new Donacion();

			donacion.correlativo = cPrestamo.getCorrelativo();
			donacion.prestamo_nombre = cPrestamo.getPrestamo_nombre();
			donacion.entidad = cPrestamo.getEntidad();
			donacion.entidad_nombre = cPrestamo.getEntidad_nombre();
			donacion.unidad_ejecutora = cPrestamo.getUnidad_ejecutora();
			donacion.unidad_ejecutora_nombre = cPrestamo.getUnidad_ejecutora_nombre();

			donacion.asignado = cPrestamo.getAsignado().doubleValue();
			donacion.ejecutado = cPrestamo.getEjecutado().doubleValue();
			donacion.vigente = cPrestamo.getVigente().doubleValue();

			donacion.modificaciones = donacion.vigente.doubleValue() - donacion.asignado.doubleValue();
			donacion.porcentaje = donacion.vigente.doubleValue() > 0
					? donacion.ejecutado.doubleValue() / donacion.vigente.doubleValue() * 100 : 0.0;

			donaciones.add(donacion);
		}

		String response_text = new GsonBuilder().serializeNulls().create().toJson(donaciones);
		response_text = String.join("", "\"donaciones\":", response_text);

		response_text = String.join("", "{\"success\":true,", response_text, "}");

		return response_text;
	}

	private String getDonacionesByEntidad(Map<String, String> map) {

		List<CDonacion> cDonaciones = CDonacionDAO.getDonacionesByEntidad();
		List<Donacion> donaciones = new ArrayList<Donacion>();

		for (CDonacion cPrestamo : cDonaciones) {
			Donacion donacion = new Donacion();

			donacion.entidad = cPrestamo.getEntidad();
			donacion.entidad_nombre = cPrestamo.getEntidad_nombre();

			donacion.asignado = cPrestamo.getAsignado().doubleValue();
			donacion.ejecutado = cPrestamo.getEjecutado().doubleValue();
			donacion.vigente = cPrestamo.getVigente().doubleValue();

			donacion.modificaciones = donacion.vigente.doubleValue() - donacion.asignado.doubleValue();
			donacion.porcentaje = donacion.vigente.doubleValue() > 0
					? donacion.ejecutado.doubleValue() / donacion.vigente.doubleValue() * 100 : 0.0;

			donaciones.add(donacion);
		}

		String response_text = new GsonBuilder().serializeNulls().create().toJson(donaciones);
		response_text = String.join("", "\"donaciones\":", response_text);

		response_text = String.join("", "{\"success\":true,", response_text, "}");

		return response_text;
	}

	private String getDonacionesByOrganismo(Map<String, String> map) {

		List<CDonacion> cDonaciones = CDonacionDAO.getDonacionesByOrganismo();
		List<Donacion> donaciones = new ArrayList<Donacion>();

		for (CDonacion cPrestamo : cDonaciones) {
			Donacion donacion = new Donacion();

			donacion.organismo = cPrestamo.getOrganismo();
			donacion.organismo_nombre = cPrestamo.getOrganismo_nombre();

			donacion.ejecutado = cPrestamo.getEjecutado().doubleValue();

			donaciones.add(donacion);
		}

		String response_text = new GsonBuilder().serializeNulls().create().toJson(donaciones);
		response_text = String.join("", "\"donaciones\":", response_text);

		response_text = String.join("", "{\"success\":true,", response_text, "}");

		return response_text;
	}

}
