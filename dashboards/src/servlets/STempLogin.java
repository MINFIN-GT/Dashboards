package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import dao.CUserDAO;
import pojo.CUser;
import shiro.utilities.CShiro;

/**
 * Servlet implementation class STempLogin
 */
@WebServlet("/STempLogin")
public class STempLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public STempLogin() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		Gson gson = new Gson();
		Type type = new TypeToken<Map<String, String>>(){}.getType();
		StringBuilder sb = new StringBuilder();
	    BufferedReader br = request.getReader();
	    String str;
	    while( (str = br.readLine()) != null ){
	        sb.append(str);
	    };
		Map<String, String> map = gson.fromJson(sb.toString(), type);
		
		String username = map.get("username");
		String password = map.get("password");
		String hash = map.get("hash");
		
		if(username!=null && username!="" && password!=null && password!="" && hash!=null && hash!=""){
			boolean result = CUserDAO.checkTemporalAccess(username, hash);
			if(result){
				boolean updated = CUserDAO.updateUserPassword(username, password);
				if(updated){
					CUserDAO.deleteTemporalAccess(username);
					Subject currentUser = CShiro.getSubject();
					if (!currentUser.isAuthenticated()) {
						UsernamePasswordToken token = new UsernamePasswordToken(username, password);
						token.setRememberMe(false);

						try {
							currentUser.login(token);
							CUser user=CUserDAO.getUser(map.get("username").toLowerCase());
							CShiro.setAttribute("username", user.getUsername());
							CShiro.setAttribute("user",user);
							response.getWriter().write("{ \"success\": true }");
							CUserDAO.userLoginHistory(user.getUsername());
						} catch (UnknownAccountException uae) {
							response.getWriter().write("{ \"success\": false, \"error\":\"Unknow Account\" }");
						} catch (IncorrectCredentialsException ice) {
							response.getWriter().write("{ \"success\": false, \"error\": \"Incorrect Credential\" }");
						} catch (LockedAccountException lae) {
							response.getWriter().write("{ \"success\": false,\"error\": \"Locked Account Exception\" }");
						} catch (Exception e) {
							response.getWriter().write("{ \"success\": false,\"error\": \"Unknow Exception\" }");
						}
					} else {
						response.getWriter().write("{ \"success\": true }");
					}
				}
				else
					response.getWriter().write("{ \"success\": false }");
			}
			else
				response.getWriter().write("{ \"success\": false }");
		}
		
		
		response.getWriter().flush();
		response.getWriter().close();
	}

}
