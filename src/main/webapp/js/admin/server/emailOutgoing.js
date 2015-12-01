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


Ext.define('com.trackplus.admin.server.EmailOutgoingController',{
	extend:'Ext.Base',

	portSMTP:25,
	portSMTP_SSL:465,
	SECURITY_CONNECTIONS_MODES:{
		NEVER:0,
		TLS_IF_AVAILABLE:1,
		TLS:2,
		SSL :3
	},
	SMTP_AUTHENTICATION_MODES:{
		CONNECT_USING_SMTP_SETTINGS:1,
		CONNECT_WITH_SAME_SETTINGS_AS_INCOMING_MAIL_SERVER: 2,
		CONNECT_TO_INCOMING_MAIL_SERVER_BEFORE_SENDING:3
	},

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
		this.initConfig(config);
	},
	view:null,
	createView:function(){
		var me=this;
		me.view=Ext.create('com.trackplus.admin.server.EmailOutgoingView',{
			labelWidth:me.labelWidth,
			textFieldWidth:me.textFieldWidth,
			textFieldWidthShort:me.textFieldWidthShort,
			alignR:me.alignR,
			FieldSetWidth:me.FieldSetWidth,
			emailController:me
		});
		return me.view;
	},
	postDataProcess: function(data) {
		var me=this;
		var userPasswordDisabled = !data["outgoingEmail.reqAuth"] || data["outgoingEmail.authMode"]!==1;
		var outgoingEmailUserControl = CWHF.getControl.call(this.view, "fsSmtpAuth", "outgoingEmailUser");
		outgoingEmailUserControl.setDisabled(userPasswordDisabled);
		var outgoingEmailPasswordControl = CWHF.getControl.call(this.view, "fsSmtpAuth",
				"passwPanel", "outgoingEmailPassword");
		outgoingEmailPasswordControl.setDisabled(userPasswordDisabled);

		var outgoingEmailSecurityConnectionModes=data["outgoingEmail.securityConnectionsModes"];
		var outgoingEmailSecurityConnection=data["outgoingEmail.securityConnection"];
		var outgoingEmailSecurityConnectionModesRadioButtons = CWHF.getRadioButtonItems(outgoingEmailSecurityConnectionModes,
				"outgoingEmail.securityConnection", "id", "label", outgoingEmailSecurityConnection, false, true);
		var outgoingEmailSecurityConnectionControl = CWHF.getWrappedControl.call(this.view, "fsSmtpServer", "outgoingEmailSecurityConnection");
		outgoingEmailSecurityConnectionControl.add(outgoingEmailSecurityConnectionModesRadioButtons);

		var authenticationModes = data["outgoingEmail.authenticationModes"];
		var authMode = data["outgoingEmail.authMode"];
		var authenticationModesRadioButtons = CWHF.getRadioButtonItems(authenticationModes,
				"outgoingEmail.authMode", "id", "label", authMode, !data["outgoingEmail.reqAuth"], true);
		 var authModeControl = CWHF.getWrappedControl.call(this.view, "fsSmtpAuth", "outgoingEmailAuthMode");
		 authModeControl.add(authenticationModesRadioButtons);


		var sendFromModes=data["outgoingEmail.sendFromModes"];
		var sendFromModeValue=data["outgoingEmail.sendFromMode"];
		var sendFromModesRadioButtons = CWHF.getRadioButtonItems(sendFromModes,
				"outgoingEmail.sendFromMode", "id", "label", sendFromModeValue, false, true);
		var sendFromModeControl = CWHF.getWrappedControl.call(this.view, "fsSmtpMore", "outgoingEmailSendFromMode");
		sendFromModeControl.add(sendFromModesRadioButtons);

		var mailEncodingControl = CWHF.getWrappedControl.call(this.view, "fsSmtpMore", "outgoingEmailMailEncoding");
			mailEncodingControl.setValue(data["outgoingEmail.mailEncoding"]);
	},


	changeSecurityMode:function(radioGroup, newValue, oldValue, options){
		var useSSL = CWHF.getSelectedRadioButtonValue(CWHF.getWrappedControl.call(this.view,
				"fsSmtpServer", "outgoingEmailSecurityConnection"));
		var portValue;
		if(useSSL===this.SECURITY_CONNECTIONS_MODES.SSL){
			portValue=this.portSMTP_SSL;
		}else{
			portValue=this.portSMTP;
		}
		var portControl=CWHF.getWrappedControl.call(this.view, "fsSmtpMore", "outgoingEmailPort");
		portControl.setValue(portValue);
	},

	changeRequireAuthentication:function() {
		var smtpReqAuth = CWHF.getWrappedControl.call(this.view, "fsSmtpAuth", "outgoingEmailReqAuth");
		var smtpReqAuthChecked = smtpReqAuth.getValue();
		var authModeWrapper=CWHF.getHelpWrapper.call(this.view, "fsSmtpAuth", "outgoingEmailAuthMode");
		authModeWrapper.setDisabled(!smtpReqAuthChecked);
		var authMode = authModeWrapper.getInputComponent();

		for(var i=0; i<3; i++){
			var item = authMode.getComponent(i);
			if (item) {
				item.setDisabled(!smtpReqAuthChecked);
			}
		}
		var clrbtn=CWHF.getControl.call(this.view, "fsSmtpAuth", "passwPanel", "ClearBtn");
		clrbtn.setDisabled(!smtpReqAuthChecked);
		if (smtpReqAuthChecked) {
			this.changeAuthenticationMode();
		} else {
			CWHF.getControl.call(this.view, "fsSmtpAuth", "outgoingEmailUser").setDisabled(true);
			CWHF.getControl.call(this.view, "fsSmtpAuth", "passwPanel", "outgoingEmailPassword").setDisabled(true);
		}
	},

	changeAuthenticationMode:function(radioGroup, newValue, oldValue, options){
		var authMode=CWHF.getWrappedControl.call(this.view, "fsSmtpAuth", "outgoingEmailAuthMode");
		var usingSMTPselected = CWHF.getSelectedRadioButtonValue(authMode)===1;
		CWHF.getControl.call(this.view, "fsSmtpAuth", "outgoingEmailUser").setDisabled(!usingSMTPselected);
		CWHF.getControl.call(this.view, "fsSmtpAuth", "passwPanel", "outgoingEmailPassword").setDisabled(!usingSMTPselected);
	},

	clearSMTPPassword:function() {
		var me=this;
		borderLayout.setLoading(true);
		Ext.Ajax.request({
			url: 'editAdminSiteConfig!clearSMTPPassword.action',
			success: function(response){
				borderLayout.setLoading(false);
				CWHF.showMsgInfo(getText('admin.server.config.smtpPasswordCleared'));
			},
			failure:function(response){
				borderLayout.setLoading(false);
				CWHF.showMsgError(getText('admin.server.config.err.smtpPasswordClearFailed'));
			}
		});
	},
	beforeSave:function(){
		var me=this;
		me.view.checkEmailTest.setValue(false);
	},
	changeEmailTest:function(){
		var me=this;
		var testEmail = me.view.checkEmailTest.getValue();
		me.view.txtEmailTestTo.setDisabled(!testEmail);
		var btnEnabled=testEmail&&me.view.txtEmailTestTo.isValid();
		Ext.apply(me.view.btnEmailTest,{formBind:testEmail},{});
		me.view.btnEmailTest.setDisabled(!btnEnabled);
		if(testEmail){//&&!me.view.txtEmailTestTo.isValid()
			me.view.txtEmailTestTo.focus(false,100);
		}
	},
	testOutgoingEmail:function(){
		var me=this;
		var urlStr='saveAdminSiteConfig!testOutgoingEmail.action';
		borderLayout.setLoading(true);
		if(me.siteCfgController){
			me.siteCfgController.test.call(me.siteCfgController,urlStr);
		}
	},
	onTxtEmailTestInputKeyPressed:function(field, e){
		var me=this;
		if (e.getKey() === e.ENTER&&me.view.txtEmailTestTo.isValid()) {
			me.testOutgoingEmail();
		}
	}
});

