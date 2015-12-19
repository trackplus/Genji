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


Ext.define("com.trackplus.admin.customize.filter.FilterConfigController", {
	extend: "Ext.app.ViewController",
	alias: "controller.filterConfig",
	mixins: {
		baseController: "com.trackplus.admin.customize.category.CategoryBaseController"
	},
	
	//used in FieldExpressionAction (not directly here)
	//issueFilter : true,
	
	folderAction: "categoryConfig",
	baseServerAction: "filterConfig",
	showGridForLeaf: false,
	
	entityDialog: "com.trackplus.admin.customize.filter.FilterEdit",
	
	/**
	 * The url for getting the leaf detail: either this should be overridden or
	 * the leafDetailUrl should be specified in the config
	 */
	/* protected */getLeafDetailUrl : function() {
	    return "categoryConfig!leafDetail.action";
	},
	
	/**
	 * Get the items array to render for leaf detail based on the response JSON
	 * Should be implemented if showGridForLeaf is false
	 */
	getLeafDetailItems : function(node, add, responseJson) {
	    if (responseJson.success === true) {
		    var detailData = responseJson.errorMessage;
		    return [ Ext.create("Ext.Component", {
		        html : detailData,
		        cls : "categoryConfigLeafDetail",
		        border : true
		    }) ];
	    }
	    return [];
	},


	changeSubscribe: function(checkBox, rowIndex, checked, eOpts) {
		var record = this.getView().grid.getStore().getAt(rowIndex);
		if (record) {
			var params = {node:record.data["node"], includeInMenu:checked};
			Ext.Ajax.request({
				url: "filterConfig!changeSubscribe.action",
				params: params,
				scope: this,
				success: function(response) {
				},
				failure: function(response) {
					Ext.MessageBox.alert(this.failureTitle, response.responseText);
				}
			});
		}
	},

	enableDisableToolbarButtons : function(view, arrSelections) {
	    if (CWHF.isNull(arrSelections) || arrSelections.length === 0) {
		    this.getView().actionDeleteGridRow.setDisabled(true);
		    this.getView().actionEditGridRow.setDisabled(true);
		    if (this.getView().isIssueFilter()) {
			    this.getView().actionExecuteGridRow.setDisabled(true);
			    this.getView().actionLinkGridRow.setDisabled(true);
			}
	    } else {
		    if (arrSelections.length === 1) {
			    var selectedRecord = arrSelections[0];
			    var isLeaf = selectedRecord.data.leaf;
			    var modifiable = selectedRecord.data.modifiable;
			    if (this.getView().isIssueFilter() && isLeaf) {
				    // for issue filter at least the include in menu and style
				    // field should be editable
				    this.getView().actionEditGridRow.setDisabled(false);
			    } else {
				    this.getView().actionEditGridRow.setDisabled(!modifiable);
			    }
			    if (this.getView().isIssueFilter()) {
				    this.getView().actionExecuteGridRow.setDisabled(!isLeaf);
				    this.getView().actionLinkGridRow.setDisabled(!isLeaf);
			    }
		    } else {
			    // more than one selection
			    this.getView().actionEditGridRow.setDisabled(true);
			    if (this.getView().isIssueFilter()) {
				    this.getView().actionExecuteGridRow.setDisabled(true);
				    this.getView().actionLinkGridRow.setDisabled(true);
			    }
		    }
		    var allIsDeletable = true;
		    for (var i = 0; i < arrSelections.length; i++) {
			    var selectedRecord = arrSelections[i];
			    var deletable = selectedRecord.get("deletable");
			    if (!deletable) {
				    allIsDeletable = false;
			    }
		    }
		    this.getView().actionDeleteGridRow.setDisabled(!allIsDeletable);
	    }
	},

	/**
	 * Handler for adding an instant issue filter
	 */
	onInstantFilter: function() {
	    var windowParameters = {
        	callerScope:this,
        	windowTitle:getText("menu.findItems.instantFilter"),
        	loadUrlParams: {instant:true, add:true, leaf:true},
        	submitUrlParams: {instant:true, add:true, leaf:true, ajax: true},
        	entityContext: {
            	operation: "instant"
        	}
        };
		var windowConfig = Ext.create(this.entityDialog, windowParameters);
		windowConfig.showWindowByConfig(this);
	},

	/**
	 * Execute a leaf node
	 */
	onExecuteTreeNode: function() {
	    this.onExecute(true);
	},

	/**
	 * Execute a grid row
	 */
	onExecuteGridRow: function() {
	    this.onExecute(false);
	},

	/**
	 * Execute a tree node or a grid row
	 */
	onExecute: function(fromTree) {
	    var recordData = this.getView().getSingleSelectedRecordData(fromTree);
	    if (recordData ) {
		    var leaf = this.getView().selectedIsLeaf(fromTree);
		    var node = this.getRecordID(recordData, {
			    fromTree : fromTree
		    });
		    if (leaf) {
			    var lastIndex = node.lastIndexOf("_");
			    var objectID = node.substring(lastIndex + 1);
			    com.trackplus.filter.executeFilter(this.getView(), objectID);
			}
	    }
	},

	/**
	 * Generate a permanent link for an issue filter tree node
	 */
	onLinkTreeNode: function() {
	    this.onLink(true);
	},

	/**
	 * Generate a permanent link for an issue filter grid row
	 */
	onLinkGridRow: function() {
	    this.onLink(false);
	},

	/**
	 * Generate permanent link for issue filter
	 */
	onLink: function(fromTree) {
	    var recordData = this.getView().getSingleSelectedRecordData(fromTree);
	    if (recordData ) {
		    var leaf = this.getView().selectedIsLeaf(fromTree);
		    if (leaf) {
			    var node = this.getRecordID(recordData, {
				    fromTree : fromTree
			    });
			    var windowParameters = {
		        	callerScope:this,
		        	loadUrlParams: {node:node},
		        	submitUrlParams: {node:node},
		        };
				var windowConfig = Ext.create("com.trackplus.admin.customize.filter.FilterLink", windowParameters);
				windowConfig.showWindowByConfig(this);
		    }
	    }
	}
});
