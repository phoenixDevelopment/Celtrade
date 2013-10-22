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
import com.Phoenix.data.Issue;
import com.Phoenix.data.User;

/**
 * Servlet implementation class UpdateIssue
 */
@WebServlet("/UpdateIssue.do")
public class UpdateIssue extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection conn;
    private  PrintWriter out;
    private User loggedUser = new User();
    private Display display;    
    private Issue issue;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateIssue() {
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
		String cmt = request.getParameter("comments");
		int issueId = Integer.valueOf(request.getParameter("issueID"));
		String action = request.getParameter("action");
		out = response.getWriter();
		ServletContext context = this.getServletContext();
		conn = (Connection) context.getAttribute("databaseConn");
		loggedUser = (User) request.getSession().getAttribute("loggedUser");
		int status = 0;
		int statusUpdate = 0;
		int statusAddCmts = 0;
		if(action.equals("addComment")){ 
			statusAddCmts = addComments(cmt,this.loggedUser.getUserID(),issueId);
		}if(action.equals("addCmtStatus")){
			// call  issue Close function here to close issue  (action=addcmtStatus);
			if(Integer.parseInt(request.getParameter("statusChange")) == currentIssueStaus(issueId)){ 
				statusAddCmts = addComments(cmt,this.loggedUser.getUserID(),issueId);
			}else{
				int statusId = Integer.parseInt(request.getParameter("statusChange"));
				statusUpdate = updateIssue(issueId, this.loggedUser.getUserID(), statusId);	
				status = addComments(cmt,this.loggedUser.getUserID(),issueId);
			}
		}
		if(status >= 1 && statusUpdate >= 1){
			response.sendRedirect("ViewIssue.do?id="+issueId);
		}if(statusAddCmts >=1){
			response.sendRedirect("ViewIssue.do?id="+issueId);
		}
	}
	
	private int addComments(String cmt,int userID,int issueID){
		int status = 0;
		try {
			PreparedStatement  stmt = conn.prepareStatement("call addComment(?,?,?);");
			stmt.setString(1, cmt);
			stmt.setInt(2, userID);
			stmt.setInt(3, issueID);
			status = stmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return status;
	}
	
	private int updateIssue(int issueId,int userId,int stauts){
		int status =0;
		int cStatus = currentIssueStaus(issueId);
		try{
			PreparedStatement stmt = conn.prepareStatement("call updateIssue(?,?,?,?);");
			stmt.setInt(1, issueId);
			stmt.setInt(2, userId);
			stmt.setInt(3, stauts);
			stmt.setInt(4, cStatus);
			status = stmt.executeUpdate();
		}catch(Exception ex){
			
		}
		return status;
	}
	
	private int currentIssueStaus(int idIssue){
		int cStatus =0;
		try {
			PreparedStatement stmt = conn.prepareStatement("select resolveStatus from Issues where idIssues=?;");
			stmt.setInt(1, idIssue);
			ResultSet res = stmt.executeQuery();
			if(res.next()){
				cStatus = res.getInt("resolveStatus");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cStatus;
	}

}
