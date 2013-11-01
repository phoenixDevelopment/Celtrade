package com.Phoenix.dashboard;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.Phoenix.dashboard.main.Display;
import com.Phoenix.data.User;

/**
 * Servlet implementation class AddIssue
 */
@WebServlet("/AddIssue.do")
public class AddIssue extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection conn;
    private  PrintWriter out;
    private User loggedUser ;
    private Display display;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddIssue() {
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
		ServletContext context = this.getServletContext();
		conn = (Connection) context.getAttribute("databaseConn");
		loggedUser = (User) request.getSession().getAttribute("loggedUser");
		display = (Display) request.getSession().getAttribute("display");
		display.setWriter(out);
		String issueName = request.getParameter("issueName");
		String issueType = request.getParameter("issueType");
		String openBy = request.getParameter("openBy");
		String openFor[] = request.getParameterValues("openFor");
		String prior = request.getParameter("priority");
		int stat = addIssueToDB(issueName, issueType, this.loggedUser.getUserID(),prior,openFor);
		if(stat >= 1){
			response.sendRedirect("main.do");
		}
		
	}
	
	private int addIssueToDB(String name,String type,int openBy,String priority,String[] deps){
		int status = 0;
		try {
			PreparedStatement stmt = conn.prepareStatement("call addNewIssue(?,?,?,?);");
			stmt.setString(1, name);
			stmt.setString(2, type);
			stmt.setInt(3, openBy);
			stmt.setInt(4,Integer.valueOf(priority));
			 status = stmt.executeUpdate();
			 if(status > 0){
				 PreparedStatement stmt1 = conn.prepareStatement("select idIssueOpen,issueID from issueOpen order by 1 desc limit 1");
				 ResultSet res = stmt1.executeQuery();
				 if(res.next()){
					 for(int i =0;i<deps.length;i++){
						 PreparedStatement stmt2 = conn.prepareStatement("call addIssueDepJoin(?,?)");
						 stmt2.setInt(1, Integer.valueOf(deps[i]));
						 stmt2.setInt(2, res.getInt("issueID"));
						 status = stmt2.executeUpdate();
					 }
				 }
			 }
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return status;
	}

}
