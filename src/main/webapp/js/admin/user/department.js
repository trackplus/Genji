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
Ext.define('com.trackplus.admin.user.Department',{
	extend:'com.trackplus.admin.TreeDetailAssignment',
	config: {
		rootID: '_'
	},
	baseAction:"department",
	editWidth: 400,
	editHeight: 150,
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

	constructor: function(cfg) {
		var config = cfg || {};
		this.initConfig(config);
		this.initBase();
	},

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
	initActions: function(){
		this.actionAddDepartment = this.createLocalizedAction(this.getTitle(this.getAddTitleKey(), {isLeaf:false}),
				this.getAddIconCls(), this.onAddMainDepartment, this.getTitle(this.getAddTitleKey(), {isLeaf:false}));
		this.actionAddSubdepartment = this.createLocalizedAction(this.getAddSubdepartmentLabel(),
				this.getAddIconCls(), this.onAddSubdepartment, this.getAddSubdepartmentLabel());
		this.actionEditDepartment = this.createLocalizedAction(this.getTitle(this.getEditTitleKey(), {isLeaf:false}),
				this.getEditIconCls(), this.onEditDepartment, this.getTitle(this.getEditTitleKey()));
		this.actionDeleteDepartment = this.createLocalizedAction(this.getTitle(this.getDeleteTitleKey(), {isLeaf:false}),
				this.getDeleteIconCls(), this.onDeleteDepartment, this.getTitle(this.getDeleteTitleKey(), {isLeaf:false}));
		this.actionDetachFromParentDepartment = this.createLocalizedAction(getText("menu.admin.users.departments.removeFromParent"),
				"clear", this.onDetachFromParentDepartment, getText("menu.admin.users.departments.removeFromParent"));
		this.actionRemovePerson = this.createLocalizedAction(getText("common.lbl.remove", getText("admin.user.lbl.user")),
				this.getDeleteIconCls(), this.onRemovePerson, getText("common.lbl.remove", getText("admin.user.lbl.user")));
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
	 * Create the edit
	 */
	onTreeNodeDblClick: function(view, record) {
		var leaf = record.isLeaf();
		if (!leaf) {
			this.onEditDepartment();
		}
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

	/**
	 * Handler for adding a new screen
	 */
	onAddMainDepartment: function() {
		return this.onAddDepartment(false);
	},

	onAddSubdepartment: function() {
		return this.onAddDepartment(true);
	},

	onAddDepartment: function(addAsSubdepartment) {
		var title = this.getTitle(this.getAddTitleKey(), {isLeaf:false});
		var loadParams = this.getEditParams();
		loadParams["add"] = true;
		var submitParams = this.getEditParams();
		submitParams["addAsSubdepartment"] = addAsSubdepartment;
		submitParams["add"] = true;
		var nodeIDToReload;
		if (addAsSubdepartment) {
			nodeIDToReload = this.selectedNodeID;
		} else {
			nodeIDToReload = this.getRootID();
		}
		return this.onAddEdit(title, loadParams, submitParams, nodeIDToReload);
	},

	/**
	 * Handler for editing a screen
	 */
	onEditDepartment: function() {
		var title = this.getTitle(this.getEditTitleKey(), {isLeaf:false});
		var loadParams = this.getEditParams();
		var submitParams = this.getEditParams();
		return this.onAddEdit(title, loadParams, submitParams, this.getParentNodeId());
	},

	/**
	 * Parameters for editing an existing entity
	 * recordData: the selected entity data
	 */
	getEditParams: function() {
		var recordData = this.getSingleSelectedRecordData(true);
		if (recordData) {
			var nodeID = recordData['id'];
			if (nodeID) {
				return {node:nodeID};
			}
		}
		return {};
	},

	getTreeExpandExtraParams: function() {
		//also include the persons in department by expanding a department node
		return {includePersons:true};
	},

	/**
	 * Get the node to reload after save after edit operation
	 */
	getParentNodeId: function() {
		if (this.selectedNode) {
				//edited/copied from tree
				var parentNode = this.selectedNode.parentNode;
				if (parentNode) {
					//the parent of the edited node should be reloaded
					return {nodeIDToReload: parentNode.data['id']}
				}
		}
		return {nodeIDToReload: this.getRootID()};
	},

	/**
	 * Handler for add/edit a node/row
	 * title: 'add'/'edit'/'copy'
	 * recordData: the selected record (tree node data or grid row data)
	 * isLeaf: whether to add a leaf or a folder
	 * add: whether it is add or edit
	 * fromTree: operations started from tree or from grid
	 * loadParams
	 * submitParams
	 * refreshParams
	 * refreshParamsFromResult
	 */
	onAddEdit: function(title, loadParams, submitParams, nodeIDToReload) {
		var loadUrl = this.getBaseAction() + '!edit.action';
		var load = {loadUrl:loadUrl, loadUrlParams:loadParams};
		var submitUrl = this.getBaseAction() + '!save.action';
		var submit = {	submitUrl:submitUrl,
						submitUrlParams:submitParams,
						submitButtonText:getText('common.btn.save'),
						refreshAfterSubmitHandler:com.trackplus.util.RefreshAfterSubmit.refreshTreeAfterSubmit,
						refreshParametersBeforeSubmit:{nodeIDToReload: nodeIDToReload},
						refreshParametersAfterSubmit:[{parameterName:'nodeIDToSelect', fieldNameFromResult:'node'},
													{parameterName:'reloadTree', fieldNameFromResult:'reloadTree'}]};
		var items = this.getPanelItems();
		var windowParameters = {title:title,
			width:this.editWidth,
			height:this.editHeight,
			load:load,
			submit:submit,
			items:items};
		var windowConfig = Ext.create('com.trackplus.util.WindowConfig', windowParameters);
		windowConfig.showWindowByConfig(this);
	},

	getPanelItems: function() {
		return [CWHF.createTextField('common.lbl.name','name',
				{anchor:'100%', allowBlank:false, labelWidth:80, maxLength:255, enforceMaxLength:true})]
	},

	onRemovePerson: function() {
		this.reloadAssigned(this.getBaseAction()+"!unassign.action", {})
	},

	reload: com.trackplus.util.RefreshAfterSubmit.refreshTreeAfterSubmit,

	getReloadParamsAfterDelete: function(selectedRecords, extraConfig, responseJson) {
	    var reloadParams = {reloadTree:true};
	    if (selectedRecords) {
	        //we suppose that only one selection is allowed in tree
	        var selNode = selectedRecords;
	        if (selNode) {
	            reloadParams["nodeIDToReload"] = selNode.parentNode.data.id;
	            var previousSibling = selNode.previousSibling;
	            if (previousSibling) {
	                reloadParams["nodeIDToSelect"] = previousSibling.data.id;
	            } else {
	                var nextSibling = selNode.nextSibling;
	                if (nextSibling) {
	                    reloadParams["nodeIDToSelect"] = nextSibling.data.id;
	                } else {
	                    reloadParams["resetDetail"] = true;
	                }
	            }
	        }
	    }
	    return reloadParams;
	},

	onDeleteDepartment: function() {
		var selectedRecords = this.getSelectedRecords(true);
		if (selectedRecords) {
			this.deleteHandler(selectedRecords, {fromTree:true});
		}
	},

	onDetachFromParentDepartment: function() {
		var selectedRecord = this.getLastSelected(true);
		if (selectedRecord) {
			var nodeID = selectedRecord.data["id"];
			Ext.Ajax.request({
				url: this.getBaseAction()+"!clearParent.action",
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
	 * Url for deleting an entity
	 * extraConfig: for simple grid nothing, for tree with grid {fromTree:fromTree, isLeaf:isLeaf}
	 */
	getDeleteUrl: function(extraConfig){
		return this.getBaseAction() + '!delete.action';
	},

	getDeleteParams: function(selectedRecords, extraConfig) {
		return this.getEditParams();
	},

	/**
	 * The replacement items for the deleted entity
	 * (The replacement panel will be created with on this items)
	 */
	getReplacementItems: function(responseJson, selectedRecords, extraConfig) {
		return [{xtype : 'label',
				itemId: 'replacementWarning'},
	            CWHF.createSingleTreePicker("Replacement",
	            "replacementID", responseJson["replacementTree"], null,
	            {itemId: "replacementID",
	            allowBlank:false,
	            blankText: getText('common.err.replacementRequired',
	            this.getEntityLabel(extraConfig)),
	            labelWidth:200,
	            margin:'5 0 0 0'
	            })
			    ];
	},

	/**
	 * Load the data source and value for the replacement options tree
	 * Override this for different tree based pickers
	 */
	loadReplacementOptionData: function(replacementControl, data) {
	    replacementControl.updateMyOptions(data["replacementTree"]);
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
	},

	getDetailWidth: function() {
		return 800;
	},

	getDetailHeight: function() {
		return 600;
	},

	getMinWidth: function() {
		return 425;
	}
});
