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

/**
 * Class for role and account assignments for project
 */
Ext.define("com.trackplus.admin.SimpleAssignmentController",{
	extend: "Ext.app.ViewController",
	alias: "controller.simpleAssignment",
	mixins: {
		baseController: "com.trackplus.admin.AdminBaseController"
	},
	
	dropAssign: function(node, data, dropRec, dropPosition) {
		if(data && data.records && data.records.length>0){
			var idsArray = new Array();
			for ( var i = 0; i < data.records.length; i++) {
				idsArray[i] = data.records[i].data.id;
			}
			var params=this.getView().getDetailParams();
			if (CWHF.isNull(params)) {
				params=new Object();
			}
			params["assign"]=idsArray.join();
			this.reloadAssigned(this.getView().getBaseServerAction()+"!assign.action", params);
		}
	},
	
	
	dropUnassign: function(node, data, dropRec, dropPosition) {
		if(data && data.records && data.records.length>0){
			var idsArray = new Array();
			for ( var i = 0; i < data.records.length; i++) {
				idsArray[i] = data.records[i].data.id;
			}
			var params=this.getView().getDetailParams();
			if (CWHF.isNull(params)) {
				params=new Object();
			}
			params["unassign"]=idsArray.join();
			this.reloadAssigned(this.getView().getBaseServerAction()+"!unassign.action", params);
		}
	},
	
	reloadAssigned: function(urlStr,params) {
		Ext.Ajax.request({
			url:urlStr,
			params:params,
			disableCaching:true,
			scope: this,
			success: function(result) {
				var jsonData=Ext.decode(result.responseText);
				if(jsonData.success===true) {
					this.getView().assignedGrid.store.loadData(jsonData["assigned"], false);
					this.getView().availableGrid.store.loadData(jsonData["unassigned"], false);
				} else {
					com.trackplus.util.requestFailureHandler(result);
				}
			},
			failure: function(result){
				com.trackplus.util.requestFailureHandler(result);
			}
		});
	},
						
		
		
});
