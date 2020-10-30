package com.example.demo;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletContext;

import static java.lang.Math.min;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.chat.*;
import com.example.demo.search.*;

//聊天用包
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;


@Controller
@SpringBootApplication
@Configuration

public class StudentSearchApplication {
	
	static Operater operater = new Operater();
	static FileOperator fileop=new FileOperator();
	
	@RequestMapping("indexs")
	public String indexs(Model model, @RequestParam(defaultValue="2020") String quary,@RequestParam(defaultValue="1") String page,@RequestParam(defaultValue="5") String limit,HttpSession session) throws SQLException {
		
		String name=(String) session.getAttribute("username");

		if(name==null) {
			return "index";
		}
		
		if(limit=="undefined") 
			limit="5";
		ResultSet rs = MySQL.SearchQuary(quary);
        List<String> cache = new LinkedList<>();
        while(rs.next()){
            cache.add(rs.getString("id"));
            cache.add(rs.getString("name"));
            cache.add(rs.getString("phone"));
            cache.add(rs.getString("qq"));
            cache.add(rs.getString("Email"));
        }
        List<Student> allstudent = new ArrayList<>();
        Control control = new Control();
        control.limit = limit;
        control.pages = page;
        control.quary = quary;
        for(int i = (Integer.parseInt(page)-1)*Integer.parseInt(limit) ; i<min(cache.size()/5,(Integer.parseInt(page))*Integer.parseInt(limit));i++){
            Student student = new Student();
            student.SetNo(cache.get(i*5));
            student.Setname(cache.get(i*5+1));
            student.Setphone(cache.get(i*5+2));
            student.SetQQ(cache.get(i*5+3));
            student.SetEmail(cache.get(i*5+4));
            allstudent.add(student);
        }  
        
        model.addAttribute("all", allstudent);
        model.addAttribute("limit",control.limit);
        model.addAttribute("page",control.pages);
        model.addAttribute("quary",control.quary);
        return "indexs";
    }
	
	//查看是否在线
	@RequestMapping(value = "find_online")
	@ResponseBody
	public String find_online(@RequestParam("username") String username,HttpSession session){
		String name="";
		ServletContext application=session.getServletContext();
	    List<String> userOnlineList= (List<String>) application.getAttribute("userOnlineList");
	    if(userOnlineList.contains(username)){
	       name=(String) session.getAttribute("username");
	    }
	    System.out.println(name);
	    return name;
	}
	
	//聊天
	/*@RequestMapping("websocket")
	public String chat(){
		return "websocket";
	}*/
	
