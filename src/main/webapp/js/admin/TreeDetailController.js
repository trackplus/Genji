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
 * Base class for all "tree-detail"-based CRUD operations
 */
Ext.define("com.trackplus.admin.TreeDetailController", {
	extend:"com.trackplus.admin.TreeBaseController",
	
	/**
	 * Whether the folder details are loaded manually or through a form load method
	 */
	folderDetailByFormLoad: false,
	/**
	 * whether the leaf details are loaded manually or through a form load method
	 */
	leafDetailByFormLoad: true,
	

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
