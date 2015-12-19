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
 * Base class for "tree with grid"-based CRUD operations
 */
Ext.define("com.trackplus.admin.TreeWithGridController",{
	extend:"com.trackplus.admin.TreeBaseController",
	config: {
	},
	/**
	 * Whether selecting a folder node in the tree results in a grid in the detail part or other detail info
	 */
	showGridForFolder: true,
	/**
	 * If showGridForFolder is false, then whether the folder details are loaded manually or through a form load method
	 */
	folderDetailByFormLoad: false,
	/**
	 * Whether to show a grid with a single row instead of other detailed view of the leaf node
	 */
	showGridForLeaf: true,
	
	/**
	 * If showGridForLeaf is false, then whether the leaf details are loaded manually or through a form load method
	 */
	leafDetailByFormLoad: false,

	//set in com.trackplus.util.RefreshAfterSubmit.refreshGridAndTreeAfterSubmit
	//rowIdToSelect: null,

	//cutCopyNode: null,
	
	getShowGridForLeaf: function() {
		return this.showGridForLeaf;
	},

	entityDialog: null,
	
	/**
	 * Get the itemId of those actions whose context menu text or
	 * toolbar button tooltip should be changed according to the current selection
	 */
	/*protected*/getActionItemIdsWithContextDependentLabel: function() {
		return ["editGridRow", "editTreeNode", "deleteGridRow", "deleteTreeNode"];
	},
	
	/**
	 * Get the context menu actions either for grid or for tree
	 */
	/*private*/getContextMenuActions: function(selectedRecords, selectionIsSimple, fromTree) {
		if (fromTree) {
			return this.getView().getTreeContextMenuActions(selectedRecords, selectionIsSimple);
		} else {
			return this.getView().getGridContextMenuActions(selectedRecords, selectionIsSimple);
		}
	},

	/**
	 * Show the context menu in grid
	 */
	/*private*/onGridRowCtxMenu: function(grid, record, item, index, evtObj) {
		this.onCtxMenu(false, record, evtObj);
		return false;
	},

	/**
	 * Get the detail part after selecting a tree node
	 */
	/*protected*/loadDetailPanel: function(node, leaf, opts) {
		if (leaf) {
			if (this.getShowGridForLeaf()) {
				this.getView().getGridPanel.call(this.getView(), node, opts);
			} else {
				if (this.leafDetailByFormLoad) {
					this.loadDetailPanelWithFormLoad(node, false);
				} else {
					this.loadSimpleDetailPanel(node, false);
				}
			}
		} else {
			if (this.showGridForFolder) {
				this.getView().getGridPanel.call(this.getView(), node, opts);
			} else {
				if (this.folderDetailByFormLoad) {
					this.loadDetailPanelWithFormLoad(node, false);
				} else {
					this.loadSimpleDetailPanel(node, false);
				}
			}
		}
	},


	/**
	 * Which actions to enable/disable depending on tree selection
	 */
	/*protected*//*getToolbarActionChangesForTreeNodeSelect: function(selectedNode) {
		//var singleSelection = arrSelectedNodes.length===1;
		this.actionAddFolder.setDisabled(false);
		this.actionAddLeaf.setDisabled(false);
		//nothing selected in the grid
		this.actionEditGridRow.setDisabled(true);
		this.actionDeleteGridRow.setDisabled(true);
	},*/

	/*protected*/onTreeNodeDblClick: function(view, record) {
		this.onEditTreeNode();
	},

	/**
	 * Whether drag and drop on tree is possible
	 */
	/*protected*/hasDragAndDropOnGrid: function(node) {
		return this.dragAndDropOnGrid;
	},

	/*protected*/onGridDrop: function(node, data, dropRec, dropPosition) {
		var before = false;
		if (dropPosition==="before") {
			before = true;
		}
		var request={node:data.records[0].get('node'), droppedToNode:dropRec.get('node'), before:before};
		this.onOrderChange(this.getBaseServerAction() + "!droppedNear.action", request);
	},

	/**
	 * Move the selected grid row up
	 */
	onMoveUpGridRow: function() {
		var gridRow = this.getView().getLastSelectedGridRow();
		if (gridRow) {
			nodeID = gridRow.data.node;
			this.onOrderChange(this.getBaseServerAction() + "!moveUp.action", {node:nodeID});
		}
	},

	/**
	 * Move the selected grid row down
	 */
	onMoveDownGridRow: function() {
		var gridRow = this.getView().getLastSelectedGridRow();
		if (gridRow) {
			nodeID = gridRow.data.node;
			this.onOrderChange(this.getBaseServerAction() + "!moveDown.action", {node:nodeID});
		}
	},

	/**
	 * Private function foe changing the order by drag and drop or move up/down
	 */
	onOrderChange: function(url, params) {
		Ext.Ajax.request({
			url: url,
			disableCaching: true,
			scope: this,
			success: function(response){
				var responseJson = Ext.decode(response.responseText);
				if (responseJson.success !== true) {
					Ext.MessageBox.alert(this.failureTitle, responseJson.errorMessage);
				}
				this.reload.call(this, {rowToSelect:responseJson.node});
			},
			failure: function(reponse){
				Ext.MessageBox.alert(this.failureTitle, reponse.responseText);
			},
			isUpload: false,
			method:'POST',
			params: params
		});
	},

	/**
	 * Enable/disable actions based on the actual selection
	 */
	/*protected*/onGridSelectionChange: function (selModel, selections) {
		this.enableDisableToolbarButtons(selModel, selections);
		var selectedRow = null;
		if (selections && selections.length>0) {
			selectedRow = selections[0];
		}
		this.adjustToolbarButtonsTooltip(selectedRow, false);
	},

	/*protected*/onGridRowDblClick: function(view, record) {
		this.onEditGridRow();
	},

	/**
	 * Enable/disable actions based on the actual selection
	 */
	/*protected*/enableDisableToolbarButtons: function (selModel, selections) {
		if (CWHF.isNull(selections) || selections.length===0) {
			if (this.getView().actionDeleteGridRow) {
				this.getView().actionDeleteGridRow.setDisabled(true);
			}
			if (this.getView().actionEditGridRow) {
				this.getView().actionEditGridRow.setDisabled(true);
			}
		} else {
			if (this.getView().actionDeleteGridRow) {
				this.getView().actionDeleteGridRow.setDisabled(false);
			}
			if (this.getView().actionEditGridRow) {
				if (selections.length===1) {
					this.getView().actionEditGridRow.setDisabled(false);
				} else {
					this.getView().actionEditGridRow.setDisabled(true);
				}
			}
		}
	},

	/**
	 * Url for editing an entity
	 * We suppose that add/edit use the same edit method on server side
	 * Differentiation is made based on "node", "add" and "leaf" request parameters
	 * addFolder: "node" is the parent node, "add" is true, "leaf" is false
	 * addLeaf: "node" is the parent node, "add" is true, "leaf" is true
	 * edit: "node" is the id of the edited entity (whether it is folder or leaf is decoded based on the structure of the "node")
	 */
	/*protected*/getEditUrl: function(isLeaf) {
		return this.getNodeBaseAction({isLeaf:isLeaf}) + '!edit.action';
	},

	/**
	 * Url for saving of an entity
	 */
	/*protected*/getSaveUrl: function(isLeaf) {
		return this.getNodeBaseAction({isLeaf:isLeaf}) + '!save.action';
	},

	/**
	 * Reload method
	 */
	/*protected*/reload: function(refreshParametersObject) {
		this.reloadTreeWithGrid(this.getView().tree, this.getView().grid, refreshParametersObject);
	},
	
	/**
	 * Refreshes a grid's store and selects a row by rowToSelect parameter if it is specified
	 * Called from selectAfterReload() (grid and tree refresh) and from simple grid CRUD
	 */
	refreshGridAfterSubmit: function(refreshParametersObject) {
		this.getView().grid.getStore().load({
			scope:this,
			callback:function(){
				if (refreshParametersObject) {
					var rowToSelect = refreshParametersObject.rowToSelect;
					if (rowToSelect) {
						var row = this.getView().grid.getStore().getById(rowToSelect);
						if (row) {
							var gridSelectionModel = this.getView().grid.getSelectionModel();
							gridSelectionModel.deselectAll();
							gridSelectionModel.select(row);
						}
					}
				}
			}
		})
	},

	/**
	 * Get the node to reload after save after add operation
	 */
	/*protected*/getAddReloadParamsAfterSave: function(addLeaf) {
		if (this.getView().selectedNode) {
			var leaf = this.getView().selectedNode.data['leaf'];
			if (leaf) {
				//selected node is leaf: add to a leaf
				var parentNode = this.getView().selectedNode.parentNode;
				if (parentNode) {
					if (addLeaf) {
						//add leaf to a leaf -> means add sibling -> the parent of the selectedNode should be reloaded
						return {nodeIDToReload: parentNode.data['id']};
					} else {
						//add folder when a leaf is selected -> add sibling to the parent's node, the parent of the parent should be reloaded
						//(from tree context menu it is not possible, only from toolbar)
						parentNode = parentNode.parentNode;
						if (parentNode) {
							return {nodeIDToReload: parentNode.data['id']};
						}
					}
				}
			} else {
				//selected node is folder
				return {nodeIDToReload: this.getView().selectedNode.data['id']};
			}
		}
		return null;
	},

	/**
	 * Get the node to reload after save after edit operation
	 */
	/*protected*/getEditReloadParamsAfterSave: function(fromTree) {
		if (this.getView().selectedNode) {
			if (fromTree) {
				//edited/copied from tree
				var parentNode = this.getView().selectedNode.parentNode;
				if (parentNode) {
					//the parent of the edited node should be reloaded
					return {nodeIDToReload: parentNode.data['id']};
				}
			} else {
				//edited from grid:
				if (this.getShowGridForLeaf() && this.getView().selectedNode.isLeaf()) {
					//in the tree a leaf node selected -> grid with a single row: the parent of the selected tree node should be reloaded
					var parentNode = this.getView().selectedNode.parentNode;

					if (parentNode) {
						//the parent of the edited node should be reloaded
						return {nodeIDToReload: parentNode.data['id']};
					}
				} else {
					//in the tree the parent of the edited grid row is selected: the actually selected tree node should be reloaded
					return {nodeIDToReload: this.getView().selectedNode.data['id']};
				}
			}
		}
		return null;
	},

	/**
	 * Get the node to select after save after add operation
	 */
	/*private*/getAddSelectionAfterSaveFromResult: function() {
		//specify nodeIDToSelect to select the added node based on the 'node' field from resulting JSON,
		//do not specify rowToSelect, do not select anything in the grid after add
		//return {parameterName:'nodeIDToSelect', fieldNameFromResult:'node'};
		return [{parameterName:"nodeIDToSelect", fieldNameFromResult:"node"},
				{parameterName:"rowToSelect", fieldNameFromResult:"node"}];
	},

	/**
	 * Get the node to select after save after edit operation
	 */
	/*private*/getEditReloadParamsAfterSaveFromResult: function(fromTree) {
		if (fromTree) {
			//edited from tree: select
			return [{parameterName:"nodeIDToSelect", fieldNameFromResult:"node"},
					{parameterName:"rowToSelect", fieldNameFromResult:"node"}];
		} else {
			//edited from grid: whatever the tree selection was it remains ()
			//specify rowToSelect to select the edited row in the grid based on the 'node' json field,
			if (this.getShowGridForLeaf() && this.getView().selectedNode.isLeaf()) {
				//leaf node selected: select it again after reload
				return [{parameterName:"nodeIDToSelect", fieldNameFromResult:"node"},
				 {parameterName:"rowToSelect", fieldNameFromResult:"node"}];
			} else {
				//parent of the edited node selected: leave the parent node selected
				return {parameterName:"rowToSelect", fieldNameFromResult:"node"};
			}
		}
	},

	/**
	 * Handler for adding a folder node
	 */
	/*private*/onAddFolder: function() {
		var operation = "addFolder";
		var title = this.getView().getActionTooltip(this.getView().getAddTitleKey(), {isLeaf:false, selectedRecord:this.getView().getLastSelected(true)});
		var loadParams = this.getAddFolderParams();
		var submitParams = this.getAddFolderParams();
		var reloadParams = this.getAddReloadParamsAfterSave(false);
		var reloadParamsFromResult = this.getAddSelectionAfterSaveFromResult();
		var selectedRecord = this.getView().getSingleSelectedRecord(true);
		if (CWHF.isNull(selectedRecord)) {
			selectedRecord = this.getView().tree.getRootNode();
		}
		return this.onAddEdit(title, selectedRecord, operation, false, true, true,
				loadParams, submitParams, reloadParams, reloadParamsFromResult, this.reload);
	},

	/**
	 * Handler for adding a leaf node
	 */
	/*private*/onAddLeaf: function() {
		var operation = "addLeaf";
		var title = this.getView().getActionTooltip(this.getView().getAddTitleKey(), {isLeaf:true, selectedRecord:this.getView().getLastSelected(true)});
		var loadParams = this.getAddLeafParams();
		var submitParams = this.getAddLeafParams();
		var reloadParams = this.getAddReloadParamsAfterSave(true);
		var reloadParamsFromResult = this.getAddSelectionAfterSaveFromResult();
		var selectedRecord = this.getView().getSingleSelectedRecord(true);
		if (CWHF.isNull(selectedRecord)) {
			selectedRecord = this.getView().tree.getRootNode();
		}
		return this.onAddEdit(title, selectedRecord, operation, true, true, true,
				loadParams, submitParams, reloadParams, reloadParamsFromResult, this.reload);
	},

	/**
	 * Handler for editing a grid row
	 */
	/*private*/onEditGridRow: function() {
		return this.onEdit(false);
	},

	/**
	 * Handler for editing a grid row
	 */
	/*private*/onEditTreeNode: function() {
		return this.onEdit(true);
	},

	/**
	 * Handler for edit
	 */
	/*private*/onEdit: function(fromTree) {
		var operation = "edit";
		var isLeaf = this.getView().selectedIsLeaf(fromTree);
		var title = this.getView().getActionTooltip(this.getView().getEditTitleKey(), {isLeaf:isLeaf, fromTree:fromTree, selectedRecord:this.getView().getLastSelected(fromTree)});
		var loadParams = this.getEditParams(fromTree);
		var submitParams = this.getEditParams(fromTree);
		var reloadParams = this.getEditReloadParamsAfterSave(fromTree);
		var reloadParamsFromResult = this.getEditReloadParamsAfterSaveFromResult(fromTree);
		var selectedRecord = this.getView().getSingleSelectedRecord(fromTree);
		return this.onAddEdit(title, selectedRecord, operation, isLeaf, false, fromTree,
				loadParams, submitParams, reloadParams, reloadParamsFromResult, this.reload);
	},
	

	/**
	 * Handler for add/edit a node/row
	 * title: 'add'/'edit'/'copy'
	 * recordData: the selected record (tree node data or grid row data)
	 * operation: "edit"/"add" or anything else in the derived classes
	 * isLeaf: whether to add a leaf or a folder
	 * add: whether it is add or edit
	 * fromTree: operations started from tree or from grid
	 * loadParams
	 * submitParams
	 * refreshParams
	 * refreshParamsFromResult
	 */
	/*private*/onAddEdit: function(title, record, operation, isLeaf, add, fromTree, loadParams, submitParams,
			refreshParams, refreshParamsFromResult, refreshAfterSubmitHandler) {
		/*var recordData = null;
		if (record) {
			recordData = record.data;
		}
		var width = this.getEditWidth(recordData, isLeaf, add, fromTree, operation);
		var height = this.getEditHeight(recordData, isLeaf, add, fromTree, operation);
		var loadUrl =  this.getEditUrl(isLeaf);
		var load = {loadUrl:loadUrl, loadUrlParams:loadParams};
		var submitUrl = this.getSaveUrl(isLeaf);
		var submit = {
			submitUrl:submitUrl,
			submitUrlParams:submitParams,
			submitButtonText:this.getSubmitButtonLabel(operation),
			submitHandler:this.submitHandler,
			submitAction:operation,
			refreshAfterSubmitHandler:this.reload,
			refreshParametersBeforeSubmit:refreshParams,
			refreshParametersAfterSubmit:refreshParamsFromResult
		};
		var postDataProcess = this.getEditPostDataProcess(record, isLeaf, add, fromTree, operation);
		var preSubmitProcess = this.getEditPreSubmitProcess(recordData, isLeaf, add);
		var items = this.getView().getPanelItems(recordData, isLeaf, add, fromTree, operation);

	    var additionalActions = this.getAdditionalActions(recordData, submitParams, operation, items);
	    if (additionalActions) {
	        additionalActions.push(submit);
	        submit = additionalActions;
	    }
		var windowParameters = {title:title,
			width:width,
			height:height,
			load:load,
			submit:submit,
			items:items,
			postDataProcess:postDataProcess,
			preSubmitProcess:preSubmitProcess};
		var extraWindowParameters = this.getExtraWindowParameters(recordData, operation);
		if (extraWindowParameters) {
			for (propertyName in extraWindowParameters) {
				windowParameters[propertyName] = extraWindowParameters[propertyName];
			}
		}
		var windowConfig = Ext.create("com.trackplus.util.WindowConfig", windowParameters);*/
		var loadAndSubmitParams = this.getEditParams(fromTree);
		if (CWHF.isNull(refreshAfterSubmitHandler)) {
			refreshAfterSubmitHandler = this.reload;
		}
        var windowParameters = {
        	callerScope:this,
        	windowTitle:title,
        	loadUrlParams: loadParams,
        	submitUrlParams: submitParams,
        	refreshParametersBeforeSubmit: refreshParams,
        	refreshParametersAfterSubmit: refreshParamsFromResult,
        	refreshAfterSubmitHandler: refreshAfterSubmitHandler,
        	entityContext: {
        		//the last selected tree node
        		selectedTreeNode: this.getView().getLastSelectedTreeNode(),
        		//the config object passed to view
        		config: this.getView().getConfig(),
        		//the record data of the actually selected node/row
        		record: record,
        		//the selection is leaf
        		isLeaf: isLeaf,
        		//the operation is add
            	add: add,
            	//whether the operation refres to tree node or grid row
            	fromTree: fromTree,
            	//the name of the operation
            	operation: operation
        	}
        };
		var windowConfig = Ext.create(this.entityDialog, windowParameters);
		windowConfig.showWindowByConfig(this);
	},

	/**
	 * Additional execute method for issue filter
	 */
	/*getAdditionalActions : function(recordData, submitParams, operation, items) {
	    return null;
	},*/
	
	/**
	 * The label for the save button
	 */
    getSubmitButtonLabel : function(operation) {
        return getText(this.getView().getSaveButtonKey());
    },
    
	/**
	 * Add extra window configuration fields by add/edit
	 * windowConfiguration argument is configured with the required fields but
	 * 		any already specified windowConfig field can be overridden, and
	 * 		further optional window options can be specified
	 * type: typically "folder" or "leaf" but for more leaf types it can be customized
	 */
	/*protected*/getExtraWindowParameters: function(recordData, operation) {
		//optionally specify
		/*
		return {fileUpload: ...
			windowConfig: ...
			panelConfig: ...}
		*/
	},

	/*private*//*getEditPreSubmitProcess: function(recordData, isLeaf, add) {
		if (isLeaf) {
			if (this.getEditLeafPreSubmitProcess) {
				return this.getEditLeafPreSubmitProcess(recordData, add);
			}
		} else {
			if (this.getEditFolderPreSubmitProcess) {
				return this.getEditFolderPreSubmitProcess(recordData, add);
			}
		}
		return null;
	},*/

	/**
	 * Method to add extra request parameters be sent to the sever before submitting the folder data
	 */
	/*protected abstract*///getEditFolderPreSubmitProcess: null,

	/**
	 * Method to add extra request parameters be sent to the sever before submitting the leaf data
	 */
	/*protected abstract*///getEditLeafPreSubmitProcess: null,

	/**
	 * Cut a tree node
	 */
	onCutTreeNode: function() {
		this.getView().cutCopyNode = this.getView().selectedNode;
		this.getView().copy = false;
	},

	/**
	 * Copy a tree node
	 */
	onCopyTreeNode: function() {
		this.getView().cutCopyNode = this.getView().selectedNode;
		this.getView().copy = true;
	},

	/**
	 * Paste a node in the tree after copy/cut
	 */
	onPasteTreeNode: function() {
		this.getView().onDropTreeNode(this.getView().cutCopyNode, this.getView().selectedNode, this.getView().copy);
		this.cutCopyNode = null;
	},

	/**
	 * Delete handler for deleting from the grid
	 */
	/*private*/onDeleteFromGrid: function() {
		this.onDelete(false);
	},

	/**
	 * Delete handler for deleting from the tree
	 */
	/*private*/onDeleteFromTree: function() {
		this.onDelete(true);
	},

	/**
	 * Handler for delete
	 */
	/*private*/onDelete: function(fromTree) {
		var selectedRecords = this.getView().getSelectedRecords(fromTree);
		if (selectedRecords) {
			var isLeaf = this.getView().selectedIsLeaf(fromTree);
			var extraConfig = {fromTree:fromTree, isLeaf:isLeaf};
			var entityLabel = this.getView().getEntityLabel(extraConfig);
			this.deleteHandler(entityLabel, selectedRecords, extraConfig);
		}
	},

	/**
	 * Get the refresh parameters after delete
	 */
	/*private*/getReloadParamsAfterDelete: function(selectedRecords, extraConfig, responseJson) {
		if (selectedRecords) {
			//we suppose that only one selection is allowed in tree
			var selNode = selectedRecords;
			if (selNode) {
				var parentNode = null;
				var parentNodeID = null;
				if (extraConfig) {
					fromTree = extraConfig.fromTree;
					if (fromTree) {
						//delete from tree
						parentNode = selNode.parentNode;
						if (parentNode) {
							parentNodeID = parentNode.data.id;
							//select the parent of the deleted node for reload and select
							return {nodeIDToReload:parentNodeID, nodeIDToSelect:parentNodeID};
						}
					} else {
						//delete from grid: the parent is selected already in tree, leave that to be reloaded and selected
						if (this.getShowGridForLeaf() && this.getView().selectedNode.isLeaf()) {
							//in the tree a leaf node selected -> grid with a single row: the parent of the selected tree node should be reloaded
							var parentNode = this.getView().selectedNode.parentNode;
							if (parentNode) {
								//the parent of the edited node should be reloaded
								return {nodeIDToReload: parentNode.data['id']};
							}
						} else {
							//in the tree the parent of the edited grid row is selected: the actually selected tree node should be reloaded
							return {nodeIDToReload: this.getView().selectedNode.data['id']};
						}
					}
				}
			}
		}
		return null;
	}
	
});
