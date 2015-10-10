<#assign s=JspTaglibs["/WEB-INF/tlds/struts2/struts-tags.tld"]>
<#assign app=JspTaglibs["/WEB-INF/tlds/app.tld"]>
<@s.set name="iconsPath"
    	value="'${request.getContextPath()}'+'/design/'+#session.userDesignPath+'/icons/'"/>

<@s.text name="versionControl.generalOptions"/>
<hr>

<table width="100%" cellspacing="0" cellpadding="0">
  <tr class="dataRow1">
        <td class="dataName" style="white-space: nowrap; width: 180px;"><@s.text name="versionControl.accessMethod"/>:</td>
        <td class="dataValue" nowrap>
        	<input type="radio" name="vcmap(accessMethod)" value="http" onclick="changeAccessMethod()"
                <@s.if test='(vcmap.accessMethod=="http")||(vcmap.accessMethod==null)'>
                    checked="checked"
                </@s.if> >
        	http
            <input type="radio" name="vcmap(accessMethod)" value="https" onclick="changeAccessMethod()"
                <@s.if test='vcmap.accessMethod=="https"'>
                    checked="checked"
                </@s.if> >
        	https
        	<input type="radio" name="vcmap(accessMethod)" value="svn" onclick="changeAccessMethod()"
                <@s.if test='vcmap.accessMethod=="svn"'>
                    checked="checked"
                </@s.if> >
        	svn
        	<input type="radio" name="vcmap(accessMethod)" value="svn+ssh" onclick="changeAccessMethod()"
                <@s.if test='vcmap.accessMethod=="svn+ssh"'>
                    checked="checked"
                </@s.if> >
        	svn+ssh
        </td>
    </tr>
    
    <tr class="dataRow1">
        <td class="dataName" style="white-space: nowrap; width: 180px;">
             <@s.text name="versionControl.userName"/>:
        </td>
        <td class="dataValue">
            <@s.textfield theme="simple" name="vcmap(userName)" value="%{vcmap.userName}" cssStyle="width:300px;"/>
        </td>
    </tr>
    <tr class="dataRow1">
        <td class="dataName" style="white-space: nowrap; width: 180px;">
             <@s.text name="versionControl.serverName"/>:
        </td>
        <td class="dataValue">
            <@s.textfield theme="simple" name="vcmap(serverName)" value="%{vcmap.serverName}" cssStyle="width:300px;"/>
        </td>
    </tr>
    <tr class="dataRow1">
        <td class="dataName" style="white-space: nowrap; width: 180px;">
             <@s.text name="versionControl.repositoryPath"/>:
        </td>
        <td class="dataValue">
            <@s.textfield theme="simple" name="vcmap(repositoryPath)" value="%{vcmap.repositoryPath}" cssStyle="width:300px;"/>
        </td>
    </tr>
    <tr class="dataRow1">
        <td class="dataName" style="white-space: nowrap; width: 180px;">
             <@s.text name="versionControl.serverPort"/>
        </td>
       <td class="dataValue">
            <input type="radio" name="vcmap(serverPort)" value="default" id="defaultPort"
                <@s.if test='(vcmap.serverPort=="default")||(vcmap.serverPort==null)'>
                    checked="checked"
                </@s.if> >
            <@s.text name="versionControl.defaultPort"/>
            &nbsp;
            <input type="radio" name="vcmap(serverPort)" value="nonDefault" id="nonDefaultPort"
                <@s.if test='vcmap.serverPort=="nonDefault"'>
                    checked="checked"
                </@s.if> >
            <@s.text name="versionControl.nonDefaultPort"/>:
            <@s.textfield theme="simple" name="vcmap(port)" value="%{vcmap.port}" 
            	cssStyle="width: 70px;" size="10" maxlength="10"/>
        </td>
    </tr>
</table>
<BR>
<BR>
<@s.text name="versionControl.configuration"/>
<hr>
<table width="100%" cellspacing="0" cellpadding="0">
    <tr class="dataRow1">
        <td class="dataName" style="white-space: nowrap; width: 180px;">
           <@s.text name="versionControl.authentication"/>:
        </td>
        <td class="dataValue" nowrap style="white-space: nowrap; width: 100px;">
            <input type="radio" name="vcmap(authentication)" value="anonymus" id="authAnonymus"
                <@s.if test='(vcmap.authentication=="anonymus")||(vcmap.authentication==null)'>
                    checked="checked"
                </@s.if> onclick="changeToAnonymusAccess()">
            <@s.text name="versionControl.anonymus"/>
         </td>
        <td class="dataValue" nowrap style="white-space: nowrap; width: 100px;">
            <input type="radio" name="vcmap(authentication)" value="password" id="authPassword"
                <@s.if test='(vcmap.authentication=="password")'>
                    checked="checked"
                </@s.if> onclick="changeToPasswordAccess()">
            <@s.text name="versionControl.password"/>
         </td>
         <td style="width: 10px;">
            <input type="radio" name="vcmap(authentication)" value="publicPrivateKey" id="publicPrivateKey"
                <@s.if test='vcmap.authentication=="publicPrivateKey"'>
                    checked="checked"
                </@s.if> onclick="changeToPpkAccess()">
         <td>    
            <span id="ppkLabel">
            	<@s.text name="versionControl.publicPrivateKey"/>
            </span>
         </td>
         <td>
         &nbsp;
         </td>
    </tr>
</table>
<table id="passwordAccessMethod" 
	<@s.if test='vcmap.authentication=="password"'>
		style="width:100%;"
	</@s.if>
	<@s.else>
		style="width:100%;display:none;"
	</@s.else> 
	>
     <tr class="dataRow1">
        <td class="dataName" style="white-space: nowrap; width: 180px;">
           <@s.text name="versionControl.password"/>:
        </td>
        <td class="dataName">
            <@s.password theme="simple" name="vcmap(password)" value="%{vcmap.password}" />
        </td>
     </tr>
    <tr>
         <td>
         &nbsp;
         </td>
    </tr>
</table>
<table id="ppkAccessMethod"
	<@s.if test='vcmap.authentication=="publicPrivateKey"'>
		style="width:100%;"
	</@s.if>
	<@s.else>
		style="width:100%;display:none;"
	</@s.else> 
	>
     <tr>
        <td class="dataName" style="white-space: nowrap; width: 180px;"  valign="top">
           <@s.text name="versionControl.privateKey"/>:
        </td>
        <td style="padding-right:5px;">
            <textarea name="vcmap(privateKey)" style="width:100%;" rows="10" cols="80"><@s.property value="%{vcmap.privateKey}"/></textarea>
        </td>
     </tr>
     <tr class="dataRow1">
         <td style="white-space: nowrap; width: 180px;">
           <@s.text name="versionControl.passphrase"/>:
        </td>
        <td>
            <@s.password theme="simple" name="vcmap(passphrase)" value="%{vcmap.passphrase}" />
        </td>
     </tr>
</table>
