package com.example.edu.service;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;

import com.example.edu.utils.SaveObjectUtils;
import com.example.edu.utils.Search_Result;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class UserService extends IntentService {
    private static Intent itbind = new Intent("com.deu.bind");
    private static Intent itchange = new Intent("com.discover");
    private static Intent itdelete = new Intent("com.deu.delete");

    // TODO: Rename actions, choose action names that describe tasks that this
    private static final String ACTION_UPLOAD_IMG = "com.example.qq.service.action.uploadphoto";
    private static final String ACTION_BAZ = "com.example.qq.service.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.example.qq.service.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.example.qq.service.extra.PARAM2";
    private File tempFile;
    private String HOME = "http://192.168.191.1:8081/edupatrol/UpLoadImageServlet";
    private String HOME1 = "http://192.168.191.1:8081/edupatrol/BindServlet";
    private String HOME2 = "http://192.168.191.1:8081/edupatrol/ModifyInforServlet";
    private String HOME3 = "http://192.168.191.1:8081/edupatrol/DeleteServlet";
    private String HOME4 = "http://192.168.191.1:8081/edupatrol/ChangePWServlet";
    public UserService() {
        super("UserService");
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getStringExtra("action");
         if (action.equals("bindnumber")){
               String openid = intent.getStringExtra("openid");
               String number = intent.getStringExtra("number");
               binddNumber(openid,number);
            }else if (action.equals("change")){
                Search_Result data = (Search_Result)intent.getBundleExtra("data").getParcelable("data");
                ModifyPersonalInformation(data);
            }else if (action.equals("delete")){
                String record_id = intent.getStringExtra("data");
                      Deleteinformation(record_id);
            }else if (action.equals("changepassword")){
             String oldpassword = intent.getStringExtra("oldpassword");
             String id = intent.getStringExtra("id");
             String newpassword = intent.getStringExtra("newpassword");
             changepassword(id,oldpassword,newpassword);
            }else if (action.equals("reget")){

         }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    public void changepassword(String id,String oldpassword,String newpassword){
        RequestParams params = new RequestParams(HOME4);


        params.addQueryStringParameter("id",id);
        params.addQueryStringParameter("oldpassword",oldpassword);
        params.addQueryStringParameter("newpassword",newpassword);

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                JSONObject root = null;
                try {
                    root = new JSONObject(result);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    int Code =  root.getInt("result");
                    if (Code == 200) {
                        itbind.putExtra("code", "pas200");

                    }else if (Code == 400){
                        itbind.putExtra("code","pas400");}
                    else if(Code == 220){
                        itbind.putExtra("code","pas220");
                    } else if(Code == 230){
                        itbind.putExtra("code","pas230");
                    }


                    getApplicationContext().sendBroadcast(itbind);


                    // int errorCode = root.getInt("error_code");

//                    Intent intent = new Intent("com.bean.action.upload");
//                    intent.putExtra("error_code", errorCode);
//                    if(errorCode==0){
//                        intent.putExtra("photo",root.getString("photo"));}
//                    sendBroadcast(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                itbind.putExtra("code","del400");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                Log.e("bean","aaa=f");
            }
        });

    }


    public void binddNumber(String openid,String number){

        RequestParams params = new RequestParams(HOME1);
        params.setMultipart(true);
        params.addQueryStringParameter("openid",openid);
        params.addQueryStringParameter("number",number);

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                JSONObject res = null;
                try {
                    res = new JSONObject(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                  int Code =  res.getInt("result");
                    if (Code == 200){
                        itbind.putExtra("code","bin200");}
                      else if (Code == 400){
                        itbind.putExtra("code","bin400");}
                    else if (Code == 230){
                        itbind.putExtra("code","bin230");
                    }
                    getApplicationContext().sendBroadcast(itbind);
                    // int errorCode = root.getInt("error_code");

//                    Intent intent = new Intent("com.bean.action.upload");
//                    intent.putExtra("error_code", errorCode);
//                    if(errorCode==0){
//                        intent.putExtra("photo",root.getString("photo"));}
//                    sendBroadcast(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                itbind.putExtra("code","bin400");

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                Log.e("bean","aaa=f");
            }

        });

    }

    public void ModifyPersonalInformation(Search_Result data){
         String stu_number = data.getStu_number();
         String  stu_name = data.getStu_name();
         String class_name = data.getClass_name();
         String message = data.getMessage();
         String mes_time = data.getMes_time();
        String record_id = data.getRecord_id();
        RequestParams params = new RequestParams(HOME2);

        params.addQueryStringParameter("stu_number", stu_number);
        params.addQueryStringParameter("stu_name",stu_name);
        params.addQueryStringParameter("class_name",class_name);
        params.addQueryStringParameter("message",message);
        params.addQueryStringParameter("mes_time",mes_time);
        params.addQueryStringParameter("record_id",record_id);

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                JSONObject root = null;
                try {
                    root = new JSONObject(result);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    int Code =  root.getInt("result");
                    if (Code == 200){
                        itbind.putExtra("code","cha200");
                          itchange.putExtra("code","cha200");}
                    else if (Code == 400)
                        itbind.putExtra("code","cha400");

                    getApplicationContext().sendBroadcast(itbind);
                    getApplicationContext().sendBroadcast(itchange);
                    // int errorCode = root.getInt("error_code");

//                    Intent intent = new Intent("com.bean.action.upload");
//                    intent.putExtra("error_code", errorCode);
//                    if(errorCode==0){
//                        intent.putExtra("photo",root.getString("photo"));}
//                    sendBroadcast(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                itchange.putExtra("code","cha400");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                Log.e("bean","aaa=f");
            }
        });

    }

    public void Deleteinformation(String record_id){
        RequestParams params = new RequestParams(HOME3);


        params.addQueryStringParameter("record_id",record_id);

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                JSONObject root = null;
                try {
                    root = new JSONObject(result);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    int Code =  root.getInt("result");
                    if (Code == 200){
                        itbind.putExtra("code","del200");
                        itchange.putExtra("code","del200");      }
                    else if (Code == 400)
                        itbind.putExtra("code","del400");

                    getApplicationContext().sendBroadcast(itbind);
                    getApplicationContext().sendBroadcast(itchange);

                    // int errorCode = root.getInt("error_code");

//                    Intent intent = new Intent("com.bean.action.upload");
//                    intent.putExtra("error_code", errorCode);
//                    if(errorCode==0){
//                        intent.putExtra("photo",root.getString("photo"));}
//                    sendBroadcast(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                itbind.putExtra("code","del400");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                Log.e("bean","aaa=f");
            }
        });

    }
}
