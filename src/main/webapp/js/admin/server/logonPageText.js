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

Ext.define('com.trackplus.admin.server.LogonPageText',{
	extend:'Ext.Base',
//	config: {
//	},
	constructor: function(config) {
	},

	btnSave:null,
	panelForm:null,
	cmbLocale:null,
	//htmlEditor:null,


	createSaveButton:function(){
		var me=this;
		if(me.btnSave==null){
			me.btnSave=new Ext.Button({
				text:getText('common.btn.save'),
				tooltip:getText('common.btn.save'),
				iconCls: 'save',
				disabled:false,
				handler:function(){
					me.save.call(me);
				}
			});
		}
		return me.btnSave;
	},
	save:function(){
		var me=this;
		me.panelForm.setLoading(true);
		me.panelForm.getForm().submit({
			method :'POST',
			scope:me,
			success: function(form, action) {
				me.panelForm.setLoading(false);
				//me.reload(me.cmbLocale.getValue());
			},
			failure: function(form, action) {
				me.panelForm.setLoading(false);
				Ext.Msg.alert('Failure',action.result.errorMessage);
				var errorCode=action.result.errorCode;
				var errorMessage=action.result.errorMessage;
			}
		});
	},

	createMainComponent:function(){
		var me=this;
		me.cmbLocale=CWHF.createCombo("admin.server.motd.locale", "theLocale",
				{idType:"string", labelWidth:120, allowBlank:false},
				{select: function(field,record) {
					var theLocale=field.getValue();
					me.reload(theLocale);
				}});
		var txtEditorTeaser =CWHF.createTextField("admin.server.motd.teaserText", "teaserText",
				{anchor:'100%', labelWidth:120});
		var htmlEditor = CWHF.createHtmlEditorField("admin.server.motd.message", "theMessage",
				{padding: '20 0 0 0', anchor:'100% 80%', labelWidth:120});
		me.panelForm= new Ext.form.FormPanel({
			url:'logonPageText!save.action',
			fieldDefaults: {
				// labelWidth: 100
			},
			region:'center',
			border:false,
			autoScroll: true,
			bodyStyle: 'padding:10px',
			defaultType: 'textfield',
			items: [me.cmbLocale, txtEditorTeaser, htmlEditor]
		});
		return me.panelForm;
	},
	reload:function(theLocale){
		var me=this;
		var url="logonPageText.action";
		me.panelForm.setLoading(true);
		me.panelForm.getForm().load({
			url : url,
			params:{
				'theLocale':theLocale
			},
			success : function(form, action) {
				try{
					me.afterLoadForm.call(me,action.result.data);
					me.panelForm.setLoading(false);
				}catch(ex){}
			}
		});
	},
	afterLoadForm:function(data){
		var me=this;
		me.cmbLocale.store.loadData(data["localeList"],false);
		me.cmbLocale.setValue(data["theLocale"]);
	}
});



