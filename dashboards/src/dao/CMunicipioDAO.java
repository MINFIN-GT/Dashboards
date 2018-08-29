package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import db.utilities.CDatabase;
import pojo.CMunicipio;
import utilities.CLogger;

public class CMunicipioDAO {
	CMunicipio municipio;
	
	public static CMunicipio getTown(Integer id){
		CMunicipio ret=null;
		Connection conn = CDatabase.connect();
		try{
			if(conn!=null && !conn.isClosed()){
				PreparedStatement pstm = conn.prepareStatement("select * from municipio where id = ?");
				pstm.setInt(1, id);
				ResultSet results = pstm.executeQuery();
				if(results.next()){
					ret = new CMunicipio(id,results.getString("nombre_mayuscula"), results.getString("nombre"), results.getString("latitud"), results.getString("longitud"),
							results.getInt("codigo_departamento"));
				}
				results.close();
				pstm.close();
				CDatabase.close();
			}
		}
		catch(Exception e){
			CLogger.write("1", CMunicipioDAO.class, e);
		}
		finally {
			CDatabase.close(conn);
		}
		return ret;	
	}
	
	public static ArrayList<CMunicipio> getMunicipios(){
		final ArrayList<CMunicipio> towns=new ArrayList<CMunicipio>();
		Connection conn = CDatabase.connect();
		try{
			if(conn!=null && !conn.isClosed()){
				Statement statement =  conn.createStatement();
				ResultSet results = statement.executeQuery("select * from municipio order by nombre");
				while (results.next()){
					CMunicipio town = new CMunicipio(results.getInt("codigo"),results.getString("nombre_mayuscula"), results.getString("nombre"), results.getString("latitud"), results.getString("longitud"),
							results.getInt("codigo_departamento"));
					towns.add(town);
				}
				results.close();
			}
		}
		catch(Exception e){
			CLogger.write("1", CMunicipioDAO.class, e);
		}
		finally{
			CDatabase.close(conn);
		}
		if(towns.size()==0)
			return null;
		
		return towns;
	}
}
