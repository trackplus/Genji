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
<s:set name="designPath"
       value="#request.appContextPath+'/design/'+#session.userDesignPath"
       scope="request"/>
<s:if test="#session.EXTJSLOCALE==null">
    <s:set name="extjsLocale" value="de"  scope="request"/>
</s:if>
<s:else>
    <s:set name="extjsLocale" value="#session.EXTJSLOCALE"
           scope="request"/>
</s:else>
<jsp:useBean id="extjsLocale" scope="request" class="java.lang.String"/>

<s:set name="iconsPath" value="#request.designPath+'/icons/'"/>
<s:set name="listIconsPath" value="#request.appContextPath+'/design/listIcons/'"/>
<s:set name="imgPath" value="#request.designPath+'/img/'"/>

<s:set name="icon16Path" value="#request.designPath+'/16x16/'"/>
<s:set name="icon32Path" value="#request.designPath+'/32x32/'"/>

<jsp:useBean id="alignR" scope="session" class="java.lang.String"/>
<jsp:useBean id="alignL" scope="session" class="java.lang.String"/>
<jsp:useBean id="dir" scope="session" class="java.lang.String"/>

<s:set name="alignR" value="#session.alignR" scope="request"/>
<s:set name="alignL" value="#session.alignL" scope="request"/>
<s:set name="dir"    value="#session.dir"    scope="request"/>

<jsp:useBean id="designPath" scope="request" class="java.lang.String"/>
<jsp:useBean id="imgPath" scope="request" class="java.lang.String"/>
<jsp:useBean id="iconsPath" scope="request" class="java.lang.String"/>
<jsp:useBean id="listIconsPath" scope="request" class="java.lang.String"/>

<jsp:useBean id="icon16Path" scope="request" class="java.lang.String"/>
<jsp:useBean id="icon32Path" scope="request" class="java.lang.String"/>

<html>
<head>
    <title>
        <s:text name="printItem.title"></s:text><s:property value="workItemID"/>
    </title>
    <!-- stylesheet resources -->
    <link rel="stylesheet" type="text/css" href='<%=designPath + "/style.css"%>'>
    <link rel="stylesheet" type="text/css" href='<%=designPath + "/printItem.css"%>'>
    <link rel="stylesheet" type="text/css" href='<%=designPath%>/daisyDiff.css?v=<s:property value="#application.TVERSION.label"/>'>
</head>

<body class="bodyPrintItem">

