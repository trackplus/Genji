<#assign s=JspTaglibs["/WEB-INF/tlds/struts2/struts-tags.tld"]>
<@s.if test="invisible">
	{html:'&nbsp;'}
</@s.if>
<@s.else>
	{
		xtype: 'displayfield',
		fieldLabel: '${label}',
		value:'<@s.property value="custom.displayValue"/>'
	}
</@s.else>
