package com.example.demo;

import java.io.Serializable;

public class Student implements Serializable {
    public String No;
    public String name;
    public String phone;
    public String QQ;
    public String Email;

    void SetNo(String s){No = s;}
    void Setname(String s){name = s;}
    void Setphone(String s){phone = s;}
    void SetQQ(String s){QQ = s;}
    void SetEmail(String s){Email = s;}
}
