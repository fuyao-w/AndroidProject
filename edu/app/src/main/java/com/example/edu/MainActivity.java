package com.example.edu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.example.edu.adapter.ViewPageAdapter;
import com.example.edu.animation.AniUtils;
import com.example.edu.service.UserService;

import com.example.edu.utils.SaveObjectUtils;
import com.example.edu.utils.ToastUtils;
import com.example.edu.utils.User;
import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private PopupWindow popWindow;
    private ViewPager viewPager;
    private static long exitTime = 0;
    private List<Fragment> fragments;
    protected static DrawerLayout drawerLayout;
    public static MainActivity mainActivity;
    protected static int roleType;
    public static BottomNavigationView navigation;
    MyBoradcast receiver;
    private EditText number;
    private Fragment currentFragment = new Fragment();
    private DiscoverFragment discoverFragment;
    private Stu_Fragment stu_fragment;
    private Teacher_chart_fragment teacher_chart_fragment;
    private patrollerControl patrollerControl;
    private int id;

    private static FragmentManager fragmentManager;
    private ThirdFragment thirdFragment;
    private Context context;
    private static final String STATE_FRAGMENT_SHOW = "STATE_FRAGMENT_SHOW";
    private LinearLayout drawexit, drawlogout, bindlayout, changelayout, updatelayout;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                   viewPager.setCurrentItem(0);

                    return true;
                case R.id.navigation_dashboard:

                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_notifications:

                    viewPager.setCurrentItem(2);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivity = this;
       // roleType = getIntent().getIntExtra("roleType", 0);
        receiver = new MyBoradcast();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        bindlayout = (LinearLayout) findViewById(R.id.bindlayout);
        changelayout = (LinearLayout) findViewById(R.id.changelayout);
        updatelayout = (LinearLayout) findViewById(R.id.updatelayout);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        fragments = new ArrayList<>();
        fragments.add(new DiscoverFragment());
        int role = getIntent().getIntExtra("roleType", 0);
        roleType = role;
        switch (role) {
            case 1:
                fragments.add(new Stu_Fragment());
                ToastUtils.AutoToast(MainActivity.this,"您是学生用户，欢迎登陆！");
                break;
            case 2:
                fragments.add(new Teacher_chart_fragment());
                ToastUtils.AutoToast(MainActivity.this,"您是教师用户，欢迎登陆！");
                break;
            case 3:
                fragments.add(new patrollerControl());
                ToastUtils.AutoToast(MainActivity.this,"您是巡查员用户，欢迎登陆！");
                break;
            case 0:
                fragments.add(new Stu_Fragment());
                ToastUtils.AutoToast(MainActivity.this,"您是学生用户，欢迎登陆！");
                break;
            default:
                break;
        }


        fragments.add(new ThirdFragment());
        viewPager.setCurrentItem(0);
        viewPager.setAdapter(new ViewPageAdapter(getSupportFragmentManager(), fragments));
        drawexit = (LinearLayout) findViewById(R.id.drawexit);
        drawlogout = (LinearLayout) findViewById(R.id.drawlogout);
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        drawerLayout.addDrawerListener(drawerListener);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                navigation.getMenu().getItem(i).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        drawexit.setOnClickListener(drawlistener);
        drawlogout.setOnClickListener(drawlistener);
        bindlayout.setOnClickListener(drawlistener);
        changelayout.setOnClickListener(drawlistener);
        updatelayout.setOnClickListener(drawlistener);






          id = getIntent().getIntExtra("login_id",0);

    }






    DrawerLayout.DrawerListener drawerListener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {
            View mainFrame = drawerLayout.getChildAt(0);

            // 这个就是隐藏起来的边侧滑菜单栏
            View leftMenu = drawerView;

            addQQStyleSlide(mainFrame, leftMenu, slideOffset);
        }

        @Override
        public void onDrawerOpened(View drawerView) {

        }

        @Override
        public void onDrawerClosed(View drawerView) {

        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    };


    private void addQQStyleSlide(View mainFrame, View leftMenu, float slideOffset) {
        float leftScale = 0.5f + 0.5f * slideOffset;
        float rightScale = 1 - 0.2f * slideOffset;

        ViewHelper.setScaleX(leftMenu, leftScale);
        ViewHelper.setScaleY(leftMenu, leftScale);
        ViewHelper.setAlpha(leftMenu, 0.5f + 0.5f * slideOffset);
        ViewHelper.setTranslationX(mainFrame, leftMenu.getMeasuredWidth() * slideOffset);
        ViewHelper.setPivotX(mainFrame, 0);
        ViewHelper.setPivotY(mainFrame, mainFrame.getMeasuredHeight() / 2);
        mainFrame.invalidate();
        ViewHelper.setScaleX(mainFrame, rightScale);
        ViewHelper.setScaleY(mainFrame, rightScale);

        // 该处主要是为了使背景的颜色渐变过渡。
        getWindow().getDecorView().getBackground().setColorFilter(evaluate(slideOffset, Color.BLACK, Color.TRANSPARENT),
                PorterDuff.Mode.SRC_OVER);
    }

    private Integer evaluate(float fraction, Object startValue, Integer endValue) {
        int startInt = (Integer) startValue;
        int startA = (startInt >> 24) & 0xff;
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;
        int endInt = (Integer) endValue;
        int endA = (endInt >> 24) & 0xff;
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >> 8) & 0xff;
        int endB = endInt & 0xff;
        return (int) ((startA + (int) (fraction * (endA - startA))) << 24)
                | (int) ((startR + (int) (fraction * (endR - startR))) << 16)
                | (int) ((startG + (int) (fraction * (endG - startG))) << 8)
                | (int) ((startB + (int) (fraction * (endB - startB))));
    }


    View.OnClickListener drawlistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.drawexit:
                    MainActivity.mainActivity.finish();
                    break;
                case R.id.drawlogout:
                    Intent intent = new Intent(MainActivity.this, Login.class);
                    intent.putExtra("logout", 2);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.bindlayout:
                    if (roleType == 0)
                    showPopWindow(getApplicationContext(), v);
                    else
                        ToastUtils.AutoToast(MainActivity.this,"当前用户不需要绑定账号");
                    break;
                case R.id.changelayout:
                    if (roleType==0)
                        ToastUtils.AutoToast(MainActivity.this,"当前用户不能修改密码");
                    else
                       showPopWindow1(getApplicationContext(),v);
                    Intent intent1 = new Intent(MainActivity.this,UserService.class);
                    intent1.putExtra("action","reget");
                    MainActivity.this.startService(intent1);

                    break;
                case R.id.updatelayout:
                  Handler handler = new Handler();
                          handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                           ToastUtils.AutoToast(MainActivity.this,"您当前已经是最新版本了");
                        }
                    },1000);
                default:

                    break;

            }

        }
    };







    private void showPopWindow1(final Context context, View parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View vPopWindow = inflater.inflate(R.layout.change_message, null, false);
        //宽300 高300
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        popWindow = new PopupWindow(vPopWindow, width - dip2px(getApplicationContext(), 150), height, true);
        Button sub_pas = (Button) vPopWindow.findViewById(R.id.sub_pas);
        ImageButton binreturn = (ImageButton) vPopWindow.findViewById(R.id.bin_return);
        final EditText  oldpassword = (EditText) vPopWindow.findViewById(R.id.oldpassword);
         final EditText  newpassword = (EditText) vPopWindow.findViewById(R.id.newpassword);
        final EditText newpassword2 = (EditText) vPopWindow.findViewById(R.id.newpassword2);
        binreturn.setOnClickListener(poplistener);

        sub_pas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(oldpassword.getText().toString().equals("")){
                    ToastUtils.AutoToast(context, "请输入原密码");
                }else if (newpassword.getText().toString().equals(newpassword2.getText().toString())) {
                    Intent in = new Intent(MainActivity.this, UserService.class);

                    in.putExtra("oldpassword", oldpassword.getText().toString());

                    in.putExtra("id", String.valueOf(id));
                    in.putExtra("newpassword", newpassword.getText().toString());
                    in.putExtra("action", "changepassword");

                    startService(in);
                } else if (!newpassword.getText().toString().equals(newpassword2.getText().toString())){
                    ToastUtils.AutoToast(context, "两次输入的密码不同");}

            }

        });



        popWindow.showAtLocation(parent, Gravity.TOP, dip2px(getApplicationContext(), 150),
         dip2px(getApplicationContext(), 0));

    }












    private void showPopWindow(final Context context, View parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View vPopWindow = inflater.inflate(R.layout.activity_bind_number, null, false);
        //宽300 高300
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        popWindow = new PopupWindow(vPopWindow, width - dip2px(getApplicationContext(), 150), height, true);
        Button BindButton = (Button) vPopWindow.findViewById(R.id.bind_but);
        ImageButton binreturn = (ImageButton) vPopWindow.findViewById(R.id.bin_return);
        number = (EditText) vPopWindow.findViewById(R.id.bnum);
        binreturn.setOnClickListener(poplistener);

        BindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Object object = SaveObjectUtils.readObject(context);

                if (object.getClass().getName().equals("com.example.edu.utils.User") && object != null) {
                    User user = (User) object;


                    String openid = ((User) object).getOpenid();

                    if (openid.equals("")) {
                        ToastUtils.AutoToast(context, "绑定失败");
                        return;
                    }
                    Intent in = new Intent(MainActivity.this, UserService.class);

                    in.putExtra("openid", openid);
                    in.putExtra("number", number.getText().toString());
                    in.putExtra("action", "bindnumber");

                    startService(in);
                } else
                    ToastUtils.AutoToast(context, "当前用户无法绑定");
            }
        });


        popWindow.showAtLocation(parent, Gravity.TOP, dip2px(getApplicationContext(), 150),
                dip2px(getApplicationContext(), 0));

    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    View.OnClickListener poplistener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.bin_return:
                    popWindow.dismiss();

            }
            // TODO Auto-generated method stub

        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                //弹出提示，可以有多种方式
                ToastUtils.AutoToast(MainActivity.this, "再按一次退出程序");
                exitTime = System.currentTimeMillis();
            } else {
                MainActivity.this.finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }




}

