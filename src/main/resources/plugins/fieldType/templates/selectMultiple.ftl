<#assign s=JspTaglibs["/WEB-INF/tlds/struts2/struts-tags.tld"]>
<@s.if test="invisible">
	&nbsp;
</@s.if>
<@s.else>
    <@s.select theme="simple"
        name="${nameMapping}"
        multiple="true"
        list="custom.list" listKey="${custom.listKey}"
        listValue="${custom.listValue}"
        title="${tooltip}"
        disabled='readonly'
        size="8">
    </@s.select>
</@s.else>
