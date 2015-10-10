<#assign s=JspTaglibs["/WEB-INF/tlds/struts2/struts-tags.tld"]>

<script type="text/javascript">
<!--
function userPickerSettingChanged(radioBoxBaseName) {
	var userPickerRadioBoxRole = document.getElementById(radioBoxBaseName+"1");
	var userPickerRadioBoxDepartment = document.getElementById(radioBoxBaseName+"2");
	var roles = document.getElementById("roles");
	var departments = document.getElementById("departments");
	if (userPickerRadioBoxRole.checked==true){
		roles.disabled=false;
		departments.disabled=true;
	} else { 
			if (userPickerRadioBoxDepartment.checked==true) {
				roles.disabled=true;
				departments.disabled=false;
			} else {
				roles.disabled=true;
				departments.disabled=true;
			}
	}	
}
//-->
</script>

<table>			
	<tr>
	    <td class="dataRow4">
	    	<@s.i18n name="resources.FieldTypeResources.FieldTypeResources">	    	
	      		<@s.text name ="customUserPicker.prompt.option"/>
	      	</@s.i18n>
	    </td>
	    <td class="dataRow4" colspan="4">
		    <@s.radio id="userPickerRadioBox" name="generalSettingsList[0].integerValue" theme="simple"    	 
		    	list="specificConfigData[0]" listKey="value" listValue="label"
		    	disabled="inheritedConfig" onchange="userPickerSettingChanged('userPickerRadioBox')"/>
		</td>
	</tr>	
	<tr>
	    <td class="dataRow4">
	    	<@s.i18n name="resources.FieldTypeResources.FieldTypeResources">	    	
	      		<@s.text name ="customUserPicker.prompt.roles"/>
	      	</@s.i18n>
	    </td>
	    <td class="dataRow4">
	    	<@s.select id="roles" name="multipleIntegerList[1]" theme="simple" 
				list="specificConfigData[1]" listKey="objectID" listValue="label" 
				multiple="true" size="5" disabled="inheritedConfig || generalSettingsList[0].integerValue!=1"/>
		</td>
		<td class="dataRow4">
			<@s.i18n name="resources.FieldTypeResources.FieldTypeResources">	    	
	      		<@s.text name ="customUserPicker.prompt.department"/>
	      	</@s.i18n>
	    </td>
	    <td class="dataRow4" >
	    	<@s.select id="departments" name="multipleIntegerList[2]" theme="simple" 
				list="specificConfigData[2]" listKey="objectID" listValue="label" 
				multiple="true" size="5" disabled="inheritedConfig || generalSettingsList[0].integerValue!=2"/>
		</td>		
	</tr>		
	<tr>
	    <td class="dataRow4">
	    	<@s.i18n name="resources.FieldTypeResources.FieldTypeResources">	    	
	      		<@s.text name ="customUserPicker.prompt.automail"/>
	      	</@s.i18n>
	    </td>
	    <td class="dataRow4" colspan="4">
	    	<@s.select name="generalSettingsList[3].integerValue" theme="simple" 
				list="specificConfigData[3]" listKey="value" listValue="label" disabled="inheritedConfig"/>
		</td>
	</tr>			
</table>