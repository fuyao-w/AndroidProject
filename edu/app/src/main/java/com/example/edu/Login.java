package com.example.edu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.edu.Role.Patroller;
import com.example.edu.Role.Student;
import com.example.edu.Role.Teacher;
import com.example.edu.service.MyServiceUtil;
import com.example.edu.utils.SaveObjectUtils;
import com.example.edu.utils.ToastUtils;
import com.example.edu.utils.User;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.open.utils.HttpUtils;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by 扶摇 on 2017/6/26.
 */

public class Login extends Activity {
    private ImageView logo = null;
    private Button logButton = null;
    private TextView register;
    Thread t;
    private static User user;
    private String username, password;
    private ImageView qqlog;
    private EditText userText, passwordText, regusername, regpassword, regpassword2, regname, regnumber;
    public CheckBox remember, autologin;
    public int[] m = new int[]{R.mipmap.head1, R.mipmap.head2, R.mipmap.head3, R.mipmap.head4, R.mipmap.head5};
    public ShareUtils shareutils;
    private boolean flag;
    private static Tencent mTencent;
    public static String mAppid = "1106184205",openid;
    UserInfo mInfo;
    ProgressBar progressBar;
    View framLayout;
    TextView textView;
    ImageView clearname, clearpasord;
    private RadioGroup radiogroup;
    private RadioButton sturole, tearole;
    private static String Home = "http://192.168.191.1:8081/edupatrol/LoginServlet";
    // private static String Home1 = "http://192.168.191.1:8081/edupatrol/RegisterServlet";
    private static Context diaContext;
    private String roletype, mes;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avtivity_login);
        logo = (ImageView) findViewById(R.id.logo);
        remember = (CheckBox) findViewById(R.id.remember);
        autologin = (CheckBox) findViewById(R.id.autologin);
        logButton = (Button) findViewById(R.id.logButton);
        passwordText = (EditText) findViewById(R.id.passwordText);

        userText = (EditText) findViewById(R.id.userText);
        qqlog = (ImageView) findViewById(R.id.qqlog);
        clearname = (ImageView) findViewById(R.id.clearname);
        clearpasord = (ImageView) findViewById(R.id.clearpassword);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        framLayout = findViewById(R.id.parentLayout);
        textView = (TextView) findViewById(R.id.waittext);
        //register = (TextView) findViewById(R.id.register);
        clearname.setOnClickListener(clearbut);
        clearpasord.setOnClickListener(clearbut);
        diaContext = this;
        if (mTencent == null) {
            mTencent = Tencent.createInstance(mAppid, this);
        }


        shareutils = new ShareUtils(this);
        Intent autoLog = getIntent();
        if (autoLog.getIntExtra("logout", 1) == 2) {
            autologin.setChecked(false);
            shareutils.setAutoLogin(false);
        }
        if (shareutils.getMessageCheckOn()) {
            //设置默认是记录密码状态
            remember.setChecked(true);
            Map<String, Object> map = shareutils.getUserInfo();

            userText.setText(map.get("username").toString());
            passwordText.setText(map.get("password").toString());
            if (shareutils.getAutoLoginOn()) {
                username = map.get("username").toString();
                password = map.get("password").toString();

              
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        eduLoginBypPost(username, password);
                    }
                }).start();
                finish();
            }

            //判断自动登陆多选框状态
        }


        this.userText.setOnFocusChangeListener(changeListener);
        this.passwordText.setOnFocusChangeListener(changeListener);

        this.logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = userText.getText().toString();
                password = passwordText.getText().toString();
                if (username.equals("")) {
                    Toast.makeText(Login.this, "请输入账号", Toast.LENGTH_SHORT).show();

                } else if (password.equals("")) {
                    Toast.makeText(Login.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                } else {

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            eduLoginBypPost(username, password);
                            Message msg = new Message();
                            msg.what = 1;
                            handler.sendMessage(msg);
                        }
                    }).start();

                }


            }


        });

        this.qqlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTencent.login(Login.this, "all", LoginListener);


            }
        });


        this.autologin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (autologin.isChecked()) {

                    shareutils.setAutoLogin(true);
                } else {

                    shareutils.setAutoLogin(false);
                }
            }
        });
        this.remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (remember.isChecked()) {


                    shareutils.setMessageCheck(true);

                } else {


                    shareutils.setMessageCheck(false);
                    shareutils.clearMessage();
                }
            }
        });

