<#assign s=JspTaglibs["/WEB-INF/tlds/struts2/struts-tags.tld"]>
<table>	
	<tr>
	    <td class="dataRow4">
	    	<@s.i18n name="resources.FieldTypeResources.FieldTypeResources">
	      		<@s.text name="customCheckBoxSingle.prompt.default"/>
	      	</@s.i18n>
	    </td>
	    <td class="dataRow4" colspan="2">
	      	<@s.checkbox name="textBoxSettingsList[0].defaultChar"
	      		theme="simple" disabled="inheritedConfig"/>
	    </td>
	</tr>
</table>	

