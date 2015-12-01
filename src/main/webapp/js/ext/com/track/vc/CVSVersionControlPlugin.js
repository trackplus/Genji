/**
 * Genji Scrum Tool and Issue Tracker
 * Copyright (C) 2015 Steinbeis GmbH & Co. KG Task Management Solutions

 * <a href="http://www.trackplus.com">Genji Scrum Tool</a>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

/* $Id:$ */

Ext.define("js.ext.com.track.vc.CVSVersionControlPlugin",{
	extend:'com.trackplus.vc.VersionControlPlugin',
	accessMethodOptions:[{id:'pserver',label:'pserver'},{id:'ext',label:'ext'}],
	authenticationOptions:[
		{id:'password',label:getText('plugins.versionControl.password')},
		{id:'publicPrivateKey',label:getText('plugins.versionControl.publicPrivateKey')}
	],
	createChildren:function(){
		var me=this;
		var items=me.callParent();
		me.fieldSetGeneral=me.createFieldSetGeneral();
		me.fieldSetConfiguration=me.createFieldSetConfiguration();
		items.push(me.fieldSetGeneral);
		items.push(me.fieldSetConfiguration);
		return items;
	},
	getControl:function(id){
		var me=this;
		switch(id){
			case 'plugins.versionControl.accessMethod':return me.rbtAccessMethod;
			case 'plugins.versionControl.userName':return me.txtUserName;
			case 'plugins.versionControl.serverName':return me.txtServerName;
			case 'plugins.versionControl.repositoryPath':return me.txtRepositoryPath;
			case 'plugins.versionControl.password':return me.txtPassword;
		}
		return null;
	},
	createFieldSetGeneral:function(){
		var me=this;
		var accessMethod="pserver";
		me.rbtAccessMethod=me.createRadioGroup('plugins.versionControl.accessMethod','vcmap.accessMethod',accessMethod,me.accessMethodOptions,me.changeAccessMethod,me);
		me.txtUserName=CWHF.createTextField('plugins.versionControl.userName','vcmap.userName');
		me.txtServerName=CWHF.createTextField('plugins.versionControl.serverName','vcmap.serverName');
		me.txtRepositoryPath=CWHF.createTextField('plugins.versionControl.repositoryPath','vcmap.repositoryPath');
		me.txtRepositoryPathWrapper=Ext.create('com.trackplus.util.HelpWrapperComponent',{
			inputComp:me.txtRepositoryPath,
			helpStr:getText('plugins.versionControl.repositoryPath.cvs.help')
		});
		var serverPort='default';
		var serverPortOptions=[
			{id:'default',label:getText('plugins.versionControl.defaultPort')},
			{id:'nonDefaultPort',label:getText('plugins.versionControl.nonDefaultPort')}
		];
		me.rbtServerPort=me.createRadioGroup('plugins.versionControl.serverPort','vcmap.serverPort',serverPort,serverPortOptions,me.changeServerPort,me);
		me.txtPort=CWHF.createNumberField(null,'vcmap.port');
		me.txtPort.width=100;
		me.rbtServerPort.width=me.rbtServerPort.width+100;
		me.rbtServerPort.add(me.txtPort);
		if(serverPort==='default'){
			me.txtPort.setDisabled(true);
		}else{
			me.txtPort.setDisabled(false);
		}
		return {
			xtype: 'fieldset',
			width:me.fieldSetWidth,
			title: getText('plugins.versionControl.generalOptions'),
			collapsible: false,
			defaultType: 'textfield',
			layout: 'anchor',
			items:[me.rbtAccessMethod,me.txtUserName,me.txtServerName,me.txtRepositoryPathWrapper,me.rbtServerPort]
		};
	},
	changeAccessMethod:function(){
	},
	changeServerPort:function(radioGroup,newValue, oldValue, options){
		var me=this;
		var checkedArr = radioGroup.getChecked();
		var checkedRadio;
		if (checkedArr.length===1) {
				checkedRadio = checkedArr[0];
				var value=checkedRadio.getSubmitValue();
			if(value==="default"){
				me.txtPort.setDisabled(true);
			}else{
				me.txtPort.setDisabled(false);
			}
		}
	},
	createFieldSetConfiguration:function(){
		var me=this;
		var authentication="password";
		me.rbtAuthentication=me.createRadioGroup('plugins.versionControl.authentication','vcmap.authentication',authentication,me.authenticationOptions,me.changeAuthentication,me);
		me.initPanelPpk();
		me.initTxtPassword();
		me.panelAccess=Ext.create('Ext.panel.Panel',{
			border:false,
			bodyBorder:false,
			bodyPadding:'0 0 5 0'
		});
		if(authentication==="password"){
			me.panelAccess.add(me.txtPassword);
		}else{
			me.panelAccess.add(me.panelPpk);
		}
		var sshType="sshAuto";
		var sshTypeOptions=[
			{id:'sshAuto',label:'Auto'},
			{id:'ssh1',label:'SSH1'},
			{id:'ssh2',label:'SSH2'}
		];
		me.rbtSshType=me.createRadioGroup('plugins.versionControl.sshType','vcmap.sshType',sshType,sshTypeOptions);

		var preferredPublicKey="sshAuto";
		var preferredPublicKeyOptions=[
			{id:'sshAuto',label:'Auto'},
			{id:'dsa',label:'DSA'},
			{id:'rsa',label:'RSA'}
		];
		me.rbtPreferredPublicKey=me.createRadioGroup('plugins.versionControl.preferredPublicKey','vcmap.preferredPublicKey',preferredPublicKey,preferredPublicKeyOptions);

		return Ext.create('Ext.form.FieldSet',{
			width:me.fieldSetWidth,
			title: getText('plugins.versionControl.configuration'),
			collapsible: false,
			defaultType: 'textfield',
			layout: 'anchor',
			items:[me.rbtAuthentication,me.panelAccess,me.rbtSshType,me.rbtPreferredPublicKey]
		});
	},
	initPanelPpk:function(){
		var me=this;
		//ppk
		me.txtPpk=CWHF.createTextAreaField('plugins.versionControl.privateKey','vcmap.privateKey');
		me.txtPpk.anchor='100%';
		me.txtPpk.height=200;
		me.txtPpkWrapper=Ext.create('com.trackplus.util.HelpWrapperComponent',{
			inputComp:me.txtPpk,
			helpStr:getText('plugins.versionControl.privateKey.help'),
			helpExpanded:false,
			flexInputComp:true
		});
		me.txtPassphrase=CWHF.createTextField('plugins.versionControl.passphrase','vcmap.passphrase');
		me.txtPassphrase.inputType='password';
		me.panelPpk=Ext.create('Ext.panel.Panel',{
			border:false,
			bodyBorder:false,
			layout:'anchor',
			items:[me.txtPpkWrapper, me.txtPassphrase]
		});
	},
	initTxtPassword:function(){
		var me=this;
		me.txtPassword=CWHF.createTextField('plugins.versionControl.password','vcmap.password',{width:550});
		me.txtPassword.inputType='password';
	},
	changeAuthentication:function(radioGroup, newValue, oldValue, options){
		var me=this;
		var checkedArr = radioGroup.getChecked();
		var checkedRadio;
		if (checkedArr.length===1) {
			checkedRadio = checkedArr[0];
			var value=checkedRadio.getSubmitValue();
			me.panelAccess.removeAll();
			var usingPassword = (value==='password');
			if(usingPassword){
				me.initTxtPassword();
				me.panelAccess.add(me.txtPassword);
			}else{
				me.initPanelPpk();
				me.panelAccess.add(me.panelPpk);
			}
			me.panelAccess.updateLayout();
		}
	},
	postProcessDataLoad:function(data){
		var me=this;
		me.callParent(arguments);
		var authentication=data['vcmap.authentication'];
		var usingPassword = (authentication==='password');
		if(usingPassword){
			me.txtPassword.setValue(data['vcmap.password']);
		}else{
			me.txtPassphrase.setValue(data['vcmap.passphrase']);
			me.txtPpk.setValue(data['vcmap.privateKey']);
		}
		me.panelAccess.updateLayout();
	}
});
