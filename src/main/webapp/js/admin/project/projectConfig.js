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


Ext.define("com.trackplus.admin.project.ProjectConfig", {
	extend: "com.trackplus.admin.TreeDetail",
	xtype: "projectConfig",
    controller: "projectConfig",
	config : {
	    // the project tree from the admin navigation menu
	    //projectTree: null,
	    rootID: null,
	    mainRelease: null,
		childRelease: null,
		showClosedReleases: null,
	    //sys: true,
	    lastSelections: {},
	    hasPrivateProject: true,
	    isTemplate: false,
	    templateIsActive: false
	},
	baseServerAction : "project",
	treeStoreUrl: "project!expand.action",
	tabPanel: null,
	// actions
	actionAddProject : null,
	actionAddSubproject : null,
	actionAddPrivateProject : null,
	actionCopyProject : null,
	actionCreateWorkspaceFromTemplate : null,
	actionCreateTemplateFromWorkspace : null,
	actionLockUnlock : null,
	actionSave : null,
	actionCancel : null,
	actionDelete : null,

	/**
	 * Gets the tree's fields: all fields for all possible
	 * config types
	 */
	getTreeFields : function() {
	    return [{name : 'id', mapping : 'id', type : 'int'},
	            {name : 'text', mapping : 'text', type : 'string'},
	            {name : 'leaf', mapping : 'leaf', type : 'boolean'},
	            {name : 'iconCls', mapping : 'iconCls', type : 'string'},
	            {name : 'projectID', mapping : 'projectID', type : 'int'},
	            {name : 'privateProject', mapping : 'privateProject', type : 'boolean'},
	            {name : 'branchRoot', mapping : 'branchRoot', type : 'string'}];
	},

	/**
	 * ****************Methods strictly for project CRUD********************
	 */

	/**
	 * The localized entity name
	 */
	getEntityLabel : function(extraConfig) {
	    if (this.getIsTemplate()) {
	        return getText('admin.project.lbl.templateForOp');
	    } else {
	        return getText('admin.project.lbl.projectForOp');
	    }
	},

	/**
	 * The iconCls for the add button, overwrites base class
	 * icon
	 */
	getAddIconCls : function() {
	    return 'projecttAdd';
	},
	/**
	 * The iconCls for the edit button, overwrites base class
	 * icon
	 */
	getSaveIconCls : function() {
	    return 'projecttSave';
	},

	getAddSubprojectLabel : function() {
	    return getText(this.getAddTitleKey(), getText("admin.project.lbl.subproject"));
	},

	getAddPrivateProjectLabel : function() {
	    return getText(this.getAddTitleKey(), getText("admin.project.lbl.privateProject"));
	},

	getCopyToButtonKey : function() {
	    return "common.btn.copyTo";
	},

	getTemplateLockUnlockLabel : function() {
	    var label = getText('admin.project.lbl.templateStateEdit');
	    if (this.getTemplateIsActive()) {
	        label = getText('admin.project.lbl.templateStateLock');
	    }
	    return label;
	},

	getTemplateLockUnlockToolTip : function() {
	    var label = getText('admin.project.lbl.templateStateEdit.tt');
	    if (this.getTemplateIsActive()) {
	        label = getText('admin.project.lbl.templateStateLock.tt');
	    }
	    return label;
	},

	getTemplateLockUnlockIcon : function() {
	    var icon = "edit16";
	    if (this.getTemplateIsActive()) {
	        icon = "lock";
	    }
	    return icon;
	},
	
	initActions : function() {
		this.actions = [];
		if (com.trackplus.TrackplusConfig.user.sys) {
			//only the sys admin or manager can create new main projects
			var addProjectLabel = this.getActionTooltip(this.getAddTitleKey());
			 this.actionAddProject = CWHF.createAction(addProjectLabel, this.getAddIconCls(),
				"onAddProject", {tooltip:addProjectLabel, labelIsLocalized:true});
			 this.actions.push(this.actionAddProject);
		}
		if (com.trackplus.TrackplusConfig.appType!==APPTYPE_BUGS) {
			//in Genji no hierarchical projects
			var addSubprojectLabel = this.getAddSubprojectLabel();
			this.actionAddSubproject = CWHF.createAction(addSubprojectLabel, this.getAddIconCls(),
				"onAddSubproject", {tooltip:addSubprojectLabel, labelIsLocalized:true});
			 this.actions.push(this.actionAddSubproject);
		}
		if (this.getHasPrivateProject()) {
			//only if private project does not exists yet
			this.actionAddPrivateProject = CWHF.createAction(this.getAddPrivateProjectLabel(), this.getAddIconCls(),
				"onAddPrivateProject", {tooltip:getText("admin.project.lbl.privateProject.tt"), labelIsLocalized:true});
			this.actions.push(this.actionAddPrivateProject);
		}
		this.actionCopyProject = CWHF.createAction(this.getCopyButtonKey(), this.getCopyIconCls(),
				"onCopy", {tooltip:this.getActionTooltip(this.getCopyTitleKey())});
		this.actions.push(this.actionCopyProject);
	
		if (this.getIsTemplate()) {
			//we are at templates: create space from template
			var spaceFromTemplateLabel =  getText("admin.project.lbl.spaceFromTemplate");
			this.actionCreateWorkspaceFromTemplate = CWHF.createAction(spaceFromTemplateLabel, this.getCopyIconCls(),
					"onCreateFromCopy", {tooltip:spaceFromTemplateLabel, labelIsLocalized:true});
			this.actions.push(this.actionCreateWorkspaceFromTemplate);
			this.actionLockUnlock = CWHF.createAction(this.getTemplateLockUnlockLabel(), this.getTemplateLockUnlockIcon(),
					"onLockUnlock", {tooltip:this.getTemplateLockUnlockToolTip(), labelIsLocalized:true});
			this.actions.push(this.actionLockUnlock);
		} else {
			//we are at spaces: create template from space
			var templateFromSpace =  getText("admin.project.lbl.templateFromSpace");
			this.actionCreateTemplateFromWorkspace = CWHF.createAction(templateFromSpace, this.getCopyIconCls(),
				"onCreateFromCopy", {tooltip:templateFromSpace, labelIsLocalized:true});
			this.actions.push(this.actionCreateTemplateFromWorkspace);
		}
	    this.actionDelete = CWHF.createAction(this.getDeleteButtonKey(), this.getDeleteIconCls(),
	    		"onDelete", {tooltip:this.getActionTooltip(this.getDeleteTitleKey())});
	    this.actions.push(this.actionDelete);
	},

	/**
	 * Handler after loading the node: select the general node
	 * after loading the root's children
	 */
	onTreeNodeLoad : function(treeStore, node) {
	    // select the first child (general settings)
	    if (node.isRoot()) {
	        var selectedNode = null;
	        if (this.getLastSelections().lastSelectedSection) {
	            selectedNode = treeStore.getNodeById(this.getLastSelections().lastSelectedSection);
	        }
	        if (CWHF.isNull(selectedNode)) {
	            selectedNode = node.firstChild;
	        }
	        var treeSelectionModel = this.tree.getSelectionModel();
	        treeSelectionModel.select(selectedNode);
	    }
	}

});
