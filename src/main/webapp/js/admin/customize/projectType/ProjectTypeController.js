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


Ext.define("com.trackplus.admin.customize.projectType.ProjectTypeController",{
	extend: "Ext.app.ViewController",
	alias: "controller.projectType",
	mixins: {
		baseController: "com.trackplus.admin.TreeDetailController"
	},
	
	baseServerAction: "projectType",
	leafDetailByFormLoad: true,
	folderDetailByFormLoad: true,
	replaceToolbarOnTreeNodeSelect: false,
	
	reload: function(refreshParamsObject) {
		this.reloadTree(this.getView().tree, refreshParamsObject);
	},
	
	PROJECT_TYPE: 0,
	STATUS_ASSIGNMENT: 1,
	ISSUE_TYPE_ASSIGNMENT: 2,
	PRIORITY_ASSIGNMENT: 3,
	SEVERITY_ASSIGNMENT: 4,
	FIELD_CONFIGURATION: 5,
	SCREEN_ASSIGNMENT: 6,
	WORKFLOW_ASSIGNMENT: 7,
	CHILD_PROJECT_TYPE_ASSIGNMENT: 8,
	ROLE_ASSIGNMENT: 9,

	
	/********************Methods for projectType-associated entities*****************/

	/**
	 * Handler for selecting a node in the tree
	 */
	onTreeNodeSelect: function(rowModel, node, index, opts) {
		if (node) {
			this.getView().selectedNodeID = node.get("id");
			this.getView().selectedNode = node;
		}
		var selectedNodeClass = this.getSelectedNodeClass(node);
		if (this.getView().centerPanel) {
			this.getView().mainPanel.remove(this.getView().centerPanel, true);
		}
		this.getView().centerPanel = selectedNodeClass;
		this.getView().mainPanel.add(this.getView().centerPanel);
		this.actualizeToolbarOnTreeSelect(node);
	},
	
	/**
	 * Which actions to enable/disable depending on tree selection
	 */
	actualizeToolbarOnTreeSelect: function(selectedNode) {
		//if (this.getView().actionDelete) {
		var projectTypeSelected = false;
		if (selectedNode) {
			var configType = selectedNode.get("configType");
			//TODO do not set to default 0 if no value sent in Json
			if (CWHF.isNull(configType) || configType===0) {
				projectTypeSelected=true;
			}
		}
		if (this.getView().actionDelete) {
			this.getView().actionDelete.setDisabled(!projectTypeSelected);
		}
		if (this.getView().actionExport) {
			this.getView().actionExport.setDisabled(!projectTypeSelected);
		}
	},

	/**
	 * Implement this method only if the tree contains heterogeneous data (like project/project type assignments)
	 * The specific methods on tree node selection should be called in the scope of the specific implementation
	 * Return the scope of the node specific implementation
	 * Additionally the locale tree and centerPanel objects are borrowed by the specific implementations
	 * (we do not initialize own tree for each specific implementation.
	 * An initialized (not null) tree is needed for the selection methods in tree in the context of a specific implementation
	 * and the centerPanel for rendering the data corresponding to the selected node
	 */
	getSelectedNodeClass: function(node) {
		var configType = node.data["configType"];
		var id = node.data["id"];
		var branchRoot = node.data["branchRoot"];
		var projectTypeID = node.data["projectTypeID"];
		if (CWHF.isNull(configType)) {
			return this;
		}
		switch (configType) {
		case this.PROJECT_TYPE:
			return Ext.create("com.trackplus.admin.customize.projectType.ProjectTypeEdit",
				{callerScope:this,
				refreshParametersBeforeSubmit: {reloadTree:true},
	        	refreshParametersAfterSubmit: [{parameterName:"node", fieldNameFromResult:"node"}],
	        	refreshAfterSubmitHandler: this.reload,
				entityContext: {node:node, add:false}});
		case this.STATUS_ASSIGNMENT:
		case this.PRIORITY_ASSIGNMENT:
		case this.SEVERITY_ASSIGNMENT:
			var rootMessageKey = null;
			if (configType===this.STATUS_ASSIGNMENT) {
				rootMessageKey = "admin.customize.projectType.lbl.assignmentInfoStatusGeneral";
			} else {
				if (configType===this.PRIORITY_ASSIGNMENT) {
					rootMessageKey = "admin.customize.projectType.lbl.assignmentInfoPriorityGeneral";
				} else {
					rootMessageKey = "admin.customize.projectType.lbl.assignmentInfoSeverityGeneral";
				}
			}
			return Ext.create("com.trackplus.admin.TreeDetailAssignment",
				{rootID:id,	dynamicIcons:true, reloadGrids:true,
				baseServerAction:"projectTypeListAssignments", rootMessageKey:rootMessageKey});
		case this.ISSUE_TYPE_ASSIGNMENT:
			return Ext.create("com.trackplus.admin.SimpleAssignment",
				{assignmentType: this.ISSUE_TYPE_ASSIGNMENT,
				assignmentTypeParameterName: "configType",
				objectIDParamName: "projectTypeID",
				objectID: node.parentNode.get("id"),
				dynamicIcons:true, baseServerAction:"projectTypeSimpleAssignment"});
		case this.CHILD_PROJECT_TYPE_ASSIGNMENT:
			return Ext.create("com.trackplus.admin.SimpleAssignment",
				{assignmentType: this.CHILD_PROJECT_TYPE_ASSIGNMENT,
				assignmentTypeParameterName: "configType",
				objectIDParamName: "projectTypeID",
				objectID: node.parentNode.get("id"),
				baseServerAction:"projectTypeSimpleAssignment"});
		case this.ROLE_ASSIGNMENT:
			return Ext.create("com.trackplus.admin.SimpleAssignment",
				{assignmentType: this.ROLE_ASSIGNMENT,
				assignmentTypeParameterName: "configType",
				objectIDParamName: "projectTypeID",
				objectID: node.parentNode.get("id"),
				baseServerAction:"projectTypeSimpleAssignment"});
		case this.FIELD_CONFIGURATION:
			var branchRoot = node.get("branchRoot");
			return Ext.create("com.trackplus.admin.customize.treeConfig.FieldConfig", {rootID:branchRoot, projectOrProjectTypeID: projectTypeID});
		case this.SCREEN_ASSIGNMENT:
			var branchRoot = node.get("branchRoot");
			return Ext.create("com.trackplus.admin.customize.treeConfig.ScreenConfig", {rootID:branchRoot, projectOrProjectTypeID: projectTypeID});
		case this.WORKFLOW_ASSIGNMENT:
			var branchRoot = node.get("branchRoot");
			return Ext.create("com.trackplus.admin.customize.treeConfig.WorkflowConfig",
						{rootID:branchRoot, projectOrProjectTypeID:projectTypeID});
		}
	},

	/**
	 * Prepare the form for adding a new projectType
	 */
	onAdd: function() {
		this.getView().tree.getSelectionModel().deselectAll(true);
		if (this.getView().centerPanel) {
			this.getView().mainPanel.remove(this.getView().centerPanel, true);
		}
		this.getView().centerPanel = Ext.create("com.trackplus.admin.customize.projectType.ProjectTypeEdit",
				{callerScope:this,
				refreshParametersBeforeSubmit: {reloadTree:true},
				refreshParametersAfterSubmit: [{parameterName:"node", fieldNameFromResult:"node"}],
				refreshAfterSubmitHandler: this.reload, entityContext: {add:true}});
		this.getView().mainPanel.add(this.getView().centerPanel);
	},

	onSave: function(button, event) {
		this.submitHandler(button, "projectType!save.action");	
	},
	
	onDelete: function() {
		var selectedRecords = this.getView().getSelectedRecords(true);
		if (selectedRecords) {
			var extraConfig = {fromTree:true, isLeaf:false};
			this.deleteHandler(this.getView().getEntityLabel(), selectedRecords, extraConfig);
		}
	},
	
	getReloadParamsAfterDelete: function(selectedRecords, extraConfig, responseJson) {
		return {reloadTree:true}
	},

	onImport: function(button) {
		var windowParameters = {
        	callerScope:this,
        	windowTitle: button.tooltip,
        	refreshParametersBeforeSubmit: {reloadTree:true},
        	refreshAfterSubmitHandler: this.reload,
        };
        var windowConfig = Ext.create("com.trackplus.admin.customize.projectType.ProjectTypeImport", windowParameters);
        windowConfig.showWindowByConfig();
	},

	onExport: function(button) {
		var selectedRecords = this.getView().getSelectedRecords(true);
		if (selectedRecords) {
			var windowParameters = {
		       	callerScope:this,
		       	windowTitle: button.tooltip,
		       	entityContext: {
		       		node: selectedRecords
		       		}
		        };
			var windowConfig = Ext.create("com.trackplus.admin.customize.projectType.ProjectTypeExport", windowParameters);
			windowConfig.showWindowByConfig();	
		}
	}
});
