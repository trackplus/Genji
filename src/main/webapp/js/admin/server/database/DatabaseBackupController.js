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

Ext.define('com.trackplus.admin.server.database.DatabaseBackupController',{
	extend: "Ext.app.ViewController",
	alias: "controller.databaseBackup",


	onAfterRender:function(cmp,opt){
		this.load();
	},


	/**
	 * load the data into the form
	 */
	load:function(){
		var me=this;
		var formPanel=this.lookupReference('formPanel');
		var ebox=this.lookupReference('errorBox');
		formPanel.getForm().load({
			url : 'databaseBackup.action',
			success: function(form, action) {
				me.afterLoad.call(me,action.result.data);
				ebox.setVisible(false);
			},
			failure: function(form, action) {
				me.afterLoad.call(me,action.result.data);
				var ebox = formPanel.getComponent("errorBox");
				ebox.update(action.result.msg);
				ebox.setVisible(true);
				Ext.MessageBox.show({
					title: '',
					msg: getText('admin.server.databaseBackup.err.problemStarting'),
					buttons: Ext.MessageBox.OK,
					icon:  Ext.MessageBox.ERROR
				});
			}
		});
	},

	/**
	 * Enable or disable input elements for incoming mail panel
	 */
	enableAutoBackup:function() {
		var me=this;
		// Get the main enable/disable check box
		var autoBackup=this.lookupReference('autoBackup');
		var autoBackupChkd = autoBackup.getValue();
		// Get all components to disable or enable

		var backupDir=this.lookupReference('backupDir');
		var includeAttachmentsConf=this.lookupReference('includeAttachmentsConf');
		var backupOnDays=this.lookupReference('backupOnDays');
		var backupTime=this.lookupReference('backupTime');
		var noOfBackups=this.lookupReference('noOfBackups');

		backupDir.setDisabled(!autoBackupChkd);
		includeAttachmentsConf.setDisabled(!autoBackupChkd);
		backupOnDays.setDisabled(!autoBackupChkd);
		backupTime.setDisabled(!autoBackupChkd);
		noOfBackups.setDisabled(!autoBackupChkd);
	},

	/**
	 * After the data has been delivered from the server...
	 * @param data
	 */
	afterLoad:function(data){
		var formPanel=this.lookupReference('formPanel');
		var gridAvailableBackups=this.lookupReference('gridAvailableBackups');
		var backupOnDays=this.lookupReference('backupOnDaysCmp');
		formPanel.setLoading(false);

		gridAvailableBackups.setTitle(getText('admin.server.databaseBackup.lbl.availableBackups')+ ' ' + data['backupDir']);
		gridAvailableBackups.store.loadData(data['availableBackups']);

		backupOnDays.store.loadData(data['backupOnDaysList']);
		backupOnDays.setValue(data['backupOnDays']);

		this.enableAutoBackup();
	},

	/**
	 * Save the configuration
	 */
	saveConfiguration:function(data) {
		var me=this;
		var formPanel=this.lookupReference('formPanel');
		var errorBox=this.lookupReference('errorBox');
		if(!formPanel.getForm().isValid()){
			return false;
		}
		formPanel.setLoading(getText("common.lbl.loading"));
		formPanel.getForm().url='databaseBackup!saveConfig.action';
		formPanel.getForm().submit({
			scope: me,
			success: function(form, action) {
				me.afterLoad.call(me,action.result.data);
				errorBox.setVisible(false);
				Ext.MessageBox.show({
					title: '',
					msg: getText('admin.server.databaseBackup.lbl.confSaved'),
					buttons: Ext.MessageBox.OK,
					icon:  Ext.MessageBox.INFO
				});
			},
			failure: function(form, action) {
				me.afterLoad.call(me,action.result.data);
				errorBox.update(action.result.msg);
				errorBox.setVisible(true);
				Ext.MessageBox.show({
					title: '',
					msg: getText('admin.server.databaseBackup.err.problemWithConf'),
					buttons: Ext.MessageBox.OK,
					icon:  Ext.MessageBox.ERROR
				});
			}
		});
	},

	/**
	 * Start the backup...
	 */
	onStartBackup:function(){
		var me=this;
		/*if(!me.formPanel.getForm().isValid()){
		 return false;
		 }*/
		var formPanel=this.lookupReference('formPanel');
		var errorBox=this.lookupReference('errorBox');
		formPanel.setLoading(getText("common.lbl.loading"));
		formPanel.getForm().submit({
			url:'databaseBackup!backup.action',
			clientValidation:false,
			success: function(form, action) {
				me.afterLoad.call(me,action.result.data);
				errorBox.update(action.result.msg);
				errorBox.setVisible(false);
				CWHF.showMsgInfo(getText('admin.server.databaseBackup.lbl.backupStarted'));
			},
			failure: function(form, action) {
				me.afterLoad.call(me,action.result.data);
				errorBox.update(action.result.msg);
				errorBox.setVisible(true);
				CWHF.showMsgInfo(getText('admin.server.databaseBackup.err.problemStarting'));
			}
		});
	}
});
