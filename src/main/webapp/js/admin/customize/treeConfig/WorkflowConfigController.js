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
 * Although it inherits from TreeConfig which is a treeBase,
 * some behavior is similar to treeWithGrid that's why some methods of
 * treeWithGrid are implemented again (alternatively multiple inheritance?)
 *
 */
Ext.define("com.trackplus.admin.customize.treeConfig.WorkflowConfigController",{
	extend: "Ext.app.ViewController",
	alias: "controller.workflowConfig",
	mixins: {
		baseController: "com.trackplus.admin.customize.treeConfig.AssignmentConfigController"
	},
	
	baseServerAction: "workflowAssignment",

	isAssignable: function(node) {
		var nodeID = node.data['id'];
		return nodeID!=="workflow_issueType" && nodeID!=="workflow_projectType" && nodeID!=="workflow_project"
	},

	/**
	 * Paste a node in the tree after copy/cut
	 */
	saveAssignment: function(draggedObjectID, droppedNodeID) {
		Ext.Ajax.request({
			url: "workflowParams!containsParameter.action",
			params: {
				workflowID: draggedObjectID,
				node: droppedNodeID
			},
			scope: this,
			disableCaching:true,
			success: function(response){
				var data = Ext.decode(response.responseText);
				var containsParameter = data.value;
				if (containsParameter) {
					var workflowParamsDialog=Ext.create("com.trackplus.admin.customize.treeConfig.WorkflowParams",{
						workflowID:draggedObjectID,
						node:droppedNodeID,
						workflowConfigContoller:this
					});
					workflowParamsDialog.showDialog();
				} else {
					this.saveWorkflowAssignment(draggedObjectID, droppedNodeID);
				}
			},
			failure: function(result){
				Ext.MessageBox.alert(scope.failureTitle, result.responseText);
			},
			method:"POST"
		});
	},

	/*saveParametersAndAssign: function(options) {
		this.saveWorkflowAssignment(options["workflowID"], options["node"]);
	},*/

	saveWorkflowAssignment: function(draggedObjectID, droppedNodeID) {
		Ext.Ajax.request({
			url: "workflowAssignment!save.action",
			params: {
				assignedID: draggedObjectID,
				node: droppedNodeID
			},
			scope: this,
			disableCaching:true,
			success: function(response){
				var responseJson = Ext.decode(response.responseText);
				if (responseJson.success === true) {
					this.refreshAfterSaveSuccess(responseJson);
				} else {
					com.trackplus.util.showError(responseJson);
				}
			},
			failure: function(result){
				Ext.MessageBox.alert(this.failureTitle, result.responseText)
			},
			method:"POST"
		});
	},

	/*isValidNode: function(node) {
		var leaf =node.data["leaf"];
		if (!leaf){
			return false;
		}
		return true;
	},*/

	
	getGridRowEditURL: function() {
		return "workflowEdit!edit.action";
	},

	getGridRowSaveURL: function() {
		return "workflowEdit!save.action";
	},

	getObjectIDName: function() {
		return "workflowID";
	},

	getImportURL: function() {
		return "workflowAssignment!importWorkflows.action";
	},

	getExportURL: function(selectedObjectIDs) {
		return "workflowAssignment!exportWorkflows.action?selectedWorkflowIDs="+selectedObjectIDs;
	},

	/**
	 * Url for deleting an entity
	 * extraConfig: for simple grid nothing, for tree with grid {fromTree:fromTree, isLeaf:isLeaf}
	 */
	getDeleteUrl: function(extraConfig){
		return "workflowEdit!delete.action";
	},

	getConfigGridRowURL: function(id) {
		return "workflowEdit.action?workflowID="+id+"&backAction=admin.action";
	},

	getDeleteParamName: function() {
	    return "selectedWorkflowIDs";
	},

	getUploadFileLabel: function() {
		return "admin.customize.workflow.import.lbl.uploadFile";
	},

	/*onImport: function() {
		var submit = [{submitUrl:this.getBaseAction() + "!importWorkflows.action",
				submitButtonText:getText('common.btn.upload'),
				validateHandler: Upload.validateUpload,
				expectedFileType: /^.*\.(xml)$/,
				refreshAfterSubmitHandler:this.reload}];
		var title = getText('common.lbl.upload', this.getEntityConfigLabelPlural());
		var windowParameters = {title:title,
				width:500,
				height:150,
				submit:submit,
				formPanel: this.getImportPanel(),
				cancelButtonText: getText('common.btn.done')};
		var windowConfig = Ext.create('com.trackplus.util.WindowConfig', windowParameters);
		windowConfig.showWindowByConfig(this);
	},*/
	
	enableDisableToolbarButtons: function (view, selections) {
		var sysAdmin=com.trackplus.TrackplusConfig.user.sys;
		if (CWHF.isNull(selections) || selections.length===0) {
			this.getView().actionEdit.setDisabled(true);
			this.getView().actionApply.setDisabled(true);
			this.getView().actionCopy.setDisabled(true);
			this.getView().actionDelete.setDisabled(true);
			this.getView().actionDesign.setDisabled(true);
			this.getView().actionExport.setDisabled(true);
		} else {
			var selectedRecord = selections[0];
			var isLeaf = selectedRecord.data['leaf'];
			this.getView().actionDelete.setDisabled(!sysAdmin);
			this.getView().actionExport.setDisabled(!sysAdmin);
			if (selections.length===1) {
				var selectedTreeNode = this.getView().getSingleSelectedRecord(true);
				if (selectedTreeNode && this.isAssignable(selectedTreeNode)) {
					this.getView().actionApply.setDisabled(false);
				}
				this.getView().actionEdit.setDisabled(!sysAdmin);
				this.getView().actionCopy.setDisabled(!sysAdmin);
				this.getView().actionDesign.setDisabled(false);
			} else {
				this.getView().actionApply.setDisabled(true);
				this.getView().actionEdit.setDisabled(true);
				this.getView().actionCopy.setDisabled(true);
				this.getView().actionDesign.setDisabled(true);
			}
		}
	},
	
	refreshAfterSaveSuccess: function(result) {
		var refreshTree = result.refreshTree;
		var nodeIdToSelect = result.node;
		var node = this.getView().tree.getStore().getNodeById(nodeIdToSelect);
		var refreshStartFromNode=(nodeIdToSelect==="workflow_workflow");
		var parentNode = null;
		if (node) {
			parentNode = node.parentNode;
		}
		if (refreshTree) {
			this.refreshTreeOpenBranches(this.getView().tree.getRootNode(), refreshStartFromNode, false, parentNode, nodeIdToSelect);
		}
	}
});
