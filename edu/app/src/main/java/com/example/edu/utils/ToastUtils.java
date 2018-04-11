package com.example.edu.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by 扶摇 on 2017/7/14.
 */

public class ToastUtils {
 private static   Context context;
 private   static String string;
    public ToastUtils(Context context,String string) {
        this.context = context;
        this.string =string;
    }
  public static void AutoToast(Context context,String string){
        Toast.makeText(context, string,Toast.LENGTH_SHORT).show();
    }
}
