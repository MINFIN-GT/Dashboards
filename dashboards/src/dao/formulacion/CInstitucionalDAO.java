package dao.formulacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import db.utilities.CDatabase;
import db.utilities.CDatabaseOracle;
import pojo.formulacion.CInstitucionalFinalidad;
import pojo.formulacion.CInstitucionalTipoGasto;
import pojo.formulacion.CInstitucionalTipoGastoGrupoGasto;
import pojo.formulacion.CInstitucionalTipoGastoRegion;
import pojo.formulacion.CInstitucionalTotal;
import pojo.formulacion.CInstitucionalTotalDetalle;
import utilities.CLogger;

public class CInstitucionalDAO {
	
	public static ArrayList<CInstitucionalTotal> getInstitucionalTotal(int ejercicio){
		ArrayList<CInstitucionalTotal> ret = new ArrayList<CInstitucionalTotal>();
		Connection conn = null;
		Connection conn_mem = null;
		try{
			conn = CDatabaseOracle.connect();
			if(!conn.isClosed()){
				conn_mem = CDatabase.connect();
				PreparedStatement pstm =  conn.prepareStatement("SELECT p.ejercicio, " + 
						"         p.entidad, " + 
						"         e.nombre_completo  entidad_nombre, " + 
						"         (SELECT SUM (pa.aprobado) " + 
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
						"    FROM fp_p6_partidas p, sicoinp_hreyes.cg_entidades_custom e " + 
						"   WHERE     p.ejercicio = ? " + 
						"         AND p.entidad = e.entidad " + 
						"GROUP BY p.ejercicio, p.entidad, e.nombre_completo " + 
						"ORDER BY p.entidad");
				pstm.setInt(1, ejercicio);
				ResultSet rs = pstm.executeQuery();
				PreparedStatement pstm_mem = conn_mem.prepareStatement("select entidad,sum(ano_actual) total " + 
						"from mv_ejecucion_presupuestaria " + 
						"where ejercicio = ? " + 
						"group by entidad " + 
						"order by entidad");
				pstm_mem.setInt(1, ejercicio-2);
				ResultSet rs_mem = pstm_mem.executeQuery();
				rs_mem.next();
				while(rs.next()){
					Double ejecutado_dos_antes=null;
					if(rs_mem.getRow()>0) {
						if(rs_mem.getDouble("entidad")<rs.getInt("entidad")){
							while(rs_mem.getDouble("entidad")<rs.getInt("entidad"))
								rs_mem.next();
						}
						if(rs_mem.getDouble("entidad")==rs.getInt("entidad"))
							ejecutado_dos_antes = rs_mem.getDouble("total");
					}
					CInstitucionalTotal entidad = new CInstitucionalTotal(ejercicio, rs.getInt("entidad"), rs.getString("entidad_nombre"),  
							ejecutado_dos_antes, rs.getDouble("aprobado_anterior"),rs.getDouble("aprobado_anterior_mas_amp"), rs.getDouble("recomendado"));
					ret.add(entidad);
				}
			}
		}
		catch(Exception e){
			CLogger.write("1", CInstitucionalDAO.class, e);
		}
		finally{
			CDatabaseOracle.close(conn);
			CDatabase.close(conn_mem);
		}
		return ret;
	}
	
