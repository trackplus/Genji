/**
 * Genji Scrum Tool and Issue Tracker
 * Copyright (C) 2015 Steinbeis GmbH & Co. KG Task Management Solutions

 * <a href="http://www.trackplus.com">Genji Scrum Tool</a>

 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

/* $Id:$ */

com.trackplus.util.RefreshAfterSubmit = function() {
}

/**
 * Refresh a tree one single level
 */
com.trackplus.util.RefreshAfterSubmit.refreshSimpleTree = function(refreshParamsObject) {
	//nodeID is null after delete, and not null after add/edit
	var nodeID = refreshParamsObject.node;
	var reloadTree = refreshParamsObject.reloadTree;
	if (reloadTree) {
		//tree reload is needed: after delete, add and edit with label change
		var treeStore = this.tree.getStore();
		var options = {};
		if (nodeID==null && treeStore.getRootNode().hasChildNodes()) {
			var childNodes = treeStore.getRootNode().childNodes;
			if (childNodes!=null) {
				var numberOfChildren = childNodes.length;
				if (numberOfChildren>0) {
					//select the first row after delete
					var firstNode = treeStore.getRootNode().getChildAt(0);
					if (firstNode!=null) {
						var selectedNodeID = null;
						if (this.selectNode!=null) {
							selectedNodeID = this.selectedNode.get("id");
						}
						nodeID = firstNode.get("id");
						if (nodeID==selectedNodeID) {
							//the first row was deleted, try to select the second row if exists
							nodeID = null;
							if (numberOfChildren>1) {
								var secondNode = treeStore.getRootNode().getChildAt(1);
								if (secondNode!=null) {
									nodeID = secondNode.get("id");
								}
							}
						}
					}
				}
			}
		}
		if (nodeID!=null) {
			options.callback = this.selectNode;
			options.scope = {tree:this.tree, nodeIdToSelect:nodeID}
		} else {
			//no node to select reset the detail panel and toolbar
			options.callback = this.resetDetailPanel;
			options.scope = this;
		}
		treeStore.load(options);
	} else {
		//edit without label change
		var selectionModel = this.tree.getSelectionModel();
		if (nodeID!=null) {
			//get the node by id
			var nodeToSelect = this.tree.getStore().getNodeById(nodeID);
			if (nodeToSelect!=null) {
				selectionModel.select(nodeToSelect);
			}
		}
	}
}

/**
 * Refresh the tree after submit
 */
com.trackplus.util.RefreshAfterSubmit.refreshTreeAfterSubmit = function(refreshParametersObject) {
	var nodeIDToSelect = refreshParametersObject.nodeIDToSelect;
	var nodeIDToReload = refreshParametersObject.nodeIDToReload;
	var reloadTree = refreshParametersObject.reloadTree;
    var resetDetail = refreshParametersObject.resetDetail;
	if (reloadTree) {
		//tree reload is needed: after delete, add and edit with label change
		var treeStore = this.tree.getStore();
		var options = {};
		if (nodeIDToReload!=null) {
			options.node = this.tree.getStore().getNodeById(nodeIDToReload);
		}
		if (nodeIDToSelect!=null) {
			options.callback = com.trackplus.util.RefreshAfterSubmit.selectTreeNodeAfterReload;
			options.scope = {treePanel:this.tree, nodeIDToSelect:nodeIDToSelect}
		} else {
			if (resetDetail) {
				options.callback = this.resetDetailPanel;
				options.scope = this;
			}
		}
		treeStore.load(options);
	} else {
		//edit without label change
		com.trackplus.util.RefreshAfterSubmit.selectTreeNodeAfterReload.call({treePanel:this.tree, nodeIDToSelect:nodeIDToSelect});
	}
}

