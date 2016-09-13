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

				List<String> campos = new ArrayList<String>();
				for (int i = 1; i <= pstm.getMetaData().getColumnCount(); i++) {
					campos.add(pstm.getMetaData().getColumnName(i));
				}

				while (rs.next()) {
					CPrestamo prestamo = new CPrestamo();

					
					// Posiblemente se camabia la estructura para que sea generica 
					// donde el metodo reciba la clase pojo y el prepareStatement
					for (String campo : campos) {
						if (campo.equalsIgnoreCase("correlativo")) {
							prestamo.setCorrelativo(rs.getInt("correlativo"));
							continue;
						}
						if (campo.equalsIgnoreCase("prestamo_nombre")) {
							prestamo.setPrestamo_nombre(rs.getString("prestamo_nombre"));
							continue;
						}
						if (campo.equalsIgnoreCase("entidad")) {
							prestamo.setEntidad(rs.getInt("entidad"));
							continue;
						}
						if (campo.equalsIgnoreCase("entidad_nombre")) {
							prestamo.setEntidad_nombre(rs.getString("entidad_nombre"));
							continue;
						}
						if (campo.equalsIgnoreCase("unidad_ejecutora")) {
							prestamo.setUnidad_ejecutora(rs.getInt("unidad_ejecutora"));
							continue;
						}
						if (campo.equalsIgnoreCase("unidad_ejecutora_nombre")) {
							prestamo.setUnidad_ejecutora_nombre(rs.getString("unidad_ejecutora_nombre"));
							continue;
						}
						if (campo.equalsIgnoreCase("actividad")) {
							prestamo.setActividad(rs.getInt("actividad"));
							continue;
						}
						if (campo.equalsIgnoreCase("actividad_obra_nombre")) {
							prestamo.setActividad_obra_nombre(rs.getString("actividad_obra_nombre"));
							continue;
						}
						if (campo.equalsIgnoreCase("ejercicio")) {
							prestamo.setEjercicio(rs.getInt("ejercicio"));
							continue;
						}
						if (campo.equalsIgnoreCase("fuente")) {
							prestamo.setFuente(rs.getInt("fuente"));
							continue;
						}
						if (campo.equalsIgnoreCase("fuente_nombre")) {
							prestamo.setFuente_nombre(rs.getString("fuente_nombre"));
							continue;
						}
						if (campo.equalsIgnoreCase("obra")) {
							prestamo.setObra(rs.getInt("obra"));
							continue;
						}
						if (campo.equalsIgnoreCase("organismo")) {
							prestamo.setOrganismo(rs.getInt("organismo"));
							continue;
						}
						if (campo.equalsIgnoreCase("organismo_nombre")) {
							prestamo.setOrganismo_nombre(rs.getString("organismo_nombre"));
							continue;
						}
						if (campo.equalsIgnoreCase("programa")) {
							prestamo.setPrograma(rs.getInt("programa"));
							continue;
						}
						if (campo.equalsIgnoreCase("programa_nombre")) {
							prestamo.setPrograma_nombre(rs.getString("programa_nombre"));
							continue;
						}
						if (campo.equalsIgnoreCase("proyecto")) {
							prestamo.setProyecto(rs.getInt("proyecto"));
							continue;
						}
						if (campo.equalsIgnoreCase("proyecto_nombre")) {
							prestamo.setProyecto_nombre(rs.getString("proyecto_nombre"));
							continue;
						}
						if (campo.equalsIgnoreCase("renglon")) {
							prestamo.setRenglon(rs.getInt("renglon"));
							continue;
						}
						if (campo.equalsIgnoreCase("renglon_nombre")) {
							prestamo.setRenglon_nombre(rs.getString("renglon_nombre"));
							continue;
						}
						if (campo.equalsIgnoreCase("subprograma")) {
							prestamo.setSubprograma(rs.getInt("subprograma"));
							continue;
						}
						if (campo.equalsIgnoreCase("subprograma_nombre")) {
							prestamo.setSubprograma_nombre("subprograma_nombre");
							continue;
						}
						if (campo.equalsIgnoreCase("asignado")) {
							prestamo.setAsignado(rs.getDouble("asignado"));
							continue;
						}
						if (campo.equalsIgnoreCase("ejecutado")) {
							prestamo.setEjecutado(rs.getDouble("ejecutado"));
							continue;
						}
						if (campo.equalsIgnoreCase("vigente")) {
							prestamo.setVigente(rs.getDouble("vigente"));
							continue;
						}

					}

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

}
