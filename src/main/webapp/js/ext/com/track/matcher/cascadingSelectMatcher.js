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

Ext.define("js.ext.com.track.matcher.cascadingSelectMatcher", {
	extend: "Ext.panel.Panel",
	border:false,
	items: [],
	config: {
		jsonData: {}
	},
	initComponent: function() {
		this.itemId = this.jsonData.name;//used for removing this panel after matcher or field change
		this.numberOfParts = this.jsonData.numberOfParts;
		this.callParent();
		for (var i=1;i<=this.numberOfParts;i++) {
			this.add(this.createCombo(this.jsonData.partNames[i], this.jsonData.disabled,
					this.jsonData.dataSource[i], this.jsonData.partValues[i],
					{select: {fn: this.jsonData.selectHandler, scope:this.jsonData.scope, panel:this.jsonData.expressionPanel,
						fieldExpression:this.jsonData.fieldExpression, matcherCombo:this.jsonData.matcherCombo,
						contextFields: this.jsonData.contextFields}}
					));
		}
	},
	getStringValue: function() {
		var stringValue = [];
		for (var i=0;i<this.items.items.length;i++) {
			stringValue.push(this.items.items[i].getValue());
		}
		return stringValue.join();
	},
	createCombo: function(name, disabled, data, value, listenerConfig) {
		if (data==null) {
			data = [];
		}
		var comboConfig = {xtype	: 'combo',
			itemId	: name,//used in getStringValue
			store: Ext.create('Ext.data.Store', {
				data	: data,
				fields	: [{name:'id', type:'int'}, {name:'label', type:'string'}],
				autoLoad: false
				}),
			displayField: 'label',
			valueField	: 'id',
			queryMode	: 'local',
			triggerAction: 'all',
			name:	name,
			disabled:	disabled
		};
		if (value!=null) {
			comboConfig.value = value;
		}
		if (listenerConfig!=null) {
			comboConfig.listeners = listenerConfig;
		}
		return Ext.create('Ext.form.field.ComboBox', comboConfig);
	}
});
