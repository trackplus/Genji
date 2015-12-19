<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page isErrorPage="true" %>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%-- $Id --%>

<%
	String theme=request.getParameter("theme");
	if(theme==null){
		theme=(String)session.getAttribute("theme");
		if(theme==null) {
			theme = "silver";
		}
	}
	session.setAttribute("theme", theme);
%>

<html>
<head>
	<meta http-equiv="Pragma" CONTENT="no-cache">
	<meta http-equiv="Expires" content="-1">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

	<!-- title -->
	<title>
		Browse File
	</title>

	<link rel="shortcut icon" href="favicon.ico" type="image/x-icon">

	<link rel="stylesheet" type="text/css" href='<%=request.getContextPath()%>/js/ext/classic/theme-<%=session.getAttribute("theme")%>/resources/theme-<%=session.getAttribute("theme")%>-all.css'>
	<link rel="stylesheet" type="text/css" href='design/<s:property value="#session.theme"/>/style-<s:property value="#session.theme"/>.css?v=<s:property value="#application.TVERSION.label"/>'>

	<script type="text/javascript">
		var funcNum ='<s:property value="CKEditorFuncNum"/>';
		function doSelectFile(fileUrl){
			window.opener.CKEDITOR.tools.callFunction( funcNum, fileUrl );
			window.close();
		}
	</script>
	<style type="text/css">
		.boxImg{
			background-color: white;
			border: 1px solid #d0d0d0;
			margin:5px;
			width: 153px;
			display:inline-table;
			text-align: center;
		}
		div.boxImg:hover {
			background-color: #cbe8f7;
			border: 1px solid #dad5d5;
		}
		.boxImg a{
			font-weight: normal;
		}
		.tumbImg{
			padding: 5px 15px;
			width:125px;
			height: 110px;
		}
		.boxImage-text{
			border-top: 1px solid #d0d0d0;
			padding: 2px 1px;
			text-align: center;
			text-overflow: ellipsis;
			overflow: hidden;
			white-space: nowrap;
			height: 21px;
			width: 153px;
		}
	</style>

</head>

<body id="body" style="background-color:#f3f3f3;margin: 0px">
<div class="headerMaster x-container x-border-item x-box-item x-container-default x-box-layout-ct" style="border: 0px none; margin: 0px; width: 100%;height:40px">
	<div style="height:35px;">
		<div class="logoHeader" style="margin-left:auto; margin-right: 10px; margin-top: 5px;"></div>
	</div>
</div>
<div style="margin-top: 40px;">
	<s:if test="attachments!=null && attachments.size()>0">
		<div class="infoBox" style="margin:0px;border-width: 0 0 1px 0;">
			<s:if test="type=='Image'">
				<s:text name="editor.browseImage.selectImage"/>
			</s:if>
			<s:else>
				Select file
			</s:else>
		</div>
		<s:iterator value="attachments" status="status">
			<div class="boxImg">
				<s:url id="printUrl" value="downloadAttachment.action" includeParams="none" includeContext="false">
					<s:param name="workItemID" value="workItem"/>
					<s:param name="attachKey" value="objectID"/>
				</s:url>
				<s:url id="thumbnailUrl" value="thumbnailAttachment.action" includeParams="none">
					<s:param name="workItemID" value="workItem"/>
					<s:param name="attachKey" value="objectID"/>
				</s:url>
				<a href="javascript:doSelectFile('<s:property value="#printUrl" escapeHtml="false"/>')">
					<div class="tumbImg">
						<img src='<s:property value="#thumbnailUrl" escapeHtml="false"/>'/>
					</div>
				</a>
				<div class="boxImage-text">
					<a href="javascript:doSelectFile('<s:property value="#printUrl" escapeHtml="false"/>')"><s:property value="fileName"/></a>
				</div>
			</div>
		</s:iterator>
	</s:if>
	<s:else>
		<div class="infoBox" style="margin:0px;border-width: 0 0 1px 0;">
			<s:text name="editor.browseImage.noImagesFound"/>
		</div>
	</s:else>
</div>
</body>
</html>
