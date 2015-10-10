/**
 * Genji Scrum Tool and Issue Tracker
 * Copyright (C) 2015 Steinbeis GmbH & Co. KG Task Management Solutions

 * <a href="http://www.trackplus.com">Genji Scrum Tool</a>

 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

/* $Id:$ */


Ext.define('com.trackplus.admin.server.LdapController',{
	extend:'Ext.Base',
	config:{
		labelWidth:250,
		textFieldWidth:550,
		textFieldWidthShort:320,
		alignR:"right",
		FieldSetWidth:600,
		siteCfgController:null
	},
	constructor : function(config) {
		var me = this;
		var config = config || {};
		me.initialConfig = config;
		Ext.apply(me, config);
	},
	view:null,
	createView:function(){
		var me=this;
		me.view=Ext.create('com.trackplus.admin.server.LdapView',{
			labelWidth:me.labelWidth,
			textFieldWidth:me.textFieldWidth,
			textFieldWidthShort:me.textFieldWidthShort,
			alignR:me.alignR,
			FieldSetWidth:me.FieldSetWidth,
			controller:me
		});
		return me.view;
	},
	postDataProcess: function(data) {
		this.changeLDAPon();
	},
	/**
	 * Enable or disable input elements for LDAP panel
	 */
	changeLDAPon:function() {
		// Get the main enable/disable check box
		var ldap = CWHF.getWrappedControl.call(this.view,"fsLdap", "ldap.enabled");
		var ldapEnabled = ldap.getValue();

		// Get all components to disable or enable
		var serverURL = CWHF.getHelpWrapper.call(this.view, "fsLdap", "ldap.serverURL");
		serverURL.setDisabled(!ldapEnabled);

		var attributeLoginName = CWHF.getHelpWrapper.call(this.view, "fsLdap", "ldap.attributeLoginName");
		attributeLoginName.setDisabled(!ldapEnabled);

		var bindDN = CWHF.getHelpWrapper.call(this.view, "fsLdap", "ldap.bindDN");
		bindDN.setDisabled(!ldapEnabled);

		var password = CWHF.getControl.call(this.view, "fsLdap", "ldap.password");
		password.setDisabled(!ldapEnabled);

		var forceLdap = CWHF.getHelpWrapper.call(this.view, "fsLdap", "ldap.force");
		forceLdap.setDisabled(!ldapEnabled);

		var btnTest = CWHF.getControl.call(this.view,"fsLdapTest", "ldap.testConnection");
		var txtLoginNameTest = CWHF.getControl.call(this.view,"fsLdapTest", "ldap.loginNameTest");
		var txtPasswordTest = CWHF.getControl.call(this.view,"fsLdapTest", "ldap.passwordTest");
		btnTest.setDisabled(!ldapEnabled);
		txtLoginNameTest.setDisabled(!ldapEnabled);
		txtPasswordTest.setDisabled(!ldapEnabled);
	},
	testLdap:function(){
		var me=this;
		if(me.siteCfgController!=null){
			me.siteCfgController.testLdap.call(me.siteCfgController);
		}
	},
	clearErrorTabs:function(){
		var me=this;
		me.view.errorBox.setVisible(false);
	},
	failureHandler:function(form, action){
		var me=this;
		if(action.result!=null&&action.result.errors!=null){
			for(var i=0;i<action.result.errors.length;i++){
				if(action.result.errors[i].controlPath==null||action.result.errors[i].controlPath.length==0){
					me.view.errorBox.update(action.result.errors[i].errorMessage);
					me.view.errorBox.setVisible(true);
					break;
				}
			}
		}
	}
});

Ext.define('com.trackplus.admin.server.LdapView',{
	extend:'Ext.form.Panel',
	config:{
		labelWidth:250,
		textFieldWidth:550,
		textFieldWidthShort:320,
		alignR:"right",
		FieldSetWidth:600,
		controller:null
	},
	itemId:'tab.ldap',
	title:getText('admin.server.config.tabLDAP'),
	checkEmailTest:null,
	txtEmailTestTo:null,
	layout: {
		type: 'anchor'
	},
	initComponent: function(){
		var me=this;
		me.items=me.createChildren();
		me.callParent();
	},
	createChildren:function(){
		var me=this;
		var testBtn={xtype:'button',
			style:{marginTop:'10px', marginBottom: '5px', marginLeft: (me.labelWidth+5)+'px'},
			iconCls: 'check16',
			itemId: 'ldap.testConnection',
			text:getText('admin.server.config.ldapTest'),
			handler:function(){
				me.controller.testLdap.call(me.controller);
			}
		};
		var txtLoginNameText=CWHF.createTextField('admin.server.config.ldapLoginNameTest','ldap.loginNameTest',{disabled:true});
		var txtPasswordTest=CWHF.createTextField('admin.server.config.ldapPasswordTest','ldap.passwordTest',{inputType:'password'});
		me.errorBox=Ext.create('Ext.Component',{
			html: '',
			margin:'5 5 5 5',
			border:true,
			anchor:'100%',
			hidden:true,
			cls:'errBox2'
		});
		var testArea={
			xtype: 'fieldset',
			itemId: 'fsLdapTest',
			//title:getText('admin.server.config.trackEmailTest'),
			title: '',//if title not empty remove the margin and padding
			margin:'8 0 0 6',
			style:{
				paddingLeft:'13px',
				paddingTop:'18px'
			},

			width: me.FieldSetWidth,
			layout: 'anchor',
			items:[txtLoginNameText,txtPasswordTest,testBtn,me.errorBox]
		};
		return [{
			xtype: 'fieldset',
			itemId: 'fsLdap',
			width: me.FieldSetWidth,

			title: '',//if title not empty remove the margin and padding
			margin:'8 0 0 6',
			style:{
				paddingLeft:'13px',
				paddingTop:'18px'
			},

			collapsible: false,
			//defaults: {anchor: '100%'},
			layout: 'anchor',
			items: [CWHF.createCheckboxWithHelp('admin.server.config.isCbaAllowed',
						'otherSiteConfig.cbaAllowed'),
					CWHF.createCheckboxWithHelp('admin.server.config.automaticGuestLogin',
							'otherSiteConfig.automaticGuestLogin'),
					CWHF.createCheckboxWithHelp('admin.server.config.isLDAPOn',
							'ldap.enabled', {},
							{change:function(){
								me.controller.changeLDAPon.call(me.controller);
							}}),
					CWHF.createTextFieldWithHelp('admin.server.config.ldapServerURL',
							'ldap.serverURL'),
					CWHF.createTextFieldWithHelp('admin.server.config.ldapLoginName',
							'ldap.attributeLoginName'),
					CWHF.createTextFieldWithHelp('admin.server.config.ldapBindDn',
							'ldap.bindDN'),
					CWHF.createTextField('admin.server.config.ldapBindPass',
						'ldap.password',{inputType:'password'}),
					CWHF.createCheckboxWithHelp('admin.server.config.isForceLdap',
						'ldap.force')]
		},testArea
		];
	}
});
