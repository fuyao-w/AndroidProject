package com.example.edu;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 扶摇 on 2017/6/21.
 */

public class ShareUtils {
 public static   SharedPreferences sp;
public   static   SharedPreferences.Editor editor;

    public Context context;
    ShareUtils(Context context) {
        sp = context.getSharedPreferences("userMessage", context.MODE_PRIVATE);
    }



    public boolean getMessageCheckOn(){
        return sp.getBoolean("ISCHECK", false);
    }
    public boolean getAutoLoginOn(){
        return sp.getBoolean("AUTO_ISCHECK", false);
    }



    public  void  clearMessage(){
        editor = sp.edit();
        editor.clear().commit();
    }
public  void  setMessageCheck(boolean flag1){

    editor = sp.edit();
    if (flag1)
    editor.putBoolean("ISCHECK", true).commit() ;
    else
        editor.putBoolean("ISCHECK", false).commit() ;


}
    public  void  setAutoLogin(boolean flag){

        editor = sp.edit();
        if (flag)
            editor.putBoolean("AUTO_ISCHECK", true).commit() ;
        else
            editor.putBoolean("AUTO_ISCHECK", false).commit() ;


    }


    public void insertUserInfo(String username, String password,int id,String openid) {
        editor = sp.edit();
        editor.putString("username", username);
        editor.putString("password", password);

        editor.putInt("id", id);

        editor.commit();
    }


    public Map<String, Object> getUserInfo() {
        Map<String, Object> map = new HashMap<>();
        map.put("username", sp.getString("username", ""));
        map.put("password", sp.getString("password", ""));
        map.put("id", sp.getInt("id", -1));

        return map;
    }
}
