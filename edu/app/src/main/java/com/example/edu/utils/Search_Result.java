package com.example.edu.utils;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 扶摇 on 2017/7/10.
 */

public class Search_Result implements Parcelable {
    public String stu_number;
    public String  stu_name;
    public String class_name;
    public String message;
    public String mes_time;
    public String record_id;
    public String stu_portrait;

    public String getStu_portrait() {
        return stu_portrait;
    }

    public void setStu_portrait(String stu_portrait) {
        this.stu_portrait = stu_portrait;
    }

    public String getRecord_id() {
        return record_id;
    }

    public void setRecord_id(String record_id) {
        this.record_id = record_id;
    }



    public String getStu_number() {
        return stu_number;
    }

    public void setStu_number(String stu_number) {
        this.stu_number = stu_number;
    }

    public String getStu_name() {
        return stu_name;
    }

    public void setStu_name(String stu_name) {
        this.stu_name = stu_name;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMes_time() {
        return mes_time;
    }

    public void setMes_time(String mes_time) {
        this.mes_time = mes_time;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.stu_number);
        dest.writeString(this.stu_name);
        dest.writeString(this.class_name);
        dest.writeString(this.message);
        dest.writeString(this.mes_time);
        dest.writeString(this.record_id);
    }

    public Search_Result() {
    }

    protected Search_Result(Parcel in) {
        this.stu_number = in.readString();
        this.stu_name = in.readString();
        this.class_name = in.readString();
        this.message = in.readString();
        this.mes_time = in.readString();
        this.record_id = in.readString();
    }

    public static final Parcelable.Creator<Search_Result> CREATOR = new Parcelable.Creator<Search_Result>() {
        @Override
        public Search_Result createFromParcel(Parcel source) {
            return new Search_Result(source);
        }

        @Override
        public Search_Result[] newArray(int size) {
            return new Search_Result[size];
        }
    };
}
