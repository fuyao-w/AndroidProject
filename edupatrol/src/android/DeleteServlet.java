package android;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
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


@WebServlet("/DeleteServlet")
public class DeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
    public DeleteServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 
	     String record_id =  request.getParameter("record_id");
	     System.out.println(record_id);
	     
	     String sql ="DELETE FROM tb_record WHERE record_id=?";
	     PreparedStatement pstmt = null;
	     Connection conn=null;
	     int count;
	     Map<String, Integer> map = new HashMap<>();
	     try {
	    	 
	    	 
			    conn = ConUtil.getConn();
			    pstmt = conn.prepareStatement(sql);
			   
			    pstmt.setString(1,record_id);
			    System.out.println(pstmt);
			  count = pstmt.executeUpdate();
			 
			  if (count >0) {
				  map.put("result", StatusCode.SUCCESS);
				 
			}else {
				 map.put("result", StatusCode.FIELD);
					
			}
	     }catch (Exception e) {
			System.out.println(e);
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
		
		doGet(request, response);
	}

}
