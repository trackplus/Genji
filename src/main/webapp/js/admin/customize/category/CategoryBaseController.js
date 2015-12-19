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
Ext.define("com.trackplus.admin.customize.category.CategoryBaseController", {
	extend: "com.trackplus.admin.TreeWithGridController",
	/*extend: "Ext.app.ViewController",
	alias: "controller.categoryBase",
	mixins: {
		baseController: "com.trackplus.admin.TreeWithGridController"
	},*/
	
	folderAction: "categoryConfig",
	replacementIsTree: true,
	
	getActionItemIdsWithContextDependentLabel : function() {
	    return [ "editGridRow", "editTreeNode", "deleteGridRow", "deleteTreeNode", "cut", "copy"/*, "paste"*/];
	},

	/**
	 * Create the edit
	 */
	onTreeNodeDblClick : function(view, record) {
	    var readOnly = record.get("readOnly");
	    if (CWHF.isNull(readOnly) || readOnly === false) {
		    this.onEditTreeNode();
	    }
	},

	onGridRowDblClick : function(view, record) {
	    var readOnly = record.get("readOnly");
	    if (CWHF.isNull(readOnly) || readOnly === false) {
		    this.onEditGridRow();
	    }
	},

	/**
	 * The struts action for delete/replace: deleting of both leafs and folders
	 * are made in the folderAction
	 */
	getDeleteUrlBase : function(extraConfig) {
	    return this.getFolderAction();
	}
});
