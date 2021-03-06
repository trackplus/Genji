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

Ext.define("js.ext.com.track.setter.multipleListSetter", {
	extend: "com.trackplus.util.MultipleSelectPicker",
	width:200,
	useNull:true,
	config: {
		jsonData: {}
	},
	initComponent: function() {
		this.itemId = this.jsonData.name;
		this.name = this.jsonData.name;
		this.options = this.jsonData.data;
		this.value = this.jsonData.value;
		this.labelWidth=this.jsonData.labelWidth;
		this.iconUrlPrefix = this.jsonData.iconUrlPrefix;
		this.useIconCls = this.jsonData.useIconCls;
		this.callParent();
	},

	getSetterValue:function() {
		if (this.getValue()) {
			//transform the array in comma sepatared string
			return this.getValue().join();
		}
		return null;
	},

	getSetterDisplayValue:function(){
		var v=this.getValue();
		if (CWHF.isNull(v)) {
			return null;
		}
		var labelsArr = [];
		Ext.Array.forEach(v, function(selection, ind) {
			var index = this.getStore().find('id', selection);
			if (index) {
				var label = this.getStore().getAt(index).get('label');
				if (label) {
					labelsArr.push(label);
				}
			}
		}, this);
		return labelsArr.join();
	}
});
