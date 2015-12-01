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

Ext.define("js.ext.com.track.bulkSetter.watcherSelectBulkSetter", {
	extend: "com.trackplus.util.MultipleSelectPicker",
	useNull:true,
	width: 220,
	config: {
		jsonData: {}
	},
	initComponent: function() {
		this.name = this.jsonData.name;
        this.matchFieldWidth = true;
        //this.pickerWidth = 500;
        this.options = this.jsonData.datasource;
		this.value = this.jsonData.value;
		this.disabled = this.jsonData.disabled;
		this.iconUrlPrefix = this.jsonData.iconUrlPrefix;
		this.useIconCls = this.jsonData.useIconCls;
		this.callParent();
	},

	/**
	 * Get the field value as a json
	 */
	getFieldValueJson: function() {
		var valueJson = new Object();
		if (this.items) {
			var multiSelect = this.items.getAt(0);
			var name = multiSelect.getName();
			var value = multiSelect.getValue();
			valueJson[name] = value;
		}
		return valueJson;
	}
});
