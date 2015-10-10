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


/**
 * Base class for all tree-based pages (tree with grid and tree detail)
 */
Ext.define('com.trackplus.admin.TreeBase', {
	extend : 'com.trackplus.admin.CrudBase',
	config : {
	    /**
		 * The ID of the root node
		 */
	    rootID : '',
	    /**
		 * Whether more tree nodes can be selected on the same time
		 */
	    allowMultipleNodeSelections : false,
	    /**
		 * The base struts action name (without method name) in struts.xml for
		 * the set this if it differs from base action (folder and leaf
		 * operations are in different struts actions)
		 */
	    folderAction : '',
	    /**
		 * the url for folder detail if showGridForFolder is false
		 */
	    folderDetailUrl : null,
	    /**
		 * the url for leaf detail if showGridForLeaf is false
		 */
	    leafDetailUrl : null,
	    // the width of the tree panel
	    treeWidth : 200,
	    replaceCenterPanel : null
	},
	treeWidth : 200,
	rootID : '',
	/**
	 * Whether selecting a folder node in the tree results in a grid in the
	 * detail part or other detail info
	 */
	showGridForFolder : false,
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
	 * whether to allow cut/copy-paste in the tree
	 */
	useCopyPaste : false,
	/**
	 * whether to enable drag and drop on the tree
	 */
	dragAndDropOnTree : false,
	/**
	 * Whether the tree has context menu listener
	 */
	treeHasItemcontextmenu : true,
	/**
	 * Whether the tree has double click listener
	 */
	treeHasItemdblclick : true,
	/**
	 * The tree
	 */
	tree : null,
	/**
	 * The selected node in the tree
	 */
	selectedNode : null,
	/**
	 * The id of the selected node in the tree
	 */
	selectedNodeID : null,
	/**
	 * the node to cut/copy
	 */
	cutCopyNode : null,
	/**
	 * Whether it is copy/paste or cut/paste
	 */
	copy : false,

	/**
	 * The panel with the tree and detail part
	 */
	mainPanel : null,

	/**
	 * The panel where the grid (for folder node) or node detail (for leaf node)
	 * is rendered
	 */
	centerPanel : null,

	constructor : function(config) {
	    var config = config || {};
	    this.initialConfig = config;
	    Ext.apply(this, config);
	    this.init();
	},

	/**
	 * Initialization method
	 */
	/* protected */init : function() {
	    this.initActions();
	},

	/**
	 * Get the base action name from struts configuration: either the baseAction
	 * or the folderAction if folderAction is null the base action is taken for
	 * both leafs and folders
	 */
	getStrutsBaseAction : function(extraConfig) {
	    if (this.folderAction == null || "" == this.folderAction) {
		    // in this case we suppose there is no different struts action for
			// folder and leaf
		    return this.baseAction;
	    }
	    var isLeaf = true;
	    if (extraConfig != null) {
		    isLeaf = extraConfig.isLeaf;
	    }
	    if (isLeaf) {
		    return this.baseAction;
	    } else {
		    return this.folderAction;
	    }
	},

	/**
	 * ********************************************action/context menu related
	 * methods**********************************************
	 */
	/**
	 * The iconCls for the add button
	 */
	/* protected */getAddFolderIconCls : function() {
	    return 'add';
	},

	/**
	 * The iconCls for the "cut" button
	 */
	/* protected */getCutIconCls : function() {
	    return 'cut';
	},
	/**
	 * The key for "cut" button text
	 */
	/* protected */getCutButtonKey : function() {
	    return 'common.btn.cut';
	},
	/**
	 * The key for "cut" context menu button' text
	 */
	/* protected */getCutTitleKey : function() {
	    return 'common.lbl.cut';
	},

	/**
	 * The iconCls for the "paste" button
	 */
	/* protected */getPasteIconCls : function() {
	    return 'paste';
	},
	/**
	 * The key for "paste" button text
	 */
	/* protected */getPasteButtonKey : function() {
	    return 'common.btn.paste';
	},
	/**
	 * The key for "paste" context menu button' text
	 */
	/* protected */getPasteTitleKey : function() {
	    return 'common.lbl.paste';
	},

	/**
	 * Gets the tree's fields
	 */
	/* protected */getTreeFields : function() {
	    return [ {
	        name : 'id',
	        mapping : 'id',
	        type : 'string'
	    }, {
	        name : 'text',
	        mapping : 'text',
	        type : 'string'
	    }, {
	        name : 'leaf',
	        mapping : 'leaf',
	        type : 'boolean'
	    }, {
	        name : 'iconCls',
	        mapping : 'iconCls',
	        type : 'string'
	    } ];
	},

	/**
	 * Extra parameters additionally to 'node' by expanding a node from the tree
	 * If the data in the tree is homogeneous then the node can be decoded
	 * uniquely But if not then extra parameters are needed to decide how to
	 * interpret the node id
	 */
	/* protected */getTreeExpandExtraParams : function(node) {
	    return null;
	},

	/**
	 * Get the itemId of those actions whose context menu text or toolbar button
	 * tooltip should be changed according to the current selection
	 */
	/* protected abstract */getActionItemIdsWithContextDependentLabel : function() {
	    return [];
	},

	/**
	 * Get the actions available in context menu depending on the currently
	 * selected row
	 *
	 */
	/* protected abstract */getTreeContextMenuActions : function(selectedRecord, selectionIsSimple) {
	    return [];
	},

	/**
	 * Context menu actions for tree
	 */
	/* protected */getContextMenuActions : function(selectedRecords, selectionIsSimple, fromTree) {
	    return this.getTreeContextMenuActions(selectedRecords, selectionIsSimple);
	},

	/**
	 * Show the context menu in grid
	 */
	/* private */onTreeNodeCtxMenu : function(tree, record, item, index, evtObj) {
	    this.onCtxMenu(true, record, evtObj);
	    return false;
	},

	/**
	 * Show the context menu in grid
	 */
	/* private */onCtxMenu : function(fromTree, record, evtObj) {
	    evtObj.stopEvent();
	    this.selectRecord(record, fromTree);
	    var selectedRecords = this.getSelectedRecords(fromTree);
	    var selectionIsSimple = this.selectionIsSimple(fromTree);
	    var actions = this.getContextMenuActions(selectedRecords, selectionIsSimple, fromTree);
	    if (actions != null && actions.length > 0) {
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
	/* private */adjustContextMenuText : function(ctxMenu, selectedRecord, fromTree) {
	    actionIds = this.getActionItemIdsWithContextDependentLabel();
	    for (var i = 0; i < actionIds.length; i++) {
		    var actionId = actionIds[i];
		    var action = ctxMenu.getComponent(actionId);
		    if (action != null) {
			    var isLeaf = this.selectedIsLeaf(fromTree);
			    action.text = this.getTitle(action.tooltipKey, {
			        isLeaf : isLeaf,
			        fromTree : fromTree,
			        selectedRecord : selectedRecord
			    });
			    action.setTooltip(this.getTitle(action.tooltipKey, {
			        isLeaf : isLeaf,
			        fromTree : fromTree,
			        selectedRecord : selectedRecord
			    }));
		    }
	    }
	},

	/**
	 * Change the text for an action depending on entity context (from grid or
	 * from three and for folder or for leaf)
	 */
	/* private */adjustToolbarButtonsTooltip : function(selectedNode, fromTree) {
	    actionIds = this.getActionItemIdsWithContextDependentLabel();
	    var toolbar = borderLayout.getActiveToolbarList();
	    for (var i = 0; i < actionIds.length; i++) {
		    var actionId = actionIds[i];
		    var toolbarButton = toolbar.getComponent(actionId);
		    if (toolbarButton != null) {
			    var isLeaf = this.selectedIsLeaf(fromTree);
			    toolbarButton.setTooltip(this.getTitle(toolbarButton.tooltipKey, {
			        isLeaf : isLeaf,
			        fromTree : fromTree,
			        selectedRecord : selectedNode
			    }));
		    }
	    }
	},

	/**
	 * ********************************************tree
	 * methods**********************************************
	 */

	/**
	 * Whether drag and drop on tree is possible
	 */
	/* protected */hasDragAndDropOnTree : function(node) {
	    return this.dragAndDropOnTree;
	},

	/**
	 * The drag and drop configuration object
	 */
	/* protected */getDragAndDropOnTreeConfig : function() {
	    return {
	        ptype : 'treeviewdragdrop',
	        dragGroup : this.baseAction + 'DDGroup',
	        dropGroup : this.baseAction + 'DDGroup',
	        appendOnly : true,
	        enableDrag : true,
	        enableDrop : true
	    };
	},

	/**
	 * Initialize the tree
	 */
	/* private */initTree : function() {
	    var modelName = this.baseAction + "TreeModel";
	    Ext.define(modelName, {
	        extend : 'Ext.data.Model',
	        proxy : {
	            fromCenterPanel : true,
	            type : 'ajax',
	            url : this.getStrutsBaseAction({
		            isLeaf : false
	            }) + '!expand.action',
	            // for expanding the root
	            extraParams : this.getTreeExpandExtraParams(),
	            listeners : {
		            exception : function(proxy, response, operation, opts) {
			            var responseJson = Ext.decode(response.responseText);
			            com.trackplus.util.showError(responseJson);
		            }
	            }
	        },
	        fields : this.getTreeFields()
	    });

	    var treeStore = Ext.create('Ext.data.TreeStore', {
	        model : modelName,
	        root : {
	            expanded : true,
	            id : this.rootID
	        }
	    });
	    var treeConfig = {
	        xtype : 'treepanel',
	        store : treeStore,
	        selModel : this.getTreeSelectionModel(),
	        region : 'west',
	        autoScroll : true,
	        width : this.treeWidth,
	        margin : '0 -5 0 0',
	        split : true,
	        // collapsible: true,
	        header : false,
	        border : false,
	        bodyBorder : false,
	        style : {
		        borderRight : '1px solid #D0D0D0'
	        },
	        animate : false,
	        useArrows : true,
	        containerScroll : true,
	        rootVisible : false,
	        cls : 'simpleTree'
	    };
	    /*
		 * The (re)loading a branch of a tree deactivates the node (looses a
		 * selection) and the selectionChange event is called with no selection
		 * (that means also by loosing a selection). It removes all from the
		 * detail panel but leaves it empty because there is no new selection to
		 * present the details for That's why, instead of selectionchange we use
		 * select event
		 */
	    var treeListeners = new Object();
	    if (this.allowMultipleNodeSelections) {
		    treeListeners.selectionchange = {
		        fn : this.treeNodeSelectionchange,
		        scope : this
		    };
	    } else {
		    treeListeners.select = {
		        fn : this.treeNodeSelect,
		        scope : this
		    };
	    }
	    // var treeListeners = {/*selectionchange:*/select:
		// {fn:this.treeNodeSelect, scope:this}};
	    if (this.treeHasItemcontextmenu) {
		    treeListeners.itemcontextmenu = {
		        fn : this.onTreeNodeCtxMenu,
		        scope : this
		    };
	    }
	    if (this.treeHasItemdblclick) {
		    treeListeners.itemdblclick = {
		        fn : this.onTreeNodeDblClick,
		        scope : this
		    };
	    }
	    treeConfig.listeners = treeListeners;
	    if (this.hasDragAndDropOnTree()) {
		    // add drag and drop
		    treeConfig.viewConfig = {
		        plugins : this.getDragAndDropOnTreeConfig(),/*
															 * { ptype:
															 * 'treeviewdragdrop',
															 * dragGroup:
															 * this.baseAction
															 * +'DDGroup',
															 * dropGroup:
															 * this.baseAction
															 * +'DDGroup',
															 * appendOnly: true,
															 * enableDrag: true,
															 * enableDrop: true },
															 */
		        // allowCopy: true,
		        listeners : {
		            beforedrop : {
		                scope : this,
		                fn : function(node, data, overModel, dropPosition) {
			                var nodeToDrag = data.records[0];
			                var copy = data.copy;
			                if (copy == null) {
				                // move by default
				                copy = false;
			                }
			                return this.canDragDropNode(nodeToDrag, copy, overModel);
		                }
		            },
		            drop : {
		                scope : this,
		                fn : function(node, data, overModel, dropPosition) {
			                var copy = data.copy;
			                if (copy == null) {
				                // move by default
				                copy = false;
			                }
			                this.onDropTreeNode(data.records[0], overModel, copy);
		                }
		            }
		        }
		    };
	    }
	    this.tree = Ext.create('Ext.tree.Panel', treeConfig);
	    // treeStore.on('beforeexpand', function(node) {
	    treeStore.on('beforeload', function(store, operation) {
		    /*
			 * Dynamic extra parameters depending on the expanded node
			 * additionally to 'node' parameter by expanding a node from the
			 * tree If the data in the tree is homogeneous then the node can be
			 * decoded uniquely But if not then extra parameters are needed to
			 * decide how to interpret the node id The last extraParams value
			 * wins, so by reloading a tree node for refreshing the content the
			 * extraParams should also be actualized
			 */
		    if (operation.node != null) {
			    var extraParams = this.getTreeExpandExtraParams(operation.node);
			    if (extraParams != null) {
				    treeStore.proxy.extraParams = extraParams;
			    }
		    }
	    }, this);
	    treeStore.on('load', function(treeStore, node) {
		    this.onTreeNodeLoad(treeStore, node);
	    }, this);
	},

	/**
	 * Get the configuration for selection model
	 */
	/* protected */getTreeSelectionModel : function() {
	    var selectionModelConfig = new Object;
	    if (this.allowMultipleNodeSelections) {
		    selectionModelConfig.mode = "MULTI";
	    } else {
		    selectionModelConfig.mode = "SINGLE";
		    if (this.allowDeselect) {
			    selectionModelConfig.allowDeselect = this.allowDeselect;
		    }
	    }
	    if (this.treeSelectionModel == null) {
		    return Ext.create("Ext.selection.TreeModel", selectionModelConfig);
	    } else {
		    return this.treeSelectionModel;
	    }
	},

	/**
	 * Handler after loading the node
	 */
	onTreeNodeLoad : function(treeStore, node) {
	    // typically used to make something after loading the children of the
		// root
	    // if (node.isRoot()) {

	    // }
	},

	/**
	 * Return false if dragging this node is not allowed
	 */
	canDragDropNode : function(nodeToDrag, copy, overModel) {
	    /*
		 * var leaf = nodeToDrag.data["leaf"]; //by default only leafs are
		 * allowed to drag if (!leaf) { return false; }
		 */
	    return false;
	},

	/**
	 * Which base action should execute the paste/drop operation depending on
	 * the dragged node's leaf property the folderAction or the baseAction (leaf
	 * action) (can be that independently from the leaf property the same action
	 * should be used)
	 */
	/* protected */getDragDropBaseAction : function(draggedNodeIsLeaf) {
	    return this.getStrutsBaseAction({
		    isLeaf : draggedNodeIsLeaf
	    });
	},

	/**
	 * Paste a node in the tree after copy/cut
	 */
	onDropTreeNode : function(nodeFrom, nodeTo, copy) {
	    var leafTo = nodeTo.isLeaf();
	    if (leafTo) {
		    nodeTo = nodeTo.parentNode;
	    }
	    var isLeaf = this.selectedIsLeaf(nodeFrom);
	    var strutsBaseAction = this.getDragDropBaseAction(isLeaf);
	    // After a paste the nodeTo should be refreshed.
	    // If the paste was after a cut the nodeFrom should be also refreshed.
	    // Get the parent hierarchy of the nodeTo, because it should be expanded
		// after paste.
	    // Although the path till nodeTo is already expanded if the paste is
		// made after a cut (not copy)
	    // in and the nodeTo is in a subtree of the nodeFrom after the nodeFrom
		// will be refreshed it will "hide"
	    // the path to nodeTo, that's why the path should be expanded manually
		// using this array
	    /*
		 * var parentHierarchy = [nodeTo.raw["id"]]; var parentNode =
		 * nodeTo.parentNode; while (parentNode!=null && parentNode.raw!=null)
		 * {//parentNode.raw is null for root node
		 * parentHierarchy.push(parentNode.raw["id"]); parentNode =
		 * parentNode.parentNode; }
		 */

	    Ext.Ajax.request({
	        fromCenterPanel : true,
	        url : strutsBaseAction + '!copy.action',
	        params : {
	            nodeFrom : nodeFrom.data["id"],
	            nodeTo : nodeTo.data["id"],
	            copy : copy
	        },
	        scope : this,
	        disableCaching : true,
	        success : function(response) {
		        var responseJson = Ext.decode(response.responseText);
		        if (responseJson.success == true) {
			        // reload the nodeFrom but only if cut
			        if (!copy) {
				        // after a cut the nodeFrom's parent should be also
						// refreshed
				        // if the nodeTo is in a subtree of the nodeFrom after
						// the nodeFrom's parent would be refreshed it would
						// "hide"
				        // the path to the already expanded nodeTo. That's why
						// we do not refresh it but delete it directly
				        nodeFrom.remove(true);
				        /*
						 * var fromParentID = nodeFrom.parentNode.raw["id"];
						 * //reload of nodeFrom if cut
						 * this.refreshGridAndTreeAfterSubmit({nodeIDToReload:fromParentID});
						 * //if nodeFrom reload "hides" the nodeTo then expand
						 * the path till nodeTo again if
						 * (parentHierarchy.indexOf(fromParentID)!=-1) { var
						 * parentNode =
						 * this.tree.getStore().getNodeById(parentHierarchy.pop());
						 * while (parentNode!=null) { parentNode.expand(); var
						 * nextId = parentHierarchy.pop(); if (nextId!=null) {
						 * parentNode =
						 * this.tree.getStore().getNodeById(nextId); } else {
						 * parentNode = null; } } }
						 */
			        }
			        // nodeTo.removeAll(true);
			        com.trackplus.util.RefreshAfterSubmit.refreshGridAndTreeAfterSubmit.call(this, {
			            nodeIDToReload : nodeTo.data["id"],
			            nodeIDToSelect : responseJson.node
			        });
		        } else {
			        if (responseJson.errorMessage != null) {
				        // no right to delete
				        Ext.MessageBox.alert(this.failureTitle, responseJson.errorMessage);
			        }
		        }
	        },
	        failure : function(result) {
		        Ext.MessageBox.alert(this.failureTitle, result.responseText);
	        },
	        method : "POST"
	    });
	},

	/* protected */onTreeNodeDblClick : function(view, record) {
	},

	/**
	 * The message to appear first time after selecting this menu entry Is
	 * should be shown by selecting the root but the root is typically not
	 * visible
	 */
	/* protected abstract */getRootMessage : function() {
	    return "";
	    // return getText('...');
	},

	/**
	 * Initialize the center panel with data by initializing the tree and
	 * eventually the detail part (if it is independent of the selected tree
	 * node) By default only the tree is initialized
	 */
	/* protected */initCenterPanel : function() {
	    this.initTree();
	},

	/**
	 * Creates the center panel containing the tree and the details part
	 */
	createCenterPanel : function() {
	    var items = [];
	    var rootMessage = this.getRootMessage();
	    if (rootMessage != null || rootMessage != "") {
		    items = this.getInfoBoxItems(rootMessage);
	    }
	    this.centerPanel = Ext.create('Ext.form.Panel', {
	        region : 'center',
	        /*
			 * style:{ borderLeft:'1px solid #D0D0D0' },
			 */
	        border : false,
	        autoScroll : true,
	        header : false,
	        items : items
	    });
	    this.initCenterPanel();
	    this.mainPanel = Ext.create('Ext.panel.Panel', {
	        layout : 'border',
	        region : 'center',
	        border : false,
	        items : [ this.tree, this.centerPanel ]
	    });
	    this.postInitCenterPanel(this.tree.getRootNode());
	    return this.mainPanel;
	},

	/**
	 * Callback called after creating the center panel
	 */
	postInitCenterPanel : function(rootNode) {

	},

	getInfoBoxItems : function(content) {
	    if (content != null && content != "") {
		    return [ {
		        xtype : 'component',
		        cls : 'infoBox_bottomBorder',
		        border : true,
		        html : content
		    } ];
	    } else {
		    return [];
	    }
	},

	/**
	 * Handler for selecting a node in the tree
	 */
	/* private */treeNodeSelect : function(rowModel, node, index, opts) {
	    var leaf = false;
	    if (node != null) {
		    // typical: called for the select event from the tree
		    leaf = node.data['leaf'];
		    this.selectedNodeID = node.data['id'];
		    this.selectedNode = node;
	    } else {
		    // treeNodeSelect was manually triggered (not by tree node's select
			// event):
		    // for example from from a toolbar action
		    // in this case the tree last selected node from tree is taken
		    node = this.selectedNode;
	    }
	    this.loadDetailPanel(node, leaf, opts);
	    // toolbar content
	    this.actualizeToolbarOnTreeSelect(node);
	},

	/**
	 * Handler for selecting a node in the tree
	 */
	/* private */treeNodeSelectionchange : function(rowModel, selections, opts) {
	    var selectionIsSimple = this.selectionIsSimple(true);
	    var node = null;
	    if (selectionIsSimple) {
		    node = selections[0];
	    }
	    if (node != null) {
		    // typical: called for the select event from the tree
		    this.selectedNodeID = node.data['id'];
		    this.selectedNode = node;
	    } else {
		    // treeNodeSelect was manually triggered (not by tree node's select
			// event):
		    // for example from from a toolbar action
		    // in this case the tree last selected node from tree is taken
		    node = this.selectedNode;
	    }
	    if (node != null) {
		    this.loadDetailPanel(node, false);
	    } else {
		    this.resetDetailPanel();
	    }
	    // toolbar content
	    this.getToolbarActionChangesForTreeNodeSelectionChange(selections);
	},

	/* protected abstract */actualizeToolbarOnTreeSelect : function(selectedNode) {
	    if (this.replaceToolbarOnTreeNodeSelect) {
		    // replace the entire toolbar on tree node select
		    // if (selectedNode.length>0) {
		    // only for select (do not call by deselecting the old node)
		    // replace the existing toolbar totally
		    borderLayout.setActiveToolbarActionList(this.getToolbarActionsForTreeNodeSelect(selectedNode));
		    this.adjustToolbarButtonsTooltip(selectedNode, true);
		    // }
	    } else {
		    // in this moment actions are initialized in initAction() methods
			// called in init() and
		    // set on toolbar by
			// borderLayout.setActiveToolbarActionList(treeBaseImpl.getToolbarActions());
		    // leave the current toolbar only enable/disable toolbar actions
		    this.getToolbarActionChangesForTreeNodeSelect(selectedNode);
		    this.adjustToolbarButtonsTooltip(selectedNode, true);
	    }
	},

	/* protected abstract */loadDetailPanel : function(node, leaf, opts) {
	    return null;
	},

	/**
	 * Get the array of toolbar actions for a tree node select Implement only if
	 * replaceToolbarOnTreeNodeSelect is true
	 */
	/* protected abstract */getToolbarActionsForTreeNodeSelect : function(selectedNode) {
	    return this.getToolbarActions();
	},

	/**
	 * Which actions to enable/disable depending on tree selection Implement
	 * only if replaceToolbarOnTreeNodeSelect is false
	 */
	/* protected abstract */getToolbarActionChangesForTreeNodeSelect : function(selectedNode) {
	},

	/**
	 * Which actions to enable/disable depending on tree selection Implement
	 * only if replaceToolbarOnTreeNodeSelect is false
	 */
	/* protected abstract */getToolbarActionChangesForTreeNodeSelectionChange : function(selections) {
	},

	/**
	 * Gets the URL for loading the node detail
	 */
	/* private */getDetailUrl : function(node, add) {
	    var leaf = node != null && node.isLeaf();
	    if (leaf || add) {
		    return this.getLeafDetailUrl(node, add);
	    } else {
		    return this.getFolderDetailUrl(node, add);
	    }
	},

	/**
	 * The url for getting the folder detail: either this should be overridden
	 * or the folderDetailUrl should be specified in the config
	 */
	/* protected */getFolderDetailUrl : function(node, add) {
	    if (this.folderDetailUrl == null) {
		    return this.folderAction + '!load.action';
	    } else {
		    return folderDetailUrl;
	    }
	},

	/**
	 * The url for getting the leaf detail: either this should be overridden or
	 * the leafDetailUrl should be specified in the config
	 */
	/* protected */getLeafDetailUrl : function(node, add) {
	    if (this.leafDetailUrl == null) {
		    return this.baseAction + '!load.action';
	    } else {
		    return leafDetailUrl;
	    }
	},

	/**
	 * Gets the URL for loading the node detail
	 */
	/* private */getDetailParams : function(node, add, extraDetailParameters) {
	    if (add) {
		    var leaf = node != null && node.isLeaf();
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
	 * Gets the items collection for detail node: the selected node add: whether
	 * it is add or edit responseJson: the JSON response for
	 * loadSimpleDetailPanel(). (When called from loadDetailPanelWithFormLoad()
	 * not specified)
	 */
	/* private */getDetailItems : function(node, add, responseJson) {
	    var leaf = node != null && node.isLeaf();
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
	    if (this.centerPanel != null) {
		    this.mainPanel.remove(this.centerPanel, true);
	    }
	    var panelConfig = {
	        region : 'center',
	        border : false,
	        autoScroll : true,
	        header : false,
	        items : this.getDetailItems(node, add)
	    };
	    var panelConfigOptions = this.getDetailPanelConfigOptions();
	    if (panelConfigOptions != null) {
		    for (propertyName in panelConfigOptions) {
			    panelConfig[propertyName] = panelConfigOptions[propertyName];
		    }
	    }
	    this.centerPanel = Ext.create('Ext.form.Panel', panelConfig);
	    if (this.replaceCenterPanel != null) {
		    this.replaceCenterPanel.call(this, this.centerPanel);
	    } else {
		    this.mainPanel.add(this.centerPanel);
	    }
	    this.centerPanel.getForm().load({
	        fromCenterPanel : true,
	        url : this.getDetailUrl(node, add),
	        scope : this,
	        params : this.getDetailParams(node, add, extraDetailParameters),
	        success : function(form, action) {
		        var isLeaf = add;
		        if (node != null) {
			        isLeaf = node.isLeaf();
		        }
		        var postDataProcess = this.getEditPostDataProcess(node, isLeaf, add);
		        if (postDataProcess != null) {
			        try {
				        postDataProcess.call(this, action.result.data, this.centerPanel);
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
	/* private */loadSimpleDetailPanel : function(node, add, extraDetailParameters) {
	    Ext.Ajax.request({
	        fromCenterPanel : true,
	        url : this.getDetailUrl(node, add),
	        params : this.getDetailParams(node, add, extraDetailParameters),
	        scope : this,
	        disableCaching : true,
	        success : function(response) {
		        var responseJson = Ext.decode(response.responseText);
		        if (this.centerPanel != null) {
			        this.mainPanel.remove(this.centerPanel, true);
		        }
		        this.centerPanel = Ext.create('Ext.panel.Panel', {
		            region : 'center',
		            border : false,
		            autoScroll : true,
		            header : false,
		            items : this.getDetailItems(node, add, responseJson)
		        });
		        if (this.replaceCenterPanel != null) {
			        this.replaceCenterPanel.call(this, this.centerPanel);
		        } else {
			        this.mainPanel.add(this.centerPanel);
		        }
	        },
	        failure : function(result) {
		        Ext.MessageBox.alert(this.failureTitle, result.responseText);
	        },
	        method : "POST"
	    });
	},

	/**
	 * Reset the detail panel
	 */
	/* private */resetDetailPanel : function() {
	    var rootMessage = this.getRootMessage();
	    if (rootMessage != null || rootMessage != "") {
		    items = this.getInfoBoxItems(rootMessage);
	    }
	    if (this.centerPanel != null) {
		    this.mainPanel.remove(this.centerPanel, true);
	    }
	    this.centerPanel = Ext.create('Ext.form.Panel', {
	        region : 'center',
	        border : false,
	        autoScroll : true,
	        header : false,
	        items : items
	    });
	    if (this.replaceCenterPanel != null) {
		    this.replaceCenterPanel.call(this, this.centerPanel);
	    } else {
		    this.mainPanel.add(this.centerPanel);
	    }
	    this.actualizeToolbarOnTreeSelect();
	},

	/**
	 * ********************************************popup methods: URL and
	 * params**********************************************
	 */

	/**
	 * Parameters for adding to a folder node
	 */
	/* protected */getAddFolderParams : function(extraDetailParameters) {
	    return this.getAddParams(false, extraDetailParameters);
	},

	/**
	 * Parameters for adding to a leaf node normally you add a node to the
	 * selected leaf node's parent, that means sibling to the selected node
	 */
	/* protected */getAddLeafParams : function(extraDetailParameters) {
	    return this.getAddParams(true, extraDetailParameters);
	},

	/**
	 * Parameters for adding a new leaf
	 */
	/* private */getAddParams : function(leaf, extraDetailParameters) {
	    var addParams = null;
	    if (extraDetailParameters != null) {
		    addParams = extraDetailParameters;
	    } else {
		    addParams = new Object;
	    }
	    addParams['add'] = true;
	    addParams['leaf'] = leaf;
	    // var addParams = {add:true, leaf:leaf};
	    var addToNode = null;
	    if (this.selectedNode != null) {
		    if (leaf && this.selectedNode.isLeaf()) {
			    // add leaf to a leaf node -> means add sibling (add node to the
				// parent of the leaf node)
			    addToNode = this.selectedNode.parentNode;
		    } else {
			    // add child to a folder node
			    addToNode = this.selectedNode;
		    }
		    if (addToNode != null) {
			    addParams['node'] = addToNode.data['id'];
		    }
	    } else {
		    // adding when no node is selected: for ex. project specific
			// queries: add means adding to the (project specific branch) root
		    addParams['node'] = this.rootID;
	    }
	    return addParams;
	},

	/**
	 * Parameters for editing an existing entity recordData: the selected entity
	 * data
	 */
	/* protected */getEditParams : function(fromTree, extraDetailParameters) {
	    var editParams = null;
	    if (extraDetailParameters != null) {
		    editParams = extraDetailParameters;
	    } else {
		    editParams = new Object();
	    }
	    var recordData = this.getSingleSelectedRecordData(fromTree);
	    if (recordData != null) {
		    var nodeID = null;
		    if (fromTree) {
			    nodeID = recordData['id'];
		    } else {
			    nodeID = recordData['node'];
		    }
		    if (nodeID != null) {
			    // return {node:nodeID};
			    editParams.node = nodeID;
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
	/* protected */getEditPostDataProcess : function(record, isLeaf, add) {
	    var recordData = null;
	    if (record != null) {
		    recordData = record.data;
	    }
	    if (isLeaf) {
		    if (this.getEditLeafPostDataProcess != null) {
			    return this.getEditLeafPostDataProcess(recordData, add);
		    }
	    } else {
		    if (this.getEditFolderPostDataProcess != null) {
			    return this.getEditFolderPostDataProcess(recordData, add);
		    }
	    }
	    return null;
	},

	/**
	 * Method to process the data to be loaded arrived from the server by
	 * editing the folder
	 */
	/* protected abstract */getEditFolderPostDataProcess : null,

	/**
	 * Method to process the data to be loaded arrived from the server by
	 * editing the leaf
	 */
	/* protected abstract */getEditLeafPostDataProcess : null,

	/**
	 * Parameter name for the submitted entityID(s) by delete If
	 * allowMultipleSelections==false then the same entityID can be used for
	 * delete and edit submits If allowMultipleSelections==true this should
	 * return another name (the submitted value will be stored on the server
	 * typically in an Integer[])
	 */
	/* protected */getDeleteParamName : function(extraConfig) {
	    return 'node';
	},

	/**
	 * Helper for preparing the params get the ID based on the record and extra
	 * config
	 */
	/* protected */getRecordID : function(recordData, extraConfig) {
	    var fromTree = null;
	    if (extraConfig != null) {
		    fromTree = extraConfig.fromTree;
	    }
	    if (fromTree) {
		    return recordData['id'];
	    } else {
		    return recordData['node'];
	    }
	},

	/**
	 * **********************************selection utility methods**************************************
	 */
	// tree specific selection utility methods (the grid specific ones are
	// inherited from crudBase.js)

	/**
	 * Select a node by id
	 * Scope is contains tree, nodeIdToSelect
	 */
	selectNode: function() {
		var selectionModel = this.tree.getSelectionModel();
		if (this.nodeIdToSelect!=null) {
			//get the node by id
			var nodeToSelect = this.tree.getStore().getNodeById(this.nodeIdToSelect);
			if (nodeToSelect!=null) {
				selectionModel.select(nodeToSelect);
			}
		}
	},

	/**
	 * Get the (last) selected tree node
	 */
	/* public */getLastSelectedTreeNode : function() {
	    if (this.tree != null) {
		    return this.tree.getSelectionModel().getLastSelected();
	    }
	    return null;
	},
	/**
	 * The the selected tree nodes
	 */
	/* public */getTreeSelection : function() {
	    if (this.tree != null) {
		    return this.tree.getSelectionModel().getSelection();
	    }
	    return null;
	},

	/**
	 * Select and returns a record in the tree by activating the context menu
	 */
	/* public */selectTreeNode : function(record) {
	    // false to not retain existing selections
	    if (this.tree != null) {
		    this.tree.getSelectionModel().select(record, true);
	    }
	    return record;
	},

	/**
	 * Gets the selected record either from tree node or from grid row
	 */
	/*public*/getLastSelected : function(fromTree) {
	    if (fromTree) {
		    return this.getLastSelectedTreeNode();
	    } else {
		    return this.getLastSelectedGridRow();
	    }
	},

	getSelection : function(fromTree) {
	    if (fromTree) {
		    return this.getTreeSelection();
	    } else {
		    return this.getGridSelection();
	    }
	},

	/**
	 * Gets the selected data (either in tree or in grid )
	 */
	/*public*/getSelectedRecords : function(fromTree) {
	    var allowMultiple = false;
	    if (fromTree) {
		    //multiple tree nodes
		    allowMultiple = this.allowMultipleNodeSelections;
	    } else {
		    //multiple grid rows
		    allowMultiple = this.allowMultipleSelections;
	    }
	    if (allowMultiple) {
		    return this.getSelection(fromTree);
	    } else {
		    return this.getLastSelected(fromTree);
	    }
	},

	/**
	 * Get the data of the single selected record
	 * For no selection or multiple selection return null
	 */
	/*private*/getSingleSelectedRecord : function(fromTree) {
	    var selectedRecords = this.getSelectedRecords(fromTree);
	    if (selectedRecords != null) {
		    var allowMultiple = false;
		    if (fromTree) {
			    //multiple tree nodes
			    allowMultiple = this.allowMultipleNodeSelections;
		    } else {
			    //multiple grid rows
			    allowMultiple = this.allowMultipleSelections;
		    }
		    if (allowMultiple) {
			    if (selectedRecords != null && selectedRecords.length == 1) {
				    return selectedRecords[0];
			    } else {
				    return null;
			    }
		    } else {
			    return selectedRecords;
		    }
	    } else {
		    return null;
	    }
	},

	/**
	 * Whether the selected tree node or grid row is leaf or not
	 * operationType: 'add'/'edit'/'copy'
	 * triggeredFrom: 'grid'/'tree'
	 */
	/*private*/selectedIsLeaf : function(fromTree) {
	    var lastSelectedRecord = this.getLastSelected(fromTree);
	    if (lastSelectedRecord != null) {
		    if (fromTree) {
			    return lastSelectedRecord.isLeaf();
		    } else {
			    return lastSelectedRecord.data.leaf;
		    }
	    }
	    return true;
	},

	/**
	 * Whether the selection is simple or multiple
	 */
	/*public*/selectionIsSimple : function(fromTree) {
	    var allowMultiple = false;
	    if (fromTree) {
		    //multiple tree nodes
		    allowMultiple = this.allowMultipleNodeSelections;
	    } else {
		    //multiple grid rows
		    allowMultiple = this.allowMultipleSelections;
	    }
	    if (allowMultiple) {
		    var selectedRecords = this.getSelection(fromTree);
		    return selectedRecords != null && selectedRecords.length == 1;
	    } else {
		    var selectedRecord = this.getLastSelected(fromTree);
		    return selectedRecord != null;
	    }
	},

	/**
	 * Select a record by activating the context menu
	 */
	/*public*/selectRecord : function(record, fromTree) {
	    if (fromTree) {
		    return this.selectTreeNode(record);
	    } else {
		    return this.selectGridRow(record);
	    }
	},

	/**
	 * Gather selected ID as a comma separated string
	 * selectedRecords - the selected record(s): simple or multiple selection:
	 * 	if simple the selection the record, if multiple an array of records
	 * getIDFormEntityFunction - function for getting the ID from an entity JSON
	 * extraConfig
	 */
	/*private*/getSelectionParam : function(selectedRecords, extraConfig) {
	    var fromTree = null;
	    if (extraConfig != null) {
		    fromTree = extraConfig.fromTree;
	    }
	    if (fromTree) {
		    //multiple tree nodes
		    allowMultiple = this.allowMultipleNodeSelections;
	    } else {
		    //multiple grid rows
		    allowMultiple = this.allowMultipleSelections;
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
	},

	/**
	 * Select a record by activating the context menu
	 */
	/*public*/changeAndRefreshNode : function(tree, nodeId, data, reloadIfExpanded) {
	    var treeStore = tree.getStore();
	    var node = treeStore.getNodeById(nodeId);
	    if (node != null) {
		    if (data != null) {
			    for (propertyName in data) {
				    node.data[propertyName] = data[propertyName];
			    }
		    }
		    node.commit();
		    var treeView = tree.getView();
		    var index = treeView.indexOf(node);
		    treeView.refreshNode(index);
		    if (node.isLoaded() && reloadIfExpanded) {
			    treeStore.load({
				    node : node
			    });
		    }
	    }
	}
});
