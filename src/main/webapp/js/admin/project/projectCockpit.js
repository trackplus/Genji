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

Ext.define("com.trackplus.admin.project.ProjectCockpit",{
	extend:"Ext.panel.Panel",
	xtype: "projectCockpit",
    controller: "projectCockpit",
    mixins:{
		actionsBase: "com.trackplus.admin.ActionBase"
	},
	config: {
		projectID: null
	},
	region: "center",
	border: false,
	layout:'anchor',
	items:[Ext.create("Ext.Component",{
			html: getText("admin.project.cockpit.help"),
			border:true,
			anchor:'100%',
			minWidth:650,
			cls:'infoBox_bottomBorder'
		})],	
		
	initComponent: function() {
		this.initActions();
		this.dockedItems = this.getToolbar();
		this.callParent();	
	},
	
	/**
	 * Initialize all actions and return the toolbar actions
	 */
	initActions: function() {
		this.actions = [];
		this.configProject = CWHF.createAction("admin.project.cockpit.lbl.configProject", "projectEdit", "onConfigProject");
		this.actions.push(this.configProject);
		this.configRelease = CWHF.createAction("admin.project.cockpit.lbl.configRelease", "releaseEdit", "onConfigRelease");
		this.actions.push(this.configRelease);
		this.actionAssignProjectCockpit = CWHF.createAction("admin.project.lbl.resetProjectCockpit", "cockpitReset", "onAssignProjectCockpit");
		this.actions.push(this.actionAssignProjectCockpit);
		this.actionAssignReleaseCockpit = CWHF.createAction("admin.project.lbl.resetReleaseCockpit", "cockpitReset", "onAssignReleaseCockpit");
		this.actions.push(this.actionAssignReleaseCockpit);	
	}
});



