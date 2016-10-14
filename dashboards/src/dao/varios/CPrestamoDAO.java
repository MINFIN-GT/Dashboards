package dao.varios;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.joda.time.DateTime;

import db.utilities.CDatabase;
import pojo.varios.CPrestamo;
import utilities.CLogger;

public class CPrestamoDAO {
	
	public static ArrayList<CPrestamo> getEntidadesEjecucion(int nivel, Integer entidad, String prestamo_sigla, Integer unidad_ejecutora, Integer programa,Integer subprograma, Integer proyecto, Integer actividad){
		final ArrayList<CPrestamo> datos=new ArrayList<CPrestamo>();
		if(CDatabase.connect()){
			Connection conn = CDatabase.getConnection();
			try{
				DateTime now = new DateTime();
				PreparedStatement  pstm1;
				pstm1 = conn.prepareStatement("select p.entidad, p.entidad_nombre "+
							( nivel > 1 ? ", p.prestamo_sigla, p.prestamo_nombre " : "" )+
							( nivel > 2 ? ", p.unidad_ejecutora, p.unidad_ejecutora_nombre " : "" )+
							( nivel > 3 ? ", p.programa, p.programa_nombre " : "" )+
							( nivel > 4 ? ", p.subprograma, p.subprograma_nombre " : "" )+
							( nivel > 5 ? ", p.proyecto, p.proyecto_nombre " : "" )+
							( nivel > 6 ? ", p.actividad + p.obra actividad, p.actividad_obra_nombre " : "" )+
							( nivel > 7 ? ", p.RENGLON, p.renglon_NOMBRE " : "" )+
							", sum(p.vigente) vigente, sum(p.ejecutado) ejecutado, sum(p.asignado) asignado "+
							"from prestamo p where p.ejercicio ="+ now.getYear() + " " +
							( nivel > 1 ? "and entidad = "+entidad+" " : "" )+
							( nivel > 2 ? "and prestamo_sigla = '"+prestamo_sigla+"' " : "" )+
							( nivel > 3 ? "and unidad_ejecutora = "+unidad_ejecutora+" " : "")+
							( nivel > 4 ? "and programa="+programa+" ":"")+
							( nivel > 5 ? "and subprograma="+subprograma+" ":"")+
							( nivel > 6 ? "and proyecto = "+proyecto+" " : "")+
							( nivel > 7 ? "and ( (actividad = "+actividad+" and obra = 0) or (actividad=0 and obra="+actividad+")) " : "")+
							"group by p.entidad, p.entidad_nombre "+
							( nivel > 1 ? ", p.prestamo_sigla, p.prestamo_nombre ": "" )+
							( nivel > 2 ? ", p.unidad_ejecutora, p.unidad_ejecutora_nombre ": "" )+
							( nivel > 3 ? ", p.programa, p.programa_nombre ": "" )+
							( nivel > 4 ? ", p.subprograma, p.subprograma_nombre ": "" )+
							( nivel > 5 ? ", p.proyecto, p.proyecto_nombre ":"")+
							( nivel > 6 ? ", p.actividad, p.obra, p.actividad_obra_nombre ":"")+
							( nivel > 7 ? ", p.RENGLON, p.renglon_NOMBRE ":""));
				ResultSet results = pstm1.executeQuery();	
				String codigo = null;
				String  nombre = "";
				while (results.next()){
				switch(nivel){
					case 1: nombre = results.getString("entidad_nombre"); codigo=results.getString("entidad"); break;
					case 2: nombre = results.getString("prestamo_nombre"); codigo=results.getString("prestamo_sigla"); break;
					case 3: nombre = results.getString("unidad_ejecutora_nombre"); codigo=results.getString("unidad_ejecutora"); break;
					case 4: nombre = results.getString("programa_nombre"); codigo=results.getString("programa"); break;
					case 5: nombre = results.getString("subprograma_nombre"); codigo=results.getString("subprograma"); break;
					case 6: nombre = results.getString("proyecto_nombre"); codigo=results.getString("proyecto"); break;
					case 7: nombre = results.getString("actividad_obra_nombre"); codigo=results.getString("actividad"); break;
					case 8: nombre = results.getString("renglon_nombre"); codigo=results.getString("renglon"); break;
					}
				CPrestamo dato = new CPrestamo(codigo,nombre
						, results.getDouble("asignado"), results.getDouble("vigente")-results.getDouble("asignado"),  results.getDouble("ejecutado"), results.getDouble("vigente")
						, results.getDouble("vigente")>0?results.getDouble("ejecutado")/results.getDouble("vigente"):0.0);
					datos.add(dato);
				}
				results.close();
				pstm1.close();
			}
			catch(Exception e){
				CLogger.write("1", CPrestamoDAO.class, e);
			}
			finally{
				CDatabase.close(conn);
			}
		}
		return datos.size()>0 ? datos : null;
	}
			
