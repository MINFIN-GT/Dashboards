package dao;

import java.net.URLEncoder;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.util.ByteSource;
import org.joda.time.DateTime;

import db.utilities.CDatabase;
import pojo.CUser;
import pojo.CUserPermission;
import utilities.CLogger;

public class CUserDAO {
	CUser user;
	
	public static boolean createUser(String username, String plainPassword, String firstname, String secondname, String lastname,
			String secondlastname, String dependence, String position, String email){
		try{
			RandomNumberGenerator rng = new SecureRandomNumberGenerator();
			ByteSource salt = rng.nextBytes();

			CUser user = new CUser(username, new Sha256Hash(plainPassword, salt, 1024).toBase64(), firstname, secondname, lastname, secondlastname, 
					dependence, position, salt.toBase64(), email);
	        if(CDatabase.connect()){
	        	PreparedStatement pstm = CDatabase.getConnection().prepareStatement("INSERT INTO user(username, password, firstname, secondname, lastname, secondlastname, dependence, position, salt, email) "
	        			+ " VALUES(?,?,?,?,?,?,?,?,?,?) ");
	        	pstm.setString(1, username);
	        	pstm.setString(2, user.getPassword());
	        	pstm.setString(3, firstname);
	        	pstm.setString(4, secondname);
	        	pstm.setString(5, lastname);
	        	pstm.setString(6, secondlastname);
	        	pstm.setString(7, dependence);
	        	pstm.setString(8, position);
	        	pstm.setString(9, user.getSalt());
	        	pstm.setString(10, email);
	        	pstm.executeUpdate();
	        	pstm.close();
	        	CDatabase.close();
	        	return true;
	        	
	        }
        }
        catch(Exception e){
        	CLogger.write("1", CUserDAO.class, e);
        }
		return false;
	}
	
	public static boolean updateUser(String username, String plainPassword, String firstname, String secondname, String lastname,
			String secondlastname, String dependence, String position, String email){
		try{
			RandomNumberGenerator rng=null;
			ByteSource salt=null;
			String password=null;
			if(plainPassword!=null && plainPassword.length()>0){
				rng = new SecureRandomNumberGenerator();
				salt = rng.nextBytes();
				password = new Sha256Hash(plainPassword, salt, 1024).toBase64();
			}

			if(CDatabase.connect()){
	        	PreparedStatement pstm = CDatabase.getConnection().prepareStatement("UPDATE user SET "
	        			+ "firstname = ?, secondname=?, lastname=?, secondlastname=?, dependence=?, position=?, email=? "
	        			+ (salt!=null ? ", password=?, salt=? " : "" ) + " WHERE username = ?");
	        	pstm.setString(1, firstname);
	        	pstm.setString(2, secondname);
	        	pstm.setString(3, lastname);
	        	pstm.setString(4, secondlastname);
	        	pstm.setString(5, dependence);
	        	pstm.setString(6, position);
	        	pstm.setString(7, email);
	        	if(salt!=null){
		        	pstm.setString(8, password);
		        	pstm.setString(9, salt.toBase64());
		        	pstm.setString(10, username);
	        	}
	        	else
	        		pstm.setString(8, username);
	        	pstm.executeUpdate();
	        	pstm.close();
	        	CDatabase.close();
	        	return true;
	        }
        }
        catch(Exception e){
        	CLogger.write("2", CUserDAO.class, e);
        }
		return false;
	}
	
	public static boolean userLoginHistory(String username){
		try{
			boolean ret=false;
			DateTime datetime = new DateTime();
	        if(CDatabase.connect()){
	        	PreparedStatement pstm = CDatabase.getConnection().prepareStatement("INSERT INTO user_login VALUES(?,?)");
	        	pstm.setString(1, username);
	        	pstm.setTimestamp(2, new Timestamp(datetime.getMillis()));
	        	ret = pstm.executeUpdate() > 0;
	        	pstm.close();
	        	CDatabase.close();
	        	return ret;
	        }
        }
        catch(Exception e){
        	CLogger.write("3", CUserDAO.class, e);
        }
        return false;
	}
	
