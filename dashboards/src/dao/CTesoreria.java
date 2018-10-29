package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import db.utilities.CDatabase;
import pojo.CTesoreriaCuenta;
import utilities.CLogger;

public class CTesoreria {
	
	static String scuentas="'GT24BAGU01010000000001100015','GT38BAGU01010000000001123819','GT38BAGU01010000000001501634','GT47BAGU01010000000001127828','GT50BAGU01010000000001126416','GT65BAGU01010000000001130608','GT74BAGU01010000000001130640','GT85BAGU01010000000001111624','GT88BAGU01010000000001130582','GT89BAGU01010000000001100106','GT28BAGU01010000000001117985','GT02BAGU01010000000001130384','GT24BAGU01010000000001130376','GT67BAGU01010000000001130475','GT12BAGU01010000000001129816','GT13BAGU01010000000001130186','GT24BAGU01010000000001130764'";
	
	public static ArrayList<CTesoreriaCuenta> getSaldoInicialCuentas(int ejercicio) {
		final ArrayList<CTesoreriaCuenta> cuentas=new ArrayList<CTesoreriaCuenta>();
		Connection conn = CDatabase.connect();
		try{
			if(conn!=null && !conn.isClosed()){
				PreparedStatement pstm1 =  conn.prepareStatement("select ejercicio, cuenta_monetaria, monto_transaccion from te_estado_cuentas where ejercicio=? and cuenta_monetaria in ("+scuentas+") order by cuenta_monetaria ");
				pstm1.setInt(1, ejercicio);
				ResultSet results = pstm1.executeQuery();	
				while (results.next()){
					CTesoreriaCuenta cuenta = new CTesoreriaCuenta(ejercicio,results.getString("cuenta_monetaria"),results.getDouble("monto_transaccion"));
					cuentas.add(cuenta);
				}
				results.close();
				pstm1.close();
			}
		}
		catch(Exception e){
			CLogger.write("1", CTesoreriaCuenta.class, e);
		}
		finally{
			CDatabase.close(conn);
		}
		return cuentas.size()>0 ? cuentas : null;
	
	}
}
