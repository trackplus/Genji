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

Ext.define('com.trackplus.admin.customize.objectStatus.ObjectStatus',{
	extend:'com.trackplus.admin.TreeWithGrid',
	config: {
		rootID:'_'
	},
	confirmDeleteEntity:true,
	baseAction: "objectStatus",
	//showGridForLeaf:true,
	entityID:'node',
	listOptionWindowWidth: 500,
	listOptionWindowHeight: 150,
	//actions
	actionAdd: null,
	actionMoveUp: null,
	actionMoveDown: null,
	localizedLabels: null,

	constructor: function(config) {
		var config = config || {};
		this.initConfig(config);
		this.initBase();
	},

	/**
	 * Initialization method
	 */
	initBase: function() {
		this.initActions();
		this.initLocalizedLabels();
	},


	initLocalizedLabels: function() {
		Ext.Ajax.request({
			url: this.getBaseAction() + '!localizedLabels.action',
			scope: this,
			success: function(response) {
				var result = Ext.decode(response.responseText);
				this.localizedLabels = new Object();
				Ext.each(result.records, function(item) {
					this.localizedLabels[item.id] = item.label;
					}, this);
			},
			failure: function(response) {
				Ext.MessageBox.alert(this.failureTitle, response.responseText);
			}
			})
	},

	getEditWidth: function(recordData, isLeaf, add, fromTree, operation) {
		return this.listOptionWindowWidth;
	},

	getEditHeight: function(recordData, isLeaf, add, fromTree, operation) {
		return this.listOptionWindowHeight;
	},

	/**
	 * The message to appear first time after selecting this menu entry
	 * Is should be shown by selecting the root but the root is typically not visible
	 */
	getRootMessage: function(rootID) {
		return getText("menu.admin.custom.objectStatus.tt");
	},

	/**
	 * Get the title of the window in context
	 */
	getEntityLabel: function(extraConfig) {
		var tooltipKey = extraConfig.tooltipKey; //see crudBase getTitle
		var listForLabel = null;
		var fromTree = false;
		if (extraConfig) {
			fromTree = extraConfig.fromTree;
		}
		var selectedRecord = this.getLastSelected(fromTree);
		if (selectedRecord) {
			if (fromTree) {
				if (tooltipKey && tooltipKey===this.actionAdd.initialConfig.tooltipKey) {
					//for add and childAssignment get the label for children
					listForLabel = selectedRecord.data['listForLabel'];
				}
			} else {
				listForLabel = selectedRecord.data['listForLabel'];
			}
		}
		if (listForLabel) {
			return this.localizedLabels[listForLabel];
		}
		return "";
	},

	initActions: function() {
		this.actionAdd = this.createAction(this.getAddButtonKey(),
				this.getAddFolderIconCls(), this.onAdd, true, this.getAddTitleKey(), "add");
		this.actionEditGridRow = this.createAction(this.getEditButtonKey(),
				this.getEditIconCls(), this.onEditGridRow, true, this.getEditTitleKey(), "editGridRow");
		this.actionDeleteGridRow = this.createAction(this.getDeleteButtonKey(),
				this.getDeleteIconCls(), this.onDeleteFromGrid, true, this.getDeleteTitleKey(), "deleteGridRow");
		this.actionMoveUp = this.createAction("common.btn.up",
				"moveUp", this.onMoveUpGridRow, true, "common.lbl.up", "moveUp");
		this.actionMoveDown = this.createAction("common.btn.down",
				"moveDown", this.onMoveDownGridRow, true, "common.lbl.down", "moveDown");
	},

	/**
	 * Initialize all actions and return the toolbar actions
	 */
	getToolbarActions: function() {
		return [this.actionAdd, this.actionEditGridRow, this.actionDeleteGridRow, this.actionMoveUp, this.actionMoveDown];
	},

	/**
	 * Initialize all actions and return the toolbar actions
	 * this time called also by treeNodeSelect
	 */
	getToolbarActionChangesForTreeNodeSelect: function(selectedNode) {
		this.actionAdd.setDisabled(false);
		this.actionEditGridRow.setDisabled(true);
		this.actionDeleteGridRow.setDisabled(true);
		this.actionMoveUp.setDisabled(true);
		this.actionMoveDown.setDisabled(true);
	},

	onTreeNodeDblClick: function(view, record) {
		//nothing: do not try to edit a tree node
	},

	/**
	 * Gets the tree's fields
	 */
	getTreeFields: function() {
		return [{name : 'id', mapping : 'id', type: 'string'},
				{ name : 'text', mapping : 'text', type: 'string'},
				{ name : 'modifiable', mapping : 'modifiable', type: 'boolean'},
				{ name : 'listForLabel', mapping : 'listForLabel', type: 'int'},
				{ name : 'iconCls', mapping : 'iconCls', type: 'string'},
				{ name : 'leaf', mapping : 'leaf', type: 'boolean'}];
	},

	/**
	 * Whether drag and drop on tree is possible
	 */
	hasDragAndDropOnGrid: function(node) {
		return true;
	},

	getGridFields: function(node) {
		return [{name: 'id', 	type: 'int'},
			{name: 'label', type: 'string'},
			{name: 'modifiable', type: 'boolean'},
			{name: 'listForLabel',	type: 'int'},
			{name: 'typeflagLabel',	type: 'string'},
			{name: 'leaf',	type: 'boolean'},
			{name: 'node',	type: 'string'}];
	},

	getGridColumns: function(node) {
		return [{text: getText('common.lbl.name'),
			flex:1, dataIndex: 'label', sortable: false, hidden:false},
			{text: getText('admin.customize.list.lbl.typeflag'),
			width: 100, dataIndex: 'typeflagLabel', sortable: false, hidden:false
		}];
	},

	onGridDrop: function(node, data, dropRec, dropPosition) {
		var before = false;
		if (dropPosition==="before") {
			before = true;
		}
		var request={node:data.records[0].get('node'), droppedToNode:dropRec.get('node'), before:before};
		this.onOrderChange(this.getBaseAction() + "!droppedNear.action", request);
	},

	enableDisableToolbarButtons: function(view, arrSelections) {
		var selectedRecord = arrSelections[0];
		if (CWHF.isNull(selectedRecord)) {
			this.actionDeleteGridRow.setDisabled(true);
			this.actionEditGridRow.setDisabled(true);
			this.actionMoveUp.setDisabled(true);
			this.actionMoveDown.setDisabled(true);
		} else {
			var modifiable = selectedRecord.data['modifiable'];
			this.actionDeleteGridRow.setDisabled(!modifiable);
			this.actionEditGridRow.setDisabled(!modifiable);
			this.actionMoveDown.setDisabled(!modifiable);
			this.actionMoveUp.setDisabled(!modifiable);
			if (modifiable) {
				var store = this.grid.getStore();
				if (store) {
					this.actionMoveDown.setDisabled(selectedRecord===store.last());
					this.actionMoveUp.setDisabled(selectedRecord===store.first());
				}
			}
		}
	},

	/**
	* Prepare adding/editing a system or custom list entry
	*/
	getListOptionWindowItems: function(node, canEdit) {
		//dialog for adding/editing a list option
		var windowItems = [CWHF.createTextField(
				'common.lbl.name', "label", {disabled:!canEdit, allowBlank:false, labelWidth:150})];
		//type flag combo
		windowItems.push(CWHF.createCombo('admin.customize.list.lbl.typeflag',
				"typeflag", {itemId:"typeflagsList", disabled:!canEdit, labelWidth:150}));
		return windowItems;
	},

	/**
	 * Load the combos after the result has arrived containing also the combo data sources
	 */
	postDataLoadCombos: function(data, panel) {
		if (panel.items.items) {
			Ext.Array.forEach(panel.items.items,function(item) {
				//for each combo-type control set the value also:
				//for a combos itemId <xxx>sList the json result should contain
				//both <xxx>sList for the combo datasource and <xxx> for the combo's actual value
				//the json field for value is
				if (item.xtype === "combo") {
					var comboSource = data[item.itemId];
					item.store.loadData(comboSource);
					//-'Lists'
					item.setValue(data[item.itemId.substring(0,item.itemId.length-5)]);
				}
			});
		}
	},

	/**
	 * The method to process the data to be loaded arrived from the server
	 */
	getEditPostDataProcess: function(record, isLeaf, add, fromTree, operation) {
		return this.postDataLoadCombos;
	},

	/**
	 * Get the panel items
	 * recordData: the record data (for the record to be edited or added to)
	 * isLeaf: whether add a leaf or a folder
	 * add: whether it is add or edit
	 * fromTree: operations started from tree or from grid
	 * operation:  the name of the operation
	 */
	getPanelItems: function(recordData, isLeaf, add, fromTree, operation) {
		var nodeID = this.getRecordID(recordData, {fromTree:fromTree});
		var canEdit = false;
		if (operation==='add' || operation==='copy') {
			canEdit = true;
		} else {
			if (operation==='edit') {
				canEdit=recordData['modifiable'];
			}
		}
		return this.getListOptionWindowItems(nodeID, canEdit);
	},

	/**
	 * Handler for adding a node (folder or leaf)
	 */
	onAdd: function() {
		var operation = "add";
		var title = this.getTitle(this.getAddTitleKey(), {fromTree:true});
		var loadParams = this.getAddLeafParams();
		var submitParams = this.getAddLeafParams();
		var reloadParams = this.getAddReloadParamsAfterSave(true);
		var reloadParamsFromResult = this.getAddSelectionAfterSaveFromResult();
		var selectedRecord = this.getSingleSelectedRecord(true);
		return this.onAddEdit(title, selectedRecord, operation, true, true, true,
				loadParams, submitParams, reloadParams, reloadParamsFromResult);
	},

	/**
	 * Parameters for adding a new entry to a leaf:
	 * add a child to a leaf node: exclusively by extending custom selects:
	 * simple -> parent child or parent child -> parent child grandchild
	 * without explicitly asking for the exact type of the composite select
	 */
	/*protected*/getAddLeafParams: function() {
		//false as it would be added to a folder to send the leaf's id, not the parent folder's id
		return this.getAddParams(false);
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
			var nodeID = gridRow.data.node;
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
				com.trackplus.util.RefreshAfterSubmit.refreshGridAndTreeAfterSubmit.call(this, {rowToSelect:responseJson.node});
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
	 * Reload after a change operation
	 */
	reload:com.trackplus.util.RefreshAfterSubmit.refreshGridAfterSubmit,

	/**
	 * Parameters for reloading after a delete operation
	 * By delete the reload and select parameters are known before
	 */
	/*protected abstract*/getReloadParamsAfterDelete: function(selectedRecords, extraConfig, responseJson) {
		return null;
	},

	/**
	 * Get the itemId of those actions whose context menu text or
	 * toolbar button tooltip should be changed according to the current selection
	 */
	getActionItemIdsWithContextDependentLabel: function() {
		return ["add", "editGridRow", "deleteGridRow", "moveUp", "moveDown"];
	},

	/**
	 * Get the actions available in context menu depending on the currently selected row
	 *
	 */
	getTreeContextMenuActions: function(selectedRecord, selectionIsSimple) {
		var modifiable=selectedRecord.data['modifiable'];
		var actions = [];
		if (modifiable) {
			actions.push(this.actionAdd);
		}
		return actions;
	},

	/**
	 * Get the actions available in context menu depending on the currently selected row
	 *
	 */
	getGridContextMenuActions: function(selectedRecord, selectionIsSimple) {
		var modifiable=selectedRecord.data['modifiable'];
		var actions = [];
		if (modifiable) {
			actions.push(this.actionEditGridRow);
			actions.push(this.actionDeleteGridRow);
			var store = this.grid.getStore();
			if (store) {
				if (selectedRecord!==store.first()) {
					actions.push(this.actionMoveUp);
				}
				if (selectedRecord!==store.last()) {
					actions.push(this.actionMoveDown);
				}
			}
		}
		return actions;
	},

	onGridRowDblClick: function(view, record) {
		var modifiable = record.data['modifiable'];
		if (modifiable) {
			this.onEditGridRow();
		}
	}
});
