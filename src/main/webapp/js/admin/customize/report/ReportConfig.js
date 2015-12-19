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


Ext.define("com.trackplus.admin.customize.report.ReportConfig", {
	extend: "com.trackplus.admin.customize.category.CategoryBase",
	xtype: "reportConfig",
    controller: "reportConfig",
	config : {
	    rootID : "",
	    // set for automail settings, filters, reports in project configuration
	    projectID : null,
	    // exclude private repository entities by default automail conditions
	    // (project specific or global)
	    excludePrivate : false
	},
	
	folderAction: "categoryConfig",
	baseServerAction: "reportConfig",
	
	// actions
	actionExecuteGridRow : null,
	actionExecuteTreeNode : null,
	actionDownloadGridRow : null,
	actionDownloadTreeNode : null,
	
	
	/**
	 * The message to appear first time after selecting this menu entry Is
	 * should be shown by selecting the root but the root is typically not
	 * visible
	 */
	getRootMessage : function(rootID) {
		return getText("admin.customize.reportTemplate.lbl.description");
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
	    	return getText("admin.customize.reportTemplate.lbl");
	    } else {
		    return getText("admin.customize.queryFilter.lbl.category");
	    }
	},

	/**
	 * The label for the save button
	 */
	/* protected */getSubmitButtonLabel : function(operation) {
	    if (operation === "instant") {
		    return getText(this.getApplyFilterButtonKey());
	    } else {
		    return getText(this.getSaveButtonKey());
	    }
	},


	initActions : function() {
	    var addFolderText = this.getActionTooltip(this.getAddTitleKey(), {isLeaf: false});
	    this.actionAddFolder = CWHF.createAction(addFolderText, "categoryReportAdd",
	    		"onAddFolder", {labelIsLocalized:true});
	    var addLeafText = this.getActionTooltip(this.getAddTitleKey(), {isLeaf: true});
	    this.actionAddLeaf = CWHF.createAction(addLeafText, "rtemplateAdd",
	    		"onAddLeaf", {labelIsLocalized:true, disabled:true});
	    this.actionEditGridRow = CWHF.createContextAction(this.getEditButtonKey(), "rtemplateEdit",
	    		"onEditGridRow", this.getEditTitleKey(), {itemId:"editGridRow", disabled:true});
	    this.actionEditTreeNode = CWHF.createContextAction(this.getEditButtonKey(), "rtemplateEdit",
	    		"onEditTreeNode", this.getEditTitleKey(), {itemId:"editTreeNode"});
	    this.actionExecuteGridRow = CWHF.createAction(this.getExecuteReportButtonKey(), this.getExecuteReportIconCls(),
	    		"onExecuteGridRow", {disabled:true});
    	this.actionExecuteTreeNode = CWHF.createAction(this.getExecuteReportButtonKey(), this.getExecuteReportIconCls(),
	    		"onExecuteTreeNode", {disabled:true});
    	this.actionDownloadGridRow = CWHF.createAction(this.getDownloadButtonKey(), this.getDownloadIconCls(),
	    		"onDownloadGridRow", {tooltip:this.getActionTooltip(this.getDownloadTitleKey(), {isLeaf:true}), disabled:true});
    	this.actionDownloadTreeNode = CWHF.createAction(this.getDownloadButtonKey(), this.getDownloadIconCls(),
	    		"onDownloadTreeNode", {tooltip:this.getActionTooltip(this.getDownloadTitleKey(), {isLeaf:true}), disabled:true});
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
    	this.actions = [this.actionAddFolder, this.actionAddLeaf, this.actionEditGridRow,
    		                this.actionExecuteGridRow, this.actionDownloadGridRow, this.actionDeleteGridRow];
    	
	},

	/**
	 * Which actions to enable/disable depending on tree selection
	 */
	getToolbarActionChangesForTreeNodeSelect : function(selectedNode) {
	    var canAddChild = false;
	    if (selectedNode) {
		    if (this.getProjectID()  && selectedNode.isRoot()) {
			    // after initializing the project specific branch for report:
			    // although in the report tree no node is selected, the
			    // add refers to the project specific root
			    canAddChild = true;
			} else {
			    canAddChild = selectedNode.data["canAddChild"];
		    }
	    }
	    this.actionAddFolder.setDisabled(!canAddChild);
	    this.actionAddLeaf.setDisabled(!canAddChild);
	    // nothing selected in the grid
	    this.actionEditGridRow.setDisabled(true);
	    this.actionDeleteGridRow.setDisabled(true);
        this.actionExecuteGridRow.setDisabled(true);
	    this.actionDownloadGridRow.setDisabled(true);
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
	    return fields;
	},

	/**
	 * Gets the grid columns for the selected node
	 */
	getGridColumns: function(node) {
	    return [{
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
	},

	/**
	 * Get the actions available in context menu depending on the currently
	 * selected node
	 */
	getTreeContextMenuActions : function(selectedRecord, selectionIsSimple) {
	    var modifiable = selectedRecord.data["modifiable"];
	    var deletable = selectedRecord.data["deletable"];
	    var canAddChild = selectedRecord.data["canAddChild"];
	    var canCopy = selectedRecord.data["canCopy"];
	    var leaf = selectedRecord.data["leaf"];
	    var actions = [];
	    if (selectionIsSimple) {
		    if (canAddChild) {
			    if (!leaf) {
				    actions.push(this.actionAddFolder);
			    }
			    actions.push(this.actionAddLeaf);
		    }
		    if (modifiable) {
			    actions.push(this.actionEditTreeNode);
		    }
		    if (leaf) {
			    actions.push(this.actionExecuteTreeNode);
			    actions.push(this.actionDownloadTreeNode);
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
	 */
	getGridContextMenuActions : function(selectedRecord, selectionIsSimple) {
		var modifiable = selectedRecord.get("modifiable");
	    var leaf = selectedRecord.get("leaf");
	    var actions = [];
	    if (leaf) {
		    if (modifiable) {
			    actions.push(this.actionEditGridRow);
		    }
	        actions.push(this.actionExecuteGridRow);
		    actions.push(this.actionDownloadGridRow);
	    } else {
		    // branch
		    if (modifiable) {
			    actions.push(this.actionEditGridRow);
		    }
	    }
	    return actions;
	},

	/**
	 * Get the configuration for selection model
	 */
	getGridSelectionModel : function(node) {
	   return Ext.create("Ext.selection.CheckboxModel", {mode : "MULTI"});
	},
	
	postGridPanel: function(grid) {
       var view = grid.getView();
	    var tip = Ext.create('Ext.tip.ToolTip', {
	        target : view.el,
	        delegate : view.itemSelector,
	        trackMouse : true,
	        //id : 'templatePreview',
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
							            tip.updateLayout();
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

});
