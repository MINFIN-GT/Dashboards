package db.utilities;


import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;
import utilities.CLogger;
import utilities.CProperties;


public class CDatabaseOracle {
	
	public static Connection connect(){
		Connection connection=null;
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection("jdbc:oracle:thin:@//"+CProperties.getOracle_sicoinp_host()+
					":"+CProperties.getOracle_sicoinp_port()+"/DBFORMU", CProperties.getOracle_sicoinp_user(),CProperties.getOracle_sicoinp_password());
			return !connection.isClosed() ?  connection : null;
		}
		catch(Exception e){
			if(connection==null) {
				try {
					connection = DriverManager.getConnection("jdbc:oracle:thin:@//172.18.28.51"+
							":"+CProperties.getOracle_sicoinp_port()+"/DBFORMU", CProperties.getOracle_sicoinp_user(),CProperties.getOracle_sicoinp_password());
					return !connection.isClosed() ?  connection : null;
				} catch (SQLException e1) {
					CLogger.write("1", CDatabaseOracle.class, e);
				}
			}
		}
		return null;
	}
	
	public static void close(Connection connection){
		try {
			if(connection!=null)
				connection.close();
		} catch (SQLException e) {
			CLogger.write("2", CDatabaseOracle.class, e);
		}
	}
}