/**
 *  Refreshes the grid and tree after an operation requiring a refresh
 *  The scope should contain tree and grid attributes
 * 	refreshParametersObject may contain the following parameters:
 * 	nodeIDToReload: the branch node to reload
 * 	nodeIDToSelect: the tree node to select after branch reload
 * 	rowToSelect: the grid row to select after grid store reload
**/
com.trackplus.util.RefreshAfterSubmit.refreshGridAndTreeAfterSubmit = function(refreshParametersObject) {
	var nodeIDToReload = null;
	var nodeIDToSelect = null;
	var rowToSelect = null;
	if (refreshParametersObject!=null) {
		nodeIDToReload = refreshParametersObject.nodeIDToReload;
		nodeIDToSelect = refreshParametersObject.nodeIDToSelect;
		rowToSelect = refreshParametersObject.rowToSelect;
	}
	var nodeToReload;
	if (nodeIDToReload!=null) {
		nodeToReload=this.tree.getStore().getNodeById(nodeIDToReload);
	} else {
		if (this.selectedNode!=null) {
			//typically no nodeIDToReload specified when this.selectedNode is the one to reload
			nodeToReload = this.selectedNode;
			nodeIDToReload = this.selectedNodeID;
		} else {
			//no node was selected to add to means adding to the root
			//(for example by project configuration add a project specific filter directly to the "branch" root)
			nodeToReload = this.tree.getRootNode();
			nodeIDToReload = this.rootID;
		}
	}

	if (nodeToReload!=null) {
		if (nodeToReload.isLoaded()) {
			//reload the node only if the node was already loaded (expanded)
			//set the global this.rowToSelect for selecting a row after the grid has been reloaded. See treeWithGrid.onGridStoreLoad()
			//(in selectTreeNodeAfterReload the grid row can't be selected: see selectTreeNodeAfterReload
			this.rowIdToSelect = rowToSelect;
			if (nodeIDToSelect==null) {
				nodeIDToSelect = nodeIDToReload;
			}
			var scope={treePanel:this.tree, grid:this.grid, nodeIDToSelect:nodeIDToSelect, rowToSelect:rowToSelect, scope: this};
			this.tree.store.load({node:nodeToReload,
								callback:com.trackplus.util.RefreshAfterSubmit.selectTreeNodeAfterReload,
								synchronous:false,
								scope:scope});
		} else {
			//tree node was not loaded previously: refresh the grid only
			if (this.grid!=null) {
				com.trackplus.util.RefreshAfterSubmit.refreshGridAfterSubmit.call(this, {rowToSelect:rowToSelect});

			}
		}
	}
}

/**
 * Refreshes a grid's store and selects a row by rowToSelect parameter if it is specified
 * Called from selectAfterReload() (grid and tree refresh) and from simple grid CRUD
 */
/*com.trackplus.util.RefreshAfterSubmit.refreshGridAfterSubmit = function(grid, rowIdToSelect) {
	if (grid!=null) {
		grid.getStore().load({scope:this, callback:function(){
			if (rowIdToSelect!=null) {
				var row = grid.getStore().getById(rowIdToSelect);
				if (row!=null) {
					var gridSelectionModel = grid.getSelectionModel();
					gridSelectionModel.select(row);
				}
			}
		}})
	}
}*/

/**
 * Callback after the branch is reloaded
 */
com.trackplus.util.RefreshAfterSubmit.selectTreeNodeAfterReload = function() {
	//re-selection should be made by code because after reload the tree node selection gets lost:
	//1. if the node to select is the reloaded node: the reloaded node looses the selection by the this.tree.store.load()
	//2. if the node to select is a child of the reloaded node it looses the selection anyway
	//Although this tree node selection by code will reload the grid (because it triggers the treeNodeSelect()),
	//but the corresponding grid row can't be selected here directly (if it would be the case) that's why it is selected
	//in treeWithGrid.onGridStoreLoad() after the global this.rowToSelect was set
	var treeSelectionModel = this.treePanel.getSelectionModel();
	if (this.nodeIDToSelect!=null) {
		var nodeToSelect=this.treePanel.getStore().getNodeById(this.nodeIDToSelect);
		if (nodeToSelect!=null) {
			if (treeSelectionModel.isSelected(nodeToSelect)) {
				//if already selected deselect it first because
				//otherwise the select does not trigger the select handler (for refreshing the detail part)
				treeSelectionModel.deselect(nodeToSelect);
			}
			treeSelectionModel.select(nodeToSelect);
			if (!nodeToSelect.isVisible()) {
				this.treePanel.expandPath(nodeToSelect.getPath());
			}
		}
	}
}

/**
 * Refreshes a grid's store and selects a row by rowToSelect parameter if it is specified
 * Called from selectAfterReload() (grid and tree refresh) and from simple grid CRUD
 */
com.trackplus.util.RefreshAfterSubmit.refreshGridAfterSubmit = function(refreshParametersObject) {
	this.grid.getStore().load({scope:this, callback:function(){
		if (refreshParametersObject!=null) {
			var rowToSelect = refreshParametersObject.rowToSelect;
			if (rowToSelect!=null) {
				var row = this.grid.getStore().getById(rowToSelect);
				if (row!=null) {
					var gridSelectionModel = this.grid.getSelectionModel();
					gridSelectionModel.select(row);
				}
			}
		}
	}})
}
