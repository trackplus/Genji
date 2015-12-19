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


Ext.define('com.trackplus.admin.customize.ReportConfig', {
	    extend : 'com.trackplus.admin.TreeDetail',
	    config : {
	    	repCfgLayout: null
	    },
	    baseAction : "reportConfig",
	    folderAction: "categoryConfig",
	    btnExecute : null,
	    confirmDeleteEntity : true,
	    confirmDeleteNotEmpty : true,
	    entityID : 'node',
	    folderEditWidth : 400,
	    folderEditHeight : 115,
	    reportEditWidth : 500,
	    reportEditHeight : 150,
	    dragAndDropOnTree : true,
	    labelWidth : 170,
	    treeHasItemdblclick : true,
	    allowMultipleSelections : true,
	    // actions
	    actionAddFolder : null,
	    actionAddLeaf : null,
	    actionEditGridRow : null,
	    actionEditTreeNode : null,
	    actionCutTreeNode : null,
	    actionCopyTreeNode : null,
	    actionPasteTreeNode : null,
	    actionDeleteGridRow : null,
	    actionDeleteTreeNode : null,
	    localizedLabels : null,
	    instant : false,
	    selectedRecords : {},

	    statics : {},

	    /*constructor : function(config) {
	        var cfg = config || {};
			this.initConfig(cfg);
	        this.btnExecute = "common.btn.executeReport";
	        this.initBase();
	    },*/

	    initComponent : function() {
			this.treeStoreUrl = "categoryConfig!expand.action";
			this.callParent();
		},
		
	    initActions : function() {
	        var addFolderIconCls;
	        var addLeafIconCls;
	        var editIconCls;

	        addFolderIconCls = this.getAddFolderReportIconCls();
	        addLeafIconCls = this.getAddReportIconCls();
	        editIconCls = this.getEditReportIconCls();
	        
	        var addFolderText = this.getActionTooltip(this.getAddTitleKey(), {isLeaf: false});
		    this.actionAddFolder = CWHF.createAction(addFolderText, addFolderIconCls,
		    		this.onAddFolder, {tooltip:addFolderText, labelIsLocalized:true});
	        /* this.actionAddFolder = this.createLocalizedAction(this.getTitle(this.getAddTitleKey(), {
	            isLeaf : false
	        }), addFolderIconCls, this.onAddFolder, this.getTitle(this.getAddTitleKey(), {
	            isLeaf : false
	        }), true);*/
		    var addLeafText = this.getActionTooltip(this.getAddTitleKey(), {isLeaf: true});
		    this.actionAddLeaf = CWHF.createAction(addLeafText, addLeafIconCls,
		    		this.onAddLeaf, {tooltip:addLeafText, labelIsLocalized:true});
		    /*this.actionAddLeaf = this.createLocalizedAction(this.getTitle(this.getAddTitleKey(), {
	            isLeaf : true
	        }), addLeafIconCls, this.onAddLeaf, this.getTitle(this.getAddTitleKey(), {
	            isLeaf : true
	        }), true);*/
		    this.actionEditGridRow = CWHF.createContextAction(this.getEditButtonKey(), editIconCls,
		    		this.onEditGridRow, this.getEditTitleKey(), {itemId:"editGridRow", disabled:true});
		    /*this.actionEditGridRow = this.createAction(this.getEditButtonKey(), editIconCls,
	                this.onEditGridRow, true, this.getEditTitleKey(), "editGridRow");*/
		    this.actionEditTreeNode = CWHF.createContextAction(this.getEditButtonKey(), editIconCls,
		    		this.onEditTreeNode, this.getEditTitleKey(), {itemId:"editTreeNode", disabled:true});
		    /*this.actionEditTreeNode = this.createAction(this.getEditButtonKey(), editIconCls,
	                this.onEditTreeNode, false, this.getEditTitleKey(), "editTreeNode");*/
		    this.actionExecuteGridRow = CWHF.createAction(this.getExecuteReportButtonKey(), this.getExecuteReportIconCls(),
		    		this.onExecuteGridRow, {disabled:true});
		    /*this.actionExecuteGridRow = this.createAction(this.btnExecute, 'rtemplateExec',
	                this.onExecuteGridRow, true, this.btnExecute);*/
		    this.actionExecuteTreeNode = CWHF.createAction(this.getExecuteReportButtonKey(), this.getExecuteReportIconCls(),
		    		this.onExecuteTreeNode, {disabled:true});
		    /*this.actionExecuteTreeNode = this.createAction(this.btnExecute, 'rtemplateExec',
	                this.onExecuteTreeNode, true, this.btnExecute);*/
	        this.actionDownloadGridRow = CWHF.createAction(this.getDownloadButtonKey(), this.getDownloadIconCls(),
		    		this.onDownloadGridRow, {tooltip:this.getActionTooltip(this.getDownloadTitleKey(), {isLeaf:true}), disabled:true});
		    /*this.actionDownloadGridRow = this.createAction('common.btn.download', 'download',
	                this.onDownloadGridRow, true, "common.lbl.download", "downloadGridRow");*/
	        this.actionDownloadTreeNode = CWHF.createAction(this.getDownloadButtonKey(), this.getDownloadIconCls(),
		    		this.onDownloadTreeNode, {tooltip:this.getActionTooltip(this.getDownloadTitleKey(), {isLeaf:true}), disabled:true});
	        /*this.actionDownloadTreeNode = this.createAction('common.btn.download', 'download',
	                this.onDownloadTreeNode, true, "common.lbl.download", "downloadTreeNode");*/

	        this.actionDeleteGridRow = CWHF.createContextAction(this.getDeleteButtonKey(), this.getDeleteIconCls(),
		    		this.onDeleteFromGrid, this.getDeleteTitleKey(), {itemId:"deleteGridRow", disabled:true});
	        /*this.actionDeleteGridRow = this.createAction(this.getDeleteButtonKey(),
	                this.getDeleteIconCls(), this.onDeleteFromGrid, true, this.getDeleteTitleKey(),
	                "deleteGridRow");*/
	        this.actionDeleteTreeNode = CWHF.createContextAction(this.getDeleteButtonKey(), this.getDeleteIconCls(),
		    		this.onDeleteFromTree, this.getDeleteTitleKey(), {itemId:"deleteTreeNode"});
	        /*this.actionDeleteTreeNode = this.createAction(this.getDeleteButtonKey(), this
	                .getDeleteIconCls(), this.onDeleteFromTree, false, this.getDeleteTitleKey(),
	                "deleteTreeNode");*/

            // cut/copy - paste and drag and drop
        	this.actionCutTreeNode = CWHF.createContextAction(this.getCutButtonKey(), this.getCutIconCls(),
		    		this.onCutTreeNode, this.getCutTitleKey(), {itemId:"cut"});
        	/*this.actionCutTreeNode = this.createAction(this.getCutButtonKey(), this.getCutIconCls(),
                    this.onCutTreeNode, false, this.getCutTitleKey(), "cut");*/
        	this.actionCopyTreeNode = CWHF.createContextAction(this.getCopyButtonKey(), this.getCopyIconCls(),
		    		this.onCopyTreeNode, this.getCopyTitleKey(), {itemId:"copy"});
        	/*this.actionCopyTreeNode = this.createAction(this.getCopyButtonKey(), this.getCopyIconCls(),
                    this.onCopyTreeNode, false, this.getCopyTitleKey(), "copy");*/
        	this.actionPasteTreeNode = CWHF.createAction(this.getPasteButtonKey(), this.getPasteIconCls(),
		    		this.onPasteTreeNode);
            /*this.actionPasteTreeNode = this.createAction(this.getPasteButtonKey(), this
                    .getPasteIconCls(), this.onPasteTreeNode, false, this.getPasteButtonKey());*/
        
	    },

	    /**
		 * Get the detail part after selecting a tree node
		 */
	    loadDetailPanel : function(node, leaf, opts) {
	        this.getGridPanel(node, opts);
	    },

	    getGridPanel : function(node, opts) {
	        var me = this;
	        this.params = {};
	        this.params = this.getGridStoreExtraParams(node, opts);
	        this.getDataViewPanelData();
	    },

	    getDataViewPanelData : function(parameters) {
	        var me = this;
	        Ext.Ajax.request({
	            url : this.getNodeBaseAction({
	                isLeaf : false
	            }) + '!loadList.action',
	            params : this.params,
	            success : function(result) {
	                var jsonData = Ext.decode(result.responseText);
	                me.centerDataViewDataArrived(jsonData);
	            },
	            failure : function() {
	            },
	            method : 'GET'
	        });
	    },

	    centerDataViewDataArrived : function(jsonData) {
	        var me = this;
	        var storeData = [];
	        for ( var ind in jsonData) {
	            var tmpObj = {};
	            tmpObj.text = jsonData[ind].text;
	            tmpObj.typeLabel = jsonData[ind].typeLabel;
	            tmpObj.icon = jsonData[ind].icon;
	            var nodeVal = jsonData[ind].node;
	            tmpObj.node = jsonData[ind].node;
	            tmpObj.templateIDLong = nodeVal;
	            tmpObj.modifiable = jsonData[ind].modifiable;
	            tmpObj.leaf = jsonData[ind].leaf;
	            tmpObj.templateID = nodeVal.substring(nodeVal.lastIndexOf("_") + 1);
	            tmpObj.reportConfigNeeded = jsonData[ind].reportConfigNeeded;
	            tmpObj.categoryType = jsonData[ind].categoryType;
	            tmpObj.deletable = jsonData[ind].deletable;
	            tmpObj.iconCls = jsonData[ind].iconCls;
	            storeData.push(tmpObj);
	        }

	        var myStore = Ext.create('Ext.data.Store', {
	            id : 'imagesStore',
	            fields : [ {
	                name : 'text',
	                type : 'string'
	            }, {
	                name : 'typeLabel',
	                type : 'string'
	            }, {
	                name : 'templateID',
	                type : 'string'
	            }, {
	                name : 'node',
	                type : 'string'
	            }, {
	                name : 'templateIDLong',
	                type : 'string'
	            }, {
	                name : 'modifiable',
	                type : 'boolean'
	            }, {
	                name : 'reportConfigNeeded',
	                type : 'boolean'
	            }, {
	                name : 'deletable',
	                type : 'boolean'
	            }, {
	                name : 'categoryType',
	                type : 'string'
	            }, {
	                name : 'iconCls',
	                type : 'string'
	            }, {
	                name : 'leaf',
	                type : 'boolean'
	            } ],
	            data : storeData
	        });

	        var mosaicTmpl = new Ext.XTemplate(
	                '<tpl for=".">',
	                '<div class="mosaicDiv">',
	                '<tpl if="leaf===true">',
	                '<a class="example-image-link" href="reportDatasource!showPreviewImage.action?templateID={templateID}&leaf={leaf}&iconCls={iconCls}" data-lightbox="example-set">',
	                '<img class = "mosaicImageReport" src="reportDatasource!showPreviewImage.action?templateID={templateID}&leaf={leaf}" />',
	                '<p class = "titeLabel" >{text}/{typeLabel}</p>',
	                '</a>',
	                '</tpl>',

	                '<tpl if="leaf===false">',
	                '<tpl if="iconCls===\'projects-ticon\'">',
	                '<img class = "mosaicImageFolder" src="reportDatasource!showPreviewImage.action?templateID={templateID}&leaf={leaf}&iconCls={iconCls}" />',
	                '<p class = "titeLabel">{text}</p>',
	                '</tpl>',
	                '<tpl if="iconCls===\'folder\'">',
	                '<img class = "mosaicImageFolder" src="reportDatasource!showPreviewImage.action?templateID={templateID}&leaf={leaf}&iconCls={iconCls}" />',
	                '<p class = "titeLabel">{text}</p>', '</tpl>', '</tpl>', '</div>', '</tpl>',
	                '<div class="x-clear"></div>');

	        var dataViewListeners = {};
	        this.dataView = Ext.create('Ext.view.View', {
	            store : myStore,
	            tpl : mosaicTmpl,
	            cls : 'reportsCenterContent',
	            itemSelector : 'div.mosaicDiv',
	            overItemCls : 'x-item-over',
	            simpleSelect : false,
	            darggable : true,

	            listeners : {
	                selectionchange : function(dv, selections) {
	                    me.onDataViewChange(dv, selections);
	                },
	                itemcontextmenu : function(dataView, record, item, index, evtObj) {
	                    me.onGridRowCtxMenu(dataView, record, item, index, evtObj);
	                },
	                itemdblclick : function(dataView, record, item, index, evtObj) {
	                    me.itemdblclick(dataView, record, item, index, evtObj);
	                }
	            },
	            multiSelect : true,
	            trackOver : true

	        });
	        me.initDraDrop();

	        this.centerPanel = this.dataView;
	        if (this.replaceCenterPanel) {
	            this.replaceCenterPanel.call(this, this.centerPanel);
	        } else {
	            this.mainPanel.add(this.centerPanel);
	        }
	    },

		replaceCenterPanel:function(centerPanel){
			var me = this;
			me.getRepCfgLayout().borderLayoutController.setCenterContent.call(me.getRepCfgLayout().borderLayoutController, centerPanel);
		},

	    initDraDrop : function() {
	        var me = this;
	        this.dataView.on('render', function(v) {
	            me.dataView = new Ext.dd.DragZone(me.dataView.getEl(), {
	                getDragData : function(e) {
	                    var sourceEl = e.getTarget('div.mosaicDiv', 10);
	                    if (sourceEl) {
	                        d = sourceEl.cloneNode(true);
	                        d.id = Ext.id();
	                        sourceEl.cls = '';
	                        return {
	                            ddel : d,
	                            sourceEl : sourceEl,
	                            repairXY : Ext.fly(sourceEl).getXY(),
	                            sourceStore : v.store,
	                            item : v.getRecord(sourceEl)
	                        }
	                    }
	                },
	                getRepairXY : function() {
	                    return this.dragData.repairXY;
	                }
	            });

	            me.dataView.dropZone = Ext.create('Ext.dd.DropZone', me.dataView.getEl(), {
	                getTargetFromEvent : function(e) {
	                    var sourceEl = e.getTarget('div.mosaicDiv', 10);
	                    if (sourceEl) {
	                        return {
		                        item : v.getRecord(sourceEl)
	                        }
	                    }
	                },

	                onNodeDrop : function(target, dd, e, data) {
	                    var nodeTo = target.item;
	                    nodeTo.data.id = nodeTo.data.node;
	                    var nodeFrom = data.item;
	                    nodeFrom.data.id = nodeFrom.data.node;
	                    if (me.dataViewDropIsAllowed(nodeFrom.data, nodeTo.data)) {
	                        me.onDropDataViewNode(nodeFrom, nodeTo, false);
	                    }
	                },

	                onNodeOver : function(target, dd, e, data) {
	                    var nodeTo = target.item.data;
	                    var nodeFrom = data.item.data;
	                    if (me.dataViewDropIsAllowed(nodeFrom, nodeTo)) {
	                        return Ext.dd.DropZone.prototype.dropAllowed;
	                    } else {
	                        return Ext.dd.DropZone.prototype.dropNotAllowed;
	                    }
	                }
	            });

	            // init drop area for tree
	            me.tree.dropZone = Ext.create('Ext.dd.DropZone', me.tree.getEl(), {
	                getTargetFromEvent : function(e) {
	                    var sourceEl = e.getTarget('.x-grid-row', 10);
	                    if (sourceEl) {
	                        return {
		                        item : me.tree.getView().getRecord(sourceEl)
	                        }
	                    }
	                },
	                onNodeDrop : function(target, dd, e, data) {
	                    var nodeTo = target.item;
	                    nodeTo.data.id = nodeTo.raw.id;
	                    var nodeFrom = data.item;
	                    nodeFrom.data.id = nodeFrom.data.node;
	                    if (me.dataViewDropIsAllowed(nodeFrom.data, nodeTo.data)) {
	                        me.onDropDataViewNode(nodeFrom, nodeTo, false);
	                    }
	                },

	                onNodeOver : function(target, dd, e, data) {
	                    var nodeTo = target.item.data;
	                    var nodeFrom = data.item.data;
	                    if (me.dataViewDropIsAllowed(nodeFrom, nodeTo)) {
	                        return Ext.dd.DropZone.prototype.dropAllowed;
	                    } else {
	                        return Ext.dd.DropZone.prototype.dropNotAllowed;
	                    }
	                }
	            });
	        });
	    },

	    dataViewDropIsAllowed : function(nodeFrom, nodeTo) {
	        if (nodeTo.leaf || nodeTo.node === nodeFrom.node) {
	            return false;
	        }
	        return true;
	    },

	    /**
		 * Enable/disable actions based on the actual selection
		 */
	    onDataViewChange : function(view, selections) {
	        this.enableDisableToolbarButtons(view, selections);
	        var selectedRow = null;
	        if (selections  && selections.length > 0) {
	            selectedRow = selections[0];
	            this.selectedRecord = selections[0];
	            this.selectedRecords = selections;
	        }
	        this.adjustToolbarButtonsTooltip(selectedRow, false);
	    },

	    /**
		 * Automatically expand the private repository
		 */
	    onTreeNodeLoad : function(treeStore, node) {
	        var me = this;
	        if (node.isRoot() && CWHF.isNull(this.projectID)) {
	            for ( var ind in node.childNodes) {
	                // Load and expand public reports
	                if (node.childNodes[ind].data.id === "report_2") {
	                    treeStore.load({
	                        node : node.childNodes[ind],
	                        callback : function() {
		                        node.childNodes[ind].expand()
	                        }
	                    });
	                    me.params = {};
	                    me.params.node = node.childNodes[ind].data.id;
	                    me.nodeIDToSelect = node.childNodes[ind].data.id;
	                    me.getDataViewPanelData();
	                }
	            }
	        }
	    },

	    enableDisableToolbarButtons : function(view, arrSelections) {
	        if (CWHF.isNull(arrSelections) || arrSelections.length === 0) {
	            this.actionDeleteGridRow.setDisabled(true);
	            this.actionEditGridRow.setDisabled(true);
	            this.actionExecuteGridRow.setDisabled(true);
	            this.actionDownloadGridRow.setDisabled(true);
	        } else {
	            if (arrSelections.length === 1) {
	                var selectedRecord = arrSelections[0];
	                var isLeaf = selectedRecord.data.leaf;
	                var modifiable = selectedRecord.data.modifiable;
	                this.actionEditGridRow.setDisabled(!modifiable);
	                this.actionExecuteGridRow.setDisabled(!isLeaf);
	                this.actionDownloadGridRow.setDisabled(!isLeaf);
	            } else {
	                // more than one selection
	                this.actionEditGridRow.setDisabled(true);
	                this.actionExecuteGridRow.setDisabled(true);
	                this.actionDownloadGridRow.setDisabled(true);
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
		 * Handler for edit
		 */
	    onEdit : function(fromTree) {
	        var operation = "edit";
	        var isLeaf = this.selectedIsLeaf(fromTree);
	        var title = this.getTitle(this.getEditTitleKey(), {
	            isLeaf : isLeaf,
	            fromTree : fromTree,
	            selectedRecord : this.getLastSelected(fromTree)
	        });
	        var loadParams = {};
	        var submitParams = {};
	        var reloadParams = {};
	        var reloadParamsFromResult = {};
	        var selectedRecord = {};
	        if (fromTree) {
	            loadParams = this.getEditParams(fromTree);
	            submitParams = this.getEditParams(fromTree);
	            reloadParams = this.getEditReloadParamsAfterSave(fromTree);
	            reloadParamsFromResult = this.getEditReloadParamsAfterSaveFromResult(fromTree);
	            selectedRecord = this.getSingleSelectedRecord(fromTree);
	        } else {
	            selectedRecord = this.selectedRecord;
	            loadParams.node = this.selectedRecord.data.templateIDLong;
	            submitParams.node = this.selectedRecord.data.templateIDLong;
	            // reloadParams =
				// this.getEditReloadParamsAfterSave(fromTree);
	        }

	        return this.onAddEdit(title, selectedRecord, operation, isLeaf, false, fromTree, loadParams,
	                submitParams, reloadParams, reloadParamsFromResult);
	    },

	    /**
		 * Add a leaf: filter or report
		 */
	    onEditTreeNode : function() {
	        this.onEdit(true);
	    },

	    /**
		 * Get the node to select after save after edit operation
		 */
	    getEditReloadParamsAfterSaveFromResult : function(fromTree) {
	        if (fromTree) {
	            // edited from tree: select
	            return [ {
	                parameterName : 'nodeIDToSelect',
	                fieldNameFromResult : 'node'
	            }, {
	                parameterName : 'rowToSelect',
	                fieldNameFromResult : 'node'
	            } ];
	        }
	    },

	    /**
		 * Create the edit
		 */
	    onTreeNodeDblClick : function(view, record) {
	        var readOnly = record.data['readOnly'];
	        if (CWHF.isNull(readOnly) || readOnly === false) {
	            this.onEditTreeNode();
	        }
	    },

	    /**
		 * Handler for add/edit a node/row title:
		 * 'add'/'edit'/'copy' recordData: the selected record (tree
		 * node data or grid row data) operation: "edit"/"add" or
		 * anything else in the derived classes isLeaf: whether to
		 * add a leaf or a folder add: whether it is add or edit
		 * fromTree: operations started from tree or from grid
		 * loadParams submitParams refreshParams
		 * refreshParamsFromResult
		 */
	    onAddEdit : function(title, record, operation, isLeaf, add, fromTree, loadParams, submitParams,
	            refreshParams, refreshParamsFromResult) {
	        var recordData = null;
	        if (record ) {
	            recordData = record.data;
	        }
	        var width = this.getEditWidth(recordData, isLeaf, add, fromTree, operation);
	        var height = this.getEditHeight(recordData, isLeaf, add, fromTree, operation);
	        var loadUrl = this.getEditUrl(isLeaf);
	        var load = {
	            loadUrl : loadUrl,
	            loadUrlParams : loadParams
	        };
	        var submitUrl = this.getSaveUrl(isLeaf);
	        var submit = {
	            submitUrl : submitUrl,
	            submitUrlParams : submitParams,
	            submitButtonText : this.getSubmitButtonLabel(operation),
	            submitHandler : this.submitHandler,
	            submitAction : operation,
	            refreshAfterSubmitHandler : this.reload,
	            refreshParametersBeforeSubmit : refreshParams,
	            refreshParametersAfterSubmit : refreshParamsFromResult
	        };
	        var postDataProcess = this.getEditPostDataProcess(record, isLeaf, add, fromTree, operation);
	        var preSubmitProcess = this.getEditPreSubmitProcess(recordData, isLeaf, add);
	        var items = this.getPanelItems(recordData, isLeaf, add, fromTree, operation);
	        /*var additionalActions = this.getAdditionalActions(recordData, submitParams, operation, items);
	        if (additionalActions ) {
	            additionalActions.push(submit);
	            submit = additionalActions;
	        }*/
	        var windowParameters = {
	            title : title,
	            width : width,
	            height : height,
	            load : load,
	            submit : submit,
	            items : items,
	            postDataProcess : postDataProcess,
	            preSubmitProcess : preSubmitProcess
	        };
	        var extraWindowParameters = this.getExtraWindowParameters(recordData, operation);
	        if (extraWindowParameters ) {
	            for (propertyName in extraWindowParameters) {
	                windowParameters[propertyName] = extraWindowParameters[propertyName];
	            }
	        }
	        var windowConfig = Ext.create('com.trackplus.util.WindowConfig', windowParameters);
	        windowConfig.showWindowByConfig(this);
	    },

	    /**
		 * Reload method
		 */
	    reload : function(params) {
	        var me = this;
	        params.reloadTree = true;
	        com.trackplus.util.RefreshAfterSubmit.refreshTreeAfterSubmit.call(this, params);
	        me.getDataViewPanelData();
	    },

	    /**
		 * Handler for adding a folder node
		 */
	    onAddFolder : function() {
	        var operation = "addFolder";
	        var title = this.getTitle(this.getAddTitleKey(), {
	            isLeaf : false,
	            selectedRecord : this.getLastSelected(true)
	        });
	        var loadParams = this.getAddFolderParams();
	        var submitParams = this.getAddFolderParams();
	        var reloadParams = this.getAddReloadParamsAfterSave(false);
	        var reloadParamsFromResult = this.getAddSelectionAfterSaveFromResult();
	        var selectedRecord = this.getSingleSelectedRecord(true);
	        if (CWHF.isNull(selectedRecord)) {
	            selectedRecord = this.tree.getRootNode();
	        }
	        return this.onAddEdit(title, selectedRecord, operation, false, true, true, loadParams,
	                submitParams, reloadParams, reloadParamsFromResult);
	    },

	    /**
		 * Add a leaf: filter or report
		 */
	    onAddLeaf : function() {
	        var operation = "addLeaf";
	        var title = this.getTitle(this.getAddTitleKey(), {
	            isLeaf : true,
	            selectedRecord : this.getLastSelected(true)
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
		 * Download the report for a tree node
		 */
	    onDownloadTreeNode : function() {
	        this.downloadReport(true);
	    },

	    /**
		 * Downloads a report zip
		 */
	    downloadReport : function(fromTree) {
	        if (fromTree) {
	            var recordData = this.getSingleSelectedRecordData(fromTree);
	            if (recordData ) {
	                var leaf = this.selectedIsLeaf(fromTree);
	                var node = this.getRecordID(recordData, {
	                    fromTree : fromTree
	                });
	                attachmentURI = this.getBaseAction() + '!download.action?node=' + node;
	                window.open(attachmentURI);
	            }
	        } else {
	            var leaf = this.selectedRecord.data.leaf;
	            var node = this.selectedRecord.data.templateIDLong;
	            attachmentURI = this.getBaseAction() + '!download.action?node=' + node;
	            window.open(attachmentURI);
	        }
	    },

	    /**
		 * The struts action for delete/replace
		 */
		getDeleteUrlBase: function(extraConfig) {
			return "categoryConfig";
		},

	    /**
		 * Delete handler for deleting from the tree
		 */
	    onDeleteFromTree : function() {
	        this.onDelete(true);
	    },

	    /**
		 * Delete handler for deleting from the grid
		 */
	    onDeleteFromGrid : function() {
	        this.onDelete(false);
	    },

	    /**
		 * Handler for delete
		 */
	    onDelete : function(fromTree) {
	        var selectedRecords = [];
	        if (fromTree) {
	            selectedRecords = this.getSelectedRecords(fromTree);
	        } else {
	            var obj = {};
	            obj.data = this.selectedRecords[0];
	            selectedRecords = this.selectedRecords;
	        }
	        if (selectedRecords ) {
	            var isLeaf = this.selectedIsLeaf(fromTree);
	            var extraConfig = {
	                fromTree : fromTree,
	                isLeaf : isLeaf
	            };
	            this.deleteHandler(selectedRecords, extraConfig);
	        }
	    },

	    /**
		 * Cut a tree node
		 */
	    onCutTreeNode : function() {
	        this.cutCopyNode = this.selectedNode;
	        this.copy = false;
	    },

	    /**
		 * Copy a tree node
		 */
	    onCopyTreeNode : function() {
	        this.cutCopyNode = this.selectedNode;
	        this.copy = true;
	    },

	    /**
		 * Paste a node in the tree after copy/cut
		 */
	    onPasteTreeNode : function() {
	        this.onDropTreeNode(this.cutCopyNode, this.selectedNode, this.copy);
	        this.cutCopyNode = null;
	    },

	    /**
		 * Show the context menu in grid
		 */
	    onGridRowCtxMenu : function(dataView, record, item, index, evtObj) {
	        evtObj.stopEvent();
	        this.selectedRecord = record;

	        var selectionIsSimple = this.selectionIsSimple(false);
	        var actions = this.getContextMenuActions(record, selectionIsSimple, false);

	        if (actions  && actions.length > 0) {
	            var treeNodeCtxMenu = this.createContextMenu(record, actions);
	            this.adjustContextMenuText(treeNodeCtxMenu, record, false);
	            treeNodeCtxMenu.showAt(evtObj.getXY());
	        }
	        return false;
	    },

	    itemdblclick : function(dataView, record, item, index, evtObj) {
	        this.params = {};
	        var me = this;
	        this.params.node = record.data.node;
	        this.nodeIDToSelect = record.data.node;
	        this.treePanel = this.tree;
	        // com.trackplus.util.RefreshAfterSubmit.selectTreeNodeAfterReload.call(this);
	        this.getDataViewPanelData();
	    },

	    /**
		 * Download the report for the grid row
		 */
	    onDownloadGridRow : function() {
	        this.downloadReport(false);
	    },

	    /**
		 * Add a leaf: filter or report
		 */
	    onEditGridRow : function() {
	        this.onEdit(false);
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
	        if (fromTree) {
	            var recordData = this.getSingleSelectedRecordData(fromTree);
	            if (recordData ) {
	                var leaf = this.selectedIsLeaf(fromTree);
	                var node = this.getRecordID(recordData, {
	                    fromTree : fromTree
	                });
	                if (leaf) {
	                    var lastIndex = node.lastIndexOf("_");
	                    var objectID = node.substring(lastIndex + 1);
	                    com.trackplus.admin.Report.executeReport(this, objectID,
	                            recordData["reportConfigNeeded"], false);
	                }
	            }
	        } else {
	            var leaf = this.selectedRecord.data.leaf;
	            var node = this.selectedRecord.data.node
	            var repConfigNeeded = this.selectedRecord.data.reportConfigNeeded;
	            if (leaf) {
	                var lastIndex = node.lastIndexOf("_");
	                var objectID = node.substring(lastIndex + 1);
	                com.trackplus.admin.Report.executeReport(this, objectID, true, false);
	            }
	        }
	    },

	    /**
		 * The localized entity name
		 */
	    getEntityLabel : function(extraConfig) {
	        var entityLabel = null;
	        var isLeaf = true;
	        if (extraConfig ) {
	            isLeaf = extraConfig.isLeaf;
	        }
	        if (isLeaf) {
	            return getText("admin.customize.reportTemplate.lbl");
	        } else {
	            return getText('admin.customize.queryFilter.lbl.category');
	        }
	    },

	    /**
		 * The label for the save button
		 */
	    getSubmitButtonLabel : function(operation) {
	        if (operation === "instant") {
	            return getText(this.getExecuteReportButtonKey());
	        } else {
	            return getText(this.getSaveButtonKey());
	        }
	    },

	    /**
		 * The url for getting the leaf detail: either this should
		 * be overridden or the leafDetailUrl should be specified in
		 * the config
		 */
	    getLeafDetailUrl : function() {
	        return this.getFolderAction() + '!leafDetail.action';
	    },

	    /**
		 * The message to appear first time after selecting this
		 * menu entry Is should be shown by selecting the root but
		 * the root is typically not visible
		 */
	    getRootMessage : function(rootID) {
	        return getText("admin.customize.reportTemplate.lbl.description");
	    },

	    getActionItemIdsWithContextDependentLabel : function() {
	        return [ "editGridRow", "editTreeNode", "deleteGridRow", "deleteTreeNode", /*"downloadGridRow",
	                "downloadTreeNode",*/ "cut", "copy" ];
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
		 * Get the extra parameters for the gridStore
		 */
	    getGridStoreExtraParams : function(node, opts) {
	        return {
	            node : this.selectedNodeID
	        }
	    },

	    /**
		 * Expanding the node
		 */
	    getTreeStoreExtraParams : function(node) {
	        var extraParams = {
	            excludePrivate : this.excludePrivate
	        };
	        if (this.projectID ) {
	            // in project configuration
	            extraParams["projectID"] = this.projectID;
	        }
	        return extraParams;
	    },

	    getAddFolderReportIconCls : function() {
	        return 'categoryReportAdd';
	    },

	    getAddReportIconCls : function() {
	        return 'rtemplateAdd';
	    },

	    getEditReportIconCls : function() {
	        return 'rtemplateEdit';
	    },

	    /**
		 * Initialize all actions and return the toolbar actions
		 */
	    getToolbarActions : function() {
	        return this.getReportToolbarItems();
	    },

	    /**
		 * Toolbar items for report
		 */
	    getReportToolbarItems : function() {
	        return [ this.actionAddFolder, this.actionAddLeaf, this.actionEditGridRow,
	                this.actionExecuteGridRow, this.actionDownloadGridRow, this.actionDeleteGridRow ];
	    },

	    /**
		 * Which actions to enable/disable depending on tree
		 * selection
		 */
	    getToolbarActionChangesForTreeNodeSelect : function(selectedNode) {
	        var canAddChild = false;
	        if (selectedNode ) {
	            if (this.projectID  && selectedNode.isRoot()) {
	                // after initializing the project specific
					// branch for issue filter and report:
	                // although in the filter/report tree no node is
					// selected, the add refers to the project
					// specific root
	                // (for notify filter the add methods are
					// allowed only after an explicit selection
	                // in notify filters branch, because there are
					// both the public and project (for the actual
					// project)
	                // specific notification filters so it is not
					// obvious where to add when nothing is selected
	                // selectedNode.isRoot(): after deleting a node
					// directly below root
	                canAddChild = true;
	            } else {
	                canAddChild = selectedNode.data['canAddChild'];
	            }
	        }
	        this.actionAddFolder.setDisabled(!canAddChild);
	        this.actionAddLeaf.setDisabled(!canAddChild);
	        // nothing selected in the grid
	        this.actionEditGridRow.setDisabled(true);
	        this.actionDeleteGridRow.setDisabled(true);
	        this.actionExecuteGridRow.setDisabled(true);
	        this.actionDownloadGridRow.setDisabled(true);
	        // this.actionExport.setDisabled(true);
	    },

	    getLastSelected : function(fromTree) {
	        if (fromTree) {
	            return this.getLastSelectedTreeNode();
	        } else {
	            return this.selectedRecord.data;
	        }
	    },

	    getSelection : function(fromTree) {
	        if (fromTree) {
	            return this.getTreeSelection();
	        } else {
	            return this.selectedRecords;
	        }
	    },

	    getShowGridForLeaf : function() {
	        return true;
	    },

	    selectedIsLeaf : function(fromTree) {
	        var lastSelectedRecord = this.getLastSelected(fromTree);
	        if (lastSelectedRecord ) {
	            if (fromTree) {
	                return lastSelectedRecord.isLeaf();
	            } else {
	                return lastSelectedRecord.leaf;
	            }
	        }
	        return true;
	    },

	    /**
		 * Get the refresh parameters after delete
		 */
	    getReloadParamsAfterDelete : function(selectedRecords, extraConfig, responseJson) {
	        if (selectedRecords ) {
	            // we suppose that only one selection is allowed in
				// tree
	            var selNode = selectedRecords;
	            if (selNode ) {
	                var parentNode = null;
	                var parentNodeID = null;
	                if (extraConfig ) {
	                    fromTree = extraConfig.fromTree;
	                    if (fromTree) {
		                    // delete from tree
		                    parentNode = selNode.parentNode;
		                    if (parentNode ) {
			                    parentNodeID = parentNode.data.id;
			                    // select the parent of the deleted
								// node for reload and select
			                    return {
			                        nodeIDToReload : parentNodeID,
			                        nodeIDToSelect : parentNodeID
			                    };
		                    }
	                    } else {
		                    // delete from grid: the parent is
							// selected already in tree, leave that
							// to be reloaded and selected
		                    if (this.getShowGridForLeaf() && this.selectedNode.isLeaf()) {
			                    // in the tree a leaf node selected
								// -> grid with a single row: the
								// parent of the selected tree node
								// should be reloaded
			                    var parentNode = this.selectedNode.parentNode;
			                    if (parentNode ) {
				                    // the parent of the edited node
									// should be reloaded
				                    return {
					                    nodeIDToReload : parentNode.data['id']
				                    }
			                    }
		                    } else {
			                    // in the tree the parent of the
								// edited grid row is selected: the
								// actually selected tree node
								// should be reloaded
			                    return {
				                    nodeIDToReload : this.selectedNode.data['id']
			                    };
		                    }
	                    }
	                }
	            }
	        }
	        return null;
	    },

	    /**
		 * Get the node to reload after save after add operation
		 */
	    /* protected */getAddReloadParamsAfterSave : function(addLeaf) {
	        if (this.selectedNode ) {
	            var leaf = this.selectedNode.data['leaf'];
	            if (leaf) {
	                // selected node is leaf: add to a leaf
	                var parentNode = this.selectedNode.parentNode;
	                if (parentNode ) {
	                    if (addLeaf) {
		                    // add leaf to a leaf -> means add
							// sibling -> the parent of the
							// selectedNode should be reloaded
		                    return {
			                    nodeIDToReload : parentNode.data['id']
		                    };
	                    } else {
		                    // add folder when a leaf is selected ->
							// add sibling to the parent's node, the
							// parent of the parent should be
							// reloaded
		                    // (from tree context menu it is not
							// possible, only from toolbar)
		                    var parentNode = parentNode.parentNode;
		                    if (parentNode ) {
			                    return {
				                    nodeIDToReload : parentNode.data['id']
			                    };
		                    }
	                    }
	                }
	            } else {
	                // selected node is folder
	                // if (addLeaf) {
	                return {
	                    nodeIDToReload : this.selectedNode.data['id']
	                };
	                /*
					 * } else { //add folder when a folder is
					 * selected var parentNode =
					 * this.selectedNode.parentNode; if
					 * (parentNode) { return {nodeIDToReload:
					 * parentNode.data['id']}; } }
					 */
	            }
	        }
	        return null;
	    },

	    /**
		 * Get the node to select after save after add operation
		 */
	    getAddSelectionAfterSaveFromResult : function() {
	        // specify nodeIDToSelect to select the added node based
			// on the 'node' field from resulting JSON,
	        // do not specify rowToSelect, do not select anything in
			// the grid after add
	        // return {parameterName:'nodeIDToSelect',
			// fieldNameFromResult:'node'};
	        return [ {
	            parameterName : 'nodeIDToSelect',
	            fieldNameFromResult : 'node'
	        }, {
	            parameterName : 'rowToSelect',
	            fieldNameFromResult : 'node'
	        } ];
	    },

	    /**
		 * Url for editing an entity We suppose that add/edit use
		 * the same edit method on server side Differentiation is
		 * made based on "node", "add" and "leaf" request parameters
		 * addFolder: "node" is the parent node, "add" is true,
		 * "leaf" is false addLeaf: "node" is the parent node, "add"
		 * is true, "leaf" is true edit: "node" is the id of the
		 * edited entity (whether it is folder or leaf is decoded
		 * based on the structure of the "node")
		 */
	    getEditUrl : function(isLeaf) {
	        return this.getNodeBaseAction({
	            isLeaf : isLeaf
	        }) + '!edit.action';
	    },

	    /**
		 * Url for saving of an entity
		 */
	    getSaveUrl : function(isLeaf) {
	        return this.getNodeBaseAction({
	            isLeaf : isLeaf
	        }) + '!save.action';
	    },

	    getEditPreSubmitProcess : function(recordData, isLeaf, add) {
	        if (isLeaf) {
	            if (this.getEditLeafPreSubmitProcess ) {
	                return this.getEditLeafPreSubmitProcess(recordData, add);
	            }
	        } else {
	            if (this.getEditFolderPreSubmitProcess ) {
	                return this.getEditFolderPreSubmitProcess(recordData, add);
	            }
	        }
	        return null;
	    },

	    /**
		 * Get the panel items recordData: the record data (for the
		 * record to be edited or added to) isLeaf: whether add a
		 * leaf or a folder add: whether it is add or edit fromTree:
		 * operations started from tree or from grid operation: the
		 * name of the operation
		 */
	    getPanelItems : function(recordData, isLeaf, add, fromTree, operation) {
	        if (isLeaf) {
	            return this.getEditLeafPanelItems(recordData, add, fromTree, operation);
	        } else {
	            return this.getEditFolderPanelItems(recordData, add, fromTree, operation);
	        }
	    },

	    /**
		 * Add extra window configuration fields by add/edit
		 * windowConfiguration argument is configured with the
		 * required fields but any already specified windowConfig
		 * field can be overridden, and further optional window
		 * options can be specified type: typically "folder" or
		 * "leaf" but for more leaf types it can be customized
		 */
	    getExtraWindowParameters : function(recordData, operation) {
	        if (operation === "addLeaf") {
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
		 * Prepare adding/editing a report or filter category
		 */
	    getEditFolderPanelItems : function(data, add, fromTree, operation) {
	        var modifiable = false;
	        if (add) {
	            modifiable = true;
	        } else {
	            var modifiable = data.modifiable;
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
	            var modifiable = data.modifiable;
	        }
	        return this.getReportItems(modifiable, add);

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
	            windowItems.push(CWHF.createFileField('admin.customize.reportTemplate.lbl.reportFile',
	                    "reportFile", {
	                        disabled : !modifiable,
	                        allowBlank : false,
	                        labelWidth : this.labelWidth,
	                        itemId     : "reportFile"
	                    }));
	        }
	        return windowItems;
	    },

	    /**
		 * Gets the base action depending on the rootID
		 */
	    getEditWidth : function(recordData, isLeaf, add, fromTree, operation) {
	        if (isLeaf) {
	            return this.reportEditWidth;
	        } else {
	            return this.folderEditWidth;
	        }
	    },

	    /**
		 * Gets the base action depending on the rootID
		 */
	    getEditHeight : function(recordData, isLeaf, add, fromTree, operation) {
	        if (isLeaf) {
	            return this.reportEditHeight;
	        } else {
	            return this.folderEditHeight;
	        }
	    },

	    /**
		 * Get the node to reload after save after edit operation
		 */
	    getEditReloadParamsAfterSave : function(fromTree) {
	        if (this.selectedNode ) {
	            if (fromTree) {
	                // edited/copied from tree
	                var parentNode = this.selectedNode.parentNode;
	                if (parentNode ) {
	                    // the parent of the edited node should be
						// reloaded
	                    return {
		                    nodeIDToReload : parentNode.data['id']
	                    }
	                }
	            } else {
	                if (this.getShowGridForLeaf() && this.selectedNode.isLeaf()) {
	                    // in the tree a leaf node selected -> grid
						// with a single row: the parent of the
						// selected tree node should be reloaded

	                    var parentNode = this.selectedNode.parentNode;
	                    if (parentNode ) {
		                    // the parent of the edited node should
							// be reloaded
		                    return {
			                    nodeIDToReload : parentNode.data['id']
		                    }
	                    }
	                } else {
	                    // in the tree the parent of the edited grid
						// row is selected: the actually selected
						// tree node should be reloaded
	                    return {
		                    nodeIDToReload : this.selectedNode.node
	                    };
	                }
	            }
	        }
	        return null;
	    },

	    /**
		 * Get the actions available in context menu depending on
		 * the currently selected row
		 *
		 */
	    getGridContextMenuActions : function(selectedRecord, selectionIsSimple) {
	        var modifiable = selectedRecord.data['modifiable'];
	        var leaf = selectedRecord.data.leaf;
	        var actions = [];
	        if (leaf) {
	            // leaf
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
		 * Get the actions available in context menu depending on
		 * the currently selected row
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
	        if (this.useCopyPaste) {
	            if (canCopy) {
	                if (modifiable) {
	                    actions.push(this.actionCutTreeNode);
	                }
	                actions.push(this.actionCopyTreeNode);
	            }
	            if (canAddChild && this.cutCopyNode ) {
	                actions.push(this.actionPasteTreeNode);
	            }
	        }
	        if (deletable) {
	            actions.push(this.actionDeleteTreeNode);
	        }
	        return actions;
	    },

	    /**
		 * Get the context menu actions either for grid or for tree
		 */
	    getContextMenuActions : function(selectedRecords, selectionIsSimple, fromTree) {
	        if (fromTree) {
	            return this.getTreeContextMenuActions(selectedRecords, selectionIsSimple);
	        } else {
	            return this.getGridContextMenuActions(selectedRecords, selectionIsSimple);
	        }
	    },

	    /**
		 * Helper for preparing the params get the ID based on the
		 * record and extra config
		 */
	    getRecordID : function(recordData, extraConfig) {
	        var fromTree = null;
	        if (extraConfig ) {
	            fromTree = extraConfig.fromTree;
	        }
	        if (fromTree) {
	            return recordData['id'];
	        } else {
	            return recordData['node'];
	        }
	    },

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

	    getDragDropBaseAction : function(draggedNodeIsLeaf) {
	        return this.getFolderAction();
	    },

	    /**
		 * Paste a node in the tree after copy/cut
		 */
	    onDropDataViewNode : function(nodeFrom, nodeTo, copy) {
	        var me = this;
	        var isLeaf = nodeFrom.data.leaf;// this.selectedIsLeaf(nodeFrom);
	        var strutsBaseAction = this.getDragDropBaseAction(isLeaf);

	        Ext.Ajax.request({
	            fromCenterPanel : true,
	            url : strutsBaseAction + '!copy.action',
	            params : {
	                nodeFrom : nodeFrom.data["id"],
	                nodeTo : nodeTo.data["id"],
	                copy : copy
	            },
	            scope : this,
	            disableCaching : true,
	            success : function(response) {
	                var responseJson = Ext.decode(response.responseText);
	                if (responseJson.success === true) {
	                    me.reload({
	                        nodeIDToReload : nodeTo.data["id"],
	                        nodeIDToSelect : nodeTo.data["id"]
	                    });
	                } else {
	                    if (responseJson.errorMessage ) {
	                        // no right to delete
	                        Ext.MessageBox.alert(this.failureTitle, responseJson.errorMessage)
	                    }
	                }
	            },
	            failure : function(result) {
	                Ext.MessageBox.alert(this.failureTitle, result.responseText)
	            },
	            method : "POST"
	        });
	    },

	    isTreeSplitOn: function() {
	    	return false;
	    }

	});
