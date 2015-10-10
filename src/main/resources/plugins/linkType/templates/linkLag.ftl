<#assign s=JspTaglibs["/WEB-INF/tlds/struts2/struts-tags.tld"]>
<table width="100%" cellpadding="0" cellspacing="0">
<tr>
<td style="width:100px;" class="dataName">  
<@s.text name="admin.actions.importMSProject.lbl.linklag"/>
</td>
<td colspan="2" class="dataValue">
<@s.select theme="simple"
        name="parametersMap[0]"
        multiple="false"
        list="parametersMap[1]" listKey="value"
        listValue="label">
</@s.select>
<@s.textfield name="parametersMap[2]" theme="simple" size="10"/>&nbsp;
<@s.select theme="simple"
        name="parametersMap[3]"
        multiple="false"
        list="parametersMap[4]" listKey="value"
        listValue="label">
</@s.select>
</td>
</tr>
</table>	

