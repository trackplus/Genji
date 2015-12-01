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
Ext.define('com.trackplus.admin.customize.treeConfig.WorkflowConfig',{
	extend:'com.trackplus.admin.customize.treeConfig.AssignmentConfig',
	config: {
		rootID:'_',
		projectOrProjectTypeID: null
	},
	baseAction: "workflowAssignment",
	/**
	 * The width of the screen edit window
	 */
	editWidth:500,
	/**
	 * The height of the scree edit window
	 */
	editHeight:350,

	constructor: function(config) {
		var config = config || {};
		this.initConfig(config);
		this.initBase();
	},

	/**
	 * The message to appear first time after selecting this menu entry
	 * Is should be shown by selecting the root but the root is typically not visible
	 */
	getRootMessage: function(rootID) {
		return getText('admin.customize.workflow.config.lbl.description');
	},

	/**
	 * The localized leaf name
	 */
	getEntityLabel: function() {
		return getText('admin.customize.workflow.config.lbl.workflowAssignment');
	},


	getEntityConfigLabelSingular: function() {
		return getText("admin.customize.workflow.config.lbl.workflow");
	},

	getEntityConfigLabelPlural: function() {
		return getText("admin.customize.workflow.config.lbl.workflows");
	},

	/**
	 * Which actions to enable/disable depending on tree selection
	 */
	getToolbarActionChangesForTreeNodeSelect: function(node) {
		if (node ) {
			var inheritedConfig=node.data['inheritedConfig'];
			this.actionReset.setDisabled(inheritedConfig);
			var nodeID = node.data['id'];
			this.actionApply.setDisabled(!this.isAssignable(node));
		}
	},

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

	saveParametersAndAssign: function(options) {
		this.saveWorkflowAssignment(options["workflowID"], options["node"]);
	},

	saveWorkflowAssignment: function(draggedObjectID, droppedNodeID) {
		Ext.Ajax.request({
			url: this.getBaseAction() + '!save.action',
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

	/**
	 * Get the actions available in context menu depending on the currently selected row
	 */
	getTreeContextMenuActions: function(selectedRecord, selectionIsSimple) {
		var actions = [];
		if (selectionIsSimple) {
			var inheritedConfig = selectedRecord.data['inheritedConfig'];
			var leaf = selectedRecord.isLeaf();
			if (!inheritedConfig) {
				actions.push(this.actionReset);
			}
			if (!leaf) {
				actions.push(this.actionReload);
			}
		}
		return actions;
	},

	isValidNode: function(node) {
		var leaf =node.data["leaf"];
		if (!leaf){
			return false;
		}
		return true;
	},

	getGridListURL:function() {
		return "workflowEdit!list.action";
	},

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
		return this.getBaseAction() + "!importWorkflows.action";
	},

	getExportURL: function(selectedObjectIDs) {
		return this.getBaseAction() + "!export.action?selectedObjectIDs="+selectedObjectIDs;
	},

	/**
	 * Url for deleting an entity
	 * extraConfig: for simple grid nothing, for tree with grid {fromTree:fromTree, isLeaf:isLeaf}
	 */
	getDeleteUrl: function(extraConfig){
		return 'workflowEdit!delete.action';
	},

	getConfigGridRowURL: function(id) {
		return 'workflowEdit.action?workflowID='+id+'&backAction=admin.action';
	},

	/*getDeleteParams: function(selectedRecords, extraConfig) {
		return this.getEditParams(false);
	},*/

	getDeleteParamName: function() {
	    return "selectedWorkflowIDs";
	},

	getPanelItems: function() {
		return [CWHF.createTextField('common.lbl.name','name',
					{anchor:'100%', allowBlank:false, labelWidth:this.labelWidth, width:this.textFieldWidth}),
				CWHF.createTextField('common.lbl.tagLabel','tagLabel',
					{anchor:'100%', labelWidth:this.labelWidth, width:this.textFieldWidth}),
				CWHF.createTextAreaField('common.lbl.description','description',
					{anchor:'100%', labelWidth:this.labelWidth, width:this.textFieldWidth})];
	},

	getUploadFileLabel: function() {
		return "admin.customize.workflow.import.lbl.uploadFile";
	},

	onImport: function() {
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
	},
	getSelectedWorkflowIDs:function(){
		var selectedTemplateIDs = new Array();
		var selectedRecordsArr = this.getSelection();
		if (selectedRecordsArr) {
			Ext.Array.forEach(selectedRecordsArr, function(record, index, allItems)
			{selectedTemplateIDs.push(record.data['id']);}, this);
		}
		return selectedTemplateIDs.join(",");
	},

	onExport: function() {
		var urlStr=this.getBaseAction() + "!exportWorkflows.action?selectedWorkflowIDs="+this.getSelectedWorkflowIDs();
		window.open(urlStr);
	},
	isDisabledAddAction:function(){
		var sysAdmin=com.trackplus.TrackplusConfig.user.sys;
		return (sysAdmin===false);
	},
	isDisabledImportAction:function(){
		var sysAdmin=com.trackplus.TrackplusConfig.user.sys;
		return (sysAdmin===false);
	},
	enableDisableToolbarButtons: function (view, selections) {
		var sysAdmin=com.trackplus.TrackplusConfig.user.sys;
		if (CWHF.isNull(selections) || selections.length===0) {
			this.actionEdit.setDisabled(true);
			this.actionApply.setDisabled(true);
			this.actionCopy.setDisabled(true);
			this.actionDelete.setDisabled(true);
			this.actionDesign.setDisabled(true);
			this.actionExport.setDisabled(true);
		} else {
			var selectedRecord = selections[0];
			var isLeaf = selectedRecord.data['leaf'];
			this.actionDelete.setDisabled(!sysAdmin);
			this.actionExport.setDisabled(!sysAdmin);
			if (selections.length===1) {
				var selectedTreeNode = this.getSingleSelectedRecord(true);
				if (selectedTreeNode && this.isAssignable(selectedTreeNode)/*selectedTreeNode.isLeaf()*/) {
					this.actionApply.setDisabled(false);
				}
				this.actionEdit.setDisabled(!sysAdmin);
				this.actionCopy.setDisabled(!sysAdmin);
				this.actionDesign.setDisabled(false);
			} else {
				this.actionApply.setDisabled(true);
				this.actionEdit.setDisabled(true);
				this.actionCopy.setDisabled(true);
				this.actionDesign.setDisabled(true);
			}
		}
	},
	getGridContextMenuActions: function(selectedRecord, selectionIsSimple) {
		var sysAdmin=com.trackplus.TrackplusConfig.user.sysAdmin;
		if (selectionIsSimple) {
			var selectedTreeNode = this.getSingleSelectedRecord(true);
			if (selectedTreeNode && selectedTreeNode.isLeaf()) {
				if(sysAdmin){
					return [this.actionApply, this.actionEdit, this.actionCopy, this.actionDelete, this.actionDesign];
				}else{
					return [this.actionDesign];
				}
			} else {
				if(sysAdmin){
					return [this.actionEdit, this.actionCopy, this.actionDelete, this.actionDesign];
				}else{
					return [this.actionDesign];
				}
			}
		} else {
			if(sysAdmin){
				return [this.actionDelete];
			}else{
				return null;
			}
		}
	},
	refreshAfterSaveSuccess: function(result) {
		var refreshTree = result.refreshTree;
		var nodeIdToSelect = result.node;
		var node = this.tree.getStore().getNodeById(nodeIdToSelect);
		var refreshStartFromNode=(nodeIdToSelect==='workflow_workflow');
		var parentNode = null;
		if (node) {
			parentNode = node.parentNode;
		}
		if (refreshTree) {
			this.refreshTreeOpenBranches(this.tree.getRootNode(), refreshStartFromNode, false, parentNode, nodeIdToSelect);
		}
	}
});
