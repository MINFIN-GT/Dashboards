package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.joda.time.DateTime;

import db.utilities.CDatabase;
import pojo.CEntidad;
import pojo.CUnidadEjecutora;
import utilities.CLogger;

public class CEntidadDAO {

	public static ArrayList<CEntidad> getEntidades(int ejercicio) {
		
			final ArrayList<CEntidad> entidades=new ArrayList<CEntidad>();
			if(CDatabase.connect()){
				Connection conn = CDatabase.getConnection();
				try{
					PreparedStatement pstm1 =  conn.prepareStatement("select ejercicio, entidad, nombre from cg_entidades where ejercicio=? "
							+ "AND (entidad between 11130003 AND 11130020 OR entidad = 11140021) order by entidad");
					pstm1.setInt(1, ejercicio);
					ResultSet results = pstm1.executeQuery();	
					while (results.next()){
						CEntidad entidad = new CEntidad(ejercicio, results.getInt("entidad"),results.getString("nombre"), null, results.getInt("entidad")+" - "+results.getString("nombre"));
						entidades.add(entidad);
					}
					results.close();
					pstm1.close();
				}
				catch(Exception e){
					CLogger.write("1", CEntidadDAO.class, e);
				}
				finally{
					CDatabase.close(conn);
				}
			}
			return entidades.size()>0 ? entidades : null;
		
	}
	
	public static ArrayList<CUnidadEjecutora> getUnidadesEjecutoras(int ejercicio, Integer entidad) {
		
		final ArrayList<CUnidadEjecutora> unidades_ejecutoras=new ArrayList<CUnidadEjecutora>();
		if(CDatabase.connect()){
			Connection conn = CDatabase.getConnection();
			try{
				PreparedStatement pstm1 =  conn.prepareStatement("select distinct ejercicio, entidad, unidad_ejecutora, unidad_ejecutora_nombre from mv_estructura where ejercicio=? and entidad = ? and unidad_ejecutora>0 order by entidad, unidad_ejecutora");
				pstm1.setInt(1, ejercicio);
				pstm1.setInt(2, entidad);
				ResultSet results = pstm1.executeQuery();	
				while (results.next()){
					CUnidadEjecutora unidad_ejecutora = new CUnidadEjecutora(ejercicio, results.getInt("entidad"), results.getInt("unidad_ejecutora"), results.getString("unidad_ejecutora_nombre"),
							results.getInt("unidad_ejecutora")+" - "+results.getString("unidad_ejecutora_nombre"));
					unidades_ejecutoras.add(unidad_ejecutora);
				}
				results.close();
				pstm1.close();
			}
			catch(Exception e){
				CLogger.write("2", CEntidadDAO.class, e);
			}
			finally{
				CDatabase.close(conn);
			}
		}
		return unidades_ejecutoras.size()>0 ? unidades_ejecutoras : null;
	
	}
	
	public static Double[] getPronosticosEgresos(int ejercicio, int mes, int entidad, int unidad_ejecutora, int ajustado, int numero) {
		ArrayList<Double> ret=new ArrayList<Double>();
		if(CDatabase.connect()){
			Connection conn = CDatabase.getConnection();
			try{
				PreparedStatement pstm1=null;
				
				pstm1 =  conn.prepareStatement("SELECT ejercicio, mes, sum(monto) monto FROM mvp_egreso "
							+ "WHERE ((ejercicio=? AND mes>=?) OR (ejercicio>?)) " + (entidad>0 ? " AND entidad=? " : " AND entidad>? ") + 
							" AND unidad_ejecutora=? AND ajustado = ? GROUP BY ejercicio, mes ORDER BY ejercicio, mes LIMIT ? ");		
					pstm1.setInt(1, ejercicio);
					pstm1.setInt(2, mes);
					pstm1.setInt(3, ejercicio);
					pstm1.setInt(4, entidad);
					pstm1.setInt(5, unidad_ejecutora);
					pstm1.setInt(6, ajustado);
					pstm1.setInt(7, numero);
				ResultSet results = pstm1.executeQuery();	
				while (results.next()){
					ret.add(results.getDouble("monto"));
				}
				results.close();
				pstm1.close();
			}
			catch(Exception e){
				CLogger.write("3", CEntidadDAO.class, e);
			}
			finally{
				CDatabase.close(conn);
			}
		}
		return ret.toArray(new Double[ret.size()]);
	}
	
