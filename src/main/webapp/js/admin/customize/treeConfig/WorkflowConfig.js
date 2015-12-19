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
 * Although it inherits from TreeConfig which is a treeBase,
 * some behavior is similar to treeWithGrid that's why some methods of
 * treeWithGrid are implemented again (alternatively multiple inheritance?)
 *
 */
Ext.define("com.trackplus.admin.customize.treeConfig.WorkflowConfig",{
	extend:"com.trackplus.admin.customize.treeConfig.AssignmentConfig",
	xtype: "workflowConfig",
    controller: "workflowConfig",
	treeWidth: 300,
	
	config: {
		rootID: "workflow",
		projectOrProjectTypeID: null
	},
	
	baseServerAction: "workflowAssignment",
	
	/**
	 * The message to appear first time after selecting this menu entry
	 * Is should be shown by selecting the root but the root is typically not visible
	 */
	getRootMessage: function(rootID) {
		return getText('admin.customize.workflow.config.lbl.description');
	},

	/**
	 * The localized leaf name
	 */
	getEntityLabel: function() {
		return getText('admin.customize.workflow.config.lbl.workflowAssignment');
	},


	getEntityConfigLabelSingular: function() {
		return getText("admin.customize.workflow.config.lbl.workflow");
	},

	getEntityConfigLabelPlural: function() {
		return getText("admin.customize.workflow.config.lbl.workflows");
	},

	isDisabledAddAction:function(){
		var sysAdmin=com.trackplus.TrackplusConfig.user.sys;
		return (sysAdmin===false);
	},
	
	isDisabledImportAction:function(){
		var sysAdmin=com.trackplus.TrackplusConfig.user.sys;
		return (sysAdmin===false);
	},
	
	/**
	 * Which actions to enable/disable depending on tree selection
	 */
	getToolbarActionChangesForTreeNodeSelect: function(node) {
		if (node) {
			var inheritedConfig=node.get("inheritedConfig");
			this.actionReset.setDisabled(inheritedConfig);
			var nodeID = node.get("id");
			this.actionApply.setDisabled(!this.isAssignable(node));
		}
	},

	isAssignable: function(node) {
		var nodeID = node.get("id");
		return nodeID!=="workflow_issueType" && nodeID!=="workflow_projectType" && nodeID!=="workflow_project"
	},

	/**
	 * Get the actions available in context menu depending on the currently selected row
	 */
	getTreeContextMenuActions: function(selectedRecord, selectionIsSimple) {
		var actions = [];
		if (selectionIsSimple) {
			var inheritedConfig = selectedRecord.data['inheritedConfig'];
			var leaf = selectedRecord.isLeaf();
			if (!inheritedConfig) {
				actions.push(this.actionReset);
			}
			if (!leaf) {
				actions.push(this.actionReload);
			}
		}
		return actions;
	},
	
	getGridContextMenuActions: function(selectedRecord, selectionIsSimple) {
		var sysAdmin=com.trackplus.TrackplusConfig.user.sysAdmin;
		if (selectionIsSimple) {
			var selectedTreeNode = this.getSingleSelectedRecord(true);
			if (selectedTreeNode && selectedTreeNode.isLeaf()) {
				if(sysAdmin){
					return [this.actionApply, this.actionEdit, this.actionCopy, this.actionDelete, this.actionDesign];
				}else{
					return [this.actionDesign];
				}
			} else {
				if(sysAdmin){
					return [this.actionEdit, this.actionCopy, this.actionDelete, this.actionDesign];
				}else{
					return [this.actionDesign];
				}
			}
		} else {
			if(sysAdmin){
				return [this.actionDelete];
			}else{
				return null;
			}
		}
	},

	getGridListURL:function() {
		return "workflowEdit!list.action";
	}

});
