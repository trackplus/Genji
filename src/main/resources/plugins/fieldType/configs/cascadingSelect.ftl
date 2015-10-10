<#assign s=JspTaglibs["/WEB-INF/tlds/struts2/struts-tags.tld"]>
<table>	
	<tr>
	    <td class="dataRow4">
	    	<@s.i18n name="resources.FieldTypeResources.FieldTypeResources">
	      		<@s.text name="customSelectParentChild.prompt.list"/>
	      	</@s.i18n>
	    </td>
	    <td class="dataRow4">
	      	<@s.select name="optionSettingsList[0].list"
	      		list="specificConfigData[0]"
	      		listKey="objectID"
	      		listValue="name"
	      		theme="simple"
	      		disabled="inheritedConfig"/>
	    </td>
	</tr>
</table>