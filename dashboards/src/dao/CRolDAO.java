package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import db.utilities.CDatabase;
import pojo.CRol;
import utilities.CLogger;

public class CRolDAO {
	
	public static CRol getRole(Integer id){
		CRol ret=null;
		Connection conn = CDatabase.connect();
		try{
			if(conn!=null && !conn.isClosed()){
				PreparedStatement pstm = conn.prepareStatement("select * from rol where id = ?");
				pstm.setInt(1, id);
				ResultSet rs = pstm.executeQuery();
				if(rs.next()){
					ret = new CRol(id, rs.getString("nombre"), null);
				}
				rs.close();
				pstm.close();
				CDatabase.close();
			}
		}
		catch(Exception e){
			CLogger.write("1", CRolDAO.class, e);
		}
		finally {
			CDatabase.close(conn);
		}
		return ret;	
	}
	
	public static ArrayList<Integer> getPermisos(Integer rol_id){
		ArrayList<Integer> ret=new ArrayList<Integer>();
		Connection conn = CDatabase.connect();
		try{
			if(conn!=null && !conn.isClosed()){
				PreparedStatement pstm = conn.prepareStatement("select * from rol_permiso where username=?");
				ResultSet rs = pstm.executeQuery();
				while(rs.next()){
					ret.add(rs.getInt("id"));
				}
				rs.close();
				pstm.close();
				CDatabase.close();
			}
		}
		catch(Exception e){
			CLogger.write("2", CRol.class, e);
		}
		finally {
			CDatabase.close(conn);
		}
		return ret.size()>0 ? ret : null;
	}
	
	public static ArrayList<CRol> getRoles(){
		ArrayList<CRol> ret=new ArrayList<CRol>();
		Connection conn = CDatabase.connect();
		try{
			if(conn!=null && !conn.isClosed()){
				PreparedStatement pstm = conn.prepareStatement("select * from rol");
				ResultSet rs = pstm.executeQuery();
				if(rs.next()){
					ret.add(new CRol(rs.getInt("id"), rs.getString("nombre"), null));
				}
				rs.close();
				pstm.close();
				CDatabase.close();
			}
		}
		catch(Exception e){
			CLogger.write("3", CRolDAO.class, e);
		}
		finally {
			CDatabase.close(conn);
		}
		return ret.size()>0 ? ret : null;
	}

	public static ArrayList<Integer> getUserRoles(String username) {
		ArrayList<Integer> ret= new ArrayList<Integer>();
		Connection conn = CDatabase.connect();
		try{
			if(conn!=null && !conn.isClosed()){
				PreparedStatement pstm = conn.prepareStatement("select * from user_rol where username=?");
				ResultSet rs = pstm.executeQuery();
				while(rs.next()){
					ret.add(rs.getInt("rol_id"));
				}
				rs.close();
				pstm.close();
				CDatabase.close();
			}
		}
		catch(Exception e){
			CLogger.write("4", CRolDAO.class, e);
		}
		finally {
			CDatabase.close(conn);
		}
		return ret.size()>0 ? ret : null;
	}
	
}
