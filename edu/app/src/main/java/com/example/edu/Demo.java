package com.example.edu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;

import com.example.edu.service.UserService;
import com.example.edu.utils.SaveObjectUtils;
import com.example.edu.utils.ToastUtils;
import com.example.edu.utils.User;

import java.io.File;

/**
 * Created by 扶摇 on 2017/7/13.
 */

public class Demo extends Activity {
    Button load;
    File tempFile = new File(Environment.getExternalStorageDirectory() + "/Postcard", "tgpu.png");
    private String HOME = "http://192.168.191.1:8081/edupatrol/UploadImageServlet";
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//
//        setContentView(R.layout.demo);
//       load = (Button) findViewById(R.id.id_btn1);







    }






}