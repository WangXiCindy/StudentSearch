package ssdut.search;

import java.util.List;
 
public class StudentDAOProxy implements StudentDAO {
                //代理类 调用前面的前面的类  ps：什么吊事也不用做，调用就完了
	StudentDAO dao=null;
	
	public StudentDAOProxy() throws Exception{
		this.dao=new StudentDAOImpl();
		
		
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
	
	public List<Student> findAllByPage(String keyWord,int currentPage, int pageSize) throws Exception {
		List<Student> ss=null;
		ss=this.dao.findAllByPage(keyWord,currentPage,pageSize);
		return ss;
	}

	@Override
	public List<Student> findByPage(int currentPage, int pageSize) throws Exception {
		List<Student> all;
		all=dao.findByPage(currentPage, pageSize);
		return all;
	}
 
	@Override
	public int findCount() throws Exception {
		
		return dao.findCount();
	}

	
}


