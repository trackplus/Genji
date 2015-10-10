{
		xtype: 'displayfield',
		fieldLabel: '${label}',
		value:'<@s.property value="%{escapeJavaScript(${nameMapping})}"/>'
		
	}
<#--
<#assign s=JspTaglibs["/WEB-INF/tlds/struts2/struts-tags.tld"]>
<@s.if test="invisible">
	{html:'&nbsp;'}
</@s.if>
<@s.else>
	<@s.if test="readonly||printItemEditable==false">
		<table width="100%" border="0" cellspacing="0" cellpadding="0" style="table-layout:fixed;">
			<tr>
				<td width="100px" class="screenFromDataName" valign="top" style="width:100px;background:none">
					<label class="required">${label}&nbsp:</label>
				</td>
				<td class="screenFromDataValue" valign="top" style="background:none;border-right:none;">
					<div class="olist descriptionWrapper"><div class="ulist">
						<@s.if test="custom.displayValue!=null">
							${custom.displayValue}
						</@s.if>	
					</div></div>
				</td>
			</tr>
		</table>
	</@s.if>
	<@s.else>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">	  
			<tr>
				<td width="100px" class="screenFromDataName" valign="top" style=" width:100px;background:none">
					<label class="required">${label}&nbsp:</label>			
				</td>
				<td class="screenFromDataValue" valign="top" style="background:none">
					<div class="editableField" id="readOnly_${nameMapping}" style="height:100%;" ondblclick="changeToEdit('${nameMapping}',true)">
						<div class="olist descriptionWrapper"><div class="ulist">
							<@s.if test="custom.displayValue!=null">
								${custom.displayValue}
							</@s.if>
						</div></div>
					</div>
				<span id="edit_${nameMapping}" style="display:none;">
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
								cols="79" rows="10" style="width: 740px;"><@s.property value="${nameMapping}"/></textarea> 		
					</@s.else>
				</span>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<div class="errorMessage" id="err_${nameMapping}"></div>
				</td>
			</tr>
		</table>
	</@s.else>	
</@s.else>
-->