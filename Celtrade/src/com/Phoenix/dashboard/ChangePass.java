package com.Phoenix.dashboard;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.Phoenix.data.User;

/**
 * Servlet implementation class ChangePass
 */
@WebServlet("/ChangePass.do")
public class ChangePass extends HttpServlet {
	private static final long serialVersionUID = 1L;
	 private PrintWriter out;
	 private Connection conn;
	 private User loggedUser;
	 private boolean logged = false;     
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChangePass() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		out = response.getWriter();
		response.setHeader("Cache-Control", "no-cache");
		ServletContext context = this.getServletContext();
		conn = (Connection) context.getAttribute("databaseConn");
		loggedUser = (User) request.getSession().getAttribute("loggedUser");
		String pass = request.getParameter("password");
		int status = changePass(pass);
		if(status > 0){
			response.sendRedirect("main.do");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		out = response.getWriter();
		response.setHeader("Cache-Control", "no-cache");
		ServletContext context = this.getServletContext();
		conn = (Connection) context.getAttribute("databaseConn");
		loggedUser = (User) request.getSession().getAttribute("loggedUser");
		String pass = request.getParameter("password");
		int status = changePass(pass);
		if(status > 0){
		response.sendRedirect("main.do");
		}
	}
	
	private int changePass(String pass){
		int status =0;
		try {
			PreparedStatement stmt = conn.prepareStatement("update users set password=md5(?) where idusers=?;");
			stmt.setString(1, pass);
			stmt.setInt(2, this.loggedUser.getUserID());
			status = stmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return status;
	}

}
