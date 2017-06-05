package cn.itcast.goods.user.service;

import java.io.IOException;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Properties;

import javax.mail.Session;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.user.dao.UserDao;
import cn.itcast.goods.user.domain.User;
import cn.itcast.mail.Mail;
import cn.itcast.mail.MailUtils;

/**
 * 服务层
 * @author 刘李
 *
 */
public class UserService {

	private UserDao userDao = new UserDao();
	
	/**
	 * 修改原密码
	 * @param uid
	 * @param newPass
	 * @param oldPass
	 * @throws UserExcepation
	 */
	public void updatePassword(String uid, String newPass, String oldPass) throws UserExcepation{
		try {
			boolean bool = userDao.findByUidAndPassword(uid, oldPass);
			if(!bool){
				throw new UserExcepation("原密码错误");
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
	}
	/**
	 * 登陆校验
	 * @param loginname
	 * @param loginpass
	 * @return
	 */
	public User login(User user){
		try {
			return userDao.findByLoginnameAndLoginpass(user.getLoginname(), user.getLoginpass());
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 校验用户是否已经存在
	 * @param loginname
	 * @return
	 * @throws SQLException
	 */
	public boolean ajaxValidateLoginname(String loginname) {
		try {
			return userDao.ajaxValidateLoginname(loginname);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 校验email是否已经存在
	 * @param loginname
	 * @return
	 * @throws SQLException
	 */
	public boolean ajaxValidateEmail(String email) {
		try {
			return userDao.ajaxValidateEmail(email);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}		
	}
	/**
	 * 用户注册
	 * @param user
	 */
	public void regist(User user){
		user.setUid(CommonUtils.uuid());
		user.setActivationCode(CommonUtils.uuid() + CommonUtils.uuid());
		user.setStatus(false);

		//调用dao层
		try {
			userDao.add(user);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
		/*
		 * 向注册用户发送邮件
		 */
//		Properties props = new Properties();
//		try {
//			props.load(this.getClass().getClassLoader().getResourceAsStream("email_template.properties"));
//			String host = props.getProperty("host");
//			String username = props.getProperty("username");
//			String password = props.getProperty("password");
//			String from = props.getProperty("from");
//			String to = user.getEmail();
//			String subject = props.getProperty("subject");
//			//邮件内容，并将用户的激活码填充入占位符
//			String content = MessageFormat.format(props.getProperty("content"), user.getActivationCode());
//			//发送邮件
//			Session session = MailUtils.createSession(host, username, password);
//			Mail mail = new Mail(from, to, subject, content);
//			MailUtils.send(session, mail);
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
	}
}
