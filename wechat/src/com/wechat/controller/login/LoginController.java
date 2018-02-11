package com.wechat.controller.login;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dee.controller.BaseController;
import com.dee.model.User;
import com.dee.util.BaseUtil;
import com.wechat.service.user.UserService;

public class LoginController extends BaseController {

	@Autowired
	private UserService userService;
	
	@ResponseBody
	@RequestMapping(value = "/login", produces = "application/json")
	public Map<String, Object> doAction(HttpServletRequest request, @RequestBody String data) {
		try {
			setMap(request, data);
			Object forward = null;
			String action = form.get("action").toString();
			
			if("login".equalsIgnoreCase(action)) forward = login();
			else if("register".equalsIgnoreCase(action)) forward = register();
			
			return toJson(forward);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return toJson(SUCCESS);
	}

	private Object login() {
		try {
			String account = BaseUtil.returnString(form.get("name"));
			String password = BaseUtil.returnString(form.get("password"));
			User user = userService.getByAccount(account, password);
			if(user == null){
				throw new Exception("用户不存在");
			}
			httpSession.setAttribute("type", form.get("type"));
			httpSession.setAttribute("user", user);
			httpSession.setAttribute("islogin", "true");
		} catch (Exception e) {
			e.printStackTrace();
			return addMessage(e.getMessage());
		}
		return redirect("common/frame.jsp", "", "", true);
	}
	
	private Object register() {
		try {
			userService.save(form);
		} catch (Exception e) {
			e.printStackTrace();
			return addMessage(e.getMessage());
		}
		return redirect("login/login.jsp", "", "保存成功", true);
	}
}
