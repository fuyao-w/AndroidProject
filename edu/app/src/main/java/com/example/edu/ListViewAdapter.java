package com.example.edu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.edu.R;
import com.example.edu.utils.Search_Result;
import com.example.edu.utils.ViewHolder;
import com.example.edu.utils.XCRoundImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by 扶摇 on 2017/7/16.
 */

public class ListViewAdapter extends BaseAdapter {
    public Context context;
    public List<Search_Result> lists;


    ListViewAdapter(Context context, List<Search_Result> lists) {
        this.context = context;
        this.lists = lists;
    }

    public void refresh(List<Search_Result> lists) {
        this.lists = lists;
        this.notifyDataSetChanged();

    }


    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int i) {
        return lists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {


        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.list_demo, null);
            viewHolder.stu_name = (TextView) view.findViewById(R.id.demo_name);
            viewHolder.stu_number = (TextView) view.findViewById(R.id.demo_number);
            viewHolder.class_name = (TextView) view.findViewById(R.id.demo_class);
            viewHolder.mes_time = (TextView) view.findViewById(R.id.demo_time);
           viewHolder.stu_portrait = (XCRoundImageView)view.findViewById(R.id.listtouxiang);
            viewHolder.message = (TextView) view.findViewById(R.id.demo_event);
            view.setTag(viewHolder);






        } else {
            viewHolder = (ViewHolder) view.getTag();

        }

//        ImageView iv_image = (ImageView) cview.findViewById(R.id.demoImage);
//        TextView tv_title = (TextView) cview.findViewById(R.id.demoTextview1);
//        TextView tv_ctime = (TextView) cview.findViewById(R.id.demoTextview2);


        //  x.image().bind(viewHolder._image, lists.get(i).getPicUrl());
        viewHolder.stu_name.setText("姓名 :" + lists.get(i).getStu_name());
        viewHolder.stu_number.setText("学号 :" + lists.get(i).getStu_number());
        viewHolder.class_name.setText("班级 :" + lists.get(i).getClass_name());
        viewHolder.message.setText("记录 :" + lists.get(i).getMessage());
        viewHolder.mes_time.setText("时间 :" + lists.get(i).getMes_time());
        String a = "0";
        if (a.equals(lists.get(i).getStu_portrait()))
            viewHolder.stu_portrait.setImageResource(R.mipmap.tjpu);
        else
        Picasso.with(context).load(lists.get(i).getStu_portrait()).into(viewHolder.stu_portrait);




        return view;
    }


}

