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

Ext.define("com.trackplus.admin.NotifyTriggerList", {
	extend: "com.trackplus.admin.GridBase",
	xtype: "notifyTriggerList",
    controller: "notifyTriggerList",
	config : {
	    defaultSettings : false
	},
	columns: [ {
	    text : getText('common.lbl.name'),
	    flex : 1,
	    dataIndex : 'label',
	    sortable : true,
		filter: {
            type: "string"
        }
	}, {
		text : getText('admin.customize.automail.trigger.lbl.type'),
	    width : 150,
	    dataIndex : 'typeLabel',
	    sortable : true
	}], 
	
	initComponent : function() {
		this.fields =[{
					    name : 'id',
					    type : 'int'
					}, {
					    name : 'label',
					    type : 'string'
					}, {
					    name : 'typeLabel',
					    type : 'string'
					}, {
					    name : 'own',
					    type : 'boolean'
					}];
		this.storeUrl = "notifyTrigger!loadList.action";
		this.callParent();
	},
	
	getEntityLabel : function() {
	    return getText('admin.customize.automail.trigger.lblOperation');
	},

	/**
	 * Get extra parameters for grid load
	 */
	getLoadGridParams : function() {
	    return {
		    defaultSettings : this.defaultSettings
	    };
	},

	
	/**
	 * If not own allow only copy
	 */
	getGridContextMenuActions : function(selectedRow) {
	    var canModify = selectedRow.data["own"];
	    if (canModify) {
		    return [this.actionEdit, this.actionCopy, this.actionDelete];
	    } else {
		    return [this.actionCopy];
	    }
	}
});
