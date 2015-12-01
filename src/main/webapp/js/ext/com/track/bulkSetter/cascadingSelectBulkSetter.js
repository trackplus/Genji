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

Ext.define("js.ext.com.track.bulkSetter.cascadingSelectBulkSetter", {
	extend: "Ext.panel.Panel",
	border:false,
	items: [],
	config: {
		jsonData: {}
	},
	initComponent: function() {
		this.callParent();
		Ext.Array.forEach(this.jsonData["listsJson"], function(cascadingListData) {
			var cascadePanel = Ext.create("Ext.panel.Panel", {
				border:false,
				itemId: "list" + cascadingListData["listID"],
				items: []
			});
			this.add(cascadePanel);
			var mainLabel = cascadingListData["label"];
			Ext.Array.forEach(cascadingListData["parts"], function(partListData, index) {
				var label = " ";
				var width = 220;
				var labelWidth = 0;
				var labelSeparator = "";
				if (mainLabel) {
					//more lists in different contexts
					if (index===0) {
						label = mainLabel;
						labelSeparator = ":";
					}
					labelWidth = 150;
					width = 125 + 220;
				}
				cascadePanel.add(CWHF.createCombo(label, partListData["name"],
						{itemId:partListData["itemId"],
						disabled:this.jsonData["disabled"],
						data:partListData["dataSource"],
						value:partListData["value"],
						labelIsLocalized:true,
						labelSeparator: labelSeparator,
						width: width,
						labelWidth:labelWidth},
						{select: {fn: this.changeCompositePart, scope:this.jsonData.scope,
							fieldID:this.fieldID, relationItemId:this.relationItemId}})
					);
			}, this);
		}, this);
	},

	/**
	 * Reload the value part of a bulk expression after changing the composite select part
	 */
	changeCompositePart: function(combo, records, options) {
		var fieldID = options.fieldID;
		var params = new Object();
		var cascadePanel = combo.ownerCt;
		var expressionPanel = cascadePanel.ownerCt.ownerCt;
		var setterRelationCombo = expressionPanel.getComponent(options["relationItemId"]);
		var relationID = null;
		if (setterRelationCombo!=null) {
			relationID = setterRelationCombo.getValue();
		}
		for (var i=0;i<cascadePanel.items.getCount();i++) {
			var partCombo = cascadePanel.items.getAt(i);
			var name = partCombo.getName();
			var value = partCombo.getValue();
			params[name] = value;
		}
		var itemId = cascadePanel.itemId;
		var listID = itemId.substring(4);
		params["listID"] = listID;
		params["fieldID"] = fieldID;
		params["relationID"] = relationID;
		params["issueTypeID"] = this.getContextField(combo.ownerCt.ownerCt.ownerCt, 2);
		params["projectID"] = this.getContextField(combo.ownerCt.ownerCt.ownerCt, 1);
		params["selectedIssueIDs"] = this.getSelectedIssueIDs();
		Ext.Ajax.request({
			url: "massOperationFieldSetterAction!compositePartChange.action",
			params: params,
			scope: this,
			disableCaching:true,
			success: function(response) {
				var cascadingListData = Ext.decode(response.responseText);
				Ext.Array.forEach(cascadingListData, function(partListData) {
					var itemId = partListData.itemId;
					var dataSource = partListData.dataSource;
					var value = partListData.value;
					var combo = cascadePanel.getComponent(itemId);
					combo.store.loadData(dataSource);
					combo.setValue(value);
				}, this);
			},
			failure: function(result){
				Ext.MessageBox.alert(this.failureTitle, result.responseText)
			},
			method:"POST"
		});
	},

	/**
	 * Get the field value as a json
	 */
	getFieldValueJson: function() {
		var valueJson = new Object();
		for (var i=0;i<this.items.getCount();i++) {
			var panel = this.items.getAt(i);
			for (var j=0;j<panel.items.getCount();j++) {
				var combo = panel.items.getAt(j);
				var name = combo.getName();
				var value = combo.getValue();
				valueJson[name] = value;
			}
		}
		return valueJson;
	},

	setDisabled: function(disabled) {
		for (var i=0;i<this.items.getCount();i++) {
			var panel = this.items.getAt(i);
			for (var j=0;j<panel.items.getCount();j++) {
				var combo = panel.items.getAt(j);
				combo.setDisabled(disabled);
			}
		}
	}
});
