package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import db.utilities.CDatabase;
import pojo.CDeudaMensualizada;
import utilities.CLogger;

public class CDeudaDAO {
	public static ArrayList<CDeudaMensualizada> getEjecucionTotalPorMes(int ejercicio){		
		final ArrayList<CDeudaMensualizada> ret=new ArrayList<CDeudaMensualizada>();
		Connection conn = CDatabase.connect();
		try{
			if(conn!=null && !conn.isClosed()){
				PreparedStatement pstm1 =  conn.prepareStatement("select ep.entidad, e.entidad_nombre,  " + 
						"ep.actividad, e.actividad_obra_nombre actividad_nombre,  " + 
						"ep.fuente, f.nombre fuente_nombre, ep.renglon, ep.renglon_nombre, " + 
						"sum(case when mes=1 then ep.asignado else 0 end) asignado, " + 
						"sum(case when mes=1 then ep.vigente else 0 end) vm1, " + 
						"sum(case when mes=2 then ep.vigente else 0 end) vm2, " + 
						"sum(case when mes=3 then ep.vigente else 0 end) vm3, " + 
						"sum(case when mes=4 then ep.vigente else 0 end) vm4, " + 
						"sum(case when mes=5 then ep.vigente else 0 end) vm5, " + 
						"sum(case when mes=6 then ep.vigente else 0 end) vm6, " + 
						"sum(case when mes=7 then ep.vigente else 0 end) vm7, " + 
						"sum(case when mes=8 then ep.vigente else 0 end) vm8, " + 
						"sum(case when mes=9 then ep.vigente else 0 end) vm9, " + 
						"sum(case when mes=10 then ep.vigente else 0 end) vm10, " + 
						"sum(case when mes=11 then ep.vigente else 0 end) vm11, " + 
						"sum(case when mes=12 then ep.vigente else 0 end) vm12, " + 
						"sum(case when mes=1 then ep.ano_actual else 0 end) m1, " + 
						"sum(case when mes=2 then ep.ano_actual else 0 end) m2, " + 
						"sum(case when mes=3 then ep.ano_actual else 0 end) m3, " + 
						"sum(case when mes=4 then ep.ano_actual else 0 end) m4, " + 
						"sum(case when mes=5 then ep.ano_actual else 0 end) m5, " + 
						"sum(case when mes=6 then ep.ano_actual else 0 end) m6, " + 
						"sum(case when mes=7 then ep.ano_actual else 0 end) m7, " + 
						"sum(case when mes=8 then ep.ano_actual else 0 end) m8, " + 
						"sum(case when mes=9 then ep.ano_actual else 0 end) m9, " + 
						"sum(case when mes=10 then ep.ano_actual else 0 end) m10, " + 
						"sum(case when mes=11 then ep.ano_actual else 0 end) m11, " + 
						"sum(case when mes=12 then ep.ano_actual else 0 end) m12 " + 
						"from mv_ejecucion_presupuestaria ep, cg_fuentes f, mv_estructura e " + 
						"where ep.ejercicio = ? " + 
						"and ep.entidad = 11130019 " + 
						"and f.ejercicio = ep.ejercicio " + 
						"and f.fuente = ep.fuente " + 
						"and e.ejercicio = ep.ejercicio " + 
						"and e.entidad = ep.entidad " + 
						"and e.unidad_ejecutora = ep.unidad_ejecutora " + 
						"and e.programa = ep.programa " + 
						"and e.subprograma = ep.subprograma " + 
						"and e.proyecto = ep.proyecto " + 
						"and e.actividad = ep.actividad " + 
						"and e.obra = ep.obra " + 
						"group by ep.entidad,e.entidad_nombre, ep.actividad, e.actividad_obra_nombre,  " + 
						"ep.fuente, f.nombre, ep.renglon, ep.renglon_nombre " + 
						"order by ep.actividad, ep.fuente, ep.renglon");		
				pstm1.setInt(1, ejercicio);
				ResultSet results = pstm1.executeQuery();	
				CDeudaMensualizada actividad = null;
				CDeudaMensualizada fuente = null;
				int actividad_actual = -1;
				int fuente_actual = -1;
				while (results.next()){
					Double[] vigentes = new Double[12];
					Double[] ejecuciones = new Double[12];
					for(int i=1; i<13; i++) {
						vigentes[i-1] = results.getDouble("vm"+i);
						ejecuciones[i-1] = results.getDouble("m"+i);
					}
					CDeudaMensualizada deuda=new CDeudaMensualizada(11130009, results.getString("entidad_nombre"), results.getInt("actividad"), results.getString("actividad_nombre"),
							results.getInt("fuente"), results.getString("fuente_nombre"), results.getInt("renglon"), results.getString("renglon_nombre"), results.getDouble("asignado"),
							vigentes, ejecuciones, null, 3);
					if(actividad_actual!=deuda.getActividad()) {
						if(actividad!=null) {
							for(int i=0; i<actividad.getChildren().size(); i++) {
								actividad.setAsignado(actividad.getAsignado()+actividad.getChildren().get(i).getAsignado());
								for(int k=0; k<12; k++) {
									actividad.getVigente_meses()[k]+=actividad.getChildren().get(i).getVigente_meses()[k];
									actividad.getEjecucion_meses()[k]+=actividad.getChildren().get(i).getEjecucion_meses()[k];
								}
							}
							ret.add(actividad);
						}
						actividad = new CDeudaMensualizada(11130009, deuda.getEntidad_nombre(), deuda.getActividad(), deuda.getActividad_nombre(), 0, null, 0, null, 0.0, 
								new Double[]{0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0}, new Double[]{0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0}, new ArrayList<CDeudaMensualizada>(),1);
						actividad_actual = actividad.getActividad();
					}
					if(fuente_actual!=deuda.getFuente()) {
						if(fuente!=null)
							actividad.getChildren().add(fuente);
						fuente = new CDeudaMensualizada(11130009, deuda.getEntidad_nombre(), deuda.getActividad(), deuda.getActividad_nombre(), deuda.getFuente(), deuda.getFuente_nombre(), 
								0, null, 0.0, new Double[] {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0}, new Double[] {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0}, new ArrayList<CDeudaMensualizada>(),2);
						fuente_actual=fuente.getFuente();
					}
					fuente.setAsignado(fuente.getAsignado()+deuda.getAsignado());
					for(int i=0; i<12; i++) {
						fuente.getVigente_meses()[i]+=deuda.getVigente_meses()[i];
						fuente.getEjecucion_meses()[i]+=deuda.getEjecucion_meses()[i];
					}
					fuente.getChildren().add(deuda);
				}
				if(fuente!=null)
					actividad.getChildren().add(fuente);
				if(actividad!=null) {
					for(int i=0; i<actividad.getChildren().size(); i++) {
						actividad.setAsignado(actividad.getAsignado()+actividad.getChildren().get(i).getAsignado());
						for(int k=0; k<12; k++) {
							actividad.getVigente_meses()[k]+=actividad.getChildren().get(i).getVigente_meses()[k];
							actividad.getEjecucion_meses()[k]+=actividad.getChildren().get(i).getEjecucion_meses()[k];
						}
					}
					ret.add(actividad);
				}
				results.close();
				pstm1.close();
			}
		}
		catch(Exception e){
			CLogger.write("1", CDeudaDAO.class, e);
		}
		finally{
			CDatabase.close(conn);
		}
		return ret.size()>0 ? ret : null;
	}
}
