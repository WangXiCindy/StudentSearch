package ssdut.search;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import javax.servlet.http.HttpSession;
 
public class StudentDAOProxy implements StudentDAO {
                //代理类 调用前面的前面的类  ps：什么吊事也不用做，调用就完了
	StudentDAO dao=null;
	
	public StudentDAOProxy(HttpServletRequest request) throws Exception{
		this.dao=new StudentDAOImpl(request);
		
		
	}
	
	public List<Student> findAll() throws Exception {
		List<Student> ss=null;
		ss=this.dao.findAll();
		return ss;
	}
 
	
	public List<Student> findAll(String keyWord) throws Exception {
		List<Student> ss=null;
		ss=this.dao.findAll(keyWord);
		return ss;
	}
	
	
	public List<Student> findAll(String keyWord,int currentPage, int pageSize,HttpSession session) throws Exception {
		List<Student> ss=null;
		ss=this.dao.findAll(keyWord,currentPage,pageSize,session);
		return ss;
	}

	@Override
	public List<Student> findByPage(int currentPage, int pageSize,HttpSession session) throws Exception {
		List<Student> all;
		all=dao.findByPage(currentPage, pageSize,session);
		return all;
	}
 
	@Override
	public int findCount() throws Exception {
		
		return dao.findCount();
	}

	
}


