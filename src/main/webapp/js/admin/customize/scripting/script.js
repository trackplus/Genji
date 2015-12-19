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


Ext.define("com.trackplus.admin.customize.script.Script",{
	extend:"com.trackplus.admin.GridBase",
	xtype: "script",
    controller: "script",
    storeUrl: "script!loadScripts.action",
    fields: [{name: "id",type:"int"},
	   	        {name: "name",type:"string"},
		        {name: "scriptType", type: "int"},
		        {name: "typeLabel", type: "string"}],
    columns: [{text:getText("admin.customize.script.lbl.className"),
				width:400,dataIndex: "name", sortable:true,
				filter: {
		            type: "string"
		        }},
			{text:getText("admin.customize.script.lbl.scriptType"),
				width:150,dataIndex: "typeLabel", sortable:true}],
	
	getEntityLabel:function(){
		return getText("admin.customize.script.lbl");
	},
	
	/**
	 * The iconCls for the add button, overwrites base class icon
	 */
	getAddIconCls: function() {
		return "scriptAdd";
	},
	
	/**
	 * The iconCls for the edit button, overwrites base class icon
	 */
	getEditIconCls: function() {
		return "scriptEdit";
	}	
});
