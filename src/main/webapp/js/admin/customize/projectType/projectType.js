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


Ext.define('com.trackplus.admin.customize.projectType.ProjectType',{
	extend:'com.trackplus.admin.TreeDetail',
	config: {
	},
	baseAction: 'projectType',
	leafDetailByFormLoad: true,
	folderDetailByFormLoad: true,
	replaceToolbarOnTreeNodeSelect: true,
	entityID:'node',
	//actions
	actionAdd: null,
	actionSave: null,
	actionDelete: null,
	actionImport: null,
	actionExport: null,

	fieldConfig: null,
	screenConfig: null,
	workflowConfig: null,
	issueTypeAssignment: null,
	childProjectTypeAssignment: null,
	roleAssignment: null,
	assignment: null,

	labelWidth:250,
	textFieldWidth:250+300,
	textFieldWidthShort:250+70,
	alignR:"right",
	FieldSetWidth:250+300+150,

	PROJECT_TYPE: 0,
	STATUS_ASSIGNMENT: 1,
	ISSUE_TYPE_ASSIGNMENT: 2,
	PRIORITY_ASSIGNMENT: 3,
	SEVERITY_ASSIGNMENT: 4,
	FIELD_CONFIGURATION: 5,
	SCREEN_ASSIGNMENT: 6,
	WORKFLOW_ASSIGNMENT: 7,
	CHILD_PROJECT_TYPE_ASSIGNMENT: 8,
	ROLE_ASSIGNMENT: 9,

	constructor: function(config) {
		var config = config || {};
		this.initConfig(config);
		this.myInit();
	},

	myInit: function() {
		this.initActions();
	},

	/********************Methods for projectType-associated entities*****************/

	/**
	 * Handler for selecting a node in the tree
	 */
	treeNodeSelect: function(rowModel, node, index, opts) {
		//this.centerPanel.removeAll(true);
		var noOfSelectedNodes = 0;
		var leaf = false;
		if (node) {
			//typical: called for the select event from the tree
			leaf = node.data['leaf'];
			this.selectedNodeID = node.data['id'];
			this.selectedNode = node;
		}
		var selectedNodeClass = this.getSelectedNodeClass(node);
		if (this.getSelectedNodeIsTreeBased(node)) {
			node = this.loadTreeWithGrid(selectedNodeClass, node);
			//call the actualizeToolbarOnTreeSelect as the rootNode of the new tree would be selected
			this.actualizeToolbarOnTreeSelect.call(selectedNodeClass, node);
		} else {
			if (this.getSelectedNodeIsDetailBased(node)) {
				if (this.centerPanel) {
					this.mainPanel.remove(this.centerPanel, true);
				}
				this.centerPanel = selectedNodeClass.getDetailPanel(node, leaf, opts);
				this.mainPanel.add(this.centerPanel);
				borderLayout.setActiveToolbarActionList([]);
			} else {
				selectedNodeClass.loadDetailPanel(node, leaf, opts);
				this.actualizeToolbarOnTreeSelect(node);
				//borderLayout.setActiveToolbarActionList(selectedNodeClass.getToolbarActions());
			}
		}
	},

	loadTreeWithGrid: function(treeWithGridConfig, node) {
		var projectTypeID = null;
	    if (node) {
	       projectTypeID = node.data["projectTypeID"];
	    }
		if (this.centerPanel) {
			this.mainPanel.remove(this.centerPanel, true);
		}
		//for workflow config
		treeWithGridConfig.projectOrProjectTypeID = projectTypeID;
		this.centerPanel = treeWithGridConfig.createCenterPanel();
		this.mainPanel.add(this.centerPanel);
		var rootNode = treeWithGridConfig.tree.getRootNode();
	    treeWithGridConfig.selectedNode = rootNode;
	    borderLayout.setActiveToolbarActionList(treeWithGridConfig.getToolbarActions());
		return rootNode;
	},

	/**
	 * Whether the node specific class is a treeBase implementation
	 */
	getSelectedNodeIsTreeBased: function(node) {
		var configType = node.data['configType'];
		switch (configType) {
		case this.STATUS_ASSIGNMENT:
		case this.PRIORITY_ASSIGNMENT:
		case this.SEVERITY_ASSIGNMENT:
		case this.FIELD_CONFIGURATION:
		case this.SCREEN_ASSIGNMENT:
		case this.WORKFLOW_ASSIGNMENT:
			return true;
		}
		return false;
	},

	/**
	 * Whether the node specific class is a wizard implementation
	 */
	getSelectedNodeIsDetailBased: function(node) {
		var projectConfigType = node.data['configType'];
		return projectConfigType===this.ISSUE_TYPE_ASSIGNMENT ||
			projectConfigType===this.CHILD_PROJECT_TYPE_ASSIGNMENT ||
			projectConfigType===this.ROLE_ASSIGNMENT;

	},

	/**
	 * Implement this method only if the tree contains heterogeneous data (like project/project type assignments)
	 * The specific methods on tree node selection should be called in the scope of the specific implementation
	 * Return the scope of the node specific implementation
	 * Additionally the locale tree and centerPanel objects are borrowed by the specific implementations
	 * (we do not initialize own tree for each specific implementation.
	 * An initialized (not null) tree is needed for the selection methods in tree in the context of a specific implementation
	 * and the centerPanel for rendering the data corresponding to the selected node
	 */
	getSelectedNodeClass: function(node) {
		var configType = node.data["configType"];
		var id = node.data["id"];
		var branchRoot = node.data["branchRoot"];
		var projectTypeID = node.data["projectTypeID"];
		if (CWHF.isNull(configType)) {
			return this;
		}
		switch (configType) {
		case this.PROJECT_TYPE:
			return this;
		case this.STATUS_ASSIGNMENT:
		case this.PRIORITY_ASSIGNMENT:
		case this.SEVERITY_ASSIGNMENT:
			if (CWHF.isNull(this.assignment)) {
				this.assignment = Ext.create("com.trackplus.admin.TreeDetailAssignment",
					{rootID:id,
					dynamicIcons:true, reloadGrids:true});
				this.assignment.baseAction = "projectTypeListAssignments";
			}
			this.assignment.setRootID(id);
			var rootMessageKey = null;
			if (configType===this.STATUS_ASSIGNMENT) {
				rootMessageKey = "admin.customize.projectType.lbl.assignmentInfoStatusGeneral";
			} else {
				if (configType===this.PRIORITY_ASSIGNMENT) {
					rootMessageKey = "admin.customize.projectType.lbl.assignmentInfoPriorityGeneral";
				} else {
					rootMessageKey = "admin.customize.projectType.lbl.assignmentInfoSeverityGeneral";
				}
			}
			this.assignment.setRootMessage(getText(rootMessageKey));
			return this.assignment;
		case this.ISSUE_TYPE_ASSIGNMENT:
			if (CWHF.isNull(this.issueTypeAssignment)) {
				this.issueTypeAssignment = Ext.create("com.trackplus.admin.SimpleAssignment",
					{assignmentType: this.ISSUE_TYPE_ASSIGNMENT,
					assignmentTypeParameterName: "configType",
					objectIDParamName: "projectTypeID",
					dynamicIcons:true});
				this.issueTypeAssignment.baseAction = "projectTypeSimpleAssignment";
			}
			this.issueTypeAssignment.setObjectID(node.parentNode.data['id']);
			return this.issueTypeAssignment;
		case this.CHILD_PROJECT_TYPE_ASSIGNMENT:
			if (CWHF.isNull(this.childProjectTypeAssignment)) {
				this.childProjectTypeAssignment = Ext.create("com.trackplus.admin.SimpleAssignment",
					{assignmentType: this.CHILD_PROJECT_TYPE_ASSIGNMENT,
					assignmentTypeParameterName: "configType",
					objectIDParamName: "projectTypeID"});
				this.childProjectTypeAssignment.baseAction = "projectTypeSimpleAssignment";
			}
			this.childProjectTypeAssignment.setObjectID(node.parentNode.data['id']);
			return this.childProjectTypeAssignment;
		case this.ROLE_ASSIGNMENT:
			if (CWHF.isNull(this.roleAssignment)) {
				this.roleAssignment = Ext.create("com.trackplus.admin.SimpleAssignment",
					{assignmentType: this.ROLE_ASSIGNMENT,
					assignmentTypeParameterName: "configType",
					objectIDParamName: "projectTypeID"});
				this.roleAssignment.baseAction = "projectTypeSimpleAssignment";
			}
			this.roleAssignment.setObjectID(node.parentNode.data['id']);
			return this.roleAssignment;
		case this.FIELD_CONFIGURATION:
			if (CWHF.isNull(this.fieldConfig)) {
				var branchRoot = node.data['branchRoot'];
				this.fieldConfig = Ext.create("com.trackplus.admin.customize.treeConfig.FieldConfig",
						{rootID:branchRoot});
			}
			return this.fieldConfig;
		case this.SCREEN_ASSIGNMENT:
			if (CWHF.isNull(this.screenConfig)) {
				var branchRoot = node.data['branchRoot'];
				this.screenConfig = Ext.create("com.trackplus.admin.customize.treeConfig.ScreenConfig",
						{rootID:branchRoot});
			}
			return this.screenConfig;
		case this.WORKFLOW_ASSIGNMENT:
			if (CWHF.isNull(this.workflowConfig)) {
				var branchRoot = node.data['branchRoot'];
				this.workflowConfig = Ext.create("com.trackplus.admin.customize.treeConfig.WorkflowConfig",
						{rootID:branchRoot, projectOrProjectTypeID:projectTypeID});
			}
			return this.workflowConfig;
		}
	},

	/**
	 * Gets the tree's fields: all fields for all possible config types
	 */
	getTreeFields: function() {
		return [{name : 'id', mapping : 'id', type: 'string'},
				{ name : 'text', mapping : 'text', type: 'string'},
				{ name : 'leaf', mapping : 'leaf', type: 'boolean'},
				{ name : 'iconCls', mapping : 'iconCls', type: 'string'},
				{ name : 'icon', mapping : 'icon', type: 'string'},
				{ name : 'configType', mapping : 'configType', type: 'int'},
				{ name : 'projectTypeID', mapping : 'projectTypeID', type: 'int'},
				{ name : 'branchRoot', mapping : 'branchRoot', type: 'string'}];
	},

	/******************Methods strictly for project type CRUD*********************/

	getWrappedControl: function() {
		return CWHF.getWrappedControl.apply(this.centerPanel, arguments);
	},

	getHelpWrapper: function() {
		return CWHF.getHelpWrapper.apply(this.centerPanel, arguments);
	},

	/**
	 * The localized entity name
	 */
	getEntityLabel: function(extraConfig) {
		return getText('common.lbl.projectType');
	},

	/**
	 * The iconCls for the save button
	 */
	getSaveIconCls: function() {
		return 'save';
	},

	initActions: function() {
		this.actionAdd = this.createLocalizedAction(this.getTitle(this.getAddTitleKey()),
				this.getAddIconCls(), this.onAdd, this.getTitle(this.getAddTitleKey()));
		this.actionSave = this.createLocalizedAction(getText('common.btn.save'),
				this.getSaveIconCls(), this.onSave, this.getTitle('common.lbl.save'), true);
		this.actionDelete = this.createLocalizedAction(getText('common.btn.delete'),
				this.getDeleteIconCls(), this.onDelete, this.getTitle('common.lbl.delete'), true);
		this.actionImport = this.createLocalizedAction(getText('common.btn.import'), 'import', this.onImport,
			getText("common.lbl.import", getText("menu.admin.custom.projectType")),false);
		this.actionExport = this.createLocalizedAction(getText('common.btn.export'), 'export', this.onExport,
			getText("common.lbl.export", getText("menu.admin.custom.projectType")),true);
	},

	/**
	 * Initialize all actions and return the toolbar actions
	 */
	getToolbarActions: function() {
		if (com.trackplus.TrackplusConfig.appType !== APPTYPE_BUGS) {
		    return [this.actionAdd, this.actionSave, this.actionDelete, this.actionImport,this.actionExport];
		} else {
			return [this.actionSave, this.actionExport];
		}
	},


	/**
	 * Prepare the form for adding a new projectType
	 */
	onAdd: function() {
		this.tree.getSelectionModel().deselectAll(true);
		var node = this.getSingleSelectedRecord(true);
		this.loadDetailPanelWithFormLoad(node, true);
		this.actionSave.setDisabled(false);
	},

	/**
	 * Save the detail part
	 */
	onSave: function() {
		this.centerPanel.getForm().submit({
			url: this.getBaseAction() + '!save.action',
			params: this.getSubmitParams(true),
			scope: this,
			success: function(form, action) {
				var result = action.result;
				if (result.success) {
					CWHF.showMsgInfo(getText('admin.customize.projectType.successSave'));
					//this.actionSave.setDisabled(true);
					this.reload(result);
				} else {
					CWHF.showMsgError(getText('admin.customize.projectType.errorSave'));
					errorHandlerSave(result);
				}
			},
			failure: function(form, action) {
				CWHF.showMsgError(getText('admin.customize.projectType.errorSave'));
				//com.trackplus.util.submitFailureHandler(form, action);
			}
		})
	},

	/**
	 * Extra parameters for editing a project type
	 */
	getSubmitParams: function(fromTree) {
		var record = this.getSingleSelectedRecord(fromTree);
		var recordData = null;
		var nodeID = null;
		if (record) {
			recordData = record.data;
			if (fromTree) {
				nodeID = recordData['id'];
			} else {
				nodeID = recordData['node'];
			}
		}
		//var params = this.getTypeParam(record, fromTree);
		var params = new Object();
		if (nodeID) {
			params['node']=nodeID;
		}
		return params;
	},

	onDelete: function() {
		var selectedRecords = this.getSelectedRecords(true);
		if (selectedRecords) {
			var extraConfig = {fromTree:true, isLeaf:false};
			this.deleteHandler(selectedRecords, extraConfig);
		}
	},

	onImport: function() {
		var submit = [{submitUrl:"projectType!importProjectTypes.action",
			submitButtonText:getText('common.btn.upload'),
			validateHandler: Upload.validateUpload,
			expectedFileType: /^.*\.(xml)$/,
			refreshAfterSubmitHandler:this.reload}];
		var title = getText('common.lbl.upload', getText('admin.customize.mailTemplate.lbl.uploadFile'));
		var windowParameters = {title:title,
			width:500,
			height:200,
			submit:submit,
			formPanel: this.getImportPanel(),
			cancelButtonText: getText('common.btn.done')};
		var windowConfig = Ext.create('com.trackplus.util.WindowConfig', windowParameters);
		windowConfig.showWindowByConfig(this);
	},

	onExport: function() {
		var selectedRecords = this.getSelectedRecords(true);
		if (selectedRecords) {
			var projectTypeID = selectedRecords.data['id'];
			var submit = [{
				submitHandler:this.doExport,
				submitButtonText:getText('common.btn.export')
			}];
			var title = getText("common.lbl.export", getText("menu.admin.custom.projectType"));
			var windowParameters = {
				title:title,
				width:500,
				height:200,
				submit:submit,
				formPanel: this.getExportPanel(),
				cancelButtonText: getText('common.btn.cancel')};
			var windowConfig = Ext.create('com.trackplus.util.WindowConfig', windowParameters);
			windowConfig.showWindowByConfig(this);


			return true;

		}
	},
	doExport:function(win, submitUrl, submitUrlParams, extraConfig){
		var selectedRecords = this.getSelectedRecords(true);
		var projectTypeID = selectedRecords.data['id'];
		var includeGlobal=this.checkIncludeGlobal.getInputComponent().getValue();
		win.close();
		var urlStr=this.getBaseAction() + '!export.action?projectTypeID='+projectTypeID+'&includeGlobal='+includeGlobal;
		window.open(urlStr);
	},

	getImportPanel:function() {
		var me=this;
		this.formPanel= new Ext.form.FormPanel({
			region:'center',
			border:false,
			bodyStyle: 'padding:5px',
			defaults: {
				labelStyle:'overflow: hidden;',
				margin:"5 5 0 0",
				msgTarget:	'under',
				anchor:	'-20'
			},
			method: 'POST',
			fileUpload: true,
			items: [
				CWHF.createCheckbox('common.lbl.overwriteExisting', 'overwriteExisting', {labelWidth:200}),
				CWHF.createFileField('common.lbl.attachment.file', 'uploadFile',
					{itemId:"uploadFile", allowBlank:false, labelWidth:200})]
		});
		return this.formPanel;
	},
	getExportPanel:function(){
		var me=this;
		var selectedRecords = this.getSelectedRecords(true);
		this.checkIncludeGlobal=CWHF.createCheckboxWithHelp('admin.customize.projectType.lbl.includeGlobal', 'includeGlobal', {labelWidth:200,width:250});

		this.formPanelExport= new Ext.form.FormPanel({
			region:'center',
			border:false,
			bodyStyle: 'padding:5px',
			defaults: {
				labelStyle:'overflow: hidden;',
				margin:"5 5 0 0",
				msgTarget:	'under',
				anchor:	'-20'
			},
			method: 'POST',
			items: [
				CWHF.createLabelComponent('common.lbl.projectType', null,{labelWidth:200,value:selectedRecords.data['text']}),
				this.checkIncludeGlobal
			]
		});
		return this.formPanelExport;
	},

	reload: com.trackplus.util.RefreshAfterSubmit.refreshSimpleTree,/*function(refreshParamsObject) {
		var nodeID = refreshParamsObject.node;
		var reloadTree = refreshParamsObject.reloadTree;
		if (reloadTree) {
			//tree reload is needed: after delete, add and edit with label change
			var treeStore = this.tree.getStore();
			var options = {};
			if (CWHF.isNull(nodeID) && treeStore.getRootNode().hasChildNodes()) {
				var firstNode = treeStore.getRootNode().getChildAt(0);
				if (firstNode) {
					var selectedNodeID = null;
					if (this.selectNode) {
						selectedNodeID = this.selectedNode.get("id");
					}
					nodeID = firstNode.get("id");
					if (nodeID===selectedNodeID) {
						var secondNode = treeStore.getRootNode().getChildAt(1);
						if (secondNode) {
							nodeID = secondNode.get("id");
						}
					}
				}
			}
			if (nodeID) {
				options.callback = this.selectNode;
				options.scope = {tree:this.tree, nodeIdToSelect:nodeID}
			} else {
				//no node to select disable save and delete
				this.actualizeToolbarOnTreeSelect();
			}
			treeStore.load(options);
		} else {
			//edit without label change
			this.selectNode.call({tree:this.tree, nodeIdToSelect:nodeID});
		}
	},*/

	getReloadParamsAfterDelete: function(selectedRecords, extraConfig, responseJson) {
		return {reloadTree:true}
	},



	/**
	 * Get the array of toolbar actions for a tree node select
	 * Implement only if replaceToolbarOnTreeNodeSelect is true
	 */
	getToolbarActionsForTreeNodeSelect: function(selectedNode) {
		if (selectedNode) {
			this.actionSave.setDisabled(false);
			var projectTypeID = selectedNode.data['projectTypeID'];
			if (projectTypeID && projectTypeID<0) {
				//do not delete the private project type
				this.actionDelete.setDisabled(true);
				this.actionExport.setDisabled(true);
			} else {
				this.actionDelete.setDisabled(false);
				this.actionExport.setDisabled(false);
			}
		} else {
			this.actionSave.setDisabled(true);
			this.actionDelete.setDisabled(true);
			this.actionExport.setDisabled(true);
		}
		if (com.trackplus.TrackplusConfig.appType !== APPTYPE_BUGS) {
			return [this.actionAdd, this.actionSave, this.actionDelete, this.actionImport, this.actionExport];
		} else {
			return [this.actionSave,this.actionExport];
		}
	},

	/**
	 * Gets the URL for loading the node detail
	 */
	getDetailUrl: function(node, add) {
		return this.getBaseAction() + '!edit.action';
	},

	/**
	 * Gets the URL for loading the node detail
	 */
	getDetailParams: function(node, add) {
		var params = new Object();
		if (node) {
			nodeID = node.data['id'];
			params['node']=nodeID;
		}
		params['add'] = add;
		return params;
	},

	/**
	 * Gets the project type items
	 */
	getDetailItems: function(node, add) {
		var projectTypeID = null;
		if (node) {
			projectTypeID = node.data['projectTypeID'];
		}
		var hideDetail = !add && projectTypeID && projectTypeID<0;
		var items = [CWHF.createTextField('common.lbl.name', 'projectTypeTO.label',
			{allowBlank:false, labelWidth:this.labelWidth, width:this.textFieldWidth})];
		if (!hideDetail) {
			items.push(CWHF.createComboWithHelp('admin.customize.projectType.lbl.projectTypeFlag',
					'projectTypeTO.projectTypeFlag', {itemId: 'projectTypeFlag', labelWidth:this.labelWidth}));
			items.push(CWHF.createCheckboxWithHelp('admin.customize.projectType.lbl.enableRelease', 'projectTypeTO.useReleases'));
			items.push(CWHF.createCheckboxWithHelp('admin.customize.projectType.lbl.enableVersionControl','projectTypeTO.useVersionControl'));
		}
		if (com.trackplus.TrackplusConfig.appType !== APPTYPE_BUGS) {
			items.push(CWHF.createCheckboxWithHelp('admin.customize.projectType.lbl.enableAccounting', 'projectTypeTO.useAccounts',
					null, {change: {fn: this.onAccountingChange, scope:this}}));
			items.push(CWHF.createNumberFieldWithHelp('common.lbl.hoursPerWorkday',
					'projectTypeTO.hoursPerWorkday', 2, 0, 24,
					{labelWidth:this.labelWidth, width:this.textFieldWidthShort, itemId:'projectTypeTOHoursPerWorkday'}));
			items.push(CWHF.createComboWithHelp('admin.project.lbl.defaultWorkUnit',
					'projectTypeTO.defaultWorkUnit', {itemId: 'defaultWorkUnit',labelWidth:this.labelWidth}));
			items.push(CWHF.createTextFieldWithHelp("admin.project.lbl.currencyName", "projectTypeTO.currencyName",
					{labelWidth:this.labelWidth, width:this.textFieldWidthShort,itemId:'projectTypeTOCurrencyName'}));
			items.push(CWHF.createTextFieldWithHelp("admin.project.lbl.currencySymbol", "projectTypeTO.currencySymbol",
					{labelWidth:this.labelWidth, width:this.textFieldWidthShort,itemId:'projectTypeTOCurrencySymbol'}));
			if (com.trackplus.TrackplusConfig.appType !== APPTYPE_DESK) {
				items.push(CWHF.createCheckboxWithHelp("admin.customize.projectType.lbl.enableMsProjectExportImport",
						"projectTypeTO.useMsProjectExportImport", {labelWidth:this.labelWidth, width:this.textFieldWidthShort}));
			}
		}
		return [//preserve the add flag through a hidden parameter
		        {xtype:'hidden',
				name: 'add',
				value: add
				},
				{xtype: 'fieldset',
				itemId: 'projectTypeFS',
				width: this.FieldSetWidth,
				style:{marginTop: '6px'},
				title: getText('admin.customize.projectType.fieldset'),
				items:items
				}];
		},

		onAccountingChange: function(field, newValue, oldValue, options) {
			var enableAccounting = field.getValue();
			this.enableAccountingFields(enableAccounting);
		},

		enableAccountingFields: function(enableAccounting) {
			this.getHelpWrapper("projectTypeFS", "projectTypeTOHoursPerWorkday").setDisabled(!enableAccounting);
			this.getHelpWrapper("projectTypeFS", "defaultWorkUnit").setDisabled(!enableAccounting);
			this.getHelpWrapper("projectTypeFS", "projectTypeTOCurrencyName").setDisabled(!enableAccounting);
			this.getHelpWrapper("projectTypeFS", "projectTypeTOCurrencySymbol").setDisabled(!enableAccounting);
		},

		/**
		 * The method to process the data to be loaded arrived from the server
		 */
		getEditPostDataProcess: function(record, isLeaf, add) {
			return this.postDataLoadCombo;
		},

		/**
		 * Load the combos after the result has arrived containing also the combo data sources
		 */
		postDataLoadCombo: function(data, panel) {
			var projectTypeFlag = this.getWrappedControl("projectTypeFS", "projectTypeFlag");
			if (projectTypeFlag) {
				projectTypeFlag.store.loadData(data['projectTypeFlagList']);
				projectTypeFlag.setValue(data['projectTypeTO.projectTypeFlag']);
			}
			var projectTypeFlag = this.getWrappedControl("projectTypeFS", "defaultWorkUnit");
			if (projectTypeFlag) {
				projectTypeFlag.store.loadData(data['workUnitList']);
				projectTypeFlag.setValue(data['projectTypeTO.defaultWorkUnit']);
			}
			//if (CWHF.isNull(data["forPrivateProjects"]) || data["forPrivateProjects"]===false) {
				this.enableAccountingFields(data["projectTypeTO.useAccounts"]);
			//}
		}
});
