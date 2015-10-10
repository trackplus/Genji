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
		<#--
		<table width="100%" border="0" cellspacing="0" cellpadding="0" style="table-layout:fixed;">	  
			 <tr>
			    <td width="100px" class="screenFromDataName" style="width:100px;">
			    	<label class="required">${label}&nbsp:</label>			
			    </td>
			    <td class="screenFromDataValue">
			      <@s.property value="${nameMapping}"/>
			    </td>
			  </tr>
		</table>
		-->
	</@s.if>	
	<@s.else>
		{
			xtype: 'displayfield',
			fieldLabel: '${label}',
			value:'<@s.property value="%{escapeJavaScript(${nameMapping})}"/>'
		}
		<#--
		<table width="100%" border="0" cellspacing="0" cellpadding="0">	  
			 <tr>
			    <td width="100px" class="screenFromDataName">
			    	<label class="required">${label}&nbsp:</label>			
			    </td>
			    <td class="screenFromDataValue">
			    	<div class="editableField" id="readOnly_${nameMapping}" ondblclick="changeToEdit('${nameMapping}')">
			      		<@s.property value="${nameMapping}"/>
			      	</div>
			      	<span id="edit_${nameMapping}" style="display:none;">
			      		<@s.textfield id="${nameMapping}" name="${nameMapping}" theme="simple"
							size="60" maxlength="80"  
							disabled="readonly" title="${tooltip}"/>
			      	</span>
			    </td>
			  </tr>
		</table>
		-->
	</@s.else>
</@s.else>
