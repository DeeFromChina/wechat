package prj.wechat.model.user;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class UserToUserGroup {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "user_id")
	private Integer UserId;
	
	@Column(name = "user_group_id")
	private Integer UserGroupId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return UserId;
	}

	public void setUserId(Integer userId) {
		UserId = userId;
	}

	public Integer getUserGroupId() {
		return UserGroupId;
	}

	public void setUserGroupId(Integer userGroupId) {
		UserGroupId = userGroupId;
	}

}
