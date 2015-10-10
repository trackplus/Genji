<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page isErrorPage="true" %>
<%@ page import="com.aurel.track.ApplicationStarter" %>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
	"http://www.w3.org/TR/html4/loose.dtd">
<%-- $Id: BorderLayout2.jsp 4515 2013-03-15 20:26:35Z friedj $ --%>
<%
//ApplicationStarter.setLocale(request.getLocales());
%>
<html>
<head>
	<meta http-equiv="Pragma" CONTENT="no-cache"> 
	<meta http-equiv="Expires" content="-1">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	
	<!-- title -->
	<title>
		<%=ApplicationStarter.TITLE%>
	</title>
	
	<link rel="shortcut icon" href="favicon.ico" type="image/x-icon">
	<link rel="stylesheet" type="text/css" href='<%=request.getContextPath()%>/js/ext/resources/css/ext-all.css'>
	<link rel="stylesheet" type="text/css" href='<%=request.getContextPath()%>/js/ext/resources/css/ext-all-gray.css'>
	<link rel="stylesheet" type="text/css" href='<%=request.getContextPath()%>/design/silver/style.css'>

    <script type="text/javascript" src="<%=request.getContextPath()%>/js/ext/ext-all.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/lib/log4javascript_production.js"></script>
	<style type="text/css">
	html {
	  height:100%;
	}
      .logoHeaderInstall{
    background-image: url(design/silver/img/trackLogo.png) !important;
    background-repeat: no-repeat;
    background-position: 0 -2px;
    width:70px;
    height:28px;
    margin: 0px 0 0 10px;
    }
    h2 {
    color: #000000;
    font-family: Helvetica Neue, Arial, Tahoma, sans-serif;
    font-size: 16px;
    font-weight: 400;
    margin: 0 0 5px;
    }
    p {
      font-size: 14px;
      font-family: Helvetica Neue, Arial, Tahoma, sans-serif;
      font-weight: 200;
    }
    .progress-text {
      font-size: 14px;
      font-family: Helvetica Neue, Arial, Tahoma, sans-serif;
      font-weight: 200;
    }
    #headerMaster{
    height: 68px;
    background-image: url(design/silver/img/bg_header.png ) !important;
    /*background-position: 0 -32px;
    background-repeat: no-repeat;*/

    border-bottom: 1px solid #979797 !important;
    border-left: medium none;
    border-right: medium none;
    border-top: medium none;
    }
    
    .x-progress-default .x-progress-text {
    	font-size: 14px;
    	line-height: 24px;
    	font-weight: normal;
    	color: #fafafa;
    }
    
    .x-progress-default .x-progress-bar-default {
      background: #fb0000; /* Old browsers */
	  background: -moz-linear-gradient(top,  #1ab1fe 0%, #1b71b1 100%); /* FF3.6+ */
      background: -webkit-gradient(linear, left top, left bottom, color-stop(0%,#1ab1fe), color-stop(100%,#1b71b1)); /* Chrome,Safari4+ */
      background: -webkit-linear-gradient(top,  #1ab1fe 0%,#1b71b1 100%); /* Chrome10+,Safari5.1+ */
      background: -o-linear-gradient(top,  #1ab1fe 0%,#1b71b1 100%); /* Opera 11.10+ */
      background: -ms-linear-gradient(top,  #1ab1fe 0%,#1b71b1 100%); /* IE10+ */
      background: linear-gradient(to bottom,  #1ab1fe 0%,#1b71b1 100%); /* W3C */            
    }
    
   .x-progress-default  {
      border-radius:15px;
   }
   
   .boxMessage  {
      background: #1ab1fe; /* Old browsers */
	  background: -moz-linear-gradient(top,  #c8c8c8 0%, #d8d8d8 100%); /* FF3.6+ */
      background: -webkit-gradient(linear, left top, left bottom, color-stop(0%,#c8c8c8), color-stop(100%,#d8d8d8)); /* Chrome,Safari4+ */
      background: -webkit-linear-gradient(top,  #c8c8c8 0%,#d8d8d8 100%); /* Chrome10+,Safari5.1+ */
      background: -o-linear-gradient(top,  #c8c8c8 0%,#d8d8d8 100%); /* Opera 11.10+ */
      background: -ms-linear-gradient(top,  #c8c8c8 0%,#d8d8d8 100%); /* IE10+ */
      background: linear-gradient(to bottom,  #c8c8c8 0%,#d8d8d8 100%); /* W3C */
      color:#000000;            
    }
    
    #body2 {
      background: #f8f8f8; /* Old browsers */
	  background: -moz-linear-gradient(top,  #b8b8b8 0%, #f8f8f8 100%); /* FF3.6+ */
      background: -webkit-gradient(linear, left top, left bottom, color-stop(0%,#a8a8a8), color-stop(100%,#f8f8f8)); /* Chrome,Safari4+ */
      background: -webkit-linear-gradient(top,  #b8b8b8 0%,#f8f8f8 100%); /* Chrome10+,Safari5.1+ */
      background: -o-linear-gradient(top,  #b8b8b8 0%,#f8f8f8 100%); /* Opera 11.10+ */
      background: -ms-linear-gradient(top,  #b8b8b8 0%,#f8f8f8 100%); /* IE10+ */
      background: linear-gradient(to bottom,  #b8b8b8 0%,#f8f8f8 100%); /* W3C */
    }
    
</style>


    <script type="text/javascript">
        function checkReady(){
            Ext.Ajax.request({
                url: 'readyTester',
                disableCaching:true,
                success: function(response){
                    var responseJson = Ext.decode(response.responseText);
                    var ready=responseJson.data.ready;
                    var progress=responseJson.data.percentComplete;
                    var progressText=responseJson.data.progressText;
                    
            		var progress100=responseJson.data.progress100;
            		var progressPercent=responseJson.data.progressPercent;
            		var redirect=responseJson.data.redirect;
            		var initializing=responseJson.data.initializing;
            		var please=responseJson.data.please;
            		var waitMinutes=responseJson.data.waitMinutes;
                    
                    var percent=progress/100;
                    if(ready){
                        window.location.href="logon.action";
                        progressCmp.updateProgress(100, progress100);
                        progressTxt.update(redirect);
                    }else{
                        progressCmp.updateProgress(percent,progress + progressPercent,true);
                        progressTxt.update(progressText);
                        setTimeout(function() {checkReady()}, 300);
                    }
                }
            });
        }
        function init(){
            Ext.onReady(function(){
                initAjaxPanel();
                checkReady();
            });
        }
        var ajaxPanel=null;
        var progressCmp=null;
        var progressTxt=null;
        function initAjaxPanel(){
            progressCmp= Ext.create('Ext.ProgressBar', {
                text:'Initializing...',
                height:'30px'
            });
            progressTxt=Ext.create("Ext.Component",{
                margin:'10 0 0 0',
                cls:'progress-text',
                html:'...'
            });
            ajaxPanel=Ext.create('Ext.container.Container',{
                border:false,
                renderTo:'ajaxPanelID',
                items:[progressCmp,progressTxt]
            });
        }
    </script>
</head>

<body id="body2" >
<div class="x-container x-border-item x-box-item x-container-default x-box-layout-ct" 
     style="border: 0px none; margin: 0px; width: 100%" id="headerMaster">
  <div style="height:59px;">  
     <div class="logoHeaderInstall" style="margin-left:auto; margin-right: 10px; margin-top: 25px;"></div>
  </div>
</div>
<div style="padding: 50px 50px 50px 50px; margin-top: 170px;">
    <div class="boxMessage boxMessage-ok" style="min-width: 300px; border: solid 1px #666666; padding: 20px;
         max-width: 700px; margin-left:auto; margin-right:auto;">
         <div style="margin-left:-5px; margin-bottom: 25px; float:left; margin-right: 10px;"><img src='<%=request.getContextPath()%>/design/silver/icons/loading.gif'/></div>
         <div style="margin-left:20px;">
            <h2>
                <%=ApplicationStarter.PLEASE_TEXT%>
            </h2><p>
            <%=ApplicationStarter.WAIT_TEXT%>
            </p>
        </div>
        <div id="ajaxPanelID" style="margin-left: 20px;">
        </div>
    </div>
</div>
</body>

</html>


<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%
    Boolean ready = (Boolean) application.getAttribute("READY");
    if(ready!=null&&ready.booleanValue()){
        response.sendRedirect(response.encodeRedirectURL("logon.action"));
    }else{%>
    <script type="text/javascript">
        init();
    </script>
    <%}
%>

<%-- For those that have bookmarked this URL --%>



