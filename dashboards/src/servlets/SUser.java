package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import dao.CUserDAO;
import pojo.CUser;
import pojo.CUserPermission;

/**
 * Servlet implementation class SUser
 */
@WebServlet("/SUser")
public class SUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SUser() {
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
		Gson gson = new Gson();
		Type type = new TypeToken<Map<String, String>>(){}.getType();
		StringBuilder sb = new StringBuilder();
	    BufferedReader br = request.getReader();
	    String str;
	    while( (str = br.readLine()) != null ){
	        sb.append(str);
	    };
	    response.setHeader("Content-Encoding", "gzip");
		response.setCharacterEncoding("UTF-8");
		Map<String, String> map = gson.fromJson(sb.toString(), type);
		String action = map.get("action");
		String response_text=null;
		if(action.equals("usersList")){
			ArrayList<CUser> users = CUserDAO.getUsersList();
			String response_users = new GsonBuilder().serializeNulls().create().toJson(users);
			response_text = String.join("", "\"users\":",response_users);
			response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(action.equals("saveUser")){
			Map<String,String> map_user = gson.fromJson(map.get("user"), type);
			String permissions = map.get("permissions");
			String rol = map.get("rol");
			boolean result=false;
			if(map.get("isnew")=="true"){
				result = CUserDAO.createUser(map_user.get("username"), map_user.get("password"), map_user.get("firstname"), 
						map_user.get("secondname"), map_user.get("lastname"), map_user.get("secondlastname"), map_user.get("dependence"), 
						map_user.get("position"), map_user.get("email"));
			}
			else{
 				result = CUserDAO.updateUser(map_user.get("username"), map_user.get("password"), map_user.get("firstname"), 
						map_user.get("secondname"), map_user.get("lastname"), map_user.get("secondlastname"), map_user.get("dependence"), 
						map_user.get("position"), map_user.get("email"));
			}
			result = result && CUserDAO.updatePermissions(map_user.get("username"),permissions);
			result = result && CUserDAO.updateRol(map_user.get("username"),rol);
			response_text = String.join("", "{\"success\":"+(result ? "true" : "false")+"}");
		}
		else if(action.equals("userPermissions")){
			String username = map.get("user");
			ArrayList<CUserPermission> permissions= CUserDAO.getUserPermissions(username);
			String response_permissions = new GsonBuilder().serializeNulls().create().toJson(permissions);
			response_text = String.join("", "\"permissions\":",response_permissions);
			response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(action.equals("deleteUser")){
			String username = map.get("user");
			boolean result = CUserDAO.deleteUser(username);
			response_text = String.join("", "{\"success\":"+(result ? "true" : "false")+"}");
		}
		else if(action.equals("emailUser")){
			String username = map.get("user");
			boolean result = CUserDAO.sendEmail(username);
			response_text = String.join("", "{\"success\":"+(result ? "true" : "false")+"}");
		}
		else if(action.equals("viewEmailUser")){
			String username = map.get("user");
			String result = CUserDAO.getUserEmail(username);
			response_text = String.join("", "{\"success\":"+(result!=null ? "true" : "false")+", \"hash\": \""+result+"\"}");
		}
		if(response_text!=null){
			OutputStream output = response.getOutputStream();
			GZIPOutputStream gz = new GZIPOutputStream(output);
	        gz.write(response_text.getBytes("UTF-8"));
	        gz.close();
	        output.close();
		}
		
        
	}

}