//
//        this.register.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                newDialog();
//
//
//
//
//            }//到这里没问题
//        });

    }

    IUiListener LoginListener = new BaseUiListener1() {
        @Override
        protected void doComplete(final org.json.JSONObject values) {//得到用户的ID  和签名等信息  用来得到用户信息

            try {
             openid =  values.getString("openid");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            t = new Thread() {
                @Override
                public void run() {
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);

                    if (mTencent == null) {
                        mTencent = Tencent.createInstance(mAppid, getApplicationContext());
                    }
                    initOpenidAndToken(values);
                    updateUserInfo(values);
                }
            };
            t.start();


        }

    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_LOGIN ||
                requestCode == Constants.REQUEST_APPBAR) {
            Tencent.onActivityResultData(requestCode, resultCode, data, LoginListener);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private class BaseUiListener1 implements IUiListener {
        @Override
        public void onComplete(Object response) {
            if (null == response) {
                Toast.makeText(Login.this, "登录失败", Toast.LENGTH_LONG).show();
                return;
            }
            org.json.JSONObject jsonResponse = (org.json.JSONObject) response;
            if (null != jsonResponse && jsonResponse.length() == 0) {
                Toast.makeText(Login.this, "登录失败", Toast.LENGTH_LONG).show();
                return;
            }

            try {
                mes = jsonResponse.getString("openid");
            } catch (JSONException e) {
                e.printStackTrace();
            }



            Toast.makeText(Login.this, "登录成功", Toast.LENGTH_LONG).show();
            doComplete((org.json.JSONObject) response);
        }

        protected void doComplete(org.json.JSONObject values) {

        }

        @Override
        public void onError(UiError e) {
            Toast.makeText(Login.this, "登录超时，请检查网络", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel() {
            // 运行完成
        }
    }

    public static void initOpenidAndToken(org.json.JSONObject jsonObject) {
        try {
            String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
            String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
            String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                    && !TextUtils.isEmpty(openId)) {
                mTencent.setAccessToken(token, expires);
                mTencent.setOpenId(openId);
            }
        } catch (Exception e) {
        }
    }

    private void updateUserInfo(org.json.JSONObject values) {
        if (mTencent != null && mTencent.isSessionValid()) {
            IUiListener listener = new IUiListener() {
                @Override
                public void onError(UiError uiError) {

                }

                @Override
                public void onCancel() {

                }

                @Override
                public void onComplete(final Object response) {
                    JSONObject r = JSONObject.parseObject(response.toString());
                    if (r.getInteger("ret") >= 0) {
                        try {

                            user = JSON.parseObject(response.toString(), User.class);
                            user.setOpenid(mes);
                            if (user != null) {


                                Intent i = new Intent(Login.this, MainActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("user", user);
                                i.putExtra("qquserinfo", bundle);
                                i.putExtra("openid",openid);
                                toMyService(user);
                                startActivity(i);

                                finish();

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                }
            };
            mInfo = new UserInfo(this, mTencent.getQQToken());
            mInfo.getUserInfo(listener);

        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                framLayout.setAlpha(0.5f);

                textView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        framLayout.setAlpha(1);
                        textView.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);

                    }
                }, 2000);

            }
        }
    };

    public String eduLoginBypPost(final String username, final String password) {


        RequestParams params = new RequestParams(Home);
        params.addQueryStringParameter("username", username);
        params.addQueryStringParameter("password", password);
        params.setConnectTimeout(5000);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                JSONObject jsonObject = JSONObject.parseObject(result);
                int Code = jsonObject.getInteger("Code");

                if (Code == 200) {

                    int roleType = jsonObject.getInteger("roletype");


                    switch (roleType) {
                        case 1:
                            Student student = new Student();
                            student = jsonObject.getObject("userinfo", Student.class);
                            id = student.getLogin_id();
                            SaveObjectUtils.saveRoleType(getApplicationContext(), 1);

                            toMyService(student);
                            //  SaveObjectUtils.saveObject(Login.this, student);
                            break;
                        case 2:
                            Teacher teacher = new Teacher();

                            teacher = jsonObject.getObject("userinfo", Teacher.class);
                            id = teacher.getLogin_id();
                            SaveObjectUtils.saveRoleType(getApplicationContext(), 2);
                            toMyService(teacher);

                            break;
                        case 3:

                            Patroller patroller = new Patroller();
                            SaveObjectUtils.saveRoleType(getApplicationContext(), 3);
                            patroller = jsonObject.getObject("userinfo", Patroller.class);
                            id = patroller.getLogin_id();

                            toMyService(patroller);

                            break;
                        default:
                            break;
                    }

                    Intent it = new Intent(Login.this, MainActivity.class);
                    it.putExtra("roleType", roleType);
                    it.putExtra("login_id", id);

                    startActivity(it);
                    finish();

                    if (remember.isChecked())

                        shareutils.insertUserInfo(username, password, jsonObject.getInteger("id"),"-1");
                    finish();
                } else if (Code == 210) {
                    Toast.makeText(Login.this, "账号错误", Toast.LENGTH_SHORT).show();
                } else if (Code == 220) {
                    Toast.makeText(Login.this, "密码错误", Toast.LENGTH_SHORT).show();
                }


            }


            @Override
            public void onError(Throwable ex, boolean isOnCallback) {


            }

            //主动调用取消请求的回调方法
            @Override
            public void onCancelled(CancelledException cex) {
                ToastUtils.AutoToast(Login.this, "请求超时");
            }

            @Override
            public void onFinished() {
            }
        });
        return null;

    }

    private void toMyService(Serializable o) {
        Intent toService = new Intent(Login.this, MyServiceUtil.class);
        toService.setAction(Intent.ACTION_RUN);
        toService.setPackage(getPackageName());
        Bundle bundle = new Bundle();
        bundle.putSerializable("Role", o);
        toService.putExtras(bundle);
        Login.this.startService(toService);

    }

    View.OnClickListener clearbut = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.clearname:
                    userText.setText("");
                    passwordText.setText("");
                    break;
                case R.id.clearpassword:
                    passwordText.setText("");
                    break;
                default:
                    break;

            }
        }
    };


