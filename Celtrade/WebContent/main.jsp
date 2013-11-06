<?xml version="1.0" encoding="UTF-8" ?>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@page import="com.Phoenix.data.*" %>
    <%@page import="java.util.Date" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="generator" content="Celtrade DashBoard">
<link rel="shortcut icon" href="images/favicon.ico">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=IE8">    <!--[if lt IE 9]>
<script language="javascript" type="text/javascript" src="js/excanvas.min.js"></script>
<![endif]-->
<style type="text/css">
@import url("css/display.css");
</style>
<script type="text/javascript" src="js/script.js"></script>
<!--[if IE]>
			<link rel="stylesheet" type="text/css" href="themes/default/ieonly.css?cb=029d92d07a20e9f98919db9e7d20e77a" />
			<![endif]-->
		<style type="text/css">
		 .body {
		 	display: none;
		 }
		 </style>
		
		 <script type="text/javascript">
		 	var count = 0;
			var title = "";
			
		 function checkNewIssues(){
			 var xmlhttp;
		 if (window.XMLHttpRequest)
		   {// code for IE7+, Firefox, Chrome, Opera, Safari
		   xmlhttp=new XMLHttpRequest();
		   }
		 else
		   {// code for IE6, IE5
		   xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
		   }
		 xmlhttp.open("GET","/Celtrade/newIssueCreated.do",true);
		 xmlhttp.send();
		 
		 xmlhttp.onreadystatechange=function()
		  {
		  if (xmlhttp.readyState==4 && xmlhttp.status==200)
		    {
		    	if(xmlhttp.responseText.toUpperCase() == "OK"){
		    		
		    			if(count % 2){
		    				title = "New issue!" + count;
		    			}else{
		    				title = "Celtrade CP24 DashBoard" + count;
		    			}
		    		
		    	}else{
		    		   title = "Celtrade CP24 DashBoard";
		    	}
		    	
		    	document.title = title;
		    }
		  }
		 	count++;
		 }
		 </script>
			<%
			ArrayList<String> status = new ArrayList<String>();
			ServletContext  context = this.getServletContext();
			int cpage = (Integer) request.getSession().getAttribute("cPage");
			User loggedUser = (User) request.getSession().getAttribute("loggedUser");
			String thought = (String) request.getSession().getAttribute("thought");
			int issueCreaterID;
			try{
			issueCreaterID = (Integer) request.getSession().getAttribute("issueCreaterID");
			}catch(NullPointerException ex){
				issueCreaterID =0;
			}
			Issue issue = (Issue) request.getSession().getAttribute("issue");
			status = (ArrayList<String>) request.getSession().getAttribute("status");
			ArrayList<OpenIssue> openIssues = new ArrayList<OpenIssue>();
			int depid = 0;
			int pages = 0;
			int totalPages = 0;
			if(cpage == 5){
				depid = (Integer)request.getSession().getAttribute("depID");
				pages = (Integer) request.getSession().getAttribute("startPage");
				totalPages = (Integer)request.getSession().getAttribute("totalPages");
			}
			openIssues = (ArrayList<OpenIssue>)request.getSession().getAttribute("issueList");
			
 	if(cpage == 0){
		out.println("<script type=\"text/javascript\" src=\"http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js\"></script>");
		out.println("<script type=\"text/javascript\">");
		out.println("$(document).ready(function(){");
		out.println("$(\"#ajaxLoading\").hide();");
		out.println("$(\"#dashboardOnWidgetArea\").load('ListDeps.do');");
		out.println("$.ajaxSetup({ cache: false });");
		out.println("setInterval(function(){$('#dashboardOnWidgetArea').load('ListDeps.do');},3000);");
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
			if(issueCreaterID == loggedUser.getUserID()){
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
		if(cpage == 4){
			out.println("<script type=\"text/javascript\">");
			out.println("function validateForm(){");
			out.println("if(document.form1.password.value == \"\"){");
			out.println("alert(\"Password is required!\");");
			out.println("document.form1.password.focus();");
			out.println("return false;");
			out.println("}");
			out.println("if(document.form1.confPass.value == \"\"){");
			out.println("alert(\"Confirm Password  is required!\");");
			out.println("document.form1.confPass.focus();");
			out.println("return false;	");
			out.println("}");
			out.println("if(document.form1.pass.value != document.form1.confPass.value){");
			out.println("alert(\"Passwords  do not match!\");");
			out.println("document.form1.password.focus();");
			out.println("return false;");
			out.println("}");
			out.println("var res = confirm(\"Are You Sure want to Update?\");");
			out.println("return res;");
			out.println("}");
			out.println("</script>");
			}
 	%>
</head>
<body style="display:block" onload="setInterval(function(){checkNewIssues();}, 1000);">
 			<div id="root">
			<!-- /*out.println("<div id=\"topBars\">
			out.println("<div id=\"topRightBar\">");
			getWeather();
			out.println("</div>");
			out.println("</div>");*/ -->
			<div id="header">
			<span id="logo">
			<a href="Celtrade" title="Powered by NetLine Solutions" style="text-decoration: none;">
			<img src="images/celtrade24.jpg" width="800" alt="Powered by Netline Solutions" style="margin-left: 10px" height="100" class="ie-hide">
			<!--[if lt IE 9]>
			<img src="" alt="Powered by Netline Solutions" style='margin-left:10px' height='50'/>
			<![endif]--></a>
			
			<div style="display: block !important; width: 320px; text-align: center; font-family: sans-serif; font-size: 12px; float:right;"><a href="http://www.wunderground.com/cgi-bin/findweather/getForecast?query=zmw:00000.11.71624&amp;bannertypeclick=wu_clean2day" title="Mississauga, Ontario Weather Forecast" target="_blank"><img src="http://weathersticker.wunderground.com/weathersticker/cgi-bin/banner/ban/wxBanner?bannertype=wu_clean2day_metric_cond&amp;airportcode=CYYZ&amp;ForcedCity=Mississauga&amp;ForcedState=Canada&amp;wmo=71624&amp;language=EN" alt="Find more about Weather in Mississauga, CA" width="300"></a><br><a href="http://www.wunderground.com/cgi-bin/findweather/getForecast?query=zmw:00000.11.71624&amp;bannertypeclick=wu_clean2day" title="Get latest Weather Forecast updates" style="font-family: sans-serif; font-size: 12px" target="_blank">Click for weather forecast</a></span>

  </div>
			    <noscript>
			&lt;div id="javascriptDisabled"&gt;JavaScript must be enabled in order for you to use Celtrade in standard view.&lt;br &gt;However, it seems JavaScript is either disabled or not supported by your browser.&lt;br /&gt;To use standard view, enable JavaScript by changing your browser options, then &lt;a href=""&gt;try again&lt;/a&gt;.&lt;br /&gt;&lt;/div&gt;"
			</noscript></div>
			<ul class="nav">
			<%
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
			%>
			</ul>
			<div class="page">
			<div class="pageWrap">
			<div class="nav_sep">
			<marquee behavior="alternate" style="padding-top:10px;color:red;"><% out.println(thought); %></marquee>
			</div>
			<div class="top_controls">
			<span id="header_message" class="header_info">
			<span class="header_short">
			Welcome <%out.println(loggedUser.getUserName());%>
			</span>
			<span class="header_full">
			Welcome <%out.println(loggedUser.getUserName());%>&nbsp;&nbsp&nbsp;
			<a href="Settings.do">Change Password</a>&nbsp;&nbsp;
			<a href="Logout.do">Log Out</a>
			</span>
			</span>
			<div id="loadingError" style="display: none;">Oops… problem during the request, please try again.</div>
			</div> <!--   end Top Controls -->
			<div id="ajaxLoading" style="display: none;">
			<div class="loadingPiwik"><img src="images/LoadingBlue.gif" alt="Loading Data ...."/>
			<div class="loadingSegment" style="display: none;">Processing  data may take a few minutes...</div> 
			</div></div> <!--  ajax loading Ends -->
			<div id="content" class="home" style="display: block;">
			<div id="dashboard">
			<div class="clear"></div> 
			<div id="dashboardOnWidgetArea">
			<!-- Display all data Here -->
			<%
			if(cpage == 1){
			//	createIssueBody();
			}
			if(cpage ==2){ %>
				<div class="home">
			<table class="dataTable" width="100%" border="0" cellpadding="10" style="width:100%">
			<tr class="labelodd">
			<td class="label columnodd" style="width:25%">Issue : </td>
			<%
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
			if(issueCreaterID == loggedUser.getUserID() /*|| issue.getStatus().equals("OPEN")*/){
				out.println("<td class=\"label columneven\" style=\"width:25%\"><label>Change Status : </label></td>");
				out.println("<td class=\"column columneven\"><select name=\"statusChange\"><option value=\"-1\" selected=\"selected\">Select</option>");
				for(int i=0;i<status.size();i++){
					out.println(status.get(i));
				}
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
			%>
			</td>
			</tr>
			<tr class="labelodd">
			<td class="label columneven" style="width:25%"></td>
			<td class="column columneven\">
			<input name="update" type="submit" value="Update" />
			</td>
			</tr>
			</form>
			</table>
		
		<%	}
			if(cpage == 4){
				%>
				<div class="col width-100 ui-sortable">
<div class="dataTableWrapper">
<div class="widget">
<div class="widgetContent">
<form id="form1" name="form1" method="post" action="ChangePass.do" onsubmit="return(validateForm());">
<table border="0" cellpadding="10" class="dataTable dataTableActions">
<tbody><tr>
<th scope="col" class="widgetTop"><h2 style="cursor:pointer">Change Password</h2><h2></h2></th>
<th class="widgetTopCreate"></th>
</tr>
<tr>
<td class="label labelodd" style="width:25%"><label>Username :</label></td>
<td><input name="username" type="text" value=<%out.println(loggedUser.getUserName()); %> disabled="true"></td>
</tr>
 <tr>
<td class="label labeleven" style="width:25%"><label>Password :</label></td>
<td><input name="password" type="password" /></td>
</tr>

<tr>
<td class="label labeleven" style="width:25%"><label>Confirm Password :</label></td>
<td><input name="confpasswrd" type="password" /></td>
</tr>
 <tr>
<td><input name="submit" type="submit" value="Change Password" class="button"></td>
</tr>
</tbody></table>
</form>
</div>
</div>
</div>
</div>	
		<%
			}if(cpage == 5){
			%>
			
			<div id="content" class="home" style="display: block;">
							<h2 style="cursor: pointer;">
								<%
								if(openIssues.size() >=1){
								out.println(openIssues.get(0).getOpenForDep()); 
								}
								%>&nbsp;&nbsp;&nbsp;
							</h2>
							<div class="dataTable" width="100%" style="width:100%;">
									<table cellspacing="0" class="dataTable dataTableActions">
										<thead>
											<tr>
												<th class="sortable first label" id="label">
													<div id="thDIV">Issues</div>
												</th>
												<th class="sortable " id="nb_hits">
													
													<div id="thDIV">Priority</div>
												</th>
												
												<th class="sortable  columnSorted" id="nb_visits">
													<div id="thDIV">Status</div></th>
												<th class="sortable  columnSorted" id="nb_visits">
													<div id="thDIV">Owner</div></th>
												<th class="sortable " id="bounce_rate">
												
													<div id="thDIV">Opened HH:MM</div>
												</th>
											</tr>
										</thead>
										<tbody>
	<% for(int i =0;i<openIssues.size();i++){  %>
											<tr class="actionsDataTable level0">
												<td class="label labelodd" style="min-width: 145px;"><div
														class="dataTableRowActions"
														style="height: 27px; margin-left: 190px; display: none;">
													</div> <span
															class="label"><span class="label"><a href="ViewIssue.do?id=<%out.println(openIssues.get(i).getIssueID());%>"><%out.println(openIssues.get(i).getIssueName());%></a>
						</span></td>
										<%
										if(openIssues.get(i).getPriority().equals("LOW")){
											out.println("<td style=\"background-color:#0F0\"></td>");
										}
										if(openIssues.get(i).getPriority().equals("MED")){
											out.println("<td  style=\"background-color:#FF0\"></td>");
										}
										if(openIssues.get(i).getPriority().equals("HIGH")){
											out.println("<td  style=\"background-color:#F00\"></td>");
										}
										%>
												<td><%out.println(openIssues.get(i).getStatus()); %></td>
												<td><%out.println(openIssues.get(i).getOpenByUser()); %></td>
												<td><%out.println(openIssues.get(i).getTime()); %></td>
											</tr>
											<%} %>
										</tbody>
									</table>
									<div class="dataTableFeatures">
										</span> <span> <span class="dataTablePrevious" style="display: inline;">
											<%if(pages >0){ %>
												<a href="MoreIssues.do?depid=<%out.print(depid); %>&page=<%out.print(pages-1);%>"> << Previous</a></span>
												<%}if(pages < totalPages){ %>
												 <span class="dataTableNext"
											style="display: inline;"><a href="MoreIssues.do?depid=<%out.print(depid); %>&page=<%out.print(pages+1);%>">Next >></a></span>
										</span>
										<%} %>
									</div>
							</div>
						</div>
						
						<%} %>
<%
			if(cpage == 6){
				int depidClosed = (Integer)request.getSession().getAttribute("depIDClosed");
				int pagesClosed = (Integer) request.getSession().getAttribute("startPageClosed");
				int totalPagesClosed = (Integer)request.getSession().getAttribute("totalPagesClosed");
				ArrayList<OpenIssue> closedIssues = new ArrayList<OpenIssue>();
				closedIssues = (ArrayList<OpenIssue>)request.getSession().getAttribute("issueListClosed");
				
			%>
			
			<div id="content" class="home" style="display: block;">
							<h2 style="cursor: pointer;">
								<%
								if(closedIssues.size() >=1){
								out.println(closedIssues.get(0).getOpenForDep()); 
								}
								%>&nbsp;&nbsp;&nbsp;
							</h2>
							<div class="dataTable" width="100%" style="width:100%;">
									<table cellspacing="0" class="dataTable dataTableActions">
										<thead>
											<tr>
												<th class="sortable first label" id="label">
													<div id="thDIV">Issues</div>
												</th>
												<th class="sortable " id="nb_hits">
													
													<div id="thDIV">Priority</div>
												</th>
												
												<th class="sortable  columnSorted" id="nb_visits">
													<div id="thDIV">Status</div></th>
												<th class="sortable  columnSorted" id="nb_visits">
													<div id="thDIV">Owner</div></th>
												<th class="sortable " id="bounce_rate">
												
													<div id="thDIV">Opened HH:MM</div>
												</th>
											</tr>
										</thead>
										<tbody>
	<% for(int i =0;i<closedIssues.size();i++){  %>
											<tr class="actionsDataTable level0">
												<td class="label labelodd" style="min-width: 145px;"><div
														class="dataTableRowActions"
														style="height: 27px; margin-left: 190px; display: none;">
													</div> <span
															class="label"><span class="label"><a href="ViewIssue.do?id=<%out.println(closedIssues.get(i).getIssueID());%>"><%out.println(closedIssues.get(i).getIssueName());%></a>
						</span></td>
										<%
										if(closedIssues.get(i).getPriority().equals("LOW")){
											out.println("<td style=\"background-color:#0F0\"></td>");
										}
										if(closedIssues.get(i).getPriority().equals("MED")){
											out.println("<td  style=\"background-color:#FF0\"></td>");
										}
										if(closedIssues.get(i).getPriority().equals("HIGH")){
											out.println("<td  style=\"background-color:#F00\"></td>");
										}
										%>
												<td><%out.println(closedIssues.get(i).getStatus()); %></td>
												<td><%out.println(closedIssues.get(i).getOpenByUser()); %></td>
												<td><%out.println(closedIssues.get(i).getTime()); %></td>
											</tr>
											<%} %>
										</tbody>
									</table>
									<div class="dataTableFeatures">
										</span> <span> <span class="dataTablePrevious" style="display: inline;">
											<%if(pagesClosed >0){ %>
												<a href="MoreIssues.do?depid=<%out.print(depidClosed); %>&page=<%out.print(pagesClosed-1);%>"> << Previous</a></span>
												<%}if(pagesClosed < totalPagesClosed){ %>
												 <span class="dataTableNext"
											style="display: inline;"><a href="MoreIssuesClosed.do?depid=<%out.print(depidClosed); %>&page=<%out.print(pagesClosed+1);%>">Next >></a></span>
										</span>
										<%} %>
									</div>
							</div>
						</div>
						
						<%} %>


			</div> <!--  dashboard widget ends -->
			</div> <!--  // dashboard ends  -->
			</div><!--  // end content  -->
			<div class="clear"></div>
			</div> <!-- // end pageWrap -->
			</div> <!-- // end page -->
			</div>
			</body>

</body>
</html>

