package com.example.edu.Role;

import java.io.Serializable;

public class Student implements Serializable {

	private String stu_number;
	private String stu_name;
	private String stu_gender;
	private String stu_class;

	private String stu_region = null;
	private String stu_portrait = null;
	private int login_id ;
	
	
	
	public int getLogin_id() {
		return login_id;
	}
	public void setLogin_id(int login_id) {
		this.login_id = login_id;
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
	public String getStu_gender() {
		return stu_gender;
	}
	public void setStu_gender(String stu_gender) {
		this.stu_gender = stu_gender;
	}
	public String getStu_class() {
		return stu_class;
	}
	public void setStu_class(String stu_class) {
		this.stu_class = stu_class;
	}

	public String getStu_region() {
		return stu_region;
	}
	public void setStu_region(String stu_region) {
		this.stu_region = stu_region;
	}
	public String getStu_portrait() {
		return stu_portrait;
	}
	public void setStu_portrait(String stu_portrait) {
		this.stu_portrait = stu_portrait;
	}



}