    //登录
	@RequestMapping(value = "login_sign")
	@ResponseBody
    public int login_sign(@RequestParam("username") String username,@RequestParam("password") String password,HttpSession session){
		
        ResultSet resultSet = null;
        try{    
            resultSet = MySQL.SQLquary("select * from users where id  = "+username+" and password ="+password);
            if (resultSet != null && resultSet.next()){
            	
            	//把用户数据保存在session域对象中
                session.setAttribute("username", username);
                session.setAttribute("userOnlineListener", new UserOnlineListener(username));
                System.out.println("当前登录用户的sessionId"+session.getId());
            	
                ServletContext application=session.getServletContext();
        	    List<String> userOnlineList= (List<String>) application.getAttribute("userOnlineList");
        	    if(userOnlineList!=null){
        	       System.out.println("在线用户数:"+userOnlineList.size());
        	    }
                
                
            	return 0;
            }
            else return 1;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }
	
	@RequestMapping("SQL")
	public String SQL(Model model,@RequestParam(defaultValue="l") String id,@RequestParam(defaultValue="l") String name,@RequestParam(defaultValue="l") String phone,@RequestParam(defaultValue="l") String qq,@RequestParam(defaultValue="l") String email,HttpServletRequest request,HttpSession session) {
		String username=(String) session.getAttribute("username");

		if(username==null) {
			return "index";
		}
		
		if(id=="l")
			return "indexs";
		
		model.addAttribute("id", id);
        model.addAttribute("name",name);
        model.addAttribute("phone",phone);
        model.addAttribute("qq",qq);
        model.addAttribute("email",email);
        
        System.out.println(request.getQueryString());
        //return request.getRequestURL()+"?"+request.getQueryString();
        return "SQL";
	}
    
	//insert into javafind values(202092177,"张朝亮","15524376928",null,"chaoliang@dlut.edu.cn");
    //增删改功能
	@RequestMapping(value = "Sql_change")
	@ResponseBody
	public int Sql_change(@RequestParam("id") String id,@RequestParam("newname") String name,@RequestParam("newphone") String phone,@RequestParam("newqq") String qq,@RequestParam("newemail") String email) {
		String SQLcmd;
		
		SQLcmd="update javafind set name=\""+name+"\",phone=\""+phone+"\",qq=\""+qq+"\",email=\""+email+"\" where id="+id;
		System.out.println(SQLcmd);
		//String SQLcmd="update javafind set name="+name+",phone="+phone+",qq="+qq+",email="+email+" where id="+id;
        if(id.equals("l")) {
            return 0;
        }
        else{
            if(MySQL.Change(SQLcmd)==false)
            	return -1;
        }
        return 1;
	}
	
	//上传文件功能
    @RequestMapping("upload")
    //@RequestParam(value="file",required=false) MultipartFile file
    public String upload(Model model,HttpServletRequest httpRequest,HttpSession session) {
    	
    	String name=(String) session.getAttribute("username");

		if(name==null) {
			return "index";
		}
    	
    	String state="-2";
    	if (httpRequest instanceof MultipartHttpServletRequest) {
    		MultipartHttpServletRequest request = (MultipartHttpServletRequest) httpRequest;
    		MultipartFile file = (MultipartFile)request.getFile("file");
	        // 获取原始名字
	        String fileName = file.getOriginalFilename();
	        // 文件保存路径
	        //String filePath = "../StudentSearch/WebContent/imgs/";
	        String filePath =request.getServletContext().getRealPath("/imgs/");
	        System.out.println(filePath);
	        // 文件重命名，防止重复
	        fileName = filePath + fileName;
	        // 文件对象
	        File dest = new File(fileName);
	        // 判断路径是否存在，如果不存在则创建
	        if(!dest.getParentFile().exists()) {
	            dest.getParentFile().mkdirs();
	        }
	        try {
	            // 保存到服务器中
	            file.transferTo(dest);
	            state="1";
	            String res=findUser(fileName);
	            model.addAttribute("id",res);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        state="-1";
    	}
    	model.addAttribute("state", state);
    	return "upload";
    }
    
    //聊天
    @Autowired
    private WebSocketOneToOne webSocketOneToOne;
    /**
     * 登录界面直接聊天
     * **/
    @RequestMapping("chat")
    public String user_1(@RequestParam("From") String fromId,@RequestParam("To")String toId,HttpSession session){
    	
    	String name=(String) session.getAttribute("username");

		if(name==null) {
			return "index";
		}
    	
    	System.out.println("fromId");
    	session.setAttribute("fromId", fromId);
    	session.setAttribute("toId",toId);
        return "chat";
    }
    /**
     * 发送消息 消息内容,发送人,接收人,会话标识
     * **/
    @RequestMapping("message")
    public void message(String msg,String from,String to,String socketId){
        webSocketOneToOne.send(msg, from, to, socketId);
 
    }


    public String findUser(String fileName) {
    	
    	
    	String findUsers=FaceSearch.faceSearch(fileName);
    	
        
        //先把String对象转换成json对象
    	JSONObject jsonObject1 = JSONObject.parseObject(findUsers);
    	JSONObject jsonObject2=JSONObject.parseObject(jsonObject1.getString("result"));
    	
    	String user_list=jsonObject2.getString("user_list");
    	JSONArray array = JSON.parseArray(user_list);
    	String res="";
        for (int i = 0; i < 1; i++) {
            //JSONArray中的数据转换为String类型需要在外边加"";不然会报出类型强转异常！
            String str = array.get(i)+"";
            JSONObject object = JSON.parseObject(str);
            res=object.getString("user_id");
            System.out.println(res);
        }
        
    	return res;
    }
    
	public static void main(String[] args) {
		operater.init();
		SpringApplication.run(StudentSearchApplication.class, args);
		System.out.println("进入成功");
	}
}
