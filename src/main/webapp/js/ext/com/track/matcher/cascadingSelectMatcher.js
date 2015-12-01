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

Ext.define("js.ext.com.track.matcher.cascadingSelectMatcher", {
	extend: "Ext.panel.Panel",
	border:false,
	items: [],
	config: {
		jsonData: {}
	},
	selectHandler:null,
	scope: null,
	expressionPanel: null,
	fieldExpression: null,
	matcherCombo: null,
	contextFields: null,
	initComponent: function() {
		this.numberOfParts = this.jsonData.numberOfParts;
		this.callParent();
		for (var i=1;i<=this.numberOfParts;i++) {
			this.add(CWHF.createCombo(null,
					this.jsonData.partNames[i], {disabled:this.jsonData.disabled, data:this.jsonData.dataSource[i], value:this.jsonData.partValues[i], width:150}));
			//this.add(this.createCombo(this.jsonData.partNames[i], this.jsonData.disabled,
			//		this.jsonData.dataSource[i], this.jsonData.partValues[i]))
			/*,		{select: {fn: this.jsonData.selectHandler, scope:this.jsonData.scope, panel:this.jsonData.expressionPanel,
						fieldExpression:this.jsonData.fieldExpression, matcherCombo:this.jsonData.matcherCombo,
						contextFields: this.jsonData.contextFields}}*/
		}
	},
	
	addListeners: function() {
		for (var i=0;i<this.items.getCount()-1;i++) {
			var combo = this.items.getAt(i);
			combo.on("select", this.selectHandler, this.scope, {panel:this.expressionPanel,
				fieldExpression:this.fieldExpression, matcherCombo:this.matcherCombo,
				contextFields: this.contextFields});
		}
	},
	
	getStringValue: function() {
		var stringValue = [];
		for (var i=0;i<this.items.getCount();i++) {
			stringValue.push(this.items.getAt(i).getValue());
		}
		return stringValue.join();
	}/*,
	
	createCombo: function(name, disabled, data, value, listenerConfig) {
		if (CWHF.isNull(data)) {
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
		if (value) {
			comboConfig.value = value;
		}
		if (listenerConfig) {
			comboConfig.listeners = listenerConfig;
		}
		return Ext.create('Ext.form.field.ComboBox', comboConfig);
	}*/
});
