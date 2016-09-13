package dao;

import java.util.ArrayList;
import java.util.List;

public class DataAccessObject {

	public static <T> T getInstance(Class<T> t) {
		try {
			return t.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static <T> List<T> getAll() throws InstantiationException, IllegalAccessException {
		return new ArrayList<T>();
	}

}
