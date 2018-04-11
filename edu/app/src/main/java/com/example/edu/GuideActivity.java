package com.example.edu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 引导页
 * @author Administrator
 *
 */
public class GuideActivity extends Activity  {

    private ViewPager vp;
    private VPAdapter vpAdapter;
    private List<View> views;

    // 底部小点图片
    private ImageView[] dots;

    // 记录当前选中位置
    private int currentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        SharedPreferences sp = getSharedPreferences("launchCount",Context.MODE_PRIVATE);
        int i = sp.getInt("launchCount",0);
        if (i > 0){
            Intent intent = new Intent(GuideActivity.this,Welcome.class);
            startActivity(intent);
            finish();
        }

        // 初始化页面
        initViews();

        // 初始化底部小点
        initDots();
    }

    private void initViews() {
        LayoutInflater inflater = LayoutInflater.from(this);

        views = new ArrayList<View>();
        // 初始化引导图片列表
        views.add(inflater.inflate(R.layout.what_new_one, null));
        views.add(inflater.inflate(R.layout.what_new_two, null));
        views.add(inflater.inflate(R.layout.what_new_three, null));


        // 初始化Adapter
        vpAdapter = new VPAdapter(views,this);

        vp = (ViewPager) findViewById(R.id.viewpager);
        vp.setAdapter(vpAdapter);
        // 绑定回调
        vp.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setCurrentDot(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initDots() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);

        dots = new ImageView[views.size()];

        // 循环取得小点图片
        for (int i = 0; i < views.size(); i++) {
            dots[i] = (ImageView) ll.getChildAt(i);
            dots[i].setEnabled(true);// 都设为灰色
        }

        currentIndex = 0;
        dots[currentIndex].setEnabled(false);// 设置为白色，即选中状态
    }

    private void setCurrentDot(int position) {
        if (position < 0 || position > views.size() - 1
                || currentIndex == position) {
            return;
        }

        dots[position].setEnabled(false);
        dots[currentIndex].setEnabled(true);

        currentIndex = position;
    }


}

class VPAdapter  extends PagerAdapter {
    private List<View> views;
    private Activity activity;

    private static final String SHAREDPREFERENCES_NAME = "launchCount";

    public VPAdapter(List<View> views, Activity activity) {
        this.views = views;
        this.activity = activity;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView(views.get(position));
    }

    @Override
    public int getCount() {
        if (views != null) {
            return views.size();
        }
        return 0;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ((ViewPager) container).addView(views.get(position), 0);
        if (position == views.size() - 1) {
            ImageView mStartWeiboImageButton = (ImageView) container
                    .findViewById(R.id.iv_start_weibo);
            mStartWeiboImageButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    setGuided();
                    goHome();

                }

            });
        }
        return views.get(position);
    }



    private void goHome() {
        Intent intent = new Intent(activity, Login.class);
        activity.startActivity(intent);
        activity.finish();
    }

    private void setGuided() {
        SharedPreferences preferences = activity.getSharedPreferences(
                SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(SHAREDPREFERENCES_NAME, (preferences.getInt(SHAREDPREFERENCES_NAME, 0)+1));
        editor.commit();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return (arg0 == arg1);
    }





}
