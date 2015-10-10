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

Ext.define('js.ext.com.track.fieldType.TextboxInteger', {
	extend: 'Ext.form.FieldSet',
	itemId: 'specificItem',
	items: [],
	onDataReady : function(data) {
		var inheritedConfig = data.inheritedConfig;
		this.add([CWHF.createNumberField(data['defaultIntegerLabel'],
				"textBoxSettingsList[0].defaultInteger",
				null, null, null,
				{value:data['textBoxSettingsList[0].defaultInteger'],
				labelIsLocalized:true,
				width:250,
				disabled:inheritedConfig,
				labelWidth:data["labelWidth"]}),
			CWHF.createNumberField(data['minIntegerLabel'],
				"textBoxSettingsList[0].minInteger",
				null, null, null,
				{value:data['textBoxSettingsList[0].minInteger'],
				labelIsLocalized:true,
				width:250,
				disabled:inheritedConfig,
				labelWidth:data["labelWidth"]}),
			CWHF.createNumberField(data['maxIntegerLabel'],
				"textBoxSettingsList[0].maxInteger",
				null, null, null,
				{value:data['textBoxSettingsList[0].maxInteger'],
				labelIsLocalized:true,
				width:250,
				disabled:inheritedConfig,
				labelWidth:data["labelWidth"]})]);
	}
});
