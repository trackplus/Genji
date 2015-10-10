<#assign s=JspTaglibs["/WEB-INF/tlds/struts2/struts-tags.tld"]>
{
	xtype: 'displayfield',
	fieldLabel: '${label}',
	name: '${nameMapping}',
	allowBlank:true,
	value:'${custom.displayValue}'
}
<#--
<@s.hidden name="${nameMapping}" value="%{${nameMapping}}"/>
<label title="${tooltip}">
	${custom.displayValue}
</label>
-->