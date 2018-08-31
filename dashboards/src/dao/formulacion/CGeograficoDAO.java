package dao.formulacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import db.utilities.CDatabaseOracle;
import pojo.formulacion.CGastoGeografico;
import utilities.CLogger;

public class CGeograficoDAO {
	
	public static ArrayList<CGastoGeografico> getGastoGeografico(int ejercicio) {
		ArrayList<CGastoGeografico> ret = new ArrayList<CGastoGeografico>();
		Connection conn = null;
		try{
			conn = CDatabaseOracle.connect();
			if(!conn.isClosed()){
				PreparedStatement pstm =  conn.prepareStatement("SELECT p.ejercicio, (p.geografico - mod(p.geografico,100))/100 departamento, d.nombre_departamento, p.geografico,  " + 
						"m.nombre nombre_geografico, SUM (p.recomendado) recomendado   " + 
						"FROM fp_p6_partidas p  " + 
						"left outer join cg_departamentos d  on d.codigo_departamento = (p.geografico - mod(p.geografico,100))/100, " + 
						"cg_geograficos m " + 
						"WHERE p.ejercicio = ?  " + 
						"and m.ejercicio = p.ejercicio " + 
						"and m.geografico = p.geografico " + 
						"GROUP BY p.ejercicio, (p.geografico - mod(p.geografico,100))/100, d.nombre_departamento, p.geografico, m.nombre " + 
						"ORDER BY 1,2,3");
				pstm.setInt(1, ejercicio);
				ResultSet rs = pstm.executeQuery();
				Double recomendado_total = 0.0;
				while(rs.next()){
					CGastoGeografico geografico = new CGastoGeografico(ejercicio, rs.getInt("departamento"),
							rs.getString("nombre_departamento"), rs.getInt("geografico"), rs.getString("nombre_geografico"),
							rs.getDouble("recomendado"));
					recomendado_total += rs.getDouble("recomendado");
					ret.add(geografico);
				}
				CGastoGeografico geografico = new CGastoGeografico(ejercicio, 0, "general", 0, "general", recomendado_total);
				ret.add(0, geografico);
			}
		}
		catch(Exception e){
			CLogger.write("1", CGeograficoDAO.class, e);
		}
		finally{
			CDatabaseOracle.close(conn);
		}
		return ret;
	}
}
