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


Ext.define("com.trackplus.admin.NotifyConfigController",{
	extend: "Ext.app.ViewController",
	alias: "controller.notifyConfig",
	mixins: {
		baseController: "com.trackplus.admin.TreeBaseController"
	},
	
	//baseServerAction: "notify",
	
	notifySettings: null,
	notifyTriggers: null,
	notifyFilters: null,
	
	AUTOMAIL_ASSIGNMENT: '1',
	AUTOMAIL_TRIGGER: '2',
	AUTOMAIL_FILTER: '3',
	
	/**
	 * Called from two places:
	 * 1. projectConfig.js: selecting the automail node (nodeId==='10')
	 * 2. from this class by selecting either an automail assignment/trigger/filter node
	 */
	loadDetailPanel: function(node, leaf, opts) {
		var nodeId = node.get("id");
		if (nodeId==='10') {
			//called from projectConfig.js: selecting the automail node (nodeId==='10')
			return this.createCenterPanel();
		} else {
			switch (nodeId) {
			case this.AUTOMAIL_ASSIGNMENT:
				//if (CWHF.isNull(this.notifySettings)) {
					this.notifySettings = Ext.create("com.trackplus.admin.NotifySettingsList",
						{defaultSettings: this.getView().getDefaultSettings(), exclusiveProjectID:this.getView().getExclusiveProjectID()});
				//}
				this.loadGrid(this.notifySettings);
				break;
			case this.AUTOMAIL_TRIGGER:
				//if (CWHF.isNull(this.notifyTriggers)) {
					this.notifyTriggers = Ext.create("com.trackplus.admin.NotifyTriggerList",
							{defaultSettings: this.getView().getDefaultSettings()});
				//}
				this.loadGrid(this.notifyTriggers);
				break;
			case this.AUTOMAIL_FILTER:
				//if (CWHF.isNull(this.notifyFilters)) {
					this.notifyFilters = Ext.create("com.trackplus.admin.customize.filter.FilterConfig",
							{rootID:"notifyFilter", projectID:this.getView().getExclusiveProjectID(), excludePrivate:this.getView().getDefaultSettings()});
				//}
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
		/*gridConfig.initGrid();
		var grid=gridConfig.getGrid();
		gridConfig.reload();*/
		if (this.getView().centerPanel) {
			this.getView().mainPanel.remove(this.getView().centerPanel, true);
		}
		this.getView().centerPanel = gridConfig;
		this.getView().mainPanel.add(this.getView().centerPanel);
		//borderLayout.setActiveToolbarActionList(gridConfig.getToolbarActions());
	},
	
	loadTreeWithGrid: function(treeWithGridConfig) {
		if (this.getView().centerPanel) {
			this.getView().mainPanel.remove(this.getView().centerPanel, true);
		}
		this.getView().centerPanel = treeWithGridConfig;//.createCenterPanel();
		this.getView().mainPanel.add(this.getView().centerPanel);
		//borderLayout.setActiveToolbarActionList(treeWithGridConfig.getToolbarActions());
	}

});
