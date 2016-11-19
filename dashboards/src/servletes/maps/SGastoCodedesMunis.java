package servletes.maps;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.CGastoGeograficoDAO;
import pojo.CGastoGeografico;
import utilities.Utils;

/**
 * Servlet implementation class SGastoGeografico
 */
@WebServlet("/SGastoCodedesMunis")
public class SGastoCodedesMunis extends HttpServlet {
	private static final long serialVersionUID = 1L;

	class stgeografico {
		int geografico;
		String nombre;
		double gasto;
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SGastoCodedesMunis() {
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

		if (action.compareTo("gastogeografico") == 0) {
			int mes = Utils.String2Int(map.get("mes"), 1);
			int ejercicio = Utils.String2Int(map.get("ejercicio"), 1);

			ArrayList<CGastoGeografico> geograficos = CGastoGeograficoDAO.getGastosCodedesMunis(mes, ejercicio,
					map.get("renglon"));

			if (geograficos != null && geograficos.size() > 0) {

				ArrayList<stgeografico> stgeograficos = new ArrayList<stgeografico>();
				Double total = 0.0;

				for (CGastoGeografico geografico : geograficos) {

					stgeografico sttemp = new stgeografico();
					sttemp.geografico = geografico.getGeografico();
					sttemp.nombre = geografico.getNombreGeografico();
					sttemp.gasto = geografico.getGasto().doubleValue();

					stgeograficos.add(sttemp);

					total += sttemp.gasto;
				}

				stgeografico ref = new stgeografico();
				ref.geografico = 0;
				ref.nombre = "General";
				ref.gasto = total;
				stgeograficos.add(0, ref);

				String response_text = Utils.getJSonString("geograficos", stgeograficos);

				Utils.writeJSon(response, response_text);
			}
		}
	}

}
