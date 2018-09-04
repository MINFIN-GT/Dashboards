package dao.formulacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import db.utilities.CDatabaseOracle;
import pojo.formulacion.CFinalidadEconomico;
import pojo.formulacion.CFinalidadRegion;
import utilities.CLogger;

public class CFinalidadDAO {
	
	private static String getFinalidadNombre(int finalidad) {
		String ret="";
		switch(finalidad) {
			case 1: ret = "Servicios Públicos Generales"; break;
			case 2: ret = "Defensa"; break;
			case 3: ret = "Orden Público y Seguridad Ciudadana"; break;
			case 4: ret = "Atención a Desastres y Gestión de Riesgos"; break;
			case 5: ret = "Asuntos Económicos"; break;
			case 6: ret = "Protección Ambiental"; break;
			case 7: ret = "Urbanización y Servicios Comunitarios"; break;
			case 8: ret = "Salud"; break;
			case 9: ret = "Actividades Deportivas, Recreativas, Cultura y Religión"; break;
			case 10: ret = "Educación"; break;
			case 11: ret = "Protección Social"; break;
			case 12: ret = "Transacciones de la Deuda Pública"; break;
		}
		return ret;
	}
	
	public static ArrayList<CFinalidadRegion> getFinalidadRegion(int ejercicio){
		ArrayList<CFinalidadRegion> ret = new ArrayList<CFinalidadRegion>();
		Connection conn = null;
		try{
			conn = CDatabaseOracle.connect();
			if(!conn.isClosed()){
				PreparedStatement pstm =  conn.prepareStatement("SELECT p.ejercicio, " + 
						"         (p.funcion-(mod(p.funcion,10000)))/10000 funcion, " + 
						"         SUM (p.recomendado) recomendado_total, " + 
						"         sum(case when g.region = 1 then p.recomendado end) r1 , " + 
						"         sum(case when g.region = 2 then p.recomendado end) r2 , " + 
						"         sum(case when g.region = 3 then p.recomendado end) r3 , " + 
						"         sum(case when g.region = 4 then p.recomendado end) r4 , " + 
						"         sum(case when g.region = 5 then p.recomendado end) r5 , " + 
						"         sum(case when g.region = 6 then p.recomendado end) r6 , " + 
						"         sum(case when g.region = 7 then p.recomendado end) r7 , " + 
						"         sum(case when g.region = 8 then p.recomendado end) r8 , " + 
						"         sum(case when g.region = 9 then p.recomendado end) r9 , " + 
						"         sum(case when g.region = 10 then p.recomendado end) r10 , " + 
						"         sum(case when g.region = 11 then p.recomendado end) r11 " + 
						"    FROM fp_p6_partidas p, cg_geograficos g " + 
						"   WHERE     p.ejercicio = ? " + 
						"         AND g.geografico = p.geografico " + 
						"         AND g.ejercicio = p.ejercicio " + 
						"GROUP BY p.ejercicio, (p.funcion-(mod(p.funcion,10000)))/10000 " + 
						"ORDER BY (p.funcion-(mod(p.funcion,10000)))/10000");
				pstm.setInt(1, ejercicio);
				ResultSet rs = pstm.executeQuery();
				while(rs.next()){
					CFinalidadRegion finalidad = new CFinalidadRegion(ejercicio, rs.getInt("funcion"), getFinalidadNombre(rs.getInt("funcion")),
							rs.getDouble("recomendado_total"), rs.getDouble("r1"),rs.getDouble("r2"),rs.getDouble("r3"),rs.getDouble("r4"),
							rs.getDouble("r5"),rs.getDouble("r6"),rs.getDouble("r7"),rs.getDouble("r8"),rs.getDouble("r9"),rs.getDouble("r10"),rs.getDouble("r11"));
					ret.add(finalidad);
				}
			
			}
		}
		catch(Exception e){
			CLogger.write("1", CFinalidadDAO.class, e);
		}
		finally{
			CDatabaseOracle.close(conn);
		}
		return ret;
	}
	
	public static ArrayList<CFinalidadEconomico> getFinalidadRecurso(int ejercicio){
		ArrayList<CFinalidadEconomico> ret = new ArrayList<CFinalidadEconomico>();
		Connection conn = null;
		try{
			conn = CDatabaseOracle.connect();
			if(!conn.isClosed()){
				PreparedStatement pstm =  conn.prepareStatement("SELECT p.ejercicio, " + 
						"         (p.funcion-(mod(p.funcion,10000)))/10000 funcion, " + 
						"         SUM (p.recomendado) recomendado_total, " + 
						"         sum(case when p.economico like '2111%' then p.recomendado end) e1 , " + 
						"         sum(case when p.economico like '2112%' then p.recomendado end) e2 , " + 
						"         sum(case when p.economico like '213%' then p.recomendado end) e3 , " + 
						"         sum(case when p.economico like '214%' then p.recomendado end) e4 , " + 
						"         sum(case when p.economico like '217%' then p.recomendado end) e5 , " + 
						"         sum(case when p.economico like '221%' then p.recomendado end) e6 , " + 
						"         sum(case when p.economico like '222%' then p.recomendado end) e7 , " + 
						"         sum(case when p.economico like '223%' then p.recomendado end) e8 " + 
						"    FROM fp_p6_partidas p " + 
						"   WHERE     p.ejercicio = ? " + 
						"GROUP BY p.ejercicio, (p.funcion-(mod(p.funcion,10000)))/10000 " + 
						"ORDER BY (p.funcion-(mod(p.funcion,10000)))/10000");
				pstm.setInt(1, ejercicio);
				ResultSet rs = pstm.executeQuery();
				while(rs.next()){
					CFinalidadEconomico finalidad = new CFinalidadEconomico(ejercicio, rs.getInt("funcion"), getFinalidadNombre(rs.getInt("funcion")),
							rs.getDouble("recomendado_total"), rs.getDouble("e1"),rs.getDouble("e2"),rs.getDouble("e3"),rs.getDouble("e4"),
							rs.getDouble("e5"),rs.getDouble("e6"),rs.getDouble("e7"),rs.getDouble("e8"),
							0.0);
					finalidad.setE9_monto(finalidad.getRecomendado_total()-finalidad.getE1_monto()-finalidad.getE2_monto()-finalidad.getE3_monto()
							-finalidad.getE4_monto()-finalidad.getE5_monto()-finalidad.getE6_monto()-finalidad.getE7_monto() 
							-finalidad.getE8_monto());
					ret.add(finalidad);
				}
				if(ejercicio==2019) {
					ret.get(ret.size()-1).setE3_monto(ret.get(ret.size()-1).getE3_monto()+ret.get(ret.size()-1).getE2_monto());
					ret.get(ret.size()-1).setE2_monto(0.0);
				}
			
			}
		}
		catch(Exception e){
			CLogger.write("2", CFinalidadDAO.class, e);
		}
		finally{
			CDatabaseOracle.close(conn);
		}
		return ret;
	}
}
