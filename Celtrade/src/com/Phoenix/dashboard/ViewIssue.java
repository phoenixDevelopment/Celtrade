package com.Phoenix.dashboard;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.Phoenix.dashboard.main.Display;
import com.Phoenix.data.Comments;
import com.Phoenix.data.Issue;
import com.Phoenix.data.User;

/**
 * Servlet implementation class ViewIssue
 */
@WebServlet("/ViewIssue.do")
public class ViewIssue extends HttpServlet {
	private static final long serialVersionUID = 1L;
     private PrintWriter out;
     private Connection conn;
     private User loggedUser;
     private Display display;
     private int currentPage = 2;
     private Issue issue;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ViewIssue() {
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
		request.getSession().setAttribute("cPage", currentPage);
		display = (Display) request.getSession().getAttribute("display");
		display.setWriter(out);
		response.setContentType("text/html");
		String id = request.getParameter("id");
		getIssue(id);
		request.getSession().setAttribute("issue", issue);
		request.getSession().setAttribute("status", getStatus());
		int issueCreater = getIssueCreaterID(Integer.parseInt(id));
		request.getSession().setAttribute("issueCreaterID", issueCreater);
		
		RequestDispatcher reqD = context.getRequestDispatcher("/main.jsp");
		reqD.forward(request, response); 
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
	
	private void getIssue(String id){
		try {
			PreparedStatement stmt = conn.prepareStatement("select * from allIssues where idIssues=?");
			stmt.setInt(1, Integer.valueOf(id));
			ResultSet res = stmt.executeQuery();
			while(res.next()){
				this.issue = new Issue(res.getInt("idIssues"), res.getString("issueName"), res.getString("issuetype"), res.getString("priority"), res.getString("type"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getComments(id);
	}
	
	private void getComments(String id){
		try {
			PreparedStatement stmt = conn.prepareStatement("SELECT idusers,depName,username,`comment`,commented FROM celtrade.userComments where idIssues=?;");
			stmt.setInt(1, Integer.valueOf(id));
			ResultSet res = stmt.executeQuery();
			while(res.next()){
				if(!res.wasNull()){
					Comments cmt = new Comments(loggedUser, "", null); 
					User usr = new User(res.getInt("idusers"),res.getString("username"),res.getString("depName"));
					if(res.getTimestamp("commented") != null){
						Date date = res.getTimestamp("commented");
						cmt = new Comments(usr, res.getString("comment"),date);
						this.issue.addComment(cmt);
					}
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(NullPointerException ex){
			
		}
		
	}
	
	private  int getIssueCreaterID(int issueID){
		int userId = 0;
		try {
			PreparedStatement stmt = conn.prepareStatement("select userOpenID from issuesOpen where idIssues=?;");
			stmt.setInt(1, issueID);
			ResultSet res = stmt.executeQuery();
			if(res.next()){
				userId = res.getInt("userOpenID");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userId;
	}
	
	private String openSince(int hrsMins){
		String data = "";
		int hrs = hrsMins / 60;
		int mins = hrsMins % 60;
		if(hrs > 0){
		data += hrs+" hrs "+mins+" mins";
		}else if(mins == 0){
			data = "";
			data += hrs+" hrs";
		}else{
			data = "";
			data += mins+" mins";
		}
		return data;
	}
	
	private ArrayList<String> getStatus(){
		ArrayList<String>status = new ArrayList<String>();
		try {
			PreparedStatement stmt = conn.prepareStatement("select distinct type,idstatus from Status;");
			ResultSet res = stmt.executeQuery();
			while(res.next()){
				status.add("<option value=\""+res.getString("idstatus")+"\">"+res.getString("type")+"</option>");								
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return status;
	}
}
