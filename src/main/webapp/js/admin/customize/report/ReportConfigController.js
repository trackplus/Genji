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


Ext.define("com.trackplus.admin.customize.filter.ReportConfigController", {
	extend: "Ext.app.ViewController",
	alias: "controller.reportConfig",
	mixins: {
		baseController: "com.trackplus.admin.customize.category.CategoryBaseController"
	},
	
	//used in FieldExpressionAction (not directly here)
	//issueFilter : true,
	
	folderAction: "categoryConfig",
	baseServerAction: "reportConfig",
	
	entityDialog: "com.trackplus.admin.customize.report.ReportEdit",
	
	
	enableDisableToolbarButtons : function(view, arrSelections) {
	    if (CWHF.isNull(arrSelections) || arrSelections.length === 0) {
		    this.getView().actionDeleteGridRow.setDisabled(true);
		    this.getView().actionEditGridRow.setDisabled(true);
		    this.getView().actionExecuteGridRow.setDisabled(true);
			this.getView().actionDownloadGridRow.setDisabled(true);
	    } else {
		    if (arrSelections.length === 1) {
			    var selectedRecord = arrSelections[0];
			    var isLeaf = selectedRecord.get("leaf");
			    var modifiable = selectedRecord.get("modifiable");
			    this.getView().actionEditGridRow.setDisabled(!modifiable);
		        this.getView().actionExecuteGridRow.setDisabled(!isLeaf);
			    this.getView().actionDownloadGridRow.setDisabled(!isLeaf);
			} else {
			    // more than one selection
			    this.getView().actionEditGridRow.setDisabled(true);
			    this.getView().actionExecuteGridRow.setDisabled(true);
				this.getView().actionDownloadGridRow.setDisabled(true);   
		    }
		    var allIsDeletable = true;
		    for (var i = 0; i < arrSelections.length; i++) {
			    var selectedRecord = arrSelections[i];
			    var deletable = selectedRecord.data.deletable;
			    if (!deletable) {
				    allIsDeletable = false;
			    }
		    }
		    this.getView().actionDeleteGridRow.setDisabled(!allIsDeletable);
	    }
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
			    //customFeature: whether record configuration is needed
			    com.trackplus.admin.Report.executeReport(this, objectID, recordData["customFeature"], false);
		    }
	    }
	},

	/**
	 * Download the report for a tree node
	 */
	onDownloadTreeNode : function() {
	    this.downloadReport(true);
	},

	/**
	 * Download the report for the grid row
	 */
	onDownloadGridRow : function() {
	    this.downloadReport(false);
	},

	/**
	 * Downloads a report zip
	 */
	downloadReport : function(fromTree) {
	    var recordData = this.getView().getSingleSelectedRecordData(fromTree);
	    if (recordData ) {
		    var leaf = this.getView().selectedIsLeaf(fromTree);
		    if (leaf) {
			    var node = this.getRecordID(recordData, {
				    fromTree : fromTree
			    });
			    attachmentURI = "reportConfig!download.action?node=" + node;
			    window.open(attachmentURI);
		    }
	    }
	}
});
