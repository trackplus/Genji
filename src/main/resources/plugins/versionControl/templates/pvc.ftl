<#assign s=JspTaglibs["/WEB-INF/tlds/struts2/struts-tags.tld"]>
<#assign app=JspTaglibs["/WEB-INF/tlds/app.tld"]>
<@s.set name="iconsPath"
    	value="'${request.getContextPath()}'+'/design/'+#session.userDesignPath+'/icons/'"/>

<@s.text name="generalOptions"/>
<hr>

<table width="100%" cellspacing="0" cellpadding="0">
      
    <tr class="dataRow1">
        <td class="dataName" style="white-space: nowrap; width: 180px;"><@s.text name="Login"/>:</td>
        <td class="dataValue" nowrap>
        	<@s.radio theme="simple"  list="{'anonym','User Name / Passwort'}" name="vcmap(Login)" value="vcmap.Login" onclick="changeAccessMethod()"/>         	
        </td>
    </tr>
    
    <tr class="dataRow1">
        <td class="dataName" style="white-space: nowrap; width: 180px;">
             <@s.text name="userName"/>:
        </td>
        <td class="dataValue">
            <@s.textfield theme="simple" name="vcmap(userName)" value="%{vcmap.userName}" cssStyle="width:300px;"/>
            
        </td>
    </tr>
    
    <tr class="dataRow1">
        <td class="dataName" style="white-space: nowrap; width: 180px;">
             <@s.text name="Passwort"/>:
        </td>
        <td class="dataValue">
            <@s.textfield theme="simple" name="vcmap(Passwort)" value="%{vcmap.Passwort}" cssStyle="width:300px;"/>
        </td>
    </tr>
    
    <tr class="dataRow1">
        <td class="dataName" style="white-space: nowrap; width: 180px;"><@s.text name=""/></td>
        <td class="dataValue" nowrap>
        	<@s.checkbox theme="simple"  name=""  value="true" onclick="changeAccessMethod()"/> Speichern des Passwortes
        </td>
    </tr>
    
    
     <tr class="dataRow1">
        <td class="dataName" style="white-space: nowrap; width: 180px;">
             <@s.text name="Server Addresse"/>:
        </td>
        <td class="dataValue">
            <@s.textfield theme="simple" name="vcmap(ServerAddresse)" value="%{vcmap.ServerAddresse}" cssStyle="width:300px;"/>
        </td>
    </tr>   

    
    
</table>

