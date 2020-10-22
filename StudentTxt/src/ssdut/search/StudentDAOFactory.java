package ssdut.search;

 
public class StudentDAOFactory {
	public static StudentDAO getIStudentDAOInstance() throws Exception {
		return new StudentDAOProxy();
	}
 
}


