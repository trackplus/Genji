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
 * Base class for all "tree-detail"-based CRUD operations
 */
Ext.define('com.trackplus.admin.TreeDetail',{
	extend:'com.trackplus.admin.TreeBase',
	config: {

	},
	/**
	 * Whether selecting a folder node in the tree results in a grid in the detail part or other detail info
	 */
	showGridForFolder: false,
	/**
	 * If showGridForFolder is false, then whether the folder details are loaded manually or through a form load method
	 */
	folderDetailByFormLoad: false,
	/**
	 * Whether to show a grid with a single row instead of other detailed view of the leaf node
	 */
	//showGridForLeaf: false,
	/**
	 * whether the leaf details are loaded manually or through a form load method
	 */
	leafDetailByFormLoad: true,
	/**
	 * Whether the tree has double click listener
	 */
	treeHasItemdblclick: false,

	constructor: function(config) {
		var config = config || {};
		this.initialConfig = config;
		Ext.apply(this, config);
		this.init();
	},

	/**
	 * Get the detail part after selecting a tree node
	 */
	/*protected*/loadDetailPanel: function(node, leaf, opts) {
		if (leaf) {
			if (this.leafDetailByFormLoad) {
				this.loadDetailPanelWithFormLoad(node, false);
			} else {
				this.loadSimpleDetailPanel(node, false);
			}
		} else {
			if (this.folderDetailByFormLoad) {
				this.loadDetailPanelWithFormLoad(node, false);
			} else {
				this.loadSimpleDetailPanel(node, false);
			}
		}
	}
});
