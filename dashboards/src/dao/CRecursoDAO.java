package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.joda.time.DateTime;

import db.utilities.CDatabase;
import pojo.CRecurso;
import pojo.CRecursoAuxiliar;
import utilities.CLogger;

public class CRecursoDAO {
	public static ArrayList<CRecurso> getRecursos(int ejercicio){		
		final ArrayList<CRecurso> recursos=new ArrayList<CRecurso>();
		if(CDatabase.connect()){
			Connection conn = CDatabase.getConnection();
			try{
				PreparedStatement pstm1 =  conn.prepareStatement("SELECT * FROM cp_recursos WHERE ejercicio=? ORDER BY recurso");		
				pstm1.setInt(1, ejercicio);
				ResultSet results = pstm1.executeQuery();	
				while (results.next()){
					CRecurso recurso = new CRecurso(ejercicio, results.getInt("recurso"), results.getString("nombre"),
							results.getInt("grupo_ingreso"), results.getString("hoja"), results.getInt("clase"),
							results.getInt("seccion"), results.getInt("grupo"), results.getInt("recurso")+" - "+results.getString("nombre"));
					recursos.add(recurso);
				}
				results.close();
				pstm1.close();
			}
			catch(Exception e){
				CLogger.write("1", CRecursoDAO.class, e);
			}
			finally{
				CDatabase.close(conn);
			}
		}
		
		return recursos.size()>0 ? recursos : null;
	}

	public static ArrayList<CRecursoAuxiliar> getAuxiliares(int ejercicio, int recurso) {
		final ArrayList<CRecursoAuxiliar> auxiliares=new ArrayList<CRecursoAuxiliar>();
		if(CDatabase.connect()){
			Connection conn = CDatabase.getConnection();
			try{
				PreparedStatement pstm1 =  conn.prepareStatement("SELECT * FROM cp_recursos_auxiliares WHERE ejercicio=? "+( recurso > 0 ? "and recurso=?" : "")+" ORDER BY recurso, recurso_auxiliar");		
				pstm1.setInt(1, ejercicio);
				if(recurso>0)
					pstm1.setInt(2, recurso);
				ResultSet results = pstm1.executeQuery();	
				while (results.next()){
					CRecursoAuxiliar auxiliar = new CRecursoAuxiliar(ejercicio, results.getInt("entidad"), results.getInt("unidad_ejecutora"),
							recurso, results.getInt("recurso_auxiliar"), results.getString("nombre"), results.getString("sigla"),
							results.getInt("recaudacion"), results.getInt("recurso_auxiliar")+" - "+results.getString("nombre"));
					auxiliares.add(auxiliar);
				}
				results.close();
				pstm1.close();
			}
			catch(Exception e){
				CLogger.write("1", CRecursoDAO.class, e);
			}
			finally{
				CDatabase.close(conn);
			}
		}
		
		return auxiliares.size()>0 ? auxiliares : null;
	}

	public static Double[] getPronosticos(int ejercicio, int mes, int recurso, int auxiliar, int ajustado, int numero) {
		ArrayList<Double> ret=new ArrayList<Double>();
		if(CDatabase.connect()){
			Connection conn = CDatabase.getConnection();
			try{
				PreparedStatement pstm1=null;
				
				pstm1 =  conn.prepareStatement("SELECT ejercicio, mes, sum(monto) monto FROM mvp_ingreso_recurso_auxiliar "
							+ "WHERE ((ejercicio=? AND mes>=?) OR (ejercicio>?)) " + (recurso>0 ? " AND recurso=? " : " AND recurso>? ") + 
							" AND auxiliar=? AND ajustado = ? GROUP BY ejercicio, mes ORDER BY ejercicio, mes LIMIT ? ");		
					pstm1.setInt(1, ejercicio);
					pstm1.setInt(2, mes);
					pstm1.setInt(3, ejercicio);
					pstm1.setInt(4, recurso);
					pstm1.setInt(5, auxiliar);
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
				CLogger.write("1", CRecursoDAO.class, e);
			}
			finally{
				CDatabase.close(conn);
			}
		}
		return ret.toArray(new Double[ret.size()]);
	}

	public static Double[] getHistoricos(int ejercicio, int mes, int recurso, int auxiliar, int numero) {
		ArrayList<Double> ret=new ArrayList<Double>();
		DateTime date=new DateTime(ejercicio, mes, 1, 0, 0);
		date = date.minusMonths(numero);
		if(CDatabase.connect()){
			Connection conn = CDatabase.getConnection();
			try{
				String query="";
				if(recurso>0)
					query = "SELECT ejercicio, m1,m2,m3,m4,m5,m6,m7,m8,m9,m10,m11,m12 "
							+ " FROM " +(auxiliar>0 ? "mv_ingreso_recurso_auxiliar " : "mv_ingreso_recurso ")
							+ " WHERE ejercicio between ? and ? " +
							(recurso>0 ? " AND recurso=? " : "") +
							(auxiliar>0 ? " AND auxiliar=? " : "") +
							" ORDER BY ejercicio";
				else
					query = "SELECT ejercicio, sum(m1) m1, sum(m2) m2, sum(m3) m3, sum(m4) m4, sum(m5) m5, sum(m6) m6, "
							+ "sum(m7) m7, sum(m8) m8, sum(m9) m9, sum(m10) m10, sum(m11) m11, sum(m12) m12 FROM mv_ingreso_recurso "
							+ "WHERE ejercicio between ? and ? GROUP BY ejercicio ORDER BY ejercicio";
				PreparedStatement pstm1 =  conn.prepareStatement(query);	
				pstm1.setInt(1, date.getYear());
				pstm1.setInt(2, ejercicio);
				if(recurso>0)
					pstm1.setInt(3, recurso);
				if(auxiliar>0)
					pstm1.setInt(4, auxiliar);
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
				CLogger.write("1", CRecursoDAO.class, e);
			}
			finally{
				CDatabase.close(conn);
			}
		}
		return ret.toArray(new Double[ret.size()]);
	}
	
	public static Double[][] getTodaHistoria(int recurso, int auxiliar) {
		ArrayList<ArrayList<Double>> ret=new ArrayList<ArrayList<Double>>();
		Double[][] ret_array=null;
		if(CDatabase.connect()){
			Connection conn = CDatabase.getConnection();
			try{
				String query="";
				if(recurso>0)
					query = "SELECT ejercicio, m1,m2,m3,m4,m5,m6,m7,m8,m9,m10,m11,m12 "
							+ " FROM " +(auxiliar>0 ? "mv_ingreso_recurso_auxiliar " : "mv_ingreso_recurso ") +
							(recurso>0 ? " WHERE recurso=? " : "") +
							(recurso>0 && auxiliar>0 ? " AND auxiliar=? " : "") +
							" ORDER BY ejercicio";
				else
					query = "SELECT ejercicio, sum(m1) m1, sum(m2) m2, sum(m3) m3, sum(m4) m4, sum(m5) m5, sum(m6) m6, "
							+ "sum(m7) m7, sum(m8) m8, sum(m9) m9, sum(m10) m10, sum(m11) m11, sum(m12) m12 FROM mv_ingreso_recurso "
							+ " GROUP BY ejercicio ORDER BY ejercicio";
				PreparedStatement pstm1 =  conn.prepareStatement(query);	
				if(recurso>0)
					pstm1.setInt(1, recurso);
				if(auxiliar>0)
					pstm1.setInt(2, auxiliar);
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
				CLogger.write("1", CRecursoDAO.class, e);
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
}
