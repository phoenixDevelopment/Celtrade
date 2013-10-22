package com.Phoenix.dashboard;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.Phoenix.dashboard.main.Display;
import com.Phoenix.data.Department;
import com.Phoenix.data.User;
import com.mysql.jdbc.PreparedStatement;

/**
 * Servlet implementation class IssueCreate
 */
@WebServlet("/IssueCreate.do")
public class IssueCreate extends HttpServlet {
	private static final long serialVersionUID = 1L;
       private Connection conn;
       private  PrintWriter out;
       private User loggedUser ;
       private int currentPage  = 1;
       private Display display;
       private ArrayList<Department> deps = new ArrayList<Department>();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public IssueCreate() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		out = response.getWriter();
		ServletContext context = this.getServletContext();
		conn = (Connection) context.getAttribute("databaseConn");
		loggedUser = (User) request.getSession().getAttribute("loggedUser");
		display = (Display) request.getSession().getAttribute("display");
		display.setWriter(out);
		response.setContentType("text/html");
		getDepartmens();
		display.setUsers(deps);
		display.getHeader(currentPage);
		display.getBody(this.loggedUser,currentPage);
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
	}
	
	private void getDepartmens(){
		try {
			java.sql.PreparedStatement stmt = conn.prepareStatement("SELECT iddepartment,depName FROM department;");
			ResultSet res = stmt.executeQuery();
			while(res.next()){
				deps.add(new Department(res.getInt("iddepartment"), res.getString("depName")));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	


}
