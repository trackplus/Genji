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
Ext.define("com.trackplus.admin.customize.treeConfig.ScreenConfig", {
	extend:"com.trackplus.admin.customize.treeConfig.AssignmentConfig",
	xtype: "screenConfig",
    controller: "screenConfig",
	treeWidth: 250,
	
	config: {
		rootID: "screen"
	},
	
	baseServerAction: "screenConfigItemDetail",
	
	initActions: function() {
		this.callParent();
		var sysAdmin=com.trackplus.TrackplusConfig.user.sys;
		this.actionImport.setDisabled(!sysAdmin);
		this.actions = [this.actionApply, this.actionReset, {xtype: 'tbspacer', width: 45, disabled:true},
					this.actionEdit, this.actionDesign, this.actionImport, this.actionExport];
	},

	/**
	 * Get the actions available in context menu depending on the currently selected row
	 */
	getTreeContextMenuActions: function(selectedRecord, selectionIsSimple) {
		var actions = [];
		if (selectionIsSimple) {
			var inheritedConfig = selectedRecord.data['inheritedConfig'];
			var defaultConfig = selectedRecord.data['defaultConfig'];
			var leaf = selectedRecord.isLeaf();
			var selectedTreeNode = this.getSingleSelectedRecord(true);
			if (!inheritedConfig && !defaultConfig&&selectedTreeNode && this.isAssignable(selectedTreeNode)) {
				actions.push(this.actionReset);
			}
			if (!leaf) {
				actions.push(this.actionReload);
			}
		}
		return actions;
	},
	
	getGridContextMenuActions: function(selectedRecord, selectionIsSimple) {
		var sysAdmin=com.trackplus.TrackplusConfig.user.sys;
		if (selectionIsSimple) {
			var selectedTreeNode = this.getSingleSelectedRecord(true);
			if (selectedTreeNode && selectedTreeNode.isLeaf()) {
				if (sysAdmin) {
					var actions = [this.actionApply, this.actionEdit, this.actionDesign];
					return actions;
				}else{
					return [this.actionApply];
				}
			} else {
				if(sysAdmin) {
					var actions =  [this.actionEdit, this.actionDesign];
					return actions;
				}
				return [];
			}
		} else {
			if (sysAdmin) {
				var actions = [];
				return actions;

			} else {
				return [];
			}
		}
	},

	/**
	 * The message to appear first time after selecting this menu entry
	 * Is should be shown by selecting the root but the root is typically not visible
	 */
	getRootMessage: function(rootID) {
		return getText('admin.customize.form.config.lbl.description');
	},

	/**
	 * The localized leaf name
	 */
	getEntityLabel: function() {
		return getText('admin.customize.form.config.lbl.formAssignment');
	},


	getEntityConfigLabelSingular: function() {
		return getText("admin.customize.form.config.lbl.form");
	},

	getEntityConfigLabelPlural: function() {
		return getText("admin.customize.form.config.lbl.forms");
	},

	/**
	 * Which actions to enable/disable depending on tree selection
	 */
	getToolbarActionChangesForTreeNodeSelect: function(node) {
		if (node ) {
			var inheritedConfig=node.data['inheritedConfig'];
			var defaultConfig=node.data['defaultConfig'];
			this.actionReset.setDisabled(inheritedConfig || defaultConfig);
			this.actionApply.setDisabled(!this.isAssignable(node));
		}
	},

	isAssignable: function(node) {
		return node.isLeaf();
	},

	getGridListURL: function() {
		return "indexScreens.action";
	}
});
