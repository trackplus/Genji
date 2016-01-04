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

Ext.define('com.trackplus.admin.server.database.DatabaseBackupView',{
	extend:'Ext.panel.Panel',
	controller:'databaseBackup',
	layout:'border',
	border	: false,
	bodyBorder:false,
	margins: '0 0 0 0',


//	static
	labelWidth:250,
	textFieldWidth:250+300,
	textFieldWidthShort:250+70,
	alignR:"right",
	FieldSetWidth:250+300+150,
	//dateTimeIsoFormat:'Y-m-d H:i:s',
//	end static


	listeners : {
		scope: 'controller',
		afterrender:'onAfterRender'
	},

	initComponent : function() {
		var gridAvailableBackups=this.createGridAvailableBackups();
		var formPanel=Ext.create('Ext.form.Panel', {
			region:'north',
			layout:'anchor',
			reference:'formPanel',
			url: 'databaseBackup!backup.action',
			border	: false,
			margins: '0 0 0 0',
			bodyStyle: "padding: 5px 5px 10px 5px;",
			fieldDefaults: {
				labelWidth: 270,
				labelAlign:this.alignR
			}
		});

		formPanel.add(this.createErrorBox());
		formPanel.add(this.createBackupFieldSet());
		formPanel.add(this.createBackupFieldSetConf());

		this.items=[formPanel,gridAvailableBackups];

		this.dockedItems = [{
			xtype: "toolbar",
			dock: 'top',
			items: [
				Ext.create('Ext.button.Button',{
					text:getText('admin.server.databaseBackup.button.Execute'),
					overflowText:getText('admin.server.databaseBackup.button.Execute'),
					tooltip:getText('admin.server.databaseBackup.button.Execute'),
					iconCls: 'backup',
					disabled:false,
					handler:'onStartBackup'
				})
			]
		}];
		this.callParent();
	},


	/**
	 * Create the grid with available backups
	 */
	createGridAvailableBackups:function(){
		var store = Ext.create('Ext.data.Store', {
			autoLoad: false,
			fields: [{name:'name', type:'string'},
				{name:'systemVersion', type:'string'},
				{name:'dbVersion', type:'string'},
				{name:'dbType', type:'string'},
				{name:'valid', type:'bool'},
				{name:'length', type:'int'},
				{name:'lastModified', type:'date', dateFormat: com.trackplus.TrackplusConfig.ISODateTimeFormat}],
			proxy: {
				type: 'ajax',
				url : 'databaseBackup!getAvailableBackups.action',
				reader: {
					type: 'json'
				}
			}
		});

		var gridAvBackups = Ext.create('Ext.grid.Panel', {
			store: store,
			bodyBorder:false,
			cls:'gridNoBorder',
			reference:'gridAvailableBackups',
			title:getText('admin.server.databaseBackup.lbl.availableBackups'),
			columns: [{text: getText('admin.server.databaseBackup.lbl.backupName'), width: 250, dataIndex: 'name', sortable: true},
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
			}
		});
		return gridAvBackups;
	},

	createErrorBox:function() {
		var errorBox = Ext.create('Ext.form.Panel', {
			reference: 'errorBox',
			bodyPadding: 10,
			margin: '0 0 15 0',
			cls: 'errBox',
			bodyCls: 'errBox',
			hidden: true
		});
		return errorBox;
	},

	/**
	 * Create the top field set for direct backup creation by user
	 */
	createBackupFieldSet:function(){
		var me=this;
		var txtName=CWHF.createTextField("admin.server.databaseBackup.lbl.backupName",
			"backupName", {itemId:'backupName', labelWidth:me.labelWidth});
		var chkSendNotifyEmail=CWHF.createCheckbox("admin.server.databaseBackup.lbl.sendNotifyEmail",
			"sendNotifyEmail", {labelWidth:me.labelWidth});

		var chkIncludeAttachments=CWHF.createCheckbox("admin.server.databaseBackup.lbl.includeAttachments",
			"includeAttachments", {labelWidth:me.labelWidth});

		var fieldSetBackup={
			xtype: 'fieldset',
			width: me.FieldSetWidth,
			title: getText('admin.server.databaseBackup.lbl.saveNow'),
			collapsible: false,
			defaultType: 'textfield',
			defaults: {anchor: '100%'},
			layout: 'anchor',
			items: [chkSendNotifyEmail,chkIncludeAttachments,txtName]
		};
		return fieldSetBackup;
	},

	/**
	 * Create the top field set for direct backup creation by user
	 */
	createBackupFieldSetConf:function(){
		var me=this;
		var autoBackup=CWHF.createCheckboxWithHelp('admin.server.config.isDatabaseBackupJobOn',
			'autoBackup',{reference:'autoBackup'},{
				scope: 'controller',
				change:'enableAutoBackup'
			}
		);
		var backupDir=CWHF.createTextField("admin.server.config.backupDir",
			"backupDir", {itemId:'backupDir',reference:'backupDir', labelWidth:me.labelWidth, width:515});

		var backupOnDays=CWHF.createComboWithHelp('admin.server.databaseBackup.lbl.backupDays',
			'backupOnDays',{itemId:'backupOnDays',reference:'backupOnDaysCmp', multiSelect :true},null,null,'backupOnDays');

		var noOfBackupsCmp=CWHF.createNumberFieldWithHelp('admin.server.databaseBackup.lbl.noOfBackups',
			'noOfBackups',0,1,99999,{width:me.labelWidth+30,hideTrigger:true,itemId:'noOfBackups',reference:'noOfBackupsCmp'},null,'noOfBackups');

		var backupTime=CWHF.createTimeFieldWithHelp('admin.server.databaseBackup.lbl.backupTime',
			'backupTime', {itemId:"backupTime",reference:'backupTimeCmp',altFormats:"H:i", allowBlank:false,increment:5},null,'backupTime');

		var chkIncludeAttachments=CWHF.createCheckbox("admin.server.databaseBackup.lbl.includeAttachments",
			"includeAttachmentsConf", {itemId:'includeAttachmentsConf', reference:'includeAttachmentsConf', labelWidth:me.labelWidth});

		var saveBtn={
			xtype:'button',
			style:{ marginBottom: '5px', marginLeft: '5px'},
			enableToggle:false,
			refenece:'saveBtn',
			width:null,
			text:getText('admin.server.databaseBackup.button.saveConfig'),
			handler:'saveConfiguration'
		};

		var btnWrapper={
			xtype: 'panel',
			border:false,
			bodyBorder:false,
			margin: '15 0 10 0',
			layout: 'hbox',
			items: [{ xtype: 'label',
				width:me.labelWidth,
				text: ''
			},
				saveBtn]
		};

		var fieldSetBackupConf={
			xtype: 'fieldset',
			itemId: 'fsauto',
			width: me.FieldSetWidth,
			title: getText('admin.server.databaseBackup.lbl.automatedBackupConf'),
			collapsible: true,
			defaultType: 'textfield',
			defaults: {anchor: '100%'},
			layout: 'anchor',
			collapsed: 'true',
			items: [autoBackup,
				backupDir,
				chkIncludeAttachments,
				backupOnDays,
				backupTime,
				noOfBackupsCmp,
				btnWrapper]
		};

		return fieldSetBackupConf;
	}
});
