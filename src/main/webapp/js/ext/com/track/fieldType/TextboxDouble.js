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

Ext.define('js.ext.com.track.fieldType.TextboxDouble', {
	extend: 'Ext.form.FieldSet',
	itemId: 'specificItem',
	items: [],
	onDataReady : function(data) {
		var inheritedConfig = data.inheritedConfig;
		this.add([CWHF.createNumberField(data['defaultDoubleLabel'],
				"textBoxSettingsList[0].defaultDouble",
				null, null, null,
				{value:data['textBoxSettingsList[0].defaultDouble'],
				labelIsLocalized:true,
				width:250,
				disabled:inheritedConfig,
				labelWidth:data["labelWidth"]}),
			CWHF.createNumberField(data['minDoubleLabel'],
				"textBoxSettingsList[0].minDouble",
				null, null, null,
				{value:data['textBoxSettingsList[0].minDouble'],
				labelIsLocalized:true,
				width:250,
				disabled:inheritedConfig,
				labelWidth:data["labelWidth"]}),
			CWHF.createNumberField(data['maxDoubleLabel'],
				"textBoxSettingsList[0].maxDouble",
				null, null, null,
				{value:data['textBoxSettingsList[0].maxDouble'],
				labelIsLocalized:true,
				width:250,
				disabled:inheritedConfig,
				labelWidth:data["labelWidth"]})]);
	}
});
