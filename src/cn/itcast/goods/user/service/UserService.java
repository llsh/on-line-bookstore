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
 * �����
 * @author ����
 *
 */
public class UserService {

	private UserDao userDao = new UserDao();
	
	/**
	 * �޸�ԭ����
	 * @param uid
	 * @param newPass
	 * @param oldPass
	 * @throws UserExcepation
	 */
	public void updatePassword(String uid, String newPass, String oldPass) throws UserExcepation{
		try {
			boolean bool = userDao.findByUidAndPassword(uid, oldPass);
			if(!bool){
				throw new UserExcepation("ԭ�������");
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
	}
	/**
	 * ��½У��
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
	 * У���û��Ƿ��Ѿ�����
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
	 * У��email�Ƿ��Ѿ�����
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
	 * �û�ע��
	 * @param user
	 */
	public void regist(User user){
		user.setUid(CommonUtils.uuid());
		user.setActivationCode(CommonUtils.uuid() + CommonUtils.uuid());
		user.setStatus(false);

		//����dao��
		try {
			userDao.add(user);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
		/*
		 * ��ע���û������ʼ�
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
//			//�ʼ����ݣ������û��ļ����������ռλ��
//			String content = MessageFormat.format(props.getProperty("content"), user.getActivationCode());
//			//�����ʼ�
//			Session session = MailUtils.createSession(host, username, password);
//			Mail mail = new Mail(from, to, subject, content);
//			MailUtils.send(session, mail);
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
	}
}
