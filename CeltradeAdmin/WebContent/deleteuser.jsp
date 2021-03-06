
<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="com.Phoenix.Celtrade.*"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.sql.*" %>
<%
	ServletContext cntxt = this.getServletContext();
	Connection conn = (Connection)cntxt.getAttribute("databaseConn");
	ArrayList <Department> deps =  (ArrayList<Department>) request.getSession().getAttribute("deps");
	int cpage = (Integer)request.getSession().getAttribute("cPage");
	String loggedUser = (String)this.getServletContext().getAttribute("loggedUser");
%> 
    
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
		<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<script type="text/javascript">
function validateForm(){
if(document.form1.departmentName.value == ""){
alert("Department Name is required!");
document.form1.departmentName.focus();
return false;
}
if(document.form1.thought.value == ""){
	alert("Thought is required!");
	document.form1.thought.focus();
	return false;
	}
if(document.form1.username.value == ""){
	alert("Usert Name is required!");
	document.form1.username.focus();
	return false;
	}
if(document.form1.userEmail.value == ""){
	alert("Email is required!");
	document.form1.userEmail.focus();
	return false;
	}
if(document.form1.password.value == ""){
	alert("Password is required!");
	document.form1.password.focus();
	return false;
	}
var res = confirm("Are You Sure want to Update?");
return res;
}

	window.onload=function(){
		loadUsers();
	}

function loadUsers(){
	var x = document.getElementById("getDepts");
	var dept = x.options[x.selectedIndex].value;
	
	var xmlhttp;
	if (window.XMLHttpRequest)
	  {// code for IE7+, Firefox, Chrome, Opera, Safari
	  xmlhttp=new XMLHttpRequest();
	  }
	else
	  {// code for IE6, IE5
	  xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
	  }
	xmlhttp.open("GET","/CeltradeAdmin/DeleteUser.do?dept="+dept,true);
	xmlhttp.send();
	xmlhttp.onreadystatechange=function()
	  {
	  if (xmlhttp.readyState==4 && xmlhttp.status==200)
	    {
	    document.getElementById("userDelete").innerHTML=xmlhttp.responseText;
	    }
	  }
}

