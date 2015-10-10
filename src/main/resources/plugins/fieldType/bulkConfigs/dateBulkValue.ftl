<#assign s=JspTaglibs["/WEB-INF/tlds/struts2/struts-tags.tld"]>

<!-- $Source$ -->
<@s.set name="iconsPath"
    	value="'${request.getContextPath()}'+'/design/'+#session.userDesignPath+'/icons/'"/>

<@s.set name="datePickerHidden"
    	value="%{'block'}"/>
<@s.if test="fieldDisabled">
<@s.set name="datePickerHidden"
    	value="%{'none'}"/>
</@s.if>

<table cellPadding="0" cellSpacing="0">
	<tr>
		<td style="padding-right: 4px;">							
			<@s.textfield name="displayValueMap[0]" id="bulkField%{fieldID}" theme="simple"
					size="10" maxlength="15" disabled="fieldDisabled"
					cssStyle="width:80px;padding:0px;" 
					onchange="updateBulkValue(%{fieldID})"/>	
		</td>
		<td>
			<img width="16" height="16" src='<@s.property value="#request.iconsPath+'calendar.gif'"/>' 
				id="datePicker<@s.property value='fieldID'/>" alt="Date picker" 
				style="display:<@s.property value='#datePickerHidden'/>"
 				onmouseover="mouseOverTrigBtn('bulkField<@s.property value="fieldID"/>', 
					'datePicker<@s.property value="fieldID"/>', '<@s.property value="jsDateFormat"/>')" 
					onmouseout="this.style.background=''" />
		</td>
	</tr>
</table>
