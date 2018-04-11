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

import DBUtils.ConUtil;
import DBUtils.DBUtil;
import DBUtils.JSONUtils;
import DBUtils.StatusCode;


@WebServlet("/BindServlet")
public class BindServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public BindServlet() {
        super();
    }

	
	@SuppressWarnings("resource")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 String openid = request.getParameter("openid");
	     String number = request.getParameter("number");
	     String sql ="UPDATE tb_student SET openid = ? WHERE stu_number = ?";
	     String sql1 ="SELECT count(openid) AS counts FROM tb_student WHERE stu_number = ? ";
	     
	     PreparedStatement pstmt = null;
	     Connection conn=null;
	     int count = 0;
	     ResultSet rs = null;
	     Map<String, Integer> map = new HashMap<>();
	     try {
	    	
	    	 conn = ConUtil.getConn();
			    pstmt = conn.prepareStatement(sql1);
			    pstmt.setString(1,number);
			    
			    rs = pstmt.executeQuery();
			    
			    if (rs.next()&&rs.getInt("counts")>0) {
			    	System.out.println(rs.getInt("counts"));
			        
			        	 
			        	 map.put("result", StatusCode.REPEATED_ACCOUNTS);
					
			       
			    	 
				}else {
					
				
	    	 
	    	 
	    	 
			 
			  
			    pstmt = conn.prepareStatement(sql);
			    pstmt.setString(1,openid);
			    pstmt.setString(2,number);
			    
			  count = pstmt.executeUpdate();
			  System.out.println(count);
			  if (count >0) {
				  map.put("result", StatusCode.SUCCESS);
				
			}else {
				 map.put("result", StatusCode.FIELD);
					
			}
				}
	     }catch (Exception e) {
			System.out.println(e+"“Ï≥£");
		}finally {
			try {
				ConUtil.close(conn, pstmt, null);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			JSONUtils.responseOutWithJson(response, map);
		}
	     
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
