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


Ext.define("com.trackplus.admin.user.FiltersInUserMenu",{
	extend:"com.trackplus.admin.GridBase",
	xtype: "filtersInUserMenu",
    controller: "filtersInUserMenu",
	config: {
			//person or group IDs
			personIDs: null
	},
	storeUrl: "filtersInMenu.action",
	fields: [{name: 'filterID',type:'int'},
	   			{name: 'filterLabel',type:'string'},
				{name: 'personID',type:'int', allowNull:true},
				{name: 'personName', type: 'string'},
				{name: 'first', type: 'boolean'}],
	columns: [{text: getText('common.lbl.filter'), flex:1,
				dataIndex: 'filterLabel', sortable:true, renderer:"filterRenderer",
				filter: {
		            type: "string"
		        }},
			{text:getText('admin.user.manage.lbl.filterAssignments.user'),
				flex:1, dataIndex:'personName', sortable:false}],
	allowMultipleSelections:true,
	useEdit:false,
	useCopy:false,
	
	initComponent : function() {
		this.style = {
				borderTop:'1px solid #D0D0D0',
				borderBottom:'1px solid #D0D0D0'
			};
		this.callParent();
	},
	
	getEntityLabel:function(){
		return getText("admin.user.manage.lbl.filterAssignments");
	},

	getGridSelectionModel: function() {
		return Ext.create('Ext.selection.CheckboxModel', {mode:"MULTI"});
	},

	/**
	 * Get extra parameters for grid load
	 */
	getStoreExtraParams:function() {
		return {personIDs:this.getPersonIDs()};
	}
	
});
