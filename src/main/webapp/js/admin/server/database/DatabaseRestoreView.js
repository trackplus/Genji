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

Ext.define('com.trackplus.admin.server.database.DatabaseRestoreView',{
	extend:'Ext.panel.Panel',
	controller:'databaseRestore',
	layout:'border',
	border	: false,
	bodyBorder:false,
	margins: '0 0 0 0',

	alignR:"right",


	listeners : {
		scope: 'controller',
		afterrender:'onAfterRender'
	},

	initComponent : function() {
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
					return record.get('valid')===true;
				},
				beforedeselect:function(selModel, record, index) {
					return record.get('valid')=== true;
				}
			}
		});

		var gridAvailableBackups = Ext.create('Ext.grid.Panel', {
			store: store,
			bodyBorder:false,
			cls:'gridNoBorder',
			selModel: sm,
			reference:'gridAvailableBackups',
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
					if(record.data['valid']===false){
						cls="invalidBackupRow";
					}
					return cls;
				}
			},
			listeners: {
				scope: 'controller',
				selectionChange:'selectedChanged'
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
		var chkIncludeAttachments=CWHF.createCheckbox("admin.server.databaseRestore.lbl.includeAttachments",
			"includeAttachments", {checked:false,reference:'chkIncludeAttachments'},
			{
				scope: 'controller',
				change: 'chkIncludeAttachmentsChange'
			}
		);


		var testBtn={
			xtype:'button',
			style:{marginTop:'3px', marginBottom: '15px', marginLeft:'255px'},
			enableToggle:false,
			iconCls: 'check16',
			text:getText('admin.server.databaseRestore.button.testConnection'),
			handler:'testConnection'
		};

		var backupFile=Ext.create('Ext.form.field.Text',{
			name:'backupFile',
			reference:'backupFile',
			hidden: true
		});

		var backupDir=Ext.create('Ext.form.field.Text',{
			name:'backupDir',
			hidden: true
		});

		var jaction=Ext.create('Ext.form.field.Text',{
			name:'action',
			hidden: true
		});


		var txtAttachmentDir=CWHF.createTextField("admin.server.databaseRestore.lbl.attachmentRestoreDir",
			"attachmentDir", {width:675,reference:'txtAttachmentDir'});

		var formPanel=Ext.create('Ext.form.Panel', {
			layout:'anchor',
			region:'north',
			reference:'formPanel',
			url: 'databaseRestore!restore.action',
			border	: false,
			margins: '0 0 5 0',
			//bodyStyle: "padding: 5px 5px 10px 5px;",
			fieldDefaults: {
				labelWidth: 270,
				labelAlign: me.alignR
			},
			items:[mainHint,txtDriverClassName, txtUrl,txtUser, txtPassword, testBtn, chkSendNotifyEmail,
				chkIncludeAttachments,txtAttachmentDir, backupFile]
		});

		this.items=[formPanel,gridAvailableBackups];

		this.dockedItems = [{
			xtype: "toolbar",
			dock: 'top',
			items: [
				Ext.create('Ext.button.Button',{
					text:getText('admin.server.databaseRestore.button.restoreExecute'),
					overflowText:getText('admin.server.databaseRestore.button.restoreExecute'),
					tooltip:getText('admin.server.databaseRestore.button.restoreExecute'),
					iconCls: 'restore',
					disabled:true,
					reference:'btnStart',
					handler:'onRestore'
				}),
				Ext.create('Ext.button.Button',{
					text:getText('admin.server.databaseRestore.button.delete'),
					overflowText:getText('admin.server.databaseRestore.button.delete'),
					tooltip:getText('admin.server.databaseRestore.button.delete'),
					iconCls: 'delete',
					disabled:true,
					reference:'btnDelete',
					handler:'onDelete'
				})
			]
		}];
		this.callParent();
	}
});


