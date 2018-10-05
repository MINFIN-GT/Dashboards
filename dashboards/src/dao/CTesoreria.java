package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import db.utilities.CDatabase;
import pojo.CTesoreriaCuenta;
import utilities.CLogger;

public class CTesoreria {
	
	public static ArrayList<CTesoreriaCuenta> getSaldoInicialCuentas(int ejercicio) {
		
		final ArrayList<CTesoreriaCuenta> cuentas=new ArrayList<CTesoreriaCuenta>();
		Connection conn = CDatabase.connect();
		try{
			if(conn!=null && !conn.isClosed()){
				PreparedStatement pstm1 =  conn.prepareStatement("select ejercicio, cuenta_monetaria, monto_transaccion from te_estado_cuentas where ejercicio=? order by cuenta_monetaria ");
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
