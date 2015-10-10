{
	xtype: 'textfield',
	fieldLabel: '${label}',
	name: '${nameMapping}',
	allowBlank:false,
	value:'<@s.property value="%{escapeJavaScript(${nameMapping})}"/>',
	anchor:'100%'
}
<#--
<#assign s=JspTaglibs["/WEB-INF/tlds/struts2/struts-tags.tld"]>
<@s.if test="invisible">
	&nbsp;
</@s.if>
<@s.else>
	<table width="100%" border="0" cellspacing="0" cellpadding="0" style="table-layout:fixed;">	  
		 <tr>
		    <td width="100px" class="screenFromDataName" style="width:100px;">
		    	<label class="required">${label}&nbsp:</label>			
		    </td>
		    <td class="screenFromDataValue">
		      <@s.textfield name="${nameMapping}" theme="simple"
				size="60" maxlength="80"  
				disabled="readonly" title="${tooltip}"/>	      
		    </td>
		  </tr>
	</table>
</@s.else>
-->
