package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.joda.time.DateTime;

import db.utilities.CDatabase;
import pojo.CEjecucion;
import utilities.CLogger;

public class CEjecucionDAO { 
	
	public static ArrayList<CEjecucion> getEntidadesEjecucion(int mes, String fuentes, String gruposGasto, boolean todosgrupos){		
		final ArrayList<CEjecucion> entidades=new ArrayList<CEjecucion>();
		if(CDatabase.connect()){
			Connection conn = CDatabase.getConnection();
			try{
				PreparedStatement pstm1 =  conn.prepareStatement("select e.entidad_nombre, a.*, c.anticipo_cuota, c.aprobado_cuota, c.anticipo_cuota_acumulado, c.aprobado_cuota_acumulado FROM " + 
								"( " + 
								"select  " + 
								"  ep.entidad " + 
								", sum(case when (ep.mes = ?) then ep.ano_1 else 0 end) ano_1 " + 
								", sum(case when (ep.mes = ?) then ep.ano_2 else 0 end) ano_2 " + 
								", sum(case when (ep.mes = ?) then ep.ano_3 else 0 end) ano_3 " + 
								", sum(case when (ep.mes = ?) then ep.ano_4 else 0 end) ano_4 " + 
								", sum(case when (ep.mes = ?) then ep.ano_5 else 0 end) ano_5 " + 
								", sum(case when (ep.mes = ?) then ep.ano_actual else 0 end) ejecutado " + 
								", sum(case when (ep.mes <= ?) then ep.ano_actual else 0 end) ejecutado_acumulado " + 
								", sum(case when (ep.mes = ?) then ep.vigente else 0 end) vigente " + 
								"from mv_ejecucion_presupuestaria ep " + 
								"where ep.mes <= ? " + 
								"and ep.fuente IN ("+fuentes+") "+
								"and ep.grupo IN ("+gruposGasto+") "+
								"group by ep.entidad " + 
								") a left join ( " + 
								"	select ep.entidad " + 
								"	, sum(case when (ep.mes = ?) then ifnull(ep.anticipo_cuota,0) else 0 end) anticipo_cuota " + 
								"	, sum(case when (ep.mes <= ?) then ifnull(ep.anticipo_cuota,0) else 0 end) anticipo_cuota_acumulado " + 
								"	, sum(case when (ep.mes = ?) then ifnull(ep.aprobado_cuota,0) else 0 end) aprobado_cuota " + 
								"	, sum(case when (ep.mes <= ?) then ifnull(ep.aprobado_cuota,0) else 0 end) aprobado_cuota_acumulado " + 
								"	from ( " + 
								"	select ep.mes, ep.entidad, ep.unidad_ejecutora  " + 
								"	, avg(ep.anticipo) anticipo_cuota " + 
								"	, avg(ep.aprobado) aprobado_cuota " + 
								"	from mv_ejecucion_presupuestaria ep  " + 
								"	where ep.mes <= ? " + 
								"   and ep.fuente IN ("+fuentes+") "+
								"   and ep.grupo IN ("+gruposGasto+") "+
								"	group by ep.ejercicio, ep.mes, ep.entidad, ep.unidad_ejecutora " + 
								"	) ep " + 
								"	group by entidad " + 
								") c  " + 
								"on ( " + 
								"	c.entidad = a.entidad " + 
								") " + 
								", " + 
								"( " + 
								"	select e.entidad, e.entidad_nombre " + 
								"	from mv_estructura e " + 
								"	group by e.entidad, e.entidad_nombre " + 
								") e " + 
								"where a.entidad = e.entidad");		
				pstm1.setInt(1, mes);
				pstm1.setInt(2, mes);
				pstm1.setInt(3, mes);
				pstm1.setInt(4, mes);
				pstm1.setInt(5, mes);
				pstm1.setInt(6, mes);
				pstm1.setInt(7, mes);
				pstm1.setInt(8, mes);
				pstm1.setInt(9, mes);
				pstm1.setInt(10, mes);
				pstm1.setInt(11, mes);
				pstm1.setInt(12, mes);
				pstm1.setInt(13, mes);
				pstm1.setInt(14, mes);
				ResultSet results = pstm1.executeQuery();	
				while (results.next()){
					CEjecucion entidad = new CEjecucion((Integer)null, results.getInt("entidad"), results.getString("entidad_nombre"), results.getDouble("ano_1"), 
							results.getDouble("ano_2"), results.getDouble("ano_3"), results.getDouble("ano_4"), results.getDouble("ano_5"), 
							null, results.getDouble("aprobado_cuota") + (todosgrupos ? results.getDouble("anticipo_cuota") : 0), 
							results.getDouble("aprobado_cuota_acumulado") + (todosgrupos ? results.getDouble("anticipo_cuota_acumulado") : 0), 
							results.getDouble("ejecutado"), results.getDouble("ejecutado_acumulado"), 
							results.getDouble("vigente"));
					entidades.add(entidad);
				}
				results.close();
				pstm1.close();
			}
			catch(Exception e){
				CLogger.write("1", CEjecucionDAO.class, e);
			}
			finally{
				CDatabase.close(conn);
			}
		}
		
		return entidades.size()>0 ? entidades : null;
	}
	
