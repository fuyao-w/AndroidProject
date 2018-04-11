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
import DBUtils.JSONUtils;
import DBUtils.StatusCode;


@WebServlet("/ModifyInforServlet")
public class ModifyInforServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PreparedStatement pstmt;
	private    Connection conn;
	private    int count=0;
	private Map<String, Integer> map = new HashMap<>();
	private String sql ="UPDATE tb_record SET stu_number=?,stu_name=?,class_name=?,message=?,mes_time=? WHERE record_id=?";
    public ModifyInforServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
   
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 String stu_number = new String(request.getParameter("stu_number").getBytes("iso-8859-1"),"utf-8");
		  System.out.println(stu_number);
	     String stu_name =  new String(request.getParameter("stu_name").getBytes("iso-8859-1"),"utf-8");
	     System.out.println(stu_name);
	     String class_name =  new String(request.getParameter("class_name").getBytes("iso-8859-1"),"utf-8");
	     System.out.println(class_name);
	     
	     String message =  new String(request.getParameter("message").getBytes("iso-8859-1"),"utf-8");
	     System.out.println(message);
	     String mes_time =  new String(request.getParameter("mes_time").getBytes("iso-8859-1"),"utf-8");
	     System.out.println(mes_time);
	     String record_id =  new String(request.getParameter("record_id").getBytes("iso-8859-1"),"utf-8");
	     System.out.println(record_id+"record_id"+stu_number+"stunumber");
	     
	   
	   System.out.println(sql);
	    
	     try {
	    	
	    	   conn = ConUtil.getConn();
	    	   System.out.println("dadadawd");
			    pstmt = conn.prepareStatement(sql);
			    System.out.println("dadadawd");
			    pstmt.setString(1,stu_number);
			    System.out.println("dadadawd");
			    pstmt.setString(2,stu_name);
			    System.out.println("dadadawd");
			    pstmt.setString(3,class_name);
			    System.out.println("dadadawd");
			    pstmt.setString(4,message);
			    System.out.println("dadadawd");
			    pstmt.setString(5,mes_time);
			    System.out.println("dadadawd");
			    pstmt.setString(6,record_id);
			    System.out.println(Integer.parseInt(record_id));
			  
			  count = pstmt.executeUpdate();
			  if (count >0) {
				  map.put("result", StatusCode.SUCCESS);
				
			}else {
				 map.put("result", StatusCode.FIELD);
					
			}
	     }catch (Exception e) {
			// TODO: handle exception
		}finally {
			JSONUtils.responseOutWithJson(response, map);
			try {
				ConUtil.close(conn, pstmt, null);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	     
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}
