<#assign s=JspTaglibs["/WEB-INF/tlds/struts2/struts-tags.tld"]>
<!-- $Source$ -->
<@s.iterator value="possibleValues" status="status">
<@s.select name="displayValueMap[%{key}]" theme="simple" 
		id="bulkField%{fieldID}param%{key}" 
  		list="%{possibleValues[key]}"  		
  		listKey="objectID"
  		listValue="label"  		
  		disabled="fieldDisabled"
  		onchange="updateBulkRelation(%{fieldID})"/>
</@s.iterator>