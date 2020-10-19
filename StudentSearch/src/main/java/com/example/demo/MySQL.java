package com.example.demo;

import org.springframework.cache.support.NullValue;

import java.sql.*;
import javax.validation.constraints.Null;

public class MySQL {
    private static Connection connection=null;
    private static Statement stmt = null;

    public static void init() throws SQLException {
    	
        //String driverName="com.mysql.jdbc.Driver";//驱动程序名
    	String driverName="com.mysql.cj.jdbc.Driver";
        String dbURL="jdbc:mysql://localhost:3306/javaee?useSSL=false&serverTimezone=UTC";//数据源
        String Name="java";
        String Pwd="123456";
        try{
            Class.forName(driverName);
            connection= DriverManager.getConnection(dbURL,Name,Pwd);
            System.out.println("连接数据库成功");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("连接失败");
        }
        try {
            stmt = connection.createStatement();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static ResultSet SQLquary(String quary)  {
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery(quary);
            return  rs;
        }
        catch (SQLException e){
            e.printStackTrace();
            return rs;
        }

    }


    public static boolean Change(String sql) {
        try{
            stmt.execute(sql);
            System.out.println(sql);
        }catch (SQLException e){
            System.out.println(e);
            return false;
        }
        return true;
    }

    public static ResultSet SearchQuary(String quary) throws SQLException {
    	String sql;
    	if(quary.equals("all"))
    		sql = "select * from javafind";
    	else {
    		quary = "%"+quary+"%";
    		sql = "select * from javafind where id like \'"+quary+"\' or name like \'"+quary+"\' or phone like \'"+quary+"\' or Email like \'"+quary+"\' or qq like \'"+quary+"\'";
    	}
        ResultSet rs = stmt.executeQuery(sql);
        return  rs;
    }
    public static void deinit(){
        //关闭
        try {
            stmt.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            connection.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
