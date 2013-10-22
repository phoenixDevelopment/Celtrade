package com.Phoenix.login;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpRetryException;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.Phoenix.data.User;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.interceptors.SessionAssociationInterceptor;

/**
 * Servlet implementation class login
 */
@WebServlet("/login.do")
public class login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String userName;
	private String pass;
	private java.sql.PreparedStatement stmt;
	private PrintWriter out;
	private User logged;
	//private final String HOST = "192.168.0.105";
	private final String HOST = "localhost";
    /**
     * Default constructor. 
     */
    public login() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.userName = request.getParameter("login");
		this.pass = request.getParameter("password");
		out = response.getWriter();
		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");
		out.println("<html>");
		out.println("<head>");
		out.println("<title>Logging In ....</title>");
		out.println("</head>");
		out.println("<body>");
		out.println(this.userName);
			checkLogin(request,response);		
		out.println("</body>");
		out.println("</html>");
	}

	private void checkLogin(HttpServletRequest req,HttpServletResponse resp) {
		// TODO Auto-generated method stub
		try{
			String connection = "jdbc:mysql://"+HOST+"/celtrade";
			java.sql.Connection conn = null;
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(connection,"celtrade","celtrade24");
			if(!conn.isClosed()){
				//out.println("Connected to database");
				String userQuery = "select idusers,username,password from userDepartment where username=? and password=md5(?)";
				stmt = conn.prepareStatement(userQuery);
				stmt.setString(1, this.userName);
				stmt.setString(2, this.pass);
				ResultSet result = stmt.executeQuery();
				if(result.next()){
					String sessionID = req.getSession().getId();
					String ip = req.getLocalAddr();
					Date created = new Date(req.getSession().getCreationTime());
					Date lastAccess = new Date(req.getSession().getLastAccessedTime());
					out.println(sessionID);
					out.println(ip);
					out.println(created.toLocaleString());
					out.println(lastAccess.toGMTString());
					this.logged = new User(result.getInt("idusers"), result.getString("username"));  
					//out.println("UserID "+logged.getUserID());
					if(checkSession(sessionID, conn)){
						updateSession(sessionID,lastAccess,conn);
						//out.println("code in  checkSession()");
					}else{
						setSession(sessionID,ip,created,lastAccess,conn);
						//out.println("code in setSession()");
					}
						Cookie cooke = new Cookie("sessionID", sessionID);
						resp.addCookie(cooke);
						resp.sendRedirect("/Celtrade/main.do");
					out.println("Done ....");
				}else{
					resp.sendRedirect("/Celtrade");
				}
			}
			stmt.close();
			conn.close();
		}catch(Exception ex){
			out.println(ex.getMessage());
			out.println(ex.getLocalizedMessage());
			out.println("Unable to connect to Database");
		}
		
	}
	
	private void updateSession(String sessionID,Date lastAccess,java.sql.Connection conn) {
		// TODO Auto-generated method stub
		try {
			java.sql.PreparedStatement stmtt = conn.prepareStatement("update sessions set lastAccess=?,userLogged=? where sessionID=?");
			stmtt.setDate(1, lastAccess);
			stmtt.setInt(2, this.logged.getUserID());
			stmtt.setString(3, sessionID);
			stmtt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			out.println("UpdateSession Error "+e.getMessage());
		}
		
	}

	public void setSession(String sessionID,String ip,Date created,Date lastAccess,java.sql.Connection conn){
			try {
				java.sql.PreparedStatement stmt = conn.prepareStatement("call addSession(?,?,?,?,?)");
				stmt.setString(1, this.logged.getUserName());
				stmt.setString(2, sessionID);
				stmt.setString(3, ip);
				stmt.setDate(4, created);
				stmt.setDate(5, lastAccess);
				stmt.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				out.println("Setsession Error "+e.getMessage());
			}
	}
	
	public boolean checkSession(String sessionID,java.sql.Connection conn){
		boolean isSet = false;
		try {
			java.sql.PreparedStatement stmtt = conn.prepareStatement("select username from userSessions where sessionID=?");
			stmtt.setString(1, sessionID);
			ResultSet res = stmtt.executeQuery();
			if(res.next()){
				isSet = true;
			}else{
				isSet = false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			out.println("CheckSession Error "+e.getMessage());
		}
		return isSet;		
	}
	

}
