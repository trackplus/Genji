<#assign s=JspTaglibs["/WEB-INF/tlds/struts2/struts-tags.tld"]>
<div class="containerRow1" width="100%">
	<table>
	  <TR>
			<td class="dataName">
				<@s.property value="projectLabel"/>:
			</td>
			<td class="dataName">
			<strong><I><@s.property value="params.oldProject"/></I></strong>
			-&gt;<strong><@s.property value="params.newProject"/></strong> 
			</td>
			<td class="dataName">
				<@s.property value="issueTypeLabel"/>:
			</td>
			<td class="dataName">
				<strong><I><@s.property value="params.oldIssueType"/></I></strong>
				-&gt;<strong><@s.property value="params.newIssueType"/> </strong>
			</td>
		 </TR>
	</table>
</div>
