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

<s:set name="icons16x16Path"
       value="#request.appContextPath+'/design/'+#session.userDesignPath+'/16x16/'"/>

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

<div id="printItemScreenDiv">
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

<%-- children --%>
<s:action name="children" executeResult="true">
	<s:param name="workItemID" value="workItemID"/>
</s:action>
<div class="containerSeparator" style="height: 10px;">&nbsp;</div>

<s:if test="flatHistoryList.size()>0">
    <div style="margin-left:5px" class="flatValueLabel"><s:property value="flatValueLabel"/></div>
    <div class="containerSeparator" style="height: 10px;"></div>
    <s:iterator value="flatHistoryList" status="status">
        <s:if test="#status.first">
            <div class="historyRow" style="border-top-width:0px; margin-left: 0px;margin-right:0px;margin-top:5px;background-color: white;">
        </s:if>
        <s:else>
            <div class="historyRow" style="margin-left: 0px;margin-right:0px;margin-top:5px;background-color: white;">
        </s:else>
            <table width="100%" border="0" cellPadding="0" cellSpacing="0" style="table-layout:fixed;">
                <tr style="height:0px;padding:0px;border:none;margin:0px;">
                    <td style="width:20px;padding:0px;border:none;margin:0px;"></td>
                    <td style="width:120px;padding:0px;border:none;margin:0px;"></td>
                    <td style="width:50%;padding:0px;border:none;margin:0px;"></td>
                    <td style="width:50%;padding:0px;border:none;margin:0px;"></td>
                </tr>
                <tr class="dataRow1">
                    <td class="dataName" style="padding-bottom:5px">
                        <img width="16" height="16" src="<s:property value='icons16x16Path'/><s:property value='iconName'/>"/>
                    </td>
                    <td class="dataName" style="width:100%; padding-bottom:5px;" colspan="3">
                        <s:property value="%{formatDateTime(lastEdit)}"/>&nbsp;&nbsp;<s:property value="changedByName"/>
                        &nbsp;&nbsp;<strong><s:property value="workItemID"/>&nbsp;<s:property value="title"/></strong>
                    </td>
                </tr>
                <s:if test="renderType==1">
                    <s:iterator value="historyEntries" status="status">
                        <tr class="dataRow1">
                            <td></td>
                            <s:if test="changedText!=null">
                                <td class="dataName" colspan="3" style="white-space: normal;overflow:hidden;">
                                    <s:property escapeHtml="false" value="changedText"/>
                                </td>
                            </s:if>
                            <s:else>
                                <td class="dataValue" style="white-space: normal;">
								    <span class="dataEmphasize">
									    <s:property escapeHtml="false" value="newValue"/>
                                    </span>
                                 </td>
                                 <td class="dataValue" style="white-space: normal;">
								    <span class="dataEmphasize">
									    <s:property escapeHtml="false" value="oldValue"/>
								    </span>
                                 </td>
                            </s:else>
                        </tr>
                    </s:iterator>
                    <s:iterator value="historyLongEntries" status="status">
                        <tr>
                            <td>
                                &nbsp;
                            </td>
                            <s:if test="diff!=null">
                                <td colspan="3" class="dataValue" style="white-space: normal;vertical-align: top;padding-bottom: 2px;">
                                    <s:property escapeHtml="false" value="diff"/>
                                </td>
                            </s:if>
                            <s:else>
                               <s:if test="fieldLabel!=null">
                                   <td colspan="3" class="dataName" style="white-space: normal;height: 19px">
                                       <strong>
                                           <s:property escapeHtml="false" value="fieldLabel"/>
                                       </strong>
                                   </td>
                               </s:if>
                                <s:if test="newValue!=null">
                                    <td colspan="3" class="dataValue" style="white-space: normal;vertical-align: top;padding-bottom: 2px;">
                                        <s:property escapeHtml="false" value="newValue"/>
                                    </td>
                                </s:if>
                                <s:if test="oldValue!=null">
                                    <td colspan="3" class="dataValue" style="white-space: normal;vertical-align: top;padding-bottom: 2px;">
                                        <s:property escapeHtml="false" value="oldValue"/>
                                    </td>
                                </s:if>
                            </s:else>
                        </tr>
                    </s:iterator>
                </s:if>

                <s:if test="renderType==2">
                    <s:iterator value="historyEntries" status="status">
                        <s:if test="oldValue==null">
                            <tr class="dataRow2">
                                <td>&nbsp; </td>
                                <td class="dataName">
                                    <strong><s:property value="fieldLabel"/></strong>
                                </td>
                                <td class="dataValue">
						<span class="dataEmphasize">
							<s:property escape="false" value="newValue"/>
						</span>
                                </td>
                            </tr>
                        </s:if>
                        <s:else>
                            <tr>
                                <td>&nbsp; </td>
                                <s:if test="fieldLabel==null">
                                    <s:set name="imgWidth" value="'width='+newValue+'px'"></s:set>
                                    <td colspan="2">
                                        <img  src='<s:property value="#request.appContextPath"/>/downloadAttachment.action?workItemID=<s:property value="workItemID"/>&attachKey=<s:property value="oldValue"/>'
                                              border="0" alt="image" <s:property value='#imgWidth'/>
                                                />
                                    </td>
                                </s:if>
                                <s:else>
                                    <s:url id="downloadAttachment" action="downloadAttachment" >
                                        <s:param name="workItemID" value="%{workItemID}"/>
                                        <s:param name="attachKey" value="%{oldValue}"/>
                                    </s:url>
                                    <td class="dataName">
                                        <strong><s:property value="fieldLabel"/></strong>
                                    </td>
                                    <td class="dataValue">
								<span class="dataEmphasize">
									<s:a href="%{downloadAttachment}"><s:property escape="false" value="newValue"/></s:a>
								</span>
                                    </td>
                                </s:else>
                            </tr>
                        </s:else>
                    </s:iterator>
                </s:if>

                <s:if test="renderType==3">
                    <tr>
                        <td></td>
                        <td colspan="3" style="padding:5px 0px 0px 0px;">
                            <s:action name="printItem2" executeResult="true">
                                <s:param name="workItemID" value="workItemID"/>
                                <s:param name="hideHeader" value="true"/>
                                <s:param name="hideChildren" value="true"/>
                                <s:param name="hideAttachments" value="true"/>
                                <s:param name="hideExpense" value="true"/>
                                <s:param name="hideHistory" value="true"/>
                            </s:action>
                        </td>
                    </tr>
                </s:if>


            </table>
        </div>
    </s:iterator>
</s:if>
