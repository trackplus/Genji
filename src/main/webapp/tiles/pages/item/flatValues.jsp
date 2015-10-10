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
<s:set name="icons16x16Path"
	value="#request.appContextPath+'/design/'+#session.userDesignPath+'/16x16/'"/>

<s:if test="flatHistoryList.size()>0">
    <div style="margin-left:5px" class="flatValueLabel">
        <s:property value="flatValueLabel"/>
    </div>
</s:if>
<s:iterator value="flatHistoryList" status="status">
    <s:if test="#status.first">
        <div class="historyRow" style="border-top-width:0px; margin-left: 0px;margin-right:0px;margin-top:5px;background-color: white;">
    </s:if>
    <s:else>
        <div class="historyRow" style="margin-left: 0px;margin-right:0px;margin-top:5px;background-color: white;">
    </s:else>
		<table width="100%" border="0" cellPadding="0" cellSpacing="0">
			<tr style="height:0px;padding:0px;border:none;margin:0px;">
                <td style="width:20px;padding:0px;border:none;margin:0px;"></td>
                <td style="width:120px;padding:0px;border:none;margin:0px;"></td>
                <td style="padding:0px;border:none;margin:0px;"></td>
			</tr>
			<tr class="dataRow1">
                <td class="dataName" style="padding-bottom:5px">
                    <img width="16" height="16" src="<s:property value='icons16x16Path'/><s:property value='iconName'/>"/>
                </td>
				<td class="dataName" colspan="2">
					<s:property value="%{formatDateTime(lastEdit)}"/>&nbsp;&nbsp;<s:property value="changedByName"/>
				</td>
			</tr>
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
		</table>
	</div>
	<div class="containerSeparator">&nbsp;</div>
</s:iterator>
