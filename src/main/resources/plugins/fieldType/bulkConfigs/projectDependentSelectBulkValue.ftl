<#assign s=JspTaglibs["/WEB-INF/tlds/struts2/struts-tags.tld"]>
<!-- $Source$ -->
<table>
<@s.iterator value="possibleValues">
<tr>
<td>
	<@s.property value="involvedProjectsMap[key].label"/>&nbsp;
</td>
<td>
	<@s.select name="displayValueMap[%{key}]" theme="simple" 
		id="bulkField%{fieldID}project%{key}" 
  		list="%{possibleValues[key]}"
  		listKey="objectID"
  		listValue="label"  		
  		disabled="fieldDisabled"
  		onchange="updateBulkValue(%{fieldID})"/>
</td>
<tr> 	
</@s.iterator>
<table>