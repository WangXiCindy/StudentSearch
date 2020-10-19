package com.example.demo;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
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
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import static java.lang.Math.min;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


@Controller
@SpringBootApplication
@Configuration

public class StudentSearchApplication {
	
	static Operater operater = new Operater();
	static FileOperator fileop=new FileOperator();
	
	@RequestMapping("indexs")
	public String indexs(Model model, @RequestParam(defaultValue="2020") String quary,@RequestParam(defaultValue="1") String page,@RequestParam(defaultValue="5") String limit) throws SQLException {
		if(limit=="undefined") 
			limit="5";
		ResultSet rs = MySQL.SearchQuary(quary);
        List<String> cache = new LinkedList<>();
        while(rs.next()){
            cache.add(rs.getString("id"));
            cache.add(rs.getString("name"));
            cache.add(rs.getString("phone"));
            cache.add(rs.getString("Email"));
            cache.add(rs.getString("qq"));
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
	
	@RequestMapping("index")
    public String login(Model model){
        return "index";
    }
	
    //登录
	@RequestMapping(value = "login_sign")
	@ResponseBody
    public int login_sign(@RequestParam("username") String username,@RequestParam("password") String password){
        ResultSet resultSet = null;
        try{    
            resultSet = MySQL.SQLquary("select * from users where username  = "+username+" and password ="+password);
            if (resultSet != null && resultSet.next()){
            	return 0;
            }
            else return 1;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }
    
	//insert into javafind values(202092177,"张朝亮","15524376928",null,"chaoliang@dlut.edu.cn");
    //增删改功能
	@RequestMapping("SQL")
	public String SQL(Model model,@RequestParam(defaultValue="l") String SQLcmd) {
		String state="-2";
        if(SQLcmd.equals("l"))
            state = "0";
        else{
            if(MySQL.Change(SQLcmd)==false)
            	state= "-1";
        }
        model.addAttribute("state", state);
        return "SQL";
	}
	
	//上传文件功能
    @RequestMapping("upload")
    //@RequestParam(value="file",required=false) MultipartFile file
    public String upload(Model model,HttpServletRequest httpRequest) {
    	String state="-2";
    	if (httpRequest instanceof MultipartHttpServletRequest) {
    		MultipartHttpServletRequest request = (MultipartHttpServletRequest) httpRequest;
    		MultipartFile file = (MultipartFile)request.getFile("file");
	        // 获取原始名字
	        String fileName = file.getOriginalFilename();
	        // 文件保存路径
	        String filePath = "/Users/CindyWang/Documents/program_practise/java/j2ee/StudentSearch/WebContent/imgs/";
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
