package com.example.hwang_gyojun.hyu_se;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by VVl-lYSl3eaR on 2015-05-11.
 */
public class HomeList {
    private class HomeData {
        private String name;
        private String grade;
        private String data1;

        HomeData(String a,String b, String c){
            name = a;
            grade = b;
            data1 = c;
        }

        public String getName() {
            return name;
        }

        public String getGrade() {
            return grade;
        }

        public String getData1() {
            return data1;
        }
    }

    ArrayList<HomeData> x;

    HomeList(){
        x = null;
    }

    void addList(String data){
        x.add(new HomeData(data,data,data));
    }

    String[] readElem(int i){
        HomeData d = x.get(i);
        String d1 = d.getData1();
        String d2 = d.getGrade();
        String d3 = d.getName();
        String[] x = {d1,d2,d3};
        return x;
    }


}
