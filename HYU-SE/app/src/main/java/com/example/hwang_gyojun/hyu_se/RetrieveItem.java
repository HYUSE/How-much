package com.example.hwang_gyojun.hyu_se;

import android.util.Log;
import android.widget.ArrayAdapter;

/**
 * Created by hwang-gyojun on 2015. 5. 10..
 */
public class RetrieveItem {
    private String name;
    private String sub_id;

    public RetrieveItem(String name, String sub_id){
        this.name = name;
        this.sub_id = sub_id;
    }

    public String getName(){return this.name;}
    public String getSubID(){return this.sub_id;}
}