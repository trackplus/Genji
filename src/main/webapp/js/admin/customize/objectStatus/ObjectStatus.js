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

Ext.define("com.trackplus.admin.customize.objectStatus.ObjectStatus",{
	extend:"com.trackplus.admin.TreeWithGrid",
	xtype: "objectStatus",
    controller: "objectStatus",
	config: {
		rootID:"_"
	},
	baseServerAction: "objectStatus",
	allowDeselect: true,
	
	//actions
	actionAdd: null,
	actionMoveUp: null,
	actionMoveDown: null,
	localizedLabels: null,
	
	initComponent : function() {
		this.treeStoreUrl = "objectStatus!expand.action";
		this.callParent();
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
			url: "objectStatus!localizedLabels.action",
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
		var tooltipKey = extraConfig.tooltipKey;
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
					listForLabel = selectedRecord.data["listForLabel"];
				}
			} else {
				listForLabel = selectedRecord.data["listForLabel"];
			}
		}
		if (listForLabel) {
			return this.localizedLabels[listForLabel];
		}
		return "";
	},

	initActions: function() {
		this.actionAdd = CWHF.createContextAction(this.getAddButtonKey(), this.getAddIconCls(),
				"onAdd", this.getAddTitleKey(), {disabled:true, itemId:"add"});
		this.actionEditGridRow = CWHF.createContextAction(this.getEditButtonKey(), this.getEditIconCls(),
				"onEditGridRow", this.getEditTitleKey(), {disabled:true, itemId:"editGridRow"});
		 this.actionDeleteGridRow = CWHF.createContextAction(this.getDeleteButtonKey(), this.getDeleteIconCls(),
 				"onDeleteFromGrid", this.getDeleteTitleKey(), {disabled:true, itemId:"deleteGridRow"});
		 this.actionMoveUp = CWHF.createContextAction(this.getMoveUpButtonKey(), this.getMoveUpIconCls(),
         		"onMoveUpGridRow", this.getMoveUpTitleKey(), {itemId:"moveUp", disabled:true});
		 this.actionMoveDown = CWHF.createContextAction(this.getMoveDownButtonKey(), this.getMoveDownIconCls(),
         		"onMoveDownGridRow", this.getMoveDownTitleKey(), {itemId:"moveDown", disabled:true});
		 this.actions = [this.actionAdd, this.actionEditGridRow, this.actionDeleteGridRow, this.actionMoveUp, this.actionMoveDown];
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

	/**
	 * Gets the tree"s fields
	 */
	getTreeFields: function() {
		return [{name : "id", mapping : "id", type: "string"},
				{ name : "text", mapping : "text", type: "string"},
				{ name : "modifiable", mapping : "modifiable", type: "boolean"},
				{ name : "listForLabel", mapping : "listForLabel", type: "int"},
				{ name : "iconCls", mapping : "iconCls", type: "string"},
				{ name : "leaf", mapping : "leaf", type: "boolean"}];
	},

	/**
	 * Whether drag and drop on tree is possible
	 */
	hasDragAndDropOnGrid: function(node) {
		return true;
	},

	getGridFields: function(node) {
		return [{name: "id", 	type: "int"},
			{name: "label", type: "string"},
			{name: "modifiable", type: "boolean"},
			{name: "listForLabel",	type: "int"},
			{name: "typeflagLabel",	type: "string"},
			{name: "leaf",	type: "boolean"},
			{name: "node",	type: "string"}];
	},

	getGridColumns: function(node) {
		return [{text: getText("common.lbl.name"),
			flex:1, dataIndex: "label", sortable: false, hidden:false},
			{text: getText("admin.customize.list.lbl.typeflag"),
			width: 100, dataIndex: "typeflagLabel", sortable: false, hidden:false
		}];
	},

	onGridDrop: function(node, data, dropRec, dropPosition) {
		var before = false;
		if (dropPosition==="before") {
			before = true;
		}
		var request={node:data.records[0].get("node"), droppedToNode:dropRec.get("node"), before:before};
		this.onOrderChange("objectStatus!droppedNear.action", request);
	},

	/**
	 * Get the actions available in context menu depending on the currently selected row
	 *
	 */
	getTreeContextMenuActions: function(selectedRecord, selectionIsSimple) {
		var modifiable=selectedRecord.data["modifiable"];
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
		var modifiable=selectedRecord.data["modifiable"];
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
	}
});
