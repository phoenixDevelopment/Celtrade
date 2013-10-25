package com.Phoenix.dashboard;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.Phoenix.data.Department;
import com.Phoenix.data.OpenIssue;
import com.Phoenix.data.User;

/**
 * Servlet implementation class ListDeps
 */
@WebServlet("/ListDeps.do")
public class ListDeps extends HttpServlet {
	private static final long serialVersionUID = 1L;
	 private ArrayList<Department> deps = new ArrayList<Department>();
	 private ArrayList<OpenIssue> openIssuesDep = new ArrayList<OpenIssue>();
	 private PrintWriter out;
	 private Connection conn;
	 private User loggedUser;
	 private int currentPage = 0;
	 private boolean logged = false;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ListDeps() {
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
		loggedUser = (User) request.getSession().getAttribute("loggedUser");
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
				openIssuesDep.add(new OpenIssue(res.getString("username"),res.getString("depName"),res.getInt("idIssues"), res.getString("issueName"), res.getString("issuetype"), res.getString("priority"), res.getString("type"),res.getInt("closedAt")));			
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
			out.println("<div id=\"thDiv\">P</div>");
			out.println("</th>");
			out.println("<th class=\"sortable last\" id=\"satus\">");
			out.println("<div id=\"thDiv\">Owner</div>");
			out.println("</th>");
			out.println("<th class=\"sortable last\" id=\"opentime\">");
			out.println("<div id=\"thDiv\">Opened HH:MM</div>");
			out.println("</th>");
			out.println("</tr>");
			out.println("</thead>");
			out.println("<tbody>");
			getOpenIssuesForDep(deps.get(i).getDepName());
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
						out.println("<td class=\"columnodd\">"+openSince(openIssuesDep.get(j).getOpenedAt())+"</td>");
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
						out.println("<td class=\"columnodd\">"+openSince(openIssuesDep.get(j).getOpenedAt())+"</td>");
						out.println("</tr>");
					}
					if((j == openIssuesDep.size()-1) && j == 4){
						out.println("<tr>");
						out.println("<td class=\"columneven\">");
						out.println("<span class=\"label\"><a href=\"MoreIssues.do?depid="+deps.get(i).getDepID()+"\">All Issues</a>");
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
			//out.println("I  value @ "+i);
			if(i == 2){
			out.println("<div class=\"clear\"></div>");
			}
		}
		deps.clear();
		