	public static ArrayList<CEjecucion> getUnidadesEjecutorasEjecucion(int entidad, int mes, String fuentes, String gruposGasto, boolean todosgrupos){
		final ArrayList<CEjecucion> entidades=new ArrayList<CEjecucion>();
		if(CDatabase.connect()){
			Connection conn = CDatabase.getConnection();
			try{
				PreparedStatement pstm1 =  conn.prepareStatement("select e.unidad_ejecutora_nombre, a.*, c.anticipo_cuota, c.aprobado_cuota, c.anticipo_cuota_acumulado, c.aprobado_cuota_acumulado FROM " + 
						"( " + 
						"select  " + 
						"  ep.unidad_ejecutora " + 
						", sum(case when (ep.mes = ?) then ep.ano_1 else 0 end) ano_1 " + 
						", sum(case when (ep.mes = ?) then ep.ano_2 else 0 end) ano_2 " + 
						", sum(case when (ep.mes = ?) then ep.ano_3 else 0 end) ano_3 " + 
						", sum(case when (ep.mes = ?) then ep.ano_4 else 0 end) ano_4 " + 
						", sum(case when (ep.mes = ?) then ep.ano_5 else 0 end) ano_5 " + 
						", sum(case when (ep.mes = ?) then ep.ano_actual else 0 end) ejecutado " + 
						", sum(case when (ep.mes <= ?) then ep.ano_actual else 0 end) ejecutado_acumulado " + 
						", sum(case when (ep.mes = ?) then ep.vigente else 0 end) vigente " + 
						"from mv_ejecucion_presupuestaria ep " + 
						"where ep.mes <= ? " + 
						"and ep.fuente IN ("+fuentes+") "+
						"and ep.grupo IN ("+gruposGasto+") "+
						"and ep.entidad = ? "+
						"group by ep.unidad_ejecutora " + 
						") a left join ( " + 
						"	select ep.unidad_ejecutora " + 
						"	, sum(case when (ep.mes = ?) then ifnull(ep.anticipo_cuota,0) else 0 end) anticipo_cuota " + 
						"	, sum(case when (ep.mes <= ?) then ifnull(ep.anticipo_cuota,0) else 0 end) anticipo_cuota_acumulado " + 
						"	, sum(case when (ep.mes = ?) then ifnull(ep.aprobado_cuota,0) else 0 end) aprobado_cuota " + 
						"	, sum(case when (ep.mes <= ?) then ifnull(ep.aprobado_cuota,0) else 0 end) aprobado_cuota_acumulado " + 
						"	from ( " + 
						"	select ep.mes, ep.unidad_ejecutora  " + 
						"	, avg(ep.anticipo) anticipo_cuota " + 
						"	, avg(ep.aprobado) aprobado_cuota " + 
						"	from mv_ejecucion_presupuestaria ep  " + 
						"	where ep.mes <= ? " +
						"	and ep.entidad  = ? "+
						"   and ep.fuente IN ("+fuentes+") "+
						"   and ep.grupo IN ("+gruposGasto+") "+
						"	group by ep.ejercicio, ep.mes, ep.entidad, ep.unidad_ejecutora " + 
						"	) ep " + 
						"	group by unidad_ejecutora " + 
						") c  " + 
						"on ( " + 
						"	c.unidad_ejecutora = a.unidad_ejecutora " + 
						") " + 
						", " + 
						"( " + 
						"	select e.unidad_ejecutora, e.unidad_ejecutora_nombre " + 
						"	from mv_estructura e " +
						"	where e.entidad = ? "+
						"	group by e.unidad_ejecutora, e.unidad_ejecutora_nombre " + 
						") e " + 
						"where a.unidad_ejecutora = e.unidad_ejecutora");				
				pstm1.setInt(1, mes);
				pstm1.setInt(2, mes);
				pstm1.setInt(3, mes);
				pstm1.setInt(4, mes);
				pstm1.setInt(5, mes);
				pstm1.setInt(6, mes);
				pstm1.setInt(7, mes);
				pstm1.setInt(8, mes);
				pstm1.setInt(9, mes);
				pstm1.setInt(10, entidad);
				pstm1.setInt(11, mes);
				pstm1.setInt(12, mes);
				pstm1.setInt(13, mes);
				pstm1.setInt(14, mes);
				pstm1.setInt(15, mes);
				pstm1.setInt(16, entidad);
				pstm1.setInt(17, entidad);
				ResultSet results = pstm1.executeQuery();	
				while (results.next()){
					CEjecucion ue = new CEjecucion(entidad, results.getInt("unidad_ejecutora"), results.getString("unidad_ejecutora_nombre"), results.getDouble("ano_1"), 
							results.getDouble("ano_2"), results.getDouble("ano_3"), results.getDouble("ano_4"), results.getDouble("ano_5"), 
							null, results.getDouble("aprobado_cuota") + (todosgrupos ? results.getDouble("anticipo_cuota") : 0) , 
							results.getDouble("aprobado_cuota_acumulado") + (todosgrupos ? results.getDouble("anticipo_cuota_acumulado") : 0), 
							results.getDouble("ejecutado"), results.getDouble("ejecutado_acumulado"), 
							results.getDouble("vigente"));
					entidades.add(ue);
				}
				results.close();
				pstm1.close();
				
			}
			catch(Exception e){
				CLogger.write("2", CEjecucionDAO.class, e);
			}
			finally{
				CDatabase.close(conn);
			}
		}
		return entidades.size()>0 ? entidades : null;
	}
	
