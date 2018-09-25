package dao.formulacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import db.utilities.CDatabase;
import db.utilities.CDatabaseOracle;
import pojo.formulacion.CDepartamento;
import pojo.formulacion.CInstitucionalFinalidad;
import pojo.formulacion.CInstitucionalFinalidadDetalle;
import pojo.formulacion.CInstitucionalTipoGasto;
import pojo.formulacion.CInstitucionalTipoGastoDetalle;
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
			CLogger.write("6", CInstitucionalDAO.class, e);
		}
		finally{
			CDatabase.close(conn_mem);
		}
		return ret;
	}
	
	private static String customSelector(String selector, String tabla) {
		return selector.replace("$", tabla);
	}
	
	private static String customCondicion(String condicion, String tabla) {
		return condicion.replace("$", tabla);
	}
	
	public static ArrayList<CInstitucionalTotalDetalle> getInstitucionalTotalDetalle(int ejercicio, int entidad, int unidad_ejecutora, int programa, int grupo, int subgrupo){
		ArrayList<CInstitucionalTotalDetalle> ret = new ArrayList<CInstitucionalTotalDetalle>();
		Connection conn = null;
		String sql;
		try{
			conn = CDatabaseOracle.connect();
			if(!conn.isClosed()){
				String selector_padre="";
				if(entidad==-1)
					selector_padre = "t1.entidad codigo, e.nombre ";
				else if(unidad_ejecutora==-1)
					selector_padre = "t1.unidad_ejecutora codigo, ue.nombre ";
				else if(programa==-1)
					selector_padre = "t1.programa codigo, p.nom_estructura nombre ";
				else if(grupo ==-1)
					selector_padre = "t1.grupo codigo, g.nombre ";
				else if(subgrupo==-1)
					selector_padre = "t1.subgrupo codigo, sg.nombre ";
				else if(subgrupo>-1)
					selector_padre = "t1.renglon codigo, r.nombre ";
				String selector="";
				if(entidad==-1)
					selector = "$.entidad";
				else if(unidad_ejecutora==-1)
					selector = "$.unidad_ejecutora";
				else if(programa==-1)
					selector = "$.programa";
				else if(grupo ==-1)
					selector = "($.renglon-mod($.renglon,100))/100 grupo";
				else if(subgrupo==-1)
					selector = "($.renglon-mod($.renglon,10))/10 subgrupo";
				else if(subgrupo>-1)
					selector = "$.renglon";
				String grupo_padre="";
				if(entidad==-1)
					grupo_padre = "t1.entidad, e.nombre ";
				else if(unidad_ejecutora==-1)
					grupo_padre = "t1.unidad_ejecutora, ue.nombre ";
				else if(programa==-1)
					grupo_padre = "t1.programa, p.nom_estructura ";
				else if(grupo ==-1)
					grupo_padre = "t1.grupo, g.nombre ";
				else if(subgrupo==-1)
					grupo_padre = "t1.subgrupo, sg.nombre ";
				else if(subgrupo>-1)
					grupo_padre = "t1.renglon, r.nombre ";
				String agrupador="";
				if(entidad==-1)
					agrupador = "$.entidad";
				else if(unidad_ejecutora==-1)
					agrupador = "$.unidad_ejecutora";
				else if(programa==-1)
					agrupador = "$.programa";
				else if(grupo ==-1)
					agrupador = "(($.renglon-mod($.renglon,100))/100)";
				else if(subgrupo==-1)
					agrupador = "(($.renglon-mod($.renglon,10))/10)";
				else if(subgrupo>-1)
					agrupador = "$.renglon";
				String condicion_entidad = (entidad>-1) ? " and $.entidad = " + entidad : "";
				String condicion_unidad_ejecutora = (unidad_ejecutora>-1) ? " and $.unidad_ejecutora = " + unidad_ejecutora : "";
				String condicion_programa = (programa>-1) ? " and $.programa = " + programa : "";
				String condicion_grupo = (grupo>-1) ? " and (($.renglon-mod($.renglon,100))/100)= " + grupo : "";
				String condicion_subgrupo = (subgrupo>-1) ? " and (($.renglon-mod($.renglon,10))/10)= " + subgrupo : "";
				String condicion=String.join(" ", (entidad>-1) ? condicion_entidad : "",(unidad_ejecutora>-1) ? condicion_unidad_ejecutora : "",(programa>-1) ? condicion_programa : "",
						grupo>-1 ? condicion_grupo : "", subgrupo>-1 ? condicion_subgrupo : "");
				sql = "select " + selector_padre +
						", sum(ejecutado_dos_antes) ejecutado_dos_antes, " + 
						"sum(aprobado_anterior) aprobado_anterior, sum(aprobado_anterior + aprobado_anterior_amp) aprobado_anterior_mas_amp, " + 
						"sum(recomendado) recomendado " + 
						"from ( " + 
						"select " + customSelector(selector,"gd") + "," +
						"sum(gd.monto_renglon) ejecutado_dos_antes, 0.0 aprobado_anterior, 0.0 aprobado_anterior_amp, 0.0 recomendado " + 
						"from sicoinprod.eg_gastos_detalle gd, sicoinprod.eg_gastos_hoja gh  " + 
						"where gh.ejercicio = ? " + 
						"and gd.ejercicio = gh.ejercicio " + 
						"and gd.entidad = gh.entidad " + 
						"and gd.unidad_ejecutora = gh.unidad_ejecutora " + 
						"and gd.unidad_desconcentrada = gh.unidad_desconcentrada " + 
						"and gd.no_cur = gh.no_cur " + 
						"and gh.clase_registro IN ('DEV', 'CYD', 'RDP', 'REG') " + 
						"and gh.estado = 'APROBADO' " + 
						customCondicion(condicion,"gd") +
						" group by " + 
						customSelector(agrupador,"gd") +
						
						" union " + 
						
						"select  " + 
						customSelector(selector,"p")  + 
						", 0.0,sum(p.asignado), 0.0, 0.0 " + 
						"from sicoinprod.eg_f6_partidas p " + 
						"where p.ejercicio = ? " +
						customCondicion(condicion,"p") +
						((entidad==-1) ? "and ((p.entidad in (11130004,11130010,11130014,11130017,11130018,11130019,11140021) and p.unidad_ejecutora=0) or  " + 
						"        (p.entidad not in (11130004,11130010,11130014,11130017,11130018,11130019,11140021) and p.unidad_ejecutora>0)) " : "") +
						" group by "+ 
						customSelector(agrupador,"p")  + 
						
						" union " + 
						
						"SELECT " + 
						customSelector(selector,"ad")  + 
						", 0.0, 0.0, SUM (ad.monto_aprobado) , 0.0    " + 
						"	 FROM eg_modificaciones_hoja am, eg_modificaciones_detalle ad      " + 
						"	 WHERE     am.ejercicio = ?     " + 
						"	AND am.clase_registro = 'AMP'      " + 
						"	AND am.estado = 'APROBADO'      " + 
						"	AND am.fec_disposicion <= TO_DATE ( ? || '/07/15','YYYY/MM/DD')      " + 
						"	AND ad.ejercicio = am.ejercicio      " + 
						"	and ad.entidad = am.entidad      " + 
						"	and ad.unidad_ejecutora = am.unidad_ejecutora   " + 
						"   and ad.unidad_desconcentrada = am.unidad_desconcentrada " + 
						"	and ad.no_cur = am.no_cur      " + 
						customCondicion(condicion,"ad") +
						((entidad==-1) ? "and ((ad.entidad in (11130004,11130010,11130014,11130017,11130018,11130019,11140021) and ad.unidad_ejecutora=0) or  " + 
								"        (ad.entidad not in (11130004,11130010,11130014,11130017,11130018,11130019,11140021) and ad.unidad_ejecutora>0)) " : "") +
						"	group by " + 
						customSelector(agrupador,"ad")  + 
						
						" union " + 
						
						"select  " + 
						customSelector(selector,"p")  + 
						", 0.0, 0.0,0.0, sum(recomendado) " + 
						"from sicoinprod.fp_p6_partidas p " + 
						"where ejercicio = ? " + 
						customCondicion(condicion,"p") +
						" group by " + 
						customSelector(agrupador,"p")  + 
						 ") t1 " + 
						(entidad==-1 ? ", cg_entidades e " : (unidad_ejecutora==-1 ? ", cg_entidades ue " : (programa==-1 ? ", cp_estructuras p " : (grupo==-1 ? ", cp_objetos_gasto g " : 
							(subgrupo==-1 ? ", cp_objetos_gasto sg " : ", cp_objetos_gasto r " )))) ) +
						" where " +
						(entidad==-1 ? "e.ejercicio="+ejercicio+" and e.entidad=t1.entidad and e.unidad_ejecutora=0 " : 
						(unidad_ejecutora==-1 ? "ue.ejercicio="+ejercicio+" and ue.entidad="+entidad+" and ue.unidad_ejecutora=t1.unidad_ejecutora " : 
						(programa==-1 ? "p.ejercicio="+ejercicio+" and p.entidad="+entidad+" and p.unidad_ejecutora="+unidad_ejecutora+" and p.programa=t1.programa and p.nivel_estructura=2 " : 
						(grupo==-1 ? "g.ejercicio="+ejercicio+" and g.renglon=t1.grupo*100 " : 
						(subgrupo==-1 ? "sg.ejercicio="+ejercicio+" and sg.renglon=t1.subgrupo*10 " : 
						(subgrupo>-1 ? "r.ejercicio="+ejercicio+" and r.renglon=t1.renglon" : "")))))) +
						" group by " + 
						grupo_padre +
						" order by " + 
						((entidad==-1) ? "t1.entidad" : 
						((unidad_ejecutora==-1) ? "t1.unidad_ejecutora" : 
						((programa==-1) ? "t1.programa" : 
						((grupo==-1) ? "t1.grupo" : ((subgrupo==-1) ? "t1.subgrupo" : ((subgrupo>-1) ? "t1.renglon" : ""))))));
				PreparedStatement pstm =  conn.prepareStatement(sql);
				pstm.setInt(1, ejercicio-2);
				pstm.setInt(2, ejercicio-1);
				pstm.setInt(3, ejercicio-1);
				pstm.setInt(4, ejercicio-1);
				pstm.setInt(5, ejercicio);
				ResultSet rs = pstm.executeQuery();
				int nivel = subgrupo>-1 ? 6 : (grupo>-1 ?  5 : (programa>-1 ? 4 : (unidad_ejecutora>-1 ? 3 : (entidad>-1 ? 2 : 1))));
				while(rs.next()) {
					CInstitucionalTotalDetalle temp = new CInstitucionalTotalDetalle(rs.getInt("codigo"), rs.getString("nombre").toLowerCase(), 
							rs.getDouble("ejecutado_dos_antes"), rs.getDouble("aprobado_anterior"), rs.getDouble("aprobado_anterior_mas_amp"), 
							rs.getDouble("recomendado"), nivel);
					ret.add(temp);
				}
					
			}
		}
		catch(Exception e){
			CLogger.write("7", CInstitucionalDAO.class, e);
		}
		finally{
			CDatabaseOracle.close(conn);
		}
		return ret.size()>0 ? ret : null;
	}
	
	public static ArrayList<CInstitucionalTipoGastoDetalle> getInstitucionalTipoGastoDetalleEntidad(int ejercicio){
		ArrayList<CInstitucionalTipoGastoDetalle> ret = new ArrayList<CInstitucionalTipoGastoDetalle>();
		Connection conn = null;
		String sql;
		try {
			conn = CDatabaseOracle.connect();
			if(!conn.isClosed()){
				sql = "SELECT p.ejercicio,  " + 
						"     p.entidad codigo,  " + 
						"     e.nombre  nombre,    " + 
						"     SUM (p.recomendado) recomendado_total,  " + 
						"     sum(case when p.tipo_presupuesto=11 then p.recomendado end) tp11,  " + 
						"     sum(case when p.tipo_presupuesto=12 then p.recomendado end) tp12,  " + 
						"     sum(case when p.tipo_presupuesto=13 then p.recomendado end) tp13,  " + 
						"     sum(case when p.tipo_presupuesto=21 then p.recomendado end) tp21,  " + 
						"     sum(case when p.tipo_presupuesto=22 then p.recomendado end) tp22,  " + 
						"     sum(case when p.tipo_presupuesto=23 then p.recomendado end) tp23,  " + 
						"     sum(case when p.tipo_presupuesto=31 then p.recomendado end) tp31  " + 
						"FROM fp_p6_partidas p, cg_entidades e " + 
						"WHERE     p.ejercicio = ?  " + 
						"     AND p.entidad = e.entidad " + 
						"     AND e.ejercicio = p.ejercicio " + 
						"     AND e.unidad_ejecutora=0 " +
						"GROUP BY p.ejercicio, p.entidad, e.nombre " + 
						"ORDER BY p.entidad";
				PreparedStatement pstm =  conn.prepareStatement(sql);
				pstm.setInt(1, ejercicio);
				ResultSet rs = pstm.executeQuery();
				int nivel = 1;
				while(rs.next()) {
					CInstitucionalTipoGastoDetalle entidad = new CInstitucionalTipoGastoDetalle(ejercicio, rs.getInt("codigo"), rs.getString("nombre").toLowerCase(), rs.getDouble("recomendado_total"),
							rs.getDouble("tp11"),rs.getDouble("tp12"),rs.getDouble("tp13"),rs.getDouble("tp21"),rs.getDouble("tp22"),rs.getDouble("tp23"),rs.getDouble("tp31"), nivel);
					ret.add(entidad);
				}
			}
		}
		catch(Exception e){
			CLogger.write("8", CInstitucionalDAO.class, e);
		}
		finally{
			CDatabaseOracle.close(conn);
		}
		return ret.size()>0 ? ret : null;
	}
	
	public static ArrayList<CInstitucionalTipoGastoDetalle> getInstitucionalTipoGastoDetalleUE(int ejercicio, int entidad){
		ArrayList<CInstitucionalTipoGastoDetalle> ret = new ArrayList<CInstitucionalTipoGastoDetalle>();
		Connection conn = null;
		String sql;
		try {
			conn = CDatabaseOracle.connect();
			if(!conn.isClosed()){
				sql = "SELECT p.ejercicio,   " + 
						"     p.unidad_ejecutora codigo, " + 
						"     ue.nombre nombre,   " + 
						"     SUM (p.recomendado) recomendado_total,   " + 
						"     sum(case when p.tipo_presupuesto=11 then p.recomendado end) tp11,   " + 
						"     sum(case when p.tipo_presupuesto=12 then p.recomendado end) tp12,   " + 
						"     sum(case when p.tipo_presupuesto=13 then p.recomendado end) tp13,   " + 
						"     sum(case when p.tipo_presupuesto=21 then p.recomendado end) tp21,   " + 
						"     sum(case when p.tipo_presupuesto=22 then p.recomendado end) tp22,   " + 
						"     sum(case when p.tipo_presupuesto=23 then p.recomendado end) tp23,   " + 
						"     sum(case when p.tipo_presupuesto=31 then p.recomendado end) tp31   " + 
						"FROM fp_p6_partidas p " + 
						"    , cg_entidades ue " + 
						"WHERE     p.ejercicio = ? " + 
						"     AND p.entidad = ? " + 
						"     AND ue.entidad = p.entidad " + 
						"     AND ue.ejercicio = p.ejercicio " + 
						"     AND ue.unidad_ejecutora = p.unidad_ejecutora " + 
						"GROUP BY p.ejercicio, p.unidad_ejecutora, ue.nombre " + 
						"ORDER BY p.unidad_ejecutora";
				PreparedStatement pstm =  conn.prepareStatement(sql);
				pstm.setInt(1, ejercicio);
				pstm.setInt(2, entidad);
				ResultSet rs = pstm.executeQuery();
				int nivel = 2;
				while(rs.next()) {
					CInstitucionalTipoGastoDetalle entidadObt = new CInstitucionalTipoGastoDetalle(ejercicio, rs.getInt("codigo"), rs.getString("nombre").toLowerCase(), rs.getDouble("recomendado_total"),
							rs.getDouble("tp11"),rs.getDouble("tp12"),rs.getDouble("tp13"),rs.getDouble("tp21"),rs.getDouble("tp22"),rs.getDouble("tp23"),rs.getDouble("tp31"), nivel);
					ret.add(entidadObt);
				}
			}
		}
		catch(Exception e){
			CLogger.write("9", CInstitucionalDAO.class, e);
		}
		finally{
			CDatabaseOracle.close(conn);
		}
		return ret.size()>0 ? ret : null;
	}
	
	public static ArrayList<CInstitucionalTipoGastoDetalle> getInstitucionalTipoGastoDetallePrograma(int ejercicio, int entidad, int unidad_ejecutora){
		ArrayList<CInstitucionalTipoGastoDetalle> ret = new ArrayList<CInstitucionalTipoGastoDetalle>();
		Connection conn = null;
		String sql;
		try {
			conn = CDatabaseOracle.connect();
			if(!conn.isClosed()){
				sql = "SELECT p.ejercicio,         " + 
						"    p.programa codigo, " + 
						"    pr.nom_estructura nombre,         " + 
						"    SUM (p.recomendado) recomendado_total,         " + 
						"    sum(case when p.tipo_presupuesto=11 then p.recomendado end) tp11,         " + 
						"    sum(case when p.tipo_presupuesto=12 then p.recomendado end) tp12,         " + 
						"    sum(case when p.tipo_presupuesto=13 then p.recomendado end) tp13,         " + 
						"    sum(case when p.tipo_presupuesto=21 then p.recomendado end) tp21,         " + 
						"    sum(case when p.tipo_presupuesto=22 then p.recomendado end) tp22,         " + 
						"    sum(case when p.tipo_presupuesto=23 then p.recomendado end) tp23,         " + 
						"    sum(case when p.tipo_presupuesto=31 then p.recomendado end) tp31    " + 
						"FROM fp_p6_partidas p, cp_estructuras pr  " + 
						"WHERE     p.ejercicio = ?       " + 
						"    AND p.entidad = ? " + 
						"    AND p.unidad_ejecutora = ?  " + 
						"    AND pr.entidad = p.entidad " + 
						"    AND pr.unidad_ejecutora = p.unidad_ejecutora " + 
						"    AND pr.programa = p.programa " + 
						"    AND pr.ejercicio = p.ejercicio " + 
						"    AND pr.nivel_estructura = 2 " +
						"GROUP BY p.ejercicio, p.programa, pr.nom_estructura  " + 
						"ORDER BY p.programa";
				PreparedStatement pstm =  conn.prepareStatement(sql);
				pstm.setInt(1, ejercicio);
				pstm.setInt(2, entidad);
				pstm.setInt(3, unidad_ejecutora);
				ResultSet rs = pstm.executeQuery();
				int nivel = 3;
				while(rs.next()) {
					CInstitucionalTipoGastoDetalle entidadObt = new CInstitucionalTipoGastoDetalle(ejercicio, rs.getInt("codigo"), rs.getString("nombre").toLowerCase(), rs.getDouble("recomendado_total"),
							rs.getDouble("tp11"),rs.getDouble("tp12"),rs.getDouble("tp13"),rs.getDouble("tp21"),rs.getDouble("tp22"),rs.getDouble("tp23"),rs.getDouble("tp31"), nivel);
					ret.add(entidadObt);
				}
			}
		}
		catch(Exception e){
			CLogger.write("10", CInstitucionalDAO.class, e);
		}
		finally{
			CDatabaseOracle.close(conn);
		}
		return ret.size()>0 ? ret : null;
	}
	
	public static ArrayList<CInstitucionalTipoGastoDetalle> getInstitucionalTipoGastoDetalleGrupo(int ejercicio, int entidad, int unidad_ejecutora,
			int programa){
		ArrayList<CInstitucionalTipoGastoDetalle> ret = new ArrayList<CInstitucionalTipoGastoDetalle>();
		Connection conn = null;
		String sql;
		try {
			conn = CDatabaseOracle.connect();
			if(!conn.isClosed()){
				sql = "SELECT p.ejercicio,   " + 
						"     (p.renglon-mod(p.renglon,100))/100 codigo, " + 
						"     g.nombre nombre, " + 
						"     SUM (p.recomendado) recomendado_total,   " + 
						"     sum(case when p.tipo_presupuesto=11 then p.recomendado end) tp11,   " + 
						"     sum(case when p.tipo_presupuesto=12 then p.recomendado end) tp12,   " + 
						"     sum(case when p.tipo_presupuesto=13 then p.recomendado end) tp13,   " + 
						"     sum(case when p.tipo_presupuesto=21 then p.recomendado end) tp21,   " + 
						"     sum(case when p.tipo_presupuesto=22 then p.recomendado end) tp22,   " + 
						"     sum(case when p.tipo_presupuesto=23 then p.recomendado end) tp23,   " + 
						"     sum(case when p.tipo_presupuesto=31 then p.recomendado end) tp31   " + 
						"FROM fp_p6_partidas p, cp_objetos_gasto g " + 
						"WHERE     p.ejercicio = ?   " + 
						"     AND p.entidad = ? " + 
						"     AND p.unidad_ejecutora = ? " + 
						"     AND p.programa = ? " + 
						"     AND g.renglon = ((p.renglon-mod(p.renglon,100))/100) * 100 " + 
						"     AND g.ejercicio = p.ejercicio " + 
						"GROUP BY p.ejercicio, ((p.renglon-mod(p.renglon,100))/100), g.nombre " + 
						"ORDER BY ((p.renglon-mod(p.renglon,100))/100)";
				PreparedStatement pstm =  conn.prepareStatement(sql);
				pstm.setInt(1, ejercicio);
				pstm.setInt(2, entidad);
				pstm.setInt(3, unidad_ejecutora);
				pstm.setInt(4, programa);
				ResultSet rs = pstm.executeQuery();
				int nivel = 4;
				while(rs.next()) {
					CInstitucionalTipoGastoDetalle entidadObt = new CInstitucionalTipoGastoDetalle(ejercicio, rs.getInt("codigo"), rs.getString("nombre").toLowerCase(), rs.getDouble("recomendado_total"),
							rs.getDouble("tp11"),rs.getDouble("tp12"),rs.getDouble("tp13"),rs.getDouble("tp21"),rs.getDouble("tp22"),rs.getDouble("tp23"),rs.getDouble("tp31"), nivel);
					ret.add(entidadObt);
				}
			}
		}
		catch(Exception e){
			CLogger.write("10", CInstitucionalDAO.class, e);
		}
		finally{
			CDatabaseOracle.close(conn);
		}
		return ret.size()>0 ? ret : null;
	}
	
	public static ArrayList<CInstitucionalTipoGastoDetalle> getInstitucionalTipoGastoDetalleSubGrupo(int ejercicio, int entidad, int unidad_ejecutora,
			int programa, int grupo){
		ArrayList<CInstitucionalTipoGastoDetalle> ret = new ArrayList<CInstitucionalTipoGastoDetalle>();
		Connection conn = null;
		String sql;
		try {
			conn = CDatabaseOracle.connect();
			if(!conn.isClosed()){
				sql = "SELECT p.ejercicio,   " + 
						"     (p.renglon-mod(p.renglon,10))/10 codigo, " + 
						"     g.nombre nombre, " + 
						"     SUM (p.recomendado) recomendado_total,   " + 
						"     sum(case when p.tipo_presupuesto=11 then p.recomendado end) tp11,   " + 
						"     sum(case when p.tipo_presupuesto=12 then p.recomendado end) tp12,   " + 
						"     sum(case when p.tipo_presupuesto=13 then p.recomendado end) tp13,   " + 
						"     sum(case when p.tipo_presupuesto=21 then p.recomendado end) tp21,   " + 
						"     sum(case when p.tipo_presupuesto=22 then p.recomendado end) tp22,   " + 
						"     sum(case when p.tipo_presupuesto=23 then p.recomendado end) tp23,   " + 
						"     sum(case when p.tipo_presupuesto=31 then p.recomendado end) tp31   " + 
						"FROM fp_p6_partidas p, cp_objetos_gasto g " + 
						"WHERE     p.ejercicio = ?   " + 
						"     AND p.entidad = ? " + 
						"     AND p.unidad_ejecutora = ? " + 
						"     AND p.programa = ? " + 
						"     AND g.grupo_gasto = ?*100" + 
						"     AND g.renglon = ((p.renglon-mod(p.renglon,10))/10) * 10 " + 
						"     AND g.ejercicio = p.ejercicio " + 
						"GROUP BY p.ejercicio, ((p.renglon-mod(p.renglon,10))/10), g.nombre " + 
						"ORDER BY (p.renglon-mod(p.renglon,10))/10";
				PreparedStatement pstm =  conn.prepareStatement(sql);
				pstm.setInt(1, ejercicio);
				pstm.setInt(2, entidad);
				pstm.setInt(3, unidad_ejecutora);
				pstm.setInt(4, programa);
				pstm.setInt(5, grupo);
				ResultSet rs = pstm.executeQuery();
				int nivel = 5;
				while(rs.next()) {
					CInstitucionalTipoGastoDetalle entidadObt = new CInstitucionalTipoGastoDetalle(ejercicio, rs.getInt("codigo"), rs.getString("nombre").toLowerCase(), rs.getDouble("recomendado_total"),
							rs.getDouble("tp11"),rs.getDouble("tp12"),rs.getDouble("tp13"),rs.getDouble("tp21"),rs.getDouble("tp22"),rs.getDouble("tp23"),rs.getDouble("tp31"), nivel);
					ret.add(entidadObt);
				}
			}
		}
		catch(Exception e){
			CLogger.write("11", CInstitucionalDAO.class, e);
		}
		finally{
			CDatabaseOracle.close(conn);
		}
		return ret.size()>0 ? ret : null;
	}
	
	public static ArrayList<CInstitucionalTipoGastoDetalle> getInstitucionalTipoGastoDetalleRenglon(int ejercicio, int entidad, int unidad_ejecutora,
			int programa, int grupo, int subgrupo){
		ArrayList<CInstitucionalTipoGastoDetalle> ret = new ArrayList<CInstitucionalTipoGastoDetalle>();
		Connection conn = null;
		String sql;
		try {
			conn = CDatabaseOracle.connect();
			if(!conn.isClosed()){
				sql = "SELECT p.ejercicio,   " +
						"     p.renglon codigo, " +
						"     g.nombre nombre, " +
						"     SUM (p.recomendado) recomendado_total,   " +
						"     sum(case when p.tipo_presupuesto=11 then p.recomendado end) tp11,   " +
						"     sum(case when p.tipo_presupuesto=12 then p.recomendado end) tp12,   " +
						"     sum(case when p.tipo_presupuesto=13 then p.recomendado end) tp13,   " +
						"     sum(case when p.tipo_presupuesto=21 then p.recomendado end) tp21,   " +
						"     sum(case when p.tipo_presupuesto=22 then p.recomendado end) tp22,   " +
						"     sum(case when p.tipo_presupuesto=23 then p.recomendado end) tp23,   " +
						"     sum(case when p.tipo_presupuesto=31 then p.recomendado end) tp31   " +
						"FROM fp_p6_partidas p, cp_objetos_gasto g " +
						"WHERE     p.ejercicio = ?   " +
						"     AND p.entidad = ? " +
						"     AND p.unidad_ejecutora = ? " +
						"     AND p.programa = ? " +
						"     AND g.renglon = p.renglon " +
						"     AND ((p.renglon-mod(p.renglon,100))/100)= ? " +
						"     AND ((p.renglon-mod(p.renglon,10))/10)= ? " +
						"     AND g.ejercicio = p.ejercicio " +
						"GROUP BY p.ejercicio, p.renglon, g.nombre " +
						"ORDER BY p.renglon";
				PreparedStatement pstm =  conn.prepareStatement(sql);
				pstm.setInt(1, ejercicio);
				pstm.setInt(2, entidad);
				pstm.setInt(3, unidad_ejecutora);
				pstm.setInt(4, programa);
				pstm.setInt(5, grupo);
				pstm.setInt(6, subgrupo);
				ResultSet rs = pstm.executeQuery();
				int nivel = 6;
				while(rs.next()) {
					CInstitucionalTipoGastoDetalle entidadObt = new CInstitucionalTipoGastoDetalle(ejercicio, rs.getInt("codigo"), rs.getString("nombre").toLowerCase(), rs.getDouble("recomendado_total"),
							rs.getDouble("tp11"),rs.getDouble("tp12"),rs.getDouble("tp13"),rs.getDouble("tp21"),rs.getDouble("tp22"),rs.getDouble("tp23"),rs.getDouble("tp31"), nivel);
					ret.add(entidadObt);
				}
			}
		}
		catch(Exception e){
			CLogger.write("12", CInstitucionalDAO.class, e);
		}
		finally{
			CDatabaseOracle.close(conn);
		}
		return ret.size()>0 ? ret : null;
	}
	
	public static ArrayList<CInstitucionalFinalidadDetalle> getInstitucionalFinalidadDetalleEntidad(int ejercicio){
		ArrayList<CInstitucionalFinalidadDetalle> ret = new ArrayList<CInstitucionalFinalidadDetalle>();
		Connection conn = null;
		String sql;
		try {
			conn = CDatabaseOracle.connect();
			if(!conn.isClosed()){
				sql = "SELECT p.ejercicio,  " + 
						"    p.entidad codigo , " + 
						"    e.nombre nombre,  " + 
						"    SUM (p.recomendado) recomendado_total,  " + 
						"    sum(case when p.funcion between 10000 and 19999 then p.recomendado end) f01,  " + 
						"    sum(case when p.funcion between 20000 and 29999 then p.recomendado end) f02,  " + 
						"    sum(case when p.funcion between 30000 and 39999 then p.recomendado end) f03,  " + 
						"    sum(case when p.funcion between 40000 and 49999 then p.recomendado end) f04,  " + 
						"    sum(case when p.funcion between 50000 and 59999 then p.recomendado end) f05,  " + 
						"    sum(case when p.funcion between 60000 and 69999 then p.recomendado end) f06,  " + 
						"    sum(case when p.funcion between 70000 and 79999 then p.recomendado end) f07,  " + 
						"    sum(case when p.funcion between 80000 and 89999 then p.recomendado end) f08,  " + 
						"    sum(case when p.funcion between 90000 and 99999 then p.recomendado end) f09,  " + 
						"    sum(case when p.funcion between 100000 and 109999 then p.recomendado end) f10,  " + 
						"    sum(case when p.funcion between 110000 and 119999 then p.recomendado end) f11,  " + 
						"sum(case when p.funcion between 120000 and 129999 then p.recomendado end) f12  " + 
						"FROM fp_p6_partidas p, cg_entidades e  " + 
						"WHERE     p.ejercicio = ?  " + 
						"     AND e.entidad = p.entidad " + 
						"     AND e.ejercicio = p.ejercicio " + 
						"     AND e.unidad_ejecutora=0 " +
						"GROUP BY p.ejercicio, p.entidad, e.nombre  " + 
						"ORDER BY p.entidad";
				PreparedStatement pstm =  conn.prepareStatement(sql);
				pstm.setInt(1, ejercicio);
				ResultSet rs = pstm.executeQuery();
				int nivel = 1;
				while(rs.next()){
					CInstitucionalFinalidadDetalle entidad = new CInstitucionalFinalidadDetalle(ejercicio, rs.getInt("codigo"), rs.getString("nombre").toLowerCase(),
							rs.getDouble("recomendado_total"), rs.getDouble("f01"), rs.getDouble("f02"),rs.getDouble("f03"),rs.getDouble("f04"),rs.getDouble("f05"),
							rs.getDouble("f06"),rs.getDouble("f07"),rs.getDouble("f08"),rs.getDouble("f09"),rs.getDouble("f10"),rs.getDouble("f11"),rs.getDouble("f12"),
							nivel);
					ret.add(entidad);
				}
			}
		}
		catch(Exception e){
			CLogger.write("13", CInstitucionalDAO.class, e);
		}
		finally{
			CDatabaseOracle.close(conn);
		}
		return ret.size()>0 ? ret : null;
	}
	
	public static ArrayList<CInstitucionalFinalidadDetalle> getInstitucionalFinalidadDetalleUE(int ejercicio, int entidad){
		ArrayList<CInstitucionalFinalidadDetalle> ret = new ArrayList<CInstitucionalFinalidadDetalle>();
		Connection conn = null;
		String sql;
		try {
			conn = CDatabaseOracle.connect();
			if(!conn.isClosed()){
				sql = "SELECT p.ejercicio,  " +
						"    p.unidad_ejecutora codigo,  " +
						"    ue.nombre, " +
						"    SUM (p.recomendado) recomendado_total,  " +
						"    sum(case when p.funcion between 10000 and 19999 then p.recomendado end) f01,  " +
						"    sum(case when p.funcion between 20000 and 29999 then p.recomendado end) f02,  " +
						"    sum(case when p.funcion between 30000 and 39999 then p.recomendado end) f03,  " +
						"    sum(case when p.funcion between 40000 and 49999 then p.recomendado end) f04,  " +
						"    sum(case when p.funcion between 50000 and 59999 then p.recomendado end) f05,  " +
						"    sum(case when p.funcion between 60000 and 69999 then p.recomendado end) f06,  " +
						"    sum(case when p.funcion between 70000 and 79999 then p.recomendado end) f07,  " +
						"    sum(case when p.funcion between 80000 and 89999 then p.recomendado end) f08,  " +
						"    sum(case when p.funcion between 90000 and 99999 then p.recomendado end) f09,  " +
						"    sum(case when p.funcion between 100000 and 109999 then p.recomendado end) f10,  " +
						"    sum(case when p.funcion between 110000 and 119999 then p.recomendado end) f11,  " +
						"sum(case when p.funcion between 120000 and 129999 then p.recomendado end) f12  " +
						"FROM fp_p6_partidas p " +
						"    , cg_entidades ue  " +
						"WHERE     p.ejercicio = ?  " +
						"AND p.entidad = ? " +
						"AND ue.entidad = p.entidad " +
						"AND ue.ejercicio = p.ejercicio " +
						"AND ue.unidad_ejecutora = p.unidad_ejecutora " +
						"GROUP BY p.ejercicio, p.unidad_ejecutora, ue.nombre " +
						"ORDER BY p.unidad_ejecutora";
				PreparedStatement pstm =  conn.prepareStatement(sql);
				pstm.setInt(1, ejercicio);
				pstm.setInt(2, entidad);
				ResultSet rs = pstm.executeQuery();
				int nivel = 2;
				while(rs.next()){
					CInstitucionalFinalidadDetalle entidadObj = new CInstitucionalFinalidadDetalle(ejercicio, rs.getInt("codigo"), rs.getString("nombre").toLowerCase(),
							rs.getDouble("recomendado_total"), rs.getDouble("f01"), rs.getDouble("f02"),rs.getDouble("f03"),rs.getDouble("f04"),rs.getDouble("f05"),
							rs.getDouble("f06"),rs.getDouble("f07"),rs.getDouble("f08"),rs.getDouble("f09"),rs.getDouble("f10"),rs.getDouble("f11"),rs.getDouble("f12"),
							nivel);
					ret.add(entidadObj);
				}
			}
		}
		catch(Exception e){
			CLogger.write("14", CInstitucionalDAO.class, e);
		}
		finally{
			CDatabaseOracle.close(conn);
		}
		return ret.size()>0 ? ret : null;
	}
	
	public static ArrayList<CInstitucionalFinalidadDetalle> getInstitucionalFinalidadDetallePrograma(int ejercicio, int entidad, int unidad_ejecutora){
		ArrayList<CInstitucionalFinalidadDetalle> ret = new ArrayList<CInstitucionalFinalidadDetalle>();
		Connection conn = null;
		String sql;
		try {
			conn = CDatabaseOracle.connect();
			if(!conn.isClosed()){
				sql = "SELECT p.ejercicio,  " + 
						"    p.programa codigo,  " + 
						"    pr.nom_estructura nombre, " + 
						"    SUM (p.recomendado) recomendado_total,  " + 
						"    sum(case when p.funcion between 10000 and 19999 then p.recomendado end) f01,  " + 
						"    sum(case when p.funcion between 20000 and 29999 then p.recomendado end) f02,  " + 
						"    sum(case when p.funcion between 30000 and 39999 then p.recomendado end) f03,  " + 
						"    sum(case when p.funcion between 40000 and 49999 then p.recomendado end) f04,  " + 
						"    sum(case when p.funcion between 50000 and 59999 then p.recomendado end) f05,  " + 
						"    sum(case when p.funcion between 60000 and 69999 then p.recomendado end) f06,  " + 
						"    sum(case when p.funcion between 70000 and 79999 then p.recomendado end) f07,  " + 
						"    sum(case when p.funcion between 80000 and 89999 then p.recomendado end) f08,  " + 
						"    sum(case when p.funcion between 90000 and 99999 then p.recomendado end) f09,  " + 
						"    sum(case when p.funcion between 100000 and 109999 then p.recomendado end) f10,  " + 
						"    sum(case when p.funcion between 110000 and 119999 then p.recomendado end) f11,  " + 
						"sum(case when p.funcion between 120000 and 129999 then p.recomendado end) f12  " + 
						"FROM fp_p6_partidas p " + 
						"    , cp_estructuras pr " + 
						"WHERE     p.ejercicio = ?  " + 
						"AND p.entidad = ? " + 
						"AND p.unidad_ejecutora = ? " + 
						"AND pr.entidad = p.entidad " + 
						"AND pr.unidad_ejecutora = p.unidad_ejecutora " + 
						"AND pr.programa = p.programa " + 
						"AND pr.ejercicio = p.ejercicio " + 
						"AND pr.nivel_estructura = 2 " + 
						"GROUP BY p.ejercicio, p.programa, pr.nom_estructura " + 
						"ORDER BY p.programa";
				PreparedStatement pstm =  conn.prepareStatement(sql);
				pstm.setInt(1, ejercicio);
				pstm.setInt(2, entidad);
				pstm.setInt(3, unidad_ejecutora);
				ResultSet rs = pstm.executeQuery();
				int nivel = 3;
				while(rs.next()){
					CInstitucionalFinalidadDetalle entidadObj = new CInstitucionalFinalidadDetalle(ejercicio, rs.getInt("codigo"), rs.getString("nombre").toLowerCase(),
							rs.getDouble("recomendado_total"), rs.getDouble("f01"), rs.getDouble("f02"),rs.getDouble("f03"),rs.getDouble("f04"),rs.getDouble("f05"),
							rs.getDouble("f06"),rs.getDouble("f07"),rs.getDouble("f08"),rs.getDouble("f09"),rs.getDouble("f10"),rs.getDouble("f11"),rs.getDouble("f12"),
							nivel);
					ret.add(entidadObj);
				}
			}
		}
		catch(Exception e){
			CLogger.write("15", CInstitucionalDAO.class, e);
		}
		finally{
			CDatabaseOracle.close(conn);
		}
		return ret.size()>0 ? ret : null;
	}
	
	public static ArrayList<CInstitucionalFinalidadDetalle> getInstitucionalFinalidadDetalleGrupo(int ejercicio, int entidad, int unidad_ejecutora,
			int programa){
		ArrayList<CInstitucionalFinalidadDetalle> ret = new ArrayList<CInstitucionalFinalidadDetalle>();
		Connection conn = null;
		String sql;
		try {
			conn = CDatabaseOracle.connect();
			if(!conn.isClosed()){
				sql = "SELECT p.ejercicio,  " + 
						"    (p.renglon-mod(p.renglon,100))/100 codigo,  " + 
						"    g.nombre nombre, " + 
						"    SUM (p.recomendado) recomendado_total,  " + 
						"    sum(case when p.funcion between 10000 and 19999 then p.recomendado end) f01,  " + 
						"    sum(case when p.funcion between 20000 and 29999 then p.recomendado end) f02,  " + 
						"    sum(case when p.funcion between 30000 and 39999 then p.recomendado end) f03,  " + 
						"    sum(case when p.funcion between 40000 and 49999 then p.recomendado end) f04,  " + 
						"    sum(case when p.funcion between 50000 and 59999 then p.recomendado end) f05,  " + 
						"    sum(case when p.funcion between 60000 and 69999 then p.recomendado end) f06,  " + 
						"    sum(case when p.funcion between 70000 and 79999 then p.recomendado end) f07,  " + 
						"    sum(case when p.funcion between 80000 and 89999 then p.recomendado end) f08,  " + 
						"    sum(case when p.funcion between 90000 and 99999 then p.recomendado end) f09,  " + 
						"    sum(case when p.funcion between 100000 and 109999 then p.recomendado end) f10,  " + 
						"    sum(case when p.funcion between 110000 and 119999 then p.recomendado end) f11,  " + 
						"sum(case when p.funcion between 120000 and 129999 then p.recomendado end) f12  " + 
						"FROM fp_p6_partidas p " + 
						"    , cp_objetos_gasto g " + 
						"    , cp_estructuras pr " +
						"WHERE     p.ejercicio = ?  " + 
						"AND p.entidad = ? " + 
						"AND p.unidad_ejecutora = ? " + 
						"AND p.programa = ? " + 
						"AND g.renglon = ((p.renglon-mod(p.renglon,100))/100) * 100 " + 
						"AND g.ejercicio = p.ejercicio " + 
						"AND pr.entidad = p.entidad " + 
						"AND pr.unidad_ejecutora = p.unidad_ejecutora " + 
						"AND pr.programa = p.programa " + 
						"AND pr.ejercicio = p.ejercicio  " +
						"AND pr.nivel_estructura = 2 " +
						"GROUP BY p.ejercicio, ((p.renglon-mod(p.renglon,100))/100), g.nombre " + 
						"ORDER BY ((p.renglon-mod(p.renglon,100))/100)";
				PreparedStatement pstm =  conn.prepareStatement(sql);
				pstm.setInt(1, ejercicio);
				pstm.setInt(2, entidad);
				pstm.setInt(3, unidad_ejecutora);
				pstm.setInt(4, programa);
				ResultSet rs = pstm.executeQuery();
				int nivel = 4;
				while(rs.next()){
					CInstitucionalFinalidadDetalle entidadObj = new CInstitucionalFinalidadDetalle(ejercicio, rs.getInt("codigo"), rs.getString("nombre").toLowerCase(),
							rs.getDouble("recomendado_total"), rs.getDouble("f01"), rs.getDouble("f02"),rs.getDouble("f03"),rs.getDouble("f04"),rs.getDouble("f05"),
							rs.getDouble("f06"),rs.getDouble("f07"),rs.getDouble("f08"),rs.getDouble("f09"),rs.getDouble("f10"),rs.getDouble("f11"),rs.getDouble("f12"),
							nivel);
					ret.add(entidadObj);
				}
			}
		}
		catch(Exception e){
			CLogger.write("16", CInstitucionalDAO.class, e);
		}
		finally{
			CDatabaseOracle.close(conn);
		}
		return ret.size()>0 ? ret : null;
	}
	
	public static ArrayList<CInstitucionalFinalidadDetalle> getInstitucionalFinalidadDetalleSubGrupo(int ejercicio, int entidad, int unidad_ejecutora,
			int programa, int grupo){
		ArrayList<CInstitucionalFinalidadDetalle> ret = new ArrayList<CInstitucionalFinalidadDetalle>();
		Connection conn = null;
		String sql;
		try {
			conn = CDatabaseOracle.connect();
			if(!conn.isClosed()){
				sql = "SELECT p.ejercicio,  " +
						"    (p.renglon-mod(p.renglon,10))/10 codigo,  " +
						"    g.nombre nombre, " +
						"    SUM (p.recomendado) recomendado_total,  " +
						"    sum(case when p.funcion between 10000 and 19999 then p.recomendado end) f01,  " +
						"    sum(case when p.funcion between 20000 and 29999 then p.recomendado end) f02,  " +
						"    sum(case when p.funcion between 30000 and 39999 then p.recomendado end) f03,  " +
						"    sum(case when p.funcion between 40000 and 49999 then p.recomendado end) f04,  " +
						"    sum(case when p.funcion between 50000 and 59999 then p.recomendado end) f05,  " +
						"    sum(case when p.funcion between 60000 and 69999 then p.recomendado end) f06,  " +
						"    sum(case when p.funcion between 70000 and 79999 then p.recomendado end) f07,  " +
						"    sum(case when p.funcion between 80000 and 89999 then p.recomendado end) f08,  " +
						"    sum(case when p.funcion between 90000 and 99999 then p.recomendado end) f09,  " +
						"    sum(case when p.funcion between 100000 and 109999 then p.recomendado end) f10,  " +
						"    sum(case when p.funcion between 110000 and 119999 then p.recomendado end) f11,  " +
						"sum(case when p.funcion between 120000 and 129999 then p.recomendado end) f12  " +
						"FROM fp_p6_partidas p " +
						"    , cp_objetos_gasto g " +
						"WHERE     p.ejercicio = ?  " +
						"AND p.entidad = ? " +
						"AND p.unidad_ejecutora = ? " +
						"AND p.programa = ? " +
						"AND g.grupo_gasto = ?*100 " +
						"AND g.renglon = ((p.renglon-mod(p.renglon,10))/10) * 10 " +
						"AND g.ejercicio = p.ejercicio " +
						"GROUP BY p.ejercicio, ((p.renglon-mod(p.renglon,10))/10), g.nombre " +
						"ORDER BY ((p.renglon-mod(p.renglon,10))/10)";
				PreparedStatement pstm =  conn.prepareStatement(sql);
				pstm.setInt(1, ejercicio);
				pstm.setInt(2, entidad);
				pstm.setInt(3, unidad_ejecutora);
				pstm.setInt(4, programa);
				pstm.setInt(5, grupo);
				ResultSet rs = pstm.executeQuery();
				int nivel = 5;
				while(rs.next()){
					CInstitucionalFinalidadDetalle entidadObj = new CInstitucionalFinalidadDetalle(ejercicio, rs.getInt("codigo"), rs.getString("nombre").toLowerCase(),
							rs.getDouble("recomendado_total"), rs.getDouble("f01"), rs.getDouble("f02"),rs.getDouble("f03"),rs.getDouble("f04"),rs.getDouble("f05"),
							rs.getDouble("f06"),rs.getDouble("f07"),rs.getDouble("f08"),rs.getDouble("f09"),rs.getDouble("f10"),rs.getDouble("f11"),rs.getDouble("f12"),
							nivel);
					ret.add(entidadObj);
				}
			}
		}
		catch(Exception e){
			CLogger.write("17", CInstitucionalDAO.class, e);
		}
		finally{
			CDatabaseOracle.close(conn);
		}
		return ret.size()>0 ? ret : null;
	}
	
	public static ArrayList<CInstitucionalFinalidadDetalle> getInstitucionalFinalidadDetalleRenglon(int ejercicio, int entidad, int unidad_ejecutora,
			int programa, int grupo, int subgrupo){
		ArrayList<CInstitucionalFinalidadDetalle> ret = new ArrayList<CInstitucionalFinalidadDetalle>();
		Connection conn = null;
		String sql;
		try {
			conn = CDatabaseOracle.connect();
			if(!conn.isClosed()){
				sql = "SELECT p.ejercicio,  " +
						"    p.renglon codigo,  " +
						"    g.nombre nombre, " +
						"    SUM (p.recomendado) recomendado_total,  " +
						"    sum(case when p.funcion between 10000 and 19999 then p.recomendado end) f01,  " +
						"    sum(case when p.funcion between 20000 and 29999 then p.recomendado end) f02,  " +
						"    sum(case when p.funcion between 30000 and 39999 then p.recomendado end) f03,  " +
						"    sum(case when p.funcion between 40000 and 49999 then p.recomendado end) f04,  " +
						"    sum(case when p.funcion between 50000 and 59999 then p.recomendado end) f05,  " +
						"    sum(case when p.funcion between 60000 and 69999 then p.recomendado end) f06,  " +
						"    sum(case when p.funcion between 70000 and 79999 then p.recomendado end) f07,  " +
						"    sum(case when p.funcion between 80000 and 89999 then p.recomendado end) f08,  " +
						"    sum(case when p.funcion between 90000 and 99999 then p.recomendado end) f09,  " +
						"    sum(case when p.funcion between 100000 and 109999 then p.recomendado end) f10,  " +
						"    sum(case when p.funcion between 110000 and 119999 then p.recomendado end) f11,  " +
						"sum(case when p.funcion between 120000 and 129999 then p.recomendado end) f12  " +
						"FROM fp_p6_partidas p " +
						"    , cp_objetos_gasto g " +
						"WHERE     p.ejercicio = ?  " +
						"AND p.entidad = ? " +
						"AND p.unidad_ejecutora = ? " +
						"AND p.programa = ? " +
						"AND g.renglon = p.renglon " +
						"AND ((p.renglon-mod(p.renglon,100))/100)= ? " +
						"AND ((p.renglon-mod(p.renglon,10))/10)= ? " +
						"AND g.ejercicio = p.ejercicio " +
						"GROUP BY p.ejercicio, p.renglon, g.nombre " +
						"ORDER BY p.renglon";
				PreparedStatement pstm =  conn.prepareStatement(sql);
				pstm.setInt(1, ejercicio);
				pstm.setInt(2, entidad);
				pstm.setInt(3, unidad_ejecutora);
				pstm.setInt(4, programa);
				pstm.setInt(5, grupo);
				pstm.setInt(6, subgrupo);
				ResultSet rs = pstm.executeQuery();
				int nivel = 6;
				while(rs.next()){
					CInstitucionalFinalidadDetalle entidadObj = new CInstitucionalFinalidadDetalle(ejercicio, rs.getInt("codigo"), rs.getString("nombre").toLowerCase(),
							rs.getDouble("recomendado_total"), rs.getDouble("f01"), rs.getDouble("f02"),rs.getDouble("f03"),rs.getDouble("f04"),rs.getDouble("f05"),
							rs.getDouble("f06"),rs.getDouble("f07"),rs.getDouble("f08"),rs.getDouble("f09"),rs.getDouble("f10"),rs.getDouble("f11"),rs.getDouble("f12"),
							nivel);
					ret.add(entidadObj);
				}
			}
		}
		catch(Exception e){
			CLogger.write("18", CInstitucionalDAO.class, e);
		}
		finally{
			CDatabaseOracle.close(conn);
		}
		return ret.size()>0 ? ret : null;
	}
	
	public static Double[] getTotalesInstitucional(int ejercicio){
		Double[] ret = new Double[3];
		Connection conn = null;
		try{
			conn = CDatabaseOracle.connect();
			if(!conn.isClosed()){
				PreparedStatement pstm =  conn.prepareStatement("select sum(ejecutado_dos_antes) ejecutado_dos_antes " + 
						"    , sum(aprobado_anterior) aprobado_anterior " + 
						"    , sum(aprobado_anterior + aprobado_anterior_amp) aprobado_anterior_mas_amp " + 
						"    , sum(recomendado) recomendado  " + 
						"from (  " + 
						"    select sum(gd.monto_renglon) ejecutado_dos_antes, 0.0 aprobado_anterior, 0.0 aprobado_anterior_amp, 0.0 recomendado  " + 
						"    from sicoinprod.eg_gastos_detalle gd, sicoinprod.eg_gastos_hoja gh   " + 
						"    where gh.ejercicio = " + (ejercicio-2) + " and gd.ejercicio = gh.ejercicio and gd.entidad = gh.entidad  " + 
						"        and gd.unidad_ejecutora = gh.unidad_ejecutora and gd.unidad_desconcentrada = gh.unidad_desconcentrada  " + 
						"        and gd.no_cur = gh.no_cur and gh.clase_registro IN ('DEV', 'CYD', 'RDP', 'REG') and gh.estado = 'APROBADO'       " + 
						"    union  " + 
						"    select  0.0,sum(p.asignado), 0.0, 0.0 from sicoinprod.eg_f6_partidas p where p.ejercicio = " + (ejercicio-1) +  
						"        and ((p.entidad in (11130004,11130010,11130014,11130017,11130018,11130019,11140021) and p.unidad_ejecutora=0) or  " + 
						"            (p.entidad not in (11130004,11130010,11130014,11130017,11130018,11130019,11140021) and p.unidad_ejecutora>0))   " + 
						"    union  " + 
						"    SELECT 0.0, 0.0, SUM (ad.monto_aprobado) , 0.0    	  " + 
						"    FROM eg_modificaciones_hoja am, eg_modificaciones_detalle ad      	  " + 
						"    WHERE     am.ejercicio = " + (ejercicio-1) + "  	AND am.clase_registro = 'AMP'      	AND am.estado = 'APROBADO'      	 " + 
						"        AND am.fec_disposicion <= TO_DATE ( " + (ejercicio-1) + " || '/07/15','YYYY/MM/DD')      	AND ad.ejercicio = am.ejercicio      	 " + 
						"        and ad.entidad = am.entidad      	and ad.unidad_ejecutora = am.unidad_ejecutora       " + 
						"        and ad.unidad_desconcentrada = am.unidad_desconcentrada 	and ad.no_cur = am.no_cur           " + 
						"        and ((ad.entidad in (11130004,11130010,11130014,11130017,11130018,11130019,11140021) and ad.unidad_ejecutora=0) or " + 
						"            (ad.entidad not in (11130004,11130010,11130014,11130017,11130018,11130019,11140021) and ad.unidad_ejecutora>0)) " + 
						"  	union  " + 
						"  	select  0.0, 0.0,0.0, sum(recomendado)  " + 
						"  	from sicoinprod.fp_p6_partidas p where ejercicio = " + ejercicio + 
						"  	) t1   ");
				ResultSet rs = pstm.executeQuery();		
				if(rs.next()){
					ret[0] = rs.getDouble("ejecutado_dos_antes");
					ret[1] = rs.getDouble("aprobado_anterior_mas_amp");
					ret[2] = rs.getDouble("recomendado");
				}
			}
		}
		catch(Exception e){
			CLogger.write("19", CInstitucionalDAO.class, e);
		}
		finally{
			CDatabaseOracle.close(conn);
		}
		return ret;
	}
	
	public static ArrayList<CDepartamento> getPresupuestoRecomendadoDepartamentalDetalle(int ejercicio, int departamento, int geografico, 
			int entidad, int unidad_ejecutora, int programa, int grupo, int subgrupo){
		ArrayList<CDepartamento> ret = new ArrayList<CDepartamento>();
		Connection conn = null;
		String sql;
		try {
			conn = CDatabaseOracle.connect();
			if(!conn.isClosed()){
				String selector = "";
				String selectorFrom = "";
				String selectorWhere = "";
				String selectorGroupBy = "";
				String selectorOrderBy = "";
				
				if(departamento==-1 && geografico==-1 && entidad==-1 && unidad_ejecutora==-1 && programa==-1 && grupo==-1 && subgrupo==-1) {
					selector = "cd.codigo_departamento codigo, NVL(cd.nombre_departamento, 'MULTIDEPARTAMENTAL') nombre ";
					selectorWhere = String.join(" ", "and p.geografico = cg.geografico", "and p.ejercicio = cg.ejercicio");
					selectorGroupBy = "cd.codigo_departamento, cd.nombre_departamento";
					selectorOrderBy = "cd.codigo_departamento";
				}
				else if(departamento>-1 && geografico==-1 && entidad==-1 && unidad_ejecutora==-1 && programa==-1 && grupo==-1 && subgrupo==-1) {
					selector = "cg.geografico codigo, cg.nombre nombre ";
					selectorWhere = String.join(" ", (departamento==0 ? "and cd.codigo_departamento is null" : "and cd.codigo_departamento=?"));
					selectorGroupBy = "cg.geografico, cg.nombre";
					selectorOrderBy = "cg.geografico";
				}
				else if(departamento>-1 && geografico>-1 && entidad==-1 && unidad_ejecutora==-1 && programa==-1 && grupo==-1 && subgrupo==-1) {
					selector = "p.entidad codigo, e.nombre nombre ";
					selectorFrom = ", cg_entidades e ";
					selectorWhere = String.join(" ", (departamento==0 ? "and cd.codigo_departamento is null" : "and cd.codigo_departamento=?"), "and cg.geografico=?", "and e.entidad = p.entidad", "and e.ejercicio = p.ejercicio", 
							"and e.unidad_ejecutora=0");
					selectorGroupBy = "p.entidad, e.nombre";
					selectorOrderBy = "p.entidad";
				}
				else if(departamento>-1 && geografico>-1 && entidad>-1 && unidad_ejecutora==-1 && programa==-1 && grupo==-1 && subgrupo==-1) {
					selector = "p.unidad_ejecutora codigo, ue.nombre nombre ";
					selectorFrom = ",   cg_entidades ue ";
					selectorWhere = String.join(" ", (departamento==0 ? "and cd.codigo_departamento is null" : "and cd.codigo_departamento=?"), "and cg.geografico=?","and p.entidad = ?", "and ue.entidad = p.entidad", 
							"and ue.unidad_ejecutora = p.unidad_ejecutora", "and ue.ejercicio = p.ejercicio");
					selectorGroupBy = "p.unidad_ejecutora, ue.nombre";
					selectorOrderBy = "p.unidad_ejecutora";
				}
				else if(departamento>-1 && geografico>-1 && entidad>-1 && unidad_ejecutora>-1 && programa==-1 && grupo==-1 && subgrupo==-1) {
					selector = "p.programa codigo, pr.nom_estructura nombre ";
					selectorFrom = ",   cp_estructuras pr ";
					selectorWhere = String.join(" ", (departamento==0 ? "and cd.codigo_departamento is null" : "and cd.codigo_departamento=?"), "and cg.geografico=?","and p.entidad = ?", "and p.unidad_ejecutora = ?", 
							"and pr.entidad = p.entidad", "and pr.unidad_ejecutora = p.unidad_ejecutora", "and pr.programa = p.programa", "and pr.nivel_estructura = 2",
							"and pr.ejercicio = p.ejercicio");
					selectorGroupBy = "p.programa, pr.nom_estructura";
					selectorOrderBy = "p.programa";
				}
				else if(departamento>-1 && geografico>-1 && entidad>-1 && unidad_ejecutora>-1 && programa>-1 && grupo==-1 && subgrupo==-1) {
					selector = "(p.renglon-mod(p.renglon,100))/100 codigo, g.nombre nombre ";
					selectorFrom = ",   cp_grupos_gasto g ";
					selectorWhere = String.join(" ", (departamento==0 ? "and cd.codigo_departamento is null" : "and cd.codigo_departamento=?"), "and cg.geografico=?","and p.entidad = ?", "and p.unidad_ejecutora = ?", 
							"and p.programa = ?", "and g.grupo_gasto = ((p.renglon-mod(p.renglon,100))/100) * 100", "and g.ejercicio = p.ejercicio");
					selectorGroupBy = "(p.renglon-mod(p.renglon,100))/100, g.nombre";
					selectorOrderBy = "(p.renglon-mod(p.renglon,100))/100";
				}
				else if(departamento>-1 && geografico>-1 && entidad>-1 && unidad_ejecutora>-1 && programa>-1 && grupo>-1 && subgrupo==-1) {
					selector = "(p.renglon-mod(p.renglon,10))/10 codigo, g.nombre nombre ";
					selectorFrom = ",   cp_objetos_gasto g ";
					selectorWhere = String.join(" ", (departamento==0 ? "and cd.codigo_departamento is null" : "and cd.codigo_departamento=?"), "and cg.geografico=?","and p.entidad = ?", "and p.unidad_ejecutora = ?", 
							"and p.programa = ?", "and g.grupo_gasto = ?*100", "and g.renglon = ((p.renglon-mod(p.renglon,10))/10) * 10", "and g.ejercicio = p.ejercicio");
					selectorGroupBy = "((p.renglon-mod(p.renglon,10))/10), g.nombre";
					selectorOrderBy = "((p.renglon-mod(p.renglon,10))/10)";
				}
				else if(departamento>-1 && geografico>-1 && entidad>-1 && unidad_ejecutora>-1 && programa>-1 && grupo>-1 && subgrupo>-1) {
					selector = "p.renglon codigo, g.nombre nombre ";
					selectorFrom = ",   cp_objetos_gasto g ";
					selectorWhere = String.join(" ", (departamento==0 ? "and cd.codigo_departamento is null" : "and cd.codigo_departamento=?"), "and cg.geografico=?","and p.entidad = ?", "and p.unidad_ejecutora = ?", 
							"and p.programa = ?", "and ((p.renglon-mod(p.renglon,100))/100)= ?", "and ((p.renglon-mod(p.renglon,10))/10)= ?", 
							"and g.renglon = p.renglon", "and g.ejercicio = p.ejercicio");
					selectorGroupBy = "p.renglon, g.nombre";
					selectorOrderBy = "p.renglon";
				}
				
				sql = "select " + selector +
					"    , sum(p.recomendado) recomendado " + 
					"from fp_p6_partidas p " + 
					"left outer join cg_geograficos cg on cg.ejercicio = p.ejercicio and cg.geografico = p.geografico " + 
					"left outer join cg_departamentos cd on cd.codigo_departamento = (cg.geografico-mod(cg.geografico,100))/100 " + 
					selectorFrom +
					"where p.ejercicio=? " + selectorWhere + 
					" group by " + selectorGroupBy + 
					" order by " + selectorOrderBy;
				
				PreparedStatement pstm =  conn.prepareStatement(sql);
				int pos = 1;
				pstm.setInt(pos, ejercicio);
				int nivel = 1;
				if(departamento>-1) {					
					if(departamento != 0) {
						pos++;
						pstm.setInt(pos, departamento);
					}
					nivel = 2;
				}				
				if(geografico>-1) {
					pos++;
					pstm.setInt(pos, geografico);
					nivel = 3;
				}
				if(entidad>-1) {
					pos++;
					pstm.setInt(pos, entidad);
					nivel = 4;
				}
				if(unidad_ejecutora>-1) {
					pos++;
					pstm.setInt(pos, unidad_ejecutora);
					nivel = 5;
				}
				if(programa>-1) {
					pos++;
					pstm.setInt(pos, programa);
					nivel = 6;
				}
				if(grupo>-1) {
					pos++;
					pstm.setInt(pos, grupo);
					nivel = 7;
				}
				if(subgrupo>-1) {
					pos++;
					pstm.setInt(pos, subgrupo);
					nivel = 8;
				}
				
				ResultSet rs = pstm.executeQuery();
				
				while(rs.next()){
					CDepartamento temp = new CDepartamento(rs.getInt("codigo"), rs.getString("nombre").toLowerCase(), rs.getDouble("recomendado"), nivel);
					ret.add(temp);
				}
			}
		}catch(Exception e) {
			CLogger.write("20", CInstitucionalDAO.class, e);
		}
		finally{
			CDatabaseOracle.close(conn);
		}
		return ret;
	}
	
	public static String getNombreDepartamento(int departamento){
		String ret = "";
		Connection conn = null;
		String sql;
		try {
			conn = CDatabaseOracle.connect();
			if(!conn.isClosed()){
				sql = "select nombre_departamento from cg_departamentos where codigo_departamento= ?";
				PreparedStatement pstm =  conn.prepareStatement(sql);
				pstm.setInt(1, departamento);
				
				ResultSet rs = pstm.executeQuery();
				while(rs.next()){
					ret = rs.getString("nombre_departamento").toLowerCase();
				}
			}
		}
		catch(Exception e) {
			CLogger.write("20", CInstitucionalDAO.class, e);
		}
		finally{
			CDatabaseOracle.close(conn);
		}
		return ret;
	}
}
