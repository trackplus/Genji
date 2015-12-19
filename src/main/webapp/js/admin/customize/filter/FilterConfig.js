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


Ext.define("com.trackplus.admin.customize.filter.FilterConfig", {
	extend: "com.trackplus.admin.customize.category.CategoryBase",
	xtype: "filterConfig",
    controller: "filterConfig",
	config : {
	    rootID : "",
	    // set for automail settings, filters, reports in project configuration
	    projectID : null,
	    // exclude private repository entities by default automail conditions
	    // (project specific or global)
	    excludePrivate : false
	},
	//used in FieldExpressionAction (not directly here)
	issueFilter : true,
	
	folderAction: "categoryConfig",
	baseServerAction: "filterConfig",
	
	/* filter specific fields */
	instant : false,
	// gather the rendered upper select fields
	//upperSelectFields : null,
	//indexMax : null,
	// actions
	actionInstantFilter : null,
	actionExecuteGridRow : null,
	actionExecuteTreeNode : null,
	actionLinkGridRow : null,
	actionLinkTreeNode : null,
	//show filter detail in textual form
	showGridForLeaf: false,
	
	/*statics : {
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
	},*/

	isIssueFilter : function() {
	    // this.rootID cold be a project specific branch node
	    return this.getRootID().indexOf("issueFilter") === 0;
	},

	isNotifyFilter : function() {
	    // this.rootID cold be a project specific branch node
	    return this.getRootID().indexOf("notifyFilter") === 0;
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
		    }
	    }
	},

	/**
	 * The localized entity name
	 */
	getEntityLabel: function(extraConfig) {
	    var entityLabel = null;
	    var isLeaf = true;
	    if (extraConfig ) {
		    isLeaf = extraConfig.isLeaf;
	    }
	    if (isLeaf) {
		    if (this.isIssueFilter()) {
			    return getText("admin.customize.queryFilter.lbl.issueFilter");
		    } else {
			    if (this.isNotifyFilter()) {
				    return getText("admin.customize.automail.filter.lblOperation");
			    }
		    }
	    } else {
		    return getText("admin.customize.queryFilter.lbl.category");
	    }
	},

	/**
	 * The label for the save button
	 */
	/* protected *//*getSubmitButtonLabel : function(operation) {
	    if (operation === "instant") {
		    return getText(this.getApplyFilterButtonKey());
	    } else {
		    return getText(this.getSaveButtonKey());
	    }
	},*/


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
		    }
	    }
	    var addFolderText = this.getActionTooltip(this.getAddTitleKey(), {isLeaf: false});
	    this.actionAddFolder = CWHF.createAction(addFolderText, addFolderIconCls,
	    		"onAddFolder", {labelIsLocalized:true});
	    var addLeafText = this.getActionTooltip(this.getAddTitleKey(), {isLeaf: true});
	    this.actionAddLeaf = CWHF.createAction(addLeafText, addLeafIconCls,
	    		"onAddLeaf", {labelIsLocalized:true, disabled:true});
	    this.actionEditGridRow = CWHF.createContextAction(this.getEditButtonKey(), editIconCls,
	    		"onEditGridRow", this.getEditTitleKey(), {itemId:"editGridRow", disabled:true});
	    this.actionEditTreeNode = CWHF.createContextAction(this.getEditButtonKey(), editIconCls,
	    		"onEditTreeNode", this.getEditTitleKey(), {itemId:"editTreeNode"});
	    if (this.isIssueFilter()) {
	    	this.actionExecuteGridRow = CWHF.createAction(this.getApplyFilterButtonKey(), this.getApplyFilterIconCls(),
		    		"onExecuteGridRow", {disabled:true});
	    	this.actionExecuteTreeNode = CWHF.createAction(this.getApplyFilterButtonKey(), this.getApplyFilterIconCls(),
		    		"onExecuteTreeNode", {disabled:true});
	    	this.actionInstantFilter = CWHF.createAction("menu.findItems.instantFilter", "filterInst",
		    		"onInstantFilter", {tooltip:getText("menu.findItems.newInstantFilter.tt")});
	    	this.actionLinkGridRow = CWHF.createAction("common.btn.link", "filterLink",
		    		"onLinkGridRow", {tooltip:getText("admin.customize.queryFilter.lbl.filterURL.report.encodedUrl"), disabled:true});
	    	this.actionLinkTreeNode = CWHF.createAction("common.btn.permURL", "filterLink",
		    		"onLinkTreeNode", {tooltip:getText("admin.customize.queryFilter.lbl.filterURL.report.encodedUrl"), disabled:true});
	    }
	    this.actionDeleteGridRow = CWHF.createContextAction(this.getDeleteButtonKey(), this.getDeleteIconCls(),
	    		"onDeleteFromGrid", this.getDeleteTitleKey(), {itemId:"deleteGridRow", disabled:true});
	    this.actionDeleteTreeNode = CWHF.createContextAction(this.getDeleteButtonKey(), this.getDeleteIconCls(),
	    		"onDeleteFromTree", this.getDeleteTitleKey(), {itemId:"deleteTreeNode"});
	    // cut/copy - paste and drag and drop
    	this.actionCutTreeNode = CWHF.createContextAction(this.getCutButtonKey(), this.getCutIconCls(),
	    		"onCutTreeNode", this.getCutTitleKey(), {itemId:"cut"});
    	this.actionCopyTreeNode = CWHF.createContextAction(this.getCopyButtonKey(), this.getCopyIconCls(),
	    		"onCopyTreeNode", this.getCopyTitleKey(), {itemId:"copy"});
    	this.actionPasteTreeNode = CWHF.createAction(this.getPasteButtonKey(), this.getPasteIconCls(),
	    		"onPasteTreeNode"/*, this.getPasteTitleKey(), {itemId:"paste"}*/);
    	if (this.isIssueFilter()) {
    		this.actions = [this.actionInstantFilter, this.actionAddFolder, this.actionAddLeaf, this.actionEditGridRow,
    		                this.actionExecuteGridRow, this.actionLinkGridRow, this.actionDeleteGridRow];
    	} else {
    		this.actions = [this.actionAddFolder, this.actionAddLeaf, this.actionEditGridRow, this.actionDeleteGridRow];
    	}
	},

	

	/**
	 * Which actions to enable/disable depending on tree selection
	 */
	getToolbarActionChangesForTreeNodeSelect : function(selectedNode) {
	    var canAddChild = false;
	    if (selectedNode) {
		    if (this.getProjectID()  && selectedNode.isRoot()) {
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
			    if (this.isIssueFilter()) {
				    canAddChild = true;
			    }
		    } else {
			    canAddChild = selectedNode.get("canAddChild");
		    }
	    }
	    this.actionAddFolder.setDisabled(!canAddChild);
	    this.actionAddLeaf.setDisabled(!canAddChild);
	    // nothing selected in the grid
	    this.actionEditGridRow.setDisabled(true);
	    this.actionDeleteGridRow.setDisabled(true);
	    if (this.isIssueFilter()) {
		    this.actionExecuteGridRow.setDisabled(true);
		    this.actionLinkGridRow.setDisabled(true);
	    }
	},

	getGridViewConfig: function() {
		return {
			forceFit: true,
			markDirty: false
		};
	},
	
	/**
	 * Gets the grid store"s fields for the selected node
	 */
	getGridFields: function(node) {
		var fields = [{name : "node", type : "string"},
		              {name : "categoryType", type : "string"},
			          {name : "text", type : "string"},
			          {name : "typeLabel", type : "string"},
			          {name : "modifiable", type : "boolean"},
			          {name : "deletable", type : "boolean"},
			          {name : "customFeature", type : "boolean"},
			          {name : "leaf", type : "boolean"}];
	    if (this.isIssueFilter() && node.data["id"] !== "issueFilter_3") {
		    // Fields for issue filters
	    	fields.push({name : "styleFieldLabel", type : "string"});
	    	fields.push({name : "viewName", type : "string"});
	    	fields.push({name : "includeInMenu", type : "boolean"});
	    } else {
		    // Fields for notify filters or (hardcoded) projects
	    	fields.push({name: "readOnly", type: "boolean"});
	    	fields.push({name: "icon", type: "string"});
	    }
	    return fields;
	},

	/**
	 * Gets the grid columns for the selected node
	 */
	getGridColumns: function(node) {
	    // Common columns for notify filters/issue filters/reports/(hardcoded)
	    // projects/
	    var columns = [{
	    	text : getText("common.lbl.name"),
	        flex : 1,
	        dataIndex : "text",
	        sortable : false,
	        hidden : false
	    }, {
	    	text : getText("common.lbl.type"),
	        flex : 1,
	        dataIndex : "typeLabel",
	        sortable : false,
	        hidden : false
	    }];
	    if (this.isIssueFilter() && node.data["id"] !== "issueFilter_3") {
		    // Extra columns for issue filters
		    columns.push({
		    	text : getText("admin.customize.queryFilter.lbl.styleField"),
		        flex : 1,
		        dataIndex : "styleFieldLabel",
		        sortable : false,
		        hidden : false
		    });
		    columns.push({
		        xtype : "checkcolumn",
		        text : getText("admin.customize.queryFilter.lbl.menu"),
		        flex : 1,
		        dataIndex : "includeInMenu",
		        sortable : false,
		        hidden : false,
		        listeners: {"checkchange": "changeSubscribe"}});
		    columns.push({
		    	text : getText("admin.customize.queryFilter.lbl.view"),
		        flex : 1,
		        dataIndex : "viewName",
		        sortable : false,
		        hidden : false
		    });
	    }
	    return columns;
	},

	/**
	 * Get the actions available in context menu depending on the currently
	 * selected row
	 *
	 */
	getTreeContextMenuActions: function(selectedRecord, selectionIsSimple) {
	    var modifiable = selectedRecord.get("modifiable");
	    var deletable = selectedRecord.get("deletable");
	    var canAddChild = selectedRecord.get("canAddChild");
	    var canCopy = selectedRecord.get("canCopy");
	    var leaf = selectedRecord.get("leaf");
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
		    if (leaf && (this.isIssueFilter())) {
			    actions.push(this.actionExecuteTreeNode);
			    actions.push(this.actionLinkTreeNode);
			}
	    } else {
		    actions = [];
	    }
	    if (canCopy) {
		    if (modifiable) {
			    actions.push(this.actionCutTreeNode);
		    }
		    actions.push(this.actionCopyTreeNode);
	    }
	    if (canAddChild && this.cutCopyNode) {
		    actions.push(this.actionPasteTreeNode);
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
	getGridContextMenuActions: function(selectedRecord, selectionIsSimple) {
	    var modifiable = selectedRecord.get("modifiable");
	    var leaf = selectedRecord.get("leaf");
	    var actions = [];
	    if (leaf) {
		    // leaf
		    if (modifiable || this.isIssueFilter()) {
			    actions.push(this.actionEditGridRow);
		    }
		    if (this.isIssueFilter()) {
			    actions.push(this.actionExecuteGridRow);
			    actions.push(this.actionLinkGridRow); 
		    }
	    } else {
		    // branch
		    if (modifiable) {
			    actions.push(this.actionEditGridRow);
		    }
	    }
	    return actions;
	},

	/**
	 * Initialize all actions and return the toolbar actions
	 */
	getToolbarActions: function() {
	    if (this.isIssueFilter()) {
		    return this.getIssueFilterToolbarItems();
	    }
	    if (this.isNotifyFilter()) {
		    return this.getNotifyFilterToolbarItems();
	    }
	},

	/**
	 * Toolbar items for issue filter
	 */
	getIssueFilterToolbarItems: function() {
	    if (CWHF.isNull(this.getProjectID())) {
		    return [this.actionInstantFilter, this.actionAddFolder, this.actionAddLeaf, this.actionEditGridRow,
		            this.actionExecuteGridRow, this.actionLinkGridRow, this.actionDeleteGridRow];
	    } else {
		    return [this.actionAddFolder, this.actionAddLeaf, this.actionEditGridRow, this.actionExecuteGridRow,
		            this.actionLinkGridRow, this.actionDeleteGridRow];
	    }
	},

	/**
	 * Toolbar items for notify filter
	 */
	getNotifyFilterToolbarItems: function() {
	    return [this.actionAddFolder, this.actionAddLeaf, this.actionEditGridRow, this.actionDeleteGridRow];
	},

	/**
	 * The iconCls for the edit button, overwrites base class icon
	 */
	getAddFolderIssueFilterIconCls: function() {
	    return "categoryFilterAdd";
	},

	getAddIssueFilterIconCls: function() {
	    return "filterAdd";
	},

	getEditIssueFilterIconCls: function() {
	    return "filterEdit";
	},

	/**
	 * The iconCls for the edit button, overwrites base class icon
	 */
	getAddFolderNotifyFilterIconCls: function() {
	    return "categoryFilterAdd";
	},

	getAddNotifyFilterIconCls: function() {
	    return "filterAdd";
	},

	getEditNotifyFilterIconCls: function() {
	    return "filterEdit";
	},

	getPasteIconCls: function() {
	    return "filterPaste";
	}

});
