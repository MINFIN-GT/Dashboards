package dao.transparencia;

import java.util.ArrayList;

import db.utilities.CDatabase;
import pojo.transparencia.CEstadoCalamidad;
import utilities.CLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CEstadoCalamidadDAO {
	
	public static ArrayList<CEstadoCalamidad> getEstadosCalamidad(){
		ArrayList<CEstadoCalamidad> ret=new ArrayList<CEstadoCalamidad>();
		Connection conn = null;
		try{
			conn = CDatabase.connectEstadosExcepcion();
			if(!conn.isClosed()){
				PreparedStatement pstm =  conn.prepareStatement("SELECT * FROM  estado_de_calamidad ORDER BY subprograma DESC ");
				ResultSet rs=pstm.executeQuery();
				while (rs.next()){
					CEstadoCalamidad estado = new CEstadoCalamidad(rs.getInt("ejercicio"), rs.getInt("programa"), 
							rs.getInt("subprograma"), rs.getString("nombre"), rs.getString("fecha_declaracion"), rs.getString("decrecto"), rs.getString("link"),
							rs.getInt("tipo_estado_calamidad"), rs.getString("latitude"), rs.getString("longitude"));  
					ret.add(estado);
				}
			}
		}
		catch(Exception e){
			CLogger.write("1", CEstadoCalamidad.class, e);
		}
		finally{
			CDatabase.close(conn);
		}
		return ret;		
	}
	
	public static CEstadoCalamidad getEstadoCalamidad(int subprograma){
		CEstadoCalamidad ret=null;
		Connection conn = null;
		try{
			conn = CDatabase.connectEstadosExcepcion();
			if(!conn.isClosed()){
				PreparedStatement pstm =  conn.prepareStatement("SELECT * FROM  estado_de_calamidad WHERE subprograma = ?");
				pstm.setInt(1, subprograma);
				ResultSet rs=pstm.executeQuery();
				if (rs.next()){
					ret = new CEstadoCalamidad(rs.getInt("ejercicio"), rs.getInt("programa"), 
							rs.getInt("subprograma"), rs.getString("nombre"), rs.getString("fecha_declaracion"), rs.getString("decrecto"), rs.getString("link"),
							rs.getInt("tipo_estado_calamidad"), rs.getString("latitude"), rs.getString("longitude")); 
				}
		}
		}
		catch(Exception e){
			CLogger.write("2", CEstadoCalamidad.class, e);
		}
		finally{
			CDatabase.close(conn);
		}
		return ret;		
	}
}
