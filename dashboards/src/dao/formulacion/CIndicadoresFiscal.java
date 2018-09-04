package dao.formulacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import db.utilities.CDatabaseOracle;
import pojo.formulacion.CIndicadorFiscal;
import utilities.CLogger;

public class CIndicadoresFiscal {
	public static ArrayList<CIndicadorFiscal> getReporte(int ejercicio){
		ArrayList<CIndicadorFiscal> ret = new ArrayList<CIndicadorFiscal>();
		Connection conn = null;
		try{
			conn = CDatabaseOracle.connect();
			if(!conn.isClosed()){
				PreparedStatement pstm =  conn.prepareStatement("select rf.posicion, rf.texto, rf.nivel, rf.negrillas " + 
						"from sicoinp_hreyes.reporte_formulacion rf " + 
						"where rf.reporte = 'CUADRO_1' " + 
						"order by rf.posicion");
				ResultSet rs = pstm.executeQuery();
				while(rs.next()){
					CIndicadorFiscal linea = new CIndicadorFiscal(rs.getString("texto"), rs.getInt("posicion"), rs.getInt("nivel"), 
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
