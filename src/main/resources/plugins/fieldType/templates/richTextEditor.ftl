{
	xtype: 'htmleditor',
	fieldLabel: '${label}',
	name: '${nameMapping}',
	allowBlank:true,
	value:'<@s.property value="%{escapeJavaScript(${nameMapping})}"/>',
	anchor:'100%'
}
<#--

<#assign s=JspTaglibs["/WEB-INF/tlds/struts2/struts-tags.tld"]>
<table width="100%" border="0" cellspacing="0" cellpadding="0">	  
	<tr>
		<td width="100px" class="screenFromDataName" valign="top" style="background:none">
			<label class="<@s.if test="required">required</@s.if>" for="${nameMapping}">${label}&nbsp:</label>
		</td>
		<td class="screenFromDataValue" valign="top" style="background:none">
			<@s.if test="readonly">
				<div class="olist descriptionWrapper"><div class="ulist">
					<@s.if test="custom.displayValue!=null">
						${custom.displayValue}
					</@s.if>
				</div></div>
			</@s.if>
			<@s.else>
					<textarea richtexteditor="true" label="${label}" id ="${nameMapping}" name="${nameMapping}" title="${tooltip}" 
			<@s.if test="custom.maxLength!=null">
				maxSize=<@s.property value="custom.maxLength"/> 
				onkeyup="javascript:maxSize(document.forms[1]['${nameMapping}'],<@s.property value="custom.maxLength"/>)" 
			</@s.if>
				cols="79" rows="10" style="width: 100%;"><@s.property value="${nameMapping}"/></textarea> 		
			</@s.else>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<div class="errorMessage" id="err_${nameMapping}"></div>
		</td>
	</tr>
</table>
-->
