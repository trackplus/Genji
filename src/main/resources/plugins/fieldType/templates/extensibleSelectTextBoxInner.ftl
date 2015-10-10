<#assign s=JspTaglibs["/WEB-INF/tlds/struts2/struts-tags.tld"]>
<@s.textfield name="${custom.nameMappingTextField}" 
	id="${custom.nameMappingTextField}" theme="simple" 
	disabled="readonly" tooltip="tooltip" onkeyup="${custom.jsTextFieldChange}"/>