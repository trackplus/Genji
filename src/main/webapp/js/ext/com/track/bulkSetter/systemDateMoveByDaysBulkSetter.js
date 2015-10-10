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

Ext.define("js.ext.com.track.bulkSetter.systemDateMoveByDaysBulkSetter", {
	extend: "Ext.panel.Panel",
	layout: {
		type: 'hbox'
		//align: 'stretch'
	},
	border:false,
	config: {
		jsonData: {}
	},
	initComponent: function() {
		this.itemId = this.jsonData['name'];
		this.name = this.jsonData['name'];
		this.adjustCheckboxName = this.jsonData.adjustCheckboxName;
		this.value = this.jsonData['value'];
		this.callParent();
		this.add(CWHF.createNumberField(null, this.name, 0, null, null,
				{disabled:this.jsonData.disabled,
				value: this.value,
				itemId: this.name,
				width:150}));
		if (this.adjustCheckboxName!=null) {
			this.add(CWHF.createCheckbox(this.jsonData.adjustCheckboxLabel, this.adjustCheckboxName,
					{labelIsLocalized:true,
					disabled:this.jsonData.disabled,
					value:this.jsonData.adjustCheckboxValue},
					{change: {fn:this.disableOpposite, scope:this}}));
		}
	},

	/**
	 * disable/enable the opposite date
	 */
	disableOpposite: function(checkboxField, newValue, oldValue, options) {
		var oppositeField = this.jsonData['oppositeField'];
		var expressionPanel = this.ownerCt.ownerCt.getComponent('expressionPanel'+oppositeField);
		var selectedFieldCheckbox = expressionPanel.getComponent("selectedFieldMap.field"+oppositeField);
		if (selectedFieldCheckbox!=null) {
			selectedFieldCheckbox.setDisabled(newValue);
		}
		var setterCombo = expressionPanel.getComponent("setterRelationMap.field"+oppositeField);
		var valuePart = expressionPanel.getComponent("displayValueMap.field"+oppositeField);
		if (newValue) {
			if (setterCombo!=null) {
				setterCombo.setDisabled(newValue);
			}
			if (valuePart!=null) {
				valuePart.setDisabled(newValue);
			}
		} else {
			if (selectedFieldCheckbox.getValue()) {
				if (setterCombo!=null) {
					setterCombo.setDisabled(newValue);
				}
				if (valuePart!=null) {
					valuePart.setDisabled(newValue);
				}
			}
		}
	},

	/**
	 * Get the field value as a json
	 */
	getFieldValueJson: function() {
		var valueJson = new Object();
		var numberField = this.getComponent(this.name);
		if (numberField!=null) {
			if (numberField.getValue()!=null) {
				valueJson[this.name] = numberField.getValue();
			}
		}
		var checkBox = this.getComponent(this.adjustCheckboxName);
		if (checkBox!=null) {
			valueJson[this.adjustCheckboxName] = checkBox.getValue();
		}
		return valueJson;
	},

	setDisabled: function(disabled) {
		for (var i=0;i<this.items.getCount();i++) {
			var item = this.items.getAt(i);
			item.setDisabled(disabled);
		}
	},

	getAdjustCheckBoxValue: function() {
		var adjustCheckBox = this.getComponent(this.adjustCheckboxName);
		if (adjustCheckBox!=null) {
			return adjustCheckBox.getValue();
		}
		return null;
	}

});
