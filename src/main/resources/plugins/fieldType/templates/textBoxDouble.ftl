<#assign s=JspTaglibs["/WEB-INF/tlds/struts2/struts-tags.tld"]>
{
	xtype: 'numberfield',
	fieldLabel: '${label}',
	name: '${nameMapping}',
	allowBlank:true
}
<#--
<#assign s=JspTaglibs["/WEB-INF/tlds/struts2/struts-tags.tld"]>
<@s.if test="invisible">
	&nbsp;
</@s.if>
<@s.else>	
   	<@s.if test='fieldsWithConversionError.contains("${nameMapping}")'>
   		<#-- if validation error occured leave the original value -->       		
  		<@s.textfield name="${nameMapping}" theme="simple" disabled="readonly"
       		title="${tooltip}"/>                	
    </@s.if> 
    <@s.else>
    	<#-- else render it according to doubleFormatted -->       		
    	<@s.textfield name="${nameMapping}" theme="simple" disabled="readonly"
       		value="${custom.doubleFormatted}" title="${tooltip}"/>                	
    </@s.else>    		
</@s.else>
-->