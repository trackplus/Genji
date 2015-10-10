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

Ext.define('com.trackplus.admin.server.DatabaseBackup',{
	extend:'Ext.Base',
	constructor: function(config) {
	},

//	static
	ERROR_NEED_FILE_NAME:1,
	labelWidth:250,
	textFieldWidth:250+300,
	textFieldWidthShort:250+70,
	alignR:"right",
	FieldSetWidth:250+300+150,
	//dateTimeIsoFormat:'Y-m-d H:i:s',
//	end static

	btnStart:null,
	formPanel:null,
	backupDir:null,

	backupFieldSet:null,
	backupFieldSetConf:null,
	gridAvailableBackups:null,


	createToolbar:function(){
		var me=this;
		if(me.btnStart==null){
			me.btnStart=new Ext.Button({
				text:getText('admin.server.databaseBackup.button.Execute'),
				overflowText:getText('admin.server.databaseBackup.button.Execute'),
				tooltip:getText('admin.server.databaseBackup.button.Execute'),
				iconCls: 'backup',
				disabled:false,
				handler:function(){
					me.startBackup.call(me);
				}
			});
		}
		return [me.btnStart];
	},

	/**
	 * Gets a control by the path according to the "arguments" starting form the main tab panel
	 */
	getControl: function() {
		return CWHF.getControl.apply(this.formPanel, arguments);
	},

	getHelpWrapper: function() {
		return CWHF.getHelpWrapper.apply(this.formPanel, arguments);
	},

	getWrappedControl: function() {
		return CWHF.getWrappedControl.apply(this.formPanel, arguments);
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
					if(record.data['valid']==false){
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
			itemId: 'errorBox',
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
				"backupName", {labelWidth:me.labelWidth});
			/*Ext.create('Ext.form.field.Text',{
			fieldLabel:getText('admin.server.databaseBackup.lbl.backupName'),
			allowBlank:false,
			blankText :getText("common.err.required"),
			width:515,
			labelStyle:{overflow:'hidden'},
			labelWidth:me.labelWidth,
			labelAlign:me.alignR,
			name:'backupName'
		});*/

		var chkSendNotifyEmail=CWHF.createCheckbox("admin.server.databaseBackup.lbl.sendNotifyEmail",
				"sendNotifyEmail", {labelWidth:me.labelWidth});
		 /*Ext.create('Ext.form.field.Checkbox',{
			fieldLabel:getText('admin.server.databaseBackup.lbl.sendNotifyEmail'),
			checked:true,
			labelStyle:{overflow:'hidden'},
			labelWidth:me.labelWidth,
			labelAlign:me.alignR,
			inputValue:'true',
			name: 'sendNotifyEmail'
		});*/

		var chkIncludeAttachments=CWHF.createCheckbox("admin.server.databaseBackup.lbl.includeAttachments",
				"includeAttachments", {labelWidth:me.labelWidth});
			/*Ext.create('Ext.form.field.Checkbox',{
			fieldLabel:getText('admin.server.databaseBackup.lbl.includeAttachments'),
			checked:true,
			inputValue:'true',
			labelStyle:{overflow:'hidden'},
			labelWidth:me.labelWidth,
			labelAlign:me.alignR,
			name: 'includeAttachments'
		});*/

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
				'autoBackup',{listeners:{
						change:function(){
							me.enableAutoBackup.call(me);
						}
					}
				}
			);
		var backupDir=CWHF.createTextField("admin.server.config.backupDir",
				"backupDir", {labelWidth:me.labelWidth, width:515});
			/*Ext.create('Ext.form.field.Text',{
			fieldLabel:getText('admin.server.config.backupDir'),
			name:'backupDir',
			itemId:'backupDir',
			width:515,
			labelStyle:{overflow:'hidden'},
			labelWidth:me.labelWidth,
			labelAlign:me.alignR
		});*/

		var backupOnDays=CWHF.createComboWithHelp('admin.server.databaseBackup.lbl.backupDays',
				'backupOnDays',{multiSelect :true},null,'backupOnDays');

		var noOfBackupsCmp=CWHF.createNumberFieldWithHelp('admin.server.databaseBackup.lbl.noOfBackups',
				'noOfBackups',0,1,99999,{width:me.labelWidth+30,hideTrigger:true});

		var backupTime=CWHF.createTimeFieldWithHelp('admin.server.databaseBackup.lbl.backupTime',
				'backupTime', {altFormats:"H:i", allowBlank:false,increment:5});

		var chkIncludeAttachments=CWHF.createCheckbox("admin.server.databaseBackup.lbl.includeAttachments",
				"includeAttachmentsConf", {labelWidth:me.labelWidth});
			/*Ext.create('Ext.form.field.Checkbox',{
			fieldLabel:getText('admin.server.databaseBackup.lbl.includeAttachments'),
			checked:true,
			inputValue:'true',
			name: 'includeAttachmentsConf',
			itemId:'includeAttachmentsConf',
			labelStyle:{overflow:'hidden'},
			labelWidth:me.labelWidth,
			labelAlign:me.alignR
		});*/

		var saveBtn={xtype:'button',
				style:{ marginBottom: '5px', marginLeft: '5px'},
				enableToggle:false,
				id:'SaveBtn',
				width:null,
				text:getText('admin.server.databaseBackup.button.saveConfig'),
				handler:function(){
					me.saveConfiguration.call(me);
				}
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
	},

	/**
	 * Enable or disable input elements for incoming mail panel
	 */
	enableAutoBackup:function() {
		var me=this;
		var fsauto = me.formPanel.getComponent('fsauto');

		// Get the main enable/disable check box
		autoBackup = this.getWrappedControl("fsauto", "autoBackup");
		var autoBackupChkd = autoBackup.getValue();

		// Get all components to disable or enable
		this.getControl("fsauto", "backupDir").setDisabled(!autoBackupChkd);
		this.getControl("fsauto", "includeAttachmentsConf").setDisabled(!autoBackupChkd);
		this.getHelpWrapper("fsauto", "backupOnDays").setDisabled(!autoBackupChkd);
		this.getHelpWrapper("fsauto", "backupTime").setDisabled(!autoBackupChkd);
		this.getHelpWrapper("fsauto", "noOfBackups").setDisabled(!autoBackupChkd);
	},

	/**
	 * Create the main component
	 *
	 */
	createMainComponent:function(){
		var me=this;

		me.gridAvailableBackups=me.createGridAvailableBackups();
		me.formPanel=Ext.create('Ext.form.Panel', {
			region:'north',
			layout:'anchor',
			url: 'databaseBackup!backup.action',
			border	: false,
			margins: '0 0 0 0',
			bodyStyle: "padding: 5px 5px 10px 5px;",
			fieldDefaults: {
				labelWidth: 270,
				labelAlign:me.alignR
			}
		});

		me.formPanel.add(me.createErrorBox());
		me.formPanel.add(me.createBackupFieldSet());
		me.formPanel.add(me.createBackupFieldSetConf());

		var panel=Ext.create('Ext.panel.Panel', {
			layout:'border',
			border	: false,
			bodyBorder:false,
			margins: '0 0 0 0',
			items:[me.formPanel,me.gridAvailableBackups]
		});
		me.mainPanel = panel;
		me.load();
		return panel;
	},

	/**
	 * load the data into the form
	 */
	load:function(){
		var me=this;

		me.formPanel.getForm().load({
			url : 'databaseBackup.action',
			success: function(form, action) {
				try{
					me.afterLoad.call(me,action.result.data);
					var ebox = me.formPanel.getComponent("errorBox");
					ebox.setVisible(false);
				}catch(ex){}
			},
			failure: function(form, action) {
				me.afterLoad.call(me,action.result.data);
				var ebox = me.formPanel.getComponent("errorBox");
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
	 * After the data has been delivered from the server...
	 * @param data
	 */
	afterLoad:function(data){
		var me=this;
		me.formPanel.setLoading(false);

		me.gridAvailableBackups.setTitle(getText('admin.server.databaseBackup.lbl.availableBackups')
				+ ' ' + data['backupDir']);

		me.enableAutoBackup();

		var bod = this.getWrappedControl("fsauto", "backupOnDays");
		bod.store.loadData(data['backupOnDaysList']);
		bod.setValue(data['backupOnDays']);
		me.gridAvailableBackups.store.loadData(data['availableBackups']);
	},

	/**
	 * Save the configuration
	 */
	saveConfiguration:function(data) {
		var me=this;
		if(!me.formPanel.getForm().isValid()){
			return false;
		}
		me.formPanel.setLoading(getText("common.lbl.loading"));
		me.formPanel.getForm().url='databaseBackup!saveConfig.action';
		me.formPanel.getForm().submit({
			scope: me,
			success: function(form, action) {
				me.afterLoad.call(me,action.result.data);
				var ebox = me.formPanel.getComponent("errorBox");
				ebox.setVisible(false);
				Ext.MessageBox.show({
					title: '',
					msg: getText('admin.server.databaseBackup.lbl.confSaved'),
					buttons: Ext.MessageBox.OK,
					icon:  Ext.MessageBox.INFO
				});
			},
			failure: function(form, action) {
				me.afterLoad.call(me,action.result.data);
				var ebox = me.formPanel.getComponent("errorBox");
				ebox.update(action.result.msg);
				ebox.setVisible(true);
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
	startBackup:function(){
		var me=this;
		/*if(!me.formPanel.getForm().isValid()){
			return false;
		}*/
		me.formPanel.setLoading(getText("common.lbl.loading"));
		me.formPanel.getForm().submit({
			url:'databaseBackup!backup.action',
			clientValidation:false,
			success: function(form, action) {
				me.afterLoad.call(me,action.result.data);
				var ebox = me.formPanel.getComponent("errorBox");
				ebox.update(action.result.msg);
				ebox.setVisible(false);

				CWHF.showMsgInfo(getText('admin.server.databaseBackup.lbl.backupStarted'));
			},
			failure: function(form, action) {
				me.afterLoad.call(me,action.result.data);
				var ebox = me.formPanel.getComponent("errorBox");
				ebox.update(action.result.msg);
				ebox.setVisible(true);

				CWHF.showMsgInfo(getText('admin.server.databaseBackup.err.problemStarting'));
			}
		});
	}
});


