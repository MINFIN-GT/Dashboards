package dao.varios;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import dao.DataAccessObject;
import db.utilities.CDatabase;
import pojo.varios.CPrestamo;
import utilities.CLogger;

public class CPrestamoDAO extends DataAccessObject {
	public static enum TIPO {
		PRESTAMOS, ENTIDADES, ORGANISMOS
	}

	private static String getQueryPrestamos() {
		StringBuilder sb = new StringBuilder();

		sb.append("select ");
		sb.append("    correlativo");
		sb.append("    ,prestamo_nombre");
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

	public static List<CPrestamo> getAllPrestamos(TIPO tipo) {
		List<CPrestamo> prestamos = new ArrayList<CPrestamo>();

		if (CDatabase.connect()) {
			Connection conn = CDatabase.getConnection();
			try {
				PreparedStatement pstm = conn.prepareStatement(getQuery(tipo));
				ResultSet rs = pstm.executeQuery();

				prestamos = getListPojo(CPrestamo.class, rs);

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

}
