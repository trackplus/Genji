<#assign s=JspTaglibs["/WEB-INF/tlds/struts2/struts-tags.tld"]>
<@s.hidden name="parentID"/>    
<@s.hidden name="params.parentID" value="%{params.parentID}"/>
<div class="containerRow1" width="100%">
	<table>
	  <TR>
			<td class="dataName"><@s.property value="params.projectLabel"/></td>
			<td class="dataValue">
				<@s.select onchange="javascript:doReload()" id="projectID" name="projectID" theme="simple" 
					list="params.projectList"  value="params.projectID" 
					listKey="objectID" listValue="label" title="%{params.projectTooltip}">
				</@s.select>
			 </td>
		 </TR>
		 <TR>
			<td class="dataName"><@s.property value="params.issueTypeLabel"/></td>
			<td class="dataValue">
			    <@s.select onchange="javascript:doReload()" id="issueTypeID" name="issueTypeID" theme="simple" 
			    	list="params.issueTypeList"  value="params.issueTypeID" 
					listKey="objectID" listValue="label" title="%{params.issueTypeTooltip}">
				</@s.select>    
			 </td>
		 </TR>
		<@s.if test="params.accessLevelVisible"> 
		 <TR>
			<td class="dataName"><@s.property value="params.accessLevelLabel"/></td>
			<td class="dataValue">
				<@s.checkbox theme="simple" name="params.accessLevelFlag" value="params.accessLevelFlag" title="%{params.accessLevelTooltip}"></@s.checkbox>
			 </td>
		 </TR>
		</@s.if>	 
	</table>
</div>