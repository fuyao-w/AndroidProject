package DBUtils;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;

public class JSONUtils {
	
	public static void responseOutWithJson(HttpServletResponse response, Object responseObject) {  
	    //将实体对象转换为JSON Object转换  
	    String responseJSONObjectStr = JSONObject.toJSONString(responseObject);  
	     
	    response.setCharacterEncoding("UTF-8");  
	    response.setContentType("application/json; charset=utf-8");  
	    PrintWriter out = null;  
	    try {  
	        out = response.getWriter();  
	        
	        out.append(responseJSONObjectStr);  
	        System.out.println(responseJSONObjectStr+   "结果");
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    } finally {  
	        if (out != null) {  
	            out.close();  
	        }  
	    }  
	} 

}