	public static Double[] getPronosticosHistoricosEgresos(int ejercicio, int mes, int entidad, int unidad_ejecutora, int numero) {
		ArrayList<Double> ret=new ArrayList<Double>();
		DateTime date=new DateTime(ejercicio, mes, 1, 0, 0);
		date = date.minusMonths(numero);
		if(CDatabase.connect()){
			Connection conn = CDatabase.getConnection();
			try{
				String query="";
				if(entidad>0)
					query = "SELECT ejercicio, sum(m1) m1, sum(m2) m2,sum(m3) m3,sum(m4) m4,sum(m5) m5,sum(m6) m6,sum(m7) m7,sum(m8) m8, sum(m9) m9, sum(m10) m10, sum(m11) m11, sum(m12) m12 "
							+ " FROM mv_ejecucion_presupuestaria_mensualizada "
							+ " WHERE ejercicio between ? and ? " +
							(entidad>0 ? " AND entidad=? " : "") +
							(unidad_ejecutora>0 ? " AND unidad_ejecutora=? " : "") +
							" GROUP BY ejercicio ORDER BY ejercicio";
				else
					query = "SELECT ejercicio, sum(m1) m1, sum(m2) m2, sum(m3) m3, sum(m4) m4, sum(m5) m5, sum(m6) m6, "
							+ "sum(m7) m7, sum(m8) m8, sum(m9) m9, sum(m10) m10, sum(m11) m11, sum(m12) m12 FROM mv_ejecucion_presupuestaria_mensualizada "
							+ "WHERE ejercicio between ? and ? GROUP BY ejercicio ORDER BY ejercicio";
				PreparedStatement pstm1 =  conn.prepareStatement(query);	
				pstm1.setInt(1, date.getYear());
				pstm1.setInt(2, ejercicio);
				if(entidad>0)
					pstm1.setInt(3, entidad);
				if(unidad_ejecutora>0)
					pstm1.setInt(4, unidad_ejecutora);
				ResultSet results = pstm1.executeQuery();
				double año = 0;
				int num_datos=0;
				while (results.next()){
					for(int i=1; i<=12; i++){
						if(((results.getInt("ejercicio")==date.getYear() && i>=date.getMonthOfYear()) || results.getInt("ejercicio")>date.getYear()) && num_datos<numero){
							ret.add(results.getDouble("m"+i));
							año = num_datos==0 ? results.getInt("ejercicio") : año;
							num_datos++;
						}
					}
				}
				ret.add(0, año);
				results.close();
				pstm1.close();
			}
			catch(Exception e){
				CLogger.write("4", CRecursoDAO.class, e);
			}
			finally{
				CDatabase.close(conn);
			}
		}
		return ret.toArray(new Double[ret.size()]);
	}
	
