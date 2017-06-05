package cn.itcast.goods.user.dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.itcast.goods.user.domain.User;
import cn.itcast.jdbc.TxQueryRunner;

/**
 * 
 * �־ò�
 * @author ����
 *
 */
public class UserDao {

	private QueryRunner qr = new TxQueryRunner();  
	
	/**
	 * �޸��û�����
	 * @param uid
	 * @param password
	 * @throws SQLException
	 */
	public void updatePassword(String uid, String password) throws SQLException{
		String sql = "update t_user set loginpass = ? where uid = ?";
		qr.update(sql, password, uid);
	}
	/**
	 * ����uid������ȷ���û������Ƿ���ȷ
	 * @param uid
	 * @param loginpass
	 * @return
	 * @throws SQLException
	 */
	public boolean findByUidAndPassword(String uid, String loginpass) throws SQLException{
		String sql = "select count(1) from t_user where uid = ? and loginpass = ?";
		Number n = (Number) qr.query(sql, new ScalarHandler(), uid, loginpass);
		return n.intValue() > 0;
	}
	/**
	 * У���û��Ƿ��Ѿ�����
	 * @param loginname
	 * @return
	 * @throws SQLException
	 */
	public boolean ajaxValidateLoginname(String loginname) throws SQLException {
		String sql = "select count(1) from t_user where loginname=?";
		Number cnt = (Number)qr.query(sql, new ScalarHandler(), loginname);
		return cnt.intValue() == 0;
	}
	/**
	 * У��email�Ƿ��Ѿ�����
	 * @param email
	 * @return
	 * @throws SQLException
	 */
	public boolean ajaxValidateEmail(String email) throws SQLException {
		String sql = "select count(1) from t_user where email=?";
		Number cnt = (Number) qr.query(sql, new ScalarHandler(), email);
		return cnt.intValue() == 0;
	}
	
	/**
	 * �����ݿ����ע���û�
	 * @param user
	 * @throws SQLException 
	 */
	public void add(User user) throws SQLException{
		String sql = "insert into t_user values(?,?,?,?,?,?)";
		Object[] params = {user.getUid(), user.getLoginname(), user.getLoginpass(), user.getEmail(), user.isStatus(), user.getActivationCode()};
		qr.update(sql, params);
	}
	
	/**
	 * ���е�½��֤
	 * @param loginname
	 * @param loginpass
	 * @return
	 * @throws SQLException 
	 */
	public User findByLoginnameAndLoginpass(String loginname, String loginpass) throws SQLException{
		String sql = "select * from t_user where loginname = ? and loginpass = ?";
		User user = qr.query(sql, new BeanHandler<User>(User.class), loginname, loginpass);
		return user;
	}
}
