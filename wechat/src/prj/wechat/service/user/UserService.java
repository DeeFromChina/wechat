package prj.wechat.service.user;

import java.util.List;
import java.util.Map;

import prj.dee.model.User;
import prj.dee.model.util.CheckBox;
import prj.dee.model.util.Tree;

public interface UserService {

	public User getById(int id) throws Exception;

	public void save(Map<String, Object> map) throws Exception;

	public void update(User user) throws Exception;

	public List<Map<String, Object>> getMap(Map<String, Object> map) throws Exception;

	public void deleteByIds(String ids) throws Exception;

	public void deleteById(String ids) throws Exception;

	public List<Tree> getTree(Map<String, Object> map) throws Exception;

	public List<CheckBox> getCheckBox(Map<String, Object> map) throws Exception;
	
	public User getByAccount(String account, String password) throws Exception;
	
	
}
