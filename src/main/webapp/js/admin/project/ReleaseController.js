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

Ext.define("com.trackplus.admin.project.ReleaseController",{
	extend: "Ext.app.ViewController",
	alias: "controller.release",
	mixins: {
		baseController: "com.trackplus.admin.TreeWithGridController"
	},
	
	entityDialog: "com.trackplus.admin.project.ReleaseEdit",
	
	baseServerAction: "release",
	replacementIsTree: true,

	//folderEditWidth:500,
	//folderEditHeight:400,
	//labelWidth: 120,

	//localizedMain: null,
	//localizedChild: null,
	//showClosedReleases: true,

	//by adding a release whether to add a main release or a child release
	//addAsChild: false,
	//actions
	

	onShowHide: function(button) {
		//var toolbar = borderLayout.getActiveToolbarList();
		//var toolbarButton = toolbar.getComponent('showHide');
		//this.showClosedReleases = !this.showClosedReleases;
		this.getView().setShowClosedReleases(!button.pressed);
		var showHideLabel = getText(this.getShowHideLabelKey());
		button.setText(showHideLabel);
		button.setTooltip(showHideLabel);
		this.reload({nodeIDToReload:this.getView().tree.getRootNode().get("id"), nodeIDToSelect:this.getView().selectedNodeID});
		//this.onTreeNodeSelect(null, this.getView().tree.getRootNode(), null, {showClosedReleases:this.getView().getShowClosedReleases()});
	},

	getShowHideLabelKey: function() {
		if (this.getView().getShowClosedReleases()) {
			return 'common.btn.hide';
		} else {
			return 'common.btn.show';
		}
	},
	
	/**
	 * Reload the selected branch after showHide
	 */
	/*onReload: function() {
		this.reloadTreeWithGrid();
		var node = this.getView().getLastSelectedTreeNode();
		var treeStore = this.tree.getStore();
		treeStore.load({node:node});
	},*/


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
		var title = null;
		if (addAsChild) {
			title = this.getView().getAddMainLabel();
		} else {
			title = this.getView().getAddChildLabel();
		}
		//var title = this.getTitle(this.getAddTitleKey());
		var loadParams = this.getAddParams(addAsChild);
		var submitParams = this.getAddParams(addAsChild);
		var reloadParamsFromResult = this.getAddSelectionAfterSaveFromResult();
		var selectedRecord = this.getView().getSingleSelectedRecord(true);
		return this.onAddEdit(title, selectedRecord, null, false, true, true,
				loadParams, submitParams, null, reloadParamsFromResult, this.reload);
	},

	/**
	 * Parameters for adding a new leaf
	 */
	/*private*/getAddParams: function(addAsChild) {
		var addParams = {add:true, addAsChild:addAsChild};
		var addToNode = null;
		//if (addAsChild) {
			if (this.getView().selectedNode) {
				addParams["node"] = this.getView().selectedNode.get("id");
			}
		/*} else {
			addParams["node"] = this.getView().getProjectID();
		}*/
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
	/*getTreeContextMenuActions: function(selectedRecord, selectionIsSimple) {
		if (selectedRecord) {
			var actions = [this.actionAddChildRelease, this.actionEditTreeNode, this.actionDeleteTreeNode, this.actionReload];
			if (selectedRecord.parentNode && !selectedRecord.parentNode.isRoot()) {
				actions.push(this.actionDetachFromParentRelease);
			}
			return actions;
		}
		return [];
	},*/


	
	onDetachFromParentRelease: function() {
		var selectedRecord = this.getView().getLastSelected(true);
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
	/*getPanelItems: function(recordData, isLeaf, add, fromTree, operation) {
		return com.trackplus.admin.project.Release.createEditPanelItems(this.labelWidth);
	},*/

	/**
	 * The method to process the data to be loaded arrived from the server
	 */
	/*getEditPostDataProcess: function(record, isLeaf, add) {
		return com.trackplus.admin.project.Release.postDataLoadCombo;
	},*/



	/*recommendedReplace:function(scope, submit, result) {
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
		var windowConfig = Ext.create('com.trackplus.util.WindowConfig');
		windowConfig.showWindow(this, title, this.replacementWidth, this.replacementHeight, replaceLoad, replaceSubmit, windowItems);
	},*/

	/**
	 * The replacement items for the deleted entity
	 */
	/*getReplacementItems: function(responseJson, selectedRecords, extraConfig) {
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
	},*/

	/**
	 * Load the data source and value for the replacement options tree
	 * Override this for different tree based pickers
	 */
	/*loadReplacementOptionData: function(replacementControl, data) {
	    replacementControl.updateMyOptions(data["replacementTree"]);
	},*/

	getActionItemIdsWithContextDependentLabel: function() {
		return ["editGridRow", "editTreeNode",
				"deleteGridRow", "deleteTreeNode", "moveUp", "moveDown", "showHide"];
	},

	/**
	 * Which actions to enable/disable depending on tree selection
	 */
	/*getToolbarActionChangesForTreeNodeSelect: function(selectedNode) {
		this.actionAddChildRelease.setDisabled(CWHF.isNull(selectedNode) || this.rootID===selectedNode.data['id']);
		this.actionEditGridRow.setDisabled(true);
		this.actionDeleteGridRow.setDisabled(true);
		this.actionMoveUp.setDisabled(true);
		this.actionMoveDown.setDisabled(true);
	},*/

	/**
	 * Enable/disable actions based on the actual selection
	 */
	enableDisableToolbarButtons: function (view, selections) {
		if (CWHF.isNull(selections) || selections.length===0) {
			this.getView().actionAddChildRelease.setDisabled(true);
			this.getView().actionDeleteGridRow.setDisabled(true);
			this.getView().actionEditGridRow.setDisabled(true);
			this.getView().actionMoveUp.setDisabled(true);
			this.getView().actionMoveDown.setDisabled(true);
		} else {
			this.getView().actionDeleteGridRow.setDisabled(false);
			if (selections.length===1) {
				this.getView().actionAddChildRelease.setDisabled(false);
				this.getView().actionEditGridRow.setDisabled(false);
				this.getView().actionMoveUp.setDisabled(false);
				this.getView().actionMoveDown.setDisabled(false);
			} else {
				this.getView().actionAddChildRelease.setDisabled(true);
				this.getView().actionEditGridRow.setDisabled(true);
				this.getView().actionMoveUp.setDisabled(true);
				this.getView().actionMoveDown.setDisabled(true);
			}
		}
	}
});
