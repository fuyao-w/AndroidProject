package com.example.edu;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by 扶摇 on 2017/6/23.
 */

public class PersonalInformation extends Activity {
    Button logout ;
    private static final int CAMERA = 1;
    private static final int PICTURE = 2;
    ImageView myhead;
    TextView touxiang;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.personal_informatiion);
//
//        myhead = (ImageView) findViewById(R.id.myhead);
//touxiang = (TextView) findViewById(R.id.touxiang);
//
//
//
//        myhead.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent picture = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//
//
//
//
//                startActivityForResult(picture,PICTURE);
//
//            }
//        });
//
//        logout = (Button) findViewById(R.id.logout);
//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent it = new Intent(PersonalInformation.this,Login.class);
//                it.putExtra("logout",2);
//                PersonalInformation.this.startActivity(it);
//                MainActivity.mainActivity.finish();
//
//                finish();
//            }
//        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CAMERA && resultCode == Activity.RESULT_OK && null != data) {

            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = this.getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String picturePath = c.getString(columnIndex);
            c.close();
            //获取图片并显示

        }
    }
}
