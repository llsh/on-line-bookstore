package cn.itcast.goods.user;

import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Properties;

import javax.mail.Session;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.itcast.jdbc.TxQueryRunner;
import cn.itcast.mail.Mail;
import cn.itcast.mail.MailUtils;

public class test {
	private static QueryRunner qr = new TxQueryRunner();  
	
	public static void main(String[] args) {
			new test().sendEmail();
	}
	
	public void sendEmail(){
		Properties props = new Properties();
		try {
			props.load(this.getClass().getClassLoader().getResourceAsStream("email_template.properties"));
			String host = props.getProperty("host");
			String username = props.getProperty("username");
			String password = props.getProperty("password");
			String from = props.getProperty("from");
			String to = "820374959@qq.com";
			String subject = props.getProperty("subject");
			//邮件内容，并将用户的激活码填充入占位符
			String content = MessageFormat.format(props.getProperty("content"), "123");
			//发送邮件
			Session session = MailUtils.createSession(host, username, password);
			Mail mail = new Mail(from, to, subject, content);
			MailUtils.send(session, mail);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
