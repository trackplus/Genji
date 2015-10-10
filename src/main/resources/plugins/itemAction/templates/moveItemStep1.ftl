<#assign s=JspTaglibs["/WEB-INF/tlds/struts2/struts-tags.tld"]>
<h4>
	<@s.text name="%{getText('print.prompt.itemNumber')}"/>
	<strong>
	 <span class="emphasize">
	   &nbsp;<@s.property value="workItemID"/> 
	 </span>:
	<span class="dataEmphasize">
	  <@s.property value="statusDisplay"/>
	 </span>
	   :&nbsp;<@s.property value="params.synopsis"/>
	</strong>
</h4>

<div class="containerRow1" width="100%">
	<table>
		<tr>
			<td class="dataName">
				<@s.property value="params.projectLabel"/>:
			 	<strong><@s.property value="params.oldProject"/></strong> 
			</td>
			<td class="dataName">
				<@s.property value="params.issueTypeLabel"/>:
				<strong>
				<@s.property value="params.oldIssueType"/> 
				</strong>
			</td>
		 </tr>
	</table>
</div>
<div class="containerSeparator">
</div> 
<div class="containerRow1" width="100%">
	<table>
	  	<tr>
			<td class="dataName"><@s.property value="params.projectLabel"/></td>
			<td class="dataValue">
				<@s.select onchange="javascript:doReload()" id="projectID" name="projectID" theme="simple" list="params.projectList"  
					value="params.projectID" listKey="objectID" listValue="label">
				</@s.select>
			 </td>
		 </tr>
		 <tr>
			<td class="dataName"><@s.property value="params.issueTypeLabel"/></td>
			<td class="dataValue">
			    <@s.select onchange="javascript:doReload()" id="issueTypeID" name="issueTypeID" theme="simple" list="params.issueTypeList" 
					value="params.issueTypeID" listKey="objectID" listValue="label">
				</@s.select>    
			 </td>
		 </tr>
		 <@s.if test="params.statusNeeded==true">
		 <tr>
			<td class="dataName"><@s.property value="params.statusLabel"/></td>
			<td class="dataValue">
			    <@s.select id="statusID" name="params.statusID" theme="simple" list="params.statusList" 
					value="params.statusID" listKey="objectID" listValue="label">
				</@s.select>    
			 </td>
		 </tr>
		 </@s.if>		 
	</table>
</div>