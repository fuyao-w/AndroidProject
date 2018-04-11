package android;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.crypto.provider.RSACipher;

import DBUtils.ConUtil;
import DBUtils.JSONUtils;

/**
 * Servlet implementation class SearchServlet2
 */
@WebServlet("/SearchServlet2")
public class SearchServlet2 extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection conn;
	 private java.sql.Statement st;
	 private ResultSet count;
	 String allrecords;
    public SearchServlet2() {
        super();
        // TODO Auto-generated constructor stub
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 String sql = "SELECT COUNT(record_id) AS allrecords FROM tb_record";
		 String k = request.getParameter("key");
		
	    
			 if (k.equals("tjpu")) {
				
			try {
	    	 
			 
			  conn = ConUtil.getConn();
		      st  = conn.createStatement();
			
			  count = st.executeQuery(sql);
			  if (count.next()) {
				  allrecords = count.getString("allrecords");
				 
				
			}
		 }catch (Exception e) {
			e.printStackTrace();
		}finally {
			Map<String, Integer> map = new HashMap<>();
			map.put("allrecords", Integer.parseInt(allrecords));
			JSONUtils.responseOutWithJson(response, map);
		}
			 }
			 else{}
		 
		 
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}
