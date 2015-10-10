<#assign s=JspTaglibs["/WEB-INF/tlds/struts2/struts-tags.tld"]>
<@s.if test="projectSpecific">
<table>	
	<tr>
	    <td class="dataRow4">
	    		<input type="button" value="<@s.text name='common.btn.renumber'/>" class="toolbutton"
				onclick="javascript:ajaxRequestWithSubmit('fieldConfigItemDetail!executeCommand.action', 'fieldDetailForm', true)"
	    </td>	    
	</tr>
</table>
</@s.if>	