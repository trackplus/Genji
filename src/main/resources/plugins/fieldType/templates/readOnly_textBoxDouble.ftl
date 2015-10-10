<#assign s=JspTaglibs["/WEB-INF/tlds/struts2/struts-tags.tld"]>
<@s.if test="invisible">
	{html:'&nbsp;'}
</@s.if>
<@s.else>	
	<@s.if test="readonly||printItemEditable==false">
		{
			xtype: 'displayfield',
			fieldLabel: '${label}',
			value:'<@s.property value="custom.doubleFormatted"/>'
		}
			
	</@s.if>
	<@s.else>
		{
			xtype: 'displayfield',
			fieldLabel: '${label}',
			value:'<@s.property value="custom.doubleFormatted"/>'
		}
		<#--
		<div class="editableField" id="readOnly_${nameMapping}" ondblclick="changeToEdit('${nameMapping}')">
			<@s.property value="custom.doubleFormatted"/>&nbsp;
		</div>
		<div id="edit_${nameMapping}" style="display:none;">
			<@s.if test='fieldsWithConversionError.contains("${nameMapping}")'>
		  		<@s.textfield name="${nameMapping}" theme="simple" disabled="readonly"
		       		title="${tooltip}"/>                	
		    </@s.if> 
		    <@s.else>
		    	<@s.textfield name="${nameMapping}" theme="simple" disabled="readonly"
		       		value="${custom.doubleFormatted}" title="${tooltip}"/>                	
		    </@s.else>
		</div>
		-->
	</@s.else>
</@s.else>
