package dao.transparencia;

import java.sql.Connection;
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
		Connection conn = null;
		try{
			conn = CDatabase.connectEstadosExcepcion();
			if(!conn.isClosed()) {
				PreparedStatement pstm1 =  conn.prepareStatement("SELECT * FROM estado_de_calamidad WHERE programa=? and subprograma=?");
				pstm1.setInt(1, 94);
				pstm1.setInt(2, subprograma);
				ResultSet rs1=pstm1.executeQuery();
				rs1.next();
				
				PreparedStatement pstm =  conn.prepareStatement("select count(*) from seg_compra where programa=94 and subprograma=? and estado_calamidad_guatecompras=?");
				pstm.setInt(1, subprograma);
				pstm.setInt(2, rs1.getInt("estado_calamidad_guatecompras"));
				ResultSet rs = pstm.executeQuery();
				if (rs.next())
					ret=rs.getInt(1);
				
				rs1.close();
				pstm1.close();
				rs.close();
				pstm.close();
			
			}
		}
		catch(Exception e){
			CLogger.write("1", CCompraDAO.class, e);
		}
		finally{
			CDatabase.close(conn);
		}
		return ret;		
	}
	
	public static ArrayList<CCompra> getCompras(int subprograma){
		ArrayList<CCompra> ret=new ArrayList<CCompra>();
		Connection conn = null;
		try{
			conn = CDatabase.connectEstadosExcepcion();
			if(!conn.isClosed()){
				PreparedStatement pstm2=null;
				ResultSet rs2;
				CCompra compra;
				PreparedStatement pstm1 =  conn.prepareStatement("SELECT * FROM estado_de_calamidad WHERE programa=? and subprograma=?");
				pstm1.setInt(1, 94);
				pstm1.setInt(2, subprograma);
				ResultSet rs1=pstm1.executeQuery();
				rs1.next();
				
						pstm2 = conn.prepareStatement("select * from minfin.mv_evento_gc where nog_concurso in (SELECT nog FROM estados_excepcion.seg_compra WHERE programa=? and subprograma=? and estado_calamidad_guatecompras=?) order by fecha_publicacion");
						pstm2.setInt(1, 94);
						pstm2.setInt(2, subprograma);
						pstm2.setInt(3, rs1.getInt("estado_calamidad_guatecompras"));
						rs2 = pstm2.executeQuery();
						while (rs2.next()){
							compra = new CCompra(rs2.getString("entidad_compradora_nombre"), rs2.getString("unidad_compradora_nombre"),"NOG", 
									rs2.getString("nog_concurso"), rs2.getTimestamp("fecha_publicacion"), rs2.getString("descripcion"), 
									rs2.getString("modalidad_nombre"),rs2.getString("estatus_concurso_nombre"),null, 
									null, rs2.getDouble("monto")
									);
							ret.add(compra);
						}		
						
						rs2.close();
						pstm2.close();
						rs1.close();
						pstm1.close();
			}
		}
		catch(Exception e){
			CLogger.write("2", CCompraDAO.class, e);
		}
		finally{
			CDatabase.close(conn);
		}
		return ret;		
	}
	
