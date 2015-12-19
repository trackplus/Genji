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

Ext.define("com.trackplus.admin.customize.userLevel.UserLevel",{
	extend:"com.trackplus.admin.TreeWithGrid",
	xtype: "userLevel",
    controller: "userLevel",
    config: {
		rootID : "0"
	},
	
	gridHasItemdblclick: false,
	baseServerAction:"userLevel",
	
	initComponent : function() {
		this.treeStoreUrl = "userLevel!expand.action";
		this.callParent();
	},
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
		this.actionAdd = CWHF.createAction(this.getAddButtonKey(), this.getAddIconCls(),
				"onAddLeaf", {tooltip:this.getActionTooltip(this.getAddTitleKey())});
		this.actionEdit = CWHF.createAction(this.getEditButtonKey(), this.getEditIconCls(),
				"onEditTreeNode", {tooltip:this.getActionTooltip(this.getEditTitleKey()), disabled:true});
		this.actionDelete = CWHF.createAction(this.getDeleteButtonKey(), this.getDeleteIconCls(), 
				"onDeleteFromTree", {tooltip:this.getActionTooltip(this.getDeleteTitleKey()), disabled:true});
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
		return [{text: getText('admin.customize.userLevel.lbl.path'),
					flex:1, dataIndex: 'path', sortable: true},
				{text: getText('common.lbl.name'),
						flex:1, dataIndex: 'name', sortable: true},
				{text: getText('admin.customize.userLevel.lbl.selected'),
						flex:1, dataIndex: 'selected', sortable: true,  xtype: "checkcolumn",
						listeners: {"checkchange": "onCheckChange"},
									"beforecheckchange": "onBeforeCheckChange"}];
	},

	getGridViewConfig: function() {
		return {
			forceFit: true,
			markDirty: false
		};
	},

	/**
	 * Which actions to enable/disable depending on tree selection
	 */
	getToolbarActionChangesForTreeNodeSelect: function(selectedNode) {
		this.actionEdit.setDisabled(false);
		this.actionDelete.setDisabled(false);
	}

});
