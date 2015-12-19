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
Ext.define("com.trackplus.admin.user.DepartmentController",{
	extend: "Ext.app.ViewController",
	alias: "controller.department",
	mixins: {
		baseController: "com.trackplus.admin.TreeDetailAssignmentController"
	},
	
	baseServerAction: "department",
	
	replacementIsTree: true,
	
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
	 * Handler for adding a new screen
	 */
	onAddMainDepartment: function() {
		return this.onAddDepartment(false);
	},

	onAddSubdepartment: function() {
		return this.onAddDepartment(true);
	},

	onAddDepartment: function(addAsSubdepartment) {
		var title = this.getView().getActionTooltip(this.getView().getAddTitleKey());
		var selectedRecord = this.getView().getSingleSelectedRecord(true);
		var loadParams = this.getEditParams();
		loadParams["add"] = true;
		var submitParams = this.getEditParams();
		submitParams["addAsSubdepartment"] = addAsSubdepartment;
		submitParams["add"] = true;
		var nodeIDToReload;
		if (addAsSubdepartment) {
			nodeIDToReload = this.getView().selectedNodeID;
		} else {
			nodeIDToReload = this.getView().tree.getRootNode().get("id");
		}
		return this.onAddEdit(title, selectedRecord, loadParams, submitParams, nodeIDToReload);
	},

	/**
	 * Handler for editing a screen
	 */
	onEditDepartment: function() {
		var title = this.getView().getActionTooltip(this.getView().getEditTitleKey());
		var selectedRecord = this.getView().getSingleSelectedRecord(true);
		var loadParams = this.getEditParams();
		var submitParams = this.getEditParams();
		return this.onAddEdit(title, selectedRecord, loadParams, submitParams, this.getParentNodeId());
	},

	/**
	 * Parameters for editing an existing entity
	 * recordData: the selected entity data
	 */
	getEditParams: function() {
		var record = this.getView().getSingleSelectedRecord(true);
		if (record) {
			var nodeID = record.get("id");
			if (nodeID) {
				return {node:nodeID};
			}
		}
		return {};
	},

	/**
	 * Get the node to reload after save after edit operation
	 */
	getParentNodeId: function() {
		if (this.getView().selectedNode) {
				//edited/copied from tree
				var parentNode = this.getView().selectedNode.parentNode;
				if (parentNode) {
					//the parent of the edited node should be reloaded
					return parentNode.get("id");
				}
		}
		return this.getView().tree.getRootNode().get("id");
	},

	/**
	 * Handler for add/edit a node/row
	 * title: 'add'/'edit'/'copy'
	 * record: the selected record (tree node or grid row )
	 * loadParams
	 * submitParams
	 * nodeIDToReload
	 */
	onAddEdit: function(title, record, loadParams, submitParams, nodeIDToReload) {
		var windowParameters = {
        	callerScope:this,
        	windowTitle:title,
        	loadUrlParams: loadParams,
        	submitUrlParams: submitParams,
        	refreshAfterSubmitHandler:this.reload,
        	refreshParametersBeforeSubmit:{nodeIDToReload: nodeIDToReload},
			refreshParametersAfterSubmit:[{parameterName:"nodeIDToSelect", fieldNameFromResult:"node"},
										{parameterName:"reloadTree", fieldNameFromResult:"reloadTree"}],
        	entityContext: {
        		//the last selected tree node
        		selectedTreeNode: this.getView().getLastSelectedTreeNode(),
        		//the record data of the actually selected node/row
        		record: record
        	}
        };
		var windowConfig = Ext.create("com.trackplus.admin.user.DepartmentEdit", windowParameters);
		windowConfig.showWindowByConfig(this);
	},

	
	onRemovePerson: function() {
		var selectedRecord = this.getView().getSingleSelectedRecord(true);
		if (selectedRecord) {
			var id = selectedRecord.get("id");
			if (id) {
				var lastIndex = id.lastIndexOf("_");
			    var personID = id.substring(lastIndex + 1);
			    this.reloadAssigned("department!unassign.action", {unassign:[personID]});
			}
		}
	},

	reload: function(refreshParamsObject) {
		this.refreshTreeAfterSubmit(this.getView().tree, refreshParamsObject);
	}, 
		
	getReloadParamsAfterDelete: function(selectedRecords, extraConfig, responseJson) {
	    var reloadParams = {reloadTree:true};
	    if (selectedRecords) {
	        //we suppose that only one selection is allowed in tree
	        var selNode = selectedRecords;
	        if (selNode) {
	            reloadParams["nodeIDToReload"] = selNode.parentNode.get("id");
	            var previousSibling = selNode.previousSibling;
	            if (previousSibling) {
	                reloadParams["nodeIDToSelect"] = previousSibling.get("id");
	            } else {
	                var nextSibling = selNode.nextSibling;
	                if (nextSibling) {
	                    reloadParams["nodeIDToSelect"] = nextSibling.get("id");
	                } else {
	                    reloadParams["resetDetail"] = true;
	                }
	            }
	        }
	    }
	    return reloadParams;
	},

	onDeleteDepartment: function() {
		var selectedRecords = this.getView().getSelectedRecords(true);
		if (selectedRecords) {
			this.deleteHandler(this.getView().getEntityLabel({fromTree:true}), selectedRecords, {fromTree:true});
		}
	},

	onDetachFromParentDepartment: function() {
		var selectedRecord = this.getView().getLastSelected(true);
		if (selectedRecord) {
			var nodeID = selectedRecord.get("id");
			Ext.Ajax.request({
				url: "department!clearParent.action",
				params: {
					node:nodeID
				},
				disableCaching:true,
				scope:this,
				success: function(response){
					this.reload(this.getView().tree, {nodeIDToSelect:nodeID, reloadTree:true});
				}
			});
		}
	}
	
});
