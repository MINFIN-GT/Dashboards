package dao.formulacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import db.utilities.CDatabase;
import db.utilities.CDatabaseOracle;
import pojo.formulacion.CGastoEconomico;
import utilities.CLogger;

public class CGastoDAO {
	public static ArrayList<CGastoEconomico> getGastosTotal(int ejercicio){
		ArrayList<CGastoEconomico> ret = new ArrayList<CGastoEconomico>();
		Connection conn = null;
		Connection conn_mem = null;
		try{
			conn = CDatabaseOracle.connect();
			if(!conn.isClosed()){
				conn_mem = CDatabase.connect();
				PreparedStatement pstm =  conn.prepareStatement("select rf.posicion, rf.texto, rf.nivel, rf.negrillas, sum(g.aprobado) aprobado " + 
						"from sicoinp_hreyes.reporte_formulacion rf " + 
						"left outer join sicoinp_hreyes.reporte_formulacion_detalle rfd " + 
						"on ( " + 
						"    rfd.reporte = rf.reporte " + 
						"    and rfd.posicion = rf.posicion " + 
						") " + 
						"left outer join fp_p6_partidas g on ( " + 
						"    g.ejercicio = ? " + 
						"    and ((rfd.dato_numero = g.economico and rfd.dato_texto = 'E') " + 
						"        or (rfd.dato_numero = g.renglon and rfd.dato_texto = 'RA')) " + 
						"    and (( g.economico = 2112100 and rfd.posicion=10 and g.renglon not in (712,715,738)) or rfd.posicion<>10) " + 
						")  " + 
						"where rf.reporte = 'CUADRO_4' " + 
						"group by rf.posicion, rf.texto, rf.nivel, rf.negrillas " + 
						"order by rf.posicion");
				pstm.setInt(1, ejercicio-1);
				ResultSet rs_aprobado = pstm.executeQuery();
				PreparedStatement pstm_amp =  conn.prepareStatement("select rf.posicion, sum(mo.modificaciones) modificaciones " + 
						"from sicoinp_hreyes.reporte_formulacion rf " + 
						"left outer join sicoinp_hreyes.reporte_formulacion_detalle rfd " + 
						"on ( " + 
						"    rfd.reporte = rf.reporte " + 
						"    and rfd.posicion = rf.posicion " + 
						") " + 
						"left outer join ( " + 
						"select d.economico, d.renglon, sum(d.monto_aprobado) modificaciones " + 
						"						    from eg_modificaciones_hoja g1, eg_modificaciones_detalle d  " + 
						"						    where g1.ejercicio = ?  " + 
						"						    and g1.unidad_ejecutora=0   " + 
						"						    and g1.clase_registro = 'AMP'   " + 
						"						    and g1.fec_disposicion <= TO_DATE ( (g1.ejercicio) || '/07/15','YYYY/MM/DD')  " + 
						"						    and d.ejercicio = g1.ejercicio   " + 
						"						    and d.entidad = g1.entidad   " + 
						"						    and d.unidad_ejecutora = g1.unidad_ejecutora   " + 
						"						    and d.no_cur = g1.no_cur  " + 
						"						    and d.clase_registro = g1.clase_registro   " + 
						"						    group by d.economico, d.renglon " + 
						") mo " + 
						"on ( " + 
						"   ((mo.economico = rfd.dato_numero  and rfd.dato_texto = 'E') " + 
						"        or (mo.renglon = rfd.dato_numero and rfd.dato_texto = 'RA')) " + 
						"    and (( mo.economico = 2112100 and rfd.posicion=10 and mo.renglon not in (712,715,738)) or rfd.posicion<>10)  " + 
						") " + 
						"where rf.reporte = 'CUADRO_4' " + 
						"group by rf.posicion " + 
						"order by rf.posicion");
				pstm_amp.setInt(1, ejercicio-1);
				ResultSet rs_amp = pstm_amp.executeQuery();
				PreparedStatement pstm_recomendado =  conn.prepareStatement("select rf.posicion, sum(g.recomendado) recomendado " + 
						"from sicoinp_hreyes.reporte_formulacion rf " + 
						"left outer join sicoinp_hreyes.reporte_formulacion_detalle rfd " + 
						"on ( " + 
						"    rfd.reporte = rf.reporte " + 
						"    and rfd.posicion = rf.posicion " + 
						") " + 
						"left outer join fp_p6_partidas g on ( " + 
						"    g.ejercicio = ? " + 
						"    and ((rfd.dato_numero = g.economico and rfd.dato_texto = 'E') " + 
						"        or (rfd.dato_numero = g.renglon and rfd.dato_texto = 'RA')) " + 
						"    and (( g.economico = 2112100 and rfd.posicion=10 and g.renglon not in (712,715,738)) or rfd.posicion<>10) " + 
						")  " + 
						"where rf.reporte = 'CUADRO_4' " + 
						"group by rf.posicion " + 
						"order by rf.posicion");
				pstm_recomendado.setInt(1, ejercicio);
				ResultSet rs_recomendado = pstm_recomendado.executeQuery();
				PreparedStatement pstm_mem = conn_mem.prepareStatement("select rfd.posicion, sum(ep.ano_actual) total " + 
						"from reporte_formulacion_detalle rfd, mv_ejecucion_presupuestaria ep " + 
						"where rfd.reporte='CUADRO_4' " + 
						"and ((rfd.dato_numero = ep.economico and rfd.dato_texto = 'E') " + 
						"or (rfd.dato_numero = ep.renglon and rfd.dato_texto = 'RA')) " + 
						"and (( ep.economico = 2112100 and rfd.posicion=10 and ep.renglon not in (712,715,738)) or rfd.posicion<>10) " + 
						"and ep.ejercicio = ? " + 
						"group by rfd.posicion " + 
						"order by rfd.posicion;");
				pstm_mem.setInt(1, ejercicio-2);
				ResultSet rs_mem = pstm_mem.executeQuery();
				rs_mem.next();
				while(rs_aprobado.next() && rs_amp.next() && rs_recomendado.next()){
					Double ejecutado_dos_antes=null;
					if(rs_mem.getRow()>0) {
						if(rs_mem.getDouble("posicion")<rs_aprobado.getInt("posicion")){
							while(rs_mem.getDouble("posicion")<rs_aprobado.getInt("posicion"))
								rs_mem.next();
						}
						if(rs_mem.getDouble("posicion")==rs_aprobado.getInt("posicion"))
							ejecutado_dos_antes = rs_mem.getDouble("total");
					}
					Double ampliaciones = rs_amp.getDouble("modificaciones");
					CGastoEconomico gasto = new CGastoEconomico(rs_aprobado.getString("texto"), rs_aprobado.getInt("posicion"), rs_aprobado.getInt("nivel"),
							ejecutado_dos_antes, rs_aprobado.getDouble("aprobado"), rs_aprobado.getDouble("aprobado") + (ampliaciones!=null ? ampliaciones : 0.0f),
							rs_recomendado.getDouble("recomendado"), rs_aprobado.getInt("negrillas"));
					ret.add(gasto);
				}
			
			}
		}
		catch(Exception e){
			CLogger.write("1", CGastoDAO.class, e);
		}
		finally{
			CDatabaseOracle.close(conn);
		}
		return ret;
	}
}
