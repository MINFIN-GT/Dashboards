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

				String query = "select  geo.nombre, muni.poblacion, geo.geografico, gasto.gasto from( select geografico,  "
						+ "sum(case when mes <= ? then ano_actual else 0 end) gasto "
						+ "from mv_ejecucion_presupuestaria_geografico "
						+ "where ejercicio = ? "
						+ (!Utils.isNullOrEmpty(fuentes) ? "and fuente in ("+fuentes+") " : "")
						+ (!Utils.isNullOrEmpty(grupos) ? "and grupo in ("+grupos+") " : "")
						+ "group by geografico ) gasto "
						+ "right outer join cg_geograficos geo on ( geo.geografico = gasto.geografico) "
						+ "left outer join municipio_demografia muni on ( muni.codigo_municipio = gasto.geografico ) "
						+ "where geo.ejercicio = ? ";

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

	public static ArrayList<CGastoGeografico> getGastosCodedesMunis(int mes, int ejercicio, String renglones) {
		final ArrayList<CGastoGeografico> geograficos = new ArrayList<CGastoGeografico>();
		if (CDatabase.connect()) {
			try {

				String query = "select  geo.nombre, muni.poblacion, geo.geografico, gasto.gasto from( select geografico,  "
						+ "sum(case when mes <= ? then ano_actual else 0 end) gasto "
						+ "from mv_ejecucion_presupuestaria_geografico where ejercicio = ? and renglon in (" + renglones + ") "
						+ " group by geografico ) gasto right outer join cg_geograficos geo "
						+ "on ( geo.geografico = gasto.geografico and geo.ejercicio = ? ) left outer join municipio_demografia muni "
						+ "on ( muni.codigo_municipio = geo.geografico ) ";
						

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
