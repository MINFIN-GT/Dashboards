package dao.transparencia;

import java.util.ArrayList;

import db.utilities.CDatabase;
import pojo.transparencia.CEstadoCalamidad;
import utilities.CLogger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CEstadoCalamidadDAO {
	
	public static ArrayList<CEstadoCalamidad> getEstadosCalamidad(){
		ArrayList<CEstadoCalamidad> ret=new ArrayList<CEstadoCalamidad>();
		if(CDatabase.connect()){
			try{
				PreparedStatement pstm =  CDatabase.getConnection().prepareStatement("SELECT * FROM  estado_de_calamidad ORDER BY programa, subprograma ");
				ResultSet rs=pstm.executeQuery();
				while (rs.next()){
					CEstadoCalamidad estado = new CEstadoCalamidad(rs.getInt("ejercicio"), rs.getInt("programa"), 
							rs.getInt("subprograma"), rs.getString("nombre"), rs.getString("fecha_declaracion"), rs.getString("decrecto"), rs.getString("link")); 
					ret.add(estado);
				}
			}
			catch(Exception e){
				CLogger.write("1", CEstadoCalamidad.class, e);
			}
			finally{
				CDatabase.close();
			}
		}
		return ret;		
	}
}
