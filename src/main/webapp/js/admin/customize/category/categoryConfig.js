/**
 * Genji Scrum Tool and Issue Tracker
 * Copyright (C) 2015 Steinbeis GmbH & Co. KG Task Management Solutions

 * <a href="http://www.trackplus.com">Genji Scrum Tool</a>

 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

/* $Id:$ */


com.trackplus.admin.CategoryConfig = Ext.define('com.trackplus.admin.customize.category.CategoryConfig', {
	extend : 'com.trackplus.admin.TreeWithGrid',
	config : {
	    rootID : '',
	    // set for automail settings, filters, reports in project configuration
	    projectID : null,
	    // exclude private repository entities by default automail conditions
	    // (project specific or global)
	    excludePrivate : false
	// btnExecute: 'common.btn.applyFilter',
	// for report templates: whether we are coming here from an issue navigator
	// fromIssueNavigator:false,
	// workItemID: null
	},
	// used in FieldExpressionAction (not directly here)
	issueFilter : false,
	confirmDeleteEntity : true,
	confirmDeleteNotEmpty : true,
	folderAction : "categoryConfig",
	// showGridForLeaf:false,
	leafDetailUrl : null,
	entityID : 'node',
	// category edit sizes

	folderEditWidth : 400,
	folderEditHeight : 115,
	reportEditWidth : 500,
	reportEditHeight : 150,
	useCopyPaste : true,
	dragAndDropOnTree : true,
	labelWidth : 170,
	/* filter specific fields */
	instant : false,
	// gather the rendered upper select fields
	upperSelectFields : null,
	indexMax : null,
	// actions
	actionInstantFilter : null,
	actionExecuteGridRow : null,
	actionExecuteTreeNode : null,
	actionLinkGridRow : null,
	actionLinkTreeNode : null,
	actionDownloadGridRow : null,
	actionDownloadTreeNode : null,
	// actionImport: null,
	// actionExport: null,
	btnExecute : null,

	statics : {
	    showInstant : function(scope, loadUrl) {
	        var title = getText("menu.findItems.instantFilter");
	        var width = com.trackplus.admin.Filter.issueFilterWindowWidth;
	        var height = com.trackplus.admin.Filter.issueFilterWindowHeight;
	        var items = com.trackplus.admin.Filter.getTreeFilterItems(true, true);
	        var loadParams = {
	            instant : true,
	            add : true,
	            leaf : true
	        };
	        var load = {
	            loadUrl : loadUrl,
	            loadUrlParams : loadParams
	        };
	        var submitUrl = "savedFilterExecute!executeUnwrapped.action";
	        // ajax is true. Although the entire page is refreshed after
	        // executing an instant filter, standardSubmit is not set to true
	        // because in case of error in instant filter (for ex. too many
	        // open/closed parenthesis) an error message should be shown without
	        // page refresh
	        // the page refresh is solved in refreshAfterSubmitHandler:
	        // com.trackplus.admin.Filter.executeInstantFilter
	        var submitParams = {
	            instant : true,
	            add : true,
	            leaf : true,
	            ajax : true
	        };
	        var applySubmit = {
	            submitUrl : submitUrl,
	            submitUrlParams : submitParams,
	            submitButtonText : getText("common.btn.applyFilter"),
	            refreshAfterSubmitHandler : com.trackplus.admin.Filter.executeInstantFilter
	        };
	        var resetSubmit = {
	            submitUrl : "filterConfig!edit.action",
	            submitUrlParams : {
	                clearFilter : true,
	                instant : true
	            },
	            submitButtonText : getText("common.btn.reset"),
	            closeAfterSubmit : false,
	            refreshParametersBeforeSubmit : {
		            items : items
	            },
	            refreshAfterSubmitHandler : com.trackplus.admin.Filter.clearFilter
	        };
	        var submit = [ applySubmit, resetSubmit ];
	        var windowConfig = Ext.create("com.trackplus.util.WindowConfig", {
	            postDataProcess : com.trackplus.admin.Filter.postLoadProcessTreeFilter,
	            preSubmitProcess : com.trackplus.admin.Filter.preSubmitProcessIssueFilter
	        });
	        // test for right parenthesis. You might comment this method to
	        // avoid two server accesses but then set the standardSubmit=true
	        // directly on the first submit button
	        /*
			 * submit[0].submitHandler = function(window, submitUrl,
			 * submitUrlParams, extraConfig) { var theForm =
			 * this.formEdit.getForm(); theForm.submit({ url:
			 * "savedFilterExecute!testUnwrappedTree.action", params:
			 * submitUrlParams, method: "POST", scope: scope, success:
			 * function(form, action) { submit[0].submitHandler = null;
			 * submit[0].standardSubmit = true; //now the "classic"
			 * submitHandler windowConfig.submitHandler(submit[0], scope, 0); },
			 * failure: function(form, action) {
			 * com.trackplus.util.submitFailureHandler(form, action); } }); }
			 */
	        windowConfig.showWindow(scope, title, width, height, load, submit, items);
	    },

	    showAddEditFilter : function(scope, refreshScope, refreshMethod, add, filterID, filterType, title) {
	        var width = com.trackplus.admin.Filter.issueFilterWindowWidth;
	        var height = com.trackplus.admin.Filter.issueFilterWindowHeight;
	        var items = com.trackplus.admin.Filter.getTreeFilterItems(true, false);
	        var loadParams = {
	            add : add,
	            filterID : filterID,
	            filterType : filterType,
	            fromNavigatorContextMenu : true
	        };
	        var load = {
	            loadUrl : "filterConfig!edit.action",
	            loadUrlParams : loadParams
	        };
	        var submitUrl = "filterConfig!save.action";
	        var submitParams = loadParams;
	        var saveSubmit = {
	            submitUrl : submitUrl,
	            submitUrlParams : submitParams,
	            submitButtonText : getText("common.btn.save"),
	            refreshParametersBeforeSubmit : {
	                refreshScope : refreshScope,
	                refreshMethod : refreshMethod
	            },
	            refreshAfterSubmitHandler : function(refreshParameters, result) {
		            refreshMethod.call(refreshScope);
	            }
	        };
	        var resetSubmit = {
	            submitUrl : "filterConfig!edit.action",
	            submitUrlParams : {
	                clearFilter : true,
	                instant : true
	            },
	            submitButtonText : getText("common.btn.reset"),
	            closeAfterSubmit : false,
	            refreshParametersBeforeSubmit : {
		            items : items
	            },
	            refreshAfterSubmitHandler : com.trackplus.admin.Filter.clearFilter
	        };
	        var submit = [ saveSubmit, resetSubmit ];
	        var windowConfig = Ext.create("com.trackplus.util.WindowConfig", {
	            postDataProcess : com.trackplus.admin.Filter.postLoadProcessTreeFilter,
	            preSubmitProcess : com.trackplus.admin.Filter.preSubmitProcessIssueFilter
	        });
	        windowConfig.showWindow(scope, title, width, height, load, submit, items);
	    }
	},

	constructor : function(config) {
	    var config = config || {};
	    this.initialConfig = config;
	    Ext.apply(this, config);
	    this.baseAction = this.initBaseAction();
	    if (this.isIssueFilter()) {
		    this.btnExecute = "common.btn.applyFilter";
	    } else {
		    if (this.isReport()) {
			    this.btnExecute = "common.btn.executeReport";
		    }
	    }
	    this.init();
	},

	isIssueFilter : function() {
	    // this.rootID cold be a project specific branch node
	    return this.rootID.indexOf("issueFilter") == 0;
	},

	isNotifyFilter : function() {
	    // this.rootID cold be a project specific branch node
	    return this.rootID.indexOf("notifyFilter") == 0;
	},

	isReport : function() {
	    // this.rootID cold be a project specific branch node
	    return this.rootID.indexOf("report") == 0;
	},

	getShowGridForLeaf : function() {
	    return this.isReport();
	},

//    /**
//	 * Get the extra parameters for the gridStore
//	 */
//    getGridExtraParams : function(node, opts) {
//	    params = this.callParent(arguments);
//	    if (this.projectID != null) {
//		    params['projectID'] = this.projectID;
//	    }
//	    return params;
//    },

	/**
	 * Gets the base action depending on the rootID
	 */
	initBaseAction : function() {
	    if (this.isIssueFilter() || this.isNotifyFilter()) {
		    if (this.isIssueFilter()) {
			    this.issueFilter = true;
		    }
		    return "filterConfig";
	    }
	    if (this.isReport()) {
		    return "reportConfig";
	    }
	},

	/**
	 * Gets the base action depending on the rootID
	 */
	getEditWidth : function(recordData, isLeaf, add, fromTree, operation) {
	    if (isLeaf) {
		    if (this.isIssueFilter()) {
			    return com.trackplus.admin.Filter.issueFilterWindowWidth;
		    } else {
			    if (this.isNotifyFilter()) {
				    return com.trackplus.admin.Filter.notifyFilterWindowWidth;
			    } else {
				    return this.reportEditWidth;
			    }
		    }
	    } else {
		    return this.folderEditWidth;
	    }
	},

	/**
	 * Gets the base action depending on the rootID
	 */
	getEditHeight : function(recordData, isLeaf, add, fromTree, operation) {
	    if (isLeaf) {
		    if (this.isIssueFilter()) {
			    return com.trackplus.admin.Filter.issueFilterWindowHeight;
		    } else {
			    if (this.isNotifyFilter()) {
				    return com.trackplus.admin.Filter.notifyFilterWindowHeight;
			    } else {
				    return this.reportEditHeight;
			    }
		    }
	    } else {
		    return this.folderEditHeight;
	    }
	},

	/**
	 * The message to appear first time after selecting this menu entry Is
	 * should be shown by selecting the root but the root is typically not
	 * visible
	 */
	getRootMessage : function(rootID) {
	    if (this.isIssueFilter()) {
		    return getText("admin.customize.queryFilter.lbl.description");
	    } else {
		    if (this.isNotifyFilter()) {
			    return getText("admin.customize.automail.filter.lbl.description");
		    } else {
			    return getText("admin.customize.reportTemplate.lbl.description");
		    }
	    }
	},

	/**
	 * The localized entity name
	 */
	getEntityLabel : function(extraConfig) {
	    var entityLabel = null;
	    var isLeaf = true;
	    if (extraConfig != null) {
		    isLeaf = extraConfig.isLeaf;
	    }
	    if (isLeaf) {
		    if (this.isIssueFilter()) {
			    return getText("admin.customize.queryFilter.lbl.issueFilter");
		    } else {
			    if (this.isNotifyFilter()) {
				    return getText("admin.customize.automail.filter.lblOperation");
			    } else {
				    return getText("admin.customize.reportTemplate.lbl");
			    }
		    }
	    } else {
		    return getText('admin.customize.queryFilter.lbl.category');
	    }
	},

	/**
	 * The label for the save button
	 */
	/* protected */getSaveLabel : function(operation) {
	    if (operation == "instant") {
		    return getText(this.btnExecute);
	    } else {
		    return getText('common.btn.save');
	    }
	},

	/**
	 * The url for getting the leaf detail: either this should be overridden or
	 * the leafDetailUrl should be specified in the config
	 */
	/* protected */getLeafDetailUrl : function() {
	    return this.folderAction + '!leafDetail.action';
	},

	initActions : function() {
	    var addFolderIconCls = null;
	    var addLeafIconCls = null;
	    var editIconCls = null;
	    if (this.isNotifyFilter()) {
		    addFolderIconCls = this.getAddFolderNotifyFilterIconCls();
		    addLeafIconCls = this.getAddNotifyFilterIconCls();
		    editIconCls = this.getEditNotifyFilterIconCls();
	    } else {
		    if (this.isIssueFilter()) {
			    addFolderIconCls = this.getAddFolderIssueFilterIconCls();
			    addLeafIconCls = this.getAddIssueFilterIconCls();
			    editIconCls = this.getEditIssueFilterIconCls();
		    } else {
			    if (this.isReport()) {
				    addFolderIconCls = this.getAddFolderReportIconCls();
				    addLeafIconCls = this.getAddReportIconCls();
				    editIconCls = this.getEditReportIconCls();
			    }
		    }
	    }
	    this.actionAddFolder = this.createLocalizedAction(this.getTitle(this.getAddTitleKey(), {
		    isLeaf : false
	    }), addFolderIconCls, this.onAddFolder, this.getTitle(this.getAddTitleKey(), {
		    isLeaf : false
	    }), true);
	    this.actionAddLeaf = this.createLocalizedAction(this.getTitle(this.getAddTitleKey(), {
		    isLeaf : true
	    }), addLeafIconCls, this.onAddLeaf, this.getTitle(this.getAddTitleKey(), {
		    isLeaf : true
	    }), true);
	    this.actionEditGridRow = this.createAction(this.getEditButtonKey(), editIconCls, this.onEditGridRow, true, this
	            .getEditTitleKey(), "editGridRow");
	    this.actionEditTreeNode = this.createAction(this.getEditButtonKey(), editIconCls, this.onEditTreeNode, false,
	            this.getEditTitleKey(), "editTreeNode");
	    if (this.isIssueFilter()) {
		    this.actionExecuteGridRow = this.createAction(this.btnExecute, 'filterExec', this.onExecuteGridRow, true,
		            this.btnExecute);
		    this.actionExecuteTreeNode = this.createAction(this.btnExecute, 'filterExec', this.onExecuteTreeNode, true,
		            this.btnExecute);
		    this.actionInstantFilter = this.createAction("menu.findItems.instantFilter", "filterInst",
		            this.onInstantFilter, false, "menu.findItems.newInstantFilter.tt");
		    this.actionLinkGridRow = this.createLocalizedAction(getText('common.btn.link'), 'filterLink',
		            this.onLinkGridRow, getText("admin.customize.queryFilter.lbl.filterURL.report.encodedUrl"), true);
		    this.actionLinkTreeNode = this.createLocalizedAction(getText('common.btn.permURL'), 'filterLink',
		            this.onLinkTreeNode, getText("admin.customize.queryFilter.lbl.filterURL.report.encodedUrl"), true);
	    } else {
		    if (this.isReport()) {
			    this.actionExecuteGridRow = this.createAction(this.btnExecute, 'rtemplateExec', this.onExecuteGridRow,
			            true, this.btnExecute);
			    this.actionExecuteTreeNode = this.createAction(this.btnExecute, 'rtemplateExec',
			            this.onExecuteTreeNode, true, this.btnExecute);
			    this.actionDownloadGridRow = this.createAction('common.btn.download', 'download',
			            this.onDownloadGridRow, true, "common.lbl.download", "downloadGridRow");
			    this.actionDownloadTreeNode = this.createAction('common.btn.download', 'download',
			            this.onDownloadTreeNode, true, "common.lbl.download", "downloadTreeNode");
			    /*
				 * this.actionImport =
				 * this.createLocalizedAction(getText("common.btn.import"),
				 * 'import', this.onImport, this.getTitle("common.lbl.import"));
				 * this.actionExport =
				 * this.createLocalizedAction(getText("common.btn.export"),
				 * 'export', this.onExport, this.getTitle("common.lbl.export"),
				 * true);
				 */
		    }
	    }
	    this.actionDeleteGridRow = this.createAction(this.getDeleteButtonKey(), this.getDeleteIconCls(),
	            this.onDeleteFromGrid, true, this.getDeleteTitleKey(), "deleteGridRow");
	    this.actionDeleteTreeNode = this.createAction(this.getDeleteButtonKey(), this.getDeleteIconCls(),
	            this.onDeleteFromTree, false, this.getDeleteTitleKey(), "deleteTreeNode");

	    if (this.useCopyPaste) {
		    // cut/copy - paste and drag and drop
		    this.actionCutTreeNode = this.createAction(this.getCutButtonKey(), this.getCutIconCls(),
		            this.onCutTreeNode, false, this.getCutTitleKey(), "cut");
		    this.actionCopyTreeNode = this.createAction(this.getCopyButtonKey(), this.getCopyIconCls(),
		            this.onCopyTreeNode, false, this.getCopyTitleKey(), "copy");
		    this.actionPasteTreeNode = this.createAction(this.getPasteButtonKey(), this.getPasteIconCls(),
		            this.onPasteTreeNode, false, this.getPasteButtonKey());
	    }
	},

	getActionItemIdsWithContextDependentLabel : function() {
	    return [ "editGridRow", "editTreeNode", "deleteGridRow", "deleteTreeNode", "downloadGridRow",
	            "downloadTreeNode", "cut", "copy" ];
	},

	/**
	 * Expanding the node
	 */
	getTreeExpandExtraParams : function(node) {
	    var extraParams = {
		    excludePrivate : this.excludePrivate
	    /*
		 * , fromIssueNavigator:this.fromIssueNavigator
		 */
	    };
	    if (this.projectID != null) {
		    // in project configuration
		    extraParams["projectID"] = this.projectID;
	    }
	    return extraParams;
	},

	/**
	 * Get the extra parameters for the gridStore
	 */
	getGridExtraParams : function(node, opts) {
	    if (node == null) {
		    // called manually
		    node = this.selectedNode;
	    }
	    var params = {
	        node : node.data['id'],
	        excludePrivate : this.excludePrivate
	    /*
		 * , fromIssueNavigator:this.fromIssueNavigator
		 */
	    };
	    if (this.projectID != null) {
		    // in project configuration
		    params["projectID"] = this.projectID;
	    }
	    return params;
	},

	/**
	 * Return false if dragging this node is not allowed
	 */
	canDragDropNode : function(nodeToDrag, copy, overModel) {
	    if (nodeToDrag.data['readOnly']) {
		    // do not drag hardcoded node
		    return false;
	    }
	    if (!nodeToDrag.data['modifiable'] && !copy) {
		    // do not move a not modifiable node
		    return false;
	    }
	    var dropOverNode = overModel;
	    var overLeaf = dropOverNode.isLeaf();
	    if (overLeaf) {
		    dropOverNode = dropOverNode.parentNode;
	    }
	    if (!dropOverNode.data['canAddChild']) {
		    // do not drop in a node with nor right to add child
		    return false;
	    }
	    return true;
	},

	/* protected */getDragDropBaseAction : function(draggedNodeIsLeaf) {
	    return this.folderAction;
	},

	/**
	 * Which actions to enable/disable depending on tree selection
	 */
	getToolbarActionChangesForTreeNodeSelect : function(selectedNode) {
	    var canAddChild = false;
	    if (selectedNode != null) {
		    if (this.projectID != null && selectedNode.isRoot()) {
			    // after initializing the project specific branch for issue
			    // filter and report:
			    // although in the filter/report tree no node is selected, the
			    // add refers to the project specific root
			    // (for notify filter the add methods are allowed only after an
			    // explicit selection
			    // in notify filters branch, because there are both the public
			    // and project (for the actual project)
			    // specific notification filters so it is not obvious where to
			    // add when nothing is selected
			    // selectedNode.isRoot(): after deleting a node directly below
			    // root
			    if (this.isIssueFilter() || this.isReport()) {
				    canAddChild = true;
			    }
		    } else {
			    canAddChild = selectedNode.data['canAddChild'];
		    }
	    }
	    this.actionAddFolder.setDisabled(!canAddChild);
	    this.actionAddLeaf.setDisabled(!canAddChild);
	    // nothing selected in the grid
	    this.actionEditGridRow.setDisabled(true);
	    this.actionDeleteGridRow.setDisabled(true);
	    if (this.isIssueFilter() || this.isReport()) {
		    this.actionExecuteGridRow.setDisabled(true);
		    if (this.isIssueFilter()) {
			    this.actionLinkGridRow.setDisabled(true);
		    } else {
			    this.actionDownloadGridRow.setDisabled(true);
			    // this.actionExport.setDisabled(true);
		    }
	    }
	},

	/**
	 * Automatically expand the private repository
	 */
	onTreeNodeLoad : function(treeStore, node) {
	    if (node.isRoot() && this.projectID == null) {
		    // for not project specific categories expand the private repository
		    // (first child)
		    treeStore.load({
		        node : node.firstChild,
		        callback : function() {
			        node.firstChild.expand();
		        }
		    });
	    }
	},

	/**
	 * Create the edit
	 */
	onTreeNodeDblClick : function(view, record) {
	    var readOnly = record.data['readOnly'];
	    if (readOnly == null || readOnly == false) {
		    this.onEditTreeNode();
	    }
	},

	onGridRowDblClick : function(view, record) {
	    var readOnly = record.data['readOnly'];
	    if (readOnly == null || readOnly == false) {
		    this.onEditGridRow();
	    }
	},

	/**
	 * Gets the tree's fields
	 */
	getTreeFields : function() {
	    return [ {
	        name : 'id',
	        mapping : 'id',
	        type : 'string'
	    }, {
	        name : 'text',
	        mapping : 'text',
	        type : 'string'
	    }, {
	        name : 'readOnly',
	        mapping : 'readOnly',
	        type : 'boolean'
	    }, {
	        name : 'modifiable',
	        mapping : 'modifiable',
	        type : 'boolean'
	    }, {
	        name : 'deletable',
	        mapping : 'deletable',
	        type : 'boolean'
	    }, {
	        name : 'canCopy',
	        mapping : 'canCopy',
	        type : 'boolean'
	    }, {
	        name : 'categoryType',
	        mapping : 'categoryType',
	        type : 'string'
	    }, {
	        name : 'treeFilter',
	        mapping : 'treeFilter',
	        type : 'boolean'
	    }, {
	        name : 'reportConfigNeeded',
	        mapping : 'reportConfigNeeded',
	        type : 'boolean'
	    }, {
	        name : 'leaf',
	        mapping : 'leaf',
	        type : 'boolean'
	    }, {
	        name : 'iconCls',
	        mapping : 'iconCls',
	        type : 'string'
	    }, {
	        name : 'canAddChild',
	        mapping : 'canAddChild',
	        type : 'boolean'
	    } ];
	},


	/**
	 * Get the items array to render for leaf detail based on the response JSON
	 * Should be implemented if showGridForLeaf is false
	 */
	getLeafDetailItems : function(node, add, responseJson) {
	    if (responseJson.success == true) {
		    var detailData = responseJson.errorMessage;
		    return [ Ext.create('Ext.Component', {
		        html : detailData,
		        cls : 'categoryConfigLeafDetail',
		        border : true
		    }) ];
	    }
	    return [];
	},

	getGridViewConfig: function() {
		return {
			forceFit: true,
			markDirty: false
		};
	},

	/**
	 * Gets the grid store's fields for the selected node
	 */
	getGridFields : function(node) {
	    if (this.isIssueFilter() && node.data['id'] != "issueFilter_3") {
		    // Fields for issue filters
		    return [ {
		        name : 'node',
		        type : 'string'
		    }, {
		        name : 'categoryType',
		        type : 'string'
		    }, {
		        name : 'text',
		        type : 'string'
		    }, {
		        name : 'typeLabel',
		        type : 'string'
		    }, {
		        name : 'styleFieldLabel',
		        type : 'string'
		    }, {
		        name : 'viewName',
		        type : 'string'
		    }, {
		        name : 'includeInMenu',
		        type : 'boolean'
		    }, {
		        name : 'treeFilter',
		        type : 'boolean'
		    }, {
		        name : 'modifiable',
		        type : 'boolean'
		    }, {
		        name : 'deletable',
		        type : 'boolean'
		    }, {
		        name : 'leaf',
		        type : 'boolean'
		    } ];
	    } else {
		    // Fields for notify filters or reports or (hardcoded) projects
		    return [ {
		        name : 'node',
		        type : 'string'
		    }, {
		        name : 'categoryType',
		        type : 'string'
		    }, {
		        name : 'text',
		        type : 'string'
		    }, {
		        name : 'typeLabel',
		        type : 'string'
		    }, {
		        name : 'modifiable',
		        type : 'boolean'
		    }, {
		        name : 'deletable',
		        type : 'boolean'
		    }, {
		        name : 'readOnly',
		        mapping : 'readOnly',
		        type : 'boolean'
		    }, {
		        name : 'reportConfigNeeded',
		        mapping : 'reportConfigNeeded',
		        type : 'boolean'
		    }, {
		        name : 'icon',
		        mapping : 'icon',
		        type : 'string'
		    }, {
		        name : 'leaf',
		        type : 'boolean'
		    } ];
	    }
	},

	/**
	 * Gets the grid columns for the selected node
	 */
	getGridColumns : function(node) {
	    // Common columns for notify filters/issue filters/reports/(hardcoded)
	    // projects/
	    var columns = [ {
	        header : getText('common.lbl.name'),
	        flex : 1,
	        dataIndex : 'text',
	        sortable : false,
	        hidden : false
	    }, {
	        header : getText('common.lbl.type'),
	        flex : 1,
	        dataIndex : 'typeLabel',
	        sortable : false,
	        hidden : false
	    } ];
	    if (this.isIssueFilter() && node.data['id'] != "issueFilter_3") {
		    // Extra columns for issue filters
		    columns.push({
		        header : getText('admin.customize.queryFilter.lbl.styleField'),
		        flex : 1,
		        dataIndex : 'styleFieldLabel',
		        sortable : false,
		        hidden : false
		    });
		    columns.push({
		        xtype : 'checkcolumn',
		        header : getText('admin.customize.queryFilter.lbl.menu'),
		        flex : 1,
		        dataIndex : 'includeInMenu',
		        sortable : false,
		        hidden : false,
		        listeners: {"checkchange": {fn: this.changeSubscribe, scope:this}}});
		    columns.push({
		        header : getText('admin.customize.queryFilter.lbl.view'),
		        flex : 1,
		        dataIndex : 'viewName',
		        sortable : false,
		        hidden : false
		    });
	    } else {
		    if (this.isReport()) {
			    // Extra columns for reports
			    /*
				 * columns.push({header:
				 * getText('admin.customize.reportTemplate.lbl.icon'), flex:1,
				 * dataIndex: 'icon', sortable: false, hidden:false,
				 * renderer:function (val) { return '<img src="' + val + '"/>';
				 * }});
				 */
		    }
	    }
	    return columns;
	},

	changeSubscribe: function(checkBox, rowIndex, checked, eOpts) {
		var record = this.grid.getStore().getAt(rowIndex);
		if (record!=null) {
			var params = {node:record.data["node"], includeInMenu:checked};
			Ext.Ajax.request({
				url: this.baseAction + "!changeSubscribe.action",
				params: params,
				scope: this,
				success: function(response) {
				},
				failure: function(response) {
					Ext.MessageBox.alert(this.failureTitle, response.responseText);
				}
			});
		}
	},

	postGridPanel : function(grid) {
	    if (this.isReport()) {
		    var view = grid.getView();
		    var tip = Ext.create('Ext.tip.ToolTip', {
		        target : view.el,
		        delegate : view.itemSelector,
		        trackMouse : true,
		        id : 'templatePreview',
		        renderTo : Ext.getBody(),
		        dismissDelay : 0,
		        listeners : {
			        beforeshow : function updateTipBody(tip) {
				        var appWidth = borderLayout.getWidth();
				        tip.maxWidth = 900;
				        if (appWidth < 850) {
					        tip.maxWidth = appWidth - 150;
				        }
				        var leaf = view.getRecord(tip.triggerElement).get('leaf');
				        if (leaf) {
					        var node = view.getRecord(tip.triggerElement).get('node');
					        var templateID = node.substring(node.lastIndexOf("_") + 1);
					        tip.removeAll();
					        var imgCmp = Ext.create('Ext.Img', {
					            src : 'reportDatasource!showPreviewImage.action?templateID=' + templateID,
					            alt : getText('admin.customize.reportTemplate.err.noPreviewAvailable'),
					            listeners : {
						            render : function(c) {
							            c.getEl().on('load', function() {
								            tip.doLayout();
							            });
						            }
					            }
					        });
					        tip.add(imgCmp);
				        } else {
					        // folder
					        tip.removeAll();
					        var text = view.getRecord(tip.triggerElement).get('text');
					        tip.add(Ext.create('Ext.Component', {
						        html : text
					        }));
				        }
			        }
		        }
		    });
	    }
	},

	/**
	 * Get the configuration for selection model
	 */
	getGridSelectionModel : function(node) {
	    if (this.isReport()) {
		    // report selection model (for export)
		    return Ext.create("Ext.selection.CheckboxModel", {
			    mode : "MULTI"
		    });
	    } else {
		    // filter selection model
		    return Ext.create("Ext.selection.RowModel", {
			    mode : "SINGLE"
		    });
	    }
	},

	getSelectedReportIDs : function() {
	    var selectedReportIDs = new Array();
	    var selectedRecordsArr = this.getSelection();
	    if (selectedRecordsArr != null) {
		    Ext.Array.forEach(selectedRecordsArr, function(record, index, allItems) {
			    var nodeID = record.data['node'];
			    var lastIndexOf = nodeID.lastIndexOf("_");
			    selectedReportIDs.push(nodeID.substring(lastIndexOf + 1));
		    }, this);
	    }
	    return selectedReportIDs.join(",");
	},

	enableDisableToolbarButtons : function(view, arrSelections) {
	    if (arrSelections == null || arrSelections.length == 0) {
		    this.actionDeleteGridRow.setDisabled(true);
		    this.actionEditGridRow.setDisabled(true);
		    if (this.isIssueFilter() || this.isReport()) {
			    this.actionExecuteGridRow.setDisabled(true);
			    if (this.isIssueFilter()) {
				    this.actionLinkGridRow.setDisabled(true);
			    } else {
				    this.actionDownloadGridRow.setDisabled(true);
				    // this.actionExport.setDisabled(true);
			    }
		    }
	    } else {
		    if (arrSelections.length == 1) {
			    var selectedRecord = arrSelections[0];
			    var isLeaf = selectedRecord.data.leaf;
			    var modifiable = selectedRecord.data.modifiable;
			    if (this.isIssueFilter() && isLeaf) {
				    // for issue filter at least the include in menu and style
				    // field should be editable
				    this.actionEditGridRow.setDisabled(false);
			    } else {
				    this.actionEditGridRow.setDisabled(!modifiable);
			    }
			    if (this.isIssueFilter() || this.isReport()) {
				    this.actionExecuteGridRow.setDisabled(!isLeaf);
				    if (this.isIssueFilter()) {
					    this.actionLinkGridRow.setDisabled(!isLeaf);
				    } else {
					    this.actionDownloadGridRow.setDisabled(!isLeaf);
				    }
			    }
		    } else {
			    // more than one selection
			    this.actionEditGridRow.setDisabled(true);
			    if (this.isIssueFilter() || this.isReport()) {
				    this.actionExecuteGridRow.setDisabled(true);
				    if (this.isIssueFilter()) {
					    this.actionLinkGridRow.setDisabled(true);
				    } else {
					    this.actionDownloadGridRow.setDisabled(true);
				    }
			    }
		    }
		    var allIsDeletable = true;
		    for (var i = 0; i < arrSelections.length; i++) {
			    var selectedRecord = arrSelections[i];
			    var deletable = selectedRecord.data.deletable;
			    if (!deletable) {
				    allIsDeletable = false;
			    }
		    }
		    this.actionDeleteGridRow.setDisabled(!allIsDeletable);
	    }
	},

	/**
	 * Prepare adding/editing a report or filter category
	 */
	getEditFolderPanelItems : function(data, add, fromTree, operation) {
	    var modifiable = false;
	    if (add) {
		    modifiable = true;
	    } else {
		    modifiable = data['modifiable'];
	    }
	    return [ CWHF.createTextField('common.lbl.name', "label", {
	        disabled : !modifiable,
	        allowBlank : false,
	        labelWidth : this.labelWidth
	    }) ];
	},

	/**
	 * Prepare adding/editing a report
	 */
	getEditLeafPanelItems : function(data, add, fromTree, operation) {
	    var modifiable = false;
	    if (add) {
		    modifiable = true;
	    } else {
		    modifiable = (data == null || data["modifiable"]);
	    }
	    if (this.isIssueFilter()) {
		    var instant = false;
		    /*
			 * if (operation=="instant") { instant = true; }
			 */
		    if (add || data == null || data["treeFilter"] == null || data["treeFilter"]) {
			    return com.trackplus.admin.Filter.getTreeFilterItems(modifiable, instant);
		    } else {
			    return com.trackplus.admin.Filter.getTQLFilterItems(modifiable);
		    }
	    } else {
		    if (this.isNotifyFilter()) {
			    return com.trackplus.admin.Filter.getNotifyFilterItems(modifiable);
		    } else {
			    return this.getReportItems(modifiable, add);
		    }
	    }

	},

	/**
	 * Prepare adding/editing a report
	 */
	getReportItems : function(modifiable, add) {
	    var windowItems = [ CWHF.createTextField('common.lbl.name', "label", {
	        disabled : !modifiable,
	        allowBlank : false,
	        labelWidth : this.labelWidth
	    }) ];
	    if (add) {
		    windowItems.push(CWHF.createFileField('admin.customize.reportTemplate.lbl.reportFile', "reportFile", {
		        disabled : !modifiable,
		        allowBlank : false,
		        labelWidth : this.labelWidth
		    }));
	    }
	    return windowItems;
	},

	/**
	 * Method to process the data to be loaded arrived from the server by
	 * editing the leaf
	 */
	getEditLeafPostDataProcess : function(recordData, add) {
	    if (this.isIssueFilter()) {
		    if (recordData == null || add || recordData['treeFilter']) {
			    return com.trackplus.admin.Filter.postLoadProcessTreeFilter;
		    } else {
			    return com.trackplus.admin.Filter.postLoadProcessTQLFilter;
		    }
	    } else {
		    if (this.isNotifyFilter()) {
			    return com.trackplus.admin.Filter.postLoadProcessNotifyFilter;
		    }
	    }
	},

	/**
	 * Method to add extra request parameters be sent to the sever before
	 * submitting the leaf data
	 */
	getEditLeafPreSubmitProcess : function(recordData, add) {
	    // is not TQL filter
	    if (this.isIssueFilter() && (recordData == null || recordData['treeFilter'] || add)) {
		    return com.trackplus.admin.Filter.preSubmitProcessIssueFilter;
	    } else {
		    if (this.isNotifyFilter()) {
			    return com.trackplus.admin.Filter.preSubmitProcessNotifyFilter;
		    } else {
			    return null;
		    }
	    }
	},

	/**
	 * Add extra window configuration fields by add/edit windowConfiguration
	 * argument is configured with the required fields but any already specified
	 * windowConfig field can be overridden, and further optional window options
	 * can be specified type: typically "folder" or "leaf" but for more leaf
	 * types it can be customized
	 */
	getExtraWindowParameters : function(recordData, operation) {
	    if (this.isReport() && operation == "addLeaf") {
		    // only by add (by edit no upload)
		    return {
		        fileUpload : true,
		        windowConfig : {
		            minWidth : 450,
		            minHeight : 150
		        },
		        panelConfig : {
			        autoScroll : false
		        }
		    };
	    }
	    return null;
	},

	/**
	 * Handler for adding an instant issue filter
	 */
	onInstantFilter : function() {
	    this.instant = true;
	    this.indexMax = 0;
	    com.trackplus.admin.CategoryConfig.showInstant(this, this.baseAction + '!edit.action');
	},

	/**
	 * Execute a leaf node
	 */
	onExecuteTreeNode : function() {
	    this.onExecute(true);
	},

	/**
	 * Execute a grid row
	 */
	onExecuteGridRow : function() {
	    this.onExecute(false);
	},

	/**
	 * Execute a tree node or a grid row
	 */
	onExecute : function(fromTree) {
	    var recordData = this.getSingleSelectedRecordData(fromTree);
	    if (recordData != null) {
		    var leaf = this.selectedIsLeaf(fromTree);
		    var node = this.getRecordID(recordData, {
			    fromTree : fromTree
		    });
		    if (leaf) {
			    var lastIndex = node.lastIndexOf("_");
			    var objectID = node.substring(lastIndex + 1);
			    if (this.isIssueFilter()) {
				    com.trackplus.admin.Filter.executeFilter(this, objectID);
			    } else {
				    com.trackplus.admin.Report.executeReport(this, objectID, recordData["reportConfigNeeded"], false);
			    }
		    }
	    }
	},

	/**
	 * Generate a permanent link for an issue filter tree node
	 */
	onLinkTreeNode : function() {
	    this.onLink(true);
	},

	/**
	 * Generate a permanent link for an issue filter grid row
	 */
	onLinkGridRow : function() {
	    this.onLink(false);
	},

	/**
	 * Generate permanent link for issue filter
	 */
	onLink : function(fromTree) {
	    var recordData = this.getSingleSelectedRecordData(fromTree);
	    if (recordData != null) {
		    var leaf = this.selectedIsLeaf(fromTree);
		    if (leaf && this.isIssueFilter()) {
			    var node = this.getRecordID(recordData, {
				    fromTree : fromTree
			    });
			    com.trackplus.admin.Filter.generateFilterLink(this, node);
		    }
	    }
	},

	/**
	 * Download the report for a tree node
	 */
	onDownloadTreeNode : function() {
	    this.downloadReport(true);
	},

	/**
	 * Download the report for the grid row
	 */
	onDownloadGridRow : function() {
	    this.downloadReport(false);
	},

	/**
	 * Downloads a report zip
	 */
	downloadReport : function(fromTree) {
	    var recordData = this.getSingleSelectedRecordData(fromTree);
	    if (recordData != null) {
		    var leaf = this.selectedIsLeaf(fromTree);
		    if (leaf && this.isReport()) {
			    var node = this.getRecordID(recordData, {
				    fromTree : fromTree
			    });
			    attachmentURI = this.baseAction + '!download.action?node=' + node;
			    window.open(attachmentURI);
		    }
	    }
	},

	/**
	 * Add a leaf: filter or report
	 */
	onAddLeaf : function() {
	    if (this.isIssueFilter() || this.isNotifyFilter()) {
		    this.indexMax = 0;
		    if (this.isIssueFilter()) {
			    this.instant = false;
		    }
	    }
	    this.callParent(arguments);
	},

	/**
	 * Additional execute method for issue filter
	 */
	getAdditionalActions : function(recordData, submitParams, operation, items) {
	    // only for "edit", not for "add" or "instant"
	    if (this.isIssueFilter() && recordData != null && recordData.leaf) {
		    var actions = [];
		    if (operation == "edit") {
			    actions.push({
			        submitUrl : 'savedFilterExecute!unwrappedContainsParameter.action',
			        submitUrlParams : submitParams,
			        submitButtonText : getText(this.btnExecute),
			        refreshAfterSubmitHandler : this.executeUnwrappedFilter
			    });
		    }
		    var modifiable = recordData["modifiable"];
		    if (modifiable) {
			    if (submitParams == null) {
				    submitParams = new Object();
			    }
			    submitParams["clearFilter"] = true;
			    actions.push({
			        submitUrl : 'filterConfig!edit.action',
			        submitUrlParams : submitParams,
			        submitButtonText : getText("common.btn.reset"),
			        closeAfterSubmit : false,
			        refreshParametersBeforeSubmit : {
				        items : items
			        },
			        refreshAfterSubmitHandler : com.trackplus.admin.Filter.clearFilter
			    });
		    }
		    return actions;
	    }
	    return null;
	},

	/**
	 * Whether the filter contains parameter(s)
	 */
	executeUnwrappedFilter : function(refreshParameters, result) {
	    containsParameter = result.value;
	    if (containsParameter) {
		    this.renderFilterParameter();
	    } else {
		    window.location.href = com.trackplus.TrackplusConfig.contextPath + "/itemNavigator.action";
	    }
	},

	/**
	 * Render the filter parameters
	 */
	renderFilterParameter : function() {
	    var title = getText('admin.customize.queryFilter.lbl.parameters');
	    var width = 1000;
	    var height = 800;
	    var windowItems = com.trackplus.admin.Filter.getTreeFilterParameterItems();
	    var loadUrl = 'filterParameters!renderParameters.action';
	    var submitUrl = 'savedFilterExecute!replaceSubmittedParameters.action';
	    var windowConfig = Ext.create('com.trackplus.util.WindowConfig', {
		    postDataProcess : com.trackplus.admin.Filter.postLoadProcessTreeFilterParameters
	    });
	    windowConfig.showWindow(this, title, width, height, {
		    loadUrl : loadUrl
	    }, {
	        submitUrl : submitUrl,
	        submitButtonText : getText(this.btnExecute),
	        standardSubmit : true
	    }, windowItems);
	},

	/**
	 * Add a leaf: filter or report
	 */
	onEditGridRow : function() {
	    if (this.isIssueFilter() || this.isNotifyFilter()) {
		    this.indexMax = 0;
		    if (this.isIssueFilter()) {
			    this.instant = false;
		    }
	    }
	    this.callParent(arguments);
	},

	/**
	 * Add a leaf: filter or report
	 */
	onEditTreeNode : function() {
	    if (this.isIssueFilter() || this.isNotifyFilter()) {
		    this.indexMax = 0;
		    if (this.isIssueFilter()) {
			    this.instant = false;
		    }
	    }
	    this.callParent(arguments);
	},

	/**
	 * The struts action for delete/replace: deleting of both leafs and folders
	 * are made in the folderAction
	 */
	getDeleteUrlBase : function(extraConfig) {
	    return this.folderAction;
	},

	/**
	 * The replacement items for the deleted entity
	 */
	getReplacementItems : function(responseJson, selectedRecords, extraConfig) {
	    // var excludePrivate = selectedRecords.data["repository"];
	    /*
		 * var nodeIDField = "node"; var fromTree = true; if (extraConfig!=null &&
		 * extraConfig.fromTree) { nodeIDField = "id"; } var nodeID =
		 * selectedRecords.data[nodeIDField]; var excludePrivate = false; var
		 * exceptID = null; if (nodeID!=null) { var parts = nodeID.split("_");
		 * if (parts!=null) { if (parts.length>1) { var repository = parts[1];
		 * if (repository!="1") { //replacing a non-private only with a
		 * non-private excludePrivate = true; } } if (parts.length>3) { exceptID =
		 * parts[3]; } } }
		 */
	    return [ {
	        xtype : 'label',
	        itemId : 'replacementWarning'
	    }, CWHF.createSingleTreePicker("Replacement", "replacementID", [], null, {
	        allowBlank : false,
	        blankText : getText('common.err.replacementRequired', this.getEntityLabel(extraConfig)),
	        labelWidth : 200,
	        margin : '5 0 0 0'
	    }) ];
	},

	/**
	 * Load the data source and value for the replacement options combo Override
	 * this for different tree based pickers
	 */
	loadReplacementOptionData : function(replacementControl, data) {
	    replacementControl.setData(data["replacementTree"]);
	},

	/**
	 * panel for importing the e-mail templates
	 */
	getImportPanel : function() {
	    this.formPanel = new Ext.form.FormPanel({
	        border : false,
	        bodyStyle : 'padding:5px',
	        defaults : {
	            labelStyle : 'overflow: hidden;',
	            margin : "5 5 0 0",
	            msgTarget : 'side',
	            anchor : '-20'
	        },
	        method : 'POST',
	        fileUpload : true,
	        items : [ CWHF.createCheckbox('common.lbl.overwriteExisting', 'overwriteExisting', {
		        labelWidth : 200
	        }), CWHF.createFileField('admin.customize.reportTemplate.lbl.reportFile', 'uploadFile', {
	            allowBlank : false,
	            labelWidth : 200
	        }) ]
	    });
	    return this.formPanel;
	},

	/*
	 * onImport: function() { var submit = [{submitUrl:this.baseAction +
	 * "!importReports.action", submitButtonText:getText('common.btn.upload'),
	 * validateHandler: Upload.validateUpload, expectedFileType: /^.*\.(zip)$/,
	 * refreshAfterSubmitHandler:this.reload}]; var title =
	 * getText('common.lbl.upload',
	 * getText('admin.customize.reportTemplate.lbl.reportFile')); var
	 * windowParameters = {title:title, width:500, height:175, submit:submit,
	 * formPanel: this.getImportPanel(), cancelButtonText:
	 * getText('common.btn.done')}; var windowConfig =
	 * Ext.create('com.trackplus.util.WindowConfig', windowParameters);
	 * windowConfig.showWindowByConfig(this); },
	 *
	 * onExport: function() { attachmentURI = this.baseAction +
	 * '!exportReports.action?selectedReportTemplateIDs='+this.getSelectedReportIDs();
	 * window.open(attachmentURI); },
	 */

	/**
	 * Get the actions available in context menu depending on the currently
	 * selected row
	 *
	 */
	getTreeContextMenuActions : function(selectedRecord, selectionIsSimple) {
	    var modifiable = selectedRecord.data['modifiable'];
	    var deletable = selectedRecord.data['deletable'];
	    var canAddChild = selectedRecord.data['canAddChild'];
	    var canCopy = selectedRecord.data['canCopy'];
	    var leaf = selectedRecord.data['leaf'];
	    var actions = [];
	    if (selectionIsSimple) {
		    if (canAddChild) {
			    if (!leaf) {
				    actions.push(this.actionAddFolder);
			    }
			    actions.push(this.actionAddLeaf);
		    }
		    if (modifiable || (leaf && this.isIssueFilter())) {
			    actions.push(this.actionEditTreeNode);
		    }
		    if (leaf && (this.isIssueFilter() || this.isReport())) {
			    actions.push(this.actionExecuteTreeNode);
			    if (this.isReport()) {
				    actions.push(this.actionDownloadTreeNode);
			    } else {
				    if (this.isIssueFilter()) {
					    actions.push(this.actionLinkTreeNode);
				    }
			    }
		    }
	    } else {
		    actions = [];
	    }
	    if (this.useCopyPaste) {
		    if (canCopy) {
			    if (modifiable) {
				    actions.push(this.actionCutTreeNode);
			    }
			    actions.push(this.actionCopyTreeNode);
		    }
		    if (canAddChild && this.cutCopyNode != null) {
			    actions.push(this.actionPasteTreeNode);
		    }
	    }
	    if (deletable) {
		    actions.push(this.actionDeleteTreeNode);
	    }
	    return actions;
	},

	/**
	 * Get the actions available in context menu depending on the currently
	 * selected row
	 *
	 */
	getGridContextMenuActions : function(selectedRecord, selectionIsSimple) {
	    var modifiable = selectedRecord.data['modifiable'];
	    var leaf = selectedRecord.data['leaf'];
	    var actions = [];
	    if (leaf) {
		    // leaf
		    if (modifiable || this.isIssueFilter()) {
			    actions.push(this.actionEditGridRow);
		    }
		    if (this.isIssueFilter() || this.isReport()) {
			    actions.push(this.actionExecuteGridRow);
			    if (this.isReport()) {
				    actions.push(this.actionDownloadGridRow);
			    } else {
				    if (this.isIssueFilter()) {
					    actions.push(this.actionEditGridRow);
					    actions.push(this.actionLinkGridRow);
				    }
			    }
		    }
	    } else {
		    // branch
		    if (modifiable) {
			    actions.push(this.actionEditGridRow);
		    }
	    }
	    /*
		 * if (modifiable) { if (selectionIsSimple) {
		 * actions.push(this.actionEditGridRow); } if (leaf &&
		 * (this.isIssueFilter() || this.isReport())) {
		 * actions.push(this.actionExecuteGridRow); if (this.isReport()) {
		 * actions.push(this.actionDownloadGridRow); } else { if
		 * (this.isIssueFilter()) { actions.push(this.actionLinkGridRow); } } }
		 * actions.push(this.actionDeleteGridRow); }
		 */
	    return actions;
	},

	/**
	 * Initialize all actions and return the toolbar actions
	 */
	getToolbarActions : function() {
	    if (this.isIssueFilter()) {
		    return this.getIssueFilterToolbarItems();
	    }
	    if (this.isNotifyFilter()) {
		    return this.getNotifyFilterToolbarItems();
	    }
	    if (this.isReport()) {
		    return this.getReportToolbarItems();
	    }
	},

	/**
	 * Toolbar items for issue filter
	 */
	getIssueFilterToolbarItems : function() {
	    if (this.projectID == null) {
		    return [ this.actionInstantFilter, this.actionAddFolder, this.actionAddLeaf, this.actionEditGridRow,
		            this.actionExecuteGridRow, this.actionLinkGridRow, this.actionDeleteGridRow ];
	    } else {
		    return [ this.actionAddFolder, this.actionAddLeaf, this.actionEditGridRow, this.actionExecuteGridRow,
		            this.actionLinkGridRow, this.actionDeleteGridRow ];
	    }

	},

	/**
	 * Toolbar items for notify filter
	 */
	getNotifyFilterToolbarItems : function() {
	    return [ this.actionAddFolder, this.actionAddLeaf, this.actionEditGridRow, this.actionDeleteGridRow ];
	},

	/**
	 * Toolbar items for report
	 */
	getReportToolbarItems : function() {
	    return [ this.actionAddFolder, this.actionAddLeaf, this.actionEditGridRow, this.actionExecuteGridRow,
	            this.actionDownloadGridRow, this.actionDeleteGridRow /*
																		 * ,this.actionImport,
																		 * this.actionExport
																		 */];
	},
	/**
	 * The iconCls for the edit button, overwrites base class icon
	 */
	getAddFolderIssueFilterIconCls : function() {
	    return "categoryFilterAdd";
	},

	getAddIssueFilterIconCls : function() {
	    return "filterAdd";
	},

	getEditIssueFilterIconCls : function() {
	    return "filterEdit";
	},

	/**
	 * The iconCls for the edit button, overwrites base class icon
	 */
	getAddFolderNotifyFilterIconCls : function() {
	    return "categoryFilterAdd";
	},

	getAddNotifyFilterIconCls : function() {
	    return "filterAdd";
	},

	getEditNotifyFilterIconCls : function() {
	    return "filterEdit";
	},

	getPasteIconCls : function() {
	    return "filterPaste";
	},

	getAddFolderReportIconCls : function() {
	    return "categoryReportAdd";
	},

	getAddReportIconCls : function() {
	    return "rtemplateAdd";
	},

	getEditReportIconCls : function() {
	    return "rtemplateEdit";
	},

	/**
	 * The iconCls for the add button, overwrites base class icon
	 */
	getAddIconCls : function() {
	    return 'rtemplateAdd';
	}
});
