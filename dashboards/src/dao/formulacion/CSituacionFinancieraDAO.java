package dao.formulacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import db.utilities.CDatabaseOracle;
import pojo.formulacion.CSituacionFinanciera;
import utilities.CLogger;

public class CSituacionFinancieraDAO {
	
	public static ArrayList<CSituacionFinanciera> getReporte(int ejercicio){
		ArrayList<CSituacionFinanciera> ret = new ArrayList<CSituacionFinanciera>();
		Connection conn = null;
		try{
			conn = CDatabaseOracle.connect();
			if(!conn.isClosed()){
				PreparedStatement pstm =  conn.prepareStatement("select rf.posicion, rf.texto, rf.nivel, rf.negrillas " + 
						"from sicoinp_hreyes.reporte_formulacion rf " + 
						"where rf.reporte = 'CUADRO_2' " + 
						"order by rf.posicion");
				ResultSet rs = pstm.executeQuery();
				while(rs.next()){
					CSituacionFinanciera linea = new CSituacionFinanciera(rs.getString("texto"), rs.getInt("posicion"), rs.getInt("nivel"), 
							rs.getInt("negrillas"));
					ret.add(linea);
				}
			
			}
		}
		catch(Exception e){
			CLogger.write("1", CSituacionFinancieraDAO.class, e);
		}
		finally{
			CDatabaseOracle.close(conn);
		}
		return ret;
	}
}
