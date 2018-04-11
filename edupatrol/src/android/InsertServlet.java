package android;

import java.io.IOException;
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

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import DBUtils.ConUtil;
import DBUtils.JSONUtils;
import DBUtils.StatusCode;


@WebServlet("/InsertServlet")
public class InsertServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private int result;
    public InsertServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		 String astu_name = new String(request.getParameter("stu_name").getBytes("iso-8859-1"),"utf-8");
		 String astu_number = new String(request.getParameter("stu_number").getBytes("iso-8859-1"),"utf-8");
		 String aclass_name = new String(request.getParameter("class_name").getBytes("iso-8859-1"),"utf-8");
		 String amessage = new String(request.getParameter("message").getBytes("iso-8859-1"),"utf-8");
		 String ames_time = new String(request.getParameter("mes_time").getBytes("iso-8859-1"),"utf-8");
	     int apatroller_id =Integer.parseInt(request.getParameter("patroller_id")) ;
	     String astu_portrait = null;
	     String sql1 = "SELECT stu_portrait FROM tb_student WHERE stu_number = ?";

	     String sql = "INSERT INTO tb_record ( patroller_id,stu_name,stu_number,class_name,message,mes_time,stu_portrait) VALUES "
	     		+ "(?,?,?,?,?,?,?)";
	     PreparedStatement pstmt = null;
	     ResultSet rs= null;
	     int count = 0;
	     Connection conn=null;
	     try {
	    	  conn = ConUtil.getConn();
	    	  pstmt = conn.prepareStatement(sql1);
	    	  pstmt.setString(1, astu_number);
	    	  rs = pstmt.executeQuery();
	    	  if(rs.next()){
	    		   astu_portrait = rs.getString("stu_portrait");
	    		  System.out.println(astu_portrait);
	    	  }
	    	  
	    	  
			    pstmt = conn.prepareStatement(sql);
			    pstmt.setInt(1, apatroller_id);
			    pstmt.setString(2, astu_name);
			    pstmt.setString(3, astu_number);
			    pstmt.setString(4, aclass_name);
			  
			    
			    pstmt.setString(5, amessage);
			    pstmt.setString(6, ames_time);
			    pstmt.setString(7, astu_portrait);
			    
		} catch (Exception e) {
			
		}
	      
	     try {
				count = pstmt.executeUpdate();
			} catch (SQLException e) {
				System.out.println(e  +"´íÎó");
				 result = StatusCode.FIELD;
				 
				 
			}finally {
				if (count > 0) {
					 result = StatusCode.SUCCESS;
					
				
					
				}
				try {
					ConUtil.close(conn, pstmt, null);
				} catch (SQLException e) {
				
					e.printStackTrace();
				}
				 Map<String, Integer> map = new HashMap<>();
				 map.put("result", result);
				 JSONUtils.responseOutWithJson(response, map);
		  	
		}
	    
		
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
