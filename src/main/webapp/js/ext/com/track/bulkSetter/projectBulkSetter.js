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

Ext.define("js.ext.com.track.bulkSetter.projectBulkSetter", {
	extend: "com.trackplus.util.SingleTreePicker",
	config: {
		jsonData: {}
	},
	initComponent: function() {
		this.itemId = this.jsonData.name;
		this.name = this.jsonData.name;
		this.disabled = this.jsonData.disabled;
        this.data = this.jsonData.data;
        this.width = 220;
        this.matchFieldWidth = false;
        this.pickerWidth = 400;
		this.value = this.jsonData.value;
		this.callParent();
        this.on("select", this.projectChange, this);
	},

    projectChange: function(tree, selectedProject, options) {
        var params = new Object();
        params['projectRefresh'] = true;
        params['projectID'] = tree.getValue();
        var scope = this.jsonData.scope;
        scope.refreshContext.call(scope, tree.ownerCt, params, this.jsonData.fieldID, true);
    },

	/**
	 * Get the field value as a json
	 */
	getFieldValueJson: function() {
		var valueJson = new Object();
		valueJson[this.name] = this.value;
		return valueJson;
	}
});
