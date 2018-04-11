package com.example.edu.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 扶摇 on 2017/7/20.
 */

public class FileUtils {
    public static String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".png";
    }
}
