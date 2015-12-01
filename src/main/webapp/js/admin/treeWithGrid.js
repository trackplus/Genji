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
Ext.define('com.trackplus.admin.TreeWithGrid',{
	extend:'com.trackplus.admin.TreeBase',
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
	 * Whether the grid has context menu listener
	 */
	gridHasItemcontextmenu: true,
	/**
	 * Whether the grid has double click listener
	 */
	gridHasItemdblclick: true,
	/**
	 * If showGridForLeaf is false, then whether the leaf details are loaded manually or through a form load method
	 */
	leafDetailByFormLoad: false,

	/**
	 * The width of the folder edit window
	 * Mandatory field
	 */
	folderEditWidth: 600,
	/**
	 * The height of the folder edit window
	 * Mandatory field
	 */
	folderEditHeight: 400,
	/**
	 * Whether to use drag and drop inside the grid
	 */
	dragAndDropOnGrid: false,
	/**
	 * actions
	 */
	actionAddFolder: null,
	actionAddLeaf: null,
	actionEditGridRow: null,
	actionEditTreeNode: null,
	actionCutTreeNode: null,
	actionCopyTreeNode: null,
	actionPasteTreeNode: null,
	actionDeleteGridRow: null,
	actionDeleteTreeNode: null,
	//set in com.trackplus.util.RefreshAfterSubmit.refreshGridAndTreeAfterSubmit
	rowIdToSelect: null,

	constructor: function(config) {
		var config = config || {};
		this.initConfig(config);
		this.initBase();
	},

	getShowGridForLeaf: function() {
		return this.showGridForLeaf;
	},

	/**
	 * Gets the grid store's fields for the selected node
	 */
	/*protected abstract*/getGridFields: function(node) {
		return [];
	},

	/**
	 * Gets the grid columns for the selected node
	 */
	/*protected abstract*/getGridColumns: function(node) {
		return [];
	},

	/**
	 * Initialize all possible actions
	 */
	/*protected*/initActions: function(){
		this.actionAddFolder = this.createLocalizedAction(this.getTitle(this.getAddTitleKey(), {isLeaf:false}),
				this.getAddFolderIconCls(), this.onAddFolder, this.getTitle(this.getAddTitleKey(), {isLeaf:false}));
		this.actionAddLeaf = this.createLocalizedAction(this.getTitle(this.getAddTitleKey(), {isLeaf:true}),
				this.getAddIconCls(), this.onAddLeaf, this.getTitle(this.getAddTitleKey(), {isLeaf:true}), true);
		this.actionEditGridRow = this.createAction(this.getEditButtonKey(),
				this.getEditIconCls(), this.onEditGridRow, true, this.getEditTitleKey(), "editGridRow");
		this.actionEditTreeNode = this.createAction(this.getEditButtonKey(),
				this.getEditIconCls(), this.onEditTreeNode, false, this.getEditTitleKey(), "editTreeNode");
		this.actionDeleteGridRow = this.createAction(this.getDeleteButtonKey(),
				this.getDeleteIconCls(), this.onDeleteFromGrid, true, this.getDeleteTitleKey(), "deleteGridRow");
		this.actionDeleteTreeNode = this.createAction(this.getDeleteButtonKey(),
				this.getDeleteIconCls(), this.onDeleteFromTree, false, this.getDeleteTitleKey(), "deleteTreeNode");
		if (this.useCopyPaste) {
			this.actionCutTreeNode = this.createAction(this.getCutButtonKey(), this.getCutIconCls(), this.onCutTreeNode, false, this.getCutTitleKey());
			this.actionCopyTreeNode = this.createAction(this.getCopyButtonKey(), this.getCopyIconCls(), this.onCopyTreeNode, false, this.getCopyTitleKey());
			this.actionPasteTreeNode = this.createAction(this.getPasteButtonKey(), this.getPasteIconCls(), this.onPasteTreeNode, false, this.getPasteTitleKey());
			this.actions=[this.actionAddFolder, this.actionAddLeaf, this.actionEditGridRow,
				this.actionCutTreeNode, this.actionCopyTreeNode, this.actionPasteTreeNode, this.actionDeleteGridRow];
		} else {
			this.actions=[this.actionAddFolder, this.actionAddLeaf, this.actionEditGridRow, this.actionDeleteGridRow];
		}
	},

	/**
	 * Initialize all actions and return the toolbar actions
	 */
	/*protected*/getToolbarActions: function() {
		return [this.actionAddFolder, this.actionAddLeaf, this.actionEditGridRow, this.actionDeleteGridRow];
	},

	/**
	 * Get the itemId of those actions whose context menu text or
	 * toolbar button tooltip should be changed according to the current selection
	 */
	/*protected*/getActionItemIdsWithContextDependentLabel: function() {
		return ["editGridRow", "editTreeNode", "deleteGridRow", "deleteTreeNode"];
	},

	/**
	 * Get the actions available in context menu depending on the currently selected row
	 */
	/*protected*/getGridContextMenuActions: function(selectedRecord, selectionIsSimple) {
		if (selectionIsSimple) {
			return [this.actionEditGridRow, this.actionDeleteGridRow];
		} else {
			return [this.actionDeleteGridRow];
		}
	},

	/**
	 * Get the actions available in context menu depending on the currently selected row
	 *
	 */
	/*protected*/getTreeContextMenuActions: function(selectedRecord, selectionIsSimple) {
		var actions = null;
		if (selectionIsSimple) {
			var leaf = selectedRecord.isLeaf();
			if (!leaf) {
				actions = [this.actionAddFolder];
			} else {
				actions = [];
			}
			actions.push(this.actionAddLeaf);
			actions.push(this.actionEditTreeNode);
		} else {
			actions = [];
		}
		if (this.useCopyPaste) {
			actions.push(this.actionCutTreeNode);
			actions.push(this.actionCopyTreeNode);
			actions.push(this.actionPasteTreeNode);
		}
		actions.push(this.actionDeleteTreeNode);
		return actions;
	},

	/**
	 * Get the context menu actions either for grid or for tree
	 */
	/*private*/getContextMenuActions: function(selectedRecords, selectionIsSimple, fromTree) {
		if (fromTree) {
			return this.getTreeContextMenuActions(selectedRecords, selectionIsSimple);
		} else {
			return this.getGridContextMenuActions(selectedRecords, selectionIsSimple);
		}
	},

	/**
	 * Show the context menu in grid
	 */
	/*private*/onGridRowCtxMenu: function(grid, record, item, index, evtObj) {
		this.onCtxMenu(false, record, evtObj);
		//var scope = this.getSelectedNodeSpecificScope(record, false);
		//this.onCtxMenu.call(scope, false, record, evtObj);
		return false;
	},

	/**
	 * Get the detail part after selecting a tree node
	 */
	/*protected*/loadDetailPanel: function(node, leaf, opts) {
		if (leaf) {
			if (this.getShowGridForLeaf()) {
				this.getGridPanel(node, opts);
			} else {
				if (this.leafDetailByFormLoad) {
					this.loadDetailPanelWithFormLoad(node, false);
				} else {
					this.loadSimpleDetailPanel(node, false);
				}
			}
		} else {
			if (this.showGridForFolder) {
				this.getGridPanel(node, opts);
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
	/*protected*/getToolbarActionChangesForTreeNodeSelect: function(selectedNode) {
		//var singleSelection = arrSelectedNodes.length===1;
		this.actionAddFolder.setDisabled(false);
		this.actionAddLeaf.setDisabled(false);
		//nothing selected in the grid
		this.actionEditGridRow.setDisabled(true);
		this.actionDeleteGridRow.setDisabled(true);
	},

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
		this.onOrderChange(this.getBaseAction() + "!droppedNear.action", request);
	},

	/**
	 * Move the selected grid row up
	 */
	onMoveUpGridRow: function() {
		var gridRow = this.getLastSelectedGridRow();
		if (gridRow) {
			nodeID = gridRow.data.node;
			this.onOrderChange(this.getBaseAction() + "!moveUp.action", {node:nodeID});
		}
	},

	/**
	 * Move the selected grid row down
	 */
	onMoveDownGridRow: function() {
		var gridRow = this.getLastSelectedGridRow();
		if (gridRow) {
			nodeID = gridRow.data.node;
			this.onOrderChange(this.getBaseAction() + "!moveDown.action", {node:nodeID});
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
	 * Get the extra parameters for the gridStore
	 */
	/*protected*/getGridExtraParams: function(node, opts) {
		return {
			node: this.selectedNodeID
		};
	},

	/**
	 * Shows the grid according to the selected node
	 */
	/*private*/getGridPanel: function(node, opts) {
		var fields = this.getGridFields(node);
		var columnModelArr = this.getGridColumns(node);
		var store = Ext.create("Ext.data.Store", {
			proxy: {
				type: "ajax",
				url: this.getNodeBaseAction({isLeaf:false}) + "!loadList.action",
				extraParams: this.getGridExtraParams(node, opts),
				reader: {
					type: 'json'
				}
			},
			fields: fields,
			idProperty: "node",
			autoLoad: true,
			listeners: {load: {fn:this.onGridStoreLoad, scope:this}}
		});
		var gridConfig = {
				xtype: "grid",
				region:"center",
				store: store,
				columns: columnModelArr,
				selModel: this.getGridSelectionModel(node),
				autoWidth: true,
				border: false,
				bodyBorder:false,
				cls:"gridNoBorder",
				stripeRows: true
			};
		gridConfig.viewConfig = this.getGridViewConfig(node);
		var gridListeners = {selectionchange: {fn:this.onGridSelectionChange, scope:this}};
		if (this.gridHasItemcontextmenu) {
			gridListeners.itemcontextmenu = {fn:this.onGridRowCtxMenu, scope:this};
		}
		if (this.gridHasItemdblclick) {
			gridListeners.itemdblclick = {fn:this.onGridRowDblClick, scope:this};
		}
		gridConfig.listeners = gridListeners;
		this.grid = Ext.create("Ext.grid.Panel", gridConfig);
		if (this.centerPanel) {
			this.mainPanel.remove(this.centerPanel, true);
		}
		this.centerPanel = this.grid;
		if (this.replaceCenterPanel) {
			this.replaceCenterPanel.call(this,this.centerPanel);
		} else {
			this.mainPanel.add(this.centerPanel);
		}
		this.postGridPanel(this.grid);
		return this.grid;
	},

	getGridViewConfig: function(node) {
		if (this.hasDragAndDropOnGrid(node)) {
			return {
				plugins: {
					ptype: "gridviewdragdrop",
					dragGroup: this.getBaseAction() + "gridDDGroup",
					dropGroup: this.getBaseAction() + "gridDDGroup",
					enableDrag: true,
					enableDrop: true
				},
				listeners: {
					drop: {scope:this, fn: function(node, data, dropRec, dropPosition) {
						this.onGridDrop(node, data, dropRec, dropPosition);
						}
					}
				}
			};
		}
		return null;
	},

	postGridPanel: function(grid) {
	},

	/**
	 * Once the data for store is loaded select the corresponding row from the grid if rowIdToSelect is set
	 */
	onGridStoreLoad: function(store) {
		if (this.rowIdToSelect) {
			var row = store.getById(this.rowIdToSelect);
			if (row) {
				var gridSelectionModel = this.grid.getSelectionModel();
				gridSelectionModel.select(row);
			}
		} else {
			if (store.getTotalCount()===1) {
				//if the grid contains only one entry (especially by showGridForLeaf===true)
				//then select it also automatically to activate the grid selection specific toolbar buttons
				var gridSelectionModel = this.grid.getSelectionModel();
				gridSelectionModel.select(0);
			}
		}
		//this.selectedRowId = null;
	},

	/**
	 * Enable/disable actions based on the actual selection
	 */
	/*protected*/onGridSelectionChange: function (view, selections) {
		this.enableDisableToolbarButtons(view, selections);
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
	/*protected*/enableDisableToolbarButtons: function (view, selections) {
		if (CWHF.isNull(selections) || selections.length===0) {
			if (this.actionDeleteGridRow) {
				this.actionDeleteGridRow.setDisabled(true);
			}
			if (this.actionEditGridRow) {
				this.actionEditGridRow.setDisabled(true);
			}
		} else {
			if (this.actionDeleteGridRow) {
				this.actionDeleteGridRow.setDisabled(false);
			}
			if (this.actionEditGridRow) {
				if (selections.length===1) {
					this.actionEditGridRow.setDisabled(false);
				} else {
					this.actionEditGridRow.setDisabled(true);
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
	/*protected*/reload:com.trackplus.util.RefreshAfterSubmit.refreshGridAndTreeAfterSubmit,

	/**
	 * Get the node to reload after save after add operation
	 */
	/*protected*/getAddReloadParamsAfterSave: function(addLeaf) {
		if (this.selectedNode) {
			var leaf = this.selectedNode.data['leaf'];
			if (leaf) {
				//selected node is leaf: add to a leaf
				var parentNode = this.selectedNode.parentNode;
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
				//if (addLeaf) {
					return {nodeIDToReload: this.selectedNode.data['id']};
				/*} else {
					//add folder when a folder is selected
					var parentNode = this.selectedNode.parentNode;
					if (parentNode) {
						return {nodeIDToReload: parentNode.data['id']};
					}
				}*/
			}
		}
		return null;
	},

	/**
	 * Get the node to reload after save after edit operation
	 */
	/*protected*/getEditReloadParamsAfterSave: function(fromTree) {
		if (this.selectedNode) {
			if (fromTree) {
				//edited/copied from tree
				var parentNode = this.selectedNode.parentNode;
				if (parentNode) {
					//the parent of the edited node should be reloaded
					return {nodeIDToReload: parentNode.data['id']};
				}
			} else {
				//edited from grid:
				if (this.getShowGridForLeaf() && this.selectedNode.isLeaf()) {
					//in the tree a leaf node selected -> grid with a single row: the parent of the selected tree node should be reloaded
					var parentNode = this.selectedNode.parentNode;

					if (parentNode) {
						//the parent of the edited node should be reloaded
						return {nodeIDToReload: parentNode.data['id']};
					}
				} else {
					//in the tree the parent of the edited grid row is selected: the actually selected tree node should be reloaded
					return {nodeIDToReload: this.selectedNode.data['id']};
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
		return [{parameterName:'nodeIDToSelect', fieldNameFromResult:'node'},
				{parameterName:'rowToSelect', fieldNameFromResult:'node'}];
	},

	/**
	 * Get the node to select after save after edit operation
	 */
	/*private*/getEditReloadParamsAfterSaveFromResult: function(fromTree) {
		if (fromTree) {
			//edited from tree: select
			return [{parameterName:'nodeIDToSelect', fieldNameFromResult:'node'},
					{parameterName:'rowToSelect', fieldNameFromResult:'node'}];
		} else {
			//edited from grid: whatever the tree selection was it remains ()
			//specify rowToSelect to select the edited row in the grid based on the 'node' json field,
			if (this.getShowGridForLeaf() && this.selectedNode.isLeaf()) {
				//leaf node selected: select it again after reload
				return [{parameterName:'nodeIDToSelect', fieldNameFromResult:'node'},
				 {parameterName:'rowToSelect', fieldNameFromResult:'node'}];
			} else {
				//parent of the edited node selected: leave the parent node selected
				return {parameterName:'rowToSelect', fieldNameFromResult:'node'};
			}
		}
	},

	/**
	 * Handler for adding a folder node
	 */
	/*private*/onAddFolder: function() {
		var operation = "addFolder";
		var title = this.getTitle(this.getAddTitleKey(), {isLeaf:false, selectedRecord:this.getLastSelected(true)});
		var loadParams = this.getAddFolderParams();
		var submitParams = this.getAddFolderParams();
		var reloadParams = this.getAddReloadParamsAfterSave(false);
		var reloadParamsFromResult = this.getAddSelectionAfterSaveFromResult();
		var selectedRecord = this.getSingleSelectedRecord(true);
		if (CWHF.isNull(selectedRecord)) {
			selectedRecord = this.tree.getRootNode();
		}
		return this.onAddEdit(title, selectedRecord, operation, false, true, true,
				loadParams, submitParams, reloadParams, reloadParamsFromResult);
	},

	/**
	 * Handler for adding a leaf node
	 */
	/*private*/onAddLeaf: function() {
		var operation = "addLeaf";
		var title = this.getTitle(this.getAddTitleKey(), {isLeaf:true, selectedRecord:this.getLastSelected(true)});
		var loadParams = this.getAddLeafParams();
		var submitParams = this.getAddLeafParams();
		var reloadParams = this.getAddReloadParamsAfterSave(true);
		var reloadParamsFromResult = this.getAddSelectionAfterSaveFromResult();
		var selectedRecord = this.getSingleSelectedRecord(true);
		if (CWHF.isNull(selectedRecord)) {
			selectedRecord = this.tree.getRootNode();
		}
		return this.onAddEdit(title, selectedRecord, operation, true, true, true,
				loadParams, submitParams, reloadParams, reloadParamsFromResult);
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
		var isLeaf = this.selectedIsLeaf(fromTree);
		var title = this.getTitle(this.getEditTitleKey(), {isLeaf:isLeaf, fromTree:fromTree, selectedRecord:this.getLastSelected(fromTree)});
		var loadParams = this.getEditParams(fromTree);
		var submitParams = this.getEditParams(fromTree);
		var reloadParams = this.getEditReloadParamsAfterSave(fromTree);
		var reloadParamsFromResult = this.getEditReloadParamsAfterSaveFromResult(fromTree);
		var selectedRecord = this.getSingleSelectedRecord(fromTree);
		return this.onAddEdit(title, selectedRecord, operation, isLeaf, false, fromTree,
				loadParams, submitParams, reloadParams, reloadParamsFromResult);
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
			refreshParams, refreshParamsFromResult) {
		var recordData = null;
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
			submitButtonText:this.getSaveLabel(operation),
			submitHandler:this.submitHandler,
			submitAction:operation,
			refreshAfterSubmitHandler:this.reload,
			refreshParametersBeforeSubmit:refreshParams,
			refreshParametersAfterSubmit:refreshParamsFromResult
		};
		var postDataProcess = this.getEditPostDataProcess(record, isLeaf, add, fromTree, operation);
		var preSubmitProcess = this.getEditPreSubmitProcess(recordData, isLeaf, add);
		var items = this.getPanelItems(recordData, isLeaf, add, fromTree, operation);

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
		var windowConfig = Ext.create('com.trackplus.util.WindowConfig', windowParameters);
		windowConfig.showWindowByConfig(this);
	},

	/**
	 * The width of the edit window
	 * recordData: the record data (for the record to be edited or added to)
	 * isLeaf: whether add a leaf or a folder
	 * add: whether it is add or edit
	 * fromTree: operations started from tree or from grid
	 * operation:  the name of the operation
	 */
	/*protected*/getEditWidth: function(recordData, isLeaf, add, fromTree, operation) {
		if (isLeaf) {
			return this.editWidth;
		} else {
			return this.folderEditWidth;
		}
	},

	/**
	 * The height of the edit window
	 * recordData: the record data (for the record to be edited or added to)
	 * isLeaf: whether add a leaf or a folder
	 * add: whether it is add or edit
	 * fromTree: operations started from tree or from grid
	 * operation:  the name of the operation
	 */
	/*protected*/getEditHeight: function(recordData, isLeaf, add, fromTree, operation) {
		if (isLeaf) {
			return this.editHeight;
		} else {
			return this.folderEditHeight;
		}
	},

	/**
	 * Get the panel items
	 * recordData: the record data (for the record to be edited or added to)
	 * isLeaf: whether add a leaf or a folder
	 * add: whether it is add or edit
	 * fromTree: operations started from tree or from grid
	 * operation:  the name of the operation
	 */
	/*private*/getPanelItems: function(recordData, isLeaf, add, fromTree, operation) {
		if (isLeaf) {
			return this.getEditLeafPanelItems(recordData, add, fromTree, operation);
		} else {
			return this.getEditFolderPanelItems(recordData, add, fromTree, operation);
		}
	},

	/**
	 * Gets the items for a folder
	 * recordData: the record data (for the record to be edited or added to)
	 * add: whether it is add or edit
	 * operation:  the name of the operation
	 */
	/*protected abstract*/getEditFolderPanelItems: function(recordData, add, operation) {
		return [];
	},

	/**
	 * Gets the items for a leaf
	 * recordData: the record data (for the record to be edited or added to)
	 * add: whether it is add or edit
	 * operation:  the name of the operation
	 */
	/*protected abstract*/getEditLeafPanelItems: function(recordData, add, operation) {
		return [];
	},

	/**
	 * Add extra window configuration fields by add/edit
	 * windowConfiguration argument is configured with the required fields but
	 * 		any already specified windowConfig field can be overridden, and
	 * 		further optional window options can be specified
	 * type: typically "folder" or "leaf" but for more leaf types it can be customized
	 */
	/*protected abstract*/getExtraWindowParameters: function(recordData, operation) {
		//optionally specify
		/*
		return {fileUpload: ...
			windowConfig: ...
			panelConfig: ...}
		*/
	},

	/*private*/getEditPreSubmitProcess: function(recordData, isLeaf, add) {
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
	},

	/**
	 * Method to add extra request parameters be sent to the sever before submitting the folder data
	 */
	/*protected abstract*/getEditFolderPreSubmitProcess: null,

	/**
	 * Method to add extra request parameters be sent to the sever before submitting the leaf data
	 */
	/*protected abstract*/getEditLeafPreSubmitProcess: null,

	/**
	 * Cut a tree node
	 */
	onCutTreeNode: function() {
		this.cutCopyNode = this.selectedNode;
		this.copy = false;
	},

	/**
	 * Copy a tree node
	 */
	onCopyTreeNode: function() {
		this.cutCopyNode = this.selectedNode;
		this.copy = true;
	},

	/**
	 * Paste a node in the tree after copy/cut
	 */
	onPasteTreeNode: function() {
		this.onDropTreeNode(this.cutCopyNode, this.selectedNode, this.copy);
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
		var selectedRecords = this.getSelectedRecords(fromTree);
		if (selectedRecords) {
			var isLeaf = this.selectedIsLeaf(fromTree);
			var extraConfig = {fromTree:fromTree, isLeaf:isLeaf};
			this.deleteHandler(selectedRecords, extraConfig);
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
						if (this.getShowGridForLeaf() && this.selectedNode.isLeaf()) {
							//in the tree a leaf node selected -> grid with a single row: the parent of the selected tree node should be reloaded
							var parentNode = this.selectedNode.parentNode;
							if (parentNode) {
								//the parent of the edited node should be reloaded
								return {nodeIDToReload: parentNode.data['id']};
							}
						} else {
							//in the tree the parent of the edited grid row is selected: the actually selected tree node should be reloaded
							return {nodeIDToReload: this.selectedNode.data['id']};
						}
					}
				}
			}
		}
		return null;
	},
	
	/**
	 * Get the configuration for selection model
	 */
	/*protected*/getGridSelectionModel: function() {
		var selectionModelConfig = new Object();
		if (this.allowMultipleSelections) {
			selectionModelConfig.mode="MULTI";
		} else {
			selectionModelConfig.mode="SINGLE";
			if (this.allowDeselect) {
				selectionModelConfig.allowDeselect=this.allowDeselect;
			}
		}
		if (CWHF.isNull(this.gridSelectionModel)) {
			return Ext.create("Ext.selection.RowModel", selectionModelConfig);
		} else {
			return this.gridSelectionModel;
		}
	}
	
});
