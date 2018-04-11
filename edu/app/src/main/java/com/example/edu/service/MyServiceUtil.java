package com.example.edu.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.edu.utils.SaveObjectUtils;

/**
 * Created by 扶摇 on 2017/7/8.
 */

public class MyServiceUtil extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
System.out.println("服务创建");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        System.out.println("服务销毁");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(final Intent intent,int flags, int startId) {
       Thread thread = new Thread(new Runnable() {
           @Override
           public void run() {
               Bundle bundle= intent.getExtras();
               Object reveive = bundle.get("Role");
               SaveObjectUtils.saveObject(MyServiceUtil.this,reveive);
           }
       });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopSelf();
        return Service.START_NOT_STICKY;
    }
}
