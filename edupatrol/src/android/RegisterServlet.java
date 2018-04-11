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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import DBUtils.ConUtil;
import DBUtils.JSONUtils;
import DBUtils.StatusCode;

/**
 * Servlet implementation class RegisterServlet
 */
@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private String sql ="INSERT INTO tb_login (username,password,role_type) VALUES (?,?,?) " ;
	
	private String sql2 ="SELECT id FROM tb_login where username = ? " ;
	private String sql3 ="INSERT INTO tb_teacher (login_id,teacher_id,teacher_name) VALUES (?,?,?) " ;
	private String sql4 ="INSERT INTO tb_student (login_id,stu_number,stu_name) VALUES (?,?,?) " ;
	private PreparedStatement pstmt,pstmt1;
	private Connection conn;
	private int result  ;
	private int count;
	private ResultSet rs;
    public RegisterServlet() {
        super();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 String regusername =  request.getParameter("regusername");
		 String regpassword =  request.getParameter("regpassword");
		 String regnumber =  request.getParameter("regnumber");
		 String regname =  request.getParameter("regname");
		 String roletype =  request.getParameter("roletype");
		 
	     
	     try {
	    	 
		 
		    conn = ConUtil.getConn();
		    pstmt = conn.prepareStatement(sql);
		    pstmt.setString(1,regusername);
		    pstmt.setString(2,regpassword);
		    pstmt.setInt(3, Integer.parseInt(roletype));
		  
		    
		    try {
		    	 count = pstmt.executeUpdate();
		    	 
				   
			} catch (MySQLIntegrityConstraintViolationException e) {
				 result = StatusCode.REPEATED_ACCOUNTS;
			}
		    
		    if (count > 0) {
		    	
            result = StatusCode.SUCCESS;
             //  pstmt = conn.prepareStatement(sql1);
               pstmt.setString(1, regusername);
               rs  = pstmt.executeQuery();
               if (rs.next()) {
            	  int id= rs.getInt(0);
           // 	  pstmt  = conn.prepareStatement(sql)
            	  
            	   
				
			}
               
			}
		    
		    }catch (Exception e) {
				e.printStackTrace();
			}finally {
				try {
					ConUtil.close(conn, pstmt, null);
					 Map<String, Integer> map = new HashMap<>();
					 map.put("result", result);
					 JSONUtils.responseOutWithJson(response, map);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
				
	    
	
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