	public static CUser getUser(String username){
		CUser ret=null;
		 try{
		        if(CDatabase.connect()){
		        	PreparedStatement pstm = CDatabase.getConnection().prepareStatement("select * from user where username=?");
		        	pstm.setString(1, username);
		        	ResultSet rs = pstm.executeQuery();
		        	if(rs.next()){
		        		ret = new CUser(rs.getString("username"),rs.getString("password"), rs.getString("firstname"), rs.getString("secondname"),
		        				rs.getString("lastname"), rs.getString("secondlastname"), rs.getString("dependence"), rs.getString("position"),
		        				rs.getString("salt"), rs.getString("email"));
		        	}
		        	rs.close();
		        	pstm.close();
		        	CDatabase.close();
		        }
	        }
	        catch(Exception e){
	        	CLogger.write("4", CUserDAO.class, e);
	        }
		return ret;	
	}
	
	public static boolean hasUserPermission(String username, String permission){
		boolean ret=false;
		try{
			Integer permission_id = Integer.parseInt(permission);
			if(CDatabase.connect()){
				PreparedStatement pstm = CDatabase.getConnection().prepareStatement("select count(*) from user_permiso where username = ? AND permiso_id = ?");
				pstm.setString(1, username);
				pstm.setInt(2, permission_id);
				ResultSet rs = pstm.executeQuery();
				if(rs.next())
					ret = rs.getInt(1) > 0;
				rs.close();
				pstm.close();
				CDatabase.close();
			}
		}
		catch(Throwable e){
				CLogger.write("5", CUserDAO.class, e);
		}
		return ret;
	}
	
	public static boolean hasUserRole(String username, String role){
		boolean ret=false;
		try{
			Integer role_id = Integer.parseInt(role);
			if(CDatabase.connect()){
				PreparedStatement pstm = CDatabase.getConnection().prepareStatement("select count(*) from user_role where username = ? AND role_id = ?");
				pstm.setString(1, username);
				pstm.setInt(2, role_id);
				ResultSet rs = pstm.executeQuery();
				if(rs.next())
					ret = rs.getInt(1) > 0;
				rs.close();
				pstm.close();
				CDatabase.close();
			}
		}
		catch(Throwable e){
				CLogger.write("6", CUserDAO.class, e);
		}
		return ret;
	}
	
	public static ArrayList<CUser> getUsersList(){
		ArrayList<CUser> users=new ArrayList<CUser>();
		try{
			if(CDatabase.connect()){
				PreparedStatement pstm = CDatabase.getConnection().prepareStatement("select * from user order by username");
				ResultSet rs = pstm.executeQuery();
				while(rs.next()){
					CUser user = new CUser(rs.getString("username"), null, rs.getString("firstname"), rs.getString("secondname"), 
							rs.getString("lastname"), rs.getString("secondlastname"), rs.getString("dependence"), 
							rs.getString("position"), null, rs.getString("email"));
					users.add(user);
				}
				rs.close();
				pstm.close();
				CDatabase.close();
			}
		}
		catch(Exception e){
			CLogger.write("7", CUserDAO.class, e);
		}
		return users;
	}
	
	public static boolean updatePermissions(String username,String permissions){
		boolean ret = false;
		try{
			if(permissions!=null){
				if(CDatabase.connect()){
					String[] apermissions = permissions.split(",");
					PreparedStatement pstm = CDatabase.getConnection().prepareStatement("delete from user_permiso where username = ?");
					pstm.setString(1, username);
					pstm.executeUpdate();
					for(String permission : apermissions){
						if(permission!=null && permission.length()>0){
							pstm = CDatabase.getConnection().prepareStatement("INSERT INTO user_permiso VALUES(?,?)");
							pstm.setInt(1, Integer.parseInt(permission));
							pstm.setString(2, username);
							pstm.executeUpdate();
						}
					}
					pstm.close();
					CDatabase.close();
					ret = true;
				}
			}
			else
				ret = true;
		}
		catch(Throwable e){
			CLogger.write("8", CUserDAO.class, e);
		}
		return ret;
	}
	
