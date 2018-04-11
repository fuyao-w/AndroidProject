package com.example.edu;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;

import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.example.edu.Role.Statistic;
import com.example.edu.Role.Teacher;
import com.example.edu.animation.AniUtils;
import com.example.edu.utils.SaveObjectUtils;
import com.example.edu.utils.ToastUtils;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static android.R.attr.id;
import static android.R.attr.resource;

public class Teacher_chart_fragment extends Fragment {
    private BarChart barChart;
    private XAxis xAxis;
    List<Statistic> list;
    private List<String> xValues;
    private View FragmentView;
    private List<BarEntry> yValues;
    private static final String Home = "http://192.168.191.1:8081/edupatrol/TeacherStatisticServlet";
    boolean flag =false, isSlither = true;
    private Button qiebutton;
    private int times =0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if ( FragmentView == null )
            FragmentView = inflater.inflate(R.layout.activity_teacher_chart_fragment,container,false);
        barChart = (BarChart) FragmentView.findViewById(R.id.barChart);
        qiebutton = (Button)FragmentView.findViewById(R.id.qiebutton);
        xValues = new ArrayList<String>();
        yValues = new ArrayList<BarEntry>();
        Object o = SaveObjectUtils.readObject(getContext());
        Teacher t=(Teacher) o;
        RequestParams params = new RequestParams(Home);
        params.setMultipart(true);
        params.addQueryStringParameter("class_name", t.getClass_name());


        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(result);

                JSONArray total = jsonObject.getJSONArray("result");
                int i = 0;

                list = new ArrayList<>();
                for (Object object : total) {
                    Statistic sta = new Statistic();
                    com.alibaba.fastjson.JSONObject obj = (com.alibaba.fastjson.JSONObject) object;
                    xValues.add(obj.get("stu_name").toString());
                    yValues.add(new BarEntry(Integer.parseInt(obj.get("total").toString()), i++));

                }





                flag = true;
                setchart(xValues, yValues, true);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ToastUtils.AutoToast(getContext(), "网络连接请求失败");
                Log.e("bean", "aaa=e");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                Log.e("bean", "aaa=f");
            }

        });

      qiebutton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              times++;
//             if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                if (times%2==1){
                    if ( MainActivity.mainActivity.navigation.getVisibility() == View.VISIBLE){
                    }

                  getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

                    MainActivity.mainActivity.navigation.startAnimation(AniUtils.xAnimation());
                    MainActivity.mainActivity.navigation.setVisibility(View.GONE);
                  DiscoverFragment.discoverFragment.relative.setBackgroundResource(R.drawable.tjpu_bgk_land);
              }
              else {
                    if ( MainActivity.mainActivity.navigation.getVisibility() == View.GONE){
                  MainActivity.mainActivity.navigation.startAnimation(AniUtils.cAnimation());
                 MainActivity.mainActivity.navigation.setVisibility(View.VISIBLE);}
                  getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    DiscoverFragment.discoverFragment.relative.setBackgroundResource(R.mipmap.tjpu_bgk);
//if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){

              }
          }
      });
        return FragmentView;
    }

    @Override
    public void onDestroyView() {
        if (null != FragmentView){
            ((ViewGroup)FragmentView.getParent()).removeView(FragmentView);
        }
        super.onDestroyView();
    }

    public void setchart(List<String> xValues, List<BarEntry> yValues, boolean flag) {

        if (flag == false)
            return;
        //1、基本设置
        xAxis = barChart.getXAxis();
        xAxis.setDrawAxisLine(true);
        barChart.setDrawValueAboveBar(false);
        xAxis.setDrawGridLines(true);
        barChart.setDragEnabled(isSlither);
        barChart.setTouchEnabled(isSlither);
        // 是否可以拖拽
        barChart.setDragEnabled(isSlither);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setGridColor(getResources().getColor(R.color.colorAccent)); /// 设置该轴的网格线颜色。
        xAxis.setTextColor(getResources().getColor(R.color.colorAccent));
        barChart.setDrawGridBackground(true); // 是否显示表格颜色
        barChart.getAxisLeft().setDrawAxisLine(true);
        barChart.setTouchEnabled(true); // 设置是否可以触摸
        barChart.setDragEnabled(true);// 是否可以拖拽
        barChart.setScaleEnabled(true);// 是否可以缩放
        barChart.setNoDataTextDescription("没有数据了");
        //2、y轴和比例尺

        barChart.setDescription("学生记录数");// 数据描述

        barChart.getAxisLeft().setEnabled(true);
        barChart.getAxisRight().setEnabled(true);

        Legend legend = barChart.getLegend();//隐藏比例尺
        legend.setEnabled(false);

        // 3、x轴数据,和显示位置
//         xValues = new ArrayList<String>();
//        xValues.add("一季度");
//        xValues.add("二季度");
//        xValues.add("三季度");
//        xValues.add("四季度");

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//数据位于底部


        //4、y轴数据
//         yValues = new ArrayList<BarEntry>();
//        //new BarEntry(20, 0)前面代表数据，后面代码柱状图的位置；
//        yValues.add(new BarEntry(20, 0));
//        yValues.add(new BarEntry(18, 1));
//        yValues.add(new BarEntry(4, 2));
//        yValues.add(new BarEntry(45, 3));

        //5、设置显示的数字为整形
        BarDataSet barDataSet = new BarDataSet(yValues, "");
        barDataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                int n = (int) value;
                return n + "";
            }
        });
        //6、设置柱状图的颜色
//        barDataSet.setColors(new int[]{Color.rgb(104, 202, 37), Color.rgb(192, 32, 32),
//                Color.rgb(34, 129, 197), Color.rgb(175, 175, 175),Color.rgb(175, 175, 175),
//                Color.rgb(175, 175, 175)});
        //7、显示，柱状图的宽度和动画效果
        BarData barData = new BarData(xValues, barDataSet);
        barDataSet.setBarSpacePercent(40f);//值越大，柱状图就越宽度越小；

        barChart.invalidate();
        Matrix mMatrix = new Matrix();
        mMatrix.postScale(2f, 1f);
        barChart.getViewPortHandler().refresh(mMatrix, barChart, false);
        barChart.animateY(3);
        barChart.setData(barData); //

            //当为true时,放大图
            // 为了使 柱状图成为可滑动的,将水平方向 放大 2.5倍



    }




}
