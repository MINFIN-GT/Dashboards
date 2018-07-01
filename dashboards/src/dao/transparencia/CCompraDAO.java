package dao.transparencia;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.joda.time.DateTime;

import db.utilities.CDatabase;
import pojo.transparencia.CCompra;
import pojo.transparencia.CEntidadCompra;
import utilities.CLogger;

public class CCompraDAO {
	public static Integer numCompras(int subprograma){
		Integer ret=0;
		if(CDatabase.connectEstadosExcepcion()){
			try{
				PreparedStatement pstm =  CDatabase.getConnection_estados_excepcion().prepareStatement("select count(*) from seg_compra where programa=94 and subprograma=?");
				pstm.setInt(1, subprograma);
				ResultSet rs = pstm.executeQuery();
				if (rs.next())
					ret=rs.getInt(1);
			}
			catch(Exception e){
				CLogger.write("1", CCompraDAO.class, e);
			}
			finally{
				CDatabase.close_estados_excepcion();
			}
		}
		return ret;		
	}
	
	public static ArrayList<CCompra> getCompras(int subprograma){
		ArrayList<CCompra> ret=new ArrayList<CCompra>();
		if(CDatabase.connectEstadosExcepcion()){
			try{
				PreparedStatement pstm2=null;
				ResultSet rs2;
				CCompra compra;
				CDatabase.connect();
					/*if (rs.getString("NPG")!=null){
						pstm2 = CDatabase.getConnection_Oracle().prepareStatement("select * from GUATECOMPRAS.VW_GC_NPG_JUTIAPA2016 where NPG_CONCURSO=?");
						pstm2.setString(1,rs.getString("NPG"));
						rs2 = pstm2.executeQuery();
						if (rs2.next()){
							compra = new CCompra(rs2.getString("ENTIDAD_GC"), rs2.getString("UNIDAD_GC"),"NPG", rs2.getString("NPG_CONCURSO"), rs2.getTimestamp("FECHA_PUBLICACION_GC"), rs2.getString("DESCRIPCION"), rs2.getString("NOMBRE_MODALIDAD")+" - "+rs2.getString("NOMBRE_MODALIDAD_EJECUCION"),rs2.getString("NOMBRE_ESTATUS"), rs2.getString("NIT"), rs2.getString("NOMBRE"), rs2.getDouble("MONTO"));
							ret.add(compra);
						}					
					}else*/ 
						pstm2 = CDatabase.getConnection().prepareStatement("select * from mv_evento_gc where nog_concurso in (SELECT nog FROM estados_excepcion.seg_compra WHERE programa=? and subprograma=?) order by fecha_publicacion");
						pstm2.setInt(1, 94);
						pstm2.setInt(2, subprograma);
						rs2 = pstm2.executeQuery();
						while (rs2.next()){
							compra = new CCompra(rs2.getString("entidad_compradora_nombre"), rs2.getString("unidad_compradora_nombre"),"NOG", 
									rs2.getString("nog_concurso"), rs2.getTimestamp("fecha_publicacion"), rs2.getString("descripcion"), 
									rs2.getString("modalidad_nombre"),rs2.getString("estatus_concurso_nombre"),null, 
									null, rs2.getDouble("monto")
									);
							ret.add(compra);
						}		
			}
			catch(Exception e){
				CLogger.write("2", CCompraDAO.class, e);
			}
			finally{
				CDatabase.close_estados_excepcion();
				CDatabase.close();
			}
		}
		return ret;		
	}
	
public static boolean crearCompra(CCompra compra) {
		boolean ret = false;
		if (CDatabase.connectEstadosExcepcion()) {
			try {
				PreparedStatement pstm = CDatabase.getConnection_estados_excepcion()
						.prepareStatement("INSERT INTO seg_compra (nog,npg,programa,subprograma,usuario_creacion,fecha_creacion,es_manual)"
								+ "values (?,?,?,?,?,?,1)");

				if (compra.getTipo().compareTo("NOG")==0){
					pstm.setNull(2, java.sql.Types.VARCHAR);
					pstm.setInt(1, Integer.parseInt(compra.getId()));
				}else{
					pstm.setNull(1, java.sql.Types.INTEGER);
					pstm.setString(2, compra.getId());
				}
				pstm.setInt(3, compra.getPrograma());
				pstm.setInt(4, compra.getSubprograma());
				pstm.setString(5, compra.getUsuario());
				pstm.setTimestamp(6, new Timestamp(DateTime.now().getMillis()));

				if (pstm.executeUpdate() > 0)
					ret = true;
			} catch (Exception e) {
				CLogger.write("3", CCompra.class, e);
			} finally {
				CDatabase.close_estados_excepcion();
			}
		}
		return ret;
	}

	

