package com.example.edu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.edu.Role.Patroller;
import com.example.edu.Role.Student;
import com.example.edu.Role.Teacher;
import com.example.edu.animation.AniUtils;
import com.example.edu.utils.FileUtils;
import com.example.edu.utils.SaveObjectUtils;
import com.example.edu.utils.ToastUtils;
import com.example.edu.utils.User;
import com.squareup.picasso.Picasso;
import com.tencent.tauth.Tencent;

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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 扶摇 on 2017/6/23.
 */

public class ThirdFragment extends Fragment {
    private Button button1;
    private ImageView myhead;
    private TextView nickname, usergrander, userlocal, usernumber, user_class;
    private Tencent mtencent;
    private static final int CAMERA = 1;
    private static final int PICTURE = 2;
    private Bitmap bitmap;
    private View FragmentView;
    public Handler mHandler;
    private Message msg;
    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;
    private static final int PHOTO_REQUEST_GALLERY = 2;
    private static final int PHOTO_REQUEST_CUT = 3;
    private static int u_id;
    private Student student;
    private Teacher teacher;
    private Patroller patroller;
    private int angle=0;
    private boolean flag = true;


    File tempFile = new File(Environment.getExternalStorageDirectory() + "/Postcard", FileUtils.getPhotoFileName());
    private String HOME = "http://192.168.191.1:8081/edupatrol/UploadImageServlet";
    private String HOME1 = "http://192.168.191.1:8081/edupatrol/GetBindStudent";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (FragmentView == null)
            FragmentView = inflater.inflate(R.layout.activity_personal_center, container, false);
        if (MainActivity.navigation.getVisibility() == View.GONE) {
            MainActivity.navigation.setVisibility(View.VISIBLE);
            MainActivity.navigation.startAnimation(AniUtils.cAnimation());
        }

        myhead = (ImageView) FragmentView.findViewById(R.id.myhead);
        nickname = (TextView) FragmentView.findViewById(R.id.nickname);
        usergrander = (TextView) FragmentView.findViewById(R.id.usergender);
        userlocal = (TextView) FragmentView.findViewById(R.id.userlocal);
        usernumber = (TextView) FragmentView.findViewById(R.id.usernumber);
        user_class = (TextView) FragmentView.findViewById(R.id.user_class);



        Intent i = getActivity().getIntent();
        if (angle<2)
        if (i.hasExtra("qquserinfo")) {
            angle++;
            if (i.hasExtra("openid")) {
                String openid = i.getStringExtra("openid");
                RequestParams params = new RequestParams(HOME1);

                params.addQueryStringParameter("openid", openid);

                x.http().post(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(result);
                        int Code = jsonObject.getInteger("Code");
                        u_id = jsonObject.getInteger("id");
                        if (Code == 400)
                            return;
                        else if (Code == 200) {
                            Student student = new Student();
                            student = jsonObject.getObject("userinfo", Student.class);
                            nickname.setText(student.getStu_name());
                            System.out.println(student.getStu_gender() + "性别");
                            usergrander.setText(student.getStu_gender());
                            userlocal.setText(student.getStu_region());
                            usernumber.setText(student.getStu_number());
                            user_class.setText(student.getStu_class());

                            if (flag)
                                Picasso.with(getActivity()).load(student.getStu_portrait()).into(myhead);

                        }


                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {

                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {
                        Log.e("bean", "aaa=f");
                    }
                });

            }
                Bundle bundle = i.getBundleExtra("qquserinfo");
                User res = (User) bundle.getSerializable("user");
                nickname.setText(res.getNickname());
                usergrander.setText(res.getGender());
                userlocal.setText(res.getCity());
                Picasso.with(getActivity()).load(res.getFigureurl_qq_2()).into(myhead);

        } else {
            int role = SaveObjectUtils.getRoleType(getContext());
            System.out.println(role + "角色");
            switch (role) {
                case 1:
                    student = (Student) SaveObjectUtils.readObject(getContext());

                    nickname.setText(student.getStu_name());
                    System.out.println(student.getStu_gender() + "性别");
                    usergrander.setText(student.getStu_gender());
                    userlocal.setText(student.getStu_region());
                    usernumber.setText(student.getStu_number());
                    user_class.setText(student.getStu_class());

                    if (flag)
                        Picasso.with(getActivity()).load(student.getStu_portrait()).into(myhead);
                    break;
                case 2:
                    teacher = (Teacher) SaveObjectUtils.readObject(getContext());

                    nickname.setText(teacher.getTeacher_name());
                    usernumber.setText(String.valueOf(teacher.getTeacher_id()));
                    user_class.setText(teacher.getClass_name() + " " + teacher.getT_course_name());
                    usergrander.setText(String.valueOf(teacher.getTeacher_gender()));
                    break;
                case 3:
                    patroller = (Patroller) SaveObjectUtils.readObject(getContext());
                    nickname.setText(patroller.getPatroller_name());
                    usernumber.setText(String.valueOf(patroller.getTeacher_id()));
                    usergrander.setText(String.valueOf(patroller.getTeacher_gender()));
                    break;
            }

        }
        if (MainActivity.roleType == 1||MainActivity.roleType == 0)
        myhead.setOnClickListener(hand);


