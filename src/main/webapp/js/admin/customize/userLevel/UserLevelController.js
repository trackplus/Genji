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

Ext.define("com.trackplus.admin.customize.userLevel.UserLevelController",{
	extend: "Ext.app.ViewController",
	alias: "controller.userLevel",
	mixins: {
		baseController: "com.trackplus.admin.TreeWithGridController"
	},
	baseServerAction : "userLevel",
	
	entityDialog: "com.trackplus.admin.customize.userLevel.UserLevelEdit",
	
	reload: function(refreshParamsObject) {
		this.reloadTree(this.getView().tree, refreshParamsObject);
	},
	
	onTreeNodeDblClick: function(view, record) {
		var sys = com.trackplus.TrackplusConfig.user.sys;
	    if (sys) {
	    	this.onEditTreeNode();
	    }
	},

	onCheckChange: function(checkBox, rowIndex, checked, eOpts) {
		var record = this.getView().grid.getStore().getAt(rowIndex);
		if (record) {
			var params = {actionID:record.get("id"), node:this.getView().selectedNodeID, checked:checked};
			Ext.Ajax.request({
				url: "userLevel!changeAction.action",
				params: params,
				scope: this,
				success: function(response) {
					var result = Ext.decode(response.responseText);
					if (result.success) {

					} else {
						com.trackplus.util.showError(result);
					}
				},
				failure: function(response) {
					Ext.MessageBox.alert(this.failureTitle, response.responseText);
				}
			});
		}
	},

	onBeforeCheckChange: function(checkBox, rowIndex, checked, eOpts) {
		var sys = com.trackplus.TrackplusConfig.user.sys;
	    if (sys) {
	    	return true;
	    } else {
	    	return false;
	    }
	},

	getAddReloadParamsAfterSave: function(addLeaf) {
		return {nodeIDToReload: this.rootID};
	},

	
	getReloadParamsAfterDelete: function(selectedRecords, extraConfig, responseJson) {
		return {reloadTree:true}
	},

	/**
	 * Get the node to select after save after add operation
	 */
	getAddSelectionAfterSaveFromResult: function() {
		//do not specify rowToSelect, do not select anything in the grid after add
		return [{parameterName:"node", fieldNameFromResult:"node"},
		        {parameterName:"reloadTree", fieldNameFromResult:"reloadTree"}];
	},

	/**
	 * Get the node to select after save after edit operation
	 */
	getEditReloadParamsAfterSaveFromResult: function(fromTree) {
		return [{parameterName:"node", fieldNameFromResult:"node"},
		        {parameterName:"reloadTree", fieldNameFromResult:"reloadTree"}];
	}

});
