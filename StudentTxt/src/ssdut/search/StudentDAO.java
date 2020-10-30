package ssdut.search;


import java.util.List;

import javax.servlet.http.HttpSession;

 
public interface StudentDAO {
	
	public List<Student> findAll() throws Exception;
	 
	public List<Student> findAll(String keyWord) throws Exception;//模糊查询方法
	
	public int findCount() throws Exception;

	public List<Student> findByPage(int currentPage,int pageSize,HttpSession session) throws Exception;

	public List<Student> findAll(String keyWord,int currentPage, int pageSize,HttpSession session) throws Exception;

}

