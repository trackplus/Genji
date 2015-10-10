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


Ext.define('com.trackplus.util.SingleTreePicker',{
	extend:'com.trackplus.util.MultipleTreePicker',
	useNull:true,
	useTooltip:false,
    includeClear:false,
	addBoundListListeners:function(){
		var me=this;
		me.callParent(arguments);
		me.boundList.addListener("select", me.treeSelect, me);
	},
	treeSelect:function(rowModel,record) {
		var me=this;
        var selectable = record.data["selectable"];
		if (selectable!=null && selectable==false) {
			return;
		}
		me.setValue(record,false);
		me.collapse();
		me.fireEvent('select', me, record);
	},
	getClearSelectionLabel:function(){
		var me=this;
		return me.lblClearSelection;
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
		if(this.value!=null&&this.value.length>0){
			return this.value[0];
		}
		return null;
	}
});
