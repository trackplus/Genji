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
Ext.define("com.trackplus.admin.user.GroupController",{
	extend: "Ext.app.ViewController",
	alias: "controller.group",
	mixins: {
		baseController: "com.trackplus.admin.TreeDetailAssignmentController"
	},
	
	baseServerAction: "group",
	
	/**
	 * Create the edit
	 */
	onTreeNodeDblClick: function(view, record) {
		var leaf = record.isLeaf();
		if (!leaf) {
			this.onEditGroup();
		}
	},

	/**
	 * Handler for adding a new screen
	 */
	onAddGroup: function() {
		var title = this.getView().getActionTooltip(this.getView().getAddTitleKey());
		var selectedRecord = this.getView().getSingleSelectedRecord(true);
		return this.onAddEdit(title, selectedRecord, {add:true}, {add:true});
	},

	/**
	 * Handler for editing a screen
	 */
	onEditGroup: function() {
		var title = this.getView().getActionTooltip(this.getView().getEditTitleKey());
		var selectedRecord = this.getView().getSingleSelectedRecord(true);
		var loadParams = this.getEditParams();
		var submitParams = this.getEditParams();
		return this.onAddEdit(title, selectedRecord, loadParams, submitParams);
	},

	onShowAssignments: function() {
		var title = getText("admin.user.group.lbl.roleAssignments");
		var submitParams = this.getEditParams();
		var windowParameters = {
	        	callerScope:this,
	        	windowTitle:getText("admin.user.group.lbl.roleAssignments"),
	        	//loadUrlParams: loadParams,
	        	loadHandler: function() {
	        		//empty no form load is needed, the grid initializes itself  
	        	},
	        	entityContext: {
	        		//the record data of the actually selected node/row
	        		record: this.getView().getSingleSelectedRecord(true),
	        		isGroup: true
	        	}
	        };
			var windowConfig = Ext.create("com.trackplus.admin.user.UserRolesInProjectWindow", windowParameters);
			windowConfig.showWindowByConfig(this);
	},

	getAssignmentGrid: function(personID) {
		var gridComponent = Ext.create("com.trackplus.admin.user.UserRolesInProject",
				{personID:personID, group:true});
		//var grid = gridComponent.getGrid();
		//grid.store.load();
		return [gridComponent];
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
	onAddEdit: function(title, record, loadParams, submitParams) {
		var windowParameters = {
        	callerScope:this,
        	windowTitle:title,
        	loadUrlParams: loadParams,
        	submitUrlParams: submitParams,
        	refreshAfterSubmitHandler:this.reload,
        	refreshParametersBeforeSubmit:{nodeIDToReload: this.getView().tree.getRootNode().get("id")},
			refreshParametersAfterSubmit:[{parameterName:"nodeIDToSelect", fieldNameFromResult:"node"},
										{parameterName:"reloadTree", fieldNameFromResult:"reloadTree"}],
        	entityContext: {
        		//the last selected tree node
        		selectedTreeNode: this.getView().getLastSelectedTreeNode(),
        		//the record data of the actually selected node/row
        		record: record
        	}
        };
		var windowConfig = Ext.create("com.trackplus.admin.user.GroupEdit", windowParameters);
		windowConfig.showWindowByConfig(this);
	},

	
	onRemovePerson: function() {
		this.reloadAssigned(this.getBaseAction()+"!unassign.action", {})
	},

	reload: function(refreshParamsObject) {
		this.refreshTreeAfterSubmit(this.getView().tree, refreshParamsObject);
	},

	getReloadParamsAfterDelete: function(selectedRecords, extraConfig, responseJson) {
	    var reloadParams = {reloadTree:true};
	    if (selectedRecords && selectedRecords.length>0) {
	        //we suppose that only one selection is allowed in tree
	    	var selNode = selectedRecords[0];
	    	reloadParams["nodeIDToReload"] = selNode.parentNode.data.id;
	    	if (selectedRecords.length===1) {
	    		//one group was deleted: select the previous or next if exist
	            if (selNode) {
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
	            } else {

	            }
	    	} else {
	    		//more than one group was selected to delete
	    		reloadParams["resetDetail"] = true;
	    	}
	    }
	    return reloadParams;
	},

	/**
	 * Get the detail part after selecting a tree node
	 */
	/*protected*/loadDetailPanel: function(node, leaf, opts) {
		if (this.getView().selectionIsSimple(true)) {
			this.loadSimpleDetailPanel(node, false);
		} else {
			this.resetDetailPanel();
		}
	},

	onDeleteGroup: function() {
		var selectedRecords = this.getView().getSelection(true);
		if (selectedRecords) {
			this.deleteHandler(selectedRecords, {fromTree:true});
		}
	},

	getDeleteParamName: function() {
	    return "selectedGroupIDs";
	}
});
