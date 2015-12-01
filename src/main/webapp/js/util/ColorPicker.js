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

Ext.define('com.trackplus.util.ColorField', {
		extend: 'Ext.form.field.Trigger',
		triggerTip: 'Please select a color.',


		onTriggerClick: function() {
		var me = this;
		picker = Ext.create('Ext.picker.Color', {
			//pickerField: this,
			ownerCt: this,
			renderTo: Ext.getBody(),//document.body,
			floating: true,
			hidden: true,
			focusOnShow: true,
			defaultAlign:"tl-bl?",
			style: {
					backgroundColor: "#fff"
				},
			listeners: {
					scope:this,
					select: function(field, value, opts){
						me.setValue(value);
						//me.inputEl.setStyle({backgroundColor:'#' + value});
						me.setFieldStyle('background-color:#'+value +'; background-image: none;');
						picker.hide();
					},
					show: function(field, opts){
						field.getEl().monitorMouseLeave(500, field.hide, field);
					}
					/*activate: function(field, opts) {
						me.setFieldStyle('background-color:#'+me.value +'; background-image: none;');
					}*/
			}
		});
		//picker.alignTo(me.inputEl/*, 'tl-bl?'*/);
		//picker.setWidth(me.inputEl.getWidth());
		picker.showBy(me.inputEl);
		}
	});
