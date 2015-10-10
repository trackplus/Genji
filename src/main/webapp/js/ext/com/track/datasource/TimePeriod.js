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

Ext.define("js.ext.com.track.datasource.TimePeriod",{
	extend: "js.ext.com.track.datasource.BasicDatasource",
	config: {
		periodTypeOptions: null,
		periodType: null,
		periodTypeName:null,
		dateFrom: null,
		dateFromName: null,
		dateTo: null,
		dateToName: null,
		daysBefore: null,
		daysBeforeName: null
	},
	FROM_TO: 1,
	DAYS_BEFORE: 2,
	CURRENT_MONTH: 3,
	LAST_MONTH: 4,
	periodTypeCombo: null,
	dateFromPicker: null,
	dateToPicker: null,
	daysBefore: null,
	dateDaysWidth: 250,
	initComponent: function() {
		this.callParent();
		Ext.Array.forEach(this.periodTypeOptions, function(periodTypeOption, index) {
			periodTypeOption.label = getText(periodTypeOption.label);
		}, this);
		this.periodTypeCombo = CWHF.createCombo("common.timePeriod", this.periodTypeName,
				{value:this.periodType,
			data:this.periodTypeOptions,
			labelWidth: this.labelWidth,
			labelAlign: this.labelAlign,
			labelStyle: this.labelStyle,
			width: this.width},
			{select: {fn: this.periodTypeChanged, scope:this}});
		this.dateFromPicker = CWHF.createDateField('common.timePeriod.dateFrom', this.dateFromName,
				{value:this.dateFrom,
			disabled:this.periodType!=this.FROM_TO,
			labelWidth: this.labelWidth,
			labelAlign: this.labelAlign,
			labelStyle: this.labelStyle,
			submitFormat: com.trackplus.TrackplusConfig.DateFormat,
			width: this.dateDaysWidth});
		this.dateToPicker = CWHF.createDateField('common.timePeriod.dateTo', this.dateToName,
				{value:this.dateTo,
			disabled:this.periodType!=this.FROM_TO,
			labelWidth: this.labelWidth,
			labelAlign: this.labelAlign,
			labelStyle: this.labelStyle,
			submitFormat: com.trackplus.TrackplusConfig.DateFormat,
			width: this.dateDaysWidth});
		this.daysBeforePicker = CWHF.createNumberField("common.timePeriod.daysBefore", this.daysBeforeName, 0, null, null,
				{value:this.daysBefore,
			disabled:this.periodType!=this.DAYS_BEFORE,
			labelWidth: this.labelWidth,
			labelAlign: this.labelAlign,
			labelStyle: this.labelStyle,
			width: this.dateDaysWidth});
		//this.add(this.daysBeforePicker);
		this.add( {
			xtype: 'fieldset',
			itemId: 'fsTimePeriod',
			title: getText('common.timePeriod'),
			collapsible: false,
			defaultType: 'textfield',
			layout: 'anchor',
			items: [this.periodTypeCombo, this.dateFromPicker, this.dateToPicker, this.daysBeforePicker]});
	},

	periodTypeChanged: function(combo, records, options) {
		var periodType = combo.getValue();
		this.dateFromPicker.setDisabled(periodType!=this.FROM_TO);
		this.dateToPicker.setDisabled(periodType!=this.FROM_TO);
		this.daysBeforePicker.setDisabled(periodType!=this.DAYS_BEFORE);
	}
});
