<#assign s=JspTaglibs["/WEB-INF/tlds/struts2/struts-tags.tld"]>
<@s.if test="invisible">
	{html:'&nbsp;'}
</@s.if>
<@s.else>
	<@s.if test="readonly||printItemEditable==false">
		{
			xtype: 'displayfield',
			fieldLabel: '${label}',
			value:'<@s.property value="custom.dateFormatted"/>'
		}
	</@s.if>
	<@s.else>
		{
			xtype: 'displayfield',
			fieldLabel: '${label}',
			value:'<@s.property value="custom.dateFormatted"/>'
		}
		<#--
		<div class="editableField" id="readOnly_${nameMapping}" ondblclick="changeToEdit('${nameMapping}')">
			<@s.property value="custom.dateFormatted"/>&nbsp;
		</div>	
	<span id="edit_${nameMapping}" style="display:none;">
		<table border="0" cellspacing="0" cellpadding="0">
        <tr>
            <td>                	        	
           		<@s.if test='fieldsWithConversionError.contains("${nameMapping}")'>
	           		<@s.textfield name="${nameMapping}" theme="simple"  id="${nameMapping}" disabled="readonly"
                   		title="${tooltip}" cssStyle="width:80px"/>                	
                </@s.if> 
                <@s.else>
                	<@s.textfield name="${nameMapping}" theme="simple"  id="${nameMapping}" disabled="readonly"
                   		value="${custom.dateFormatted}" title="${tooltip}" cssStyle="width:80px"/>                	
                </@s.else>
            </td>
            <td nowrap>
                  &nbsp;
                  <@s.if test="%{!readonly}">
                      <img width="16" height="16" src='${iconsPath}calendar.gif' id="trig${nameMapping}" alt="start date" onmouseover="mouseOverTrigBtn('${nameMapping}','${custom.jsCalDateFormat}')" onmouseout="this.style.background=''" />
                  </@s.if>
            </td>
         </tr>
     </table>
	</span>
	-->
	</@s.else> 	
</@s.else>