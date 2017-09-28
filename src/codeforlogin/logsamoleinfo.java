package codeforlogin;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javax.ws.rs.Path;
@Path("/login")
public class logsamoleinfo extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public  logsamoleinfo() {
		// TODO Auto-generated constructor stu 
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String usernameCode = request.getParameter("usernameCode");
        
        PrintWriter out = response.getWriter();
        response.setContentType("text/html");
        response.setHeader("Cache-control", "no-cache, no-store");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "-1");

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setHeader("Access-Control-Max-Age", "86400");

        Gson gson = new Gson(); 
        JsonObject myObj = new JsonObject();

        logsample countryInfo = getInfo(usernameCode);
        JsonElement countryObj = gson.toJsonTree(countryInfo);
        if(countryInfo.getName() == null){
            myObj.addProperty("success", false);
        }
        else {
            myObj.addProperty("success", true);
        }
        myObj.add("countryInfo", countryObj);
        out.println(myObj.toString());

        out.close();

    }

    //Get Country Information
    private logsample getInfo(String usernameCode) {

    	logsample country = new logsample();
        Connection conn = null;            
        PreparedStatement stmt = null;     
        String sql = null;

        try {      
            Context ctx = (Context) new InitialContext().lookup("java:comp/env");
            conn = ((DataSource) ctx.lookup("jdbc/mysql")).getConnection(); 

            sql = "Select * from loginpage where username = ?"; 
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, usernameCode.trim());
            ResultSet rs = stmt.executeQuery(); 

            while(rs.next()){ 
                country.setCode(rs.getString("usernameCode").trim());
                country.setCode(rs.getString("passwordcode").trim());
                
            }                                                                         

            rs.close();                                                               
            stmt.close();                                                             
            stmt = null;                                                              


            conn.close();                                                             
            conn = null;                                                   

        }                                                               
        catch(Exception e){System.out.println(e);}                      

        finally {                                                       
 
            if (stmt != null) {                                            
                try {                                                         
                    stmt.close();                                                
                } catch (SQLException sqlex) {                                
                    // ignore -- as we can't do anything about it here           
                }                                                             

                stmt = null;                                            
            }                                                        

            if (conn != null) {                                      
                try {                                                   
                    conn.close();                                          
                } catch (SQLException sqlex) {                          
                    // ignore -- as we can't do anything about it here     
                }                                                       

                conn = null;                                            
            }                                                        
        }              

        return country;

    }   

}


