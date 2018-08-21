package dao.formulacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import db.utilities.CDatabaseOracle;
import pojo.formulacion.CInstitucionalTotal;
import utilities.CLogger;

public class CInstitucionalDAO {
	
	public static ArrayList<CInstitucionalTotal> getInstitucionalTotal(int ejercicio){
		ArrayList<CInstitucionalTotal> ret = new ArrayList<CInstitucionalTotal>();
		Connection conn = null;
		try{
			conn = CDatabaseOracle.connect();
			if(!conn.isClosed()){
				PreparedStatement pstm =  conn.prepareStatement("  SELECT p.ejercicio, " + 
						"         p.entidad, " + 
						"         e.nombre          entidad_nombre, " + 
						"         (SELECT SUM (gd.monto_renglon) " + 
						"            FROM sicoinprod.eg_gastos_hoja gh, sicoinprod.eg_gastos_detalle gd " + 
						"           WHERE     gh.ejercicio = p.ejercicio - 2 " + 
						"                 AND gh.entidad = p.entidad " + 
						"                 AND gh.ejercicio = gd.ejercicio " + 
						"                 AND gh.entidad = gd.entidad " + 
						"                 AND gh.unidad_ejecutora = gd.unidad_ejecutora " + 
						"                 AND gh.no_cur = gd.no_cur " + 
						"                 AND gh.clase_registro IN ('DEV','CYD','RDP','REG') " + 
						"                 AND gh.estado = 'APROBADO') " + 
						"            ejecutado_dos_antes, " + 
						"			(SELECT SUM (pa.aprobado) " + 
						"              FROM fp_p6_partidas pa " + 
						"             WHERE pa.ejercicio = p.ejercicio - 1 AND pa.entidad = p.entidad) aprobado_anterior, " + 
						"           (SELECT SUM (pa.aprobado) " + 
						"              FROM fp_p6_partidas pa " + 
						"             WHERE pa.ejercicio = p.ejercicio - 1 AND pa.entidad = p.entidad) " + 
						"         + NVL ( " + 
						"              (SELECT SUM (am.monto_aprobado) " + 
						"                 FROM eg_modificaciones_hoja am " + 
						"                WHERE     am.ejercicio = p.ejercicio - 1 " + 
						"                      AND am.entidad = p.entidad " + 
						"                      AND am.clase_registro = 'AMP' " + 
						"                      AND am.estado = 'APROBADO' " + 
						"                      AND am.fec_disposicion <= TO_DATE ( (p.ejercicio - 1) || '/07/15','YYYY/MM/DD') " + 
						"                      AND am.unidad_ejecutora = 0), " + 
						"              0) " + 
						"            aprobado_anterior_mas_amp, " + 
						"         SUM (p.recomendado) recomendado " + 
						"    FROM fp_p6_partidas p, cg_entidades e " + 
						"   WHERE     p.ejercicio = ? " + 
						"         AND p.ejercicio = e.ejercicio " + 
						"         AND p.entidad = e.entidad " + 
						"         AND e.unidad_ejecutora = 0 " + 
						"GROUP BY p.ejercicio, p.entidad, e.nombre " + 
						"ORDER BY p.entidad");
				pstm.setInt(1, ejercicio);
				ResultSet rs = pstm.executeQuery();
				while(rs.next()){
					CInstitucionalTotal entidad = new CInstitucionalTotal(ejercicio, rs.getInt("entidad"), rs.getString("entidad_nombre"), rs.getDouble("ejecutado_dos_antes"), 
							rs.getDouble("aprboado_anterior"),rs.getDouble("aprboado_anterior_mas_amp"), rs.getDouble("recomendado"));
					ret.add(entidad);
				}
			
			}
		}
		catch(Exception e){
			CLogger.write("1", CInstitucionalDAO.class, e);
		}
		finally{
			CDatabaseOracle.close(conn);
		}
		return ret;
	}
}
