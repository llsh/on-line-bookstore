package cn.itcast.goods.category.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.category.domain.Category;
import cn.itcast.jdbc.TxQueryRunner;

public class CategoryDao {
	private QueryRunner qr = new TxQueryRunner();
	
	/*
	 * ��һ��Map�е�����ӳ�䵽Category��
	 */
	private Category toCategory(Map<String,Object> map) {
		/*
		 * map {cid:xx, cname:xx, pid:xx, desc:xx, orderBy:xx}
		 * Category{cid:xx, cname:xx, parent:(cid=pid), desc:xx}
		 */
		Category category = CommonUtils.toBean(map, Category.class);
		String pid = (String)map.get("pid");
		if(pid != null) {//���������ID��Ϊ�գ�
			/*
			 * ʹ��һ�����������������pid
			 * �ٰѸ��������ø�category
			 */
			Category parent = new Category();
			parent.setCid(pid);
			category.setParent(parent);
		}
		return category;
	}
	
	/*
	 * ���԰Ѷ��Map(List<Map>)ӳ��ɶ��Category(List<Category>)
	 */
	private List<Category> toCategoryList(List<Map<String,Object>> mapList) {
		List<Category> categoryList = new ArrayList<Category>();//����һ���ռ���
		for(Map<String,Object> map : mapList) {//ѭ������ÿ��Map
			Category c = toCategory(map);//��һ��Mapת����һ��Category
			categoryList.add(c);//��ӵ�������
		}
		return categoryList;//���ؼ���
	}
	
	/**
	 * �������з���
	 * @return
	 * @throws SQLException 
	 */
	public List<Category> findAll() throws SQLException {
		/*
		 * 1. ��ѯ������һ������
		 */
		String sql = "select * from t_category where pid is null";
		List<Map<String,Object>> mapList = qr.query(sql, new MapListHandler());
		
		List<Category> parents = toCategoryList(mapList);
		
		/*
		 * 2. ѭ���������е�һ�����࣬Ϊÿ��һ������������Ķ������� 
		 */
		for(Category parent : parents) {
			// ��ѯ����ǰ������������ӷ���
			List<Category> children = findByParent(parent.getCid());
			// ���ø�������
			parent.setChildren(children);
		}
		return parents;
	}
	
	/**
	 * ͨ���������ѯ�ӷ���
	 * @param pid
	 * @return
	 * @throws SQLException 
	 */
	public List<Category> findByParent(String pid) throws SQLException {
		String sql = "select * from t_category where pid=?";
		List<Map<String,Object>> mapList = qr.query(sql, new MapListHandler(), pid);
		return toCategoryList(mapList);
	}
}