public static boolean crearCompra(CCompra compra) {
		boolean ret = false;
		Connection conn = null;
		try{
			conn = CDatabase.connectEstadosExcepcion();
			if(!conn.isClosed()){
				PreparedStatement pstm =  conn
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
			
			}
		} catch (Exception e) {
			CLogger.write("3", CCompra.class, e);
		} finally {
			CDatabase.close(conn);
		}
		return ret;
	}

	

	public static boolean deleteCompra(String id, String tipo, int programa, int subprograma) {
		boolean ret = false;
		Connection conn = null;
		try{
			conn = CDatabase.connectEstadosExcepcion();
			if(!conn.isClosed()){
				PreparedStatement pstm =  conn.prepareStatement("DELETE FROM seg_compra " + "WHERE "+
						(tipo.compareTo("NPG")==0? "NPG = '"+id+"'": "NOG = "+id) + " and programa = "+programa+" and subprograma= "+subprograma);
				if (pstm.executeUpdate() > 0)
					ret = true;
			
			}
		} catch (Exception e) {
			CLogger.write("4", CCompra.class, e);
		} finally {
			CDatabase.close(conn);
		}
		return ret;
	}

	public static boolean getCompra(int nog) {
		boolean ret = false;
		Connection conn = null;
		try{
			conn = CDatabase.connectEstadosExcepcion();
			if(!conn.isClosed()){
				PreparedStatement pstm =  conn.prepareStatement("select count(*) total from minfin.mv_evento_gc where nog_concurso=?");
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
			CDatabase.close(conn);
		}
		return ret;
	}
	
	public static ArrayList<CEntidadCompra> getComprasEntidades(int subprograma){
		ArrayList<CEntidadCompra> ret=new ArrayList<CEntidadCompra>();
		Connection conn = null;
		try{
			conn = CDatabase.connectEstadosExcepcion();
			if(!conn.isClosed()){
				PreparedStatement pstm =  conn.prepareStatement("SELECT * FROM estado_de_calamidad WHERE programa=? and subprograma=?");
				pstm.setInt(1, 94);
				pstm.setInt(2, subprograma);
				ResultSet rs=pstm.executeQuery();
				PreparedStatement pstm2=null;
				ResultSet rs2;
				CEntidadCompra compra;
				if (rs.next()){
						pstm2 = conn.prepareStatement("select entidad_compradora, entidad_compradora_nombre,count(*) num_eventos, " +
								"sum( case when estatus_concurso=3 then 1 else 0 end) num_adjudicados, " + 
								"sum( case when estatus_concurso=3 then monto else 0 end) total_adjudicado " + 
								"from minfin.mv_evento_gc where estado_calamidad=? group by entidad_compradora, entidad_compradora_nombre order by entidad_compradora_nombre");
						pstm2.setString(1,rs.getString("estado_calamidad_guatecompras"));
						rs2 = pstm2.executeQuery();
						while (rs2.next()){
							compra = new CEntidadCompra(rs2.getInt("entidad_compradora"),rs2.getString("entidad_compradora_nombre"), rs2.getInt("num_eventos"),
									rs2.getInt("num_adjudicados"),rs2.getDouble("total_adjudicado"));
							ret.add(compra);
						}		
				}
			}
		}
		catch(Exception e){
			CLogger.write("6", CCompraDAO.class, e);
		}
		finally{
			CDatabase.close(conn);
		}
		return ret;		
	}
	
	public static ArrayList<CCompra> getComprasPorEntidad(int subprograma, int entidad){
		ArrayList<CCompra> ret=new ArrayList<CCompra>();
		Connection conn = null;
		try{
			conn = CDatabase.connectEstadosExcepcion();
			if(!conn.isClosed()){
				PreparedStatement pstm =  conn.prepareStatement("SELECT * FROM estado_de_calamidad WHERE programa=? and subprograma=?");
				pstm.setInt(1, 94);
				pstm.setInt(2, subprograma);
				ResultSet rs=pstm.executeQuery();
				rs.next();
				
				PreparedStatement pstm2=null;
				ResultSet rs2;
				CCompra compra;
				pstm2 = conn.prepareStatement("select * from minfin.mv_evento_gc where nog_concurso in ( " + 
						"SELECT nog FROM estados_excepcion.seg_compra WHERE programa=? and subprograma=? and entidad=? and estado_calamidad_guatecompras=? " 
					+")");
				pstm2.setInt(1, 94);
				pstm2.setInt(2, subprograma);
				pstm2.setInt(3, entidad);
				pstm2.setInt(4, rs.getInt("estado_calamidad_guatecompras_fuera_de_estado"));
				rs2 = pstm2.executeQuery();
				while (rs2.next()){
					compra = new CCompra(rs2.getString("entidad_compradora_nombre"), rs2.getString("unidad_compradora_nombre"),"NOG", 
									rs2.getString("nog_concurso"), rs2.getTimestamp("fecha_publicacion"), rs2.getString("descripcion"), 
									rs2.getString("modalidad_nombre"),rs2.getString("estatus_concurso_nombre"),null, 
									null, rs2.getDouble("monto")
									);
					ret.add(compra);
				}		
				rs2.close();
				pstm2.close();
				rs.close();
				pstm.close();
			}
		}
		catch(Exception e){
			CLogger.write("7", CCompraDAO.class, e);
		}
		finally{
			CDatabase.close(conn);
		}
		return ret;		
	}
	
	public static ArrayList<CEntidadCompra> getComprasEntidadesFueraDeEstado(int subprograma){
		ArrayList<CEntidadCompra> ret=new ArrayList<CEntidadCompra>();
		Connection conn = null;
		try{
			conn = CDatabase.connectEstadosExcepcion();
			if(!conn.isClosed()){
				PreparedStatement pstm =  conn.prepareStatement("SELECT * FROM estado_de_calamidad WHERE programa=? and subprograma=?");
				pstm.setInt(1, 94);
				pstm.setInt(2, subprograma);
				ResultSet rs=pstm.executeQuery();
				PreparedStatement pstm2=null;
				ResultSet rs2;
				CEntidadCompra compra;
				if (rs.next()){
						pstm2 = conn.prepareStatement("select entidad_compradora, entidad_compradora_nombre,count(*) num_eventos, " +
								"sum( case when estatus_concurso=3 then 1 else 0 end) num_adjudicados, " + 
								"sum( case when estatus_concurso=3 then monto else 0 end) total_adjudicado " + 
								"from minfin.mv_evento_gc where estado_calamidad=? group by entidad_compradora, entidad_compradora_nombre order by entidad_compradora_nombre");
						pstm2.setString(1,rs.getString("estado_calamidad_guatecompras_fuera_de_estado"));
						rs2 = pstm2.executeQuery();
						while (rs2.next()){
							compra = new CEntidadCompra(rs2.getInt("entidad_compradora"),rs2.getString("entidad_compradora_nombre"), rs2.getInt("num_eventos"),
									rs2.getInt("num_adjudicados"),rs2.getDouble("total_adjudicado"));
							ret.add(compra);
						}		
				}
			}
		}
		catch(Exception e){
			CLogger.write("8", CCompraDAO.class, e);
		}
		finally{
			CDatabase.close(conn);
		}
		return ret;		
	}
	
	public static ArrayList<CCompra> getComprasPorEntidadFueraDeEstado(int subprograma, int entidad){
		ArrayList<CCompra> ret=new ArrayList<CCompra>();
		Connection conn = null;
		try{
			conn = CDatabase.connectEstadosExcepcion();
			if(!conn.isClosed()){
				PreparedStatement pstm2=null;
				ResultSet rs2;
				CCompra compra;
				
				PreparedStatement pstm =  conn.prepareStatement("SELECT * FROM estado_de_calamidad WHERE programa=? and subprograma=?");
				pstm.setInt(1, 94);
				pstm.setInt(2, subprograma);
				ResultSet rs=pstm.executeQuery();
				rs.next();
				
				pstm2 = conn.prepareStatement("select * from minfin.mv_evento_gc " +
						"where nog_concurso in ( " +
							" SELECT nog FROM estados_excepcion.seg_compra WHERE programa=? and subprograma=? and entidad=? and estado_calamidad_guatecompras=?"+
						")");
				pstm2.setInt(1, 94);
				pstm2.setInt(2, subprograma);
				pstm2.setInt(3, entidad);
				pstm2.setInt(4, rs.getInt("estado_calamidad_guatecompras_fuera_de_estado"));
				rs2 = pstm2.executeQuery();
				while (rs2.next()){
					compra = new CCompra(rs2.getString("entidad_compradora_nombre"), rs2.getString("unidad_compradora_nombre"),"NOG", 
									rs2.getString("nog_concurso"), rs2.getTimestamp("fecha_publicacion"), rs2.getString("descripcion"), 
									rs2.getString("modalidad_nombre"),rs2.getString("estatus_concurso_nombre"),null, 
									null, rs2.getDouble("monto")
									);
					ret.add(compra);
				}
				rs2.close();
				pstm2.close();
				rs.close();
				pstm.close();
			}
		}
		catch(Exception e){
			CLogger.write("9", CCompraDAO.class, e);
		}
		finally{
			CDatabase.close(conn);
		}
		return ret;		
	}
	
	public static Integer numComprasFuera(int subprograma){
		Integer ret=0;
		Connection conn = null;
		try{
			conn = CDatabase.connectEstadosExcepcion();
			
			PreparedStatement pstm1 =  conn.prepareStatement("SELECT * FROM estado_de_calamidad WHERE programa=? and subprograma=?");
			pstm1.setInt(1, 94);
			pstm1.setInt(2, subprograma);
			ResultSet rs1=pstm1.executeQuery();
			rs1.next();
			
			if(!conn.isClosed()) {
				PreparedStatement pstm =  conn.prepareStatement("select count(*) from seg_compra where programa=94 and subprograma=? and estado_calamidad_guatecompras=?");
				pstm.setInt(1, subprograma);
				pstm.setInt(2, rs1.getInt("estado_calamidad_guatecompras_fuera_de_estado"));
				ResultSet rs = pstm.executeQuery();
				if (rs.next())
					ret=rs.getInt(1);
				
				rs1.close();
				pstm1.close();
				rs.close();
				pstm.close();
			}
		}
		catch(Exception e){
			CLogger.write("10", CCompraDAO.class, e);
		}
		finally{
			CDatabase.close(conn);
		}
		return ret;		
	}
	
	public static ArrayList<CCompra> getComprasFueaDeEstado(int subprograma){
		ArrayList<CCompra> ret=new ArrayList<CCompra>();
		Connection conn = null;
		try{
			conn = CDatabase.connectEstadosExcepcion();
			if(!conn.isClosed()){
				PreparedStatement pstm2=null;
				ResultSet rs2;
				CCompra compra;
				PreparedStatement pstm1 =  conn.prepareStatement("SELECT * FROM estado_de_calamidad WHERE programa=? and subprograma=?");
				pstm1.setInt(1, 94);
				pstm1.setInt(2, subprograma);
				ResultSet rs1=pstm1.executeQuery();
				rs1.next();
				
						pstm2 = conn.prepareStatement("select * from minfin.mv_evento_gc where nog_concurso in (SELECT nog FROM estados_excepcion.seg_compra WHERE programa=? and subprograma=? and estado_calamidad_guatecompras=?) order by fecha_publicacion");
						pstm2.setInt(1, 94);
						pstm2.setInt(2, subprograma);
						pstm2.setInt(3, rs1.getInt("estado_calamidad_guatecompras_fuera_de_estado"));
						rs2 = pstm2.executeQuery();
						while (rs2.next()){
							compra = new CCompra(rs2.getString("entidad_compradora_nombre"), rs2.getString("unidad_compradora_nombre"),"NOG", 
									rs2.getString("nog_concurso"), rs2.getTimestamp("fecha_publicacion"), rs2.getString("descripcion"), 
									rs2.getString("modalidad_nombre"),rs2.getString("estatus_concurso_nombre"),null, 
									null, rs2.getDouble("monto")
									);
							ret.add(compra);
						}		
						
						rs2.close();
						pstm2.close();
						rs1.close();
						pstm1.close();
			}
		}
		catch(Exception e){
			CLogger.write("11", CCompraDAO.class, e);
		}
		finally{
			CDatabase.close(conn);
		}
		return ret;		
	}


}