	public static Double[][] getTodaHistoria(int entidad, int unidad_ejecutora) {
		ArrayList<ArrayList<Double>> ret=new ArrayList<ArrayList<Double>>();
		Double[][] ret_array=null;
		if(CDatabase.connect()){
			Connection conn = CDatabase.getConnection();
			try{
				String query="";
				if(entidad>0)
					query = "SELECT ejercicio, sum(m1) m1,sum(m2) m2,sum(m3) m3,sum(m4) m4,sum(m5) m5,sum(m6) m6,sum(m7) m7,sum(m8) m8,sum(m9) m9,sum(m10) m10, sum(m11) m11, sum(m12) m12 "
							+ " FROM mv_ejecucion_presupuestaria_mensualizada" +
							(entidad>0 ? " WHERE entidad=? " : "") +
							(entidad>0 && unidad_ejecutora>0 ? " AND unidad_ejecutora=? " : "") +
							" GROUP BY ejercicio ORDER BY ejercicio";
				else
					query = "SELECT ejercicio, sum(m1) m1, sum(m2) m2, sum(m3) m3, sum(m4) m4, sum(m5) m5, sum(m6) m6, "
							+ "sum(m7) m7, sum(m8) m8, sum(m9) m9, sum(m10) m10, sum(m11) m11, sum(m12) m12 FROM mv_ejecucion_presupuestaria_mensualizada "
							+ " GROUP BY ejercicio ORDER BY ejercicio";
				PreparedStatement pstm1 =  conn.prepareStatement(query);	
				if(entidad>0)
					pstm1.setInt(1, entidad);
				if(unidad_ejecutora>0)
					pstm1.setInt(2, unidad_ejecutora);
				ResultSet results = pstm1.executeQuery();
				while (results.next()){
					ArrayList<Double> temp = new ArrayList<Double>();
					temp.add((double) results.getInt("ejercicio"));
					for(int i=1; i<=12; i++){
						temp.add(results.getDouble("m"+i));
					}
					ret.add(temp);
				}
				results.close();
				pstm1.close();
			}
			catch(Exception e){
				CLogger.write("5", CRecursoDAO.class, e);
			}
			finally{
				CDatabase.close(conn);
			}
		}
		ret_array=new Double[ret.size()][13];
		for(int i=0; i<ret.size(); i++){
			for(int j=0; j<ret.get(i).size(); j++)
				ret_array[i][j]=ret.get(i).get(j);
		}
		return ret_array;
	}
	
	public static Double[] getPronosticosEgresosSinRegularizaciones(int ejercicio, int mes, int entidad, int unidad_ejecutora, int ajustado, int numero) {
		ArrayList<Double> ret=new ArrayList<Double>();
		if(CDatabase.connect()){
			Connection conn = CDatabase.getConnection();
			try{
				PreparedStatement pstm1=null;
				
				pstm1 =  conn.prepareStatement("SELECT ejercicio, mes, sum(monto) monto FROM mvp_egreso_sin_regularizaciones "
							+ "WHERE ((ejercicio=? AND mes>=?) OR (ejercicio>?)) " + (entidad>0 ? " AND entidad=? " : " AND entidad>? ") + 
							" AND unidad_ejecutora=? AND ajustado = ? GROUP BY ejercicio, mes ORDER BY ejercicio, mes LIMIT ? ");		
					pstm1.setInt(1, ejercicio);
					pstm1.setInt(2, mes);
					pstm1.setInt(3, ejercicio);
					pstm1.setInt(4, entidad);
					pstm1.setInt(5, unidad_ejecutora);
					pstm1.setInt(6, ajustado);
					pstm1.setInt(7, numero);
				ResultSet results = pstm1.executeQuery();	
				while (results.next()){
					ret.add(results.getDouble("monto"));
				}
				results.close();
				pstm1.close();
			}
			catch(Exception e){
				CLogger.write("6", CEntidadDAO.class, e);
			}
			finally{
				CDatabase.close(conn);
			}
		}
		return ret.toArray(new Double[ret.size()]);
	}
	
