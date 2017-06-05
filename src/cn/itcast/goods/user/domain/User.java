package cn.itcast.goods.user.domain;

/**
 * 实体类
 * @author 刘李
 *
 */
/*
 * 属性：
 * 1.t_user表 将表中数据封装到对象中
 * 2.根据用户需求确定user的字段，将表单数据封装到对象中
 */
public class User {
	private String uid;
	private String loginname;
	
	private String loginpass;
	private String reloginpass;
	private String email;
	private String verifyCode;
	private boolean status;
	private String activationCode;
	private String newloginpass;
	
	public String getNewloginpass() {
		return newloginpass;
	}
	public void setNewloginpass(String newloginpass) {
		this.newloginpass = newloginpass;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String string) {
		this.uid = string;
	}
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public String getLoginpass() {
		return loginpass;
	}
	public void setLoginpass(String loginpass) {
		this.loginpass = loginpass;
	}
	public String getReloginpass() {
		return reloginpass;
	}
	public void setReloginpass(String reloginpass) {
		this.reloginpass = reloginpass;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getVerifyCode() {
		return verifyCode;
	}
	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getActivationCode() {
		return activationCode;
	}
	public void setActivationCode(String activationCode) {
		this.activationCode = activationCode;
	}
	@Override
	public String toString() {
		return "User [uid=" + uid + ", loginname=" + loginname + ", loginpass="
				+ loginpass + ", reloginpass=" + reloginpass + ", email="
				+ email + ", verifyCode=" + verifyCode + ", status=" + status
				+ ", activationCode=" + activationCode + "]";
	}

}
