<#assign s=JspTaglibs["/WEB-INF/tlds/struts2/struts-tags.tld"]>
<h4>
    <@s.text name="%{getText('print.prompt.itemNumber')}"/>
    <strong>
	 <span class="emphasize">
	   &nbsp;<@s.property value="workItemID"/> 
	 </span>:
	<span class="dataEmphasize">
	  <@s.property value="statusDisplay"/>
	 </span>
	   :&nbsp;<@s.property value="params.synopsis"/>
	</strong>
</h4>
 <div class="containerRow1">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
   	  <tr>
	    <td width="40%" class="dataValue">
	    	<@s.checkbox name="params.deepCopy" theme="simple"/>
            <@s.text name="%{getText('item.action.copy.deepCopy')}"/>
      	</td>
      </tr>
      <tr>
	    <td width="40%" class="dataValue">
	    	<@s.checkbox name="params.copyAttachments" theme="simple"/>
            <@s.text name="%{getText('item.action.copy.copyAttachments')}"/>
     	</td>
      </tr>
      <tr>
	    <td width="40%" class="dataValue">
           <@s.if test="params.hasChildren">
            	<@s.checkbox name="params.copyChildren" theme="simple"/>
            </@s.if>
            <@s.else>
            	<input name="params.copyChildren" id="copyItem_copyChildren" type="checkbox" disabled="disabled"/>
            </@s.else>                        
            <@s.text name="%{getText('item.action.copy.copyChildren')}"/>     
        </td>
      </tr>
	</table>
</div>
