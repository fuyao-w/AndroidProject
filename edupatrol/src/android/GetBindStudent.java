package android;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import DBUtils.ConUtil;
import DBUtils.JSONUtils;


@WebServlet("/GetBindStudent")
public class GetBindStudent extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Map<String, Object> map;
	private static final String sql = "SELECT * FROM tb_student WHERE openid = ?";
    public GetBindStudent() {
        super();
       
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String openid = request.getParameter("openid");
		System.out.println(openid);
		 PreparedStatement pstmt = null;
	     ResultSet rs = null;
	     Connection conn=null;
	     
	     
	     try{
	     conn = ConUtil.getConn();
	     pstmt = conn.prepareStatement(sql);
	     pstmt.setString(1, openid);
		    rs = pstmt.executeQuery();
		    System.out.println(pstmt);
		    
		    
		    if (rs.next()) {
		    	
		    	Student student = new Student();
    			student.setStu_number(rs.getString("stu_number"));
    			
    			
    			student.setStu_name(rs.getString("stu_name"));
    			student.setStu_class(rs.getString("stu_class"));
    			student.setStu_gender(rs.getString("stu_gender"));
    			student.setStu_region(rs.getString("stu_region"));
    			student.setStu_number(rs.getString("stu_number"));
    			student.setStu_portrait(rs.getString("stu_portrait"));
    			student.setLogin_id(rs.getInt("id"));
    			System.out.println(rs.getInt("id"));
    			map = new HashMap<>();
    			
    			map.put("Code", 200);
    			map.put("id", rs.getInt("id"));
    			map.put("userinfo", student);
    			
    			JSONUtils.responseOutWithJson(response,map);
				
			}
		    else {
map = new HashMap<>();
    			
    			map.put("Code", 400);
    			JSONUtils.responseOutWithJson(response,map);
			}
		    
	     }catch (Exception e) {
			// TODO: handle exception
		}finally {
			
			try {
				ConUtil.close(conn, pstmt, rs);
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
