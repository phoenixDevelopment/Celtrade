package com.Phoenix.Celtrade;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



/**
 * Servlet implementation class Login
 */
@WebServlet("/Login.do")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String userName;
	private String pass;
	private java.sql.PreparedStatement stmt;
	private PrintWriter out;
	private String loggedUser;
	//private final String HOST = "192.168.0.105";
	private final String HOST = "localhost";
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
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
		out = response.getWriter();
		try{
			if(request.getParameter("username") != null){
				this.userName = request.getParameter("username");
			}if(request.getParameter("password") != null){
				this.pass = request.getParameter("password");
			}
			//out.println(userName+"  "+pass);
			checkLogin(request, response);
		}catch(NullPointerException ex){
			response.sendRedirect("/CeltradeAdmin");
		}
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
				String userQuery = "select username,password from admin where username=? and password=md5(?)";
				stmt = conn.prepareStatement(userQuery);
				stmt.setString(1, this.userName);
				stmt.setString(2, this.pass);
				ResultSet result = stmt.executeQuery();
				if(result.next()){
					this.loggedUser = result.getString("username");
					this.getServletContext().setAttribute("loggedUser", loggedUser); 
						resp.sendRedirect("/CeltradeAdmin/Main.do");
				}else{
					resp.sendRedirect("/CeltradeAdmin");
				}
				ServletContext context = this.getServletContext();
				context.setAttribute("databaseConn", conn);
			}
			stmt.close();
			//conn.close();
		}catch(Exception ex){
			out.println(ex.getMessage());
			out.println(ex.getLocalizedMessage());
			out.println("Unable to connect to Database");
		}
		
	}

}
