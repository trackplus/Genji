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

Ext.define("js.ext.com.track.matcher.timeMatcher", {
	extend: "Ext.panel.Panel",
	layout: {
		type: 'hbox'
	},
	border:false,
	items: [],
	config: {
		jsonData: {}
	},
	initComponent: function() {
		this.itemId = this.jsonData.name;//used for removing this panel after matcher or field change
		this.callParent();
		this.numberField = CWHF.createNumberField(null, this.jsonData.valueName, this.jsonData.decimalPrecision, null, null, {width: 100, value: this.jsonData.value});
		this.add(this.numberField);
		this.unitCombo = CWHF.createCombo(null, this.jsonData.unitName, {data:this.jsonData.unitList, width:100});
		var unit = this.jsonData.unit;
		if (unit==null) {
			unit = this.jsonData.unitList[0].id;
		}
		this.unitCombo.setValue(unit);
		this.add(this.unitCombo);
	},
	getStringValue: function() {
		//splitter can't be "," because double can have decimals with ","
		return this.numberField.getValue() + "#" + this.unitCombo.getValue();
	}
});
