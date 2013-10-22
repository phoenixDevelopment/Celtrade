package com.Phoenix.dashboard;

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

import com.Phoenix.data.OpenIssue;
import com.Phoenix.data.User;

/**
 * Servlet implementation class MoreIssues
 */
@WebServlet("/MoreIssues.do")
public class MoreIssues extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PrintWriter out ;
    private int startPage;
    private int depId ;
    private int PERPAGE = 10;
    private int totalPages;
    private Connection conn;
    private User loggedUser = new User();
    private int currentPage = 5;
    private ArrayList<OpenIssue> openIssuesDep = new ArrayList<OpenIssue>();
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MoreIssues() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		out = response.getWriter();
		response.setContentType("text/html");
		ServletContext context = this.getServletContext();
		conn = (Connection) context.getAttribute("databaseConn");
		loggedUser= (User)request.getSession().getAttribute("loggedUser");
		if(request.getParameter("depid") != null){
			this.depId = Integer.parseInt(request.getParameter("depid"));
			if(request.getParameter("page") != null){
				startPage = Integer.valueOf(request.getParameter("page"));
				//startPage = (startPage * PERPAGE);
				totalPages = getTotalPages(depId);
			}else{
				startPage = 0;
				totalPages = getTotalPages(depId);
			}
		}
		this.openIssuesDep = getAllIssues(depId);
		request.getSession().setAttribute("cPage", currentPage);
		request.getSession().setAttribute("depID", this.depId);
		request.getSession().setAttribute("startPage", this.startPage);
		request.getSession().setAttribute("issueList", this.openIssuesDep);
		request.getSession().setAttribute("totalPages", totalPages);
		//out.println(this.openIssuesDep.size());
		RequestDispatcher reqF = context.getRequestDispatcher("/main.jsp");
		reqF.forward(request, response); 
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
	
	private int getTotalPages(int depid){
		int total = 0;
		try {
			PreparedStatement stmt = conn.prepareStatement("select count(*) as 'total' from issuesOpen where depOpenID=?");
			stmt.setInt(1, depid);
			ResultSet res = stmt.executeQuery();
			if(res.next()){
				total = res.getInt("total");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		total = (total / PERPAGE);
		return total;
	}
	
	private ArrayList<OpenIssue> getAllIssues(int depid){
		ArrayList<OpenIssue> openIssues = new ArrayList<OpenIssue>();
		try {
			PreparedStatement stmt = conn.prepareStatement("select * from issuesOpen where depOpenID=? order by openedTime asc limit ?,?");
			stmt.setInt(1, depid);
			stmt.setInt(2, startPage);
			stmt.setInt(3, (startPage+PERPAGE));
			ResultSet res = stmt.executeQuery();
			while(res.next()){
				openIssues.add(new OpenIssue(res.getString("username"),res.getString("depOpenFor"),res.getInt("idIssues"), res.getString("issueName"), res.getString("issuetype"), res.getString("priority"), res.getString("type"),openSince(res.getInt("openedTime"))));			
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			out.println(e.getMessage());
		}
		return openIssues;
	}
	
	private String openSince(int hrsMins){
		String data = "";
		int hrs = hrsMins / 60;
		int mins = hrsMins % 60;
		if(hrs > 0){
		data += hrs+":"+mins;
		}else if((hrs > 0 && hrs <=9)){
			data ="";
			data += "0"+hrs+":"+mins;
		}else if((mins > 0 && mins <=9)){
			data ="";
			data += hrs+":0"+mins;
		}else if((hrs > 0 && hrs <=9) && (mins > 0 && mins <=9)){
			data ="";
			data += "0"+hrs+":0"+mins;
		}else if(mins == 0){
			data = "";
			data += hrs+":00";
		}else if(hrs ==0 && mins > 0){
			data = "";
			data += "00:"+mins;
		}else if(hrs == 0 && mins ==0){
			data ="";
			data += "00:00";
		}
		return data;
	}

}
