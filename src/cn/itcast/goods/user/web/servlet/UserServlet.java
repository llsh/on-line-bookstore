package cn.itcast.goods.user.web.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.user.domain.User;
import cn.itcast.goods.user.service.UserExcepation;
import cn.itcast.goods.user.service.UserService;
import cn.itcast.servlet.BaseServlet;

/**
 * ���Ʋ�
 * 
 * @author ����
 * 
 */
public class UserServlet extends BaseServlet {

	private UserService userService = new UserService();

	/**
	 * �˳�
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String quit(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		req.getSession().invalidate();
		return "r:/jsps/user/login.jsp";
	}
	/**
	 * �޸�����
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String updatePassword(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		User formUser = CommonUtils.toBean(req.getParameterMap(), User.class);
		User user = (User) req.getSession().getAttribute("sessionUser");
		if (user == null) {
			req.setAttribute("msg", "����û�е�½");
			return "f:/jsp/user/login.jsp";
		}

		try {
			userService.updatePassword(user.getUid(),
					formUser.getNewloginpass(), user.getLoginpass());
			req.setAttribute("msg", "�޸�����ɹ�");
			req.setAttribute("code", "success");
		} catch (UserExcepation e) {
			req.setAttribute("msg", e.getMessage());
			req.setAttribute("user", formUser);
			return "f:/jsps/user/pwd.jsp";
		}
		
		return null;
	}

	/**
	 * ��½����
	 * 
	 * @param req
	 * @param resp
	 * @return
	 */
	public String login(HttpServletRequest req, HttpServletResponse resp) {
		/*
		 * 1.��װ�ɱ�����
		 */
		User fromUser = CommonUtils.toBean(req.getParameterMap(), User.class);
		/*
		 * 2.�Ա�������к�̨У��
		 */
		Map<String, String> errors = validateLogin(fromUser, req.getSession());
		if (errors != null && errors.size() > 0) {
			req.setAttribute("errors", errors);
			req.setAttribute("user", fromUser);
			return "f:/jsps/user/login.jsp";
		}
		/*
		 * 3.����userService��ɵ�½
		 */
		User user = userService.login(fromUser);
		/*
		 * 4.��User����У��
		 */
		if (user == null) {
			req.setAttribute("msg", "�û������������");
			req.setAttribute("user", fromUser);
			return "f:/jsps/user/login.jsp";
		} else {
			if (!user.isStatus()) {
				req.setAttribute("msg", "����û�м��");
				req.setAttribute("user", fromUser);
				return "f:/jsps/user/login.jsp";
			} else {
				// �����û���session
				req.getSession().setAttribute("sessionUser", user);
				// ��ȡ�û������浽cookie��
				String loginname = user.getLoginname();
				try {
					loginname = URLEncoder.encode(loginname, "utf-8");
				} catch (UnsupportedEncodingException e) {
					throw new RuntimeException(e);
				}
				Cookie cookie = new Cookie("loginname", loginname);
				// ����10��
				cookie.setMaxAge(60 * 60 * 24 * 10);
				resp.addCookie(cookie);
				// �ض�����ҳ
				return "r:/index.jsp";
			}
		}

	}

	/**
	 * 
	 * �Զ����½��֤����
	 * 
	 * @param user
	 * @param session
	 * @return
	 */
	private Map<String, String> validateLogin(User user, HttpSession session) {
		Map<String, String> errors = new HashMap<String, String>();
		return errors;
	}

	/**
	 * У���û���
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws IOException
	 */
	public String ajaxValidateLoginname(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
		String loginname = req.getParameter("loginname");
		boolean bool = userService.ajaxValidateLoginname(loginname);
		resp.getWriter().print(bool);
		// ����null����ת��Ҳ���ض���
		return null;
	}

	/**
	 * У����Email
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws IOException
	 */
	public String ajaxValidateEmail(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
		String email = req.getParameter("email");
		boolean bool = userService.ajaxValidateEmail(email);
		resp.getWriter().print(bool);
		// ����null����ת��Ҳ���ض���
		return null;
	}

	/**
	 * ���Դ�Сд��У����֤��
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws IOException
	 */
	public String ajaxValidateCode(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
		String verifyCode = req.getParameter("verifyCode");
		String vCode = (String) req.getSession().getAttribute("vCode");
		boolean bool = verifyCode.equalsIgnoreCase(vCode);
		resp.getWriter().print(bool);
		// ����null����ת��Ҳ���ض���
		return null;
	}

	/**
	 * �û�ע��
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws IOException
	 */
	public String regist(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		/*
		 * 1.��װ�ɱ�����
		 */
		User user = CommonUtils.toBean(req.getParameterMap(), User.class);
		/*
		 * 2.�Ա�������к�̨У��
		 */
		Map<String, String> errors = validateRegist(user, req);
		if (errors != null && errors.size() > 0) {
			req.setAttribute("errors", errors);
			req.setAttribute("user", user);
			return "f:/jsps/user/regist.jsp";
		}
		/*
		 * 3.����userService���ע��
		 */
		userService.regist(user);
		/*
		 * 4.��ת��msg.jspҳ�棬������������
		 */
		req.setAttribute("code", "success");
		req.setAttribute("msg", "��ϲע��ɹ��������ϵ�������м��");
		return "f:/jsps/msg.jsp";
	}

	private Map<String, String> validateRegist(User user, HttpServletRequest req) {
		Map<String, String> errors = new HashMap<String, String>();
		// ��loginname����У��
		String loginname = user.getLoginname();
		if (loginname == null || loginname.isEmpty()) {
			errors.put("loginname", "�û�������Ϊ�գ�");
		} else if (loginname.length() < 3 || loginname.length() > 20) {
			errors.put("loginname", "�û������ȱ�����3~20֮�䣡");
		} else if (!userService.ajaxValidateLoginname(loginname)) {
			errors.put("loginname", "�û����ѱ�ע�����");
		}

		// ��loginpass����У��
		String loginpass = user.getLoginpass();
		if (loginpass == null || loginpass.isEmpty()) {
			errors.put("loginpass", "���벻��Ϊ�գ�");
		} else if (loginpass.length() < 3 || loginpass.length() > 20) {
			errors.put("loginpass", "���볤�ȱ�����3~20֮�䣡");
		}

		// ��ȷ���������У��
		String reloginpass = user.getReloginpass();
		if (reloginpass == null || reloginpass.isEmpty()) {
			errors.put("reloginpass", "ȷ�����벻��Ϊ�գ�");
		} else if (!reloginpass.equalsIgnoreCase(loginpass)) {
			errors.put("reloginpass", "�����������벻һ�£�");
		}

		// ��Email����У��
		String email = user.getEmail();
		if (email == null || email.isEmpty()) {
			errors.put("email", "Email����Ϊ�գ�");
		} else if (!email
				.matches("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{2,3}){1,2})$")) {
			errors.put("email", "�����Email��ʽ��");
		} else if (!userService.ajaxValidateEmail(email)) {
			errors.put("email", "Email�ѱ�ע�����");
		}

		// ����֤�����У��
		String verifyCode = user.getVerifyCode();
		String vCode = (String) req.getSession().getAttribute("vCode");
		if (verifyCode == null || verifyCode.isEmpty()) {
			errors.put("verifyCode", "��֤�벻��Ϊ�գ�");
		} else if (verifyCode.length() != 4) {
			errors.put("verifyCode", "�������֤�룡");
		} else if (!verifyCode.equalsIgnoreCase(vCode)) {
			errors.put("verifyCode", "�������֤�룡");
		}
		return errors;
	}

}