        return FragmentView;

    }

    @Override
    public void onDestroyView() {
        if (null != FragmentView) {
            ((ViewGroup) FragmentView.getParent()).removeView(FragmentView);
        }
        super.onDestroyView();
    }

    View.OnClickListener hand = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.myhead:
                    showDialog();

                    break;

                default:
                    break;
            }
        }
    };


    private void showDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("头像设置")
                .setPositiveButton("拍照", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(tempFile));
                        startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
                    }
                }).setNegativeButton("相册", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
// TODO Auto-generated method stub
                dialog.dismiss();
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        "image/*");
                startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
            }
        }).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
// TODO Auto-generated method stub

        if (resultCode == 0)
            return;
        switch (requestCode) {
            case PHOTO_REQUEST_TAKEPHOTO:
                startPhotoZoom(Uri.fromFile(tempFile), 150);
                break;

            case PHOTO_REQUEST_GALLERY:
                if (data != null)

                    startPhotoZoom(data.getData(), 150);
                break;

            case PHOTO_REQUEST_CUT:
                if (data != null) {
                    setPicToView(data);
                    Bundle bundle = data.getExtras();

                    Bitmap bp = bundle.getParcelable("data");

                    uploadPhoto(bp, student.getLogin_id(), tempFile);

                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void startPhotoZoom(Uri uri, int size) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");

        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        intent.putExtra("outputX", size);
        intent.putExtra("outputY", size);
        intent.putExtra("return-data", true);

        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    @SuppressWarnings("deprecation")
    private void setPicToView(Intent picdata) {

        Bundle bundle = picdata.getExtras();
        Bitmap photo = null;
        if (bundle != null) {
            photo = bundle.getParcelable("data");

            Drawable drawable = new BitmapDrawable( rotateBitmapByDegree(photo,90));
            myhead.setImageDrawable(drawable);
        } else
            return;
        flag = false;
    }


    public Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }


    public void uploadPhoto(Bitmap bitmap, int u_id, File tempFile) {
        //将bitmap转换成File

        if (bitmap == null)
            return;
        try {
            BufferedOutputStream bo = new BufferedOutputStream(new FileOutputStream(tempFile));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bo);
            bo.flush();
            bo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        RequestParams params = new RequestParams(HOME);
        params.setMultipart(true);

        params.addBodyParameter("u_id", String.valueOf(u_id));
        params.addBodyParameter("userphoto", tempFile);

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                Log.e("路径", Environment.getExternalStorageDirectory().toString());

                JSONObject root = null;
                try {
                    root = new JSONObject(result);
                    if (root.getInt("Code") == 200) {
                        ToastUtils.AutoToast(getContext(), "更换头像成功");
                    } else if (root.getInt("Code") == 400) {
                        ToastUtils.AutoToast(getContext(), "更换头像失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("bean", "aaa=e");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


}
