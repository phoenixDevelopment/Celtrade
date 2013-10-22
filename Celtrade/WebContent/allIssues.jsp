<?xml version="1.0" encoding="UTF-8" ?>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@page import="com.Phoenix.data.*" %>
    <%@page import="java.util.Date" %>
    <%@page import="java.io.*" %>   
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Celtrade CP24 DashBoard </title>
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
			
</head>
<body style="display:block">
			<div id="root">
			<!-- /*out.println("<div id=\"topBars\">
			out.println("<div id=\"topRightBar\">");
			getWeather();
			out.println("</div>");
			out.println("</div>");*/ -->
			<div id="header">
			<span id="logo">
			<a href="Celttade" title="Powered by NetLine Solutions" style="text-decoration: none;">
			<img src="images/celtrade24.jpg" width="800" alt="Powered by Netline Solutions" style="margin-left: 10px" height="40" class="ie-hide">
			<!--[if lt IE 9]>
			<img src="" alt="Powered by Netline Solutions" style='margin-left:10px' height='50'/>
			<![endif]--></a>
			
			<div style="display: block !important; width: 320px; text-align: center; font-family: sans-serif; font-size: 12px; float:right;"><a href="http://www.wunderground.com/cgi-bin/findweather/getForecast?query=zmw:00000.11.71624&amp;bannertypeclick=wu_clean2day" title="Mississauga, Ontario Weather Forecast" target="_blank"><img src="http://weathersticker.wunderground.com/weathersticker/cgi-bin/banner/ban/wxBanner?bannertype=wu_clean2day_metric_cond&amp;airportcode=CYYZ&amp;ForcedCity=Mississauga&amp;ForcedState=Canada&amp;wmo=71624&amp;language=EN" alt="Find more about Weather in Mississauga, CA" width="300"></a><br><a href="http://www.wunderground.com/cgi-bin/findweather/getForecast?query=zmw:00000.11.71624&amp;bannertypeclick=wu_clean2day" title="Get latest Weather Forecast updates" style="font-family: sans-serif; font-size: 12px" target="_blank">Click for weather forecast</a></span>

  </div>
			</span>    <noscript>
			&lt;div id="javascriptDisabled"&gt;JavaScript must be enabled in order for you to use Celtrade in standard view.&lt;br &gt;However, it seems JavaScript is either disabled or not supported by your browser.&lt;br /&gt;To use standard view, enable JavaScript by changing your browser options, then &lt;a href=""&gt;try again&lt;/a&gt;.&lt;br /&gt;&lt;/div&gt;"
			</noscript></div>
			<ul class="nav">
			 <%
			PrintWriter printer = response.getWriter();
			int cpage = (Integer) request.getSession().getAttribute("cPage");
			int depid = (Integer) request.getSession().getAttribute("depID");
			int pages = 0;
			pages = (Integer) request.getSession().getAttribute("startPage");
			User loggedUser = (User) request.getSession().getAttribute("loggedUser");
			String thought = (String) request.getSession().getAttribute("thought");
			ArrayList<OpenIssue> openIssues = new ArrayList<OpenIssue>();
			openIssues = (ArrayList<OpenIssue>)request.getSession().getAttribute("issueList");
			
			
 	 	%>
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

						<div id="content" class="home" style="display: block;">
							<h2 style="cursor: pointer;">
								<%
								if(openIssues.size() >=1){
								//response.getWriter().println(openIssues.get(0).getOpenForDep()); 
								}
								%>&nbsp;&nbsp;&nbsp;
							</h2>
							<div class="dataTable" data-table-type="actionDataTable">
								<div class="dataTableActionsWrapper">
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
													<div id="thDIV">Owner</div></th>
												<th class="sortable " id="bounce_rate">
												
													<div id="thDIV">Opened HH:MM</div>
												</th>
											</tr>
										</thead>
										<tbody>
	<% //for(int i =0;i<openIssues.size();i++){  %>
											<tr class="actionsDataTable level0">
												<td class="label labelodd" style="min-width: 145px;"><div
														class="dataTableRowActions"
														style="height: 27px; margin-left: 190px; display: none;">
													</div> <span
															class="label"><span class="label"><a href="ViewIssue.do?id=<%//response.getWriter().println(openIssues.get(i).getIssueID());%>"><%//response.getWriter().println(openIssues.get(i).getIssueName());%></a>
						</span></td>
												<td><%//response.getWriter().println(openIssues.get(i).getPriority()); %></td>
												<td><%//response.getWriter().println(openIssues.get(i).getOpenByUser()); %></td>
												<td><%//response.getWriter().println(openIssues.get(i).getOpenedAt()); %></td>
											</tr>
											<%//} %>
										</tbody>
									</table>
									<div class="dataTableFeatures">
										</span> <span> <span class="dataTablePrevious" style="display: inline;">
												<a href="MoreIssues.do?depid=<%response.getWriter().println(depid); %>&page=<%response.getWriter().println(pages-1);%>">Previous</a></span> <span class="dataTableNext"
											style="display: inline;"><a href="MoreIssues.do?depid=<%response.getWriter().println(depid); %>&page=<%response.getWriter().println(pages+1);%>">Next</a></span>
										</span>




									</div>

								</div>
							</div>
						</div>

						<!-- Display Data ends -->
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

