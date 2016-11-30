package servletes.maps;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.maps.CGastoGeograficoDAO;
import dao.maps.CGastoMunicipioDAO;
import pojo.maps.CGastoGeografico;
import pojo.maps.CGastoMunicipio;
import utilities.Utils;

/**
 * Servlet implementation class SGastoGeografico
 */
@WebServlet("/SGastoGeneral")
public class SGastoGeneral extends HttpServlet {
	private static final long serialVersionUID = 1L;

	class stgeografico {
		int geografico;
		String nombre;
		double gasto;
		double poblacion;
		double gastoPerCapita;
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SGastoGeneral() {
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

		Map<String, String> map = Utils.getParams(request);

		String action = map.get("action");

		int mes = Utils.String2Int(map.get("mes"), 1);
		int ejercicio = Utils.String2Int(map.get("ejercicio"), 1);

		if (action.compareTo("gastogeografico") == 0) {
			ArrayList<CGastoGeografico> geograficos = CGastoGeograficoDAO.getGastoGeneral(mes, ejercicio,
					map.get("fuentes"), map.get("grupos"));

			if (geograficos != null && geograficos.size() > 0) {

				ArrayList<stgeografico> stgeograficos = new ArrayList<stgeografico>();
				Double total = 0.0;
				Double totalPerCapita = 0.0;

				for (CGastoGeografico geografico : geograficos) {

					stgeografico sttemp = new stgeografico();
					sttemp.geografico = geografico.getGeografico();
					sttemp.nombre = geografico.getNombreGeografico();
					sttemp.gasto = geografico.getGasto().doubleValue();
					sttemp.poblacion = geografico.getPoblacion().doubleValue();
					sttemp.gastoPerCapita = sttemp.poblacion > 0 ? sttemp.gasto / sttemp.poblacion : 0.0;

					stgeograficos.add(sttemp);

					total += sttemp.gasto;
					totalPerCapita += sttemp.gastoPerCapita;
				}

				stgeografico ref = new stgeografico();
				ref.geografico = 0;
				ref.nombre = "General";
				ref.gasto = total;
				ref.gastoPerCapita = totalPerCapita;
				stgeograficos.add(0, ref);

				String response_text = Utils.getJSonString("geograficos", stgeograficos);

				Utils.writeJSon(response, response_text);
			}
		} else if (action.compareTo("gastomunicipio") == 0) {
			int geografico = Utils.String2Int(map.get("geografico"), 0);
			int nivel = Utils.String2Int(map.get("nivel"), 0);
			long entidad = Utils.String2Long(map.get("entidad"), 0);
			int unidad_ejecutora = Utils.String2Int(map.get("unidad_ejecutora"), 0);
			int programa = Utils.String2Int(map.get("programa"), 0);
			int subprograma = Utils.String2Int(map.get("subprograma"), 0);
			int proyecto = Utils.String2Int(map.get("proyecto"), 0);

			List<CGastoMunicipio> gastoMunicipios = CGastoMunicipioDAO.getGastosMunicipio(mes, ejercicio, geografico,
					nivel, map.get("fuentes"), map.get("grupos"), entidad, unidad_ejecutora, programa, subprograma,
					proyecto);

			String response_text = Utils.getJSonString("gasto", gastoMunicipios);

			Utils.writeJSon(response, response_text);
		}
	}

}