		if(isUserAllowedtoAddNewOrders(loggedUser)){
			out.println("<div class=\"col width-100 ui-sotable\" style=\" margin-bottom: 1px; padding-bottom: 1px; height: 50px; \">");
			out.println("<div class=\"sortable\" widgetid=\"widgetUserSettingsgetPlugun\">");
			out.println("<div id=\"widgetUserSettingsgetPlugin\" class=\"widget\">");
			out.println("<div class=\"widgetTop\">");
			out.println("<div class=\"widgetName\">Update Orders</div>");
			out.println("</div>"); 
			out.println("<div class=\"widgetContent\">");
			out.println("<div class=\"dataTable\" id=\"dataTable_1\">");
			out.println("<div class=\"dataTableWrapper\">");
			out.println("<form method=\"post\" action=\"addNewOrder.do\" onsubmit=\"return(validateForm());\">");
			out.println("<input type=\"hidden\" value=\""+loggedUser.getUserID()+"\"/>");
			out.println("<input name=\"update\" type=\"submit\" value=\"Update Orders\" />");
			out.println(" </form>");
			out.println("<span>");
			lastUpdatedOrders();
			out.println("</span>");
			out.println("</div>");//  data table wrapper ends
			out.println("</div>"); // data table ends
			out.println("</div>"); // widget content ends
			out.println("</div>"); // widget ends
			out.println("</div>"); // get Plugin ends
			out.println("</div>"); // col width 33 ends
			
		}
		//if(isUserAllowedToViewNewOrders(loggedUser)){
			out.println("<div class=\"col width-100 ui-sotable\" style=\" margin-bottom: 1px; padding-bottom: 1px; height: 50px; \">");
			out.println("<div class=\"sortable\" widgetid=\"widgetUserSettingsgetPlugun\">");
			out.println("<div id=\"widgetUserSettingsgetPlugin\" class=\"widget\">");
			out.println("<div class=\"widgetTop\">");
			out.println("<div class=\"widgetName\">Orders Last Updated</div>");
			out.println("</div>"); 
			out.println("<div class=\"widgetContent\">");
			out.println("<div class=\"dataTable\" id=\"dataTable_1\">");
			out.println("<div class=\"dataTableWrapper\">");
			getNewOrdersView();
			out.println("</div>");//  data table wrapper ends
			out.println("</div>"); // data table ends
			out.println("</div>"); // widget content ends
			out.println("</div>"); // widget ends
			out.println("</div>"); // get Plugin ends
			out.println("</div>"); // col width 33 ends
		//}
		if(isUserAllowedtoAddScOrders(loggedUser)){
			out.println("<div class=\"col width-100 ui-sotable\" style=\" margin-bottom: 1px; padding-bottom: 1px; height: 50px; \">");
			out.println("<div class=\"sortable\" widgetid=\"widgetUserSettingsgetPlugun\">");
			out.println("<div id=\"widgetUserSettingsgetPlugin\" class=\"widget\">");
			out.println("<div class=\"widgetTop\">");
			out.println("<div class=\"widgetName\">Update Schedules</div>");
			out.println("</div>"); 
			out.println("<div class=\"widgetContent\">");
			out.println("<div class=\"dataTable\" id=\"dataTable_1\">");
			out.println("<div class=\"dataTableWrapper\">");
			out.println("<form method=\"post\" action=\"AddSchedules.do\" onsubmit=\"return(validateForm());\">");
			out.println("<input type=\"hidden\" value=\""+loggedUser.getUserID()+"\"/>");
			out.println("<input name=\"update\" type=\"submit\" value=\"Update Schedules\" />");
			out.println(" </form>");
			out.println("<span>");
			lastUpdatedScOrders();
			out.println("</span>");
			out.println("</div>");//  data table wrapper ends
			out.println("</div>"); // data table ends
			out.println("</div>"); // widget content ends
			out.println("</div>"); // widget ends
			out.println("</div>"); // get Plugin ends
			out.println("</div>"); // col width 33 ends
			}
		//if(isUserAllowedToViewScOrders(loggedUser)){
			out.println("<div class=\"col width-100 ui-sotable\" style=\" margin-bottom: 1px; padding-bottom: 1px; height: 50px; \">");
			out.println("<div class=\"sortable\" widgetid=\"widgetUserSettingsgetPlugun\">");
			out.println("<div id=\"widgetUserSettingsgetPlugin\" class=\"widget\">");
			out.println("<div class=\"widgetTop\">");
			out.println("<div class=\"widgetName\">Schedules Last Updated</div>");
			out.println("</div>"); 
			out.println("<div class=\"widgetContent\">");
			out.println("<div class=\"dataTable\" id=\"dataTable_1\">");
			out.println("<div class=\"dataTableWrapper\">");
			getScOrdersView();
			out.println("</div>");//  data table wrapper ends
			out.println("</div>"); // data table ends
			out.println("</div>"); // widget content ends
			out.println("</div>"); // widget ends
			out.println("</div>"); // get Plugin ends
			out.println("</div>"); // col width 33 ends
		//}
		if(isUserAllowedtoAddWorkOrders(loggedUser)){
			out.println("<div class=\"col width-100 ui-sotable\" style=\" margin-bottom: 1px; padding-bottom: 1px; height: 50px; \">");
			out.println("<div class=\"sortable\" widgetid=\"widgetUserSettingsgetPlugun\">");
			out.println("<div id=\"widgetUserSettingsgetPlugin\" class=\"widget\">");
			out.println("<div class=\"widgetTop\">");
			out.println("<div class=\"widgetName\">Update Work Orders</div>");
			out.println("</div>"); 
			out.println("<div class=\"widgetContent\">");
			out.println("<div class=\"dataTable\" id=\"dataTable_1\">");
			out.println("<div class=\"dataTableWrapper\">");
			out.println("<form method=\"post\" action=\"AddWorkOrders.do\" onsubmit=\"return(validateForm());\">");
			out.println("<input type=\"hidden\" value=\""+loggedUser.getUserID()+"\"/>");
			out.println("<input name=\"update\" type=\"submit\" value=\"Update Work Orders\" />");
			out.println(" </form>");
			out.println("<span>");
			lastUpdatedWorkOrders();
			out.println("</span>");
			out.println("</div>");//  data table wrapper ends
			out.println("</div>"); // data table ends
			out.println("</div>"); // widget content ends
			out.println("</div>"); // widget ends
			out.println("</div>"); // get Plugin ends
			out.println("</div>"); // col width 33 ends
			}
		//if(isUserAllowedToViewWorkOrders(loggedUser)){
			out.println("<div class=\"col width-100 ui-sotable\" style=\" margin-bottom: 1px; padding-bottom: 1px; height: 50px; \">");
			out.println("<div class=\"sortable\" widgetid=\"widgetUserSettingsgetPlugun\">");
			out.println("<div id=\"widgetUserSettingsgetPlugin\" class=\"widget\">");
			out.println("<div class=\"widgetTop\">");
			out.println("<div class=\"widgetName\">Work Orders Last Updated</div>");
			out.println("</div>"); 
			out.println("<div class=\"widgetContent\">");
			out.println("<div class=\"dataTable\" id=\"dataTable_1\">");
			out.println("<div class=\"dataTableWrapper\">");
			getWorkOrdersView();
			out.println("</div>");//  data table wrapper ends
			out.println("</div>"); // data table ends
			out.println("</div>"); // widget content ends
			out.println("</div>"); // widget ends
			out.println("</div>"); // get Plugin ends
			out.println("</div>"); // col width 33 ends
		//}
	}
	
	private boolean isUserAllowedtoAddNewOrders(User user){
		PreparedStatement stmt;
		boolean isTrue = false;
		try {
			stmt = conn.prepareStatement("select canAddNewOrder,canViewNewOrder from users where idusers=?");
			stmt.setInt(1, user.getUserID());
			ResultSet res = stmt.executeQuery();
			if(res.next()){
				if(res.getBoolean("canAddNewOrder")){
					isTrue = true;
				}else{
					isTrue = false;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isTrue;
	}
	
	private void lastUpdatedOrders(){
		try {
			PreparedStatement stmt = conn.prepareStatement("select time,username from newOrders inner join users  on newOrders.addedBy=users.idusers order by time desc limit 1;");
			ResultSet res = stmt.executeQuery();
			if(res.next()){
				out.println("&nbsp;&nbsp;&nbsp; Last Updated : "+res.getTimestamp("time").toLocaleString()+" By "+res.getString("username"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private boolean isUserAllowedToViewNewOrders(User user){
		boolean isTrue = false;
		try{
		PreparedStatement stmt = conn.prepareStatement("select canAddNewOrder,canViewNewOrder from users where idusers=?");
		stmt.setInt(1, user.getUserID());
		ResultSet res = stmt.executeQuery();
		if(res.next()){
			if(res.getBoolean("canViewNewOrder")){
				isTrue = true;
			}else{
				isTrue = false;
			}
		}
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return isTrue;
	}
	
	private void getNewOrdersView(){
		try {
			PreparedStatement stmt = conn.prepareStatement("call viewNewOrders()");
			ResultSet res = stmt.executeQuery();
			if(res.next()){
				out.println("<div id=\"dialog\" title=\"Orders have Updated\">");
				out.println("<p><a href=\"javascript:openPopUp('viewNewOrders.do')\"> Updated New Orders On "+res.getTimestamp("time").toLocaleString()+" By "+res.getString("username")+"</a></p>");
				out.println("</div>");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private boolean isUserAllowedtoAddScOrders(User user){
		PreparedStatement stmt;
		boolean isTrue = false;
		try {
			stmt = conn.prepareStatement("select canAddScOrder,canViewScOrder from users where idusers=?");
			stmt.setInt(1, user.getUserID());
			ResultSet res = stmt.executeQuery();
			if(res.next()){
				if(res.getBoolean("canAddScOrder")){
					isTrue = true;
				}else{
					isTrue = false;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isTrue;
	}
	
	private void lastUpdatedScOrders(){
		try {
			PreparedStatement stmt = conn.prepareStatement("select time,username from ScOrders inner join users  on ScOrders.addedBy=users.idusers order by time desc limit 1;");
			ResultSet res = stmt.executeQuery();
			if(res.next()){
				out.println("&nbsp;&nbsp;&nbsp; Last Updated : "+res.getTimestamp("time").toLocaleString()+" By "+res.getString("username"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private boolean isUserAllowedToViewScOrders(User user){
		boolean isTrue = false;
		try{
		PreparedStatement stmt = conn.prepareStatement("select canAddScOrder,canViewScOrder from users where idusers=?");
		stmt.setInt(1, user.getUserID());
		ResultSet res = stmt.executeQuery();
		if(res.next()){
			if(res.getBoolean("canViewScOrder")){
				isTrue = true;
			}else{
				isTrue = false;
			}
		}
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return isTrue;
	}
	
	private void getScOrdersView(){
		try {
			PreparedStatement stmt = conn.prepareStatement("call viewScOrders()");
			ResultSet res = stmt.executeQuery();
			if(res.next()){
				out.println("<div id=\"dialog\" title=\"Orders have Updated\">");
				out.println("<p><a href=\"javascript:openPopUp('ViewScOrders.do')\"> Updated Schedules On "+res.getTimestamp("time").toLocaleString()+" By "+res.getString("username")+"</a></p>");
				out.println("</div>");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private boolean isUserAllowedtoAddWorkOrders(User user){
		PreparedStatement stmt;
		boolean isTrue = false;
		try {
			stmt = conn.prepareStatement("select canAddWorkOrder,canViewWorkOrder from users where idusers=?");
			stmt.setInt(1, user.getUserID());
			ResultSet res = stmt.executeQuery();
			if(res.next()){
				if(res.getBoolean("canAddWorkOrder")){
					isTrue = true;
				}else{
					isTrue = false;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isTrue;
	}
	
	private void lastUpdatedWorkOrders(){
		try {
			PreparedStatement stmt = conn.prepareStatement("select time,username from workOrders inner join users  on workOrders.addedBy=users.idusers order by time desc limit 1;");
			ResultSet res = stmt.executeQuery();
			if(res.next()){
				out.println("&nbsp;&nbsp;&nbsp; Last Updated : "+res.getTimestamp("time").toLocaleString()+" By "+res.getString("username"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private boolean isUserAllowedToViewWorkOrders(User user){
		boolean isTrue = false;
		try{
		PreparedStatement stmt = conn.prepareStatement("select canAddWorkOrder,canViewWorkOrder from users where idusers=?");
		stmt.setInt(1, user.getUserID());
		ResultSet res = stmt.executeQuery();
		if(res.next()){
			if(res.getBoolean("canViewWorkOrder")){
				isTrue = true;
			}else{
				isTrue = false;
			}
		}
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return isTrue;
	}
	
	private void getWorkOrdersView(){
		try {
			PreparedStatement stmt = conn.prepareStatement("call viewWorkOrders()");
			ResultSet res = stmt.executeQuery();
			if(res.next()){
				out.println("<div id=\"dialog\" title=\"Work Orders have Updated\">");
				out.println("<p><a href=\"javascript:openPopUp('ViewWorkOrders.do')\"> Updated Schedules On "+res.getTimestamp("time").toLocaleString()+" By "+res.getString("username")+"</a></p>");
				out.println("</div>");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