	public static ArrayList<CInstitucionalTipoGasto> getInstitucionalTipoGasto(int ejercicio){
		ArrayList<CInstitucionalTipoGasto> ret = new ArrayList<CInstitucionalTipoGasto>();
		Connection conn = null;
		try{
			conn = CDatabaseOracle.connect();
			if(!conn.isClosed()){
				PreparedStatement pstm =  conn.prepareStatement("SELECT p.ejercicio, " + 
						"         p.entidad, " + 
						"         e.nombre_completo  entidad_nombre, " + 
						"         SUM (p.recomendado) recomendado_total, " + 
						"         sum(case when p.tipo_presupuesto=11 then p.recomendado end) tp11, " + 
						"         sum(case when p.tipo_presupuesto=12 then p.recomendado end) tp12, " + 
						"         sum(case when p.tipo_presupuesto=13 then p.recomendado end) tp13, " + 
						"         sum(case when p.tipo_presupuesto=21 then p.recomendado end) tp21, " + 
						"         sum(case when p.tipo_presupuesto=22 then p.recomendado end) tp22, " + 
						"         sum(case when p.tipo_presupuesto=23 then p.recomendado end) tp23, " + 
						"         sum(case when p.tipo_presupuesto=31 then p.recomendado end) tp31 " + 
						"    FROM fp_p6_partidas p, sicoinp_hreyes.cg_entidades_custom e " + 
						"   WHERE     p.ejercicio = ? " + 
						"         AND p.entidad = e.entidad " + 
						"GROUP BY p.ejercicio, p.entidad, e.nombre_completo " + 
						"ORDER BY p.entidad");
				pstm.setInt(1, ejercicio);
				ResultSet rs = pstm.executeQuery();
				while(rs.next()){
					CInstitucionalTipoGasto entidad = new CInstitucionalTipoGasto(ejercicio, rs.getInt("entidad"), rs.getString("entidad_nombre"), rs.getDouble("recomendado_total"),
							rs.getDouble("tp11"),rs.getDouble("tp12"),rs.getDouble("tp13"),rs.getDouble("tp21"),rs.getDouble("tp22"),rs.getDouble("tp23"),rs.getDouble("tp31"));
					ret.add(entidad);
				}
			
			}
		}
		catch(Exception e){
			CLogger.write("2", CInstitucionalDAO.class, e);
		}
		finally{
			CDatabaseOracle.close(conn);
		}
		return ret;
	}
	
	public static ArrayList<CInstitucionalTipoGastoGrupoGasto> getInstitucionalTipoGastoGrupoGasto(int ejercicio, int tipo_gasto){
		ArrayList<CInstitucionalTipoGastoGrupoGasto> ret = new ArrayList<CInstitucionalTipoGastoGrupoGasto>();
		Connection conn = null;
		try{
			conn = CDatabaseOracle.connect();
			if(!conn.isClosed()){
				PreparedStatement pstm =  conn.prepareStatement("SELECT p.ejercicio, " + 
						"         p.entidad, " + 
						"         e.nombre_corto  entidad_nombre, " + 
						"         SUM (p.recomendado) recomendado_total, " + 
						"         sum(case when p.renglon between 0 and 99 then p.recomendado end) g0, " + 
						"         sum(case when p.renglon between 100 and 199 then p.recomendado end) g1, " + 
						"         sum(case when p.renglon between 200 and 299 then p.recomendado end) g2, " + 
						"         sum(case when p.renglon between 300 and 399 then p.recomendado end) g3, " + 
						"         sum(case when p.renglon between 400 and 499 then p.recomendado end) g4, " + 
						"         sum(case when p.renglon between 500 and 599 then p.recomendado end) g5, " + 
						"         sum(case when p.renglon between 600 and 699 then p.recomendado end) g6, " + 
						"         sum(case when p.renglon between 700 and 799 then p.recomendado end) g7, " + 
						"         sum(case when p.renglon between 800 and 899 then p.recomendado end) g8, " + 
						"         sum(case when p.renglon between 900 and 999 then p.recomendado end) g9 " + 
						"    FROM fp_p6_partidas p, sicoinp_hreyes.cg_entidades_custom e " + 
						"   WHERE     p.ejercicio = ? " + 
						"         AND p.entidad = e.entidad " + 
						"         and p.tipo_presupuesto between ? and ? " + 
						"GROUP BY p.ejercicio, p.entidad, e.nombre_corto " + 
						"ORDER BY p.entidad");
				pstm.setInt(1, ejercicio);
				switch(tipo_gasto) {
					case 10: pstm.setInt(2, 11); pstm.setInt(3, 13); break;
					case 20: pstm.setInt(2, 21); pstm.setInt(3, 23); break;
					case 30: pstm.setInt(2, 31); pstm.setInt(3, 31); break;
				}
				ResultSet rs = pstm.executeQuery();
				while(rs.next()){
					CInstitucionalTipoGastoGrupoGasto entidad = new CInstitucionalTipoGastoGrupoGasto(ejercicio, rs.getInt("entidad"), rs.getString("entidad_nombre"),
							tipo_gasto, rs.getDouble("recomendado_total"), rs.getDouble("g0"), rs.getDouble("g1"),rs.getDouble("g2"),rs.getDouble("g3"),rs.getDouble("g4"),
							rs.getDouble("g5"),rs.getDouble("g6"),rs.getDouble("g7"),rs.getDouble("g8"),rs.getDouble("g9"));
					ret.add(entidad);
				}
			
			}
		}
		catch(Exception e){
			CLogger.write("3", CInstitucionalDAO.class, e);
		}
		finally{
			CDatabaseOracle.close(conn);
		}
		return ret;
	}
	
