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


Ext.define('com.trackplus.admin.project.ProjectConfig', {
	extend : 'com.trackplus.admin.TreeDetail',
	config : {
	    // the project tree from the admin navigation menu
	    projectTree : null,
	    rootID : null,
	    sys : true,
	    lastSelections : {},
	    isTemplate : false
	},
	hasPrivateProject : true,
	templateIsActive : false,
	baseAction : 'project',
	tabPanel: null,
	/**
	 * Ask for extra confirmation if the entity to be deleted
	 * has dependencies
	 */
	confirmDeleteNotEmpty : true,
	// subproject
	addAsSubproject : false,
	// by adding a project whether to add a private project
	addAsPrivateProject : false,
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
	// force getToolbarActionsForTreeNodeSelect() because:
	// getToolbarActions() is called when selecting a project
	// and getToolbarActionsForTreeNodeSelect() is called after
	// selecting the fist project node (general)
	replaceToolbarOnTreeNodeSelect : true,
	roleAssignment : null,
	accountAssignment : null,
	phases : null,
	lists : null,
	filters : null,
	reportTemplates : null,
	screenConfig : null,
	fieldConfig : null,
	workflowConfig : null,
	automail : null,
	cockpit : null,
	versionControl : null,
	importFromMsProject : null,
	exportToMsProject : null,

	labelWidth : 200,
	textFieldWidth : 200 + 300,
	textFieldWidthShort : 200 + 150,
	textFieldWidthCheck : 200 + 80,
	FieldSetWidth : 250 + 300 + 150,
	alignR : "right",
	prefillByWidth : 200 + 350,
	protocolWidth : 200 + 350,
	securityConnectionWidth : 200 + 350,

	COPY_ACTION_WP_FROM_TPL : 0,
	COPY_ACTION_TPL_FROM_WP : 1,
	COPY_ACTION_COPY_TPL : 2,
	COPY_ACTION_COPY_WP : 3,

	constructor : function(config) {
	    var config = config || {};
	    this.initConfig(config);
	    // the actions are initialized asynchronously after
		// getting the localized labels:
	    // initialize the lists and phases directly to be sure
		// that the localized labels are available
	    // when actions are initialized on treeNodeSelect
	    this.lists = Ext.create('com.trackplus.admin.customize.list.ListConfig', {
	        fromProjectConfig : true
	    });
	    this.phases = Ext.create('com.trackplus.admin.project.Release', {
	        projectID : this.getRootID()
	    });
	    this.initBase(config);
	},

	GENERAL : 1,
	ASSIGN_ROLES : 2,
	PHASES : 3,
	LISTS : 4,
	FILTERS : 5,
	REPORT_TEMPLTATES : 6,
	SCREEN_ASSIGNMENT : 7,
	FIELD_CONFIGURATION : 8,
	ASSIGN_ACCOUNTS : 9,
	AUTOMAIL : 10,
	COCKPIT : 11,
	VERSION_CONTROL : 12,
	WORKFLOW : 13,
	EXPORT_TO_MSPROJECT : 14,
	IMPORT_FROM_MSPROJECT : 15,

	EMAIL_PROTOCOL : {
	    POP3 : 'pop3',
	    IMAP : 'imap'
	},

	SECURITY_CONNECTIONS_MODES : {
	    NEVER : 0,
	    TLS_IF_AVAILABLE : 1,
	    TLS : 2,
	    SSL : 3
	},

	DEFAULT_EMAIL_PORTS : {
	    POP3 : 110,
	    POP3_SSL : 995,
	    IMAP : 143,
	    IMAP_SSL : 993
	},

	PREFILL : {
	    LASTWORKITEM : 1,
	    PROJECTDEFAULT : 2
	},

	addOrEditProject:function(loadParams, submitParams, title, refreshScope, refreshMethod){
		var width = 800;
		var height = 650;
		var loadUrl = this.getDetailUrl();
		var load = {loadUrl:loadUrl, loadUrlParams:loadParams};
		var submitUrl = this.getBaseAction() + "!save.action";
		var submit = {
			submitUrl:submitUrl,
			submitUrlParams:submitParams,
			refreshParametersBeforeSubmit : {
	            refreshScope : refreshScope,
	            refreshMethod : refreshMethod
	        },
	        refreshAfterSubmitHandler : function(refreshParameters, result) {
	            refreshMethod.call(refreshScope);
	        }
		};
		var windowParameters = {title:title,
			width: width,
			height: height,
			load: load,
			submit: submit,
			items: this.getDetailItems(null, loadParams.add),
			postDataProcess: this.postDataLoadCombos
		};
		var windowConfig = Ext.create("com.trackplus.util.WindowConfig", windowParameters);
		windowConfig.showWindowByConfig(this);
	},

	/**
	 * ******************Methods for project and all
	 * associated entities****************
	 */

	/**
	 * Handler for selecting a node in the tree
	 */
	treeNodeSelect : function(rowModel, node, index, opts) {
		var leaf = false;
	    var privateProject = false;
	    if (node ) {
	        // called for the select event of the tree
	        leaf = node.data['leaf'];
	        this.selectedNodeID = node.data['id'];
	        this.storeLastSelectedSection(this.selectedNodeID);
	        this.selectedNode = node;
	        privateProject = node.data['privateProject'];
	    }

	    var selectedNodeClass = this.getSelectedNodeClass(node);
	    if (this.getSelectedNodeIsTreeBased(node)) {
	        node = this.loadTreeWithGrid(selectedNodeClass, node);
	        // call the actualizeToolbarOnTreeSelect as the
			// rootNode of the new tree would be selected
	        this.actualizeToolbarOnTreeSelect.call(selectedNodeClass, node);
	    } else {
	        if (this.getSelectedNodeIsWizardBased(node) || this.getSelectedNodeIsDetailBased(node)) {
	            if (this.centerPanel ) {
	                this.mainPanel.remove(this.centerPanel, true);
	            }
	            if (this.getSelectedNodeIsWizardBased(node)) {
	                this.centerPanel = selectedNodeClass.getWizardPanel();
	            } else {
	                this.centerPanel = selectedNodeClass.getDetailPanel(node, leaf, opts);
	            }
	            this.mainPanel.add(this.centerPanel);
	            borderLayout.setActiveToolbarActionList(selectedNodeClass.getToolbarActions());
	        } else {
	            // project general
	            selectedNodeClass.loadDetailPanel(node, leaf, opts);
	            // false because at least one project exists
	            borderLayout.setActiveToolbarActionList(selectedNodeClass.getToolbarActions(true,
	                    privateProject));
	        }
	    }


	    // toolbar content
	    // call the actualizeToolbarOnTreeSelect of the main
		// base class: "this."
	    // (important by heterogeneous trees where
		// actualizeToolbarOnTreeSelect is overridden to
	    // completly replace the toolbar at any tree node
		// select)
	    // but the toolbar methods in the overridden method are
		// called according to the actual scope
	},

	loadTreeWithGrid : function(treeWithGridConfig, node) {
	    var branchRoot = null;
	    if (node ) {
	        branchRoot = node.data['branchRoot'];
	    }
	    if (this.centerPanel ) {
	        this.mainPanel.remove(this.centerPanel, true);
	    }
	    this.centerPanel = treeWithGridConfig.createCenterPanel();
	    this.mainPanel.add(this.centerPanel);
	    var rootNode = treeWithGridConfig.tree.getRootNode();
	    treeWithGridConfig.selectedNode = rootNode;
	    treeWithGridConfig.selectedNodeID = branchRoot;
	    borderLayout.setActiveToolbarActionList(treeWithGridConfig.getToolbarActions());
	    return rootNode;
	},

	/**
	 * Whether the node specific class is a treeWithGrid
	 * implementation
	 */
	getSelectedNodeIsTreeBased : function(node) {
	    var projectConfigType = node.data['id'];
	    switch (projectConfigType) {
	    case this.PHASES:
	    case this.ASSIGN_ROLES:
	    case this.LISTS:
	    case this.FILTERS:
	    case this.REPORT_TEMPLTATES:
	    case this.FIELD_CONFIGURATION:
	    case this.SCREEN_ASSIGNMENT:
	    case this.WORKFLOW:
	    case this.AUTOMAIL:
	        return true;
	    }
	    return false;
	},

	/**
	 * Whether the node specific class is a wizard
	 * implementation
	 */
	getSelectedNodeIsWizardBased : function(node) {
	    var projectConfigType = node.data['id'];
	    return projectConfigType === this.IMPORT_FROM_MSPROJECT;

	},

	/**
	 * Whether the node specific class is a wizard
	 * implementation
	 */
	getSelectedNodeIsDetailBased : function(node) {
	    var projectConfigType = node.data['id'];
	    if (projectConfigType === this.EXPORT_TO_MSPROJECT || projectConfigType === this.VERSION_CONTROL
	            || projectConfigType === this.COCKPIT || projectConfigType === this.ASSIGN_ACCOUNTS) {
	        return true;
	    }
	    return false;
	},

	/**
	 * Implement this method only if the tree contains
	 * heterogeneous data (like project/project type
	 * assignments) The specific methods on tree node selection
	 * should be called in the scope of the specific
	 * implementation Return the scope of the node specific
	 * implementation Additionally the locale tree and
	 * centerPanel objects are borrowed by the specific
	 * implementations (we do not initialize own tree for each
	 * specific implementation. An initialized (not null) tree
	 * is needed for the selection methods in tree in the
	 * context of a specific implementation and the centerPanel
	 * for rendering the data corresponding to the selected node
	 */
	getSelectedNodeClass : function(node) {
	    var projectConfigType = node.data['id'];
	    var branchRoot = node.data['branchRoot'];
	    var projectID = node.data['projectID'];
	    switch (projectConfigType) {
	    case this.GENERAL:
	        return this;
	    case this.PHASES:
	        this.phases.rootID = branchRoot;
	        return this.phases;
	    case this.ASSIGN_ROLES:
	        if (CWHF.isNull(this.roleAssignment)) {
	            this.roleAssignment = Ext.create("com.trackplus.admin.project.RoleAssignment", {
	                rootID : branchRoot,
	                treeWidth : 200
	            });
	        }
	        return this.roleAssignment;
	    case this.ASSIGN_ACCOUNTS:
	        if (CWHF.isNull(this.accountAssignment)) {
	            this.accountAssignment = Ext.create("com.trackplus.admin.SimpleAssignment", {
	                objectID : this.getRootID(),
	                objectIDParamName : "projectID"
	            });
	            this.accountAssignment.baseAction = "accountAssignments";
	        }
	        return this.accountAssignment;
	    case this.LISTS:
	        this.lists.setRootID(branchRoot);
	        return this.lists;
	    case this.FILTERS:
	        if (CWHF.isNull(this.filters)) {
	            this.filters = Ext.create('com.trackplus.admin.customize.category.CategoryConfig', {
	                rootID : branchRoot,
	                projectID : this.getRootID()
	            });
	        }
	        return this.filters;
	    case this.REPORT_TEMPLTATES:
	        if (CWHF.isNull(this.reportTemplates)) {
	            var branchRoot = node.data['branchRoot'];
	            this.reportTemplates = Ext.create(
	                    'com.trackplus.admin.customize.category.CategoryConfig', {
	                        rootID : branchRoot,
	                        projectID : this.getRootID()
	                    });
	        }
	        return this.reportTemplates;
	    case this.FIELD_CONFIGURATION:
	        if (CWHF.isNull(this.fieldConfig)) {
	            var branchRoot = node.data['branchRoot'];
	            this.fieldConfig = Ext.create('com.trackplus.admin.customize.treeConfig.FieldConfig', {
	                rootID : branchRoot,
	                fromProjectConfig : true
	            });
	        }
	        return this.fieldConfig;
	    case this.SCREEN_ASSIGNMENT:
	        if (CWHF.isNull(this.screenConfig)) {
	            var branchRoot = node.data['branchRoot'];
	            this.screenConfig = Ext.create('com.trackplus.admin.customize.treeConfig.ScreenConfig',
	                    {
	                        rootID : branchRoot,
	                        fromProjectConfig : true
	                    });
	        }
	        return this.screenConfig;
	    case this.AUTOMAIL:
	        if (CWHF.isNull(this.automail)) {
	            this.automail = Ext.create('com.trackplus.admin.NotifyConfig', {
	                defaultSettings : true,
	                exclusiveProjectID : this.getRootID()
	            });
	        }
	        return this.automail;
	    case this.COCKPIT:
	        if (CWHF.isNull(this.cockpit)) {
	            this.cockpit = Ext.create('com.trackplus.admin.project.ProjectCockpit', {
	                projectID : this.getRootID()
	            });
	        }
	        return this.cockpit;
	    case this.VERSION_CONTROL:
	        if (CWHF.isNull(this.versionControl)) {
	            this.versionControl = Ext.create('com.trackplus.vc.VersionControlFacade', {
	            	versionControlFacadeModel: {
	                    projectID : this.getRootID()
	                }
	            });
	        }
	        return this.versionControl;
	    case this.WORKFLOW:
	        if (CWHF.isNull(this.workflowConfig)) {
	            var branchRoot = node.data['branchRoot'];
	            this.workflowConfig = Ext.create(
	                    'com.trackplus.admin.customize.treeConfig.WorkflowConfig', {
	                        rootID : branchRoot,
	                        fromProjectConfig : true,
	                        projectOrProjectTypeID : projectID
	                    });
	        }
	        return this.workflowConfig;
	    case this.EXPORT_TO_MSPROJECT:
	        if (CWHF.isNull(this.exportToMsProject)) {
	            this.exportToMsProject = Ext.create('com.trackplus.admin.action.ExportMsProject', {
	                projectID : this.getRootID()
	            });
	        }
	        return this.exportToMsProject;
	    case this.IMPORT_FROM_MSPROJECT:
	        if (CWHF.isNull(this.importFromMsProject)) {
	            this.importFromMsProject = Ext.create('com.trackplus.admin.action.ImportMsProject', {
	                projectID : this.getRootID()
	            });
	        }
	        return this.importFromMsProject;
	    }
	},

	/**
	 * Gets the tree's fields: all fields for all possible
	 * config types
	 */
	getTreeFields : function() {
	    return [ {
	        name : 'id',
	        mapping : 'id',
	        type : 'int'
	    }, {
	        name : 'text',
	        mapping : 'text',
	        type : 'string'
	    }, {
	        name : 'leaf',
	        mapping : 'leaf',
	        type : 'boolean'
	    }, {
	        name : 'iconCls',
	        mapping : 'iconCls',
	        type : 'string'
	    }, {
	        name : 'projectID',
	        mapping : 'projectID',
	        type : 'int'
	    }, {
	        name : 'privateProject',
	        mapping : 'privateProject',
	        type : 'boolean'
	    }, {
	        name : 'branchRoot',
	        mapping : 'branchRoot',
	        type : 'string'
	    } ];
	},

	/**
	 * ****************Methods strictly for project
	 * CRUD********************
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

	/**
	 * Initialization method
	 */
	initBase: function(config) {
	    Ext.Ajax.request({
	        url : this.getBaseAction() + '!getWorkspaceAndTemplateToolbarConfig.action',
	        params : {
	            projectID : this.getRootID()
	        },
	        fromCenterPanel : true,
	        scope : this,
	        success : function(response) {
	            var result = Ext.decode(response.responseText);
	            this.hasPrivateProject = result.hasPrivateWorkspace;
	            this.templateIsActive = result.templateIsActive;
	            this.initActions();
	        },
	        failure : function(response) {
	            Ext.MessageBox.alert(this.failureTitle, response.responseText);
	        }
	    });
	},

	initActions : function() {
	    this.actionAddProject = this.createLocalizedAction(this.getTitle(this.getAddTitleKey()), this
	            .getAddIconCls(), this.onAddProject, this.getTitle(this.getAddTitleKey()));
	    this.actionAddSubproject = this.createLocalizedAction(this.getAddSubprojectLabel(), this
	            .getAddIconCls(), this.onAddSubproject, this.getAddSubprojectLabel());
	    this.actionAddPrivateProject = this.createLocalizedAction(this.getAddPrivateProjectLabel(),
	            this.getAddIconCls(), this.onAddPrivateProject,
	            getText("admin.project.lbl.privateProject.tt"));
	    this.actionCopyProject = this.createLocalizedAction(getText(this.getCopyButtonKey()), this
	            .getCopyIconCls(), this.onCopy, this.getTitle(this.getCopyTitleKey()));
	    this.actionCreateWorkspaceFromTemplate = this.createLocalizedAction(
	            getText('admin.project.lbl.spaceFromTemplate'), this.getCopyIconCls(),
	            this.onCreateFromCopy, getText('admin.project.lbl.spaceFromTemplate'));
	    this.actionCreateTemplateFromWorkspace = this.createLocalizedAction(
	            getText('admin.project.lbl.templateFromSpace'), this.getCopyIconCls(),
	            this.onCreateFromCopy, getText('admin.project.lbl.templateFromSpace'));
	    this.actionLockUnlock = this.createLocalizedAction(this.getTemplateLockUnlockLabel(), this
	            .getTemplateLockUnlockIcon(), this.onLockUnlock, this.getTemplateLockUnlockToolTip());
	    this.actionSave = this.createLocalizedAction(getText('common.btn.save'), this.getSaveIconCls(),
	            this.onSave, this.getTitle('common.lbl.save'));
	    this.actionCancel = this.createLocalizedAction(getText('common.btn.cancel'), 'cancel',
	            this.onCancel, getText('common.btn.cancel'));
	    this.actionDelete = this.createLocalizedAction(getText('common.btn.delete'), this
	            .getDeleteIconCls(), this.onDelete, this.getTitle('common.lbl.delete'));
	},

	/**
	 * Initialize all actions and return the toolbar actions
	 * noProject no project exists for this user privateProject
	 * the selected project is private project
	 */
	getToolbarActions : function(projectIsSelected, privateProjectIsSelected) {
	    var actions = [];
	    if (this.getSys()) {
	        actions.push(this.actionAddProject);
	    }
	    if (com.trackplus.TrackplusConfig.appType!==APPTYPE_BUGS && projectIsSelected && privateProjectIsSelected && !privateProjectIsSelected) {
	        actions.push(this.actionAddSubproject);
	    }
	    if (!this.hasPrivateProject && com.trackplus.TrackplusConfig.user.privateWorkspace
	            && !this.getIsTemplate()) {
	        actions.push(this.actionAddPrivateProject);
	    }
	    if (projectIsSelected && privateProjectIsSelected  && !privateProjectIsSelected) {
	        actions.push(this.actionCopyProject);
	    }
	    if (projectIsSelected) {
	        if (this.getIsTemplate()) {
	            actions.push(this.actionCreateWorkspaceFromTemplate);
	            actions.push(this.actionLockUnlock);
	        } else {
	            actions.push(this.actionCreateTemplateFromWorkspace);
	        }
	    }
	    if (projectIsSelected) {
	        actions.push(this.actionSave);
	        actions.push(this.actionDelete);
	    }
	    return actions;
	},

	/**
	 * Handler after loading the node: select the general node
	 * after loading the root's children
	 */
	onTreeNodeLoad : function(treeStore, node) {
	    var me = this;
	    // select the first child (general settings)
	    if (node.isRoot()) {
	        var selectedNode = null;
	        if (me.getLastSelections().lastSelectedSection ) {
	            selectedNode = treeStore.getNodeById(me.getLastSelections().lastSelectedSection);
	        }
	        if (CWHF.isNull(selectedNode)) {
	            selectedNode = node.firstChild;
	        }
	        var treeSelectionModel = this.tree.getSelectionModel();
	        treeSelectionModel.select(selectedNode);
	    }
	},

	/**
	 * Prepare the form for adding a new project
	 */
	onAddProject : function() {
	    this.onAdd(false, false);
	},

	/**
	 * Prepare the form for adding a new project
	 */
	onAddSubproject : function() {
	    this.onAdd(true, false);
	},

	onAddPrivateProject : function() {
	    this.onAdd(false, true);
	},

	onAdd : function(addAsSubproject, addAsPrivateProject) {
	    this.addAsSubproject = addAsSubproject;
	    this.addAsPrivateProject = addAsPrivateProject;
	    this.mainPanel = Ext.create('Ext.panel.Panel', {
	        layout : 'border',
	        //region : 'center',
	        border : false,
	        items : []
	    });
	    borderLayout.borderLayoutController.setCenterContent(this.mainPanel);
	    this.loadDetailPanelWithFormLoad(null, true, {
	        addAsSubproject : addAsSubproject,
	        addAsPrivateProject : addAsPrivateProject
	    });
	    borderLayout.setActiveToolbarActionList(this.getToolbarActionsForTreeNodeSelect());
	},

	onCopy : function() {
	    var projectCopyModel = {
	        projectID : this.getRootID()
	    };
	    var actionTarget = this.COPY_ACTION_COPY_WP;
	    if (this.getIsTemplate()) {
	        actionTarget = this.COPY_ACTION_COPY_TPL;
	    }
	    var projectCopyController = Ext.create('com.trackplus.admin.project.ProjectCopyController', {
	    	dataModel: projectCopyModel,
	        projectConfig: this,
	        isTemplate: this.getIsTemplate(),// copy template or
											// workspace
	        applyTemplate : false,
	        actionTarget : actionTarget
	    });
	    projectCopyController.showDialog();
	},

	onCreateFromCopy : function() {
	    var projectCopyModel = {
	        projectID : this.getRootID()
	    };

	    var actionTarget = this.COPY_ACTION_TPL_FROM_WP;
	    if (this.getIsTemplate()) {
	        actionTarget = this.COPY_ACTION_WP_FROM_TPL;
	    }
	    var projectCopyController = Ext.create('com.trackplus.admin.project.ProjectCopyController', {
	    	dataModel: projectCopyModel,
	        projectConfig: this,
	        isTemplate: false, // applying this template
	        applyTemplate: true,
	        actionTarget: actionTarget
	    });
	    projectCopyController.showDialog();
	},

	onLockUnlock : function() {
	    Ext.Ajax.request({
	        url : this.getBaseAction() + '!changeTemplateState.action',
	        params : {
	            projectID : this.getRootID()
	        },
	        scope : this,
	        success : function(response) {
	            var result = Ext.decode(response.responseText);
	            var data = result.data;
	            var success = result.success;
	            if (success) {
	                this.templateIsActive = data.templateIsActive;
	                this.actionLockUnlock.setText(this.getTemplateLockUnlockLabel());
	                this.actionLockUnlock.setIconCls(this.getTemplateLockUnlockIcon());
	            } else {
	                Ext.MessageBox.alert(this.failureTitle,
	                        getText('admin.project.lbl.changeTemplateStateError'));
	            }
	        },
	        failure : function(response) {
	            Ext.MessageBox.alert(this.failureTitle, response.responseText);
	        }
	    });
	},

	onCancel : function() {
	    var selectionModel = this.getProjectTree().getSelectionModel();
	    var lastSelected = selectionModel.getLastSelected();
	    if (lastSelected ) {
	        selectionModel.select(lastSelected);
	        com.trackplus.admin.myClick(this.getProjectTree(), lastSelected);
	    } else {
	        // cancel when no project is selected: no project
			// exists for the user
	        borderLayout.controller.setCenterContent(null);
	        borderLayout.setActiveToolbarActionList(this.getToolbarActions(false));
	    }
	},

	onSave : function(button, event) {
	    this.saveProject(false);
	},

	/**
	 * Save the detail part
	 */
	saveProject : function(confirmSave) {
	    var me = this;
	    borderLayout.setLoading(true);
	    this.clearErrorTabs();
	    var params = {
	        projectID : this.getRootID(),
	        addAsSubproject : this.addAsSubproject,
	        addAsPrivateProject : this.addAsPrivateProject,
	        isTemplate : this.getIsTemplate()
	    };
	    if (confirmSave ) {
	        params['confirmSave'] = confirmSave;
	    }
	    this.centerPanel.getForm().submit({
	        url : this.getBaseAction() + '!save.action',
	        params : params,
	        scope : this,
	        success : function(form, action) {
	            borderLayout.setLoading(false);
	            var result = action.result;
	            if (result.success) {
	                this.reload(result);
	                var projectNodeToSelect = result['projectNodeToSelect'];
	                this.setRootID(projectNodeToSelect);
	                CWHF.showMsgInfo(getText('admin.project.successSave'));
	            } else {
	                errorHandlerSave(result);
	            }
	        },
	        failure : function(form, action) {
	            borderLayout.setLoading(false);
	            result = action.result;
	            if (result ) {
	                if (action.result  && action.result.errors ) {
	                    me.handleErrors(action.result.errors);
	                } else {
	                    var errorCode = result.errorCode;
	                    var title = result.title;
	                    if (errorCode  && errorCode === 4) {
	                        // 4===NEED_CONFIRMATION
	                        var errorMessage = result.errorMessage;
	                        Ext.MessageBox.confirm(title, errorMessage, function(btn) {
		                        if (btn === "no") {
			                        return false;
		                        } else {
			                        this.saveProject(true);
		                        }
	                        }, this);
	                    } else {
	                        com.trackplus.util.submitFailureHandler(form, action);
	                    }
	                }
	            } else {
	                CWHF.showMsgError(getText('admin.project.err.errorSave'));
	            }
	        }
	    });
	},

	onDelete : function() {
	    var selectedRecord = this.getProjectTree().getSelectionModel().getLastSelected();
	    if (selectedRecord ) {
	        var extraConfig = {
	            fromTree : true,
	            isLeaf : false
	        };
	        this.deleteHandler(selectedRecord, extraConfig);
	    }
	},

	/**
	 * This method returns the proper flag for reloading: or the
	 * project tree (===true) or the template project tree
	 * (===false)
	 */
	reloadProjectTree : function(actionTarget) {
	    switch (actionTarget) {
	    case this.COPY_ACTION_COPY_WP:
	    case this.COPY_ACTION_WP_FROM_TPL:
	        return true;
	        break;
	    case this.COPY_ACTION_TPL_FROM_WP:
	    case this.COPY_ACTION_COPY_TPL:
	        return false;
	        break;
	    default:
	        return true;
	    }
	},

	/**
	 * This method handles reloading the proper tree item
	 * (project or template) based on actionTarget parameter:
	 * For ex: when creating a template from workspace: the
	 * workspace node must collapse, the template node must
	 * expand with selecting the newly created node.
	 */
	reloadAfterApplyingTemplate : function(refreshParamsObject, actionTarget) {
	    var templateTree = Ext.getCmp('tree-projectTemplateTreePanel');
	    var projectTree = Ext.getCmp('tree-projectTreePanel');
	    if (actionTarget === this.COPY_ACTION_WP_FROM_TPL) {
	        Ext.getCmp('projectTreePanel').expand();
	        templateTree.getSelectionModel().deselectAll();
	    }

	    if (actionTarget === this.COPY_ACTION_TPL_FROM_WP) {
	        Ext.getCmp('projectTemplateTreePanel').expand();
	        projectTree.getSelectionModel().deselectAll();
	    }
	    var reloadProjectTree = refreshParamsObject['reloadProjectTree'];
	    var projectNodeToSelect = refreshParamsObject['projectNodeToSelect'];
	    var projectNodeToReload = refreshParamsObject['projectNodeToReload'];
	    var deletedProjectID = refreshParamsObject['deletedProjectID'];
	    // tree reload is needed: after delete, add and edit
		// with label change
	    var options = new Object();
	    if (projectNodeToReload ) {
	        var nodeToReload = null;
	        if (this.reloadProjectTree(actionTarget)) {
	            nodeToReload = projectTree.getStore().getNodeById(projectNodeToReload);
	        } else {
	            nodeToReload = templateTree.getStore().getNodeById(projectNodeToReload);
	        }
	        // if null reload the branch i.e. the main projects
	        options.node = nodeToReload;
	    }
	    if (CWHF.isNull(projectNodeToSelect)) {
	        // after deleting a main project
	        var rootNode = null;
	        if (this.reloadProjectTree(actionTarget)) {
	            rootNode = projectTree.getRootNode();
	        } else {
	            rootNode = templateTree.getRootNode();
	        }
	        if (rootNode  && rootNode.childNodes  && rootNode.childNodes.length > 0) {
	            var firstChild = rootNode.getChildAt(0);
	            if (deletedProjectID  && firstChild.data['id'] === deletedProjectID) {
	                if (rootNode.childNodes.length > 1) {
	                    var secondChild = rootNode.getChildAt(1);
	                    projectNodeToSelect = secondChild.data['id'];
	                }
	            } else {
	                projectNodeToSelect = firstChild.data['id'];
	            }
	        }
	    }
	    options.callback = this.selectNode;
	    if (this.reloadProjectTree(actionTarget)) {
	        options.scope = {
	            tree : projectTree,
	            nodeIdToReload : projectNodeToReload,
	            nodeIdToSelect : projectNodeToSelect,
	            scope : this,
	            expandWorkspaceNode : true
	        };
	        projectTree.getStore().load(options);
	    } else {
	        options.scope = {
	            tree : templateTree,
	            nodeIdToReload : projectNodeToReload,
	            nodeIdToSelect : projectNodeToSelect,
	            scope : this,
	            expandWorkspaceNode : true
	        };
	        templateTree.getStore().load(options);
	    }
	},

	/**
	 * Reload after save or delete
	 */
	reload : function(refreshParamsObject) {
	    var reloadProjectTree = refreshParamsObject['reloadProjectTree'];
	    var projectNodeToSelect = refreshParamsObject['projectNodeToSelect'];
	    var projectNodeToReload = refreshParamsObject['projectNodeToReload'];
	    var reloadProjectConfigTree = refreshParamsObject['reloadProjectConfigTree'];
	    var projectConfigTypeNodeToSelect = refreshParamsObject['projectConfigTypeNodeToSelect'];
	    var deletedProjectID = refreshParamsObject['deletedProjectID'];
	    if (!reloadProjectTree && reloadProjectConfigTree && this.tree ) {

	        // this.tree is null by add
	        // only after save
	        var options = new Object();
	        if (projectConfigTypeNodeToSelect ) {
	            // callback after load
	            options.callback = this.selectNode;
	            // options.node = projectNodeToSelect;
	            // scope for callback
	            options.scope = {
	                tree : this.tree,
	                nodeIdToSelect : projectConfigTypeNodeToSelect
	            };
	        }
	        this.tree.getStore().load(options);
	    }
	    if (reloadProjectTree) {
	        // tree reload is needed: after delete, add and edit
			// with label change
	        var options = new Object();
	        if (projectNodeToReload ) {
	            var nodeToReload = this.getProjectTree().getStore().getNodeById(projectNodeToReload);
	            // if null reload the branch i.e. the main
				// projects
	            options.node = nodeToReload;
	        }
	        if (CWHF.isNull(projectNodeToSelect)) {
	            // after deleting a main project
	            var rootNode = this.getProjectTree().getRootNode();
	            if (rootNode  && rootNode.childNodes  && rootNode.childNodes.length > 0) {
	                var firstChild = rootNode.getChildAt(0);
	                if (deletedProjectID  && firstChild.data['id'] === deletedProjectID) {
	                    if (rootNode.childNodes.length > 1) {
		                    var secondChild = rootNode.getChildAt(1);
		                    projectNodeToSelect = secondChild.data['id'];
	                    } /*
							 * else { //the last project was
							 * selected this.rootID = null;
							 * this.onCancel(); }
							 */
	                } else {
	                    projectNodeToSelect = firstChild.data['id'];
	                }
	            }
	        }
	        // if (projectNodeToSelect) {
	        // callback after load
	        options.callback = this.selectNode;
	        // scope for callback
	        options.scope = {
	            tree : this.getProjectTree(),
	            nodeIdToReload : projectNodeToReload,
	            nodeIdToSelect : projectNodeToSelect,
	            scope : this,
	            expandWorkspaceNode : false
	        };
	        // }
	        this.getProjectTree().getStore().load(options);
	    }
	},

	selectNode : function() {
	    if (this.nodeIdToReload ) {
	        nodeReloaded = this.tree.getStore().getNodeById(this.nodeIdToReload);
	        if (nodeReloaded  && !nodeReloaded.isExpanded()) {
	            nodeReloaded.expand();
	        }
	    }
	    if (this.nodeIdToSelect ) {
	        var nodeToSelect = this.tree.getStore().getNodeById(this.nodeIdToSelect);
	        if (nodeToSelect ) {
	            var selectionModel = this.tree.getSelectionModel();
	            selectionModel.select(nodeToSelect);
	            com.trackplus.admin.myClick(this.tree.getView(), nodeToSelect);
	        }
	    } else {
	        // no node to select -> no project
	        borderLayout.controller.setCenterContent(null);
	        borderLayout.setActiveToolbarActionList(this.scope.getToolbarActions(false));
	    }
	},

	/**
	 * Parameters for deleting entity recordData: the selected
	 * entity data Even if there is more than one entity
	 * selected for delete this method is called for each
	 * selected entity separately extraConfig: for simple grid
	 * nothing, for tree with grid {fromTree:fromTree,
	 * isLeaf:isLeaf}
	 */
	getDeleteParams : function(selectedRecords, extraConfig) {
	    var deleteParams = this.callParent(arguments);
	    deleteParams['projectID'] = this.getRootID();
	    return deleteParams;
	},

	/**
	 * By selecting the General node
	 */
	getToolbarActionsForTreeNodeSelect : function(selectedNode) {
	    return [ this.actionSave, this.actionCancel ];
	},

	/**
	 * Gets the URL for loading the node detail
	 */
	getDetailUrl : function(node, add) {
	    return this.getBaseAction() + '!load.action';
	},

	/**
	 * Gets the URL for loading the node detail
	 */
	getDetailParams : function(node, add, extraDetailParameters) {
	    var detailParams = null;
	    if (CWHF.isNull(extraDetailParameters)) {
	        detailParams = new Object();
	    } else {
	        detailParams = extraDetailParameters;
	    }
	    detailParams['projectID'] = this.getRootID();
	    detailParams['add'] = add;
	    // return {projectID:this.rootID, add:add};
	    return detailParams;
	},

	/**
	 * The method to process the data to be loaded arrived from
	 * the server
	 */
	getEditPostDataProcess : function(record, isLeaf, add) {
	    return this.postDataLoadCombos;
	},

	/**
	 * Gets a control by the path according to the "arguments"
	 * starting form the main tab panel
	 */
	getControl : function() {
	    return CWHF.getControl.apply(this.tabPanel, arguments);
	},

	getHelpWrapper : function() {
	    return CWHF.getHelpWrapper.apply(this.tabPanel, arguments);
	},

	getWrappedControl : function() {
	    return CWHF.getWrappedControl.apply(this.tabPanel, arguments);
	},

	/**
	 * Load the combos after the result has arrived containing
	 * also the combo data sources
	 */
	postDataLoadCombos : function(data, panel) {
	    // project main
		var me = this;
	    var mainTab = this.getControl("mainTab");
	    var projectType = this.getWrappedControl("mainTab", "fsbasic", "projectType");
	    projectType.store.loadData(data['projectTypeList']);
	    projectType.setValue(data['projectBaseTO.projectTypeID']);
	    // add the change listener only here: if it would be
		// added by form creation by setting
	    // the listenerConfig parameter the setValue() above
		// would trigger
	    // onProjectTypeOrIssueTypeChange which is not desirable
	    projectType.on('change', this.onProjectTypeOrIssueTypeChange, this);

	    if (!this.getIsTemplate()) {
	        var projectStatus = this.getWrappedControl("mainTab", "fsbasic", "projectStatus");
	        projectStatus.store.loadData(data['projectStatusList']);
	        projectStatus.setValue(data['projectBaseTO.projectStatusID']);
	    }
	    var hasPrefix = data['hasPrefix'];
	    if (hasPrefix) {
	    	var workspacePrefixTxtBoxChangeObj = {};
	    	workspacePrefixTxtBoxChangeObj.change = function(txtBox, newValue, oldValue, eOpts) {
        		me.checkNewWorkspacePrefix(txtBox, newValue, me.getRootID());
	    	};

	    	mainTab.add({
	            xtype : 'fieldset',
	            itemId : 'fsprefix',
	            width : this.FieldSetWidth,
	            title : getText('admin.project.lbl.prefix'),
	            collapsible : false,
	            defaults : {
	                anchor : '100%'
	            },
	            layout : 'anchor',
	            items : [ CWHF.createTextFieldWithHelp('admin.project.lbl.prefix',
	                    'projectBaseTO.prefix', {
	                        width : this.textFieldWidthShort,
	                        value : data['projectBaseTO.prefix'],
	                        allowBlank : false,
	                        readOnly: data['projectBaseTO.projectTypeID'] < 0
	                    }, workspacePrefixTxtBoxChangeObj)]
	        });
	    }
	    var hasAccounting = data['hasAccounting'];
	    if (hasAccounting) {
	        this.addAccountingFieldSet(data, mainTab);
	    }

	    var hasRelease = data['hasRelease'];
	    if (hasRelease) {
	        this.addReleaseFieldSet(data, mainTab);
	    }

	    // default tab
	    var defaultManager = this.getWrappedControl("defaultTab", "fsdef",
	            "projectDefaultsTODefaultManagerID");
	    defaultManager.store.loadData(data['managerList']);
	    defaultManager.setValue(data['projectDefaultsTO.defaultManagerID']);

	    var defaultResponsible = this.getWrappedControl("defaultTab", "fsdef",
	            "projectDefaultsTODefaultResponsibleID");
	    defaultResponsible.store.loadData(data['responsibleList']);
	    defaultResponsible.setValue(data['projectDefaultsTO.defaultResponsibleID']);

	    var defaultIssueType = this.getWrappedControl("defaultTab", "fsdef",
	            "projectDefaultsTODefaultIssueTypeID");
	    defaultIssueType.store.loadData(data['issueTypeList']);
	    defaultIssueType.setValue(data['projectDefaultsTO.defaultIssueTypeID']);
	    // add the change listener only here: if it would be
		// added by form creation by setting
	    // the listenerConfig parameter the setValue() above
		// would trigger
	    // onProjectTypeOrIssueTypeChange which is not desirable
	    defaultIssueType.on('change', this.onProjectTypeOrIssueTypeChange, this);

	    var defaultPriority = this.getWrappedControl("defaultTab", "fsdef",
	            "projectDefaultsTODefaultPriorityID");
	    defaultPriority.store.loadData(data['priorityList']);
	    defaultPriority.setValue(data['projectDefaultsTO.defaultPriorityID']);

	    var defaultSeverity = this.getWrappedControl("defaultTab", "fsdef",
	            "projectDefaultsTODefaultSeverityID");
	    defaultSeverity.store.loadData(data['severityList']);
	    defaultSeverity.setValue(data['projectDefaultsTO.defaultSeverityID']);

	    // add the initial statuses for issueTypes only when the
		// data has arrived
	    var fsInitSt = this.getControl("defaultTab", "fsInitSt");

	    var initialStatus = this.getWrappedControl("defaultTab", "fsInitSt",
	            "projectDefaultsTOInitialStatusID");
	    initialStatus.store.loadData(data['statusList']);
	    initialStatus.setValue(data['projectDefaultsTO.initialStatusID']);

	    initStItems = [];
	    var initStatusData = data['initStatuses'];
	    if (initStatusData ) {
	        Ext.Array.forEach(initStatusData, function(item) {
	            var issueType = item['issueType'];
	            var status = item['status'];
	            var active = item['active'];
	            var statusList = item['statusList'];
	            var comboName = 'projectDefaultsTO.issueTypeToInitStatusMap[' + issueType + ']';
	            if (issueType < 0) {
	                comboName = 'projectDefaultsTO.issueTypeNegativeToInitStatusMap[' + (-issueType)
	                        + ']';
	            }
	            var comboControl = CWHF.createCombo(item['label'], comboName, {
	                value : status,
	                labelWidth : this.labelWidth - 40,
	                width : this.textFieldWidth - 60,
	                data : statusList,
	                disabled : !active,
	                labelIsLocalized : true
	            });
	            var issueTypePanel = new Ext.Panel({
	                border : false,
	                defaults : {
	                    margin : "4 4 0 0"
	                },
	                margin : "0 10 3 80",
	                layout : {
	                    type : 'hbox'
	                },
	                itemId : 'issueTypePanel' + issueType,
	                items : [ CWHF.createCheckbox('', '', {
	                    value : active,
	                    width : 20
	                }, {
	                    change : {
	                        fn : this.onStatusForIssueTypeChange,
	                        scope : this,
	                        comboControl : comboControl
	                    }
	                }), comboControl ]
	            });
	            initStItems.push(issueTypePanel);
	        }, this);
	        fsInitSt.add(initStItems);
	    }

	    var errors = data.errors;
	    this.markInvalidByErrors(errors, panel);
	    /*
		 * if (errors) { var accountErrorMessage =
		 * errors['projectBaseTO.defaultAccount']; if
		 * (accountErrorMessage) { var defaultAccount =
		 * this.getWrappedControl("mainTab", "fsacc",
		 * "projectBaseTO.defaultAccount")
		 * defaultAccount.markInvalid(accountErrorMessage); }
		 * else { var managerErrorMessage =
		 * errors['projectDefaultsTO.defaultManagerID']; if
		 * (managerErrorMessage) {
		 * defaultManager.markInvalid(managerErrorMessage); }
		 * var responsibleErrorMessage =
		 * errors['projectDefaultsTO.defaultResponsiblID']; if
		 * (responsibleErrorMessage) {
		 * defaultResponsible.markInvalid(responsibleErrorMessage); }
		 * var statusErrorMessage =
		 * errors['projectDefaultsTO.initialStatusID']; if
		 * (statusErrorMessage) {
		 * initialStatus.markInvalid(statusErrorMessage); } var
		 * issueTypeErrorMessage =
		 * errors['projectDefaultsTO.defaultIssueTypeID']; if
		 * (issueTypeErrorMessage) {
		 * defaultIssueType.markInvalid(issueTypeErrorMessage); }
		 * var priorityErrorMessage =
		 * errors['projectDefaultsTO.defaultPriorityID']; if
		 * (priorityErrorMessage) {
		 * defaultPriority.markInvalid(priorityErrorMessage); }
		 * var severityErrorMessage =
		 * errors['projectDefaultsTO.defaultSeverityID']; if
		 * (severityErrorMessage) {
		 * defaultSeverity.markInvalid(severityErrorMessage); }
		 * var tabPanel = panel.getComponent('tabPanel');
		 * tabPanel.setActiveTab(defaultTab); } }
		 */
	    // email tab

	    if (com.trackplus.TrackplusConfig.appType !== APPTYPE_BUGS) {
	        var eoutValue = this.getWrappedControl("emailTab", "fsout",
	                "projectEmailTOSendFromProjectEmail").getValue();
	        this.enableEmailOutFields(eoutValue);
	        var emailEnabled = this.getWrappedControl("emailTab", "fseserv", "projectEmailTOEnabled");
	        this.onChangeEmailEnabled(emailEnabled, emailEnabled.getValue());
	    }
	},

	checkNewWorkspacePrefix: function(txtBox, newValue, projectID) {
		Ext.Ajax.request({
	        url : "	project!validateProjectPrefix.action",
	        disableCaching : true,
	        success : function(result) {
	        	var jsonData = Ext.decode(result.responseText);
	        	if(jsonData.projectPrefixExists) {
	        		txtBox.markInvalid(getText('common.err.duplicatePrefix'));
	        	}
	        },
	        failure : function() {
	        },
	        method : 'POST',
	        params : {'projectBaseTO.prefix': newValue, 'projectID': projectID}
	    });
	},

	markInvalidByErrors : function(errors, panel) {
	    var accountErrorFound = false;
	    var systemListErrorFound = false;
	    if (errors ) {
	        var accountErrorMessage = errors['projectBaseTO.defaultAccount'];
	        if (accountErrorMessage ) {
	            var defaultAccount = this.getWrappedControl("mainTab", "fsacc",
	                    "projectBaseTO.defaultAccount");
	            defaultAccount.markInvalid(accountErrorMessage);
	            accountErrorFound = true;
	        }

	        var defaultSystemListIDs = [ "projectDefaultsTODefaultManagerID",
	                "projectDefaultsTODefaultResponsiblID", "projectDefaultsTOInitialStatusID",
	                "projectDefaultsTODefaultIssueTypeID", "projectDefaultsTODefaultPriorityID",
	                "projectDefaultsTODefaultSeverityID" ];
	        Ext.Array.forEach(defaultSystemListIDs, function(item) {
	            var errorMessage = errors[item];
	            if (errorMessage ) {
	                var control = this.getWrappedControl("defaultTab", "fsdef", item);
	                if (control ) {
	                    if (!systemListErrorFound) {
		                    systemListErrorFound = true;
	                    }
	                    control.markInvalid(errorMessage);
	                }
	            }
	        }, this);
	        if (!accountErrorFound && systemListErrorFound) {
	            var tabPanel = panel.getComponent('tabPanel');
	            tabPanel.setActiveTab('defaultTab');
	        }
	        /*
			 * var tabBar=tabPanel.getTabBar(); var
			 * headerCm=tabBar.getComponent(1);
			 * headerCm.addCls("errorTab");
			 */
	    }
	},

	addAccountingFieldSet : function(data, mainTab) {
	    var subproject = data["subproject"];
	    var accountingInherited = data["projectAccountingTO.accountingInherited"];
	    var workTracking = data["projectAccountingTO.workTracking"];
	    var costTracking = data["projectAccountingTO.costTracking"];
	    var items = [];
	    if (this.addAsSubproject || subproject) {
	        items.push(CWHF.createCheckboxWithHelp("admin.project.lbl.accountingInherited",
	                "projectAccountingTO.accountingInherited", {
	                    width : this.textFieldWidthCheck,
	                    value : accountingInherited
	                }, {
	                    change : {
	                        fn : this.onAccountingInheritedChange,
	                        scope : this
	                    }
	                }));
	    }
	    items.push(CWHF.createCheckboxWithHelp("admin.project.lbl.work",
	            "projectAccountingTO.workTracking", {
	                width : this.textFieldWidthCheck,
	                value : workTracking,
	                itemId: 'projectAccountingTOWorkTracking'
	            }, {
	                change : {
	                    fn : this.onWorkTrackingChange,
	                    scope : this
	                }
	            }));
	    items.push(CWHF.createNumberFieldWithHelp("common.lbl.hoursPerWorkday",
	            "projectAccountingTO.hoursPerWorkday", 2, 0, 24, {
	                width : this.textFieldWidthShort,
	                value : data['projectAccountingTO.hoursPerWorkday'],
	                itemId: 'projectAccountingTOHoursPerWorkday'
	            }));
	    items.push(CWHF.createComboWithHelp("admin.project.lbl.defaultWorkUnit",
	            "projectAccountingTO.defaultWorkUnit", {
	                itemId: "projectAccountingTODefaultWorkUnit",
	    			data : data['workUnitList'],
	                value : data["projectAccountingTO.defaultWorkUnit"]
	            }));
	    items.push(CWHF.createCheckboxWithHelp("admin.project.lbl.cost",
	            "projectAccountingTO.costTracking", {
	                width : this.textFieldWidthCheck,
	                value : costTracking,
	                itemId: 'projectAccountingTOCostTracking'
	            }, {
	                change : {
	                    fn : this.onCostTrackingChange,
	                    scope : this
	                }
	            }));
	    items.push(CWHF.createTextFieldWithHelp("admin.project.lbl.currencyName",
	            "projectAccountingTO.currencyName", {
	                width : this.textFieldWidthShort,
	                value : data['projectAccountingTO.currencyName'],
	                itemId: 'projectAccountingTOCurrencyName'
	            }));
	    items.push(CWHF.createTextFieldWithHelp("admin.project.lbl.currencySymbol",
	            "projectAccountingTO.currencySymbol", {
	                width : this.textFieldWidthShort,
	                value : data['projectAccountingTO.currencySymbol'],
	                itemId : 'projectAccountingTOCurrencySymbol'
	            }));
	    items.push(CWHF.createComboWithHelp('admin.project.lbl.defaultAccount',
	            "projectAccountingTO.defaultAccount", {
	    			itemId: "projectAccountingTODefaultAccount",
	                data : data["accountList"],
	                value : data['projectAccountingTO.defaultAccount']
	            }));
	    var accountingFieldSet = {
	        xtype : 'fieldset',
	        itemId : 'fsacc',
	        width : this.FieldSetWidth,
	        title : getText('admin.project.lbl.fsaccount'),
	        collapsible : false,
	        defaultType : 'textfield',
	        defaults : {
	            anchor : '100%'
	        },
	        layout : 'anchor',
	        items : items
	    };
	    mainTab.add(accountingFieldSet);
	    var disabled = (this.addAsSubproject || subproject) && accountingInherited;
	    this.enableAccountingFields(disabled, workTracking, costTracking);
	},

	addReleaseFieldSet : function(data, mainTab) {
	    mainTab.add({
	        xtype : 'fieldset',
	        itemId : 'fsrel',
	        width : this.FieldSetWidth,
	        title : getText('admin.project.lbl.release'),
	        collapsible : false,
	        defaults : {
	            anchor : '100%'
	        },
	        layout : 'anchor',
	        items : [ CWHF.createCheckboxWithHelp('admin.project.lbl.useRelease',
	                'projectBaseTO.useRelease', {
	                    width : this.textFieldWidthCheck,
	                    value : data['projectBaseTO.useRelease'],
	                    itemId: 'projectBaseTOUseRelease'
	                }) ]
	    });
	},

	onStatusForIssueTypeChange : function(field, newValue, oldValue, options) {
	    options.comboControl.setDisabled(!newValue);
	},

	/**
	 * Enable or disable the fields related to accounting (work and cost tracking)
	 */
	enableAccountingFields : function(accountingInherited, workTracking, costTracking) {
	    this.getHelpWrapper("mainTab", "fsacc", "projectAccountingTOWorkTracking").setDisabled(
	            accountingInherited);
	    this.getHelpWrapper("mainTab", "fsacc", "projectAccountingTOHoursPerWorkday").setDisabled(
	            accountingInherited || !workTracking);
	    this.getHelpWrapper("mainTab", "fsacc", "projectAccountingTODefaultWorkUnit").setDisabled(
	            accountingInherited || !workTracking);
	    this.getHelpWrapper("mainTab", "fsacc", "projectAccountingTOCostTracking").setDisabled(
	            accountingInherited);
	    this.getHelpWrapper("mainTab", "fsacc", "projectAccountingTOCurrencyName").setDisabled(
	            accountingInherited || !costTracking);
	    this.getHelpWrapper("mainTab", "fsacc", "projectAccountingTOCurrencySymbol").setDisabled(
	            accountingInherited || !costTracking);
	    this.getHelpWrapper("mainTab", "fsacc", "projectAccountingTODefaultAccount").setDisabled(
	            accountingInherited || !workTracking && !costTracking);
	},

	/**
	 * Handler for changing an invalid value handling
	 */
	onAccountingInheritedChange : function(field, newValue, oldValue, options) {
	    var accountingInherited = field.getValue();
	    var workTracking = this.getWrappedControl("mainTab", "fsacc",
	            "projectAccountingTOWorkTracking").getValue();
	    var costTracking = this.getWrappedControl("mainTab", "fsacc",
	            "projectAccountingTOCostTracking").getValue();
	    this.enableAccountingFields(accountingInherited, workTracking, costTracking);
	},

	onWorkTrackingChange : function(field, newValue, oldValue, options) {
	    var workTracking = field.getValue();
	    this.enableWorkTrackingFields(workTracking);
	},

	/**
	 * Enable or disable the fields related to work tracking
	 */
	enableWorkTrackingFields : function(workTracking) {
	    var costTracking = this.getWrappedControl("mainTab", "fsacc",
	            "projectAccountingTOCostTracking").getValue();
	    this.getHelpWrapper("mainTab", "fsacc", "projectAccountingTOHoursPerWorkday").setDisabled(
	            !workTracking);
	    this.getHelpWrapper("mainTab", "fsacc", "projectAccountingTODefaultWorkUnit").setDisabled(
	            !workTracking);
	    this.getHelpWrapper("mainTab", "fsacc", "projectAccountingTODefaultAccount").setDisabled(
	            !workTracking && !costTracking);
	},

	onCostTrackingChange : function(field, newValue, oldValue, options) {
	    var costTracking = field.getValue();
	    this.enableCostTrackingFields(costTracking);
	},

	/**
	 * Enable or disable the fields related to cost tracking
	 */
	enableCostTrackingFields : function(costTracking) {
	    var workTracking = this.getWrappedControl("mainTab", "fsacc",
	            "projectAccountingTOWorkTracking").getValue();
	    this.getHelpWrapper("mainTab", "fsacc", "projectAccountingTOCurrencyName").setDisabled(
	            !costTracking);
	    this.getHelpWrapper("mainTab", "fsacc", "projectAccountingTOCurrencySymbol").setDisabled(
	            !costTracking);
	    this.getHelpWrapper("mainTab", "fsacc", "projectAccountingTODefaultAccount").setDisabled(
	            !costTracking && !workTracking);
	},

	/**
	 * Extra options for the detail panel
	 */
	getDetailPanelConfigOptions : function() {
	    //avoid double scrolls by adding a single panel (tab panel) tp the form panel
	    return {
	        autoScroll : false,
	        layout : "fit"
	    };
	},

	/**
	 * Gets the project type items
	 */
	getDetailItems : function(node, add) {
	    var me = this;
	    var activeTab = null;
	    if (me.getLastSelections()) {
	    	activeTab = me.getLastSelections().lastSelectedTab;
		}
	    if (CWHF.isNull(activeTab)) {
	        activeTab = 0;
	    }
	    var tabs =  [ this.createTabMain(add), this.createTabDefault()];
	    if (com.trackplus.TrackplusConfig.appType !== APPTYPE_BUGS) {
	    	tabs.push(this.createTabEmail());
	    }
	    this.tabPanel = Ext.create('Ext.tab.Panel', {
	        itemId : 'tabPanel',
	        plain : true,
	        border : false,
	        bodyBorder : false,
	        cls : 'projectConfig',
	        defaults : {
	            border : false,
	            autoScroll : true,
	            bodyStyle : {
	                border : 'none',
	                padding : '0px'
	            }
	        },
	        items : tabs,
	        activeTab : activeTab
	    });
	    return this.tabPanel;
	},
	tabActivate: function(tab) {
	    var me = this;
	    var tabIndex = me.tabPanel.items.indexOf(tab);
	    me.getLastSelections().lastSelectedTab = tabIndex;
	    var storeTabUrl = "project!storeLastSelectedTab.action";
	    Ext.Ajax.request({
	        fromCenterPanel : true,
	        url : storeTabUrl,
	        disableCaching : true,
	        method : 'POST',
	        params : {
	            "tabIndex" : tabIndex
	        }
	    });
	},
	storeLastSelectedSection : function(sectionID) {
	    var storeTabUrl = "project!storeLastSelectedSection.action";
	    Ext.Ajax.request({
	        fromCenterPanel : true,
	        url : storeTabUrl,
	        disableCaching : true,
	        method : 'POST',
	        params : {
	            "sectionID" : sectionID
	        }
	    });
	},

	/**
	 * Creates the main project tab
	 * @return {Ext.form.Panel} A panel for this tab
	 */
	createTabMain : function(add) {
	    var me = this;
	    var items = [];
	    items.push(CWHF.createTextField('common.lbl.name', 'projectBaseTO.label', {
	        allowBlank : false,
	        maxLength : 255
	    }));
	    items.push(CWHF.createTextAreaField('common.lbl.description', 'projectBaseTO.description', {
	        height : 150,
	        maxLength : 255
	    }));
	    items.push(CWHF.createComboWithHelp('common.lbl.projectType', 'projectBaseTO.projectTypeID',
	            {itemId:'projectType'}, null));
	    if (!this.getIsTemplate()) {
	        items.push(CWHF.createComboWithHelp('admin.customize.localeEditor.type.projectStatus',
	                'projectBaseTO.projectStatusID', {itemId:'projectStatus'}, null));
	    }
	    var panel = Ext.create('Ext.panel.Panel', {
	        title : getText('admin.project.lbl.mainTab'),
	        itemId : 'mainTab',
	        items : [
	                {
	                    xtype : 'fieldset',
	                    itemId : 'fsbasic',
	                    width : this.FieldSetWidth,
	                    title : getText('admin.project.lbl.fsbasic'),
	                    collapsible : false,
	                    defaultType : 'textfield',
	                    defaults : {
	                        anchor : '100%'
	                    },
	                    layout : 'anchor',
	                    items : items
	                },
	                {
	                    xtype : 'fieldset',
	                    itemId : 'fslnk',
	                    width : this.FieldSetWidth,
	                    title : getText('admin.project.lbl.linking'),
	                    collapsible : false,
	                    defaults : {
	                        anchor : '100%'
	                    },
	                    layout : 'anchor',
	                    items : [ CWHF.createCheckboxWithHelp('admin.project.lbl.linking',
	                            'projectBaseTO.linking', {
	                                width : this.textFieldWidthCheck,
	                                itemId : 'projectBaseTOLinking'
	                            }) ]
	                }, {
	                    xtype : 'hidden',
	                    name : 'add',
	                    value : add
	                } ]
	    });
	    panel.addListener('activate', me.tabActivate, me);
	    return panel;
	},

	/**
	 * Handler for project type or issue type change
	 */
	onProjectTypeOrIssueTypeChange : function(combo, newValue, oldValue, eOpts) {
	    this.tabPanel.ownerCt.getForm().submit({
	        //allow submit even without the required name this time
	        clientValidation : false,
	        url : this.getBaseAction() + '!projectTypeChange.action',
	        params : {
	            projectID : this.getRootID(),
	            addAsSubproject : this.addAsSubproject
	        },
	        scope : this,
	        success : function(form, action) {
	            var result = action.result;
	            if (result.success) {
	                this.refreshSystemFieldCombos(result.data, this.tabPanel.ownerCt);
	            } else {
	                errorHandlerSave(result);
	            }
	        },
	        failure : function(form, action) {
	            com.trackplus.util.submitFailureHandler(form, action);
	        }
	    });
	},

	/**
	 * Load the combos after the result has arrived containing also the combo data sources
	 */
	refreshSystemFieldCombos : function(data, panel) {
	    var mainTab = this.getControl("mainTab");
	    var accountingFieldSet = this.getControl("mainTab", "fsacc");
	    var oldProjectTypeHasAccounting = accountingFieldSet !== null;
	    var newProjectTypeHasAccounting = data["hasAccounting"];
	    if (oldProjectTypeHasAccounting !== newProjectTypeHasAccounting) {
	        if (oldProjectTypeHasAccounting && !newProjectTypeHasAccounting) {
	            mainTab.remove(accountingFieldSet);
	        } else {
	            this.addAccountingFieldSet(data, mainTab);
	        }
	    } else {
	        if (newProjectTypeHasAccounting) {
	            this.actualizeAccountingByProjectType(data);
	        }
	    }
	    var releaseFieldSet = this.getControl("mainTab", "fsrel");
	    var oldProjectTypeHasRelease = releaseFieldSet !== null;
	    var newProjectTypeHasRelease = data["hasRelease"];
	    if (oldProjectTypeHasRelease !== newProjectTypeHasRelease) {
	        if (oldProjectTypeHasRelease && !newProjectTypeHasRelease) {
	            mainTab.remove(releaseFieldSet);
	        } else {
	            this.addReleaseFieldSet(data, mainTab);
	        }
	    }
	    var initialStatus = this.getWrappedControl("defaultTab", "fsInitSt",
	            "projectDefaultsTO_initialStatusID");
	    initialStatus.store.loadData(data['statusList']);
	    initialStatus.setValue(data['projectDefaultsTO.initialStatusID']);

	    var defaultIssueType = this.getWrappedControl("defaultTab", "fsdef",
	            "projectDefaultsTO_defaultIssueTypeID");
	    defaultIssueType.store.loadData(data['issueTypeList']);
	    defaultIssueType.setValue(data['projectDefaultsTO.defaultIssueTypeID']);

	    var defaultPriority = this.getWrappedControl("defaultTab", "fsdef",
	            "projectDefaultsTO_defaultPriorityID");
	    defaultPriority.store.loadData(data['priorityList']);
	    defaultPriority.setValue(data['projectDefaultsTO.defaultPriorityID']);

	    var defaultSeverity = this.getWrappedControl("defaultTab", "fsdef",
	            "projectDefaultsTODefaultSeverityID");
	    defaultSeverity.store.loadData(data['severityList']);
	    defaultSeverity.setValue(data['projectDefaultsTO.defaultSeverityID']);

	    //after project type or issue type change the system list are refreshed from the server with valid values
	    //call validate for each system field to remove the eventual invalid marked fields at postDataLoadCombos()
	    initialStatus.validate();
	    defaultIssueType.validate();
	    defaultPriority.validate();
	    defaultSeverity.validate();
	    //var tabBar=tabPanel.getTabBar();
	    //var headerCm=tabBar.getComponent(1);
	    //headerCm.removeCls("errorTab");
	},

	actualizeAccountingByProjectType : function(data) {
	    var accountingControls = [ "projectAccountingTODefaultWorkUnit",
	            "projectAccountingTOHoursPerWorkday", "projectAccountingTOCurrencyName",
	            "projectAccountingTOCurrencySymbol" ];
	    Ext.Array.forEach(accountingControls, function(itemId) {
	        var control = this.getWrappedControl("mainTab", "fsacc", itemId.split(".").join("_"));
	        if (control ) {
	            control.setValue(data[itemId]);
	        }
	    }, this);
	},

	/**
	 * Creates the project defaults tab
	 * @return {Ext.form.Panel} A panel for this tab
	 */
	createTabDefault : function() {
	    var me = this;
	    var panel = Ext
	            .create(
	                    'Ext.panel.Panel',
	                    {
	                        title : getText('admin.project.lbl.defaultTab'),
	                        itemId : 'defaultTab',
	                        items : [
	                                {
	                                    xtype : 'fieldset',
	                                    itemId : 'fsdef',
	                                    width : this.FieldSetWidth,
	                                    title : getText('admin.project.lbl.defaultFields'),
	                                    collapsible : false,
	                                    defaultType : 'textfield',
	                                    defaults : {
	                                        anchor : '100%'
	                                    },
	                                    layout : 'anchor',
	                                    items : [
	                                            CWHF.createComboWithHelp(
	                                                    'admin.project.lbl.defaultManager',
	                                                    'projectDefaultsTO.defaultManagerID',{itemId:'projectDefaultsTODefaultManagerID'}),
	                                            CWHF.createComboWithHelp(
	                                                    'admin.project.lbl.defaultResponsible',
	                                                    'projectDefaultsTO.defaultResponsibleID',{itemId:'projectDefaultsTODefaultResponsibleID'}),
	                                            CWHF.createComboWithHelp(
	                                                    'admin.project.lbl.defaultIssueType',
	                                                    'projectDefaultsTO.defaultIssueTypeID',{itemId:'projectDefaultsTODefaultIssueTypeID'}),
	                                            CWHF.createComboWithHelp(
	                                                    'admin.project.lbl.defaultPriority',
	                                                    'projectDefaultsTO.defaultPriorityID',{itemId:'projectDefaultsTODefaultPriorityID'}),
	                                            CWHF.createComboWithHelp(
	                                                    'admin.project.lbl.defaultSeverity',
	                                                    'projectDefaultsTO.defaultSeverityID',{itemId:'projectDefaultsTODefaultSeverityID'}),
	                                            CWHF.getRadioGroupWithHelp(
	                                                            'admin.project.lbl.prefill',
	                                                            this.prefillByWidth,
	                                                            [
	                                                                    {
	                                                                        boxLabel : getText('admin.project.lbl.prefill.lastItem'),
	                                                                        name : 'projectDefaultsTO.prefillBy',
	                                                                        inputValue : this.PREFILL.LASTWORKITEM,
	                                                                        checked : true
	                                                                    },
	                                                                    {
	                                                                        boxLabel : getText('admin.project.lbl.prefill.projectDefault'),
	                                                                        name : 'projectDefaultsTO.prefillBy',
	                                                                        inputValue : this.PREFILL.PROJECTDEFAULT
	                                                                    } ], {
	                                                            	itemId : 'prefillBy',
	                                                                layout : 'checkboxgroup',
	                                                                columns : 2
	                                                            }) ]
	                                },
	                                {
	                                    xtype : 'fieldset',
	                                    itemId : 'fsInitSt',
	                                    width : this.FieldSetWidth,
	                                    //padding: '8 0 6 0',
	                                    title : getText('admin.project.lbl.initialState'),
	                                    collapsible : false,
	                                    defaults : {
	                                        anchor : '100%'
	                                    },
	                                    layout : 'anchor',
	                                    items : [
	                                            CWHF.createComboWithHelp(
	                                                    'admin.project.lbl.defaultInitialState',
	                                                    'projectDefaultsTO.initialStatusID',{itemId:'projectDefaultsTOInitialStatusID'}),
	                                            Ext
	                                                    .create(
	                                                            'Ext.Component',
	                                                            {
	                                                                html : getText('admin.project.lbl.defaultIssueTypeInitialState'),
	                                                                margin : '0 5 5 5',
	                                                                border : true,
	                                                                cls : 'infoBox3'
	                                                            }) ]
	                                } ]
	                    });
	    panel.addListener('activate', me.tabActivate, me);
	    return panel;
	},

	/**
	 * Handler for changing an invalid value handling
	 */
	onEmailOutChange : function(field, newValue, oldValue, options) {
	    var eoutValue = field.getValue();
	    this.enableEmailOutFields(eoutValue);
	},

	enableEmailOutFields : function(eout) {
	    this.getHelpWrapper("emailTab", "fsout", "projectEmailTOProjectFromEmail").setDisabled(!eout);
	    this.getHelpWrapper("emailTab", "fsout", "projectEmailTOProjectFromEmailName").setDisabled(!eout);
	    this.getHelpWrapper("emailTab", "fsout", "projectEmailTOSendFromProjectAsReplayTo").setDisabled(!eout);
	},

	onChangeEmailEnabled : function(checkBox, newValue, oldValue, eOpts) {
	    disabled = !newValue;
	    this.getHelpWrapper("emailTab", "fseserv", "projectEmailTOProtocol").setDisabled(disabled);
	    this.getHelpWrapper("emailTab", "fseserv", "projectEmailTOServerName").setDisabled(disabled);
	    this.getHelpWrapper("emailTab", "fseserv", "projectEmailTOPort").setDisabled(disabled);

	    this.getHelpWrapper("emailTab", "fsauth", "projectEmailTOUser").setDisabled(disabled);
	    this.getHelpWrapper("emailTab", "fsauth", "projectEmailTOPassword").setDisabled(disabled);
	    this.getHelpWrapper("emailTab", "fsauth", "securityConnection").setDisabled(disabled);


	    this.getHelpWrapper("emailTab", "fsother", "keep").setDisabled(disabled);
	    this.testBtn.setDisabled(disabled);
	},

	/**
	 * Creates the project defaults tab
	 * @return {Ext.form.Panel} A panel for this tab
	 */
	createTabEmail : function() {
	    var me = this;
	    me.testBtn = Ext.create('Ext.button.Button', {
	        xtype : 'button',
	        style : {
	            marginTop : '10px',
	            marginBottom : '5px',
	            marginLeft : (me.labelWidth + 20) + 'px'
	        },
	        itemId : 'emailTab_testConnection',
	        enableToggle : false,
	        iconCls : 'check16',
	        text : getText('admin.server.config.testConnection'),
	        handler : function() {
	            me.testEmailIncoming.call(me);
	        }
	    });
	    var panel = Ext.create(
	    		'Ext.panel.Panel', {
	    			title : getText('admin.project.lbl.emailTab'),
	    			itemId : 'emailTab',
	    			layout : {
	    				type : 'anchor'
	    			},
	    			items : [{
	    				xtype : 'fieldset',
	    				itemId : 'fseserv',
	    				width : this.FieldSetWidth,
                        title : getText('admin.server.config.incomingServer'),
                        collapsible : false,
                        defaultType : 'textfield',
	                    defaults : {
	                    	anchor : '100%'
	                    },
	                    layout : 'anchor',
	                    items : [
	                    CWHF.createCheckboxWithHelp(
	                    		'admin.server.config.emailSubmission',
	                            'projectEmailTO.enabled', {itemId:"projectEmailTOEnabled"}, {
	                            change : {
	                            	fn: this.onChangeEmailEnabled,
	                                	scope: this
	                            }}),
	                             CWHF.getRadioGroupWithHelp('admin.server.config.mailReceivingProtocol',
	                            	this.protocolWidth, [{
	                            			 boxLabel : 'POP3',
	                            			 name : 'projectEmailTO.protocol',
	                            			 inputValue : 'pop3',
	                            			 checked : true
	                            		 }, {
	                            			 boxLabel : 'IMAP',
	                            			 name : 'projectEmailTO.protocol',
	                            			 inputValue : 'imap'
	                            		 }], {itemId:'projectEmailTOProtocol'}, {
	                            			 change : {
	                            				 fn : this.changePort,
	                            				 scope : this
	                            			 }
	                            		 }),
	                            		 CWHF.createTextFieldWithHelp(
	                            				 'admin.server.config.mailReceivingServerName',
	                            				 'projectEmailTO.serverName',{itemId:'projectEmailTOServerName'}),
	                            		CWHF.createTextFieldWithHelp(
	                            				'admin.server.config.mailReceivingPort',
	                            				'projectEmailTO.port', {
	                            				width : this.textFieldWidthShort,
	                            				itemId : 'projectEmailTOPort'
	                            				})
	                            ]
	                    },{
	    				xtype : 'fieldset',
	    				itemId : 'fsauth',
	                                    width : this.FieldSetWidth,
	                                    title : getText('admin.server.config.incomingAuth'),
	                                    collapsible : false,
	                                    defaultType : 'textfield',
	                                    defaults : {
	                                        anchor : '100%'
	                                    },
	                                    layout : 'anchor',
	                                    items : [
	                                            CWHF.createTextFieldWithHelp(
	                                                    'admin.server.config.mailReceivingUser',
	                                                    'projectEmailTO.user', {itemId:'projectEmailTOUser'}),
	                                            CWHF.createTextFieldWithHelp(
	                                                    'admin.server.config.mailReceivingPassWord',
	                                                    'projectEmailTO.password', {
	                                                        inputType : 'password',
	                                                        itemId : 'projectEmailTOPassword'
	                                                    }),
	                                            CWHF.getRadioGroupWithHelp('admin.server.config.mailReceivingSecurityConnection',
	                                            		this.securityConnectionWidth,[
	                                                                    {
	                                                                        boxLabel : getText('admin.server.config.securityConnections.never'),
	                                                                        name : 'projectEmailTO.securityConnection',
	                                                                        inputValue : this.SECURITY_CONNECTIONS_MODES.NEVER
	                                                                    },
	                                                                    {
	                                                                        boxLabel : getText('admin.server.config.securityConnections.tlsIsAvailable'),
	                                                                        name : 'projectEmailTO.securityConnection',
	                                                                        inputValue : this.SECURITY_CONNECTIONS_MODES.TLS_IF_AVAILABLE
	                                                                    },
	                                                                    {
	                                                                        boxLabel : getText('admin.server.config.securityConnections.tls'),
	                                                                        name : 'projectEmailTO.securityConnection',
	                                                                        inputValue : this.SECURITY_CONNECTIONS_MODES.TLS
	                                                                    },
	                                                                    {
	                                                                        boxLabel : getText('admin.server.config.securityConnections.ssl'),
	                                                                        name : 'projectEmailTO.securityConnection',
	                                                                        inputValue : this.SECURITY_CONNECTIONS_MODES.SSL
	                                                                    } ], {itemId:'securityConnection'}, {
	                                                                change : {
	                                                                    fn : this.changePort,
	                                                                    scope : this
	                                                                }
	                                                            }) ]
	                                },
	                                {
	                                    xtype : 'fieldset',
	                                    itemId : 'fsout',
	                                    width : this.FieldSetWidth,
	                                    title : getText('admin.project.lbl.fsoutgoing'),
	                                    collapsible : false,
	                                    defaultType : 'textfield',
	                                    defaults : {
	                                        anchor : '100%'
	                                    },
	                                    layout : 'anchor',
	                                    items : [
	                                            CWHF.createCheckboxWithHelp(
	                                                    'admin.project.lbl.useProjectFromAddress',
	                                                    'projectEmailTO.sendFromProjectEmail', {
	                                                        width : this.textFieldWidthCheck,
	                                                        itemId: 'projectEmailTOSendFromProjectEmail'
	                                                    }, {
	                                                        change : {
	                                                            fn : this.onEmailOutChange,
	                                                            scope : this
	                                                        }
	                                                    }),
	                                            CWHF.createTextFieldWithHelp(
	                                                    'admin.project.lbl.projectEmail',
	                                                    'projectEmailTO.projectFromEmail',
	                                                    {itemId: 'projectEmailTOProjectFromEmail'}),
	                                            CWHF.createTextFieldWithHelp(
	                                                    'admin.project.lbl.projectEmailPersonalName',
	                                                    'projectEmailTO.projectFromEmailName', {itemId:'projectEmailTOProjectFromEmailName'}),
	                                            CWHF.createCheckboxWithHelp(
	                                                    'admin.project.lbl.useProjectFromAsReplyTo',
	                                                    'projectEmailTO.sendFromProjectAsReplayTo', {
	                                                        width : this.textFieldWidthCheck,
	                                                        itemId : 'projectEmailTOSendFromProjectAsReplayTo'
	                                                    }) ]
	                                },
	                                {
	                                    xtype : 'fieldset',
	                                    itemId : 'fsother',
	                                    width : this.FieldSetWidth,
	                                    title : getText('admin.server.config.incomingOther'),
	                                    collapsible : false,
	                                    defaultType : 'textfield',
	                                    defaults : {
	                                        anchor : '100%'
	                                    },
	                                    layout : 'anchor',
	                                    items : [ CWHF.createCheckboxWithHelp(
	                                            'admin.server.config.keepMessagesOnServer',
	                                            'projectEmailTO.keepMessagesOnServer', {
	                                                itemId : 'keep',
	                                                width : this.textFieldWidthCheck
	                                            }) ]
	                                }, me.testBtn ]
	                    });
	    panel.addListener('activate', me.tabActivate, me);
	    return panel;
	},

	handleErrors : function(errors, errorMsg) {
	    var me = this;
	    var errStr = '';
	    var tabErrors = new Array();
	    if (CWHF.isNull(errorMsg)) {
	        errorMsg = getText('admin.project.err.errorSave');
	    }
	    if (errors  && errors.length > 0) {
	        for (var i = 0; i < errors.length; i++) {
	            var error = errors[i];
	            var controlPath = error.controlPath;
	            var inputComp = null;
	            if (controlPath  && controlPath.length > 0) {
	                var tabId = controlPath[0];
	                if (!Ext.Array.contains(tabErrors, tabId)) {
	                    tabErrors.push(tabId);
	                }
	                inputComp = this.getControl.apply(this, controlPath);
	                if (CWHF.isNull(inputComp)) {
	                    inputComp = this.getWrappedControl.apply(this, controlPath);
	                }
	            }
	            if (inputComp ) {
	                inputComp.markInvalid(error.errorMessage);
	            } else {
	                errStr += error.errorMessage + "</br>";
	            }
	        }
	        me.markErrorTabs(tabErrors);
	    }
	    if (errStr !== '') {
	        errorMsg = errStr;
	    }
	    CWHF.showMsgError(errorMsg);
	},
	markErrorTabs : function(tabErrors) {
	    var me = this;
	    var tabErrorsCmp = new Array();
	    if (tabErrors.length > 0) {
	        for (var i = 0; i < tabErrors.length; i++) {
	            var tabComp = this.getControl(tabErrors[i]);
	            if (tabComp ) {
	                tabErrorsCmp.push(tabComp);
	            }
	        }
	        if (tabErrorsCmp.length > 0) {
	            var selectedTab = me.tabPanel.getActiveTab();
	            if (!Ext.Array.contains(tabErrorsCmp, selectedTab)) {
	                me.tabPanel.setActiveTab(tabErrorsCmp[0]);
	            }
	            var tabBar = me.tabPanel.getTabBar();
	            for (var i = 0; i < tabErrorsCmp.length; i++) {
	                var index = me.tabPanel.items.findIndex('itemId', tabErrorsCmp[i].getItemId());
	                var headerCm = tabBar.getComponent(index);
	                headerCm.addCls("errorTab");
	            }
	        }
	    }
	},
	clearErrorTabs : function() {
	    var me = this;
	    var tabBar = me.tabPanel.getTabBar();
	    for (var i = 0; i < tabBar.items.length; i++) {
	        var headerCm = tabBar.getComponent(i);
	        headerCm.removeCls("errorTab");
	    }
	    if (me.ldapController ) {
	        me.ldapController.clearErrorTabs.call(me.ldapController);
	    }
	},

	testEmailIncoming : function() {
	    var me = this;
	    borderLayout.setLoading(true);
	    var urlStr = 'project!testIncomingEmail.action';
	    me.clearErrorTabs();
	    var form = me.tabPanel.ownerCt.getForm();
	    if (!form.isValid()) {
	        CWHF.showMsgError(getText('admin.project.err.invalidEmail'));
	        return false;
	    }
	    borderLayout.setLoading(true);
	    form.submit({
	        url : urlStr,
	        params : {
	            projectID : this.getRootID()
	        },
	        success : function(form, action) {
	            borderLayout.setLoading(false);
	            CWHF.showMsgInfo(getText('admin.project.msg.validEmail'));
	        },
	        failure : function(form, action) {
	            borderLayout.setLoading(false);
	            if (action.result  && action.result.errors ) {
	                me.handleErrors(action.result.errors, getText('admin.project.err.invalidEmail'));
	            } else {
	                CWHF.showMsgError(getText('admin.project.err.invalidEmail'));
	            }
	        }
	    });
	},

	changePort : function(protocol, securityConnection, port) {
	    var protocol = this.getWrappedControl("emailTab", "fseserv", "projectEmailTOProtocol");
	    var securityConnection = this.getWrappedControl("emailTab", "fsauth", "securityConnection");
	    var port = this.getWrappedControl("emailTab", "fseserv", "projectEmailTOPort");
	    var protocolValue = CWHF.getSelectedRadioButtonValue(protocol);
	    var securityConnectionValue = CWHF.getSelectedRadioButtonValue(securityConnection);
	    var portValue;
	    if (protocolValue === this.EMAIL_PROTOCOL.POP3) {
	        if (securityConnectionValue === this.SECURITY_CONNECTIONS_MODES.SSL) {
	            portValue = this.DEFAULT_EMAIL_PORTS.POP3_SSL;
	        } else {
	            portValue = this.DEFAULT_EMAIL_PORTS.POP3;
	        }
	    } else {
	        if (securityConnectionValue === this.SECURITY_CONNECTIONS_MODES.SSL) {
	            portValue = this.DEFAULT_EMAIL_PORTS.IMAP_SSL;
	        } else {
	            portValue = this.DEFAULT_EMAIL_PORTS.IMAP;
	        }
	    }
	    port.setValue(portValue);
	},

	getTemplateLockUnlockLabel : function() {
	    var label = getText('admin.project.lbl.templateStateEdit');
	    if (this.templateIsActive) {
	        label = getText('admin.project.lbl.templateStateLock');
	    }
	    return label;
	},

	getTemplateLockUnlockToolTip : function() {
	    var label = getText('admin.project.lbl.templateStateEdit.tt');
	    if (this.templateIsActive) {
	        label = getText('admin.project.lbl.templateStateLock.tt');
	    }
	    return label;
	},

	getTemplateLockUnlockIcon : function() {
	    var icon = "edit16";
	    if (this.templateIsActive) {
	        icon = "lock";
	    }
	    return icon;
	}

});
