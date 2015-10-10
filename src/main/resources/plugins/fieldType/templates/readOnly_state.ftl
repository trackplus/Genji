<#assign s=JspTaglibs["/WEB-INF/tlds/struts2/struts-tags.tld"]>
<@s.if test="invisible">
	{html:'&nbsp;'}
</@s.if>
<@s.else>
	<@s.if test="readonly||printItemEditable==false">
		{
			xtype: 'displayfield',
			fieldLabel: '${label}',
			value:'<@s.property value="custom.displayValue"/>'
		}
		
	</@s.if>
	<@s.else>
		{
			xtype: 'displayfield',
			fieldLabel: '${label}',
			value:'<@s.property value="custom.displayValue"/>'
		}
		<#--
		<div class="editableField" id="readOnly_${nameMapping}" ondblclick="changeToEdit('${nameMapping}')">	
			<@s.property value="custom.displayValue"/>
		</div>
		<div id="edit_${nameMapping}" style="display:none;">
			<@s.if test='custom.hasHeader'>
			<@s.select theme="simple"
				id="${nameMapping}"
		        name="${nameMapping}"
		        multiple="false"
		        list="custom.list" listKey="${custom.listKey}"
		        listValue="${custom.listValue}"
		        title="${tooltip}"
		        disabled='readonly'
		        headerKey=''
		        headerValue="%{getText('common.lbl.headerText')}"
		        emptyOption="true"
		        onkeypress="onEnterpress(event);"
		        >
		    </@s.select>		
		</@s.if>
		<@s.else>
		    <@s.select theme="simple"
		    	id="${nameMapping}"
		        name="${nameMapping}"
		        multiple="false"
		        list="custom.list" listKey="${custom.listKey}"
		        listValue="${custom.listValue}"
		        title="${tooltip}"
		        onkeypress="onEnterpress(event);"
		        disabled='readonly'>
		    </@s.select>
	    </@s.else>
		</div>
		-->
	</@s.else>
</@s.else>