	public static ArrayList<CInstitucionalFinalidad> getInstitucionalFinalidad(int ejercicio){
		ArrayList<CInstitucionalFinalidad> ret = new ArrayList<CInstitucionalFinalidad>();
		Connection conn = null;
		try{
			conn = CDatabaseOracle.connect();
			if(!conn.isClosed()){
				PreparedStatement pstm =  conn.prepareStatement("SELECT p.ejercicio, " + 
						"         p.entidad, " + 
						"         e.nombre_corto          entidad_nombre, " + 
						"         SUM (p.recomendado) recomendado_total, " + 
						"         sum(case when p.funcion between 10000 and 19999 then p.recomendado end) f01, " + 
						"         sum(case when p.funcion between 20000 and 29999 then p.recomendado end) f02, " + 
						"         sum(case when p.funcion between 30000 and 39999 then p.recomendado end) f03, " + 
						"         sum(case when p.funcion between 40000 and 49999 then p.recomendado end) f04, " + 
						"         sum(case when p.funcion between 50000 and 59999 then p.recomendado end) f05, " + 
						"         sum(case when p.funcion between 60000 and 69999 then p.recomendado end) f06, " + 
						"         sum(case when p.funcion between 70000 and 79999 then p.recomendado end) f07, " + 
						"         sum(case when p.funcion between 80000 and 89999 then p.recomendado end) f08, " + 
						"         sum(case when p.funcion between 90000 and 99999 then p.recomendado end) f09, " + 
						"         sum(case when p.funcion between 100000 and 109999 then p.recomendado end) f10, " + 
						"         sum(case when p.funcion between 110000 and 119999 then p.recomendado end) f11, " + 
						"         sum(case when p.funcion between 120000 and 129999 then p.recomendado end) f12 " + 
						"    FROM fp_p6_partidas p, sicoinp_hreyes.cg_entidades_custom e " + 
						"   WHERE     p.ejercicio = ? " + 
						"         AND p.entidad = e.entidad " + 
						"GROUP BY p.ejercicio, p.entidad, e.nombre_corto " + 
						"ORDER BY p.entidad");
				pstm.setInt(1, ejercicio);
				ResultSet rs = pstm.executeQuery();
				while(rs.next()){
					CInstitucionalFinalidad entidad = new CInstitucionalFinalidad(ejercicio, rs.getInt("entidad"), rs.getString("entidad_nombre"),
							rs.getDouble("recomendado_total"), rs.getDouble("f01"), rs.getDouble("f02"),rs.getDouble("f03"),rs.getDouble("f04"),rs.getDouble("f05"),
							rs.getDouble("f06"),rs.getDouble("f07"),rs.getDouble("f08"),rs.getDouble("f09"),rs.getDouble("f10"),rs.getDouble("f11"),rs.getDouble("f12"));
					ret.add(entidad);
				}
			
			}
		}
		catch(Exception e){
			CLogger.write("4", CInstitucionalDAO.class, e);
		}
		finally{
			CDatabaseOracle.close(conn);
		}
		return ret;
	}
	
