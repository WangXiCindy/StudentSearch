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
int flag=0;				//��ʾ�Ƿ�Ϊ����keywordģʽ
%>
<%    String s=request.getParameter("realPage");
      String keyWord=request.getParameter("query");
if(s!=null){
	  currentPage = Integer.parseInt(s);
}
if("".equals(keyWord)||keyWord==null||"ȫ��".equals(keyWord)||"null".equals(keyWord)){
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
<%=(currentPage==1 | flag==1)?"DISABLED":""%>></td>
<td><input type="button" value="��һҳ" onclick="go(<%=currentPage-1%>)"
<%=(currentPage==1 | flag==1)?"DISABLED":""%>></td>
<td><input type="button" value="��һҳ" onclick="go(<%=currentPage+1%>)"
<%=(currentPage==pageSize | flag==1)?"DISABLED":""%>></td>
<td>��ת��<select name="jump" onchange="go(this.value)">
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
		</select>ҳ</td>
 
 
 
<td><input type="button" value="βҳ" onclick="go(<%=pageSize%>)"
<%=(currentPage==pageSize | flag==1)?"DISABLED":""%>></td>
 
</tr>
</table>
<input type="hidden" name="realPage" id="realPage" value="1">
</form>
</center>
 
</body>
</html>

