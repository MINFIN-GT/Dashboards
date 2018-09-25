package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import db.utilities.CDatabase;
import pojo.CEntidad;
import pojo.CGasto;
import pojo.CUnidadEjecutora;
import utilities.CLogger;

public class CEntidadDAO {

	public static ArrayList<CEntidad> getEntidades(int ejercicio) {
		
			final ArrayList<CEntidad> entidades=new ArrayList<CEntidad>();
			Connection conn = CDatabase.connect();
			try{
				if(conn!=null && !conn.isClosed()){
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
			}
			catch(Exception e){
				CLogger.write("1", CEntidadDAO.class, e);
			}
			finally{
				CDatabase.close(conn);
			}
			return entidades.size()>0 ? entidades : null;
		
	}
	
	public static ArrayList<CUnidadEjecutora> getUnidadesEjecutoras(int ejercicio, Integer entidad) {
		
		final ArrayList<CUnidadEjecutora> unidades_ejecutoras=new ArrayList<CUnidadEjecutora>();
		Connection conn = CDatabase.connect();
		try{
			if(conn!=null && !conn.isClosed()){
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
		}
		catch(Exception e){
			CLogger.write("2", CEntidadDAO.class, e);
		}
		finally{
			CDatabase.close(conn);
		}
		return unidades_ejecutoras.size()>0 ? unidades_ejecutoras : null;
	
	}
	
	public static Double[] getPronosticosEgresos(int ejercicio, int entidad, int unidad_ejecutora, int ajustado) {
		ArrayList<Double> ret=new ArrayList<Double>();
		Connection conn = CDatabase.connect();
		try{
			if(conn!=null && !conn.isClosed()){
				PreparedStatement pstm1=null;
				
				pstm1 =  conn.prepareStatement("SELECT ejercicio, mes, sum(monto) monto FROM mvp_egreso "
							+ "WHERE ejercicio=? " + (entidad>0 ? " AND entidad=? " : " AND entidad>? ") + 
							" AND unidad_ejecutora=? AND ajustado = ? GROUP BY ejercicio, mes ORDER BY ejercicio, mes ");		
					pstm1.setInt(1, ejercicio);
					pstm1.setInt(2, entidad);
					pstm1.setInt(3, unidad_ejecutora);
					pstm1.setInt(4, ajustado);
				ResultSet results = pstm1.executeQuery();	
				while (results.next()){
					ret.add(results.getDouble("monto"));
				}
				results.close();
				pstm1.close();
			}
		}
		catch(Exception e){
			CLogger.write("3", CEntidadDAO.class, e);
		}
		finally{
			CDatabase.close(conn);
		}
		return ret.toArray(new Double[ret.size()]);
	}
	
	public static Double[] getPronosticosHistoricosEgresos(int ejercicio,int entidad, int unidad_ejecutora) {
		ArrayList<Double> ret=new ArrayList<Double>();
		Connection conn = CDatabase.connect();
		try{
			if(conn!=null && !conn.isClosed()){
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
							+ "WHERE ejercicio = ? GROUP BY ejercicio ORDER BY ejercicio";
				PreparedStatement pstm1 =  conn.prepareStatement(query);	
				pstm1.setInt(1, ejercicio);
				if(entidad>0)
					pstm1.setInt(3, entidad);
				if(unidad_ejecutora>0)
					pstm1.setInt(4, unidad_ejecutora);
				ResultSet results = pstm1.executeQuery();
				double año = 0;
				int num_datos=0;
				while (results.next()){
					for(int i=1; i<=12; i++){
						ret.add(results.getDouble("m"+i));
						año = num_datos==0 ? results.getInt("ejercicio") : año;
						num_datos++;
					}
				}
				ret.add(0, año);
				results.close();
				pstm1.close();
			}
		}
		catch(Exception e){
			CLogger.write("4", CRecursoDAO.class, e);
		}
		finally{
			CDatabase.close(conn);
		}
		return ret.toArray(new Double[ret.size()]);
	}
	
	public static Double[][] getTodaHistoria(int entidad, int unidad_ejecutora) {
		ArrayList<ArrayList<Double>> ret=new ArrayList<ArrayList<Double>>();
		Double[][] ret_array=null;
		Connection conn = CDatabase.connect();
		try{
			if(conn!=null && !conn.isClosed()){
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
		}
		catch(Exception e){
			CLogger.write("5", CEntidadDAO.class, e);
		}
		finally{
			CDatabase.close(conn);
		}
		ret_array=new Double[ret.size()][13];
		for(int i=0; i<ret.size(); i++){
			for(int j=0; j<ret.get(i).size(); j++)
				ret_array[i][j]=ret.get(i).get(j);
		}
		return ret_array;
	}
	
	public static Double[] getPronosticosEgresosSinRegularizaciones(int ejercicio, int entidad, int unidad_ejecutora, int ajustado) {
		ArrayList<Double> ret=new ArrayList<Double>();
		Connection conn = CDatabase.connect();
		try{
			if(conn!=null && !conn.isClosed()){
				PreparedStatement pstm1=null;
				
				pstm1 =  conn.prepareStatement("SELECT ejercicio, mes, sum(monto) monto FROM mvp_egreso_sin_regularizaciones "
							+ "WHERE ejercicio=?  " + (entidad>0 ? " AND entidad=? " : " AND entidad>? ") + 
							" AND unidad_ejecutora=? AND ajustado = ? GROUP BY ejercicio, mes ORDER BY ejercicio, mes");		
					pstm1.setInt(1, ejercicio);
					pstm1.setInt(2, entidad);
					pstm1.setInt(3, unidad_ejecutora);
					pstm1.setInt(4, ajustado);
				ResultSet results = pstm1.executeQuery();	
				while (results.next()){
					ret.add(results.getDouble("monto"));
				}
				results.close();
				pstm1.close();
			}
		}
		catch(Exception e){
			CLogger.write("6", CEntidadDAO.class, e);
		}
		finally{
			CDatabase.close(conn);
		}
		return ret.toArray(new Double[ret.size()]);
	}
	
	public static Double[] getPronosticosHistoricosEgresosSinRegularizaciones(int ejercicio, int entidad, int unidad_ejecutora) {
		ArrayList<Double> ret=new ArrayList<Double>();
		Connection conn = CDatabase.connect();
		try{
			if(conn!=null && !conn.isClosed()){
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
							"	where ejercicio = ? " +
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
							"	where ejercicio = ? " +
							" GROUP BY ejercicio ORDER BY ejercicio";
				PreparedStatement pstm1 =  conn.prepareStatement(query);	
				pstm1.setInt(1, ejercicio);
				if(entidad>0)
					pstm1.setInt(2, entidad);
				if(unidad_ejecutora>0)
					pstm1.setInt(3, unidad_ejecutora);
				ResultSet results = pstm1.executeQuery();
				double año = 0;
				int num_datos=0;
				while (results.next()){
					for(int i=1; i<=12; i++){
						ret.add(results.getDouble("m"+i));
						año = num_datos==0 ? results.getInt("ejercicio") : año;
						num_datos++;
					}
				}
				ret.add(0, año);
				results.close();
				pstm1.close();
			}
		}
		catch(Exception e){
			CLogger.write("7", CRecursoDAO.class, e);
		}
		finally{
			CDatabase.close(conn);
		}
		return ret.toArray(new Double[ret.size()]);
	}
	
	public static ArrayList<CGasto> getPronosticosEgresosTree(int ejercicio, int entidad, int unidad_ejecutora, int ajustado) {
		ArrayList<CGasto> ret=new ArrayList<CGasto>();
		Connection conn = CDatabase.connect();
		try{
			if(conn!=null && !conn.isClosed()){
				PreparedStatement pstm1=null;
				pstm1 =  conn.prepareStatement("SELECT eg.ejercicio, eg.mes, e.entidad, e.unidad_ejecutora, e.nombre, e.ues, sum(monto) monto  " + 
						"FROM cg_entidades e left outer join mvp_egreso eg  " + 
						"on (e.entidad = eg.entidad and e.unidad_ejecutora = eg.unidad_ejecutora) " + 
						"WHERE e.ejercicio = ? " + 
						"and eg.ejercicio = e.ejercicio " + 
						"AND eg.entidad>0  " + 
						"AND eg.ajustado = ?  " + 
						"GROUP BY e.entidad, e.unidad_ejecutora, eg.ejercicio, eg.mes, e.nombre, e.ues " + 
						"ORDER BY e.entidad, e.unidad_ejecutora, eg.ejercicio, eg.mes");		
					pstm1.setInt(1, ejercicio);
					pstm1.setInt(2, ajustado);
				ResultSet results = pstm1.executeQuery();	
				CGasto papa=null;
				CGasto nodo=null;
				int pos=0;
				while (results.next()){
					if(results.getInt("unidad_ejecutora")==0 && results.getInt("ues")>0){ //papa co hijos
						if(papa!=null) {
							ret.add(papa);
							papa=null;
						}
						papa = new CGasto(results.getInt("ejercicio"),results.getInt("entidad"),results.getString("nombre"),
							   new Double[12], new ArrayList<CGasto>());
						while(results.next()&&results.getInt("entidad")==papa.getCodigo()&&results.getInt("unidad_ejecutora")==0);
					}
					else if(results.getInt("unidad_ejecutora")==0 && results.getInt("ues")==0) { //entidad sin hijos
						nodo = new CGasto(results.getInt("ejercicio"),results.getInt("entidad"),results.getString("nombre"),
								new Double[12], new ArrayList<CGasto>());
						nodo.getPronosticos()[0]=results.getDouble("monto");
						pos=1;
						while(results.next()&&results.getInt("entidad")==nodo.getCodigo()) {
							nodo.getPronosticos()[pos]=results.getDouble("monto");
							pos++;
						}
						if(papa!=null) {
							ret.add(papa);
							papa=null;
						}
						ret.add(nodo);
					}
					else if(results.getInt("unidad_ejecutora")>0) { //hijo
						nodo = new CGasto(results.getInt("ejercicio"),results.getInt("unidad_ejecutora"),results.getString("nombre"),
								new Double[12], new ArrayList<CGasto>());
						nodo.getPronosticos()[0]=results.getDouble("monto");
						pos=1;
						while(results.next()&&results.getInt("unidad_ejecutora")==nodo.getCodigo()) {
							nodo.getPronosticos()[pos]=results.getDouble("monto");
							pos++;
						}
						papa.getChildren().add(nodo);
					}
					if(!results.isLast())
						results.previous();
				}
				if(papa!=null) {
					ret.add(papa);
				}
				results.close();
				pstm1.close();
			}
		}
		catch(Exception e){
			CLogger.write("8", CEntidadDAO.class, e);
		}
		finally{
			CDatabase.close(conn);
		}
		return ret.size()>0 ? ret : null;
	}
	
	public static Double[][] getTodaHistoriaSinRegularizaciones(int entidad, int unidad_ejecutora) {
		ArrayList<ArrayList<Double>> ret=new ArrayList<ArrayList<Double>>();
		Double[][] ret_array=null;
		Connection conn = CDatabase.connect();
		try{
			if(conn!=null && !conn.isClosed()){
				String query="";
				if(entidad>0)
					query = "SELECT ejercicio, " +
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
							" FROM mv_gasto_sin_regularizaciones " +
							(entidad>0 ? " WHERE entidad=? " : "") +
							(entidad>0 && unidad_ejecutora>0 ? " AND unidad_ejecutora=? " : "") +
							" GROUP BY ejercicio ORDER BY ejercicio";
				else
					query = "SELECT ejercicio,  " +
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
							" FROM mv_gasto_sin_regularizaciones " +
							" GROUP BY ejercicio ORDER BY ejercicio";
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
		}
		catch(Exception e){
			CLogger.write("9", CEntidadDAO.class, e);
		}
		finally{
			CDatabase.close(conn);
		}
		ret_array=new Double[ret.size()][13];
		for(int i=0; i<ret.size(); i++){
			for(int j=0; j<ret.get(i).size(); j++)
				ret_array[i][j]=ret.get(i).get(j);
		}
		return ret_array;
	}
	
	public static ArrayList<CGasto> getPronosticosEgresosTreeSinRegularizaciones(int ejercicio, int entidad, int unidad_ejecutora, int ajustado) {
		ArrayList<CGasto> ret=new ArrayList<CGasto>();
		Connection conn = CDatabase.connect();
		try{
			if(conn!=null && !conn.isClosed()){
				PreparedStatement pstm1=null;
				pstm1 =  conn.prepareStatement("SELECT eg.ejercicio, eg.mes, e.entidad, e.unidad_ejecutora, e.nombre, e.ues, sum(monto) monto  " + 
						"FROM cg_entidades e left outer join mvp_egreso_sin_regularizaciones eg  " + 
						"on (e.entidad = eg.entidad and e.unidad_ejecutora = eg.unidad_ejecutora) " + 
						"WHERE e.ejercicio = ? " + 
						"and eg.ejercicio =  e.ejercicio  " + 
						"AND eg.entidad>0  " + 
						"AND eg.ajustado = ?  " + 
						"GROUP BY e.entidad, e.unidad_ejecutora, eg.ejercicio, eg.mes, e.nombre, e.ues " + 
						"ORDER BY e.entidad, e.unidad_ejecutora, eg.ejercicio, eg.mes");		
					pstm1.setInt(1, ejercicio);
					pstm1.setInt(2, ajustado);
				ResultSet results = pstm1.executeQuery();	
				CGasto papa=null;
				CGasto nodo=null;
				int pos=0;
				while (results.next()){
					if(results.getInt("unidad_ejecutora")==0 && results.getInt("ues")>0){ //papa co hijos
						if(papa!=null) {
							ret.add(papa);
							papa=null;
						}
						papa = new CGasto(results.getInt("ejercicio"),results.getInt("entidad"),results.getString("nombre"),
							   new Double[12], new ArrayList<CGasto>());
						while(results.next()&&results.getInt("entidad")==papa.getCodigo()&&results.getInt("unidad_ejecutora")==0);
					}
					else if(results.getInt("unidad_ejecutora")==0 && results.getInt("ues")==0) { //entidad sin hijos
						nodo = new CGasto(results.getInt("ejercicio"),results.getInt("entidad"),results.getString("nombre"),
								new Double[12], new ArrayList<CGasto>());
						nodo.getPronosticos()[0]=results.getDouble("monto");
						pos=1;
						while(results.next()&&results.getInt("entidad")==nodo.getCodigo()) {
							nodo.getPronosticos()[pos]=results.getDouble("monto");
							pos++;
						}
						if(papa!=null) {
							ret.add(papa);
							papa=null;
						}
						ret.add(nodo);
					}
					else if(results.getInt("unidad_ejecutora")>0) { //hijo
						nodo = new CGasto(results.getInt("ejercicio"),results.getInt("unidad_ejecutora"),results.getString("nombre"),
								new Double[12], new ArrayList<CGasto>());
						nodo.getPronosticos()[0]=results.getDouble("monto");
						pos=1;
						while(results.next()&&results.getInt("unidad_ejecutora")==nodo.getCodigo()) {
							nodo.getPronosticos()[pos]=results.getDouble("monto");
							pos++;
						}
						papa.getChildren().add(nodo);
					}
					if(!results.isLast())
						results.previous();
				}
				if(papa!=null) {
					ret.add(papa);
				}
				results.close();
				pstm1.close();
			}
		}
		catch(Exception e){
			CLogger.write("10", CEntidadDAO.class, e);
		}
		finally{
			CDatabase.close(conn);
		}
		return ret.size()>0 ? ret : null;
	}

}
