package dao.transparencia;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.joda.time.DateTime;

import db.utilities.CDatabase;
import pojo.transparencia.CDonacion;
import utilities.CLogger;

public class CDonacionDAO {
	public static Integer numDonaciones(int subprograma){
		Integer ret=0;
		if(CDatabase.connect()){
			try{
				PreparedStatement pstm =  CDatabase.getConnection().prepareStatement("select count(*) from seg_donacion where programa=94 and subprograma=?");
				pstm.setInt(1, subprograma);
				ResultSet rs = pstm.executeQuery();
				if (rs.next())
					ret=rs.getInt(1);
			}
			catch(Exception e){
				CLogger.write("1", CDonacionDAO.class, e);
			}
			finally{
				CDatabase.close();
			}
		}
		return ret;		
	}
	
	public static ArrayList<CDonacion> getDonaciones(int subprograma){
		ArrayList<CDonacion> ret=new ArrayList<CDonacion>();
		if(CDatabase.connect()){
			try{
				PreparedStatement pstm =  CDatabase.getConnection().prepareStatement("SELECT * FROM seg_donacion WHERE programa=? and subprograma=? order by id");
				pstm.setInt(1, 94);
				pstm.setInt(2, subprograma);
				ResultSet rs=pstm.executeQuery();
				CDonacion donacion;
				while (rs.next()){
					donacion = new CDonacion(rs.getInt("id"),rs.getInt("programa"), subprograma, rs.getString("donante"), 
							rs.getString("procedencia"), rs.getString("metodo_acreditamiento"), 
							rs.getTimestamp("fecha_ingreso"), rs.getDouble("monto_d"), 
							rs.getDouble("monto_q"), rs.getString("estado"), rs.getString("destino"), 
							rs.getString("usuario_creacion"), rs.getTimestamp("fecha_creacion"), 
							rs.getString("usuario_modificacion"), rs.getTimestamp("fecha_modificacion"));
					ret.add(donacion);					
				}
			}
			catch(Exception e){
				CLogger.write("2", CDonacionDAO.class, e);
			}
			finally{
				CDatabase.close();
			}
		}
		return ret;		
	}
	
	public static boolean crearDonacion(CDonacion donacion) {
		boolean ret = false;
		if (CDatabase.connect()) {
			try {
				PreparedStatement pstm = CDatabase.getConnection()
						.prepareStatement("INSERT INTO seg_donacion (programa,subprograma,donante,procedencia,metodo_acreditamiento,"
								+ "fecha_ingreso,monto_d,monto_q,estado,destino,usuario_creacion,fecha_creacion)"
								+ "values (?,?,?,?,?,?,?,?,?,?,"
								+ "?,?)");

				pstm.setInt(1, donacion.getPrograma());
				pstm.setInt(2, donacion.getSubprograma());
				pstm.setString(3, donacion.getDonante());
				pstm.setString(4, donacion.getProcedencia());
				pstm.setString(5, donacion.getMetodo_acreditamiento());
				pstm.setTimestamp(6, donacion.getFecha_ingreso());
				pstm.setDouble(7, donacion.getMonto_d());
				pstm.setDouble(8, donacion.getMonto_q());
				pstm.setString(9, donacion.getEstado());
				pstm.setString(10, donacion.getDestino());
				pstm.setString(11, donacion.getUsuario_creacion());
				pstm.setTimestamp(12, new Timestamp(DateTime.now().getMillis()));

				if (pstm.executeUpdate() > 0)
					ret = true;
			} catch (Exception e) {
				CLogger.write("3", CDonacion.class, e);
			} finally {
				CDatabase.close();
			}
		}
		return ret;
	}

	

	public static boolean deleteDonacion(int id) {
		boolean ret = false;
		if (CDatabase.connect()) {
			try {
				PreparedStatement pstm = CDatabase.getConnection().prepareStatement("DELETE FROM seg_donacion WHERE id = ?");
				pstm.setInt(1, id);
				if (pstm.executeUpdate() > 0)
					ret = true;
			} catch (Exception e) {
				CLogger.write("4", CDonacion.class, e);
			} finally {
				CDatabase.close();
			}
		}
		return ret;
	}

}
