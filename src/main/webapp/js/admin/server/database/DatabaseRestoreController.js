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

Ext.define('com.trackplus.admin.server.database.DatabaseRestoreController',{
	extend: "Ext.app.ViewController",
	alias: "controller.databaseRestore",

	onAfterRender:function(cmp,opt){
		this.reload();
	},

	reload:function(){
		var me=this;
		var me=this;
		var formPanel=this.lookupReference('formPanel');
		var gridAvailableBackups=this.lookupReference('gridAvailableBackups');
		formPanel.getForm().load({
			url : 'databaseRestore.action',
			success: function(form, action) {
				var jsonData=Ext.decode(action.response.responseText).data;
				gridAvailableBackups.setTitle(getText('admin.server.databaseBackup.lbl.availableBackups')+ ' ' + jsonData.backupDir);
				me.afterLoad.call(me,action.result.data);
			},
			failure: function(form, action) {
				Ext.MessageBox.alert(this.failureTitle, action.response.responseText);
			}
		});
	},
	afterLoad:function(data){
		var me=this;
		var gridAvailableBackups=this.lookupReference('gridAvailableBackups');
		gridAvailableBackups.store.loadData(data['availableBackups']);
	},
	onRestore:function(){
		var me=this;
		var formPanel=this.lookupReference('formPanel');
		if(!formPanel.getForm().isValid()){
			return false;
		}
		formPanel.setLoading(getText("common.lbl.loading"));
		formPanel.getForm().submit({
			success: function(form, action) {
				formPanel.setLoading(false);
				CWHF.showMsgInfo(getText('admin.server.databaseRestore.lbl.started'));
			},
			failure: function(form, action) {
				formPanel.setLoading(false);
				var errorMessage=action.result.errorMessage;
				if(CWHF.isNull(errorMessage)||errorMessage===''){
					errorMessage="";
					if(action.result.errors){
						for(var x in action.result.errors){
							errorMessage+=action.result.errors[x]+"</br>";
						}
					}
				}
				CWHF.showMsgError(errorMessage);
			}
		});
	},

	onDelete:function(){
		var me=this;
		var formPanel=this.lookupReference('formPanel');
		var gridAvailableBackups=this.lookupReference('gridAvailableBackups');
		if(!formPanel.getForm().isValid()){
			return false;
		}
		formPanel.setLoading(getText("common.lbl.loading"));
		formPanel.getForm().submit({
			url: 'databaseRestore!delete.action',
			success: function(form, action) {
				formPanel.setLoading(false);
				var availableBackups=Ext.decode(action.response.responseText).data;
				gridAvailableBackups.store.loadData(availableBackups);
			},
			failure: function(form, action) {
				formPanel.setLoading(false);
				var errorCode=action.result.errorCode;
				var errorMessage="";
				if(action.result.errors){
					for(var i=0;i<action.result.errors.length;i++){
						errorMessage+=action.result.errors[i].label+"</br>";
					}
				}
				CWHF.showMsgError(errorMessage);
			}
		});
	},

	chkIncludeAttachmentsChange:function(){
		var chkIncludeAttachments=this.lookupReference('chkIncludeAttachments');
		var txtAttachmentDir=this.lookupReference('txtAttachmentDir');
		var check=chkIncludeAttachments.getRawValue();
		txtAttachmentDir.allowBlank = !check;
		txtAttachmentDir.validate();
	},

	selectedChanged:function() {
		var gridAvailableBackups=this.lookupReference('gridAvailableBackups');
		var btnStart=this.lookupReference('btnStart');
		var btnDelete=this.lookupReference('btnDelete');
		var backupFile=this.lookupReference('backupFile');
		var record = gridAvailableBackups.selModel.getLastSelected();

		if (gridAvailableBackups.selModel.getCount() < 1) {
			btnStart.setDisabled(true);
			btnDelete.setDisabled(true);
		}
		else {
			backupFile.setValue(record.get('name'));
			btnStart.setDisabled(false);
			btnDelete.setDisabled(false);
		}
	},

	testConnection:function() {
		var me=this;
		var formPanel=this.lookupReference('formPanel');
		formPanel.setLoading(getText("common.lbl.loading"));
		formPanel.getForm().submit({
			url: 'databaseRestore!testConnection.action',
			success: function(form, response){
				formPanel.setLoading(false);
				CWHF.showMsgInfo(getText('admin.server.databaseRestore.lbl.connectSucceeded'));
			},
			failure:function(form, response){
				formPanel.setLoading(false);
				CWHF.showMsgError(getText('admin.server.databaseRestore.err.connectFailed'));
			}
		});
	}
});


