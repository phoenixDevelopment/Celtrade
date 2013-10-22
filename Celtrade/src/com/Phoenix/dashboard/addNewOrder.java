package com.Phoenix.dashboard;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.Phoenix.data.User;

/**
 * Servlet implementation class addNewOrder
 */
@WebServlet("/addNewOrder.do")
public class addNewOrder extends HttpServlet {
	private static final long serialVersionUID = 1L;
	 private PrintWriter out;
	 private Connection conn;
	 private User loggedUser;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public addNewOrder() {
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
		response.setContentType("text/html");
		out = response.getWriter();
		response.setHeader("Cache-Control", "no-cache");
		ServletContext context = this.getServletContext();
		conn = (Connection) context.getAttribute("databaseConn");
		loggedUser = (User) request.getSession().getAttribute("loggedUser");
		updateOrders();
		response.sendRedirect("main.do");
	}
	
	
	private void updateOrders(){
		try {
			PreparedStatement stmt = conn.prepareStatement("call addnewOrders(?);");
			stmt.setInt(1, loggedUser.getUserID());
			stmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

}
