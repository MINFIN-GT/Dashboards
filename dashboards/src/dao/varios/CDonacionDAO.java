package dao.varios;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import db.utilities.CDatabase;
import pojo.varios.CDonacion;
import utilities.CLogger;

public class CDonacionDAO {
	public static enum TIPO {
		DONACIONES, ENTIDADES, ORGANISMOS
	}

	private static String getQueryDonaciones() {
		StringBuilder sb = new StringBuilder();

		sb.append("select ");
		sb.append("    correlativo");
		sb.append("    ,prestamo_nombre");
		sb.append("    ,prestamo_sigla");
		sb.append("    ,entidad");
		sb.append("    ,entidad_nombre");
		sb.append("    ,unidad_ejecutora");
		sb.append("    ,unidad_ejecutora_nombre");
		sb.append("    ,sum(asignado) asignado");
		sb.append("    ,sum(ejecutado) ejecutado");
		sb.append("    ,sum(vigente) vigente");
		sb.append(" from");
		sb.append("    prestamo");
		sb.append(" group by ");
		sb.append("    correlativo");
		sb.append("    ,prestamo_nombre");
		sb.append("    ,prestamo_sigla");
		sb.append("    ,entidad");
		sb.append("    ,entidad_nombre");
		sb.append("    ,unidad_ejecutora");
		sb.append("    ,unidad_ejecutora_nombre");
		sb.append(" order by");
		sb.append("    prestamo_nombre");
		sb.append("    ,entidad_nombre");
		sb.append("    ,unidad_ejecutora_nombre");

		return sb.toString();
	}

	private static String getQueryDonacionesByEntidad() {
		StringBuilder sb = new StringBuilder();

		sb.append("select ");
		sb.append("	   entidad");
		sb.append("    ,entidad_nombre");
		sb.append("	   ,sum(asignado) asignado");
		sb.append("    ,sum(ejecutado) ejecutado");
		sb.append("    ,sum(vigente) vigente ");
		sb.append(" from");
		sb.append("	   prestamo");
		sb.append(" group by ");
		sb.append("	   entidad");
		sb.append("    ,entidad_nombre ");
		sb.append(" order by ");
		sb.append("    entidad_nombre");

		return sb.toString();
	}

	private static String getQueryDonacionesByOrganismo() {
		StringBuilder sb = new StringBuilder();

		sb.append("select ");
		sb.append("	   organismo");
		sb.append("	   ,organismo_nombre ");
		sb.append("	   ,sum(ejecutado) ejecutado ");
		sb.append(" from ");
		sb.append("	   prestamo");
		sb.append(" group by ");
		sb.append("	   organismo");
		sb.append("    ,organismo_nombre");
		sb.append(" order by");
		sb.append("	   organismo_nombre");

		return sb.toString();
	}

	private static String getQuery(TIPO tipo) {
		switch (tipo) {
		case ORGANISMOS:
			return getQueryDonacionesByOrganismo();
		case ENTIDADES:
			return getQueryDonacionesByEntidad();
		default: // case PRESTAMOS:
			return getQueryDonaciones();
		}
	}

	// CPrestamo prestamo = new CPrestamo(
	// rs.getInt("ejercicio"),
	// rs.getInt("fuente"),
	// rs.getString("fuente_nombre"),
	// rs.getInt("organismo"),
	// rs.getString("organismo_nombre"),
	// rs.getInt("correlativo"),
	// rs.getString("prestamo_nombre"),
	// rs.getString("prestamo_sigla"),
	// rs.getInt("entidad"),
	// rs.getString("entidad_nombre"),
	// rs.getInt("unidad_ejecutora"),
	// rs.getString("unidad_ejecutora_nombre"),
	// rs.getInt("programa"),
	// rs.getString("programa_nombre"),
	// rs.getInt("subprograma"),
	// rs.getString("subprograma_nombre"),
	// rs.getInt("proyecto"),
	// rs.getString("proyecto_nombre"),
	// rs.getInt("actividad"),
	// rs.getInt("obra"),
	// rs.getString("actividad_obra_nombre"),
	// rs.getInt("renglon"),
	// rs.getString("renglon_nombre"),
	// rs.getDouble("asignado"),
	// rs.getDouble("vigente"),
	// rs.getDouble("ejecutado")
	// );

	public static List<CDonacion> getDonaciones() {
		List<CDonacion> donaciones = new ArrayList<CDonacion>();

		Connection conn = CDatabase.connect();
		try{
			if(conn!=null && !conn.isClosed()){
				PreparedStatement pstm = conn.prepareStatement(getQuery(TIPO.DONACIONES));
				ResultSet rs = pstm.executeQuery();

				while (rs.next()) {
					CDonacion donacion = new CDonacion(null, null, null, null, null, rs.getInt("correlativo"),
							rs.getString("prestamo_nombre"), rs.getString("prestamo_sigla"), rs.getInt("entidad"),
							rs.getString("entidad_nombre"), rs.getInt("unidad_ejecutora"),
							rs.getString("unidad_ejecutora_nombre"), null, null, null, null, null, null, null, null,
							null, null, null, rs.getDouble("asignado"), rs.getDouble("vigente"),
							rs.getDouble("ejecutado"));

					donaciones.add(donacion);
				}

				rs.close();
				pstm.close();

			} 
		}
		catch (Exception e) {
			CLogger.write("1", CDonacionDAO.class, e);
		} finally {
			CDatabase.close(conn);
		}
		return donaciones;
	}

	public static List<CDonacion> getDonacionesByEntidad() {
		List<CDonacion> donaciones = new ArrayList<CDonacion>();

		Connection conn = CDatabase.connect();
		try{
			if(conn!=null && !conn.isClosed()){
				PreparedStatement pstm = conn.prepareStatement(getQuery(TIPO.ENTIDADES));
				ResultSet rs = pstm.executeQuery();

				while (rs.next()) {
					CDonacion donacion = new CDonacion(null, null, null, null, null, null, null, null,
							rs.getInt("entidad"), rs.getString("entidad_nombre"), null, null, null, null, null, null,
							null, null, null, null, null, null, null, rs.getDouble("asignado"), rs.getDouble("vigente"),
							rs.getDouble("ejecutado"));

					donaciones.add(donacion);
				}

				rs.close();
				pstm.close();

			}
		}
		 catch (Exception e) {
				CLogger.write("2", CDonacionDAO.class, e);
			} finally {
				CDatabase.close(conn);
			}
		return donaciones;
	}

	public static List<CDonacion> getDonacionesByOrganismo() {
		List<CDonacion> donaciones = new ArrayList<CDonacion>();

		Connection conn = CDatabase.connect();
		try{
			if(conn!=null && !conn.isClosed()){
				PreparedStatement pstm = conn.prepareStatement(getQuery(TIPO.ORGANISMOS));
				ResultSet rs = pstm.executeQuery();

				while (rs.next()) {
					CDonacion donacion = new CDonacion(null, null, null, rs.getInt("organismo"),
							rs.getString("organismo_nombre"), null, null, null, null, null, null, null, null, null,
							null, null, null, null, null, null, null, null, null, null, null,
							rs.getDouble("ejecutado"));

					donaciones.add(donacion);
				}

				rs.close();
				pstm.close();

			} 
		}
		catch (Exception e) {
			CLogger.write("3", CDonacionDAO.class, e);
		} finally {
			CDatabase.close(conn);
		}
		return donaciones;
	}

}
