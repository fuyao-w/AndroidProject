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
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.jdbc.Statement;
import com.mysql.jdbc.exceptions.MySQLNonTransientConnectionException;
import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;

import DBUtils.ConUtil;
import DBUtils.JSONUtils;

/**
 * Servlet implementation class SearchServlet
 */
@WebServlet("/SearchServlet")
public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	//String searchMessage ;
	//private 
	
	
	ResultSet rs;
	
	 Connection conn;
	 private java.sql.Statement st;
	 private int count;
	

    public SearchServlet() {
        super();
        
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	
		Map<String,List<Map<String, String>>> results = new HashMap<String,List<Map<String, String>>>();
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		Map<String, String> map = null ;
	//	 String searchMessage = request.getParameter("searchMessage");
	
		 String searchMessage = new String(request.getParameter("searchMessage").getBytes("iso-8859-1"),"utf-8");
		 System.out.println(searchMessage);
		 String c = request.getParameter("currentPage");
		
		 String  l = request.getParameter("linesize");
		
		int currentPage = Integer.parseInt(c);
		 System.out.println(c +"  Ò³Êý");
		int linesize = Integer.parseInt(l);
		 System.out.println(l+"ÐÐÊý");
		
		
		 
		
		 String sql ="SELECT * FROM tb_record WHERE stu_number LIKE '%"+searchMessage+"%' OR stu_name LIKE '%"+searchMessage+"%' LIMIT "+
		 ((currentPage-1)*linesize)+"," +linesize;
		 
		 
		 String sql1 ="SELECT tb_student.stu_portrait,tb_record.*FROM tb_student RIGHT JOIN tb_record USING (stu_name) WHERE tb_record.stu_name  "
		 		+ "LIKE '%"+searchMessage+"%' OR tb_record.stu_number LIKE '%"+searchMessage+"%'"
+ "LIMIT " +((currentPage-1)*linesize)+"," +linesize;

		 
		
		
		 
		
		
		 try {
	    	 
			 
			    conn = ConUtil.getConn();
		
			
			  st  = conn.createStatement();
			   
			
			  System.out.println(sql1);
			  rs = st.executeQuery(sql1);
                
			 
			    while(rs.next()) {
			    	
			    	map  = new HashMap<>();
			    	map.put("stu_name", rs.getString("stu_name"));
			    	map.put("stu_number", rs.getString("stu_number"));
			    	map.put("class_name", rs.getString("class_name"));
			    	map.put("message", rs.getString("message"));
			    	map.put("mes_time", rs.getString("mes_time"));
			    	map.put("record_id", rs.getString("record_id"));
			    	if (rs.getString("stu_portrait") == null) 
			    		map.put("stu_portrait","0");
			    	else
			    	
			    	map.put("stu_portrait", rs.getString("stu_portrait"));
			    	System.out.println(rs.getString("stu_portrait") == null);
			    	result.add(map);
			    	
			    	}
			 
//			   if (!result.isEmpty()) {
				   
				   results.put("result", result);
				  
				   JSONUtils.responseOutWithJson(response, results);
				
//			}else {
//				map  = new HashMap<>();
//				String field = String.valueOf(StatusCode.FIELD);
//				map.put("field",field);
//				
//				System.out.println("beizhixing");
//				 JSONUtils.responseOutWithJson(response, map);
//			}
//			    
			   
		 }catch (Exception e) {
				
				}finally{
				
					try {
						ConUtil.close(conn, st, rs);
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
