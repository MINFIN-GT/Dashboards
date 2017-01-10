package dao.maps;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import db.utilities.CDatabase;
import pojo.maps.CGastoGeografico;
import utilities.CLogger;
import utilities.Utils;

public class CGastoGeograficoDAO {

	public static ArrayList<CGastoGeografico> getGastoGeneral(int mes, int ejercicio, String fuentes, String grupos) {
		final ArrayList<CGastoGeografico> geograficos = new ArrayList<CGastoGeografico>();
		if (CDatabase.connect()) {
			try {

				String where = (Utils.isNullOrEmpty(fuentes) ? "fuente is null " : "fuente in (" + fuentes + ") ");
				if (!Utils.isNullOrEmpty(where))
					where += " and ";
				where += (Utils.isNullOrEmpty(grupos) ? "grupo is null " : "grupo in (" + grupos + ") ");

				String query = "select  geo.nombre, muni.poblacion, gasto.* from( select geografico,  "
						+ "sum(case when mes <= ? then ano_actual else 0 end) gasto "
						+ "from mv_ejecucion_presupuestaria_geografico "
						+ "where ejercicio = ? " + ((Utils.isNullOrEmpty(where) ? "" : "and  " + where)) + " group by geografico ) gasto "
						+ "left join municipio_demografia muni " + "on ( muni.codigo_municipio = gasto.geografico ) "
						+ "left join cg_geograficos geo "
						+ "on ( geo.geografico = gasto.geografico and geo.ejercicio = ? )";

				PreparedStatement pstm = CDatabase.getConnection().prepareStatement(query);

				pstm.setInt(1, mes);
				pstm.setInt(2, ejercicio);

				ResultSet results = pstm.executeQuery();
				while (results.next()) {
					CGastoGeografico gg = new CGastoGeografico(mes, ejercicio, results.getInt("geografico"),
							results.getString("nombre"), results.getDouble("gasto"), results.getDouble("poblacion"));

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

	public static ArrayList<CGastoGeografico> getGastosCodedesMunis(int mes, int ejercicio, String renglones) {
		final ArrayList<CGastoGeografico> geograficos = new ArrayList<CGastoGeografico>();
		if (CDatabase.connect()) {
			try {

				String query = "select  geo.nombre, muni.poblacion, gasto.* from( select geografico,  "
						+ "sum(case when mes <= ? then ano_actual else 0 end) gasto "
						+ "from mv_ejecucion_presupuestaria_geografico where ejercicio = ? and renglon in (" + renglones + ") "
						+ " group by geografico ) gasto left join municipio_demografia muni "
						+ "on ( muni.codigo_municipio = gasto.geografico ) " + "left join cg_geograficos geo "
						+ "on ( geo.geografico = gasto.geografico and geo.ejercicio = ? )";

				PreparedStatement pstm = CDatabase.getConnection().prepareStatement(query);

				pstm.setInt(1, mes);
				pstm.setInt(2, ejercicio);
				pstm.setInt(3, ejercicio);

				ResultSet results = pstm.executeQuery();
				while (results.next()) {
					CGastoGeografico gg = new CGastoGeografico(mes, ejercicio, results.getInt("geografico"),
							results.getString("nombre"), results.getDouble("gasto"), results.getDouble("poblacion"));

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