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

Ext.define("js.ext.com.track.datasource.FilterHistory",{
	extend: "js.ext.com.track.datasource.TimePeriod",
	config: {
		excludeWithoutHistory: null,
		excludeWithoutHistoryName: null,
		fieldOptions: null,
		fields: null,
		fieldsName: null,
		personOptions: null,
		person: null,
		personName: null
	},
	initComponent: function() {
		this.callParent();
		this.excludeWithoutHistory = CWHF.createCheckbox("filterHistoryConfig.prompt.excludeWithoutHistory", this.excludeWithoutHistoryName,
				{value:this.excludeWithoutHistory,
			labelWidth: this.labelWidth,
			labelAlign: this.labelAlign,
			labelStyle: this.labelStyle,
			width: this.width
				});
		/*this.selectedFields =  CWHF.createMultiSelect(null, this.fieldsName,
				{data: this.fieldOptions,
			value: this.fields,
			labelWidth: this.labelWidth,
			labelAlign: this.labelAlign,
			labelStyle: this.labelStyle,
			width: this.width,
			height: 100,
			fieldLabel:"filterHistoryConfig.prompt.fields"});*/

		this.selectedFields = CWHF.createMultipleSelectPicker(null, this.fieldsName, this.fieldOptions, this.fields,
				{width: this.width,
				margin:'0 0 5 0',
				labelWidth:this.labelWidth,
				labelAlign: this.labelAlign,
				fieldLabel:getText("filterHistoryConfig.prompt.fields"),
				useRemoveBtn:false, useNull:true});
		this.changedByPerson = CWHF.createCombo("filterHistoryConfig.prompt.changedByPerson", this.personName,
				{value:this.person,
			data:this.personOptions,
			labelWidth: this.labelWidth,
			labelAlign: this.labelAlign,
			labelStyle: this.labelStyle,
			width: this.width});
		this.add( {
			xtype: 'fieldset',
			itemId: 'fsOther',
			title: getText('common.otherSettings'),
			collapsible: false,
			layout: 'anchor',
			items: [this.excludeWithoutHistory, this.selectedFields, this.changedByPerson]});
	}
});
