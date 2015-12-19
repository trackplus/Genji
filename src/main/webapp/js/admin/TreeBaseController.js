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
 * Base for all ViewControllers used for TreeBase implementations
 */
Ext.define("com.trackplus.admin.TreeBaseController",{
	extend:"com.trackplus.admin.AdminBaseController",
	/**
	 * The following parameters must be specified for a derived GridBaseController
	 */
	
	baseServerAction:null,
	
	/**
	 * If showGridForFolder is false, then whether the folder details are loaded
	 * manually or through a form load method
	 */
	folderDetailByFormLoad : false,
	/**
	 * Whether to show a grid with a single row instead of other detailed view
	 * of the leaf node
	 */
	// showGridForLeaf: false,
	/**
	 * If showGridForLeaf is false, then whether the leaf details are loaded
	 * manually or through a form load method
	 */
	leafDetailByFormLoad : false,
	
	/**
	 * Whether to completely replace the toolbar for tree node select or only
	 * change the disabled status If true, the toolbar actions for nodes of
	 * different levels change radically, replace the toolbar after each node
	 * select In this case getToolbarActionsForTreeNodeSelect() should be
	 * implemented If false, the toolbar actions are relative constant. In this
	 * case getToolbarActionChangesForTreeNodeSelect() should be implemented
	 */
	replaceToolbarOnTreeNodeSelect : false,
	
	/**
	 * Bae name of the server side action
	 */
	getBaseServerAction:function() {
		return this.baseServerAction;
	},
	
	/**
	 * Needed if tree folder operations should be sent to another server action as leaf operations
	 */
	getFolderAction: function() {
		return this.folderAction;
	},
	
	/**
	 * Get the base action name from struts configuration: either the baseAction
	 * or the folderAction if folderAction is null the base action is taken for
	 * both leafs and folders
	 */
	getNodeBaseAction : function(extraConfig) {
	    if (CWHF.isNull(this.getFolderAction()) || "" === this.getFolderAction()) {
		    // in this case we suppose there is no different struts action for
			// folder and leaf
		    return this.getBaseServerAction();
	    }
	    var isLeaf = true;
	    if (extraConfig ) {
		    isLeaf = extraConfig.isLeaf;
	    }
	    if (isLeaf) {
		    return this.getBaseServerAction();
	    } else {
		    return this.getFolderAction();
	    }
	},
	
	/**
	 * ************tree selection handlers********************* 
	 */
	
	/**
	 * Handler for selecting a node in the tree
	 */
	/*private*/onTreeNodeSelect : function(rowModel, node, index, opts) {
		var leaf = false;
	    if (node) {
		    // typical: called for the select event from the tree
		    leaf = node.isLeaf();
		    this.getView().selectedNodeID = node.get("id");
		    this.getView().selectedNode = node;
	    } else {
		    // onTreeNodeSelect was manually triggered (not by tree node's select
			// event):
		    // for example from from a toolbar action
		    // in this case the tree last selected node from tree is taken
		    node = this.getView().selectedNode;
	    }
	    this.loadDetailPanel(node, leaf, opts);
	    // toolbar content
	    this.actualizeToolbarOnTreeSelect(node);
	},
	
	/**
	 * Handler for selecting/deselecting nodes in the tree
	 */
	/*private*/onTreeSelectionchange : function(rowModel, selections, opts) {
		var selectionIsSimple = this.getView().selectionIsSimple(true);
	    var node = null;
	    if (selectionIsSimple) {
		    node = selections[0];
	    }
	    if (node ) {
		    // typical: called for the select event from the tree
		    this.getView().selectedNodeID = node.data['id'];
		    this.getView().selectedNode = node;
	    } else {
		    // onTreeNodeSelect was manually triggered (not by tree node's select
			// event):
		    // for example from from a toolbar action
		    // in this case the tree last selected node from tree is taken
		    node = this.getView().selectedNode;
	    }
	    if (node) {
		    this.loadDetailPanel(node, false);
	    } else {
		    this.resetDetailPanel();
	    }
	    // toolbar content
	    this.getView().getToolbarActionChangesForTreeNodeSelectionChange(selections);
	},
	
	/*protected*/onTreeNodeDblClick : function(view, record) {
	},
	
	/*protected abstract*/loadDetailPanel : function(node, leaf, opts) {
	    return null;
	},
	
	/**
	 * Reset the detail panel
	 */
	/*private*/resetDetailPanel : function() {
	    var rootMessage = this.getView().getRootMessage();
	    if (rootMessage  || rootMessage !== "") {
		    items = this.getView().getInfoBoxItems(rootMessage);
	    }
	    if (this.getView().centerPanel) {
		    this.getView().mainPanel.remove(this.centerPanel, true);
	    }
	    this.getView().centerPanel = Ext.create('Ext.form.Panel', {
	        region : 'center',
	        border : false,
	        autoScroll : true,
	        header : false,
	        items : items
	    });
	    if (this.replaceCenterPanel) {
	    	//TODO
		    this.replaceCenterPanel.call(this, this.getView().centerPanel);
	    } else {
		    this.getView().mainPanel.add(this.getView().centerPanel);
	    }
	    this.actualizeToolbarOnTreeSelect();
	},
	
	/**
	 * ************toolbar handlers********************* 
	 */
	/* protected abstract */actualizeToolbarOnTreeSelect : function(selectedNode) {
	    if (this.replaceToolbarOnTreeNodeSelect) {
		    // replace the entire toolbar on tree node select
		    // if (selectedNode.length>0) {
		    // only for select (do not call by deselecting the old node)
		    // replace the existing toolbar totally
		    //borderLayout.setActiveToolbarActionList(this.getToolbarActionsForTreeNodeSelect(selectedNode));
	    	this.getView().replaceToolbar(this.getToolbarActionsForTreeNodeSelect(selectedNode));
		    this.adjustToolbarButtonsTooltip(selectedNode, true);
		    // }
	    } else {
		    // in this moment actions are initialized in initAction() methods
			// called in init() and
		    // set on toolbar by
			// borderLayout.setActiveToolbarActionList(treeBaseImpl.getToolbarActions());
		    // leave the current toolbar only enable/disable toolbar actions
		    this.getView().getToolbarActionChangesForTreeNodeSelect(selectedNode);
		    this.adjustToolbarButtonsTooltip(selectedNode, true);
	    }
	},
	
	/**
	 * Get the array of toolbar actions for a tree node select Implement only if
	 * replaceToolbarOnTreeNodeSelect is true
	 */
	/*protected abstract*/getToolbarActionsForTreeNodeSelect: function(selectedNode) {
	    return this.getView().getToolbarActions();
	},
	
	/**
	 * Get the itemId of those actions whose context menu text or toolbar button
	 * tooltip should be changed according to the current selection
	 */
	/*protected abstract*/getActionItemIdsWithContextDependentLabel : function() {
	    return [];
	},
	
	/**
	 * Change the text for an action depending on entity context (from grid or
	 * from three and for folder or for leaf)
	 */
	/* private */adjustToolbarButtonsTooltip : function(selectedNode, fromTree) {
	    actionIds = this.getActionItemIdsWithContextDependentLabel();
	    //var toolbar = borderLayout.getActiveToolbarList();
	    var toolbars = this.getView().getDockedItems('toolbar[dock="top"]');
	    if (toolbars) {
	    	var toolbar = toolbars[0];
		    if (toolbar && actionIds) {
			    for (var i = 0; i < actionIds.length; i++) {
				    var actionId = actionIds[i];
				    var toolbarButton = toolbar.getComponent(actionId);
				    if (toolbarButton ) {
					    var isLeaf = this.getView().selectedIsLeaf(fromTree);
					    toolbarButton.setTooltip(this.getView().getActionTooltip(toolbarButton.tooltipKey, {
					    	tooltipKey: toolbarButton.tooltipKey,//might be needed in getEntityLabel()
					        isLeaf : isLeaf,
					        fromTree : fromTree,
					        selectedRecord : selectedNode
					    }));
				    }
			    }
		    }
	    }
	},
	
	/**
	 * ************context menu handlers********************* 
	 */
	
	/**
	 * Context menu actions for tree
	 */
	/*protected*/getContextMenuActions : function(selectedRecords, selectionIsSimple, fromTree) {
	    return this.getView().getTreeContextMenuActions(selectedRecords, selectionIsSimple);
	},

	/**
	 * Show the context menu in grid
	 */
	/*private*/onTreeNodeCtxMenu : function(tree, record, item, index, evtObj) {
	    this.onCtxMenu(true, record, evtObj);
	    return false;
	},

	/**
	 * Show the context menu in grid
	 */
	/*private*/onCtxMenu : function(fromTree, record, evtObj) {
	    evtObj.stopEvent();
	    this.getView().selectRecord(record, fromTree);
	    var selectedRecords = this.getView().getSelectedRecords(fromTree);
	    var selectionIsSimple = this.getView().selectionIsSimple(fromTree);
	    var actions = this.getContextMenuActions(selectedRecords, selectionIsSimple, fromTree);
	    if (actions  && actions.length > 0) {
		    var treeNodeCtxMenu = this.createContextMenu(record, actions);
		    this.adjustContextMenuText(treeNodeCtxMenu, selectedRecords, fromTree);
		    treeNodeCtxMenu.showAt(evtObj.getXY());
	    }
	    return false;
	},
	
	/**
	 * Change the text for an action depending on entity context (from grid or
	 * from three and for folder or for leaf)
	 */
	/*private*/adjustContextMenuText : function(ctxMenu, selectedRecord, fromTree) {
	    actionIds = this.getActionItemIdsWithContextDependentLabel();
	    for (var i = 0; i < actionIds.length; i++) {
		    var actionId = actionIds[i];
		    var action = ctxMenu.getComponent(actionId);
		    if (action) {
			    var isLeaf = this.getView().selectedIsLeaf(fromTree);
			    var actionLabel = this.getView().getActionTooltip(action.tooltipKey, {
			    	tooltipKey: action.tooltipKey,//might be needed in getEntityLabel()
			        isLeaf : isLeaf,
			        fromTree : fromTree,
			        selectedRecord : selectedRecord
			    });
			    action.setText(actionLabel);
			    action.setTooltip(actionLabel);
		    }
	    }
	},
	
	
	/**
	 * ************detail part helper methods menu handlers********************* 
	 */
	
	/**
	 * Gets the URL for loading the node detail
	 */
	/*private*/getDetailUrl : function(node, add) {
	    var leaf = node  && node.isLeaf();
	    if (leaf || add) {
		    return this.getLeafDetailUrl(node, add);
	    } else {
		    return this.getFolderDetailUrl(node, add);
	    }
	},
	
	/**
	 * The url for getting the leaf detail: either this should be overridden or
	 * the leafDetailUrl should be specified in the config
	 */
	/* protected */getLeafDetailUrl : function(node, add) {
	    return this.getBaseServerAction() + '!load.action';
	},

	/**
	 * The url for getting the folder detail: either this should be overridden
	 * or the folderDetailUrl should be specified in the config
	 */
	/*protected*/getFolderDetailUrl : function(node, add) {
	    return this.getFolderAction() + '!load.action';
	},

	/**
	 * Gets the URL for loading the node detail
	 */
	/* private */getDetailParams : function(node, add, extraDetailParameters) {
	    if (add) {
		    var leaf = node  && node.isLeaf();
		    if (leaf) {
			    return this.getAddLeafParams(extraDetailParameters);
		    } else {
			    return this.getAddFolderParams(extraDetailParameters);
		    }
	    } else {
		    return this.getEditParams(true, extraDetailParameters);
	    }
	},

	/**
	 * Gets the items collection for detail
	 * node: the selected node
	 * add: whether it is add or edit responseJson: the JSON response for
	 * loadSimpleDetailPanel(). (When called from loadDetailPanelWithFormLoad()
	 * not specified)
	 */
	/* private */getDetailItems : function(node, add, responseJson) {
	    var leaf = node  && node.isLeaf();
	    if (leaf || add) {
		    return this.getLeafDetailItems(node, add, responseJson);
	    } else {
		    return this.getFolderDetailItems(node, add, responseJson);
	    }
	},

	/**
	 *
	 * Get the items array to render for folder detail either based on the
	 * response JSON (if folderDetailByFormLoad is false) or based on
	 * form.load() (if folderDetailByFormLoad is true) Should be implemented if
	 * showGridForFolder is false node: the selected node for add/edit add:
	 * whether it is an add or edit operation. When save is called directly from
	 * the detail part (tree-detail, not tree-grid-popup) then 'add' should be
	 * added to the form as hidden parameter (difference between save after edit
	 * and save after add) (alternatively a class level 'add' field should be
	 * declared and added to the save parameters in getSaveParams())
	 * responseJson: the response from the server (if folderDetailByFormLoad is
	 * false, otherwise null)
	 */
	/* protected */getFolderDetailItems : function(node, add, responseJson) {
	    return [];
	},

	/**
	 * Get the items array to render for leaf detail either based on the
	 * response JSON (if leafDetailByFormLoad is false) or based on form.load()
	 * (if leafDetailByFormLoad is true) Should be implemented if
	 * showGridForLeaf is false node: the selected node for add/edit add:
	 * whether it is an add or edit operation. When save is called directly from
	 * the detail part (tree-detail, not tree-grid-popup) then 'add' should be
	 * added to the form as hidden parameter (difference between save after edit
	 * and save after add) (alternatively a class level 'add' field should be
	 * declared and added to the save parameters in getSaveParams())
	 * responseJson: the response from the server (if leafDetailByFormLoad is
	 * false, otherwise null)
	 */
	/* protected */getLeafDetailItems : function(node, add, responseJson) {
	    return [];
	},

	/**
	 * Extra options for the detail panel
	 */
	getDetailPanelConfigOptions : function() {
	    return null;
	},

	/**
	 * Show the node detail using a form.load() method
	 */
	/*private*/loadDetailPanelWithFormLoad : function(node, add, extraDetailParameters) {
	    if (this.getView().centerPanel) {
		    this.getView().mainPanel.remove(this.getView().centerPanel, true);
	    }
	    var panelConfig = {
	        region : 'center',
	        border : false,
	        autoScroll : true,
	        header : false,
	        items : this.getDetailItems(node, add)
	    };
	    var panelConfigOptions = this.getDetailPanelConfigOptions();
	    if (panelConfigOptions ) {
		    for (propertyName in panelConfigOptions) {
			    panelConfig[propertyName] = panelConfigOptions[propertyName];
		    }
	    }
		this.getView().centerPanel = Ext.create('Ext.form.Panel', panelConfig);
		if (this.replaceCenterPanel) {
			//TODO
		    this.replaceCenterPanel.call(this, this.getView().centerPanel);
	    } else {
		    this.getView().mainPanel.add(this.getView().centerPanel);
	    }
	    this.getView().centerPanel.getForm().load({
	        //fromCenterPanel : true,
	        url : this.getDetailUrl(node, add),
	        scope : this,
	        params : this.getDetailParams(node, add, extraDetailParameters),
	        success : function(form, action) {
		        var isLeaf = add;
		        if (node ) {
			        isLeaf = node.isLeaf();
		        }
		        var postDataProcess = this.getEditPostDataProcess(node, isLeaf, add);
		        if (postDataProcess ) {
			        try {
				        postDataProcess.call(this, action.result.data, this.getView().centerPanel);
			        } catch (ex) {
			        }
		        }
	        },
	        failure : function(form, action) {
		        Ext.MessageBox.alert(this.failureTitle, action.response.responseText);
	        }
	    });
	},

	/**
	 * Show the node detail based on responseJson
	 */
	/*private*/loadSimpleDetailPanel : function(node, add, extraDetailParameters) {
	    Ext.Ajax.request({
	        //fromCenterPanel : true,
	        url : this.getDetailUrl(node, add),
	        params : this.getDetailParams(node, add, extraDetailParameters),
	        scope : this,
	        disableCaching : true,
	        success : function(response) {
		        var responseJson = Ext.decode(response.responseText);
		        if (this.getView().centerPanel ) {
			        this.getView().mainPanel.remove(this.getView().centerPanel, true);
		        }
		        this.getView().centerPanel = Ext.create('Ext.panel.Panel', {
		            region : 'center',
		            border : false,
		            autoScroll : true,
		            header : false,
		            items : this.getDetailItems(node, add, responseJson)
		        });
		        if (this.replaceCenterPanel) {
			        this.replaceCenterPanel.call(this, this.getView().centerPanel);
		        } else {
			        this.getView().mainPanel.add(this.getView().centerPanel);
		        }
	        },
	        failure : function(result) {
		        Ext.MessageBox.alert(this.failureTitle, result.responseText);
	        },
	        method : "POST"
	    });
	},
	

	/**
	 * ************add/edit dialog ********************* 
	 */
	
	/**
	 * Parameters for adding to a folder node
	 */
	/*protected*/getAddFolderParams : function(extraDetailParameters) {
	    return this.getAddParams(false, extraDetailParameters);
	},

	/**
	 * Parameters for adding to a leaf node normally you add a node to the
	 * selected leaf node's parent, that means sibling to the selected node
	 */
	/*protected*/getAddLeafParams : function(extraDetailParameters) {
	    return this.getAddParams(true, extraDetailParameters);
	},

	/**
	 * Parameters for adding a new leaf
	 */
	/*private*/getAddParams : function(leaf, extraDetailParameters) {
	    var addParams = null;
	    if (extraDetailParameters ) {
		    addParams = extraDetailParameters;
	    } else {
		    addParams = new Object;
	    }
	    addParams["add"] = true;
	    addParams["leaf"] = leaf;
	    // var addParams = {add:true, leaf:leaf};
	    var addToNode = null;
	    var lastSelectedTreeNode = this.getView().getLastSelectedTreeNode();
	    if (lastSelectedTreeNode) {
		    if (leaf && lastSelectedTreeNode.isLeaf()) {
			    // add leaf to a leaf node -> means add sibling (add node to the
				// parent of the leaf node)
			    addToNode = lastSelectedTreeNode.parentNode;
		    } else {
			    // add child to a folder node
			    addToNode = lastSelectedTreeNode;
		    }
		    if (addToNode) {
			    addParams["node"] = addToNode.get("id");
		    }
	    } else {
		    // adding when no node is selected: for ex. project specific
			// queries: add means adding to the (project specific branch) root
		    addParams["node"] = this.getView().tree.getRootNode().get("id");
	    }
	    return addParams;
	},

	/**
	 * Parameters for editing an existing entity recordData: the selected entity
	 * data
	 */
	/*protected*/getEditParams : function(fromTree, extraDetailParameters) {
	    var editParams = null;
	    if (extraDetailParameters) {
		    editParams = extraDetailParameters;
	    } else {
		    editParams = new Object();
	    }
	    var recordData = this.getView().getSingleSelectedRecordData(fromTree);
	    if (recordData) {
		    var nodeID = null;
		    if (fromTree) {
			    nodeID = recordData["id"];
		    } else {
			    nodeID = recordData["node"];
		    }
		    if (nodeID) {
			    // return {node:nodeID};
			    editParams["node"] = nodeID;
		    }
	    }
	    // should not be null because dynamic parameters might be added right
		// before submit
	    // (in either submitHandler() or preSubmitProcess() implementations)
	    return editParams;// new Object();
	},

	/**
	 * The method to process the data to be loaded arrived from the server
	 */
	/*protected*/getEditPostDataProcess : function(record, isLeaf, add) {
	    var recordData = null;
	    if (record ) {
		    recordData = record.data;
	    }
	    if (isLeaf) {
		    if (this.getEditLeafPostDataProcess ) {
			    return this.getEditLeafPostDataProcess(recordData, add);
		    }
	    } else {
		    if (this.getEditFolderPostDataProcess ) {
			    return this.getEditFolderPostDataProcess(recordData, add);
		    }
	    }
	    return null;
	},

	/**
	 * Method to process the data to be loaded arrived from the server by
	 * editing the folder
	 */
	/*protected abstract*/getEditFolderPostDataProcess : null,

	/**
	 * Method to process the data to be loaded arrived from the server by
	 * editing the leaf
	 */
	/*protected abstract*/getEditLeafPostDataProcess : null,

	/**
	 * Parameter name for the submitted entityID(s) by delete If
	 * allowMultipleSelections===false then the same entityID can be used for
	 * delete and edit submits If allowMultipleSelections===true this should
	 * return another name (the submitted value will be stored on the server
	 * typically in an Integer[])
	 */
	/*protected*/getDeleteParamName : function(extraConfig) {
	    return "node";
	},

	/**
	 * Helper for preparing the params get the ID based on the record and extra
	 * config
	 */
	/*protected*/getRecordID : function(recordData, extraConfig) {
	    var fromTree = null;
	    if (extraConfig ) {
		    fromTree = extraConfig.fromTree;
	    }
	    if (fromTree) {
		    return recordData["id"];
	    } else {
		    return recordData["node"];
	    }
	},
	
	/*refreshSimpleTree: function(refreshParamsObject) {
		//nodeID is null after delete, and not null after add/edit
		var nodeID = refreshParamsObject.node;
		var reloadTree = refreshParamsObject.reloadTree;
		if (reloadTree) {
			//tree reload is needed: after delete, add and edit with label change
			var treeStore = this.getView().tree.getStore();
			var options = {};
			if (CWHF.isNull(nodeID) && treeStore.getRootNode().hasChildNodes()) {
				var childNodes = treeStore.getRootNode().childNodes;
				if (childNodes) {
					var numberOfChildren = childNodes.length;
					if (numberOfChildren>0) {
						//select the first row after delete
						var firstNode = treeStore.getRootNode().getChildAt(0);
						if (firstNode) {
							var selectedNodeID = null;
							if (this.selectNode) {
								selectedNodeID = this.getView().selectedNode.get("id");
							}
							nodeID = firstNode.get("id");
							if (nodeID===selectedNodeID) {
								//the first row was deleted, try to select the second row if exists
								nodeID = null;
								if (numberOfChildren>1) {
									var secondNode = treeStore.getRootNode().getChildAt(1);
									if (secondNode) {
										nodeID = secondNode.get("id");
									}
								}
							}
						}
					}
				}
			}
			if (nodeID) {
				options.callback = this.getView().selectNode;
				options.scope = {tree:this.getView().tree, nodeIdToSelect:nodeID}
			} else {
				//no node to select reset the detail panel and toolbar
				options.callback = this.getView().resetDetailPanel;
				options.scope = this;
			}
			treeStore.load(options);
		} else {
			//edit without label change
			var selectionModel = this.getView().tree.getSelectionModel();
			if (nodeID) {
				//get the node by id
				var nodeToSelect = this.getView().tree.getStore().getNodeById(nodeID);
				if (nodeToSelect) {
					selectionModel.select(nodeToSelect);
				}
			}
		}
	},*/
	
	/**
	 * Gather selected ID as a comma separated string
	 * selectedRecords - the selected record(s): simple or multiple selection:
	 * 	if simple the selection the record, if multiple an array of records
	 * getIDFormEntityFunction - function for getting the ID from an entity JSON
	 * extraConfig
	 */
	/*private*/getSelectedIDs : function(selectedRecords, extraConfig) {
	    var fromTree = null;
	    if (extraConfig ) {
		    fromTree = extraConfig.fromTree;
	    }
	    if (fromTree) {
		    //multiple tree nodes
		    allowMultiple = this.getView().allowMultipleNodeSelections;
	    } else {
		    //multiple grid rows
		    allowMultiple = this.getView().allowMultipleSelections;
	    }
	    if (allowMultiple) {
		    var idArray = [];
		    Ext.Array.forEach(selectedRecords, function(record) {
			    idArray.push(this.getRecordID(record.data, extraConfig));
		    }, this);
		    return idArray.join();
	    } else {
		    return this.getRecordID(selectedRecords.data, extraConfig);
	    }
	}
	
})
