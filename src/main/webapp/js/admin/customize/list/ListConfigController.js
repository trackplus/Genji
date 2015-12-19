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

Ext.define("com.trackplus.admin.customize.list.ListConfigController", {
	extend: "Ext.app.ViewController",
	alias: "controller.listConfig",
	mixins: {
		baseController: "com.trackplus.admin.TreeWithGridController"
	},
	baseServerAction : "listOptions",
    
    replaceToolbarOnTreeNodeSelect : true,

    entityDialog: "com.trackplus.admin.customize.list.ListEdit",

    isProjectSpecificRoot : function(node) {
        return this.getView().getFromProjectConfig() && node.get("id") === this.getView().tree.getRootNode().get("id");
    },
    
    /**
	 * Initialize all actions and return the toolbar actions
	 * this time called also by treeNodeSelect
	 */
    getToolbarActionsForTreeNodeSelect : function(node) {
        if (CWHF.isNull(node) /* || arrSelectedNodes.length===0 */) {
            // first load of the listConfig
            // return [this.actionAdd, this.actionEditGridRow,
			// this.actionDeleteGridRow];
            return [];
        } else {
            var actions = [];
            // after selecting a tree node
            var isLeaf = node.isLeaf();
            var mightEditChild = false;
            var mightDeleteChild = false;
            var canAddChild = false;
            var mightCopyChild = false;
            if (this.isProjectSpecificRoot(node)) {
                // after initializing the project specific
				// branch for project lists
                // although in the list tree no node is
				// selected, the add refers to the project
				// specific root
                // selectedNode.isRoot(): after deleting a list
				// (directly below root)
                canAddChild = true;
                mightEditChild = true;
                mightDeleteChild = true;
                mightCopyChild = true;
            } else {
                canAddChild = node.data["canAddChild"];
                mightEditChild = node.data["mightEditChild"]
                        || (isLeaf && node.parentNode.data["mightEditChild"]);
                mightDeleteChild = node.data["mightDeleteChild"]
                        || (isLeaf && node.parentNode.data["mightDeleteChild"]);
                mightCopyChild = node.data["mightCopyChild"];
            }
            var hasChildFilter = node.data["hasChildFilter"];
            var childrenHaveIcon = node.data["childrenHaveIcon"];
            if (canAddChild) {
                actions.push(this.getView().actionAdd);
                this.getView().actionAdd.setDisabled(false);
            }
            if (mightCopyChild) {
                // first unselected, select by grid row select
                actions.push(this.getView().actionCopyGridRow);
                this.getView().actionCopyGridRow.setDisabled(true);
            }
            if (mightEditChild) {
                actions.push(this.getView().actionEditGridRow);
                this.getView().actionEditGridRow.setDisabled(true);
            }
            if (mightDeleteChild) {
                actions.push(this.getView().actionDeleteGridRow);
                this.getView().actionDeleteGridRow.setDisabled(true);
            }
            var childListID = null;
            if (canAddChild) {
                childListID = node.data["childListID"];
                if (CWHF.isNull(childListID) || childListID === 0) {
                    // global or project node
                    actions.push(this.getView().actionImport);
                }
            }
            if (mightCopyChild) {
                // first unselected, select by grid row select
                actions.push(this.getView().actionExportGridRow);
                this.getView().actionExportGridRow.setDisabled(true);
            }
            if (hasChildFilter) {
                // actions.push(this.actionChildAssignment);
                // this.actionChildAssignment.setDisabled(false);
                actions.push(this.getView().actionChildIssueTypeAssignmentFromGrid);
                this.getView().actionChildIssueTypeAssignmentFromGrid.setDisabled(true);
            }
            if (childrenHaveIcon) {
                if (mightEditChild) {
                    actions.push(this.getView().actionUploadFromGrid);
                }
                actions.push(this.getView().actionDownloadFromGrid);
                // both disabled until selection
                this.getView().actionUploadFromGrid.setDisabled(true);
                this.getView().actionDownloadFromGrid.setDisabled(true);
            }
            if (childListID  && childListID !== 0 && mightEditChild) {
                actions.push(this.getView().actionMoveUp);
                actions.push(this.getView().actionMoveDown);
                // both disabled until selection
                this.getView().actionMoveUp.setDisabled(true);
                this.getView().actionMoveDown.setDisabled(true);
            }
            return actions;
        }
    },

    onChildIssueTypeAssignmentGrid : function() {
        var recordData = this.getView().getSingleSelectedRecordData(false);
        if (recordData ) {
            this.onChildIssueTypeAssignment(recordData.node, recordData);
        }
    },

    onChildIssueTypeAssignmentTree : function() {
        this.onChildIssueTypeAssignment(this.getView().selectedNodeID, this.getView().selectedNode);
    },

    onChildIssueTypeAssignment : function(nodeID, record) {
        var childIssueTypeAssignment = Ext.create("com.trackplus.admin.SimpleAssignment", {
            objectID: nodeID,
            objectIDParamName: "parentIssueTypeNodeID",
            dynamicIcons: true,
            baseServerAction: "childIssueTypeAssignments"
        });
        childIssueTypeAssignment.loadDetailData();
        //this.childIssueTypeAssignment.baseServerAction = "childIssueTypeAssignments";
        var assignmentWin = Ext.create("Ext.window.Window", {
            height: 450,
            width: 500,
            title: getText("admin.customize.list.button.filterByChildIssueType"),
            autoScroll: true,
            border: false,
            buttons: [ {
                text: getText("common.btn.done"),
                handler: function() {
                    assignmentWin.close();
                }
            }],
            layout: "fit",
            items: [childIssueTypeAssignment]
        }).show();
    },

    /** ****fileupload start***** */
    onDownloadFromGrid : function() {
        var recordData = this.getView().getSingleSelectedRecordData(false);
        if (recordData ) {
            this.downloadIcon(recordData.node);
        }
    },

    onDownloadFromTree : function() {
        this.downloadIcon(this.getView().selectedNodeID);
    },

    downloadIcon : function(selectedNodeID) {
        attachmentURI = "listOptionIcon!download.action?node=" + selectedNodeID;
        window.open(attachmentURI);
    },

    onUploadFromGrid : function() {
        var record = this.getView().getSingleSelectedRecord(false);
        if (record) {
            this.createUploadForm(false, record);
        }
    },

    onUploadFromTree : function() {
        this.createUploadForm(true, this.getView().selectedNode);
    },

    createUploadForm : function(fromTree, record) {
    	var loadAndSubmitParams = this.getEditParams(fromTree);
        var windowParameters = {
        	callerScope:this,
        	loadUrlParams: loadAndSubmitParams,
        	submitUrlParams: loadAndSubmitParams,
        	refreshParametersBeforeSubmit: this.getReloadParamsAfterUpload(),
        	refreshAfterSubmitHandler: this.reload,
        	extraConfig : {
                fromTree : fromTree
            }
        };
        var windowConfig = Ext.create("com.trackplus.admin.customize.list.IconUpload", windowParameters);
        windowConfig.showWindowByConfig();
    },

    /**
	 * Get the node to reload after upload operation
	 */
    getReloadParamsAfterUpload: function(fromTree) {
        if (this.getView().selectedNode) {
            if (fromTree) {
                // edited/copied from tree
                var parentNode = this.getView().selectedNode.parentNode;
                if (parentNode) {
                    // the parent of the edited node should be
					// reloaded
                    return {
                        nodeIDToReload: parentNode.data["id"],
                        nodeIDToSelect: this.getView().selectedNode.data["id"],
                        rowToSelect: this.getView().selectedNode.data["id"]
                    };
                }
            } else {
                // edited from grid:
                var gridRow = this.getView().getLastSelected(fromTree);
                if (this.getShowGridForLeaf() && this.getView().selectedNode.isLeaf()) {
                    // in the tree a leaf node selected -> grid
					// with a single row: the parent of the
					// selected tree node should be reloaded
                    var parentNode = this.getView().selectedNode.parentNode;
                    if (parentNode) {
	                    // the parent of the edited node should
						// be reloaded
	                    var reloadParams = {
	                        nodeIDToReload: parentNode.data["id"],
	                        nodeIDToSelect: this.getView().selectedNode.data["id"]
	                    };
	                    if (gridRow ) {
		                    reloadParams["rowToSelect"] = gridRow.data["node"];
	                    }
	                    return reloadParams;
                    }
                } else {
                    // in the tree the parent of the edited grid
					// row is selected: the actually selected
					// tree node should be reloaded
                    var reloadParams = {
	                    nodeIDToReload: this.getView().selectedNode.data["id"]
                    };
                    if (gridRow) {
	                    reloadParams["rowToSelect"] = gridRow.data["node"];
                    }
                    return reloadParams;
                }
            }
        }
        return null;
    },

    /** ****fileupload end***** */

    

    enableDisableToolbarButtons : function(view, arrSelections) {
        var selectedRecord = arrSelections[0];
        if (CWHF.isNull(selectedRecord)) {
            this.getView().actionDeleteGridRow.setDisabled(true);
            this.getView().actionEditGridRow.setDisabled(true);
            this.getView().actionCopyGridRow.setDisabled(true);
            this.getView().actionChildIssueTypeAssignmentFromGrid.setDisabled(true);
            this.getView().actionExportGridRow.setDisabled(true);
            this.getView().actionUploadFromGrid.setDisabled(true);
            this.getView().actionDownloadFromGrid.setDisabled(true);
            this.getView().actionMoveUp.setDisabled(true);
            this.getView().actionMoveDown.setDisabled(true);
        } else {
            var canDelete = selectedRecord.data["canDelete"];
            this.getView().actionDeleteGridRow.setDisabled(!canDelete);
            var canEdit = selectedRecord.data["canEdit"];
            this.getView().actionEditGridRow.setDisabled(!canEdit);
            this.getView().actionChildIssueTypeAssignmentFromGrid.setDisabled(!canEdit);
            this.getView().actionUploadFromGrid.setDisabled(!canEdit);
            this.getView().actionMoveDown.setDisabled(!canEdit);
            this.getView().actionMoveUp.setDisabled(!canEdit);
            if (canEdit) {
                var store = this.getView().grid.getStore();
                if (store) {
                    this.getView().actionMoveDown.setDisabled(selectedRecord === store.last());
                    this.getView().actionMoveUp.setDisabled(selectedRecord === store.first());
                }
            }
            var canCopy = selectedRecord.data["canCopy"];
            this.getView().actionCopyGridRow.setDisabled(!canCopy);
            this.getView().actionExportGridRow.setDisabled(!canCopy);
            var hasIcon = selectedRecord.data["iconName"]
                    && selectedRecord.data["iconName"] !== "";
            this.getView().actionDownloadFromGrid.setDisabled(!hasIcon);
        }
    },

    /**
     * Handler for adding a node (folder or leaf)
     */
    onAdd: function() {
        var operation = "add";
        var title = this.getView().getActionTooltip(this.getView().getAddTitleKey(), {fromTree:true});
        var loadParams = this.getAddLeafParams();
        var submitParams = this.getAddLeafParams();
        var reloadParams = this.getAddReloadParamsAfterSave(true);
        var reloadParamsFromResult = this.getAddSelectionAfterSaveFromResult();
        var selectedRecord = this.getView().getSingleSelectedRecord(true);
        if (CWHF.isNull(selectedRecord)) {
            selectedRecord = this.getView().tree.getRootNode();
        }
        return this.onAddEdit(title, selectedRecord, operation, true, true, true, loadParams,
                submitParams, reloadParams, reloadParamsFromResult, this.reload);
    },

    /**
     * Parameters for adding a new entry to a leaf:
     * add a child to a leaf node: exclusively by extending custom selects:
     * simple -> parent child or parent child -> parent child grandchild
     * without explicitly asking for the exact type of the composite select
     */
    getAddLeafParams: function() {
        //false as it would be added to a folder to send the leaf's id, not the parent folder's id
        return this.getAddParams(false);
    },

    /**
     * Copy a grid row: right now only custom lists can be copied (options, or option branches not)
     */
    onCopyGridRow: function() {
        return this.onCopy(false);
    },

    /**
     * Copy a tree node: right now only custom lists can be copied (options, or option branches not)
     */
    onCopyTreeNode: function() {
        return this.onCopy(true);
    },

    /**
     * Handler for copy
     */
    onCopy: function(fromTree) {
        var operation = "copy";
        var isLeaf = this.getView().selectedIsLeaf(fromTree);
        var title = this.getView().getActionTooltip(this.getView().getCopyTitleKey(), {fromTree:fromTree});
        var loadParams = this.getEditParams(fromTree);
        loadParams["copy"] = true;
        var submitParams = this.getEditParams(fromTree);
        submitParams["copy"] = true;
        var reloadParams = this.getCopyReloadParamsAfterSave(fromTree);
        var reloadParamsFromResult = this.getEditReloadParamsAfterSaveFromResult(fromTree);
        var selectedRecord = this.getView().getSingleSelectedRecord(fromTree);
        return this.onAddEdit(title, selectedRecord, operation, isLeaf, false, fromTree, loadParams,
                submitParams, reloadParams, reloadParamsFromResult, this.reload);
    },

    /**
     * Get the node to reload after save after copy operation
     */
    getCopyReloadParamsAfterSave : function(fromTree) {
        if (fromTree) {
            if (this.getView().selectedNode ) {
                var parentNode = this.getView().selectedNode.parentNode;
                if (parentNode ) {
                    return {
	                    nodeIDToReload : parentNode.data["id"]
                    };
                }
            }
        }
        //else nodeIDToReload is not set which means by default this.selectedNode (see refreshGridAndTreeAfterSubmit())
        return null;
    },

    /**
     * Parameters for reloading after a delete operation
     */
    getReloadParamsAfterDelete : function(selectedRecords, extraConfig, responseJson) {
        if (selectedRecords ) {
            //we suppose that only one selection is allowed in tree
            var selNode = selectedRecords;
            if (selNode ) {
                var parentNode = null;
                var grandParentNode = null;
                var parentNodeID = null;
                if (extraConfig ) {
                    fromTree = extraConfig.fromTree;
                    if (fromTree) {
	                    //delete from tree: select the parent of the deleted node for reload and select
	                    parentNode = selNode.parentNode;
	                    if (parentNode ) {
		                    parentNodeID = parentNode.data.id;
	                    }
	                    if (responseJson["lastCascadingChildDeleted"] && parentNode ) {
		                    grandParentNode = parentNode.parentNode;
		                    //in this case not the same as parentNode.data.id becuause the
		                    //childListID and leaf components of the nodeID changes
		                    parentNodeID = responseJson["node"];
	                    }
	                    var grandParentNodeID = null;
	                    if (grandParentNode ) {
		                    grandParentNodeID = grandParentNode.data.id;
	                    } else {
		                    grandParentNodeID = parentNodeID;
	                    }
	                    return {
	                        nodeIDToReload : grandParentNodeID,
	                        nodeIDToSelect : parentNodeID
	                    };
                    } else {
	                    //delete from grid: the parent or the leaf to be deleted is selected already in tree
	                    if (responseJson["lastCascadingChildDeleted"] && this.getView().selectedNode ) {
		                    //in this case not the same as this.selectedNode.data.id becuause the
		                    //childListID and leaf components of the nodeID changes
		                    parentNodeID = responseJson["node"];
		                    //in both cases the grid contains the single element to be deleted
		                    if (this.getView().selectedNode.isLeaf()) {
			                    //the leaf to be deleted was selected in the tree
			                    grandParentNode = this.getView().selectedNode.parentNode.parentNode;
		                    } else {
			                    //the parent of the leaf to be deleted was selected in the tree
			                    grandParentNode = this.getView().selectedNode.parentNode;
		                    }
		                    if (grandParentNode ) {
			                    var grandParentNodeID = grandParentNode.data.id;
			                    return {
			                        nodeIDToReload : grandParentNodeID,
			                        nodeIDToSelect : parentNodeID
			                    };
		                    }
	                    } else {
		                    if (this.getShowGridForLeaf() && this.getView().selectedNode.isLeaf()) {
			                    return {
				                    nodeIDToReload : this.getView().selectedNode.parentNode.data.id
			                    };
		                    }
		                    return {
			                    nodeIDToSelect : this.getView().selectedNode.data.id
		                    };
	                    }
                    }
                }
            }
        }
        return null;
    },

    onImport : function() {
    	var loadAndSubmitParams = this.getEditParams(true);
        var windowParameters = {
        	callerScope:this,
        	title: getText(this.getView().getUploadTitleKey(), getText("admin.customize.list.import.lbl.uploadFile")),
        	loadUrlParams: loadAndSubmitParams,
        	submitUrlParams: loadAndSubmitParams
        };
        var importWindow = Ext.create("com.trackplus.admin.customize.list.ListImport", windowParameters);
        importWindow.showWindowByConfig(this);
    },

    onExportGridRow : function() {
        this.onExport(false);
    },

    onExportTreeNode : function() {
        this.onExport(true);
    },

    onExport : function(fromTree) {
        attachmentURI = this.getBaseServerAction() + "!exportList.action?node=" + this.getSelectedID(fromTree);
        window.open(attachmentURI);
    },

    getSelectedID : function(fromTree) {
        var recordData = this.getView().getSingleSelectedRecordData(fromTree);
        if (recordData ) {
            return this.getRecordID(recordData, {
                fromTree : fromTree
            });
        }
        return null;
    },

    /**
     * Get the itemId of those actions whose context menu text or
     * toolbar button tooltip should be changed according to the current selection
     */
    getActionItemIdsWithContextDependentLabel : function() {
        return [ "add", "editGridRow", "editTreeNode", "copyGridRow", "copyTreeNode", "deleteGridRow",
                "deleteTreeNode", /*"uploadIconGridRow", "uploadIconTreeNode", "downloadIconGridRow",
                "downloadIconTreeNode",*/ "moveUp", "moveDown", "exportGridRow", "exportTreeNode",
                "importList" ];
    },
    

    onTreeNodeDblClick : function(view, record) {
        var canEdit = record.data["canEdit"];
        if (canEdit) {
            this.onEditTreeNode();
        }
    },

    onGridRowDblClick : function(view, record) {
        var canEdit = record.data["canEdit"];
        if (canEdit) {
            this.onEditGridRow();
        }
    },
    
    changeDefault: function(checkBox, rowIndex, checked, eOpts) {
		var record = this.getView().grid.getStore().getAt(rowIndex);
		if (record) {
			var params = {node:record.data["node"], defaultOption:checked};
			Ext.Ajax.request({
				url: this.getBaseServerAction() + "!changeDefault.action",
				params: params,
				scope: this,
				success: function(response) {
				},
				failure: function(response) {
					Ext.MessageBox.alert(this.failureTitle, response.responseText);
				}
			});
		}
	}
});
