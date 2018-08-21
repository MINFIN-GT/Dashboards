package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Map;

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
							results.getInt("grupo_ingreso"), results.getInt("clase"),
							results.getInt("seccion"), results.getInt("grupo"), results.getInt("recurso")+" - "+results.getString("nombre"), 0,null,0, null);
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
				CLogger.write("2", CRecursoDAO.class, e);
			}
			finally{
				CDatabase.close(conn);
			}
		}
		
		return auxiliares.size()>0 ? auxiliares : null;
	}

	public static Double[] getPronosticos(int ejercicio, int mes, String[] recursosIds, Map<String,Integer[]> auxiliaresIds, int ajustado, int numero) {
		ArrayList<Double> ret=new ArrayList<Double>();
		if(CDatabase.connect()){
			Connection conn = CDatabase.getConnection();
			try{
				String srecursosIds="";
				String sauxiliaresIds="";
				for(int i=0; recursosIds!=null && i<recursosIds.length; i++){
					srecursosIds+=","+recursosIds[i];
				}
				if(auxiliaresIds!=null) {
					for (Map.Entry<String, Integer[]> entry : auxiliaresIds.entrySet()) {
						sauxiliaresIds+= " OR (recurso="+entry.getKey().substring(1)+" AND auxiliar IN (";
						String auxiliares = "";
						for(int i=0; i<entry.getValue().length;i++){
							auxiliares += ","+entry.getValue()[i];
						}
						sauxiliaresIds+= (auxiliares.length()>0) ? auxiliares.substring(1)+")) " : ")";
					}
				}
				PreparedStatement pstm1=null;
				pstm1 =  conn.prepareStatement("SELECT ejercicio, mes, sum(monto) monto FROM mvp_ingreso_recurso_auxiliar "
							+ "WHERE ((ejercicio=? AND mes>=?) OR (ejercicio>?)) " + 
							(srecursosIds.length()>0 || sauxiliaresIds.length()>0 ? " AND ("  : "") + 
							(srecursosIds.length()>0 ? "recurso IN ("+srecursosIds.substring(1)+")" : "") + 
							(srecursosIds.length()>0 ? sauxiliaresIds  : (sauxiliaresIds.length()>0 ? sauxiliaresIds.substring(3) : "")) + 
							(srecursosIds.length()>0 || sauxiliaresIds.length()>0 ? " ) "  : "") +
							(recursosIds==null && auxiliaresIds==null ? " AND auxiliar>0 " : "") +
							" AND ajustado = ? GROUP BY ejercicio, mes ORDER BY ejercicio, mes LIMIT ? ");		
					pstm1.setInt(1, ejercicio);
					pstm1.setInt(2, mes);
					pstm1.setInt(3, ejercicio);
					pstm1.setInt(4, ajustado);
					pstm1.setInt(5, numero);
				ResultSet results = pstm1.executeQuery();	
				while (results.next()){
					ret.add(results.getDouble("monto"));
				}
				results.close();
				pstm1.close();
			}
			catch(Exception e){
				CLogger.write("3", CRecursoDAO.class, e);
			}
			finally{
				CDatabase.close(conn);
			}
		}
		return ret.toArray(new Double[ret.size()]);
	}

	public static Double[] getHistoricos(int ejercicio, int mes, String[] recursosIds, Map<String,Integer[]> auxiliaresIds, int numero) {
		ArrayList<Double> ret=new ArrayList<Double>();
		DateTime date=new DateTime(ejercicio, mes, 1, 0, 0);
		date = date.minusMonths(numero);
		if(CDatabase.connect()){
			Connection conn = CDatabase.getConnection();
			try{
				String srecursosIds="";
				String sauxiliaresIds="";
				for(int i=0; recursosIds!=null && i<recursosIds.length; i++){
					srecursosIds+=","+recursosIds[i];
				}
				if(auxiliaresIds!=null) {
					for (Map.Entry<String, Integer[]> entry : auxiliaresIds.entrySet()) {
						sauxiliaresIds+= " OR (recurso="+entry.getKey().substring(1)+" AND auxiliar IN (";
						String auxiliares = "";
						for(int i=0; i<entry.getValue().length;i++){
							auxiliares += ","+entry.getValue()[i];
						}
						sauxiliaresIds+= (auxiliares.length()>0) ? auxiliares.substring(1)+")) " : ")";
					}
				}
				String query = "SELECT ejercicio, sum(m1) m1, sum(m2) m2,sum(m3) m3,sum(m4) m4, sum(m5) m5, sum(m6) m6, sum(m7) m7, sum(m8) m8, sum(m9) m9, "
							+ "sum(m10) m10, sum(m11) m11, sum(m12) m12 "
							+ " FROM mv_ingreso_recurso_auxiliar " 
							+ " WHERE ejercicio between ? and ? " 
							+ (srecursosIds.length()>0 || sauxiliaresIds.length()>0 ? " AND ("  : "") + 
							(srecursosIds.length()>0 ? " recurso IN ("+srecursosIds.substring(1)+")" : "" ) 
							+ ( srecursosIds.length()>0 ? sauxiliaresIds  : (sauxiliaresIds.length()>0 ? sauxiliaresIds.substring(3) : "") ) 
							+ (srecursosIds.length()>0 || sauxiliaresIds.length()>0 ? " )"  : "") 
							+ " GROUP BY ejercicio ORDER BY ejercicio";
				
				PreparedStatement pstm1 =  conn.prepareStatement(query);	
				pstm1.setInt(1, date.getYear());
				pstm1.setInt(2, ejercicio);
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
	
	public static Double[][] getTodaHistoria(String[] recursosIds, Map<String,Integer[]> auxiliaresIds) {
		ArrayList<ArrayList<Double>> ret=new ArrayList<ArrayList<Double>>();
		Double[][] ret_array=null;
		if(CDatabase.connect()){
			Connection conn = CDatabase.getConnection();
			try{
				String srecursosIds="";
				String sauxiliaresIds="";
				for(int i=0; recursosIds!=null && i<recursosIds.length; i++){
					srecursosIds+=","+recursosIds[i];
				}
				if(auxiliaresIds!=null) {
					for (Map.Entry<String, Integer[]> entry : auxiliaresIds.entrySet()) {
						sauxiliaresIds+= " OR (recurso="+entry.getKey().substring(1)+" AND auxiliar IN (";
						String auxiliares = "";
						for(int i=0; i<entry.getValue().length;i++){
							auxiliares += ","+entry.getValue()[i];
						}
						sauxiliaresIds+= (auxiliares.length()>0) ? auxiliares.substring(1)+")) " : ")";
					}
				}
				String query= "SELECT ejercicio, sum(m1) m1, sum(m2) m2, sum(m3) m3, sum(m4) m4, sum(m5) m5, sum(m6) m6, " + 
							"sum(m7) m7, sum(m8) m8, sum(m9) m9, sum(m10) m10, sum(m11) m11, sum(m12) m12 " +
							" FROM mv_ingreso_recurso_auxiliar " +
							(srecursosIds.length()>0 || sauxiliaresIds.length()>0 ? " WHERE ("  : "") + 
							(srecursosIds.length()>0 ? " recurso IN ("+srecursosIds.substring(1)+")" : "") +
							(srecursosIds.length()>0 ? sauxiliaresIds : (sauxiliaresIds.length()>0 ? sauxiliaresIds.substring(3) : "") ) +
							(srecursosIds.length()>0 || sauxiliaresIds.length()>0 ? " ) "  : "") +
							" GROUP BY ejercicio ORDER BY ejercicio";
				PreparedStatement pstm1 =  conn.prepareStatement(query);	
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
	
	public static CRecurso getRecursosTree(Integer ejercicio){
		CRecurso ret = new CRecurso(ejercicio, 0, "RECURSOS", null, null, null, null, "RECURSO", -1, new ArrayList<CRecurso>(),0, null);
		if(CDatabase.connect()){
			Connection conn = CDatabase.getConnection();
			CRecurso hijo= null;
			try{
				PreparedStatement pstm =  conn.prepareStatement("select * " + 
						"from ( " + 
							"SELECT c1.recurso parent, c.ejercicio ejercicio, c.recurso recurso, -1 auxiliar, c.nombre, c.grupo_ingreso, c.clase, c.seccion, c.grupo  " + 
							"FROM cp_recursos c left outer join cp_recursos c1  " + 
							"				    ON ( c.ejercicio = c1.ejercicio  " + 
							"						and ( " + 
							"							( " + 
							"							 (c.recurso%10>=0 and c1.recurso = (c.recurso - c.recurso%10)) OR " + 
							"							 (c.recurso%100>=10 and c.recurso%10=0 and c1.recurso = (c.recurso - c.recurso%100)) OR " + 
							"							  (c.recurso%1000>=100 and c.recurso%100=0 and c1.recurso = (c.recurso - c.recurso%1000)) " + 
							"							) " + 
							"							and c1.recurso<>c.recurso " + 
							"						) " + 
							"					)  " + 
							"WHERE c.ejercicio = ?    " + 
							"UNION ALL " + 
							"SELECT ca.recurso, ca.ejercicio, ca.recurso, ca.recurso_auxiliar, ca.nombre, null, null, null, null  " + 
							"FROM cp_recursos_auxiliares ca " + 
							"WHERE ca.ejercicio=? and ca.recurso_auxiliar > 0 " + 
						") t1 " + 
						"order by t1.recurso, t1.auxiliar");
				pstm.setInt(1, ejercicio);
				pstm.setInt(2, ejercicio);
				ResultSet rs = pstm.executeQuery();
				while(rs.next()){
					rs.getInt("parent");
					if(rs.wasNull()){
						hijo = new CRecurso(ejercicio, rs.getInt("recurso"), rs.getString("nombre"), rs.getInt("grupo_ingreso"),
								rs.getInt("clase"), rs.getInt("seccion"), rs.getInt("grupo"), rs.getInt("recurso")+" "+rs.getString("nombre"), rs.getInt("auxiliar"), new ArrayList<CRecurso>(),0, null);
						ArrayList<CRecurso> hijos = getRecursosTreeHijos(hijo.getRecurso(), rs); 
						if(hijos!=null)
							hijo.getChildren().addAll(hijos);
						ret.getChildren().add(hijo);
					}
				}
			}
			catch(Throwable e){
				CLogger.write("6", CRecursoDAO.class, e);
			}
		}
		return ret;
	} 
	
	public static ArrayList<CRecurso> getRecursosTreeHijos(Integer recurso, ResultSet rs){
		ArrayList<CRecurso> ret = new ArrayList<CRecurso>();
		try{
			CRecurso hijo = null;
			while(rs.next()){
				if(rs.getInt("parent")==recurso){
					hijo = new CRecurso(rs.getInt("ejercicio"), rs.getInt("recurso"), rs.getString("nombre"), rs.getInt("grupo_ingreso"),
							rs.getInt("clase"), rs.getInt("seccion"), rs.getInt("grupo"), 
							rs.getInt("auxiliar")==-1 ? rs.getInt("recurso")+" "+rs.getString("nombre") : "<span class='recurso_auxiliar'>" + rs.getInt("auxiliar")+" "+rs.getString("nombre") + "</span>", 
							rs.getInt("auxiliar"), new ArrayList<CRecurso>(),0, null);
					if(rs.getInt("auxiliar")==-1)
						hijo.getChildren().addAll(getRecursosTreeHijos(hijo.getRecurso(), rs));
					ret.add(hijo);
				}
				else{
					rs.previous();
					break;
				}
			}
		}
		catch(Throwable e){
			CLogger.write("7", CRecursoDAO.class, e);
		}
		return ret;
	}
	
	public static ArrayList<CRecurso> getPronosticosDetalle(Integer ejercicio, Integer mes, Integer num_pronosticos,String[] recursosIds, Map<String,Integer[]> auxiliaresIds){
		ArrayList<CRecurso> ret = new ArrayList<CRecurso>();
		if(CDatabase.connect()){
			Connection conn = CDatabase.getConnection();
			try{
				String srecursosIds="";
				String sauxiliaresIds="";
				for(int i=0; recursosIds!=null && i<recursosIds.length; i++){
					srecursosIds+=","+recursosIds[i];
				}
				for (Map.Entry<String, Integer[]> entry : auxiliaresIds.entrySet()) {
					sauxiliaresIds+= " OR ((recurso="+entry.getKey().substring(1)+" OR recurso="+entry.getKey().substring(1, 4)+"00 OR recurso="+entry.getKey().substring(1,3)+"000 OR recurso="+entry.getKey().substring(1,5)+"0) AND auxiliar IN (-1, ";
					String auxiliares = "";
					for(int i=0; i<entry.getValue().length;i++){
						auxiliares += ","+entry.getValue()[i];
					}
					sauxiliaresIds+= (auxiliares.length()>0) ? auxiliares.substring(1)+")) " : ")";
				}
				DateTime start = DateTime.now();
				DateTime end = start.plusMonths(num_pronosticos);
				PreparedStatement pstm =  conn.prepareStatement("select * from ( " +
						"select t3.ejercicio, t3.mes,  t2.clase, t2.seccion, t2.grupo, t2.recurso, ifnull(t3.auxiliar,t2.auxiliar) auxiliar, t2.nombre, t2.nivel, " + 
						"sum(t3.monto) monto " + 
						"from ( " + 
						"select ejercicio,clase, seccion, grupo, recurso, auxiliar, nombre, " + 
						"	case " + 
						"		when clase>0 and seccion=0 then 1 " + 
						"		when clase>0 and seccion>0 and grupo=0 then 2 " + 
						"		when clase>0 and seccion>0 and grupo>0 and auxiliar=-1 then 3 " + 
						"		else 4 " + 
						"	end nivel    " + 
						"						 from (    " + 
						"						 SELECT c1.recurso parent, c.ejercicio ejercicio, c.recurso recurso, -1 auxiliar, c.nombre, c.grupo_ingreso, c.clase, c.seccion, c.grupo     " + 
						"						 FROM cp_recursos c left outer join cp_recursos c1     " + 
						"						 				    ON ( c.ejercicio = c1.ejercicio     " + 
						"						 						and (    " + 
						"						 							(    " + 
						"						 							 (c.recurso%10>=0 and c1.recurso = (c.recurso - c.recurso%10)) OR    " + 
						"						 							 (c.recurso%100>=10 and c.recurso%10=0 and c1.recurso = (c.recurso - c.recurso%100)) OR    " + 
						"						 							  (c.recurso%1000>=100 and c.recurso%100=0 and c1.recurso = (c.recurso - c.recurso%1000))    " + 
						"						 							)    " + 
						"						 							and c1.recurso<>c.recurso    " + 
						"						 						)    " + 
						"						 					)     " + 
						"						 WHERE c.ejercicio = ?       " + 
						"						 and c.recurso>9999 " + 
						"						 UNION ALL    " + 
						"						 SELECT ca.recurso, ca.ejercicio, ca.recurso, ca.recurso_auxiliar, ca.nombre, null,  " + 
						"						 floor(ca.recurso/1000), floor(((ca.recurso - floor(ca.recurso/1000)*1000) - (ca.recurso%100))/100),  " + 
						"						 floor(ca.recurso%100)    " + 
						"						 FROM cp_recursos_auxiliares ca    " + 
						"						 WHERE ca.ejercicio = ? and ca.recurso_auxiliar > 0    " + 
						"						 ) t1    " + 
						") t2 left outer join " + 
						"( " + 
						"	select ejercicio, mes, recurso,auxiliar,sum(monto) monto " + 
						"	from mvp_ingreso_recurso_auxiliar  " + 
						"	where ejercicio between ? and ? " + 
						"	AND ajustado = 0 "+
						"	group by ejercicio, mes, recurso, auxiliar " + 
						") t3 " + 
						"on (t2.recurso = t3.recurso and t2.auxiliar=t3.auxiliar) "+
						"group by t3.ejercicio, t3.mes, t2.clase, t2.seccion, t2.grupo, t2.recurso, ifnull(t3.auxiliar,t2.auxiliar), t2.nombre, t2.nivel " + 
						") t4 " + 
						(srecursosIds.length()>0 || sauxiliaresIds.length()>0 ? " WHERE ("  : "") + 
						(srecursosIds.length()>0 ? " recurso IN ( "+srecursosIds.substring(1)+")" : "") +
						(srecursosIds.length()>0 ? sauxiliaresIds : (sauxiliaresIds.length()>0 ? sauxiliaresIds.substring(3) : "") ) +
						(srecursosIds.length()>0 || sauxiliaresIds.length()>0 ? " ) "  : "") +
						"order by clase, seccion, grupo, recurso, auxiliar, ejercicio, mes, nivel");
				pstm.setInt(1, start.getYear());
				pstm.setInt(2, start.getYear());
				pstm.setInt(3, start.getYear());
				pstm.setInt(4, end.getYear());
				ResultSet rs = pstm.executeQuery();
				CRecurso recurso=null;
				int clase_actual = -1;
				int seccion_actual = -1;
				int grupo_actual = -1;
				int recurso_actual = -1;
				int auxiliar_actual = -1;
				Double monto=null;
				while(rs.next()){
					if(clase_actual!= rs.getInt("clase")){
						if(recurso!=null)
							ret.add(recurso);
						recurso = new CRecurso();
						recurso.setNombre(rs.getString("nombre"));
						recurso.setClase(rs.getInt("clase"));
						recurso.setSeccion(0);
						recurso.setGrupo(0);
						recurso.setRecurso(0);
						recurso.setAuxiliar(0);
						recurso.setNivel(1);
						recurso.setPronosticos(new Double[num_pronosticos]);
						clase_actual=recurso.getClase();
						auxiliar_actual = -1;
					}
					else if(seccion_actual!=rs.getInt("seccion")){
						if(recurso!=null)
							ret.add(recurso);
						recurso = new CRecurso();
						recurso.setNombre(rs.getString("nombre"));
						recurso.setClase(rs.getInt("clase"));
						recurso.setSeccion(rs.getInt("seccion"));
						recurso.setGrupo(0);
						recurso.setRecurso(0);
						recurso.setAuxiliar(0);
						recurso.setNivel(2);
						recurso.setPronosticos(new Double[num_pronosticos]);
						seccion_actual=recurso.getSeccion();
						auxiliar_actual = -1;
					}
					else if(grupo_actual!=rs.getInt("grupo") && (rs.getInt("grupo")%10)==0){
						if(recurso!=null)
							ret.add(recurso);
						recurso = new CRecurso();
						recurso.setNombre(rs.getString("nombre"));
						recurso.setClase(rs.getInt("clase"));
						recurso.setSeccion(rs.getInt("seccion"));
						recurso.setGrupo(rs.getInt("grupo"));
						recurso.setRecurso(0);
						recurso.setAuxiliar(0);
						recurso.setNivel(3);
						recurso.setPronosticos(new Double[num_pronosticos]);
						grupo_actual=recurso.getGrupo();
						auxiliar_actual = -1;
					}
					else if(recurso_actual!=rs.getInt("recurso")){
						if(recurso!=null)
							ret.add(recurso);
						recurso = new CRecurso();
						recurso.setNombre(rs.getString("nombre"));
						recurso.setClase(rs.getInt("clase"));
						recurso.setSeccion(rs.getInt("seccion"));
						recurso.setGrupo(rs.getInt("grupo"));
						recurso.setRecurso(rs.getInt("recurso"));
						recurso.setAuxiliar(0);
						recurso.setNivel(4);
						recurso.setPronosticos(new Double[num_pronosticos]);
						recurso_actual=recurso.getRecurso();
						auxiliar_actual = - 1;
					}
					else if(auxiliar_actual!=rs.getInt("auxiliar")){
						if(recurso!=null)
							ret.add(recurso);
						recurso = new CRecurso();
						recurso.setNombre(rs.getString("nombre"));
						recurso.setClase(rs.getInt("clase"));
						recurso.setSeccion(rs.getInt("seccion"));
						recurso.setGrupo(rs.getInt("grupo"));
						recurso.setRecurso(rs.getInt("recurso"));
						recurso.setAuxiliar(rs.getInt("auxiliar"));
						recurso.setNivel(5);
						recurso.setPronosticos(new Double[num_pronosticos]);
						auxiliar_actual=recurso.getAuxiliar();
					}
					if(auxiliar_actual>-1 && rs.getInt("ejercicio")>0 && rs.getInt("mes")>0){
						monto = rs.getDouble("monto");
						/// index = month + (year - year_start)*12 - start_month
						recurso.getPronosticos()[rs.getInt("mes") + ((rs.getInt("ejercicio")-start.getYear())*12) - start.getMonthOfYear()-1] = monto;
					}
				}
				ret.add(recurso);
			}
			catch(Throwable e){
				CLogger.write("6", CRecursoDAO.class, e);
			}
		}
		return ret;
	} 
}
