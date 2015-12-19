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


Ext.define("com.trackplus.admin.user.UserRolesInProject",{
	extend:"com.trackplus.admin.GridBase",
	xtype: "userRolesInProject",
    controller: "userRolesInProject",
    config: {
		//person or group ID
		personID: null,
		//whether it is group or person
		group: null
	},
	storeUrl: "rolesInProjects.action",
	fields: [{name:"projectID", type:"int"},
	   			{name:"projectLabel", type:"string"},
				{name:"roleID", type:"int"},
				{name:"roleLabel", type: "string"},
				{name:"direct", type: "boolean"},
				{name:"first", type: "boolean"}],
	columns:[{text: getText("admin.project.lbl.project"), flex:1,
				dataIndex: "projectLabel", sortable:true, renderer:"projectRenderer",
		        filter: {
		            type: "string"
		        }},
			{text: getText("admin.customize.role.lbl.role"),
				flex:1, dataIndex:"roleLabel", sortable:false, renderer:"roleRenderer"}],
	allowMultipleSelections:true,
	useEdit:false,
	useCopy:false,
	
	initComponent : function() {
		this.style = {
				borderTop:"1px solid #D0D0D0",
				borderBottom:"1px solid #D0D0D0"
			};
		this.callParent();
	},
		
	getEntityLabel:function(){
		return getText("admin.user.group.lbl.roleAssignment");
	},

	getGridSelectionModel: function() {
		return Ext.create("Ext.selection.CheckboxModel", {mode:"MULTI"});
	},

	/**
	 * Get extra parameters for grid load
	 */
	getStoreExtraParams:function() {
		return {personID:this.getPersonID(), group:this.getGroup()};
	}

});
