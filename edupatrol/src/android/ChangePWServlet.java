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

import com.sun.javafx.collections.ElementObservableListDecorator;

import DBUtils.ConUtil;
import DBUtils.JSONUtils;
import DBUtils.StatusCode;

/**
 * Servlet implementation class ChangePWServlet
 */
@WebServlet("/ChangePWServlet")
public class ChangePWServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	 PreparedStatement pstmt = null;
     Connection conn=null;
     ResultSet rs;
     int count = 0;
       String sql = "SELECT password FROM tb_login WHERE id = ?";
       String sql1 = "UPDATE tb_login SET `password` = ? WHERE id = ?";
      Map<String, Integer> map ;
    public ChangePWServlet() {
        super();
       
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String oldpassword = request.getParameter("oldpassword");
		System.out.println(oldpassword);
		String newpassword = request.getParameter("newpassword");
		
		String id = request.getParameter("id");
		
		
		             try{
			    	    conn = ConUtil.getConn();
					    pstmt = conn.prepareStatement(sql);
					    pstmt.setInt(1, Integer.parseInt(id));
					    rs = pstmt.executeQuery();
					   if (rs.next()) {
						   System.out.println(rs.getString("password"));
						   if (!oldpassword.equals(rs.getString("password"))) {
							   map = new HashMap<>();
								map.put("result", StatusCode.PASSWORD_IS_FALSE);
								System.out.println("√‹¬Î¥ÌŒÛ");
								JSONUtils.responseOutWithJson(response, map);}
						   else {
							   if (oldpassword.equals(newpassword)) {
									 map = new HashMap<>();
									 System.out.println("√‹¬Î÷ÿ∏¥");
										map.put("result", StatusCode.REPEATED_ACCOUNTS);
										JSONUtils.responseOutWithJson(response, map); }
							   else {
								   System.out.println("÷¥––¡À");
								   try{
						                pstmt = conn.prepareStatement(sql1);
									    pstmt.setString(1,newpassword);
									    pstmt.setString(2,id);
									    count = pstmt.executeUpdate();
									    if (count>0) {
									    	map = new HashMap<>();
											map.put("result", StatusCode.SUCCESS);
											
										}else {
											map = new HashMap<>();
											map.put("result", StatusCode.FIELD);
										}
						             
						             }catch (Exception e) {
										// TODO: handle exception
									}
								}
						        }
					   }}catch (Exception e) {} 
		             
		           finally {
						try {
						
							
						ConUtil.close(conn, pstmt, null);
						JSONUtils.responseOutWithJson(response, map);
					} catch (SQLException e) {
					
						e.printStackTrace();
					}
					
			 
			 }
           	   
					
	
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
