package com.dee.util;

public class CheckUtil {

	public static boolean isEmail(Object obj){
		try {
			if(obj != null){
				String email = BaseUtil.returnString(obj);
				if("".equals(email)){
					return false;
				}
				String regex="^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+\\.[a-zA-Z0-9_-]+$";  
		        if (email.matches(regex)){
		        	return true;
		        }
			}
		} catch (Exception e) {}
		return false;
	}
	
	public static boolean isIntegeter(Object obj){
		try {
			Integer.valueOf(obj.toString());
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static boolean isNull(Object obj){
		try {
			if(obj == null){
				return true;
			}
			if(obj instanceof String){
				if("".equals(obj.toString().trim())){
					return true;
				}
				if("undefined".equals(obj.toString().trim())){
					return true;
				}
			}else if(obj instanceof Integer){
				if((Integer)obj == 0){
					return true;
				}
			}
		} catch (Exception e) {
			return true;
		}
		return false;
	}
	
	public static void checkRequired(Object... params) throws Exception {
		for(int i = 0; i < params.length; i++){
			if(params[i] == null){
				throw new Exception(params[i]+"不能为空!");
			}
			else if("".equals(params[i])){
				throw new Exception(params[i]+"不能为空!");
			}
		}
	}
	
}
