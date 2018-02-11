package com.wechat.service.communicate.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dee.dao.BaseDao;
import com.dee.service.baseData.impl.BaseServiceImpl;
import com.dee.util.CheckUtil;
import com.wechat.model.talk.CommunicateObject;
import com.wechat.service.communicate.CommunicateService;

@Service("communicateService")
public class CommunicateServiceImpl extends BaseServiceImpl implements CommunicateService{

	@Autowired
	private BaseDao basedao;
	
	@Autowired
	public List<Map<String, Object>> query(Map<String, Object> form) throws Exception{
		try {
			Map<String, String> params = new HashMap<String, String>();
			if(!CheckUtil.isNull(form.get("lastTime"))){
				params.put("param", " and o.send_time > " + form.get("lastTime").toString());
			}else if(!CheckUtil.isNull(form.get("sendTime"))){
				params.put("param", " and o.send_time > " + form.get("sendTime").toString());
			}
			basedao.findByQuerySql("queryCommunicateObject", params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void save(CommunicateObject communicateObject) throws Exception {
		basedao.save(communicateObject);
	}

	@Override
	public void update(CommunicateObject communicateObject) throws Exception {
		basedao.update(communicateObject);
	}

	@Override
	public void delete(int id) throws Exception {
		basedao.deleteById(id, CommunicateObject.class);
	}


}