Ext.define('com.trackplus.admin.server.EmailOutgoingView',{
	extend:'Ext.form.Panel',
	config:{
		labelWidth:250,
		textFieldWidth:550,
		textFieldWidthShort:320,
		alignR:"right",
		FieldSetWidth:600,
		emailController:null
	},
	itemId:'tab_outgoingEmail',
	title:getText('admin.server.config.tabEmailOut'),
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
		var options=[{'id':'UTF-8','label':'Unicode UTF-8'},
				 {'id':'ISO-8859-1','label':'Western (ISO-8859-1)'},
				 {'id':'ISO-8859-2','label':'Central Europe (ISO-8859-2)'},
				 {'id':'ISO-8859-3','label':'Southern Europe (ISO-8859-3)'},
				 {'id':'ISO-8859-4','label':'Northern Europe (ISO-8859-4)'},
				 {'id':'ISO-8859-6','label':'Arabic (ISO-8859-6)'},
				 {'id':'ISO-8859-7','label':'Greek (ISO-8859-7)'},
				 {'id':'ISO-8859-8','label':'Hebrew (ISO-8859-8)'},
				 {'id':'ISO-8859-9','label':'Turkish (ISO-8859-9)'},
				 {'id':'ISO-8859-10','label':'Nordic (ISO-8859-10)'},
				 {'id':'ISO-8859-11','label':'Thai (ISO-8859-11)'},
				 {'id':'ISO-8859-13','label':'Baltic (ISO-8859-13)'},
				 {'id':'ISO-8859-14','label':'Celtic (ISO-8859-14)'},
				 {'id':'ISO-8859-16','label':'S.E. Europe (ISO-8859-16)'},
				 {'id':'KOI8-R','label':'Russian (KOI8-R)'},
				 {'id':'KOI8-U','label':'Ukraine (KOI8-U)'},
				 {'id':'Shift-JIS','label':'Japanese (Shift-JIS)'},
				 {'id':'ISO-2022-JP','label':'Japanese (ISO-2022-JP)'},
				 {'id':'GB18030','label':'Chinese (simplified)'},
				 {'id':'Big5','label':'Chinese (traditional)'},
				 {'id':'UTF-16','label':'Unicode UTF-16'}];
		var inputCompPwd=CWHF.createTextField('admin.server.config.smtpPassWord',
				'outgoingEmail.password', {inputType:'password', itemId:"outgoingEmailPassword"});

		var clearBtn={xtype:'button',
				style:{ marginBottom: '5px', marginLeft: '5px'},
				enableToggle:false,
				itemId:'ClearBtn',
				text:getText('common.btn.clear'),
				handler:function(){
					me.emailController.clearSMTPPassword.call(me.emailController);
				}
		};
		var inputCompPwdWrapper={
				xtype: 'panel',
				itemId: 'passwPanel',
				border:false,
				bodyBorder:false,
				layout: 'hbox',
				items: [inputCompPwd,clearBtn]
		};
		var testBtn={xtype:'button',
			style:{marginTop:'10px', marginBottom: '5px', marginLeft: (me.labelWidth+5)+'px'},
			enableToggle:false,
			iconCls: 'check16',
			text:'Test',
			handler:function(){
				me.testEmailOutgoing.call(me);
			}
		};

		return [{
			xtype: 'fieldset',
			itemId: 'fsTrackEmail',
			width: me.FieldSetWidth,
			title: getText('admin.server.config.smtpUser'),
			collapsible: false,
			defaults: {anchor: '100%'},
			layout: 'anchor',
			items: [CWHF.createTextFieldWithHelp('admin.server.config.trackEmailPersonalName',
					'outgoingEmail.emailPersonalName'),
					CWHF.createTextFieldWithHelp('admin.server.config.trackEmail',
							'outgoingEmail.trackEmail',{allowBlank:false,vtype: 'email'})]
		}, {
			xtype: 'fieldset',
			itemId: 'fsSmtpServer',
			width: me.FieldSetWidth,
			title: getText('admin.server.config.smtpServer'),
			collapsible: false,
			defaultType: 'textfield',
			defaults: {anchor: '100%'},
			layout: 'anchor',
			items: [CWHF.createTextFieldWithHelp('admin.server.config.smtpServerName',
						'outgoingEmail.serverName'),
					CWHF.getRadioGroupWithHelp('admin.server.config.smtpSecurityConnection',
						me.textFieldWidth, null, {itemId:"outgoingEmailSecurityConnection"},
						{change: function(radioGroup, newValue, oldValue, options) {
								me.emailController.changeSecurityMode.call(me.emailController,radioGroup, newValue, oldValue, options)}
						})
					]
		}, {
			xtype: 'fieldset',
			itemId: 'fsSmtpAuth',
			width: me.FieldSetWidth,
			title: getText('admin.server.config.smtpAuthentication'),
			collapsible: false,
			defaultType: 'textfield',
			layout: 'anchor',
			items: [CWHF.createCheckboxWithHelp('admin.server.config.smtpReqAuth',
					'outgoingEmail.reqAuth', {itemId:'outgoingEmailReqAuth',height:40}, {change:function(){
						me.emailController.changeRequireAuthentication.call(me.emailController);
					}}),
					CWHF.getRadioGroupWithHelp('admin.server.config.smtpAuthMode',
							me.textFieldWidth, null, {itemId:'outgoingEmailAuthMode',layout: 'vbox'},
							{change: function(radioGroup, newValue, oldValue, options) {
									me.emailController.changeAuthenticationMode.call(me.emailController,radioGroup, newValue, oldValue, options);
							}}),
					CWHF.createTextField('admin.server.config.smtpUser', 'outgoingEmail.user', {itemId:"outgoingEmailUser"}),
					inputCompPwdWrapper]
		}, {
			xtype: 'fieldset',
			itemId: 'fsSmtpMore',
			width: me.FieldSetWidth,
			title: getText('admin.server.config.smtpMoreOptions'),
			collapsible: true,
			collapsed: true,
			defaultType: 'textfield',
			layout: 'anchor',
			items: [CWHF.getRadioGroupWithHelp(me.textFieldWidth,null, [], {itemId:'outgoingEmailSendFromMode',layout: 'vbox'}),
					CWHF.createComboWithHelp('admin.server.config.mailEncoding',
							'outgoingEmail.mailEncoding',{itemId:'outgoingEmailMailEncoding', data:options, idType:'string'}),
					CWHF.createNumberFieldWithHelp('admin.server.config.smtpPort',
						'outgoingEmail.port', 0, 1, 9999, {width:me.textFieldWidth, hideTrigger: true,itemId:'outgoingEmailPort'})]
		},
		me.createTestPanel()
		];
	},
	createTestPanel:function(){
		var me=this;
		me.checkEmailTest=CWHF.createCheckbox('admin.server.config.trackEmailTest', 'outgoingEmail.emailTest',null,
			{
				change:function(){
					me.emailController.changeEmailTest.call(me.emailController);
				}
			}
		);
		me.txtEmailTestTo=CWHF.createTextField('admin.server.config.trackEmailTestTo','emailTestTo',{disabled:true,allowBlank:false,vtype: 'email'});
		me.txtEmailTestTo.addListener('specialkey',me.emailController.onTxtEmailTestInputKeyPressed,me.emailController);
		me.btnEmailTest=Ext.create('Ext.button.Button',{
			style:{marginTop:'10px', marginBottom: '5px', marginLeft: (me.labelWidth+5)+'px'},
			disabled:true,
			iconCls: 'check16',
			text: getText('admin.server.config.trackEmailTestButton'),
			handler:function(){
				me.emailController.testOutgoingEmail.call(me.emailController);
			}
		});
		var testArea={
			xtype: 'fieldset',
			itemId: 'fsSmtpTest',
			//title:getText('admin.server.config.trackEmailTest'),
			title: '',//if title not empty remove the margin and padding
			margin:'8 0 0 6',
			style:{
				paddingLeft:'13px',
				paddingTop:'18px'
			},

			width: me.FieldSetWidth,
			layout: 'anchor',
			items:[me.checkEmailTest,me.txtEmailTestTo,me.btnEmailTest]
		};
		return testArea;
	}
});