//public  void newDialog(){
//    LayoutInflater factory = LayoutInflater.from(Login.this);
//    View myView = factory.inflate(R.layout.regester_activity, null);
//    regusername = (EditText) myView.findViewById(R.id.regusername);
//    regpassword = (EditText) myView.findViewById(R.id.regpassword);
//    regpassword2 = (EditText) myView.findViewById(R.id.regpassword2);
//    regname = (EditText) myView.findViewById(R.id.regname);
//    regnumber = (EditText) myView.findViewById(R.id.regnumber);
//    radiogroup = (RadioGroup) myView.findViewById(R.id.radiogroup);
//
//    sturole = (RadioButton) myView.findViewById(R.id.sturole);
//    tearole = (RadioButton) myView.findViewById(R.id.tearole);
//
//    AlertDialog.Builder builder = new AlertDialog.Builder(diaContext);
//
//
//    builder .setTitle("新用户注册");
//    builder .setView(myView);
//    builder   .setNeutralButton("注册",null );
//
//
//    builder .setNegativeButton("取消",new DialogInterface.OnClickListener(){
//        @Override
//        public void onClick(DialogInterface dialog, int which) {
//
//        }
//    });
//
//    final AlertDialog dialog = builder.create();
//    dialog.show();
//    dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener(){
//        @Override
//        public void onClick(View v) {
//
//
//            if (sturole.isChecked()) {
//                roletype = "1";
//            } else if (tearole.isChecked()) {
//                roletype = "2";
//            } else {
//                Toast.makeText(Login.this, "请选择您的角色", Toast.LENGTH_SHORT).show();
//            }
//
//
//            if (regusername.getText().toString().equals("") || regpassword.getText().toString().equals("")
//                    || regpassword2.getText().toString().equals("")) {
//                Toast.makeText(Login.this, "账号密码不能为空", Toast.LENGTH_SHORT).show();
//
//            } else if (regusername.getText().toString().length() < 6) {
//                Toast.makeText(Login.this, "用户名太短，请重新输入", Toast.LENGTH_SHORT).show();
//
//            } else if (regpassword.getText().toString().length() < 6) {
//                Toast.makeText(Login.this, "密码名太短，请重新输入", Toast.LENGTH_SHORT).show();
//            } else if (!regpassword.getText().toString().equals
//                    (regpassword2.getText().toString())) {
//                Toast.makeText(Login.this, "两次输入的密码不一致，亲重新输入", Toast.LENGTH_SHORT).show();
//
//            }else if (regname.getText().toString().equals("")){
//                Toast.makeText(Login.this, "请输入姓名", Toast.LENGTH_SHORT).show();
//            }else if (regnumber.getText().toString().equals("")){
//                Toast.makeText(Login.this, "请输入学号或工号", Toast.LENGTH_SHORT).show();
//            }else {
//                if (roletype != null)
//                    dialog.dismiss();
//              Thread regs = new Thread(new Runnable() {
//                  @Override
//                  public void run() {
//                      RequestParams params = new RequestParams(Home1);
//                      params.addQueryStringParameter("regusername", regusername.getText().toString());
//                      params.addQueryStringParameter("regpassword", regpassword.getText().toString());
//                      params.addQueryStringParameter("regname", regname.getText().toString());
//                      params.addQueryStringParameter("regnumber", regnumber.getText().toString());
//
//                      params.addQueryStringParameter("roletype", roletype);
//                      x.http().get(params, new Callback.CommonCallback<String>() {
//                          @Override
//                          public void onSuccess(String result) {
//                              //解析result
//
//                              JSONObject jsonObject = JSON.parseObject(result);
//                              int res = jsonObject.getInteger("result");
//                              switch (res) {
//                                  case 200:
//                                      Toast.makeText(Login.this, "注册成功", Toast.LENGTH_SHORT).show();
//                                      break;
//
//                                  case 230:
//                                      Toast.makeText(Login.this, "注册失败,用户名重复", Toast.LENGTH_SHORT).show();
//                                      break;
//
//
//                                  default:
//                                      Toast.makeText(Login.this, "连接服务器失败", Toast.LENGTH_LONG).show();
//                              }
//
//
//                          }
//
//                          //请求异常后的回调方法
//                          @Override
//                          public void onError(Throwable ex, boolean isOnCallback) {
//                          }
//
//                          //主动调用取消请求的回调方法
//                          @Override
//                          public void onCancelled(CancelledException cex) {
//                          }
//
//                          @Override
//                          public void onFinished() {
//                          }
//
//                      });
//                  }
//              });
//                 regs.start();
//
//            }
//        }
//    });
//
//
//}

    View.OnFocusChangeListener changeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            switch (v.getId()) {
                case R.id.userText:
                    if (userText.hasFocus())
                        clearname.setVisibility(View.VISIBLE);
                    else
                        clearname.setVisibility(View.INVISIBLE);
                    break;
                case R.id.passwordText:
                    if (passwordText.hasFocus())
                        clearpasord.setVisibility(View.VISIBLE);
                    else
                        clearpasord.setVisibility(View.INVISIBLE);
            }
        }
    };

}

