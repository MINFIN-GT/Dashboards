package dao.maps;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import db.utilities.CDatabase;
import pojo.maps.CGastoMunicipio;
import utilities.CLogger;
import utilities.Utils;

public class CGastoMunicipioDAO {
	public static enum TIPO {
		ENTIDADES, UNIDADES, PROGRAMAS, SUBPROGRAMAS, PROYECTOS, ACTIVIDADES_OBRAS
	}

	private static String getQuery(TIPO tipo, String fuentes, String grupos) {
		String selectJoin = "";
		String optionJoin = "";
		String selectGroupEjecucion = "";
		String whereEjecucion = "";
		String selectGroupEstructura = "";
		String whereEstructura = "";

		switch (tipo) {
		case ACTIVIDADES_OBRAS:
			selectJoin = " e.actividad + e.obra codigo, e.actividad_obra_nombre nombre ";
			optionJoin = " and e.actividad = a.actividad and e.obra = a.obra " + optionJoin;

			selectGroupEjecucion = " ,actividad ,obra " + selectGroupEjecucion;
			whereEjecucion = " and proyecto = ? " + whereEjecucion;

			selectGroupEstructura = " ,actividad ,obra ,actividad_obra_nombre " + selectGroupEstructura;
			whereEstructura = " and proyecto = ? " + whereEstructura;

		case PROYECTOS:
			selectJoin = (Utils.isNullOrEmpty(selectJoin) ? "	e.proyecto codigo ,e.proyecto_nombre nombre " : selectJoin);
			optionJoin = " and e.proyecto = a.proyecto " + optionJoin;

			selectGroupEjecucion = " ,proyecto " + selectGroupEjecucion;
			whereEjecucion = " and subprograma = ? " + whereEjecucion;

			selectGroupEstructura = " ,proyecto ,proyecto_nombre " + selectGroupEstructura;
			whereEstructura = " and subprograma = ? " + whereEstructura;

		case SUBPROGRAMAS:
			selectJoin = (Utils.isNullOrEmpty(selectJoin) ? "	e.subprograma codigo ,e.subprograma_nombre nombre " : selectJoin);
			optionJoin = " and e.subprograma = a.subprograma " + optionJoin;

			selectGroupEjecucion = " ,subprograma " + selectGroupEjecucion;
			whereEjecucion = " and programa = ? " + whereEjecucion;

			selectGroupEstructura = " ,subprograma ,subprograma_nombre " + selectGroupEstructura;
			whereEstructura = " and programa = ? " + whereEstructura;

		case PROGRAMAS:
			selectJoin = (Utils.isNullOrEmpty(selectJoin) ? "	e.programa codigo ,e.programa_nombre nombre " : selectJoin);
			optionJoin = " and e.programa = a.programa " + optionJoin;

			selectGroupEjecucion = " ,programa " + selectGroupEjecucion;
			whereEjecucion = " and unidad_ejecutora = ? " + whereEjecucion;

			selectGroupEstructura = " ,programa ,programa_nombre " + selectGroupEstructura;
			whereEstructura = " and unidad_ejecutora = ? " + whereEstructura;

		case UNIDADES:
			selectJoin = (Utils.isNullOrEmpty(selectJoin) ? "	e.unidad_ejecutora codigo ,e.unidad_ejecutora_nombre nombre " : selectJoin);
			optionJoin = " and e.unidad_ejecutora = a.unidad_ejecutora " + optionJoin;

			selectGroupEjecucion = " ,unidad_ejecutora " + selectGroupEjecucion;
			whereEjecucion = " and entidad = ? " + whereEjecucion;

			selectGroupEstructura = " ,unidad_ejecutora ,unidad_ejecutora_nombre " + selectGroupEstructura;
			whereEstructura = " and entidad = ? " + whereEstructura;

		case ENTIDADES:
		default:
			selectJoin = (Utils.isNullOrEmpty(selectJoin) ? "	e.entidad codigo ,e.entidad_nombre nombre " : selectJoin);
			optionJoin = " e.entidad = a.entidad " + optionJoin;

			selectGroupEjecucion = " entidad " + selectGroupEjecucion;
			selectGroupEstructura = " entidad, entidad_nombre " + selectGroupEstructura;
			break;
		}
		
		String query = "select  " 
				+ selectJoin 
				+ "	,a.ejecutado " 
				+ "	,a.vigente " 
				+ "	,a.asignado " 
				+ "from ( "
				+ "	select  " 
				+ selectGroupEjecucion 
				+ "		,sum(ano_actual) ejecutado "
				+ "		,sum(case when mes = ? then asignado else 0 end) asignado "
				+ "		,sum(case when mes = ? then vigente else 0 end) vigente " 
				+ "	FROM  "
				+ "		mv_ejecucion_presupuestaria_geografico " 
				+ "	where ejercicio = ? "
				+ "	and geografico = ? " 
				+ "	and mes <= ? " 
				+ (Utils.isNullOrEmpty(fuentes) ? "and fuente is null " : "and fuente in (" + fuentes + ") ")
				+ (Utils.isNullOrEmpty(grupos) ? "and grupo is null " : "and grupo in (" + grupos + ") ")
				+ whereEjecucion 
				+ "	group by  " 
				+ selectGroupEjecucion
				+ ") a left join " 
				+ "( " 
				+ "	select  " 
				+ selectGroupEstructura 
				+ " 	from  "
				+ " 		mv_estructura " 
				+ "	where ejercicio = ? " 
				+ whereEstructura 
				+ "	group by  "
				+ selectGroupEstructura 
				+ ") e " 
				+ "on (  " 
				+ optionJoin 
				+ ") "
				+ "where e.entidad_nombre is not null ";

		return query;
	}

