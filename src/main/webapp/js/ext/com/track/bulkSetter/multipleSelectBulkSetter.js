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

Ext.define("js.ext.com.track.bulkSetter.multipleSelectBulkSetter", {
	extend: "Ext.panel.Panel",
	border:false,
	items: [],
	config: {
		jsonData: {}
	},
	initComponent: function() {
		this.itemId = this.jsonData['name'];//used for removing this panel after a setter relation change
		this.callParent();
		var projectJsonArray = this.jsonData['projectJson'];
		Ext.Array.forEach(projectJsonArray, function(listData) {
			var label = listData['label'];
			var width = 220;
			var labelWidth = 0;
			if (label!=null) {
				//more lists in different contexts
				labelWidth = 150;
				width = 125 + 220;
			}
			this.add(CWHF.createMultipleSelectPicker(null, listData['name'], listData['dataSource'], listData['value'],
					{disabled:this.jsonData['disabled'],
					width: width,
					labelWidth:labelWidth,
					labelIsLocalized:true,
					fieldLabel:label,
					useRemoveBtn:false, useNull:true}));
			}, this);
	},
	/**
	 * Get the field value as a json
	 */
	getFieldValueJson: function() {
		var valueJson = new Object();
		if (this.items!=null) {
			for (var i=0;i<this.items.getCount();i++) {
				var multiSelect = this.items.getAt(i);
				var name = multiSelect.getName();
				var value = multiSelect.getValue();
				valueJson[name] = value;
			}
		}
		return valueJson;
	},

	setDisabled: function(disabled) {
		if (this.items!=null) {
			for (var i=0;i<this.items.getCount();i++) {
				var multiSelect = this.items.getAt(i);
				multiSelect.setDisabled(disabled);
			}
		}
	}
});
