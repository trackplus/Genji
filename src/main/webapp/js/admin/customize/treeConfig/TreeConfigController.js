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


Ext.define("com.trackplus.admin.customize.treeConfig.TreeConfigController",{
	//inherits from TreeBase (neither TreeDetail nor TreeWithGrid)
	//because it contains common functionality for
	//fieldConfig (TreeDetail) and screenConfig (a kind if TreeWithGrid)
	extend:"com.trackplus.admin.TreeBaseController",
	
	folderAction: "treeConfig",
	leafDetailByFormLoad: true,
	
	/**
	 * Get the detail part after selecting a tree node
	 * The base class (TreeConfig) inherits from TreeBase (neither TreeDetail nor TreeWithGrid)
	 * which means that the getDetailPanel is not implemented in base classes
	 */
	loadDetailPanel: function(node, leaf) {
		if (leaf) {
			this.loadDetailPanelWithFormLoad(node, false);
		} else {
			this.loadSimpleDetailPanel(node, false);
		}
	},

	/**
	 * The url for getting the leaf detail: either this should be overridden or the leafDetailUrl should be specified in the config
	 */
	/*protected*/getLeafDetailUrl: function() {
		return this.getBaseServerAction() + "!load.action";
	},

	
	/**
	 * Refresh after a successful save
	 */
	/*protected abstract*/refreshAfterSaveSuccess: function(result) {
	},

	/**
	 * The validate method called before submit
	 * Implement if the validation is not set directly at the form items (but verified later)
	 * panel: the form panel whose controls should or shouldn't be validated
	 * enableValidation: whether the validation should really occur
	 * 	true: before save
	 * 	false: submit needed to refresh only a part of the detail (like change field type specific part after field type change)
	 * 	The validation occurs automatically before every submit.
	 * 	If the form control validations are set then the validation might
	 * 	fail even if for a certain submit no validation is needed.
	 * 	Because in this case the "automatic" validation should succeed in order to make the submit possible
	 * 	we can't set validation directly on form controls, but this validate method will be called with the corresponding validateNeeded
	 */
	/*protected*/prepareValidate: function(panel, enableValidation) {
	},

	/**
	 * The node parameter sent to server
	 *
	 */
	/*protected*/getNodeParam: function(extraConfig) {
		var saveParams = new Object();
		var recordData = this.getView().getSingleSelectedRecordData(true);
		if (recordData) {
			var nodeId = this.getRecordID(recordData, {fromTree:true});
			if (nodeId) {
				saveParams['node'] = nodeId;
			}
		}
		return saveParams;
	},


	/**
	 * Overwrite a leaf node
	 */
	onOverwrite: function() {
		Ext.Ajax.request({
			url: this.folderAction + "!overwrite.action",
			params: this.getNodeParam(),
			scope: this,
			success : function(response) {
				var result = Ext.decode(response.responseText);
				var treeStore = this.getView().tree.getStore();
				var treeView = this.getView().tree.getView();
				var nodeToReload=treeStore.getNodeById(result.node);
				if (nodeToReload) {
					//for actualizing toolbar buttons in
					//onTreeNodeSelect according to  inheritedConfig
					nodeToReload.set("inheritedConfig", result.inheritedConfig);
					nodeToReload.set("text", result.text);
					nodeToReload.commit();
					var index = treeView.indexOf(nodeToReload);
					treeView.refreshNode(index);
					//actualize the toolbar buttons
					//although a tree refresh is not needed
					this.onTreeNodeSelect(null, nodeToReload);
				}
			},
			failure: function(response) {
				com.trackplus.util.requestFailureHandler(response, options);
			}
		});
	},

	/**
	 * Reset a leaf or a branch node
	 */
	onReset: function() {
		Ext.Ajax.request({
			url: this.folderAction + "!reset.action",
			params: this.getNodeParam(),
			scope: this,
			success : function(response) {
				var result = Ext.decode(response.responseText);
				var treeView = this.getView().tree.getView();
				var nodeToReload=this.getView().tree.getStore().getNodeById(result.node);
				if (nodeToReload) {
					var refreshTree = result.refreshTree;
					var branchReset = result.branchReset;
					if (refreshTree || branchReset) {
						//screen assignment reset or field configuration reset for a branch node
						var startFromNode;
						var refreshStartFromNode = false;
						var selectStartFromNode = false;
						if (refreshTree) {
							//refresh the entire tree (screen assignment)
							startFromNode = this.getView().tree.getRootNode();
						} else {
							//refresh only a branch (field configuration)
							/*if (nodeToReload.isLeaf()) {
							 //refresh the leaf's parent
							 startFromNode = nodeToReload.parentNode;
							 } else {*/
							//refresh the resetted branch node itself
							startFromNode = nodeToReload;
							refreshStartFromNode = true;
							selectStartFromNode = true;
							//}
						}
						refreshStartFromNode=refreshStartFromNode||(result.node==='workflow_workflow');
						this.refreshTreeOpenBranches(startFromNode, refreshStartFromNode, selectStartFromNode, null, result.node,
								result.issueType, result.projectType, result.project);
						//this.onTreeNodeSelect(this.tree, [startFromNode]);
					} else {
						//actualize the node directly in the tree without branch reload
						//(by a field configuration leaf reset)
						nodeToReload.data['inheritedConfig'] = true;
						nodeToReload.set('text', result.text);
						nodeToReload.commit();
						var index = treeView.indexOf(nodeToReload);
						treeView.refreshNode(index);
						//actualize the toolbar buttons according to  inheritedConfig
						this.onTreeNodeSelect(null, nodeToReload);
					}
				}
			},
			failure: function(response, options) {
				com.trackplus.util.requestFailureHandler(response, options);
			}
		});
	},

	/**
	 * Reload the selected branch: typically useful to refresh a branch caused by changes made by other users
	 */
	onReload: function() {
		var node = this.getView().getLastSelectedTreeNode();
		var treeStore = this.getView().tree.getStore();
		treeStore.load({node:node});
	},


	/**
	 * Reload the needed branches of the tree and select a node after reload
	 * startFromNode: the node to start the reload from
	 * refreshStartFromNode: whether to refresh also the startFromNode or start with their children
	 * selectStartFromNode: whether to select the startNode (true by reset)
	 * parentNode: the parent of the node to be selected. If this is specified
	 * the callback after load will be called to select the nodeIdToSelect.
	 * Should be specified only if another node should be specified as the actually selected
	 * (for reset not needed)
	 * nodeIdToSelect: which node to select after reload
	 */
	refreshTreeOpenBranches: function(startFromNode, refreshStartFromNode,
			selectStartFromNode, parentNode, nodeIdToSelect, issueType, projectType, project) {
		var treeStore = this.getView().tree.getStore();
		var branchNodes;
		if (refreshStartFromNode) {
			branchNodes = [startFromNode];
		} else {
			branchNodes = startFromNode.childNodes;
		}
		Ext.each(branchNodes, function(branchNode, index) {
			var nodeID=branchNode.data['id'];
			if (branchNode.hasChildNodes()) {
				var childrenAreLeaf=branchNode.data['childrenAreLeaf'];
				if(branchNode===treeStore.getRootNode()){
					childrenAreLeaf=true;
				}
				//TODO  fix childrenAreLeaf into tree node model. There seems to be a problem with extJS,
				if(nodeID.indexOf('workflow_projectType_')===0||
					nodeID.indexOf('workflow_project_')===0){
					childrenAreLeaf=true;
				}
				if (childrenAreLeaf && branchNode.isLoaded() &&
						this.branchReloadNeeded(branchNode, issueType, projectType, project)) {
					//real reload is made only at the last branch level (higher level branches are not reloaded)
					var options = {node:branchNode};
					//call the selection callback only if the actual branchNode is the parentNode of the node to be selected
					if (selectStartFromNode || (parentNode && branchNode.data['id']===parentNode.data['id'])) {
						if (selectStartFromNode && CWHF.isNull(nodeIdToSelect)) {
							//if selectStartFromNode the nodeIdToSelect parameter
							//is not required because it should be same as startFromNode
							nodeIdToSelect = startFromNode;
						}
						options.callback = this.selectNode;
						options.scope = {tree:this.getView().tree, nodeIDToSelect:nodeIdToSelect};
					}
					//branchNode.removeAll(false);
					//by loading a node the selection is lost which fires a selectionChange event: treeNode select is executed with empty selections
					//but it does not fire the select event!!!
					treeStore.load(options);
					//this.loadStore(treeStore, options);
				} else {
					this.refreshTreeOpenBranches.call(this, branchNode, false, false, parentNode, nodeIdToSelect, issueType, projectType, project);
				}
			}
		}, this);
	},

	/**
	 * Reduce the number of open branches to reload by filtering by domain parameters if they are specified
	 */
	branchReloadNeeded: function(branchNode, issueType, projectType, project) {
		var branchIssueType = branchNode.get("issueType");
		var branchProjectType = branchNode.get("projectType");
		var branchProject = branchNode.get("project");
		if (issueType && branchIssueType && issueType!==branchIssueType) {
			return false;
		} else {
			if (projectType && branchProjectType && projectType!==branchProjectType) {
				return false;
			} else {
				if (project && branchProject && project!==branchProject) {
					return false;
				}
			}
		}
		return true;
	}
});

