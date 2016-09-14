package dao.varios;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import db.utilities.CDatabase;
import pojo.varios.CPrestamo;
import utilities.CLogger;

public class CPrestamoDAO {
	public static enum TIPO {
		PRESTAMOS, ENTIDADES, ORGANISMOS
	}

	private static String getQueryPrestamos() {
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

	private static String getQueryPrestamosByEntidad() {
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

	private static String getQueryPrestamosByOrganismo() {
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
			return getQueryPrestamosByOrganismo();
		case ENTIDADES:
			return getQueryPrestamosByEntidad();
		default: // case PRESTAMOS:
			return getQueryPrestamos();
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

	public static List<CPrestamo> getPrestamos() {
		List<CPrestamo> prestamos = new ArrayList<CPrestamo>();

		if (CDatabase.connect()) {
			Connection conn = CDatabase.getConnection();
			try {
				PreparedStatement pstm = conn.prepareStatement(getQuery(TIPO.PRESTAMOS));
				ResultSet rs = pstm.executeQuery();

				while (rs.next()) {
					CPrestamo prestamo = new CPrestamo(null, null, null, null, null, rs.getInt("correlativo"),
							rs.getString("prestamo_nombre"), rs.getString("prestamo_sigla"), rs.getInt("entidad"),
							rs.getString("entidad_nombre"), rs.getInt("unidad_ejecutora"),
							rs.getString("unidad_ejecutora_nombre"), null, null, null, null, null, null, null, null,
							null, null, null, rs.getDouble("asignado"), rs.getDouble("vigente"),
							rs.getDouble("ejecutado"));

					prestamos.add(prestamo);
				}

				rs.close();
				pstm.close();

			} catch (Exception e) {
				CLogger.write("1", CPrestamoDAO.class, e);
			} finally {
				CDatabase.close(conn);
			}
		}

		return prestamos;
	}

	public static List<CPrestamo> getPrestamosByEntidad() {
		List<CPrestamo> prestamos = new ArrayList<CPrestamo>();

		if (CDatabase.connect()) {
			Connection conn = CDatabase.getConnection();
			try {
				PreparedStatement pstm = conn.prepareStatement(getQuery(TIPO.ENTIDADES));
				ResultSet rs = pstm.executeQuery();

				while (rs.next()) {
					CPrestamo prestamo = new CPrestamo(null, null, null, null, null, null, null, null,
							rs.getInt("entidad"), rs.getString("entidad_nombre"), null, null, null, null, null, null,
							null, null, null, null, null, null, null, rs.getDouble("asignado"), rs.getDouble("vigente"),
							rs.getDouble("ejecutado"));

					prestamos.add(prestamo);
				}

				rs.close();
				pstm.close();

			} catch (Exception e) {
				CLogger.write("2", CPrestamoDAO.class, e);
			} finally {
				CDatabase.close(conn);
			}
		}

		return prestamos;
	}

	public static List<CPrestamo> getPrestamosByOrganismo() {
		List<CPrestamo> prestamos = new ArrayList<CPrestamo>();

		if (CDatabase.connect()) {
			Connection conn = CDatabase.getConnection();
			try {
				PreparedStatement pstm = conn.prepareStatement(getQuery(TIPO.ORGANISMOS));
				ResultSet rs = pstm.executeQuery();

				while (rs.next()) {
					CPrestamo prestamo = new CPrestamo(null, null, null, rs.getInt("organismo"),
							rs.getString("organismo_nombre"), null, null, null, null, null, null, null, null, null,
							null, null, null, null, null, null, null, null, null, null, null,
							rs.getDouble("ejecutado"));

					prestamos.add(prestamo);
				}
				rs.close();
				pstm.close();

			} catch (Exception e) {
				CLogger.write("3", CPrestamoDAO.class, e);
			} finally {
				CDatabase.close(conn);
			}
		}

		return prestamos;
	}

}