	public static ArrayList<CEjecucion> getProgramasEjecucion(int entidad, Integer unidad_ejecutora, int mes, String fuentes, String gruposGasto){
		final ArrayList<CEjecucion> entidades=new ArrayList<CEjecucion>();
		if(CDatabase.connect()){
			Connection conn = CDatabase.getConnection();
			try{
				PreparedStatement pstm1 =  conn.prepareStatement("select e.programa, e.programa_nombre, a.* FROM " + 
						"( " + 
						"select  " + 
						"  ep.programa " + 
						", sum(case when (ep.mes = ?) then ep.ano_1 else 0 end) ano_1 " + 
						", sum(case when (ep.mes = ?) then ep.ano_2 else 0 end) ano_2 " + 
						", sum(case when (ep.mes = ?) then ep.ano_3 else 0 end) ano_3 " + 
						", sum(case when (ep.mes = ?) then ep.ano_4 else 0 end) ano_4 " + 
						", sum(case when (ep.mes = ?) then ep.ano_5 else 0 end) ano_5 " + 
						", sum(case when (ep.mes = ?) then ep.ano_actual else 0 end) ejecutado " + 
						", sum(case when (ep.mes <= ?) then ep.ano_actual else 0 end) ejecutado_acumulado " + 
						", sum(case when (ep.mes = ?) then ep.vigente else 0 end) vigente " + 
						"from mv_ejecucion_presupuestaria ep " + 
						"where ep.mes <= ? " + 
						"and ep.fuente IN ("+fuentes+") "+
						"and ep.grupo IN ("+gruposGasto+") "+
						"and ep.entidad = ? "+
						(unidad_ejecutora!=null ? "and ep.unidad_ejecutora = "+unidad_ejecutora : "") +
						"  group by ep.programa " + 
						") a, " + 
						"( " + 
						"	select e.programa, e.programa_nombre " + 
						"	from mv_estructura e " +
						"	where e.entidad = ? "+
						(unidad_ejecutora!=null ? "and e.unidad_ejecutora = "+unidad_ejecutora : "") +
						"	group by e.programa, e.programa_nombre " + 
						") e " + 
						"where a.programa = e.programa");				
				pstm1.setInt(1, mes);
				pstm1.setInt(2, mes);
				pstm1.setInt(3, mes);
				pstm1.setInt(4, mes);
				pstm1.setInt(5, mes);
				pstm1.setInt(6, mes);
				pstm1.setInt(7, mes);
				pstm1.setInt(8, mes);
				pstm1.setInt(9, mes);
				pstm1.setInt(10, entidad);
				pstm1.setInt(11, entidad);
				ResultSet results = pstm1.executeQuery();	
				while (results.next()){
					CEjecucion ue = new CEjecucion(new Integer[]{ entidad, unidad_ejecutora}, results.getInt("programa"), results.getString("programa_nombre"), results.getDouble("ano_1"), 
							results.getDouble("ano_2"), results.getDouble("ano_3"), results.getDouble("ano_4"), results.getDouble("ano_5"), 
							null, null, null, results.getDouble("ejecutado"), results.getDouble("ejecutado_acumulado"), 
							results.getDouble("vigente"));
					entidades.add(ue);
				}
				results.close();
				pstm1.close();
				
			}
			catch(Exception e){
				CLogger.write("3", CEjecucionDAO.class, e);
			}
			finally{
				CDatabase.close(conn);
			}
		}
		return entidades.size()>0 ? entidades : null;
	}
	
