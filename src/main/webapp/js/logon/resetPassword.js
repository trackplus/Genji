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

Ext.define('com.trackplus.layout.ResetPasswordLayout',{
	extend:'com.trackplus.layout.BaseLayout',
	useToolbar:false,
	constructor : function(config) {
		var me = this;
		me.callParent(arguments);
		me.borderLayoutController.setHelpContext("logon");
	},
	applyPasswordVType: function() {
		// Add the additional 'advanced' VTypes
		Ext.apply(Ext.form.field.VTypes, {
			password: function(val, field) {
				if (field.initialPassField) {
					// var pwd = field.up('form').down('#' + field.initialPassField);
					var pwd = Ext.getCmp(field.initialPassField);
					return (val === pwd.getValue());
				}
				return true;
			},
			passwordText: getText('admin.user.profile.err.password.match')
		});
	},
	resetPassword:function(){
		var me=this;
		me.form.getForm().submit({
			url:'resetPassword!reset.action',
			params:{
				'ctk':me.initData.token
			},
			success:function(form,action){
				me.saveSuccess.call(me,form,action);
			},
			scope:me,
			failure:me.saveFailure
		});
	},

	//In case of changing password succeeded, the user will be logged in
	//automatically and redirected to default page.
	saveSuccess:function(form, action){
		var me=this;
		var data=null;
		if(action){
			data=action.result.data;
		}
		var title=getText("logon.resetPassword.title");
		var message=getText("logon.resetPassword.msg.resetSuccessfully");
		Ext.MessageBox.show({
			title: title,
			msg: message,
			width: 400,
			buttons: Ext.Msg.OK,
			fn: function(){
				var loginActionResponse = action.response.responseText;
				var loginActionResponseJSON  = Ext.JSON.decode(loginActionResponse);
				var JSONUrl = loginActionResponseJSON.data.jsonURL;
				var ftever = loginActionResponseJSON.data.ftever;
				var licURL = loginActionResponseJSON.data.licURL;
				var ftever=action.result.data.ftever;
				var licURL=action.result.data.licURL;
				if(JSONUrl  && JSONUrl !== "") {
					if (ftever) {
						Ext.MessageBox.show({
						    msg: getText('logon.getLicense'),
						    width: 450,
						    height: 300,
						    cls: 'licenseRequest',
						    closable:false,
						    buttonText: {ok: getText('logon.register')},
						    buttons: Ext.Msg.OK,
						    fn: function(buttonId) {
						    	window.open(licURL,'_blank');
						    	document.location=jsonURL;
						    },
						    icon: Ext.window.MessageBox.INFO
						});
					}
					document.location = JSONUrl;
				}else {
					window.location.href="logoff.action"
				}
			},
			icon: Ext.MessageBox.INFO
		});
	},
	saveFailure:function(form, action){
		var title=getText("logon.resetPassword.title");
		var message=getText("logon.resetPassword.error.cantResetPassword");
		Ext.MessageBox.show({
			title: title,
			msg: message,
			width: 400,
			buttons: Ext.Msg.OK,
			icon: Ext.MessageBox.ERROR
		});
	},
	createCenterPanel:function(){
		var me=this;
		me.applyPasswordVType();
		var btnReset=Ext.create('Ext.Button',{
			text: getText('common.btn.reset'),
			margin:'10 0 0 255',
			formBind: true,
			handler:me.resetPassword,
			scope:me
		});
		me.form=Ext.create('Ext.form.Panel', {
			layout: 'anchor',
			border: false,
			margin: '0 0 0 0',
			baseCls:'x-plain',
			unstyled: true,
			bodyStyle: {
				padding: '10px'
			},
			items: [CWHF.createTextField('admin.user.profile.lbl.passwd','passwd',
					{itemId:'passwd', inputType:'password',minLength:5, id:'passwd',allowBlank:false}),
				CWHF.createTextField('admin.user.profile.lbl.passwd2','passwd2',
					{itemId:'passwd2', inputType:'password',vtype:'password',initialPassField:'passwd',allowBlank:false}),
				btnReset
			]
		});
		var box=Ext.create('Ext.panel.Panel',{
			cls:'boxMessage boxMessage-ok',
			baseCls:'x-plain',
			unstyled: true,
			items:[
				{xtype:'component',html:getText('logon.resetPassword.msg',me.initData.loginName)},
				me.form]
		});
		return Ext.create('Ext.panel.Panel',{
			region: 'center',
			cls:'messagePageWrapper',
			border: false,
			baseCls:'x-plain',
			unstyled: true,
			items:[box]
		});
	}
});
