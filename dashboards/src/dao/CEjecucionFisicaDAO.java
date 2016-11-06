package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.joda.time.DateTime;

import db.utilities.CDatabase;
import pojo.CEjecucionFisica;
import utilities.CLogger;

public class CEjecucionFisicaDAO {
	
	public static ArrayList<CEjecucionFisica> getEntidadesEjecucion(){		
		final ArrayList<CEjecucionFisica> entidades=new ArrayList<CEjecucionFisica>();
		if(CDatabase.connect()){
			Connection conn = CDatabase.getConnection();
			try{
				if(!conn.isClosed()){
					DateTime now = new DateTime();    
					
					PreparedStatement pstm1 =  conn.prepareStatement("select t1.ejercicio, t1.entidad, t1.entidad_nombre, (sum(ejecucion_porcentaje*vigente)/sum(vigente))*100 ejecucion_fisica_ponderada, t2.ejecucion_financiera_porcentaje*100 ejecucion_financiera_porcentaje " + 
							"from " + 
							"( " + 
							"select ef.ejercicio, ef.entidad, entidad_nombre, " + 
							"ef.unidad_ejecutora, ef.unidad_ejecutora_nombre, " + 
							"ef.programa, ef.programa_nombre, ef.subprograma, ef.subprograma_nombre, " + 
							"ef.proyecto, ef.proyecto_nombre, ef.actividad, ef.obra, ef.actividad_obra_nombre, ep.vigente, avg(ef.ejecucion_porcentaje) ejecucion_porcentaje " + 
							"from ( " + 
							"select ejercicio, entidad, entidad_nombre, " + 
							"	unidad_ejecutora, unidad_ejecutora_nombre, " + 
							"	programa,  programa_nombre, " + 
							"	subprograma,  subprograma_nombre, " + 
							"	proyecto,  proyecto_nombre, " + 
							"	actividad, obra, actividad_obra_nombre,codigo_meta,vigente, sum(ejecucion) ejecucion, sum(ejecucion)/vigente ejecucion_porcentaje " + 
							"	from mv_ejecucion_fisica " + 
							"	group by entidad, entidad_nombre, unidad_ejecutora, unidad_ejecutora_nombre, " + 
							"	programa, programa_nombre, subprograma, subprograma_nombre, proyecto, proyecto_nombre, actividad, obra,  actividad_obra_nombre, codigo_meta " + 
							") ef, " + 
							"( " + 
							"	select ejercicio, entidad, unidad_ejecutora, programa, subprograma, proyecto, actividad, obra, sum(vigente) vigente " + 
							"	from mv_ejecucion_presupuestaria  " + 
							"	group by ejercicio, entidad, unidad_ejecutora, programa, subprograma, proyecto, actividad, obra " + 
							") ep " + 
							"where ef.entidad = ep.entidad " + 
							"and ef.unidad_ejecutora = ep.unidad_ejecutora " + 
							"and ef.programa = ep.programa " + 
							"and ef.subprograma = ep.subprograma " + 
							"and ef.proyecto = ep.proyecto " + 
							"and ef.actividad = ep.actividad " + 
							"and ef.obra = ep.obra " + 
							"group by ef.ejercicio, ef.entidad, ef.entidad_nombre, ef.unidad_ejecutora, ef.unidad_ejecutora_nombre, ef.programa, ef.programa_nombre,   " + 
							"ef.subprograma, ef.subprograma_nombre, ef.proyecto, ef.proyecto_nombre, ef.actividad, ef.obra, ef.actividad_obra_nombre, ep.vigente " + 
							") t1, " + 
							"( " + 
							" select ejercicio,entidad, sum(ano_actual) ejecucion_financiera, sum( case when mes = ? then vigente else 0 end) vigente_financiero, sum(ano_actual)/sum( case when mes = 11 then vigente else 0 end) ejecucion_financiera_porcentaje " + 
							" from mv_ejecucion_presupuestaria  " + 
							" group by ejercicio, entidad " + 
							") t2 " + 
							"where t1.entidad = t2.entidad " + 
							"and t1.ejercicio = t2.ejercicio " + 
							"group by t1.ejercicio, t1.entidad, t1.entidad_nombre");
					pstm1.setInt(1, now.getMonthOfYear());
					ResultSet results=pstm1.executeQuery();
					while (results.next()){
						CEjecucionFisica entidad = new CEjecucionFisica(null, results.getInt("entidad"), 
								results.getString("entidad_nombre"), results.getDouble("ejecucion_financiera_porcentaje"), 
								results.getDouble("ejecucion_fisica_ponderada"));
						entidades.add(entidad);
					}
					results.close();
					pstm1.close();
				}
			}
			catch(Exception e){
				CLogger.write("1", CEjecucionFisicaDAO.class, e);
			}
			finally{
				CDatabase.close(conn);
			}
		}
		return entidades;
	}
}
