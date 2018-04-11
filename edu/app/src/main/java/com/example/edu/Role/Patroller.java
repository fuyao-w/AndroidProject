package com.example.edu.Role;

import java.io.Serializable;

public class Patroller implements Serializable{
	private String patroller_name;

	private int login_id ;
	private int teacher_id ;
	private String teacher_gender;
	public String getTeacher_gender() {
		return teacher_gender;
	}

	public void setTeacher_gender(String teacher_gender) {
		this.teacher_gender = teacher_gender;
	}

	public int getLogin_id() {
		return login_id;
	}
	public void setLogin_id(int login_id) {
		this.login_id = login_id;
	}



	public int getTeacher_id() {
		return teacher_id;
	}
	public void setTeacher_id(int teacher_id) {
		this.teacher_id = teacher_id;
	}
	public String getPatroller_name() {
		return patroller_name;
	}

	public void setPatroller_name(String patroller_name) {
		this.patroller_name = patroller_name;
	}





}
