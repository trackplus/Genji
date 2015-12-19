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
Ext.define("com.trackplus.admin.user.Department",{
	extend:"com.trackplus.admin.TreeDetailAssignment",
	xtype: "department",
    controller: "department",
	config: {
		rootID: '_'
	},
	
	getBaseServerAction: function() {
		return "department";
	},
	
	/**
	 * Whether the tree has double click listener
	 */
	treeHasItemdblclick: true,
	dragAndDropOnTree: true,
	/**
	 * actions
	 */
	actionAddDepartment: null,
	actionAddSubdepartment: null,
	actionEditDepartment: null,
	actionDeleteDepartment: null,
	actionDetachFromParentDepartment: null,
	actionRemovePerson: null,

	getEntityLabel: function(extraConfig) {
		return getText("admin.user.department.lbl.department");
	},

	getAddIconCls: function() {
		return "departmentAdd";
	},

	getEditIconCls: function() {
		return "departmentEdit";
	},

	/**
	 * Add "Form" not "Form assignment" (getEntityLabel() does not fit here)
	 */
	getAddSubdepartmentLabel: function() {
		return getText(this.getAddTitleKey(), getText("admin.user.department.lbl.subdepartment"));
	},

	/**
	 * Initialize all possible actions
	 */
	initActions: function() {
		this.actionAddDepartment = CWHF.createAction(this.getActionTooltip(this.getAddTitleKey()), "departmentAdd",
				"onAddMainDepartment", {labelIsLocalized:true});
		this.actionAddSubdepartment = CWHF.createAction(this.getAddSubdepartmentLabel(), this.getAddIconCls(),
				"onAddSubdepartment", {labelIsLocalized:true, disabled: true});
		this.actionEditDepartment = CWHF.createAction(this.getActionTooltip(this.getEditTitleKey()), this.getEditIconCls(),
				"onEditDepartment", {labelIsLocalized:true});
		this.actionDeleteDepartment = CWHF.createAction(this.getActionTooltip(this.getDeleteTitleKey()), this.getDeleteIconCls(),
				"onDeleteDepartment", {labelIsLocalized:true});
		this.actionDetachFromParentDepartment = CWHF.createAction("common.btn.detachFromParent", "clear",
				"onDetachFromParentDepartment");
		this.actionRemovePerson = CWHF.createAction(getText(this.getRemoveTitleKey(), getText("admin.user.lbl.user")), this.getDeleteIconCls(),
				"onRemovePerson", {labelIsLocalized:true});
		this.actions=[this.actionAddDepartment, this.actionAddSubdepartment];
	},

	/**
	 * Context menu for tree
	 */
	getTreeContextMenuActions: function(selectedRecord, selectionIsSimple) {
		if (selectedRecord.isLeaf()) {
			return [this.actionRemovePerson];
		} else {
			var actions = [this.actionAddSubdepartment, this.actionEditDepartment, this.actionDeleteDepartment];
			if (selectedRecord && selectedRecord.parentNode && !selectedRecord.parentNode.isRoot()) {
				actions.push(this.actionDetachFromParentDepartment);
			}
			return actions;
		}
	},

	/**
	 * Which actions to enable/disable depending on tree selection
	 */
	getToolbarActionChangesForTreeNodeSelect: function(selectedNode) {
		this.actionAddSubdepartment.setDisabled(false);
	},

	/**
	 * Return false if dragging this node is not allowed
	 */
	canDragDropNode: function(nodeToDrag, copy, overModel) {
		return !nodeToDrag.isAncestor(overModel);
	},

	/**
	 * The message to appear first time after selecting this menu entry
	 * Is should be shown by selecting the root but the root is typically not visible
	 */
	getRootMessage: function() {
		return getText("menu.admin.users.departments.tt");
	},

	getTreeStoreExtraParams: function() {
		//also include the persons in department by expanding a department node
		return {includePersons:true};
	},

	getGridFields: function(record) {
		return [{name:'id', type:'int'},
				{name:'userName', type:'string'},
				{name:'name', type:'string'},
				{name:'active', type:'boolean'},
				{name:'activeLabel', type:'string'},
				{name:'employeeId', type:'string'},
				{name:'department', type:'string'}];
	},

	getColumnModel: function() {
		return [{text:getText('admin.user.profile.lbl.userName'),
			flex:2, dataIndex:'userName', sortable:true,
			renderer:this.renderer,
			filter: {
	            type: "string"
	        }
		}, {
			text:getText('common.lbl.name'),
			flex:2, dataIndex:'name', sortable:true,
			renderer:this.renderer,
			filter: {
	            type: "string"
	        }
		}, {
			text:getText('admin.user.profile.lbl.department'),
			flex:2, dataIndex:'department', sortable:true,
			renderer:this.renderer,
			filter: {
	            type: "string"
	        }
		}, {
			text:getText('admin.user.profile.lbl.employeeId'),
			flex:1, dataIndex:'employeeId', sortable:true,
			renderer:this.renderer, hidden: true,
			filter: {
	            type: "string"
	        }
		}, {
			text:getText('admin.user.manage.lbl.activ'),
			flex:1, dataIndex:'activeLabel', sortable:true,
			renderer:this.renderer, hidden: true,
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
	}
});
