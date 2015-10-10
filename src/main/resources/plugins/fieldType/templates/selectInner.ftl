<@s.set name="currentJSreload" value="custom.jsReload${parameterCode}"/>
<@s.if test='custom.hasHeader'>
	<@s.select theme="simple"
        id="${custom.nameMapping}${parameterCode}"
		name="${custom.nameMapping}${parameterCode}"
		multiple="false"
		list="custom.list${parameterCode}" listKey="${custom.listKey}"
		listValue="${custom.listValue}"
		onchange="${currentJSreload}"
		title="${tooltip}"
		disabled="readonly"
        headerKey=''
        headerValue="%{getText('common.lbl.headerText')}"
        emptyOption="false">
    </@s.select>		
	</@s.if>
	<@s.else>
	<@s.select theme="simple"
		id="${custom.nameMapping}${parameterCode}"
		name="${custom.nameMapping}${parameterCode}"
		multiple="false"
		list="custom.list${parameterCode}" listKey="${custom.listKey}"
		listValue="${custom.listValue}"
		onchange="${currentJSreload}"
		title="${tooltip}"
		disabled="readonly">
	</@s.select>
</@s.else>