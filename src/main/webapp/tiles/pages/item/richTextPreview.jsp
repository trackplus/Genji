<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	/* set the context path as a request attribute because
	only the request attributes map is available from OGNL
	(not the entire HttpRequest object )*/
	request.setAttribute("appContextPath", request.getContextPath());
%>
<s:set name="iconsPath"
    	value="#request.appContextPath+'/design/'+#session.userDesignPath+'/icons/'"/>
<s:set name="userPath"
    	value="#request.appContextPath+'/design/'+#session.userDesignPath+'/'"/>
<html>
	<head>
		<link rel="stylesheet" type="text/css" href='<%=request.getContextPath()+"/design/"+session.getAttribute("userDesignPath")+ "/styleRichText.css"%>'>
		<script language="JavaScript" type="text/javascript">
		function dinamicContent(){
			document.body.innerHTML=parent.htmlDinamicContent;
			changeLink();
		}
		function changeLink() {
			if (!document.getElementsByTagName){
				return;
			}
			var anchors = document.getElementsByTagName("a");
			for (var i=0; i<anchors.length;i++){
				var anchor = anchors[i];
				var targetAttr=anchor.getAttribute("target");
				if (targetAttr==null||targetAttr.nodeValue==null||targetAttr.nodeValue==""||targetAttr.nodeValue=="_self"){
					var attr = document.createAttribute('target');
					attr.nodeValue = '_top';
					anchor.setAttributeNode(attr);
				}
			}
		}
		</script>
	</head>
	<s:if test="idRichTextValue==null">
		<body onload="dinamicContent()">
		</body>
	</s:if>
	<s:else>
		<body onload="changeLink()">
			<s:property value="richTextValue" escape="false"/>
		</body>
	</s:else>
</html>
