<#assign s=JspTaglibs["/WEB-INF/tlds/struts2/struts-tags.tld"]>

<script type="text/javascript">
<!--
/*function dateSettingChanged(value){
	var dateRadioBox = document.getElementById(value+"2");		
	if (dateRadioBox.checked==true){
		//enable
		setDisableFromTo(value, false);
	}
	else
	{
		//disable
		setDisableFromTo(value, true);
	}
}
   	   	
function setDisableFromTo(value, disableFlag){
	var dateField = document.getElementById("dateField"+value);
	var datePicker = document.getElementById("datePicker"+value);
	dateField.disabled = disableFlag;	
	if (disableFlag) {
		datePicker.style.display = 'none';
	} else {
		datePicker.style.display = 'block';
	}
}*/
//-->
</script>

<@s.set name="iconsPath"
    	value="'${request.getContextPath()}'+'/design/'+#session.userDesignPath+'/icons/'"/>
<table>			
	<tr>
	   	<td class="dataRow4">
	   		<@s.i18n name="resources.FieldTypeResources.FieldTypeResources">
	      		<@s.text name ="customTextBoxDate.prompt.defaultDate"/>
	      	</@s.i18n>
	   	</td>
	   	<td class="dataRow4">
	      	<@s.radio name="textBoxSettingsList[0].defaultOption"
	      		id="Default" 
	      		value="%{textBoxSettingsList[0].defaultOption}"  
	      		list="specificConfigData[1]" listKey="value" listValue="label"
	      		onclick="javascript:systemDateSettingChanged('Default')"
	      		disabled="inheritedConfig" theme="simple"/>
	    </td>
	    <td class="dataRow4">
    		<@s.set name="daysAfterCreateDefaultSelected" value="textBoxSettingsList[0].defaultOption==3"></@s.set>    		
			<@s.set name="displayDaysAfter" value="%{'none'}"></@s.set>
			<@s.if test="daysAfterCreateDefaultSelected">
				<@s.set name="displayDaysAfter" value="%{'block'}"></@s.set>
			</@s.if>
			<@s.if test="fieldErrors['textBoxSettingsList[0].defaultInteger']!=null">
				<@s.textfield name="textBoxSettingsList[0].defaultInteger" theme="simple"
		      		id="daysAfterCreateDefault" size="10" cssStyle="width:80px;display:%{#displayDaysAfter}" disabled="inheritedConfig"></@s.textfield>
	    	</@s.if>
			<@s.else>
				<@s.textfield name="textBoxSettingsList[0].defaultInteger" theme="simple" 
		      		id="daysAfterCreateDefault" size="10" cssStyle="width:80px;display:%{#displayDaysAfter}" disabled="inheritedConfig"></@s.textfield>
			</@s.else>			      	      		    
			<@s.set name="textBoxDefaultDateSelected" value="textBoxSettingsList[0].defaultOption==2"></@s.set>			
			<@s.set name="datePickerDefaultDisplay" value="%{'none'}"></@s.set>
			<@s.set name="textBoxDefaultDateDisplay" value="%{'none'}"></@s.set>
			<@s.if test="textBoxDefaultDateSelected">
				<@s.set name="textBoxDefaultDateDisplay" value="%{'block'}"></@s.set>
				<@s.if test="!inheritedConfig">
					<@s.set name="datePickerDefaultDisplay" value="%{'block'}"></@s.set>
				</@s.if>
			</@s.if>
			<@s.if test="fieldErrors['textBoxSettingsList[0].defaultDate']!=null">
				<@s.textfield name="textBoxSettingsList[0].defaultDate" theme="simple" 
		      		id="dateFieldDefault" size="10" maxlength="15" cssStyle="width:80px;display:%{#textBoxDefaultDateDisplay}" disabled="inheritedConfig"></@s.textfield>
	    	</@s.if>
			<@s.else>
				<@s.textfield name="textBoxSettingsList[0].defaultDate" value="%{getDateFormatted(textBoxSettingsList[0].defaultDate)}" theme="simple" 
		      		id="dateFieldDefault" size="10" maxlength="15" cssStyle="width:80px;display:%{#textBoxDefaultDateDisplay}" disabled="inheritedConfig"></@s.textfield>
			</@s.else>				      
      	</td>
      	<td class="dataRow4">       		     	
			<img width="16" height="16" src='<@s.property value="#request.iconsPath+'calendar.gif'"/>' id="datePickerDefault" alt="Date picker"
 				onmouseover="mouseOverTrigBtn('dateFieldDefault', 'datePickerDefault', '<@s.property value="jsDateFormat"/>')" 
 				onmouseout="this.style.background=''" style='display:<@s.property value="#datePickerDefaultDisplay"/>'/>	    	          			
	    </td>	    
	</tr>
