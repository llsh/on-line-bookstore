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
 * 控制层
 * 
 * @author 刘李
 * 
 */
public class UserServlet extends BaseServlet {

	private UserService userService = new UserService();

	/**
	 * 退出
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
	 * 修改密码
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
			req.setAttribute("msg", "您还没有登陆");
			return "f:/jsp/user/login.jsp";
		}

		try {
			userService.updatePassword(user.getUid(),
					formUser.getNewloginpass(), user.getLoginpass());
			req.setAttribute("msg", "修改密码成功");
			req.setAttribute("code", "success");
		} catch (UserExcepation e) {
			req.setAttribute("msg", e.getMessage());
			req.setAttribute("user", formUser);
			return "f:/jsps/user/pwd.jsp";
		}
		
		return null;
	}

	/**
	 * 登陆方法
	 * 
	 * @param req
	 * @param resp
	 * @return
	 */
	public String login(HttpServletRequest req, HttpServletResponse resp) {
		/*
		 * 1.封装成表单对象
		 */
		User fromUser = CommonUtils.toBean(req.getParameterMap(), User.class);
		/*
		 * 2.对表单对象进行后台校验
		 */
		Map<String, String> errors = validateLogin(fromUser, req.getSession());
		if (errors != null && errors.size() > 0) {
			req.setAttribute("errors", errors);
			req.setAttribute("user", fromUser);
			return "f:/jsps/user/login.jsp";
		}
		/*
		 * 3.调用userService完成登陆
		 */
		User user = userService.login(fromUser);
		/*
		 * 4.对User进行校验
		 */
		if (user == null) {
			req.setAttribute("msg", "用户名或密码错误！");
			req.setAttribute("user", fromUser);
			return "f:/jsps/user/login.jsp";
		} else {
			if (!user.isStatus()) {
				req.setAttribute("msg", "您还没有激活！");
				req.setAttribute("user", fromUser);
				return "f:/jsps/user/login.jsp";
			} else {
				// 保存用户到session
				req.getSession().setAttribute("sessionUser", user);
				// 获取用户名保存到cookie中
				String loginname = user.getLoginname();
				try {
					loginname = URLEncoder.encode(loginname, "utf-8");
				} catch (UnsupportedEncodingException e) {
					throw new RuntimeException(e);
				}
				Cookie cookie = new Cookie("loginname", loginname);
				// 保存10天
				cookie.setMaxAge(60 * 60 * 24 * 10);
				resp.addCookie(cookie);
				// 重定向到主页
				return "r:/index.jsp";
			}
		}

	}

	/**
	 * 
	 * 自定义登陆验证方法
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
	 * 校验用户名
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
		// 返回null，不转发也不重定向
		return null;
	}

	/**
	 * 校验用Email
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
		// 返回null，不转发也不重定向
		return null;
	}

	/**
	 * 忽略大小写的校验验证码
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
		// 返回null，不转发也不重定向
		return null;
	}

	/**
	 * 用户注册
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws IOException
	 */
	public String regist(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		/*
		 * 1.封装成表单对象
		 */
		User user = CommonUtils.toBean(req.getParameterMap(), User.class);
		/*
		 * 2.对表单对象进行后台校验
		 */
		Map<String, String> errors = validateRegist(user, req);
		if (errors != null && errors.size() > 0) {
			req.setAttribute("errors", errors);
			req.setAttribute("user", user);
			return "f:/jsps/user/regist.jsp";
		}
		/*
		 * 3.调用userService完成注册
		 */
		userService.regist(user);
		/*
		 * 4.跳转到msg.jsp页面，并将参数传入
		 */
		req.setAttribute("code", "success");
		req.setAttribute("msg", "恭喜注册成功！请马上到邮箱进行激活！");
		return "f:/jsps/msg.jsp";
	}

	private Map<String, String> validateRegist(User user, HttpServletRequest req) {
		Map<String, String> errors = new HashMap<String, String>();
		// 对loginname进行校验
		String loginname = user.getLoginname();
		if (loginname == null || loginname.isEmpty()) {
			errors.put("loginname", "用户名不能为空！");
		} else if (loginname.length() < 3 || loginname.length() > 20) {
			errors.put("loginname", "用户名长度必须在3~20之间！");
		} else if (!userService.ajaxValidateLoginname(loginname)) {
			errors.put("loginname", "用户名已被注册过！");
		}

		// 对loginpass进行校验
		String loginpass = user.getLoginpass();
		if (loginpass == null || loginpass.isEmpty()) {
			errors.put("loginpass", "密码不能为空！");
		} else if (loginpass.length() < 3 || loginpass.length() > 20) {
			errors.put("loginpass", "密码长度必须在3~20之间！");
		}

		// 对确认密码进行校验
		String reloginpass = user.getReloginpass();
		if (reloginpass == null || reloginpass.isEmpty()) {
			errors.put("reloginpass", "确认密码不能为空！");
		} else if (!reloginpass.equalsIgnoreCase(loginpass)) {
			errors.put("reloginpass", "两次输入密码不一致！");
		}

		// 对Email进行校验
		String email = user.getEmail();
		if (email == null || email.isEmpty()) {
			errors.put("email", "Email不能为空！");
		} else if (!email
				.matches("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{2,3}){1,2})$")) {
			errors.put("email", "错误的Email格式！");
		} else if (!userService.ajaxValidateEmail(email)) {
			errors.put("email", "Email已被注册过！");
		}

		// 对验证码进行校验
		String verifyCode = user.getVerifyCode();
		String vCode = (String) req.getSession().getAttribute("vCode");
		if (verifyCode == null || verifyCode.isEmpty()) {
			errors.put("verifyCode", "验证码不能为空！");
		} else if (verifyCode.length() != 4) {
			errors.put("verifyCode", "错误的验证码！");
		} else if (!verifyCode.equalsIgnoreCase(vCode)) {
			errors.put("verifyCode", "错误的验证码！");
		}
		return errors;
	}

}
