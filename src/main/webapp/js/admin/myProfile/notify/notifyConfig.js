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


Ext.define('com.trackplus.admin.NotifyConfig',{
	extend:'com.trackplus.admin.TreeBase',
	config: {
		//default project setting
		defaultSettings: false,
		//the exclusiveProject set only for automail in project settings
		exclusiveProjectID: null
	},
	defaultSettings: false,
	exclusiveProjectID: null,
	baseAction: 'notify',
	entityID:'node',
	//actions
	actionSave: null,
	actionDelete: null,

	notifySettings: null,
	notifyTriggers: null,
	notifyFilters: null,

	AUTOMAIL_ASSIGNMENT: '1',
	AUTOMAIL_TRIGGER: '2',
	AUTOMAIL_FILTER: '3',


	constructor: function(config) {
		var config = config || {};
		this.initialConfig = config;
		Ext.apply(this, config);
	},

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
	 * Called from two places:
	 * 1. projectConfig.js: selecting the automail node (nodeId=='10')
	 * 2. from this class by selecting either an automail assignment/trigger/filter node
	 */
	loadDetailPanel: function(node, leaf, opts) {
		var nodeId = node.data['id'];
		if (nodeId=='10') {
			//called from projectConfig.js: selecting the automail node (nodeId=='10')
			return this.createCenterPanel();
		} else {
			switch (nodeId) {
			case this.AUTOMAIL_ASSIGNMENT:
				if (this.notifySettings==null) {
					this.notifySettings = Ext.create('com.trackplus.admin.NotifySettingsList',
						{defaultSettings: this.defaultSettings, exclusiveProjectID:this.exclusiveProjectID});
				}
				this.loadGrid(this.notifySettings);
				break;
			case this.AUTOMAIL_TRIGGER:
				if (this.notifyTriggers==null) {
					this.notifyTriggers = Ext.create('com.trackplus.admin.NotifyTriggerList',
							{defaultSettings: this.defaultSettings});
				}
				this.loadGrid(this.notifyTriggers);
				break;
			case this.AUTOMAIL_FILTER:
				if (this.notifyFilters==null) {
					this.notifyFilters = Ext.create('com.trackplus.admin.customize.category.CategoryConfig',
							{rootID:'notifyFilter', projectID:this.exclusiveProjectID, excludePrivate:this.defaultSettings});
				}
				this.loadTreeWithGrid(this.notifyFilters);
				break;
			}
		}
	},

	loadGrid: function(gridConfig) {
		//not quite clear why this initGrid() must be called: gridConfig.getGrid() executes the initGrid() internally
		//if grid is not null, and grid is not null after the first call but somehow the this.centerPanel.removeAll()
		//leaves the grid in an inconsistent state that's why initGrid() should be called again although grid is not null.
		//Otherwise the further attempts to select the grid result in empty detail section even through the grid data is received
		gridConfig.initGrid();
		var grid=gridConfig.getGrid();
		gridConfig.reload();
		if (this.centerPanel!=null) {
			this.mainPanel.remove(this.centerPanel, true);
		}
		this.centerPanel = grid;
		this.mainPanel.add(this.centerPanel);
		borderLayout.setActiveToolbarActionList(gridConfig.getToolbarActions());
	},

	loadTreeWithGrid: function(treeWithGridConfig) {
		if (this.centerPanel!=null) {
			this.mainPanel.remove(this.centerPanel, true);
		}
		this.centerPanel = treeWithGridConfig.createCenterPanel();
		this.mainPanel.add(this.centerPanel);
		borderLayout.setActiveToolbarActionList(treeWithGridConfig.getToolbarActions());
	},

	/**
	 * Automatically expand the private repository
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
