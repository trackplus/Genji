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

Ext.define("js.ext.com.track.bulkSetter.issueTypeBulkSetter", {
	extend: "js.ext.com.track.bulkSetter.selectBulkSetter",
	width: 220,
	useNull:true,
	config: {
		jsonData: {}
	},
	initComponent: function() {
		this.callParent();
		this.on("change", this.issueTypeChange, this);
	},

	issueTypeChange: function(combo, records, options) {
		var params = new Object();
		params['issueTypeRefresh'] = true;
		params['issueTypeID'] = combo.getValue();
		var scope = this.jsonData.scope;
		scope.refreshContext.call(scope, combo.ownerCt, params, this.jsonData.fieldID);
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
