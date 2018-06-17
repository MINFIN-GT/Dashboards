package dao.transparencia;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.joda.time.DateTime;

import db.utilities.CDatabase;
import pojo.transparencia.CCur;
import utilities.CLogger;

public class CCurDAO {
	public static Integer numCurs(int subprograma){
		Integer ret=0;
		if(CDatabase.connect()){
			try{
				PreparedStatement pstm =  CDatabase.getConnection().prepareStatement("select count(*) from seg_cur where programa=94 and subprograma=?");
				pstm.setInt(1, subprograma);
				ResultSet rs = pstm.executeQuery();
				if (rs.next())
					ret=rs.getInt(1);
			}
			catch(Exception e){
				CLogger.write("1", CCurDAO.class, e);
			}
			finally{
				CDatabase.close();
			}
		}
		return ret;		
	}
	
	public static ArrayList<CCur> getCurs(int subprograma){
		ArrayList<CCur> ret=new ArrayList<CCur>();
		if(CDatabase.connect()){
			try{
				PreparedStatement pstm =  CDatabase.getConnection().prepareStatement("SELECT * FROM seg_cur WHERE programa=? and subprograma=?");
				pstm.setInt(1, 94);
				pstm.setInt(2, subprograma);
				ResultSet rs=pstm.executeQuery();
				PreparedStatement pstm2=null;
				ResultSet rs2;
				CCur cur;
				while (rs.next()){
					if (rs.getInt("cur")>0){
						pstm2 = CDatabase.getConnection().prepareStatement("select * from mv_evento_gc where nog_concurso=?");
						pstm2.setString(1,rs.getString("NOG"));
						rs2 = pstm2.executeQuery();
						if (rs2.next()){
							//cur = new CCur();
							//ret.add(cur);
						}		
					}					
				}
			}
			catch(Exception e){
				CLogger.write("2", CCurDAO.class, e);
			}
			finally{
				CDatabase.close();
				//CDatabase.close_oracle();
			}
		}
		return ret;		
	}
	
	public static boolean crearCur(CCur cur) {
		boolean ret = false;
		if (CDatabase.connect()) {
			try {
				PreparedStatement pstm = CDatabase.getConnection()
						.prepareStatement("INSERT INTO seg_cur (programa,subprograma,ejercicio, entidad, unidad_ejecutora, usuario_creacion,fecha_creacion)"
								+ "values (?,?,?,?,?,?)");

				pstm.setInt(1, cur.getPrograma());
				pstm.setInt(2, cur.getSubprograma());
				
				/*pstm.setString(5, compra.getUsuario());
				pstm.setTimestamp(6, new Timestamp(DateTime.now().getMillis()));
*/
				if (pstm.executeUpdate() > 0)
					ret = true;
			} catch (Exception e) {
				CLogger.write("3", CCurDAO.class, e);
			} finally {
				CDatabase.close();
			}
		}
		return ret;
	}

	

	public static boolean deleteCompra(String id, String tipo, int programa, int subprograma) {
		boolean ret = false;
		if (CDatabase.connect()) {
			try {
				PreparedStatement pstm = CDatabase.getConnection().prepareStatement("DELETE FROM seg_compra " + "WHERE "+
						(tipo.compareTo("NPG")==0? "NPG = '"+id+"'": "NOG = "+id) + " and programa = "+programa+" and subprograma= "+subprograma);
				if (pstm.executeUpdate() > 0)
					ret = true;
			} catch (Exception e) {
				CLogger.write("4", CCurDAO.class, e);
			} finally {
				CDatabase.close();
			}
		}
		return ret;
	}

	public static boolean getCompra(int nog) {
		boolean ret = false;
		try{
			if(CDatabase.connect()){
				PreparedStatement pstm = CDatabase.getConnection().prepareStatement("select count(*) total from mv_evento_gc where nog_concurso=?");
				pstm.setInt(1, nog);
				ResultSet rs=pstm.executeQuery();
				if(rs.next() && rs.getInt("total")>0){
					ret = true;
				}
			}
		}
		catch(Exception e){
			CLogger.write("4", CCurDAO.class, e);
		}
		finally{
			CDatabase.close();
		}
		return ret;
	}


}
