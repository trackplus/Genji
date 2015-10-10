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

Ext.define('js.ext.com.track.fieldType.ItemPicker', {
	extend: 'Ext.form.FieldSet',
	itemId: 'specificItem',
	items: [],
	onDataReady : function(data) {
		var inheritedConfig = data.inheritedConfig;
		var datasourceOption = data['generalSettingsList[0].integerValue'];
		var datasourceOptionRadioGroup = CWHF.getRadioButtonItems(data['dataSourceList'], 'generalSettingsList[0].integerValue', 'id', 'label',
				datasourceOption, inheritedConfig, true);
		var datasouceOptionRadioGroup = CWHF.getRadioGroup("datasourceOption", data['dataSourceLabel'],
				500, datasourceOptionRadioGroup, {labelIsLocalized:true, labelWidth:150}, {
					change: function(radioGroup, newValue, oldValue, options) {
						radioGroup.ownerCt.datasourceChanged(radioGroup);
		}});
		var projectReleasePickerCfg = {useRemoveBtn:false, useNull:true, useTooltip:false, labelIsLocalized:true,
	            labelWidth: 150, width:400, disabled:inheritedConfig || datasourceOption!=1, itemId:"projectRelease"};
		var projectReleasePicker = CWHF.createSingleTreePicker(data["projectReleaseLabel"], "generalSettingsList[1].integerValue",
				data["projectReleaseTree"], data["generalSettingsList[1].integerValue"], projectReleasePickerCfg);
		var includeClosed = CWHF.createCheckbox(data["includeClosedLabel"],
				"generalSettingsList[3].characterValueString",
				{disabled:inheritedConfig  || datasourceOption!=1,
				labelIsLocalized:true,
				value:data["generalSettingsList[3].characterValueString"],
				labelWidth:150, itemId:"includeClosed"});
        var filterCfg = {useRemoveBtn:false, useNull:true, useTooltip:false, labelIsLocalized:true,
            labelWidth: 150, width:400, disabled:inheritedConfig || datasourceOption!=2, itemId:"filters"};
        var filterPicker = CWHF.createSingleTreePicker(data["filterLabel"],  "generalSettingsList[2].integerValue",
           data["filterTree"], data["generalSettingsList[2].integerValue"], filterCfg);
		this.add([datasouceOptionRadioGroup, projectReleasePicker, includeClosed, filterPicker]);
	},

	datasourceChanged: function(radioGroup) {
		var checkedArr = radioGroup.getChecked();
		var projectReleasePicker = this.getComponent("projectRelease");
		var filterPicker = this.getComponent("filters");
		var includeClosedCheckbox = this.getComponent("includeClosed");
		if (checkedArr.length>0) {
			checkedRadio = checkedArr[0];
			//projectRelease=1, filter=2
			if (projectReleasePicker!=null) {
				projectReleasePicker.setDisabled(checkedArr[0].getSubmitValue()!=1);
			}
			if (includeClosedCheckbox!=null) {
				includeClosedCheckbox.setDisabled(checkedArr[0].getSubmitValue()!=1);
			}
			if (filterPicker!=null) {
				filterPicker.setDisabled(checkedArr[0].getSubmitValue()!=2);
			}
		}
	}
});
