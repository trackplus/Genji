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

Ext.define('js.ext.com.track.fieldType.TextboxDate', {
	extend: 'Ext.form.FieldSet',
	itemId: 'specificItem',
	items: [],
	onDataReady: function(data) {
		var inheritedConfig = data.inheritedConfig;
		var defaultValueRadioButtonItems = CWHF.getRadioButtonItems(data['dateOptions'], 'textBoxSettingsList[0].defaultOption', 'id', 'label',
				data['textBoxSettingsList[0].defaultOption'], inheritedConfig, true);
		var minValueRadioButtonItems = CWHF.getRadioButtonItems(data['dateOptions'], 'textBoxSettingsList[0].minOption', 'id', 'label',
				data['textBoxSettingsList[0].minOption'], inheritedConfig, true);
		var maxValueRadioButtonItems = CWHF.getRadioButtonItems(data['dateOptions'], 'textBoxSettingsList[0].maxOption', 'id', 'label',
				data['textBoxSettingsList[0].maxOption'], inheritedConfig, true);
		var defaultOptionRadioGroup = CWHF.getRadioGroup(data['defaultOptionLabel'], 500,
				defaultValueRadioButtonItems, {itemId:"defaultOption",labelIsLocalized:true, labelWidth:data["labelWidth"]}, {
			change: function(radioGroup, newValue, oldValue, options) {
				var defaulDateSelected = radioGroup.ownerCt.absoluteDateSelected(radioGroup);
				var defaultDate = radioGroup.ownerCt.getComponent("defaultDate");
				defaultDate.setDisabled(!defaulDateSelected);
			}});
		var minOptionRadioGroup = CWHF.getRadioGroup(data['minOptionLabel'],
				500, minValueRadioButtonItems, {itemId:"minOption",labelIsLocalized:true, labelWidth:data["labelWidth"]}, {
			change: function(radioGroup, newValue, oldValue, options) {
				var defaulDateSelected = radioGroup.ownerCt.absoluteDateSelected(radioGroup);
				var defaultDate = radioGroup.ownerCt.getComponent("minDate");
				defaultDate.setDisabled(!defaulDateSelected);
			}});
		var maxOptionRadioGroup = CWHF.getRadioGroup(data['maxOptionLabel'],
				500, maxValueRadioButtonItems, {itemId:"maxOption",labelIsLocalized:true, labelWidth:data["labelWidth"]}, {
			change: function(radioGroup, newValue, oldValue, options) {
				var defaulDateSelected = radioGroup.ownerCt.absoluteDateSelected(radioGroup);
				var defaultDate = radioGroup.ownerCt.getComponent("maxDate");
				defaultDate.setDisabled(!defaulDateSelected);
			}});
		var dateOptions = data['dateOptions'];
		var dateLabel = dateOptions[2].label;
		var items = [defaultOptionRadioGroup,
				CWHF.createDateField(dateLabel, "textBoxSettingsList[0].defaultDate",
				{itemId: "defaultDate",
				value:data['textBoxSettingsList[0].defaultDate'],
				//submitFormat: data["submitFormat"],
				width:350,
				disabled:inheritedConfig || !this.absoluteDateSelected(defaultOptionRadioGroup),
				labelIsLocalized: true}),
		minOptionRadioGroup,
		CWHF.createDateField(dateLabel, "textBoxSettingsList[0].minDate",
				{itemId: "minDate",
				value:data['textBoxSettingsList[0].minDate'],
				//submitFormat: data["submitFormat"],
				width:350,
				disabled:inheritedConfig || !this.absoluteDateSelected(minOptionRadioGroup),
				labelIsLocalized: true}),
		maxOptionRadioGroup,
		CWHF.createDateField(dateLabel, "textBoxSettingsList[0].maxDate",
				{itemId: "maxDate",
				value:data['textBoxSettingsList[0].maxDate'],
				//submitFormat: data["submitFormat"],
				width:350,
				disabled:inheritedConfig || !this.absoluteDateSelected(maxOptionRadioGroup),
				labelIsLocalized: true})];
		this.add(items);
	},
	absoluteDateSelected: function(radioGroup) {
		var checkedArr = radioGroup.getChecked();
		var checkedRadio;
		if (checkedArr.length>0) {
			checkedRadio = checkedArr[0];
			return checkedRadio.getSubmitValue()===2;
		}
		return false;
	}
});
