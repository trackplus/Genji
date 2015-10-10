{
	xtype: 'textarea',
	fieldLabel: '${label}',
	name: '${nameMapping}',
	value:'<@s.property value="%{escapeJavaScript(${nameMapping})}"/>',
	allowBlank:true
}
<#--
<@s.if test="invisible">
	&nbsp;
</@s.if>
<@s.else>
	<textarea id ="${nameMapping}" name="${nameMapping}" title="${tooltip}" <@s.if test="readonly"> disabled="disabled" </@s.if>
	  <@s.if test="custom.maxLength!=null">
	      onkeyup="javascript:maxSize(document.forms[1]['${nameMapping}'],<@s.property value="custom.maxLength"/>)" 
	  </@s.if>
	  cols="79" rows="10" style="width: 740px;"><@s.property value="${nameMapping}"/></textarea>
</@s.else>
-->