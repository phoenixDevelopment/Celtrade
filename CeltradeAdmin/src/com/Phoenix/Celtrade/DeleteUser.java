package com.Phoenix.Celtrade;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class DeleteUser
 */
@WebServlet("/DeleteUser.do")
public class DeleteUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PrintWriter out;
    private Connection conn; 
    private int cPage = 4;  
    private ArrayList<User> users = new ArrayList<User>();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteUser() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		out = response.getWriter();
		ServletContext context = this.getServletContext();
		conn = (Connection) context.getAttribute("databaseConn");
		request.getSession().setAttribute("cPage", cPage);
		getUsers();
		request.getSession().setAttribute("users", users);
		RequestDispatcher reqd = request.getRequestDispatcher("/deleteuser.jsp");
		reqd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		out = response.getWriter();
		ServletContext context = this.getServletContext();
		conn = (Connection) context.getAttribute("databaseConn");
		request.getSession().setAttribute("cPage", cPage);
		if(request.getParameter("users") != null){
			deleteUser(Integer.parseInt(request.getParameter("users")));
		}
		getUsers();
		request.getSession().setAttribute("users", users);
		RequestDispatcher reqd = request.getRequestDispatcher("/deleteuser.jsp");
		reqd.forward(request, response);
	}
	
	private int deleteUser(int userid){
		int status =0;
		try {
			PreparedStatement stmt = conn.prepareStatement("call deleteUser(?)");
			stmt.setInt(1, userid);
			status = stmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			out.println(e.getMessage());
		}
		return status;
	}
	
	private void getUsers(){
		try {
			PreparedStatement stmt = conn.prepareStatement("select distinct username,idusers from users");
			ResultSet res = stmt.executeQuery();
			while(res.next()){
				users.add(new User(res.getInt("idusers"), res.getString("username")));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
