package dao.formulacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import db.utilities.CDatabaseOracle;
import pojo.formulacion.CRecursoEconomico;
import utilities.CLogger;

public class CRecursoDAO {
	public static ArrayList<CRecursoEconomico> getRecursosTotal(int ejercicio){
		ArrayList<CRecursoEconomico> ret = new ArrayList<CRecursoEconomico>();
		Connection conn = null;
		try{
			conn = CDatabaseOracle.connect();
			if(!conn.isClosed()){
				PreparedStatement pstm =  conn.prepareStatement("select posicion, texto, nivel,negrillas, ( " + 
						"    select sum(ide.monto_rubro) " + 
						"    from er_ingresos_hoja ih, er_ingresos_detalle ide " + 
						"    where ih.ejercicio = ? " + 
						"    and ih.estado = 'APROBADO' " + 
						"    and ide.ejercicio = ih.ejercicio " + 
						"    and ide.entidad = ih.entidad " + 
						"    and ide.unidad_ejecutora = ih.unidad_ejecutora " + 
						"    and ide.no_cur = ih.no_cur " + 
						"    and ide.recurso in (select rfd.dato_numero  " + 
						"        from reporte_formulacion_detalle rfd " + 
						"        where rfd.reporte = rf.reporte " + 
						"        and rfd.posicion = rf.posicion " + 
						"    ) " + 
						") ejecutado_dos_antes,( " + 
						"    select sum(r.aprobado) " + 
						"    from fp_r1_recursos r " + 
						"    where r.recurso in (select rfd.dato_numero  " + 
						"        from reporte_formulacion_detalle rfd " + 
						"        where rfd.reporte = rf.reporte " + 
						"        and rfd.posicion = rf.posicion " + 
						"    ) " + 
						"    and r.ejercicio = ? " + 
						") aprobado, nvl(( " + 
						"    select sum(r.aprobado) " + 
						"    from fp_r1_recursos r " + 
						"    where r.recurso in (select rfd.dato_numero  " + 
						"        from reporte_formulacion_detalle rfd " + 
						"        where rfd.reporte = rf.reporte " + 
						"        and rfd.posicion = rf.posicion " + 
						"    ) " + 
						"    and r.ejercicio = ? " + 
						"),0) + nvl(( " + 
						"    select sum(d.monto_aprobado) " + 
						"    from er_modificaciones_hoja h, er_modificaciones_detalle d " + 
						"    where h.ejercicio = ? " + 
						"    and h.unidad_ejecutora=0 " + 
						"    and h.clase_registro = 'AMPLI' " + 
						"    and h.fec_disposicion <= TO_DATE ( (h.ejercicio) || '/07/15','YYYY/MM/DD') " + 
						"    and d.ejercicio = h.ejercicio " + 
						"    and d.entidad = h.entidad " + 
						"    and d.unidad_ejecutora = h.unidad_ejecutora " + 
						"    and d.no_cur = h.no_cur " + 
						"    and d.clase_registro = h.clase_registro " + 
						"    and d.recurso in (select rfd.dato_numero  " + 
						"        from reporte_formulacion_detalle rfd " + 
						"        where rfd.reporte = rf.reporte " + 
						"        and rfd.posicion = rf.posicion " + 
						"    ) " + 
						"),0) aprobado_mas_amp,( " + 
						"    select sum(r.recomendado) " + 
						"    from fp_r1_recursos r " + 
						"    where r.recurso in (select rfd.dato_numero  " + 
						"        from reporte_formulacion_detalle rfd " + 
						"        where rfd.reporte = rf.reporte " + 
						"        and rfd.posicion = rf.posicion " + 
						"    ) " + 
						"    and r.ejercicio = ? " + 
						") recomendado " + 
						"from sicoinp_hreyes.reporte_formulacion rf " + 
						"where rf.reporte = 'CUADRO_3' " + 
						"order by posicion");
				pstm.setInt(1, ejercicio-2);
				pstm.setInt(2, ejercicio-1);
				pstm.setInt(3, ejercicio-1);
				pstm.setInt(4, ejercicio-1);
				pstm.setInt(5, ejercicio);
				ResultSet rs = pstm.executeQuery();
				while(rs.next()){
					CRecursoEconomico recurso = new CRecursoEconomico(rs.getString("texto"), rs.getInt("posicion"), rs.getInt("nivel"),
							rs.getDouble("ejecutado_dos_antes"), rs.getDouble("aprobado"), rs.getDouble("aprobado_mas_amp"),
							rs.getDouble("recomendado"), rs.getInt("negrillas"));
					ret.add(recurso);
				}
			
			}
		}
		catch(Exception e){
			CLogger.write("1", CRecursoDAO.class, e);
		}
		finally{
			CDatabaseOracle.close(conn);
		}
		return ret;
	}
}
