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
 * Common functionality for category (folder) based entity hierarchies: now item filter, notify filter and reports
 */
Ext.define("com.trackplus.admin.customize.category.CategoryBase", {
	extend : "com.trackplus.admin.TreeWithGrid",
	//xtype: "categoryBase",
    //controller: "categoryBase",
    
	folderAction: "categoryConfig",
	dragAndDropOnTree: true,
	useCopyPaste : true,
	
	initComponent : function() {
		this.treeStoreUrl = "categoryConfig!expand.action";
		this.callParent();
	},

	/**
	 * Expanding the node
	 */
	getTreeStoreExtraParams : function(node) {
	    var extraParams = {
		    excludePrivate : this.getExcludePrivate()
	    };
	    if (this.getProjectID()) {
		    // in project configuration
		    extraParams["projectID"] = this.getProjectID();
	    }
	    return extraParams;
	},

	/**
	 * Get the extra parameters for the gridStore
	 */
	getGridStoreExtraParams : function(node, opts) {
	    if (CWHF.isNull(node)) {
		    // called manually
		    node = this.selectedNode;
	    }
	    var params = {
	        node : node.get("id"),
	        excludePrivate : this.getExcludePrivate()
	    /*
		 * , fromIssueNavigator:this.fromIssueNavigator
		 */
	    };
	    if (this.getProjectID() ) {
		    // in project configuration
		    params["projectID"] = this.getProjectID();
	    }
	    return params;
	},

	/**
	 * Return false if dragging this node is not allowed
	 */
	canDragDropNode : function(nodeToDrag, copy, overModel) {
	    if (nodeToDrag.get("readOnly")) {
		    // do not drag hardcoded node
		    return false;
	    }
	    if (!nodeToDrag.get("modifiable") && !copy) {
		    // do not move a not modifiable node
		    return false;
	    }
	    var dropOverNode = overModel;
	    var overLeaf = dropOverNode.isLeaf();
	    if (overLeaf) {
		    dropOverNode = dropOverNode.parentNode;
	    }
	    if (!dropOverNode.get("canAddChild")) {
		    // do not drop in a node with nor right to add child
		    return false;
	    }
	    return true;
	},

	/* protected */getDragDropBaseAction : function(draggedNodeIsLeaf) {
	    return "categoryConfig";
	},

	/**
	 * Automatically expand the private repository
	 */
	onTreeNodeLoad : function(treeStore, node) {
	    if (node.isRoot() && CWHF.isNull(this.getProjectID())) {
		    // for not project specific categories expand the private repository
		    // (first child)
		    treeStore.load({
		        node : node.firstChild,
		        callback : function() {
			        node.firstChild.expand();
		        }
		    });
	    }
	},

	/**
	 * Gets the tree's fields
	 */
	getTreeFields : function() {
	    return [{name : "id", mapping : "id", type : "string"},
	            {name : "text", mapping : "text", type : "string"},
	            {name : "readOnly", mapping : "readOnly", type : "boolean"},
	            {name : "modifiable", mapping : "modifiable", type : "boolean"},
	            {name : "deletable", mapping : "deletable", type : "boolean"},
	            {name : "canCopy", mapping : "canCopy", type : "boolean"},
	            {name : "categoryType", mapping : "categoryType", type : "string"},
	            {name : "customFeature", mapping : "customFeature", type : "boolean"},
	            {name : "leaf", mapping : "leaf", type : "boolean"},
	            {name : "iconCls", mapping : "iconCls", type : "string"},
	            {name : "canAddChild", mapping : "canAddChild",type : "boolean"}];
	}	
});
