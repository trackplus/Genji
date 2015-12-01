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


Ext.define('com.trackplus.util.SingleSelectPicker',{
	extend:'com.trackplus.util.MultipleSelectPicker',
	useNull:true,
	useTooltip:false,
	multiSelect:false,
	includeClear:false,
	onListSelectionChange:function(list,records){
		var me=this;
		if(me.ignoreSelection>0){
			return;
		}
		me.setValue(records,false);
		me.collapse();
	},
	clearSelection:function(){
		var me=this;
		me.callParent(arguments);
		me.collapse();
	},
	getSubmitValue: function() {
		return this.getValue();
	},
	getValue: function() {
		if(this.value){
			var value = Ext.Array.from(this.value);
			if(value.length>0) {
				return value[0];
			}
		}
		return null;
	}
});
