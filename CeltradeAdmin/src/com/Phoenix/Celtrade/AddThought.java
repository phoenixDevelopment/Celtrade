package com.Phoenix.Celtrade;

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

/**
 * Servlet implementation class AddThought
 */
@WebServlet("/AddThought.do")
public class AddThought extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PrintWriter out;
    private Connection conn; 
    private int cPage = 3;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddThought() {
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
		RequestDispatcher reqd = request.getRequestDispatcher("/main.jsp");
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
		String thoughts = request.getParameter("thought");
		int status = addThought(thoughts);
		if(status > 0 ){
			RequestDispatcher reqD = request.getRequestDispatcher("/main.jsp");
			reqD.forward(request, response);
		}
	}
	
	private int addThought(String thoughts){
		int status = 0;
		try {
			PreparedStatement stmt = conn.prepareStatement("insert into Thought(thoughtOfDay)values(?)");
			stmt.setString(1, thoughts);
			status = stmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			out.println(e.getMessage());
		}
		return status;
		
	}

}
