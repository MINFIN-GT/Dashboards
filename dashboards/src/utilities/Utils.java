package utilities;

import java.io.BufferedReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class Utils {

	public static Map<String, String> getParams(HttpServletRequest request) {

		Map<String, String> map = null;

		try {

			request.setCharacterEncoding("UTF-8");

			Gson gson = new Gson();

			Type type = new TypeToken<Map<String, String>>() {
			}.getType();

			StringBuilder sb = new StringBuilder();
			BufferedReader br = request.getReader();
			String str;

			while ((str = br.readLine()) != null) {
				sb.append(str);
			}

			map = gson.fromJson(sb.toString(), type);

		} catch (Exception e) {

		}

		return map;
	}

	public static String getJSonString(String nombre, List<?> objetos) {

		String jsonText = new GsonBuilder().serializeNulls().create().toJson(objetos);

		jsonText = String.join("", "\"" + nombre + "\":", jsonText);

		jsonText = String.join("", "{\"success\":true,", jsonText, "}");

		return jsonText;
	}

	public static int String2Int(String value) {
		
		try {
			
			int val = Integer.parseInt(value);
			
			return val;
			
		} catch (NumberFormatException e) {
			
			return 0;
		}

	}

}
