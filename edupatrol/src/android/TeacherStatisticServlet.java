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


@WebServlet("/TeacherStatisticServlet")
public class TeacherStatisticServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String sql ="SELECT stu_name, stu_number,COUNT(record_id) AS total"
			+ " FROM tb_record LEFT JOIN tb_teacher ON tb_teacher.class_name = tb_record.class_name"
			+ " WHERE tb_teacher.class_name=? GROUP BY tb_record.stu_name";
	Map<String, String> map ;
	Map<String,List<Map<String, String>>>  result;
	private List<Map<String, String>> list = new ArrayList<>();
    public TeacherStatisticServlet() {
        super();
   
    }

    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String class_name = new String(request.getParameter("class_name").getBytes("iso-8859-1"),"utf-8");
		   System.out.println(class_name);
		  
		     PreparedStatement pstmt = null;
		     ResultSet rs = null;
		     Connection conn=null;
		    
		     
		     try {
		    	 
			 
			    conn = ConUtil.getConn();
			    pstmt = conn.prepareStatement(sql);
			    pstmt.setString(1, class_name);
			    rs = pstmt.executeQuery();
			    System.out.println(pstmt);
			    while(rs.next()){
			    	map = new  HashMap<>();
			    	map.put("stu_name", rs.getString("stu_name"));
			    	System.out.println(rs.getString("stu_name"));
			    	map.put("total", rs.getString("total"));
			    	System.out.println(rs.getString("total"));
			    	
			    	list.add(map);
			    
			    	}
		     }catch (Exception e) {
				
			}finally {
				try {
					ConUtil.close(conn, pstmt, null);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				result = new HashMap<>();
				result.put("result", list);
				
				JSONUtils.responseOutWithJson(response, result);
				list.clear();
				result.clear();
			}
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
