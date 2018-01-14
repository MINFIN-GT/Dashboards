package servlets;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
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

import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.lang3.StringEscapeUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import utilities.CViewLog;

/**
 * Servlet implementation class SViewLog
 */
@WebServlet("/SViewLog")
public class SViewLog extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	//private static String path = "/Users/rafa/hadoop/bash_logs";
	private static String path = "/home/hadoop/bash_logs";
	
	/**
     * @see HttpServlet#HttpServlet()
     */
    public SViewLog() {
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
		String response_text=null;
		Gson gson = new Gson();
		Type type = new TypeToken<Map<String, String>>(){}.getType();
		StringBuilder sb = new StringBuilder();
	    BufferedReader br = request.getReader();
	    String str;
	    while( (str = br.readLine()) != null ){
	        sb.append(str);
	    };
		Map<String, String> map = gson.fromJson(sb.toString(), type);
		String action = map.get("action");
		response.setHeader("Content-Encoding", "gzip");
		response.setCharacterEncoding("UTF-8");
		response_text="";
		if(action.equals("getListFiles")){
			ArrayList<String> files=CViewLog.getListLogs(path);
			
			for(String file:files){
				response_text = String.join("", response_text,",{ \"name\": \"",file,"\", \"children\":[] }");
			}
            response_text = String.join("", "\"files\":[",response_text.substring(1),"]");
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
	        
	        
		}
		else if(action.equals("getContentFile")){
			String name=map.get("name");
			ByteArrayOutputStream bos = loadFile(name);
			String Base64String = StringUtils.newStringUtf8(bos.toByteArray());
			Base64String = StringEscapeUtils.escapeJson(Base64String);
			response_text = String.join("", "\"content_file\":\"",Base64String,"\"");
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		OutputStream output = response.getOutputStream();
		GZIPOutputStream gz = new GZIPOutputStream(output);
        gz.write(response_text.getBytes("UTF-8"));
        gz.close();
        output.close();
	}
	
	private ByteArrayOutputStream loadFile(String fileName)
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
		try {
			File file = new File(path+"/"+fileName);
			if(file.exists()){
				FileInputStream fis = new FileInputStream(file);
				byte[] buf = new byte[1024];
				for (int readNum; (readNum = fis.read(buf)) != -1;) {
					bos.write(buf, 0, readNum); 
				}
				fis.close();
			}
		} 
		catch (IOException ex) {
			ex.printStackTrace();
		}
		return bos;
	}

}