	public static ArrayList<CPrestamo> getPrestamosEjecucion(int nivel, String prestamo_sigla, Integer entidad, Integer unidad_ejecutora, Integer programa,Integer subprograma, Integer proyecto, Integer actividad){
		final ArrayList<CPrestamo> datos=new ArrayList<CPrestamo>();
		if(CDatabase.connect()){
			Connection conn = CDatabase.getConnection();
			try{
				DateTime now = new DateTime();
				PreparedStatement  pstm1;
				pstm1 = conn.prepareStatement("select p.prestamo_sigla, p.prestamo_nombre "+
							( nivel > 1 ? ", p.entidad, p.entidad_nombre " : "" )+
							( nivel > 2 ? ", p.unidad_ejecutora, p.unidad_ejecutora_nombre " : "" )+
							( nivel > 3 ? ", p.programa, p.programa_nombre " : "" )+
							( nivel > 4 ? ", p.subprograma, p.subprograma_nombre " : "" )+
							( nivel > 5 ? ", p.proyecto, p.proyecto_nombre " : "" )+
							( nivel > 6 ? ", p.actividad + p.obra actividad, p.actividad_obra_nombre " : "" )+
							( nivel > 7 ? ", p.RENGLON, p.renglon_NOMBRE " : "" )+
							", sum(p.vigente) vigente, sum(p.ejecutado) ejecutado, sum(p.asignado) asignado "+
							"from prestamo p where p.ejercicio ="+ now.getYear() + " " +
							( nivel > 1 ? "and prestamo_sigla = '"+prestamo_sigla+"' " : "" )+
							( nivel > 2 ? "and entidad = "+entidad+" " : "" )+
							( nivel > 3 ? "and unidad_ejecutora = "+unidad_ejecutora+" " : "")+
							( nivel > 4 ? "and programa="+programa+" ":"")+
							( nivel > 5 ? "and subprograma="+subprograma+" ":"")+
							( nivel > 6 ? "and proyecto = "+proyecto+" " : "")+
							( nivel > 7 ? "and ( (actividad = "+actividad+" and obra = 0) or (actividad=0 and obra="+actividad+")) " : "")+
							"group by p.prestamo_sigla, p.prestamo_nombre "+
							( nivel > 1 ? ", p.entidad, p.entidad_nombre ": "" )+
							( nivel > 2 ? ", p.unidad_ejecutora, p.unidad_ejecutora_nombre ": "" )+
							( nivel > 3 ? ", p.programa, p.programa_nombre ": "" )+
							( nivel > 4 ? ", p.subprograma, p.subprograma_nombre ": "" )+
							( nivel > 5 ? ", p.proyecto, p.proyecto_nombre ":"")+
							( nivel > 6 ? ", p.actividad, p.obra, p.actividad_obra_nombre ":"")+
							( nivel > 7 ? ", p.RENGLON, p.renglon_NOMBRE ":""));
				ResultSet results = pstm1.executeQuery();	
				String codigo = null;
				String  nombre = "";
				while (results.next()){
				switch(nivel){
					case 1: nombre = results.getString("prestamo_nombre"); codigo=results.getString("prestamo_sigla"); break;
					case 2: nombre = results.getString("entidad_nombre"); codigo=results.getString("entidad"); break;
					case 3: nombre = results.getString("unidad_ejecutora_nombre"); codigo=results.getString("unidad_ejecutora"); break;
					case 4: nombre = results.getString("programa_nombre"); codigo=results.getString("programa"); break;
					case 5: nombre = results.getString("subprograma_nombre"); codigo=results.getString("subprograma"); break;
					case 6: nombre = results.getString("proyecto_nombre"); codigo=results.getString("proyecto"); break;
					case 7: nombre = results.getString("actividad_obra_nombre"); codigo=results.getString("actividad"); break;
					case 8: nombre = results.getString("renglon_nombre"); codigo=results.getString("renglon"); break;
					}
				CPrestamo dato = new CPrestamo(codigo,nombre
						, results.getDouble("asignado"), results.getDouble("vigente")-results.getDouble("asignado"),  results.getDouble("ejecutado"), results.getDouble("vigente")
						, results.getDouble("vigente")>0?results.getDouble("ejecutado")/results.getDouble("vigente"):0.0);
					datos.add(dato);
				}
				results.close();
				pstm1.close();
			}
			catch(Exception e){
				CLogger.write("1", CPrestamoDAO.class, e);
			}
			finally{
				CDatabase.close(conn);
			}
		}
		return datos.size()>0 ? datos : null;
	}

}
