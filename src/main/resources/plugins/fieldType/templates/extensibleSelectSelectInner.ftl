<#assign s=JspTaglibs["/WEB-INF/tlds/struts2/struts-tags.tld"]>
<@s.if test='custom.hasHeader'>
	<@s.select theme="simple"
        id="${custom.nameMappingSelectionField}"
		name="${custom.nameMappingSelectionField}"
		multiple="false"
		list="custom.list" listKey="${custom.listKey}"
		listValue="${custom.listValue}"
		onchange="${custom.jsSelectionChange}"
		title="${tooltip}"
		disabled="readonly"
        headerKey=''
        headerValue="%{getText('common.lbl.headerText')}"
        emptyOption="false">
    </@s.select>		
	</@s.if>
	<@s.else>
	<@s.select theme="simple"
		id="${custom.nameMappingSelectionField}"
		name="${custom.nameMappingSelectionField}"
		multiple="false"
		list="custom.list" listKey="${custom.listKey}"
		listValue="${custom.listValue}"
		onchange="${custom.jsSelectionChange}"
		title="${tooltip}"
		disabled="readonly">
	</@s.select>
</@s.else>