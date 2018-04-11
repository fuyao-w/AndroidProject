package com.example.edu.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

/**
 * Created by 扶摇 on 2017/6/24.
 */

public class MyexpandableListAdapter extends BaseExpandableListAdapter {
    private Context context=null;
    public String[] Groups = {"好友","同事","黑名单"};
    public String[][] children = {{"詹姆斯","韦德","安东尼","保罗"},{"诺维斯基","大卫·格里芬","库班"}
    ,{"黄牛","骗子","痴呆儿"}};

    public MyexpandableListAdapter(Context context ) {
        this.context = context;
    }

public TextView buildTextView(){
    AbsListView.LayoutParams  param = new AbsListView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,90
    );
    TextView textView = new TextView(this.context);
    textView.setLayoutParams(param);
    textView.setTextSize(20);
    textView.setPadding(90,8,3,3);


        return textView;
}
    @Override
    public int getGroupCount() {
        return Groups.length;
    }

    @Override
    public int getChildrenCount(int i) {
        return this.children[i].length;
    }

    @Override
    public Object getGroup(int i) {
        return this.Groups[i];
    }

    @Override
    public Object getChild(int i, int i1) {
        return this.children[i][i1];
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        TextView textView = buildTextView();
        textView.setText(this.getGroup(i).toString());
        return textView;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        TextView textView = buildTextView();
        textView.setText(getChild(i,i1).toString());
        textView.setPadding(110,8,3,3);
        return textView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
