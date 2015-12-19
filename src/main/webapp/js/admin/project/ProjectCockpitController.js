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

Ext.define("com.trackplus.admin.project.ProjectCockpitController",{
	extend: "Ext.app.ViewController",
	alias: "controller.projectCockpit",
	mixins: {
		baseController: "com.trackplus.admin.AdminBaseController"
	},
	
	onConfigProject: function(button, event) {
		var urlEditScreen="dashboardEdit.action?backAction=admin.action&projectID="+this.getView().getProjectID()+"&entityType=1";
		window.location.href=urlEditScreen;
	},

	onConfigRelease: function(button, event) {
		var urlEditScreen="dashboardEdit.action?backAction=admin.action&projectID="+this.getView().getProjectID()+"&entityType=9";
		window.location.href=urlEditScreen;
	},

	onAssignProjectCockpit:function(){
		com.trackplus.dashboard.resetDashboard('project!cokpitAssignment.action',
			{projectID:this.getView().getProjectID(),entityType:1},this.projectCokpitAssignmentSuccess,this);
	},
	
	projectCokpitAssignmentSuccess: function() {
		CWHF.showMsgInfo(getText('admin.project.msg.resetProjectCockpit'));
	},
	
	onAssignReleaseCockpit:function(){
		com.trackplus.dashboard.resetDashboard('project!cokpitAssignment.action',
			{projectID:this.getView().getProjectID(),entityType:9}, this.releaseCokpitAssignmentSuccess,this);
	},
	
	releaseCokpitAssignmentSuccess: function() {
		CWHF.showMsgInfo(getText('admin.project.msg.resetReleaseCockpit'));
	}
});



