<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page isErrorPage="true" %>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
	"http://www.w3.org/TR/html4/loose.dtd">
<%-- $Id: installProblem.jsp 4549 2013-03-23 15:08:32Z friedj $ --%>
<html>
<head>
	<meta http-equiv="Pragma" CONTENT="no-cache"> 
	<meta http-equiv="Expires" content="-1">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	
	<!-- title -->
	<title>
		Setting User Property
	</title>
	
	<link rel="shortcut icon" href="favicon.ico" type="image/x-icon">
	<link rel="stylesheet" type="text/css" href='<%=request.getContextPath()%>/js/ext/resources/css/ext-all.css'>
	<link rel="stylesheet" type="text/css" href='<%=request.getContextPath()%>/js/ext/resources/css/ext-all-gray.css'>
	<link rel="stylesheet" type="text/css" href='<%=request.getContextPath()%>/design/silver/style.css'>
</head>

<body id="body" style="background-color:#f3f3f3;">
<div class="x-container x-border-item x-box-item x-container-default x-box-layout-ct" 
     style="border: 0px none; margin: 0px; width: 100%" id="headerMaster">
  <div style="height:59px;">  
     <div class="logoHeader" style="margin-left:auto; margin-right: 10px; margin-top: 25px;"></div>
  </div>
</div>
<div style="padding: 50px 50px 50px 50px; margin-top: 70px;">
<div style="min-width: 300px; border: solid 1px #666666; padding: 20px; 
     background-color: #e8ffe8; max-width: 700px; margin-left:auto; margin-right:auto;">
     <div style="margin-left:-5px; margin-bottom: 25px; float:left; margin-right: 20px;"><img src='<%=request.getContextPath()%>/design/silver/32x32/check.png'/></div>
     <div style:"float:right;">
<h2>
<s:text name='admin.user.profile.userProperty.head'/>
</h2>
		<p>&nbsp;</p>
</div>
</div>
</div>
</body>

</html>   

