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

Ext.define("js.ext.com.track.vc.GITVersionControlPlugin",{
	extend:'com.trackplus.vc.VersionControlPlugin',
	authenticationOptions:[
		{id:'anonymus',label:getText('plugins.versionControl.anonymus')},
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
			case 'plugins.versionControl.repositoryPath':return me.txtRepositoryPath;
		}
		return null;
	},
	createFieldSetGeneral:function(){
		var me=this;
		me.txtRepositoryPath=CWHF.createTextField('plugins.versionControl.repositoryPath','vcmap.repositoryPath');
		me.txtRepositoryPathWrapper=Ext.create('com.trackplus.util.HelpWrapperComponent',{
			inputComp:me.txtRepositoryPath,
			helpStr:getText('plugins.versionControl.repositoryPath.git.help')
		});
		var fieldSetGeneral={
			xtype: 'fieldset',
			title: getText('plugins.versionControl.generalOptions'),
			collapsible: false,
			defaultType: 'textfield',
			layout: 'anchor',
			items:[me.txtRepositoryPathWrapper]
		};
		return fieldSetGeneral;
	},
	createFieldSetConfiguration:function(){
		var me=this;
		var authentication="anonymus";
		me.rbtAuthentication=me.createRadioGroup('plugins.versionControl.authentication','vcmap.authentication',authentication,me.authenticationOptions,me.changeAuthentication,me);
		me.initPanelPpk();
		me.initTxtPassword();
		me.panelAccess=Ext.create('Ext.panel.Panel',{
			border:false,
			bodyBorder:false,
			bodyPadding:'0 0 5 0'
		});
		if(authentication!=="anonymus"){
			if(authentication==="password"){
				me.panelAccess.add(me.txtPassword);
			}else{
				me.panelAccess.add(me.panelPpk);
			}
		}
		return Ext.create('Ext.form.FieldSet',{
			title: getText('plugins.versionControl.configuration'),
			collapsible: false,
			defaultType: 'textfield',
			layout: 'anchor',
			items:[me.rbtAuthentication,me.panelAccess]
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
			var usingAnonymus=(value==='anonymus');
			me.panelAccess.removeAll();
			if(!usingAnonymus){
				var usingPassword = (value==='password');
				if(usingPassword){
					me.initTxtPassword();
					me.panelAccess.add(me.txtPassword);
				}else{
					me.initPanelPpk();
					me.panelAccess.add(me.panelPpk);
				}
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
