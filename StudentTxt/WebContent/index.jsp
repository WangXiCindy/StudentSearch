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
%>
<%    String s=request.getParameter("realPage");
	  String z=request.getParameter("realSize");
      String keyWord=request.getParameter("query");

if(request.getSession().getAttribute("nowFlag")!=null){
	if(z!=null&&z!=""){
		  lineSize = Integer.parseInt(z);
		  //System.out.println("realSize="+String.valueOf(lineSize));
	}
	else{
		lineSize=(int)request.getSession().getAttribute("nowSize"); 
		//System.out.println("nowSize="+String.valueOf(lineSize));
	}
	if(s!=null&&s!=""){
		  currentPage = Integer.parseInt(s);
		  //System.out.println("realPage="+String.valueOf(currentPage));
	}
	else{
		currentPage = (int)request.getSession().getAttribute("nowPage"); 
		//System.out.println("nowPage="+String.valueOf(currentPage));
	}
}
else{
	request.getSession().setAttribute("nowFlag",1);
	request.getSession().setAttribute("nowPage",1);
	request.getSession().setAttribute("nowSize",5);
}
if("".equals(keyWord)||keyWord==null||"全部".equals(keyWord)||"null".equals(keyWord)){
	all=stud.getIStudentDAOInstance(request).findByPage((currentPage-1), lineSize,request.getSession());
}else{
	//all=stud.getIStudentDAOInstance().findAll(keyWord);
    all=stud.getIStudentDAOInstance(request).findAll(keyWord,(currentPage-1), lineSize,request.getSession());
}

Iterator<Student>it=all.iterator();
pageSize=stud.getIStudentDAOInstance(request).findCount()%lineSize==0?
		stud.getIStudentDAOInstance(request).findCount()/lineSize:stud.getIStudentDAOInstance(request).findCount()/lineSize+1;
if(currentPage>pageSize){
	currentPage=1;
}
//System.out.println("current="+String.valueOf(currentPage)+"Size="+String.valueOf(pageSize));
%>
 
<script language="javaScript">
 function go(num){
	 document.getElementById("realPage").value = num;
	 document.myform.submit();
}
function change(num){
	 document.getElementById("realSize").value = num;
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
<%=(currentPage==1)?"DISABLED":""%>></td>
<td><input type="button" value="上一页" onclick="go(<%=currentPage-1%>)"
<%=(currentPage==1)?"DISABLED":""%>></td>
<td><input type="button" value="下一页" onclick="go(<%=currentPage+1%>)"
<%=(currentPage==pageSize)?"DISABLED":""%>></td>
<td>跳转到<select name="jump" onchange="go(this.value)">
		<%	int nowpageSize=pageSize;
		%>
		<%
			for(int i=1;i<=nowpageSize;i++){
		%>
			<option value="<%=i%>" <%=i==currentPage?"SELECTED":""%>><%=i%></option>
		<%
			}
		%>
		</select>页</td>

<td>显示<select name="chaSize" onchange="change(this.value)">
		<%
			for(int i=5;i<=20;i+=5){
		%>
			<option value="<%=i%>" <%=i==lineSize?"SELECTED":""%>><%=i%></option>
		<%
			}
		%>
		</select>条</td>

 
</tr>
</table>
<input type="hidden" name="realPage" id="realPage">
<input type="hidden" name="realSize" id="realSize">

</form>
</center>
 
</body>
</html>

