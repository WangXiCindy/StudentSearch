<%@ page language="java" contentType="text/html; charset=gbk"
    pageEncoding="gbk"%>
<%@page import="java.util.*" %>
<%@page import="ssdut.search.Student" %>
 
<html>
<head>
<meta charset="gbk">
<title>学生信息查询</title>
</head>
<body>
<%request.setCharacterEncoding("GBK"); %>
<jsp:useBean id="stud"  scope="request" class="ssdut.search.StudentDAOFactory"/>
<%
List<Student> all=null;
Student ss=null;
 
%>
<%int currentPage = 1 ;	// 为当前所在的页，默认在第1页
int lineSize = 5 ;		// 每次显示的记录数
int pageSize = 1 ;		// 表示全部的页数（尾页）
int flag=0;				//表示是否为查找keyword模式
%>
<%    String s=request.getParameter("realPage");
      String keyWord=request.getParameter("query");
if(s!=null){
	  currentPage = Integer.parseInt(s);
}
if("".equals(keyWord)||keyWord==null||"全部".equals(keyWord)||"null".equals(keyWord)){
	flag=0;
	all=stud.getIStudentDAOInstance().findByPage((currentPage-1), lineSize);
}else{
	flag=1;
	all=stud.getIStudentDAOInstance().findAll(keyWord);
}

Iterator<Student>it=all.iterator();
pageSize=stud.getIStudentDAOInstance().findCount()%lineSize==0?
		stud.getIStudentDAOInstance().findCount()/lineSize:stud.getIStudentDAOInstance().findCount()/lineSize+1;
  
%>
 
<script language="javaScript">
 function go(num){
	 document.getElementById("realPage").value = num;
	 document.myform.submit();
}
 
</script>
 
<center>
<form name="myform" action="index.jsp" method="post">
输入姓名或地址进行查询<input type="text" name="query" width="5" value="<%=keyWord%>">
<input type="button"   value="查询" onclick="go(1)">
<table border="1" width="60%">
<tr>
<td>学号</td>
<td>姓名</td>
<td>电话</td>
<td>QQ</td>
<td>Email</td>

</tr>
 
<%while(it.hasNext()){
	ss=it.next();%>
<tr>
<td><%=ss.getId() %></td>
<td><%=ss.getName() %></td>
<td><%=ss.getPhone() %></td>
<td><%=ss.getQQ() %></td>
<td><%=ss.getEmail() %></td>
</tr>
 
<%} %>
<tr>
<td><input type="button" value="首页"  onclick="go(<%=1%>)" 
<%=(currentPage==1 | flag==1)?"DISABLED":""%>></td>
<td><input type="button" value="上一页" onclick="go(<%=currentPage-1%>)"
<%=(currentPage==1 | flag==1)?"DISABLED":""%>></td>
<td><input type="button" value="下一页" onclick="go(<%=currentPage+1%>)"
<%=(currentPage==pageSize | flag==1)?"DISABLED":""%>></td>
<td>跳转到<select name="jump" onchange="go(this.value)">
		<%	int nowpageSize=pageSize;
			if(flag==1){
				nowpageSize=1;
			}
		%>
		<%
			for(int i=1;i<=nowpageSize;i++){
		%>
			<option value="<%=i%>" <%=i==currentPage?"SELECTED":""%>><%=i%></option>
		<%
			}
		%>
		</select>页</td>
 
 
 
<td><input type="button" value="尾页" onclick="go(<%=pageSize%>)"
<%=(currentPage==pageSize | flag==1)?"DISABLED":""%>></td>
 
</tr>
</table>
<input type="hidden" name="realPage" id="realPage" value="1">
</form>
</center>
 
</body>
</html>

