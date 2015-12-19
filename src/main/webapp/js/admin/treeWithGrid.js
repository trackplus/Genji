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
Ext.define("com.trackplus.admin.TreeWithGrid",{
	extend:"com.trackplus.admin.TreeBase",
	/*config: {
		rootID : '_',
	},*/
	allowMultipleSelections:false,
	
	/**
	 * Whether the grid has context menu listener
	 */
	gridHasItemcontextmenu: true,
	/**
	 * Whether the grid has double click listener
	 */
	gridHasItemdblclick: true,

	/**
	 * Whether to use drag and drop inside the grid
	 */
	dragAndDropOnGrid: false,
	
	useCopyPaste: false,
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
	/*protected*/initActions: function() {
		var addFolderText = this.getActionTooltip(this.getAddTitleKey(), {isLeaf: false});
		this.actionAddFolder = CWHF.createAction(addFolderText, this.getAddFolderIconCls(),
				"onAddFolder", {labelIsLocalized:true});
		var addLeafText = this.getActionTooltip(this.getAddTitleKey(), {isLeaf: true});
		this.actionAddLeaf = CWHF.createAction(addLeafText, this.getAddIconCls(),
		   		"onAddLeaf", {labelIsLocalized:true, disabled:true});
		this.actionEditGridRow = CWHF.createContextAction(this.getEditButtonKey(), this.getEditIconCls(),
		    		"onEditGridRow", this.getEditTitleKey(), {itemId:"editGridRow", disabled:true});
		this.actionEditTreeNode = CWHF.createContextAction(this.getEditButtonKey(), this.getEditIconCls(),
	    		"onEditTreeNode", this.getEditTitleKey(), {itemId:"editTreeNode"});
		this.actionDeleteGridRow = CWHF.createContextAction(this.getDeleteButtonKey(), this.getDeleteIconCls(),
	    		"onDeleteFromGrid", this.getDeleteTitleKey(), {itemId:"deleteGridRow", disabled:true});
		this.actionDeleteTreeNode = CWHF.createContextAction(this.getDeleteButtonKey(), this.getDeleteIconCls(),
	    		"onDeleteFromTree", this.getDeleteTitleKey(), {itemId:"deleteTreeNode"});
		if (this.useCopyPaste) {
			this.actionCutTreeNode = CWHF.createContextAction(this.getCutButtonKey(), this.getCutIconCls(),
		    		"onCutTreeNode", this.getCutTitleKey());
			this.actionCopyTreeNode = CWHF.createContextAction(this.getCopyButtonKey(), this.getCopyIconCls(),
		    		"onCopyTreeNode", this.getCopyTitleKey());
			this.actionPasteTreeNode = CWHF.createAction(this.getPasteButtonKey(), this.getPasteIconCls(),
		    		"onPasteTreeNode", this.getPasteTitleKey());
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
	 * Which actions to enable/disable depending on tree selection
	 */
	/*protected*/getToolbarActionChangesForTreeNodeSelect: function(selectedNode) {
		this.actionAddFolder.setDisabled(false);
		this.actionAddLeaf.setDisabled(false);
		//nothing selected in the grid
		this.actionEditGridRow.setDisabled(true);
		this.actionDeleteGridRow.setDisabled(true);
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
	 * Get the extra parameters for the gridStore
	 */
	/*protected*/getGridStoreExtraParams: function(node, opts) {
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
				extraParams: this.getGridStoreExtraParams(node, opts),
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
		var gridListeners = {selectionchange: "onGridSelectionChange"};
		if (this.gridHasItemcontextmenu) {
			gridListeners.itemcontextmenu = "onGridRowCtxMenu";
		}
		if (this.gridHasItemdblclick) {
			gridListeners.itemdblclick = "onGridRowDblClick"
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
					dragGroup: this.getBaseServerAction() + "gridDDGroup",
					dropGroup: this.getBaseServerAction() + "gridDDGroup",
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
	},

	/**
	 * Get the panel items
	 * recordData: the record data (for the record to be edited or added to)
	 * isLeaf: whether add a leaf or a folder
	 * add: whether it is add or edit
	 * fromTree: operations started from tree or from grid
	 * operation:  the name of the operation
	 */
	/*private*//*getPanelItems: function(recordData, isLeaf, add, fromTree, operation) {
		if (isLeaf) {
			return this.getEditLeafPanelItems(recordData, add, fromTree, operation);
		} else {
			return this.getEditFolderPanelItems(recordData, add, fromTree, operation);
		}
	},*/

	/**
	 * Gets the items for a folder
	 * recordData: the record data (for the record to be edited or added to)
	 * add: whether it is add or edit
	 * operation:  the name of the operation
	 */
	/*protected abstract*//*getEditFolderPanelItems: function(recordData, add, operation) {
		return [];
	},*/

	/**
	 * Gets the items for a leaf
	 * recordData: the record data (for the record to be edited or added to)
	 * add: whether it is add or edit
	 * operation:  the name of the operation
	 */
	/*protected abstract*//*getEditLeafPanelItems: function(recordData, add, operation) {
		return [];
	},*/

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
