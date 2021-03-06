package prj.dee.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import prj.dee.model.User;
import prj.dee.util.BaseUtil;
import prj.dee.util.CheckUtil;
import prj.dee.util.RedisJava;

public class BaseController{

	public HttpSession httpSession;
	public static String FILE_PATH = "";
	public static String CONTENT_PATH = "";
	
	public List<Map<String, Object>> homeList = new ArrayList<Map<String, Object>>();
	public Map<String, Object> form = new HashMap<String, Object>();
	public Map<String, Object> redisMap = new HashMap<String, Object>();
	public User user = null;
	
	protected SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	protected SimpleDateFormat sdt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	protected SimpleDateFormat ymd = new SimpleDateFormat("yyyy年MM月dd日");
	protected SimpleDateFormat ymdhms = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
	
	public static String SUCCESS = "success";
	public static String ERROR = "error";
	public static String SAVEERROR = "保存失败";
	public static String SESSIONERROR = "session数据有误";
	public static String NORMAL = "normal";
	public boolean isMsg = false;
	public String isRedirect = "0";
	
	public Map<String, Object> getForm() {
		return form;
	}

	public void setForm(Map<String, Object> form) {
		this.form = form;
	}
	
	public String setMap(HttpServletRequest request, String data) throws Exception{
		if(request == null){
			return "request error";
		}
		if("".equals(FILE_PATH)){
			CONTENT_PATH = request.getSession().getServletContext().getContextPath().substring(1);
			FILE_PATH = request.getSession().getServletContext().getRealPath("/upload/");
			form.put("FILE_PATH", FILE_PATH);
		}
		Map<String, String[]> map = request.getParameterMap();
		if(map == null){
			return "map error";
		}
		if(map.get("action") == null || map.get("action").length == 0){
			return "action error";
		}
		form.clear();
		for(Map.Entry<String, String[]> entry : map.entrySet()){
			if(entry.getKey() == null){
				return entry.getValue()+" error";
			}
			if(entry.getValue() == null){
				return entry.getKey()+" error";
			}
			if("undefined".equals(entry.getValue()[0].toString().trim())){
				form.put(entry.getKey(), "");
			}
			else if("null".equals(entry.getValue()[0].toString().trim())){
				form.put(entry.getKey(), "");
			}
			form.put(entry.getKey(), entry.getValue()[0]);
		}
		if(data != null){
			putToForm(data);
		}
		httpSession = request.getSession();
		user = (User) httpSession.getAttribute("user");
		form.put("currentUser", user);
		return SUCCESS;
	}
	
	private void sessionToForm(HttpServletRequest request){
		RedisJava.openRedis(redisMap);
	}
	
	public String addMessage(String msg){
		isMsg = true;
		return msg;
	}
	
	public Object[] redirect(String str, Object param, String msg) {
		isRedirect = "2";
		Object[] returnMsg = new Object[3];
		returnMsg[0] = str;
		returnMsg[1] = param;
		returnMsg[2] = msg;
		return returnMsg;
	}
	
	public Object[] redirect(String str, Object param, String msg, boolean isTop) {
		if(isTop){
			isRedirect = "3";
		}else{
			isRedirect = "2";
		}
		Object[] returnMsg = new Object[3];
		returnMsg[0] = str;
		returnMsg[1] = param;
		returnMsg[2] = msg;
		return returnMsg;
	}
	
	public Map<String, Object> toJson(Object obj){
		Map<String, Object> map = new HashMap<String, Object>();
		String status = "1";
		if(obj != null){
			status = isRedirect;
			isRedirect = "0";
		}
		map.put("time", sdt.format(new Date()));
		map.put("status", status);
		if (isMsg) {
			map.put("msg", obj);
		}else {
			map.put("data", obj);
		}
		isMsg = false;
		return map;
	}
	
	public void putToForm(String record){
		Map<String, Object> dataForm = JSON.parseObject(record, new TypeReference<Map<String, Object>>(){});
		form.putAll(dataForm);
	}
	
	public boolean isIntegeter(Object obj){
		return CheckUtil.isIntegeter(obj);
	}
	
	public String returnString(Object obj){
		return BaseUtil.returnString(obj);
	}
	
	public void checkRequired(String... params) throws Exception {
		for(int i = 0; i < params.length; i++){
			if(form.get(params[i]) == null){
				throw new Exception(params[i]+"不能为空!");
			}
			else if("".equals(form.get(params[i]))){
				throw new Exception(params[i]+"不能为空!");
			}
		}
	}
	
}
