package ssdut.search;

import java.util.ArrayList;
import java.util.List;

 
public class StudentDAOImpl implements StudentDAO {
 
	private String filePath=null;
	private List<Student> allStudent=null;
	public StudentDAOImpl() throws Exception {
		//通过构造方法获得数据库的连接
		this.filePath="/(Your own folder)你的文件夹为位置/StudentSearch/StudentTxt/WEB-INF/contact/data.txt";
		allStudent=findAll();
	}
 
	@Override
	public List<Student> findAll() throws Exception{
		
		fileDealer deal=new fileDealer();
		List<String> readList=deal.getFileToList(this.filePath);
		List<Student> all=new ArrayList<Student>();
		
		int a=0;
		int b=1;
		int c=2;
		int d=3;
		int e=4;
		Student s=new Student();
		for (int i = 0; i < readList.size(); i++) {
            if(i==a) {
            	s.setId(readList.get(i));
            	a+=6;
            }
            else if(i==b) {
            	s.setName(readList.get(i));
            	b+=6;
            }
            else if(i==c) {
            	s.setPhone(readList.get(i));
            	c+=6;
            }
            else if(i==d) {
            	s.setQQ(readList.get(i));
            	d+=6;
            }
            else if(i==e) {
            	s.setEmail(readList.get(i));
            	e+=6;
            	all.add(s);
            	System.out.println(s.getId()+" "+s.getName()+" "+s.getPhone()+" "+s.getQQ()+" "+s.getEmail());
            	s=new Student();
            }
            else
            	continue;
        }
		return all;
	}
 
	public List<Student> findAll(String keyWord) throws Exception {
		List<Student> res=new ArrayList<Student>();
		for(int i=0;i<this.allStudent.size();i++) {
			Student s=this.allStudent.get(i);
			String id=s.getId();
			String name=s.getName();
			String phone=s.getPhone();
			String qq=s.getQQ();
			String Email=s.getEmail();
			if(id.indexOf(keyWord)!=-1 | name.indexOf(keyWord)!=-1 | phone.indexOf(keyWord)!=-1 | qq.indexOf(keyWord)!=-1 | Email.indexOf(keyWord)!=-1) {
				res.add(s);
			}
		}
		
		return res;
	}
	
	public List<Student> findAllByPage(String keyWord,int currentPage, int lineSize) throws Exception {
		List<Student> res=new ArrayList<Student>();
		System.out.println("currentPage"+String.valueOf(currentPage)+" pageSize"+String.valueOf(lineSize));
		int total=lineSize*(currentPage);
		int nextotal=lineSize*(currentPage+1);
		int now=0;
		for(int i=0;i<this.allStudent.size();i++) {
			Student s=this.allStudent.get(i);
			String id=s.getId();
			String name=s.getName();
			String phone=s.getPhone();
			String qq=s.getQQ();
			String Email=s.getEmail();
			if(id.indexOf(keyWord)!=-1 | name.indexOf(keyWord)!=-1 | phone.indexOf(keyWord)!=-1 | qq.indexOf(keyWord)!=-1 | Email.indexOf(keyWord)!=-1) {
				if(now>=total&&now<nextotal)
					res.add(s);
				else if(now>=nextotal)
					break;
			}
		}
		
		return res;
	}
	
	@Override
	public List<Student> findByPage(int currentPage, int lineSize) throws Exception{
		List<Student> res=new ArrayList<Student>();
		 
		int total=lineSize*(currentPage);
		int nextotal=lineSize*(currentPage+1);
		for(int i=total;i<nextotal&&i<this.allStudent.size();i++) {
			Student s=this.allStudent.get(i);
			res.add(s);
		}
		return res;
	}
 
	@Override
	public int findCount() throws Exception{
		int count =this.allStudent.size();
		
		return count;
	}
}

