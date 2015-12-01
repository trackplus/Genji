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

Ext.define('com.trackplus.wiki.WestPanelView',{
	extend:'Ext.tree.Panel',
	config:{
		data:{},
		treeBaseAction:'wiki',
		model:null,
		queryContextID:null
	},

	useArrows: true,
	autoScroll: true,
	rootVisible: false,
	border: false,
	region:'west',
	cls:'westTreeNavigator',
	bodyBorder:false,
	margins:Ext.isIE?'0 -4 0 0':'0 -5 0 0',
	width:200,
	frame: false,
	collapsible:true,
	collapseDirection:'top',
	collapseFirst:'true',
	//title:getText('common.lbl.queries'),
	//iconCls:'queryView',
	collapseMode:'mini',
	collapsedCls:'westTreeNavigatorCollapsed',
	split:true,
	stateful:true,
	stateId :'wiki_westNavigator',
	NODE_TYPES: {
		PROJECT:'1',
		RELEASE: '2',
		DOCUMENT: '3',
		DOCUMENT_SECTION: '4',
		DOCUMENT_FOLDER: '5'
	},
	listeners:{
		'staterestore':{
			fn:function(panel,state){
				var w=state.width;
				var appWidth=borderLayout.getWidth();
				if(w>appWidth+100){
					w=appWidth-100;
					if(w<0){
						w=50
					}
					panel.width=w;
				}
			}
		}
	},
	canDropNode: function(node, data, overModel, dropPosition, dropHandlers, eOpts) {
		var me=this;
		var nodeToDrag=data.records[0];
		var nodeType=nodeToDrag.data.nodeType;
		if(nodeType===me.NODE_TYPES.PROJECT||nodeType===me.NODE_TYPES.RELEASE){
			return false;
		}
		var overNodeType=overModel.data.nodeType;
		//dropPosition :"before", "after" or "append"
		switch (overNodeType){
			case me.NODE_TYPES.PROJECT:
			case me.NODE_TYPES.RELEASE:{
				return dropPosition==="append";
			}
			case me.NODE_TYPES.DOCUMENT_FOLDER:{
				return true;
			}
			case me.NODE_TYPES.DOCUMENT:{
				if(dropPosition==='append'){
					return (nodeType === me.NODE_TYPES.DOCUMENT_SECTION);
				}else{
					return (nodeType === me.NODE_TYPES.DOCUMENT);
				}
			}
			case me.NODE_TYPES.DOCUMENT_SECTION:{
				return (nodeType === me.NODE_TYPES.DOCUMENT_SECTION);
			}
		}
		return true;
	},
	onDropTreeNode: function(node, data, overModel, dropPosition, eOpts) {
		var me=this;
		var dragNodeID=data.records[0].data.objectID;
		var nodeObjectID=overModel.data.objectID;
		var nodeType=overModel.data.nodeType;
		borderLayout.setLoading(true);
		var urlStr,params;
		if(dropPosition==='append') {
			urlStr = "wiki!dropOnNode.action";
			params={
				dragNodeID:dragNodeID,
				nodeObjectID:nodeObjectID,
				nodeType:nodeType
			};
		}else{
			var before=(dropPosition==="before");
			params={
				workItems:dragNodeID,
				targetWorkItemID:nodeObjectID,
				before:before
			};
			urlStr="itemNavigator!changeWBS.action";
		}

		Ext.Ajax.request({
			url: urlStr,
			params: params,
			disableCaching:true,
			success: function(response){
				borderLayout.setLoading(false);
				var nodeToReload=overModel;
				me.store.load({
					node:nodeToReload,
					callback:function(){
						me.fireEvent("wbsChange");
					},
					synchronous:false
				});

			}
		});
		return true;
	},

	initComponent: function(){
		var me=this;
		//me.title=" ";
		me.store = Ext.create('Ext.data.TreeStore', {
			fields: ['id','text','objectID','canDrop','verifyAllowDropAJAX','nodeType','useFilter','iconCls','icon','dropHandlerCls','cls'],
			root: {
				expanded: true,
				text:"",
				user:"",
				status:"",
				children:CWHF.isNull(me.data.projects)?[]:me.data.projects
			},
			proxy:{
				type: 'ajax',
				url: me.treeBaseAction+'!expandNode.action'
			}
		});
		me.viewConfig= {
			plugins: {
				ptype: 'treeviewdragdrop',
					dragGroup: 'wikiWestTreeDDGroup',
					dropGroup:  'wikiWestTreeDDGroup',
					//appendOnly: true,
					enableDrag: true,
					enableDrop: true
			},
			listeners: {
				'beforedrop': {
					scope:this,
					fn: function(node, data, overModel, dropPosition, dropHandlers, eOpts) {
						return me.canDropNode(node, data, overModel, dropPosition, dropHandlers, eOpts);
					}
				},
				'drop': {
					scope:me,
					fn:me.onDropTreeNode
				}
			}
		};
		me.addEvents("wbsChange");
		me.callParent();
		me.initMyListeners();
	},

	initMyListeners:function(){
		var me=this;
		me.store.on('beforeload',function( store, operation, eOpts){
			if(operation.node){
				var extraParams = me.getTreeExpandExtraParams.call(me,operation.node);
				if (extraParams) {
					me.store.proxy.extraParams = extraParams;
				}
			}
		});
		me.addListener('itemcontextmenu',me.onTreeNodeCtxMenu,me);
	},
	getTreeExpandExtraParams:function(node){
		var nodeObjectID=node.data.objectID;
		var nodeType=node.data.nodeType;
		return {
			nodeType:nodeType,
			nodeObjectID:nodeObjectID
		};
	},
	onTreeNodeCtxMenu: function(tree, record, item, index, evtObj) {
		var me=this;
		evtObj.stopEvent();
		me.createCtxMenu(tree, record,evtObj);
		return false;
	},
	createCtxMenu: function(tree, record,evtObj) {
		var me=this;
		if (record) {
			var nodeType = record.data["nodeType"];
			var nodeObjectID=record.data.objectID;
			if (nodeType) {
				me.createProjectReleaseCtxMenu(tree, record,evtObj);
			}
		}
	},
	createProjectReleaseCtxMenu:function(tree, record,evtObj) {
		var me=this;
		var entityID=record.data["objectID"];
		tree.setLoading(true);
		var urlStr="wiki!getProjectReleaseCtxMenu.action";
		var nodeObjectID=record.data.objectID;
		var nodeType=record.data.nodeType;
		var nodeID=record.data.id;
		Ext.Ajax.request({
			url: urlStr,
			params: {
				nodeType:nodeType,
				nodeObjectID:nodeObjectID
			},
			disableCaching:true,
			success: function(response){
				var responseJson = Ext.decode(response.responseText);
				var issueTypes=responseJson.data.issueTypes;
				var projectID=responseJson.data.projectID;
				var releaseID=responseJson.data.releaseID;
				var editRelease=responseJson.data.editRelease;
				var parentID=null;
				if(nodeType!==me.NODE_TYPES.PROJECT&&nodeType!=='release'){
					parentID=nodeObjectID;
				}
				var items = [];
				if(issueTypes&&issueTypes.length>0){
					for(var i=0;i<issueTypes.length;i++){
						var issueType=issueTypes[i];
						items.push({
							text:getText('common.lbl.add',issueType.label),
							icon:'optionIconStream.action?fieldID=-2&optionID='+issueType.id,
							scope:{
								issueTypeID:issueType.id,
								projectID:projectID,
								releaseID:releaseID,
								button:null,
								parentID:parentID
							},
							handler:function(btn){
								var afterCreateItemScope={
									me:me,
									nodeID:nodeID
								};
								borderLayout.controller.createNewIssue.call(borderLayout.controller,this.issueTypeID,this.projectID,this.releaseID,this.button,this.parentID,me.afterItemCreated,afterCreateItemScope);
							}
						});
					}
				}
				if(nodeType===me.NODE_TYPES.DOCUMENT){
					if(items.length>0) {
						items.push('-');
					}
					items.push({
						text:getText('common.btn.export'),
						iconCls:'export',
						scope: this,
						handler:function() {
							var exportDocx = Ext.create("com.trackplus.admin.action.ExportDocx", {workItemID:nodeObjectID});
							exportDocx.createExportForm();
						}
					});
				}
				if(nodeType!==me.NODE_TYPES.PROJECT&&nodeType!==me.NODE_TYPES.RELEASE){
					if(items.length>0) {
						items.push('-');
					}
					items.push({
						text:getText('common.btn.delete'),
						iconCls:'itemAction_delete16',
						scope: this,
						handler:function(){
							var node=me.getStore().getNodeById(nodeID);
							var parentNode=node.parentNode;
							me.deleteWorkItem(parentNode,nodeObjectID);
						}
					});
				}
				tree.setLoading(false);
				if(items.length>0) {
					var contextMenu = new Ext.menu.Menu({
						items: items
					});
					contextMenu.showAt(evtObj.getXY());
				}
			},
			failure:function(){
				tree.setLoading(false);
			}
		})
	},

	deleteWorkItem:function(parentNode,workItemID){
		var me=this;
		var messageConfirmDelete=com.trackplus.TrackplusConfig.getText("common.lbl.messageBox.removeSelected.confirm");
		var titleDelete=com.trackplus.TrackplusConfig.getText("common.btn.delete")
		Ext.MessageBox.show({
			title:titleDelete,
			msg: messageConfirmDelete,
			buttons: Ext.MessageBox.YESNO,
			fn: function(btn){
				if(btn==="yes"){
					me.doDeleteItem(parentNode,workItemID);
				}
			},
			icon: Ext.MessageBox.QUESTION
		});
	},
	doDeleteItem:function(parentNode,workItemID){
		var me=this;
		borderLayout.setLoading(true);
		var urlStr='item!reverseDelete.action?workItemID='+workItemID;
		Ext.Ajax.request({
			url: urlStr,
			success: function(response){
				borderLayout.setLoading(false);
				var responseJson = Ext.decode(response.responseText);
				if (responseJson.success===true) {
					var scope={
						me:me,
						nodeIDToSelect:parentNode.data.id
					};
					me.store.load({
						node:parentNode,
						callback:me.selectTreeNodeAfterReload,
						synchronous:false,
						scope:scope
					});
				}else{
					if (responseJson.errorMessage) {
						//parent change for an issue
						Ext.MessageBox.show({
							title: getText('common.warning'),
							msg: responseJson.errorMessage,
							buttons: Ext.Msg.OK,
							icon: Ext.MessageBox.ERROR
						});
					}
				}
			},
			failure:function(){
				borderLayout.setLoading(false);
				var jsonData=Ext.decode(action.response.responseText);
				//com.trackplus.item.ItemErrorHandler.handleErrors(me,jsonData.data);
				//me.handleErrors.call(me,jsonData.data);
			}
		});
	},
	afterItemCreated:function(data){
		var scope=this;
		var me=scope.me;
		var nodeID=scope.nodeID;
		var workItemID=data.workItemID;
		var scope={
			me:me,
			nodeIDToSelect:"i_" + workItemID
		};
		var nodeToReload=me.getStore().getNodeById(nodeID);
		me.store.load({
			node:nodeToReload,
			callback:me.selectTreeNodeAfterReload,
			synchronous:false,
			scope:scope
		});
	},
	selectTreeNodeAfterReload:function() {
		var scope = this;
		var me = scope.me;
		var nodeIDToSelect = scope.nodeIDToSelect;
		var nodeToSelect = me.getStore().getNodeById(nodeIDToSelect);
		var treeSelectionModel = me.getSelectionModel();
		if (nodeToSelect ) {
			if (treeSelectionModel.isSelected(nodeToSelect)) {
				//if already selected deselect it first because
				//otherwise the select does not trigger the select handler (for refreshing the detail part)
				treeSelectionModel.deselect(nodeToSelect);
			}
			treeSelectionModel.select(nodeToSelect);
			if (!nodeToSelect.isVisible()) {
				me.expandPath(nodeToSelect.getPath());
			}
			me.fireEvent('itemclick',me,nodeToSelect);
		}
	}
});
