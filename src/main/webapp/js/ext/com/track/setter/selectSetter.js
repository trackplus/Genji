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

Ext.define("js.ext.com.track.setter.selectSetter", {
	extend: "Ext.form.field.ComboBox",
	displayField: 'label',
	valueField	 : 'id',
	queryMode: 'local',
	typeAhead: true,
	triggerAction: 'all',
	width:200,
	config: {
		jsonData: {}
	},
	initComponent: function() {
		this.itemId = this.jsonData.name;
		this.name = this.jsonData.name;
		this.assignment = this.jsonData.assignment;
		this.activityTypeLabel = this.jsonData.activityTypeLabel;
		this.store = new Ext.data.Store({
			data	: this.jsonData.dataSource,
			fields	: [{name:'id', type:'int'}, {name:'label', type:'string'}],
			autoLoad: false
		});
		var fieldLabel = "";
		if (this.assignment && this.assignment && this.activityTypeLabel) {
			fieldLabel = this.activityTypeLabel;
		}
		this.value = this.jsonData.value;
		this.labelWidth=this.jsonData.labelWidth;
		this.fieldLabel = fieldLabel;
		this.callParent();
	},
	getSetterValue:function(){
		return this.getValue();
	},
	getSetterDisplayValue:function(){
		var v=this.getValue();
		if(CWHF.isNull(v)){
			return null;
		}
		var r = this.getStore().find('id',v);
		return this.getStore().getAt(r).get('label');
	}
});
