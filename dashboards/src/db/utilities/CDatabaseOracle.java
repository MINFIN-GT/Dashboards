package db.utilities;


import java.sql.Connection;

import java.sql.SQLException;

import oracle.jdbc.pool.OracleDataSource;
import utilities.CLogger;
import utilities.CProperties;
import java.util.Properties;

public class CDatabaseOracle {
	
	private final  static String CACHE_NAME = "OCACHE";
    private  static OracleDataSource ods = null;
    private  static OracleDataSource ods_local = null;
    
    private static OracleDataSource ods_general=null;

        static {
            System.out.println("OracleDataSource Initialization");
            try {
                ods = new OracleDataSource();
                ods.setURL("jdbc:oracle:thin:@//"+CProperties.getOracle_sicoinp_host()+":"+CProperties.getOracle_sicoinp_port()+"/DBFORMU");
                ods.setUser(CProperties.getOracle_sicoinp_user());
                ods.setPassword(CProperties.getOracle_sicoinp_password());
                // caching parms
                ods.setConnectionCacheName(CACHE_NAME);
                Properties cacheProps = new Properties();
                cacheProps.setProperty("MinLimit", "1");
                cacheProps.setProperty("MaxLimit", "10");
                cacheProps.setProperty("InitialLimit", "1");
                cacheProps.setProperty("ConnectionWaitTimeout", "5");
                cacheProps.setProperty("ValidateConnection", "true");
                ods.setConnectionProperties(cacheProps);
                
                ods_local = new OracleDataSource();
                ods_local.setURL("jdbc:oracle:thin:@//172.18.28.51:"+CProperties.getOracle_sicoinp_port()+"/DBFORMU");
                ods_local.setUser(CProperties.getOracle_sicoinp_user());
                ods_local.setPassword(CProperties.getOracle_sicoinp_password());
                // caching parms
                ods_local.setConnectionCacheName(CACHE_NAME);
                ods.setConnectionProperties(cacheProps);
                
                try {
	                if(ods!=null) {
	                	if(ods.getConnection()!=null)
	                		ods_general = ods;
	                }
                }
                catch(Exception e1) {
                	try {
	                	if(ods_local!=null) {
	                		ods_general=ods_local;
	                	}
                	}
                	catch(Exception e2) {
                		CLogger.write("1", CDatabaseOracle.class, e2);
                	}
                }

            }
            catch (Exception e) {
            	CLogger.write("1", CDatabaseOracle.class, e);
            }
        }
	
	public static Connection connect(){
		try {
			if (ods_general != null) 
		         return ods_general.getConnection();
		}
		catch (SQLException e) {
			CLogger.write("2", CDatabaseOracle.class, e);
        }
	    return null;
	}
	
	public static void close(Connection connection){
		try {
			if (connection != null ) {
		          connection.close();
		      }
		} catch (SQLException e) {
			CLogger.write("3", CDatabaseOracle.class, e);
		}
	}
}
