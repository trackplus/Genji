<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	/* set the context path as a request attribute because
	only the request attributes map is available from OGNL
	(not the entire HttpRequest object )*/
	request.setAttribute("appContextPath", request.getContextPath());
%>
<%-- children --%>
<s:if test="children!=null && children.size()>0">
    <div class="containerSeparator" style="height: 10px;">&nbsp;</div>
    <div style="margin-left:5px" class="flatValueLabel">
        <s:text name="item.printItem.lbl.childIssues"/>:&nbsp;<s:property value="children.size()"/>
    </div>
    <div class="containerSeparator" style="height: 3px;">&nbsp;</div>
    <div class="containerRow2" style="margin:0px 0px 0px 5px;background-color: white;">
        <table width="100%" border="0" class="smallTable">
            <tr class="dataRow1">
                <td width="75px"  class="dataName">
                    <strong><s:text name="item.lbl.itemNumber"/></strong>
                </td>
                <td width="75px"  class="dataName">
                    <strong><s:text name="field.label.4"/></strong>
                </td>
                <%--
                <td width="170px"  class="dataName">
                    <!--originator-->
                    <strong><s:text name="field.label.13"/></strong>
                </td>--%>
                <td width="170px"  class="dataName">
                   <!--responsible-->
                    <strong> <s:text name="field.label.6"/></strong>
                </td>
                <td class="dataName">&nbsp;
                    <!--synopsis-->
                    <strong><s:text name="field.label.17"/></strong>
                </td>
            </tr>
            <s:iterator value="children">
            <tr>
                <td class="dataName">
                    <s:property value="%{obtainWorkItemDisplayID(top)}"/>
                </td>
                <td  class="dataName">
                    <span class="dataEmphasize">
                        <s:property value="statusMap[stateID].label"/>
                    </span>
                </td>
                <%--
                <td class="dataName">
                    <s:property value="personMap[originatorID].label"/>
                </td>
                --%>
                <td class="dataName">
                    <s:property value="personMap[responsibleID].label"/>
                </td>
                <td class="dataName">&nbsp;
                    <s:property value="synopsis"/>
                </td>
            </tr>
            </s:iterator>
        </table>
    </div>
</s:if>
