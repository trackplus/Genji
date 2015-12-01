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

Ext.define("js.ext.com.track.bulkSetter.systemDateEarliestLatestBulkSetter", {
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
		this.dateItemId = this.jsonData.dateItemId;
		this.adjustCheckboxName = this.jsonData.adjustCheckboxName;
		this.adjustCheckboxItemId = this.jsonData.adjustCheckboxItemId;
		var disabled = this.jsonData.disabled;
		this.value = this.jsonData.value;
		this.callParent();
		this.add(CWHF.createDateField(null, this.jsonData.name,
				{disabled: disabled,
				value: this.value,
				itemId: this.dateItemId,
				//same as format because the submit is parsed as a gui date string (with 4 digits year)
				//(not interpreted by struts2 (two digits year) to convert directly into a Date type object)
				submitFormat: com.trackplus.TrackplusConfig.DateFormat,
				width:150}));
		if (this.adjustCheckboxName) {
			this.add(CWHF.createCheckbox(this.jsonData.adjustCheckboxLabel, this.adjustCheckboxName,
					{itemId: this.adjustCheckboxItemId,
					labelIsLocalized:true,
					disabled: disabled,
					value: this.jsonData.adjustCheckboxValue},
					{change: {fn:this.disableOpposite, scope:this}}));
		}
	},

	/**
	 * disable/enable the opposite date
	 */
	disableOpposite: function(checkboxField, newValue, oldValue, options) {
		var oppositeField = this.jsonData.oppositeField;
		var expressionPanel = this.ownerCt.ownerCt.getComponent("expressionPanel"+oppositeField);
		var selectedFieldCheckbox = expressionPanel.getComponent("selectedFieldItemIdfield"+oppositeField);
		if (selectedFieldCheckbox) {
			selectedFieldCheckbox.setDisabled(newValue);
		}
		var setterCombo = expressionPanel.getComponent("setterRelationItemIdfield"+oppositeField);
		var valuePart = expressionPanel.getComponent("valueItemIDfield"+oppositeField);
		if (newValue) {
			if (setterCombo) {
				setterCombo.setDisabled(newValue);
			}
			if (valuePart) {
				valuePart.setDisabled(newValue);
			}
		} else {
			if (selectedFieldCheckbox.getValue()) {
				if (setterCombo) {
					setterCombo.setDisabled(newValue);
				}
				if (valuePart) {
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
		var dateField = this.getComponent(this.dateItemId);
		if (dateField) {
			if (dateField.getValue()) {
				var formattedDate = Ext.Date.format(dateField.getValue(), com.trackplus.TrackplusConfig.DateFormat);
				valueJson[this.name] = formattedDate;
			}
		}
		var checkBox = this.getComponent(this.adjustCheckboxItemId);
		if (checkBox) {
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
		var adjustCheckBox = this.getComponent(this.adjustCheckboxItemId);
		if (adjustCheckBox) {
			return adjustCheckBox.getValue();
		}
		return null;
	}

});