<div id="contentPrintItem">
    <s:if test="!hideHeader">
        <div id="printItemHeader">
            <h4 style="margin: 5px">
                <s:property value="issueNoLabel"/>
                <strong>
                 <span class="emphasize">
                   &nbsp;<s:property value="workItemDisplayID"/>
                 </span>:
                <span class="dataEmphasize">
                  <s:property value="statusDisplay"/>
                 </span>
                   :&nbsp;<s:property value="synopsis"/>
                </strong>
            </h4>
         </div>
    </s:if>
    <div id="printItemScreenDiv" class="olist-ulist">
        <s:if test="screenBean.tabs.size()==1">
            <s:iterator value="screenBean.tabs[0].panels" status="status">
                <s:if test="!#status.first">
                    <div class="containerSeparator">&nbsp;</div>
                </s:if>
                <div  style="border:1px solid #C0C0C0;margin: 0 0 5px 5px; padding: 5px">
                    <table class="containerTable" border="0" style="width:100%">
                        <s:iterator value="fieldWrappers" status="status1">
                            <TR class="printItemRow">
                                <s:iterator value="top" status="status">
                                    <s:if test="top!=null">
                                        <s:if test="field!=null">
                                            <td width="90px;" style="white-space: nowrap" class="printItemCell fieldLabel"
                                                align='<s:property value="labelHAlign"/>'
                                                valign='<s:property value="labelVAlign"/>'
                                                rowspan='<s:property value="field.rowSpan"/>'>
                                                <s:property value="%{getFieldLabel(fieldID)}"/>&nbsp:

                                            </td>
                                            <td class="printItemCell fieldValue"
                                                align='<s:property value="valueHAlign"/>'
                                                valign='<s:property value="valueVAlign"/>'
                                                rowspan='<s:property value="field.rowSpan"/>'
                                                colspan='<s:property value="2*field.colSpan-1"/>' >
                                                <s:property escapeHtml="false" value="%{getFieldDisplayValue(fieldID)}"/>

                                            </td>
                                        </s:if>
                                        <s:else>
                                            <TD>&nbsp;</TD>
                                        </s:else>
                                    </s:if>
                                </s:iterator>
                            </TR>
                       </s:iterator>
                    </table>
                </div>
            </s:iterator>
        </s:if>
        <s:else>
            <s:iterator value="screenBean.tabs" status="status1">
                <s:if test="!#status.first">
                    <div class="containerSeparator">&nbsp;</div>
                </s:if>
                <strong style="margin-left: 5px;"><s:property value="label"/></strong>
                <div class="containerSeparator">&nbsp;</div>
                <s:iterator value="panels" status="status">
                    <s:if test="!#status.first">
                        <div class="containerSeparator">&nbsp;</div>
                    </s:if>
                    <div  style="border:1px solid #C0C0C0;margin: 0 0 5px 5px; padding: 5px">
                        <table class="containerTable" border="0" style="width:100%">
                            <s:iterator value="fieldWrappers" status="status1">
                                <TR class="printItemRow">
                                    <s:iterator value="top" status="status">
                                        <s:if test="top!=null">
                                            <s:if test="field!=null">
                                                <td width="100px;" style="white-space: nowrap" class="printItemCell fieldLabel"
                                                    align='<s:property value="labelHAlign"/>'
                                                    valign='<s:property value="labelVAlign"/>'
                                                    rowspan='<s:property value="field.rowSpan"/>'>
                                                    <s:property value="%{getFieldLabel(fieldID)}"/>&nbsp:

                                                </td>
                                                <td nowrap class="printItemCell fieldValue"
                                                    align='<s:property value="valueHAlign"/>'
                                                    valign='<s:property value="valueVAlign"/>'
                                                    rowspan='<s:property value="field.rowSpan"/>'
                                                    colspan='<s:property value="2*field.colSpan-1"/>' >
                                                    <s:property escapeHtml="false" value="%{getFieldDisplayValue(fieldID)}"/>

                                                </td>
                                            </s:if>
                                            <s:else>
                                                <TD>&nbsp;</TD>
                                            </s:else>
                                        </s:if>
                                    </s:iterator>
                                </TR>
                            </s:iterator>
                        </table>
                    </div>
                </s:iterator>
            </s:iterator>
        </s:else>
     </div>

    <s:if test="!hideChildren">
        <s:action name="children" executeResult="true">
            <s:param name="workItemID" value="workItemID"/>
        </s:action>
    </s:if>

    <s:if test="!hideAttachments">
        <div class="containerSeparator" style="height: 10px;">&nbsp;</div>
        <s:action name="flatAttachment" executeResult="true">
            <s:param name="workItemID" value="workItemID"/>
        </s:action>
    </s:if>

    <s:if test="!hideExpense">
        <div class="containerSeparator">&nbsp;</div>
        <s:action name="flatExpense" executeResult="true">
            <s:param name="workItemID" value="workItemID"/>
        </s:action>
    </s:if>

    <s:if test="!hideHistory">
        <div class="containerSeparator">&nbsp;</div>
        <div style="margin-left: 5px;" class="flatValueLabel"><s:text name="item.printItem.lbl.tab.commonHistory"/></div>
        <s:action name="itemDetailHistory" executeResult="true">
            <s:param name="workItemID" value="workItemID"/>
        </s:action>
    </s:if>
</div>

</body>
</html>
