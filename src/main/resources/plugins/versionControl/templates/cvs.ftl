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
        	<input type="radio" name="vcmap(accessMethod)" value="pserver" onclick="changeAccessMethod()"
                <@s.if test='(vcmap.accessMethod=="pserver")||(vcmap.accessMethod==null)'>
                    checked="checked"
                </@s.if> >
        	pserver
            <input type="radio" name="vcmap(accessMethod)" value="ext" onclick="changeAccessMethod()"
                <@s.if test='vcmap.accessMethod=="ext"'>
                    checked="checked"
                </@s.if> >
        	ext
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
            <input type="radio" name="vcmap(authentication)" value="password" id="authPassword"
                <@s.if test='(vcmap.authentication=="password")||(vcmap.authentication==null)'>
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
<table id="passwordAccessMethod" style="width:100%;">
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

<table width="100%" cellspacing="0" cellpadding="0">
     <tr class="dataRow1">
         <td class="dataName" style="white-space: nowrap; width: 180px;">
           <@s.text name="versionControl.sshType"/>:
        </td>
        <td class="dataValue">
        	<input type="radio" name="vcmap(sshType)" value="auto" id="sshAuto"
                <@s.if test='(vcmap.sshType=="auto")||(vcmap.sshType==null)'>
                    checked="checked"
                </@s.if> >
        	Auto
            <input type="radio" name="vcmap(sshType)" value="ssh1" id="ssh1"
                <@s.if test='vcmap.sshType=="ssh1"'>
                    checked="checked"
                </@s.if> >
        	SSH1
            <input type="radio" name="vcmap(sshType)" value="ssh2" id="ssh2"
                <@s.if test='vcmap.sshType=="ssh2"'>
                    checked="checked"
                </@s.if> >
        	SSH2
     </tr>
     <tr class="dataRow1">
        <td class="dataName" style="white-space: nowrap; width: 180px;">
           <@s.text name="versionControl.preferredPublicKey"/>:
        </td>
        <td class="dataValue">
        	<input type="radio" name="vcmap(prefferedPublicKey)" value="auto" id="ppkAuto"
                <@s.if test='(vcmap.prefferedPublicKey=="auto")||(vcmap.prefferedPublicKey==null)'>
                    checked="checked"
                </@s.if> >
        	Auto
            <input type="radio" name="vcmap(prefferedPublicKey)" value="dsa" id="dsa"
                <@s.if test='vcmap.prefferedPublicKey=="dsa"'>
                    checked="checked"
                </@s.if> >
        	DSA
            <input type="radio" name="vcmap(prefferedPublicKey)" value="rsa" id="rsa"
                <@s.if test='vcmap.prefferedPublicKey=="rsa"'>
                    checked="checked"
                </@s.if> >
        	RSA
     </tr>
</table>