	public static ArrayList<CEjecucion> getSubProgramasEjecucion(int entidad, Integer unidad_ejecutora, Integer programa, 
			int mes, String fuentes, String gruposGasto){
		final ArrayList<CEjecucion> entidades=new ArrayList<CEjecucion>();
		if(CDatabase.connect()){
			Connection conn = CDatabase.getConnection();
			try{
				PreparedStatement pstm1 =  conn.prepareStatement("select e.subprograma, e.subprograma_nombre, a.* FROM " + 
						"( " + 
						"select  " + 
						"  ep.subprograma " + 
						", sum(case when (ep.mes = ?) then ep.ano_1 else 0 end) ano_1 " + 
						", sum(case when (ep.mes = ?) then ep.ano_2 else 0 end) ano_2 " + 
						", sum(case when (ep.mes = ?) then ep.ano_3 else 0 end) ano_3 " + 
						", sum(case when (ep.mes = ?) then ep.ano_4 else 0 end) ano_4 " + 
						", sum(case when (ep.mes = ?) then ep.ano_5 else 0 end) ano_5 " + 
						", sum(case when (ep.mes = ?) then ep.ano_actual else 0 end) ejecutado " + 
						", sum(case when (ep.mes <= ?) then ep.ano_actual else 0 end) ejecutado_acumulado " + 
						", sum(case when (ep.mes = ?) then ep.vigente else 0 end) vigente " + 
						"from mv_ejecucion_presupuestaria ep " + 
						"where ep.mes <= ? " + 
						"and ep.fuente IN ("+fuentes+") "+
						"and ep.grupo IN ("+gruposGasto+") "+
						"and ep.entidad = ? "+
						(unidad_ejecutora!=null ? " and ep.unidad_ejecutora = "+unidad_ejecutora : "") +
						(programa!=null ? " and ep.programa = "+programa : "") +
						"  group by ep.subprograma " + 
						") a, " + 
						"( " + 
						"	select e.subprograma, e.subprograma_nombre " + 
						"	from mv_estructura e " +
						"	where e.entidad = ? "+
						(unidad_ejecutora!=null ? " and e.unidad_ejecutora = "+unidad_ejecutora : "") +
						(programa!=null ? " and e.programa = "+programa : "") +
						"	group by e.subprograma, e.subprograma_nombre " + 
						") e " + 
						"where a.subprograma = e.subprograma");				
				pstm1.setInt(1, mes);
				pstm1.setInt(2, mes);
				pstm1.setInt(3, mes);
				pstm1.setInt(4, mes);
				pstm1.setInt(5, mes);
				pstm1.setInt(6, mes);
				pstm1.setInt(7, mes);
				pstm1.setInt(8, mes);
				pstm1.setInt(9, mes);
				pstm1.setInt(10, entidad);
				pstm1.setInt(11, entidad);
				ResultSet results = pstm1.executeQuery();	
				while (results.next()){
					CEjecucion ue = new CEjecucion(new Integer[]{ entidad, unidad_ejecutora, programa}, results.getInt("subprograma"), results.getString("subprograma_nombre"), results.getDouble("ano_1"), 
							results.getDouble("ano_2"), results.getDouble("ano_3"), results.getDouble("ano_4"), results.getDouble("ano_5"), 
							null, null, null, results.getDouble("ejecutado"), results.getDouble("ejecutado_acumulado"), 
							results.getDouble("vigente"));
					entidades.add(ue);
				}
				results.close();
				pstm1.close();
				
			}
			catch(Exception e){
				CLogger.write("4", CEjecucionDAO.class, e);
			}
			finally{
				CDatabase.close(conn);
			}
		}
		return entidades.size()>0 ? entidades : null;
	}
	
