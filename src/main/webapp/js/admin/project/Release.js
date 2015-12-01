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

/**
 * Class for role and account assignments for project
 */
com.trackplus.admin.project.Release=Ext.define('com.trackplus.admin.project.Release',{
	extend:'com.trackplus.admin.TreeWithGrid',
	config: {
		/**
		 * The ID of the selected project, used only by localization
		 */
		projectID: null
	},
	statics:{
		createEditPanelItems:function(lw){
			return [CWHF.createTextField('common.lbl.name',
				'releaseDetailTO.label', {allowBlank:false, labelWidth:lw, maxLength:255}),
				CWHF.createCombo('admin.customize.localeEditor.type.releaseStatus',
					'releaseDetailTO.releaseStatusID', {itemId:"releaseStatusID", labelWidth:lw}),
				CWHF.createDateField('admin.project.release.lbl.dueDate', 'releaseDetailTO.dueDate', {labelWidth:lw}),
				CWHF.createCheckboxWithHelp('admin.project.release.lbl.defaultReleaseNoticed', 'releaseDetailTO.defaultNoticed', {labelWidth:lw, width:lw+50}),
				CWHF.createCheckboxWithHelp('admin.project.release.lbl.defaultReleaseScheduled', 'releaseDetailTO.defaultScheduled',{labelWidth:lw, width:lw+50}),
				CWHF.createTextAreaField('common.lbl.description', 'releaseDetailTO.description', {maxLength:255, labelWidth:lw})];
		},
		/**
		 * Load the combos after the result has arrived containing also the combo data sources
		 */
		postDataLoadCombo: function(data, panel) {
			var statusCombo = panel.getComponent('releaseStatusID');
			if (statusCombo) {
				//no statusCombo for phase
				statusCombo.store.loadData(data['statusList']);
				statusCombo.setValue(data['releaseDetailTO.statusID']);
			}
		}
	},
	baseAction: 'release',

	dragAndDropOnGrid: true,
	dragAndDropOnTree: true,
	/**
	 * The tree, set in projectConfig.js
	 */
	tree: null,
	folderEditWidth:500,
	folderEditHeight:400,
	labelWidth: 120,

	localizedMain: null,
	localizedChild: null,
	showClosedReleases: true,

	//by adding a release whether to add a main release or a child release
	addAsChild: false,
	//actions
	actionAddMainRelease: null,
	actionAddChildRelease: null,
	actionEditGridRow: null,
	actionEditTreeNode: null,
	actionDeleteGridRow: null,
	actionDeleteTreeNode: null,
	actionDetachFromParentRelease: null,
	actionMoveUp: null,
	actionMoveDown: null,
	actionShowClosed: null,
	actionReload: null,

	constructor: function(config) {
		var config = config || {};
		this.initConfig(config);
		this.initBase(config);
	},

	/**
	 * Initialization method
	 */
	initBase: function(config) {
		this.initLocalizedLabels(config.projectID);
	},

	initLocalizedLabels: function(projectID) {
		Ext.Ajax.request({
			fromCenterPanel:true,
			url: this.getBaseAction() + '!localizedLabels.action',
			scope: this,
			params: {node:projectID},
			success: function(response) {
				var result = Ext.decode(response.responseText);
				this.localizedMain = result.localizedMain;
				this.localizedChild = result.localizedChild;
				this.showClosedReleases = result.showClosedReleases;
				this.initActions();
			},
			failure: function(response) {
				Ext.MessageBox.alert(this.failureTitle, response.responseText);
			}
		});
	},

	/**
	 * to show the release grid with all main releases
	 * even if no release is selected to allow changing the order of the main releases
	 */
	postInitCenterPanel: function(rootNode) {
		this.getGridPanel(rootNode);
	},

	/**
	 * The localized entity name
	 */
	getEntityLabel: function(extraConfig) {
		var entityLabel = null;
		if (extraConfig) {
			var selectedRecord = extraConfig.selectedRecord;
			var fromTree = extraConfig.fromTree;
			if (selectedRecord) {
				var isChild = selectedRecord.data['isChild'];
				if (isChild) {
					//a release node in grid or in tree
					return this.localizedChild;
				}
			}
		}
		return this.localizedMain;
	},

	/**
	 * The iconCls for the add button, overwrites base class icon
	 */
	getAddIconCls: function() {
		return 'releaseAdd';
	},
	/**
	 * The iconCls for the edit button, overwrites base class icon
	 */
	getEditIconCls: function() {
		return 'releaseEdit';
	},

	getAddMainLabel: function() {
		return getText(this.getAddTitleKey(), this.localizedMain);
	},

	/**
	 * Add "Form" not "Form assignment" (getEntityLabel() does not fit here)
	 */
	getAddChildLabel: function() {
		return getText(this.getAddTitleKey(), this.localizedChild);
	},

	initActions: function() {
		this.actionAddMainRelease = this.createLocalizedAction(this.getAddMainLabel(),
				this.getAddIconCls(), this.onAddMainRelease, this.getAddMainLabel());
		this.actionAddChildRelease = this.createLocalizedAction(this.getAddChildLabel(),
				this.getAddIconCls(), this.onAddChildRelease, this.getAddChildLabel(), true);
		this.actionEditGridRow = this.createAction(this.getEditButtonKey(), this.getEditIconCls(),
				this.onEditGridRow, true, this.getEditTitleKey(), "editGridRow");
		this.actionEditTreeNode = this.createAction(this.getEditTitleKey(), this.getEditIconCls(),
				this.onEditTreeNode, false, this.getEditTitleKey(), "editTreeNode");
		this.actionDeleteGridRow = this.createAction(this.getDeleteButtonKey(), this.getDeleteIconCls(),
				this.onDeleteFromGrid, true, this.getDeleteTitleKey(), "deleteGridRow");
		this.actionDeleteTreeNode = this.createAction(this.getDeleteTitleKey(), this.getDeleteIconCls(),
				this.onDeleteFromTree, false, this.getDeleteTitleKey(), "deleteTreeNode");
		this.actionDetachFromParentRelease = this.createLocalizedAction(getText("common.lbl.detachFromParent"),
				"clear", this.onDetachFromParentRelease, getText("common.lbl.detachFromParent"));
		this.actionMoveUp = this.createAction("common.btn.up",
				"moveUp", this.onMoveUpGridRow, true, "common.lbl.up", "moveUp");
		this.actionMoveDown = this.createAction('common.btn.down',
				"moveDown", this.onMoveDownGridRow, true, "common.lbl.down", "moveDown");
		var showHideLabelKey = this.getShowHideLabelKey();
		this.actionShowClosed = this.createAction(showHideLabelKey,
				"showHide", this.onShowHide, false, showHideLabelKey, "showHide",
				{enableToggle:true, pressed:!this.showClosedReleases});
		this.actionReload = this.createLocalizedAction(getText("common.btn.reload"),
				"reload", this.onReload);
	},

	getShowHideLabelKey: function() {
		if (this.showClosedReleases) {
			return 'common.btn.hide';
		} else {
			return 'common.btn.show';
		}
	},

	onShowHide: function() {
		var toolbar = borderLayout.getActiveToolbarList();
		var toolbarButton = toolbar.getComponent('showHide');
		//this.showClosedReleases = !this.showClosedReleases;
		this.showClosedReleases = !toolbarButton.pressed;
		var showHideLabel = getText(this.getShowHideLabelKey());
		toolbarButton.setText(showHideLabel);
		toolbarButton.setTooltip(showHideLabel);
		this.treeNodeSelect(null, null, null, {showClosedReleases:this.showClosedReleases});
	},

	/**
	 * Reload the selected branch after showHide
	 */
	onReload: function() {
		var node = this.getLastSelectedTreeNode();
		var treeStore = this.tree.getStore();
		treeStore.load({node:node});
	},

	/**
	 * Get the extra parameters for the gridStore
	 */
	getGridExtraParams: function(node, opts) {
		if (CWHF.isNull(node)) {
			//called manually
			node = this.selectedNode;
		}
		var params = {
			node: node.data['id']
		};
		if (opts && opts['showClosedReleases']) {
			//called manually from onShowHide handler
			params['showClosedReleases'] = opts['showClosedReleases'];
		}
		return params;
	},


	/**
	 * Initialize all actions and return the toolbar actions
	 */
	getToolbarActions: function() {
		return [this.actionAddMainRelease, this.actionAddChildRelease,
				this.actionEditGridRow, this.actionDeleteGridRow,
				this.actionMoveUp, this.actionMoveDown, this.actionShowClosed];
	},

	/**
	 * Handler for adding a folder node
	 */
	onAddMainRelease: function() {
		this.onAdd(false);
	},

	/**
	 * Handler for adding a leaf node
	 */
	onAddChildRelease: function() {
		this.onAdd(true);
	},

	/**
	 * Handler for adding a folder node
	 */
	onAdd: function(addAsChild) {
		var title = this.getTitle(this.getAddTitleKey());
		var loadParams = this.getAddParams(addAsChild);
		var submitParams = this.getAddParams(addAsChild);
		//var reloadParams = this.getAddReloadParamsAfterSave(false);
		var reloadParamsFromResult = this.getAddSelectionAfterSaveFromResult();
		var selectedRecord = this.getSingleSelectedRecord(true);
		return this.onAddEdit(title, selectedRecord, null, false, true, true,
				loadParams, submitParams, null, reloadParamsFromResult);
	},

	/**
	 * Parameters for adding a new leaf
	 */
	/*private*/getAddParams: function(addAsChild) {
		var addParams = {add:true, addAsChild:addAsChild};
		var addToNode = null;
		if (this.selectedNode) {
			addParams['node'] = this.selectedNode.data['id'];
		}
		return addParams;
	},

	/**
	 * Get the node to select after save after add operation
	 */
	/*private*/getAddSelectionAfterSaveFromResult: function() {
		//specify nodeIDToSelect to select the added node based on the 'node' field from resulting JSON,
		//do not specify rowToSelect, do not select anything in the grid after add
		//return {parameterName:'nodeIDToSelect', fieldNameFromResult:'node'};
		return [{parameterName:'nodeIDToSelect', fieldNameFromResult:'node'},
				{parameterName:'rowToSelect', fieldNameFromResult:'node'},
				{parameterName:'nodeIDToReload', fieldNameFromResult:'nodeIDToReload'},
				{parameterName:'reloadTree', fieldNameFromResult:'reloadTree'}];
	},

	/**
	 * Get the actions available in context menu depending on the currently selected row
	 *
	 */
	getTreeContextMenuActions: function(selectedRecord, selectionIsSimple) {
		if (selectedRecord) {
			var actions = [this.actionAddChildRelease, this.actionEditTreeNode, this.actionDeleteTreeNode, this.actionReload];
			if (selectedRecord.parentNode && !selectedRecord.parentNode.isRoot()) {
				actions.push(this.actionDetachFromParentRelease);
			}
			return actions;
		}
		return [];
	},


	/**
	 * Gets the grid store's fields for the selected node
	 */
	getTreeFields: function(node) {
			return [{name : 'id', mapping : 'id', type: 'string'},
					{ name : 'text', mapping : 'text', type: 'string'},
					{ name : 'leaf', mapping : 'leaf', type: 'boolean'},
					{ name: 'isChild',	type: 'boolean'},
					{ name : 'iconCls', mapping : 'iconCls', type: 'string'}];
	},

	/**
	 * Gets the grid store's fields for the selected node
	 */
	getGridFields: function(node) {
			return [{name: 'node',	type: 'string'},
				{name : 'label', type: 'string'},
				{name: 'statusFlag', type: 'int'},
				{name: 'statusLabel', type: 'string'},
				{name: 'dueDate',	type: 'date', dateFormat: com.trackplus.TrackplusConfig.ISODateFormat},
				{name: 'defaultNoticed',	type: 'boolean'},
				{name: 'defaultScheduled',	type: 'boolean'},
				{name: 'isChild',	type: 'boolean'}];
	},

	/**
	 * Gets the grid columns for the selected node
	 */
	getGridColumns: function(node) {
			return [{text: getText('common.lbl.name'),
				flex:1, dataIndex: 'label', sortable: false, renderer:this.renderer},
			{text: getText('admin.project.release.lbl.status'),
				flex:1, dataIndex: 'statusLabel', sortable: false, renderer:this.renderer},
			{text: getText('admin.project.release.lbl.dueDate'),
				flex:1, dataIndex: 'dueDate', sortable: false,
				renderer:this.dateRenderer},
			{xtype: 'checkcolumn', text: getText('admin.project.release.lbl.defaultReleaseNoticed'),
				flex:1, dataIndex: 'defaultNoticed', sortable: false},
			{xtype: 'checkcolumn', text: getText('admin.project.release.lbl.defaultReleaseScheduled'),
				flex:1, dataIndex: 'defaultScheduled', sortable: false}];
	},

	dateRenderer: function(value, metadata, record) {
		if (record.data.statusFlag===2) {
			metadata.style = 'color:#909090';
		}
		return Ext.util.Format.date(value, com.trackplus.TrackplusConfig.DateFormat);
	},

	/**
	 * Renderer for grid columns
	 */
	renderer: function(value, metadata, record){
		if (record.data.statusFlag===2) {
			metadata.style = 'color:#909090';
		}
		return value;
	},


	/**
	 * Return false if dragging this node is not allowed
	 */
	canDragDropNode: function(nodeToDrag, copy, overModel) {
		return nodeToDrag && !nodeToDrag.isAncestor(overModel);
	},

	onDetachFromParentRelease: function() {
		var selectedRecord = this.getLastSelected(true);
		if (selectedRecord) {
			var nodeID = selectedRecord.data["id"];
			Ext.Ajax.request({
				url: this.getBaseAction() + "!clearParent.action",
				params: {
					node:nodeID
				},
				disableCaching:true,
				scope:this,
				success: function(response){
					com.trackplus.util.RefreshAfterSubmit.refreshTreeAfterSubmit.call(this, {nodeIDToSelect:nodeID, reloadTree:true});
				}
			});
		}
	},

	/**
	 * Get the panel items
	 * recordData: the record data (for the record to be edited or added to)
	 * isLeaf: whether add a leaf or a folder
	 * add: whether it is add or edit
	 * fromTree: operations started from tree or from grid
	 * operation: the name of the operation
	 */
	getPanelItems: function(recordData, isLeaf, add, fromTree, operation) {
		return com.trackplus.admin.project.Release.createEditPanelItems(this.labelWidth);
	},

	/**
	 * The method to process the data to be loaded arrived from the server
	 */
	getEditPostDataProcess: function(record, isLeaf, add) {
		return com.trackplus.admin.project.Release.postDataLoadCombo;
	},



	recommendedReplace:function(scope, submit, result) {
		var releaseTree = CWHF.createSingleTreePicker("admin.project.release.moveReleaseItems.replacementRelease",
	            "replacementID", result.replacementTree, null,
	            {itemId:"replacementID",
				allowBlank:true,
	            labelWidth:250,
	            margin:'5 0 0 0'
	            });
		var windowItems = [{xtype : 'label',
			html: result.errorMessage}, releaseTree];
		var title = getText("admin.project.release.moveReleaseItems.title");
		var replaceLoad = {loadHandler: function() {}};//nothing to load
		var replaceSubmit = {	//submitUrl:me.getReplaceAndDeleteUrl(extraConfig),
						//submitUrlParams:me.getReplaceAndDeleteParams(selectedRecords, extraConfig),
						submitButtonText:getText("common.btn.move"),
						submitHandler: function(window, submitUrl, submitUrlParams, extraConfig) {
							//var theForm = scope.formEdit.getForm();
							//var replaceReleaseForItems = scope.formPanel.getComponent("replacementID");
							submit.submitUrlParams["replacementID"]=releaseTree.getValue();
							window.close();
							scope.submitHandler(submit, this, 0);
						}
						//deleting more users can be a lengthy operation
	                    //timeout:300,
						//refreshAfterSubmitHandler:me.reload,
						//refreshParametersBeforeSubmit:me.getReloadParamsAfterDelete(selectedRecords, extraConfig, responseJson)
					};
		var windowConfig = Ext.create('com.trackplus.util.WindowConfig'/*,
				{postDataProcess:me.replaceOptionPostDataProcess, extraConfig:extraConfig}*/);
		windowConfig.showWindow(this, title, this.replacementWidth, this.replacementHeight, replaceLoad, replaceSubmit, windowItems);
	},

	/**
	 * The replacement items for the deleted entity
	 */
	getReplacementItems: function(responseJson, selectedRecords, extraConfig) {
		return [{xtype : 'label',
				itemId: 'replacementWarning'},
	            CWHF.createSingleTreePicker("Replacement",
	            "replacementID", [], null,
	            {itemId:"replacementID",
	            allowBlank:false,
	             blankText: getText('common.err.replacementRequired',
	                    this.getEntityLabel(extraConfig)),
	             labelWidth:150,
	             margin:'5 0 0 0'
	            })
				];
	},

	/**
	 * Load the data source and value for the replacement options tree
	 * Override this for different tree based pickers
	 */
	loadReplacementOptionData: function(replacementControl, data) {
	    replacementControl.updateMyOptions(data["replacementTree"]);
	},

	getActionItemIdsWithContextDependentLabel: function() {
		return ["editGridRow", "editTreeNode",
				"deleteGridRow", "deleteTreeNode", "moveUp", "moveDown", "showHide"];
	},

	/**
	 * Which actions to enable/disable depending on tree selection
	 */
	getToolbarActionChangesForTreeNodeSelect: function(selectedNode) {
		this.actionAddChildRelease.setDisabled(CWHF.isNull(selectedNode) || this.rootID===selectedNode.data['id']);
		this.actionEditGridRow.setDisabled(true);
		this.actionDeleteGridRow.setDisabled(true);
		this.actionMoveUp.setDisabled(true);
		this.actionMoveDown.setDisabled(true);
	},

	/**
	 * Enable/disable actions based on the actual selection
	 */
	enableDisableToolbarButtons: function (view, selections) {
		if (CWHF.isNull(selections) || selections.length===0) {
			this.actionAddChildRelease.setDisabled(true);
			this.actionDeleteGridRow.setDisabled(true);
			this.actionEditGridRow.setDisabled(true);
			this.actionMoveUp.setDisabled(true);
			this.actionMoveDown.setDisabled(true);
		} else {
			this.actionDeleteGridRow.setDisabled(false);
			if (selections.length===1) {
				this.actionAddChildRelease.setDisabled(false);
				this.actionEditGridRow.setDisabled(false);
				this.actionMoveUp.setDisabled(false);
				this.actionMoveDown.setDisabled(false);
			} else {
				this.actionAddChildRelease.setDisabled(true);
				this.actionEditGridRow.setDisabled(true);
				this.actionMoveUp.setDisabled(true);
				this.actionMoveDown.setDisabled(true);
			}
		}
	},

	getRootID: function() {
		return this.getProjectID();
	}

});
