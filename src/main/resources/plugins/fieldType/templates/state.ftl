<#assign s=JspTaglibs["/WEB-INF/tlds/struts2/struts-tags.tld"]>
{xtype: 'combo',
	fieldLabel: '${label}',
	store:Ext.create('Ext.data.Store', {
		fields:[
			{type: 'string', name: '${custom.listKey}'},
			{type: 'string', name: '${custom.listValue}'}
		],
		data:[
			<@s.iterator value="custom.list" status="status1">
				<@s.if test="#status1.first"></@s.if><@s.else>,</@s.else>
				{'${custom.listKey}':'<@s.property value="objectID"/>',
				'${custom.listValue}':'<@s.property value="label"/>'}
			</@s.iterator>
		] 
	}),
	displayField: '${custom.listValue}',
	queryMode: 'local',
	name: '${nameMapping}',
	value:'<@s.property value="${nameMapping}"/>',
	forceSelection: true
}
<#--
<@s.if test="invisible">
	&nbsp;
</@s.if>
<@s.else>
	<@s.if test='custom.hasHeader'>
		<@s.select theme="simple"
	        name="${nameMapping}"
	        multiple="false"
	        list="custom.list" listKey="${custom.listKey}"
	        listValue="${custom.listValue}"
	        title="${tooltip}"
	        disabled='readonly'
	        headerKey=''
	        headerValue="%{getText('common.lbl.headerText')}"
	        emptyOption="true"
	        onchange="statusChange(this.value)"
	        >
	    </@s.select>		
	</@s.if>
	<@s.else>
	    <@s.select theme="simple"
	        name="${nameMapping}"
	        multiple="false"
	        list="custom.list" listKey="${custom.listKey}"
	        listValue="${custom.listValue}"
	        title="${tooltip}"
	        onchange="statusChange(this.value)"
	        disabled='readonly'>
	    </@s.select>
    </@s.else>
</@s.else>
-->