	public static ArrayList<CEjecucion> getProyectosEjecucion(int entidad, Integer unidad_ejecutora, Integer programa, 
			Integer subprograma, int mes, String fuentes, String gruposGasto){
		final ArrayList<CEjecucion> entidades=new ArrayList<CEjecucion>();
		if(CDatabase.connect()){
			Connection conn = CDatabase.getConnection();
			try{
				PreparedStatement pstm1 =  conn.prepareStatement("select e.proyecto, e.proyecto_nombre, a.* FROM " + 
						"( " + 
						"select  " + 
						"  ep.proyecto " + 
						", sum(case when (ep.mes = ?) then ep.ano_1 else 0 end) ano_1 " + 
						", sum(case when (ep.mes = ?) then ep.ano_2 else 0 end) ano_2 " + 
						", sum(case when (ep.mes = ?) then ep.ano_3 else 0 end) ano_3 " + 
						", sum(case when (ep.mes = ?) then ep.ano_4 else 0 end) ano_4 " + 
						", sum(case when (ep.mes = ?) then ep.ano_5 else 0 end) ano_5 " + 
						", sum(case when (ep.mes = ?) then ep.ano_actual else 0 end) ejecutado " + 
						", sum(case when (ep.mes <= ?) then ep.ano_actual else 0 end) ejecutado_acumulado " + 
						", sum(case when (ep.mes = ?) then ep.vigente else 0 end) vigente " + 
						"from mv_ejecucion_presupuestaria ep " + 
						"where ep.mes <= ? " + 
						"and ep.fuente IN ("+fuentes+") "+
						"and ep.grupo IN ("+gruposGasto+") "+
						"and ep.entidad = ? "+
						(unidad_ejecutora!=null ? " and ep.unidad_ejecutora = "+unidad_ejecutora : "") +
						(programa!=null ? " and ep.programa = "+programa : "") +
						(subprograma!=null ? " and ep.subprograma = "+subprograma : "") +
						"  group by ep.proyecto " + 
						") a, " + 
						"( " + 
						"	select e.proyecto, e.proyecto_nombre " + 
						"	from mv_estructura e " +
						"	where e.entidad = ? "+
						(unidad_ejecutora!=null ? " and e.unidad_ejecutora = "+unidad_ejecutora : "") +
						(programa!=null ? " and e.programa = "+programa : "") +
						(subprograma!=null ? " and e.subprograma = "+subprograma : "") +
						"	group by e.proyecto, e.proyecto_nombre " + 
						") e " + 
						"where a.proyecto = e.proyecto");				
				pstm1.setInt(1, mes);
				pstm1.setInt(2, mes);
				pstm1.setInt(3, mes);
				pstm1.setInt(4, mes);
				pstm1.setInt(5, mes);
				pstm1.setInt(6, mes);
				pstm1.setInt(7, mes);
				pstm1.setInt(8, mes);
				pstm1.setInt(9, mes);
				pstm1.setInt(10, entidad);
				pstm1.setInt(11, entidad);
				ResultSet results = pstm1.executeQuery();	
				while (results.next()){
					CEjecucion ue = new CEjecucion(new Integer[]{ entidad, unidad_ejecutora, programa, subprograma}, results.getInt("proyecto"), results.getString("proyecto_nombre"), results.getDouble("ano_1"), 
							results.getDouble("ano_2"), results.getDouble("ano_3"), results.getDouble("ano_4"), results.getDouble("ano_5"), 
							null, null, null, results.getDouble("ejecutado"), results.getDouble("ejecutado_acumulado"), 
							results.getDouble("vigente"));
					entidades.add(ue);
				}
				results.close();
				pstm1.close();
				
			}
			catch(Exception e){
				CLogger.write("5", CEjecucionDAO.class, e);
			}
			finally{
				CDatabase.close(conn);
			}
		}
		return entidades.size()>0 ? entidades : null;
	}	
	
