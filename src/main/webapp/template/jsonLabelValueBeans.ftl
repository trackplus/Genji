<#assign s=JspTaglibs["/WEB-INF/tlds/struts2/struts-tags.tld"]>
[<@s.iterator value="options" status="status">
    <@s.if test="#status.first"></@s.if><@s.else>,</@s.else>
    {value:'${value}', label: '${label}'}
</@s.iterator>]