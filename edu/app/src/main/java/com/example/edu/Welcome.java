package com.example.edu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

/**
 * Created by 扶摇 on 2017/6/27.
 */

public class Welcome extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activuty_welcome);
        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {

                        Intent intent = new Intent(Welcome.this, Login.class);
                        Welcome.this.startActivity(intent);
                        finish();
                    }
                }, 1000);

    }
}
