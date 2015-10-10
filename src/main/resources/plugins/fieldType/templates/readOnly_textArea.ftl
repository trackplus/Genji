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
			<textarea  title="${tooltip}" readonly="true" cols="79" rows="10" style="width: 740px;"><@s.property value="${nameMapping}"/></textarea>
		</div>
		<div id="edit_${nameMapping}" style="display:none;">
			<textarea id ="${nameMapping}" name="${nameMapping}" title="${tooltip}" <@s.if test="readonly"> disabled="disabled" </@s.if>
		      <@s.if test="custom.maxLength!=null">
			      onkeyup="javascript:maxSize(document.forms[1]['${nameMapping}'],<@s.property value="custom.maxLength"/>)" 
		      </@s.if>
		      cols="79" rows="10" style="width: 740px;"><@s.property value="${nameMapping}"/></textarea>
	 	</div>
	 	--> 
	</@s.else>
</@s.else>