</script>
</head>
<body style="display:block">
			<div id="root">
			<div id="header">
			<span id="logo">
			<a href="Celttade" title="Powered by NetLine Solutions" style="text-decoration: none;">
			<img src="images/celtrade24.jpg" alt="Powered by Netline Solutions" style="margin-left: 10px" height="100" class="ie-hide">
			<!--[if lt IE 9]>
			<img src="images/celtrade24.jpg" alt="Powered by Netline Solutions" style='margin-left:10px' height='50'/>
			<![endif]--></a>
			
			<div style="display: block !important; width: 320px; text-align: center; font-family: sans-serif; font-size: 12px; float:right;"><a href="http://www.wunderground.com/cgi-bin/findweather/getForecast?query=zmw:00000.11.71624&amp;bannertypeclick=wu_clean2day" title="Mississauga, Ontario Weather Forecast" target="_blank"><img src="http://weathersticker.wunderground.com/weathersticker/cgi-bin/banner/ban/wxBanner?bannertype=wu_clean2day_metric_cond&amp;airportcode=CYYZ&amp;ForcedCity=Mississauga&amp;ForcedState=Canada&amp;wmo=71624&amp;language=EN" alt="Find more about Weather in Mississauga, CA" width="300"></a><br><a href="http://www.wunderground.com/cgi-bin/findweather/getForecast?query=zmw:00000.11.71624&amp;bannertypeclick=wu_clean2day" title="Get latest Weather Forecast updates" style="font-family: sans-serif; font-size: 12px" target="_blank">Click for weather forecast</a></span>

  </div>
			</span>    <noscript>
			&lt;div id="javascriptDisabled"&gt;JavaScript must be enabled in order for you to use Celtrade in standard view.&lt;br &gt;However, it seems JavaScript is either disabled or not supported by your browser.&lt;br /&gt;To use standard view, enable JavaScript by changing your browser options, then &lt;a href=""&gt;try again&lt;/a&gt;.&lt;br /&gt;&lt;/div&gt;"
			</noscript></div>
			<ul class="nav">
			<%if(cpage == 1){ %>
			<li id="dashboard" class="sfActive sfHover"><a href="Main.do">  Add User </a></li>
			<%}else{ %>
			<li id="dashboard"><a href="Main.do">  Add User </a></li>
			<%}if(cpage ==2){ %>
			<li id="dashboard" class="sfActive sfHover"><a href="AddDepartments.do"> Add Departments </a></li>
			<%}else{ %>
			<li id="dashboard" ><a href="AddDepartments.do"> Add Departments </a></li>
			<%}if(cpage == 3){ %>
			<li id="dashboard" class="sfActive sfHover"><a href="AddThought.do"> Add Thought </a></li>
			<%}else{ %>
			<li id="dashboard" ><a href="AddThought.do"> Add Thought </a></li>
			<%} %>
			<%if(cpage == 4){ %>
			<li id="dashboard" class="sfActive sfHover"><a href="DeleteUser.do"> Delete User </a></li>
			<%}else{ %>
			<li id="dashboard" ><a href="DeleteUser.do"> Delete User </a></li>
			<%} %>
			<%if(cpage == 5){ %>
			<li id="dashboard" class="sfActive sfHover"><a href="DeleteDepartment.do"> Delete Department </a></li>
			<%}else{ %>
			<li id="dashboard" ><a href="DeleteDepartment.do"> Delete Department </a></li>
			<%} %>
			</ul>
			<div class="page">
			<div class="pageWrap">
			<div class="nav_sep"></div>
			<div class="top_controls">
			<span id="header_message" class="header_info">
			<span class="header_short">
			<!--  Logged user -->
			<% out.println(loggedUser); %>
			</span>
			<span class="header_full">
			<!--  Logged user -->
			<% out.println(loggedUser); %>
			<a href="Settings.do">Change Password</a>&nbsp;&nbsp;
			<a href="Logout.do">Log Out</a>
			</span>
			</span>
			<div id="loadingError" style="display: none;">Oops? problem during the request, please try again.</div>
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
			<%if(cpage == 4){ %>
			<div class="col width-100 ui-sortable">
<div class="dataTableWrapper">
<div class="widget">
<div class="widgetContent">
<form id="form1" name="form1" method="post" action="DeleteUser.do">
<table border="0" cellpadding="10" class="dataTable dataTableActions">
<tbody><tr>
<th scope="col" class="widgetTop"><h2 style="cursor:pointer">Delete User</h2><h2></h2></th>
<th class="widgetTopCreate"></th>
</tr>
<tr>
<td class="label labeleven" style="width:25%"><label>Department :</label>
</td>
<td><select name="department" id="getDepts" onchange="loadUsers()">
<!-- print deps here  -->
<%
for(int i =0;i<deps.size();i++){
	out.println("<option value="+deps.get(i).getDepID()+">"+deps.get(i).getDepName()+"</option>");
}
deps.clear();
%>
</select>
</td>
</tr>
<tr id="userDelete">


</tr>
 <tr>
<td><input name="submit" type="submit" value="Delete User" class="button"></td>
</tr>
</tbody></table>
</form>
</div>
</div>
</div>
</div>
<%} %>
<%if(cpage == 5){ %>
<div class="col width-100 ui-sortable">
<div class="dataTableWrapper">
<div class="widget">
<div class="widgetContent">
<form id="form1" name="form1" method="post" action="DeleteDepartment.do" onsubmit="return(validateForm());">
<table border="0" cellpadding="10" class="dataTable dataTableActions">
<tbody><tr>
<th scope="col" class="widgetTop"><h2 style="cursor:pointer">Delete Department</h2><h2></h2></th>
<th class="widgetTopCreate"></th>
</tr>
<tr>
<td class="label labelodd" style="width:25%"><label>Department  :</label></td>
<td><select name="depts" onchange="loadUsers();">
<!-- print deps here  -->
<%
for(int i =0;i<deps.size();i++){
	out.println("<option value="+deps.get(i).getDepID()+">"+deps.get(i).getDepName()+"</option>");
}
deps.clear();
%>
</select>
</td>

</tr>
 <tr>
<td><input name="submit" type="submit" value="Delete Department" class="button"></td>
</tr>
</tbody></table>
</form>
</div>
</div>
</div>
</div>
<%}if(cpage == 3){ %>
<div class="col width-100 ui-sortable">
<div class="dataTableWrapper">
<div class="widget">
<div class="widgetContent">
<form id="form1" name="form1" method="post" action="AddThought.do" onsubmit="return(validateForm());">
<table border="0" cellpadding="10" class="dataTable dataTableActions">
<tbody><tr>
<th scope="col" class="widgetTop"><h2 style="cursor:pointer">Add New Thought</h2><h2></h2></th>
<th class="widgetTopCreate"></th>
</tr>
<tr>
<td class="label labelodd" style="width:25%"><label>Thought :</label></td>
<td><textarea name="thought"></textarea></td>
</tr>
 <tr>
<td><input name="submit" type="submit" value="Add Thought" class="button"></td>
</tr>
</tbody></table>
</form>
</div>
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