	public static ArrayList<CInstitucionalTipoGastoRegion> getInstitucionalTipoGastoRegion(int ejercicio, int tipo_gasto){
		ArrayList<CInstitucionalTipoGastoRegion> ret = new ArrayList<CInstitucionalTipoGastoRegion>();
		Connection conn = null;
		try{
			conn = CDatabaseOracle.connect();
			if(!conn.isClosed()){
				PreparedStatement pstm =  conn.prepareStatement("SELECT p.ejercicio, " + 
						"         p.entidad, " + 
						"         e.nombre_corto          entidad_nombre, " + 
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
						"    FROM fp_p6_partidas p, sicoinp_hreyes.cg_entidades_custom e, cg_geograficos g " + 
						"   WHERE     p.ejercicio = ? " + 
						"         AND p.entidad = e.entidad " + 
						"         AND g.geografico = p.geografico " + 
						"         AND g.ejercicio = p.ejercicio " + 
						"         and p.tipo_presupuesto between ? and ? " + 
						"GROUP BY p.ejercicio, p.entidad, e.nombre_corto " + 
						"ORDER BY p.entidad");
				pstm.setInt(1, ejercicio);
				switch(tipo_gasto) {
					case 10: pstm.setInt(2, 11); pstm.setInt(3, 13); break;
					case 20: pstm.setInt(2, 21); pstm.setInt(3, 23); break;
					case 30: pstm.setInt(2, 31); pstm.setInt(3, 31); break;
				}
				ResultSet rs = pstm.executeQuery();
				while(rs.next()){
					CInstitucionalTipoGastoRegion entidad = new CInstitucionalTipoGastoRegion(ejercicio, rs.getInt("entidad"), rs.getString("entidad_nombre"),
							tipo_gasto, rs.getDouble("recomendado_total"), rs.getDouble("r1"),rs.getDouble("r2"),rs.getDouble("r3"),rs.getDouble("r4"),
							rs.getDouble("r5"),rs.getDouble("r6"),rs.getDouble("r7"),rs.getDouble("r8"),rs.getDouble("r9"),rs.getDouble("r10"),rs.getDouble("r11"));
					ret.add(entidad);
				}
			
			}
		}
		catch(Exception e){
			CLogger.write("5", CInstitucionalDAO.class, e);
		}
		finally{
			CDatabaseOracle.close(conn);
		}
		return ret;
	}
	
	public static Double getTotalEjecutado(int ejercicio){
		Double ret = 0.0;
		Connection conn_mem = null;
		try{
			conn_mem = CDatabase.connect();
				PreparedStatement pstm_mem = conn_mem.prepareStatement("select sum(ano_actual) total " + 
						"from mv_ejecucion_presupuestaria " + 
						"where ejercicio = ? ");
				pstm_mem.setInt(1, ejercicio);
				ResultSet rs_mem = pstm_mem.executeQuery();
				if(rs_mem.next()){
					ret=rs_mem.getDouble("total");
				}
			
		}
		catch(Exception e){
			CLogger.write("5", CInstitucionalDAO.class, e);
		}
		finally{
			CDatabase.close(conn_mem);
		}
		return ret;
	}
	
