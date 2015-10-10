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

Ext.define('com.trackplus.admin.server.SiteConfig',{
	extend:'Ext.Base',
	constructor : function(config) {
		var me = this;
		var config = config || {};
		me.initialConfig = config;
		Ext.apply(me, config);
	},

	SECURITY_CONNECTIONS_MODES:{
		NEVER:0,
		TLS_IF_AVAILABLE:1,
		TLS:2,
		SSL :3
	},
	mainForm:null,
	btnSave:null,
	jsonData:null,
	mainComponent:null,
	labelWidth:250,
	textFieldWidth:250+300,
	textFieldWidthShort:250+70,
	alignR:"right",
	FieldSetWidth:250+300+150,



	/**
	 * Create a Save button
	 * @return {Ext.button.Button} The save button
	 */
	createSaveButton:function(){
		var me=this;
		if(me.btnSave==null){
			me.btnSave=new Ext.button.Button({
				overflowText:getText('common.btn.save'),
				tooltip:getText('common.btn.save'),
				text: getText('common.btn.save'),
				iconCls: 'save',
				disabled: true,
				handler:function(){
					me.save.call(me);
				}
			});
		}
		return me.btnSave;
	},

	/**
	 * Gets a control by the path according to the "arguments" starting form the main tab panel
	 */
	getControl: function() {
		return CWHF.getControl.apply(this.mainForm.getComponent(0), arguments);
	},

	getHelpWrapper: function() {
		return CWHF.getHelpWrapper.apply(this.mainForm.getComponent(0), arguments);
	},

	getWrappedControl: function() {
		return CWHF.getWrappedControl.apply(this.mainForm.getComponent(0), arguments);
	},


	/**
	 * Creates the outgoing e-mail server (SMTP) configuration tab
	 * @param {Object} jsonData The data required for this and other tabs
	 * @return {Ext.form.Panel} A panel for this tab
	 */
	createTabEmailOutgoing:function(){
		var me=this;
		me.emailOutgoingController=Ext.create('com.trackplus.admin.server.EmailOutgoingController',{
			labelWidth:me.labelWidth,
			textFieldWidth:me.textFieldWidth,
			textFieldWidthShort:me.textFieldWidthShort,
			alignR:me.alignR,
			FieldSetWidth:me.FieldSetWidth,
			siteCfgController:me
		});
		return me.emailOutgoingController.createView.call(me.emailOutgoingController);
	},

	
	/**
	 * Enable or disable input elements for full text search panel
	 */
	changeFtsOn:function() {
		var me=this;
		// Get the main enable/disable check box
		var fts = this.getWrappedControl("tab.fullTextSearch",
				"fsFullTextSearch", "fullTextSearch.useLucene");
		var ftsEnabled = fts.getValue();

		// Get all components to disable or enable
		var reindexOnStartup = this.getHelpWrapper("tab.fullTextSearch",
				"fsFullTextSearch", "fullTextSearch.reindexOnStartup");
		reindexOnStartup.setDisabled(!ftsEnabled);

		var indexAttachments = this.getHelpWrapper("tab.fullTextSearch",
				"fsFullTextSearch", "fullTextSearch.indexAttachments");
		indexAttachments.setDisabled(!ftsEnabled);

		var indexPath = this.getHelpWrapper("tab.otherSiteConfig",
				"fsDirectories", "fullTextSearch.indexPath");
		indexPath.setDisabled(!ftsEnabled);

		var analyzer = this.getHelpWrapper("tab.fullTextSearch",
				"fsFullTextSearch", "fullTextSearch.analyzer");
		analyzer.setDisabled(!ftsEnabled);
	},

	/**
	 * Creates the full text search configuration tab
	 * @param {Object} jsonData The data required for this and other tabs
	 * @return {Ext.form.Panel} A panel for this tab
	 */
	createTabTextSearch:function(){
		var me=this;
		var panel=new Ext.Panel({
			itemId:'tab.fullTextSearch',
			title:getText('admin.server.config.tabTextSearch'),
			layout: {
				type: 'anchor'
			},
			items: [{
				xtype: 'fieldset',
				itemId: 'fsFullTextSearch',
				width: me.FieldSetWidth,

				title: '',//if title not empty remove the margin and padding
				margin:'8 0 0 6',
				style:{
					paddingLeft:'13px',
					paddingTop:'18px'
				},

				collapsible: false,
				defaults: {anchor: '100%'},
				layout: 'anchor',
				items: [CWHF.createCheckboxWithHelp('admin.server.config.useLucene',
							'fullTextSearch.useLucene', {},
							{change:function(){
								me.changeFtsOn.call(me);
							}}),
						CWHF.createCheckboxWithHelp('admin.server.config.reindexOnStartup',
							'fullTextSearch.reindexOnStartup'),
						CWHF.createCheckboxWithHelp('admin.server.config.indexAttachments',
						'fullTextSearch.indexAttachments'),
						CWHF.createComboWithHelp('admin.server.config.analyzer',
								'fullTextSearch.analyzer',{idType:'string'})]
			}]
		});
		return panel;
	},

	/**
	 * Creates the LDAP and SSO configuration tab
	 * @param {Object} jsonData The data required for this and other tabs
	 * @return {Ext.form.Panel} A panel for this tab
	 */
	createTabLdap:function() {
		var me=this;
		me.ldapController=Ext.create('com.trackplus.admin.server.LdapController',{
			labelWidth:me.labelWidth,
			textFieldWidth:me.textFieldWidth,
			textFieldWidthShort:me.textFieldWidthShort,
			alignR:me.alignR,
			FieldSetWidth:me.FieldSetWidth,
			siteCfgController:me
		});
		return me.ldapController.createView.call(me.ldapController);
	},



	/**
	 * Creates the remaining items configuration tab
	 * @param {Object} jsonData The data required for this and other tabs
	 * @return {Ext.form.Panel} A panel for this tab
	 */
	createTabOther:function(){
		var me=this;
		var panel=new Ext.Panel({
			itemId:'tab.otherSiteConfig',
			title:getText('admin.server.config.tabOther'),
			layout: {
				type: 'anchor'
			},
			items: [{
				xtype: 'fieldset',
				itemId: 'fsDirectories',
				width: me.FieldSetWidth,
				title: getText('admin.server.config.tabOther.paths'),
				collapsible: false,
				defaultType: 'textfield',
				defaults: {anchor: '100%'},
				layout: 'anchor',
				items: [CWHF.createTextFieldWithHelp('admin.server.config.attachmentRootDir',
							'otherSiteConfig.attachmentRootDir'),
						CWHF.createTextFieldWithHelp('admin.server.config.backupDir',
								'otherSiteConfig.backupDir'),
						CWHF.createTextFieldWithHelp('admin.server.config.indexPath',
								'fullTextSearch.indexPath'),
						CWHF.createTextFieldWithHelp('admin.server.config.serverURL',
								'otherSiteConfig.serverURL')]
			}, {
				xtype: 'fieldset',
				width: me.FieldSetWidth,
				title: getText('admin.server.config.tabOther.limits'),
				collapsible: false,
				defaultType: 'textfield',
				defaults: {anchor: '100%'},
				layout: 'anchor',
				items: [CWHF.createNumberFieldWithHelp('admin.server.config.attachmentMaxSize',
							'otherSiteConfig.maxAttachmentSize', 2, 0, 9999, {hideTrigger:true})]
//						CWHF.createNumberFieldWithHelp('admin.server.config.descriptionLength',
//							'otherSiteConfig.descriptionLength', 0, 0, 9999, {hideTrigger:true})]
			},
			{
				xtype: 'fieldset',
				width: me.FieldSetWidth,
				title: getText('admin.server.config.tabOther.various'),
				collapsible: false,
				defaultType: 'textfield',
				defaults: {anchor: '100%'},
				layout: 'anchor',
				items: this.getVariousSettings()
			}]
		});
		return panel;
	},

	getVariousSettings:function() {
		var settingsItems = [CWHF.createCheckboxWithHelp('admin.server.config.isSelfRegisterAllowed', 'otherSiteConfig.selfRegisterAllowed'),
			//CWHF.createCheckboxWithHelp('admin.server.config.automaticGuestLogin', 'otherSiteConfig.automaticGuestLogin'),
			CWHF.createCheckboxWithHelp('admin.server.config.projectSpecificIDsOn', 'otherSiteConfig.projectSpecificIDsOn')];
		if (com.trackplus.TrackplusConfig.appType != APPTYPE_BUGS
				&& com.trackplus.TrackplusConfig.appType != APPTYPE_DESK) {
			//although forced to be set for Genji and Teamdesk (on server side), do not show the checkbox for them
			settingsItems.push(CWHF.createCheckboxWithHelp('admin.server.config.summaryItemsBehavior', 'otherSiteConfig.summaryItemsBehavior'));
			//not set for Genji and Teamdesk
			settingsItems.push(CWHF.createCheckboxWithHelp('admin.server.config.budgetActive', 'otherSiteConfig.budgetActive'));
			settingsItems.push(CWHF.createCheckboxWithHelp('admin.server.config.isDemoSite', 'otherSiteConfig.demoSite'));
		}
		settingsItems.push(CWHF.createCheckboxWithHelp('admin.server.config.isVersionReminderOn', 'otherSiteConfig.versionReminder'));
		settingsItems.push(CWHF.createCheckboxWithHelp('admin.server.config.isWSOn', 'otherSiteConfig.webserviceEnabled'));
		settingsItems.push(CWHF.createCheckboxWithHelp('admin.server.config.isDatabaseBackupJobOn', 'otherSiteConfig.automatedDatabaseBackup'));
		return settingsItems;
	},

	/**
	 * Create the main component here.
	 * @param {Object} jsonData The data required for this component
	 * @return {Ext.form.Panel} The site configuration form
	 */
	createMainComponent:function(){
		var me=this;
		var tabItems=new Array();
		tabItems.push(me.createTabEmailOutgoing());
		tabItems.push(me.createTabTextSearch());
		tabItems.push(me.createTabLdap());
		tabItems.push(me.createTabOther());
		me.tabPanel = Ext.create('Ext.tab.Panel',{
			plain:true,
			border:false,
			bodyBorder:false,
			defaults:{
				border:false,
				autoScroll:true,
				bodyStyle:{
					border:'none',
					padding:'0px'
				}
			},
			items:tabItems
		});
		var form=Ext.create('Ext.form.Panel', {
			url:'saveAdminSiteConfig!save.action',
			margins: '3 0 0 0',
			//standardSubmit:true,
			border: false,
			baseCls:'x-plain',
			layout:'fit',
			items:[me.tabPanel]
		});

		me.mainForm=form;
		//me.tabPanel.setActiveTab(0);
		return form;
	},
	loadMyForm:function(){
		var me=this;
		borderLayout.setLoading(true);
		me.mainForm.getForm().load({
			url : "editAdminSiteConfig!load.action",
			scope: this,
			success: function(form, action) {
				borderLayout.setLoading(false);
				try{
					//call postDataProcess only after window is rendered because
					//some fields (like labelEl) are available only after the window is rendered
					me.btnSave.setDisabled(false);
					if (me.postDataProcess!=null) {
						me.postDataProcess.call(this, action.result.data, me.formPanel, me.extraConfig);
					}
				}catch(ex){}
			},
			failure: function(form, action) {
				borderLayout.setLoading(false);
				Ext.MessageBox.alert(this.failureTitle, action.response.responseText);
			}
		});
	},

	postDataProcess: function(data, panel) {
		//license tab
		var me=this;

		//outgoingEmail tab
		me.emailOutgoingController.postDataProcess.call(me.emailOutgoingController,data);

		//incomingEmail tab

		//fullTextSearch tab
		var analyzer = this.getWrappedControl("tab.fullTextSearch", "fsFullTextSearch", "fullTextSearch.analyzer");
		analyzer.store.loadData(data["fullTextSearch.analyzers"]);
		analyzer.setValue(data["fullTextSearch.analyzer"]);
		this.changeFtsOn();

		//ldap tab
		me.ldapController.postDataProcess.call(me.ldapController,data);

	},
	clearErrorTabs:function(){
		var me=this;
		var tabBar=me.tabPanel.getTabBar();
		for(var i=0;i<tabBar.items.length;i++){
			var headerCm=tabBar.getComponent(i);
			headerCm.removeCls("errorTab");
		}
		if(me.ldapController!=null){
			me.ldapController.clearErrorTabs.call(me.ldapController);
		}
	},
	save:function(){
		var me=this;
		me.clearErrorTabs();
		me.emailOutgoingController.beforeSave.call(me.emailOutgoingController);
		if(!me.isValidForm()){
			this.handleInvalidForm();
			return false;
		}
		borderLayout.setLoading(true);
		me.mainForm.getForm().submit({
			scope:this,
			timeout:60000,// 60s
			success: function(form, action) {
				borderLayout.setLoading(false);

				CWHF.showMsgInfo(getText('admin.server.config.successSave'));
			},
			failure: function(form, action) {
				borderLayout.setLoading(false);

				me.handleErrors(action.result.errors);
			}
		});
	},

	handleErrors:function(errors){
		var me=this;
		var errStr='';
		var tabErrors=new Array();
		if (errors!=null && errors.length>0){
			for(var i=0;i<errors.length;i++){
				var error=errors[i];
				var controlPath = error.controlPath;
				var inputComp=null;
				if(controlPath!=null&&controlPath.length>0){
					inputComp=this.getControl.apply(this, controlPath);
					var tabId = controlPath[0];
					if(!Ext.Array.contains(tabErrors,tabId)){
						tabErrors.push(tabId);
					}
				}
				if(inputComp!=null){
					inputComp.markInvalid(error.errorMessage);
				}
				errStr+=error.label+"</br>";
			}
			me.markErrorTabs(tabErrors);
		}
		CWHF.showMsgError(getText('admin.server.config.errorSave'));
	},
	markErrorTabs:function(tabErrors){
		var me=this;
		var tabErrorsCmp=new Array();
		if(tabErrors.length>0){
			for(var i=0;i<tabErrors.length;i++){
				var tabComp=this.getControl(tabErrors[i]);
				if(tabComp!=null){
					tabErrorsCmp.push(tabComp);
				}
			}
			if(tabErrorsCmp.length>0){
				var selectedTab=me.tabPanel.getActiveTab();
				if(!Ext.Array.contains(tabErrorsCmp,selectedTab)){
					me.tabPanel.setActiveTab(tabErrorsCmp[0])
				}
				var tabBar=me.tabPanel.getTabBar();
				for(var i=0;i<tabErrorsCmp.length;i++){
					var index=me.tabPanel.items.findIndex('itemId', tabErrorsCmp[i].getItemId());
					var headerCm=tabBar.getComponent(index);
					headerCm.addCls("errorTab");
				}
			}
		}
	},

	testLdap:function(){
		var me=this;
		me.emailOutgoingController.beforeSave.call(me.emailOutgoingController);
		var urlStr='saveAdminSiteConfig!testLdap.action';
		me.test.call(me,urlStr,me.ldapController.failureHandler,me.ldapController);
	},

	isValidForm:function(){
		var me=this;
		return me.mainForm.getForm().isValid();
	},
	handleInvalidForm:function(){
		var me=this;
		CWHF.showMsgError(getText('admin.server.config.errorSave'));
		var invalidFields = this.mainForm.getForm().getFields().filterBy(function(field) {
			return !field.validate();
		});
		var tabErrors=new Array();
		for(var i=0;i<invalidFields.items.length;i++){
			var field=invalidFields.items[i];
			var id=field.getItemId();
			var prefix='tab.'+id.substring(0,id.indexOf('.'));
			if(!Ext.Array.contains(tabErrors,prefix)){
				tabErrors.push(prefix);
			}
		}
		this.markErrorTabs(tabErrors);
	},
	test:function(urlStr,failureHandler,failureScope){
		var me=this;
		me.clearErrorTabs();
		if(!me.isValidForm()){
			this.handleInvalidForm();
			return false;
		}
		borderLayout.setLoading(true);
		me.mainForm.getForm().submit({
			url:urlStr,
			timeout:70,//seconds
			success: function(form, action) {
				borderLayout.setLoading(false);
				CWHF.showMsgInfo(getText("admin.server.config.mailReceivingTestSuccess"));
			},
			failure: function(form, action) {
				borderLayout.setLoading(false);
				if(action.result!=null&&action.result.errors!=null){
					me.handleErrors(action.result.errors);
				}else{
					CWHF.showMsgError(getText("admin.server.config.mailReceivingTestFailure"));
				}
				if(failureHandler!=null){
					failureHandler.call(failureScope==null?me:failureScope,form, action);
				}

			}
		});
	}
});


