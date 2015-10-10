<@s.url id="printUrl" value="/printItem.action" includeParams="none">
	<@s.param name="key" value="${nameMapping}" />
</@s.url>
{
		xtype: 'displayfield',
		fieldLabel: '${label}',
		anchor: '100%',
		<@s.if test="${nameMapping}!=null">
			value:'<a href="<@s.property value='#printUrl'/>" class="parentid"><@s.property value="${nameMapping}"/></a>'+
				'&nbsp;<a href="<@s.property value='#printUrl'/>" class="parent_synopsys"><@s.property value="session.parentSynopsis"/></a>'	
		</@s.if>
		<@s.else>
			value:''
		</@s.else>
}
