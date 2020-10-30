package ssdut.search;

import javax.servlet.http.HttpServletRequest;

public class StudentDAOFactory {
	public static StudentDAO getIStudentDAOInstance(HttpServletRequest request) throws Exception {
		return new StudentDAOProxy(request);
	}
 
}


