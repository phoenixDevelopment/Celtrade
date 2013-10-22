package com.Phoenix.dashboard;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.DefaultEditorKit.CutAction;

import org.omg.CORBA.RepositoryIdHelper;

import com.Phoenix.data.Department;
import com.Phoenix.data.Issue;
import com.Phoenix.data.OpenIssue;
import com.Phoenix.data.User;

/**
 * Servlet implementation class main
 */
@WebServlet("/main.do")
public class main extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private Connection conn;
    private PrintWriter out;
    private User loggedUser = new User();
    private int currentPage = 0;
    private boolean logged = false;
    //private final String HOST = "192.168.0.105";
    private final String HOST = "localhost";
    private Display display;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public main() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.setIntHeader("Refresh", 5);
		out = response.getWriter();
		display = new Display(out);
		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");
		connect();
		String session = request.getSession().getId();
		getUsername(session);
		String thought = getThoughts();
		request.getSession().setAttribute("thought", thought);
		if(!logged){
			response.sendRedirect("/Celtrade");
		}else{
		ServletContext context = this.getServletContext();
		request.getSession().setAttribute("loggedUser", loggedUser);
		context.setAttribute("databaseConn", conn);
		request.getSession().setAttribute("logged", logged);
		request.getSession().setAttribute("display", display);
		request.getSession().setAttribute("cPage", currentPage);
		RequestDispatcher reqD = context.getRequestDispatcher("/main.jsp");
		reqD.forward(request, response);
		//display.getHeader(currentPage);
		//display.getBody(this.loggedUser,currentPage); 
		}
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
	
	private void connect(){
		try{
			String connection = "jdbc:mysql://"+HOST+"/celtrade";
			java.sql.Connection conn = null;
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			this.conn = DriverManager.getConnection(connection,"celtrade","celtrade24");
			if(this.conn != null){
				//out.println("Connected  to  database");
			}
	}catch(Exception e){
			out.println(e.getMessage());
		}
	}
	
	private void getUsername(String sess){
		try {
			PreparedStatement stmt = conn.prepareStatement("select idusers,userName from userSessions where sessionID=?");
			stmt.setString(1,sess);
			ResultSet res = stmt.executeQuery();
			if(res.next()){
				this.loggedUser = new User(res.getShort("idusers"), res.getString("userName"));
				this.logged = true;
			} 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String getThoughts(){
		String thought = "";
		try {
			PreparedStatement stmt = conn.prepareStatement("select thoughtOfDay from Thought order by idThought desc limit 1;");
			ResultSet res = stmt.executeQuery();
			if(res.next()){
				thought = res.getString("thoughtOfDay");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return thought;
	}
	
	private String getTimeSince(Date date){
		String data = "";
		Date now = new Date(System.currentTimeMillis());
		long diff = now.getTime() - date.getTime();
		long diffMins = diff / (60 * 1000) % 60;
		long diffHours = diff / (60 * 60 * 1000) % 24;
		long diffdays = diff /(24 * 60 * 60 * 1000);
		
		if(diffdays !=0){
			data += diffdays+"D : ";
		}else{
			data = "";
		}if(diffHours != 0){
			data += diffHours + "H : ";
		}else{
			data += "";
		}
		if(diffMins != 0){
			data += diffMins + "M ";
		}else{
			data += "";
		}
		
		return data;
	}
	
	public  class Display{
		private  PrintWriter out;
		private ArrayList<Department> deps = new ArrayList<Department>();
		private Issue issue;
		private int issueCreaterID;
		
		public Display(PrintWriter wr){
			this.out = wr;
		}
		
		public void setWriter(PrintWriter writer){
			this.out = writer;
		}
		
		public void setUsers(ArrayList<Department> arr){
			this.deps = arr;
		}
		
		public void setIssue(Issue issue){
			this.issue = issue;
		}
		
		public void setIssueCreaterID(int issueCreaterID) {
			this.issueCreaterID = issueCreaterID;
		}
		
		
		public  void getHeader(int cpage){
			out.println("<!--[if lt IE 9 ]>");
			out.println("<html class=\"old-ie\"> <![endif]-->");
			out.println("<!--[if (gte IE 9)|!(IE)]><!-->");
			out.println("<html>");
			out.println("<!--<![endif]-->");
			out.println("<head>");
			out.println("<title>Celtrade DashBoard</title>");
			out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">");
			out.println("<meta name=\"generator\" content=\"Celtrade DashBoard\">");
			out.println("<link rel=\"shortcut icon\" href=\"images/favicon.ico\">");
			out.println("<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=IE8\">    <!--[if lt IE 9]>");
			out.println("<script language=\"javascript\" type=\"text/javascript\" src=\"js/excanvas.min.js\"></script>");
			out.println("<![endif]-->");
			out.println("<style type=\"text/css\">");
			out.println("@import url(\"css/display.css\");");
			out.println("</style>\");");
			out.println("<script type=\"text/javascript\" src=\"js/script.js\"></script>");
			out.println("<!--[if IE]>");
			out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"themes/default/ieonly.css?cb=029d92d07a20e9f98919db9e7d20e77a\" />");
			out.println("<![endif]-->");
			out.println("<style type=\"text/css\">body {");
			out.println("display: none;");
			out.println("}</style>");
			if(cpage == 0){
			out.println("<script type=\"text/javascript\" src=\"http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js\"></script>");
			out.println("<script type=\"text/javascript\">");
			out.println("$(document).ready(function(){");
			out.println("$(\"#ajaxLoading\").hide();");
			out.println("$(\"#dashboardOnWidgetArea\").load('ListDeps.do');");
			out.println("setTimeout(function(){");
			out.println("$(\"#dashboardOnWidgetArea\").load('ListDeps.do');");
			out.println("},5000);");
			out.println("});");
			out.println("function validateForm(){");
			out.println("var res = confirm(\"Are You Sure want to Update?\");");
			out.println("return res;");
			out.println("}");
			out.println("function openPopUp(url){");
			out.println("winpops=window.open(url,\"New Orders\",\"width=400,height=300\");");
			out.println("}");
			out.println("</script>");
			}
			if(cpage == 1){
				out.println("<script type=\"text/javascript\">");
				out.println("function validateForm(){");
				out.println("if(document.form1.issueName.value == \"\"){");
				out.println("alert(\"Issue Name is required!\");");
				out.println("document.form1.issueName.focus();");
				out.println("return false;");
				out.println("}");
				out.println("if(document.form1.issueType.value == \"\"){");
				out.println("alert(\"Issue Type is required!\");");
				out.println("document.form1.issueType.focus();");
				out.println("return false;	");
				out.println("}");
				out.println("if(document.form1.openFor.value == \"-1\"){");
				out.println("alert(\"Opened For  is required!\");");
				out.println("document.form1.openFor.focus();");
				out.println("return false;");
				out.println("}");
				out.println("var res = confirm(\"Are You Sure want to Update?\");");
				out.println("return res;");
				out.println("}");
				out.println("</script>");
			}
			if(cpage == 2){
				out.println("<script type=\"text/javascript\">");
				out.println("function validateForm(){");
				if(this.issueCreaterID == loggedUser.getUserID()){
					out.println("if(document.update.statusChange.value == \"-1\"){");
					out.println("alert(\"Status  is required!\");");
					out.println("document.update.statusChange.focus();");
					out.println("return false;	");
					out.println("}");
				}
				out.println("if(document.update.comments.value == \"\"){");
				out.println("alert(\"Comment is required!\");");
				out.println("document.update.comments.focus();");
				out.println("return false;");
				out.println("}");
				out.println("var res = confirm(\"Are You Sure want to Update?\");");
				out.println("return res;");
				out.println("}");
				out.println("</script>");
			}
			if(cpage == 3){
				out.println("<script type=\"text/javascript\" src=\"http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js\"></script>");
				out.println("<script type=\"text/javascript\">");
				out.println("$(document).ready(function(){");
				out.println("$(\"#ajaxLoading\").hide();");
				out.println("$(\"#dashboardOnWidgetArea\").load('ListDepsClosed.do');");
				out.println("setTimeout(function(){");
				out.println("$(\"#dashboardOnWidgetArea\").load('ListDepsClosed.do');");
				out.println("},5000);");
				out.println("});");
				out.println("</script>");
				}
			out.println("</head>");
		}

		public  void getBody(User logged,int cpage){
				out.println("<body style=\"display:block\">");
			out.println("<div id=\"root\">");
			/*out.println("<div id=\"topBars\">");
			out.println("<div id=\"topRightBar\">");
			getWeather();
			out.println("</div>");
			out.println("</div>");*/
			
			out.println("<div id=\"header\">");
			out.println("<span id=\"logo\">");
			out.println("<a href=\"Celtrade\" title=\"Powered by NetLine Solutions\" style=\"text-decoration: none;\">");
			out.println("<img src=\"images/celtrade24.jpg\" width=\"800\" alt=\"Powered by Netline Solutions\" style=\"margin-left: 10px\" height=\"100\" class=\"ie-hide\">");
			out.println("<!--[if lt IE 9]>");
			out.println("<img src=\"\" alt=\"Powered by Netline Solutions\" style='margin-left:10px' height='50'/>");
			out.println("<![endif]--></a>");
			
			out.println("<div style=\"display: block !important; width: 320px; text-align: center; font-family: sans-serif; font-size: 12px; float:right;\"><a href=\"http://www.wunderground.com/cgi-bin/findweather/getForecast?query=zmw:00000.11.71624&amp;bannertypeclick=wu_clean2day\" title=\"Mississauga, Ontario Weather Forecast\" target=\"_blank\"><img src=\"http://weathersticker.wunderground.com/weathersticker/cgi-bin/banner/ban/wxBanner?bannertype=wu_clean2day_metric_cond&amp;airportcode=CYYZ&amp;ForcedCity=Mississauga&amp;ForcedState=Canada&amp;wmo=71624&amp;language=EN\" alt=\"Find more about Weather in Mississauga, CA\" width=\"300\"></a><br><a href=\"http://www.wunderground.com/cgi-bin/findweather/getForecast?query=zmw:00000.11.71624&amp;bannertypeclick=wu_clean2day\" title=\"Get latest Weather Forecast updates\" style=\"font-family: sans-serif; font-size: 12px\" target=\"_blank\">Click for weather forecast</a></span>");

			out.println("</div>");
			
			out.println("<noscript>");
			out.println("&lt;div id=\"javascriptDisabled\"&gt;JavaScript must be enabled in order for you to use Celtrade in standard view.&lt;br /&gt;However, it seems JavaScript is either disabled or not supported by your browser.&lt;br /&gt;To use standard view, enable JavaScript by changing your browser options, then &lt;a href=\"\"&gt;try again&lt;/a&gt;.&lt;br /&gt;&lt;/div&gt;");
			out.println("</noscript></div>");
			out.println("<ul class=\"nav\">");
			if(cpage == 0){
				out.println("<li id=\"dashboard\" class=\"sfActive sfHover\"><a href=\"main.do\">  DashBoard </a></li>");
			}else{
				out.println("<li id=\"dashboard\"><a href=\"main.do\"> DashBoard </a></li>");
			}if(cpage == 1){
				out.println("<li id=\"dashboard\" class=\"sfActive sfHover\"><a href=\"IssueCreate.do\">  Create Issue  </a></li>"); // Navigation
			}else{
				out.println("<li id=\"dashboard\"><a href=\"IssueCreate.do\"> Create Issue  </a></li>"); // Navigation
			}if(cpage == 3){
				out.println("<li id=\"dashboard\" class=\"sfActive sfHover\"><a href=\"ClosedIssues.do\">  Closed Issues  </a></li>"); // Navigation
			}else{
				out.println("<li id=\"dashboard\"><a href=\"ClosedIssues.do\"> Closed Issues </a></li>"); // Navigation
			}
			out.println("</ul>");
			out.println("<div class=\"page\">");
			out.println("<div class=\"pageWrap\">");
			out.println("<div class=\"nav_sep\"></div>");
			out.println("<div class=\"top_controls\">");
			out.println("<span id=\"header_message\" class=\"header_info\">");
			out.println("<span class=\"header_short\">");
			out.println("Welcome "+logged.getUserName());
			out.println("</span>");
			out.println("<span class=\"header_full\">");
			out.println("Welcome "+logged.getUserName()+"&nbsp;&nbsp&nbsp;"); 
			out.println("<a href=\"settings.do\">Change Password</a>&nbsp;&nbsp;");
			out.println("<a href=\"Logout.do\">Log Out</a>");
			out.println("</span>");
			out.println("</span>");
			out.println("<div id=\"loadingError\" style=\"display: none;\">Oops… problem during the request, please try again.</div>");
			out.println("</div>");// end Top Controls
			if(cpage == 0){
			out.println("<div id=\"ajaxLoading\" style=\"display: block;\">");
			}else{
				out.println("<div id=\"ajaxLoading\" style=\"display: none;\">");
			}
			out.println("<div class=\"loadingPiwik\"><img src=\"images/LoadingBlue.gif\" alt=\"\"> Loading data...");
			out.println("<div class=\"loadingSegment\" style=\"display: none;\">Processing  data may take a few minutes...</div> ");
			out.println("</div></div>"); // ajax loading Ends
			out.println("<div id=\"content\" class=\"home\" style=\"display: block;\">");
			out.println("<div id=\"dashboard\">");
			out.println("<div class=\"clear\"></div>"); 
			out.println("<div id=\"dashboardOnWidgetArea\">");
			/// Display all data Here
			if(cpage == 1){
				createIssueBody();
			}
			if(cpage ==2){
				viewIssueBody();
			}
			out.println("</div>"); // dashboard widget ends  
			out.println("</div>"); // dashboard ends 
			out.println("</div>"); // end content 
			out.println("<div class=\"clear\"></div>"); 
			out.println("</div>"); // end pageWrap
			out.println("</div>"); // end page
			out.println("</div>");
			out.println("</body");
			
		}
		
		private void createIssueBody(){
			//out.println("<div class=\"col width-33 ui-sortable\"></div>");
			out.println("<div class=\"col width-100 ui-sortable\">");
			out.println("<div class=\"dataTableWrapper\">");
			out.println("<div class=\"widget\">");
			out.println("<div class=\"widgetContent\">");
			out.println("<form id=\"form1\" name=\"form1\" method=\"POST\" action=\"AddIssue.do\" onsubmit=\"return(validateForm());\">");
			out.println("<table  border=\"0\" cellpadding=\"10\" class=\"dataTable dataTableActions\">");
			out.println("<tr>");
			out.println("<th scope=\"col\" class=\"widgetTop\"><h2 style=\"cursor:pointer\">Add New Issue<h2></th>");
			out.println("<th class=\"widgetTopCreate\"></th>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td class=\"label labelodd\" style=\"width:25%\"><label>Issue :</label></td>");
			out.println("<td><input name=\"issueName\" type=\"text\" /></td>");
			out.println("</tr>");
			out.println(" <tr>");
			out.println("<td class=\"label labeleven\" style=\"width:25%\"><label>Detail :</label></td>");
			out.println("<td><textarea name=\"issueType\" type=\"textarea\" rows=\"5\" cols=\"30\"></textarea></td>");
			out.println("</tr>");
			//out.println(" <tr>");
			//out.println("<td class=\"label labelodd\"><label>Issue Open By :</label>");
			//out.println("</td>");
			//out.println("<td><select name=\"openBy\">");
			//out.println("<option value=\"1\">User1</option>");
			out.println("<input name=\"openBy\" type=\"hidden\" value=\""+loggedUser.getUserID()+"\"/>");
			//out.println("</select></td>");
			//out.println("</tr>");
			out.println("<tr>");
			out.println("<td class=\"label labeleven\" style=\"width:25%\"><label>Issue Priority :</label>");
			out.println("</td>");
			out.println("<td><select name=\"priority\">");
			out.println("<option value=\"-1\" selected>Select</option>"); //print from database
			//out.println("<option value="2">User2</option>");
				getPriority();
			out.println("</select>");
			out.println("</td>");
			out.println("<tr>");
			out.println("<td class=\"label labeleven\" style=\"width:25%\"><label>Issue Open For :</label>");
			out.println("</td>");
			out.println("<td><select name=\"openFor\">");
			out.println("<option value=\"-1\" selected>Select</option>"); //print from database
			//out.println("<option value="2">User2</option>");
			for(int i=0;i<deps.size();i++){
				out.println("<option value=\""+deps.get(i).getDepID()+"\">"+deps.get(i).getDepName()+"</option>");
			}
			deps.clear();
			out.println("</select>");
			out.println("</td>");
			out.println(" </tr>");
			out.println(" <tr>");
			out.println("<td><input name=\"submit\" type=\"submit\" value=\"Create Issue\" class=\"button\"/></td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("</form>");
			out.println("</div>");
			out.println("</div>");
			out.println("</div>");
			out.println("</div>");
		}
		
		private void viewIssueBody(){
			out.println("<div class=\"home\">");
			out.println("<table class=\"dataTable\" width=\"100%\" border=\"0\" cellpadding=\"10\" style=\"width:100%\">");
			out.println("<tr class=\"labelodd\">");
			out.println("<td class=\"label columnodd\" style=\"width:25%\">Issue : </td>");
			out.println("<td class=\"column columnodd\">"+issue.getIssueName()+"</td>");
			out.println("</tr>");
			out.println("<tr class=\"labeleven\">");
			out.println("<td class=\"label columneven\" style=\"width:25%\">Detail : </td>");
			out.println("<td class=\"column columneven\" style=\"height:220px\">"+issue.getIssueType()+"</td>");
			out.println("</tr>");
			out.println("<tr class=\"labelodd\">");
			out.println("<td class=\"label columnodd\" style=\"width:25%\">Priority :</td>");
			out.println("<td class=\"column columnodd\">"+issue.getPriority()+"</td>");
			out.println("</tr>");
			out.println("<tr class=\"labeleven\">");
			out.println("<td class=\"label columneven\" style=\"width:25%\">Status :</td>");
			out.println("<td class=\"column columneven\">"+issue.getStatus()+"</td>");
			out.println("</tr>");
			out.println("<tr class=\"labelodd\">");
			out.println("<td class=\"label columnodd\" style=\"width:25%\">Comments</td>");
			out.println("<td class=\"column columnodd\">");
			out.println("</td>");
			out.println("</tr>");
			
			for(int i=0;i<issue.getComment().size();i++){
				if(i % 2 == 0){
					out.println("<tr class=\"labeleven\">");
					out.println("<td class=\"label columneven\" style=\"width:25%\"></td>");
					Date commented  = issue.getComment().get(i).getCommentedAt();
					if(issue.getComment().get(i).getCommentedAt() != null){
						out.println("<td class=\"column columneven\" style=\"background-color:#CCC\"><span style=\"color:#3CC;background-color:inherit\">"+issue.getComment().get(i).getUser().getUserName()+"&nbsp;&nbsp;&nbsp;</span> "+issue.getComment().get(i).getComment()+" <span class=\"annotationView\"> "+commented.toLocaleString()+"</span></td>");
					}
					out.println("</tr>");
				}else{
					out.println("<tr class=\"labelodd\">");
					out.println("<td class=\"label columnodd\" style=\"width:25%\"></td>");
					Date commented  = issue.getComment().get(i).getCommentedAt();
					if(issue.getComment().get(i).getCommentedAt() != null){
						out.println("<td class=\"column columnodd\" style=\"background-color:#CCC\"><span style=\"color:#3CC;background-color:inherit\">"+issue.getComment().get(i).getUser().getUserName()+"&nbsp;&nbsp;&nbsp;</span> "+issue.getComment().get(i).getComment()+" <span class=\"annotationView\"> "+commented.toLocaleString()+"</span></td>");
					}
					out.println("</tr>");
				}
			}
			
			out.println(" <tr class=\"labelodd\">");
			out.println("<form action=\"UpdateIssue.do\" method=\"post\" name=\"update\" onsubmit=\"return(validateForm())\">");
			if(this.issueCreaterID == loggedUser.getUserID() || issue.getStatus().equals("OPEN")){
				out.println("<td class=\"label columneven\" style=\"width:25%\"><label>Change Status : </label></td>");
				out.println("<td class=\"column columneven\"><select name=\"statusChange\"><option value=\"-1\" selected=\"selected\">Select</option>");
				getStatus();
				out.println("</select></td>");
				out.println("<input name=\"action\" type=\"hidden\" value=\"addCmtStatus\" />");
			}else{
				out.println("<input name=\"action\" type=\"hidden\" value=\"addComment\" />");
			}
			out.println("</tr>");
			out.println("<tr class=\"labeleven\">");
			out.println("<td class=\"label columneven\" style=\"width:25%\"><label>Comment : </label></td>");
			out.println("<td class=\"column columneven\" style=\"height:auto\">");
			out.println("<input name=\"issueID\" type=\"hidden\" value=\""+issue.getIssueID()+"\" />");
			if(issue.getStatus().equals("OPEN")){
			out.println("<textarea name=\"comments\" rows=\"5\" cols=\"90\"></textarea>");
			}else{
				out.println("<textarea name=\"comments\" rows=\"5\" cols=\"90\" disabled=\"true\"></textarea>");
			}
			out.println("</td>");
			out.println("</tr>");
			out.println("<tr class=\"labelodd\">");
			out.println("<td class=\"label columneven\" style=\"width:25%\"></td>");
			out.println("<td class=\"column columneven\">");
			out.println("<input name=\"update\" type=\"submit\" value=\"Update\" />");
			out.println("</td>");
			out.println("</tr>");
			out.println(" </form>");
			out.println("</table>");
			
		}
		
		private void getStatus(){
			try {
				PreparedStatement stmt = conn.prepareStatement("select distinct type,idstatus from Status;");
				ResultSet res = stmt.executeQuery();
				while(res.next()){
					out.println("<option value=\""+res.getString("idstatus")+"\">"+res.getString("type")+"</option>");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		private void getPriority(){
			try {
				PreparedStatement stmt = conn.prepareStatement("select distinct priority,idIssuePriority from issuePriority;");
				ResultSet res = stmt.executeQuery();
				while(res.next()){
					out.println("<option value=\""+res.getString("idIssuePriority")+"\">"+res.getString("priority")+"</option>");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		private void getWeather(){
			out.println("<script type=\"text/javascript\" src=\"http://voap.weather.com/weather/oap/USGA0028?template=GENXH&par=3000000007&unit=0&key=twciweatherwidget\">");
			out.println("</script>");
		}
		
		
	}
	
}
