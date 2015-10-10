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
		Browse File
	</title>

	<link rel="shortcut icon" href="favicon.ico" type="image/x-icon">
	<link rel="stylesheet" type="text/css" href='<%=request.getContextPath()%>/js/ext/resources/css/ext-all.css'>
	<link rel="stylesheet" type="text/css" href='<%=request.getContextPath()%>/js/ext/resources/css/ext-all-gray.css'>
	<link rel="stylesheet" type="text/css" href='<%=request.getContextPath()%>/design/silver/style.css'>

	<script type="text/javascript">
		var funcNum ='<s:property value="CKEditorFuncNum"/>';
		function doSelectFile(fileUrl){
			window.opener.CKEDITOR.tools.callFunction( funcNum, fileUrl );
			window.close();
		}
	</script>
</head>

<body id="body" style="background-color:#f3f3f3;">
<div class="x-container x-border-item x-box-item x-container-default x-box-layout-ct" style="border: 0px none; margin: 0px; width: 100%" id="headerMaster">
	<div style="height:59px;">
		<div class="logoHeader" style="margin-left:auto; margin-right: 10px; margin-top: 25px;"></div>
	</div>
</div>
<div style="padding-top: 5px; margin-top: 70px;">
	<s:if test="attachments!=null && attachments.size()>0">
		<div class="infoBox">
			<s:text name="editor.browseImage.selectImage"/>
		</div>
			<s:iterator value="attachments" status="status">
				<div style=" margin-bottom:5px;width: 150px;border:1px solid #d0d0d0;background-color: white">
				<s:url id="printUrl" value="/downloadAttachment.action" includeParams="none">
					<s:param name="workItemID" value="workItem"/>
					<s:param name="attachKey" value="objectID"/>
				</s:url>
				<s:url id="thumbnailUrl" value="/thumbnailAttachment.action" includeParams="none">
					<s:param name="workItemID" value="workItem"/>
					<s:param name="attachKey" value="objectID"/>
				</s:url>
				<a href="javascript:doSelectFile('<s:property value="#printUrl" escapeHtml="false"/>')">
					<img src='<s:property value="#thumbnailUrl" escapeHtml="false"/>'/>
				</a>
				<div style="padding: 5px; border-top:1px solid #d0d0d0;">
					<a href="javascript:doSelectFile('<s:property value="#printUrl" escapeHtml="false"/>')">
						<s:property value="fileName"/>
					</a>
				</div>
				</div>
			</s:iterator>
		</table>
	</s:if>
	<s:else>
		<div class="infoBox">
			<s:text name="editor.browseImage.noImagesFound"/>
		</div>
	</s:else>

</div>
</body>

</html>

