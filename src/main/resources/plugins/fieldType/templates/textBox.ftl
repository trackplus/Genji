<#assign s=JspTaglibs["/WEB-INF/tlds/struts2/struts-tags.tld"]>
{
	xtype: 'textfield',
	fieldLabel: '${label}',
	name: '${nameMapping}',
	value:'<@s.property value="%{escapeJavaScript(${nameMapping})}"/>',
	allowBlank:true
}
<#--
<@s.if test="invisible">
	&nbsp;
</@s.if>
<@s.else>
	
	<@s.textfield name="${nameMapping}" theme="simple" 
			disabled="readonly" title="${tooltip}"/>
</@s.else>
-->