package com.example.demo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;

public class Operater {
    LinkedList <String>  data = new LinkedList<>();
    static String filename = "data";

    public void init(){
        try {
            MySQL.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> search(String quary){
        LinkedList<String> return_data = new LinkedList<>();
        int copyindex;
        for(int i=0 ; i< data.size();i++){
            if(data.get(i).indexOf(quary)!=-1){
                copyindex = i/5;
                copyindex*=5;
                for(int j=0;j<5;j++){
                    return_data.add(data.get(copyindex+j));
                }
            }
        }
        return return_data;
    }

    public static void main(String[] a){
        Operater operater = new Operater();
        operater.init();
    }

}
