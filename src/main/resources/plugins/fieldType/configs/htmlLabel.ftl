<#assign s=JspTaglibs["/WEB-INF/tlds/struts2/struts-tags.tld"]>
<table>
	<tr>
	    <td class="dataRow4">
	    	<@s.i18n name="resources.FieldTypeResources.FieldTypeResources">
	      		<@s.text name ="customHTMLLabel.labelText"/>
	      	</@s.i18n>
	    </td>
	    <td class="dataRow4">
	    	<@s.textarea name="textBoxSettingsList[0].defaultText" theme="simple" id="htmlLabel"
	    		disabled="inheritedConfig"></@s.textarea>		      	
	    </td>
	</tr>	
</table>