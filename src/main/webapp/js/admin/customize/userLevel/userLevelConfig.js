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

Ext.define("com.trackplus.admin.customize.userLevel.userLevelConfig",{
	extend:"com.trackplus.admin.TreeWithGrid",
	config: {
		rootID : ""
	},
	confirmDeleteEntity:true,
	gridHasItemdblclick: false,
	baseAction:"userLevel",
	entityID:"node",
	editWidth:400,
	editHeight:200,
	/**
	 * The message to appear first time after selecting this menu entry
	 * Is should be shown by selecting the root but the root is typically not visible
	 */
	getRootMessage: function() {
		return getText("admin.customize.userLevel.lbl.description");
	},

	/**
	 * The localized entity name
	 */
	getEntityLabel: function(extraConfig) {
		return getText("admin.customize.userLevel.lbl.userLevel");
	},

	initActions: function() {
		this.actionAdd = this.createAction(this.getAddButtonKey(),
				this.getAddIconCls(), this.onAddLeaf, false, this.getAddTitleKey());
		this.actionEdit = this.createAction(this.getEditButtonKey(),
				this.getEditIconCls(), this.onEditTreeNode, true,
		            this.getEditTitleKey());
		this.actionDelete = this.createAction(this.getDeleteButtonKey(),
				this.getDeleteIconCls(), this.onDeleteFromTree, true, this.getDeleteTitleKey());
	    this.actions = [this.actionAdd, this.actionEdit, this.actionDelete];
	},

	/**
	 * Initialize all actions and return the toolbar actions
	 */
	getToolbarActions: function() {
		var sys = com.trackplus.TrackplusConfig.user.sys;
	    if (sys) {
	    	return [this.actionAdd, this.actionEdit, this.actionDelete];
	    } else {
	    	return [];
	    }
	},

	onTreeNodeDblClick: function(view, record) {
		var sys = com.trackplus.TrackplusConfig.user.sys;
	    if (sys) {
	        return this.callParent(arguments);
	    } else {
	        return [];
	    }
	},

	/**
	 * Gets the tree's fields
	 */
	getTreeFields: function() {
		return [{name: 'id', type: 'string'},
				{name: 'text', type: 'string'},
				{name: 'iconCls', type: 'string'},
				{name: 'leaf', type: 'boolean'}];
	},
	/**
	 * Gets the grid store's fields for the selected node
	 */
	getGridFields: function(node) {
		return [{name: 'id',	type: 'string'},
				{name: 'path', type: 'string'},
				{name: 'name',	type: 'string'},
				{name: 'selected',	type: 'boolean'}];
	},
	/**
	 * Gets the grid columns for the selected node
	 */
	getGridColumns: function(node) {
		return [{header: getText('admin.customize.userLevel.lbl.path'),
					flex:1, dataIndex: 'path', sortable: true},
				{header: getText('common.lbl.name'),
						flex:1, dataIndex: 'name', sortable: true},
				{header: getText('admin.customize.userLevel.lbl.selected'),
						flex:1, dataIndex: 'selected', sortable: true,  xtype: "checkcolumn",
						listeners: {"checkchange": {fn: this.onCheckChange, scope:this},
									"beforecheckchange": {fn: this.onBeforeCheckChange, scope:this}}}];
	},

	onCheckChange: function(checkBox, rowIndex, checked, eOpts) {
		var record = this.grid.getStore().getAt(rowIndex);
		if (record!=null) {
			var params = {actionID:record.data["id"], node:this.selectedNodeID, checked:checked};
			Ext.Ajax.request({
				url: this.baseAction + "!changeAction.action",
				params: params,
				scope: this,
				success: function(response) {
					var result = Ext.decode(response.responseText);
					if (result.success) {

					} else {
						com.trackplus.util.showError(result);
					}
				},
				failure: function(response) {
					Ext.MessageBox.alert(this.failureTitle, response.responseText);
				}
			});
		}
	},

	onBeforeCheckChange: function(checkBox, rowIndex, checked, eOpts) {
		var sys = com.trackplus.TrackplusConfig.user.sys;
	    if (sys) {
	    	return true;
	    } else {
	    	return false;
	    }
	},

	getGridViewConfig: function() {
		return {
			forceFit: true,
			markDirty: false
		};
	},

	/**
	* Prepare adding/editing a report
	*/
	getEditLeafPanelItems: function() {
		return [CWHF.createTextField("common.lbl.name",
					"name", {labelWidth:100}),
				CWHF.createTextAreaField('common.lbl.description',
					"description", {labelWidth:100})];
	},


	/**
	 * Which actions to enable/disable depending on tree selection
	 */
	getToolbarActionChangesForTreeNodeSelect: function(selectedNode) {
		this.actionEdit.setDisabled(false);
		this.actionDelete.setDisabled(false);
	},

	getAddReloadParamsAfterSave: function(addLeaf) {
		return {nodeIDToReload: this.rootID};
	},

	reload:com.trackplus.util.RefreshAfterSubmit.refreshSimpleTree,

	getReloadParamsAfterDelete: function(selectedRecords, extraConfig, responseJson) {
		return {reloadTree:true}
	},

	/**
	 * Get the node to select after save after add operation
	 */
	getAddSelectionAfterSaveFromResult: function() {
		//do not specify rowToSelect, do not select anything in the grid after add
		return [{parameterName:"node", fieldNameFromResult:"node"},
		        {parameterName:"reloadTree", fieldNameFromResult:"reloadTree"}];
	},

	/**
	 * Get the node to select after save after edit operation
	 */
	getEditReloadParamsAfterSaveFromResult: function(fromTree) {
		return [{parameterName:"node", fieldNameFromResult:"node"},
		        {parameterName:"reloadTree", fieldNameFromResult:"reloadTree"}];
	}

});
