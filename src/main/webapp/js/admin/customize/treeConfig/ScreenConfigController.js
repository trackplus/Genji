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
 * Although it inherits from TreeConfig which is a treeBase,
 * some behavior is similar to treeWithGrid that's why some methods of
 * treeWithGrid are implemented again (alternatively multiple inheritance?)
 *
 */
Ext.define("com.trackplus.admin.customize.treeConfig.ScreenConfigController",{
	extend: "Ext.app.ViewController",
	alias: "controller.screenConfig",
	mixins: {
		baseController: "com.trackplus.admin.customize.treeConfig.AssignmentConfigController"
	},
	
	baseServerAction: "screenConfigItemDetail",
	
	enableDisableToolbarButtons: function (view, selections) {
		var sysAdmin=com.trackplus.TrackplusConfig.user.sys;
		if (CWHF.isNull(selections) || selections.length===0) {
			this.getView().actionEdit.setDisabled(true);
			this.getView().actionApply.setDisabled(true);
			this.getView().actionDesign.setDisabled(true);
			this.getView().actionExport.setDisabled(true);
		} else {
			var selectedRecord = selections[0];
			var isLeaf = selectedRecord.data['leaf'];
			this.getView().actionExport.setDisabled(!sysAdmin);
			if (selections.length===1) {
				var selectedTreeNode = this.getView().getSingleSelectedRecord(true);
				if (selectedTreeNode && this.isAssignable(selectedTreeNode)) {
					this.getView().actionApply.setDisabled(false);
				}
				this.getView().actionEdit.setDisabled(!sysAdmin);
				this.getView().actionDesign.setDisabled(!sysAdmin);
			} else {
				this.getView().actionApply.setDisabled(true);
				this.getView().actionEdit.setDisabled(true);
				this.getView().actionDesign.setDisabled(true);
			}
		}
	},

	isAssignable: function(node) {
		return node.isLeaf();
	},

	getGridRowEditURL: function() {
		return "indexScreens!edit.action";
	},

	getGridRowSaveURL: function() {
		return "indexScreens!save.action";
	},

	getObjectIDName: function() {
		return "screenID";
	},

	getImportURL: function() {
		return "screenConfigItemDetail!importScreens.action";
	},

	getExportURL: function(selectedObjectIDs) {
		return "screenConfigItemDetail!export.action?selectedObjectIDs="+selectedObjectIDs;
	},

	/**
	 * The struts action for delete/replace
	 */
	getDeleteUrlBase: function(extraConfig) {
		return "indexScreens";
	},

	getConfigGridRowURL: function(id) {
		return 'screenEdit.action?componentID='+id;
	},

	getDeleteParamName: function() {
	    return "selectedScreenIDs";
	},

	getUploadFileLabel: function() {
		return "admin.customize.form.import.lbl.uploadFile";
	}
});
