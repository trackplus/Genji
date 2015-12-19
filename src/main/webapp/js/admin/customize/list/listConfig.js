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

Ext.define("com.trackplus.admin.customize.list.ListConfig", {
	extend:"com.trackplus.admin.TreeWithGrid",
	xtype: "listConfig",
    controller: "listConfig",
	config : {
        rootID : '_',
        fromProjectConfig : false
    },
    treeFields: [{name : "id", mapping : "id", type : "string"},
                 {name : "text", mapping : "text", type : "string"},
                 {name : "topParentList", mapping : "topParentList", type : "int"},
                 {name : "type", mapping : "type", type : "int"},
                 {name : "childListID", mapping : "childListID", type : "int"},
                 {name : "optionID", mapping : "optionID", type : "int"},
                 {name : "canEdit", mapping : "canEdit", type : "boolean"},
                 {name : "mightEditChild", mapping : "mightEditChild", type : "boolean"},
                 {name : "canDelete", mapping : "canDelete", type : "boolean"},
                 {name : "mightDeleteChild", mapping : "mightDeleteChild", type : "boolean"},
                 {name : "nodeListForLabel", mapping : "nodeListForLabel", type : "int"},
                 {name : "canAddChild", mapping : "canAddChild", type : "boolean"},
                 {name : "canCopy", mapping : "canCopy", type : "boolean"},
                 {name : "mightCopyChild", mapping : "mightCopyChild", type : "boolean"},
                 {name : "nodeChildrenListForLabel", mapping : "nodeChildrenListForLabel", type : "int"},
                 {name : "icon", mapping : "icon", type : "string"},
                 {name : "leaf", mapping : "leaf", type : "boolean"},
                 {name : "hasTypeflag", mapping : "hasTypeflag", type : "boolean"},
                 {name : "childrenHaveTypeflag", mapping : "childrenHaveTypeflag", type : "boolean"},
                 {name : "disableTypeflag", mapping : "disableTypeflag", type : "boolean"},
                 {name : "hasIcon", mapping : "hasIcon", type : "boolean"},
                 {name : "childrenHaveIcon", mapping : "childrenHaveIcon", type : "boolean"},
                 {name : "hasCssStyle", mapping : "hasCssStyle", type : "boolean"},
                 {name : "childrenHaveCssStyle", mapping : "childrenHaveCssStyle", type : "boolean"},
                 {name : "hasChildFilter", mapping : "hasChildFilter", type : "boolean"},
                 {name : "hasPercentComplete", mapping : "hasPercentComplete", type : "boolean"},
                 {name : "childrenHavePercentComplete", mapping : "childrenHavePercentComplete", type : "boolean"},
                 {name : "hasDefaultOption", mapping : "hasDefaultOption", type : "boolean"},
                 {name : "childrenHaveDefaultOption", mapping : "childrenHaveDefaultOption", type : "boolean"}],
    
    baseServerAction : "listOptions",
    dragAndDropOnTree : true,
    
    // actions
    actionChildIssueTypeAssignmentFromGrid : null,
    actionChildIssueTypeAssignmentFromTree : null,
    actionUploadFromGrid : null,
    actionUploadFromTree : null,
    actionDownloadFromGrid : null,
    actionDownloadFromTree : null,
    actionMoveUp : null,
    actionMoveDown : null,
    actionImport : null,
    actionExportFromGrid : null,
    actionExportFromTree : null,
    localizedLabels : null,
    uploadWindowWidth : 550,
    uploadWindowHeight : 180,

    initComponent : function() {
    	this.treeStoreUrl = "listOptions!expand.action";
		this.initLocalizedLabels();
		this.callParent();
	},

    initLocalizedLabels : function() {
        Ext.Ajax.request({
            //fromCenterPanel : true,
            url : 'listOptionsLocalize.action',
            scope : this,
            success : function(response) {
                var result = Ext.decode(response.responseText);
                this.localizedLabels = new Object();
                Ext.each(result.records, function(item) {
                    this.localizedLabels[item.id] = item.label;
                }, this);
            },
            failure : function(response) {
                Ext.MessageBox.alert(this.failureTitle, response.responseText);
            }
        });
    },

    /**
	 * The message to appear first time after selecting this
	 * menu entry Is should be shown by selecting the root but
	 * the root is typically not visible
	 */
    getRootMessage : function(rootID) {
        return getText("admin.customize.list.lbl.description");
    },

    /**
	 * Get the title of the window in context
	 */
    getEntityLabel : function(extraConfig) {
    	/*if (CWHF.isNull(extraConfig)) {
    		return "";
    	}*/
        var tooltipKey = extraConfig.tooltipKey; // see crudBase getTitle
        var listForLabel = null;
        var fromTree = false;
        if (extraConfig ) {
            fromTree = extraConfig.fromTree;
        }
        var selectedRecord = this.getLastSelected(fromTree);
        if (selectedRecord) {
            if (fromTree) {
                if (tooltipKey
                        && (tooltipKey === this.actionAdd.initialConfig.tooltipKey || tooltipKey === this.actionImport.initialConfig.tooltipKey)) {
                    // for add and childAssignment get the
					// label for children
                    listForLabel = selectedRecord.data["nodeChildrenListForLabel"];
                } else {
                    // for other operation get the label
					// corresponding to the node
                    listForLabel = selectedRecord.data["nodeListForLabel"];
                }
            } else {
                listForLabel = selectedRecord.data["rowListForLabel"];
            }
        } else {
            // probably root in projectConfig
            listForLabel = -21;// project specific list
        }
        if (listForLabel && this.localizedLabels) {
            return this.localizedLabels[listForLabel];
        }
        return "";
    },

    initActions: function() {
    	this.actionAdd = CWHF.createContextAction(this.getAddButtonKey(), this.getAddIconCls(),
				"onAdd", this.getAddTitleKey(), {/*disabled:true,*/ itemId:"add"});
        this.actionEditGridRow = CWHF.createContextAction(this.getEditButtonKey(), this.getEditIconCls(),
				"onEditGridRow", this.getEditTitleKey(), {disabled:true, itemId:"editGridRow"});
        this.actionEditTreeNode = CWHF.createContextAction(this.getEditButtonKey(), this.getEditIconCls(),
				"onEditTreeNode", this.getEditTitleKey(), {itemId:"editTreeNode"});
        this.actionCopyGridRow = CWHF.createContextAction(this.getCopyButtonKey(), this.getCopyIconCls(),
				"onCopyGridRow", this.getCopyTitleKey(), {disabled:true, itemId:"copyGridRow"});
        this.actionCopyTreeNode = CWHF.createContextAction(this.getCopyButtonKey(), this.getCopyIconCls(),
				"onCopyTreeNode", this.getCopyTitleKey(), {itemId:"copyTreeNode"});
        this.actionDeleteGridRow = CWHF.createContextAction(this.getDeleteButtonKey(), this.getDeleteIconCls(),
				"onDeleteFromGrid", this.getDeleteTitleKey(), {disabled:true, itemId:"deleteGridRow"});
        this.actionDeleteTreeNode = CWHF.createContextAction(this.getDeleteButtonKey(), this.getDeleteIconCls(),
				"onDeleteFromTree", this.getDeleteTitleKey(), {itemId:"deleteTreeNode"});
        this.actionChildIssueTypeAssignmentFromGrid = CWHF.createAction("admin.customize.list.button.filterByChildIssueType", "itemAction_addChild",
				"onChildIssueTypeAssignmentGrid", {tooltip:getText("admin.customize.list.button.filterByChildIssueType.tt"), disabled:true});
        this.actionChildIssueTypeAssignmentFromTree = CWHF.createAction("admin.customize.list.button.filterByChildIssueType", "itemAction_addChild",
				"onChildIssueTypeAssignmentTree", {tooltip:getText("admin.customize.list.button.filterByChildIssueType.tt")});
        this.actionUploadFromGrid = CWHF.createAction(getText(this.getUploadTitleKey(), getText("admin.customize.list.lbl.iconOp")), this.getUploadIconCls(),
                "onUploadFromGrid",  {labelIsLocalized:true, disabled:true});
        this.actionUploadFromTree = CWHF.createAction(getText(this.getUploadTitleKey(), getText("admin.customize.list.lbl.iconOp")), this.getUploadIconCls(),
                "onUploadFromTree", {labelIsLocalized:true});
        this.actionDownloadFromGrid = CWHF.createAction(getText(this.getDownloadTitleKey(), getText("admin.customize.list.lbl.iconOp")), this.getDownloadIconCls(),
                "onDownloadFromGrid", {labelIsLocalized:true, disabled:true});
        this.actionDownloadFromTree = CWHF.createAction(getText(this.getDownloadTitleKey(), getText("admin.customize.list.lbl.iconOp")), this.getDownloadIconCls(),
        		"onDownloadFromTree", {labelIsLocalized:true, disabled:true});
        this.actionMoveUp = CWHF.createContextAction(this.getMoveUpButtonKey(), this.getMoveUpIconCls(),
        		"onMoveUpGridRow", this.getMoveUpTitleKey(), {itemId:"moveUp", disabled:true});
        this.actionMoveDown = CWHF.createContextAction(this.getMoveDownButtonKey(), this.getMoveDownIconCls(),
        		"onMoveDownGridRow", this.getMoveDownTitleKey(), {itemId:"moveDown", disabled:true});
        this.actionImport = CWHF.createContextAction(this.getImportButtonKey(), this.getImportIconCls(),
        		"onImport", this.getImportTitleKey(), {itemId:"importList"});
        this.actionExportGridRow = CWHF.createContextAction(this.getExportButtonKey(), this.getExportIconCls(),
                "onExportGridRow", this.getExportTitleKey(), {itemId:"exportGridRow", disabled:true});
        this.actionExportTreeNode = CWHF.createContextAction(this.getExportButtonKey(), this.getExportIconCls(),
                "onExportTreeNode", this.getExportTitleKey(), {itemId:"exportTreeNode"});
      //at the very beginning no actions are possible: first select a node in the tree 
        this.actions = [this.actionAdd, this.actionCopyGridRow, this.actionEditGridRow, this.actionDeleteGridRow, this.actionImport, this.actionExportGridRow];
    },

    getTreeStoreExtraParams : function() {
        return {
        	//called from project configuration or globally
            fromProjectConfig : this.getFromProjectConfig()
        };
    },

    /**
	 * Automatically select the first node
	 */
	onTreeNodeLoad: function(treeStore, node) {
	    if (!this.getFromProjectConfig() && node.isRoot() && node.firstChild) {
	        this.tree.getSelectionModel().select(node.firstChild);
	        treeStore.load({
		        node : node.firstChild,
		        callback : function() {
			        node.firstChild.expand();
		        }
		    });
	    }
	},
    
    getGridViewConfig: function(node) {
		if (this.hasDragAndDropOnGrid(node)) {
			return {
				plugins: {
					ptype: "gridviewdragdrop",
					dragGroup: this.getBaseServerAction() + "gridDDGroup",
					dropGroup: this.getBaseServerAction() + "gridDDGroup",
					enableDrag: true,
					enableDrop: true
				},
				listeners: {
					drop: {scope:this, fn: function(node, data, dropRec, dropPosition) {
						this.onGridDrop(node, data, dropRec, dropPosition);
						}
					}
				},
				markDirty: false //for changing the default values for custom lists
			};
		}
		return null;
	},

    getListFields : function() {
        return [{name : "id", type : "int"},
                {name : "label", type : "string"},
                {name : "canEdit", type : "boolean"},
                {name : "canCopy", type : "boolean"},
                {name : "canDelete", type : "boolean"},
                {name : "rowListForLabel", type : "int"},
                {name : "iconCls", type : "string"},
                {name : "leaf", type : "boolean"},
                {name : "node", type : "string"}];
    },

    getListOptionFields : function() {
        return [{name : "id", type : "int"},
                {name : "label", type : "string"},
                {name : "canEdit", type : "boolean"},
                {name : "canDelete", type : "boolean"},
                {name : "rowListForLabel", type : "int"},
                {name : "hasTypeflag", type : "boolean"},
                {name : "disableTypeflag", type : "boolean"},
                {name : "typeflagLabel", type : "string"},
                {name : "hasIcon", type : "boolean"},
                {name : "iconName", type : "string"},
                {name : "icon", type : "string"},
                {name : "hasCssStyle", type : "boolean"},
                {name : "cssStyle", type : "string"},
                {name : "hasChildFilter", type : "boolean"},
                {name : "hasPercentComplete", type : "boolean"},
                {name : "percentComplete", type : "int", allowNull : true},
                {name : "hasDefaultOption", type : "boolean"},
                {name : "defaultOption", type : "boolean"},
                {name : "leaf", type : "boolean"},
                {name : "node", type : "string"}];
    },

    getListColumns: function() {
        return [{
        	text : getText("admin.customize.list.lbl.icon"),
            width : 60,
            dataIndex : "iconCls",
            sortable : false,
            hidden : false,
            renderer : function(value, metadata, record) {
                // metadata.tdCls = value;
                return '<div style="width:16px;height:16px" class="' + value + '">&nbsp;</div>';
            }
        }, {
        	text : getText("common.lbl.name"),
            flex : 1,
            dataIndex : "label",
            sortable : false,
            hidden : false
        } ];
    },

    getListOptionColumns : function(node) {
        var columnModelArr = [ {
        	text : getText("common.lbl.name"),
            flex : 1,
            dataIndex : "label",
            sortable : false,
            hidden : false
        } ];
        var hasIcon;
        var hasTypeflag;
        var hasCssStyle;
        var hasPercentComplete;
        var hasDefaultOption;
        if (node.isLeaf()) {
            hasIcon = node.data["hasIcon"];
            hasTypeflag = node.data["hasTypeflag"];
            hasCssStyle = node.data["hasCssStyle"];
            hasPercentComplete = node.data["hasPercentComplete"];
            hasDefaultOption = node.data["hasDefaultOption"];
        } else {
            hasIcon = node.data["childrenHaveIcon"];
            hasTypeflag = node.data["childrenHaveTypeflag"];
            hasCssStyle = node.data["childrenHaveCssStyle"];
            hasPercentComplete = node.data["childrenHavePercentComplete"];
            hasDefaultOption = node.data["childrenHaveDefaultOption"];
        }
        if (hasIcon) {
            columnModelArr.push({
            	text : getText("admin.customize.list.lbl.icon"),
                width : 60,
                dataIndex : "icon",
                sortable : false,
                hidden : false,
                renderer : function(val) {
                    if (val  && val !== "") {
                        return '<img src="' + val + '"/>';
                    }
                    return '';
                }
            });
            columnModelArr.push({
            	text : getText("admin.customize.list.lbl.iconName"),
                width : 100,
                dataIndex : "iconName",
                sortable : false,
                hidden : false
            });
        }
        if (hasCssStyle) {
            var colorRendererColumn = Ext.create("Ext.grid.column.Column", {
            	text : getText("admin.customize.list.lbl.style"),
                width : 100,
                dataIndex : "cssStyle",
                sortable : false,
                hidden : false
            });
            colorRendererColumn.renderer = function(value, metadata) {
                metadata.style = value;// 'background-color:#'+
										// value;
                return "Test text";
            };
            columnModelArr.push(colorRendererColumn);
        }
        if (hasPercentComplete) {
            columnModelArr.push({
            	text : getText("admin.customize.list.lbl.percentComplete"),
                width : 100,
                dataIndex : "percentComplete",
                sortable : false,
                hidden : false
            });
        }
        if (hasTypeflag) {
            columnModelArr.push({
            	text : getText("admin.customize.list.lbl.typeflag"),
                width : 150,
                dataIndex : "typeflagLabel",
                sortable : false,
                hidden : false
            });
        }
        if (hasDefaultOption) {
            columnModelArr.push({
                xtype : "checkcolumn",
                text : getText("admin.customize.list.lbl.defaultOption"),
                width : 100,
                dataIndex : "defaultOption",
                sortable : false,
                hidden : false,
                //disabled : true,
                listeners: {"checkchange": "changeDefault"}
            });
        }
        return columnModelArr;
    },


    /**
	 * Whether drag and drop on grid is possible
	 */
    hasDragAndDropOnGrid : function(node) {
        var childListID = node.data["childListID"];
        if (childListID  && childListID !== 0) {
            // custom or system options
            return true;
        }
    },

    getGridFields : function(node) {
        var childListID = node.data["childListID"];
        if (CWHF.isNull(childListID) || childListID === 0) {
            return this.getListFields();
        } else {
            // custom or system options
            return this.getListOptionFields();
        }
    },

    getGridColumns : function(node) {
        var childListID = node.data["childListID"];
        if (CWHF.isNull(childListID) || childListID === 0) {
            return this.getListColumns();
        } else {
            // custom or system options
            return this.getListOptionColumns(node);
        }
    },

    /**
	 * Return false if dragging this node is not allowed
	 */
    canDragDropNode : function(nodeToDrag, copy, overModel) {
        // drag a global or project specific custom list or
		// option within a custom cascading list
        var canDragDropOption = false;
        var draggedNodeTopList = nodeToDrag.data["topParentList"];
        var droppedToTopList = overModel.data["topParentList"];
        if (draggedNodeTopList  && droppedToTopList
                && draggedNodeTopList === droppedToTopList) {
            canDragDropOption = true;
        }
        var canDragList = nodeToDrag.data["nodeListForLabel"] === -20
                || nodeToDrag.data["nodeListForLabel"] === -21;
        if (!(canDragList || canDragDropOption)) {
            return false;
        }
        // move a list to another place
        var canDropList = overModel.data["canAddChild"]
                && (overModel.data["nodeChildrenListForLabel"] === -20 || overModel.data["nodeChildrenListForLabel"] === -21);
        if (!(canDropList || canDragDropOption)) {
            return false;
        }
        return true;
    },

     /**
     * Get the actions available in context menu depending on the currently selected row
     *
     */
    getTreeContextMenuActions : function(selectedRecord, selectionIsSimple) {
        var canEdit = selectedRecord.data["canEdit"];
        var canDelete = selectedRecord.data["canDelete"];
        var canAddChild = selectedRecord.data["canAddChild"];
        var canCopy = selectedRecord.data["canCopy"];
        var hasIcon = selectedRecord.data["hasIcon"];
        var hasChildFilter = selectedRecord.data["hasChildFilter"];
        var actions = [];
        if (canAddChild) {
            actions.push(this.actionAdd);
            var childListID = selectedRecord.data["childListID"];
            if (CWHF.isNull(childListID) || childListID === 0) {
                actions.push(this.actionImport);
            }
        }
        if (canEdit) {
            actions.push(this.actionEditTreeNode);
        }
        if (hasChildFilter && selectedRecord.isLeaf()) {
            actions.push(this.actionChildIssueTypeAssignmentFromTree);
        }
        if (canDelete) {
            actions.push(this.actionDeleteTreeNode);
        }
        if (canCopy) {
            actions.push(this.actionCopyTreeNode);
            actions.push(this.actionExportTreeNode);
        }
        if (hasIcon) {
            if (canEdit) {
                actions.push(this.actionUploadFromTree);
            }
            actions.push(this.actionDownloadFromTree);
        }
        return actions;
    },

    /**
     * Get the actions available in context menu depending on the currently selected row
     *
     */
    getGridContextMenuActions : function(selectedRecord, selectionIsSimple) {
        var canEdit = selectedRecord.data["canEdit"];
        var canDelete = selectedRecord.data["canDelete"];
        var canCopy = selectedRecord.data["canCopy"];
        var hasIcon = selectedRecord.data["hasIcon"];
        var listOption = selectedRecord.data["id"];
        var hasChildFilter = selectedRecord.data["hasChildFilter"];
        var actions = [];
        if (canEdit) {
            actions.push(this.actionEditGridRow);
        }
        if (hasChildFilter) {
            actions.push(this.actionChildIssueTypeAssignmentFromGrid);
        }
        if (canDelete) {
            actions.push(this.actionDeleteGridRow);
        }
        if (canCopy) {
            actions.push(this.actionCopyGridRow);
            actions.push(this.actionExportGridRow);
        }
        if (hasIcon ) {
            if (canEdit) {
                actions.push(this.actionUploadFromGrid);
            }
            if (selectedRecord.data["iconName"]  && selectedRecord.data["iconName"] !== "") {
                actions.push(this.actionDownloadFromGrid);
            }
        }
        if (listOption  && listOption !== 0 && canEdit) {
            //it is an option
            var store = this.grid.getStore();
            if (store ) {
                if (selectedRecord !== store.first()) {
                    actions.push(this.actionMoveUp);
                }
                if (selectedRecord !== store.last()) {
                    actions.push(this.actionMoveDown);
                }
            }
        }
        return actions;
    }

});
