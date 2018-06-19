package db.utilities;


import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;
import utilities.CLogger;
import utilities.CProperties;


public class CDatabase {
	private static Connection connection;
	private static Connection connection_des;
	private static Connection connection_estados_excepcion;
	private static String host;
	private static String host_intra;
	private static Integer port;
	private static String user;
	private static String password;
	private static String schema;
	private static String schema_des;
	private static String schema_estados_excepcion;

	private static Connection connection_oracle;
	private static String host_oracle;
	private static Integer port_oracle;
	private static String user_oracle;
	private static String password_oracle;
	private static String schema_oracle;
	
	static {
		host = CProperties.getmemsql_host();
		host_intra = CProperties.getmemsql_host_intra();
		port = CProperties.getmemsql_port();
		user = CProperties.getmemsql_user();
		password = CProperties.getmemsql_password();
		schema = CProperties.getmemsql_schema();
		schema_des = CProperties.getmemsql_schema_des();
		schema_estados_excepcion = CProperties.getmemsql_schema_estados_excepcion();
		
		host_oracle = CProperties.getOracle_host();
		port_oracle = CProperties.getOracle_port();
		user_oracle = CProperties.getOracle_user();
		password_oracle = CProperties.getOracle_password();
		schema_oracle = CProperties.getOracle_schema();
		
		try{
			Class.forName("com.mysql.jdbc.Driver");
			DriverManager.setLoginTimeout(10);
			connection = DriverManager.getConnection(String.join("", "jdbc:mysql://",String.valueOf(host),":", String.valueOf(port),"/",schema), user, password);
		}
		catch(Exception e){
			try {
				connection = DriverManager.getConnection(String.join("", "jdbc:mysql://",String.valueOf(host_intra),":", String.valueOf(port),"/",schema), user, password);
				if(!connection.isClosed())
					host = host_intra;
			} 
			catch (Exception e1) {
			
			}
			
		}
	}
	
	public static boolean connect(){
		try{
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(String.join("", "jdbc:mysql://",String.valueOf(host),":", String.valueOf(port),"/",schema,"?zeroDateTimeBehavior=convertToNull"), user, password);
			return !connection.isClosed();
		}
		catch(Exception e){
			CLogger.write("1", CDatabase.class, e);
		}
		return false;
	}
	
	public static boolean connectDes(){
		try{
			Class.forName("com.mysql.jdbc.Driver");
			connection_des = DriverManager.getConnection(String.join("", "jdbc:mysql://",String.valueOf(host),":", String.valueOf(port),"/",schema_des,"?zeroDateTimeBehavior=convertToNull"), user, password);
			return !connection_des.isClosed();
		}
		catch(Exception e){
			CLogger.write("2", CDatabase.class, e);
		}
		return false;
	}
	
	public static boolean connectOracle(){
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection_oracle = DriverManager.getConnection(String.join("", "jdbc:oracle:thin:@//",String.valueOf(host_oracle),":", String.valueOf(port_oracle),"/",schema_oracle,"?zeroDateTimeBehavior=convertToNull"), user_oracle, password_oracle);
			return !connection_oracle.isClosed();
		}
		catch(Exception e){
			CLogger.write("3", CDatabase.class, e);
		}
		return false;
	}
	
	public static Connection getConnection(){
		return connection;
	}
	
	public static Connection getConnection_des(){
		return connection_des;
	}
	
	public static Connection getConnection_estados_excepcion(){
		return connection_estados_excepcion;
	}
	
	public static Connection getConnection_Oracle(){
		return connection_oracle;
	}
	
	public static void close(){
		try {
			connection.close();
		} catch (SQLException e) {
			CLogger.write("4", CDatabase.class, e);
		}
	}
	
	public static void close_des(){
		try {
			connection_des.close();
		} catch (SQLException e) {
			CLogger.write("5", CDatabase.class, e);
		}
	}
	
	public static void close_oracle(){
		try {
			connection_oracle.close();
		} catch (SQLException e) {
			CLogger.write("6", CDatabase.class, e);
		}
	}
	
	public static void close(Connection conn){
		try {
			conn.close();
		} catch (SQLException e) {
			CLogger.write("7", CDatabase.class, e);
		}
	}
	
	public static boolean connectEstadosExcepcion(){
		try{
			Class.forName("com.mysql.jdbc.Driver");
			connection_estados_excepcion = DriverManager.getConnection(String.join("", "jdbc:mysql://",String.valueOf(host),":", String.valueOf(port),"/",schema_estados_excepcion,"?zeroDateTimeBehavior=convertToNull"), user, password);
			return !connection_estados_excepcion.isClosed();
		}
		catch(Exception e){
			CLogger.write("8", CDatabase.class, e);
		}
		return false;
	}
	
	public static void close_estados_excepcion(){
		try {
			connection_estados_excepcion.close();
		} catch (SQLException e) {
			CLogger.write("9", CDatabase.class, e);
		}
	}
}
