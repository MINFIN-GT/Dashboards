package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import db.utilities.CDatabase;
import pojo.CEventoGC;
import pojo.CModalidadGC;
import utilities.CLogger;

public class CEventoGCDAO {
	
	public static ArrayList<CEventoGC> getEntidadesEventos(int ejercicio){
		final ArrayList<CEventoGC> entidades = new ArrayList<CEventoGC>();
		if(CDatabase.connect()){
			Connection conn = CDatabase.getConnection();
			try{
				PreparedStatement pstm1 =  conn.prepareStatement("SELECT * FROM vw_evento_gc_group WHERE ano_publicacion=? ORDER BY nombre_corto");
				pstm1.setInt(1, ejercicio);
				ResultSet results = pstm1.executeQuery();	
				while (results.next()){
					CEventoGC entidad = new CEventoGC(ejercicio, results.getInt("entidad_compradora"), 
							0, results.getString("nombre"), 
							results.getString("nombre_corto"), 
							new int[]{ results.getInt("mes_1_ano_1"), results.getInt("mes_2_ano_1"),results.getInt("mes_3_ano_1"),results.getInt("mes_4_ano_1"),results.getInt("mes_5_ano_1"),results.getInt("mes_6_ano_1"),results.getInt("mes_7_ano_1"),results.getInt("mes_8_ano_1"),results.getInt("mes_9_ano_1"),results.getInt("mes_10_ano_1"),results.getInt("mes_11_ano_1"),results.getInt("mes_12_ano_1") }, 
							new int[]{ results.getInt("mes_1_ano_2"), results.getInt("mes_2_ano_2"),results.getInt("mes_3_ano_2"),results.getInt("mes_4_ano_2"),results.getInt("mes_5_ano_2"),results.getInt("mes_6_ano_2"),results.getInt("mes_7_ano_2"),results.getInt("mes_8_ano_2"),results.getInt("mes_9_ano_2"),results.getInt("mes_10_ano_2"),results.getInt("mes_11_ano_2"),results.getInt("mes_12_ano_2") }, 
							new int[]{ results.getInt("mes_1_ano_actual"), results.getInt("mes_2_ano_actual"),results.getInt("mes_3_ano_actual"),results.getInt("mes_4_ano_actual"),results.getInt("mes_5_ano_actual"),results.getInt("mes_6_ano_actual"),results.getInt("mes_7_ano_actual"),results.getInt("mes_8_ano_actual"),results.getInt("mes_9_ano_actual"),results.getInt("mes_10_ano_actual"),results.getInt("mes_11_ano_actual"),results.getInt("mes_12_ano_actual") } 
							);
					entidades.add(entidad);
				}
				results.close();
				pstm1.close();
			}
			catch(Exception e){
				CLogger.write("1", CEventoGCDAO.class, e);
			}
			finally{
				CDatabase.close(conn);
			}
		}
		return entidades.size()>0 ? entidades : null;
	}
	
	public static ArrayList<CModalidadGC> getModalidadesEventos(int ejercicio, int mes){
		final ArrayList<CModalidadGC> modalidades = new ArrayList<CModalidadGC>();
		if(CDatabase.connect()){
			Connection conn = CDatabase.getConnection();
			try{
				PreparedStatement pstm1 =  conn.prepareStatement("select modalidad, sum(total_eventos) total_eventos " + 
						"from ( " + 
						"	select case  " + 
						"	when modalidad=33 then 'Compras de baja cuantía' " + 
						"	when modalidad in (1,8) then 'Compras directa' " + 
						"	when modalidad in (5,12) then 'Contrato abierto' " + 
						"	when modalidad in (3,10) then 'Cotización' " + 
						"	when modalidad in (4,11,34,35) then 'Licitación' " + 
						"	when modalidad in (17,18) then 'Subasta' " + 
						"	when modalidad=6 then 'Casos de excepción' " + 
						"	else 'Otras' " + 
						"	end modalidad,  count(*) total_eventos " + 
						"	from mv_evento_gc " + 
						"	where ano_publicacion = ? " + 
						"	and mes_publicacion <= ? " +
						"	and tipo_entidad = 4 " + 
						"	and entidad_compradora in (452, 458, 1253, 1526,413,414,415,416,423,424,426,428,429,432,433,435,436,437,439,444,445,500,513,571,724,845,933,1214,1216,4,5,6,7,8,9,10,11,12,13,14,15,16,20,1220) " + 
						"	group by modalidad " + 
						") t1 " + 
						"group by modalidad " + 
						"order by modalidad ");
				pstm1.setInt(1, ejercicio);
				pstm1.setInt(2, mes);
				ResultSet results = pstm1.executeQuery();	
				while (results.next()){
					CModalidadGC modalidad = new CModalidadGC(0, results.getString("modalidad"), results.getInt("total_eventos"));
					modalidades.add(modalidad);
				}
				results.close();
				pstm1.close();
			}
			catch(Exception e){
				CLogger.write("2", CEventoGCDAO.class, e);
			}
			finally{
				CDatabase.close(conn);
			}
		}
		
		return modalidades.size()>0 ? modalidades : null;
	}
	
	public static ArrayList<CModalidadGC> getEstadosEventos(int ejercicio, int mes){
		final ArrayList<CModalidadGC> estados = new ArrayList<CModalidadGC>();
		if(CDatabase.connect()){
			Connection conn = CDatabase.getConnection();
			try{
				PreparedStatement pstm1 =  conn.prepareStatement("select estado, sum(total_eventos) total_eventos " + 
						"from ( " + 
						"select case  " + 
						"when estatus_concurso in (1,2,6,7,14) then 'En proceso' " + 
						"when estatus_concurso in (3,8,9,10,12,13) then 'Terminados adjudicados' " + 
						"when estatus_concurso in (4,5,11,15,16) then 'Terminados no adjudicados' " + 
						"end estado, count(*) total_eventos " + 
						"from mv_evento_gc " + 
						"where ano_publicacion = ? " + 
						"and mes_publicacion <= ? "+
						"and tipo_entidad = 4 " + 
						"and entidad_compradora in (452, 458, 1253, 1526,413,414,415,416,423,424,426,428,429,432,433,435,436,437,439,444,445,500,513,571,724,845,933,1214,1216,4,5,6,7,8,9,10,11,12,13,14,15,16,20,1220) " + 
						"group by estatus_concurso " +
						") t1 " + 
						"group by estado "+
						"order by estado ");
				pstm1.setInt(1, ejercicio);
				pstm1.setInt(2, mes);
				ResultSet results = pstm1.executeQuery();	
				while (results.next()){
					CModalidadGC estado = new CModalidadGC(0, results.getString("estado"), results.getInt("total_eventos"));
					estados.add(estado);
				}
				results.close();
				pstm1.close();
			}
			catch(Exception e){
				CLogger.write("3", CEventoGCDAO.class, e);
			}
			finally{
				CDatabase.close(conn);
			}
		}
		
		return estados.size()>0 ? estados : null;
	}
}
