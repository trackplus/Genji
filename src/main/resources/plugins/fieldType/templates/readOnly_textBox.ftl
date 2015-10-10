<#assign s=JspTaglibs["/WEB-INF/tlds/struts2/struts-tags.tld"]>
<@s.if test="invisible">
	{html:'&nbsp;'}
</@s.if>
<@s.else>
	<@s.if test="readonly||printItemEditable==false">
		{
			xtype: 'displayfield',
			fieldLabel: '${label}',
			value:'<@s.property value="%{escapeJavaScript(${nameMapping})}"/>'
		}
	</@s.if>
	<@s.else>
		{
			xtype: 'displayfield',
			fieldLabel: '${label}',
			value:'<@s.property value="%{escapeJavaScript(${nameMapping})}"/>'
		}
		<#--
		<div class="editableField" id="readOnly_${nameMapping}" ondblclick="changeToEdit('${nameMapping}')">
			<@s.property value="${nameMapping}"/>&nbsp;
		</div>
		<div id="edit_${nameMapping}" style="display:none;">
			<@s.textfield id="${nameMapping}" name="${nameMapping}" theme="simple" 
				disabled="readonly" title="${tooltip}" onkeypress="onEnterpress(event);"/>
		</div>
		-->
	</@s.else>	
</@s.else>
