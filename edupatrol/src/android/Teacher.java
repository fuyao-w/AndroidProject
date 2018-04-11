package android;

public class Teacher {
	private int login_id;
	private int teacher_id;
	private String teacher_name;
	private String teacher_gender;
	public String getTeacher_gender() {
		return teacher_gender;
	}
	public void setTeacher_gender(String teacher_gender) {
		this.teacher_gender = teacher_gender;
	}
	private String class_name;
	private String t_course_name;

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
	public String getTeacher_name() {
		return teacher_name;
	}
	public void setTeacher_name(String teacher_name) {
		this.teacher_name = teacher_name;
	}
	
	public String getClass_name() {
		return class_name;
	}
	public void setClass_name(String class_name) {
		this.class_name = class_name;
	}
	public String getT_course_name() {
		return t_course_name;
	}
	public void setT_course_name(String t_course_name) {
		this.t_course_name = t_course_name;
	}
	
}