	public static ArrayList<CInstitucionalTotalDetalle> getInstitucionalTotalDetalle(int ejercicio){
		ArrayList<CInstitucionalTotalDetalle> ret = new ArrayList<CInstitucionalTotalDetalle>();
		Connection conn = null;
		Connection conn_mem = null;
		try{
			conn = CDatabaseOracle.connect();
			if(!conn.isClosed()){
				conn_mem = CDatabase.connect();
				PreparedStatement pstm =  conn.prepareStatement("SELECT p.ejercicio,     " + 
						"     p.entidad,     " + 
						"     e.nombre_completo  entidad_nombre,     " + 
						"     p.unidad_ejecutora,     " + 
						"     ue.nombre unidad_ejecutora_nombre,     " + 
						"     p.programa,     " + 
						"     pr.nom_estructura programa_nombre,     " + 
						"     p.renglon,     " + 
						"     r.nombre renglon_nombre,     " + 
						"     (   SELECT SUM (pa.aprobado)     " + 
						"          FROM fp_p6_partidas pa     " + 
						"         WHERE pa.ejercicio = p.ejercicio - 1     " + 
						"         AND pa.entidad = p.entidad     " + 
						"         AND pa.unidad_ejecutora = p.unidad_ejecutora     " + 
						"         AND pa.programa = p.programa     " + 
						"         AND pa.renglon = p.renglon     " + 
						"         ) aprobado_anterior,     " + 
						"       (SELECT SUM (pa.aprobado)     " + 
						"          FROM fp_p6_partidas pa     " + 
						"         WHERE pa.ejercicio = p.ejercicio - 1     " + 
						"         AND pa.entidad = p.entidad     " + 
						"         AND pa.unidad_ejecutora = p.unidad_ejecutora     " + 
						"         AND pa.programa = p.programa     " + 
						"         and pa.renglon = p.renglon     " + 
						"       )     " + 
						"     + NVL (     " + 
						"          (SELECT SUM (ad.monto_aprobado)     " + 
						"             FROM eg_modificaciones_hoja am, eg_modificaciones_detalle ad     " + 
						"            WHERE     am.ejercicio = p.ejercicio - 1     " + 
						"                  AND am.entidad = p.entidad     " + 
						"                  AND am.clase_registro = 'AMP'     " + 
						"                  AND am.estado = 'APROBADO'     " + 
						"                  AND am.fec_disposicion <= TO_DATE ( (p.ejercicio - 1) || '/07/15','YYYY/MM/DD')     " + 
						"                  AND am.unidad_ejecutora = p.unidad_ejecutora     " + 
						"                  AND ad.ejercicio = am.ejercicio     " + 
						"                  and ad.entidad = am.entidad     " + 
						"                  AND ad.programa = p.programa     " + 
						"                  and ad.unidad_ejecutora = am.unidad_ejecutora     " + 
						"                  and ad.no_cur = am.no_cur     " + 
						"                  AND ad.renglon = p.renglon),     " + 
						"          0)     " + 
						"        aprobado_anterior_mas_amp,     " + 
						"     SUM (p.recomendado) recomendado     " + 
						"FROM fp_p6_partidas p, sicoinp_hreyes.cg_entidades_custom e, cg_entidades ue, cp_estructuras pr, cp_objetos_gasto r     " + 
						"WHERE     p.ejercicio = ?     " + 
						"     AND p.entidad = e.entidad     " + 
						"     AND ue.ejercicio = p.ejercicio     " + 
						"     AND ue.entidad = e.entidad     " + 
						"     AND ue.unidad_ejecutora = p.unidad_ejecutora     " + 
						"     AND pr.ejercicio = p.ejercicio     " + 
						"     AND pr.entidad = p.entidad     " + 
						"     AND pr.unidad_ejecutora = p.unidad_ejecutora     " + 
						"     AND pr.programa = p.programa     " + 
						"     AND pr.nivel_estructura = 2     " + 
						"     AND r.ejercicio = p.ejercicio     " + 
						"     and r.renglon = p.renglon     " + 
						"GROUP BY p.ejercicio, p.entidad,e.nombre_completo, p.unidad_ejecutora, ue.nombre,     " + 
						"p.programa, pr.nom_estructura, p.renglon, r.nombre     " + 
						"ORDER BY p.entidad, p.unidad_ejecutora, p.programa, p.renglon");
				pstm.setInt(1, ejercicio);
				ResultSet rs = pstm.executeQuery();
				PreparedStatement pstm_mem = conn_mem.prepareStatement("select entidad, unidad_ejecutora, programa, renglon, sum(ano_actual) total " + 
						"from mv_ejecucion_presupuestaria " + 
						"where ejercicio = ? " + 
						"group by entidad, unidad_ejecutora, programa, renglon " + 
						"order by entidad, unidad_ejecutora, programa, renglon");
				pstm_mem.setInt(1, ejercicio-2);
				ResultSet rs_mem = pstm_mem.executeQuery();
				rs_mem.next();
				CInstitucionalTotalDetalle entidad=null;
				CInstitucionalTotalDetalle ue=null;
				CInstitucionalTotalDetalle programa=null;
				int entidad_actual=-1;
				int ue_actual=-1;
				int programa_actual=-1;
				while(rs.next()) {
					CInstitucionalTotalDetalle renglon = new CInstitucionalTotalDetalle(ejercicio,rs.getInt("entidad"), rs.getString("entidad_nombre"), rs.getInt("unidad_ejecutora"), 
							rs.getString("unidad_ejecutora_nombre"), rs.getInt("programa"), rs.getString("programa_nombre"), rs.getInt("renglon"), rs.getString("renglon_nombre"),0.0,
							rs.getDouble("aprobado_anterior"), rs.getDouble("aprobado_anterior_mas_amp"), rs.getDouble("recomendado"), 4, null);
					boolean exit = false;
					while(rs_mem.next()&&!exit) {
						if(rs_mem.getInt("entidad")==renglon.getEntidad() && rs_mem.getInt("unidad_ejecutora")==renglon.getUnidad_ejecutora() &&
								rs_mem.getInt("programa")==renglon.getPrograma() && rs_mem.getInt("renglon")==renglon.getRenglon()) 
							renglon.setEjecutado_dos_antes(rs_mem.getDouble("total"));
						else if(rs_mem.getInt("entidad")==renglon.getEntidad() && rs_mem.getInt("unidad_ejecutora")==renglon.getUnidad_ejecutora() &&
								rs_mem.getInt("programa")==renglon.getPrograma() && rs_mem.getInt("renglon")<renglon.getRenglon())
							rs_mem.next();
						else if(rs_mem.getInt("entidad")==renglon.getEntidad() && rs_mem.getInt("unidad_ejecutora")==renglon.getUnidad_ejecutora() &&
								rs_mem.getInt("programa")==renglon.getPrograma() && rs_mem.getInt("renglon")>renglon.getRenglon()) {
							exit=true;
							if(!rs_mem.isLast())
								rs_mem.previous();
						}
						else if(rs_mem.getInt("entidad")==renglon.getEntidad() && rs_mem.getInt("unidad_ejecutora")==renglon.getUnidad_ejecutora() &&
								rs_mem.getInt("programa")<renglon.getPrograma()) {
							rs_mem.next();
						}
						else if(rs_mem.getInt("entidad")==renglon.getEntidad() && rs_mem.getInt("unidad_ejecutora")==renglon.getUnidad_ejecutora() &&
								rs_mem.getInt("programa")<renglon.getPrograma()) {
							exit=true;
							if(!rs_mem.isLast())
								rs_mem.previous();
						}
						else if(rs_mem.getInt("entidad")==renglon.getEntidad() && rs_mem.getInt("unidad_ejecutora")<renglon.getUnidad_ejecutora() ) {
							rs_mem.next();
						}
						else if(rs_mem.getInt("entidad")==renglon.getEntidad() && rs_mem.getInt("unidad_ejecutora")<renglon.getUnidad_ejecutora() ) {
							exit=true;
							if(!rs_mem.isLast())
								rs_mem.previous();
						}
						else if(rs_mem.getInt("entidad")<renglon.getEntidad() ) {
							rs_mem.next();
						}
						else if(rs_mem.getInt("entidad")>renglon.getEntidad() ) {
							exit=true;
							if(!rs_mem.isLast())
								rs_mem.previous();
						}
					};
					if(renglon.getEntidad()!=entidad_actual) {
						if(entidad!=null) {
							for(int i=0; i<entidad.getChildren().size(); i++) {
								entidad.setEjecutado_dos_antes(entidad.getEjecutado_dos_antes()+entidad.getChildren().get(i).getEjecutado_dos_antes());
								entidad.setAprboado_anterior(entidad.getAproobado_anterior()+entidad.getChildren().get(i).getAproobado_anterior());
								entidad.setAprboado_anterior_mas_ampliaciones(entidad.getAproobado_anterior_mas_ampliaciones()+entidad.getChildren().get(i).getAproobado_anterior_mas_ampliaciones());
								entidad.setRecomendado(entidad.getRecomendado()+entidad.getChildren().get(i).getRecomendado());
							}
							ret.add(entidad);
						}
						entidad=new CInstitucionalTotalDetalle(ejercicio,rs.getInt("entidad"), rs.getString("entidad_nombre"), 0, 
								null, 0, null, 0, null,0.0,0.0, 0.0, 0.0, 1, new ArrayList<CInstitucionalTotalDetalle>());
						entidad_actual = entidad.getEntidad();
					}
					if(renglon.getUnidad_ejecutora()!=ue_actual) {
						if(ue!=null) {
							for(int i=0; i<ue.getChildren().size(); i++) {
								ue.setEjecutado_dos_antes(ue.getEjecutado_dos_antes()+ue.getChildren().get(i).getEjecutado_dos_antes());
								ue.setAprboado_anterior(ue.getAproobado_anterior()+ue.getChildren().get(i).getAproobado_anterior());
								ue.setAprboado_anterior_mas_ampliaciones(ue.getAproobado_anterior_mas_ampliaciones()+ue.getChildren().get(i).getAproobado_anterior_mas_ampliaciones());
								ue.setRecomendado(ue.getRecomendado()+ue.getChildren().get(i).getRecomendado());
							}
							entidad.getChildren().add(ue);
						}
						ue=new CInstitucionalTotalDetalle(ejercicio,rs.getInt("entidad"), rs.getString("entidad_nombre"), rs.getInt("unidad_ejecutora"), 
								rs.getString("unidad_ejecutora_nombre"), 0, null, 0, null,0.0,0.0, 0.0, 0.0, 1, new ArrayList<CInstitucionalTotalDetalle>());
						ue_actual = ue.getUnidad_ejecutora();
					}
					if(renglon.getPrograma()!=programa_actual) {
						if(programa!=null) {
							for(int i=0; i<programa.getChildren().size(); i++) {
								programa.setEjecutado_dos_antes(programa.getEjecutado_dos_antes()+programa.getChildren().get(i).getEjecutado_dos_antes());
								programa.setAprboado_anterior(programa.getAproobado_anterior()+programa.getChildren().get(i).getAproobado_anterior());
								programa.setAprboado_anterior_mas_ampliaciones(programa.getAproobado_anterior_mas_ampliaciones()+programa.getChildren().get(i).getAproobado_anterior_mas_ampliaciones());
								programa.setRecomendado(programa.getRecomendado()+programa.getChildren().get(i).getRecomendado());
							}
							ue.getChildren().add(programa);
						}
						programa = new CInstitucionalTotalDetalle(ejercicio,rs.getInt("entidad"), rs.getString("entidad_nombre"), rs.getInt("unidad_ejecutora"), 
								rs.getString("unidad_ejecutora_nombre"), rs.getInt("programa"), rs.getString("programa_nombre"), 
								0, null,0.0,0.0, 0.0, 0.0, 1, new ArrayList<CInstitucionalTotalDetalle>());
						programa_actual = programa.getPrograma();
					}
					programa.getChildren().add(renglon);
				}
				if(programa!=null) {
					for(int i=0; i<programa.getChildren().size(); i++) {
						programa.setEjecutado_dos_antes(programa.getEjecutado_dos_antes()+programa.getChildren().get(i).getEjecutado_dos_antes());
						programa.setAprboado_anterior(programa.getAproobado_anterior()+programa.getChildren().get(i).getAproobado_anterior());
						programa.setAprboado_anterior_mas_ampliaciones(programa.getAproobado_anterior_mas_ampliaciones()+programa.getChildren().get(i).getAproobado_anterior_mas_ampliaciones());
						programa.setRecomendado(programa.getRecomendado()+programa.getChildren().get(i).getRecomendado());
					}
					ue.getChildren().add(programa);
				}
				if(ue!=null) {
					for(int i=0; i<ue.getChildren().size(); i++) {
						ue.setEjecutado_dos_antes(ue.getEjecutado_dos_antes()+ue.getChildren().get(i).getEjecutado_dos_antes());
						ue.setAprboado_anterior(ue.getAproobado_anterior()+ue.getChildren().get(i).getAproobado_anterior());
						ue.setAprboado_anterior_mas_ampliaciones(ue.getAproobado_anterior_mas_ampliaciones()+ue.getChildren().get(i).getAproobado_anterior_mas_ampliaciones());
						ue.setRecomendado(ue.getRecomendado()+ue.getChildren().get(i).getRecomendado());
					}
					entidad.getChildren().add(ue);
				}
				if(entidad!=null) {
					for(int i=0; i<entidad.getChildren().size(); i++) {
						entidad.setEjecutado_dos_antes(entidad.getEjecutado_dos_antes()+entidad.getChildren().get(i).getEjecutado_dos_antes());
						entidad.setAprboado_anterior(entidad.getAproobado_anterior()+entidad.getChildren().get(i).getAproobado_anterior());
						entidad.setAprboado_anterior_mas_ampliaciones(entidad.getAproobado_anterior_mas_ampliaciones()+entidad.getChildren().get(i).getAproobado_anterior_mas_ampliaciones());
						entidad.setRecomendado(entidad.getRecomendado()+entidad.getChildren().get(i).getRecomendado());
					}
					ret.add(entidad);
				}
					
			}
		}
		catch(Exception e){
			CLogger.write("5", CInstitucionalDAO.class, e);
		}
		finally{
			CDatabaseOracle.close(conn);
			CDatabase.close(conn_mem);
		}
		return ret;
	}
	
	
}
