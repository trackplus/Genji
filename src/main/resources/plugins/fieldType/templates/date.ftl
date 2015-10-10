{
	xtype: 'datefield',
	fieldLabel: '${label}',
	name: '${nameMapping}',
	allowBlank:true
}
<#--
<@s.set name="iconsPath" 
    	value="'${request.getContextPath()}'+'/design/'+#session.userDesignPath+'/icons/'"/>
<@s.if test="invisible">
	&nbsp;
</@s.if>
<@s.else>
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
</@s.else>
-->