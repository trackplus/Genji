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
 * Base class for all tree-based pages (tree with grid and tree detail)
 */
Ext.define("com.trackplus.admin.TreeBase", {
	extend: "Ext.panel.Panel",
	mixins:{
		actionsBase: "com.trackplus.admin.ActionBase"
	},
	//generally good but override if needed
	treeFields: [{
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
    }],
	//Url for loading the tree
	treeStoreUrl: null,
	//the initial width of the tree component
	treeWidth : 200,

	config : {
	    /**
		 * The ID of the root node
		 */
	    rootID : '_',
	},

	initActions: function() {
		return [];
	},

	/**
	 * Extra parameters additionally to 'node' by expanding a node from the tree
	 * If the data in the tree is homogeneous then the node can be decoded
	 * uniquely. Otherwise extra parameters are needed to decide how to
	 * interpret the node id
	 */
	/*protected*/getTreeStoreExtraParams : function(node) {
	    return null;
	},


	baseServerAction: null,
	getBaseServerAction: function() {
		return this.baseServerAction;
	},

	replaceCenterPanel : null,

	 /**
	 * Whether more tree nodes can be selected on the same time
	 */
    allowMultipleNodeSelections : false,

	/**
	 * The base struts action name (without method name) in struts.xml for
	 * the set this if it differs from base action (folder and leaf
	 * operations are in different struts actions)
	 */
    folderAction : null,

	/**
	 * Whether selecting a folder node in the tree results in a grid in the
	 * detail part or other detail info
	 */
	//showGridForFolder : false,
	/**
	 * If showGridForFolder is false, then whether the folder details are loaded
	 * manually or through a form load method
	 */
	//folderDetailByFormLoad : false,
	/**
	 * Whether to show a grid with a single row instead of other detailed view
	 * of the leaf node
	 */
	// showGridForLeaf: false,
	/**
	 * If showGridForLeaf is false, then whether the leaf details are loaded
	 * manually or through a form load method
	 */
	//leafDetailByFormLoad : false,
	/**
	 * Whether to completely replace the toolbar for tree node select or only
	 * change the disabled status If true, the toolbar actions for nodes of
	 * different levels change radically, replace the toolbar after each node
	 * select In this case getToolbarActionsForTreeNodeSelect() should be
	 * implemented If false, the toolbar actions are relative constant. In this
	 * case getToolbarActionChangesForTreeNodeSelect() should be implemented
	 */
	//replaceToolbarOnTreeNodeSelect : false,
	/**
	 * whether to allow cut/copy-paste in the tree
	 */
	//useCopyPaste : false,
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

	initComponent: function() {
		this.initBase();
		this.layout = "border";
		this.region = "center";
		this.border = false;
		this.bodyBorder = false;
		if (this.actions) {
			this.dockedItems = this.getToolbar();
		}
		var items = [];
	    var rootMessage = this.getRootMessage();
	    if (rootMessage && rootMessage !== "") {
		    items = this.getInfoBoxItems(rootMessage);
	    }
	    this.centerPanel = Ext.create("Ext.panel.Panel", {
	        region : "center",
	        border : false,
	        autoScroll : true,
	        header : false,
	        items : items
	    });
	    this.initCenterPanel();
		this.mainPanel = Ext.create('Ext.panel.Panel',{
			region:'center',
			layout:'fit',
			border:false,
			bodyBorder:false,
			items:[this.centerPanel]
		});
	    this.postInitCenterPanel(this.tree.getRootNode());
	    this.callParent();
	    this.add(this.tree);
	    this.add(this.mainPanel);
	},

	/*protected*/replaceToolbar: function (newToolbarActions) {
		var toolbars = this.getDockedItems('toolbar[dock="top"]');
		if (toolbars) {
			var toolbar = toolbars[0];
			toolbar.removeAll();
			toolbar.add(this.getToolbarActionButtons(newToolbarActions));
		}
		/*this.addDocked([{
			xtype: 'toolbar',
			dock: 'top',
			items: this.getToolbarActionButtons(newToolbarActions)
		}])
		this.updateLayout();*/
	},

	/**
	 * Initialize the center panel with data by initializing the tree and
	 * eventually the detail part (if it is independent of the selected tree
	 * node) By default only the tree is initialized
	 */
	/*protected*/initCenterPanel : function() {
	    this.initTree();
	},

	getTreeStoreUrl: function() {
		return this.treeStoreUrl;
	},

	/**
	 * Initialize the tree
	 */
	/*private*/initTree : function() {
		var treeStore = Ext.create("Ext.data.TreeStore", {
	        root : {
	            expanded : true,
	            id : this.getRootID()
	        },
		    proxy : {
			    //fromCenterPanel : true,
			    type : "ajax",
			    url : this.getTreeStoreUrl(), /*this.getNodeBaseAction({
				    isLeaf : false
			    }) + '!expand.action',*/
			    // for expanding the root
			    extraParams : this.getTreeStoreExtraParams(),
			    listeners : {
				    exception : function(proxy, response, operation, opts) {
					    var responseJson = Ext.decode(response.responseText);
					    com.trackplus.util.showError(responseJson);
				    }
			    }
		    },
		    fields : this.getTreeFields()
	    });
	    var treeConfig = {
	        xtype : "treepanel",
	        store : treeStore,
	        selModel : this.getTreeSelectionModel(),
	        region : "west",
	        autoScroll : true,
	        width : this.treeWidth,
		    margin : '0 -5 0 0',
	        split: this.isTreeSplitOn(),
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
	        cls : "simpleTree"
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
		    treeListeners.selectionchange = "onTreeSelectionchange";
	    } else {
		    treeListeners.select = "onTreeNodeSelect";
	    }
	    if (this.treeHasItemcontextmenu) {
		    treeListeners.itemcontextmenu = "onTreeNodeCtxMenu";
	    }
	    if (this.treeHasItemdblclick) {
		    treeListeners.itemdblclick = "onTreeNodeDblClick";
	    }
	    treeConfig.listeners = treeListeners;
	    if (this.hasDragAndDropOnTree()) {
		    // add drag and drop
		    treeConfig.viewConfig = {
		        plugins : this.getDragAndDropOnTreeConfig(),
		        // allowCopy: true,
		        listeners : {
		            beforedrop : {
		                scope : this,
		                fn : function(node, data, overModel, dropPosition) {
			                var nodeToDrag = data.records[0];
			                var copy = data.copy;
			                if (CWHF.isNull(copy)) {
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
			                if (CWHF.isNull(copy)) {
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
		    if (operation.node ) {
			    var extraParams = this.getTreeStoreExtraParams(operation.node);
			    if (extraParams ) {
				    treeStore.proxy.extraParams = extraParams;
			    }
		    }
	    }, this);
	    treeStore.on('load', function(treeStore,  records, successful, operation, node, eOpts) {
		    this.onTreeNodeLoad(treeStore, node);
	    }, this);
	},

	/**
	 * Gets the tree fields for the tree store
	 * Normally it is enough to specify the treeFields directly in derived classes
	 * If dynamic tree fields are possible overwrite this method in derived class
	 */
	/*protected*/getTreeFields: function() {
		return this.treeFields;
	},

	/**
	 * Initialization method
	 */
	/*protected*/initBase : function() {
	    this.initActions();
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

	getFolderAction: function() {
		return this.folderAction;
	},

	/**
	 * ********************************************action/context menu related * methods**********************************************
	 */


	/**
	 * Get the itemId of those actions whose context menu text or toolbar button
	 * tooltip should be changed according to the current selection
	 */
	/*protected abstract*//*getActionItemIdsWithContextDependentLabel : function() {
	    return [];
	},*/

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
	/* protected *//*getContextMenuActions : function(selectedRecords, selectionIsSimple, fromTree) {
	    return this.getTreeContextMenuActions(selectedRecords, selectionIsSimple);
	},*/

	/**
	 * Show the context menu in grid
	 */
	/* private *//*onTreeNodeCtxMenu : function(tree, record, item, index, evtObj) {
	    this.onCtxMenu(true, record, evtObj);
	    return false;
	},*/

	/**
	 * Show the context menu in grid
	 */
	/* private *//*onCtxMenu : function(fromTree, record, evtObj) {
	    evtObj.stopEvent();
	    this.selectRecord(record, fromTree);
	    var selectedRecords = this.getSelectedRecords(fromTree);
	    var selectionIsSimple = this.selectionIsSimple(fromTree);
	    var actions = this.getContextMenuActions(selectedRecords, selectionIsSimple, fromTree);
	    if (actions  && actions.length > 0) {
		    var treeNodeCtxMenu = this.createContextMenu(record, actions);
		    this.adjustContextMenuText(treeNodeCtxMenu, selectedRecords, fromTree);
		    treeNodeCtxMenu.showAt(evtObj.getXY());
	    }
	    return false;
	},*/

	/**
	 * Change the text for an action depending on entity context (from grid or
	 * from three and for folder or for leaf)
	 */
	/* private *//*adjustContextMenuText : function(ctxMenu, selectedRecord, fromTree) {
	    actionIds = this.getActionItemIdsWithContextDependentLabel();
	    for (var i = 0; i < actionIds.length; i++) {
		    var actionId = actionIds[i];
		    var action = ctxMenu.getComponent(actionId);
		    if (action ) {
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
	},*/

	/**
	 * Change the text for an action depending on entity context (from grid or
	 * from three and for folder or for leaf)
	 */
	/* private *//*adjustToolbarButtonsTooltip : function(selectedNode, fromTree) {
	    actionIds = this.getActionItemIdsWithContextDependentLabel();
	    var toolbar = borderLayout.getActiveToolbarList();
	    for (var i = 0; i < actionIds.length; i++) {
		    var actionId = actionIds[i];
		    var toolbarButton = toolbar.getComponent(actionId);
		    if (toolbarButton ) {
			    var isLeaf = this.selectedIsLeaf(fromTree);
			    toolbarButton.setTooltip(this.getTitle(toolbarButton.tooltipKey, {
			        isLeaf : isLeaf,
			        fromTree : fromTree,
			        selectedRecord : selectedNode
			    }));
		    }
	    }
	},*/

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
	        ptype : "treeviewdragdrop",
	        dragGroup : this.getBaseServerAction() + "DDGroup",
	        dropGroup : this.getBaseServerAction() + "DDGroup",
	        appendOnly : true,
	        enableDrag : true,
	        enableDrop : true
	    };
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
	    if (CWHF.isNull(this.treeSelectionModel)) {
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
		return this.getNodeBaseAction({
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
		 * nodeTo.parentNode; while (parentNode && parentNode.raw)
		 * {//parentNode.raw is null for root node
		 * parentHierarchy.push(parentNode.raw["id"]); parentNode =
		 * parentNode.parentNode; }
		 */

	    Ext.Ajax.request({
	       // fromCenterPanel : true,
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
		        if (responseJson.success === true) {
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
						 * (parentHierarchy.indexOf(fromParentID)!==-1) { var
						 * parentNode =
						 * this.tree.getStore().getNodeById(parentHierarchy.pop());
						 * while (parentNode) { parentNode.expand(); var
						 * nextId = parentHierarchy.pop(); if (nextId) {
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
			        if (responseJson.errorMessage ) {
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
	 * Callback called after creating the center panel
	 */
	postInitCenterPanel : function(rootNode) {

	},

	getInfoBoxItems : function(content) {
	    if (content  && content !== "") {
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
	 * Get the toolbar actions.
	 * By default all actions, override if not all actions should appear in toolbar
	 */
	/*protected*/getToolbarActions: function(){
		return this.actions;
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
	 * **********************************selection utility methods**************************************
	 */

	/**
	 * Select a node by id
	 * Scope is contains tree, nodeIdToSelect
	 */
	selectNode: function() {
		var selectionModel = this.tree.getSelectionModel();
		if (this.nodeIdToSelect) {
			//get the node by id
			var nodeToSelect = this.tree.getStore().getNodeById(this.nodeIdToSelect);
			if (nodeToSelect) {
				selectionModel.select(nodeToSelect);
			}
		}
	},

	/**
	 * Get the (last) selected tree node
	 */
	/* public */getLastSelectedTreeNode : function() {
	    if (this.tree ) {
		    return this.tree.getSelectionModel().getLastSelected();
	    }
	    return null;
	},
	/**
	 * The the selected tree nodes
	 */
	/* public */getTreeSelection : function() {
	    if (this.tree ) {
		    return this.tree.getSelectionModel().getSelection();
	    }
	    return null;
	},

	/**
	 * Select and returns a record in the tree by activating the context menu
	 */
	/* public */selectTreeNode : function(record) {
	    // false to not retain existing selections
	    if (this.tree ) {
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

	/*public*/getLastSelectedGridRow: function() {
		if (this.grid) {
			return this.grid.getSelectionModel().getLastSelected();
		}
		return null;
	},

	getSelection : function(fromTree) {
	    if (fromTree) {
		    return this.getTreeSelection();
	    } else {
		    return this.getGridSelection();
	    }
	},

	/*public*/getGridSelection: function() {
		if (this.grid) {
			return this.grid.getSelectionModel().getSelection();
		}
		return null;
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
	    if (selectedRecords ) {
		    var allowMultiple = false;
		    if (fromTree) {
			    //multiple tree nodes
			    allowMultiple = this.allowMultipleNodeSelections;
		    } else {
			    //multiple grid rows
			    allowMultiple = this.allowMultipleSelections;
		    }
		    if (allowMultiple) {
			    if (selectedRecords  && selectedRecords.length === 1) {
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
	    if (lastSelectedRecord ) {
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
		    return selectedRecords  && selectedRecords.length === 1;
	    } else {
		    var selectedRecord = this.getLastSelected(fromTree);
		    return selectedRecord !== null;
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

	/*public*/selectGridRow: function(record) {
		//false to not retain existing selections
		if (this.grid) {
			this.grid.getSelectionModel().select(record, false);
		}
		return record;
	},

	/**
	 * Get the data of the single selected record
	 * For no selection or multiple selection return null
	 * param: typically fromTree
	 */
	/*private*/getSingleSelectedRecordData: function(param) {
		var selectedRecord = this.getSingleSelectedRecord(param);
		if (selectedRecord) {
			return selectedRecord.data;
		}
		return null;
	},

	/**
	 * Get the data of the single selected record
	 * For no selection or multiple selection return null
	 */
	/*private*//*getSingleSelectedRecord: function(param) {
		var selectedRecords = this.getSelectedRecords(param);
		if (selectedRecords) {
			if (this.allowMultipleSelections) {
				if (selectedRecords && selectedRecords.length===1) {
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
	},*/

	/**
	 * Select a record by activating the context menu
	 */
	/*public*//*changeAndRefreshNode : function(tree, nodeId, data, reloadIfExpanded) {
	    var treeStore = tree.getStore();
	    var node = treeStore.getNodeById(nodeId);
	    if (node ) {
		    if (data ) {
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
	},*/

	isTreeSplitOn: function() {
		return true;
	}
});
