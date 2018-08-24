package dao.formulacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import db.utilities.CDatabaseOracle;
import pojo.formulacion.CGastoEconomico;
import utilities.CLogger;

public class CGastoDAO {
	public static ArrayList<CGastoEconomico> getGastosTotal(int ejercicio){
		ArrayList<CGastoEconomico> ret = new ArrayList<CGastoEconomico>();
		Connection conn = null;
		try{
			conn = CDatabaseOracle.connect();
			if(!conn.isClosed()){
				PreparedStatement pstm =  conn.prepareStatement("select posicion, texto, nivel, negrillas, ( " + 
						"    select sum(gd.monto_renglon) total " + 
						"    from eg_gastos_hoja gh, eg_gastos_detalle gd, (select ue.ejercicio, ue.entidad, count(*) total from cg_entidades ue  " + 
						"            group by ue.ejercicio, ue.entidad " + 
						"        ) num_ues " + 
						"    where gh.ejercicio = ? " + 
						"    and gh.clase_registro IN ('DEV','CYD','RDP','REG') "+
						"    and gh.estado = 'APROBADO' " + 
						"    and gd.ejercicio = gh.ejercicio " + 
						"    and gd.entidad = gh.entidad " + 
						"    and gd.unidad_ejecutora = gh.unidad_ejecutora " + 
						"    and gd.no_cur = gh.no_cur " + 
						"    and num_ues.ejercicio = gh.ejercicio " + 
						"    and num_ues.entidad = gh.entidad " + 
						"    and ((num_ues.total>1 and gh.unidad_ejecutora>0) or (num_ues.total=1 and gh.unidad_ejecutora=0)) " + 
						"    and gd.renglon in (select rfd.dato_numero  " + 
						"        from reporte_formulacion_detalle rfd " + 
						"        where rfd.reporte = rf.reporte " + 
						"        and rfd.posicion = rf.posicion " + 
						"    ) " + 
						") ejecutado_dos_antes,( " + 
						"    select sum(g.aprobado) " + 
						"    from fp_p6_partidas g " + 
						"    where g.renglon in (select rfd.dato_numero  " + 
						"        from reporte_formulacion_detalle rfd " + 
						"        where rfd.reporte = rf.reporte " + 
						"        and rfd.posicion = rf.posicion " + 
						"    ) " + 
						"    and g.ejercicio = ? " + 
						") aprobado, ( " + 
						"    select sum(g.aprobado) " + 
						"    from fp_p6_partidas g " + 
						"    where g.renglon in (select rfd.dato_numero  " + 
						"        from reporte_formulacion_detalle rfd " + 
						"        where rfd.reporte = rf.reporte " + 
						"        and rfd.posicion = rf.posicion " + 
						"    ) " + 
						"    and g.ejercicio = ? " + 
						") + nvl(( " + 
						"    select sum(d.monto_aprobado) " + 
						"    from eg_modificaciones_hoja g, eg_modificaciones_detalle d " + 
						"    where g.ejercicio = ? " + 
						"    and g.unidad_ejecutora=0 " + 
						"    and g.clase_registro = 'AMP' " + 
						"    and g.fec_disposicion <= TO_DATE ( (g.ejercicio) || '/07/15','YYYY/MM/DD') " + 
						"    and d.ejercicio = g.ejercicio " + 
						"    and d.entidad = g.entidad " + 
						"    and d.unidad_ejecutora = g.unidad_ejecutora " + 
						"    and d.no_cur = g.no_cur " + 
						"    and d.clase_registro = g.clase_registro " + 
						"    and d.renglon in (select rfd.dato_numero  " + 
						"        from reporte_formulacion_detalle rfd " + 
						"        where rfd.reporte = rf.reporte " + 
						"        and rfd.posicion = rf.posicion " + 
						"    ) " + 
						"),0) aprobado_mas_amp,( " + 
						"    select sum(g.recomendado) " + 
						"    from fp_p6_partidas g " + 
						"    where g.renglon in (select rfd.dato_numero  " + 
						"        from reporte_formulacion_detalle rfd " + 
						"        where rfd.reporte = rf.reporte " + 
						"        and rfd.posicion = rf.posicion " + 
						"    ) " + 
						"    and g.ejercicio = ? " + 
						") recomendado " + 
						"from sicoinp_hreyes.reporte_formulacion rf " + 
						"where rf.reporte = 'CUADRO_4' " + 
						"order by posicion");
				pstm.setInt(1, ejercicio-2);
				pstm.setInt(2, ejercicio-1);
				pstm.setInt(3, ejercicio-1);
				pstm.setInt(4, ejercicio-1);
				pstm.setInt(5, ejercicio);
				ResultSet rs = pstm.executeQuery();
				while(rs.next()){
					CGastoEconomico gasto = new CGastoEconomico(rs.getString("texto"), rs.getInt("posicion"), rs.getInt("nivel"),
							rs.getDouble("ejecutado_dos_antes"), rs.getDouble("aprobado"), rs.getDouble("aprobado_mas_amp"),
							rs.getDouble("recomendado"), rs.getInt("negrillas"));
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
