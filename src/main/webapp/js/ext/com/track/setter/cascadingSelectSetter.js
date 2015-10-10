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

Ext.define("js.ext.com.track.setter.cascadingSelectSetter", {
	extend: "Ext.panel.Panel",
	border:false,
	items: [],
	layout:'anchor',
	config: {
		jsonData: {}
	},
	initComponent: function() {
		this.itemId = this.jsonData['name'];
		this.callParent();
		Ext.Array.forEach(this.jsonData['parts'], function(partListData, index) {
			var partName = this.itemId + "_" + index;
			this.add(CWHF.createCombo("", partName,
					{data:partListData['dataSource'],
					value:partListData['value'],
					labelIsLocalized:true,
					width:200,
					labelWidth:this.jsonData["labelWidth"]/*,
					anchor:'100%'*/},
					{select: {fn: this.changeCompositePart, scope:this}},
						partName)
				);
		}, this);
	},

	/**
	 * Reload the value part of a bulk expression after changing the composite select part
	 */
	changeCompositePart: function(combo, records, options) {
		var params = new Object();
		params["activityParams"] = this.getSetterValue();
		params["activityTypeID"] = this.jsonData.fieldID;
		params["setterID"] = 1;
		Ext.Ajax.request({
			url: 'workflowActivityConfig!changeSetter.action',
			params: params,
			scope: this,
			disableCaching:true,
			success: function(response) {
				var cascadingListData = Ext.decode(response.responseText);
				var parts = cascadingListData.jsonConfig.parts;
				for (var i=0;i<this.items.getCount();i++) {
					var partCombo = this.items.getAt(i);
					if (partCombo!=null) {
						var dataSource = parts[i].dataSource;
						var value =   parts[i].value;
						partCombo.store.loadData(dataSource);
						partCombo.setValue(value);
					}
				}
			},
			failure: function(result){
				Ext.MessageBox.alert(this.failureTitle, result.responseText)
			},
			method:"POST"
		});
	},

	getSetterValue:function() {
		var valueArr = [];
		for (var i=0;i<this.items.getCount();i++) {
			var partCombo = this.items.getAt(i);
			var value = partCombo.getValue();
			if (value!=null) {
				valueArr.push(value);
			}
		}
		return valueArr.join();
	},

	getSetterDisplayValue:function(){
		var labelsArr = [];
		for (var i=0;i<this.items.getCount();i++) {
			var partCombo = this.items.getAt(i);
			if (partCombo!=null) {
				var partValue = partCombo.getValue();
				if (partValue!=null) {
					var index = partCombo.getStore().find('id', partValue);
					if (index!=null) {
						var partLabel = partCombo.getStore().getAt(index).get('label');
						if (partLabel!=null) {
							labelsArr.push(partLabel);
						}
					}
				}
			}
		}
		return labelsArr.join();
	}
});
