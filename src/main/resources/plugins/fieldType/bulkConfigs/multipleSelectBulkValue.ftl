<#assign s=JspTaglibs["/WEB-INF/tlds/struts2/struts-tags.tld"]>
<!-- $Source$ -->
<@s.select name="displayValueMap[0]" id="bulkField%{fieldID}" theme="simple"
  		list="%{possibleValues}"
  		listKey="objectID"
  		listValue="label"  		
  		disabled="fieldDisabled"
  		multiple="true"
		size="8"
  		onchange="updateBulkValue(%{fieldID})"/>