	public static boolean updateRol(String username,String rol){
		boolean ret = false;
		try{
			if(rol!=null){
				if(CDatabase.connect()){
					PreparedStatement pstm = CDatabase.getConnection().prepareStatement("delete from user_rol where username = ?");
					pstm.setString(1, username);
					pstm.executeUpdate();
					
						pstm = CDatabase.getConnection().prepareStatement("INSERT INTO user_rol VALUES(?,?)");
						pstm.setInt(1, Integer.parseInt(rol));
						pstm.setString(2, username);
						pstm.executeUpdate();
					
					pstm.close();
					CDatabase.close();
					ret = true;
				}
			}
			else
				ret = true;
		}
		catch(Throwable e){
			CLogger.write("9", CUserDAO.class, e);
		}
		return ret;
	}

	public static ArrayList<CUserPermission> getUserPermissions(String username) {
		ArrayList<CUserPermission> permissions=new ArrayList<CUserPermission>();
		try{
			if(CDatabase.connect()){
				PreparedStatement pstm = CDatabase.getConnection().prepareStatement("select p.*, ifnull(up.permiso_id,0)>0 haspermission " + 
						"from permiso p " + 
						"left outer join user_permiso up on (p.id = up.permiso_id and up.username = ?) order by p.nombre");
				pstm.setString(1, username);
				ResultSet rs = pstm.executeQuery();
				while(rs.next()){
					CUserPermission permission = new CUserPermission(rs.getInt("id"), rs.getString("nombre"), rs.getInt("haspermission")==1);
					permissions.add(permission);
				}
				rs.close();
				pstm.close();
				CDatabase.close();
			}
		}
		catch(Exception e){
			CLogger.write("10", CUserDAO.class, e);
		}
		return permissions;
	}

	public static boolean deleteUser(String username) {
		boolean ret = false;
		try{
			if(CDatabase.connect()){
				PreparedStatement pstm = CDatabase.getConnection().prepareStatement("delete from user_permiso where username = ?");
				pstm.setString(1, username);
				pstm.executeUpdate();
				pstm = CDatabase.getConnection().prepareStatement("delete from user_rol where username = ?");
				pstm.setString(1, username);
				pstm.executeUpdate();
				pstm = CDatabase.getConnection().prepareStatement("delete from user where username = ?");
				pstm.setString(1, username);
				pstm.executeUpdate();
				pstm.close();
				CDatabase.close();
				ret = true;
			}
		}
		catch(Throwable e){
			CLogger.write("11", CUserDAO.class, e);
		}
		return ret;
	}
	
	public static String getTemporalAccess(String username){
		String ret=null;
		RandomNumberGenerator rng=new SecureRandomNumberGenerator();
		ByteSource salt=rng.nextBytes();
		String hash=new Sha256Hash(username, salt, 1024).toBase64();
		try{
			if(CDatabase.connect()){
				PreparedStatement pstm = CDatabase.getConnection().prepareStatement("DELETE FROM user_acceso_temporal WHERE username=?");
				pstm.setString(1, username);
				pstm.executeUpdate();
				pstm.close();
				pstm = CDatabase.getConnection().prepareStatement("INSERT INTO user_acceso_temporal VALUES(?,?,?,1)");
				pstm.setString(1, username);
				pstm.setTimestamp(2, new Timestamp(new DateTime().getMillis()));
				pstm.setString(3, hash);
				pstm.executeUpdate();
				pstm.close();
				CDatabase.close();
				ret=hash;
			}
		}
		catch(Throwable e){
			CLogger.write("12", CUserDAO.class, e);
		}
		
		return ret;
	}
	
