package android;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.record.PageBreakRecord.Break;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sun.corba.se.spi.orbutil.fsm.Guard.Result;

import DBUtils.ConUtil;
import DBUtils.DBUtil;
import DBUtils.JSONUtils;
import DBUtils.StatusCode;


@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String pw;
	private int id;
	private int roletype;
	private int Code;
	private Map<String, Object> map;
	
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	
	@SuppressWarnings("resource")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		     String username = request.getParameter("username");
		     String password = request.getParameter("password");
		     System.out.println("连接成功");
		     String sqllog ="SELECT * FROM tb_login where username = ?";
		     String sqlstu ="SELECT * FROM tb_student where id = ?";
		     String sqltea ="SELECT * FROM tb_teacher WHERE login_id=?";
		     String sqlpat ="SELECT * FROM tb_patroller where login_id = ?";
		     
		     PreparedStatement pstmt = null;
		     ResultSet rs = null;
		     Connection conn=null;
		    
		     
		     try {
		    	 
			 
			    conn = ConUtil.getConn();
			    pstmt = conn.prepareStatement(sqllog);
			    pstmt.setString(1, username);
			    rs = pstmt.executeQuery();
			    if(rs.next()){
			    	pw = rs.getString("password");
			    	System.out.println(pw);
			    	if(pw.equals(password)){
			    	id = rs.getInt("id");
			    
			    	roletype = rs.getInt("role_type");
			    	System.out.println(roletype);
			    	Code = StatusCode.SUCCESS;
			    	
			    	}else {
			    		Code = StatusCode.PASSWORD_IS_FALSE;
			    		map = new HashMap<>();
	    				map.put("Code", Code);
	    				
	    				JSONUtils.responseOutWithJson(response,map);
					}
			    	
			    	switch(roletype){
			    	case 1:
			    	 
			    		pstmt = conn.prepareStatement(sqlstu);
			    		pstmt.setInt(1, id);
			    		 rs = pstmt.executeQuery();
			    	
			    		if (rs.next()) {
			    			Student student = new Student();
			    			student.setStu_number(rs.getString("stu_number"));
			    			
			    			
			    			student.setStu_name(rs.getString("stu_name"));
			    			student.setStu_class(rs.getString("stu_class"));
			    			student.setStu_gender(rs.getString("stu_gender"));
			    			student.setStu_region(rs.getString("stu_region"));
			    			student.setStu_number(rs.getString("stu_number"));
			    			student.setStu_portrait(rs.getString("stu_portrait"));
			    			student.setLogin_id(id);
			    		   
			    			map = new HashMap<>();
			    			map.put("Code", Code);
			    			map.put("id", id);
		    				map.put("roletype", roletype);
			    			map.put("userinfo", student);
			    			
			    			 JSONUtils.responseOutWithJson(response,map);
			    			}else{
			    			
			    				map = new HashMap<>();
			    				map.put("Code", Code);
			    				map.put("id", id);
			    				map.put("roletype", roletype);
			    				JSONUtils.responseOutWithJson(response,map);
				    	}
			    		break;
			    	
			    	
			    	case 2:
			    		System.out.println(id);
			    		pstmt = conn.prepareStatement(sqltea);
			    		pstmt.setString(1, String.valueOf(id));
			    		 rs = pstmt.executeQuery();
			    		if (rs.next()) {
			    			map = new HashMap<>();
			    		Teacher teacher = new Teacher();
			    		teacher.setTeacher_id(rs.getInt("teacher_id"));
			    		teacher.setTeacher_name(rs.getString("teacher_name"));
			    		teacher.setClass_name(rs.getString("class_name"));
			    		teacher.setT_course_name(rs.getString("t_course_name"));
			    		teacher.setLogin_id(id);
			    		teacher.setTeacher_gender(rs.getString("teacher_gender"));
			    		map.put("roletype", roletype);
			    		map.put("Code", Code);
			    		map.put("id", id);
			    		map.put("userinfo", teacher);
		    	
		    			 JSONUtils.responseOutWithJson(response, map);
			    		}
			    		else{
		    				map = new HashMap<>();
		    				map.put("Code", Code);
		    				map.put("id", id);
		    				map.put("roletype", roletype);
		    				
		    				JSONUtils.responseOutWithJson(response,map);
			    	}
		    		break;
			    	case 3:
			    		System.out.println(id);
			    		pstmt = conn.prepareStatement(sqlpat);
			    		pstmt.setInt(1, id);
			    		 rs = pstmt.executeQuery();
			    		if (rs.next()) {
			    			
			    			map = new HashMap<>();
			    			Patroller patroller = new Patroller();
			    			patroller.setPatroller_name(rs.getString("patroller_name"));
			    			patroller.setLogin_id(id);
			    			patroller.setTeacher_id(rs.getInt("teacher_id"));
			    			patroller.setTeacher_gender(rs.getString("teacher_gender"));
			    			
			    			map.put("Code", Code);
			    			map.put("id", id);
			    			map.put("roletype", roletype);
			    			map.put("userinfo", patroller);
			    		 
			    			 JSONUtils.responseOutWithJson(response, map);
				    		}
			    		else{
		    				map = new HashMap<>();
		    				map.put("Code", Code);
		    				map.put("id", id);
		    				map.put("roletype", roletype);
		    				
		    				JSONUtils.responseOutWithJson(response,map);
			    	}
		    		break;
		    		
		    		default:
		    		break;
			    	}
			    	
			    	
		       }else{
		    	   Code = StatusCode.USERNAME_NOT_FOUND;
		    	   map = new HashMap<>();
   				map.put("Code", Code);
   				
   				JSONUtils.responseOutWithJson(response,map);
		       }
			      }catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally {
					try {
						ConUtil.close(conn, pstmt, rs);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
		     
		    
		     
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