	public static Double[] getPronosticosHistoricosEgresosSinRegularizaciones(int ejercicio, int mes, int entidad, int unidad_ejecutora, int numero) {
		ArrayList<Double> ret=new ArrayList<Double>();
		DateTime date=new DateTime(ejercicio, mes, 1, 0, 0);
		date = date.minusMonths(numero);
		if(CDatabase.connect()){
			Connection conn = CDatabase.getConnection();
			try{
				String query="";
				if(entidad>0)
					query = "select ejercicio,   " + 
							"	sum(case when mes = 1 then (ifnull(gasto,0)-ifnull(deducciones,0)) else 0 end) m1, " + 
							"	sum(case when mes = 2 then (ifnull(gasto,0)-ifnull(deducciones,0)) else 0 end) m2,  " + 
							"	sum(case when mes = 3 then (ifnull(gasto,0)-ifnull(deducciones,0)) else 0 end) m3,  " + 
							"	sum(case when mes = 4 then (ifnull(gasto,0)-ifnull(deducciones,0)) else 0 end) m4,  " + 
							"	sum(case when mes = 5 then (ifnull(gasto,0)-ifnull(deducciones,0)) else 0 end) m5,  " + 
							"	sum(case when mes = 6 then (ifnull(gasto,0)-ifnull(deducciones,0)) else 0 end) m6,  " + 
							"	sum(case when mes = 7 then (ifnull(gasto,0)-ifnull(deducciones,0)) else 0 end) m7,  " + 
							"	sum(case when mes = 8 then (ifnull(gasto,0)-ifnull(deducciones,0)) else 0 end) m8,  " + 
							"	sum(case when mes = 9 then (ifnull(gasto,0)-ifnull(deducciones,0)) else 0 end) m9,  " + 
							"	sum(case when mes = 10 then (ifnull(gasto,0)-ifnull(deducciones,0)) else 0 end) m10,  " + 
							"	sum(case when mes = 11 then (ifnull(gasto,0)-ifnull(deducciones,0)) else 0 end) m11,  " + 
							"	sum(case when mes = 12 then (ifnull(gasto,0)-ifnull(deducciones,0)) else 0 end) m12  " + 
							"	from minfin.mv_gasto_sin_regularizaciones " + 
							"	where ejercicio between ? and ? " +
							(entidad>0 ? " AND entidad=? " : "") +
							(unidad_ejecutora>0 ? " AND unidad_ejecutora=? " : "") +
							" GROUP BY ejercicio ORDER BY ejercicio";
				else
					query = "select ejercicio,   " + 
							"	sum(case when mes = 1 then (ifnull(gasto,0)-ifnull(deducciones,0)) else 0 end) m1, " + 
							"	sum(case when mes = 2 then (ifnull(gasto,0)-ifnull(deducciones,0)) else 0 end) m2,  " + 
							"	sum(case when mes = 3 then (ifnull(gasto,0)-ifnull(deducciones,0)) else 0 end) m3,  " + 
							"	sum(case when mes = 4 then (ifnull(gasto,0)-ifnull(deducciones,0)) else 0 end) m4,  " + 
							"	sum(case when mes = 5 then (ifnull(gasto,0)-ifnull(deducciones,0)) else 0 end) m5,  " + 
							"	sum(case when mes = 6 then (ifnull(gasto,0)-ifnull(deducciones,0)) else 0 end) m6,  " + 
							"	sum(case when mes = 7 then (ifnull(gasto,0)-ifnull(deducciones,0)) else 0 end) m7,  " + 
							"	sum(case when mes = 8 then (ifnull(gasto,0)-ifnull(deducciones,0)) else 0 end) m8,  " + 
							"	sum(case when mes = 9 then (ifnull(gasto,0)-ifnull(deducciones,0)) else 0 end) m9,  " + 
							"	sum(case when mes = 10 then (ifnull(gasto,0)-ifnull(deducciones,0)) else 0 end) m10,  " + 
							"	sum(case when mes = 11 then (ifnull(gasto,0)-ifnull(deducciones,0)) else 0 end) m11,  " + 
							"	sum(case when mes = 12 then (ifnull(gasto,0)-ifnull(deducciones,0)) else 0 end) m12  " + 
							"	from minfin.mv_gasto_sin_regularizaciones " + 
							"	where ejercicio between ? and ? " +
							" GROUP BY ejercicio ORDER BY ejercicio";
				PreparedStatement pstm1 =  conn.prepareStatement(query);	
				pstm1.setInt(1, date.getYear());
				pstm1.setInt(2, ejercicio);
				if(entidad>0)
					pstm1.setInt(3, entidad);
				if(unidad_ejecutora>0)
					pstm1.setInt(4, unidad_ejecutora);
				ResultSet results = pstm1.executeQuery();
				double año = 0;
				int num_datos=0;
				while (results.next()){
					for(int i=1; i<=12; i++){
						if(((results.getInt("ejercicio")==date.getYear() && i>=date.getMonthOfYear()) || results.getInt("ejercicio")>date.getYear()) && num_datos<numero){
							ret.add(results.getDouble("m"+i));
							año = num_datos==0 ? results.getInt("ejercicio") : año;
							num_datos++;
						}
					}
				}
				ret.add(0, año);
				results.close();
				pstm1.close();
			}
			catch(Exception e){
				CLogger.write("4", CRecursoDAO.class, e);
			}
			finally{
				CDatabase.close(conn);
			}
		}
		return ret.toArray(new Double[ret.size()]);
	}

}
