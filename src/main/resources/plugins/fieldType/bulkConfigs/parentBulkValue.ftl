<#assign s=JspTaglibs["/WEB-INF/tlds/struts2/struts-tags.tld"]>
<!-- $Source$ -->		   
<table><tr>
	<td>
		<@s.textfield name="displayValueMap[0]" id="bulkField%{fieldID}" size="60" theme="simple"
	    	disabled="fieldDisabled" readonly="true" onchange="updateBulkValue(%{fieldID})"/>					
	</td>	
	<td>
		<a HREF="javascript:openChooseParent();" id="parentBtn"			 	
			class="<@s.if test='%{fieldDisabled}'>chooseParent</@s.if><@s.else>chooseParentDisabled</@s.else>"/>						
	</td>
</tr></table>