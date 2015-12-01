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

Ext.define('com.trackplus.admin.project.ProjectCockpit',{
	extend:'Ext.Base',
	config: {
		projectID: null
	},

	constructor : function(config) {
		var me = this;
		var config = config || {};
		this.initConfig(config);
	},
	configButton:null,

	/**
	 * Initialize all actions and return the toolbar actions
	 */
	getToolbarActions: function() {
		var me=this;
		if (CWHF.isNull(this.configButton)) {
			this.configButton = new Ext.Button({
				overflowText:getText('admin.project.cockpit.lbl.configProject'),
				tooltip:getText('admin.project.cockpit.lbl.configProject'),
				text: getText('admin.project.cockpit.lbl.configProject'),
				iconCls: 'projectEdit',
				disabled:false,
				handler:function(){
					//project
					var urlEditScreen="dashboardEdit.action?backAction=admin.action&projectID="+me.getProjectID()+"&entityType=1";
					window.location.href=urlEditScreen;
				}
			});
			this.configReleaseButton = new Ext.Button({
				overflowText:getText('admin.project.cockpit.lbl.configRelease'),
				tooltip:getText('admin.project.cockpit.lbl.configRelease'),
				text: getText('admin.project.cockpit.lbl.configRelease'),
				iconCls: 'releaseEdit',
				disabled:false,
				handler:function(){
					//release
					var urlEditScreen="dashboardEdit.action?backAction=admin.action&projectID="+me.getProjectID()+"&entityType=9";
					window.location.href=urlEditScreen;
				}
			});
			this.actionAssignProjectCockpit =new Ext.Button({
				overflowText:getText('admin.project.lbl.resetProjectCockpit'),
				tooltip:getText('admin.project.lbl.resetProjectCockpit'),
				text: getText('admin.project.lbl.resetProjectCockpit'),
				iconCls: 'cockpitReset',
				disabled:false,
				handler:me.onAssignProjectCockpit,
				scope:me
			});
			this.actionAssignReleaseCockpit =new Ext.Button({
				overflowText:getText('admin.project.lbl.resetReleaseCockpit'),
				tooltip:getText('admin.project.lbl.resetReleaseCockpit'),
				text: getText('admin.project.lbl.resetReleaseCockpit'),
				iconCls: 'cockpitReset',
				disabled:false,
				handler:me.onAssignReleaseCockpit,
				scope:me
			});
		}
		return [this.configButton,this.actionAssignProjectCockpit,this.configReleaseButton,this.actionAssignReleaseCockpit];
	},
	onAssignProjectCockpit:function(){
		var me=this;
		com.trackplus.dashboard.resetDashboard('project!cokpitAssignment.action',
			{projectID:me.getProjectID(),entityType:1},me.projectCokpitAssignmentSuccess,me);
	},
	projectCokpitAssignmentSuccess: function() {
		CWHF.showMsgInfo(getText('admin.project.msg.resetProjectCockpit'));
	},
	onAssignReleaseCockpit:function(){
		var me=this;
		com.trackplus.dashboard.resetDashboard('project!cokpitAssignment.action',
			{projectID:me.getProjectID(),entityType:9}, me.releaseCokpitAssignmentSuccess,me);
	},
	releaseCokpitAssignmentSuccess: function() {
		CWHF.showMsgInfo(getText('admin.project.msg.resetReleaseCockpit'));
	},

	getDetailPanel: function() {
		this.panel= Ext.create('Ext.panel.Panel',{
			region: 'center',
			border: false,
			autoScroll: false,
			bodyStyle: 'padding:0px',
			layout:'anchor',
			items:[Ext.create('Ext.Component',{
				html: getText('admin.project.cockpit.help'),
				border:true,
				anchor:'100%',
				minWidth:650,
				cls:'infoBox_bottomBorder'
			})]
		});
		return this.panel;
	},
	loadDetailPanel:function(){
	},
	panelLoad: function(projectOrReleaseID) {
	},

	postLoadProcess: function(panel, data) {
	}
});



