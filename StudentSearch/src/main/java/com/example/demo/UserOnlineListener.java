package com.example.demo;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import java.util.ArrayList;
import java.util.List;

public class UserOnlineListener implements HttpSessionBindingListener {

    private String userId;

    public UserOnlineListener(String userId){
        this.userId=userId;
    }

    @Override
    public void valueBound(HttpSessionBindingEvent event) {
        HttpSession session=event.getSession();
        ServletContext application=session.getServletContext();
        //从application获取当前登录用户列表
        List<String> userOnlineList= (List<String>) application.getAttribute("userOnlineList");
        //如果该属性不存在,则初始化
        if(userOnlineList==null){
            userOnlineList=new ArrayList<>();
            application.setAttribute("userOnlineList",userOnlineList);
        }
        //将当前用户添加至用户列表
        userOnlineList.add(this.userId);
        System.out.println("session属性绑定=======>"+this.userId);
    }

    @Override
    public void valueUnbound(HttpSessionBindingEvent event) {
        HttpSession session=event.getSession();
        ServletContext application=session.getServletContext();
        //从application获取当前登录用户列表
        List<String> userOnlineList= (List<String>) application.getAttribute("userOnlineList");
        //将该用户从列表中移除
        userOnlineList.remove(this.userId);
        System.out.println("session属性解除绑定=======>"+this.userId);
    }
}