class MyBoradcast extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("com.deu.bind")) {

            String action = intent.getStringExtra("code");
            if (action.equals("cha200")) {
                ToastUtils.AutoToast(context, "修改成功");
            } else if (action.equals("cha400")) {
                ToastUtils.AutoToast(context, "修改失败");
            } else if (action.equals("del200")) {
                ToastUtils.AutoToast(context, "删除成功");
            } else if (action.equals("cha400")) {
                ToastUtils.AutoToast(context, "删除失败");
            } else if (action.equals("bin200")) {
                ToastUtils.AutoToast(context, "绑定成功");
            } else if (action.equals("bin400")) {
                ToastUtils.AutoToast(context, "绑定失败");
            }else if (action.equals("bin230")) {
                    ToastUtils.AutoToast(context, "改账号已经被绑定");
            }else if (action.equals("up200")){
                ToastUtils.AutoToast(context, "更换成功");
            }else if (action.equals("up400")){
                ToastUtils.AutoToast(context, "更换失败");
            } else if (action.equals("pas200")){
                ToastUtils.AutoToast(context, "密码修改成功");
            }else if (action.equals("pas400")){
                ToastUtils.AutoToast(context, "密码修改失败");
            }else if (action.equals("pas220")){
                ToastUtils.AutoToast(context, "原密码错误请重新输入");
            }else if (action.equals("pas230")){
                ToastUtils.AutoToast(context, "新密码不能和原密码相同");
            }

        }


    }


}
