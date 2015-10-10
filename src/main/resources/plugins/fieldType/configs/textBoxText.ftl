<#assign s=JspTaglibs["/WEB-INF/tlds/struts2/struts-tags.tld"]>
<table>
	<tr>
	    <td class="dataRow4">
	    	<@s.i18n name="resources.FieldTypeResources.FieldTypeResources">
	      		<@s.text name ="customTextBoxText.prompt.defaultText"/>
	      	</@s.i18n>
	    </td>
	    <td class="dataRow4" colspan="2">
	      	<@s.textfield name="textBoxSettingsList[0].defaultText" 
	      		theme="simple" disabled="inheritedConfig" size="40"/>
	    </td>
	</tr>
	<tr>
	    <td class="dataRow4">
	    	<@s.i18n name="resources.FieldTypeResources.FieldTypeResources">
	      		<@s.text name="customTextBoxText.prompt.minLength"/>
	      	</@s.i18n>
	    </td>	    
	    <td class="dataRow4" colspan="2">
	      	<@s.textfield name="textBoxSettingsList[0].minTextLength" 
		      	theme="simple" disabled="inheritedConfig" size="10"/>
	    </td>
	</tr>
	<tr>
	    <td class="dataRow4">
	    	<@s.i18n name="resources.FieldTypeResources.FieldTypeResources">
	      		<@s.text name="customTextBoxText.prompt.maxLength"/>
	      	</@s.i18n>
	    </td>	    	    
	    <td class="dataRow4" colspan="2">
	      	<@s.textfield name="textBoxSettingsList[0].maxTextLength" 
	      		theme="simple" disabled="inheritedConfig" size="10"/>
	    </td>
	</tr>
</table>