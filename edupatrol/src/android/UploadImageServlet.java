package android;

import java.io.File;
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

import com.alibaba.fastjson.JSONObject;
import com.jspsmart.upload.SmartUpload;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import DBUtils.ConUtil;
import DBUtils.DBUtil;
import DBUtils.JSONUtils;
import DBUtils.StatusCode;


@WebServlet("/UploadImageServlet")
public class UploadImageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String FilePath ="G:\\projectload\\";
	private static final String sql = "UPDATE tb_student SET stu_portrait = ? WHERE id = ?";
	private String virtualFileName ;
	private String id= null;
	private int count;
	Map< String, Integer> map ;
    public UploadImageServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
	doPost(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		  PreparedStatement pstmt = null;
		     
		     Connection conn=null;
		   
		
		   request.setCharacterEncoding("utf-8");
	        response.setCharacterEncoding("utf-8");
	        response.setContentType("text/html,charset=utf-8");
	        SmartUpload smartUpload = new SmartUpload();
	        
	        try {  
	            smartUpload.initialize(this.getServletConfig(), request, response); 
	            smartUpload.upload();  
	             id = smartUpload.getRequest().getParameter("u_id");
	             
	            
	            com.jspsmart.upload.File smartFile = smartUpload.getFiles().getFile(0);  
	            if (!smartFile.isMissing()) {  
	            	String path = genericPath(smartFile.getFileName(), FilePath);
	            	
	                String saveFileName = FilePath +path+ smartFile.getFileName(); 
	                 virtualFileName = "/images" +path+ smartFile.getFileName(); 
	                
	                System.out.println(virtualFileName);
	                smartFile.saveAs(saveFileName, SmartUpload.SAVE_PHYSICAL);  
	            }
	        } catch (Exception e) {  
	            e.printStackTrace();
	        }
	        try {
	        	
		    	 String path = "http://192.168.191.1:8081"+virtualFileName;
	   		 
			    conn = ConUtil.getConn();
			    pstmt = conn.prepareStatement(sql);
			    pstmt.setString(1,path);
			    pstmt.setString(2,id);
			   
			  
			 
			    try {
			    	 count = pstmt.executeUpdate();
			    	
					   
				} catch (MySQLIntegrityConstraintViolationException e) {
					
				}
	        }catch (Exception e) {
				// TODO: handle exception
			}finally {
				try {
					
					ConUtil.close(conn, pstmt, null);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (count > 0) {
					map = new HashMap<>();
					map.put("Code", StatusCode.SUCCESS);
				}else{
					map = new HashMap<>();
					map.put("Code", StatusCode.FIELD);
				}

             JSONUtils.responseOutWithJson(response, map);
				
				    }  
				

				
				
				
				
			
	        
	    	
	}

	
	
	private String genericPath(String filename, String storeDirectory) {
		int hashCode = filename.hashCode();
		int dir1 = hashCode&0xf;
		int dir2 = (hashCode&0xf0)>>4;
		
		String dir = "/"+dir1+"/"+dir2;
		
		File file = new File(storeDirectory,dir);
		if(!file.exists()){
			file.mkdirs();
		}
		return dir;
	}
}