	public static boolean sendEmail(String username){
		boolean ret = false;
		CUser user = getUser(username);
		if(user!=null){
			try
		    {
				String hash = getTemporalAccess(username);
				if(hash!=null){
					Authenticator auth = new Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication("hreyes@minfin.gob.gt", "");
						}
					};
					Properties props = new Properties();
					Session session = Session.getInstance(props, auth);	
					MimeMessage msg = new MimeMessage(session);
					msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
					msg.addHeader("format", "flowed");
					msg.addHeader("Content-Transfer-Encoding", "8bit");
					msg.setFrom(new InternetAddress("hreyes@minfin.gob.gt", "Rafael Reyes"));
					msg.setReplyTo(InternetAddress.parse("hreyes@minfin.gob.gt", false));
					msg.setSubject("Sistema de tableros MINFIN", "UTF-8");
					msg.setContent("Estimado "+user.getFirstname()+" "+user.getLastname()+": <br/><br/> Es un gusto saludarle y desearle éxitos en sus actividades diarias.<br/>"
							+ "Le damos la bienvenida al uso del sistema de tableros del MINFIN, del cual se describen a continuación los datos para su accesso.<br/><br/>"
							+ "Link: <a href=\"http:\\\\tableros.minfin.gob.gt\\templogin.jsp?user="+username+"&access="+URLEncoder.encode(hash,"UTF-8")+"\">http://tableros.minfin.gob.gt</a><br/>"
							+ "Usurio: "+user.getUsername()+"<br/><br/>"
							+ "Al ingresar el sistema le pedirá que ingrese una contraseña la cual utilizará para el ingreso al sistema.<br/><br/>"
							+ "Agradeceré cualquier comentario que tenga sobre el sistema para poder incluirlo en mejoras futuras.<br/></br>"
							+"Ing. Rafael Reyes<br/>"
							+"Ministerio de Finanzas Públicas", "text/html; charset=utf-8");
					msg.setSentDate(new Date());
					msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail(), false));
					msg.saveChanges();
					Transport transport = session.getTransport("smtp");
					transport.connect("172.30.1.50", "", "");
					transport.sendMessage(msg, msg.getAllRecipients());
					transport.close();
					
					ret = true;
				}
		    }
		    catch (Exception e) {
		    	CLogger.write("13", CUserDAO.class, e);
		    }
		}
		return ret;
	}
	
	public static String getUserEmail(String username){
		String ret = null;
		try{
			if(CDatabase.connect()){
				PreparedStatement pstm = CDatabase.getConnection().prepareStatement("SELECT * FROM user_acceso_temporal WHERE username=?");
				pstm.setString(1, username);
				ResultSet rs = pstm.executeQuery();
				if(rs.next()){
					ret = rs.getString("hash");
				}
				rs.close();
				pstm.close();
				CDatabase.close();
			}
		}
		catch(Throwable e){
			CLogger.write("14", CUserDAO.class, e);
		}
		return ret;
	}

	public static boolean checkTemporalAccess(String username, String hash) {
		boolean ret=false;
		try{
			if(CDatabase.connect()){
				PreparedStatement pstm = CDatabase.getConnection().prepareStatement("SELECT count(*) total FROM user_acceso_temporal WHERE username=? AND hash=?");
				pstm.setString(1, username);
				pstm.setString(2, hash);
				ResultSet rs = pstm.executeQuery();
				if(rs.next()){
					ret = rs.getInt("total")==1;
				}
				rs.close();
				pstm.close();
				CDatabase.close();
			}
		}
		catch(Throwable e){
			CLogger.write("15", CUserDAO.class, e);
		}
		return ret;
	}

	public static boolean updateUserPassword(String username, String plain_password) {
		boolean ret=false;
		RandomNumberGenerator rng=null;
		ByteSource salt=null;
		String password=null;
		
		rng = new SecureRandomNumberGenerator();
		salt = rng.nextBytes();
		password = new Sha256Hash(plain_password, salt, 1024).toBase64();
		
		try{
			if(CDatabase.connect()){
	        	PreparedStatement pstm = CDatabase.getConnection().prepareStatement("UPDATE user SET password=?, salt=?  WHERE username = ?");
	        	pstm.setString(1, password);
		        pstm.setString(2, salt.toBase64());
		        pstm.setString(3, username);
	        	pstm.executeUpdate();
	        	pstm.close();
	        	CDatabase.close();
	        	ret=true;
			}
		}
		catch(Throwable e){
			CLogger.write("16", CUserDAO.class, e);
		}
		return ret;
	}

	public static void deleteTemporalAccess(String username) {
		try{
			if(CDatabase.connect()){
				PreparedStatement pstm = CDatabase.getConnection().prepareStatement("DELETE FROM user_acceso_temporal WHERE username=?");
				pstm.setString(1, username);
				pstm.executeUpdate();
				pstm.close();
				CDatabase.close();
			}
		}
		catch(Throwable e){
			CLogger.write("17", CUserDAO.class, e);
		}
	}
}
