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
			<@s.property value="custom.displayValue"/>&nbsp;
		</div>
		<div id="edit_${nameMapping}" style="display:none;">
			<@s.select theme="simple"
		        name="${nameMapping}"
		        multiple="true"
		        list="custom.list" listKey="${custom.listKey}"
		        listValue="${custom.listValue}"
		        title="${tooltip}"
		        disabled='readonly'
		        size="8">
		    </@s.select>
		</div>
		-->
	</@s.else>		    
</@s.else>