package com.Phoenix.dashboard;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.Phoenix.dashboard.main.Display;
import com.Phoenix.data.User;

/**
 * Servlet implementation class ClosedIssues
 */
@WebServlet("/ClosedIssues.do")
public class ClosedIssues extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection conn;
    private  PrintWriter out;
    private User loggedUser = new User();
    private Display display;   
    private int currentPage = 3;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ClosedIssues() {
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
		display.setWriter(out);
		response.setContentType("text/html");
		display.getHeader(currentPage);
		display.getBody(loggedUser, currentPage);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
