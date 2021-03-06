package prj.dee.util;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Map;

public class BaseUtil {

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat sdt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private SimpleDateFormat ymd = new SimpleDateFormat("yyyy年MM月dd日");
	private SimpleDateFormat ymdhms = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
	
	public static String returnString(Object object) {
		try {
			if(object instanceof Integer){
				return String.valueOf(object);
			}
			return object.toString();
		} catch (Exception e) {
			return "";
		}
	}
	
	public static Integer returnInt(Object obj){
		try {
			if(obj != null){
				return Integer.valueOf(obj.toString());
			}
		} catch (Exception e) {}
		return 0;
	}

	public static Double returnDouble(Object obj){
		try {
			if(obj != null){
				return Double.valueOf(obj.toString());
			}
		} catch (Exception e) {}
		return 0D;
	}
	
	public String returnDateType(Object obj, String type){
		try {
			if(obj != null){
				if("yyyy-MM-dd".equalsIgnoreCase(type)){
					return sdf.format(obj);
				}
				if("yyyy-MM-dd HH:mm:ss".equalsIgnoreCase(type)){
					return sdt.format(obj);
				}
				if("yyyy年MM月dd日".equalsIgnoreCase(type)){
					return ymd.format(obj);
				}
				if("yyyy年MM月dd日 HH:mm:ss".equalsIgnoreCase(type)){
					return ymdhms.format(obj);
				}
			}
		} catch (Exception e) {}
		return null;
	}
	
	public static Object[] returnObjects(Object...objects){
		return objects;
	}
	
	public static void mapToObject(Object target, Map src)
			throws Exception {
		if (src == null || target == null){
			target = null;
			return;
		}

		Iterator<String> it = src.keySet().iterator();
		Method methods[] = target.getClass().getMethods();
		while (it.hasNext()) {
			String key = it.next();
			String value = String.valueOf(src.get(key));
			String methordName = "set" + key.substring(0, 1).toUpperCase() + key.substring(1);
			for (int i = 0; i < methods.length; i++) {
				if (methods[i].getName().equalsIgnoreCase(methordName)) {
					Class type = methods[i].getParameterTypes()[0];
					Object retValue = null;
					retValue = parseParamenter(type, value);
					if ("".equals(retValue) || "null".equals(retValue) || "undefined".equals(retValue)) {
				      retValue = null;
					}
					methods[i].invoke(target, new Object[] { retValue });
					break;
				}
			}
		}
		return;
	}
	
	private static Object parseParamenter(Class type, Object strValue) throws Exception {
		String typeName = type.getName();
		Object ret = null;
		if (type.isPrimitive()) {
			if ("int".equals(typeName))
				type = Class.forName("java.lang.Integer");
			else if ("long".equals(typeName))
				type = Class.forName("java.lang.Long");
			else if ("float".equals(typeName))
				type = Class.forName("java.lang.Float");
			else if ("double".equals(typeName))
				type = Class.forName("java.lang.Double");
			else if ("boolean".equals(typeName))
				type = Class.forName("java.lang.Boolean");
			else if ("char".equals(typeName))
				type = Class.forName("java.lang.Character");
			else if ("byte".equals(typeName))
				type = Class.forName("java.lang.Byte");
		}

		if ("java.util.Date".equals(typeName) || "java.sql.Date".equals(typeName)) {
			String value = strValue.toString().trim();
			if (value.indexOf(":") == -1)
				value += " 00:00:00";
			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			ret = format.parse(value);
		} else if (typeName.equals("java.lang.String")) {
			ret = strValue.toString();
		} else {
			if (typeName.equals("java.sql.Timestamp") && strValue != null) {
				String value = strValue.toString().trim();
				int len = value.trim().length();
				if (len > 7 && len < 11) {
					value = value + " 00:00:00.0";
				} else if (len > 11 && value.indexOf(".") == -1) {
					value = value + ".0";
				}
				strValue = value;
			} else if (typeName.equals("java.sql.Date") && strValue != null) {
				String value = strValue.toString().trim();
				if (value.length() > 10) {
					value = value.substring(0, 10);
				}
				strValue = value;
			}
			Method method = type.getMethod("valueOf", new Class[] { "java.lang.String".getClass() });
			ret = method.invoke(type, new Object[] { strValue.toString() });
		}

		return ret;
	}
	
	public static void objectToMap(Map target, Object src) throws Exception {
		if (src == null || target == null)
			return;
		Method methods[] = src.getClass().getMethods();
		for (int i = 0; i < methods.length; i++) {
			String method = methods[i].getName();
			if (method.startsWith("get") && !"getClass".equals(method)) {
				Class type = methods[i].getReturnType();
				String key = method.replaceFirst("get", "");
				key = key.substring(0, 1).toLowerCase() + key.substring(1);
				Object value = methods[i].invoke(src, (Object[]) null);
				if (value == null)
					target.put(key, "");
				else {
					target.put(key, parseParamenter(type, value));
				}
			}
		}
	}
	
	/**
	 * 通配符匹配
	 * 
	 * @param pattern
	 *            通配符模式
	 * @param str
	 *            待匹配的字符串
	 * @return 匹配成功则返回true，否则返回false
	 */
	public static boolean wildcardMatch(String pattern, String str) {
		int patternLength = pattern.length();
		int strLength = str.length();
		int strIndex = 0;
		char ch;
		for (int patternIndex = 0; patternIndex < patternLength; patternIndex++) {
			ch = pattern.charAt(patternIndex);
			if (ch == '*') {
				// 通配符星号*表示可以匹配任意多个字符
				while (strIndex < strLength) {
					if (wildcardMatch(pattern.substring(patternIndex + 1), str.substring(strIndex))) {
						return true;
					}
					strIndex++;
				}
			} else if (ch == '?') {
				// 通配符问号?表示匹配任意一个字符
				strIndex++;
				if (strIndex > strLength) {
					// 表示str中已经没有字符匹配?了。
					return false;
				}
			} else {
				if ((strIndex >= strLength) || (ch != str.charAt(strIndex))) {
					return false;
				}
				strIndex++;
			}
		}
		return (strIndex == strLength);
	}
}
