<#assign s=JspTaglibs["/WEB-INF/tlds/struts2/struts-tags.tld"]>
[<@s.iterator value="options" status="status">
    <@s.if test="#status.first"></@s.if><@s.else>,</@s.else>
    {id:'${id}', name: '${name}',changesetLink: '${changesetLink}',addedLink: '${addedLink}',modifiedLink: '${modifiedLink}',replacedLink: '${replacedLink}',deletedLink: '${deletedLink}'}
</@s.iterator>]