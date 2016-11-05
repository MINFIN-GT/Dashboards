package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import db.utilities.CDatabase;
import pojo.CNewEjecucion;
import utilities.CLogger;

public class CNewEjecucionDAO {

	public static List<CNewEjecucion> getEntidadesEjecucion(int mes) {

		List<CNewEjecucion> data = new ArrayList<>();

		String query = "select e.entidad_nombre, a.*, c.anticipo_cuota, c.aprobado_cuota, c.anticipo_cuota_acumulado, c.aprobado_cuota_acumulado FROM " + 
				"( " + 
				"select  " + 
				"  ep.entidad " + 
				", sum(case when (ep.mes = ?) then ep.ano_1 else 0 end) ano_1 " + 
				", sum(case when (ep.mes = ?) then ep.ano_2 else 0 end) ano_2 " + 
				", sum(case when (ep.mes = ?) then ep.ano_3 else 0 end) ano_3 " + 
				", sum(case when (ep.mes = ?) then ep.ano_4 else 0 end) ano_4 " + 
				", sum(case when (ep.mes = ?) then ep.ano_5 else 0 end) ano_5 " + 
				", sum(case when (ep.mes = ?) then ep.ano_actual else 0 end) ejecutado " + 
				", sum(case when (ep.mes <= ?) then ep.ano_actual else 0 end) ejecutado_acumulado " + 
				", sum(case when (ep.mes = ?) then ep.vigente else 0 end) vigente " + 
				"from mv_ejecucion_presupuestaria ep " + 
				"where ep.mes <= ? " + 
				"group by ep.entidad " + 
				") a left join ( " + 
				"	select ep.entidad " + 
				"	, sum(case when (ep.mes = ?) then ifnull(ep.anticipo_cuota,0) else 0 end) anticipo_cuota " + 
				"	, sum(case when (ep.mes <= ?) then ifnull(ep.anticipo_cuota,0) else 0 end) anticipo_cuota_acumulado " + 
				"	, sum(case when (ep.mes = ?) then ifnull(ep.aprobado_cuota,0) else 0 end) aprobado_cuota " + 
				"	, sum(case when (ep.mes <= ?) then ifnull(ep.aprobado_cuota,0) else 0 end) aprobado_cuota_acumulado " + 
				"	from ( " + 
				"	select ep.mes, ep.entidad, ep.unidad_ejecutora  " + 
				"	, avg(ep.anticipo) anticipo_cuota " + 
				"	, avg(ep.aprobado) aprobado_cuota " + 
				"	from mv_ejecucion_presupuestaria ep  " + 
				"	where ep.mes <= ? " + 
				"	group by ep.ejercicio, ep.mes, ep.entidad, ep.unidad_ejecutora " + 
				"	) ep " + 
				"	group by entidad " + 
				") c  " + 
				"on ( " + 
				"	c.entidad = a.entidad " + 
				") " + 
				", " + 
				"( " + 
				"	select e.entidad, e.entidad_nombre " + 
				"	from mv_estructura e " + 
				"	group by e.entidad, e.entidad_nombre " + 
				") e " + 
				"where a.entidad = e.entidad";

		if (CDatabase.connect()) {
			Connection conn = CDatabase.getConnection();
			try {
				PreparedStatement pstm1 = conn.prepareStatement(query);
				pstm1.setInt(1, mes);
				pstm1.setInt(2, mes);
				pstm1.setInt(3, mes);
				pstm1.setInt(4, mes);
				pstm1.setInt(5, mes);
				pstm1.setInt(6, mes);
				pstm1.setInt(7, mes);
				pstm1.setInt(8, mes);
				pstm1.setInt(9, mes);
				pstm1.setInt(10, mes);
				pstm1.setInt(11, mes);
				pstm1.setInt(12, mes);
				pstm1.setInt(13, mes);
				pstm1.setInt(14, mes);

				ResultSet results = pstm1.executeQuery();
				while (results.next()) {
					CNewEjecucion ejecucion = new CNewEjecucion(
							2016, 
							mes,
							results.getInt("entidad"), 
							results.getString("entidad_nombre"),
							results.getDouble("ano_1"), 
							results.getDouble("ano_2"), 
							results.getDouble("ano_3"),
							results.getDouble("ano_4"), 
							results.getDouble("ano_5"), 
							results.getDouble("anticipo_cuota"),
							results.getDouble("anticipo_cuota_acumulado"), 
							results.getDouble("aprobado_cuota"),
							results.getDouble("aprobado_cuota_acumulado"),
							results.getDouble("anticipo_cuota") + results.getDouble("aprobado_cuota"),
							results.getDouble("anticipo_cuota_acumulado") + results.getDouble("aprobado_cuota_acumulado"),
							results.getDouble("ejecutado"),
							results.getDouble("ejecutado_acumulado"), 
							results.getDouble("vigente"));
					
					data.add(ejecucion);
				}
				
				pstm1.close();

			} catch (Exception e) {
				CLogger.write("1", CNewEjecucionDAO.class, e);
			}
			finally{
				CDatabase.close(conn);
			}

		}

		return data;
	}

}
