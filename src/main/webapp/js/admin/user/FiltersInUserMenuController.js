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


Ext.define("com.trackplus.admin.user.FiltersInUserMenuController",{
	extend: "Ext.app.ViewController",
	alias: "controller.filtersInUserMenu",
	mixins: {
		baseController: "com.trackplus.admin.GridBaseController"
	},
	
	confirmDeleteEntity:false,
	entityDialog: "com.trackplus.admin.user.FiltersInUserMenuEdit",
	/**
	 * Render the inherited rows as grey
	 */
	filterRenderer: function(value, metadata, record) {
	    if (!record.data["first"]) {
	        return "";
	    }
	    return value;
	},

	/**
	 * Get the ID based from the recordData
	 *  No objectID primary key in tacl table
	 */
	getRecordID: function(recordData) {
		if (recordData) {
	        var filterID = recordData["filterID"];
	        var personID = recordData["personID"];
	        if (CWHF.isNull(personID)) {
	            return filterID;
	        } else {
	            return filterID + "|" + personID;
	        }
		}
		return null;
	},

	getDeleteUrl: function() {
		return "filtersInMenu!delete.action";
	},
	
	/**
	 * Parameters for deleting entity
	 * recordData: the selected entity data
	 * Even if there is more than one entity selected for delete
	 * this method is called for each selected entity separately
	 * extraConfig: for simple grid nothing, for tree with grid {fromTree:fromTree, isLeaf:isLeaf}
	 */
	getDeleteParams: function(selectedRecords, extraConfig) {
		var params=new Object();
		var selectionParam = this.getSelectedIDs(selectedRecords, extraConfig);
		params["unassign"] = selectionParam;
	    params["personIDs"] = this.getView().getPersonIDs();
		return params;
	}

});
