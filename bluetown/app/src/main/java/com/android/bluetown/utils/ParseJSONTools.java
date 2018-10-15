package com.android.bluetown.utils;

import java.lang.reflect.Field;
import java.util.Date;

import org.json.JSONObject;

/**
 * <解析JSON的方法>
 * 
 * @author
 */
public class ParseJSONTools {
	private static ParseJSONTools tools;

	public static ParseJSONTools getInstance() {
		if (null == tools) {
			tools = new ParseJSONTools();
		}
		return tools;
	}

	/**
	 * <将json对象转化为java实体类>
	 */
	public Object fromJsonToJava(JSONObject jo, Class pojo) throws Exception {
		/**
		 * 首先得到pojo所定义的字段
		 */
		Field[] fields = pojo.getFields();
		/**
		 * 根据传入的Class动态生成pojo对象
		 */
		Object obj = pojo.newInstance();
		for (Field field : fields) {
			// 设置字段可访问（必须，否则报错）
			field.setAccessible(true);

			// 得到字段的属性名
			String name = field.getName();

			// 这一段的作用是如果字段在JSONObject中不存在会抛出异常，如果出异常，则跳过。
			try {
				jo.get(name);

			if (null != jo.get(name)) {
				// 根据字段的类型将值转化为相应的类型，并设置到生成的对象中。
				if (field.getType().equals(Long.class)
						|| field.getType().equals(long.class)) {
					field.set(obj, Long.parseLong(jo.getString(name)));
				} else if (field.getType().equals(String.class)) {
					field.set(obj, jo.getString(name));
				} else if (field.getType().equals(Double.class)
						|| field.getType().equals(double.class)) {
					field.set(obj, Double.parseDouble(jo.getString(name)));
				} else if (field.getType().equals(Integer.class)
						|| field.getType().equals(int.class)) {
					field.set(obj, Integer.parseInt(jo.getString(name)));
				} else if (field.getType().equals(Boolean.class)
						|| field.getType().equals(boolean.class)) {
					field.set(obj, jo.getBoolean(name));
				} else if (field.getType().equals(Date.class)) {
					field.set(obj, Date.parse(jo.getString(name)));
				} else if (field.getType().equals(JSONObject.class)) {
					field.set(obj, jo.getJSONObject(name));
				} else {
					continue;
				}
			}
			} catch (Exception ex) {
				//LogUtil.LogError(field.getName());
				continue;
			}
		}
		return obj;
	}

}