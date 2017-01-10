package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.joda.time.DateTime;

import db.utilities.CDatabase;
import pojo.CEjecucionFisica;
import pojo.CMeta;
import pojo.varios.CEjecucionFisicaMensualizada;
import utilities.CLogger;

public class CEjecucionFisicaDAO {
	
	public static ArrayList<CEjecucionFisicaMensualizada> getEjecucionFisicaMensual(Integer level,Integer ejercicio, Integer entidad, Integer unidad_ejecutora, Integer programa, Integer subprograma,
			Integer proyecto, Integer actividad, Integer obra){		
		final ArrayList<CEjecucionFisicaMensualizada> entidades=new ArrayList<CEjecucionFisicaMensualizada>();
		if(CDatabase.connect()){
			Connection conn = CDatabase.getConnection();
			String proyeccion="";
			String group ="";
			switch(level){
				case 1: proyeccion="ef.entidad id,  "; 
					group = "ef.entidad";
					break;
				case 2: proyeccion="ef.unidad_ejecutora id, "; 
					 group = "ef.unidad_ejecutora";
					 break;
				case 3: proyeccion="ef.programa id, "; 
					 group = "ef.programa";
					 break;
				case 4: proyeccion="ef.subprograma id, "; 
					 group = "ef.subprograma";
					 break;
				case 5: proyeccion="ef.proyecto id, ";
					 group = "ef.proyecto";
					 break;
				case 6: proyeccion="ef.actividad id, ef.obra, "; 
					 group = "ef.actividad, ef.obra";
					 break;
			}
			try{
				if(!conn.isClosed()){
					DateTime now = new DateTime();    
					PreparedStatement pstm1 =  conn.prepareStatement("select ef.ejercicio,        " + 
				 	 proyeccion+" " + 
				 	"  sum(ep.ejecucion_1)/sum(ep.vigente_1) ejecucion_financiera_1,   " + 
				 	"  sum(ep.ejecucion_2)/sum(ep.vigente_2) ejecucion_financiera_2,   " + 
				 	"  sum(ep.ejecucion_3)/sum(ep.vigente_3) ejecucion_financiera_3,   " + 
				 	"  sum(ep.ejecucion_4)/sum(ep.vigente_4) ejecucion_financiera_4,   " + 
				 	"  sum(ep.ejecucion_5)/sum(ep.vigente_5) ejecucion_financiera_5,   " + 
				 	"  sum(ep.ejecucion_6)/sum(ep.vigente_6) ejecucion_financiera_6,   " + 
				 	"  sum(ep.ejecucion_7)/sum(ep.vigente_7) ejecucion_financiera_7,   " + 
				 	"  sum(ep.ejecucion_8)/sum(ep.vigente_8) ejecucion_financiera_8,   " + 
				 	"  sum(ep.ejecucion_9)/sum(ep.vigente_9) ejecucion_financiera_9,   " + 
				 	"  sum(ep.ejecucion_10)/sum(ep.vigente_10) ejecucion_financiera_10,   " + 
				 	"  sum(ep.ejecucion_11)/sum(ep.vigente_11) ejecucion_financiera_11,   " + 
				 	"  sum(ep.ejecucion_12)/sum(ep.vigente_12) ejecucion_financiera_12,    " + 
				 	"  sum(ef.ejecucion_porcentaje_1*ep.vigente_1)/sum(ep.vigente_1) ejecucion_fisica_1,   " + 
				 	"  sum(ef.ejecucion_porcentaje_2*ep.vigente_2)/sum(ep.vigente_2) ejecucion_fisica_2,  " + 
				 	"  sum(ef.ejecucion_porcentaje_3*ep.vigente_3)/sum(ep.vigente_3) ejecucion_fisica_3,  " + 
				 	"  sum(ef.ejecucion_porcentaje_4*ep.vigente_4)/sum(ep.vigente_4) ejecucion_fisica_4,   " + 
				 	"  sum(ef.ejecucion_porcentaje_5*ep.vigente_5)/sum(ep.vigente_5) ejecucion_fisica_5,  " + 
				 	"  sum(ef.ejecucion_porcentaje_6*ep.vigente_6)/sum(ep.vigente_6) ejecucion_fisica_6,   " + 
				 	"  sum(ef.ejecucion_porcentaje_7*ep.vigente_7)/sum(ep.vigente_7) ejecucion_fisica_7,   " + 
				 	"  sum(ef.ejecucion_porcentaje_8*ep.vigente_8)/sum(ep.vigente_8) ejecucion_fisica_8,    " + 
				 	"  sum(ef.ejecucion_porcentaje_9*ep.vigente_9)/sum(ep.vigente_9) ejecucion_fisica_9,    " + 
				 	"  sum(ef.ejecucion_porcentaje_10*ep.vigente_10)/sum(ep.vigente_10) ejecucion_fisica_10,    " + 
				 	"  sum(ef.ejecucion_porcentaje_11*ep.vigente_11)/sum(ep.vigente_11) ejecucion_fisica_11,   " + 
				 	"  sum(ef.ejecucion_porcentaje_12*ep.vigente_12)/sum(ep.vigente_12) ejecucion_fisica_12       " + 
				 	"  from (     " + 
				 	"  select   ejercicio, entidad,      " + 
				 	"  		unidad_ejecutora,        " + 
				 	" 	 	programa,         " + 
				 	" 	 	subprograma,         " + 
				 	" 	 	proyecto,         " + 
				 	" 	 	actividad, obra, " + 
				 	" 		avg(ejecucion_porcentaje_1) ejecucion_porcentaje_1, " + 
				 	" 		avg(ejecucion_porcentaje_2) ejecucion_porcentaje_2, " + 
				 	" 		avg(ejecucion_porcentaje_3) ejecucion_porcentaje_3, " + 
				 	" 		avg(ejecucion_porcentaje_4) ejecucion_porcentaje_4, " + 
				 	" 		avg(ejecucion_porcentaje_5) ejecucion_porcentaje_5, " + 
				 	" 		avg(ejecucion_porcentaje_6) ejecucion_porcentaje_6, " + 
				 	" 		avg(ejecucion_porcentaje_7) ejecucion_porcentaje_7, " + 
				 	" 		avg(ejecucion_porcentaje_8) ejecucion_porcentaje_8, " + 
				 	" 		avg(ejecucion_porcentaje_9) ejecucion_porcentaje_9, " + 
				 	" 		avg(ejecucion_porcentaje_10) ejecucion_porcentaje_10, " + 
				 	" 		avg(ejecucion_porcentaje_11) ejecucion_porcentaje_11, " + 
				 	" 		avg(ejecucion_porcentaje_12) ejecucion_porcentaje_12 " + 
				 	" 		from( " + 
				 	" 	 		select ejercicio, entidad, unidad_ejecutora, programa, subprograma, proyecto,  actividad, obra, codigo_meta,    " + 
				 	" 		 	sum(case when mes=1 then ejecucion else 0 end)/(avg(cantidad)+sum(case when mes<=1 then modificacion else 0 end)) ejecucion_porcentaje_1,   " + 
				 	" 		 	sum(case when mes<=2 then ejecucion else 0 end)/(avg(cantidad)+sum(case when mes<=2 then modificacion else 0 end)) ejecucion_porcentaje_2,   " + 
				 	" 		 	sum(case when mes<=3 then ejecucion else 0 end)/(avg(cantidad)+sum(case when mes<=3 then modificacion else 0 end)) ejecucion_porcentaje_3,   " + 
				 	" 		 	sum(case when mes<=4 then ejecucion else 0 end)/(avg(cantidad)+sum(case when mes<=4 then modificacion else 0 end)) ejecucion_porcentaje_4,   " + 
				 	" 	 		sum(case when mes<=5 then ejecucion else 0 end)/(avg(cantidad)+sum(case when mes<=5 then modificacion else 0 end)) ejecucion_porcentaje_5,   " + 
				 	" 		 	sum(case when mes<=6 then ejecucion else 0 end)/(avg(cantidad)+sum(case when mes<=6 then modificacion else 0 end)) ejecucion_porcentaje_6,   " + 
				 	" 		 	sum(case when mes<=7 then ejecucion else 0 end)/(avg(cantidad)+sum(case when mes<=7 then modificacion else 0 end)) ejecucion_porcentaje_7,   " + 
				 	" 		 	sum(case when mes<=8 then ejecucion else 0 end)/(avg(cantidad)+sum(case when mes<=8 then modificacion else 0 end)) ejecucion_porcentaje_8,   " + 
				 	" 		 	sum(case when mes<=9 then ejecucion else 0 end)/(avg(cantidad)+sum(case when mes<=9 then modificacion else 0 end)) ejecucion_porcentaje_9,   " + 
				 	"  			sum(case when mes<=10 then ejecucion else 0 end)/(avg(cantidad)+sum(case when mes<=10 then modificacion else 0 end)) ejecucion_porcentaje_10,   " + 
				 	" 		 	sum(case when mes<=11 then ejecucion else 0 end)/(avg(cantidad)+sum(case when mes<=11 then modificacion else 0 end)) ejecucion_porcentaje_11,   " + 
				 	" 	 		sum(case when mes<=12 then ejecucion else 0 end)/(avg(cantidad)+sum(case when mes<=12 then modificacion else 0 end)) ejecucion_porcentaje_12       " + 
				 	" 	 		from mv_ejecucion_fisica       " + 
				 	" 	 		where mes <= ?    " + 
				 	"			and ejercicio = ? " +		
				 	"			and entidad = ? " +
				 	" 		   	group by ejercicio, entidad, entidad_nombre, unidad_ejecutora,        " + 
				 	" 		 	programa,  subprograma,  proyecto,  actividad, obra, codigo_meta   " + 
				 	" 		) t1     " + 
				 	" 		group by ejercicio, entidad,      " + 
				 	"  		unidad_ejecutora,        " + 
				 	" 	 	programa,         " + 
				 	" 	 	subprograma,         " + 
				 	" 	 	proyecto,         " + 
				 	" 	 	actividad, obra " + 
				 	"  ) ef left outer join      " + 
				 	"  (       " + 
				 	"  	select ejercicio, entidad, unidad_ejecutora, programa, subprograma, proyecto, actividad, obra,    " + 
				 	"  	sum(case when mes<=1 then ano_actual else 0 end) ejecucion_1,    " + 
				 	"  	sum(case when mes<=2 then ano_actual else 0 end) ejecucion_2,   " + 
				 	"  	sum(case when mes<=3 then ano_actual else 0 end) ejecucion_3,   " + 
				 	"  	sum(case when mes<=4 then ano_actual else 0 end) ejecucion_4,   " + 
				 	"  	sum(case when mes<=5 then ano_actual else 0 end) ejecucion_5,   " + 
				 	"  	sum(case when mes<=6 then ano_actual else 0 end) ejecucion_6,   " + 
				 	"  	sum(case when mes<=7 then ano_actual else 0 end) ejecucion_7,   " + 
				 	"  	sum(case when mes<=8 then ano_actual else 0 end) ejecucion_8,   " + 
				 	"  	sum(case when mes<=9 then ano_actual else 0 end) ejecucion_9,   " + 
				 	"  	sum(case when mes<=10 then ano_actual else 0 end) ejecucion_10,   " + 
				 	"  	sum(case when mes<=11 then ano_actual else 0 end) ejecucion_11,   " + 
				 	"  	sum(case when mes<=12 then ano_actual else 0 end) ejecucion_12, " + 
				 	" 	sum(case when mes=1 then vigente else 0 end) vigente_1,    " + 
				 	"  	sum(case when mes=2 then vigente else 0 end) vigente_2, " + 
				 	" 	sum(case when mes=3 then vigente else 0 end) vigente_3, " + 
				 	" 	sum(case when mes=4 then vigente else 0 end) vigente_4, " + 
				 	" 	sum(case when mes=5 then vigente else 0 end) vigente_5, " + 
				 	" 	sum(case when mes=6 then vigente else 0 end) vigente_6, " + 
				 	" 	sum(case when mes=7 then vigente else 0 end) vigente_7, " + 
				 	" 	sum(case when mes=8 then vigente else 0 end) vigente_8, " + 
				 	" 	sum(case when mes=9 then vigente else 0 end) vigente_9, " + 
				 	" 	sum(case when mes=10 then vigente else 0 end) vigente_10, " + 
				 	" 	sum(case when mes=11 then vigente else 0 end) vigente_11, " + 
				 	" 	sum(case when mes=12 then vigente else 0 end) vigente_12       " + 
				 	"  	from mv_ejecucion_presupuestaria     " + 
				 	"  	where mes <= ?     " + 
				 	"   and ejercicio = ? " +
				 	"	and entidad = ? " +	
				 	"  	group by ejercicio, entidad,entidad, unidad_ejecutora, programa, subprograma, proyecto, actividad, obra " + 
				 	"  ) ep       " + 
				 	"  on ( ef.entidad = ep.entidad " + 
				 	"  and ef.unidad_ejecutora = ep.unidad_ejecutora " + 
				 	"  and ef.programa = ep.programa " + 
				 	"  and ef.subprograma = ep.subprograma " + 
				 	"  and ef.proyecto = ep.proyecto " + 
				 	"  and ef.actividad = ep.actividad " + 
				 	"  and ef.obra = ep.obra     " + 
				 	"  and ef.ejercicio = ep.ejercicio  ) " + 
				 	" where ef.entidad = "+entidad + " " +
				 	(unidad_ejecutora!=null ? " and ef.unidad_ejecutora = "+unidad_ejecutora : "")+
				 	(programa!=null ? " and ef.programa = "+programa : "")+
				 	(subprograma!=null ? " and ef.subprograma = "+subprograma : "")+
				 	(proyecto!=null ? " and ef.proyecto = "+proyecto : "")+
				 	(actividad!=null ? " and ef.actividad = "+actividad : "")+
				 	(obra!=null ? " and ef.obra = "+obra : "")+
				 	"  group by "+group);
					pstm1.setInt(1, ejercicio<now.getYear() ? 12 : now.getMonthOfYear());
					pstm1.setInt(2, ejercicio);
					pstm1.setInt(3, entidad);
					pstm1.setInt(4, ejercicio<now.getYear() ? 12 : now.getMonthOfYear());
					pstm1.setInt(5, ejercicio);
					pstm1.setInt(6, entidad);
					ResultSet results=pstm1.executeQuery();
					while (results.next()){
						 CEjecucionFisicaMensualizada ejecucion = new CEjecucionFisicaMensualizada(
						 		results.getInt("id"),
						 		(obra!=null) ? results.getInt("obra") : null,
						 		null,
						 		results.getDouble("ejecucion_fisica_1")*100,
						 		results.getDouble("ejecucion_fisica_2")*100,
						 		results.getDouble("ejecucion_fisica_3")*100,
						 		results.getDouble("ejecucion_fisica_4")*100,
						 		results.getDouble("ejecucion_fisica_5")*100,
						 		results.getDouble("ejecucion_fisica_6")*100,
						 		results.getDouble("ejecucion_fisica_7")*100,
						 		results.getDouble("ejecucion_fisica_8")*100,
						 		results.getDouble("ejecucion_fisica_9")*100,
						 		results.getDouble("ejecucion_fisica_10")*100,
						 		results.getDouble("ejecucion_fisica_11")*100,
						 		results.getDouble("ejecucion_fisica_12")*100,
						 		results.getDouble("ejecucion_financiera_1")*100,
						 		results.getDouble("ejecucion_financiera_2")*100,
						 		results.getDouble("ejecucion_financiera_3")*100,
						 		results.getDouble("ejecucion_financiera_4")*100,
						 		results.getDouble("ejecucion_financiera_5")*100,
						 		results.getDouble("ejecucion_financiera_6")*100,
						 		results.getDouble("ejecucion_financiera_7")*100,
						 		results.getDouble("ejecucion_financiera_8")*100,
						 		results.getDouble("ejecucion_financiera_9")*100,
						 		results.getDouble("ejecucion_financiera_10")*100,
						 		results.getDouble("ejecucion_financiera_11")*100,
						 		results.getDouble("ejecucion_financiera_12")*100
						 );
						 entidades.add(ejecucion);
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
	
	public static ArrayList<CEjecucionFisica> getEjecucion(Integer level,Integer ejercicio,Integer entidad, Integer unidad_ejecutora, Integer programa, Integer subprograma,
			Integer proyecto, Integer actividad, Integer obra){		
		final ArrayList<CEjecucionFisica> entidades=new ArrayList<CEjecucionFisica>();
		if(CDatabase.connect()){
			Connection conn = CDatabase.getConnection();
			try{
				if(!conn.isClosed()){
					String select = "";
					String select_estructura = "";
					String where_estructura = "";
					String where="";
					String groupby="";
					String groupby_estructura="";
					switch(level){
					 case 1: //Devuelve todas la entidades
					 		select = "ep.entidad id, est.nombre nombre";
					 		select_estructura = "entidad, entidad_nombre nombre";
					 		where_estructura = "";
					 		groupby_estructura = "entidad, entidad_nombre";
					 		where = "est.entidad = ep.entidad";
					 		groupby="ep.entidad, est.nombre";
					 		break;
					 case 2: //Devuelve todas las unidades_ejecutoras
					 	select = "ep.unidad_ejecutora id, est.nombre";
					 	select_estructura = "unidad_ejecutora, unidad_ejecutora_nombre nombre";
					 	where_estructura = " and entidad = "+entidad;
					 	groupby_estructura = "unidad_ejecutora, unidad_ejecutora_nombre";
					 	where = " ep.entidad="+entidad+" and ep.unidad_ejecutora = est.unidad_ejecutora ";
					 	groupby="ep.unidad_ejecutora, est.nombre, est.sigla";
					 	break;
					 case 3: //Devuelve todas las programas
					 	select = "ep.programa id, est.nombre";
					 	select_estructura = "programa, programa_nombre nombre";
					 	where_estructura = " and entidad = "+entidad+" and unidad_ejecutora = "+unidad_ejecutora;
					 	groupby_estructura = "programa, programa_nombre";
					 	where = " ep.entidad="+entidad+" and ep.unidad_ejecutora="+unidad_ejecutora+" and ep.programa = est.programa ";
					 	groupby="ep.programa, est.nombre, est.sigla";
					 	break;
					 case 4: //Devuelve todas las subprogramas
					 	select = "ep.subprograma id, est.nombre";
					 	select_estructura = "subprograma, subprograma_nombre nombre";
					 	where_estructura = " and entidad = "+entidad+" and unidad_ejecutora = "+unidad_ejecutora+" and programa = "+ programa;
					 	groupby_estructura = "subprograma, subprograma_nombre";
					 	where = " ep.entidad="+entidad+" and ep.unidad_ejecutora="+unidad_ejecutora+" and ep.programa = "+programa+" and ep.subprograma = est.subprograma ";
					 	groupby="ep.subprograma, est.nombre, est.sigla";
					 	break;
					 case 5: //Devuelve todas las proyectos
					 	select = "ep.proyecto id, est.nombre";
					 	select_estructura = "proyecto, proyecto_nombre nombre";
					 	where_estructura = " and entidad = "+entidad+" and unidad_ejecutora = "+unidad_ejecutora+" and programa = "+ programa+" and subprograma = "+subprograma;
					 	groupby_estructura = "proyecto, proyecto_nombre";
					 	where = " ep.entidad="+entidad+" and ep.unidad_ejecutora="+unidad_ejecutora+" and ep.programa = "+programa+" and ep.subprograma = "+subprograma+" and ep.proyecto = est.proyecto ";
					 	groupby="ep.proyecto, est.nombre, est.sigla";
					 	break;
					 case 6: //Actividad /Obra
					 	select = "ep.actividad id, ep.obra obra, est.nombre";
					 	select_estructura = "actividad, obra, actividad_obra_nombre nombre";
					 	where_estructura = " and entidad = "+entidad+" and unidad_ejecutora = "+unidad_ejecutora+" and programa = "+ programa+" and subprograma = "+subprograma+" and proyecto = "+proyecto;
					 	groupby_estructura = "actividad, obra, actividad_obra_nombre";
					 	where = " ep.entidad="+entidad+" and ep.unidad_ejecutora="+unidad_ejecutora+" and ep.programa = "+programa+" and ep.subprograma = "+subprograma+" and ep.proyecto="+proyecto+" and ep.actividad = est.actividad and ep.obra = est.obra ";
					 	groupby="ep.actividad, ep.obra, est.nombre, est.sigla";
					 	break;
					}
						DateTime now = new DateTime();    
						PreparedStatement pstm1 =  conn.prepareStatement("SELECT " +select + ",   " + 
					 	"est.sigla,   " + 
					 	"ifnull(sum(ep.ejecucion_presupuestaria),0) ejecucion_presupuestaria,    " + 
					 	"ifnull(sum(ep.vigente_presupuestario),0) vigente_presupuestario,    " + 
					 	"ifnull(sum(ef.ejecucion_fisica_porcentaje*ep.vigente_presupuestario) /    " + 
					 	"sum(ep.vigente_presupuestario),0) ejecucion_fisica_ponderada   " + 
					 	"FROM    " + 
					 	"(   " + 
					 	"	select t1.entidad, t1.unidad_ejecutora, t1.programa, t1.subprograma, t1.proyecto, t1.actividad, t1.obra, " + 
					 	"	avg(ejecucion_fisica_porcentaje) ejecucion_fisica_porcentaje " + 
					 	"	from ( " + 
					 	"		SELECT e.entidad, e.unidad_ejecutora, e.programa, e.subprograma, e.proyecto, e.actividad, e.obra, e.codigo_meta, " + 
					 	"       	sum(e.ejecucion)/(avg(e.cantidad)+sum(e.modificacion)) ejecucion_fisica_porcentaje " + 
					 	" 		FROM mv_ejecucion_fisica e " + 
					 	" 		WHERE e.ejercicio = ? and e.mes <= ?  " + 
					 	"       	group by e.ejercicio, e.entidad, e.unidad_ejecutora, e.programa, e.subprograma, e.proyecto, e.actividad, e.obra, e.codigo_meta " + 
					 	"	) t1 	 " + 
					 	"	group by t1.entidad,  t1.unidad_ejecutora,  t1.programa,  t1.subprograma,  t1.proyecto,  t1.actividad,  t1.obra   " +
					 	") ef left outer join  (   " + 
					 	"	select entidad, unidad_ejecutora, programa, subprograma, proyecto, actividad, obra, sum(ano_actual) ejecucion_presupuestaria,   " + 
					 	"	sum(case when mes=? then vigente else 0 end) vigente_presupuestario   " + 
					 	"	from mv_ejecucion_presupuestaria   " + 
					 	"	where ejercicio=? and mes<=?   " + 
					 	"	group by entidad, unidad_ejecutora, programa, subprograma, proyecto, actividad, obra   " +  
					 	") ep    " + 
					 	"on (   " + 
					 	"	ep.entidad = ef.entidad   " + 
					 	"	and ep.unidad_ejecutora = ef.unidad_ejecutora   " + 
					 	"	and ep.programa = ef.programa   " + 
					 	"	and ep.subprograma = ef.subprograma   " + 
					 	"	and ep.proyecto = ef.proyecto   " + 
					 	"	and ep.actividad = ef.actividad   " + 
					 	"	and ep.obra = ef.obra   " + 
					 	"), (   " + 
					 	"	select "+select_estructura+", sigla   " + 
					 	"	from mv_estructura  " + 
					 	" 	where ejercicio = ? "+ where_estructura +
					 	"	group by "+groupby_estructura+", sigla    " + 
					 	") est   " + 
					 	"where "+where+" " + 
					 	"group by "+groupby+", est.sigla");
					int mes=(ejercicio<now.getYear()) ? 12 : now.getMonthOfYear();
					pstm1.setInt(1, ejercicio);
					pstm1.setInt(2, mes);
					pstm1.setInt(3, mes);
					pstm1.setInt(4, ejercicio);
					pstm1.setInt(5, mes);
					pstm1.setInt(6, ejercicio);
					ResultSet results=pstm1.executeQuery();
					while (results.next()){
						 CEjecucionFisica tentidad = new CEjecucionFisica(level==6 ? results.getInt("obra") : null, results.getInt("id"), 
						 results.getString("nombre"), results.getString("sigla"), 
						 results.getDouble("vigente_presupuestario")>0 ? (results.getDouble("ejecucion_presupuestaria")/results.getDouble("vigente_presupuestario"))*100 : 0, 
						 results.getDouble("ejecucion_fisica_ponderada")*100, 
						 results.getDouble("ejecucion_presupuestaria"), 
						 results.getDouble("vigente_presupuestario"));
						 entidades.add(tentidad);
					}
					results.close();
					pstm1.close();
				}
			}
			catch(Exception e){
				CLogger.write("2", CEjecucionFisicaDAO.class, e);
			}
			finally{
				CDatabase.close(conn);
			}
		}
		return entidades;
	}
	
	public static ArrayList<CMeta> getMetas(Integer ejercicio,Integer entidad, Integer unidad_ejecutora, Integer programa, Integer subprograma,
			Integer proyecto, Integer actividad, Integer obra){		
		final ArrayList<CMeta> metas=new ArrayList<CMeta>();
		if(CDatabase.connect()){
			Connection conn = CDatabase.getConnection();
			try{
				if(!conn.isClosed()){
					PreparedStatement pstm1 =  conn.prepareStatement("select ef.*, m.descripcion, m.fecha_inicio, m.fecha_fin " + 
							"from ( " + 
							"select ef.ejercicio, ef.entidad, ef.unidad_ejecutora, ef.programa, ef.subprograma, ef.proyecto,  " + 
							"ef.actividad, ef.obra, ef.codigo_meta, ef.unidad_nombre, " + 
							"sum(case when ef.mes<=1 then ef.ejecucion else 0 end) ejecucion_1, " + 
							"sum(case when ef.mes<=2 then ef.ejecucion else 0 end) ejecucion_2, " + 
							"sum(case when ef.mes<=3 then ef.ejecucion else 0 end) ejecucion_3, " + 
							"sum(case when ef.mes<=4 then ef.ejecucion else 0 end) ejecucion_4, " + 
							"sum(case when ef.mes<=5 then ef.ejecucion else 0 end) ejecucion_5, " + 
							"sum(case when ef.mes<=6 then ef.ejecucion else 0 end) ejecucion_6, " + 
							"sum(case when ef.mes<=7 then ef.ejecucion else 0 end) ejecucion_7, " + 
							"sum(case when ef.mes<=8 then ef.ejecucion else 0 end) ejecucion_8, " + 
							"sum(case when ef.mes<=9 then ef.ejecucion else 0 end) ejecucion_9, " + 
							"sum(case when ef.mes<=10 then ef.ejecucion else 0 end) ejecucion_10, " + 
							"sum(case when ef.mes<=11 then ef.ejecucion else 0 end) ejecucion_11, " + 
							"sum(case when ef.mes<=12 then ef.ejecucion else 0 end) ejecucion_12, " + 
							"sum(case when ef.mes=1 then ef.modificacion else 0 end) modificacion_1, " + 
							"sum(case when ef.mes=2 then ef.modificacion else 0 end) modificacion_2, " + 
							"sum(case when ef.mes=3 then ef.modificacion else 0 end) modificacion_3, " + 
							"sum(case when ef.mes=4 then ef.modificacion else 0 end) modificacion_4, " + 
							"sum(case when ef.mes=5 then ef.modificacion else 0 end) modificacion_5, " + 
							"sum(case when ef.mes=6 then ef.modificacion else 0 end) modificacion_6, " + 
							"sum(case when ef.mes=7 then ef.modificacion else 0 end) modificacion_7, " + 
							"sum(case when ef.mes=8 then ef.modificacion else 0 end) modificacion_8, " + 
							"sum(case when ef.mes=9 then ef.modificacion else 0 end) modificacion_9, " + 
							"sum(case when ef.mes=10 then ef.modificacion else 0 end) modificacion_10, " + 
							"sum(case when ef.mes=11 then ef.modificacion else 0 end) modificacion_11, " + 
							"sum(case when ef.mes=12 then ef.modificacion else 0 end) modificacion_12, " + 
							"avg(ef.cantidad)+sum(case when ef.mes<=1 then ef.modificacion else 0 end) vigente_1, " + 
							"avg(ef.cantidad)+sum(case when ef.mes<=2 then ef.modificacion else 0 end) vigente_2, " + 
							"avg(ef.cantidad)+sum(case when ef.mes<=3 then ef.modificacion else 0 end) vigente_3, " + 
							"avg(ef.cantidad)+sum(case when ef.mes<=4 then ef.modificacion else 0 end) vigente_4, " + 
							"avg(ef.cantidad)+sum(case when ef.mes<=5 then ef.modificacion else 0 end) vigente_5, " + 
							"avg(ef.cantidad)+sum(case when ef.mes<=6 then ef.modificacion else 0 end) vigente_6, " + 
							"avg(ef.cantidad)+sum(case when ef.mes<=7 then ef.modificacion else 0 end) vigente_7, " + 
							"avg(ef.cantidad)+sum(case when ef.mes<=8 then ef.modificacion else 0 end) vigente_8, " + 
							"avg(ef.cantidad)+sum(case when ef.mes<=9 then ef.modificacion else 0 end) vigente_9, " + 
							"avg(ef.cantidad)+sum(case when ef.mes<=10 then ef.modificacion else 0 end) vigente_10, " + 
							"avg(ef.cantidad)+sum(case when ef.mes<=11 then ef.modificacion else 0 end) vigente_11, " + 
							"avg(ef.cantidad)+sum(case when ef.mes<=12 then ef.modificacion else 0 end) vigente_12 " + 
							"from mv_ejecucion_fisica ef " + 
							"where ef.ejercicio= ? " + 
							"and ef.entidad = ? " + 
							"and ef.unidad_ejecutora = ? " + 
							"and ef.programa = ? " + 
							"and ef.subprograma = ? " + 
							"and ef.proyecto = ? " + 
							"and ef.actividad = ? " + 
							"and ef.obra = ? " + 
							"group by ef.ejercicio,  ef.entidad,  ef.unidad_ejecutora,  ef.programa,  ef.subprograma,  ef.proyecto,  ef.actividad, " + 
							"ef.codigo_meta " + 
							") ef, sf_meta m " + 
							"where ef.ejercicio = m.ejercicio " +
							"and ef.entidad = m.entidad " + 
							"and ef.unidad_ejecutora = m.unidad_ejecutora " + 
							"and ef.programa = m.programa " + 
							"and ef.subprograma = m.subprograma " + 
							"and ef.proyecto = m.proyecto " + 
							"and ef.actividad = m.actividad " + 
							"and ef.obra = m.obra " + 
							"and ef.codigo_meta = m.codigo_meta ");
					pstm1.setInt(1, ejercicio);
					pstm1.setInt(2, entidad);
					pstm1.setInt(3, unidad_ejecutora);
					pstm1.setInt(4, programa);
					pstm1.setInt(5, subprograma);
					pstm1.setInt(6, proyecto);
					pstm1.setInt(7, actividad!=null ? actividad : 0);
					pstm1.setInt(8, obra!=null ? obra : 0);
					ResultSet results=pstm1.executeQuery();
					while (results.next()){
						 CMeta meta = new CMeta(results.getInt("codigo_meta"), results.getString("descripcion"), results.getString("unidad_nombre"), 
								 ejercicio, entidad, unidad_ejecutora, subprograma, subprograma, proyecto, actividad, obra, 
								 results.getTimestamp("fecha_inicio"), 
								 results.getTimestamp("fecha_fin"), 
								 results.getInt("ejecucion_1"), results.getInt("ejecucion_2"), results.getInt("ejecucion_3"), results.getInt("ejecucion_4"), 
								 results.getInt("ejecucion_5"), results.getInt("ejecucion_6"), results.getInt("ejecucion_7"), results.getInt("ejecucion_8"), 
								 results.getInt("ejecucion_9"), results.getInt("ejecucion_10"), results.getInt("ejecucion_11"), results.getInt("ejecucion_12"), 
								 results.getInt("modificacion_1"), results.getInt("modificacion_2"), results.getInt("modificacion_3"), results.getInt("modificacion_4"), 
								 results.getInt("modificacion_5"), results.getInt("modificacion_6"), results.getInt("modificacion_7"), results.getInt("modificacion_8"), 
								 results.getInt("modificacion_9"), results.getInt("modificacion_10"), results.getInt("modificacion_11"), results.getInt("modificacion_12"), 
								 results.getInt("vigente_1"), results.getInt("vigente_2"), results.getInt("vigente_3"), results.getInt("vigente_4"), 
								 results.getInt("vigente_5"), results.getInt("vigente_6"), results.getInt("vigente_7"), results.getInt("vigente_8"), 
								 results.getInt("vigente_9"), results.getInt("vigente_10"), results.getInt("vigente_11"), results.getInt("vigente_12"));
						 metas.add(meta);
					}
					results.close();
					pstm1.close();
				}
			}
			catch(Exception e){
				CLogger.write("3", CEjecucionFisicaDAO.class, e);
			}
			finally{
				CDatabase.close(conn);
			}
		}
		return metas;
	}
}
