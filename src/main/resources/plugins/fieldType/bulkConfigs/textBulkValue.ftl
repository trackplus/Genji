<#assign s=JspTaglibs["/WEB-INF/tlds/struts2/struts-tags.tld"]>
<!-- $Source$ -->	
<@s.textfield name="displayValueMap[0]" id="bulkField%{fieldID}" theme="simple"
	    disabled="fieldDisabled" onchange="updateBulkValue(%{fieldID})" size="40"/>	   