</table>
<table>			
	<tr>
	    <td class="dataRow4">
	    	<@s.i18n name="resources.FieldTypeResources.FieldTypeResources">	    	
	      		<@s.text name="customTextBoxDate.prompt.minDate"/>
	      	</@s.i18n>
	    </td>
	    <td class="dataRow4">
		    <@s.radio name="textBoxSettingsList[0].minOption" id="MinValue" 
		    	value="%{textBoxSettingsList[0].minOption}"
		    	list="specificConfigData[0]" listKey="value" listValue="label"
		    	onclick="javascript:dateSettingChanged('MinValue')"
		    	disabled="inheritedConfig" theme="simple"/>
		</td>		
		<td class="dataRow4">		
	      	<@s.set name="dateFieldMinDateSelected" value="textBoxSettingsList[0].minOption==2"></@s.set>
			<@s.set name="datePickerMinDisplay" value="%{'none'}"></@s.set>
			<@s.set name="textBoxMinValueDisplay" value="%{'none'}"></@s.set>
			<@s.if test="dateFieldMinDateSelected">
				<@s.set name="textBoxMinValueDisplay" value="%{'block'}"></@s.set>
				<@s.if test="!inheritedConfig">
					<@s.set name="datePickerMinDisplay" value="%{'block'}"></@s.set>
				</@s.if>
			</@s.if>
			<@s.if test="fieldErrors['textBoxSettingsList[0].minDate']!=null">
				<@s.textfield name="textBoxSettingsList[0].minDate" theme="simple" 
		      		id="dateFieldMinValue" size="10" maxlength="15" cssStyle="width:80px;display:%{#textBoxMinValueDisplay}" disabled="inheritedConfig"></@s.textfield>
	    	</@s.if>
			<@s.else>
				<@s.textfield name="textBoxSettingsList[0].minDate" value="%{getDateFormatted(textBoxSettingsList[0].minDate)}" theme="simple" 
		  			id="dateFieldMinValue" size="10" maxlength="15" cssStyle="width:80px;display:%{#textBoxMinValueDisplay}" disabled="inheritedConfig"></@s.textfield>
			</@s.else>				      		  		
  		</td>
  		<td class="dataRow4">
			<img width="16" height="16" src='<@s.property value="#request.iconsPath+'calendar.gif'"/>' id="datePickerMinValue" alt="Date picker"
 				onmouseover="mouseOverTrigBtn('dateFieldMinValue', 'datePickerMinValue', '<@s.property value="jsDateFormat"/>')" 
 				onmouseout="this.style.background=''" style='display:<@s.property value="#datePickerMinDisplay"/>'/>
	    </td>
	</tr>
	<tr>
	    <td class="dataRow4">
			<@s.i18n name="resources.FieldTypeResources.FieldTypeResources">	    	
	      		<@s.text name="customTextBoxDate.prompt.maxDate"/>
	      	</@s.i18n>
	    </td>	    
	    <td class="dataRow4">
	    	<@s.radio name="textBoxSettingsList[0].maxOption" id="MaxValue"  
	    		value="%{textBoxSettingsList[0].maxOption}"
	    		list="specificConfigData[0]" listKey="value" listValue="label"
	    		onclick="javascript:dateSettingChanged('MaxValue')"
	    		disabled="inheritedConfig" theme="simple"/>
	    </td>	    
	    <td class="dataRow4">	    	
      		<@s.set name="dateFieldMaxDateSelected" value="textBoxSettingsList[0].maxOption==2"></@s.set>
			<@s.set name="datePickerMaxDisplay" value="%{'none'}"></@s.set>
			<@s.set name="textBoxMaxValueDisplay" value="%{'none'}"></@s.set>
			<@s.if test="dateFieldMaxDateSelected">
				<@s.set name="textBoxMaxValueDisplay" value="%{'block'}"></@s.set>
				<@s.if test="!inheritedConfig">
					<@s.set name="datePickerMaxDisplay" value="%{'block'}"></@s.set>	
				</@s.if>				
			</@s.if>	
			<@s.if test="fieldErrors['textBoxSettingsList[0].maxDate']!=null">
				<@s.textfield name="textBoxSettingsList[0].maxDate" theme="simple" 
		      		id="dateFieldMaxValue" size="10" maxlength="15" cssStyle="width:80px;display:%{#textBoxMaxValueDisplay}" disabled="inheritedConfig"></@s.textfield>
	    	</@s.if>
			<@s.else>
				<@s.textfield name="textBoxSettingsList[0].maxDate" value="%{getDateFormatted(textBoxSettingsList[0].maxDate)}" theme="simple" 
					id="dateFieldMaxValue" size="10" maxlength="15" cssStyle="width:80px;display:%{#textBoxMaxValueDisplay}" disabled="inheritedConfig"></@s.textfield>
			</@s.else>			
		</td>
		<td class="dataRow4">
			<img width="16" height="16" src='<@s.property value="#request.iconsPath+'calendar.gif'"/>' id="datePickerMaxValue" alt="Date picker"
 				onmouseover="mouseOverTrigBtn('dateFieldMaxValue', 'datePickerMaxValue', '<@s.property value="jsDateFormat"/>')" 
 				onmouseout="this.style.background=''" style='display:<@s.property value="#datePickerMaxDisplay"/>'/>	      		
	    </td>
	</tr>
</table>	
	