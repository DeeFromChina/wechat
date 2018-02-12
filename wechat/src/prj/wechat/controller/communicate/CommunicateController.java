package prj.wechat.controller.communicate;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import prj.dee.controller.BaseController;
import prj.dee.model.User;
import prj.dee.util.BaseUtil;
import prj.wechat.model.talk.CommunicateObject;
import prj.wechat.service.communicate.CommunicateService;
import prj.wechat.service.user.UserService;

public class CommunicateController extends BaseController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private CommunicateService communicateService;
	
	@ResponseBody
	@RequestMapping(value = "/talk", produces = "application/json")
	public Map<String, Object> doAction(HttpServletRequest request, @RequestBody String data) {
		try {
			setMap(request, data);
			Object forward = null;
			String action = form.get("action").toString();
			
			if("send".equalsIgnoreCase(action)) forward = send();
			else if("get".equalsIgnoreCase(action)) forward = get();
			else if("query".equalsIgnoreCase(action)) forward = query();
			
			return toJson(forward);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return toJson(SUCCESS);
	}

	private Object send() {
		try {
			CommunicateObject communicateObject = new CommunicateObject();
			BaseUtil.mapToObject(communicateObject, form);
			communicateService.save(communicateObject);
		} catch (Exception e) {
			e.printStackTrace();
			return addMessage(e.getMessage());
		}
		return redirect("common/frame.jsp", "", "", true);
	}
	
	private Object get() {
		try {
			communicateService.query(form);
		} catch (Exception e) {
			e.printStackTrace();
			return addMessage(e.getMessage());
		}
		return redirect("login/login.jsp", "", "����ɹ�", true);
	}
	
	private Object query() {
		try {
			userService.save(form);
		} catch (Exception e) {
			e.printStackTrace();
			return addMessage(e.getMessage());
		}
		return redirect("login/login.jsp", "", "����ɹ�", true);
	}
}
