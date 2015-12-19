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


Ext.define("com.trackplus.admin.NotifyConfig", {
	extend:"com.trackplus.admin.TreeBase",
	xtype: "notifyConfig",
    controller: "notifyConfig",
    
	config: {
		//default project setting
		defaultSettings: false,
		//the exclusiveProject set only for automail in project settings
		exclusiveProjectID: null
	},
	baseServerAction: "notify",
	treeStoreUrl: "notify!expand.action",
	
	/**
	 * Gets the tree's fields: all fields for all possible types
	 */
	getTreeFields: function() {
		return [{name : 'id', mapping : 'id', type: 'string'},
				{ name : 'text', mapping : 'text', type: 'string'},
				{ name : 'leaf', mapping : 'leaf', type: 'boolean'},
				{ name : 'iconCls', mapping : 'iconCls', type: 'string'}];
	},
	
	/**
	 * Automatically select the first node
	 */
	onTreeNodeLoad: function(treeStore, node) {
	    if (node.isRoot()) {
	        this.tree.getSelectionModel().select(node.firstChild);
	        //node.firstChild.select();
	        //for not project specific categories expand the private repository (first child)
	        //treeStore.load({node:node.firstChild, callback:function(){node.firstChild.select()}});
	    }
	}

});
