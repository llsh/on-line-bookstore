package cn.itcast.goods.category.web.servlet;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.goods.category.domain.Category;
import cn.itcast.goods.category.service.CategoryService;
import cn.itcast.servlet.BaseServlet;

public class CategoryServlet extends BaseServlet {

	private CategoryService categoryService = new CategoryService();
	
	public String findAll(HttpServletRequest req, HttpServletResponse resp){
		/**
		 * 1.�õ����з���
		 * 2.���浽request�У�ת����left.jsp
		 */
		List<Category> parents = categoryService.findAll();
		req.setAttribute("parents", parents);
		return "f:/jsps/left.jsp";
	}

}
