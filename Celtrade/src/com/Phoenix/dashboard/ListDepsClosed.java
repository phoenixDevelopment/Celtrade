package com.Phoenix.dashboard;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.Phoenix.data.Department;
import com.Phoenix.data.OpenIssue;

/**
 * Servlet implementation class ListDepsClosed
 */
@WebServlet("/ListDepsClosed.do")
public class ListDepsClosed extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ArrayList<Department> deps = new ArrayList<Department>();
	 private ArrayList<OpenIssue> openIssuesDep = new ArrayList<OpenIssue>();
	 private PrintWriter out;
	 private Connection conn;
	 private int currentPage = 3;
	 private boolean logged = false;   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ListDepsClosed() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");
		out = response.getWriter();
		response.setHeader("Cache-Control", "no-cache");
		ServletContext context = this.getServletContext();
		conn = (Connection) context.getAttribute("databaseConn");
		displayIssuesDeps();
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

	private void getDepartments(){
		try {
			PreparedStatement stmt = conn.prepareStatement("select distinct depName,iddepartment from department");
			ResultSet res = stmt.executeQuery();
			while(res.next()){
				this.deps.add(new Department(res.getInt("iddepartment"),res.getString("depName")));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void getOpenIssuesForDep(String depN){
		try {
			PreparedStatement stmt = conn.prepareStatement("call openIssuesForDepName(?)");
			stmt.setString(1, depN);
			ResultSet res = stmt.executeQuery();
			while(res.next()){
				this.openIssuesDep.add(new OpenIssue(res.getString("username"), res.getString("depOpenFor"), res.getInt("idIssues"), res.getString("issueName"), res.getString("issuetype"), res.getString("priority"), res.getString("type"),res.getInt("openedTime")));				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void getClosedIssuesByDep(String depN){
		try {
			PreparedStatement stmt = conn.prepareStatement("call closeIssuesByDepName(?);");
			stmt.setString(1, depN);
			ResultSet res = stmt.executeQuery();
			while(res.next()){
				openIssuesDep.add(new OpenIssue(res.getString("username"),res.getString("depClosedFor"),res.getInt("idIssues"), res.getString("issueName"), res.getString("issuetype"), res.getString("priority"), res.getString("type"),res.getInt("closedTime")));			
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void getResolvedIssuesByDep(String depN){
		try {
			PreparedStatement stmt = conn.prepareStatement("call resolvedIssuesByDepName(?);");
			stmt.setString(1, depN);
			ResultSet res = stmt.executeQuery();
			while(res.next()){
				openIssuesDep.add(new OpenIssue(res.getString("username"),res.getString("depName"),res.getInt("idIssues"), res.getString("issueName"), res.getString("issuetype"), res.getString("priority"), res.getString("type"),res.getInt("resolvedAt")));					
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void displayIssuesDeps(){
		getDepartments();
		for(int i=0;i<deps.size();i++){
			out.println("<div class=\"col width-33 ui-sotable\">");
			out.println("<div class=\"sortable\" widgetid=\"widgetUserSettingsgetPlugun\">");
			out.println("<div id=\"widgetUserSettingsgetPlugin\" class=\"widget\">");
			out.println("<div class=\"widgetTop\">");
			out.println("<div class=\"widgetName\">"+deps.get(i).getDepName()+"</div>");
			out.println("</div>"); 
			out.println("<div class=\"widgetContent\">");
			out.println("<div class=\"dataTable\" id=\"dataTable_1\">");
			out.println("<div class=\"dataTableWrapper\">");
			out.println("<table cellspacing=\"0\" class=\"dataTable\">");
			out.println("<thead>");
			out.println("<tr>");
			out.println("<th class=\"sortable first_label\" id=\"label\">");
			out.println("<div id=\"thDiv\">Issues</div>");
			out.println("</th>");
			out.println("<th class=\"sortable columnSorted\" id=\"priority\">");
			out.println("<div id=\"thDiv\">Priority</div>");
			out.println("</th>");
			out.println("<th class=\"sortable last\" id=\"satus\">");
			out.println("<div id=\"thDiv\">Closed By</div>");
			out.println("</th>");
			out.println("</tr>");
			out.println("</thead>");
			out.println("<tbody>");
			getClosedIssuesByDep(deps.get(i).getDepName());
			for(int j=0;j<openIssuesDep.size();j++){
				if(j%2 !=0){
					out.println("<tr>");
					out.println("<td class=\"label labelodd\" tyle=\"min:width: 145px;\">");
					out.println("<span class=\"label\"><a href=\"ViewIssue.do?id="+String.valueOf(openIssuesDep.get(j).getIssueID())+"\">"+openIssuesDep.get(j).getIssueName()+"</a></span>");
					out.println("</td>");
					if(openIssuesDep.get(j).getPriority().equals("LOW")){
						out.println("<td class=\"columnodd\" style=\"background-color:#0F0\">"/*+openIssuesDep.get(j).getPriority()*/+"</td>");
					}
					if(openIssuesDep.get(j).getPriority().equals("MED")){
						out.println("<td class=\"columnodd\" style=\"background-color:#FF0\">"/*+openIssuesDep.get(j).getPriority()*/+"</td>");
					}
					if(openIssuesDep.get(j).getPriority().equals("HIGH")){
						out.println("<td class=\"columnodd\" style=\"background-color:#F00\">"/*+openIssuesDep.get(j).getPriority()*/+"</td>");
					}
					out.println("<td class=\"columnodd\">"+openIssuesDep.get(j).getOpenByUser()+"</td>");
					out.println("</tr>");
				}if(j%2==0){
					out.println("<tr>");
					out.println("<td class=\"label labeleven\" tyle=\"min:width: 145px;\">");
					out.println("<span class=\"label\"><a href=\"ViewIssue.do?id="+String.valueOf(openIssuesDep.get(j).getIssueID())+"\">"+openIssuesDep.get(j).getIssueName()+"</a>");
					out.println("</td>");
					if(openIssuesDep.get(j).getPriority().equals("LOW")){
						out.println("<td class=\"columnodd\" style=\"background-color:#0F0\">"/*+openIssuesDep.get(j).getPriority()*/+"</td>");
					}
					if(openIssuesDep.get(j).getPriority().equals("MED")){
						out.println("<td class=\"columnodd\" style=\"background-color:#FF0\">"/*+openIssuesDep.get(j).getPriority()*/+"</td>");
					}
					if(openIssuesDep.get(j).getPriority().equals("HIGH")){
						out.println("<td class=\"columnodd\" style=\"background-color:#F00\">"/*+openIssuesDep.get(j).getPriority()*/+"</td>");
					}
					out.println("<td class=\"columneven\">"+openIssuesDep.get(j).getOpenByUser()+"</td>");
					out.println("</tr>");
				}
				if((j == openIssuesDep.size()-1) && j == 4){
					out.println("<tr>");
					out.println("<td class=\"columneven\">");
					out.println("<span class=\"label\"><a href=\"MoreIssuesClosed.do?depid="+deps.get(i).getDepID()+"\">All Issues</a>");
					out.println("</td>");
					out.println("</tr>");
				}
			}
			openIssuesDep.clear();
			out.println("<tbody>");
			out.println("</table>"); // table ends
			//out.println("<div class=\"dataTableSpacer\"></div>");
			out.println("</div>");//  data table wrapper ends
			out.println("</div>"); // data table ends
			out.println("</div>"); // widget content ends
			out.println("</div>"); // widget ends
			out.println("</div>"); // get Plugin ends
			out.println("</div>"); // col width 33 ends
			if(i == 2){
				out.println("<div class=\"clear\"></div>");
				}
		}
		deps.clear();
	}
	
}
