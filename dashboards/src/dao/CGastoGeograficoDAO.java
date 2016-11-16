package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import db.utilities.CDatabase;
import pojo.CGastoGeografico;
import utilities.CLogger;
import utilities.Utils;

public class CGastoGeograficoDAO {

	public static ArrayList<CGastoGeografico> getGastoGeograficoPuntos(int mes, int ejercicio, String fuentes,
			String grupos) {
		final ArrayList<CGastoGeografico> geograficos = new ArrayList<CGastoGeografico>();
		if (CDatabase.connect()) {
			try {

				String where = (Utils.isNullOrEmpty(fuentes) ? "fuente is null " : "fuente in (" + fuentes + ") ");
				if (!Utils.isNullOrEmpty(where))
					where += " and ";
				where += (Utils.isNullOrEmpty(grupos) ? "grupo is null " : "grupo in (" + grupos + ") ");

				String query = "select  muni.nombre, gasto.* from( select geografico,  "
						+ "sum(case when mes <= ? then ano_actual else 0 end) gasto "
						+ "from mv_ejecucion_presupuestaria_geografico "
						+ (Utils.isNullOrEmpty(where) ? "" : "where " + where)
						+ " group by geografico ) gasto left join cg_geograficos muni "
						+ "on ( muni.geografico = gasto.geografico and muni.ejercicio = ?)";

				PreparedStatement pstm = CDatabase.getConnection().prepareStatement(query);

				pstm.setInt(1, mes);
				pstm.setInt(2, ejercicio);

				ResultSet results = pstm.executeQuery();
				while (results.next()) {
					CGastoGeografico gg = new CGastoGeografico(mes, ejercicio, results.getInt("geografico"),
							results.getString("nombre"), results.getDouble("gasto"));

					geograficos.add(gg);
				}
				results.close();
				pstm.close();
			} catch (Exception e) {
				CLogger.write("1", CGastoGeograficoDAO.class, e);
			} finally {
				CDatabase.close();
			}
		}
		return geograficos.size() > 0 ? geograficos : null;
	}
}
