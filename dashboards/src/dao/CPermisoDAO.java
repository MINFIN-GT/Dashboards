package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import db.utilities.CDatabase;
import pojo.CPermiso;
import utilities.CLogger;

public class CPermisoDAO {
	
	public static CPermiso getPermiso(Integer id){
		CPermiso ret=null;
		Connection conn = CDatabase.connect();
		try{
			if(conn!=null && !conn.isClosed()){
				PreparedStatement pstm = conn.prepareStatement("select * from permiso where id = ?");
				pstm.setInt(1, id);
				ResultSet rs = pstm.executeQuery();
				if(rs.next()){
					ret = new CPermiso(id, rs.getString("nombre"), null);
				}
				rs.close();
				pstm.close();
			}
		}
		catch(Exception e){
			CLogger.write("1", CPermisoDAO.class, e);
		}
		finally {
			CDatabase.close(conn);
		}
		return ret;	
	}
	
	public static ArrayList<CPermiso> getPermisosByUser(String username){
		ArrayList<CPermiso> ret= new ArrayList<CPermiso>();
		Connection conn = CDatabase.connect();
		try{
			if(conn!=null && !conn.isClosed()){
				PreparedStatement pstm = conn.prepareStatement("select * from user_permiso where username=?");
				ResultSet rs = pstm.executeQuery();
				while(rs.next()){
					ret.add(new CPermiso(rs.getInt("id"), rs.getString("nombre"), null));
				}
				rs.close();
				pstm.close();
			}
		}
		catch(Exception e){
			CLogger.write("2", CPermisoDAO.class, e);
		}
		finally {
			CDatabase.close(conn);
		}
		return ret.size()>0 ? ret : null;
	}
	
	public static ArrayList<CPermiso> getPermisos(){
		ArrayList<CPermiso> ret=new ArrayList<CPermiso>();
		Connection conn = CDatabase.connect();
		try{
			if(conn!=null && !conn.isClosed()){
				PreparedStatement pstm = conn.prepareStatement("select * from permiso");
				ResultSet rs = pstm.executeQuery();
				while(rs.next()){
					ret.add(new CPermiso(rs.getInt("id"), rs.getString("nombre"), null));
				}
				rs.close();
				pstm.close();
			}
		}
		catch(Exception e){
			CLogger.write("3", CPermisoDAO.class, e);
		}
		finally {
			CDatabase.close(conn);
		}
		return ret.size()>0 ? ret : null;
	}
	
}
