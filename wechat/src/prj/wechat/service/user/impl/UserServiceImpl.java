package prj.wechat.service.user.impl;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import prj.dee.dao.BaseDao;
import prj.dee.model.User;
import prj.dee.model.util.CheckBox;
import prj.dee.model.util.Tree;
import prj.dee.service.baseData.impl.BaseServiceImpl;
import prj.dee.util.BaseUtil;
import prj.dee.util.CheckUtil;
import prj.wechat.service.user.UserService;

@Service("userService")
public class UserServiceImpl extends BaseServiceImpl implements UserService {

	@Autowired
	private BaseDao baseDao;

	@Override
	public User getById(int id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(Map<String, Object> map) throws Exception {
		User user = new User();
		BaseUtil.mapToObject(user, map);
		if((user.getEmail() != null || user.getPhone() != null) && user.getPassword() != null){
			baseDao.save(user);
		}
	}

	@Override
	public void update(User user) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Map<String, Object>> getMap(Map<String, Object> map) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteByIds(String ids) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(String ids) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Tree> getTree(Map<String, Object> map) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CheckBox> getCheckBox(Map<String, Object> map) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User getByAccount(String account, String password) throws Exception {
		StringBuffer hql = new StringBuffer();
		hql.append("FROM User WHERE ");
		if(!CheckUtil.isEmail(account)){
			Integer phone = BaseUtil.returnInt(account);
			if(phone != 0){
				hql.append("phone=" + phone);
			}else{
				throw new Exception("�˺�����");
			}
		}else{
			hql.append("email='" + account + "'");
		}
		User user = null;
		List<User> users = (List<User>) baseDao.findByHql(hql.toString());
		if(users != null && users.size() == 1){
			user = users.get(0);
			if(!password.equals(user.getPassword())){
				throw new Exception("��������");
			}
		}else if(users.size() > 1){
			throw new Exception("�û�����");
		}
		return user;
	}
}
