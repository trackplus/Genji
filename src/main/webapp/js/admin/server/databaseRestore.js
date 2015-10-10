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

Ext.define('com.trackplus.admin.server.DatabaseRestore',{
	extend:'Ext.Base',
	constructor: function(config) {
	},

//static
ERROR_NEED_FILE_NAME:1,
//dateTimeIsoFormat:'Y-m-d H:i:s',
//end static

btnStart:null,
btnDelete:null,
formPanel:null,
chkIncludeAttachments:null,
txtAttachmentDir:null,
turl:null,
backupFile:null,
backupDir:null,
alignR:"right",

gridAvailableBackups:null,

createToolbar:function(){
	var me=this;
	if(me.btnStart==null){
		me.btnStart=new Ext.Button({
			text:getText('admin.server.databaseRestore.button.restoreExecute'),
			overflowText:getText('admin.server.databaseRestore.button.restoreExecute'),
			tooltip:getText('admin.server.databaseRestore.button.restoreExecute'),
			iconCls: 'restore',
			disabled:true,
			handler:function(){
				me.execute.call(me);
			}
		});
	}
	if(me.btnDelete==null){
		me.btnDelete=new Ext.Button({
			text:getText('admin.server.databaseRestore.button.delete'),
			overflowText:getText('admin.server.databaseRestore.button.delete'),
			tooltip:getText('admin.server.databaseRestore.button.delete'),
			iconCls: 'delete',
			disabled:true,
			handler:function(){
				me.exeDelete.call(me);
			}
		});
	}
	return [me.btnStart, me.btnDelete];
},

createMainComponent:function(){
	var me=this;
	var store = Ext.create('Ext.data.Store', {
		autoLoad: false,
		fields: [
			{name:'name', type:'string'},
			{name:'systemVersion', type:'string'},
			{name:'dbVersion', type:'string'},
			{name:'dbType', type:'string'},
			{name:'valid', type:'bool'},
			{name:'length', type:'int'},
			{name:'lastModified', type:'date', dateFormat: com.trackplus.TrackplusConfig.ISODateTimeFormat}
		],
		proxy: {
			type: 'ajax',
			url : 'databaseBackup!getAvailableBackups.action',
			reader: {
				type: 'json'
			}
		}
	});

	var sm = Ext.create('Ext.selection.CheckboxModel',{
		checkOnly:false,
		mode:'SINGLE',
		listeners:{
			beforeselect:function(selModel, record, index) {
				return record.get('valid')==true;
			},
			beforedeselect:function(selModel, record, index) {
				return record.get('valid')== true;
			}
		}
	});

	me.gridAvailableBackups = Ext.create('Ext.grid.Panel', {
		store: store,
		bodyBorder:false,
		cls:'gridNoBorder',
		selModel: sm,
		title:getText('admin.server.databaseBackup.lbl.availableBackups'),
		columns: [
			{text: getText('common.lbl.name'), width: 250, dataIndex: 'name', sortable: true},
			{text: getText('common.lbl.size'), width: 100, dataIndex: 'length', sortable: true,
				renderer:function(value){
					return Ext.util.Format.fileSize(value);
				}
			},
			{text: getText('common.lbl.date'), width: 125, dataIndex: 'lastModified', sortable: true,
				renderer:Ext.util.Format.dateRenderer(com.trackplus.TrackplusConfig.DateTimeFormat)
			},{text:getText('admin.server.config.systemVersion'),width: 115, dataIndex: 'systemVersion', sortable: true},
			{text:getText('admin.server.config.dbVersion'),width: 115, dataIndex: 'dbVersion', sortable: true},
			{text:getText('admin.server.databaseBackup.dbType'),width: 100, dataIndex: 'dbType', sortable: true}
		],
		margin:'0 0 0 0',
		region:'center',
		border:false,
		viewConfig: {
			stripeRows:true,
			getRowClass: function(record) {
				var cls="";
				if(record.data['valid']==false){
					cls="invalidBackupRow";
				}
				return cls;
			}
		},
		listeners: {
			selectionChange: function() {
				me.selectedChanged.call(me);
			}
		}
	});

	var txtDriverClassName=CWHF.createTextField("admin.server.databaseRestore.lbl.driverClassName",
			"driverClassName", {width:675, allowBlank:false});

	var mainHint = Ext.create('Ext.Component', {
		cls: 'infoBox_bottomBorder',
		border:true,
		html:getText('admin.server.databaseRestore.lbl.mainHint'),
		margin:'0 0 5 0'
	});

	var txtUrl=CWHF.createTextField("admin.server.databaseRestore.lbl.url",
			"turl", {width:675, allowBlank:false});
	var txtUser=CWHF.createTextField("admin.server.databaseRestore.lbl.user",
			"user", {width:415, allowBlank:false});
	var txtPassword=CWHF.createTextField("admin.server.databaseRestore.lbl.password",
			"password", {inputType: 'password', width:415, allowBlank:false});

	var chkSendNotifyEmail=CWHF.createCheckbox("admin.server.databaseRestore.lbl.sendNotifyEmail",
			"sendNotifyEmail", {checked:true});
	me.chkIncludeAttachments=CWHF.createCheckbox("admin.server.databaseRestore.lbl.includeAttachments",
		"includeAttachments", {checked:false},
		{
			change: function(radioGroup, newValue, oldValue, options) {
				me.chkIncludeAttachmentsChange.call(me);
			}
		}
	);


	var testBtn={xtype:'button',
		style:{marginTop:'3px', marginBottom: '15px', marginLeft:'255px'},
		enableToggle:false,
		iconCls: 'check16',
		text:getText('admin.server.databaseRestore.button.testConnection'),
		handler:function(){
			me.testConnection.call(me);
		}
	};

	me.backupFile=Ext.create('Ext.form.field.Text',{
		name:'backupFile',
		hidden: true
	});

	me.backupDir=Ext.create('Ext.form.field.Text',{
		name:'backupDir',
		hidden: true
	});

	me.jaction=Ext.create('Ext.form.field.Text',{
		name:'action',
		hidden: true
	});


	me.txtAttachmentDir=CWHF.createTextField("admin.server.databaseRestore.lbl.attachmentRestoreDir",
			"attachmentDir", {width:675});
		/*Ext.create('Ext.form.field.Text',{
		name:'attachmentDir',
		fieldLabel:getText('admin.server.databaseRestore.lbl.attachmentRestoreDir'),
		width:675
	});*/

	me.formPanel=Ext.create('Ext.form.Panel', {
		layout:'anchor',
		region:'north',
		url: 'databaseRestore!restore.action',
		border	: false,
		margins: '0 0 5 0',
		//bodyStyle: "padding: 5px 5px 10px 5px;",
		fieldDefaults: {
			labelWidth: 270,
			labelAlign: me.alignR
		},
		items:[mainHint,txtDriverClassName, txtUrl,txtUser, txtPassword, testBtn, chkSendNotifyEmail,
		me.chkIncludeAttachments,me.txtAttachmentDir, me.backupFile]
	});

	var panel=Ext.create('Ext.panel.Panel', {
		layout:'border',
		border	: false,
		bodyBorder:false,
		margins: '0 0 0 0',
		items:[me.formPanel,me.gridAvailableBackups]
	});
	return panel;
},

reload:function(){
	var me=this;
	me.formPanel.getForm().load({
		url : 'databaseRestore.action',
		success: function(form, action) {
			try{
				var jsonData=Ext.decode(action.response.responseText).data;
				me.gridAvailableBackups.setTitle(getText('admin.server.databaseBackup.lbl.availableBackups')
						+ ' ' + jsonData.backupDir);
				me.afterLoad.call(me,action.result.data);
			}catch(ex){}
		},
		failure: function(form, action) {
			Ext.MessageBox.alert(this.failureTitle, action.response.responseText);
		}
	});
},
afterLoad:function(data){
	var me=this;
	me.gridAvailableBackups.store.loadData(data['availableBackups']);
},
execute:function(){
	var me=this;

	if(!me.formPanel.getForm().isValid()){
		return false;
	}
	me.formPanel.setLoading(getText("common.lbl.loading"));
	me.formPanel.getForm().submit({
		success: function(form, action) {
			me.formPanel.setLoading(false);
			CWHF.showMsgInfo(getText('admin.server.databaseRestore.lbl.started'));
		},
		failure: function(form, action) {
			me.formPanel.setLoading(false);
			var errorMessage=action.result.errorMessage;
			if(errorMessage==null||errorMessage==''){
				errorMessage="";
				if(action.result.errors!=null){
					for(var x in action.result.errors){
						errorMessage+=action.result.errors[x]+"</br>";
					}
				}
			}
			CWHF.showMsgError(errorMessage);
		}
	});
},

exeDelete:function(){
	var me=this;

	if(!me.formPanel.getForm().isValid()){
		return false;
	}
	me.formPanel.setLoading(getText("common.lbl.loading"));
	me.formPanel.getForm().submit({
		url: 'databaseRestore!delete.action',
		success: function(form, action) {
			me.formPanel.setLoading(false);
			var availableBackups=Ext.decode(action.response.responseText).data;
			me.gridAvailableBackups.store.loadData(availableBackups);
		},
		failure: function(form, action) {
			me.formPanel.setLoading(false);
			var errorCode=action.result.errorCode;
			var errorMessage="";
			if(action.result.errors!=null){
				for(var i=0;i<action.result.errors.length;i++){
					errorMessage+=action.result.errors[i].label+"</br>";
				}
			}
			CWHF.showMsgError(errorMessage);
		}
	});
},

chkIncludeAttachmentsChange:function(){
	var me=this;
	var check=me.chkIncludeAttachments.getRawValue();
	me.txtAttachmentDir.allowBlank = !check;
	me.txtAttachmentDir.validate();
},

selectedChanged:function() {
	var me = this;
	var record = me.gridAvailableBackups.selModel.getLastSelected();
	if (me.gridAvailableBackups.selModel.getCount() < 1) {
		me.btnStart.setDisabled(true);
		me.btnDelete.setDisabled(true);
	}
	else {
		me.backupFile.setValue(record.get('name'));
		me.btnStart.setDisabled(false);
		me.btnDelete.setDisabled(false);
	}
},

testConnection:function() {
	var me=this;
	me.formPanel.setLoading(getText("common.lbl.loading"));
	me.formPanel.getForm().submit({
		url: 'databaseRestore!testConnection.action',
		success: function(form, response){
			me.formPanel.setLoading(false);
			CWHF.showMsgInfo(getText('admin.server.databaseRestore.lbl.connectSucceeded'));
		},
		failure:function(form, response){
			me.formPanel.setLoading(false);
			CWHF.showMsgError(getText('admin.server.databaseRestore.err.connectFailed'));
		}
	});
}
});


