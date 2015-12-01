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

Ext.define('com.trackplus.admin.customize.list.ListConfig',
	            {
	                extend : 'com.trackplus.admin.TreeWithGrid',
	                config : {
	                    rootID : '_',
	                    fromProjectConfig : false
	                },
	                confirmDeleteEntity : true,
	                baseAction : "listOptions",
	                entityID : 'node',
	                dragAndDropOnTree : true,
	                customListWindowWidth : 500,
	                customListWindowHeight : 300,

	                labelWidth : 180,
	                listOptionWindowWidth : 450,
	                listOptionWindowHeight : 390,

	                replaceToolbarOnTreeNodeSelect : true,

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

	                constructor : function(config) {
	                    var config = config || {};
						this.initConfig(config);
	                    this.initBase();
	                },

	                isProjectSpecificRoot : function(recordData) {
	                    return this.getFromProjectConfig() && recordData['id'] === this.getRootID();
	                },

	                /**
					 * Initialization method
					 */
	                initBase: function() {
	                    this.initLocalizedLabels();
	                },

	                initLocalizedLabels : function() {
	                    Ext.Ajax.request({
	                        fromCenterPanel : true,
	                        url : 'listOptionsLocalize.action',
	                        scope : this,
	                        success : function(response) {
		                        var result = Ext.decode(response.responseText);
		                        this.localizedLabels = new Object();
		                        Ext.each(result.records, function(item) {
			                        this.localizedLabels[item.id] = item.label;
		                        }, this);
		                        this.initActions();
	                        },
	                        failure : function(response) {
		                        Ext.MessageBox.alert(this.failureTitle, response.responseText);
	                        }
	                    });
	                },

	                getEditWidth : function(recordData, isLeaf, add, fromTree, operation) {
	                    if (this.isListOperation(recordData, isLeaf, add, fromTree, operation)) {
		                    return this.customListWindowWidth;
	                    } else {
		                    return this.listOptionWindowWidth;
	                    }
	                },

	                getEditHeight : function(recordData, isLeaf, add, fromTree, operation) {
	                    if (this.isListOperation(recordData, isLeaf, add, fromTree, operation)) {
		                    return this.customListWindowHeight;
	                    } else {
		                    return this.listOptionWindowHeight;
	                    }
	                },

	                /**
					 * Whether the operation refers to a list or a list option
					 */
	                isListOperation : function(recordData, isLeaf, add, fromTree, operation) {
	                    if (this.isProjectSpecificRoot(recordData)) {
		                    return true;
	                    }
	                    // in the tree either the global lists node or a project
						// node is selected
	                    var listParentNode = this.selectedNode.data['nodeChildrenListForLabel'] === -20
	                            || this.selectedNode.data['nodeChildrenListForLabel'] === -21;
	                    if ((listParentNode && (operation === "add" || ((operation === "edit" || operation === "copy") && !fromTree)))
	                            || // add from tree or edit or copy from grid
	                            ((operation === "edit" || operation === "copy") && fromTree && (CWHF.isNull(recordData["optionID"]) || recordData["optionID"] === 0))) {// a
																																								// custom
																																								// list
																																								// selected:edit
																																								// or
																																								// copy
																																								// from
																																								// tree
		                    return true;
	                    } else {
		                    return false;
	                    }
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
	                    var tooltipKey = extraConfig.tooltipKey; // see crudBase getTitle
	                    if (tooltipKey && ("common.lbl.upload" === tooltipKey || "common.lbl.download" === tooltipKey)) {
		                    return getText('admin.customize.list.lbl.iconOp');
	                    } else {
		                    var listForLabel = null;
		                    var fromTree = false;
		                    if (extraConfig ) {
			                    fromTree = extraConfig.fromTree;
		                    }
		                    var selectedRecord = this.getLastSelected(fromTree);
		                    if (selectedRecord ) {
			                    if (fromTree) {
				                    if (tooltipKey
				                            && (tooltipKey === this.actionAdd.initialConfig.tooltipKey || tooltipKey === this.actionImport.initialConfig.tooltipKey)) {
					                    // for add and childAssignment get the
										// label for children
					                    listForLabel = selectedRecord.data['nodeChildrenListForLabel'];
				                    } else {
					                    // for other operation get the label
										// corresponding to the node
					                    listForLabel = selectedRecord.data['nodeListForLabel'];
				                    }
			                    } else {
				                    listForLabel = selectedRecord.data['rowListForLabel'];
			                    }
		                    } else {
			                    // probably root in projectConfig
			                    listForLabel = -21;// project specific list
		                    }
		                    if (listForLabel ) {
			                    return this.localizedLabels[listForLabel];
		                    }
	                    }
	                    return "";
	                },

	                initActions : function() {
	                    this.actionAdd = this.createAction(this.getAddButtonKey(), this.getAddFolderIconCls(),
	                            this.onAdd, true, this.getAddTitleKey(), "add");
	                    this.actionEditGridRow = this.createAction(this.getEditButtonKey(), this.getEditIconCls(),
	                            this.onEditGridRow, true, this.getEditTitleKey(), "editGridRow");
	                    this.actionEditTreeNode = this.createAction(this.getEditButtonKey(), this.getEditIconCls(),
	                            this.onEditTreeNode, false, this.getEditTitleKey(), "editTreeNode");
	                    this.actionCopyGridRow = this.createAction(this.getCopyButtonKey(), this.getCopyIconCls(),
	                            this.onCopyGridRow, true, this.getCopyTitleKey(), "copyGridRow");
	                    this.actionCopyTreeNode = this.createAction(this.getCopyButtonKey(), this.getCopyIconCls(),
	                            this.onCopyTreeNode, false, this.getCopyTitleKey(), "copyTreeNode");
	                    this.actionDeleteGridRow = this.createAction(this.getDeleteButtonKey(),
	                            this.getDeleteIconCls(), this.onDeleteFromGrid, true, this.getDeleteTitleKey(),
	                            "deleteGridRow");
	                    this.actionDeleteTreeNode = this.createAction(this.getDeleteButtonKey(), this
	                            .getDeleteIconCls(), this.onDeleteFromTree, false, this.getDeleteTitleKey(),
	                            "deleteTreeNode");
	                    this.actionChildIssueTypeAssignmentFromGrid = this.createAction(
	                            "admin.customize.list.button.filterByChildIssueType", "itemAction_addChild",
	                            this.onChildIssueTypeAssignmentGrid, true,
	                            "admin.customize.list.button.filterByChildIssueType.tt");
	                    this.actionChildIssueTypeAssignmentFromTree = this.createAction(
	                            "admin.customize.list.button.filterByChildIssueType", "itemAction_addChild",
	                            this.onChildIssueTypeAssignmentTree, false,
	                            "admin.customize.list.button.filterByChildIssueType.tt");
	                    this.actionUploadFromGrid = this.createAction("common.btn.upload", "upload",
	                            this.createUploadFormFromGrid, false, "common.lbl.upload", "uploadIconGridRow");
	                    this.actionUploadFromTree = this.createAction("common.btn.upload", "upload",
	                            this.createUploadFormFromTree, true, "common.lbl.upload", "uploadIconTreeNode");
	                    this.actionDownloadFromGrid = this.createAction("common.btn.download", "download",
	                            this.downloadIconFromGrid, false, "common.lbl.download", "downloadIconGridRow");
	                    this.actionDownloadFromTree = this.createAction("common.btn.download", "download",
	                            this.downloadIconFromTree, true, "common.lbl.download", "downloadIconTreeNode");
	                    this.actionMoveUp = this.createAction("common.btn.up", "moveUp", this.onMoveUpGridRow, false,
	                            "common.lbl.up", "moveUp");
	                    this.actionMoveDown = this.createAction("common.btn.down", "moveDown", this.onMoveDownGridRow,
	                            false, "common.lbl.down", "moveDown");
	                    this.actionImport = this.createAction("common.btn.import", "import", this.onImport, false,
	                            "common.lbl.import", "importList");
	                    this.actionExportGridRow = this.createAction("common.btn.export", "export",
	                            this.onExportGridRow, true, "common.lbl.export", "exportGridRow");
	                    this.actionExportTreeNode = this.createAction("common.btn.export", "export",
	                            this.onExportTreeNode, false, "common.lbl.export", "exportTreeNode");

	                },

	                /**
					 * Get the toolbar actions. By default all actions, override
					 * if not all actions should appear in toolbar
					 */
	                getToolbarActions : function() {
	                    return [];
	                },

	                /**
					 * Initialize all actions and return the toolbar actions
					 * this time called also by treeNodeSelect
					 */
	                getToolbarActionsForTreeNodeSelect : function(node) {
	                    if (CWHF.isNull(node) /* || arrSelectedNodes.length===0 */) {
		                    // first load of the listConfig
		                    // return [this.actionAdd, this.actionEditGridRow,
							// this.actionDeleteGridRow];
		                    return [];
	                    } else {
		                    var actions = [];
		                    // after selecting a tree node
		                    var isLeaf = node.isLeaf();
		                    var mightEditChild = false;
		                    var mightDeleteChild = false;
		                    var canAddChild = false;
		                    var mightCopyChild = false;
		                    if (this.isProjectSpecificRoot(node.data)) {
			                    // after initializing the project specific
								// branch for project lists
			                    // although in the list tree no node is
								// selected, the add refers to the project
								// specific root
			                    // selectedNode.isRoot(): after deleting a list
								// (directly below root)
			                    canAddChild = true;
			                    mightEditChild = true;
			                    mightDeleteChild = true;
			                    mightCopyChild = true;
		                    } else {
			                    canAddChild = node.data["canAddChild"];
			                    mightEditChild = node.data["mightEditChild"]
			                            || (isLeaf && node.parentNode.data["mightEditChild"]);
			                    mightDeleteChild = node.data["mightDeleteChild"]
			                            || (isLeaf && node.parentNode.data["mightDeleteChild"]);
			                    mightCopyChild = node.data["mightCopyChild"];
		                    }
		                    var hasChildFilter = node.data["hasChildFilter"];
		                    var childrenHaveIcon = node.data["childrenHaveIcon"];
		                    if (canAddChild) {
			                    actions.push(this.actionAdd);
			                    this.actionAdd.setDisabled(false);
		                    }
		                    if (mightCopyChild) {
			                    // first unselected, select by grid row select
			                    actions.push(this.actionCopyGridRow);
			                    this.actionCopyGridRow.setDisabled(true);
		                    }
		                    if (mightEditChild) {
			                    actions.push(this.actionEditGridRow);
			                    this.actionEditGridRow.setDisabled(true);
		                    }
		                    if (mightDeleteChild) {
			                    actions.push(this.actionDeleteGridRow);
			                    this.actionDeleteGridRow.setDisabled(true);
		                    }
		                    var childListID = null;
		                    if (canAddChild) {
			                    childListID = node.data["childListID"];
			                    if (CWHF.isNull(childListID) || childListID === 0) {
				                    // global or project node
				                    actions.push(this.actionImport);
			                    }
		                    }
		                    if (mightCopyChild) {
			                    // first unselected, select by grid row select
			                    actions.push(this.actionExportGridRow);
			                    this.actionExportGridRow.setDisabled(true);
		                    }
		                    if (hasChildFilter) {
			                    // actions.push(this.actionChildAssignment);
			                    // this.actionChildAssignment.setDisabled(false);
			                    actions.push(this.actionChildIssueTypeAssignmentFromGrid);
			                    this.actionChildIssueTypeAssignmentFromGrid.setDisabled(true);
		                    }
		                    if (childrenHaveIcon) {
			                    if (mightEditChild) {
				                    actions.push(this.actionUploadFromGrid);
			                    }
			                    actions.push(this.actionDownloadFromGrid);
			                    // both disabled until selection
			                    this.actionUploadFromGrid.setDisabled(true);
			                    this.actionDownloadFromGrid.setDisabled(true);
		                    }
		                    if (childListID  && childListID !== 0 && mightEditChild) {
			                    actions.push(this.actionMoveUp);
			                    actions.push(this.actionMoveDown);
			                    // both disabled until selection
			                    this.actionMoveUp.setDisabled(true);
			                    this.actionMoveDown.setDisabled(true);
		                    }
		                    return actions;
	                    }
	                },

	                /**
					 * Gets the tree's fields
					 */
	                getTreeFields : function() {
	                    return [ {
	                        name : "id",
	                        mapping : "id",
	                        type : "string"
	                    }, {
	                        name : "text",
	                        mapping : "text",
	                        type : "string"
	                    }, {
	                        name : "topParentList",
	                        mapping : "topParentList",
	                        type : "int"
	                    }, {
	                        name : "type",
	                        mapping : "type",
	                        type : "int"
	                    }, {
	                        name : "childListID",
	                        mapping : "childListID",
	                        type : "int"
	                    }, {
	                        name : "optionID",
	                        mapping : "optionID",
	                        type : "int"
	                    }, {
	                        name : "canEdit",
	                        mapping : "canEdit",
	                        type : "boolean"
	                    }, {
	                        name : "mightEditChild",
	                        mapping : "mightEditChild",
	                        type : "boolean"
	                    }, {
	                        name : "canDelete",
	                        mapping : "canDelete",
	                        type : "boolean"
	                    }, {
	                        name : "mightDeleteChild",
	                        mapping : "mightDeleteChild",
	                        type : "boolean"
	                    }, {
	                        name : "nodeListForLabel",
	                        mapping : "nodeListForLabel",
	                        type : "int"
	                    }, {
	                        name : "canAddChild",
	                        mapping : "canAddChild",
	                        type : "boolean"
	                    }, {
	                        name : "canCopy",
	                        mapping : "canCopy",
	                        type : "boolean"
	                    }, {
	                        name : "mightCopyChild",
	                        mapping : "mightCopyChild",
	                        type : "boolean"
	                    }, {
	                        name : "nodeChildrenListForLabel",
	                        mapping : "nodeChildrenListForLabel",
	                        type : "int"
	                    }, {
	                        name : "icon",
	                        mapping : "icon",
	                        type : "string"
	                    }, {
	                        name : "leaf",
	                        mapping : "leaf",
	                        type : "boolean"
	                    }, {
	                        name : "hasTypeflag",
	                        mapping : "hasTypeflag",
	                        type : "boolean"
	                    }, {
	                        name : "childrenHaveTypeflag",
	                        mapping : "childrenHaveTypeflag",
	                        type : "boolean"
	                    }, {
	                        name : "disableTypeflag",
	                        mapping : "disableTypeflag",
	                        type : "boolean"
	                    }, {
	                        name : "hasIcon",
	                        mapping : "hasIcon",
	                        type : "boolean"
	                    }, {
	                        name : "childrenHaveIcon",
	                        mapping : "childrenHaveIcon",
	                        type : "boolean"
	                    }, {
	                        name : "hasCssStyle",
	                        mapping : "hasCssStyle",
	                        type : "boolean"
	                    }, {
	                        name : "childrenHaveCssStyle",
	                        mapping : "childrenHaveCssStyle",
	                        type : "boolean"
	                    }, {
	                        name : "hasChildFilter",
	                        mapping : "hasChildFilter",
	                        type : "boolean"
	                    }, {
	                        name : "hasPercentComplete",
	                        mapping : "hasPercentComplete",
	                        type : "boolean"
	                    }, {
	                        name : "childrenHavePercentComplete",
	                        mapping : "childrenHavePercentComplete",
	                        type : "boolean"
	                    }, {
	                        name : "hasDefaultOption",
	                        mapping : "hasDefaultOption",
	                        type : "boolean"
	                    }, {
	                        name : "childrenHaveDefaultOption",
	                        mapping : "childrenHaveDefaultOption",
	                        type : "boolean"
	                    } ];
	                },

	                getTreeExpandExtraParams : function() {
	                    return {
		                    fromProjectConfig : this.getFromProjectConfig()
	                    };
	                },

	                onChildIssueTypeAssignmentGrid : function() {
	                    var recordData = this.getSingleSelectedRecordData(false);
	                    if (recordData ) {
		                    this.onChildIssueTypeAssignment(recordData.node, recordData);
	                    }
	                },

	                onChildIssueTypeAssignmentTree : function() {
	                    this.onChildIssueTypeAssignment(this.selectedNodeID, this.selectedNode);
	                },

	                onChildIssueTypeAssignment : function(nodeID, record) {
	                    this.childIssueTypeAssignment = Ext.create("com.trackplus.admin.SimpleAssignment", {
	                        objectID : nodeID,
	                        objectIDParamName : "parentIssueTypeNodeID",
	                        dynamicIcons : true
	                    });
	                    this.childIssueTypeAssignment.baseAction = "childIssueTypeAssignments";
	                    var assignmentWin = Ext.create("Ext.window.Window", {
	                        height : 450,
	                        width : 500,
	                        title : getText("admin.customize.list.button.filterByChildIssueType"),
	                        autoScroll : true,
	                        border : false,
	                        buttons : [ {
	                            text : getText("common.btn.done"),
	                            handler : function() {
		                            assignmentWin.close();
	                            }
	                        } ],
	                        layout : "fit",
	                        items : [ this.childIssueTypeAssignment.getDetailPanel(record) ]
	                    }).show();
	                },

	                /** ****fileupload start***** */
	                downloadIconFromGrid : function() {
	                    var recordData = this.getSingleSelectedRecordData(false);
	                    if (recordData ) {
		                    this.downloadIcon(recordData.node);
	                    }
	                },

	                downloadIconFromTree : function() {
	                    this.downloadIcon(this.selectedNodeID);
	                },

	                downloadIcon : function(selectedNodeID) {
	                    attachmentURI = "listOptionIcon!download.action?node=" + selectedNodeID;
	                    window.open(attachmentURI);
	                },

	                createUploadFormFromGrid : function() {
	                    var record = this.getSingleSelectedRecord(false);
	                    if (record ) {
		                    this.createUploadForm(false, record);
	                    }
	                },

	                createUploadFormFromTree : function() {
	                    this.createUploadForm(true, this.selectedNode);
	                },

	                createUploadForm : function(fromTree, record) {
	                    var width = this.uploadWindowWidth;
	                    var height = this.uploadWindowHeight;
	                    var loadUrl = "listOptionIcon.action";
	                    var loadParams = this.getEditParams(fromTree);
	                    var load = {
	                        loadUrl : loadUrl,
	                        loadUrlParams : loadParams
	                    };
	                    var submitParams = this.getEditParams(fromTree);
	                    var submit = [ {
	                        submitUrl : "listOptionIcon!upload.action",
	                        submitUrlParams : submitParams,
							disabled:true,
	                        submitButtonText : getText("common.btn.upload"),
	                        submitHandler : this.uploadFileHandler
	                    }, {
	                        submitUrl : "listOptionIcon!delete.action",
	                        submitUrlParams : submitParams,
	                        submitButtonText : getText("common.btn.delete"),
	                        submitHandler : this.deleteUploadedFileHandler
	                    } ];
	                    var postDataProcess = this.renderUploadPostDataProcess;
	                    var title = getText("common.lbl.upload", getText("admin.customize.list.lbl.icon"));
	                    var windowParameters = {
	                        title : title,
	                        width : width,
	                        height : height,
	                        load : load,
	                        submit : submit,
	                        formPanel : this.getFormPanel(),
	                        postDataProcess : postDataProcess,
	                        cancelButtonText : getText("common.btn.done"),
	                        refreshAfterCancel : true,
	                        extraConfig : {
		                        fromTree : fromTree
	                        }
	                    };
	                    var windowConfig = Ext.create("com.trackplus.util.WindowConfig", windowParameters);
	                    windowConfig.showWindowByConfig(this);
	                },

	                /**
					 * Get the node to reload after upload operation
					 */
	                getReloadParamsAfterUpload : function(fromTree) {
	                    if (this.selectedNode ) {
		                    if (fromTree) {
			                    // edited/copied from tree
			                    var parentNode = this.selectedNode.parentNode;
			                    if (parentNode ) {
				                    // the parent of the edited node should be
									// reloaded
				                    return {
				                        nodeIDToReload : parentNode.data["id"],
				                        nodeIDToSelect : this.selectedNode.data["id"],
				                        rowToSelect : this.selectedNode.data["id"]
				                    };
			                    }
		                    } else {
			                    // edited from grid:
			                    var gridRow = this.getLastSelected(fromTree);
			                    if (this.getShowGridForLeaf() && this.selectedNode.isLeaf()) {
				                    // in the tree a leaf node selected -> grid
									// with a single row: the parent of the
									// selected tree node should be reloaded
				                    var parentNode = this.selectedNode.parentNode;
				                    if (parentNode ) {
					                    // the parent of the edited node should
										// be reloaded
					                    var reloadParams = {
					                        nodeIDToReload : parentNode.data["id"],
					                        nodeIDToSelect : this.selectedNode.data["id"]
					                    };
					                    if (gridRow ) {
						                    reloadParams["rowToSelect"] = gridRow.data["node"];
					                    }
					                    return reloadParams;
				                    }
			                    } else {
				                    // in the tree the parent of the edited grid
									// row is selected: the actually selected
									// tree node should be reloaded
				                    var reloadParams = {
					                    nodeIDToReload : this.selectedNode.data["id"]
				                    };
				                    if (gridRow ) {
					                    reloadParams["rowToSelect"] = gridRow.data["node"];
				                    }
				                    return reloadParams;
			                    }
		                    }
	                    }
	                    return null;
	                },

	                validateFileExtension : function(fileName) {
	                    var exp = /^.*\.(jpg|JPG|png|PNG|gif|GIF)$/;
	                    return exp.test(fileName);
	                },

	                getFormPanel : function(fromTree, selectedEntryID) {
	                    var icon = Ext.create("Ext.Img", {
	                        src : Ext.BLANK_IMAGE_URL,
	                        width : 16,
	                        height : 16,
	                        itemId : "actualIcon"
	                    });
	                    var iconWrapper = Ext.create("Ext.form.FieldContainer", {
	                        combineErrors : true,
	                        itemId : "iconPanel",
	                        fieldLabel : getText("admin.customize.list.lbl.icon"),
	                        labelWidth : this.labelWidth,
	                        labelAlign : "right",
	                        labelStyle : {
		                        overflow : "hidden"
	                        },
	                        layout : "hbox",
	                        items : [ icon ]
	                    });

	                    var picFiles = [ iconWrapper,
	                            CWHF.createLabelComponent("admin.customize.list.lbl.iconName", "actualIconName", {
		                            labelWidth : this.labelWidth, itemId:"actualIconName"
	                            }), CWHF.createFileField("admin.customize.list.lbl.newIcon", "iconFile", {
	                                allowBlank : false,
	                                labelWidth : this.labelWidth,
	                                itemId     : "iconFile"
	                            },{change:{fn: this.newIconFileChanged, scope:this}}) ];
	                    return Ext.create("Ext.form.Panel", {
	                        bodyStyle : "padding:5px",
	                        url : "listOptionIcon!upload.action",
	                        defaults : {
	                            labelStyle : "overflow: hidden;",
	                            margin : "5 5 0 0",
	                            msgTarget : "side",
	                            anchor : "-20"
	                        },
	                        method : "POST",
	                        fileUpload : true,
	                        items : picFiles
	                    });
	                },
					newIconFileChanged:function(){
						var toolbars = this.win.getDockedItems("toolbar[dock='bottom']");
						if (toolbars ) {
							// disable delete button if no icon is specified
							toolbars[0].getComponent(0).setDisabled(false);
						}
					},
	                renderUploadPostDataProcess : function(data, formPanel, fromTree) {
	                    var actualIcon = formPanel.getComponent("iconPanel").getComponent("actualIcon");
	                    if (actualIcon ) {
		                    actualIcon.setSrc(data.icon);
		                    // actualIcon.doComponentLayout();
	                    }
	                    actualIconName = formPanel.getComponent("actualIconName");
	                    if (actualIconName ) {
		                    actualIconName.setValue(data["iconName"]);
	                    }
	                    // Ext.getDom("pic").src = data.icon;
	                    /*
						 * if (data["iconName"]) {
						 * formPanel.getComponent("actualIconName").setValue(data["iconName"]);
						 * formPanel.getComponent("actualIconName").setVisible(true); }
						 */
	                    formPanel.getComponent("iconFile").setValue("");
	                    var toolbars = this.win.getDockedItems("toolbar[dock='bottom']");
	                    if (toolbars ) {
							toolbars[0].getComponent(0).setDisabled(true);
		                    // disable delete button if no icon is specified
		                    toolbars[0].getComponent(1).setDisabled(CWHF.isNull(data.iconName) || data.iconName === "");
	                    }
	                },

	                uploadFileHandler : function(window, submitUrl, submitUrlParams, extraConfig) {
	                    var theForm = this.formEdit.getForm();
	                    if (!theForm.isValid()) {
		                    Ext.MessageBox.alert(getText("admin.customize.list.title.upload", this.getEntityLabel({
			                    fromTree : false
		                    })), getText("admin.customize.list.lbl.iconFileNotSpecified"));
		                    return;
	                    }
	                    var iconFile = this.formEdit.getComponent("iconFile");
	                    if (!this.validateFileExtension(iconFile.getRawValue())) {
		                    Ext.MessageBox.alert(getText("admin.customize.list.title.upload", this.getEntityLabel({
			                    fromTree : false
		                    })), getText("admin.customize.list.lbl.iconFileWrongType"));
		                    return;
	                    }

	                    var fromTree = false;
	                    if (extraConfig ) {
		                    fromTree = extraConfig.fromTree;
	                    }
	                    theForm.submit({
	                        scope : this,
	                        params : submitUrlParams,
	                        success : function(form, action) {
		                        this.renderUploadPostDataProcess(action.result.data, this.formEdit, fromTree);
		                        this.reload.call(this, this.getReloadParamsAfterUpload(fromTree));
	                        },
	                        failure : function(form, action) {
		                        com.trackplus.util.submitFailureHandler(form, action);
	                        }
	                    });
	                },

	                deleteUploadedFileHandler : function(window, submitUrl, submitUrlParams, extraConfig) {
	                    var pictUploadForm = this.formEdit;
	                    var fromTree = false;
	                    if (extraConfig ) {
		                    fromTree = extraConfig.fromTree;
	                    }
	                    Ext.Ajax.request({
	                        url : submitUrl,
	                        scope : this,
	                        params : submitUrlParams,
	                        success : function(response) {
		                        var result = Ext.decode(response.responseText);
		                        /*
								 * Ext.getDom('pic').src = "";
								 * pictUploadForm.getComponent('actualIconName').setValue("");
								 * pictUploadForm.getComponent('iconFile').setRawValue('');
								 */
		                        this.renderUploadPostDataProcess(result.data, pictUploadForm, fromTree);
		                        this.reload.call(this, this.getReloadParamsAfterUpload(fromTree));
	                        }
	                    });
	                },

	                /** ****fileupload end***** */

	                getGridViewConfig: function(node) {
	            		if (this.hasDragAndDropOnGrid(node)) {
	            			return {
	            				plugins: {
	            					ptype: "gridviewdragdrop",
	            					dragGroup: this.getBaseAction() + "gridDDGroup",
	            					dropGroup: this.getBaseAction() + "gridDDGroup",
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
	                    return [ {
	                        name : "id",
	                        type : "int"
	                    }, {
	                        name : "label",
	                        type : "string"
	                    }, {
	                        name : "canEdit",
	                        type : "boolean"
	                    }, {
	                        name : "canCopy",
	                        type : "boolean"
	                    }, {
	                        name : "canDelete",
	                        type : "boolean"
	                    }, {
	                        name : "rowListForLabel",
	                        type : "int"
	                    }, {
	                        name : "iconCls",
	                        type : "string"
	                    }, {
	                        name : "leaf",
	                        type : "boolean"
	                    }, {
	                        name : "node",
	                        type : "string"
	                    } ];
	                },

	                getListOptionFields : function() {
	                    return [ {
	                        name : "id",
	                        type : "int"
	                    }, {
	                        name : "label",
	                        type : "string"
	                    }, {
	                        name : "canEdit",
	                        type : "boolean"
	                    }, {
	                        name : "canDelete",
	                        type : "boolean"
	                    }, {
	                        name : "rowListForLabel",
	                        type : "int"
	                    }, {
	                        name : "hasTypeflag",
	                        type : "boolean"
	                    }, {
	                        name : "disableTypeflag",
	                        type : "boolean"
	                    }, {
	                        name : "typeflagLabel",
	                        type : "string"
	                    }, {
	                        name : "hasIcon",
	                        type : "boolean"
	                    }, {
	                        name : "iconName",
	                        type : "string"
	                    }, {
	                        name : "icon",
	                        type : "string"
	                    }, {
	                        name : "hasCssStyle",
	                        type : "boolean"
	                    }, {
	                        name : "cssStyle",
	                        type : "string"
	                    }, {
	                        name : "hasChildFilter",
	                        type : "boolean"
	                    }, {
	                        name : "hasPercentComplete",
	                        type : "boolean"
	                    }, {
	                        name : "percentComplete",
	                        type : "int",
		                    allowNull : true
	                    }, {
	                        name : "hasDefaultOption",
	                        type : "boolean"
	                    }, {
	                        name : "defaultOption",
	                        type : "boolean"
	                    }, {
	                        name : "leaf",
	                        type : "boolean"
	                    }, {
	                        name : "node",
	                        type : "string"
	                    } ];
	                },

	                getListColumns : function() {
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
		                        listeners: {"checkchange": {fn: this.changeDefault, scope:this}}
		                    });
	                    }
	                    return columnModelArr;
	                },

	                changeDefault: function(checkBox, rowIndex, checked, eOpts) {
	            		var record = this.grid.getStore().getAt(rowIndex);
	            		if (record) {
	            			var params = {node:record.data["node"], defaultOption:checked};
	            			Ext.Ajax.request({
	            				url: this.getBaseAction() + "!changeDefault.action",
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
	                    /*
						 * Ext.Ajax.request({ url: strutsBaseAction +
						 * '!dropWouldDuplicate.action', params: { nodeFrom:
						 * nodeFrom.data["id"], nodeTo: nodeTo.data["id"], },
						 * scope: this, disableCaching:true, success:
						 * function(response){ var responseJson =
						 * Ext.decode(response.responseText); if
						 * (responseJson.success === false) { if
						 * (responseJson.errorMessage) { //no right to
						 * delete Ext.MessageBox.alert(this.failureTitle,
						 * responseJson.errorMessage) } } }, failure:
						 * function(result){
						 * Ext.MessageBox.alert(this.failureTitle,
						 * result.responseText) }, method:"POST", });
						 */
	                    return true;
	                },

	                enableDisableToolbarButtons : function(view, arrSelections) {
	                    var selectedRecord = arrSelections[0];
	                    if (CWHF.isNull(selectedRecord)) {
		                    this.actionDeleteGridRow.setDisabled(true);
		                    this.actionEditGridRow.setDisabled(true);
		                    this.actionCopyGridRow.setDisabled(true);
		                    this.actionChildIssueTypeAssignmentFromGrid.setDisabled(true);
		                    this.actionExportGridRow.setDisabled(true);
		                    this.actionUploadFromGrid.setDisabled(true);
		                    this.actionDownloadFromGrid.setDisabled(true);
		                    this.actionMoveUp.setDisabled(true);
		                    this.actionMoveDown.setDisabled(true);
	                    } else {
		                    var canDelete = selectedRecord.data["canDelete"];
		                    this.actionDeleteGridRow.setDisabled(!canDelete);
		                    var canEdit = selectedRecord.data["canEdit"];
		                    this.actionEditGridRow.setDisabled(!canEdit);
		                    this.actionChildIssueTypeAssignmentFromGrid.setDisabled(!canEdit);
		                    this.actionUploadFromGrid.setDisabled(!canEdit);
		                    this.actionMoveDown.setDisabled(!canEdit);
		                    this.actionMoveUp.setDisabled(!canEdit);
		                    if (canEdit) {
			                    var store = this.grid.getStore();
			                    if (store ) {
				                    this.actionMoveDown.setDisabled(selectedRecord === store.last());
				                    this.actionMoveUp.setDisabled(selectedRecord === store.first());
			                    }
		                    }
		                    var canCopy = selectedRecord.data["canCopy"];
		                    this.actionCopyGridRow.setDisabled(!canCopy);
		                    this.actionExportGridRow.setDisabled(!canCopy);
		                    var hasIcon = selectedRecord.data["iconName"]
		                            && selectedRecord.data["iconName"] !== "";
		                    this.actionDownloadFromGrid.setDisabled(!hasIcon);
	                    }
	                },

	                /**
	                 * Prepare adding/editing a custom list
	                 */
	                getCustomListWindowItems : function(node, add, canEdit) {
	                    var windowItems = [ CWHF.createTextField("common.lbl.name", "label", {
	                        disabled : !canEdit,
	                        allowBlank : false,
	                        labelWidth : this.labelWidth
	                    }) ];
	                    //list types
	                    /*com.trackplus.util.createCombo("customListTypesList",
	                    		getText('admin.customize.list.lbl.listtype'),
	                    		"customListType", add, null, null, null, null,
	                    		{select: function(listTypeCombo) {
	                    			var cascadingCombo = listTypeCombo.ownerCt.getComponent('cascadingTypesList');
	                    			if (listTypeCombo.getValue()===3) {
	                    				if (CWHF.isNull(cascadingCombo.getValue()) || cascadingCombo.getValue()==="") {
	                    					cascadingCombo.setValue(cascadingCombo.store.data.items[0].data.id);
	                    				}
	                    				cascadingCombo.show();
	                    			} else {
	                    				cascadingCombo.hide();
	                    			}
	                    		}})];*/
	                    //cascading parent
	                    /*windowItems.push(com.trackplus.util.createCombo("cascadingTypesList",
	                    		getText('admin.customize.list.lbl.cascadingType'),
	                    		"cascadingType", add, null, null, null, null, null, {hidden:true}));*/
	                    //repository types
	                    /*windowItems.push(com.trackplus.util.createCombo("repositoryTypesList",
	                    		getText('common.lbl.queries.repository'),
	                    		"repositoryType", add || canEdit, null, null, null, null,
	                    		{select: function(repositoryCombo) {
	                    			var projectCombo = repositoryCombo.ownerCt.getComponent('projectsList');
	                    			if (repositoryCombo.getValue()===3) {
	                    				if (CWHF.isNull(projectCombo.getValue()) || projectCombo.getValue()==="") {
	                    					projectCombo.setValue(projectCombo.store.data.items[0].data.id);
	                    				}
	                    				projectCombo.show();
	                    			} else {
	                    				projectCombo.hide();}
	                    		}}));
	                    //project
	                    windowItems.push(com.trackplus.util.createCombo("projectsList",
	                    		this.localizedLabels[1], "project", add || canEdit, null, null, null, null, null, {hidden:true}));*/
	                    windowItems.push(CWHF.createTextAreaField("common.lbl.description", "description", {
	                        disabled : !(add || canEdit),
	                        labelWidth : this.labelWidth
	                    }));
	                    return windowItems;
	                },

	                /**
	                 * Prepare adding/editing a system or custom list entry
	                 */
	                getListOptionWindowItems : function(node, hasTypeflag, disableTypeflag, hasCssStyle,
	                        hasPercentComplete, hasDefaultOption, canEdit) {
	                    //dialog for adding/editing a list option
	                    var windowItems = [ CWHF.createTextField("common.lbl.name", "label", {
	                        disabled : !canEdit,
	                        allowBlank : false,
	                        labelWidth : this.labelWidth
	                    }) ];
	                    if (hasTypeflag) {
		                    //type flag combo
		                    windowItems.push(CWHF.createCombo("admin.customize.list.lbl.typeflag", "typeflag", {
		                    	itemId: "typeflag",
		                        disabled : disableTypeflag || !canEdit,
		                        labelWidth : this.labelWidth
		                    }));
	                    }
	                    if (hasPercentComplete) {
		                    windowItems.push(CWHF.createNumberField("admin.customize.list.lbl.percentComplete",
		                            "percentComplete", 0, 0, 100, {
		                                disabled : !canEdit,
		                                labelWidth : this.labelWidth
		                            }));
	                    }
	                    if (hasDefaultOption) {
		                    windowItems.push(CWHF.createCheckbox("admin.customize.list.lbl.defaultOption",
		                            "defaultOption", {
		                                disabled : !canEdit,
		                                labelWidth : this.labelWidth
		                            }));
	                    }
	                    if (hasCssStyle) {
		                    windowItems.push({
		                        xtype : "fieldset",
		                        itemId : "fsStyle",
		                        //width: this.FieldSetWidth,
		                        title : getText("admin.customize.list.lbl.style"),
		                        collapsible : false,
		                        defaults : {
			                        anchor : "100%"
		                        },
		                        layout : "anchor",
		                        items : [
		                                CWHF.createColorPicker("admin.customize.list.lbl.style.bgrColor",
		                                        "cssStyleBean.bgrColor", {
		                                			itemId : "cssStyleBeanBgrColor",
		                                            disabled : !canEdit,
		                                            labelWidth : this.labelWidth
		                                        }),
		                                CWHF.createColorPicker("admin.customize.list.lbl.style.color",
		                                        "cssStyleBean.color", {
		                                			itemId : "cssStyleBeanColor",
		                                            disabled : !canEdit,
		                                            labelWidth : this.labelWidth
		                                        }),
		                                CWHF.createCombo("admin.customize.list.lbl.style.fontWeight",
		                                        "cssStyleBean.fontWeight", {
		                                			itemId: "fontWeight",
		                                            disabled : !canEdit,
		                                            idType : "string",
		                                            labelWidth : this.labelWidth
		                                        }),
		                                CWHF.createCombo("admin.customize.list.lbl.style.fontStyle",
		                                        "cssStyleBean.fontStyle", {
		                                			itemId: "fontStyle",
		                                            disabled : !canEdit,
		                                            idType : "string",
		                                            labelWidth : this.labelWidth
		                                        }),
		                                CWHF.createCombo("admin.customize.list.lbl.style.text-decoration",
		                                        "cssStyleBean.textDecoration", {
		                                			itemId: "textDecoration",
		                                            disabled : !canEdit,
		                                            idType : "string",
		                                            labelWidth : this.labelWidth
		                                        }) ]
		                    });

		                    /*windowItems.push(CWHF.createColorPicker("admin.customize.list.lbl.style.bgrColor",
		                    		"cssStyleBean.bgrColor", {disabled:!canEdit}));
		                    windowItems.push(CWHF.createColorPicker("admin.customize.list.lbl.style.color",
		                    		"cssStyleBean.color", {disabled:!canEdit}));
		                    windowItems.push(CWHF.createCombo(
		                    		"admin.customize.list.lbl.style.fontWeight",
		                    		"cssStyleBean.fontWeight", {disabled:!canEdit, idType:"String"}));
		                    windowItems.push(CWHF.createCombo(
		                    		"admin.customize.list.lbl.style.fontStyle",
		                    		"cssStyleBean.fontStyle", {disabled:!canEdit, idType:"String"}));
		                    windowItems.push(CWHF.createCombo(
		                    		"admin.customize.list.lbl.style.text-decoration",
		                    		"cssStyleBean.textDecoration", {disabled:!canEdit, idType:"String"}));*/
	                    }
	                    return windowItems;
	                },

	                /**
	                 * Load the combos after the result has arrived containing also the combo data sources
	                 */
	                postDataLoadCombos : function(data, panel) {
	                    /*if (panel.items.items) {
	                    	panel.items.items.forEach(function(item) {
	                    		//for each combo-type control set the value also:
	                    		//for a combos itemId <xxx>sList the json result should contain
	                    		//both <xxx>sList for the combo datasource and <xxx> for the combo's actual value
	                    		//the json field for value is
	                    		if (item.xtype === "combo") {
	                    			var comboSource = data[item.itemId];
	                    			item.store.loadData(comboSource);
	                    			//-'Lists'
	                    			item.setValue(data[item.itemId.substring(0,item.itemId.length-5)]);
	                    		}
	                    	});
	                    }*/

	                    var typeFlagsList = panel.getComponent("typeflag");
	                    if (typeFlagsList ) {
		                    typeFlagsList.store.loadData(data["typeflagsList"]);
		                    typeFlagsList.setValue(data["typeflag"]);
	                    }
	                    var fsStyle = panel.getComponent("fsStyle");
	                    if (fsStyle ) {
		                    var fontWeight = fsStyle.getComponent("fontWeight");
		                    if (fontWeight ) {
			                    fontWeight.store.loadData(data["fontWeightsList"]);
		                    }
		                    var fontStyle = fsStyle.getComponent("fontStyle");
		                    if (fontStyle ) {
			                    fontStyle.store.loadData(data["fontStylesList"]);
		                    }
		                    var textDecoration = panel.getComponent("fsStyle").getComponent("textDecoration");
		                    if (textDecoration ) {
			                    textDecoration.store.loadData(data["textDecorationsList"]);
		                    }
		                    var bgrColor = fsStyle.getComponent("cssStyleBeanBgrColor");
		                    if (bgrColor  && data["cssStyleBean.bgrColor"] ) {
			                    bgrColor.setFieldStyle('background-color:#' + data["cssStyleBean.bgrColor"]
			                            + '; background-image: none;');
		                    }
		                    var color = fsStyle.getComponent("cssStyleBeanColor");
		                    if (color  && data["cssStyleBean.color"] ) {
			                    color.setFieldStyle('background-color:#' + data["cssStyleBean.color"]
			                            + '; background-image: none;');
		                    }
	                    }
	                },

	                optionPostDataProcess : function(data, panel) {
	                    this.postDataLoadCombos(data, panel);
	                },

	                /**
	                 * The method to process the data to be loaded arrived from the server
	                 */
	                getEditPostDataProcess : function(record, isLeaf, add, fromTree, operation) {
	                    var recordData = null;
	                    if (record ) {
		                    recordData = record.data;
	                    }
	                    if (this.isListOperation(recordData, isLeaf, add, fromTree, operation)) {
		                    return null;//this.listPostDataProcess;
	                    } else {
		                    return this.optionPostDataProcess;
	                    }
	                },

	                /**
	                 * Get the panel items
	                 * recordData: the record data (for the record to be edited or added to)
	                 * isLeaf: whether add a leaf or a folder
	                 * add: whether it is add or edit
	                 * fromTree: operations started from tree or from grid
	                 * operation:  the name of the operation
	                 */
	                getPanelItems : function(recordData, isLeaf, add, fromTree, operation) {
	                    var nodeID = this.getRecordID(recordData, {
		                    fromTree : fromTree
	                    });
	                    var canEdit = false;
	                    if (operation === "add" || operation === "copy") {
		                    canEdit = true;
	                    } else {
		                    if (operation === "edit") {
			                    canEdit = recordData["canEdit"];
		                    }
	                    }
	                    if (this.isListOperation(recordData, isLeaf, add, fromTree, operation)) {
		                    return this.getCustomListWindowItems(nodeID, add, canEdit);
	                    } else {
		                    var hasTypeflag;
		                    var disableTypeflag;
		                    var hasCssStyle;
		                    var hasPercentComplete;
		                    var hasDefaultOption;
		                    if (add) {
			                    hasTypeflag = recordData["childrenHaveTypeflag"];
			                    disableTypeflag = recordData["disableTypeflag"];
			                    hasCssStyle = recordData["childrenHaveCssStyle"];
			                    hasPercentComplete = recordData["childrenHavePercentComplete"];
			                    hasDefaultOption = recordData["childrenHaveDefaultOption"];
		                    } else {
			                    hasTypeflag = recordData["hasTypeflag"];
			                    disableTypeflag = recordData["disableTypeflag"];
			                    hasCssStyle = recordData["hasCssStyle"];
			                    hasPercentComplete = recordData["hasPercentComplete"];
			                    hasDefaultOption = recordData["hasDefaultOption"];
		                    }
		                    return this.getListOptionWindowItems(nodeID, hasTypeflag, disableTypeflag, hasCssStyle,
		                            hasPercentComplete, hasDefaultOption, canEdit);
	                    }
	                },

	                /**
	                 * Handler for adding a node (folder or leaf)
	                 */
	                onAdd : function() {
	                    var operation = "add";
	                    var title = this.getTitle(this.getAddTitleKey(), {
		                    fromTree : true
	                    });
	                    var loadParams = this.getAddLeafParams();
	                    var submitParams = this.getAddLeafParams();
	                    var reloadParams = this.getAddReloadParamsAfterSave(true);
	                    var reloadParamsFromResult = this.getAddSelectionAfterSaveFromResult();
	                    var selectedRecord = this.getSingleSelectedRecord(true);
	                    if (CWHF.isNull(selectedRecord)) {
		                    selectedRecord = this.tree.getRootNode();
	                    }
	                    return this.onAddEdit(title, selectedRecord, operation, true, true, true, loadParams,
	                            submitParams, reloadParams, reloadParamsFromResult);
	                },

	                /**
	                 * Parameters for adding a new entry to a leaf:
	                 * add a child to a leaf node: exclusively by extending custom selects:
	                 * simple -> parent child or parent child -> parent child grandchild
	                 * without explicitly asking for the exact type of the composite select
	                 */
	                getAddLeafParams : function() {
	                    //false as it would be added to a folder to send the leaf's id, not the parent folder's id
	                    return this.getAddParams(false);
	                },

	                /**
	                 * Copy a grid row: right now only custom lists can be copied (options, or option branches not)
	                 */
	                onCopyGridRow : function() {
	                    return this.onCopy(false);
	                },

	                /**
	                 * Copy a tree node: right now only custom lists can be copied (options, or option branches not)
	                 */
	                onCopyTreeNode : function() {
	                    return this.onCopy(true);
	                },

	                /**
	                 * Handler for copy
	                 */
	                onCopy : function(fromTree) {
	                    var operation = "copy";
	                    var isLeaf = this.selectedIsLeaf(fromTree);
	                    var title = this.getTitle(this.getCopyTitleKey(), {
		                    fromTree : fromTree
	                    });
	                    var loadParams = this.getEditParams(fromTree);
	                    loadParams["copy"] = true;
	                    var submitParams = this.getEditParams(fromTree);
	                    submitParams["copy"] = true;
	                    var reloadParams = this.getCopyReloadParamsAfterSave(fromTree);
	                    var reloadParamsFromResult = this.getEditReloadParamsAfterSaveFromResult(fromTree);
	                    var selectedRecord = this.getSingleSelectedRecord(fromTree);
	                    return this.onAddEdit(title, selectedRecord, operation, isLeaf, false, fromTree, loadParams,
	                            submitParams, reloadParams, reloadParamsFromResult);
	                },

	                /**
	                 * Get the node to reload after save after copy operation
	                 */
	                getCopyReloadParamsAfterSave : function(fromTree) {
	                    if (fromTree) {
		                    if (this.selectedNode ) {
			                    var parentNode = this.selectedNode.parentNode;
			                    if (parentNode ) {
				                    return {
					                    nodeIDToReload : parentNode.data["id"]
				                    };
			                    }
		                    }
	                    }
	                    //else nodeIDToReload is not set which means by default this.selectedNode (see refreshGridAndTreeAfterSubmit())
	                    return null;
	                },

	                /**
	                 * Parameters for reloading after a delete operation
	                 */
	                getReloadParamsAfterDelete : function(selectedRecords, extraConfig, responseJson) {
	                    if (selectedRecords ) {
		                    //we suppose that only one selection is allowed in tree
		                    var selNode = selectedRecords;
		                    if (selNode ) {
			                    var parentNode = null;
			                    var grandParentNode = null;
			                    var parentNodeID = null;
			                    if (extraConfig ) {
				                    fromTree = extraConfig.fromTree;
				                    if (fromTree) {
					                    //delete from tree: select the parent of the deleted node for reload and select
					                    parentNode = selNode.parentNode;
					                    if (parentNode ) {
						                    parentNodeID = parentNode.data.id;
					                    }
					                    if (responseJson["lastCascadingChildDeleted"] && parentNode ) {
						                    grandParentNode = parentNode.parentNode;
						                    //in this case not the same as parentNode.data.id becuause the
						                    //childListID and leaf components of the nodeID changes
						                    parentNodeID = responseJson["node"];
					                    }
					                    var grandParentNodeID = null;
					                    if (grandParentNode ) {
						                    grandParentNodeID = grandParentNode.data.id;
					                    } else {
						                    grandParentNodeID = parentNodeID;
					                    }
					                    return {
					                        nodeIDToReload : grandParentNodeID,
					                        nodeIDToSelect : parentNodeID
					                    };
				                    } else {
					                    //delete from grid: the parent or the leaf to be deleted is selected already in tree
					                    if (responseJson["lastCascadingChildDeleted"] && this.selectedNode ) {
						                    //in this case not the same as this.selectedNode.data.id becuause the
						                    //childListID and leaf components of the nodeID changes
						                    parentNodeID = responseJson["node"];
						                    //in both cases the grid contains the single element to be deleted
						                    if (this.selectedNode.isLeaf()) {
							                    //the leaf to be deleted was selected in the tree
							                    grandParentNode = this.selectedNode.parentNode.parentNode;
						                    } else {
							                    //the parent of the leaf to be deleted was selected in the tree
							                    grandParentNode = this.selectedNode.parentNode;
						                    }
						                    if (grandParentNode ) {
							                    var grandParentNodeID = grandParentNode.data.id;
							                    return {
							                        nodeIDToReload : grandParentNodeID,
							                        nodeIDToSelect : parentNodeID
							                    };
						                    }
					                    } else {
						                    if (this.getShowGridForLeaf() && this.selectedNode.isLeaf()) {
							                    return {
								                    nodeIDToReload : this.selectedNode.parentNode.data.id
							                    };
						                    }
						                    return {
							                    nodeIDToSelect : this.selectedNode.data.id
						                    };
					                    }
				                    }
			                    }
		                    }
	                    }
	                    return null;
	                },

	                /**
	                 * panel for importing the e-mail templates
	                 */
	                getImportPanel : function() {
	                    this.formPanel = new Ext.form.FormPanel({
	                        border : false,
	                        bodyStyle : "padding-left:10px",
	                        defaults : {
	                            labelStyle : "overflow: hidden;",
	                            margin : "5 5 0 0",
	                            msgTarget : "side",
	                            anchor : "-20"
	                        },
	                        method : "POST",
	                        fileUpload : true,
	                        items : [
	                                CWHF.createFileField("admin.customize.list.import.lbl.uploadFile", "uploadFile", {
		                                allowBlank : false,
		                                itemId     : "uploadFile"
	                                }),
	                                CWHF.createCheckbox("common.lbl.overwriteExisting", "overwriteExisting"),
	                                CWHF.createCheckbox("common.lbl.clearChildren", "clearChildren") ]
	                    });
	                    return this.formPanel;
	                },

	                onImport : function() {
	                    var submit = [ {
	                        submitUrl : this.getBaseAction() + "!importList.action",
	                        submitButtonText : getText("common.btn.upload"),
	                        submitUrlParams : {
		                        node : this.selectedNodeID
	                        },
	                        validateHandler : Upload.validateUpload,
	                        expectedFileType : /^.*\.(xml)$/,
	                        refreshAfterSubmitHandler : this.reload
	                    } ];
	                    var title = getText("common.lbl.upload", getText("admin.customize.list.import.lbl.uploadFile"));
	                    var windowParameters = {
	                        title : title,
	                        width : 600,
	                        height : 180,
	                        submit : submit,
	                        formPanel : this.getImportPanel(),
	                        cancelButtonText : getText("common.btn.done")
	                    };
	                    var windowConfig = Ext.create("com.trackplus.util.WindowConfig", windowParameters);
	                    windowConfig.showWindowByConfig(this);
	                },

	                onExportGridRow : function() {
	                    this.onExport(false);
	                },

	                onExportTreeNode : function() {
	                    this.onExport(true);
	                },

	                onExport : function(fromTree) {
	                    attachmentURI = this.getBaseAction() + "!exportList.action?node=" + this.getSelectedID(fromTree);
	                    window.open(attachmentURI);
	                },

	                getSelectedID : function(fromTree) {
	                    var recordData = this.getSingleSelectedRecordData(fromTree);
	                    if (recordData ) {
		                    return this.getRecordID(recordData, {
			                    fromTree : fromTree
		                    });
	                    }
	                    return null;
	                },

	                /**
	                 * Get the itemId of those actions whose context menu text or
	                 * toolbar button tooltip should be changed according to the current selection
	                 */
	                getActionItemIdsWithContextDependentLabel : function() {
	                    return [ "add", "editGridRow", "editTreeNode", "copyGridRow", "copyTreeNode", "deleteGridRow",
	                            "deleteTreeNode", "uploadIconGridRow", "uploadIconTreeNode", "downloadIconGridRow",
	                            "downloadIconTreeNode", "moveUp", "moveDown", "exportGridRow", "exportTreeNode",
	                            "importList" ];
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
	                },

	                onTreeNodeDblClick : function(view, record) {
	                    var canEdit = record.data["canEdit"];
	                    if (canEdit) {
		                    this.onEditTreeNode();
	                    }
	                },

	                onGridRowDblClick : function(view, record) {
	                    var canEdit = record.data["canEdit"];
	                    if (canEdit) {
		                    this.onEditGridRow();
	                    }
	                }
	            });
