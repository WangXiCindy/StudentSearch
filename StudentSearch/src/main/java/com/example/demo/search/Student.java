package com.example.demo.search;

import java.io.Serializable;

public class Student implements Serializable {
    public String No;
    public String name;
    public String phone;
    public String QQ;
    public String Email;

    public void SetNo(String s){No = s;}
    public void Setname(String s){name = s;}
    public void Setphone(String s){phone = s;}
    public void SetQQ(String s){QQ = s;}
    public void SetEmail(String s){Email = s;}
}
