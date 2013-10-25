package com.Phoenix.dashboard;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.Phoenix.dashboard.main.Display;
import com.Phoenix.data.Issue;
import com.Phoenix.data.User;

/**
 * Servlet implementation class ViewScOrders
 */
@WebServlet("/ViewScOrders.do")
public class ViewScOrders extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PrintWriter out;
    private Connection conn;
    private User loggedUser;
    private Display display;
    private Issue issue;   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ViewScOrders() {
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
		loggedUser = (User) request.getSession().getAttribute("loggedUser");
		display = (Display) request.getSession().getAttribute("display");
		if(getScOrdersView() > 0){
			//response.sendRedirect("main.do");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
	
	private int getScOrdersView(){
		int status =0;
		try {
			PreparedStatement stmt = conn.prepareStatement("call viewScOrders()");
			ResultSet res = stmt.executeQuery();
			if(res.next()){
				out.println("<div id=\"dialog\" title=\"Orders have Updated\">");
				out.println("<p> Updated Schedules On "+res.getTimestamp("time").toLocaleString()+" By "+res.getString("username")+"</p>");
				out.println("</div>");
				//status = updateScOrders(res.getTimestamp("time"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return status;
	}
	
	private int updateScOrders(Timestamp time){
		int status =0;
		try {
			PreparedStatement stmt = conn.prepareStatement("call updateScOrders(?)");
			stmt.setTimestamp(1, time);
			 status = stmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return status;
	}

}