	public static ArrayList<CEjecucion> getActividadesObrasEjecucion(int entidad, Integer unidad_ejecutora, Integer programa, 
			Integer subprograma, Integer proyecto, int mes, String fuentes, String gruposGasto){
		final ArrayList<CEjecucion> entidades=new ArrayList<CEjecucion>();
		if(CDatabase.connect()){
			Connection conn = CDatabase.getConnection();
			try{
				PreparedStatement pstm1 =  conn.prepareStatement("select e.actividad, e.obra, e.actividad_obra_nombre, a.* FROM " + 
						"( " + 
						"select  " + 
						"  ep.actividad, ep.obra " + 
						", sum(case when (ep.mes = ?) then ep.ano_1 else 0 end) ano_1 " + 
						", sum(case when (ep.mes = ?) then ep.ano_2 else 0 end) ano_2 " + 
						", sum(case when (ep.mes = ?) then ep.ano_3 else 0 end) ano_3 " + 
						", sum(case when (ep.mes = ?) then ep.ano_4 else 0 end) ano_4 " + 
						", sum(case when (ep.mes = ?) then ep.ano_5 else 0 end) ano_5 " + 
						", sum(case when (ep.mes = ?) then ep.ano_actual else 0 end) ejecutado " + 
						", sum(case when (ep.mes <= ?) then ep.ano_actual else 0 end) ejecutado_acumulado " + 
						", sum(case when (ep.mes = ?) then ep.vigente else 0 end) vigente " + 
						"from mv_ejecucion_presupuestaria ep " + 
						"where ep.mes <= ? " + 
						"and ep.fuente IN ("+fuentes+") "+
						"and ep.grupo IN ("+gruposGasto+") "+
						"and ep.entidad = ? "+
						(unidad_ejecutora!=null ? " and ep.unidad_ejecutora = "+unidad_ejecutora : "") +
						(programa!=null ? " and ep.programa = "+programa : "") +
						(subprograma!=null ? " and ep.subprograma = "+subprograma : "") +
						(proyecto!=null ? " and ep.proyecto = "+proyecto : "") +
						"  group by ep.actividad, ep.obra " + 
						") a, " + 
						"( " + 
						"	select e.actividad, e.obra, e.actividad_obra_nombre " + 
						"	from mv_estructura e " +
						"	where e.entidad = ? "+
						(unidad_ejecutora!=null ? " and e.unidad_ejecutora = "+unidad_ejecutora : "") +
						(programa!=null ? " and e.programa = "+programa : "") +
						(subprograma!=null ? " and e.subprograma = "+subprograma : "") +
						(proyecto!=null ? " and e.proyecto = "+proyecto : "") +
						"	group by e.actividad, e.obra, e.actividad_obra_nombre " + 
						") e " + 
						"where a.actividad = e.actividad and a.obra = e.obra");				
				pstm1.setInt(1, mes);
				pstm1.setInt(2, mes);
				pstm1.setInt(3, mes);
				pstm1.setInt(4, mes);
				pstm1.setInt(5, mes);
				pstm1.setInt(6, mes);
				pstm1.setInt(7, mes);
				pstm1.setInt(8, mes);
				pstm1.setInt(9, mes);
				pstm1.setInt(10, entidad);
				pstm1.setInt(11, entidad);
				ResultSet results = pstm1.executeQuery();	
				while (results.next()){
					Integer actividad = results.getInt("actividad");
					Integer obra = results.getInt("obra");
					CEjecucion ue = new CEjecucion(new Integer[]{ entidad, unidad_ejecutora, programa, subprograma, proyecto}, actividad!=null ? actividad : obra, results.getString("actividad_obra_nombre"), results.getDouble("ano_1"), 
							results.getDouble("ano_2"), results.getDouble("ano_3"), results.getDouble("ano_4"), results.getDouble("ano_5"), 
							null, null, null, results.getDouble("ejecutado"), results.getDouble("ejecutado_acumulado"), 
							results.getDouble("vigente"));
					entidades.add(ue);
				}
				results.close();
				pstm1.close();
				
			}
			catch(Exception e){
				CLogger.write("6", CEjecucionDAO.class, e);
			}
			finally{
				CDatabase.close(conn);
			}
		}
		return entidades.size()>0 ? entidades : null;
	}

