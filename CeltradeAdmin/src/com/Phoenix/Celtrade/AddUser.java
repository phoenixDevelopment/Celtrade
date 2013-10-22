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
 * Servlet implementation class AddUser
 */
@WebServlet("/AddUser.do")
public class AddUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private PrintWriter out;
    private Connection conn;
    private int cPage = 1;
    private Boolean orderAllowed = false ;
    private Boolean schedulesAllowed = false;
    private Boolean wrkOrderAllowed = false;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddUser() {
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
		RequestDispatcher reqd = request.getRequestDispatcher("/Main.do");
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
		String username = request.getParameter("userName");
		String useremail = request.getParameter("userEmail");
		String password = request.getParameter("password");
		int depID = Integer.parseInt(request.getParameter("department"));
		if(request.getParameter("orderAllowed") != null){
			orderAllowed = true;
		}
		if(request.getParameter("schecdulesAllowed") != null){
			schedulesAllowed = true;
		}
		if(request.getParameter("wrkOrdersAllowed") != null){
			wrkOrderAllowed = true;
		}
		int status = addUsers(username,useremail,password,depID,orderAllowed,schedulesAllowed,wrkOrderAllowed);
		if(status > 0){
		RequestDispatcher reqd = request.getRequestDispatcher("/main.jsp");
		reqd.forward(request, response);
		}
	}
	
	
	
	private int addUsers(String userName,String useremail,String password,int depID,boolean orderAllowed,boolean schedules,boolean wrkOrders){
		int status =0;
		String sql = "insert into users(username,email,password,department,canAddNewOrder,canAddScOrder,canAddWorkOrder)values(?,?,md5(?),?,?,?,?)";
		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, userName);
			stmt.setString(2, useremail);
			stmt.setString(3, password);
			stmt.setInt(4, depID);
			stmt.setBoolean(5, orderAllowed);
			stmt.setBoolean(6, schedules);
			stmt.setBoolean(7, wrkOrders);
			status = stmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			out.println(e.getMessage());
		}
		return status;		
	}

}
