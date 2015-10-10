<#assign s=JspTaglibs["/WEB-INF/tlds/struts2/struts-tags.tld"]>
<table>	
	<tr>
	    <td class="dataRow4">
	    	<@s.i18n name="resources.FieldTypeResources.FieldTypeResources">
	      		<@s.text name="customTextBoxDouble.prompt.defaultDouble"/>
	      	</@s.i18n>
	    </td>
	    <td class="dataRow4" colspan="2">
	      	<@s.if test="fieldErrors['textBoxSettingsList[0].defaultDouble']!=null">
	    		<@s.textfield name="textBoxSettingsList[0].defaultDouble"	      		
		      		theme="simple" disabled="inheritedConfig"/>
	    	</@s.if>
			<@s.else>
				<@s.textfield name="textBoxSettingsList[0].defaultDouble"
		      		value="%{getDoubleFormatted(textBoxSettingsList[0].defaultDouble)}" 
		      		theme="simple" disabled="inheritedConfig"/>
			</@s.else>    		      	
	    </td>
	</tr>		
	<tr>
	    <td class="dataRow4">
	    	<@s.i18n name="resources.FieldTypeResources.FieldTypeResources">
	      		<@s.text name ="customTextBoxDouble.prompt.minValue"/>
	      	</@s.i18n>
	    </td>	    
	    <td class="dataRow4" colspan="2">
	    	<@s.if test="fieldErrors['textBoxSettingsList[0].minDouble']!=null">
	    		<@s.textfield name="textBoxSettingsList[0].minDouble"	      		
		      		theme="simple" disabled="inheritedConfig"/>
	    	</@s.if>
	    	<@s.else>
		    	<@s.textfield name="textBoxSettingsList[0].minDouble"
		      		value="%{getDoubleFormatted(textBoxSettingsList[0].minDouble)}" 
	    	  		theme="simple" disabled="inheritedConfig"/>
	    	</@s.else>	      	
	    </td>
	</tr>
	<tr>
	    <td class="dataRow4">
	    	<@s.i18n name="resources.FieldTypeResources.FieldTypeResources">
	      		<@s.text name ="customTextBoxDouble.prompt.maxValue"/>
	      	</@s.i18n>
	    </td>	    	    
	    <td class="dataRow4" colspan="2">
	    	<@s.if test="fieldErrors['textBoxSettingsList[0].maxDouble']!=null">
	    		<@s.textfield name="textBoxSettingsList[0].maxDouble"	      		
		      		theme="simple" disabled="inheritedConfig"/>
	    	</@s.if>
	    	<@s.else>
	    		<@s.textfield name="textBoxSettingsList[0].maxDouble"
	      			value="%{getDoubleFormatted(textBoxSettingsList[0].maxDouble)}" 
		      		theme="simple" disabled="inheritedConfig"/>    		
	    	</@s.else>
	    </td>
	</tr>
	<tr>
	    <td class="dataRow4">
	    	<@s.i18n name="resources.FieldTypeResources.FieldTypeResources">
	      		<@s.text name ="customTextBoxDouble.prompt.maxDecimalDigits"/>
	      	</@s.i18n>
	    </td>	    	    
	    <td class="dataRow4" colspan="2">
	      	<@s.textfield name="textBoxSettingsList[0].maxDecimalDigit"
	      		theme="simple" disabled="inheritedConfig"/>
	    </td>
	</tr>
</table>