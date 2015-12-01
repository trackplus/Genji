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

Ext.define("com.trackplus.admin.action.DocxPreview",{
	extend:"Ext.container.Container",
	config: {
		/**
		 * the extra parameters for proxy
		 */
		extraProxyParameters: null,

		/**
		 * tree configuration
		 */
		treeConfig: null

	},
	items: [],
	extraProxyParameters: null,

	/**
	 * The tree instance
	 */
	tree: null,

	initComponent: function() {
		this.callParent();
		this.initTree();
		this.add(this.tree);

	},

	/**
	 * Gets the tree component
	 */
	getTree: function() {
		return this.tree;
	},

	/**
	 * The URL for the tree store proxy
	 */
	/*protected abstract*/getTreeProxyURL: function() {
		return null;
	},

	/**
	 * Gets the ID of the root node
	 */
	/*protected*/getRootNodeID: function() {
		return "";
	},

	/**
	 * Create the tree
	 */
	initTree: function() {
		var treeFields = [{name : "id", mapping : "id", type: "string"},
						{name : "text", mapping : "text", type: "string"},
						{name : "leaf", mapping : "leaf", type: "boolean"},
						{name : "iconCls", mapping : "iconCls", type: "string"}];
		var treeStore = Ext.create("Ext.data.TreeStore", {
			root: {
				expanded: true,
				id:this.getRootNodeID()
			},
			listeners: {
				load: {fn: this.selectNode, scope:this}
			},
			proxy: {
				type: "ajax",
				url: this.getTreeProxyURL(),
				extraParams: this.extraProxyParameters
			},
			fields: treeFields
		});
		this.treeConfig.store = treeStore;
		this.tree= Ext.create("Ext.tree.Panel", this.treeConfig);
	}
});

