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
 * Servlet implementation class DeleteDepartment
 */
@WebServlet("/DeleteDepartment.do")
public class DeleteDepartment extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PrintWriter out;
    private Connection conn; 
    private int cPage = 5;  
    private ArrayList<Department> deps = new ArrayList<Department>();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteDepartment() {
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
		getDepartments();
		request.getSession().setAttribute("deps", deps);
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
		if(request.getParameter("depts") != null){
			deleteDepartment(Integer.parseInt(request.getParameter("depts")));
		}
		getDepartments();
		request.getSession().setAttribute("deps", deps);
		RequestDispatcher reqd = request.getRequestDispatcher("/deleteuser.jsp");
		reqd.forward(request, response);
	}
	
	private int deleteDepartment(int depid){
		int status =0;
		try {
			PreparedStatement stmt = conn.prepareStatement("call deleteDept(?)");
			stmt.setInt(1, depid);
			status = stmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			out.println(e.getMessage());
		}
		return status;
	}
	
	private void getDepartments(){
		try {
			PreparedStatement stmt = conn.prepareStatement("select distinct depName,iddepartment from department");
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
