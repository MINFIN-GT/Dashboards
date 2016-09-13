package dao;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import annotations.Column;

public class DataAccessObject {

	public static <T> List<T> getListPojo(Class<T> pojoClass, ResultSet result) {
		try {
			if (pojoClass == null || result == null) {
				return null;
			}

			// obtenemos los nombres de las columnas
			// el campo puede ser el nombre de la columna
			// o puede ser personalizado con la annotacion @Column
			Map<String, Field> campos = new HashMap<String, Field>();
			for (Field field : pojoClass.getDeclaredFields()) {
				if (field.getName().compareToIgnoreCase("serialVersionUID") != 0) {

					// accedemos a parametros privados
					field.setAccessible(true);

					// obtenemos el nombre del campo de nuestro POJO
					String key = field.getName().toLowerCase();

					// obtenemos las anotaciones del campo
					Annotation annotation = field.getAnnotation(Column.class);
					// si las anotaciones son de tipo Column
					if (annotation != null && annotation instanceof Column) {
						// obtenemos el nombre de la columna
						key = ((Column) annotation).ColumnName();
					}

					// insertamos con nombre de columna o campo a
					// configurar su campo
					campos.put(key, field);
				}
			}

			// obtenemos los nombres de las columnas
			// estos nombres de las columnas deben de coincidir con
			// alguno de los campos
			List<String> columnas = new ArrayList<String>();
			for (int i = 1; i <= result.getMetaData().getColumnCount(); i++) {
				columnas.add(result.getMetaData().getColumnName(i).toLowerCase());
			}

			List<T> values = new ArrayList<T>();
			while (result.next()) {

				T clase = pojoClass.newInstance();

				for (String columna : columnas) {

					Field campo = campos.get(columna);
					try {
						campo.set(clase, result.getObject(columna));
					} catch (Exception e) {
						// no hacer nada
						// debe segir cargando los otros valores
					}
				}

				values.add(clase);
			}

			return values;
		} catch (Exception e) {
			System.out.println("---------------error");
		}

		return new ArrayList<T>();
	}

}