	public static boolean deleteCompra(String id, String tipo, int programa, int subprograma) {
		boolean ret = false;
		if (CDatabase.connectEstadosExcepcion()) {
			try {
				PreparedStatement pstm = CDatabase.getConnection_estados_excepcion().prepareStatement("DELETE FROM seg_compra " + "WHERE "+
						(tipo.compareTo("NPG")==0? "NPG = '"+id+"'": "NOG = "+id) + " and programa = "+programa+" and subprograma= "+subprograma);
				if (pstm.executeUpdate() > 0)
					ret = true;
			} catch (Exception e) {
				CLogger.write("4", CCompra.class, e);
			} finally {
				CDatabase.close_estados_excepcion();
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
			CLogger.write("5", CCompraDAO.class, e);
		}
		finally{
			CDatabase.close();
		}
		return ret;
	}
	
	public static ArrayList<CEntidadCompra> getComprasEntidades(int subprograma){
		ArrayList<CEntidadCompra> ret=new ArrayList<CEntidadCompra>();
		if(CDatabase.connectEstadosExcepcion()){
			try{
				PreparedStatement pstm =  CDatabase.getConnection_estados_excepcion().prepareStatement("SELECT * FROM estado_de_calamidad WHERE programa=? and subprograma=?");
				pstm.setInt(1, 94);
				pstm.setInt(2, subprograma);
				ResultSet rs=pstm.executeQuery();
				PreparedStatement pstm2=null;
				ResultSet rs2;
				CEntidadCompra compra;
				CDatabase.connect();
				if (rs.next()){
						pstm2 = CDatabase.getConnection().prepareStatement("select entidad_compradora, entidad_compradora_nombre,count(*) num_eventos, " +
								"sum( case when estatus_concurso=3 then 1 else 0 end) num_adjudicados, " + 
								"sum( case when estatus_concurso=3 then monto else 0 end) total_adjudicado " + 
								"from mv_evento_gc where estado_calamidad=? group by entidad_compradora, entidad_compradora_nombre order by entidad_compradora_nombre");
						pstm2.setString(1,rs.getString("estado_calamidad_guatecompras"));
						rs2 = pstm2.executeQuery();
						while (rs2.next()){
							compra = new CEntidadCompra(rs2.getInt("entidad_compradora"),rs2.getString("entidad_compradora_nombre"), rs2.getInt("num_eventos"),
									rs2.getInt("num_adjudicados"),rs2.getDouble("total_adjudicado"));
							ret.add(compra);
						}		
				}
			}
			catch(Exception e){
				CLogger.write("6", CCompraDAO.class, e);
			}
			finally{
				CDatabase.close_estados_excepcion();
				CDatabase.close();
			}
		}
		return ret;		
	}
	
	public static ArrayList<CCompra> getComprasPorEntidad(int subprograma, int entidad){
		ArrayList<CCompra> ret=new ArrayList<CCompra>();
		if(CDatabase.connectEstadosExcepcion()){
			try{
				PreparedStatement pstm2=null;
				ResultSet rs2;
				CCompra compra;
				CDatabase.connect();
				pstm2 = CDatabase.getConnection().prepareStatement("select * from mv_evento_gc where nog_concurso in (SELECT nog FROM estados_excepcion.seg_compra WHERE programa=? and subprograma=? and entidad=?)");
				pstm2.setInt(1, 94);
				pstm2.setInt(2, subprograma);
				pstm2.setInt(3, entidad);
				rs2 = pstm2.executeQuery();
				while (rs2.next()){
					compra = new CCompra(rs2.getString("entidad_compradora_nombre"), rs2.getString("unidad_compradora_nombre"),"NOG", 
									rs2.getString("nog_concurso"), rs2.getTimestamp("fecha_publicacion"), rs2.getString("descripcion"), 
									rs2.getString("modalidad_nombre"),rs2.getString("estatus_concurso_nombre"),null, 
									null, rs2.getDouble("monto")
									);
					ret.add(compra);
				}		
			}
			catch(Exception e){
				CLogger.write("7", CCompraDAO.class, e);
			}
			finally{
				CDatabase.close_estados_excepcion();
				CDatabase.close();
			}
		}
		return ret;		
	}


}
