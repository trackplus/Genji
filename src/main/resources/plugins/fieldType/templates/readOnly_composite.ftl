<#assign s=JspTaglibs["/WEB-INF/tlds/struts2/struts-tags.tld"]>
<@s.if test="invisible">
	&nbsp;
</@s.if>
<@s.else>
	<@s.property value="custom.displayValue"/>
</@s.else>