	public static ArrayList<CGastoMunicipio> getGastosMunicipio(int mes, int ejercicio, int geografico, int nivel, String fuentes, String grupos,
			Number... estructuras) {
		ArrayList<CGastoMunicipio> geograficos = new ArrayList<CGastoMunicipio>();

		Connection conn = CDatabase.connect();
		try{
			if(conn!=null && !conn.isClosed()){
				TIPO tipo;
				switch (nivel) {
				case 6: // ACTIVIDADES OBRAS
					tipo = TIPO.ACTIVIDADES_OBRAS;
					break;
				case 5: // PROYECTOS
					tipo = TIPO.PROYECTOS;
					break;
				case 4: // SUBPROGRAMAS
					tipo = TIPO.SUBPROGRAMAS;
					break;
				case 3: // PROGRAMAS
					tipo = TIPO.PROGRAMAS;
					break;
				case 2: // UNIDADES_EJECUTORAS
					tipo = TIPO.UNIDADES;
					break;
				default: // ENTIDADES
					tipo = TIPO.ENTIDADES;
					break;
				}

				String query = getQuery(tipo, fuentes, grupos);

				PreparedStatement pstm = conn.prepareStatement(query);
				
				pstm.setInt(1, mes);
				pstm.setInt(2, mes);
				pstm.setInt(3, ejercicio);
				pstm.setInt(4, geografico);
				pstm.setInt(5, mes);

				switch (nivel) {
				case 6: // ACTIVIDADES OBRAS
					pstm.setLong(6, estructuras[0].longValue());
					pstm.setInt(7, estructuras[1].intValue());
					pstm.setInt(8, estructuras[2].intValue());
					pstm.setInt(9, estructuras[3].intValue());
					pstm.setInt(10, estructuras[4].intValue());
					pstm.setInt(11, ejercicio);
					pstm.setLong(12, estructuras[0].longValue());
					pstm.setInt(13, estructuras[1].intValue());
					pstm.setInt(14, estructuras[2].intValue());
					pstm.setInt(15, estructuras[3].intValue());
					pstm.setInt(16, estructuras[4].intValue());
					break;
				case 5: // PROYECTOS
					pstm.setLong(6, estructuras[0].longValue());
					pstm.setInt(7, estructuras[1].intValue());
					pstm.setInt(8, estructuras[2].intValue());
					pstm.setInt(9, estructuras[3].intValue());
					pstm.setInt(10, ejercicio);
					pstm.setLong(11, estructuras[0].longValue());
					pstm.setInt(12, estructuras[1].intValue());
					pstm.setInt(13, estructuras[2].intValue());
					pstm.setInt(14, estructuras[3].intValue());
					break;
				case 4: // SUBPROGRAMAS
					pstm.setLong(6, estructuras[0].longValue());
					pstm.setInt(7, estructuras[1].intValue());
					pstm.setInt(8, estructuras[2].intValue());
					pstm.setInt(9, ejercicio);
					pstm.setLong(10, estructuras[0].longValue());
					pstm.setInt(11, estructuras[1].intValue());
					pstm.setInt(12, estructuras[2].intValue());
					break;
				case 3: // PROGRAMAS
					pstm.setLong(6, estructuras[0].longValue());
					pstm.setInt(7, estructuras[1].intValue());
					pstm.setInt(8, ejercicio);
					pstm.setLong(9, estructuras[0].longValue());
					pstm.setInt(10, estructuras[1].intValue());
					break;
				case 2: // UNIDADES_EJECUTORAS
					pstm.setLong(6, estructuras[0].longValue());
					pstm.setInt(7, ejercicio);
					pstm.setLong(8, estructuras[0].longValue());
					break;
				case 1: // ENTIDADES
				default: 
					pstm.setInt(6, ejercicio);
					break;
				}
				
				ResultSet results = pstm.executeQuery();
				while (results.next()) {
					CGastoMunicipio detalleGasto = new CGastoMunicipio(results.getString("nombre"), results.getInt("codigo"),
							results.getDouble("ejecutado"), results.getDouble("vigente"),
							results.getDouble("asignado"));

					geograficos.add(detalleGasto);
				}
				results.close();
				pstm.close();

			} 
		}
		catch (Exception e) {
			CLogger.write("1", CGastoGeograficoDAO.class, e);
		} finally {
			CDatabase.close();
		}
		return geograficos;
	}

}
