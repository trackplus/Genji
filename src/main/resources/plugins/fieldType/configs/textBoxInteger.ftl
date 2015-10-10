<#assign s=JspTaglibs["/WEB-INF/tlds/struts2/struts-tags.tld"]>
<table>
	<tr>
	    <td class="dataRow4">
	    	<@s.i18n name="resources.FieldTypeResources.FieldTypeResources">
	      		<@s.text name ="customTextBoxInteger.prompt.defaultInteger"/>
	      	</@s.i18n>
	    </td>
	    <td class="dataRow4" colspan="2">
	      	<@s.textfield name="textBoxSettingsList[0].defaultInteger"
	      		theme="simple" disabled="inheritedConfig"/>
	    </td>
	</tr>
	<tr>
	    <td class="dataRow4">
	    	<@s.i18n name="resources.FieldTypeResources.FieldTypeResources">
	      		<@s.text name ="customTextBoxInteger.prompt.minValue"/>
	      	</@s.i18n>
	    </td>	    
	    <td class="dataRow4" colspan="2">
	      	<@s.textfield name="textBoxSettingsList[0].minInteger" 
	      		theme="simple" disabled="inheritedConfig"/>
	    </td>
	</tr>
	<tr>
	    <td class="dataRow4">
	    	<@s.i18n name="resources.FieldTypeResources.FieldTypeResources">
	      		<@s.text name ="customTextBoxInteger.prompt.maxValue"/>
	      	</@s.i18n>
	    </td>	    	    
	    <td class="dataRow4" colspan="2">
	      	<@s.textfield name="textBoxSettingsList[0].maxInteger"
	      		theme="simple" disabled="inheritedConfig"/>
	    </td>
	</tr>
</table>	