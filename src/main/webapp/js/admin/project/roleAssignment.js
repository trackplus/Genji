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
Ext.define("com.trackplus.admin.project.RoleAssignment",{
	extend:"com.trackplus.admin.TreeDetailAssignment",
	xtype: "roleAssignment",
    controller: "roleAssignment",
	config: {
		//should be set to the projectID
		rootID: null
	},
	baseServerAction:"roleAssignments",

	treeStoreUrl: "roleAssignments!expand.action",
	/**
	 * actions
	 */
	actionRemovePerson: null,

	getEntityLabel: function(extraConfig) {
		var isGroup = false;
		if (extraConfig && extraConfig.selectedRecord) {
			isGroup = extraConfig.selectedRecord.data["isGroup"];
		}
		if (isGroup) {
			return getText("admin.user.group.lbl.group");
		} else {
			return getText("admin.user.lbl.user");
		}
	},

	/**
	 * Initialize all possible actions
	 */
	initActions: function() {
		this.actionRemovePerson = CWHF.createContextAction(this.getRemoveButtonKey(), this.getDeleteIconCls(),
				"onRemovePerson", this.getRemoveTitleKey(), {itemId: "removePerson", labelIsLocalized:true});
	},

	/**
	 * Context menu for tree
	 */
	getTreeContextMenuActions: function(selectedRecord, selectionIsSimple) {
		if (selectedRecord.isLeaf()) {
			return [this.actionRemovePerson];
		} else {
			return [];
		}
	},

	/*getActionItemIdsWithContextDependentLabel: function() {
		return ["removePerson"];
	},*/

	/**
	 * The message to appear first time after selecting this menu entry
	 * Is should be shown by selecting the root but the root is typically not visible
	 */
	getRootMessage: function() {
		return getText("admin.project.roleAssignment.lbl.infoGeneral");
	},

	/*onRemovePerson: function() {
		this.reloadAssigned(this.getBaseAction()+"!unassign.action", {});
	},*/

	/**
	 * Gets the tree's fields
	 */
	getTreeFields: function() {
		return [{name : "id", mapping : "id", type: "string"},
				{ name : "text", mapping : "text", type: "string"},
				{ name : "leaf", mapping : "leaf", type: "boolean"},
				{ name : "group", mapping : "group", type: "boolean"},
				{ name : "iconCls", mapping : "iconCls", type: "string"}];
	},

	getGridFields: function(record) {
		return [{name:"id", type:"int"},
				{name:"name", type:"string"},
				{name:"userName", type:"string"},
				{name:"active", type:"boolean"},
				{name:"activeLabel", type:"string"},
				{name:"group", type:"boolean"},
				{name:"groupLabel", type:"string"},
				{name:"employeeId", type:"string"},
				{name:"department", type:"string"}];
	},

	getColumnModel: function() {
		return [{
			text:getText("common.lbl.name"),
			flex:2, dataIndex:"name", sortable:true,groupable:false,
			renderer:this.renderer,
			filter: {
	            type: "string"
	        }
		}, {
			text:getText("admin.user.profile.lbl.userName"),
			flex:1, dataIndex:'userName', sortable:true,groupable:false,
			renderer:this.renderer,
			filter: {
	            type: "string"
	        }
		}, {
			text:getText("admin.user.profile.lbl.department"),
			flex:2, dataIndex:'department', sortable:true,groupable:true,
			renderer:this.renderer,
			filter: {
	            type: "string"
	        }
		}, {
			text:getText("admin.project.roleAssignment.lbl.group"),
			flex:1, dataIndex:'groupLabel', sortable:true, groupable:true,
			renderer:this.renderer,
			filter: {
				type: "list",
				options: [getText("common.boolean.Y"),
							getText("common.boolean.N")]
			}
		}, {
			text:getText("admin.user.profile.lbl.employeeId"),
			flex:1, dataIndex:"employeeId", sortable:true,groupable:false,
			renderer:this.renderer, hidden:true,
			filter: {
	            type: "string"
	        }
		},{
			text: getText("admin.user.manage.lbl.activ"),
			flex:1, dataIndex: 'activeLabel', sortable:true,groupable:true,
			renderer:this.renderer, hidden:true,
			filter: {
				type: "list",
				options: [getText("common.boolean.Y"),
							getText("common.boolean.N")]
			}
		}];
	},

	/**
	 * Render the inherited rows as grey
	 */
	renderer: function(value, metadata, record) {
		if (!record.data["active"]) {
			metadata.style = 'color:#909090';
		}
		return value;
	},

	hideGridHeaders: function() {
		return false;
	},

	enableColumnHide: function() {
		return true;
	},

	getGridFeatures: function() {
		var groupingFeature = Ext.create('Ext.grid.feature.Grouping',{
			groupHeaderTpl: '{name} ({rows.length} Item{[values.rows.length > 1 ? "s" : ""]})'
		});
		return features = [groupingFeature];
	},
	
	getGroupingFeature:function(features){
	  return features[0];
	}

	/*getDetailWidth: function() {
		return 600;
	},

	getDetailHeight: function() {
		return 600;
	},

	getMinWidth: function() {
		return 425;
	}*/
});
