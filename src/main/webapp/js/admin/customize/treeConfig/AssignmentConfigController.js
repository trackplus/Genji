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
 * treeWithGrid are implemented again (alternatively use multiple inheritance?)
 *
 */
Ext.define("com.trackplus.admin.customize.treeConfig.AssignmentConfigController", {
	extend:"com.trackplus.admin.customize.treeConfig.TreeConfigController",
	config: {
		rootID:'_'
	},
	allowMultipleSelections:true,//for delete
	//textFieldWidth: 350,
	//labelWidth: 120,
	
	/*protected abstract*/getGridRowEditURL: function() {
		return "";
	},

	/*protected abstract*/getGridRowSaveURL: function() {
		return "";
	},

	/*protected abstract*/getConfigGridRowURL: function() {
		return "";
	},

	/*protected abstract*/getImportURL: function() {
		return "";
	},

	/*protected abstract*/getExportURL: function(selectedObjectIDs) {
		return "";
	},

	
	/**
	 * Paste a node in the tree after copy/cut
	 */
	saveAssignment: function(draggedObjectID, droppedNodeID) {
		Ext.Ajax.request({
			url: this.getView().getBaseServerAction() + "!save.action",
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

	onTreeNodeSelect: function(rowModel, node, index) {
		var record = this.getView().grid.getStore().getById(node.data['assignedID']);
		if (record) {
			this.getView().selectGridRow(record);
		}
		this.getView().getToolbarActionChangesForTreeNodeSelect(node);
	},

	onTreeViewAfterRenderer:function(){
		this.getView().dropZone = Ext.create("com.trackplus.util.TreeDropZone", {
			view: this.getView().tree.view,
			ddGroup: this.getView().getBaseServerAction(),
			isValidNode:function(node){
				return this.isAssignable(node);
			},
			isValidNodeScope: this
		});
	},
	
	onTreeViewBeforeDrop: function(node,data, overModel, dropFunction,eOpts) {
		return this.isAssignable(overModel);
	},
	
	isAssignable: function(node) {
		return node.isLeaf();
	},
	
	onTreeViewDrop: function(node,data, overModel, dropFunction,eOpts){
		this.onDropTreeNode.call(this,node,data, overModel,dropFunction,eOpts);
		return true;
	},
	
	onDropTreeNode: function(node, data, overModel) {
		var draggedNodeID=data.records[0].data['id'];
		var droppedNodeID=overModel.data['id'];
		if (draggedNodeID && droppedNodeID) {
			this.saveAssignment(draggedNodeID, droppedNodeID);
		}
		return true;
	},

	/**
	 * Enable/disable actions based on the actual selection
	 */
	onGridSelectionChange: function (view, selections) {
		this.enableDisableToolbarButtons(view, selections);
		var selectedRow = null;
		if (selections && selections.length>0) {
			selectedRow = selections[0];
		}
		this.adjustToolbarButtonsTooltip(selectedRow, false);
	},

	/**
	 * Show the context menu in grid
	 */
	onGridRowCtxMenu: function(grid, record, item, index, evtObj) {
		this.onCtxMenu(false, record, evtObj);
		return false;
	},

	/**
	 * Enable/disable actions based on the actual selection
	 */
	enableDisableToolbarButtons: function (view, selections) {
		if (CWHF.isNull(selections) || selections.length===0) {
			this.getView().actionEdit.setDisabled(true);
			this.getView().actionApply.setDisabled(true);
			this.getView().actionCopy.setDisabled(true);
			this.getView().actionDelete.setDisabled(true);
			this.getView().actionDesign.setDisabled(true);
			this.getView().actionExport.setDisabled(true);
		} else {
			var selectedRecord = selections[0];
			var isLeaf = selectedRecord.get("leaf");
			this.getView().actionDelete.setDisabled(false);
			this.getView().actionExport.setDisabled(false);
			if (selections.length===1) {
				var selectedTreeNode = this.getView().getSingleSelectedRecord(true);
				if (selectedTreeNode && this.isAssignable(selectedTreeNode)) {
					this.getView().actionApply.setDisabled(false);
				}
				this.getView().actionEdit.setDisabled(false);
				this.getView().actionCopy.setDisabled(false);
				this.getView().actionDesign.setDisabled(false);
			} else {
				this.getView().actionApply.setDisabled(true);
				this.getView().actionEdit.setDisabled(true);
				this.getView().actionCopy.setDisabled(true);
				this.getView().actionDesign.setDisabled(true);
			}
		}
	},

	/**
	 * Save the detail part
	 * The 'add' parameter is added as hidden field to the corresponding form detail items
	 * Alternatively this should be included as extra parameter here if 'add' were declared
	 * as class level variable set in onAdd action and reset on treeNode select
	 */
	onApply: function() {
		var selectedTreeNode = this.getView().getSingleSelectedRecord(true);
		var selectedGridRow = this.getView().getSingleSelectedRecord(false);
		if (selectedTreeNode && selectedGridRow) {
			this.saveAssignment(selectedGridRow.get("id"), selectedTreeNode.get("id"));
		}
	},

	/**
	 * Handler for adding a new screen
	 */
	onAdd: function() {
		var title = this.getView().getAddLabel();
		return this.onAddEdit(title, true,
				null, null);
	},

	/**
	 * Handler for editing a screen
	 */
	onEdit: function() {
		var title = this.getView().getEditLabel();
		var loadParams = this.getEditParams(false);
		var submitParams = this.getEditParams(false);
		return this.onAddEdit(title, false,
				loadParams, submitParams);
	},

	/**
	 * Handler for copying a screen
	 */
	onCopy: function() {
		var title = this.getView().getCopyLabel();
		var loadParams = this.getEditParams(true);
		var submitParams = this.getEditParams(true);
		return this.onAddEdit(title, false,
				loadParams, submitParams);
	},

	getObjectIDName: function() {
		return "objectID";
	},

	/**
	 * Parameters for editing an existing entity
	 * recordData: the selected entity data
	 */
	getEditParams: function(copy) {
		var params = {copy:copy};
		var record = this.getView().getSingleSelectedRecord(false);
		if (record) {
			var objectID = record.get("id");
			if (objectID) {
				params[this.getObjectIDName()] = objectID;
			}
		}
		return params;
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
	onAddEdit: function(title, add, loadParams, submitParams) {
		var windowParameters = {
	        	callerScope:this,
	        	windowTitle:title,
	        	loadUrlParams: loadParams,
	        	submitUrlParams: submitParams,
	        	refreshAfterSubmitHandler:this.reload,
	        	refreshParametersAfterSubmit:[{parameterName:"rowToSelect", fieldNameFromResult:"id"}],
	        	entityContext: {
	        		loadUrl: this.getGridRowEditURL(),
	        		submitUrl: this.getGridRowSaveURL(),
	        	}
	        };
			var windowConfig = Ext.create("com.trackplus.admin.customize.treeConfig.AssignmentConfigEdit", windowParameters);
			windowConfig.showWindowByConfig(this);
	},

	/**
	 * Reload after a change operation
	 */
	reload: function(refreshParamsObject) {
		this.reloadGrid(this.getView().grid, refreshParamsObject);
	},
	
	/**
	 * Gather selected ID as a comma separated string
	 * selectedRecords - the selected records
	 * Get the "id" for selections: used in delete and export
	 * 
	 */
	/*private*/getSelectedIDs : function(selectedRecords) {
	    var idArray = [];
	    Ext.Array.forEach(selectedRecords, function(record) {
		    idArray.push(record.get("id"));
	    }, this);
	    return idArray.join();
	},
	
	onDelete: function() {
		//from grid
		var selectedRecords = this.getView().getSelectedRecords(false);
		if (selectedRecords) {
			this.deleteHandler(this.getView().getEntityLabel(), selectedRecords);
		}
	},


	onConfig:function() {
		var record = this.getView().getSingleSelectedRecord(false);
		if (record) {
			var id = record.get("id");
			window.location.href=this.getConfigGridRowURL(id);
		}
	},


	/**
	 * panel for importing the e-mail templates
	 */
	getImportPanel:function() {
		this.formPanel= new Ext.form.FormPanel({
			region:'center',
			border:false,
			bodyStyle: 'padding:5px',
			defaults: {
				labelStyle:'overflow: hidden;',
				margin:"5 5 0 0",
				msgTarget:	'side',
				anchor:	'-20'
			},
			method: 'POST',
			fileUpload: true,
			items: [CWHF.createCheckbox('common.lbl.overwriteExisting', 'overwriteExisting', {labelWidth:200}),
					CWHF.createFileField(this.getUploadFileLabel(), 'uploadFile',
					{itemId:"uploadFile", allowBlank:false, labelWidth:200})]
		});
		return this.formPanel;
	},

	/*protected abstract*/getUploadFileLabel: function() {
		return null;
	},
	
	onImport: function() {
		var submit = [{submitUrl:this.getImportURL(),
				submitButtonText:getText('common.btn.upload'),
				validateHandler: Upload.validateUpload,
				expectedFileType: /^.*\.(xml)$/,
				refreshAfterSubmitHandler:this.reload}];
		var title = getText('common.lbl.upload', this.getView().getEntityConfigLabelPlural());
		var windowParameters = {title:title,
				width:550,
				height:150,
				submit:submit,
				formPanel: this.getImportPanel(),
				cancelButtonText: getText('common.btn.done')};
		var windowConfig = Ext.create('com.trackplus.util.WindowConfig', windowParameters);
		windowConfig.showWindowByConfig(this);
	},

	onExport: function() {
		var selectedRecords = this.getView().getSelectedRecords(false);
		var selectedIDs = this.getSelectedIDs(selectedRecords);
		attachmentURI=this.getExportURL(selectedIDs);
		window.open(attachmentURI);
	},
	
	/**
	 * Refresh after a successful save
	 */
	refreshAfterSaveSuccess: function(result) {
		var refreshTree = result.refreshTree;
		var nodeIdToSelect = result.node;
		var node = this.getView().tree.getStore().getNodeById(nodeIdToSelect);
		var parentNode = null;
		if (node) {
			parentNode = node.parentNode;
		}
		if (refreshTree) {
			this.refreshTreeOpenBranches(this.getView().tree.getRootNode(), false, false, parentNode, nodeIdToSelect);
		}
	}
});
