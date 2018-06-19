package dao.transparencia;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.joda.time.DateTime;

import db.utilities.CDatabase;
import pojo.transparencia.CCur;
import shiro.utilities.CShiro;
import utilities.CLogger;

public class CCurDAO {
	public static Integer numCurs(int subprograma){
		Integer ret=0;
		if(CDatabase.connectEstadosExcepcion()){
			try{
				PreparedStatement pstm =  CDatabase.getConnection_estados_excepcion().prepareStatement("select count(*) from seg_cur where programa=94 and subprograma=?");
				pstm.setInt(1, subprograma);
				ResultSet rs = pstm.executeQuery();
				if (rs.next())
					ret=rs.getInt(1);
			}
			catch(Exception e){
				CLogger.write("1", CCurDAO.class, e);
			}
			finally{
				CDatabase.close_estados_excepcion();
			}
		}
		return ret;		
	}
	
	public static ArrayList<CCur> getCurs(int subprograma){
		ArrayList<CCur> ret=new ArrayList<CCur>();
		if(CDatabase.connectEstadosExcepcion()){
			try{
				PreparedStatement pstm =  CDatabase.getConnection_estados_excepcion().prepareStatement("SELECT * FROM seg_cur WHERE programa=? and subprograma=?");
				pstm.setInt(1, 94);
				pstm.setInt(2, subprograma);
				ResultSet rs=pstm.executeQuery();
				CCur cur;
				while (rs.next()){
					cur = new CCur(rs.getInt("programa"),rs.getInt("subprograma"), rs.getInt("ejercicio"), rs.getInt("entidad"), 
							rs.getInt("unidad_ejecutora"), rs.getInt("cur"));
					ret.add(cur);				
				}
			}
			catch(Exception e){
				CLogger.write("2", CCurDAO.class, e);
			}
			finally{
				CDatabase.close_estados_excepcion();
			}
		}
		return ret;		
	}
	
	public static boolean crearCur(CCur cur) {
		boolean ret = false;
		if (CDatabase.connectEstadosExcepcion()) {
			try {
				PreparedStatement pstm = CDatabase.getConnection_estados_excepcion()
						.prepareStatement("INSERT INTO seg_cur (programa,subprograma,ejercicio, entidad, unidad_ejecutora, cur, usuario_creacion,fecha_creacion)"
								+ "values (?,?,?,?,?,?,?,?)");

				pstm.setInt(1, cur.getPrograma());
				pstm.setInt(2, cur.getSubprograma());
				pstm.setInt(3, cur.getEjercicio());
				pstm.setInt(4, cur.getEntidad());
				pstm.setInt(5, cur.getUnidad_ejecutora());
				pstm.setInt(6, (int)cur.getCur());
				pstm.setString(7, CShiro.getIdUser());
				pstm.setTimestamp(8, new Timestamp(DateTime.now().getMillis()));

				if (pstm.executeUpdate() > 0)
					ret = true;
			} catch (Exception e) {
				CLogger.write("3", CCurDAO.class, e);
			} finally {
				CDatabase.close_estados_excepcion();
			}
		}
		return ret;
	}

	

	public static boolean deleteCur(int programa, int subprograma, int ejercicio, int entidad, int unidad_ejecutora, int cur) {
		boolean ret = false;
		if (CDatabase.connectEstadosExcepcion()) {
			try {
				PreparedStatement pstm = CDatabase.getConnection_estados_excepcion().prepareStatement("DELETE FROM seg_cur " + "WHERE "+
						 "programa = "+programa+" and subprograma= "+subprograma+" and ejercicio = "+ejercicio+" and entidad= " +entidad +
						 " and unidad_ejecutora = "+ unidad_ejecutora+ " and cur="+cur);
				if (pstm.executeUpdate() > 0)
					ret = true;
			} catch (Exception e) {
				CLogger.write("4", CCurDAO.class, e);
			} finally {
				CDatabase.close_estados_excepcion();
			}
		}
		return ret;
	}


}