	public static ArrayList<CEjecucion> getRenglonesEjecucion(int entidad, Integer unidad_ejecutora, Integer programa, 
			Integer subprograma, Integer proyecto, Integer actividad, Integer obra, int mes, String fuentes, String gruposGasto){
		final ArrayList<CEjecucion> lista=new ArrayList<CEjecucion>();
		if(CDatabase.connect()){
			Connection conn = CDatabase.getConnection();
			try{
				PreparedStatement pstm1 =  conn.prepareStatement("select r.renglon, r.nombre renglon_nombre, sg.nombre subgrupo_nombre, g.nombre grupo_nombre, a.* FROM " + 
						"( " + 
						"select  " + 
						"  ep.renglon, ep.grupo, ep.subgrupo " + 
						", sum(case when (ep.mes = ?) then ep.ano_1 else 0 end) ano_1 " + 
						", sum(case when (ep.mes = ?) then ep.ano_2 else 0 end) ano_2 " + 
						", sum(case when (ep.mes = ?) then ep.ano_3 else 0 end) ano_3 " + 
						", sum(case when (ep.mes = ?) then ep.ano_4 else 0 end) ano_4 " + 
						", sum(case when (ep.mes = ?) then ep.ano_5 else 0 end) ano_5 " + 
						", sum(case when (ep.mes = ?) then ep.ano_actual else 0 end) ejecutado " + 
						", sum(case when (ep.mes <= ?) then ep.ano_actual else 0 end) ejecutado_acumulado " + 
						", sum(case when (ep.mes = ?) then ep.vigente else 0 end) vigente " + 
						"from mv_ejecucion_presupuestaria ep " + 
						"where ep.mes <= ? " + 
						"and ep.fuente IN ("+fuentes+") "+
						"and ep.grupo IN ("+gruposGasto+") "+
						"and ep.entidad = ? "+
						(unidad_ejecutora!=null ? " and ep.unidad_ejecutora = "+unidad_ejecutora : "") +
						(programa!=null ? " and ep.programa = "+programa : "") +
						(subprograma!=null ? " and ep.subprograma = "+subprograma : "") +
						(proyecto!=null ? " and ep.proyecto = "+proyecto : "") +
						(actividad!=null ? " and ep.actividad = "+actividad : "") +
						(obra!=null ? " and ep.obra = "+obra : "") +
						"  group by ep.renglon, ep.grupo, ep.subgrupo " + 
						") a, renglones r, renglones sg, cp_grupos_gasto g " + 
						"where a.renglon = r.renglon "
						+ "and r.ejercicio = ? "
						+ "and a.vigente > 0 "
						+ "and a.ejecutado_acumulado > 0 "
						+ "and r.subgrupo = sg.subgrupo "
						+ "and r.subgrupo = sg.renglon "
						+ "and sg.ejercicio = r.ejercicio "
						+ "and g.ejercicio = r.ejercicio "
						+ "and g.grupo_gasto = r.grupo "
						+ "order by r.renglon");				
				pstm1.setInt(1, mes);
				pstm1.setInt(2, mes);
				pstm1.setInt(3, mes);
				pstm1.setInt(4, mes);
				pstm1.setInt(5, mes);
				pstm1.setInt(6, mes);
				pstm1.setInt(7, mes);
				pstm1.setInt(8, mes);
				pstm1.setInt(9, mes);
				pstm1.setInt(10, entidad);
				pstm1.setInt(11, (new DateTime()).getYear());
				ResultSet results = pstm1.executeQuery();	
				int grupo_actual=-1;
				int subgrupo_actual =-1;
				ArrayList<CEjecucion> subgrupos=new ArrayList<CEjecucion>();
				ArrayList<CEjecucion> renglones=new ArrayList<CEjecucion>();
				
				Double g_ano1=0.0, g_ano2=0.0,g_ano3=0.0,g_ano4=0.0,g_ano5=0.0, g_ejecutado=0.0, g_acumulado=0.0, g_vigente=0.0;
				Double sg_ano1=0.0, sg_ano2=0.0, sg_ano3=0.0, sg_ano4=0.0, sg_ano5=0.0, sg_ejecutado=0.0, sg_acumulado=0.0, sg_vigente=0.0;
				CEjecucion egrupo=null, esubgrupo=null;
				boolean cambio_grupo=false;
				while (results.next()){
					if(grupo_actual!=results.getInt("grupo")){
						if(grupo_actual>-1){
							lista.add(egrupo);
							g_ano1+=sg_ano1; g_ano2+=sg_ano2; g_ano3+=sg_ano3; g_ano4+=sg_ano4; g_ano5+=sg_ano5;
							g_ejecutado += sg_ejecutado; g_acumulado += sg_acumulado; g_vigente += sg_vigente;
							esubgrupo.setAno1(sg_ano1); esubgrupo.setAno2(sg_ano2); esubgrupo.setAno3(sg_ano3); esubgrupo.setAno4(sg_ano4); esubgrupo.setAno5(sg_ano5); 
							esubgrupo.setEjecutado(sg_ejecutado); esubgrupo.setEjecutado_acumulado(sg_acumulado); esubgrupo.setVigente(sg_vigente);
							subgrupos.add(esubgrupo);
							subgrupos.addAll(renglones);
							lista.addAll(subgrupos);
							egrupo.setAno1(g_ano1); egrupo.setAno2(g_ano2); egrupo.setAno3(g_ano3); egrupo.setAno4(g_ano4); egrupo.setAno5(g_ano5); 
							egrupo.setEjecutado(g_ejecutado); egrupo.setEjecutado_acumulado(g_acumulado); egrupo.setVigente(g_vigente);
							subgrupos.clear();
							renglones.clear();
							cambio_grupo = true;
							sg_ano1=0.0; sg_ano2=0.0; sg_ano3=0.0; sg_ano4=0.0; sg_ano5=0.0; sg_ejecutado=0.0; sg_acumulado=0.0; sg_vigente=0.0;
						}
						grupo_actual = results.getInt("grupo");
						egrupo = new CEjecucion(0,grupo_actual, results.getString("grupo_nombre"), 0.0, 
								0.0, 0.0, 0.0, 0.0,null, 0.0, 0.0, 0.0, 0.0,0.0);
						g_ano1=0.0; g_ano2=0.0; g_ano3=0.0; g_ano4=0.0; g_ano5=0.0; g_ejecutado=0.0; g_acumulado=0.0; g_vigente=0.0;
					}
					if(subgrupo_actual!=results.getInt("subgrupo")){
						if(subgrupo_actual>0 && !cambio_grupo){
							esubgrupo.setAno1(sg_ano1); esubgrupo.setAno2(sg_ano2); esubgrupo.setAno3(sg_ano3); esubgrupo.setAno4(sg_ano4); esubgrupo.setAno5(sg_ano5); 
							esubgrupo.setEjecutado(sg_ejecutado); esubgrupo.setEjecutado_acumulado(sg_acumulado); esubgrupo.setVigente(sg_vigente);
							subgrupos.add(esubgrupo);
							subgrupos.addAll(renglones);
							renglones.clear();
						}
						else
							cambio_grupo=false;
						subgrupo_actual = results.getInt("subgrupo");
						esubgrupo = new CEjecucion(1,subgrupo_actual, results.getString("subgrupo_nombre"), 0.0, 
								0.0, 0.0, 0.0, 0.0,null, 0.0, 0.0, 0.0, 0.0,0.0);
						g_ano1+=sg_ano1; g_ano2+=sg_ano2; g_ano3+=sg_ano3; g_ano4+=sg_ano4; g_ano5+=sg_ano5;
						g_ejecutado += sg_ejecutado; g_acumulado += sg_acumulado; g_vigente += sg_vigente;
						sg_ano1=0.0; sg_ano2=0.0; sg_ano3=0.0; sg_ano4=0.0; sg_ano5=0.0; sg_ejecutado=0.0; sg_acumulado=0.0; sg_vigente=0.0;
						
					}
					CEjecucion renglon = new CEjecucion((Integer)null, results.getInt("renglon"), results.getString("renglon_nombre"), results.getDouble("ano_1"), 
							results.getDouble("ano_2"), results.getDouble("ano_3"), results.getDouble("ano_4"), results.getDouble("ano_5"), 
							null, null, null, results.getDouble("ejecutado"), results.getDouble("ejecutado_acumulado"), 
							results.getDouble("vigente"));
					sg_ano1 += results.getDouble("ano_1");
					sg_ano2 += results.getDouble("ano_2");
					sg_ano3 += results.getDouble("ano_3");
					sg_ano4 += results.getDouble("ano_4");
					sg_ano5 += results.getDouble("ano_5");
					sg_ejecutado += results.getDouble("ejecutado");
					sg_acumulado += results.getDouble("ejecutado_acumulado");
					sg_vigente += results.getDouble("vigente");
					renglones.add(renglon);
				}
				lista.add(egrupo);
				g_ano1+=sg_ano1; g_ano2+=sg_ano2; g_ano3+=sg_ano3; g_ano4+=sg_ano4; g_ano5+=sg_ano5;
				g_ejecutado += sg_ejecutado; g_acumulado += sg_acumulado; g_vigente += sg_vigente;
				sg_ano1=0.0; sg_ano2=0.0; sg_ano3=0.0; sg_ano4=0.0; sg_ano5=0.0; sg_ejecutado=0.0; sg_acumulado=0.0;
				esubgrupo.setAno1(sg_ano1); esubgrupo.setAno2(sg_ano2); esubgrupo.setAno3(sg_ano3); esubgrupo.setAno4(sg_ano4); esubgrupo.setAno5(sg_ano5); 
				esubgrupo.setEjecutado(sg_ejecutado); esubgrupo.setEjecutado_acumulado(sg_acumulado); esubgrupo.setVigente(sg_vigente);
				subgrupos.add(esubgrupo);
				subgrupos.addAll(renglones);
				lista.addAll(subgrupos);
				egrupo.setAno1(g_ano1); egrupo.setAno2(g_ano2); egrupo.setAno3(g_ano3); egrupo.setAno4(g_ano4); egrupo.setAno5(g_ano5); 
				egrupo.setEjecutado(g_ejecutado); egrupo.setEjecutado_acumulado(g_acumulado); egrupo.setVigente(g_vigente);
				
				results.close();
				pstm1.close();
				
			}
			catch(Exception e){
				CLogger.write("7", CEjecucionDAO.class, e);
			}
			finally{
				CDatabase.close(conn);
			}
		}
		return lista.size()>0 ? lista : null;
	}
}
