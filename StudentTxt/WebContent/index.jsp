<%@ page language="java" contentType="text/html; charset=gbk"
    pageEncoding="gbk"%>
<%@page import="java.util.*" %>
<%@page import="ssdut.search.Student" %>
 
<html>
<head>
<meta charset="gbk">
<title>ѧ����Ϣ��ѯ</title>
</head>
<body>
<%request.setCharacterEncoding("GBK"); %>
<jsp:useBean id="stud"  scope="request" class="ssdut.search.StudentDAOFactory"/>
<%
List<Student> all=null;
Student ss=null;
 
%>
<%int currentPage = 1 ;	// Ϊ��ǰ���ڵ�ҳ��Ĭ���ڵ�1ҳ
int lineSize = 5 ;		// ÿ����ʾ�ļ�¼��
int pageSize = 1 ;		// ��ʾȫ����ҳ����βҳ��
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
if("".equals(keyWord)||keyWord==null||"ȫ��".equals(keyWord)||"null".equals(keyWord)){
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
�����������ַ���в�ѯ<input type="text" name="query" width="5" value="<%=keyWord%>">
<input type="button"   value="��ѯ" onclick="go(1)">
<table border="1" width="60%">
<tr>
<td>ѧ��</td>
<td>����</td>
<td>�绰</td>
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
<td><input type="button" value="��ҳ"  onclick="go(<%=1%>)" 
<%=(currentPage==1)?"DISABLED":""%>></td>
<td><input type="button" value="��һҳ" onclick="go(<%=currentPage-1%>)"
<%=(currentPage==1)?"DISABLED":""%>></td>
<td><input type="button" value="��һҳ" onclick="go(<%=currentPage+1%>)"
<%=(currentPage==pageSize)?"DISABLED":""%>></td>
<td>��ת��<select name="jump" onchange="go(this.value)">
		<%	int nowpageSize=pageSize;
		%>
		<%
			for(int i=1;i<=nowpageSize;i++){
		%>
			<option value="<%=i%>" <%=i==currentPage?"SELECTED":""%>><%=i%></option>
		<%
			}
		%>
		</select>ҳ</td>

<td>��ʾ<select name="chaSize" onchange="change(this.value)">
		<%
			for(int i=5;i<=20;i+=5){
		%>
			<option value="<%=i%>" <%=i==lineSize?"SELECTED":""%>><%=i%></option>
		<%
			}
		%>
		</select>��</td>

 
</tr>
</table>
<input type="hidden" name="realPage" id="realPage">
<input type="hidden" name="realSize" id="realSize">

</form>
</center>
 
</body>
</html>

