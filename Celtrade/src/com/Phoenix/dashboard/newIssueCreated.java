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
 * Servlet implementation class newIssueCreated
 */
@WebServlet("/newIssueCreated.do")
public class newIssueCreated extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection conn;
    private  PrintWriter out;
    private User loggedUser ;
    private Display display;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public newIssueCreated() {
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
		checkIsnewIssueCreated();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
	
	private void checkIsnewIssueCreated(){
		try{
			PreparedStatement stmt = conn.prepareStatement("select idNewIssueAdded,TIMESTAMPDIFF(MINUTE,added,now()) as 'openedSince' from newIssueAdded where TIMESTAMPDIFF(MINUTE,added,now()) < 4 order by 1 desc limit 1;");
			ResultSet res = stmt.executeQuery();
			if(res.next()){
				out.println("OK");
			}else{
				out.println("NOTOK");
			}
		}catch(SQLException ex){
			out.println("SQL Exception Occured");
		}
	}

}
