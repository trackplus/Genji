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

Ext.define('js.ext.com.track.fieldType.TextboxSystemDate', {
	extend: 'Ext.form.FieldSet',
	itemId: 'specificItem',
	items: [],
	onDataReady: function(data) {
		var inheritedConfig = data.inheritedConfig;
		var defaultValueRadioButtonItems = CWHF.getRadioButtonItems(data['defaultDateOptions'], 'textBoxSettingsList[0].defaultOption', 'id', 'label',
				data['textBoxSettingsList[0].defaultOption'], inheritedConfig, true);
		var minValueRadioButtonItems = CWHF.getRadioButtonItems(data['dateOptions'], 'textBoxSettingsList[0].minOption', 'id', 'label',
				data['textBoxSettingsList[0].minOption'], inheritedConfig, true);
		var maxValueRadioButtonItems = CWHF.getRadioButtonItems(data['dateOptions'], 'textBoxSettingsList[0].maxOption', 'id', 'label',
				data['textBoxSettingsList[0].maxOption'], inheritedConfig, true);
		var defaultOptionRadioGroup = CWHF.getRadioGroup(data['defaultOptionLabel'],
				500, defaultValueRadioButtonItems, {itemId:"defaultOption",labelIsLocalized:true, labelWidth:data["labelWidth"]}, {
			change: function(radioGroup, newValue, oldValue, options) {
				var selectedRadio = radioGroup.ownerCt.getSelectedRadio(radioGroup);
				var defaultDate = radioGroup.ownerCt.getComponent("defaultDate");
				if (selectedRadio!==2) {
					defaultDate.setDisabled(true);
					defaultDate.setValue(null);
				}
				defaultDate.setDisabled(selectedRadio!==2);
				var defaultInteger = radioGroup.ownerCt.getComponent("defaultInteger");
				if (selectedRadio!==3) {
					defaultInteger.setDisabled(true);
					defaultInteger.setValue(null);
				} else {
					defaultInteger.setDisabled(false);
				}
			}});
		var minOptionRadioGroup = CWHF.getRadioGroup(data['minOptionLabel'],
				500, minValueRadioButtonItems, {itemId:"minOption",labelIsLocalized:true, labelWidth:data["labelWidth"]}, {
			change: function(radioGroup, newValue, oldValue, options) {
				var selectedRadio = radioGroup.ownerCt.getSelectedRadio(radioGroup);
				var minDate = radioGroup.ownerCt.getComponent("minDate");
				if (selectedRadio!==2) {
					minDate.setDisabled(true);
					minDate.setValue(null);
				} else {
					minDate.setDisabled(false);
				}
			}});
		var maxOptionRadioGroup = CWHF.getRadioGroup(data['maxOptionLabel'],
				500, maxValueRadioButtonItems, {itemId:"maxOption",labelIsLocalized:true, labelWidth:data["labelWidth"]}, {
			change: function(radioGroup, newValue, oldValue, options) {
				var selectedRadio = radioGroup.ownerCt.getSelectedRadio(radioGroup);
				var maxDate = radioGroup.ownerCt.getComponent("maxDate");
				if (selectedRadio!==2) {
					maxDate.setDisabled(true);
					maxDate.setValue(null);
				} else {
					maxDate.setDisabled(false);
				}
			}});
		var dateOptions = data['defaultDateOptions'];
		var dateLabel = dateOptions[2].label;
		var daysLabel = dateOptions[3].label;
		var recalculateButton = Ext.create("Ext.button.Button", {
			text: getText("common.btn.recompute"),
			margins:'0 0 0 5',
			handler: function(button) {
				Ext.Ajax.request({
					url: 'fieldConfigItemDetail!executeCommand.action',
					params: {node:data['node']},
					success : function(response) {
						var result = Ext.decode(response.responseText);
						button.enable(true);
					},
					failure: function(response) {
						Ext.MessageBox.alert(getText('common.err.failure'), response.responseText);
						button.enable(true);
					}
				});
				button.disable(true);
			}
		});
		var hierarchicalCombo = CWHF.createCombo(null, "textBoxSettingsList[0].minInteger",
				{disabled:inheritedConfig,
			labelIsLocalized:true,
			labelWidth:130,
			width:220,
			data:data["hierarchicalBehaviorOptions"],
			value:data["textBoxSettingsList[0].minInteger"]},
			{select: function(field,record) {
				var hierarchicalBehavior=field.getValue();
				if (hierarchicalBehavior===1) {
					recalculateButton.setVisible(true);
				} else {
					recalculateButton.setVisible(false);
				}
			}});

		if (data["textBoxSettingsList[0].minInteger"]===1) {
			//bottom up
			recalculateButton.setVisible(true);
		} else {
			recalculateButton.setVisible(false);
		}
		var items = [defaultOptionRadioGroup,
				CWHF.createNumberField(daysLabel, "textBoxSettingsList[0].defaultInteger", 0, null, null,
						{itemId:"defaultInteger",
						width:350,
						labelIsLocalized: true,
						value:data['textBoxSettingsList[0].defaultInteger'],
						disabled:inheritedConfig || !this.relativeDateSelected(defaultOptionRadioGroup)}),
				CWHF.createDateField(dateLabel, "textBoxSettingsList[0].defaultDate",
						{itemId: "defaultDate",
						value:data['textBoxSettingsList[0].defaultDate'],
						width:350,
						labelIsLocalized: true,
						disabled:inheritedConfig || !this.absoluteDateSelected(defaultOptionRadioGroup)}),
		minOptionRadioGroup,
		CWHF.createDateField(dateLabel, "textBoxSettingsList[0].minDate",
				{itemId: "minDate",
				value:data['textBoxSettingsList[0].minDate'],
				width:350,
				labelIsLocalized: true,
				disabled:inheritedConfig || !this.absoluteDateSelected(minOptionRadioGroup)}),
		maxOptionRadioGroup,
		CWHF.createDateField(dateLabel, "textBoxSettingsList[0].maxDate",
				{itemId: "maxDate",
				value:data['textBoxSettingsList[0].maxDate'],
				width:350,
				labelIsLocalized: true,
				disabled:inheritedConfig || !this.absoluteDateSelected(maxOptionRadioGroup)}),
		Ext.create('Ext.form.FieldContainer',{
				fieldLabel:data["hierarchicalBehaviorLabel"],
				labelWidth:130,
				labelStyle:{overflow:'hidden'},
				layout: 'hbox',
				items: [hierarchicalCombo, recalculateButton]
				})];
		this.add(items);
	},

	getSelectedRadio: function(radioGroup) {
		//0-empty, 1-now, 2-date, 3 x days after creation
		var checkedArr = radioGroup.getChecked();
		var checkedRadio;
		if (checkedArr.length>0) {
			checkedRadio = checkedArr[0];
			return checkedRadio.getSubmitValue();
		}
		return 0;
	},
	absoluteDateSelected: function(radioGroup) {
		var checkedArr = radioGroup.getChecked();
		var checkedRadio;
		if (checkedArr.length>0) {
			checkedRadio = checkedArr[0];
			return checkedRadio.getSubmitValue()===2;
		}
	},
	relativeDateSelected: function(radioGroup) {
		var checkedArr = radioGroup.getChecked();
		var checkedRadio;
		if (checkedArr.length>0) {
			checkedRadio = checkedArr[0];
			return checkedRadio.getSubmitValue()===3;
		}
	}
});
