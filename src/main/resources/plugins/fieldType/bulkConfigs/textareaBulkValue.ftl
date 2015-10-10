<#assign s=JspTaglibs["/WEB-INF/tlds/struts2/struts-tags.tld"]>
<!-- $Source$ -->	
<@s.textarea name="displayValueMap[0]" id="bulkField%{fieldID}" theme="simple" 
		cols="80" rows="6"
	    disabled="fieldDisabled" onchange="updateBulkValue(%{fieldID})"/>	   