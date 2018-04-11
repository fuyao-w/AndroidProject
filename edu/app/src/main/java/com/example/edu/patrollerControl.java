package com.example.edu;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.edu.animation.AniUtils;
import com.example.edu.utils.ToastUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class patrollerControl extends Fragment {
  private EditText stu_name,stu_class,stu_number,stu_event;
  private TextView timetext;
  private Button databutton,submit;
    private View FragmentView;
  private int tyear,tmonth,tdayOfMonth;
  private Toolbar toolbar;
  private static  String curTime;
  private static  int flag=0;

  private static String Home = "http://192.168.191.1:8081/edupatrol/InsertServlet";
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (FragmentView == null)
            FragmentView = inflater.inflate(R.layout.activity_teacher__control, container, false);
        stu_name = (EditText) FragmentView.findViewById(R.id.stu_name);

        stu_class = (EditText) FragmentView.findViewById(R.id.stu_class);
        stu_number = (EditText) FragmentView.findViewById(R.id.stu_number);
        stu_event = (EditText) FragmentView.findViewById(R.id.stu_event);
        timetext = (TextView) FragmentView.findViewById(R.id.timetext);
        databutton = (Button) FragmentView.findViewById(R.id.databutton);
        submit = (Button) FragmentView.findViewById(R.id.submit);
        toolbar = (Toolbar) FragmentView.findViewById(R.id.toolbar);

        if (MainActivity.navigation.getVisibility() == View.GONE) {
            MainActivity.navigation.setVisibility(View.VISIBLE);
            MainActivity.navigation.startAnimation(AniUtils.cAnimation());
        }
        //((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.top_item);
        setHasOptionsMenu(true);
        SpannableString spanText = new SpannableString("巡查信息录入");
        spanText.setSpan(new ForegroundColorSpan(Color.WHITE), 0,
                spanText.length(),
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        toolbar.setTitle(spanText);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        patrollerControl.this.timetext.setText(getCueTime());

        DataUpdata();

        databutton.setOnClickListener(cListener);

 toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
     @Override
     public boolean onMenuItemClick(MenuItem item) {
         int id = item.getItemId();

        if (id == R.id.clear) {
           stu_name.setText("");
           stu_class.setText("");
           stu_number .setText("");
           stu_event.setText("");
            timetext.setText(curTime);



     }
         return true;
    }

 });


        submit.setOnClickListener(submitListener);




          return FragmentView;

    }

    @Override
    public void onDestroyView() {
        if (null != FragmentView){
            ((ViewGroup)FragmentView.getParent()).removeView(FragmentView);
        }
        super.onDestroyView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.top_item,menu);


    }



    public void  DataUpdata() {

        Pattern pat = Pattern.compile("[0-9]+");
        Matcher mat = pat.matcher(timetext.getText());

        while(mat.find()){

            if ((flag++)==4)
                break;
            else {
                switch (flag) {
                    case 1:
                        patrollerControl.this.tyear = Integer.parseInt(mat.group());
                        break;
                    case 2:
                        patrollerControl.this.tmonth = (Integer.parseInt(mat.group()))-1;
                        break;
                    case 3:
                        patrollerControl.this.tdayOfMonth = Integer.parseInt(mat.group());
                        break;
                    default:
                        break;


                }
            }
            System.out.println(tyear+" "+tmonth+" "+ tdayOfMonth);

        }
        flag=0;

    }

    View.OnClickListener cListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.databutton:
                    DataUpdata();
                    Dialog ddialog = new DatePickerDialog(getContext(),new DatePickerDialog
                            .OnDateSetListener(){

                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                              timetext.setText(year + " 年 " + (month+1) + " 月 " + dayOfMonth + " 日");

                        }
                    },tyear,tmonth,tdayOfMonth);
                    ddialog.show();
                    return;


            }
        }
    };


//    private void judgeProfessAndClass(String string){
//
//        Pattern pro = Pattern.compile("..");
//        Matcher mat = pro.matcher(string);
//        if (mat.find())
//        profess =mat.group();
//        Pattern pclass = Pattern.compile("[0-9][0-9]");
//        Matcher mat1 = pclass.matcher(string);
//        while (mat1.find())
//        {
//            if ((flag1++)==2)
//            {flag1=0;
//                break;}
//
//
//            switch (flag1){
//              case 1:
//                  grade = mat1.group();
//
//              case 2:
//                  sclass  = mat1.group();
//
//
//
//            }
//        }
//
//
//
//    }

private String getCueTime(){
    SimpleDateFormat formatter  =  new SimpleDateFormat("yyyy 年 MM 月 dd 日");
    formatter.setTimeZone(TimeZone.getTimeZone("GMT+8"));
    curTime = formatter.format(new Date());
    return curTime;
}



View.OnClickListener submitListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if (stu_name.getText().toString().equals("") || stu_class.getText().toString().equals("") ||
                stu_number.getText().toString().equals("") || stu_event.getText().toString().equals("")){
            ToastUtils.AutoToast(getContext(),"请把巡查信息填充完整");
            return;
        }
        RequestParams params = new RequestParams(Home);
        params.addQueryStringParameter("stu_name", stu_name.getText().toString());
        params.addQueryStringParameter("stu_number", stu_number.getText().toString());
        params.addQueryStringParameter("class_name", stu_class.getText().toString());
        params.addQueryStringParameter("message", stu_event.getText().toString());

        String timetexts = tyear+"-"+(tmonth+1)+"-"+tdayOfMonth;
        System.out.println(timetexts  +"时间");
        params.addQueryStringParameter("mes_time", timetexts);
        params.addQueryStringParameter("patroller_id","5");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                JSONObject jsonObject = JSONObject.parseObject(result);
                int Code = jsonObject.getInteger("result");
                switch (Code){
                    case 200:
                        Toast.makeText(getContext(), "提交成功", Toast.LENGTH_SHORT).show();
                        break;
                    case 400:
                        Toast.makeText(getContext(), "提交失败", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;


                }



            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println(ex+"  "+isOnCallback);
                Toast.makeText(getContext(), "请求超时，经检查手机网络",Toast.LENGTH_SHORT).show();
            }

            //主动调用取消请求的回调方法
            @Override
            public void onCancelled(CancelledException cex) {
                System.out.println(cex);
            }

            @Override
            public void onFinished() {
            }
        });
     }
 };


}
