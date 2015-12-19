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
Ext.define("com.trackplus.admin.customize.treeConfig.MailTemplateConfig",{
	extend:"com.trackplus.admin.customize.treeConfig.AssignmentConfig",
	xtype: "mailTemplateConfig",
    controller: "mailTemplateConfig",
	treeWidth: 250,
	
	config: {
		rootID: "mailTemplate"
	},
	
	baseServerAction: "mailTemplateConfigItemDetail",
	
	getGridFields: function() {
		return [
			{name: 'id',type:'int'},
			{name: 'name',type:'string'},
			{name: 'tagLabel',type:'string'},
			{name: 'description', type: 'string'}
		];
	},

	getGridColumns: function() {
		return [{text: getText('common.lbl.name'), flex:1,
			dataIndex: 'name',id:'name',sortable:true,
			filter: {
	            type: "string"
	        }},
			{text: getText('common.lbl.tagLabel'), flex:1,
				dataIndex: 'tagLabel',id:'tagLabel',sortable:true,
				filter: {
		            type: "string"
		        }},
			{text: getText('common.lbl.description'),flex:1,
				dataIndex: 'description',id:'description',sortable:true,
				filter: {
		            type: "string"
		        }}
		];
	},

	/**
	 * The message to appear first time after selecting this menu entry
	 * Is should be shown by selecting the root but the root is typically not visible
	 */
	getRootMessage: function(rootID) {
		return getText('admin.customize.mailTemplate.config.lbl.description');
	},

	/**
	 * The localized leaf name
	 */
	getEntityLabel: function() {
		return getText('admin.customize.mailTemplate.config.lbl.mailTemplateAssignment');
	},


	getEntityConfigLabelSingular: function() {
		return getText("admin.customize.mailTemplate.config.lbl.mailTemplate");
	},

	getEntityConfigLabelPlural: function() {
		return getText("admin.customize.mailTemplate.config.lbl.mailTemplates");
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

	/**
	 * Get the actions available in context menu depending on the currently selected row
	 */
	getTreeContextMenuActions: function(selectedRecord, selectionIsSimple) {
		var actions = [];
		if (selectionIsSimple) {
			var inheritedConfig = selectedRecord.data['inheritedConfig'];
			var defaultConfig = selectedRecord.data['defaultConfig'];
			var leaf = selectedRecord.isLeaf();
			if (!inheritedConfig && !defaultConfig) {
				actions.push(this.actionReset);
			}
			if (!leaf) {
				actions.push(this.actionReload);
			}
		}
		return actions;
	},

	getGridListURL: function() {
		return "mailTemplate.action";
	}
});
