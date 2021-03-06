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

Ext.define('js.ext.com.track.fieldType.TextboxText', {
	extend: 'Ext.form.FieldSet',
	itemId: 'specificItem',
	items: [],
	onDataReady : function(data) {
		var inheritedConfig = data.inheritedConfig;
		this.add([CWHF.createTextField(data['defaultTextLabel'],
				"textBoxSettingsList[0].defaultText",
				{value:data['textBoxSettingsList[0].defaultText'],
				disabled:inheritedConfig,
				labelIsLocalized:true,
				labelWidth:data["labelWidth"],
				width:450
				}),
			CWHF.createNumberField(data['minTextLengthLabel'],
				"textBoxSettingsList[0].minTextLength",
				0, data['minValue'], data['maxValue'],
				{value:data['textBoxSettingsList[0].minTextLength'],
				labelIsLocalized:true,
				width:250,
				disabled:inheritedConfig,
				labelWidth:data["labelWidth"]}),
			CWHF.createNumberField(data['maxTextLengthLabel'],
				"textBoxSettingsList[0].maxTextLength",
				0, data['minValue'], data['maxValue'],
				{value:data['textBoxSettingsList[0].maxTextLength'],
				labelIsLocalized:true,
				width:250,
				disabled:inheritedConfig, labelWidth:data["labelWidth"]})]);
	}

});
